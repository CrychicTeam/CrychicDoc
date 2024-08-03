package com.simibubi.create.content.trains.display;

import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.Arrays;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.apache.commons.lang3.mutable.MutableInt;

public class FlapDisplayLayout {

    List<FlapDisplaySection> sections;

    String layoutKey;

    public FlapDisplayLayout(int maxCharCount) {
        this.loadDefault(maxCharCount);
    }

    public void loadDefault(int maxCharCount) {
        this.configure("Default", Arrays.asList(new FlapDisplaySection((float) maxCharCount * 7.0F, "alphabet", false, false)));
    }

    public boolean isLayout(String key) {
        return this.layoutKey.equals(key);
    }

    public void configure(String layoutKey, List<FlapDisplaySection> sections) {
        this.layoutKey = layoutKey;
        this.sections = sections;
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Key", this.layoutKey);
        tag.put("Sections", NBTHelper.writeCompoundList(this.sections, FlapDisplaySection::write));
        return tag;
    }

    public void read(CompoundTag tag) {
        String prevKey = this.layoutKey;
        this.layoutKey = tag.getString("Key");
        ListTag sectionsTag = tag.getList("Sections", 10);
        if (!prevKey.equals(this.layoutKey)) {
            this.sections = NBTHelper.readCompoundList(sectionsTag, FlapDisplaySection::load);
        } else {
            MutableInt index = new MutableInt(0);
            NBTHelper.iterateCompoundList(sectionsTag, nbt -> ((FlapDisplaySection) this.sections.get(index.getAndIncrement())).update(nbt));
        }
    }

    public List<FlapDisplaySection> getSections() {
        return this.sections;
    }
}