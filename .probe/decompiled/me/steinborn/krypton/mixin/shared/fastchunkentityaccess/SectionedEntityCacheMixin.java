package me.steinborn.krypton.mixin.shared.fastchunkentityaccess;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.steinborn.krypton.mod.shared.WorldEntityByChunkAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ EntitySectionStorage.class })
public abstract class SectionedEntityCacheMixin implements WorldEntityByChunkAccess {

    @Shadow
    @Final
    private Long2ObjectMap<EntitySection<Entity>> sections;

    @Override
    public Collection<Entity> getEntitiesInChunk(int chunkX, int chunkZ) {
        LongSortedSet set = this.getChunkSections(chunkX, chunkZ);
        if (set.isEmpty()) {
            return List.of();
        } else {
            List<Entity> entities = new ArrayList();
            LongIterator sectionsIterator = set.iterator();
            while (sectionsIterator.hasNext()) {
                long key = sectionsIterator.nextLong();
                EntitySection<Entity> value = (EntitySection<Entity>) this.sections.get(key);
                if (value != null && value.getStatus().isAccessible()) {
                    entities.addAll(((EntityTrackingSectionAccessor) value).getCollection());
                }
            }
            return entities;
        }
    }

    @Shadow
    protected abstract LongSortedSet getChunkSections(int var1, int var2);
}