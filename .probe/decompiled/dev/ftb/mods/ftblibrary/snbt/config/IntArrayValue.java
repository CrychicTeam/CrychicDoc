package dev.ftb.mods.ftblibrary.snbt.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IntArrayValue extends BaseValue<int[]> {

    IntArrayValue(SNBTConfig c, String n, int[] def) {
        super(c, n, def);
        this.set(Arrays.copyOf(def, def.length));
    }

    @Override
    public void write(SNBTCompoundTag tag) {
        List<String> s = new ArrayList(this.comment);
        s.add("Default: " + Arrays.toString(this.defaultValue));
        tag.comment(this.key, String.join("\n", s));
        tag.m_128385_(this.key, this.get());
    }

    @Override
    public void read(SNBTCompoundTag tag) {
        this.set(tag.m_128465_(this.key));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void createClientConfig(ConfigGroup group) {
    }
}