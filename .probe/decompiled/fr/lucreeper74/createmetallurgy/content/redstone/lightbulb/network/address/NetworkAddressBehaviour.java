package fr.lucreeper74.createmetallurgy.content.redstone.lightbulb.network.address;

import com.simibubi.create.content.equipment.clipboard.ClipboardCloneable;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import fr.lucreeper74.createmetallurgy.content.redstone.lightbulb.network.INetworkNode;
import fr.lucreeper74.createmetallurgy.content.redstone.lightbulb.network.NetworkHandler;
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

public class NetworkAddressBehaviour extends BlockEntityBehaviour implements INetworkNode, ClipboardCloneable {

    public static final BehaviourType<NetworkAddressBehaviour> TYPE = new BehaviourType<>();

    ValueBoxTransform slot;

    NetworkHandler.Address address = NetworkHandler.Address.EMPTY;

    private IntSupplier transmission;

    private IntConsumer signalCallback;

    public NetworkAddressBehaviour(SmartBlockEntity be, ValueBoxTransform AddressSlot) {
        super(be);
        this.slot = AddressSlot;
    }

    public static NetworkAddressBehaviour networkNode(SmartBlockEntity be, ValueBoxTransform slot, IntConsumer signalCallback, IntSupplier transmission) {
        NetworkAddressBehaviour behaviour = new NetworkAddressBehaviour(be, slot);
        behaviour.signalCallback = signalCallback;
        behaviour.transmission = transmission;
        return behaviour;
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    private NetworkHandler getHandler() {
        return CreateMetallurgy.NETWORK_HANDLER;
    }

    public void setAddress(ItemStack stack) {
        stack = stack.copy();
        stack.setCount(1);
        boolean changed = !ItemStack.isSameItem(stack, this.address.getStack());
        if (changed) {
            this.getHandler().getNetOf(this.getWorld(), this).removeNode(this);
            this.address = NetworkHandler.Address.of(stack);
            this.blockEntity.sendData();
            this.getHandler().getNetOf(this.getWorld(), this).addNode(this);
        }
    }

    @Override
    public void unload() {
        super.unload();
        if (!this.getWorld().isClientSide) {
            this.getHandler().getNetOf(this.getWorld(), this).removeNode(this);
        }
    }

    public void notifySignalChange() {
        CreateMetallurgy.NETWORK_HANDLER.getNetOf(this.getWorld(), this).transmit(this);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!this.getWorld().isClientSide) {
            this.getHandler().getNetOf(this.getWorld(), this).addNode(this);
        }
    }

    public boolean testHit(Vec3 hit) {
        BlockState state = this.blockEntity.m_58900_();
        Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(this.blockEntity.m_58899_()));
        return this.slot.testHit(state, localHit);
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
    public boolean isSafeNBT() {
        return true;
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        nbt.put("Address", this.address.getStack().save(new CompoundTag()));
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        this.address = NetworkHandler.Address.of(ItemStack.of(nbt.getCompound("Address")));
    }

    @Override
    public int getTransmittedSignal() {
        return this.transmission.getAsInt();
    }

    @Override
    public void setReceivedSignal(int power) {
        this.signalCallback.accept(power);
    }

    @Override
    public BlockPos getLocation() {
        return this.getPos();
    }

    @Override
    public NetworkHandler.Address getAddress() {
        return this.address;
    }

    @Override
    public String getClipboardKey() {
        return "Address";
    }

    @Override
    public boolean writeToClipboard(CompoundTag tag, Direction side) {
        tag.put("AddressClip", this.address.getStack().save(new CompoundTag()));
        return true;
    }

    @Override
    public boolean readFromClipboard(CompoundTag tag, Player player, Direction side, boolean simulate) {
        if (!tag.contains("AddressClip")) {
            return false;
        } else if (simulate) {
            return true;
        } else {
            this.setAddress(ItemStack.of(tag.getCompound("AddressClip")));
            return true;
        }
    }
}