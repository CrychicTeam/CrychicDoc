package io.github.lightman314.lightmanscurrency.common.blockentity;

import io.github.lightman314.lightmanscurrency.api.misc.blockentity.EasyBlockEntity;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.items.CoinJarItem;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class CoinJarBlockEntity extends EasyBlockEntity {

    public static final int COIN_LIMIT = 64;

    private int color = -1;

    List<ItemStack> storage = new ArrayList();

    private final CoinJarBlockEntity.ItemViewer viewer = new CoinJarBlockEntity.ItemViewer(this);

    public int getColor() {
        return this.color >= 0 ? this.color : 16777215;
    }

    public List<ItemStack> getStorage() {
        return this.storage;
    }

    public void clearStorage() {
        this.storage.clear();
    }

    public CoinJarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COIN_JAR.get(), pos, state);
    }

    public boolean addCoin(ItemStack coin) {
        if (this.getCurrentCount() >= 64) {
            return false;
        } else if (!CoinAPI.API.IsCoin(coin, false)) {
            return false;
        } else {
            boolean foundStack = false;
            for (int i = 0; i < this.storage.size() && !foundStack; i++) {
                if (InventoryUtil.ItemMatches(coin, (ItemStack) this.storage.get(i)) && ((ItemStack) this.storage.get(i)).getCount() < ((ItemStack) this.storage.get(i)).getMaxStackSize()) {
                    ((ItemStack) this.storage.get(i)).grow(1);
                    foundStack = true;
                }
            }
            if (!foundStack) {
                ItemStack newCoin = coin.copy();
                newCoin.setCount(1);
                this.storage.add(newCoin);
            }
            if (!this.f_58857_.isClientSide) {
                BlockEntityUtil.sendUpdatePacket(this, this.writeStorage(new CompoundTag()));
            }
            return true;
        }
    }

    protected int getCurrentCount() {
        int count = 0;
        for (ItemStack stack : this.storage) {
            count += stack.getCount();
        }
        return count;
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        this.writeStorage(compound);
        if (this.color >= 0) {
            compound.putInt("Color", this.color);
        }
        super.m_183515_(compound);
    }

    protected CompoundTag writeStorage(CompoundTag compound) {
        ListTag storageList = new ListTag();
        for (ItemStack stack : this.storage) {
            storageList.add(stack.save(new CompoundTag()));
        }
        compound.put("Coins", storageList);
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("Coins")) {
            this.storage = new ArrayList();
            ListTag storageList = compound.getList("Coins", 10);
            for (int i = 0; i < storageList.size(); i++) {
                CompoundTag thisItem = storageList.getCompound(i);
                this.storage.add(ItemStack.of(thisItem));
            }
        }
        if (compound.contains("Color")) {
            this.color = compound.getInt("Color");
        }
        super.m_142466_(compound);
    }

    public void onLoad() {
        if (this.f_58857_.isClientSide) {
            BlockEntityUtil.requestUpdatePacket(this);
        }
    }

    public void writeItemTag(ItemStack item) {
        if (!this.storage.isEmpty()) {
            item.getOrCreateTag().put("JarData", this.writeStorage(new CompoundTag()));
        }
        this.writeSimpleItemTag(item);
    }

    public void writeSimpleItemTag(ItemStack item) {
        if (this.color >= 0) {
            CompoundTag compound = item.getOrCreateTag();
            CompoundTag displayTag = new CompoundTag();
            displayTag.putInt("color", this.color);
            compound.put("display", displayTag);
        }
    }

    public void readItemTag(ItemStack item) {
        if (item.hasTag()) {
            CompoundTag compound = item.getTag();
            if (compound.contains("JarData", 10)) {
                CompoundTag jarData = compound.getCompound("JarData");
                if (jarData.contains("Coins")) {
                    this.storage = new ArrayList();
                    ListTag storageList = jarData.getList("Coins", 10);
                    for (int i = 0; i < storageList.size(); i++) {
                        CompoundTag thisItem = storageList.getCompound(i);
                        this.storage.add(ItemStack.of(thisItem));
                    }
                }
            }
            if (item.getItem() instanceof CoinJarItem.Colored coloredJar) {
                this.color = coloredJar.getColor(item);
            }
            this.m_6596_();
        }
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, LazyOptional.of(() -> this.viewer)) : super.getCapability(cap, side);
    }

    private static record ItemViewer(CoinJarBlockEntity be) implements IItemHandler {

        @Override
        public int getSlots() {
            return this.be.storage.size();
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot >= 0 && slot < this.be.storage.size() ? ((ItemStack) this.be.storage.get(slot)).copy() : ItemStack.EMPTY;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return stack.copy();
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return false;
        }
    }
}