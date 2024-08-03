package io.github.lightman314.lightmanscurrency.api.traders;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyStorage;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyHolder;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.MoneyHolder;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.MultiMoneyHolder;
import io.github.lightman314.lightmanscurrency.common.blockentity.handler.ICanCopy;
import io.github.lightman314.lightmanscurrency.common.items.TicketItem;
import io.github.lightman314.lightmanscurrency.common.menus.slots.InteractionSlot;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import io.github.lightman314.lightmanscurrency.util.ItemRequirement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TradeContext {

    private static long nextID = 0L;

    public final long id = nextID++;

    public final boolean isStorageMode;

    private final TraderData trader;

    private final Player player;

    private final PlayerReference playerReference;

    private final MultiMoneyHolder moneyHolders;

    private final InteractionSlot interactionSlot;

    private final IItemHandler itemHandler;

    private final IFluidHandler fluidTank;

    private final IEnergyStorage energyTank;

    public boolean hasTrader() {
        return this.trader != null;
    }

    public TraderData getTrader() {
        return this.trader;
    }

    public boolean hasPlayer() {
        return this.player != null;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean hasPlayerReference() {
        return this.playerReference != null;
    }

    public final PlayerReference getPlayerReference() {
        return this.playerReference;
    }

    public final void clearCache() {
        this.moneyHolders.clearCache(this);
    }

    private boolean hasInteractionSlot(String type) {
        return this.getInteractionSlot(type) != null;
    }

    private InteractionSlot getInteractionSlot(String type) {
        if (this.interactionSlot == null) {
            return null;
        } else {
            return this.interactionSlot.isType(type) ? this.interactionSlot : null;
        }
    }

    private boolean hasItemHandler() {
        return this.itemHandler != null;
    }

    private boolean hasFluidTank() {
        return this.fluidTank != null;
    }

    private boolean hasEnergyTank() {
        return this.energyTank != null;
    }

    private TradeContext(TradeContext.Builder builder) {
        this.isStorageMode = builder.storageMode;
        this.trader = builder.trader;
        this.player = builder.player;
        this.moneyHolders = new MultiMoneyHolder(builder.moneyHandlers);
        this.playerReference = builder.playerReference;
        this.interactionSlot = builder.interactionSlot;
        this.itemHandler = builder.itemHandler;
        this.fluidTank = builder.fluidHandler;
        this.energyTank = builder.energyHandler;
    }

    public boolean hasPaymentMethod() {
        return this.hasPlayer();
    }

    public boolean hasFunds(MoneyValue price) {
        return !price.isFree() && !price.isEmpty() ? this.getAvailableFunds().containsValue(price) : true;
    }

    @Nonnull
    public MoneyView getAvailableFunds() {
        return this.moneyHolders.getStoredMoney();
    }

    @Nonnull
    public List<Component> getAvailableFundsDescription() {
        List<Component> text = new ArrayList();
        this.moneyHolders.formatTooltip(text);
        return text;
    }

    public boolean getPayment(MoneyValue price) {
        if (price == null) {
            return false;
        } else if (price.isFree() || price.isEmpty()) {
            return true;
        } else if (this.moneyHolders.extractMoney(price, true).isEmpty()) {
            this.moneyHolders.extractMoney(price, false);
            return true;
        } else {
            return false;
        }
    }

    public boolean givePayment(MoneyValue price) {
        if (price == null) {
            return false;
        } else if (price.isFree()) {
            return true;
        } else if (this.moneyHolders.insertMoney(price, true).isEmpty()) {
            this.moneyHolders.insertMoney(price, false);
            return true;
        } else {
            return false;
        }
    }

    public boolean hasItem(ItemStack item) {
        if (this.hasItemHandler()) {
            return InventoryUtil.CanExtractItem(this.itemHandler, item);
        } else {
            return this.hasPlayer() ? InventoryUtil.GetItemCount(this.player.getInventory(), item) >= item.getCount() : false;
        }
    }

    public boolean hasItems(ItemStack... items) {
        for (ItemStack item : InventoryUtil.combineQueryItems(items)) {
            if (!this.hasItem(item)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasItems(List<ItemStack> items) {
        if (items == null) {
            return false;
        } else {
            for (ItemStack item : InventoryUtil.combineQueryItems(items)) {
                if (!this.hasItem(item)) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean hasItems(ItemRequirement... requirements) {
        if (this.hasItemHandler()) {
            return ItemRequirement.getFirstItemsMatchingRequirements(this.itemHandler, requirements) != null;
        } else {
            return this.hasPlayer() ? ItemRequirement.getFirstItemsMatchingRequirements(this.player.getInventory(), requirements) != null : false;
        }
    }

    public boolean hasTicket(long ticketID) {
        if (this.hasItemHandler()) {
            for (int i = 0; i < this.itemHandler.getSlots(); i++) {
                ItemStack stack = this.itemHandler.getStackInSlot(i);
                if (TicketItem.isTicket(stack)) {
                    long id = TicketItem.GetTicketID(stack);
                    if (id == ticketID) {
                        ItemStack copyStack = stack.copy();
                        copyStack.setCount(1);
                        if (InventoryUtil.CanExtractItem(this.itemHandler, copyStack)) {
                            return true;
                        }
                    }
                }
            }
        } else if (this.hasPlayer()) {
            Inventory inventory = this.player.getInventory();
            for (int ix = 0; ix < inventory.getContainerSize(); ix++) {
                ItemStack stack = inventory.getItem(ix);
                if (TicketItem.isTicket(stack)) {
                    long id = TicketItem.GetTicketID(stack);
                    if (id == ticketID) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasPass(long ticketID) {
        if (this.hasItemHandler()) {
            for (int i = 0; i < this.itemHandler.getSlots(); i++) {
                ItemStack stack = this.itemHandler.getStackInSlot(i);
                if (TicketItem.isPass(stack)) {
                    long id = TicketItem.GetTicketID(stack);
                    if (id == ticketID) {
                        return true;
                    }
                }
            }
        } else if (this.hasPlayer()) {
            Inventory inventory = this.player.getInventory();
            for (int ix = 0; ix < inventory.getContainerSize(); ix++) {
                ItemStack stack = inventory.getItem(ix);
                if (TicketItem.isPass(stack)) {
                    long id = TicketItem.GetTicketID(stack);
                    if (id == ticketID) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean collectItem(ItemStack item) {
        if (this.hasItem(item)) {
            if (this.hasItemHandler()) {
                InventoryUtil.RemoveItemCount(this.itemHandler, item);
                return true;
            }
            if (this.hasPlayer()) {
                InventoryUtil.RemoveItemCount(this.player.getInventory(), item);
                return true;
            }
        }
        return false;
    }

    public boolean collectItems(List<ItemStack> items) {
        items = InventoryUtil.combineQueryItems(items);
        for (ItemStack item : items) {
            if (!this.hasItem(item)) {
                return false;
            }
        }
        for (ItemStack itemx : items) {
            this.collectItem(itemx);
        }
        return true;
    }

    public List<ItemStack> getCollectableItems(ItemRequirement... requirements) {
        if (this.hasItemHandler()) {
            return ItemRequirement.getFirstItemsMatchingRequirements(this.itemHandler, requirements);
        } else {
            return this.hasPlayer() ? ItemRequirement.getFirstItemsMatchingRequirements(this.player.getInventory(), requirements) : null;
        }
    }

    public void hightlightItems(List<ItemRequirement> requirements, List<Slot> slots, List<Integer> results) {
        if (this.hasPlayer()) {
            Map<Integer, Integer> inventoryConsumedCounts = new HashMap();
            Container inventory = this.player.getInventory();
            for (ItemRequirement requirement : requirements) {
                int amountToConsume = requirement.count;
                for (int i = 0; i < inventory.getContainerSize() && amountToConsume > 0; i++) {
                    ItemStack stack = inventory.getItem(i);
                    if (requirement.test(stack) && !stack.isEmpty()) {
                        int alreadyConsumed = (Integer) inventoryConsumedCounts.getOrDefault(i, 0);
                        int consumeCount = Math.min(amountToConsume, stack.getCount() - alreadyConsumed);
                        amountToConsume -= consumeCount;
                        alreadyConsumed += consumeCount;
                        if (alreadyConsumed > 0) {
                            inventoryConsumedCounts.put(i, alreadyConsumed);
                        }
                    }
                }
            }
            for (int relevantSlot : inventoryConsumedCounts.keySet()) {
                for (int ix = 0; ix < slots.size(); ix++) {
                    Slot slot = (Slot) slots.get(ix);
                    if (slot.container == inventory && slot.getContainerSlot() == relevantSlot) {
                        results.add(ix);
                    }
                }
            }
        }
    }

    public boolean collectTicket(long ticketID) {
        if (this.hasTicket(ticketID)) {
            if (this.hasItemHandler()) {
                for (int i = 0; i < this.itemHandler.getSlots(); i++) {
                    ItemStack stack = this.itemHandler.getStackInSlot(i);
                    if (TicketItem.isTicket(stack)) {
                        long id = TicketItem.GetTicketID(stack);
                        if (id == ticketID) {
                            ItemStack extractStack = stack.copy();
                            extractStack.setCount(1);
                            if (InventoryUtil.RemoveItemCount(this.itemHandler, extractStack)) {
                                return true;
                            }
                        }
                    }
                }
            } else if (this.hasPlayer()) {
                Inventory inventory = this.player.getInventory();
                for (int ix = 0; ix < inventory.getContainerSize(); ix++) {
                    ItemStack stack = inventory.getItem(ix);
                    if (TicketItem.isTicket(stack)) {
                        long id = TicketItem.GetTicketID(stack);
                        if (id == ticketID) {
                            inventory.removeItem(ix, 1);
                            inventory.setChanged();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean canFitItem(ItemStack item) {
        if (item.isEmpty()) {
            return true;
        } else {
            return this.hasItemHandler() ? ItemHandlerHelper.insertItemStacked(this.itemHandler, item, true).isEmpty() : this.hasPlayer();
        }
    }

    public boolean canFitItems(ItemStack... items) {
        if (!this.hasItemHandler()) {
            return this.hasPlayer();
        } else {
            IItemHandler original = this.itemHandler;
            IItemHandler copy;
            if (original instanceof ICanCopy) {
                copy = (IItemHandler) ((ICanCopy) original).copy();
            } else {
                NonNullList<ItemStack> inventory = NonNullList.withSize(original.getSlots(), ItemStack.EMPTY);
                for (int i = 0; i < original.getSlots(); i++) {
                    inventory.set(i, original.getStackInSlot(i));
                }
                copy = new ItemStackHandler(inventory);
            }
            for (ItemStack item : items) {
                if (!ItemHandlerHelper.insertItemStacked(copy, item, false).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean canFitItems(List<ItemStack> items) {
        if (this.hasItemHandler()) {
            IItemHandler original = this.itemHandler;
            IItemHandler copy = null;
            if (original instanceof ICanCopy) {
                try {
                    copy = (IItemHandler) ((ICanCopy) original).copy();
                } catch (Throwable var6) {
                    LightmansCurrency.LogDebug("Error copying item handler.", var6);
                }
            }
            if (copy == null) {
                NonNullList<ItemStack> inventory = NonNullList.withSize(original.getSlots(), ItemStack.EMPTY);
                for (int i = 0; i < original.getSlots(); i++) {
                    inventory.set(i, original.getStackInSlot(i).copy());
                }
                copy = new ItemStackHandler(inventory);
            }
            for (ItemStack item : InventoryUtil.combineQueryItems(items)) {
                if (!ItemHandlerHelper.insertItemStacked(copy, item, false).isEmpty()) {
                    return false;
                }
            }
            return true;
        } else {
            return this.hasPlayer();
        }
    }

    public boolean putItem(ItemStack item) {
        if (this.canFitItem(item)) {
            if (this.hasItemHandler()) {
                ItemStack leftovers = ItemHandlerHelper.insertItemStacked(this.itemHandler, item, false);
                if (leftovers.isEmpty()) {
                    return true;
                }
                ItemStack placedStack = item.copy();
                placedStack.setCount(item.getCount() - leftovers.getCount());
                if (!item.isEmpty()) {
                    this.collectItem(placedStack);
                }
                return false;
            }
            if (this.hasPlayer()) {
                ItemHandlerHelper.giveItemToPlayer(this.player, item);
                return true;
            }
        }
        return false;
    }

    public boolean hasFluid(FluidStack fluid) {
        if (!this.hasFluidTank()) {
            if (this.hasInteractionSlot("BUCKET_SLOT")) {
                ItemStack bucketStack = this.getInteractionSlot("BUCKET_SLOT").m_7993_();
                AtomicBoolean hasFluid = new AtomicBoolean(false);
                FluidUtil.getFluidHandler(bucketStack).ifPresent(fluidHandler -> {
                    FluidStack result = fluidHandler.drain(fluid, IFluidHandler.FluidAction.SIMULATE);
                    hasFluid.set(!result.isEmpty() && result.getAmount() == fluid.getAmount());
                });
                return hasFluid.get();
            } else {
                return false;
            }
        } else {
            FluidStack result = this.fluidTank.drain(fluid, IFluidHandler.FluidAction.SIMULATE);
            return !result.isEmpty() && result.getAmount() >= fluid.getAmount();
        }
    }

    public boolean drainFluid(FluidStack fluid) {
        if (this.hasFluid(fluid)) {
            if (this.hasFluidTank()) {
                this.fluidTank.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
                return true;
            }
            if (this.hasInteractionSlot("BUCKET_SLOT")) {
                InteractionSlot slot = this.getInteractionSlot("BUCKET_SLOT");
                ItemStack bucketStack = slot.m_7993_();
                FluidUtil.getFluidHandler(bucketStack).ifPresent(fluidHandler -> {
                    fluidHandler.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
                    slot.m_5852_(fluidHandler.getContainer());
                });
                return true;
            }
        }
        return false;
    }

    public boolean canFitFluid(FluidStack fluid) {
        if (this.hasFluidTank()) {
            return this.fluidTank.fill(fluid, IFluidHandler.FluidAction.SIMULATE) == fluid.getAmount();
        } else if (this.hasInteractionSlot("BUCKET_SLOT")) {
            ItemStack bucketStack = this.getInteractionSlot("BUCKET_SLOT").m_7993_();
            AtomicBoolean fitFluid = new AtomicBoolean(false);
            FluidUtil.getFluidHandler(bucketStack).ifPresent(fluidHandler -> fitFluid.set(fluidHandler.fill(fluid, IFluidHandler.FluidAction.SIMULATE) == fluid.getAmount()));
            return fitFluid.get();
        } else {
            return false;
        }
    }

    public boolean fillFluid(FluidStack fluid) {
        if (this.canFitFluid(fluid)) {
            if (this.hasFluidTank()) {
                this.fluidTank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                return true;
            }
            if (this.hasInteractionSlot("BUCKET_SLOT")) {
                InteractionSlot slot = this.getInteractionSlot("BUCKET_SLOT");
                ItemStack bucketStack = slot.m_7993_();
                FluidUtil.getFluidHandler(bucketStack).ifPresent(fluidHandler -> {
                    fluidHandler.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                    slot.m_5852_(fluidHandler.getContainer());
                });
            }
        }
        return false;
    }

    public boolean hasEnergy(int amount) {
        if (this.hasEnergyTank()) {
            return this.energyTank.extractEnergy(amount, true) == amount;
        } else if (this.hasInteractionSlot("ENERGY_SLOT")) {
            ItemStack batteryStack = this.getInteractionSlot("ENERGY_SLOT").m_7993_();
            AtomicBoolean hasEnergy = new AtomicBoolean(false);
            batteryStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyHandler -> hasEnergy.set(energyHandler.extractEnergy(amount, true) == amount));
            return hasEnergy.get();
        } else {
            return false;
        }
    }

    public boolean drainEnergy(int amount) {
        if (this.hasEnergy(amount)) {
            if (this.hasEnergyTank()) {
                this.energyTank.extractEnergy(amount, false);
                return true;
            }
            if (this.hasInteractionSlot("ENERGY_SLOT")) {
                ItemStack batteryStack = this.getInteractionSlot("ENERGY_SLOT").m_7993_();
                batteryStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyHandler -> energyHandler.extractEnergy(amount, false));
                return true;
            }
        }
        return false;
    }

    public boolean canFitEnergy(int amount) {
        if (this.hasEnergyTank()) {
            return this.energyTank.receiveEnergy(amount, true) == amount;
        } else if (this.hasInteractionSlot("ENERGY_SLOT")) {
            ItemStack batteryStack = this.getInteractionSlot("ENERGY_SLOT").m_7993_();
            AtomicBoolean fitsEnergy = new AtomicBoolean(false);
            batteryStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyHandler -> fitsEnergy.set(energyHandler.receiveEnergy(amount, true) == amount));
            return fitsEnergy.get();
        } else {
            return false;
        }
    }

    public boolean fillEnergy(int amount) {
        if (this.canFitEnergy(amount)) {
            if (this.hasEnergyTank()) {
                this.energyTank.receiveEnergy(amount, false);
                return true;
            }
            if (this.hasInteractionSlot("ENERGY_SLOT")) {
                ItemStack batteryStack = this.getInteractionSlot("ENERGY_SLOT").m_7993_();
                batteryStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyHandler -> energyHandler.receiveEnergy(amount, false));
                return true;
            }
        }
        return false;
    }

    public static TradeContext createStorageMode(TraderData trader) {
        return new TradeContext.Builder(trader).build();
    }

    public static TradeContext.Builder create(TraderData trader, Player player) {
        return new TradeContext.Builder(trader, player, true);
    }

    public static TradeContext.Builder create(TraderData trader, PlayerReference player) {
        return new TradeContext.Builder(trader, player);
    }

    public static class Builder {

        private final boolean storageMode;

        private final TraderData trader;

        private final Player player;

        private final PlayerReference playerReference;

        private final List<IMoneyHolder> moneyHandlers = new ArrayList();

        private InteractionSlot interactionSlot;

        private IItemHandler itemHandler;

        private IFluidHandler fluidHandler;

        private IEnergyStorage energyHandler;

        private Builder(@Nonnull TraderData trader) {
            this.storageMode = true;
            this.trader = trader;
            this.player = null;
            this.playerReference = null;
        }

        private Builder(@Nonnull TraderData trader, @Nonnull Player player, boolean playerInteractable) {
            this.trader = trader;
            this.player = player;
            this.playerReference = PlayerReference.of(player);
            this.storageMode = false;
            if (playerInteractable) {
                this.withMoneyHolder(MoneyAPI.API.GetPlayersMoneyHandler(player));
            }
        }

        private Builder(@Nonnull TraderData trader, @Nonnull PlayerReference player) {
            this.trader = trader;
            this.playerReference = player;
            this.player = null;
            this.storageMode = false;
        }

        public TradeContext.Builder withBankAccount(@Nonnull BankReference bankAccount) {
            return this.withMoneyHolder(bankAccount);
        }

        public TradeContext.Builder withCoinSlots(@Nonnull Container coinSlots) {
            return this.player == null ? this : this.withMoneyHandler(MoneyAPI.API.GetContainersMoneyHandler(coinSlots, this.player), LCText.TOOLTIP_MONEY_SOURCE_SLOTS.get(), 100);
        }

        public TradeContext.Builder withStoredCoins(@Nonnull MoneyStorage storedCoins) {
            return this.withMoneyHolder(storedCoins);
        }

        public TradeContext.Builder withMoneyHandler(@Nonnull IMoneyHandler moneyHandler, @Nonnull Component title, int priority) {
            return this.withMoneyHolder(MoneyHolder.createFromHandler(moneyHandler, title, priority));
        }

        public TradeContext.Builder withMoneyHolder(@Nonnull IMoneyHolder moneyHandler) {
            if (!this.moneyHandlers.contains(moneyHandler)) {
                this.moneyHandlers.add(moneyHandler);
            }
            return this;
        }

        public TradeContext.Builder withInteractionSlot(InteractionSlot interactionSlot) {
            this.interactionSlot = interactionSlot;
            return this;
        }

        public TradeContext.Builder withItemHandler(@Nonnull IItemHandler itemHandler) {
            this.itemHandler = itemHandler;
            return this;
        }

        public TradeContext.Builder withFluidHandler(@Nonnull IFluidHandler fluidHandler) {
            this.fluidHandler = fluidHandler;
            return this;
        }

        public TradeContext.Builder withEnergyHandler(@Nonnull IEnergyStorage energyHandler) {
            this.energyHandler = energyHandler;
            return this;
        }

        public TradeContext build() {
            return new TradeContext(this);
        }
    }
}