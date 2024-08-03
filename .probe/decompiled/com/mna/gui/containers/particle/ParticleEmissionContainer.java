package com.mna.gui.containers.particle;

import com.mna.ManaAndArtifice;
import com.mna.blocks.tileentities.ParticleEmitterTile;
import com.mna.capabilities.particles.ParticleAuraProvider;
import com.mna.gui.containers.ContainerInit;
import com.mna.network.ClientMessageDispatcher;
import com.mna.particles.emitter.EmitterData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ParticleEmissionContainer extends AbstractContainerMenu {

    private final EmitterData _emitterData;

    private final ParticleEmitterTile _tile;

    public ParticleEmissionContainer(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        this(id, playerInv, EmitterData.fromTag(buffer.readAnySizeNbt()), BlockPos.of(buffer.readLong()));
    }

    public ParticleEmissionContainer(int id, Inventory playerInventoryIn, EmitterData data, BlockPos pos) {
        super(ContainerInit.PARTICLE_EMITTER.get(), id);
        this._emitterData = data;
        if (pos.equals(BlockPos.ZERO)) {
            this._tile = null;
        } else {
            BlockEntity te = playerInventoryIn.player.m_9236_().getBlockEntity(pos);
            if (te != null && te instanceof ParticleEmitterTile) {
                this._tile = (ParticleEmitterTile) te;
            } else {
                this._tile = null;
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public boolean isForPlayer() {
        return this._tile == null;
    }

    public void update(boolean sync) {
        if (this._tile == null) {
            Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
            if (player != null) {
                player.getCapability(ParticleAuraProvider.AURA).ifPresent(a -> a.load(this._emitterData.getTag()));
            }
            if (sync) {
                ClientMessageDispatcher.sendAuraSyncMessage();
            }
        } else {
            this._tile.setData(this._emitterData.getTag());
            if (sync) {
                ClientMessageDispatcher.sendAuraSyncMessage(this._tile);
            }
        }
    }

    public EmitterData getData() {
        return this._emitterData;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }
}