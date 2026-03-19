package ge.nika.pptfontfixer.service;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class PptFontFixerService {

    public FixPptFontsResult fixPptFonts(InputStream inputStream) {
        try(HSLFSlideShow ppt = new HSLFSlideShow(inputStream);
            var byteArrayOutputStream = new ByteArrayOutputStream()) {

            var fixedTexts = new HashMap<String, String>();

            ppt.getSlides().stream()
                    .map(HSLFSlide::getTextParagraphs)
                    .flatMap(List::stream)
                    .flatMap(List::stream)
                    .flatMap(p -> p.getTextRuns().stream())
                    .forEach(textRun -> {
                        if (Objects.equals(textRun.getFontFamily(), "AcadNusx")) {
                            fixedTexts.put(textRun.getRawText(), toUnicodeGeorgian(textRun.getRawText()));
                            convertToUnicodeGeorgian(textRun);
                        }
                    });


            ppt.write(byteArrayOutputStream);
            var byteArray = byteArrayOutputStream.toByteArray();
            return new FixPptFontsResult(fixedTexts, byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void convertToUnicodeGeorgian(HSLFTextRun textRun) {
        textRun.setFontFamily("Sylfaen");
        textRun.setText(toUnicodeGeorgian(textRun.getRawText()));
    }

    private String toUnicodeGeorgian(String str) {
        StringBuilder result = new StringBuilder();

        for (char ch : str.toCharArray()) {
            result.append(convertAcadNusxToGeorgian(ch));
        }

        return result.toString();
    }

    private char convertAcadNusxToGeorgian(char ch) {
        switch (ch) {
            case 'a': return 'ა';
            case 'b': return 'ბ';
            case 'g': return 'გ';
            case 'd': return 'დ';
            case 'e': return 'ე';
            case 'v': return 'ვ';
            case 'z': return 'ზ';
            case 'T': return 'თ'; // capital T
            case 'i': return 'ი';
            case 'k': return 'კ';
            case 'l': return 'ლ';
            case 'm': return 'მ';
            case 'n': return 'ნ';
            case 'o': return 'ო';
            case 'p': return 'პ';
            case 'J': return 'ჟ';
            case 'r': return 'რ';
            case 's': return 'ს';
            case 't': return 'ტ'; // small t
            case 'u': return 'უ';
            case 'f': return 'ფ';
            case 'q': return 'ქ';
            case 'R': return 'ღ';
            case 'y': return 'ყ';
            case 'S': return 'შ';
            case 'C': return 'ჩ';
            case 'c': return 'ც';
            case 'Z': return 'ძ';
            case 'w': return 'წ';
            case 'W': return 'ჭ';
            case 'x': return 'ხ';
            case 'j': return 'ჯ';
            case 'h': return 'ჰ';
            default: return ch; // leave unchanged if not mapped
        }
    }
}
