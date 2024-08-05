package vectorwing.farmersdelight.data;

import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.tag.ModTags;

public class EntityTags extends EntityTypeTagsProvider {

    public EntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "farmersdelight", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206424_(ModTags.DOG_FOOD_USERS).add(EntityType.WOLF);
        this.m_206424_(ModTags.HORSE_FEED_USERS).add(EntityType.HORSE, EntityType.SKELETON_HORSE, EntityType.ZOMBIE_HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.LLAMA);
        this.m_206424_(ModTags.HORSE_FEED_TEMPTED).add(EntityType.HORSE, EntityType.DONKEY, EntityType.MULE);
    }
}