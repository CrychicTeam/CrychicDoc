package com.rekindled.embers.blockentity;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.particle.VaporParticleOptions;
import com.rekindled.embers.upgrade.GeologicSeparatorUpgrade;
import com.rekindled.embers.util.Misc;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.joml.Vector3f;

public class GeologicSeparatorBlockEntity extends OpenTankBlockEntity implements IExtraCapabilityInformation {

    int ticksExisted = 0;

    public float renderOffset;

    int previousFluid;

    Random random = new Random();

    protected GeologicSeparatorUpgrade upgrade;

    public GeologicSeparatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.GEOLOGIC_SEPARATOR_ENTITY.get(), pPos, pBlockState);
        this.tank = new FluidTank(ConfigManager.GEO_SEPARATOR_CAPACITY.get()) {

            @Override
            public void onContentsChanged() {
                GeologicSeparatorBlockEntity.this.m_6596_();
            }

            @Override
            public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
                if (Misc.isGaseousFluid(resource)) {
                    GeologicSeparatorBlockEntity.this.setEscapedFluid(resource);
                    return resource.getAmount();
                } else {
                    return super.fill(resource, action);
                }
            }
        };
        this.upgrade = new GeologicSeparatorUpgrade(this);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (this.f_58859_ || cap != EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY || side != null && this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.HORIZONTAL_FACING) != side) {
            return side == Direction.UP ? LazyOptional.empty() : super.getCapability(cap, side);
        } else {
            return this.upgrade.getCapability(cap, side);
        }
    }

    public int getCapacity() {
        return this.tank.getCapacity();
    }

    public FluidStack getFluidStack() {
        return this.tank.getFluid();
    }

    public FluidTank getTank() {
        return this.tank;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, GeologicSeparatorBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        if (blockEntity.ticksExisted == 1) {
            blockEntity.previousFluid = blockEntity.tank.getFluidAmount();
        }
        if (blockEntity.tank.getFluidAmount() != blockEntity.previousFluid) {
            blockEntity.renderOffset = blockEntity.renderOffset + (float) blockEntity.tank.getFluidAmount() - (float) blockEntity.previousFluid;
            blockEntity.previousFluid = blockEntity.tank.getFluidAmount();
        }
        if (blockEntity.shouldEmitParticles()) {
            blockEntity.updateEscapeParticles();
        }
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.FLUID_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.OUTPUT, "embers.tooltip.goggles.fluid", Component.translatable("embers.tooltip.goggles.fluid.metal")));
        }
    }

    @Override
    protected void updateEscapeParticles() {
        Vector3f color = IClientFluidTypeExtensions.of(this.lastEscaped.getFluid().getFluidType()).modifyFogColor(Minecraft.getInstance().gameRenderer.getMainCamera(), 0.0F, (ClientLevel) this.f_58857_, 6, 0.0F, new Vector3f(1.0F, 1.0F, 1.0F));
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            float xOffset = 0.5F + (random.nextFloat() - 0.5F) * 2.0F * 0.2F;
            float yOffset = 0.4F;
            float zOffset = 0.5F + (random.nextFloat() - 0.5F) * 2.0F * 0.2F;
            this.f_58857_.addParticle(new VaporParticleOptions(color, 2.0F), (double) ((float) this.f_58858_.m_123341_() + xOffset), (double) ((float) this.f_58858_.m_123342_() + yOffset), (double) ((float) this.f_58858_.m_123343_() + zOffset), 0.0, 0.2F, 0.0);
        }
    }
}