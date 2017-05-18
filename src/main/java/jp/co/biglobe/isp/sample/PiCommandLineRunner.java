package jp.co.biglobe.isp.sample;

import jp.co.biglobe.isp.sample.pi.service.PiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PiCommandLineRunner implements CommandLineRunner {

    private final PiService pi;

    @Autowired
    public PiCommandLineRunner(PiService pi) {
        this.pi = pi;
    }

    @Override
    public void run(String... args) throws Exception {
        pi.calculate();
    }
}
