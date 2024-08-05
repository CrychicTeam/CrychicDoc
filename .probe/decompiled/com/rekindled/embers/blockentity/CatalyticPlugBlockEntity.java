package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.VaporParticleOptions;
import com.rekindled.embers.recipe.IGaseousFuelRecipe;
import com.rekindled.embers.upgrade.CatalyticPlugUpgrade;
import com.rekindled.embers.util.sound.ISoundController;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.joml.Vector3f;

public class CatalyticPlugBlockEntity extends BlockEntity implements ISoundController, IExtraCapabilityInformation {

    public static final int SOUND_OFF = 1;

    public static final int SOUND_ON = 2;

    public static final int[] SOUND_IDS = new int[] { 1, 2 };

    int ticksExisted = 0;

    public float renderOffset;

    int previousFluid;

    public int activeTicks = 0;

    public int burnTime = 0;

    public CatalyticPlugUpgrade upgrade;

    public FluidTank tank = new FluidTank(4000) {

        @Override
        public void onContentsChanged() {
            CatalyticPlugBlockEntity.this.setChanged();
        }
    };

    private static Random random = new Random();

    public IGaseousFuelRecipe cachedRecipe = null;

    HashSet<Integer> soundsPlaying = new HashSet();

    public LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> this.tank);

    public CatalyticPlugBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.CATALYTIC_PLUG_ENTITY.get(), pPos, pBlockState);
        this.upgrade = new CatalyticPlugUpgrade(this);
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

    public static void serverTick(Level level, BlockPos pos, BlockState state, CatalyticPlugBlockEntity blockEntity) {
        blockEntity.activeTicks--;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CatalyticPlugBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        if (blockEntity.ticksExisted == 1) {
            blockEntity.previousFluid = blockEntity.tank.getFluidAmount();
        }
        if (blockEntity.tank.getFluidAmount() != blockEntity.previousFluid) {
            blockEntity.renderOffset = blockEntity.renderOffset + (float) blockEntity.tank.getFluidAmount() - (float) blockEntity.previousFluid;
            blockEntity.previousFluid = blockEntity.tank.getFluidAmount();
        }
        blockEntity.handleSound();
        blockEntity.activeTicks--;
        if (blockEntity.activeTicks > 0 && state.m_61138_(BlockStateProperties.FACING)) {
            Direction facing = (Direction) state.m_61143_(BlockStateProperties.FACING);
            float yoffset = 0.38F;
            float wideoffset = 0.45F;
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
            Vector3f color = IClientFluidTypeExtensions.of(blockEntity.tank.getFluid().getFluid().getFluidType()).modifyFogColor(Minecraft.getInstance().gameRenderer.getMainCamera(), 0.0F, (ClientLevel) level, 6, 0.0F, new Vector3f(1.0F, 1.0F, 1.0F));
            for (Direction planar : planars) {
                BlockState sideState = level.getBlockState(pos.relative(planar));
                if (sideState.m_60655_(level, pos.relative(planar), planar.getOpposite()).isEmpty()) {
                    float x = (float) pos.m_123341_() + (float) baseOffset.x + (float) planar.getNormal().getX() * wideoffset;
                    float y = (float) pos.m_123342_() + (float) baseOffset.y + (float) planar.getNormal().getY() * wideoffset;
                    float z = (float) pos.m_123343_() + (float) baseOffset.z + (float) planar.getNormal().getZ() * wideoffset;
                    float motionx = (float) planar.getNormal().getX() * 0.053F - (float) facing.getNormal().getX() * 0.015F - 0.01F + random.nextFloat() * 0.02F;
                    float motiony = (float) planar.getNormal().getY() * 0.053F - (float) facing.getNormal().getY() * 0.015F - 0.01F + random.nextFloat() * 0.02F;
                    float motionz = (float) planar.getNormal().getZ() * 0.053F - (float) facing.getNormal().getZ() * 0.015F - 0.01F + random.nextFloat() * 0.02F;
                    level.addParticle(new VaporParticleOptions(color, new Vec3((double) motionx, (double) motiony, (double) motionz), 1.25F), (double) x, (double) y, (double) z, 0.0, 0.0, 0.0);
                }
            }
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
    public void playSound(int id) {
        float soundX = (float) this.f_58858_.m_123341_() + 0.5F;
        float soundY = (float) this.f_58858_.m_123342_() + 0.5F;
        float soundZ = (float) this.f_58858_.m_123343_() + 0.5F;
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.CATALYTIC_PLUG_LOOP_READY.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 2:
                EmbersSounds.playMachineSound(this, 2, EmbersSounds.CATALYTIC_PLUG_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                this.f_58857_.playLocalSound((double) soundX, (double) soundY, (double) soundZ, EmbersSounds.CATALYTIC_PLUG_START.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        this.soundsPlaying.add(id);
    }

    @Override
    public void stopSound(int id) {
        if (id == 2) {
            this.f_58857_.playLocalSound(this.f_58858_, EmbersSounds.CATALYTIC_PLUG_STOP.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        this.soundsPlaying.remove(id);
    }

    @Override
    public boolean isSoundPlaying(int id) {
        return this.soundsPlaying.contains(id);
    }

    @Override
    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    @Override
    public boolean shouldPlaySound(int id) {
        boolean isWorking = this.activeTicks > 0;
        switch(id) {
            case 1:
                return !isWorking && this.tank.getFluidAmount() > 0;
            case 2:
                return isWorking;
            default:
                return false;
        }
    }

    @Override
    public float getCurrentVolume(int id, float volume) {
        boolean isWorking = this.activeTicks > 0;
        switch(id) {
            case 1:
                return !isWorking ? 1.0F : 0.0F;
            case 2:
                return isWorking ? 1.0F : 0.0F;
            default:
                return 0.0F;
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