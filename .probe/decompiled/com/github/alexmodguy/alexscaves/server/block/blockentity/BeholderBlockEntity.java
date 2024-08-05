package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.BeholderEyeEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BeholderBlockEntity extends BlockEntity {

    private int prevUsingEntityId = -1;

    private int currentlyUsingEntityId = -1;

    private float eyeYRot;

    private float prevEyeYRot;

    private float eyeXRot;

    private float prevEyeXRot;

    public int age;

    public int soundCooldown = 40;

    public BeholderBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntityRegistry.BEHOLDER.get(), pos, state);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, BeholderBlockEntity entity) {
        entity.prevEyeXRot = entity.eyeXRot;
        entity.prevEyeYRot = entity.eyeYRot;
        entity.age++;
        Entity currentlyUsing = entity.getUsingEntity();
        if (currentlyUsing == null) {
            entity.eyeXRot = Mth.approach(entity.eyeXRot, 0.0F, 10.0F);
            entity.eyeYRot++;
        } else {
            entity.eyeXRot = Mth.approach(entity.eyeXRot, currentlyUsing.getXRot(), 10.0F);
            entity.eyeYRot = Mth.approach(entity.eyeYRot, currentlyUsing.getYRot(), 10.0F);
        }
        if (entity.soundCooldown-- <= 0) {
            entity.soundCooldown = level.random.nextInt(100) + 100;
            Vec3 vec3 = entity.m_58899_().getCenter();
            level.playSound((Player) null, vec3.x, vec3.y, vec3.z, entity.currentlyUsingEntityId == -1 ? ACSoundRegistry.BEHOLDER_IDLE.get() : ACSoundRegistry.BEHOLDER_VIEW_IDLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        if (!level.isClientSide && entity.prevUsingEntityId != entity.currentlyUsingEntityId) {
            level.sendBlockUpdated(entity.m_58899_(), entity.m_58900_(), entity.m_58900_(), 2);
            entity.prevUsingEntityId = entity.currentlyUsingEntityId;
        }
    }

    public float getEyeXRot(float partialTicks) {
        return this.prevEyeXRot + (this.eyeXRot - this.prevEyeXRot) * partialTicks;
    }

    public Entity getUsingEntity() {
        return this.currentlyUsingEntityId == -1 ? null : this.f_58857_.getEntity(this.currentlyUsingEntityId);
    }

    public float getEyeYRot(float partialTicks) {
        return this.prevEyeYRot + (this.eyeYRot - this.prevEyeYRot) * partialTicks;
    }

    public void startObserving(Level level, Player player) {
        BeholderEyeEntity beholderEyeEntity = ACEntityRegistry.BEHOLDER_EYE.get().create(level);
        double dist = Math.sqrt(this.m_58899_().m_123331_(player.m_20183_()));
        if (dist > 1000.0) {
            ACAdvancementTriggerRegistry.BEHOLDER_FAR_AWAY.triggerForEntity(player);
        }
        Vec3 vec = this.m_58899_().getCenter().add(0.0, -0.15, 0.0);
        beholderEyeEntity.m_146884_(vec);
        beholderEyeEntity.setUsingPlayerUUID(player.m_20148_());
        beholderEyeEntity.m_146922_(player.m_146908_());
        level.m_7967_(beholderEyeEntity);
        this.currentlyUsingEntityId = beholderEyeEntity.m_19879_();
        player.displayClientMessage(Component.translatable("item.alexscaves.occult_gem.start_observing"), true);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.currentlyUsingEntityId = tag.getInt("UsingEntityID");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("UsingEntityID", this.currentlyUsingEntityId);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        if (packet != null && packet.getTag() != null) {
            this.currentlyUsingEntityId = packet.getTag().getInt("UsingEntityID");
        }
    }

    public boolean isFirstPersonView(Entity cameraEntity) {
        return cameraEntity != null && cameraEntity instanceof BeholderEyeEntity && cameraEntity.blockPosition().equals(this.m_58899_());
    }
}