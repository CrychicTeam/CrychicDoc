package net.minecraft.world.entity.animal.horse;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class TraderLlama extends Llama {

    private int despawnDelay = 47999;

    public TraderLlama(EntityType<? extends TraderLlama> entityTypeExtendsTraderLlama0, Level level1) {
        super(entityTypeExtendsTraderLlama0, level1);
    }

    @Override
    public boolean isTraderLlama() {
        return true;
    }

    @Nullable
    @Override
    protected Llama makeNewLlama() {
        return EntityType.TRADER_LLAMA.create(this.m_9236_());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("DespawnDelay", this.despawnDelay);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("DespawnDelay", 99)) {
            this.despawnDelay = compoundTag0.getInt("DespawnDelay");
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(1, new PanicGoal(this, 2.0));
        this.f_21346_.addGoal(1, new TraderLlama.TraderLlamaDefendWanderingTraderGoal(this));
    }

    public void setDespawnDelay(int int0) {
        this.despawnDelay = int0;
    }

    @Override
    protected void doPlayerRide(Player player0) {
        Entity $$1 = this.m_21524_();
        if (!($$1 instanceof WanderingTrader)) {
            super.m_6835_(player0);
        }
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_9236_().isClientSide) {
            this.maybeDespawn();
        }
    }

    private void maybeDespawn() {
        if (this.canDespawn()) {
            this.despawnDelay = this.isLeashedToWanderingTrader() ? ((WanderingTrader) this.m_21524_()).getDespawnDelay() - 1 : this.despawnDelay - 1;
            if (this.despawnDelay <= 0) {
                this.m_21455_(true, false);
                this.m_146870_();
            }
        }
    }

    private boolean canDespawn() {
        return !this.m_30614_() && !this.isLeashedToSomethingOtherThanTheWanderingTrader() && !this.m_146898_();
    }

    private boolean isLeashedToWanderingTrader() {
        return this.m_21524_() instanceof WanderingTrader;
    }

    private boolean isLeashedToSomethingOtherThanTheWanderingTrader() {
        return this.m_21523_() && !this.isLeashedToWanderingTrader();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        if (mobSpawnType2 == MobSpawnType.EVENT) {
            this.m_146762_(0);
        }
        if (spawnGroupData3 == null) {
            spawnGroupData3 = new AgeableMob.AgeableMobGroupData(false);
        }
        return super.finalizeSpawn(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    protected static class TraderLlamaDefendWanderingTraderGoal extends TargetGoal {

        private final Llama llama;

        private LivingEntity ownerLastHurtBy;

        private int timestamp;

        public TraderLlamaDefendWanderingTraderGoal(Llama llama0) {
            super(llama0, false);
            this.llama = llama0;
            this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (!this.llama.m_21523_()) {
                return false;
            } else if (!(this.llama.m_21524_() instanceof WanderingTrader $$1)) {
                return false;
            } else {
                this.ownerLastHurtBy = $$1.m_21188_();
                int $$2 = $$1.m_21213_();
                return $$2 != this.timestamp && this.m_26150_(this.ownerLastHurtBy, TargetingConditions.DEFAULT);
            }
        }

        @Override
        public void start() {
            this.f_26135_.setTarget(this.ownerLastHurtBy);
            Entity $$0 = this.llama.m_21524_();
            if ($$0 instanceof WanderingTrader) {
                this.timestamp = ((WanderingTrader) $$0).m_21213_();
            }
            super.start();
        }
    }
}