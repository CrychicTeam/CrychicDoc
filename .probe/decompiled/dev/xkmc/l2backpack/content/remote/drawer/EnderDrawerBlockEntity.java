package dev.xkmc.l2backpack.content.remote.drawer;

import dev.xkmc.l2backpack.content.drawer.IDrawerBlockEntity;
import dev.xkmc.l2backpack.content.remote.common.DrawerAccess;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class EnderDrawerBlockEntity extends IDrawerBlockEntity {

    @SerialField(toClient = true)
    public UUID owner_id = Util.NIL_UUID;

    @SerialField(toClient = true)
    public String owner_name = "";

    @SerialField(toClient = true)
    public Item item = Items.AIR;

    @SerialField(toClient = true)
    public CompoundTag config = new CompoundTag();

    private LazyOptional<IItemHandler> handler;

    private boolean added = false;

    public EnderDrawerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (this.f_58857_ == null || this.f_58859_ || cap != ForgeCapabilities.ITEM_HANDLER) {
            return super.getCapability(cap, side);
        } else if (this.f_58857_.isClientSide()) {
            return LazyOptional.<InvWrapper>of(() -> new InvWrapper(new SimpleContainer(64))).cast();
        } else {
            if (this.handler == null) {
                this.handler = this.owner_id == null ? LazyOptional.empty() : LazyOptional.of(() -> new EnderDrawerItemHandler(this.getAccess(), true));
            }
            return this.handler.cast();
        }
    }

    public DrawerAccess getAccess() {
        return DrawerAccess.of(this.f_58857_, this.owner_id, this.item);
    }

    public void onChunkUnloaded() {
        this.removeFromListener();
        super.onChunkUnloaded();
    }

    @Override
    public void setRemoved() {
        this.removeFromListener();
        super.m_7651_();
    }

    public void onLoad() {
        super.onLoad();
        this.addToListener();
    }

    public void addToListener() {
        if (!this.added && this.f_58857_ != null && !this.f_58857_.isClientSide() && this.owner_id != null) {
            this.added = true;
            this.getAccess().listener.add(this);
        }
    }

    public void removeFromListener() {
        if (this.added && this.f_58857_ != null && !this.f_58857_.isClientSide() && this.owner_id != null) {
            this.added = false;
            this.getAccess().listener.remove(this);
        }
    }

    @Override
    public Item getItem() {
        return this.item;
    }
}