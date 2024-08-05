package com.mna.entities.projectile;

import com.mna.api.sound.SFX;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SkeletonAssassinBolo extends AbstractArrow implements GeoEntity {

    private final AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public SkeletonAssassinBolo(EntityType<? extends SkeletonAssassinBolo> p_i231584_1_, Level p_i231584_2_) {
        super(p_i231584_1_, p_i231584_2_);
    }

    public SkeletonAssassinBolo(Level world, LivingEntity entity) {
        super(EntityInit.SKELETON_ASSASSIN_BOLO.get(), world);
        Vec3 position = entity.m_20182_().add(0.0, 1.0, 0.0);
        this.m_6021_(position.x, position.y, position.z);
        this.m_5602_(entity);
        this.f_36705_ = AbstractArrow.Pickup.DISALLOWED;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        if (entity instanceof LivingEntity && entity.getType() != EntityInit.SKELETON_ASSASSIN.get() && entity.getType() != EntityInit.HULKING_ZOMBIE.get()) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(EffectInit.ENTANGLE.get(), 100));
            this.m_5496_(SFX.Entity.SkeletonAssassin.SHURIKEN_IMPACT, 1.0F, (float) (0.9 + Math.random() * 0.2));
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void tickDespawn() {
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    public boolean isInGround() {
        return this.f_36703_;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> PlayState.STOP));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }
}