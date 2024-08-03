package com.mna.entities.faction.util;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.entities.FactionRaidRegistry;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.FactionIDs;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.entities.EntityInit;
import com.mna.factions.Factions;
import com.mna.tools.SummonUtils;
import com.mojang.datafixers.util.Pair;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableInt;

public class FactionRaid extends Entity {

    private static final EntityDataAccessor<String> FACTION = SynchedEntityData.defineId(FactionRaid.class, EntityDataSerializers.STRING);

    private Player player;

    private UUID playerUUID;

    private int strength;

    private boolean isProtective;

    private MobEffectInstance[] additionalEffects;

    private IFaction __cachedFaction;

    public FactionRaid(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public FactionRaid(Level world, Player target, int strength) {
        super(EntityInit.FACTION_RAID_ENTITY.get(), world);
        this.player = target;
        this.strength = strength;
    }

    public void setProtective(Player guard) {
        this.player = guard;
        this.isProtective = true;
    }

    public void setAdditionalEffects(MobEffectInstance[] effects) {
        this.additionalEffects = effects;
    }

    @Override
    public void tick() {
        if (!this.m_9236_().isClientSide() && this.f_19797_ % 20 == 0 && !this.spawnRaidEntity()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (this.m_9236_().isClientSide()) {
            if (this.getFaction().is(FactionIDs.DEMONS)) {
                this.spawnDemonParticles();
            } else if (this.getFaction().is(FactionIDs.UNDEAD)) {
                this.spawnUndeadParticles();
            }
        } else if (this.f_19797_ == 20) {
            IFaction faction = this.getFaction();
            if (faction != null) {
                this.m_5496_(faction.getRaidSound(), 1.0F, 1.0F);
            }
        }
    }

    private void spawnDemonParticles() {
        for (int i = 0; i < 15; i++) {
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), -0.05F + Math.random() * 0.1F, 0.1F, -0.05F + Math.random() * 0.1F);
        }
        this.m_9236_().addParticle(ParticleTypes.LAVA, this.m_20185_() - 0.5 + Math.random() * 1.0, this.m_20186_(), this.m_20189_() - 0.5 + Math.random() * 1.0, 0.0, 0.05F, 0.0);
        for (int i = 0; i < 5; i++) {
            this.m_9236_().addParticle(ParticleTypes.LANDING_LAVA, this.m_20185_() - 1.5 + Math.random() * 3.0, this.m_20186_(), this.m_20189_() - 1.5 + Math.random() * 3.0, 0.0, 0.05F, 0.0);
        }
    }

    private void spawnUndeadParticles() {
        for (int i = 0; i < 15; i++) {
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.FROST.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), -0.05F + Math.random() * 0.1F, 0.1F, -0.05F + Math.random() * 0.1F);
        }
        for (int i = 0; i < 5; i++) {
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.BONE.get()), this.m_20185_() - 1.5 + Math.random() * 3.0, this.m_20186_(), this.m_20189_() - 1.5 + Math.random() * 3.0, 0.0, 0.05F, 0.0);
        }
    }

    private Player getPlayer() {
        if (this.player == null && this.playerUUID != null) {
            this.player = this.m_9236_().m_46003_(this.playerUUID);
        }
        return this.player;
    }

    private boolean spawnRaidEntity() {
        if (this.getPlayer() == null) {
            return false;
        } else {
            Pair<EntityType<? extends IFactionEnemy<? extends Mob>>, Integer> soldier = FactionRaidRegistry.getSoldier(this.getFaction(), this.strength);
            if (soldier == null) {
                return false;
            } else {
                IFactionEnemy<? extends Mob> entity = (IFactionEnemy<? extends Mob>) ((EntityType) soldier.getFirst()).create(this.m_9236_());
                if (!this.isProtective) {
                    entity.setRaidTarget(this.getPlayer());
                } else {
                    SummonUtils.setSummon((Mob) entity, this.player, 1200);
                }
                entity.setTier((Integer) soldier.getSecond());
                ((LivingEntity) entity).m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 3));
                if (this.additionalEffects != null) {
                    for (MobEffectInstance inst : this.additionalEffects) {
                        ((LivingEntity) entity).addEffect(new MobEffectInstance(inst.getEffect(), inst.getDuration(), inst.getAmplifier(), inst.isAmbient(), inst.isVisible(), inst.showIcon()));
                    }
                }
                this.m_9236_().m_7967_((Entity) entity);
                int entity_strength_rating = FactionRaidRegistry.getStrengthRating(this.getFaction(), (EntityType<? extends IFactionEnemy<? extends Mob>>) soldier.getFirst(), (Integer) soldier.getSecond());
                if (entity_strength_rating == -1) {
                    return false;
                } else {
                    this.strength -= entity_strength_rating;
                    return this.strength > 0;
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(FACTION, FactionIDs.COUNCIL.toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("strength")) {
            this.strength = compound.getInt("strength");
        }
        if (compound.contains("protecting")) {
            this.isProtective = compound.getBoolean("protecting");
        }
        if (compound.contains("faction")) {
            this.f_19804_.set(FACTION, compound.getString("faction"));
        }
        if (compound.contains("afx")) {
            ListTag nbt = compound.getList("afx", 10);
            if (nbt != null) {
                this.additionalEffects = new MobEffectInstance[nbt.size()];
                MutableInt idx = new MutableInt(0);
                nbt.forEach(n -> this.additionalEffects[idx.intValue()] = MobEffectInstance.load((CompoundTag) n));
            }
        }
        if (compound.contains("target")) {
            try {
                this.playerUUID = UUID.fromString(compound.getString("target"));
            } catch (Exception var4) {
                ManaAndArtifice.LOGGER.error("Failed to load player UUID when loading faction raid.  Skipping and despawning the raid.");
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("strength", this.strength);
        compound.putString("faction", this.f_19804_.get(FACTION));
        compound.putBoolean("protecting", this.isProtective);
        if (this.additionalEffects != null) {
            ListTag afx = new ListTag();
            for (MobEffectInstance inst : this.additionalEffects) {
                CompoundTag instSaved = inst.save(new CompoundTag());
                afx.add(instSaved);
            }
            compound.put("afx", afx);
        }
        if (this.player != null) {
            compound.putString("target", this.player.m_20148_().toString());
        }
    }

    public IFaction getFaction() {
        if (this.__cachedFaction == null) {
            ResourceLocation factionID = new ResourceLocation(this.f_19804_.get(FACTION));
            this.__cachedFaction = Factions.INSTANCE.getFaction(factionID);
        }
        return this.__cachedFaction;
    }

    public void setFaction(IFaction faction) {
        this.f_19804_.set(FACTION, ((IForgeRegistry) Registries.Factions.get()).getKey(faction).toString());
    }

    public void setFaction(ResourceLocation factionID) {
        this.f_19804_.set(FACTION, factionID.toString());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (pKey == FACTION) {
            this.__cachedFaction = null;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}