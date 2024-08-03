package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.entity.util.IHumanoid;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

public class EntityDreadMob extends Monster implements IDreadMob {

    protected static final EntityDataAccessor<Optional<UUID>> COMMANDER_UNIQUE_ID = SynchedEntityData.defineId(EntityDreadMob.class, EntityDataSerializers.OPTIONAL_UUID);

    public EntityDreadMob(EntityType<? extends Monster> t, Level worldIn) {
        super(t, worldIn);
    }

    public static Entity necromancyEntity(LivingEntity entity) {
        Entity lichSummoned = null;
        if (entity.getMobType() == MobType.ARTHROPOD) {
            Entity var10 = new EntityDreadScuttler(IafEntityRegistry.DREAD_SCUTTLER.get(), entity.m_9236_());
            float readInScale = entity.m_20205_() / 1.5F;
            if (entity.m_9236_() instanceof ServerLevelAccessor) {
                var10.finalizeSpawn((ServerLevelAccessor) entity.m_9236_(), entity.m_9236_().getCurrentDifficultyAt(entity.m_20183_()), MobSpawnType.MOB_SUMMONED, null, null);
            }
            var10.setSize(readInScale);
            return var10;
        } else if (!(entity instanceof Zombie) && !(entity instanceof IHumanoid)) {
            if (entity.getMobType() == MobType.UNDEAD || entity instanceof AbstractSkeleton || entity instanceof Player) {
                Entity var9 = new EntityDreadThrall(IafEntityRegistry.DREAD_THRALL.get(), entity.m_9236_());
                EntityDreadThrall thrall = var9;
                if (entity.m_9236_() instanceof ServerLevelAccessor) {
                    thrall.finalizeSpawn((ServerLevelAccessor) entity.m_9236_(), entity.m_9236_().getCurrentDifficultyAt(entity.m_20183_()), MobSpawnType.MOB_SUMMONED, null, null);
                }
                thrall.setCustomArmorHead(false);
                thrall.setCustomArmorChest(false);
                thrall.setCustomArmorLegs(false);
                thrall.setCustomArmorFeet(false);
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    thrall.m_8061_(slot, entity.getItemBySlot(slot));
                }
                return thrall;
            } else if (entity instanceof AbstractHorse) {
                return new EntityDreadHorse(IafEntityRegistry.DREAD_HORSE.get(), entity.m_9236_());
            } else if (entity instanceof Animal) {
                Entity var8 = new EntityDreadBeast(IafEntityRegistry.DREAD_BEAST.get(), entity.m_9236_());
                float readInScale = entity.m_20205_() / 1.2F;
                if (entity.m_9236_() instanceof ServerLevelAccessor) {
                    var8.finalizeSpawn((ServerLevelAccessor) entity.m_9236_(), entity.m_9236_().getCurrentDifficultyAt(entity.m_20183_()), MobSpawnType.MOB_SUMMONED, null, null);
                }
                var8.setSize(readInScale);
                return var8;
            } else {
                return lichSummoned;
            }
        } else {
            Entity var7 = new EntityDreadGhoul(IafEntityRegistry.DREAD_GHOUL.get(), entity.m_9236_());
            float readInScale = entity.m_20205_() / 0.6F;
            if (entity.m_9236_() instanceof ServerLevelAccessor) {
                var7.finalizeSpawn((ServerLevelAccessor) entity.m_9236_(), entity.m_9236_().getCurrentDifficultyAt(entity.m_20183_()), MobSpawnType.MOB_SUMMONED, null, null);
            }
            var7.setSize(readInScale);
            return var7;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(COMMANDER_UNIQUE_ID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getCommanderId() != null) {
            compound.putUUID("CommanderUUID", this.getCommanderId());
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7378_(compound);
        UUID uuid;
        if (compound.hasUUID("CommanderUUID")) {
            uuid = compound.getUUID("CommanderUUID");
        } else {
            String s = compound.getString("CommanderUUID");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.m_20194_(), s);
        }
        if (uuid != null) {
            try {
                this.setCommanderId(uuid);
            } catch (Throwable var4) {
            }
        }
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
        return entityIn instanceof IDreadMob || super.m_7307_(entityIn);
    }

    @Nullable
    public UUID getCommanderId() {
        return (UUID) this.f_19804_.get(COMMANDER_UNIQUE_ID).orElse(null);
    }

    public void setCommanderId(@Nullable UUID uuid) {
        this.f_19804_.set(COMMANDER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.m_9236_().isClientSide && this.getCommander() instanceof EntityDreadLich) {
            EntityDreadLich lich = (EntityDreadLich) this.getCommander();
            if (lich.m_5448_() != null && lich.m_5448_().isAlive()) {
                this.m_6710_(lich.m_5448_());
            }
        }
    }

    @Override
    public Entity getCommander() {
        try {
            UUID uuid = this.getCommanderId();
            LivingEntity player = uuid == null ? null : this.m_9236_().m_46003_(uuid);
            if (player != null) {
                return player;
            } else {
                if (!this.m_9236_().isClientSide) {
                    Entity entity = this.m_9236_().getServer().getLevel(this.m_9236_().dimension()).getEntity(uuid);
                    if (entity instanceof LivingEntity) {
                        return entity;
                    }
                }
                return null;
            }
        } catch (IllegalArgumentException var4) {
            return null;
        }
    }

    public void onKillEntity(LivingEntity LivingEntityIn) {
        Entity commander = (Entity) (this instanceof EntityDreadLich ? this : this.getCommander());
        if (commander != null && !(LivingEntityIn instanceof EntityDragonBase)) {
            Entity summoned = necromancyEntity(LivingEntityIn);
            if (summoned != null) {
                summoned.copyPosition(LivingEntityIn);
                if (!this.m_9236_().isClientSide) {
                    this.m_9236_().m_7967_(summoned);
                }
                if (commander instanceof EntityDreadLich) {
                    ((EntityDreadLich) commander).setMinionCount(((EntityDreadLich) commander).getMinionCount() + 1);
                }
                if (summoned instanceof EntityDreadMob) {
                    ((EntityDreadMob) summoned).setCommanderId(commander.getUUID());
                }
            }
        }
    }

    @Override
    public void remove(@NotNull Entity.RemovalReason reason) {
        if (!this.m_213877_() && this.getCommander() != null && this.getCommander() instanceof EntityDreadLich) {
            EntityDreadLich lich = (EntityDreadLich) this.getCommander();
            lich.setMinionCount(lich.getMinionCount() - 1);
        }
        super.m_142687_(reason);
    }

    @NotNull
    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
}