package dev.ftb.mods.ftblibrary.snbt.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StringListValue extends BaseValue<List<String>> {

    StringListValue(SNBTConfig c, String n, List<String> def) {
        super(c, n, def);
        super.set(new ArrayList(def));
    }

    public void set(List<String> v) {
        this.get().clear();
        this.get().addAll(v);
    }

    @Override
    public void write(SNBTCompoundTag tag) {
        List<String> s = new ArrayList(this.comment);
        s.add("Default: [" + (String) this.defaultValue.stream().map(StringTag::m_129303_).collect(Collectors.joining(", ")) + "]");
        tag.comment(this.key, String.join("\n", s));
        ListTag stag = new ListTag();
        for (String s1 : this.get()) {
            stag.add(StringTag.valueOf(s1));
        }
        tag.m_128365_(this.key, stag);
    }

    @Override
    public void read(SNBTCompoundTag tag) {
        Tag stag = tag.m_128423_(this.key);
        if (stag instanceof ListTag && (((ListTag) stag).isEmpty() || ((ListTag) stag).getElementType() == 8)) {
            this.get().clear();
            for (int i = 0; i < ((ListTag) stag).size(); i++) {
                this.get().add(((ListTag) stag).getString(i));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void createClientConfig(ConfigGroup group) {
        group.addList(this.key, this.get(), new StringConfig(null), "").setCanEdit(this.enabled.getAsBoolean());
    }
}