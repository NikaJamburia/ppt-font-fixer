package ge.nika.pptfontfixer.controller;

import ge.nika.pptfontfixer.service.PptFontFixerService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainController {

    @FXML private Label fileNameLabel;
    @FXML private Label statusLabel;
    @FXML private ProgressIndicator progressIndicator;

    private final PptFontFixerService service = new PptFontFixerService();
    private File selectedFile;

    @FXML
    public void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("აირჩიე PPT ფაილი");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PowerPoint ფაილები", "*.ppt", "*.pptx")
        );

        Stage stage = (Stage) fileNameLabel.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedFile = file;
            fileNameLabel.setText(file.getName());
            statusLabel.setText("");
        }
    }

    @FXML
    public void startProcessing() {
        if (selectedFile == null) {
            statusLabel.setText("ჯერ აირჩიე ფაილი");
            statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
            return;
        }

        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("აირჩიე საქაღალდე შედეგის შესანახად");
        dirChooser.setInitialDirectory(selectedFile.getParentFile());

        Stage stage = (Stage) statusLabel.getScene().getWindow();
        File outputDir = dirChooser.showDialog(stage);

        if (outputDir == null) {
            return;
        }

        progressIndicator.setVisible(true);
        statusLabel.setText("მიმდინარეობს დამუშავება...");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

        Thread thread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                var result = service.fixPptFonts(fis);
                String outputName = buildOutputFileName(selectedFile.getName());
                File outputFile = new File(outputDir, outputName);

                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    fos.write(result.output());
                }

                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    statusLabel.setText("შესრულდა: " + outputFile.getName());
                    statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: green;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    statusLabel.setText("შეცდომა: " + e.getMessage());
                    statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
                });
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private String buildOutputFileName(String originalName) {
        int dotIndex = originalName.lastIndexOf('.');
        String name = originalName.substring(0, dotIndex);
        String ext = originalName.substring(dotIndex);
        return name + "-fixed-" + System.currentTimeMillis() + ext;
    }
}
