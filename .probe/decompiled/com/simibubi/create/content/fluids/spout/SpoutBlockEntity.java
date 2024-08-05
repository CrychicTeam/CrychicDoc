package com.simibubi.create.content.fluids.spout;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;

public class SpoutBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public static final int FILLING_TIME = 20;

    protected BeltProcessingBehaviour beltProcessing;

    public int processingTicks = -1;

    public boolean sendSplash;

    public BlockSpoutingBehaviour customProcess;

    SmartFluidTankBehaviour tank;

    private boolean createdSweetRoll;

    private boolean createdHoneyApple;

    private boolean createdChocolateBerries;

    protected static int SPLASH_PARTICLE_COUNT = 20;

    public SpoutBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().expandTowards(0.0, -2.0, 0.0);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.tank = SmartFluidTankBehaviour.single(this, 1000);
        behaviours.add(this.tank);
        this.beltProcessing = new BeltProcessingBehaviour(this).whenItemEnters(this::onItemReceived).whileItemHeld(this::whenItemHeld);
        behaviours.add(this.beltProcessing);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.SPOUT, AllAdvancements.FOODS });
    }

    protected BeltProcessingBehaviour.ProcessingResult onItemReceived(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler) {
        if (handler.blockEntity.isVirtual()) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else if (!FillingBySpout.canItemBeFilled(this.f_58857_, transported.stack)) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else if (this.tank.isEmpty()) {
            return BeltProcessingBehaviour.ProcessingResult.HOLD;
        } else {
            return FillingBySpout.getRequiredAmountForItem(this.f_58857_, transported.stack, this.getCurrentFluidInTank()) == -1 ? BeltProcessingBehaviour.ProcessingResult.PASS : BeltProcessingBehaviour.ProcessingResult.HOLD;
        }
    }

    protected BeltProcessingBehaviour.ProcessingResult whenItemHeld(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler) {
        if (this.processingTicks != -1 && this.processingTicks != 5) {
            return BeltProcessingBehaviour.ProcessingResult.HOLD;
        } else if (!FillingBySpout.canItemBeFilled(this.f_58857_, transported.stack)) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else if (this.tank.isEmpty()) {
            return BeltProcessingBehaviour.ProcessingResult.HOLD;
        } else {
            FluidStack fluid = this.getCurrentFluidInTank();
            int requiredAmountForItem = FillingBySpout.getRequiredAmountForItem(this.f_58857_, transported.stack, fluid.copy());
            if (requiredAmountForItem == -1) {
                return BeltProcessingBehaviour.ProcessingResult.PASS;
            } else if (requiredAmountForItem > fluid.getAmount()) {
                return BeltProcessingBehaviour.ProcessingResult.HOLD;
            } else if (this.processingTicks == -1) {
                this.processingTicks = 20;
                this.notifyUpdate();
                return BeltProcessingBehaviour.ProcessingResult.HOLD;
            } else {
                ItemStack out = FillingBySpout.fillItem(this.f_58857_, requiredAmountForItem, transported.stack, fluid);
                if (!out.isEmpty()) {
                    List<TransportedItemStack> outList = new ArrayList();
                    TransportedItemStack held = null;
                    TransportedItemStack result = transported.copy();
                    result.stack = out;
                    if (!transported.stack.isEmpty()) {
                        held = transported.copy();
                    }
                    outList.add(result);
                    handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertToAndLeaveHeld(outList, held));
                }
                this.award(AllAdvancements.SPOUT);
                if (this.trackFoods()) {
                    this.createdChocolateBerries = this.createdChocolateBerries | AllItems.CHOCOLATE_BERRIES.isIn(out);
                    this.createdHoneyApple = this.createdHoneyApple | AllItems.HONEYED_APPLE.isIn(out);
                    this.createdSweetRoll = this.createdSweetRoll | AllItems.SWEET_ROLL.isIn(out);
                    if (this.createdChocolateBerries && this.createdHoneyApple && this.createdSweetRoll) {
                        this.award(AllAdvancements.FOODS);
                    }
                }
                this.tank.getPrimaryHandler().setFluid(fluid);
                this.sendSplash = true;
                this.notifyUpdate();
                return BeltProcessingBehaviour.ProcessingResult.HOLD;
            }
        }
    }

    private FluidStack getCurrentFluidInTank() {
        return this.tank.getPrimaryHandler().getFluid();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("ProcessingTicks", this.processingTicks);
        if (this.sendSplash && clientPacket) {
            compound.putBoolean("Splash", true);
            this.sendSplash = false;
        }
        if (this.trackFoods()) {
            if (this.createdChocolateBerries) {
                NBTHelper.putMarker(compound, "ChocolateBerries");
            }
            if (this.createdHoneyApple) {
                NBTHelper.putMarker(compound, "HoneyApple");
            }
            if (this.createdSweetRoll) {
                NBTHelper.putMarker(compound, "SweetRoll");
            }
        }
    }

    private boolean trackFoods() {
        return this.getBehaviour(AdvancementBehaviour.TYPE).isOwnerPresent();
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.processingTicks = compound.getInt("ProcessingTicks");
        this.createdChocolateBerries = compound.contains("ChocolateBerries");
        this.createdHoneyApple = compound.contains("HoneyApple");
        this.createdSweetRoll = compound.contains("SweetRoll");
        if (clientPacket) {
            if (compound.contains("Splash")) {
                this.spawnSplash(this.tank.getPrimaryTank().getRenderedFluid());
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ForgeCapabilities.FLUID_HANDLER && side != Direction.DOWN ? this.tank.getCapability().cast() : super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        super.tick();
        FluidStack currentFluidInTank = this.getCurrentFluidInTank();
        if (this.processingTicks == -1 && (this.isVirtual() || !this.f_58857_.isClientSide()) && !currentFluidInTank.isEmpty()) {
            BlockSpoutingBehaviour.forEach(behaviour -> {
                if (this.customProcess == null) {
                    if (behaviour.fillBlock(this.f_58857_, this.f_58858_.below(2), this, currentFluidInTank, true) > 0) {
                        this.processingTicks = 20;
                        this.customProcess = behaviour;
                        this.notifyUpdate();
                    }
                }
            });
        }
        if (this.processingTicks >= 0) {
            this.processingTicks--;
            if (this.processingTicks == 5 && this.customProcess != null) {
                int fillBlock = this.customProcess.fillBlock(this.f_58857_, this.f_58858_.below(2), this, currentFluidInTank, false);
                this.customProcess = null;
                if (fillBlock > 0) {
                    this.tank.getPrimaryHandler().setFluid(FluidHelper.copyStackWithAmount(currentFluidInTank, currentFluidInTank.getAmount() - fillBlock));
                    this.sendSplash = true;
                    this.notifyUpdate();
                }
            }
        }
        if (this.processingTicks >= 8 && this.f_58857_.isClientSide) {
            this.spawnProcessingParticles(this.tank.getPrimaryTank().getRenderedFluid());
        }
    }

    protected void spawnProcessingParticles(FluidStack fluid) {
        if (!this.isVirtual()) {
            Vec3 vec = VecHelper.getCenterOf(this.f_58858_);
            vec = vec.subtract(0.0, 0.5, 0.0);
            ParticleOptions particle = FluidFX.getFluidParticle(fluid);
            this.f_58857_.addAlwaysVisibleParticle(particle, vec.x, vec.y, vec.z, 0.0, -0.1F, 0.0);
        }
    }

    protected void spawnSplash(FluidStack fluid) {
        if (!this.isVirtual()) {
            Vec3 vec = VecHelper.getCenterOf(this.f_58858_);
            vec = vec.subtract(0.0, 1.6875, 0.0);
            ParticleOptions particle = FluidFX.getFluidParticle(fluid);
            for (int i = 0; i < SPLASH_PARTICLE_COUNT; i++) {
                Vec3 m = VecHelper.offsetRandomly(Vec3.ZERO, this.f_58857_.random, 0.125F);
                m = new Vec3(m.x, Math.abs(m.y), m.z);
                this.f_58857_.addAlwaysVisibleParticle(particle, vec.x, vec.y, vec.z, m.x, m.y, m.z);
            }
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return this.containedFluidTooltip(tooltip, isPlayerSneaking, this.getCapability(ForgeCapabilities.FLUID_HANDLER));
    }
}