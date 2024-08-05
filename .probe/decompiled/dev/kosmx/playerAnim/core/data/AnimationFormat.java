package dev.kosmx.playerAnim.core.data;

public enum AnimationFormat {

    JSON_EMOTECRAFT("json"),
    JSON_MC_ANIM("json"),
    QUARK("emote"),
    BINARY("emotecraft"),
    SERVER(null),
    UNKNOWN(null);

    private final String extension;

    private AnimationFormat(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return this.extension;
    }
}