package net.mehvahdjukaar.supplementaries.common.items;

import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AbstractRopeKnotBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RopeBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class RopeItem extends BlockItem {

    public RopeItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Player player = context.m_43723_();
        if (player == null || Utils.mayPerformBlockAction(player, context.getClickedPos(), context.m_43722_())) {
            Level world = context.m_43725_();
            BlockPos pos = context.getClickedPos().relative(context.m_43719_().getOpposite());
            BlockState state = world.getBlockState(pos);
            ModBlockProperties.PostType type = ModBlockProperties.PostType.get(state);
            if (type != null) {
                ItemStack stack = context.m_43722_();
                if (AbstractRopeKnotBlock.convertToRopeKnot(type, state, world, pos) == null) {
                    return InteractionResult.FAIL;
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
                }
                SoundType soundtype = ((RopeBlock) ModRegistry.ROPE.get()).m_49966_().m_60827_();
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                if (player == null || !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return super.place(context);
    }
}