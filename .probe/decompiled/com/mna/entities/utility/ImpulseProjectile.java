package com.mna.entities.utility;

import com.mna.api.entities.DamageHelper;
import com.mna.enchantments.framework.EnchantmentInit;
import com.mna.entities.EntityInit;
import com.mna.items.ItemInit;
import com.mna.tools.InventoryUtilities;
import java.util.Map;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class ImpulseProjectile extends ThrowableItemProjectile implements IEntityAdditionalSpawnData {

    ItemStack defaultStack;

    float damage;

    float chanceForItemBack = 0.0F;

    public ImpulseProjectile(EntityType<? extends ImpulseProjectile> p_i50159_1_, Level p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
        this.defaultStack = new ItemStack(ItemInit.RUNE_PATTERN.get());
    }

    public ImpulseProjectile(Level worldIn, LivingEntity throwerIn, ItemStack stack, float damage) {
        super(EntityInit.IMPULSE_PROJECTILE.get(), throwerIn, worldIn);
        this.defaultStack = stack.copy();
        this.defaultStack.setCount(1);
        this.damage = damage;
    }

    public ImpulseProjectile(Level worldIn, double x, double y, double z, ItemStack stack, float damage) {
        super(EntityInit.IMPULSE_PROJECTILE.get(), x, y, z, worldIn);
        this.defaultStack = stack.copy();
        this.defaultStack.setCount(1);
        this.damage = damage;
    }

    @Override
    protected Item getDefaultItem() {
        return this.defaultStack.getItem();
    }

    public void setChanceForRecovery(float chance) {
        this.chanceForItemBack = chance;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @OnlyIn(Dist.CLIENT)
    private ParticleOptions makeParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, this.defaultStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions iparticledata = this.makeParticle();
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(iparticledata, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        Entity owner = this.m_19749_();
        if (result instanceof EntityHitResult && owner instanceof LivingEntity) {
            Entity e = ((EntityHitResult) result).getEntity();
            if (e == owner) {
                return;
            }
            if (e instanceof LivingEntity) {
                ((LivingEntity) e).hurt(DamageHelper.createSourcedType(DamageTypes.FALLING_STALACTITE, e.level().registryAccess(), owner), this.damage);
            }
        }
        if (result.getType() != HitResult.Type.MISS) {
            if (this.defaultStack.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) this.defaultStack.getItem()).getBlock();
                SoundEvent sound = block.defaultBlockState().getSoundType(this.m_9236_(), this.m_20183_(), this).getPlaceSound();
                this.m_5496_(sound, 1.0F, 1.0F);
            } else {
                this.m_146852_(GameEvent.PROJECTILE_LAND, this.m_19749_());
            }
        }
        ItemStack dropStack = this.defaultStack.copy();
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(dropStack);
        if (owner != null && owner instanceof Player && !((Player) owner).isCreative() && enchants.containsKey(Enchantments.LOYALTY) || enchants.containsKey(EnchantmentInit.RETURNING.get())) {
            dropStack = InventoryUtilities.mergeToPlayerInvPrioritizeOffhand((Player) owner, dropStack);
        }
        if (dropStack.getCount() > 0 && ((double) this.chanceForItemBack > Math.random() || result instanceof BlockHitResult) && !this.m_9236_().isClientSide()) {
            ItemEntity returnedItem = new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), dropStack);
            this.m_9236_().m_7967_(returnedItem);
        }
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        CompoundTag item = new CompoundTag();
        this.defaultStack.save(item);
        pCompound.put("item", item);
        pCompound.putFloat("recoveryChance", this.chanceForItemBack);
        pCompound.putFloat("damage", this.damage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("item")) {
            this.defaultStack = ItemStack.of(pCompound.getCompound("item"));
        }
        if (pCompound.contains("recoveryChance")) {
            this.chanceForItemBack = pCompound.getFloat("recoveryChance");
        }
        if (pCompound.contains("damage")) {
            this.damage = pCompound.getFloat("damage");
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItemStack(this.defaultStack, true);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.defaultStack = additionalData.readItem();
    }
}