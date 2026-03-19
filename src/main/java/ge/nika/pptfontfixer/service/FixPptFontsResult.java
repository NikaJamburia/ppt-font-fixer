package ge.nika.pptfontfixer.service;

import java.util.Map;

public record FixPptFontsResult(
        Map<String, String> fixedTexts,
        byte[] output
) {
}
