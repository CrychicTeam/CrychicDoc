package team.lodestar.lodestone.helpers;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;

public class NBTHelper {

    public static CompoundTag filterTag(CompoundTag orig, NBTHelper.TagFilter filter) {
        if (filter.filters.isEmpty()) {
            return orig;
        } else {
            CompoundTag copy = orig.copy();
            removeTags(copy, filter);
            return copy;
        }
    }

    public static CompoundTag removeTags(CompoundTag tag, NBTHelper.TagFilter filter) {
        CompoundTag newTag = new CompoundTag();
        for (String i : filter.filters) {
            if (!tag.contains(i)) {
                for (String key : tag.getAllKeys()) {
                    if (tag.get(key) instanceof CompoundTag ctag) {
                        removeTags(ctag, filter);
                    }
                }
            } else if (filter.isWhitelist) {
                newTag.put(i, newTag);
            } else {
                tag.remove(i);
            }
        }
        if (filter.isWhitelist) {
            tag = newTag;
        }
        return tag;
    }

    public static NBTHelper.TagFilter create(String... filters) {
        return new NBTHelper.TagFilter(filters);
    }

    public static class TagFilter {

        public final ArrayList<String> filters = new ArrayList();

        public boolean isWhitelist;

        public TagFilter(String... filters) {
            this.filters.addAll(List.of(filters));
        }

        public NBTHelper.TagFilter setWhitelist() {
            this.isWhitelist = true;
            return this;
        }
    }
}