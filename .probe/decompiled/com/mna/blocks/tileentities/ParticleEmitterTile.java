package com.mna.blocks.tileentities;

import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.gui.containers.particle.ParticleEmissionContainer;
import com.mna.particles.emitter.EmitterData;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ParticleEmitterTile extends BlockEntity implements MenuProvider, Consumer<FriendlyByteBuf> {

    private EmitterData _data = new EmitterData();

    public ParticleEmitterTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.PARTICLE_EMITTER.get(), pos, state);
    }

    public static void ClientTick(Level level, BlockPos pos, BlockState state, ParticleEmitterTile tile) {
        if (!level.m_276867_(pos)) {
            tile._data.spawn(level, Vec3.atCenterOf(pos), new Vec3(1.0, 0.0, 0.0));
        }
    }

    public EmitterData getData() {
        return this._data;
    }

    public void setData(CompoundTag data) {
        this._data = EmitterData.fromTag(data);
        this.m_6596_();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag base = super.getUpdateTag();
        base.put("data", this._data.getTag());
        return base;
    }

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        this._data = EmitterData.fromTag(tag.getCompound("data"));
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag data = pkt.getTag();
        this._data = EmitterData.fromTag(data.getCompound("data"));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.put("data", this._data.getTag());
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("data")) {
            this._data = EmitterData.fromTag(compound.getCompound("data"));
        }
        super.load(compound);
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ParticleEmissionContainer(i, inventory, this._data, this.m_58899_());
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }

    public void accept(FriendlyByteBuf t) {
        t.writeNbt(this._data.getTag());
        t.writeLong(this.m_58899_().asLong());
    }
}