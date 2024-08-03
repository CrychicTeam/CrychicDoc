package dev.xkmc.l2backpack.content.remote.worldchest;

import dev.xkmc.l2backpack.content.capability.DestroyMode;
import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.capability.PickupMode;
import dev.xkmc.l2backpack.content.remote.common.StorageContainer;
import dev.xkmc.l2backpack.content.remote.common.WorldStorage;
import dev.xkmc.l2backpack.init.data.LangData;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.NameSetable;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
public class WorldChestBlockEntity extends BaseBlockEntity implements MenuProvider, NameSetable, ContainerListener {

    @SerialField
    public UUID owner_id;

    @SerialField(toClient = true)
    public String owner_name;

    @SerialField
    long password;

    @SerialField(toClient = true)
    public int color;

    @SerialField(toClient = true)
    public PickupConfig config = new PickupConfig(PickupMode.NONE, DestroyMode.NONE);

    private Component name;

    private LazyOptional<IItemHandler> handler;

    private boolean added = false;

    public WorldChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (this.f_58857_ != null && !this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (!(this.f_58857_ instanceof ServerLevel sl)) {
                return LazyOptional.<InvWrapper>of(() -> new InvWrapper(new SimpleContainer(27))).cast();
            } else {
                if (this.handler == null) {
                    Optional<StorageContainer> storage = WorldStorage.get((ServerLevel) this.f_58857_).getOrCreateStorage(this.owner_id, this.color, this.password, null, null, 0L);
                    if (storage.isEmpty()) {
                        this.handler = LazyOptional.empty();
                    } else if (this.config != null && this.config.pickup() != PickupMode.NONE) {
                        this.handler = LazyOptional.of(() -> new BlockPickupInvWrapper(sl, this, (StorageContainer) storage.get(), this.config));
                    } else {
                        this.handler = LazyOptional.of(() -> new WorldChestInvWrapper(((StorageContainer) storage.get()).container, this.owner_id));
                    }
                }
                return this.handler.cast();
            }
        } else {
            return super.getCapability(cap, side);
        }
    }

    public void setColor(int color) {
        if (this.color != color) {
            this.handler = null;
            this.color = color;
            this.password = (long) color;
            this.m_6596_();
        }
    }

    public Component getName() {
        return (Component) (this.name == null ? LangData.IDS.STORAGE_OWNER.get(WorldChestItem.getName(this.owner_name)) : this.name);
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int wid, Inventory inventory, Player player) {
        if (this.f_58857_ != null && this.owner_id != null) {
            Optional<StorageContainer> storage = this.getAccess();
            return storage.isEmpty() ? null : new WorldChestContainer(wid, inventory, ((StorageContainer) storage.get()).container, (StorageContainer) storage.get(), this);
        } else {
            return null;
        }
    }

    public void setCustomName(Component component) {
        this.name = component;
    }

    public boolean stillValid(Player player) {
        assert this.f_58857_ != null;
        return this.f_58857_.getBlockEntity(this.f_58858_) != this ? false : !(player.m_20275_((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5) > 64.0);
    }

    private Optional<StorageContainer> getAccess() {
        assert this.f_58857_ != null;
        return WorldStorage.get((ServerLevel) this.f_58857_).getOrCreateStorage(this.owner_id, this.color, this.password, null, null, 0L);
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
            this.getAccess().ifPresent(e -> e.container.addListener(this));
        }
    }

    public void removeFromListener() {
        if (this.added && this.f_58857_ != null && !this.f_58857_.isClientSide() && this.owner_id != null) {
            this.added = false;
            this.getAccess().ifPresent(e -> e.container.removeListener(this));
        }
    }

    @Override
    public void containerChanged(Container container0) {
        this.m_6596_();
    }

    public void setPickupMode(PickupConfig click) {
        this.config = click;
        this.handler = null;
        this.sync();
        this.m_6596_();
    }
}