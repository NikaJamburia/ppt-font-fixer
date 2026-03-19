package ge.nika.pptfontfixer.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.Instant;

public class PptFontFixerServiceTest {

    private final PptFontFixerService subject = new PptFontFixerService();

    @Test
    void idk() throws IOException {
        File file = new File("src/test/resources/broken_ppt.ppt");
        var result = subject.fixPptFonts(new FileInputStream(file));

        result.fixedTexts().forEach((k, v) -> System.out.println(k + " -> " + v));
        Assertions.assertFalse(result.fixedTexts().isEmpty());

        var fos = new FileOutputStream("src/test/resources/fixed_ppt" + System.currentTimeMillis() + ".ppt");
        fos.write(result.output());
        fos.close();
    }
}
