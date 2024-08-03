package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.particle.VaporParticleOptions;
import com.rekindled.embers.recipe.IGaseousFuelRecipe;
import com.rekindled.embers.upgrade.WildfireStirlingUpgrade;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.joml.Vector3f;

public class WildfireStirlingBlockEntity extends BlockEntity implements IExtraCapabilityInformation {

    public int activeTicks = 0;

    public int burnTime = 0;

    public WildfireStirlingUpgrade upgrade;

    public FluidTank tank = new FluidTank(4000) {

        @Override
        public void onContentsChanged() {
            WildfireStirlingBlockEntity.this.setChanged();
        }
    };

    private static Random random = new Random();

    public IGaseousFuelRecipe cachedRecipe = null;

    public LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> this.tank);

    public WildfireStirlingBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.WILDFIRE_STIRLING_ENTITY.get(), pPos, pBlockState);
        this.upgrade = new WildfireStirlingUpgrade(this);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.tank.readFromNBT(nbt.getCompound("tank"));
        this.activeTicks = nbt.getInt("active");
        this.burnTime = nbt.getInt("burnTime");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("tank", this.tank.writeToNBT(new CompoundTag()));
        nbt.putInt("active", this.activeTicks);
        nbt.putInt("burnTime", this.burnTime);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.put("tank", this.tank.writeToNBT(new CompoundTag()));
        nbt.putInt("active", this.activeTicks);
        nbt.putInt("burnTime", this.burnTime);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void setActive(int ticks) {
        this.activeTicks = Math.max(ticks, this.activeTicks);
        this.setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, WildfireStirlingBlockEntity blockEntity) {
        blockEntity.activeTicks--;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, WildfireStirlingBlockEntity blockEntity) {
        blockEntity.activeTicks--;
        if (blockEntity.activeTicks > 0 && state.m_61138_(BlockStateProperties.FACING)) {
            Direction facing = ((Direction) state.m_61143_(BlockStateProperties.FACING)).getOpposite();
            float frontoffset = -0.6F;
            float yoffset = 0.2F;
            float wideoffset = 0.5F;
            float breadthoffset = 0.4F;
            Vec3 frontOffset = new Vec3(0.5 - (double) ((float) facing.getNormal().getX() * frontoffset), 0.5 - (double) ((float) facing.getNormal().getY() * frontoffset), 0.5 - (double) ((float) facing.getNormal().getZ() * frontoffset));
            Vec3 baseOffset = new Vec3(0.5 - (double) ((float) facing.getNormal().getX() * yoffset), 0.5 - (double) ((float) facing.getNormal().getY() * yoffset), 0.5 - (double) ((float) facing.getNormal().getZ() * yoffset));
            Direction[] planars = switch(facing.getAxis()) {
                case X ->
                    new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH };
                case Y ->
                    new Direction[] { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH };
                case Z ->
                    new Direction[] { Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST };
                default ->
                    null;
            };
            Vector3f color = new Vector3f(1.0F, 0.2509804F, 0.0627451F);
            for (Direction planar : planars) {
                BlockState sideState = level.getBlockState(pos.relative(planar));
                if (sideState.m_60655_(level, pos.relative(planar), planar.getOpposite()).isEmpty()) {
                    Direction cross = facing.getClockWise(planar.getAxis());
                    float x1 = (float) pos.m_123341_() + (float) baseOffset.x + (float) planar.getNormal().getX() * wideoffset;
                    float y1 = (float) pos.m_123342_() + (float) baseOffset.y + (float) planar.getNormal().getY() * wideoffset;
                    float z1 = (float) pos.m_123343_() + (float) baseOffset.z + (float) planar.getNormal().getZ() * wideoffset;
                    float x2 = (float) pos.m_123341_() + (float) frontOffset.x + (float) planar.getNormal().getX() * wideoffset + (float) cross.getNormal().getX() * (random.nextFloat() - 0.5F) * 2.0F * breadthoffset;
                    float y2 = (float) pos.m_123342_() + (float) frontOffset.y + (float) planar.getNormal().getY() * wideoffset + (float) cross.getNormal().getY() * (random.nextFloat() - 0.5F) * 2.0F * breadthoffset;
                    float z2 = (float) pos.m_123343_() + (float) frontOffset.z + (float) planar.getNormal().getZ() * wideoffset + (float) cross.getNormal().getZ() * (random.nextFloat() - 0.5F) * 2.0F * breadthoffset;
                    int lifetime = 24 + random.nextInt(8);
                    float motionx = (x2 - x1) / (float) lifetime;
                    float motiony = (y2 - y1) / (float) lifetime;
                    float motionz = (z2 - z1) / (float) lifetime;
                    level.addParticle(new VaporParticleOptions(color, new Vec3((double) motionx, (double) motiony, (double) motionz), (float) lifetime / 16.0F), (double) x1, (double) y1, (double) z1, 0.0, 0.0, 0.0);
                }
            }
            float x = (float) pos.m_123341_() + (float) frontOffset.x;
            float y = (float) pos.m_123342_() + (float) frontOffset.y;
            float z = (float) pos.m_123343_() + (float) frontOffset.z;
            int lifetime = 16 + random.nextInt(16);
            float motionx = (float) (Math.abs(facing.getNormal().getX()) - 1) * (random.nextFloat() - 0.5F) * 2.0F * wideoffset / (float) lifetime;
            float motiony = (float) (Math.abs(facing.getNormal().getY()) - 1) * (random.nextFloat() - 0.5F) * 2.0F * wideoffset / (float) lifetime;
            float motionz = (float) (Math.abs(facing.getNormal().getZ()) - 1) * (random.nextFloat() - 0.5F) * 2.0F * wideoffset / (float) lifetime;
            level.addParticle(new VaporParticleOptions(color, new Vec3((double) motionx, (double) motiony, (double) motionz), (float) lifetime / 16.0F), (double) x, (double) y, (double) z, 0.0, 0.0, 0.0);
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_ && this.f_58857_.getBlockState(this.f_58858_).m_61138_(BlockStateProperties.FACING)) {
            Direction facing = (Direction) this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.FACING);
            if (cap == ForgeCapabilities.FLUID_HANDLER && (side == null || side == facing)) {
                return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, this.holder);
            }
            if (cap == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && (side == null || side.getOpposite() == facing)) {
                return this.upgrade.getCapability(cap, side);
            }
        }
        return super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
        this.upgrade.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.FLUID_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.fluid", Component.translatable("embers.tooltip.goggles.fluid.steam")));
        }
    }
}