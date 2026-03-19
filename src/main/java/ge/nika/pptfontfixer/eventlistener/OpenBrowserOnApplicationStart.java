package ge.nika.pptfontfixer.eventlistener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OpenBrowserOnApplicationStart implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            Runtime.getRuntime()
                    .exec("explorer \"http://localhost:8080/\"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
