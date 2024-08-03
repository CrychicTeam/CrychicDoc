package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class HologramProjectorBlockEntity extends BlockEntity {

    public int tickCount;

    private EntityType entityType;

    private CompoundTag entityTag;

    private Entity displayEntity;

    private Entity prevDisplayEntity;

    private UUID displayUUID;

    private UUID prevDisplayUUID;

    private float prevSwitchProgress;

    private float switchProgress;

    private float previousRotation;

    private float rotation;

    private UUID lastPlayerUUID;

    public HologramProjectorBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntityRegistry.HOLOGRAM_PROJECTOR.get(), pos, state);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, HologramProjectorBlockEntity entity) {
        entity.tickCount++;
        entity.prevSwitchProgress = entity.switchProgress;
        entity.previousRotation = entity.rotation;
        if (entity.prevDisplayUUID != entity.displayUUID) {
            if (entity.switchProgress < 10.0F) {
                if (entity.switchProgress == 0.0F) {
                    level.m_247517_((Player) null, blockPos, ACSoundRegistry.HOLOGRAM_STOP.get(), SoundSource.BLOCKS);
                }
                entity.switchProgress++;
            } else {
                entity.prevDisplayEntity = entity.displayEntity;
                entity.prevDisplayUUID = entity.displayUUID;
                entity.markUpdated();
            }
            if (!entity.m_58901_() && level.isClientSide) {
                AlexsCaves.PROXY.playWorldSound(entity, (byte) 3);
            }
        }
        if (entity.isPlayerRender() && entity.lastPlayerUUID == null) {
            entity.lastPlayerUUID = entity.entityTag.getUUID("UUID");
            entity.markUpdated();
        }
        if (entity.isPlayerRender()) {
            if (entity.lastPlayerUUID == null || entity.displayUUID == null || !entity.lastPlayerUUID.equals(entity.displayUUID)) {
                entity.displayUUID = entity.lastPlayerUUID;
                entity.switchProgress = 0.0F;
            }
        } else if (entity.displayEntity != null && entity.displayUUID != entity.displayEntity.getUUID()) {
            entity.displayUUID = entity.displayEntity.getUUID();
            entity.switchProgress = 0.0F;
        }
        float redstoneSignal = (float) level.m_277086_(blockPos) * 1.0F;
        if (redstoneSignal > 0.0F) {
            entity.rotation += redstoneSignal;
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("EntityType")) {
            String str = tag.getString("EntityType");
            this.entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(str));
        }
        if (tag.contains("EntityTag")) {
            this.entityTag = tag.getCompound("EntityTag");
        }
        this.rotation = tag.getFloat("Rotation");
        if (tag.contains("LastPlayerUUID")) {
            this.lastPlayerUUID = tag.getUUID("LastPlayerUUID");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.entityType != null) {
            tag.putString("EntityType", ForgeRegistries.ENTITY_TYPES.getKey(this.entityType).toString());
        }
        if (this.entityTag != null) {
            tag.put("EntityTag", this.entityTag);
        }
        tag.putFloat("Rotation", this.rotation);
        if (this.lastPlayerUUID != null) {
            tag.putUUID("LastPlayerUUID", this.lastPlayerUUID);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        BlockPos pos = this.m_58899_();
        float f = this.displayEntity == null ? 1.0F : Math.max(this.displayEntity.getBbWidth(), this.displayEntity.getBbHeight());
        return new AABB(pos.offset(-1, -1, -1), pos.offset(2, 2, 2)).inflate((double) Math.max(f - 0.5F, 1.0F));
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        if (packet != null && packet.getTag() != null) {
            if (packet.getTag().contains("EntityType")) {
                String str = packet.getTag().getString("EntityType");
                this.entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(str));
            }
            this.entityTag = packet.getTag().getCompound("EntityTag");
            this.rotation = packet.getTag().getFloat("Rotation");
            if (packet.getTag().contains("LastPlayerUUID")) {
                this.lastPlayerUUID = packet.getTag().getUUID("LastPlayerUUID");
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        if (this.entityType != null) {
            compoundtag.putString("EntityType", ForgeRegistries.ENTITY_TYPES.getKey(this.entityType).toString());
        }
        if (this.entityTag != null) {
            compoundtag.put("EntityTag", this.entityTag);
        }
        compoundtag.putFloat("Rotation", this.rotation);
        if (this.lastPlayerUUID != null) {
            compoundtag.putUUID("LastPlayerUUID", this.lastPlayerUUID);
        }
        return compoundtag;
    }

    public void setEntity(EntityType entityType, CompoundTag entityTag, float playerRot) {
        this.entityType = entityType;
        this.entityTag = entityTag;
        this.rotation = playerRot;
        this.displayEntity = null;
        this.lastPlayerUUID = null;
    }

    public boolean isPlayerRender() {
        return this.entityType == EntityType.PLAYER;
    }

    public UUID getLastPlayerUUID() {
        return this.isPlayerRender() ? this.lastPlayerUUID : null;
    }

    public Entity getDisplayEntity(Level level) {
        if (this.isPlayerRender()) {
            return null;
        } else {
            if (this.displayEntity == null && this.entityType != null) {
                this.displayEntity = EntityType.loadEntityRecursive(this.entityTag, level, Function.identity());
            }
            return this.displayEntity == null && this.prevDisplayEntity != null ? this.prevDisplayEntity : this.displayEntity;
        }
    }

    public float getSwitchAmount(float partialTicks) {
        return (this.prevSwitchProgress + (this.switchProgress - this.prevSwitchProgress) * partialTicks) * 0.1F;
    }

    public float getRotation(float partialTicks) {
        return this.previousRotation + (this.rotation - this.previousRotation) * partialTicks;
    }

    private void markUpdated() {
        this.m_6596_();
        this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
    }

    @Override
    public void setRemoved() {
        AlexsCaves.PROXY.clearSoundCacheFor(this);
        this.f_58857_.m_247517_((Player) null, this.m_58899_(), ACSoundRegistry.HOLOGRAM_STOP.get(), SoundSource.BLOCKS);
        super.setRemoved();
    }
}