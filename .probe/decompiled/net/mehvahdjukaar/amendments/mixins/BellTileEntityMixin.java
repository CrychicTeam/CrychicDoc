package net.mehvahdjukaar.amendments.mixins;

import net.mehvahdjukaar.amendments.common.IBellConnections;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ BellBlockEntity.class })
public abstract class BellTileEntityMixin extends BlockEntity implements IBellConnections {

    @Unique
    public IBellConnections.BellConnection amendments$connection = IBellConnections.BellConnection.NONE;

    protected BellTileEntityMixin(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    @Override
    public IBellConnections.BellConnection amendments$getConnected() {
        return this.amendments$connection;
    }

    @Override
    public void amendments$setConnected(IBellConnections.BellConnection con) {
        this.amendments$connection = con;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.amendments$connection != null) {
            compound.putInt("Connection", this.amendments$connection.ordinal());
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("Connection")) {
            this.amendments$connection = IBellConnections.BellConnection.values()[compound.getInt("Connection")];
        } else {
            this.amendments$connection = IBellConnections.BellConnection.NONE;
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_);
    }
}