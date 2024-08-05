package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class FloaterEntity extends Entity {

    public int timeOutOfWater = 0;

    public FloaterEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public FloaterEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.FLOATER.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_20068_()) {
            this.m_20256_(this.m_20184_().add(0.0, -0.04, 0.0));
        }
        if (this.f_19800_) {
            this.m_20256_(this.m_20184_().add(0.0, 0.2, 0.0));
            if (this.m_9236_().isClientSide) {
                Vec3 center = this.m_20182_();
                this.m_9236_().addParticle(ParticleTypes.CURRENT_DOWN, center.x, center.y, center.z, 0.0, 0.0, 0.0);
            }
        } else if (!this.m_9236_().isClientSide() && this.timeOutOfWater++ > 5) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_146870_();
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
        this.m_20256_(this.m_20184_().scale(0.9));
    }

    @Override
    public void handleEntityEvent(byte message) {
        if (message == 3) {
            for (int i = 0; i < 10 + this.f_19796_.nextInt(4); i++) {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ACItemRegistry.FLOATER.get())), this.m_20208_(1.0), this.m_20187_(), this.m_20262_(1.0), ((double) this.f_19796_.nextFloat() - 0.5) * 0.3, ((double) this.f_19796_.nextFloat() - 0.5) * 0.3, ((double) this.f_19796_.nextFloat() - 0.5) * 0.3);
            }
        } else {
            super.handleEntityEvent(message);
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public boolean isPickable() {
        return !this.m_213877_();
    }

    @Override
    public boolean shouldBeSaved() {
        return !this.m_213877_();
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ACItemRegistry.FLOATER.get());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.m_9236_().isClientSide) {
            return player.m_20329_(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.m_20365_(passenger) && passenger instanceof LivingEntity living && !this.m_146899_()) {
            double d0 = this.m_20186_() + 0.7F + passenger.getMyRidingOffset();
            moveFunction.accept(passenger, this.m_20185_(), d0, this.m_20189_());
            passenger.fallDistance = 0.0F;
            return;
        }
        super.positionRider(passenger, moveFunction);
    }

    @Override
    public boolean causeFallDamage(float f, float f1, DamageSource damageSource) {
        return false;
    }
}