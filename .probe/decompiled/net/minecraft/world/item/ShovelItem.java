package net.minecraft.world.item;

import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class ShovelItem extends DiggerItem {

    protected static final Map<Block, BlockState> FLATTENABLES = Maps.newHashMap(new Builder().put(Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.DIRT, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.PODZOL, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.COARSE_DIRT, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.MYCELIUM, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.defaultBlockState()).build());

    public ShovelItem(Tier tier0, float float1, float float2, Item.Properties itemProperties3) {
        super(float1, float2, tier0, BlockTags.MINEABLE_WITH_SHOVEL, itemProperties3);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        if (useOnContext0.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        } else {
            Player $$4 = useOnContext0.getPlayer();
            BlockState $$5 = (BlockState) FLATTENABLES.get($$3.m_60734_());
            BlockState $$6 = null;
            if ($$5 != null && $$1.getBlockState($$2.above()).m_60795_()) {
                $$1.playSound($$4, $$2, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                $$6 = $$5;
            } else if ($$3.m_60734_() instanceof CampfireBlock && (Boolean) $$3.m_61143_(CampfireBlock.LIT)) {
                if (!$$1.isClientSide()) {
                    $$1.m_5898_(null, 1009, $$2, 0);
                }
                CampfireBlock.dowse(useOnContext0.getPlayer(), $$1, $$2, $$3);
                $$6 = (BlockState) $$3.m_61124_(CampfireBlock.LIT, false);
            }
            if ($$6 != null) {
                if (!$$1.isClientSide) {
                    $$1.setBlock($$2, $$6, 11);
                    $$1.m_220407_(GameEvent.BLOCK_CHANGE, $$2, GameEvent.Context.of($$4, $$6));
                    if ($$4 != null) {
                        useOnContext0.getItemInHand().hurtAndBreak(1, $$4, p_43122_ -> p_43122_.m_21190_(useOnContext0.getHand()));
                    }
                }
                return InteractionResult.sidedSuccess($$1.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }
}