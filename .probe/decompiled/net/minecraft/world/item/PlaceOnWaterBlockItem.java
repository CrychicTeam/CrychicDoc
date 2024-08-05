package net.minecraft.world.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

public class PlaceOnWaterBlockItem extends BlockItem {

    public PlaceOnWaterBlockItem(Block block0, Item.Properties itemProperties1) {
        super(block0, itemProperties1);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        BlockHitResult $$3 = m_41435_(level0, player1, ClipContext.Fluid.SOURCE_ONLY);
        BlockHitResult $$4 = $$3.withPosition($$3.getBlockPos().above());
        InteractionResult $$5 = super.useOn(new UseOnContext(player1, interactionHand2, $$4));
        return new InteractionResultHolder<>($$5, player1.m_21120_(interactionHand2));
    }
}