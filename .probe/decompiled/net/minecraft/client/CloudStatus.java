package net.minecraft.client;

import net.minecraft.util.OptionEnum;

public enum CloudStatus implements OptionEnum {

    OFF(0, "options.off"), FAST(1, "options.clouds.fast"), FANCY(2, "options.clouds.fancy");

    private final int id;

    private final String key;

    private CloudStatus(int p_231334_, String p_231335_) {
        this.id = p_231334_;
        this.key = p_231335_;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}