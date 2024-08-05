package net.mehvahdjukaar.amendments.common.tile;

import java.util.UUID;
import net.mehvahdjukaar.amendments.common.block.WallLanternBlock;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.ThinAirCompat;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.block.IBlockHolder;
import net.mehvahdjukaar.moonlight.api.block.IOwnerProtected;
import net.mehvahdjukaar.moonlight.api.block.MimicBlockTile;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.IExtraModelDataProvider;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class WallLanternBlockTile extends SwayingBlockTile implements IBlockHolder, IOwnerProtected, IExtraModelDataProvider {

    public static final ModelDataKey<BlockState> MIMIC_KEY = MimicBlockTile.MIMIC_KEY;

    private BlockState mimic = Blocks.LANTERN.defaultBlockState();

    protected double attachmentOffset = 0.0;

    protected boolean isRedstoneLantern = false;

    private UUID owner = null;

    public WallLanternBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.WALL_LANTERN_TILE.get(), pos, state);
    }

    public boolean isRedstoneLantern() {
        return this.isRedstoneLantern;
    }

    public double getAttachmentOffset() {
        return this.attachmentOffset;
    }

    @Override
    public Vector3f getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(WallLanternBlock.FACING)).step();
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        super.addExtraModelData(builder);
        builder.with(MIMIC_KEY, this.getHeldBlock());
    }

    @Override
    public void load(CompoundTag compound) {
        super.m_142466_(compound);
        this.setHeldBlock(Utils.readBlockState(compound.getCompound("Lantern"), this.f_58857_));
        this.isRedstoneLantern = compound.getBoolean("IsRedstone");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.m_183515_(compound);
        compound.put("Lantern", NbtUtils.writeBlockState(this.mimic));
        compound.putBoolean("IsRedstone", this.isRedstoneLantern);
    }

    @Override
    public BlockState getHeldBlock(int index) {
        return this.mimic;
    }

    @Override
    public boolean setHeldBlock(BlockState state, int index) {
        if (state.m_61138_(LanternBlock.HANGING)) {
            state = (BlockState) state.m_61124_(LanternBlock.HANGING, false);
        }
        if (CompatHandler.THIN_AIR && this.f_58857_ != null && ThinAirCompat.isAirLantern(state)) {
            BlockState newState = ThinAirCompat.maybeSetAirQuality(state, Vec3.atCenterOf(this.f_58858_), this.f_58857_);
            if (newState != null) {
                state = newState;
            }
            this.f_58857_.m_186464_(this.f_58858_, this.m_58900_().m_60734_(), 20, TickPriority.NORMAL);
        }
        this.mimic = state;
        int light = ForgeHelper.getLightEmission(state, this.f_58857_, this.f_58858_);
        boolean lit = true;
        ResourceLocation res = Utils.getID(this.mimic.m_60734_());
        if (res.toString().equals("charm:redstone_lantern")) {
            this.isRedstoneLantern = true;
            light = 15;
            lit = false;
        }
        if (this.f_58857_ != null && !this.mimic.m_60795_()) {
            VoxelShape shape = state.m_60808_(this.f_58857_, this.f_58858_);
            if (!shape.isEmpty() && !res.getNamespace().equals("twigs")) {
                this.attachmentOffset = shape.bounds().maxY - 0.5625;
            }
            if ((Integer) this.m_58900_().m_61143_(WallLanternBlock.LIGHT_LEVEL) != light) {
                if (light == 0) {
                    lit = false;
                }
                BlockState newState = (BlockState) ((BlockState) this.m_58900_().m_61124_(WallLanternBlock.LIT, lit)).m_61124_(WallLanternBlock.LIGHT_LEVEL, Math.max(light, 5));
                this.m_58904_().setBlock(this.f_58858_, newState, 20);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }
}