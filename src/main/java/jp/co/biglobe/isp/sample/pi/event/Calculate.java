package jp.co.biglobe.isp.sample.pi.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
public class Calculate implements Serializable {

    private final int nrOfMessages;

    private final int nrOfElements;
}
