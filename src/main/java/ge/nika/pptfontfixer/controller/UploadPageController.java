package ge.nika.pptfontfixer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UploadPageController {

    @GetMapping("/")
    public String uploadPage() {
        return "index";
    }
}
