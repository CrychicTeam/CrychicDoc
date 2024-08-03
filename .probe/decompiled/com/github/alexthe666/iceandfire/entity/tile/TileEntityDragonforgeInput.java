package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.block.BlockDragonforgeInput;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class TileEntityDragonforgeInput extends BlockEntity {

    private static final int LURE_DISTANCE = 50;

    private static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    private int ticksSinceDragonFire;

    private TileEntityDragonforge core = null;

    public TileEntityDragonforgeInput(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_INPUT.get(), pos, state);
    }

    public void onHitWithFlame() {
        if (this.core != null) {
            this.core.transferPower(1);
        }
    }

    public static void tick(Level level, BlockPos position, BlockState state, TileEntityDragonforgeInput forgeInput) {
        if (forgeInput.core == null) {
            forgeInput.core = forgeInput.getConnectedTileEntity(position);
        }
        if (forgeInput.ticksSinceDragonFire > 0) {
            forgeInput.ticksSinceDragonFire--;
        }
        if ((forgeInput.ticksSinceDragonFire == 0 || forgeInput.core == null) && forgeInput.isActive()) {
            BlockEntity tileentity = level.getBlockEntity(position);
            level.setBlockAndUpdate(position, forgeInput.getDeactivatedState());
            if (tileentity != null) {
                tileentity.clearRemoved();
                level.setBlockEntity(tileentity);
            }
        }
        if (forgeInput.isAssembled()) {
            forgeInput.lureDragons();
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        this.m_142466_(packet.getTag());
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187480_();
    }

    protected void lureDragons() {
        Vec3 targetPosition = new Vec3((double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + 0.5F), (double) ((float) this.m_58899_().m_123343_() + 0.5F));
        AABB searchArea = new AABB((double) this.f_58858_.m_123341_() - 50.0, (double) this.f_58858_.m_123342_() - 50.0, (double) this.f_58858_.m_123343_() - 50.0, (double) this.f_58858_.m_123341_() + 50.0, (double) this.f_58858_.m_123342_() + 50.0, (double) this.f_58858_.m_123343_() + 50.0);
        boolean dragonSelected = false;
        for (EntityDragonBase dragon : this.f_58857_.m_45976_(EntityDragonBase.class, searchArea)) {
            if (!dragonSelected && this.getDragonType() == dragon.dragonType.getIntFromType() && (dragon.isChained() || dragon.m_21824_()) && this.canSeeInput(dragon, targetPosition)) {
                dragon.burningTarget = this.f_58858_;
                dragonSelected = true;
            } else if (dragon.burningTarget == this.f_58858_) {
                dragon.burningTarget = null;
                dragon.setBreathingFire(false);
            }
        }
    }

    public boolean isAssembled() {
        return this.core != null && this.core.assembled() && this.core.canSmelt();
    }

    public void resetCore() {
        this.core = null;
    }

    private boolean canSeeInput(EntityDragonBase dragon, Vec3 target) {
        if (target != null) {
            HitResult rayTrace = this.f_58857_.m_45547_(new ClipContext(dragon.getHeadPosition(), target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, dragon));
            double distance = dragon.getHeadPosition().distanceTo(rayTrace.getLocation());
            return distance < (double) (10.0F + dragon.m_20205_());
        } else {
            return false;
        }
    }

    private BlockState getDeactivatedState() {
        return switch(this.getDragonType()) {
            case 0 ->
                (BlockState) IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get().defaultBlockState().m_61124_(BlockDragonforgeInput.ACTIVE, false);
            case 1 ->
                (BlockState) IafBlockRegistry.DRAGONFORGE_ICE_INPUT.get().defaultBlockState().m_61124_(BlockDragonforgeInput.ACTIVE, false);
            case 2 ->
                (BlockState) IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.get().defaultBlockState().m_61124_(BlockDragonforgeInput.ACTIVE, false);
            default ->
                (BlockState) IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get().defaultBlockState().m_61124_(BlockDragonforgeInput.ACTIVE, false);
        };
    }

    private int getDragonType() {
        BlockState state = this.f_58857_.getBlockState(this.f_58858_);
        if (state.m_60734_() == IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get()) {
            return 0;
        } else if (state.m_60734_() == IafBlockRegistry.DRAGONFORGE_ICE_INPUT.get()) {
            return 1;
        } else {
            return state.m_60734_() == IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.get() ? 2 : 0;
        }
    }

    private boolean isActive() {
        BlockState state = this.f_58857_.getBlockState(this.f_58858_);
        return state.m_60734_() instanceof BlockDragonforgeInput && (Boolean) state.m_61143_(BlockDragonforgeInput.ACTIVE);
    }

    private TileEntityDragonforge getConnectedTileEntity(BlockPos position) {
        for (Direction facing : HORIZONTALS) {
            BlockEntity var7 = this.f_58857_.getBlockEntity(position.relative(facing));
            if (var7 instanceof TileEntityDragonforge) {
                return (TileEntityDragonforge) var7;
            }
        }
        return null;
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return this.core != null && capability == ForgeCapabilities.ITEM_HANDLER ? this.core.getCapability(capability, facing) : super.getCapability(capability, facing);
    }
}