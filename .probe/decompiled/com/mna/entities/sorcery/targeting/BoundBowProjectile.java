package com.mna.entities.sorcery.targeting;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

public class BoundBowProjectile extends AbstractArrow {

    private static final EntityDataAccessor<Integer> AFFINITY = SynchedEntityData.defineId(BoundBowProjectile.class, EntityDataSerializers.INT);

    private ISpellDefinition spell;

    public BoundBowProjectile(EntityType<? extends AbstractArrow> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.m_20331_(true);
    }

    public void setSpell(ISpellDefinition recipe) {
        this.spell = recipe;
        this.f_19804_.set(AFFINITY, this.spell.getHighestAffinity().ordinal());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(AFFINITY, Affinity.ARCANE.ordinal());
    }

    public Affinity getAffinity() {
        return Affinity.values()[this.f_19804_.get(AFFINITY)];
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().isClientSide()) {
            float spread = 0.2F;
            for (int i = 0; i < 10; i++) {
                double velMod = Math.random();
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), this.m_20185_() - (double) spread * Math.random() * (double) spread * 2.0, this.m_20186_() - (double) spread * Math.random() * (double) spread * 2.0, this.m_20189_() - (double) spread * Math.random() * (double) spread * 2.0, this.m_20184_().x * velMod, this.m_20184_().y * velMod, this.m_20184_().z * velMod);
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        if (this.spell != null && this.spell.isValid()) {
            this.spell.writeToNBT(compound);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.spell = SpellRecipe.fromNBT(compound);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.m_9236_().isClientSide() && this.spell != null && this.spell.isValid()) {
            Entity shooter = this.m_19749_();
            if (shooter != null && shooter instanceof LivingEntity) {
                SpellSource source = new SpellSource((LivingEntity) shooter, InteractionHand.MAIN_HAND);
                SpellTarget target = new SpellTarget(result.getEntity());
                SpellContext context = new SpellContext(this.m_9236_(), this.spell);
                HashMap<SpellEffect, ComponentApplicationResult> results = SpellCaster.ApplyComponents(this.spell, source, target, context);
                if (this.m_19749_() instanceof Player) {
                    results.entrySet().forEach(e -> {
                        if (((ComponentApplicationResult) e.getValue()).is_success) {
                            SpellCaster.addComponentRoteProgress((Player) this.m_19749_(), (SpellEffect) e.getKey());
                        }
                    });
                }
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.m_9236_().isClientSide() && this.spell != null && this.spell.isValid()) {
            Entity shooter = this.m_19749_();
            if (shooter != null && shooter instanceof LivingEntity) {
                SpellSource source = new SpellSource((LivingEntity) shooter, InteractionHand.MAIN_HAND);
                SpellTarget target = new SpellTarget(result.getBlockPos(), result.getDirection());
                SpellContext context = new SpellContext(this.m_9236_(), this.spell);
                HashMap<SpellEffect, ComponentApplicationResult> results = SpellCaster.ApplyComponents(this.spell, source, target, context);
                if (this.m_19749_() instanceof Player) {
                    results.entrySet().forEach(e -> {
                        if (((ComponentApplicationResult) e.getValue()).is_success) {
                            SpellCaster.addComponentRoteProgress((Player) this.m_19749_(), (SpellEffect) e.getKey());
                        }
                    });
                }
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }
}