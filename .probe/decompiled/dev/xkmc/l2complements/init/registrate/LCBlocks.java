package dev.xkmc.l2complements.init.registrate;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.materials.LCMats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class LCBlocks {

    public static final BlockEntry<AnvilBlock> ETERNAL_ANVIL = ((BlockBuilder) L2Complements.REGISTRATE.block("eternal_anvil", p -> new AnvilBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL))).blockstate((ctx, pvd) -> pvd.horizontalBlock((Block) ctx.getEntry(), pvd.models().getExistingFile(new ResourceLocation("l2complements", "eternal_anvil")), 90)).tag(new TagKey[] { BlockTags.ANVIL, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_STONE_TOOL }).item().tag(new TagKey[] { ItemTags.ANVIL }).build()).register();

    public static final BlockEntry<Block>[] GEN_BLOCK = L2Complements.MATS.genBlockMats(LCMats.values());

    public static void register() {
    }
}