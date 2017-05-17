package jp.co.biglobe.isp.sample.pi.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
public class Work implements Serializable {
    private final int start;
    private final int nrOfElements;
}
