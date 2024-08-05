package dev.ftb.mods.ftblibrary.snbt.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StringValue extends BaseValue<String> {

    private Pattern pattern;

    StringValue(SNBTConfig c, String n, String def) {
        super(c, n, def);
    }

    public StringValue pattern(Pattern p) {
        this.pattern = p;
        return this;
    }

    public void set(String v) {
        super.set(v);
        if (this.pattern != null && !this.pattern.matcher((CharSequence) this.get()).find()) {
            super.set(this.defaultValue);
        }
    }

    @Override
    public void write(SNBTCompoundTag tag) {
        List<String> s = new ArrayList(this.comment);
        s.add("Default: \"" + this.defaultValue + "\"");
        tag.comment(this.key, String.join("\n", s));
        tag.m_128359_(this.key, this.get());
    }

    @Override
    public void read(SNBTCompoundTag tag) {
        this.set(tag.m_128461_(this.key));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void createClientConfig(ConfigGroup group) {
        group.addString(this.key, this.get(), this::set, this.defaultValue, this.pattern).setCanEdit(this.enabled.getAsBoolean());
    }
}