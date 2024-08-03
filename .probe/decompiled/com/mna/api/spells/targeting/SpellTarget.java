package com.mna.api.spells.targeting;

import com.mna.api.spells.parts.SpellEffect;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class SpellTarget {

    private BlockPos BlockTarget;

    private Direction BlockFace;

    private Entity EntityTarget;

    private boolean offsetFace = true;

    public static SpellTarget NONE = new SpellTarget();

    public static SpellTarget NPC_CAST_ASSIST_NOT_IMPLEMENTED = new SpellTarget();

    private SpellTarget() {
        this.BlockTarget = null;
        this.EntityTarget = null;
        this.BlockFace = Direction.UP;
    }

    public SpellTarget(BlockPos pos, @Nullable Direction face) {
        this.BlockTarget = pos;
        this.EntityTarget = null;
        this.BlockFace = face;
    }

    public SpellTarget(Entity entity) {
        this.EntityTarget = entity;
        this.BlockTarget = null;
        this.BlockFace = Direction.UP;
    }

    public SpellTarget doNotOffsetFace() {
        this.offsetFace = false;
        return this;
    }

    public final boolean isBlock() {
        return this.BlockTarget != null;
    }

    public final boolean isEntity() {
        return this.EntityTarget != null;
    }

    public final boolean isLivingEntity() {
        return this.isEntity() && this.EntityTarget instanceof LivingEntity;
    }

    public final BlockPos getBlock() {
        return this.isBlock() ? this.BlockTarget : BlockPos.containing(this.EntityTarget.position());
    }

    public final Direction getBlockFace(@Nullable SpellEffect c) {
        return this.BlockFace != null ? this.BlockFace : (c != null ? c.defaultBlockFace() : Direction.UP);
    }

    public boolean offsetFace() {
        return this.offsetFace;
    }

    @Nullable
    public final Entity getEntity() {
        return this.EntityTarget;
    }

    public final Vec3 getPosition() {
        return this.isBlock() ? new Vec3((double) this.BlockTarget.m_123341_(), (double) this.BlockTarget.m_123342_(), (double) this.BlockTarget.m_123343_()).add(0.5, 0.5, 0.5) : this.EntityTarget.position();
    }

    @Nullable
    public final LivingEntity getLivingEntity() {
        return this.isLivingEntity() ? (LivingEntity) this.EntityTarget : null;
    }

    public final void overrideSpellTarget(Entity e) {
        this.EntityTarget = e;
        this.BlockTarget = null;
        this.BlockFace = Direction.UP;
    }

    public final void overrideSpellTarget(BlockPos pos, Direction face) {
        this.EntityTarget = null;
        this.BlockTarget = pos;
        this.BlockFace = face;
    }
}