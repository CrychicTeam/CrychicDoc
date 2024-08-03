package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.PossessesCamera;
import com.github.alexmodguy.alexscaves.server.message.BeholderSyncMessage;
import com.github.alexmodguy.alexscaves.server.message.PossessionKeyMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.BitSet;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class BeholderEyeEntity extends Entity implements PossessesCamera {

    private static final int LOAD_CHUNK_DISTANCE = 2;

    private static final EntityDataAccessor<Optional<UUID>> USING_PLAYER_ID = SynchedEntityData.defineId(BeholderEyeEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Float> Y_ROT = SynchedEntityData.defineId(BeholderEyeEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> X_ROT = SynchedEntityData.defineId(BeholderEyeEntity.class, EntityDataSerializers.FLOAT);

    private boolean loadingChunks = false;

    private boolean stopPossession;

    private boolean prevStopPossession = true;

    private float prevEyeXRot;

    private float prevEyeYRot;

    public boolean hasTakenFullControlOfCamera;

    public BeholderEyeEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public BeholderEyeEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.BEHOLDER_EYE.get(), level);
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(USING_PLAYER_ID, Optional.empty());
        this.m_20088_().define(X_ROT, 0.0F);
        this.m_20088_().define(Y_ROT, 0.0F);
    }

    @Override
    public void tick() {
        super.tick();
        Entity usingPlayer = this.getUsingPlayer();
        if (usingPlayer == null) {
            if (!this.m_9236_().isClientSide && this.loadingChunks) {
                this.loadingChunks = false;
                this.loadChunksAround(false);
            }
            if (this.f_19797_ > 3) {
                this.m_146870_();
            }
        } else {
            if (!this.m_9236_().isClientSide && this.hasTakenFullControlOfCamera && !this.loadingChunks) {
                this.loadingChunks = true;
                this.loadChunksAround(true);
            }
            if (usingPlayer instanceof LivingEntity living) {
                living.zza = 0.0F;
                living.yya = 0.0F;
                living.xxa = 0.0F;
            }
            if (this.m_9236_().isClientSide) {
                Player clientSidePlayer = AlexsCaves.PROXY.getClientSidePlayer();
                if (usingPlayer == clientSidePlayer && AlexsCaves.PROXY.isKeyDown(4)) {
                    AlexsCaves.sendMSGToServer(new PossessionKeyMessage(this.m_19879_(), usingPlayer.getId(), 0));
                }
            } else {
                if (this.prevStopPossession != this.stopPossession) {
                    this.handleCameraServerSide(usingPlayer, !this.stopPossession);
                    this.prevStopPossession = this.stopPossession;
                }
                if (this.stopPossession) {
                    this.setUsingPlayerUUID(null);
                }
                if (usingPlayer.isShiftKeyDown()) {
                    this.stopPossession = true;
                }
            }
        }
    }

    @Override
    public Vec3 getDeltaMovement() {
        return Vec3.ZERO;
    }

    @Override
    public void setDeltaMovement(Vec3 vec3) {
    }

    public void handleCameraServerSide(Entity usingPlayer, boolean turnOn) {
        if (usingPlayer.level().equals(this.m_9236_())) {
            AlexsCaves.sendMSGToAll(new BeholderSyncMessage(this.m_19879_(), turnOn));
            if (turnOn) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 77);
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 78);
            }
            if (turnOn) {
                this.hasTakenFullControlOfCamera = true;
            }
        } else {
            this.m_146870_();
        }
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        this.m_9236_().broadcastEntityEvent(this, (byte) 78);
        super.remove(removalReason);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b != 77 && b != 78) {
            super.handleEntityEvent(b);
        } else if (this.getUsingPlayer() instanceof Player player) {
            if (b == 77) {
                player.m_216990_(ACSoundRegistry.BEHOLDER_ENTER.get());
                AlexsCaves.PROXY.setRenderViewEntity(player, this);
            } else {
                player.m_216990_(ACSoundRegistry.BEHOLDER_EXIT.get());
                AlexsCaves.PROXY.resetRenderViewEntity(player);
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("UsingPlayerUUID")) {
            this.setUsingPlayerUUID(tag.getUUID("UsingPlayerUUID"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        UUID uuid1 = this.getUsingPlayerUUID();
        if (uuid1 != null) {
            tag.putUUID("UsingPlayerUUID", uuid1);
        }
    }

    public void setUsingPlayerUUID(UUID uuid) {
        this.f_19804_.set(USING_PLAYER_ID, Optional.ofNullable(uuid));
    }

    public UUID getUsingPlayerUUID() {
        return (UUID) this.f_19804_.get(USING_PLAYER_ID).orElse(null);
    }

    public Entity getUsingPlayer() {
        UUID id = this.getUsingPlayerUUID();
        if (id == null) {
            return null;
        } else {
            return (Entity) (this.m_9236_().isClientSide ? this.m_9236_().m_46003_(id) : this.m_9236_().getServer().getPlayerList().getPlayer(id));
        }
    }

    @Override
    public float getYRot() {
        return this.f_19804_.get(Y_ROT);
    }

    @Override
    public float getXRot() {
        return this.f_19804_.get(X_ROT);
    }

    @Override
    public float getViewXRot(float f) {
        return f == 1.0F ? this.getXRot() : Mth.lerp(f, this.prevEyeXRot, this.getXRot());
    }

    @Override
    public float getViewYRot(float f) {
        return f == 1.0F ? this.getYRot() : Mth.lerp(f, this.prevEyeYRot, this.getYRot());
    }

    public void setEyeYRot(float f) {
        this.f_19804_.set(Y_ROT, f);
    }

    public void setEyeXRot(float f) {
        this.f_19804_.set(X_ROT, f);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onPossessionKeyPacket(Entity keyPresser, int type) {
        Entity possessed = this.getUsingPlayer();
        if (possessed != null && possessed.equals(keyPresser)) {
            this.stopPossession = true;
        }
    }

    private void loadChunksAround(boolean load) {
        if (this.m_9236_() instanceof ServerLevel serverLevel) {
            if (this.getUsingPlayer() instanceof ServerPlayer serverPlayer) {
                ChunkPos playerChunkPos = new ChunkPos(serverPlayer.m_20183_());
                ForgeChunkManager.forceChunk(serverLevel, "alexscaves", this, playerChunkPos.x, playerChunkPos.z, load, load);
            }
            ChunkPos chunkPos = new ChunkPos(this.m_20183_());
            int dist = Math.max(2, serverLevel.getServer().getPlayerList().getViewDistance() / 2);
            for (int i = -dist; i <= dist; i++) {
                for (int j = -dist; j <= dist; j++) {
                    ForgeChunkManager.forceChunk(serverLevel, "alexscaves", this, chunkPos.x + i, chunkPos.z + j, load, load);
                    if (load && this.getUsingPlayer() instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundLevelChunkWithLightPacket(this.m_9236_().getChunk(chunkPos.x + i, chunkPos.z + j), this.m_9236_().getLightEngine(), (BitSet) null, (BitSet) null));
                    }
                }
            }
        }
    }

    @Override
    public float getPossessionStrength(float partialTicks) {
        float age = (float) this.f_19797_ + partialTicks;
        if (age > 10.0F) {
            return 0.0F;
        } else {
            float j = age / 10.0F;
            return 1.0F - j;
        }
    }

    @Override
    public boolean instant() {
        return true;
    }

    @Override
    public boolean isPossessionBreakable() {
        return true;
    }

    public void setOldRots() {
        this.prevEyeYRot = this.getYRot();
        this.prevEyeXRot = this.getXRot();
    }
}