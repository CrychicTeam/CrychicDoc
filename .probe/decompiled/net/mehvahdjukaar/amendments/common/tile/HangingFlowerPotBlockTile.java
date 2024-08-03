package net.mehvahdjukaar.amendments.common.tile;

import java.util.UUID;
import net.mehvahdjukaar.amendments.common.block.HangingFlowerPotBlock;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.block.IOwnerProtected;
import net.mehvahdjukaar.moonlight.api.block.MimicBlockTile;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class HangingFlowerPotBlockTile extends MimicBlockTile implements IOwnerProtected {

    private UUID owner = null;

    public HangingFlowerPotBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.HANGING_FLOWER_POT_TILE.get(), pos, state);
    }

    @Override
    public boolean setHeldBlock(BlockState state) {
        super.setHeldBlock(state);
        if (this.f_58857_ instanceof ServerLevel) {
            int newLight = ForgeHelper.getLightEmission(this.getHeldBlock(), this.f_58857_, this.f_58858_);
            this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(HangingFlowerPotBlock.LIGHT_LEVEL, newLight), 3);
        }
        return true;
    }

    @Nullable
    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.saveOwner(tag);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.loadOwner(compound);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_);
    }
}