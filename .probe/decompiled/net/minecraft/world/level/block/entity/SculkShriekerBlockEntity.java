package net.minecraft.world.level.block.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.OptionalInt;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class SculkShriekerBlockEntity extends BlockEntity implements GameEventListener.Holder<VibrationSystem.Listener>, VibrationSystem {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int WARNING_SOUND_RADIUS = 10;

    private static final int WARDEN_SPAWN_ATTEMPTS = 20;

    private static final int WARDEN_SPAWN_RANGE_XZ = 5;

    private static final int WARDEN_SPAWN_RANGE_Y = 6;

    private static final int DARKNESS_RADIUS = 40;

    private static final int SHRIEKING_TICKS = 90;

    private static final Int2ObjectMap<SoundEvent> SOUND_BY_LEVEL = Util.make(new Int2ObjectOpenHashMap(), p_222866_ -> {
        p_222866_.put(1, SoundEvents.WARDEN_NEARBY_CLOSE);
        p_222866_.put(2, SoundEvents.WARDEN_NEARBY_CLOSER);
        p_222866_.put(3, SoundEvents.WARDEN_NEARBY_CLOSEST);
        p_222866_.put(4, SoundEvents.WARDEN_LISTENING_ANGRY);
    });

    private int warningLevel;

    private final VibrationSystem.User vibrationUser = new SculkShriekerBlockEntity.VibrationUser();

    private VibrationSystem.Data vibrationData = new VibrationSystem.Data();

    private final VibrationSystem.Listener vibrationListener = new VibrationSystem.Listener(this);

    public SculkShriekerBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.SCULK_SHRIEKER, blockPos0, blockState1);
    }

    @Override
    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        if (compoundTag0.contains("warning_level", 99)) {
            this.warningLevel = compoundTag0.getInt("warning_level");
        }
        if (compoundTag0.contains("listener", 10)) {
            VibrationSystem.Data.CODEC.parse(new Dynamic(NbtOps.INSTANCE, compoundTag0.getCompound("listener"))).resultOrPartial(LOGGER::error).ifPresent(p_281147_ -> this.vibrationData = p_281147_);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        compoundTag0.putInt("warning_level", this.warningLevel);
        VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(LOGGER::error).ifPresent(p_222871_ -> compoundTag0.put("listener", p_222871_));
    }

    @Nullable
    public static ServerPlayer tryGetPlayer(@Nullable Entity entity0) {
        if (entity0 instanceof ServerPlayer) {
            return (ServerPlayer) entity0;
        } else {
            if (entity0 != null) {
                LivingEntity $$6 = entity0.getControllingPassenger();
                if ($$6 instanceof ServerPlayer) {
                    return (ServerPlayer) $$6;
                }
            }
            if (entity0 instanceof Projectile $$3) {
                Entity var3 = $$3.getOwner();
                if (var3 instanceof ServerPlayer) {
                    return (ServerPlayer) var3;
                }
            }
            if (entity0 instanceof ItemEntity $$5) {
                Entity var9 = $$5.getOwner();
                if (var9 instanceof ServerPlayer) {
                    return (ServerPlayer) var9;
                }
            }
            return null;
        }
    }

    public void tryShriek(ServerLevel serverLevel0, @Nullable ServerPlayer serverPlayer1) {
        if (serverPlayer1 != null) {
            BlockState $$2 = this.m_58900_();
            if (!(Boolean) $$2.m_61143_(SculkShriekerBlock.SHRIEKING)) {
                this.warningLevel = 0;
                if (!this.canRespond(serverLevel0) || this.tryToWarn(serverLevel0, serverPlayer1)) {
                    this.shriek(serverLevel0, serverPlayer1);
                }
            }
        }
    }

    private boolean tryToWarn(ServerLevel serverLevel0, ServerPlayer serverPlayer1) {
        OptionalInt $$2 = WardenSpawnTracker.tryWarn(serverLevel0, this.m_58899_(), serverPlayer1);
        $$2.ifPresent(p_222838_ -> this.warningLevel = p_222838_);
        return $$2.isPresent();
    }

    private void shriek(ServerLevel serverLevel0, @Nullable Entity entity1) {
        BlockPos $$2 = this.m_58899_();
        BlockState $$3 = this.m_58900_();
        serverLevel0.m_7731_($$2, (BlockState) $$3.m_61124_(SculkShriekerBlock.SHRIEKING, true), 2);
        serverLevel0.m_186460_($$2, $$3.m_60734_(), 90);
        serverLevel0.m_46796_(3007, $$2, 0);
        serverLevel0.m_220407_(GameEvent.SHRIEK, $$2, GameEvent.Context.of(entity1));
    }

    private boolean canRespond(ServerLevel serverLevel0) {
        return (Boolean) this.m_58900_().m_61143_(SculkShriekerBlock.CAN_SUMMON) && serverLevel0.m_46791_() != Difficulty.PEACEFUL && serverLevel0.m_46469_().getBoolean(GameRules.RULE_DO_WARDEN_SPAWNING);
    }

    public void tryRespond(ServerLevel serverLevel0) {
        if (this.canRespond(serverLevel0) && this.warningLevel > 0) {
            if (!this.trySummonWarden(serverLevel0)) {
                this.playWardenReplySound(serverLevel0);
            }
            Warden.applyDarknessAround(serverLevel0, Vec3.atCenterOf(this.m_58899_()), null, 40);
        }
    }

    private void playWardenReplySound(Level level0) {
        SoundEvent $$1 = (SoundEvent) SOUND_BY_LEVEL.get(this.warningLevel);
        if ($$1 != null) {
            BlockPos $$2 = this.m_58899_();
            int $$3 = $$2.m_123341_() + Mth.randomBetweenInclusive(level0.random, -10, 10);
            int $$4 = $$2.m_123342_() + Mth.randomBetweenInclusive(level0.random, -10, 10);
            int $$5 = $$2.m_123343_() + Mth.randomBetweenInclusive(level0.random, -10, 10);
            level0.playSound(null, (double) $$3, (double) $$4, (double) $$5, $$1, SoundSource.HOSTILE, 5.0F, 1.0F);
        }
    }

    private boolean trySummonWarden(ServerLevel serverLevel0) {
        return this.warningLevel < 4 ? false : SpawnUtil.trySpawnMob(EntityType.WARDEN, MobSpawnType.TRIGGERED, serverLevel0, this.m_58899_(), 20, 5, 6, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER).isPresent();
    }

    public VibrationSystem.Listener getListener() {
        return this.vibrationListener;
    }

    class VibrationUser implements VibrationSystem.User {

        private static final int LISTENER_RADIUS = 8;

        private final PositionSource positionSource = new BlockPositionSource(SculkShriekerBlockEntity.this.f_58858_);

        public VibrationUser() {
        }

        @Override
        public int getListenerRadius() {
            return 8;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.SHRIEKER_CAN_LISTEN;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel serverLevel0, BlockPos blockPos1, GameEvent gameEvent2, GameEvent.Context gameEventContext3) {
            return !(Boolean) SculkShriekerBlockEntity.this.m_58900_().m_61143_(SculkShriekerBlock.SHRIEKING) && SculkShriekerBlockEntity.tryGetPlayer(gameEventContext3.sourceEntity()) != null;
        }

        @Override
        public void onReceiveVibration(ServerLevel serverLevel0, BlockPos blockPos1, GameEvent gameEvent2, @Nullable Entity entity3, @Nullable Entity entity4, float float5) {
            SculkShriekerBlockEntity.this.tryShriek(serverLevel0, SculkShriekerBlockEntity.tryGetPlayer(entity4 != null ? entity4 : entity3));
        }

        @Override
        public void onDataChanged() {
            SculkShriekerBlockEntity.this.m_6596_();
        }

        @Override
        public boolean requiresAdjacentChunksToBeTicking() {
            return true;
        }
    }
}