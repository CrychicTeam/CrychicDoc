package com.mna.items.food;

import com.mna.api.items.TieredItem;
import com.mna.blocks.BlockInit;
import com.mna.blocks.decoration.ClayMugBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemClayMug extends TieredItem {

    public ItemClayMug() {
        super(new Item.Properties());
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public int getEnchantmentValue(ItemStack stack) {
        return 0;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        HitResult raytraceresult = m_41435_(world, player, ClipContext.Fluid.NONE);
        if (raytraceresult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockraytraceresult = (BlockHitResult) raytraceresult;
            BlockPos blockpos = blockraytraceresult.getBlockPos();
            Direction direction = blockraytraceresult.getDirection();
            if (!world.mayInteract(player, blockpos) || !player.mayUseItemAt(blockpos.relative(direction), direction, itemstack)) {
                return InteractionResultHolder.fail(itemstack);
            }
            BlockState state = world.getBlockState(blockpos);
            if (state.m_60783_(world, blockpos, Direction.UP) && world.m_46859_(blockpos.above())) {
                world.playSound(player, (double) blockpos.m_123341_(), (double) blockpos.m_123342_(), (double) blockpos.m_123343_(), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.setBlock(blockpos.above(), (BlockState) ((BlockState) BlockInit.CLAY_MUG.get().defaultBlockState().m_61124_(ClayMugBlock.HAS_LIQUID, false)).m_61124_(HorizontalDirectionalBlock.FACING, Direction.fromYRot((double) player.m_146908_()).getOpposite()), 3);
                itemstack.shrink(1);
                return InteractionResultHolder.success(itemstack);
            }
        }
        return InteractionResultHolder.pass(itemstack);
    }
}