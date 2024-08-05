package io.github.lightman314.lightmanscurrency.common.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.IClientTicker;
import io.github.lightman314.lightmanscurrency.api.misc.IServerTicker;
import io.github.lightman314.lightmanscurrency.api.misc.blockentity.EasyBlockEntity;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.upgrades.IUpgradeable;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.menus.CoinChestMenu;
import io.github.lightman314.lightmanscurrency.common.menus.containers.CoinContainer;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.upgrades.types.coin_chest.CoinChestUpgrade;
import io.github.lightman314.lightmanscurrency.common.upgrades.types.coin_chest.CoinChestUpgradeData;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;

public class CoinChestBlockEntity extends EasyBlockEntity implements IUpgradeable, IClientTicker, IServerTicker, LidBlockEntity {

    private final ChestLidController chestLidController = new ChestLidController();

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(Level level, BlockPos pos, @Nonnull BlockState state) {
            level.playSound(null, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, @Nonnull BlockState state) {
            level.playSound(null, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }

        @Override
        protected void openerCountChanged(Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, int oldCount, int newCount) {
            level.blockEvent(CoinChestBlockEntity.this.f_58858_, ModBlocks.COIN_CHEST.get(), 1, newCount);
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof CoinChestMenu ccm && ccm.be == CoinChestBlockEntity.this) {
                return true;
            }
            return false;
        }
    };

    public static final int STORAGE_ROWS = 3;

    public static final int STORAGE_SIZE = 27;

    public static final int UPGRADE_SIZE = 3;

    private final CoinChestBlockEntity.ItemHandler handler = new CoinChestBlockEntity.ItemHandler(this);

    private Component customName = null;

    private CoinContainer storage;

    private SimpleContainer upgrades;

    private List<CoinChestUpgradeData> unfilteredUpgradeDataCache = new ArrayList();

    private List<CoinChestUpgradeData> upgradeDataCache = new ArrayList();

    private boolean allowEvents = true;

    private final List<Player> relevantPlayers = new ArrayList();

    public int getOpenerCount() {
        return this.openersCounter.getOpenerCount();
    }

    public void setCustomName(Component name) {
        this.customName = name;
        this.markCustomNameDirty();
    }

    public Component getDisplayName() {
        return (Component) (this.customName != null ? this.customName : LCText.BLOCK_MONEY_CHEST.get());
    }

    public final CoinContainer getStorage() {
        return this.storage;
    }

    public final SimpleContainer getUpgrades() {
        return this.upgrades;
    }

    public final ImmutableList<CoinChestUpgradeData> getChestUpgrades() {
        if (this.unfilteredUpgradeDataCache.size() != 3) {
            this.refreshUpgradeCache();
        }
        return ImmutableList.copyOf(this.upgradeDataCache);
    }

    private void refreshUpgradeCache() {
        List<CoinChestUpgradeData> oldList = this.unfilteredUpgradeDataCache;
        this.unfilteredUpgradeDataCache = new ArrayList();
        for (int i = 0; i < 3; i++) {
            this.unfilteredUpgradeDataCache.add(CoinChestUpgradeData.forItem(this.upgrades.getItem(i), i, this::markUpgradesChanged));
            if (i < oldList.size()) {
                ((CoinChestUpgradeData) this.unfilteredUpgradeDataCache.get(i)).copyRelevantData((CoinChestUpgradeData) oldList.get(i));
            }
        }
        this.upgradeDataCache = this.unfilteredUpgradeDataCache.stream().filter(CoinChestUpgradeData::notNull).toList();
    }

    @Nullable
    public final CoinChestUpgradeData getChestUpgradeForSlot(int slot) {
        List<CoinChestUpgradeData> list = this.getChestUpgrades();
        return slot >= 0 && slot < list.size() ? (CoinChestUpgradeData) list.get(slot) : null;
    }

    public final boolean hasChestUpgradeOfType(CoinChestUpgrade type) {
        return this.getChestUpgradeOfType(type) != null;
    }

    @Nullable
    public final CoinChestUpgradeData getChestUpgradeOfType(CoinChestUpgrade type) {
        UnmodifiableIterator var2 = this.getChestUpgrades().iterator();
        while (var2.hasNext()) {
            CoinChestUpgradeData data = (CoinChestUpgradeData) var2.next();
            if (data.upgrade == type) {
                return data;
            }
        }
        return null;
    }

    public CoinChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COIN_CHEST.get(), pos, state);
        this.storage = new CoinContainer(27);
        this.storage.m_19164_(i -> this.markStorageDirty());
        this.upgrades = new SimpleContainer(3);
        this.upgrades.addListener(i -> this.markUpgradesDirty());
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.m_142466_(compound);
        if (compound.contains("Name")) {
            this.customName = Component.Serializer.fromJson(compound.getString("Name"));
        }
        if (compound.contains("Storage")) {
            this.storage = new CoinContainer(InventoryUtil.loadAllItems("Storage", compound, 27));
            this.storage.m_19164_(i -> this.markStorageDirty());
        }
        if (compound.contains("Upgrades")) {
            this.upgrades = InventoryUtil.loadAllItems("Upgrades", compound, 3);
            this.upgrades.addListener(i -> this.markUpgradesDirty());
            this.refreshUpgradeCache();
        }
    }

    public static MenuProvider getMenuProvider(CoinChestBlockEntity be) {
        return new CoinChestBlockEntity.CoinChestMenuProvider(be);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound) {
        super.m_183515_(compound);
        this.saveCustomName(compound);
        this.saveStorage(compound);
        this.saveUpgrades(compound);
    }

    protected CompoundTag saveCustomName(CompoundTag compound) {
        if (this.customName != null) {
            compound.putString("Name", Component.Serializer.toJson(this.customName));
        }
        return compound;
    }

    protected CompoundTag saveStorage(CompoundTag compound) {
        InventoryUtil.saveAllItems("Storage", compound, this.storage);
        return compound;
    }

    protected CompoundTag saveUpgrades(CompoundTag compound) {
        InventoryUtil.saveAllItems("Upgrades", compound, this.upgrades);
        return compound;
    }

    public final void markCustomNameDirty() {
        this.m_6596_();
        if (this.isServer()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveCustomName(new CompoundTag()));
        }
    }

    public final void markStorageDirty() {
        this.m_6596_();
        if (this.isServer() && this.allowEvents) {
            this.allowEvents = false;
            UnmodifiableIterator var1 = this.getChestUpgrades().iterator();
            while (var1.hasNext()) {
                CoinChestUpgradeData data = (CoinChestUpgradeData) var1.next();
                try {
                    data.upgrade.OnStorageChanged(this, data);
                } catch (Throwable var4) {
                    LightmansCurrency.LogError("Error on CoinChestUpgrade Storage Change listener!", var4);
                }
            }
            this.allowEvents = true;
            BlockEntityUtil.sendUpdatePacket(this, this.saveStorage(new CompoundTag()));
        }
    }

    public final void markUpgradesDirty() {
        this.refreshUpgradeCache();
        this.markUpgradesChanged();
    }

    private void markUpgradesChanged() {
        this.m_6596_();
        if (this.isServer()) {
            BlockEntityUtil.sendUpdatePacket(this, this.saveUpgrades(new CompoundTag()));
        }
    }

    public final void checkUpgradeEquipped(int slot) {
        if (!this.isClient()) {
            CoinChestUpgradeData data = this.getChestUpgradeForSlot(slot);
            if (data != null) {
                try {
                    data.upgrade.OnEquip(this, data);
                } catch (Throwable var7) {
                    LightmansCurrency.LogError("Error during CoinChestUpgrade's Equip listener!", var7);
                }
            }
            for (int i = 0; i < this.relevantPlayers.size(); i++) {
                Player player = (Player) this.relevantPlayers.get(i);
                if (player.containerMenu instanceof CoinChestMenu menu && menu.be == this) {
                    menu.SendMessageToClient(LazyPacketData.builder().setBoolean("RefreshTabs", true));
                    continue;
                }
                this.relevantPlayers.remove(i);
                i--;
            }
        }
    }

    @Override
    public void clientTick() {
        this.chestLidController.tickLid();
    }

    @Override
    public void serverTick() {
        UnmodifiableIterator var1 = this.getChestUpgrades().iterator();
        while (var1.hasNext()) {
            CoinChestUpgradeData data = (CoinChestUpgradeData) var1.next();
            data.tick(this);
        }
    }

    @Override
    public boolean allowUpgrade(@Nonnull UpgradeType type) {
        if (type instanceof CoinChestUpgrade upgrade && (upgrade.allowsDuplicates() || !this.hasChestUpgradeOfType(upgrade))) {
            return true;
        }
        return false;
    }

    public boolean allowAccess(Player player) {
        if (LCAdminMode.isAdminPlayer(player)) {
            return true;
        } else {
            UnmodifiableIterator var2 = this.getChestUpgrades().iterator();
            while (var2.hasNext()) {
                CoinChestUpgradeData data = (CoinChestUpgradeData) var2.next();
                if (data.upgrade.BlockAccess(this, data, player)) {
                    return false;
                }
            }
            return true;
        }
    }

    public void onValidBlockRemoval() {
        UnmodifiableIterator var1 = this.getChestUpgrades().iterator();
        while (var1.hasNext()) {
            CoinChestUpgradeData data = (CoinChestUpgradeData) var1.next();
            data.upgrade.OnValidBlockRemoval(this, data);
        }
    }

    public void onBlockRemoval() {
        UnmodifiableIterator var1 = this.getChestUpgrades().iterator();
        while (var1.hasNext()) {
            CoinChestUpgradeData data = (CoinChestUpgradeData) var1.next();
            data.upgrade.OnBlockRemoval(this, data);
        }
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, LazyOptional.of(() -> this.handler)) : super.getCapability(cap, side);
    }

    @Override
    public float getOpenNess(float partial) {
        return this.chestLidController.getOpenness(partial);
    }

    @Override
    public boolean triggerEvent(int event, int value) {
        if (event == 1) {
            this.chestLidController.shouldBeOpen(value > 0);
            return true;
        } else {
            return super.m_7531_(event, value);
        }
    }

    public void startOpen(Player player) {
        if (!this.f_58859_ && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
        if (!this.relevantPlayers.contains(player)) {
            this.relevantPlayers.add(player);
        }
    }

    public void stopOpen(Player player) {
        if (!this.f_58859_ && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
        this.relevantPlayers.remove(player);
    }

    public void recheckOpen() {
        if (!this.f_58859_) {
            this.openersCounter.recheckOpeners(this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    private static record CoinChestMenuProvider(CoinChestBlockEntity be) implements MenuProvider {

        @Nonnull
        @Override
        public Component getDisplayName() {
            return this.be.getDisplayName();
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, @Nonnull Player player) {
            return new CoinChestMenu(id, inventory, this.be);
        }
    }

    private static class ItemHandler extends InvWrapper {

        private final CoinChestBlockEntity blockEntity;

        public ItemHandler(CoinChestBlockEntity blockEntity) {
            super(blockEntity.storage);
            this.blockEntity = blockEntity;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return CoinAPI.API.IsCoin(stack, false);
        }

        @Override
        public Container getInv() {
            return this.blockEntity.storage;
        }
    }
}