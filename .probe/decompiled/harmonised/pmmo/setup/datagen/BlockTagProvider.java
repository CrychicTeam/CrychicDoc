package harmonised.pmmo.setup.datagen;

import harmonised.pmmo.util.Reference;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "pmmo", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.m_206424_(Reference.CASCADING_BREAKABLES).add(Blocks.SUGAR_CANE).add(Blocks.BAMBOO).add(Blocks.CACTUS).add(Blocks.CHORUS_PLANT).add(Blocks.POINTED_DRIPSTONE).add(Blocks.KELP).add(Blocks.KELP_PLANT).add(Blocks.TWISTING_VINES).add(Blocks.TWISTING_VINES_PLANT).add(Blocks.WEEPING_VINES).add(Blocks.WEEPING_VINES_PLANT).add(Blocks.CAVE_VINES).add(Blocks.CAVE_VINES_PLANT);
        this.m_206424_(Reference.CROPS).addTag(BlockTags.CROPS).add(Blocks.MELON).add(Blocks.PUMPKIN).add(Blocks.BAMBOO).add(Blocks.COCOA).add(Blocks.SUGAR_CANE).add(Blocks.SWEET_BERRY_BUSH).add(Blocks.CACTUS).add(Blocks.RED_MUSHROOM).add(Blocks.BROWN_MUSHROOM).add(Blocks.KELP).add(Blocks.KELP_PLANT).add(Blocks.NETHER_WART).add(Blocks.SEA_PICKLE).add(Blocks.CHORUS_PLANT).add(Blocks.TWISTING_VINES).add(Blocks.TWISTING_VINES_PLANT).add(Blocks.WEEPING_VINES).add(Blocks.WEEPING_VINES_PLANT).add(Blocks.CAVE_VINES).add(Blocks.CAVE_VINES_PLANT);
    }
}