package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.server.block.SirenLightBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SirenLightBlockEntity extends BlockEntity {

    private float onProgress;

    private float prevOnProgress;

    private float sirenRotation;

    private float prevSirenRotation;

    private int color = -1;

    public SirenLightBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntityRegistry.SIREN_LIGHT.get(), pos, state);
        if ((Boolean) state.m_61143_(SirenLightBlock.POWERED)) {
            this.prevOnProgress = this.onProgress = 10.0F;
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, SirenLightBlockEntity entity) {
        entity.prevOnProgress = entity.onProgress;
        entity.prevSirenRotation = entity.sirenRotation;
        boolean powered = (Boolean) state.m_61143_(SirenLightBlock.POWERED);
        if (powered && entity.onProgress < 10.0F) {
            entity.onProgress++;
        } else if (!powered && entity.onProgress > 0.0F) {
            entity.onProgress--;
        }
        if (powered) {
            entity.sirenRotation = entity.sirenRotation + entity.onProgress * 2.0F + 0.25F;
        }
    }

    public float getOnProgress(float partialTicks) {
        return (this.prevOnProgress + (this.onProgress - this.prevOnProgress) * partialTicks) * 0.1F;
    }

    public float getSirenRotation(float partialTicks) {
        return this.prevSirenRotation + (this.sirenRotation - this.prevSirenRotation) * partialTicks;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.color = tag.getInt("Color");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Color", this.color);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        if (packet != null && packet.getTag() != null) {
            this.color = packet.getTag().getInt("Color");
        }
    }

    public void setColor(int setTo) {
        this.color = setTo;
        this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 2);
    }

    public int getColor() {
        return this.color < 0 ? 65280 : this.color;
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        BlockPos pos = this.m_58899_();
        return new AABB(pos.offset(-3, -3, -3), pos.offset(4, 4, 4));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }
}