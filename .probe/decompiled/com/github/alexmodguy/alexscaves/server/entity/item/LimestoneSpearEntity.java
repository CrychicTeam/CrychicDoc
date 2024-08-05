package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class LimestoneSpearEntity extends AbstractArrow {

    private boolean dealtDamage;

    public LimestoneSpearEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public LimestoneSpearEntity(Level level, LivingEntity shooter, ItemStack itemStack) {
        super(ACEntityRegistry.LIMESTONE_SPEAR.get(), shooter, level);
    }

    public LimestoneSpearEntity(Level level, double x, double y, double z) {
        super(ACEntityRegistry.LIMESTONE_SPEAR.get(), x, y, z, level);
    }

    public LimestoneSpearEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.LIMESTONE_SPEAR.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ACItemRegistry.LIMESTONE_SPEAR.get());
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 vec3, Vec3 vec31) {
        return this.dealtDamage ? null : super.findHitEntity(vec3, vec31);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        float f = 4.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.getPickupItem(), livingentity.getMobType());
        }
        Entity entity1 = this.m_19749_();
        DamageSource damagesource = this.m_269291_().trident(this, (Entity) (entity1 == null ? this : entity1));
        this.dealtDamage = true;
        SoundEvent soundevent = ACSoundRegistry.LIMESTONE_SPEAR_HIT.get();
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingentity1) {
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
                }
                this.m_7761_(livingentity1);
            }
        }
        this.m_20256_(this.m_20184_().multiply(-0.01, -0.1, -0.01));
        float f1 = 1.0F;
        this.m_5496_(soundevent, f1, 1.0F);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ACSoundRegistry.LIMESTONE_SPEAR_HIT.get();
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}