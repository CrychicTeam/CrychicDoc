package com.mna.entities.faction.base;

import com.google.common.collect.Maps;
import com.mna.api.entities.IFactionEnemy;
import com.mna.entities.IAnimPacketSync;
import com.mna.entities.ai.RetaliateOnAttackGoal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Tuple;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.mutable.MutableInt;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BaseFactionMob<T extends BaseFactionMob<?>> extends Monster implements IFactionEnemy<T>, GeoEntity, IAnimPacketSync<T>, IEntityAdditionalSpawnData {

    private static final UUID TierHealthID = UUID.fromString("76082490-0ded-11ee-be56-0242ac120002");

    private static final UUID TierArmorID = UUID.fromString("79474e2e-0ded-11ee-be56-0242ac120002");

    private static final EntityDataAccessor<Integer> DATA_STATE = SynchedEntityData.defineId(BaseFactionMob.class, EntityDataSerializers.INT);

    protected Player raidTarget;

    protected int tier;

    protected HashMap<String, Integer> damageResists;

    protected AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    private final Map<String, Tuple<MutableInt, Runnable>> timers = Maps.newHashMap();

    private final Map<Integer, Integer> cooldowns = Maps.newHashMap();

    protected BaseFactionMob(EntityType<T> mobType, Level world) {
        super(mobType, world);
        this.damageResists = new HashMap();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_STATE, 0);
    }

    protected void setTimer(String id, int delay, Runnable callback) {
        this.setTimer(id, delay, callback, true);
    }

    protected void setTimer(String id, int delay, Runnable callback, boolean runIfExists) {
        if (runIfExists && this.timers.containsKey(id)) {
            ((Runnable) ((Tuple) this.timers.get(id)).getB()).run();
        }
        this.timers.put(id, new Tuple<>(new MutableInt(delay), callback));
    }

    protected void setCooldown(int id, int ticks) {
        this.cooldowns.put(id, ticks);
    }

    protected boolean isOnCooldown(int id) {
        return (Integer) this.cooldowns.getOrDefault(id, 0) > 0;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.cooldowns.keySet().forEach(c -> {
            int cd = (Integer) this.cooldowns.get(c);
            if (cd > 0) {
                this.cooldowns.put(c, cd - 1);
            }
        });
        List<String> timersToRemove = new ArrayList();
        this.timers.forEach((k, v) -> {
            ((MutableInt) ((Tuple) this.timers.get(k)).getA()).subtract(1);
            if (((MutableInt) ((Tuple) this.timers.get(k)).getA()).getValue() <= 0) {
                timersToRemove.add(k);
            }
        });
        timersToRemove.forEach(id -> {
            ((Runnable) ((Tuple) this.timers.get(id)).getB()).run();
            this.timers.remove(id);
        });
    }

    protected int getStateFlag() {
        return this.f_19804_.get(DATA_STATE);
    }

    protected void setState(int... flags) {
        int finalFlag = 0;
        for (int i : flags) {
            finalFlag |= i;
        }
        this.f_19804_.set(DATA_STATE, finalFlag);
    }

    @Override
    protected void registerGoals() {
        this.f_21346_.addGoal(1, new RetaliateOnAttackGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, (Class<T>) Player.class, 10, true, false, this::factionTargetPlayerPredicate));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, (Class<T>) Mob.class, 10, true, false, this::factionTargetHelpPredicate));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, (Class<T>) Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    public float getStepHeight() {
        return 1.2F;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
        this.applyInitialSpawnTier(worldIn);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        AnimationController<T> controller = new AnimationController<>((T) this, state -> this.handleAnimState(state));
        this.addControllerListeners(controller);
        registrar.add(controller);
    }

    protected void addControllerListeners(AnimationController<T> controller) {
    }

    @Override
    public void setRaidTarget(Player player) {
        this.raidTarget = player;
        this.m_6710_(player);
    }

    @Override
    public Player getRaidTarget() {
        return this.raidTarget;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public void setTier(int tier) {
        this.tier = tier;
        AttributeInstance maxHPAttr = this.m_21051_(Attributes.MAX_HEALTH);
        AttributeInstance maxArmorAttr = this.m_21051_(Attributes.ARMOR);
        maxHPAttr.removePermanentModifier(TierHealthID);
        maxArmorAttr.removePermanentModifier(TierArmorID);
        maxHPAttr.addPermanentModifier(new AttributeModifier(TierHealthID, "tier_health_bonus", (double) (tier + 1), AttributeModifier.Operation.MULTIPLY_BASE));
        maxArmorAttr.addPermanentModifier(new AttributeModifier(TierArmorID, "tier_armor_bonus", (double) (5 * tier), AttributeModifier.Operation.ADDITION));
        this.m_21153_(this.m_21233_());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        this.writeFactionData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.readFactionData(compound);
    }

    @Override
    public HashMap<String, Integer> getDamageResists() {
        return this.damageResists;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        amount = this.applyDamageResists(source, amount);
        return super.m_6469_(source, amount);
    }

    @Override
    public void checkDespawn() {
        super.m_6043_();
        this.raidTargetDespawn();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void die(DamageSource cause) {
        super.m_6667_(cause);
        this.onKilled(cause);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.tier = additionalData.readInt();
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.tier);
    }

    protected abstract PlayState handleAnimState(AnimationState<? extends BaseFactionMob<?>> var1);
}