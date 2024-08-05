package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityDreadLichSkull extends AbstractArrow {

    public EntityDreadLichSkull(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
        this.m_36781_(6.0);
    }

    public EntityDreadLichSkull(EntityType<? extends AbstractArrow> type, Level worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.m_6034_(x, y, z);
        this.m_36781_(6.0);
    }

    public EntityDreadLichSkull(EntityType<? extends AbstractArrow> type, Level worldIn, LivingEntity shooter, double x, double y, double z) {
        super(type, shooter, worldIn);
        this.m_36781_(6.0);
    }

    public EntityDreadLichSkull(EntityType<? extends AbstractArrow> type, Level worldIn, LivingEntity shooter, double dmg) {
        super(type, shooter, worldIn);
        this.m_36781_(dmg);
    }

    public EntityDreadLichSkull(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.DREAD_LICH_SKULL.get(), worldIn);
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void tick() {
        float sqrt = Mth.sqrt((float) (this.m_20184_().x * this.m_20184_().x + this.m_20184_().z * this.m_20184_().z));
        boolean flag = true;
        Entity shootingEntity = this.m_19749_();
        if (shootingEntity != null && shootingEntity instanceof Mob && ((Mob) shootingEntity).getTarget() != null) {
            LivingEntity target = ((Mob) shootingEntity).getTarget();
            double minusX = target.m_20185_() - this.m_20185_();
            double minusY = target.m_20186_() - this.m_20186_();
            double minusZ = target.m_20189_() - this.m_20189_();
            double speed = 0.15;
            this.m_20256_(this.m_20184_().add(minusX * speed * 0.1, minusY * speed * 0.1, minusZ * speed * 0.1));
        }
        if (shootingEntity instanceof Player) {
            LivingEntity target = ((Player) shootingEntity).m_21232_();
            if (target == null || !target.isAlive()) {
                double d0 = 10.0;
                List<Entity> list = this.m_9236_().getEntities(shootingEntity, new AABB(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_20185_() + 1.0, this.m_20186_() + 1.0, this.m_20189_() + 1.0).inflate(d0, 10.0, d0), EntitySelector.ENTITY_STILL_ALIVE);
                LivingEntity closest = null;
                if (!list.isEmpty()) {
                    for (Entity e : list) {
                        if (e instanceof LivingEntity && !e.getUUID().equals(shootingEntity.getUUID()) && e instanceof Enemy && (closest == null || closest.m_20270_(shootingEntity) > e.distanceTo(shootingEntity))) {
                            closest = (LivingEntity) e;
                        }
                    }
                }
                target = closest;
            }
            if (target != null && target.isAlive()) {
                double minusX = target.m_20185_() - this.m_20185_();
                double minusY = target.m_20186_() + (double) target.m_20192_() - this.m_20186_();
                double minusZ = target.m_20189_() - this.m_20189_();
                double speed = 0.25 * Math.min((double) this.m_20270_(target), 10.0) / 10.0;
                this.m_20256_(this.m_20184_().add((Math.signum(minusX) * 0.5 - this.m_20184_().x) * 0.1F, (Math.signum(minusY) * 0.5 - this.m_20184_().y) * 0.1F, (Math.signum(minusZ) * 0.5 - this.m_20184_().z) * 0.1F));
                this.m_146922_((float) (Mth.atan2(this.m_20184_().x, this.m_20184_().z) * (180.0 / Math.PI)));
                this.m_146926_((float) (Mth.atan2(this.m_20184_().y, (double) sqrt) * (180.0 / Math.PI)));
                flag = false;
            }
        }
        if ((sqrt < 0.1F || this.f_19862_ || this.f_19863_ || this.f_36703_) && this.f_19797_ > 5 && flag) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        double d0 = 0.0;
        double d1 = 0.01;
        double d2 = 0.0;
        double x = this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_();
        double y = this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()) - (double) this.m_20206_();
        double z = this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_();
        float f = (this.m_20205_() + this.m_20206_() + this.m_20205_()) * 0.333F + 0.5F;
        if (this.particleDistSq(x, y, z) < (double) (f * f)) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, x, y + 0.5, z, d0, d1, d2);
        }
        super.tick();
    }

    public double particleDistSq(double toX, double toY, double toZ) {
        double d0 = this.m_20185_() - toX;
        double d1 = this.m_20186_() - toY;
        double d2 = this.m_20189_() - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    @Override
    public void playSound(@NotNull SoundEvent soundIn, float volume, float pitch) {
        if (!this.m_20067_() && soundIn != SoundEvents.ARROW_HIT && soundIn != SoundEvents.ARROW_HIT_PLAYER) {
            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), soundIn, this.m_5720_(), volume, pitch);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult raytraceResultIn) {
        if (raytraceResultIn.getType() == HitResult.Type.ENTITY) {
            Entity entity = raytraceResultIn.getEntity();
            Entity shootingEntity = this.m_19749_();
            if (entity != null && shootingEntity != null && entity.isAlliedTo(shootingEntity)) {
                return;
            }
        }
        super.onHitEntity(raytraceResultIn);
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity living) {
        super.doPostHurtEffects(living);
        Entity shootingEntity = this.m_19749_();
        if (living != null && (shootingEntity == null || !living.m_7306_(shootingEntity)) && living instanceof Player) {
            this.damageShield((Player) living, (float) this.m_36789_());
        }
    }

    protected void damageShield(Player player, float damage) {
        if (damage >= 3.0F && player.m_21211_().getItem().canPerformAction(player.m_21211_(), ToolActions.SHIELD_BLOCK)) {
            ItemStack copyBeforeUse = player.m_21211_().copy();
            int i = 1 + Mth.floor(damage);
            player.m_21211_().hurtAndBreak(i, player, playerSheild -> playerSheild.m_21190_(playerSheild.m_7655_()));
            if (player.m_21211_().isEmpty()) {
                InteractionHand Hand = player.m_7655_();
                ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, Hand);
                if (Hand == InteractionHand.MAIN_HAND) {
                    this.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.m_8061_(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                player.m_5810_();
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.m_9236_().random.nextFloat() * 0.4F);
            }
        }
    }

    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @NotNull
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}