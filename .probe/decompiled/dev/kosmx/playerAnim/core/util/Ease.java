package dev.kosmx.playerAnim.core.util;

public enum Ease {

    LINEAR(0, f -> f),
    CONSTANT(1, f -> 0.0F),
    INSINE(6, Easing::inSine),
    OUTSINE(7, Easing::outSine),
    INOUTSINE(8, Easing::inOutSine),
    INCUBIC(9, Easing::inCubic),
    OUTCUBIC(10, Easing::outCubic),
    INOUTCUBIC(11, Easing::inOutCubic),
    INQUAD(12, Easing::inQuad),
    OUTQUAD(13, Easing::outQuad),
    INOUTQUAD(14, Easing::inOutQuad),
    INQUART(15, Easing::inQuart),
    OUTQUART(16, Easing::outQuart),
    INOUTQUART(17, Easing::inOutQuart),
    INQUINT(18, Easing::inQuint),
    OUTQUINT(19, Easing::outQuint),
    INOUTQUINT(20, Easing::inOutQuint),
    INEXPO(21, Easing::inExpo),
    OUTEXPO(22, Easing::outExpo),
    INOUTEXPO(23, Easing::inOutExpo),
    INCIRC(24, Easing::inCirc),
    OUTCIRC(25, Easing::outCirc),
    INOUTCIRC(26, Easing::inOutCirc),
    INBACK(27, Easing::inBack),
    OUTBACK(28, Easing::outBack),
    INOUTBACK(29, Easing::inOutBack),
    INELASTIC(30, Easing::inElastic),
    OUTELASTIC(31, Easing::outElastic),
    INOUTELASTIC(32, Easing::inOutElastic),
    INBOUNCE(33, Easing::inBounce),
    OUTBOUNCE(34, Easing::outBack),
    INOUTBOUNCE(35, Easing::inOutBounce);

    final byte id;

    private final Ease._F impl;

    private Ease(byte id, Ease._F impl) {
        this.id = id;
        this.impl = impl;
    }

    private Ease(int id, Ease._F impl) {
        this((byte) id, impl);
    }

    public byte getId() {
        return this.id;
    }

    public float invoke(float f) {
        return this.impl.invoke(f);
    }

    public static Ease getEase(byte b) {
        for (Ease ease : values()) {
            if (ease.id == b) {
                return ease;
            }
        }
        return LINEAR;
    }

    private interface _F {

        float invoke(float var1);
    }
}