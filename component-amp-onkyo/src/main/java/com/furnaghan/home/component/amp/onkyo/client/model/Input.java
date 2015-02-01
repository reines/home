package com.furnaghan.home.component.amp.onkyo.client.model;

public enum Input {
    VCR_DVR(0x00),
    CBL_SAT(0x01),
    GAME(0x02),
    AUX(0x03),
    AUX2(0x04),
    PC(0x05),
    VIDEO7(0x06),

    BD_DVD(0x10),

    TAPE(0x20),
    TAPE2(0x21),
    PHONO(0x22),
    CD(0x23),
    FM(0x24),
    AM(0x25),
    TUNER(0x26),
    MUSIC_SERVER(0x27),
    INTERNET_RADIO(0x28),
    USB(0x29),

    MULTI_CH(0x30),
    XM_1(0x31),
    SIRIUS_1(0x32);

    private final int code;

    private Input(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
