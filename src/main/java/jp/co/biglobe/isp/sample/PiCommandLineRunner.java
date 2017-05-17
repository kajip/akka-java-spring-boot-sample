package jp.co.biglobe.isp.sample;

import jp.co.biglobe.isp.sample.pi.service.PiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PiCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        PiService pi = new PiService();

        pi.calculate(4, 10000, 10000);
    }
}
