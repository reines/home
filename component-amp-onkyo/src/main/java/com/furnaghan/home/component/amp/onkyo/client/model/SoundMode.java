package com.furnaghan.home.component.amp.onkyo.client.model;

public enum SoundMode {
    STEREO(0x00),
    DIRECT(0x01),
    SURROUND(0x02),
    FILM(0x03),
    THX(0x04),
    ACTION(0x05),
    MUSICAL(0x06),
    MONO_MOVIE(0x07),
    ORCHESTRA(0x08),
    UNPLUGGED(0x09),
    STUDIO_MIX(0x0A),
    TV_LOGIC(0x0B),
    ALL_CH_STEREO(0x0C),
    THEATER_DIMENSIONAL(0x0D),
    ENHANCED_7_ENHANCE(0x0E),
    MONO(0x0F),
    TEST_TONE(0x10),
    PURE_AUDIO(0x11),
    MULTIPLEX(0x12),
    FULL_MONO(0x13),
    DOLBY_VIRTUAL(0x14),

    STRAIGHT_DECODE(0x40),
    DOLBY_EX_DTS_ES(0x41),
    THX_CINEMA(0x42),
    THX_SURROUND_EX(0x43),

    U2_S2_CINEMA_CINEMA_2(0x50),
    MUSIC_MODE(0x51),
    GAMES_MODE(0x52),

    PLIIX_MOVIE(0x80),
    PLIIX_MUSIC(0x81),
    NEO6_CINEMA(0x82),
    NEO6_MUSIC(0x83),
    PLIIX_THX_CINEMA(0x84),
    NEO6_THX_CINEMA(0x85),
    PLIIX_GAME(0x86),
    NEURAL_SURROUND_3(0x87),
    NEURAL_THX(0x88),
    PLII_THX_GAMES(0x89),
    NEO6_THX_GAMES(0x8A);

    private final int code;

    private SoundMode(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
