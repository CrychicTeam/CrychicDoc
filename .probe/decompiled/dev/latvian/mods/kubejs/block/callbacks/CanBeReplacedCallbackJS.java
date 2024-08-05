package dev.latvian.mods.kubejs.block.callbacks;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CanBeReplacedCallbackJS {

    private final BlockPlaceContext context;

    public CanBeReplacedCallbackJS(BlockPlaceContext blockPlaceContext, BlockState state) {
        this.context = blockPlaceContext;
    }

    public BlockPos getClickedPos() {
        return this.context.getClickedPos();
    }

    public BlockContainerJS getClickedBlock() {
        return new BlockContainerJS(this.getLevel(), this.getClickedPos());
    }

    public Direction getNearestLookingDirection() {
        return this.context.getNearestLookingDirection();
    }

    public Direction getNearestLookingVerticalDirection() {
        return this.context.getNearestLookingVerticalDirection();
    }

    public Direction[] getNearestLookingDirections() {
        return this.context.getNearestLookingDirections();
    }

    public Direction getClickedFace() {
        return this.context.m_43719_();
    }

    public Vec3 getClickLocation() {
        return this.context.m_43720_();
    }

    public boolean isInside() {
        return this.context.m_43721_();
    }

    public ItemStack getItem() {
        return ItemStackJS.of(this.context.m_43722_());
    }

    @Nullable
    public Player getPlayer() {
        return this.context.m_43723_();
    }

    public InteractionHand getHand() {
        return this.context.m_43724_();
    }

    public Level getLevel() {
        return this.context.m_43725_();
    }

    public Direction getHorizontalDirection() {
        return this.context.m_8125_();
    }

    public boolean isSecondaryUseActive() {
        return this.context.m_7078_();
    }

    public float getRotation() {
        return this.context.m_7074_();
    }

    public FluidState getFluidStateAtClickedPos() {
        return this.context.m_43725_().getFluidState(this.context.getClickedPos());
    }

    public boolean isClickedPosIn(Fluid fluid) {
        return this.getFluidStateAtClickedPos().is(fluid);
    }

    public boolean canBeReplaced() {
        return this.getLevel().getBlockState(this.getClickedPos()).m_247087_();
    }
}