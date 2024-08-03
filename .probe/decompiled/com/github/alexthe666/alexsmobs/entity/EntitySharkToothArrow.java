package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntitySharkToothArrow extends Arrow {

    public EntitySharkToothArrow(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    public EntitySharkToothArrow(EntityType type, double x, double y, double z, Level worldIn) {
        this(type, worldIn);
        this.m_6034_(x, y, z);
    }

    public EntitySharkToothArrow(Level worldIn, LivingEntity shooter) {
        this(AMEntityRegistry.SHARK_TOOTH_ARROW.get(), shooter.m_20185_(), shooter.m_20188_() - 0.1F, shooter.m_20189_(), worldIn);
        this.m_5602_(shooter);
        if (shooter instanceof Player) {
            this.f_36705_ = AbstractArrow.Pickup.ALLOWED;
        }
    }

    protected void damageShield(Player player, float damage) {
        if (damage >= 3.0F && player.m_21211_().getItem().canPerformAction(player.m_21211_(), ToolActions.SHIELD_BLOCK)) {
            ItemStack copyBeforeUse = player.m_21211_().copy();
            int i = 1 + Mth.floor(damage);
            player.m_21211_().hurtAndBreak(i, player, p_213360_0_ -> p_213360_0_.m_21166_(EquipmentSlot.CHEST));
            if (player.m_21211_().isEmpty()) {
                InteractionHand Hand = player.m_7655_();
                ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, Hand);
                if (Hand == InteractionHand.MAIN_HAND) {
                    this.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.m_8061_(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                player.m_5810_();
                this.m_5496_(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.m_9236_().random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        if (living instanceof Player) {
            this.damageShield((Player) living, (float) this.m_36789_());
        }
        Entity entity1 = this.m_19749_();
        if (living.getMobType() == MobType.WATER || living instanceof Drowned || living.getMobType() != MobType.UNDEAD && living.canBreatheUnderwater()) {
            DamageSource damagesource;
            if (entity1 == null) {
                damagesource = this.m_269291_().arrow(this, this);
            } else {
                damagesource = this.m_269291_().arrow(this, entity1);
            }
            living.hurt(damagesource, 7.0F);
        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    public EntitySharkToothArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.SHARK_TOOTH_ARROW.get(), world);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(AMItemRegistry.SHARK_TOOTH_ARROW.get());
    }
}