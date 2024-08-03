package dev.latvian.mods.rhino.mod.util;

import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class OrderedCompoundTag extends CompoundTag {

    public final Map<String, Tag> tagMap;

    public OrderedCompoundTag(Map<String, Tag> map) {
        super(map);
        this.tagMap = map;
    }

    public OrderedCompoundTag() {
        this(new LinkedHashMap());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        for (Entry<String, Tag> entry : this.tagMap.entrySet()) {
            Tag tag = (Tag) entry.getValue();
            dataOutput.writeByte(tag.getId());
            if (tag.getId() != 0) {
                dataOutput.writeUTF((String) entry.getKey());
                tag.write(dataOutput);
            }
        }
        dataOutput.writeByte(0);
    }
}