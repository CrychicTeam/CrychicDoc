package org.violetmoon.quark.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class SimpleHarvestEvent extends Event {

    public final BlockState blockState;

    public final BlockPos pos;

    public final Level level;

    @Nullable
    public final InteractionHand hand;

    @Nullable
    public final Entity entity;

    private BlockPos newTarget;

    private SimpleHarvestEvent.ActionType action;

    public SimpleHarvestEvent(BlockState blockState, BlockPos pos, Level level, @Nullable InteractionHand hand, @Nullable Entity entity, SimpleHarvestEvent.ActionType originalActionType) {
        this.blockState = blockState;
        this.pos = pos;
        this.hand = hand;
        this.level = level;
        this.entity = entity;
        this.newTarget = pos;
        this.action = originalActionType;
    }

    public void setTargetPos(BlockPos pos) {
        this.newTarget = pos;
    }

    public void setCanceled(boolean cancel) {
        if (cancel) {
            this.action = SimpleHarvestEvent.ActionType.NONE;
        }
        super.setCanceled(cancel);
    }

    public SimpleHarvestEvent.ActionType getAction() {
        return this.action;
    }

    public void setAction(SimpleHarvestEvent.ActionType action) {
        this.action = action;
    }

    public BlockPos getTargetPos() {
        return this.newTarget;
    }

    public static enum ActionType {

        NONE, CLICK, HARVEST
    }
}