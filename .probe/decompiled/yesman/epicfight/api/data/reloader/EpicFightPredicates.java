package yesman.epicfight.api.data.reloader;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;

public abstract class EpicFightPredicates<T> {

    public abstract boolean test(T var1);

    public static class HasTag extends EpicFightPredicates<Entity> {

        private final Set<String> allowedTags = Sets.newHashSet();

        public HasTag(ListTag allowedTags) {
            allowedTags.stream().map(Tag::m_7916_).forEach(this.allowedTags::add);
        }

        public boolean test(Entity object) {
            for (String tag : this.allowedTags) {
                if (object.getTags().contains(tag)) {
                    return true;
                }
            }
            return false;
        }
    }
}