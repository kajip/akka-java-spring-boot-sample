package jp.co.biglobe.isp.sample.pi.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Duration;

@RequiredArgsConstructor
@Getter
public class PiApproximation implements Serializable {
    private final double pi;
    private final Duration duration;
}
