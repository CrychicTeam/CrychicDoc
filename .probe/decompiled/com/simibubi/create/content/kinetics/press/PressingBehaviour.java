package com.simibubi.create.content.kinetics.press;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeItemStack;

public class PressingBehaviour extends BeltProcessingBehaviour {

    public static final int CYCLE = 240;

    public static final int ENTITY_SCAN = 10;

    public List<ItemStack> particleItems = new ArrayList();

    public PressingBehaviour.PressingBehaviourSpecifics specifics;

    public int prevRunningTicks;

    public int runningTicks;

    public boolean running;

    public boolean finished;

    public PressingBehaviour.Mode mode;

    int entityScanCooldown;

    public <T extends SmartBlockEntity & PressingBehaviour.PressingBehaviourSpecifics> PressingBehaviour(T be) {
        super(be);
        this.specifics = be;
        this.mode = PressingBehaviour.Mode.WORLD;
        this.entityScanCooldown = 10;
        this.whenItemEnters((s, i) -> BeltPressingCallbacks.onItemReceived(s, i, this));
        this.whileItemHeld((s, i) -> BeltPressingCallbacks.whenItemHeld(s, i, this));
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        this.running = compound.getBoolean("Running");
        this.mode = PressingBehaviour.Mode.values()[compound.getInt("Mode")];
        this.finished = compound.getBoolean("Finished");
        this.prevRunningTicks = this.runningTicks = compound.getInt("Ticks");
        super.read(compound, clientPacket);
        if (clientPacket) {
            NBTHelper.iterateCompoundList(compound.getList("ParticleItems", 10), c -> this.particleItems.add(ItemStack.of(c)));
            this.spawnParticles();
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", this.running);
        compound.putInt("Mode", this.mode.ordinal());
        compound.putBoolean("Finished", this.finished);
        compound.putInt("Ticks", this.runningTicks);
        super.write(compound, clientPacket);
        if (clientPacket) {
            compound.put("ParticleItems", NBTHelper.writeCompoundList(this.particleItems, IForgeItemStack::serializeNBT));
            this.particleItems.clear();
        }
    }

    public float getRenderedHeadOffset(float partialTicks) {
        if (!this.running) {
            return 0.0F;
        } else {
            int runningTicks = Math.abs(this.runningTicks);
            float ticks = Mth.lerp(partialTicks, (float) this.prevRunningTicks, (float) runningTicks);
            return runningTicks < 160 ? (float) Mth.clamp(Math.pow((double) (ticks / 240.0F * 2.0F), 3.0), 0.0, 1.0) : Mth.clamp((240.0F - ticks) / 240.0F * 3.0F, 0.0F, 1.0F);
        }
    }

    public void start(PressingBehaviour.Mode mode) {
        this.mode = mode;
        this.running = true;
        this.prevRunningTicks = 0;
        this.runningTicks = 0;
        this.particleItems.clear();
        this.blockEntity.sendData();
    }

    public boolean inWorld() {
        return this.mode == PressingBehaviour.Mode.WORLD;
    }

    public boolean onBasin() {
        return this.mode == PressingBehaviour.Mode.BASIN;
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.getWorld();
        BlockPos worldPosition = this.getPos();
        if (this.running && level != null) {
            if (level.isClientSide && this.runningTicks == -120) {
                this.prevRunningTicks = 120;
            } else {
                if (this.runningTicks == 120 && this.specifics.getKineticSpeed() != 0.0F) {
                    if (this.inWorld()) {
                        this.applyInWorld();
                    }
                    if (this.onBasin()) {
                        this.applyOnBasin();
                    }
                    if (level.getBlockState(worldPosition.below(2)).m_60827_() == SoundType.WOOL) {
                        AllSoundEvents.MECHANICAL_PRESS_ACTIVATION_ON_BELT.playOnServer(level, worldPosition);
                    } else {
                        AllSoundEvents.MECHANICAL_PRESS_ACTIVATION.playOnServer(level, worldPosition, 0.5F, 0.75F + Math.abs(this.specifics.getKineticSpeed()) / 1024.0F);
                    }
                    if (!level.isClientSide) {
                        this.blockEntity.sendData();
                    }
                }
                if (!level.isClientSide && this.runningTicks > 240) {
                    this.finished = true;
                    this.running = false;
                    this.particleItems.clear();
                    this.specifics.onPressingCompleted();
                    this.blockEntity.sendData();
                } else {
                    this.prevRunningTicks = this.runningTicks;
                    this.runningTicks = this.runningTicks + this.getRunningTickSpeed();
                    if (this.prevRunningTicks < 120 && this.runningTicks >= 120) {
                        this.runningTicks = 120;
                        if (level.isClientSide && !this.blockEntity.isVirtual()) {
                            this.runningTicks = -120;
                        }
                    }
                }
            }
        } else {
            if (level != null && !level.isClientSide) {
                if (this.specifics.getKineticSpeed() == 0.0F) {
                    return;
                }
                if (this.entityScanCooldown > 0) {
                    this.entityScanCooldown--;
                }
                if (this.entityScanCooldown <= 0) {
                    this.entityScanCooldown = 10;
                    if (BlockEntityBehaviour.get(level, worldPosition.below(2), TransportedItemStackHandlerBehaviour.TYPE) != null) {
                        return;
                    }
                    if (AllBlocks.BASIN.has(level.getBlockState(worldPosition.below(2)))) {
                        return;
                    }
                    for (ItemEntity itemEntity : level.m_45976_(ItemEntity.class, new AABB(worldPosition.below()).deflate(0.125))) {
                        if (itemEntity.m_6084_() && itemEntity.m_20096_() && this.specifics.tryProcessInWorld(itemEntity, true)) {
                            this.start(PressingBehaviour.Mode.WORLD);
                            return;
                        }
                    }
                }
            }
        }
    }

    protected void applyOnBasin() {
        Level level = this.getWorld();
        if (!level.isClientSide) {
            this.particleItems.clear();
            if (this.specifics.tryProcessInBasin(false)) {
                this.blockEntity.sendData();
            }
        }
    }

    protected void applyInWorld() {
        Level level = this.getWorld();
        BlockPos worldPosition = this.getPos();
        AABB bb = new AABB(worldPosition.below(1));
        boolean bulk = this.specifics.canProcessInBulk();
        this.particleItems.clear();
        if (!level.isClientSide) {
            for (Entity entity : level.m_45933_(null, bb)) {
                if (entity instanceof ItemEntity) {
                    ItemEntity itemEntity = (ItemEntity) entity;
                    if (entity.isAlive() && entity.onGround()) {
                        this.entityScanCooldown = 0;
                        if (this.specifics.tryProcessInWorld(itemEntity, false)) {
                            this.blockEntity.sendData();
                        }
                        if (!bulk) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public int getRunningTickSpeed() {
        float speed = this.specifics.getKineticSpeed();
        return speed == 0.0F ? 0 : (int) Mth.lerp(Mth.clamp(Math.abs(speed) / 512.0F, 0.0F, 1.0F), 1.0F, 60.0F);
    }

    protected void spawnParticles() {
        if (!this.particleItems.isEmpty()) {
            BlockPos worldPosition = this.getPos();
            if (this.mode == PressingBehaviour.Mode.BASIN) {
                this.particleItems.forEach(stack -> this.makeCompactingParticleEffect(VecHelper.getCenterOf(worldPosition.below(2)), stack));
            }
            if (this.mode == PressingBehaviour.Mode.BELT) {
                this.particleItems.forEach(stack -> this.makePressingParticleEffect(VecHelper.getCenterOf(worldPosition.below(2)).add(0.0, 0.5, 0.0), stack));
            }
            if (this.mode == PressingBehaviour.Mode.WORLD) {
                this.particleItems.forEach(stack -> this.makePressingParticleEffect(VecHelper.getCenterOf(worldPosition.below(1)).add(0.0, -0.25, 0.0), stack));
            }
            this.particleItems.clear();
        }
    }

    public void makePressingParticleEffect(Vec3 pos, ItemStack stack) {
        this.makePressingParticleEffect(pos, stack, this.specifics.getParticleAmount());
    }

    public void makePressingParticleEffect(Vec3 pos, ItemStack stack, int amount) {
        Level level = this.getWorld();
        if (level != null && level.isClientSide) {
            for (int i = 0; i < amount; i++) {
                Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, level.random, 0.125F).multiply(1.0, 0.0, 1.0);
                motion = motion.add(0.0, amount != 1 ? 0.125 : 0.0625, 0.0);
                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.x, pos.y - 0.25, pos.z, motion.x, motion.y, motion.z);
            }
        }
    }

    public void makeCompactingParticleEffect(Vec3 pos, ItemStack stack) {
        Level level = this.getWorld();
        if (level != null && level.isClientSide) {
            for (int i = 0; i < 20; i++) {
                Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, level.random, 0.175F).multiply(1.0, 0.0, 1.0);
                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.x, pos.y, pos.z, motion.x, motion.y + 0.25, motion.z);
            }
        }
    }

    public static enum Mode {

        WORLD(1.0F), BELT(1.1875F), BASIN(1.375F);

        public float headOffset;

        private Mode(float headOffset) {
            this.headOffset = headOffset;
        }
    }

    public interface PressingBehaviourSpecifics {

        boolean tryProcessInBasin(boolean var1);

        boolean tryProcessOnBelt(TransportedItemStack var1, List<ItemStack> var2, boolean var3);

        boolean tryProcessInWorld(ItemEntity var1, boolean var2);

        boolean canProcessInBulk();

        void onPressingCompleted();

        int getParticleAmount();

        float getKineticSpeed();
    }
}