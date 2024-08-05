package com.simibubi.create.content.kinetics.crusher;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CrushingWheelControllerBlockEntity extends SmartBlockEntity {

    public Entity processingEntity;

    private UUID entityUUID;

    protected boolean searchForEntity;

    public ProcessingInventory inventory;

    protected LazyOptional<IItemHandlerModifiable> handler = LazyOptional.of(() -> this.inventory);

    private RecipeWrapper wrapper;

    public float crushingspeed;

    public CrushingWheelControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = new ProcessingInventory(this::itemInserted) {

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return super.isItemValid(slot, stack) && CrushingWheelControllerBlockEntity.this.processingEntity == null;
            }
        };
        this.wrapper = new RecipeWrapper(this.inventory);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this).onlyInsertWhen(this::supportsDirectBeltInput));
    }

    private boolean supportsDirectBeltInput(Direction side) {
        BlockState blockState = this.m_58900_();
        if (blockState == null) {
            return false;
        } else {
            Direction direction = (Direction) blockState.m_61143_(CrushingWheelControllerBlock.f_52588_);
            return direction == Direction.DOWN || direction == side;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.searchForEntity) {
            this.searchForEntity = false;
            List<Entity> search = this.f_58857_.getEntities((Entity) null, new AABB(this.m_58899_()), e -> this.entityUUID.equals(e.getUUID()));
            if (search.isEmpty()) {
                this.clear();
            } else {
                this.processingEntity = (Entity) search.get(0);
            }
        }
        if (this.isOccupied()) {
            if (this.crushingspeed != 0.0F) {
                if (this.f_58857_.isClientSide) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.tickAudio());
                }
                float speed = this.crushingspeed * 4.0F;
                Vec3 centerPos = VecHelper.getCenterOf(this.f_58858_);
                Direction facing = (Direction) this.m_58900_().m_61143_(CrushingWheelControllerBlock.f_52588_);
                int offset = facing.getAxisDirection().getStep();
                Vec3 outSpeed = new Vec3((facing.getAxis() == Direction.Axis.X ? 0.25 : 0.0) * (double) offset, offset == 1 ? (facing.getAxis() == Direction.Axis.Y ? 0.5 : 0.0) : 0.0, (facing.getAxis() == Direction.Axis.Z ? 0.25 : 0.0) * (double) offset);
                Vec3 outPos = centerPos.add((double) (facing.getAxis() == Direction.Axis.X ? 0.55F * (float) offset : 0.0F), (double) (facing.getAxis() == Direction.Axis.Y ? 0.55F * (float) offset : 0.0F), (double) (facing.getAxis() == Direction.Axis.Z ? 0.55F * (float) offset : 0.0F));
                if (!this.hasEntity()) {
                    float processingSpeed = Mth.clamp(speed / (float) (!this.inventory.appliedRecipe ? Mth.log2(this.inventory.getStackInSlot(0).getCount()) : 1), 0.25F, 20.0F);
                    this.inventory.remainingTime -= processingSpeed;
                    this.spawnParticles(this.inventory.getStackInSlot(0));
                    if (!this.f_58857_.isClientSide) {
                        if (this.inventory.remainingTime < 20.0F && !this.inventory.appliedRecipe) {
                            this.applyRecipe();
                            this.inventory.appliedRecipe = true;
                            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 18);
                        } else if (!(this.inventory.remainingTime > 0.0F)) {
                            this.inventory.remainingTime = 0.0F;
                            if (facing != Direction.UP) {
                                BlockPos nextPos = this.f_58858_.offset(facing.getAxis() == Direction.Axis.X ? 1 * offset : 0, -1, facing.getAxis() == Direction.Axis.Z ? 1 * offset : 0);
                                DirectBeltInputBehaviour behaviour = BlockEntityBehaviour.get(this.f_58857_, nextPos, DirectBeltInputBehaviour.TYPE);
                                if (behaviour != null) {
                                    boolean changed = false;
                                    if (!behaviour.canInsertFromSide(facing)) {
                                        return;
                                    }
                                    for (int slot = 0; slot < this.inventory.getSlots(); slot++) {
                                        ItemStack stack = this.inventory.getStackInSlot(slot);
                                        if (!stack.isEmpty()) {
                                            ItemStack remainder = behaviour.handleInsertion(stack, facing, false);
                                            if (!remainder.equals(stack, false)) {
                                                this.inventory.setStackInSlot(slot, remainder);
                                                changed = true;
                                            }
                                        }
                                    }
                                    if (changed) {
                                        this.m_6596_();
                                        this.sendData();
                                    }
                                    return;
                                }
                            }
                            for (int slotx = 0; slotx < this.inventory.getSlots(); slotx++) {
                                ItemStack stack = this.inventory.getStackInSlot(slotx);
                                if (!stack.isEmpty()) {
                                    ItemEntity entityIn = new ItemEntity(this.f_58857_, outPos.x, outPos.y, outPos.z, stack);
                                    entityIn.m_20256_(outSpeed);
                                    entityIn.getPersistentData().put("BypassCrushingWheel", NbtUtils.writeBlockPos(this.f_58858_));
                                    this.f_58857_.m_7967_(entityIn);
                                }
                            }
                            this.inventory.clear();
                            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 18);
                        }
                    }
                } else if (this.processingEntity.isAlive() && this.processingEntity.getBoundingBox().intersects(new AABB(this.f_58858_).inflate(0.5))) {
                    double xMotion = ((double) ((float) this.f_58858_.m_123341_() + 0.5F) - this.processingEntity.getX()) / 2.0;
                    double zMotion = ((double) ((float) this.f_58858_.m_123343_() + 0.5F) - this.processingEntity.getZ()) / 2.0;
                    if (this.processingEntity.isShiftKeyDown()) {
                        zMotion = 0.0;
                        xMotion = 0.0;
                    }
                    double movement = (double) (Math.max(-speed / 4.0F, -0.5F) * (float) (-offset));
                    this.processingEntity.setDeltaMovement(new Vec3(facing.getAxis() == Direction.Axis.X ? movement : xMotion, facing.getAxis() == Direction.Axis.Y ? movement : 0.0, facing.getAxis() == Direction.Axis.Z ? movement : zMotion));
                    if (!this.f_58857_.isClientSide) {
                        if (!(this.processingEntity instanceof ItemEntity itemEntity)) {
                            Vec3 entityOutPos = outPos.add(facing.getAxis() == Direction.Axis.X ? (double) (0.5F * (float) offset) : 0.0, facing.getAxis() == Direction.Axis.Y ? (double) (0.5F * (float) offset) : 0.0, facing.getAxis() == Direction.Axis.Z ? (double) (0.5F * (float) offset) : 0.0);
                            int crusherDamage = AllConfigs.server().kinetics.crushingDamage.get();
                            if (this.processingEntity instanceof LivingEntity && ((LivingEntity) this.processingEntity).getHealth() - (float) crusherDamage <= 0.0F && ((LivingEntity) this.processingEntity).hurtTime <= 0) {
                                this.processingEntity.setPos(entityOutPos.x, entityOutPos.y, entityOutPos.z);
                            }
                            this.processingEntity.hurt(CreateDamageSources.crush(this.f_58857_), (float) crusherDamage);
                            if (!this.processingEntity.isAlive()) {
                                this.processingEntity.setPos(entityOutPos.x, entityOutPos.y, entityOutPos.z);
                            }
                        } else {
                            itemEntity.setPickUpDelay(20);
                            if (facing.getAxis() == Direction.Axis.Y) {
                                if (this.processingEntity.getY() * (double) (-offset) < (centerPos.y - 0.25) * (double) (-offset)) {
                                    this.intakeItem(itemEntity);
                                }
                            } else if (facing.getAxis() == Direction.Axis.Z) {
                                if (this.processingEntity.getZ() * (double) (-offset) < (centerPos.z - 0.25) * (double) (-offset)) {
                                    this.intakeItem(itemEntity);
                                }
                            } else if (this.processingEntity.getX() * (double) (-offset) < (centerPos.x - 0.25) * (double) (-offset)) {
                                this.intakeItem(itemEntity);
                            }
                        }
                    }
                } else {
                    this.clear();
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        float pitch = Mth.clamp(this.crushingspeed / 256.0F + 0.45F, 0.85F, 1.0F);
        if (this.entityUUID != null || !this.inventory.getStackInSlot(0).isEmpty()) {
            SoundScapes.play(SoundScapes.AmbienceGroup.CRUSHING, this.f_58858_, pitch);
        }
    }

    private void intakeItem(ItemEntity itemEntity) {
        this.inventory.clear();
        this.inventory.setStackInSlot(0, itemEntity.getItem().copy());
        this.itemInserted(this.inventory.getStackInSlot(0));
        itemEntity.m_146870_();
        this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 18);
    }

    protected void spawnParticles(ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            ParticleOptions particleData = null;
            if (stack.getItem() instanceof BlockItem) {
                particleData = new BlockParticleOption(ParticleTypes.BLOCK, ((BlockItem) stack.getItem()).getBlock().defaultBlockState());
            } else {
                particleData = new ItemParticleOption(ParticleTypes.ITEM, stack);
            }
            RandomSource r = this.f_58857_.random;
            for (int i = 0; i < 4; i++) {
                this.f_58857_.addParticle(particleData, (double) ((float) this.f_58858_.m_123341_() + r.nextFloat()), (double) ((float) this.f_58858_.m_123342_() + r.nextFloat()), (double) ((float) this.f_58858_.m_123343_() + r.nextFloat()), 0.0, 0.0, 0.0);
            }
        }
    }

    private void applyRecipe() {
        Optional<ProcessingRecipe<RecipeWrapper>> recipe = this.findRecipe();
        List<ItemStack> list = new ArrayList();
        if (recipe.isPresent()) {
            int rolls = this.inventory.getStackInSlot(0).getCount();
            this.inventory.clear();
            for (int roll = 0; roll < rolls; roll++) {
                List<ItemStack> rolledResults = ((ProcessingRecipe) recipe.get()).rollResults();
                for (int i = 0; i < rolledResults.size(); i++) {
                    ItemStack stack = (ItemStack) rolledResults.get(i);
                    ItemHelper.addToList(stack, list);
                }
            }
            for (int slot = 0; slot < list.size() && slot + 1 < this.inventory.getSlots(); slot++) {
                this.inventory.setStackInSlot(slot + 1, (ItemStack) list.get(slot));
            }
        } else {
            this.inventory.clear();
        }
    }

    public Optional<ProcessingRecipe<RecipeWrapper>> findRecipe() {
        Optional<ProcessingRecipe<RecipeWrapper>> crushingRecipe = AllRecipeTypes.CRUSHING.find(this.wrapper, this.f_58857_);
        if (!crushingRecipe.isPresent()) {
            crushingRecipe = AllRecipeTypes.MILLING.find(this.wrapper, this.f_58857_);
        }
        return crushingRecipe;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        if (this.hasEntity()) {
            compound.put("Entity", NbtUtils.createUUID(this.entityUUID));
        }
        compound.put("Inventory", this.inventory.serializeNBT());
        compound.putFloat("Speed", this.crushingspeed);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (compound.contains("Entity") && !this.isOccupied()) {
            this.entityUUID = NbtUtils.loadUUID(NBTHelper.getINBT(compound, "Entity"));
            this.searchForEntity = true;
        }
        this.crushingspeed = compound.getFloat("Speed");
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
    }

    public void startCrushing(Entity entity) {
        this.processingEntity = entity;
        this.entityUUID = entity.getUUID();
    }

    private void itemInserted(ItemStack stack) {
        Optional<ProcessingRecipe<RecipeWrapper>> recipe = this.findRecipe();
        this.inventory.remainingTime = recipe.isPresent() ? (float) ((ProcessingRecipe) recipe.get()).getProcessingDuration() : 100.0F;
        this.inventory.appliedRecipe = false;
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.handler.cast() : super.getCapability(cap, side);
    }

    public void clear() {
        this.processingEntity = null;
        this.entityUUID = null;
    }

    public boolean isOccupied() {
        return this.hasEntity() || !this.inventory.isEmpty();
    }

    public boolean hasEntity() {
        return this.processingEntity != null;
    }
}