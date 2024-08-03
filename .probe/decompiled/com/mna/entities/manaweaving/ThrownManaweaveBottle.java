package com.mna.entities.manaweaving;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.items.ItemInit;
import com.mna.items.manaweaving.ItemManaweaveBottle;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import java.util.function.Predicate;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class ThrownManaweaveBottle extends ThrowableItemProjectile {

    public static final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::m_6126_;

    public ThrownManaweaveBottle(EntityType<? extends ThrownManaweaveBottle> typeIn, Level worldIn) {
        super(typeIn, worldIn);
    }

    public ThrownManaweaveBottle(Level worldIn, LivingEntity livingEntityIn) {
        super(EntityInit.MANAWEAVE_BOTTLE_THROWN.get(), livingEntityIn, worldIn);
    }

    public ThrownManaweaveBottle(Level worldIn, double x, double y, double z) {
        super(EntityInit.MANAWEAVE_BOTTLE_THROWN.get(), x, y, z, worldIn);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.MANAWEAVE_BOTTLE.get();
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    protected void onHit(HitResult result) {
        super.m_6532_(result);
        if (!this.m_9236_().isClientSide()) {
            ItemStack itemstack = this.m_7846_();
            ManaweavingPattern p = ItemManaweaveBottle.getPattern(itemstack);
            Entity shooter = this.m_19749_();
            if (p != null && shooter != null && shooter instanceof Player caster) {
                caster.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(progression -> {
                    int tier = progression.getTier();
                    if (tier >= p.getTier()) {
                        Manaweave emw = new Manaweave(this.m_9236_());
                        emw.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                        emw.setPattern(p.m_6423_());
                        emw.setCaster(caster, InteractionHand.MAIN_HAND);
                        this.m_9236_().m_7967_(emw);
                    }
                });
                this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (this.m_9236_().isClientSide()) {
            for (int i = 0; i < 50; i++) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), -0.5 + Math.random(), -0.5 + Math.random(), -0.5 + Math.random());
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}