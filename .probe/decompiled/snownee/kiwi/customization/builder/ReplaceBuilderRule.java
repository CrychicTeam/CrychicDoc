package snownee.kiwi.customization.builder;

import com.google.common.base.Predicates;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import snownee.kiwi.Kiwi;
import snownee.kiwi.customization.block.family.BlockFamily;

public record ReplaceBuilderRule(Map<BlockFamily, Object> families, BlockSpread spread) implements BuilderRule {

    @Override
    public Stream<Block> relatedBlocks() {
        return this.families.keySet().stream().flatMap(BlockFamily::blocks);
    }

    @Override
    public boolean matches(Player player, ItemStack itemStack, BlockState blockState) {
        return itemStack.is(blockState.m_60734_().asItem()) ? false : this.relatedBlocks().anyMatch(block -> itemStack.is(block.asItem()));
    }

    @Override
    public void apply(UseOnContext context, List<BlockPos> positions) {
        ItemStack itemStack = context.getItemInHand().copy();
        if (itemStack.getItem().asItem() instanceof BlockItem item) {
            BlockPlaceContext var12 = new BlockPlaceContext(context);
            Player player = context.getPlayer();
            Level level = context.getLevel();
            boolean success = false;
            for (BlockPos pos : positions) {
                BlockState oldBlock = level.getBlockState(pos);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
                var12 = BlockPlaceContext.at(var12, pos, context.getClickedFace());
                if (item.place(var12) == InteractionResult.FAIL) {
                    level.setBlock(pos, oldBlock, 4);
                } else {
                    success = true;
                }
                if (player != null) {
                    player.m_21008_(context.getHand(), itemStack);
                }
            }
            if (success && player != null) {
                BlockState blockState = item.getBlock().defaultBlockState();
                SoundType soundType = blockState.m_60827_();
                level.playSound(null, player.m_20183_(), soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
            }
        }
    }

    @Override
    public List<BlockPos> searchPositions(UseOnContext context) {
        List<BlockPos> list = List.of();
        try {
            list = this.spread.collect(context, Predicates.alwaysTrue());
        } catch (Exception var4) {
            Kiwi.LOGGER.error("Failed to collect positions", var4);
        }
        return list;
    }
}