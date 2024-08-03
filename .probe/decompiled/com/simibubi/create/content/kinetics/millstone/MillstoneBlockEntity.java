package com.simibubi.create.content.kinetics.millstone;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class MillstoneBlockEntity extends KineticBlockEntity {

    public ItemStackHandler inputInv = new ItemStackHandler(1);

    public ItemStackHandler outputInv = new ItemStackHandler(9);

    public LazyOptional<IItemHandler> capability = LazyOptional.of(() -> new MillstoneBlockEntity.MillstoneInventoryHandler());

    public int timer;

    private MillingRecipe lastRecipe;

    public MillstoneBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));
        super.addBehaviours(behaviours);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.MILLSTONE });
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void tickAudio() {
        super.tickAudio();
        if (this.getSpeed() != 0.0F) {
            if (!this.inputInv.getStackInSlot(0).isEmpty()) {
                float pitch = Mth.clamp(Math.abs(this.getSpeed()) / 256.0F + 0.45F, 0.85F, 1.0F);
                SoundScapes.play(SoundScapes.AmbienceGroup.MILLING, this.f_58858_, pitch);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getSpeed() != 0.0F) {
            for (int i = 0; i < this.outputInv.getSlots(); i++) {
                if (this.outputInv.getStackInSlot(i).getCount() == this.outputInv.getSlotLimit(i)) {
                    return;
                }
            }
            if (this.timer > 0) {
                this.timer = this.timer - this.getProcessingSpeed();
                if (this.f_58857_.isClientSide) {
                    this.spawnParticles();
                } else {
                    if (this.timer <= 0) {
                        this.process();
                    }
                }
            } else if (!this.inputInv.getStackInSlot(0).isEmpty()) {
                RecipeWrapper inventoryIn = new RecipeWrapper(this.inputInv);
                if (this.lastRecipe != null && this.lastRecipe.matches(inventoryIn, this.f_58857_)) {
                    this.timer = this.lastRecipe.getProcessingDuration();
                    this.sendData();
                } else {
                    Optional<MillingRecipe> recipe = AllRecipeTypes.MILLING.find(inventoryIn, this.f_58857_);
                    if (!recipe.isPresent()) {
                        this.timer = 100;
                        this.sendData();
                    } else {
                        this.lastRecipe = (MillingRecipe) recipe.get();
                        this.timer = this.lastRecipe.getProcessingDuration();
                        this.sendData();
                    }
                }
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.capability.invalidate();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(this.f_58857_, this.f_58858_, this.inputInv);
        ItemHelper.dropContents(this.f_58857_, this.f_58858_, this.outputInv);
    }

    private void process() {
        RecipeWrapper inventoryIn = new RecipeWrapper(this.inputInv);
        if (this.lastRecipe == null || !this.lastRecipe.matches(inventoryIn, this.f_58857_)) {
            Optional<MillingRecipe> recipe = AllRecipeTypes.MILLING.find(inventoryIn, this.f_58857_);
            if (!recipe.isPresent()) {
                return;
            }
            this.lastRecipe = (MillingRecipe) recipe.get();
        }
        ItemStack stackInSlot = this.inputInv.getStackInSlot(0);
        stackInSlot.shrink(1);
        this.inputInv.setStackInSlot(0, stackInSlot);
        this.lastRecipe.rollResults().forEach(stack -> ItemHandlerHelper.insertItemStacked(this.outputInv, stack, false));
        this.award(AllAdvancements.MILLSTONE);
        this.sendData();
        this.m_6596_();
    }

    public void spawnParticles() {
        ItemStack stackInSlot = this.inputInv.getStackInSlot(0);
        if (!stackInSlot.isEmpty()) {
            ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
            float angle = this.f_58857_.random.nextFloat() * 360.0F;
            Vec3 offset = new Vec3(0.0, 0.0, 0.5);
            offset = VecHelper.rotate(offset, (double) angle, Direction.Axis.Y);
            Vec3 target = VecHelper.rotate(offset, this.getSpeed() > 0.0F ? 25.0 : -25.0, Direction.Axis.Y);
            Vec3 center = offset.add(VecHelper.getCenterOf(this.f_58858_));
            target = VecHelper.offsetRandomly(target.subtract(offset), this.f_58857_.random, 0.0078125F);
            this.f_58857_.addParticle(data, center.x, center.y, center.z, target.x, target.y, target.z);
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Timer", this.timer);
        compound.put("InputInventory", this.inputInv.serializeNBT());
        compound.put("OutputInventory", this.outputInv.serializeNBT());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.timer = compound.getInt("Timer");
        this.inputInv.deserializeNBT(compound.getCompound("InputInventory"));
        this.outputInv.deserializeNBT(compound.getCompound("OutputInventory"));
        super.read(compound, clientPacket);
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(this.getSpeed() / 16.0F), 1, 512);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return this.isItemHandlerCap(cap) ? this.capability.cast() : super.getCapability(cap, side);
    }

    private boolean canProcess(ItemStack stack) {
        ItemStackHandler tester = new ItemStackHandler(1);
        tester.setStackInSlot(0, stack);
        RecipeWrapper inventoryIn = new RecipeWrapper(tester);
        return this.lastRecipe != null && this.lastRecipe.matches(inventoryIn, this.f_58857_) ? true : AllRecipeTypes.MILLING.find(inventoryIn, this.f_58857_).isPresent();
    }

    private class MillstoneInventoryHandler extends CombinedInvWrapper {

        public MillstoneInventoryHandler() {
            super(MillstoneBlockEntity.this.inputInv, MillstoneBlockEntity.this.outputInv);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return MillstoneBlockEntity.this.outputInv == this.getHandlerFromIndex(this.getIndexForSlot(slot)) ? false : MillstoneBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (MillstoneBlockEntity.this.outputInv == this.getHandlerFromIndex(this.getIndexForSlot(slot))) {
                return stack;
            } else {
                return !this.isItemValid(slot, stack) ? stack : super.insertItem(slot, stack, simulate);
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return MillstoneBlockEntity.this.inputInv == this.getHandlerFromIndex(this.getIndexForSlot(slot)) ? ItemStack.EMPTY : super.extractItem(slot, amount, simulate);
        }
    }
}