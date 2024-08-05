package net.mehvahdjukaar.supplementaries.common.block.dispenser;

import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.mehvahdjukaar.supplementaries.common.entities.PearlMarker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

class EnderPearlBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    protected EnderPearlBehavior() {
        super(Items.ENDER_PEARL);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        Level level = source.getLevel();
        BlockPos pos = source.getPos();
        ThrownEnderpearl pearl = PearlMarker.getPearlToDispense(source, level, pos);
        Direction direction = (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING);
        pearl.m_6686_((double) direction.getStepX(), (double) ((float) direction.getStepY() + 0.1F), (double) direction.getStepZ(), this.getPower(), this.getUncertainty());
        level.m_7967_(pearl);
        stack.shrink(1);
        return InteractionResultHolder.success(stack);
    }

    @Override
    protected void playSound(BlockSource source, boolean success) {
        source.getLevel().m_46796_(1002, source.getPos(), 0);
    }

    protected float getUncertainty() {
        return 6.0F;
    }

    protected float getPower() {
        return 1.1F;
    }
}