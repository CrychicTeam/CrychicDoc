package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class EntityHippogryphEgg extends ThrownEgg {

    private ItemStack itemstack;

    public EntityHippogryphEgg(EntityType<? extends ThrownEgg> type, Level world) {
        super(type, world);
    }

    public EntityHippogryphEgg(EntityType<? extends ThrownEgg> type, Level worldIn, double x, double y, double z, ItemStack stack) {
        this(type, worldIn);
        this.m_6034_(x, y, z);
        this.itemstack = stack;
    }

    public EntityHippogryphEgg(EntityType<? extends ThrownEgg> type, Level worldIn, LivingEntity throwerIn, ItemStack stack) {
        this(type, worldIn);
        this.m_6034_(throwerIn.m_20185_(), throwerIn.m_20188_() - 0.1F, throwerIn.m_20189_());
        this.itemstack = stack;
        this.m_5602_(throwerIn);
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_7846_()), this.m_20185_(), this.m_20186_(), this.m_20189_(), ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        Entity thrower = this.m_19749_();
        if (result.getType() == HitResult.Type.ENTITY) {
            ((EntityHitResult) result).getEntity().hurt(this.m_9236_().damageSources().thrown(this, thrower), 0.0F);
        }
        if (!this.m_9236_().isClientSide) {
            EntityHippogryph hippogryph = new EntityHippogryph(IafEntityRegistry.HIPPOGRYPH.get(), this.m_9236_());
            hippogryph.m_146762_(-24000);
            hippogryph.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), 0.0F);
            if (this.itemstack != null) {
                int variant = 0;
                CompoundTag tag = this.itemstack.getTag();
                if (tag != null) {
                    variant = tag.getInt("EggOrdinal");
                }
                hippogryph.setVariant(variant);
            }
            if (thrower instanceof Player) {
                hippogryph.m_21828_((Player) thrower);
            }
            this.m_9236_().m_7967_(hippogryph);
        }
        this.m_9236_().broadcastEntityEvent(this, (byte) 3);
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    @NotNull
    @Override
    protected Item getDefaultItem() {
        return IafItemRegistry.HIPPOGRYPH_EGG.get();
    }
}