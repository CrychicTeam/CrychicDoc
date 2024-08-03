package com.simibubi.create.content.redstone.link;

import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.clipboard.ClipboardCloneable;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.Couple;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

public class LinkBehaviour extends BlockEntityBehaviour implements IRedstoneLinkable, ClipboardCloneable {

    public static final BehaviourType<LinkBehaviour> TYPE = new BehaviourType<>();

    RedstoneLinkNetworkHandler.Frequency frequencyFirst = RedstoneLinkNetworkHandler.Frequency.EMPTY;

    RedstoneLinkNetworkHandler.Frequency frequencyLast = RedstoneLinkNetworkHandler.Frequency.EMPTY;

    ValueBoxTransform firstSlot;

    ValueBoxTransform secondSlot;

    Vec3 textShift;

    public boolean newPosition;

    private LinkBehaviour.Mode mode;

    private IntSupplier transmission;

    private IntConsumer signalCallback;

    protected LinkBehaviour(SmartBlockEntity be, Pair<ValueBoxTransform, ValueBoxTransform> slots) {
        super(be);
        this.firstSlot = (ValueBoxTransform) slots.getLeft();
        this.secondSlot = (ValueBoxTransform) slots.getRight();
        this.textShift = Vec3.ZERO;
        this.newPosition = true;
    }

    public static LinkBehaviour receiver(SmartBlockEntity be, Pair<ValueBoxTransform, ValueBoxTransform> slots, IntConsumer signalCallback) {
        LinkBehaviour behaviour = new LinkBehaviour(be, slots);
        behaviour.signalCallback = signalCallback;
        behaviour.mode = LinkBehaviour.Mode.RECEIVE;
        return behaviour;
    }

    public static LinkBehaviour transmitter(SmartBlockEntity be, Pair<ValueBoxTransform, ValueBoxTransform> slots, IntSupplier transmission) {
        LinkBehaviour behaviour = new LinkBehaviour(be, slots);
        behaviour.transmission = transmission;
        behaviour.mode = LinkBehaviour.Mode.TRANSMIT;
        return behaviour;
    }

    public LinkBehaviour moveText(Vec3 shift) {
        this.textShift = shift;
        return this;
    }

    public void copyItemsFrom(LinkBehaviour behaviour) {
        if (behaviour != null) {
            this.frequencyFirst = behaviour.frequencyFirst;
            this.frequencyLast = behaviour.frequencyLast;
        }
    }

    @Override
    public boolean isListening() {
        return this.mode == LinkBehaviour.Mode.RECEIVE;
    }

    @Override
    public int getTransmittedStrength() {
        return this.mode == LinkBehaviour.Mode.TRANSMIT ? this.transmission.getAsInt() : 0;
    }

    @Override
    public void setReceivedStrength(int networkPower) {
        if (this.newPosition) {
            this.signalCallback.accept(networkPower);
        }
    }

    public void notifySignalChange() {
        Create.REDSTONE_LINK_NETWORK_HANDLER.updateNetworkOf(this.getWorld(), this);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!this.getWorld().isClientSide) {
            this.getHandler().addToNetwork(this.getWorld(), this);
            this.newPosition = true;
        }
    }

    @Override
    public Couple<RedstoneLinkNetworkHandler.Frequency> getNetworkKey() {
        return Couple.create(this.frequencyFirst, this.frequencyLast);
    }

    @Override
    public void unload() {
        super.unload();
        if (!this.getWorld().isClientSide) {
            this.getHandler().removeFromNetwork(this.getWorld(), this);
        }
    }

    @Override
    public boolean isSafeNBT() {
        return true;
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        nbt.put("FrequencyFirst", this.frequencyFirst.getStack().save(new CompoundTag()));
        nbt.put("FrequencyLast", this.frequencyLast.getStack().save(new CompoundTag()));
        nbt.putLong("LastKnownPosition", this.blockEntity.m_58899_().asLong());
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        long positionInTag = this.blockEntity.m_58899_().asLong();
        long positionKey = nbt.getLong("LastKnownPosition");
        this.newPosition = positionInTag != positionKey;
        super.read(nbt, clientPacket);
        this.frequencyFirst = RedstoneLinkNetworkHandler.Frequency.of(ItemStack.of(nbt.getCompound("FrequencyFirst")));
        this.frequencyLast = RedstoneLinkNetworkHandler.Frequency.of(ItemStack.of(nbt.getCompound("FrequencyLast")));
    }

    public void setFrequency(boolean first, ItemStack stack) {
        stack = stack.copy();
        stack.setCount(1);
        ItemStack toCompare = first ? this.frequencyFirst.getStack() : this.frequencyLast.getStack();
        boolean changed = !ItemStack.isSameItemSameTags(stack, toCompare);
        if (changed) {
            this.getHandler().removeFromNetwork(this.getWorld(), this);
        }
        if (first) {
            this.frequencyFirst = RedstoneLinkNetworkHandler.Frequency.of(stack);
        } else {
            this.frequencyLast = RedstoneLinkNetworkHandler.Frequency.of(stack);
        }
        if (changed) {
            this.blockEntity.sendData();
            this.getHandler().addToNetwork(this.getWorld(), this);
        }
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    private RedstoneLinkNetworkHandler getHandler() {
        return Create.REDSTONE_LINK_NETWORK_HANDLER;
    }

    public boolean testHit(Boolean first, Vec3 hit) {
        BlockState state = this.blockEntity.m_58900_();
        Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(this.blockEntity.m_58899_()));
        return (first ? this.firstSlot : this.secondSlot).testHit(state, localHit);
    }

    @Override
    public boolean isAlive() {
        Level level = this.getWorld();
        BlockPos pos = this.getPos();
        if (this.blockEntity.isChunkUnloaded()) {
            return false;
        } else if (this.blockEntity.m_58901_()) {
            return false;
        } else {
            return !level.isLoaded(pos) ? false : level.getBlockEntity(pos) == this.blockEntity;
        }
    }

    @Override
    public BlockPos getLocation() {
        return this.getPos();
    }

    @Override
    public String getClipboardKey() {
        return "Frequencies";
    }

    @Override
    public boolean writeToClipboard(CompoundTag tag, Direction side) {
        tag.put("First", this.frequencyFirst.getStack().save(new CompoundTag()));
        tag.put("Last", this.frequencyLast.getStack().save(new CompoundTag()));
        return true;
    }

    @Override
    public boolean readFromClipboard(CompoundTag tag, Player player, Direction side, boolean simulate) {
        if (!tag.contains("First") || !tag.contains("Last")) {
            return false;
        } else if (simulate) {
            return true;
        } else {
            this.setFrequency(true, ItemStack.of(tag.getCompound("First")));
            this.setFrequency(false, ItemStack.of(tag.getCompound("Last")));
            return true;
        }
    }

    static enum Mode {

        TRANSMIT, RECEIVE
    }

    public static class SlotPositioning {

        Function<BlockState, Pair<Vec3, Vec3>> offsets;

        Function<BlockState, Vec3> rotation;

        float scale;

        public SlotPositioning(Function<BlockState, Pair<Vec3, Vec3>> offsetsForState, Function<BlockState, Vec3> rotationForState) {
            this.offsets = offsetsForState;
            this.rotation = rotationForState;
            this.scale = 1.0F;
        }

        public LinkBehaviour.SlotPositioning scale(float scale) {
            this.scale = scale;
            return this;
        }
    }
}