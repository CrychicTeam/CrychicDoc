package com.mna.api.spells.targeting;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class SpellSource {

    private LivingEntity caster;

    private Vec3 origin;

    private Vec3 forward;

    private InteractionHand hand;

    public SpellSource(LivingEntity caster, InteractionHand hand) {
        this.caster = caster;
        this.origin = caster.m_20182_().add(0.0, (double) caster.m_20192_(), 0.0);
        this.forward = caster.m_20154_();
        this.hand = hand;
    }

    public SpellSource(LivingEntity caster, InteractionHand hand, Vec3 origin, Vec3 forward) {
        this.caster = caster;
        this.hand = hand;
        this.origin = origin;
        this.forward = forward;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public Vec3 getOrigin() {
        return this.origin;
    }

    public Vec3 getForward() {
        return this.forward;
    }

    @Nullable
    public LivingEntity getCaster() {
        return this.caster;
    }

    @Nullable
    public Player getPlayer() {
        return this.isPlayerCaster() ? (Player) this.caster : null;
    }

    public boolean hasCasterReference() {
        return this.caster != null;
    }

    public boolean isPlayerCaster() {
        return this.caster != null && this.caster instanceof Player;
    }

    public AABB getBoundingBox() {
        return this.caster != null ? this.caster.m_20191_() : new AABB(BlockPos.containing(this.origin));
    }
}