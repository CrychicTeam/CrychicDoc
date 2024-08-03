package com.mna.entities.faction.util;

import com.mna.Registries;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.entities.FactionRaidRegistry;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.IFaction;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.tools.math.MathUtils;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableInt;

public class FactionWar extends Mob {

    public static final int MAXIMUM_STRENGTH = 300;

    public static final int MINIMUM_STRENGTH = 30;

    int strength_first = 0;

    int strength_second = 0;

    boolean calculatedStrength = false;

    IFaction first;

    IFaction second;

    public FactionWar(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_19794_ = true;
        this.m_20242_(true);
    }

    @Override
    public void tick() {
        if (!this.calculatedStrength) {
            this.calculateStrength();
        }
        if (!this.m_9236_().isClientSide()) {
            if (this.first != null && this.second != null && (this.strength_first > 0 || this.strength_second > 0)) {
                if (this.m_9236_().getGameTime() % 10L == 0L) {
                    if (this.strength_first > 0) {
                        this.strength_first = this.spawnRaidEntity(this.first, this.strength_first);
                    }
                    if (this.strength_second > 0) {
                        this.strength_second = this.spawnRaidEntity(this.second, this.strength_second);
                    }
                }
            } else {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    public static boolean checkMonsterSpawnRules(EntityType<FactionWar> pType, ServerLevelAccessor pLevel, MobSpawnType pReason, BlockPos pPos, RandomSource pRandom) {
        return pLevel.m_46791_() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(pLevel, pPos, pRandom) && m_217057_(pType, pLevel, pReason, pPos, pRandom);
    }

    private int spawnRaidEntity(IFaction faction, int strength) {
        Pair<EntityType<? extends IFactionEnemy<? extends Mob>>, Integer> soldier = FactionRaidRegistry.getSoldier(faction, strength);
        if (soldier == null) {
            return 0;
        } else {
            IFactionEnemy<? extends Mob> entity = (IFactionEnemy<? extends Mob>) ((EntityType) soldier.getFirst()).create(this.m_9236_());
            entity.setTier((Integer) soldier.getSecond());
            Vec3 spawnPos = this.m_20182_();
            for (int i = 0; i < 10; i++) {
                BlockPos testSpawnPos = this.m_20183_().offset((int) (-8.0 + Math.random() * 16.0), (int) (-8.0 + Math.random() * 16.0), (int) (-8.0 + Math.random() * 16.0));
                int adjustCount = 0;
                while (this.m_9236_().m_46859_(testSpawnPos) && adjustCount++ < 10) {
                    testSpawnPos = testSpawnPos.below();
                }
                if (!this.m_9236_().m_46859_(testSpawnPos)) {
                    adjustCount = 0;
                    while (!this.m_9236_().m_46859_(testSpawnPos) && adjustCount++ < 10) {
                        testSpawnPos = testSpawnPos.above();
                    }
                    if (this.m_9236_().m_46859_(testSpawnPos)) {
                        spawnPos = Vec3.atBottomCenterOf(testSpawnPos);
                        break;
                    }
                }
            }
            LivingEntity living = (LivingEntity) entity;
            living.m_6034_(spawnPos.x, spawnPos.y, spawnPos.z);
            living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 3));
            if (living.getMobType() == MobType.UNDEAD && this.m_9236_().isDay()) {
                living.addEffect(new MobEffectInstance(EffectInit.SOAKED.get(), 300, 0));
            }
            this.m_9236_().m_7967_((Entity) entity);
            int entity_strength_rating = FactionRaidRegistry.getStrengthRating(faction, (EntityType<? extends IFactionEnemy<? extends Mob>>) soldier.getFirst(), (Integer) soldier.getSecond());
            return entity_strength_rating == -1 ? 0 : strength - entity_strength_rating;
        }
    }

    private void calculateStrength() {
        this.calculatedStrength = true;
        MutableInt totalNearbyTier = new MutableInt(0);
        MutableInt totalNearbyLevel = new MutableInt(0);
        AABB bb = this.m_20191_().inflate(128.0);
        this.m_9236_().m_6907_().forEach(player -> {
            if (bb.intersects(player.m_20191_())) {
                player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> totalNearbyLevel.add(m.getMagicLevel()));
                player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> totalNearbyTier.add(p.getTier()));
            }
        });
        if (totalNearbyTier.getValue() != 0 && totalNearbyLevel.getValue() != 0) {
            this.strength_first = totalNearbyLevel.getValue() / 3 * totalNearbyTier.getValue();
            this.strength_first = MathUtils.clamp(this.strength_first, 30, 300);
            this.strength_second = this.strength_first;
        } else {
            this.strength_first = 0;
            this.strength_second = 0;
        }
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!this.calculatedStrength) {
            this.calculateStrength();
            List<IFaction> allFactions = ManaAndArtificeMod.getFactionHelper().getAllFactions();
            this.first = (IFaction) allFactions.get((int) (Math.random() * (double) allFactions.size()));
            List<IFaction> otherFactions = ManaAndArtificeMod.getFactionHelper().getFactionsExcept(this.first);
            this.second = (IFaction) otherFactions.get((int) (Math.random() * (double) otherFactions.size()));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.strength_first = pCompound.getInt("strength_first");
        this.strength_second = pCompound.getInt("strength_second");
        if (pCompound.contains("first") && pCompound.contains("second")) {
            this.first = ManaAndArtificeMod.getFactionHelper().getFaction(new ResourceLocation(pCompound.getString("first")));
            this.second = ManaAndArtificeMod.getFactionHelper().getFaction(new ResourceLocation(pCompound.getString("second")));
            this.calculatedStrength = this.first != null && this.second != null;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("strength_first", this.strength_first);
        pCompound.putInt("strength_second", this.strength_second);
        if (this.first != null) {
            pCompound.putString("first", ((IForgeRegistry) Registries.Factions.get()).getKey(this.first).toString());
        }
        if (this.second != null) {
            pCompound.putString("second", ((IForgeRegistry) Registries.Factions.get()).getKey(this.second).toString());
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0).add(Attributes.MOVEMENT_SPEED, 0.0).add(Attributes.ATTACK_DAMAGE, 0.0).add(Attributes.ATTACK_SPEED, 0.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance pPotioneffect) {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public boolean canBeSeenByAnyone() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void setSecondsOnFire(int pSeconds) {
    }

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }
}