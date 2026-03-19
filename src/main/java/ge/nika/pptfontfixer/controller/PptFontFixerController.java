package ge.nika.pptfontfixer.controller;

import ge.nika.pptfontfixer.service.PptFontFixerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class PptFontFixerController {

    private final PptFontFixerService pptFontFixerService;

    public PptFontFixerController(PptFontFixerService pptFontFixerService) {
        this.pptFontFixerService = pptFontFixerService;
    }

    @PostMapping(value = "/fix", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> fixPptFile(@RequestParam("file") MultipartFile file) throws IOException {

        try (InputStream is = file.getInputStream()) {

            var result = pptFontFixerService.fixPptFonts(is);
            byte[] byteArray = result.output();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + buildNewFileName(file.getOriginalFilename()))
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(byteArray);
        }
    }

    private String buildNewFileName(String originalFileName) {
        String fileName = StringUtils.getFilename(originalFileName);
        String fileExtension = StringUtils.getFilenameExtension(fileName);

        return fileName.replace("." + fileExtension, "") + "-fixed-" + System.currentTimeMillis() + "." + fileExtension;
    }
}
