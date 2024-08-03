package net.minecraft.world.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class AxeItem extends DiggerItem {

    protected static final Map<Block, Block> STRIPPABLES = new Builder().put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD).put(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM).put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM).put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD).put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG).put(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK).build();

    protected AxeItem(Tier tier0, float float1, float float2, Item.Properties itemProperties3) {
        super(float1, float2, tier0, BlockTags.MINEABLE_WITH_AXE, itemProperties3);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        Player $$3 = useOnContext0.getPlayer();
        BlockState $$4 = $$1.getBlockState($$2);
        Optional<BlockState> $$5 = this.getStripped($$4);
        Optional<BlockState> $$6 = WeatheringCopper.getPrevious($$4);
        Optional<BlockState> $$7 = Optional.ofNullable((Block) ((BiMap) HoneycombItem.WAX_OFF_BY_BLOCK.get()).get($$4.m_60734_())).map(p_150694_ -> p_150694_.withPropertiesOf($$4));
        ItemStack $$8 = useOnContext0.getItemInHand();
        Optional<BlockState> $$9 = Optional.empty();
        if ($$5.isPresent()) {
            $$1.playSound($$3, $$2, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            $$9 = $$5;
        } else if ($$6.isPresent()) {
            $$1.playSound($$3, $$2, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            $$1.m_5898_($$3, 3005, $$2, 0);
            $$9 = $$6;
        } else if ($$7.isPresent()) {
            $$1.playSound($$3, $$2, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            $$1.m_5898_($$3, 3004, $$2, 0);
            $$9 = $$7;
        }
        if ($$9.isPresent()) {
            if ($$3 instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) $$3, $$2, $$8);
            }
            $$1.setBlock($$2, (BlockState) $$9.get(), 11);
            $$1.m_220407_(GameEvent.BLOCK_CHANGE, $$2, GameEvent.Context.of($$3, (BlockState) $$9.get()));
            if ($$3 != null) {
                $$8.hurtAndBreak(1, $$3, p_150686_ -> p_150686_.m_21190_(useOnContext0.getHand()));
            }
            return InteractionResult.sidedSuccess($$1.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    private Optional<BlockState> getStripped(BlockState blockState0) {
        return Optional.ofNullable((Block) STRIPPABLES.get(blockState0.m_60734_())).map(p_150689_ -> (BlockState) p_150689_.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, (Direction.Axis) blockState0.m_61143_(RotatedPillarBlock.AXIS)));
    }
}