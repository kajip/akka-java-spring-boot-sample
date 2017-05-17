package jp.co.biglobe.isp.sample.pi.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
public class Result implements Serializable {
    private final double value;
}
