package com.simibubi.create.content.equipment.toolbox;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.ResetableLazy;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ToolboxBlockEntity extends SmartBlockEntity implements MenuProvider, Nameable {

    public LerpedFloat lid = LerpedFloat.linear().startWithValue(0.0);

    public LerpedFloat drawers = LerpedFloat.linear().startWithValue(0.0);

    UUID uniqueId;

    ToolboxInventory inventory;

    LazyOptional<IItemHandler> inventoryProvider;

    ResetableLazy<DyeColor> colorProvider;

    protected int openCount;

    Map<Integer, WeakHashMap<Player, Integer>> connectedPlayers = new HashMap();

    private Component customName;

    public ToolboxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = new ToolboxInventory(this);
        this.inventoryProvider = LazyOptional.of(() -> this.inventory);
        this.colorProvider = ResetableLazy.of(() -> {
            BlockState blockState = this.m_58900_();
            return blockState != null && blockState.m_60734_() instanceof ToolboxBlock ? ((ToolboxBlock) blockState.m_60734_()).getColor() : DyeColor.BROWN;
        });
        this.setLazyTickRate(10);
    }

    public DyeColor getColor() {
        return this.colorProvider.get();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void initialize() {
        super.initialize();
        ToolboxHandler.onLoad(this);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        ToolboxHandler.onUnload(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_58857_.isClientSide) {
            this.tickAudio();
        }
        if (!this.f_58857_.isClientSide) {
            this.tickPlayers();
        }
        this.lid.chase(this.openCount > 0 ? 1.0 : 0.0, 0.2F, LerpedFloat.Chaser.LINEAR);
        this.drawers.chase(this.openCount > 0 ? 1.0 : 0.0, 0.2F, LerpedFloat.Chaser.EXP);
        this.lid.tickChaser();
        this.drawers.tickChaser();
    }

    private void tickPlayers() {
        boolean update = false;
        Iterator<Entry<Integer, WeakHashMap<Player, Integer>>> toolboxSlots = this.connectedPlayers.entrySet().iterator();
        while (toolboxSlots.hasNext()) {
            Entry<Integer, WeakHashMap<Player, Integer>> toolboxSlotEntry = (Entry<Integer, WeakHashMap<Player, Integer>>) toolboxSlots.next();
            WeakHashMap<Player, Integer> set = (WeakHashMap<Player, Integer>) toolboxSlotEntry.getValue();
            int slot = (Integer) toolboxSlotEntry.getKey();
            ItemStack referenceItem = (ItemStack) this.inventory.filters.get(slot);
            boolean clear = referenceItem.isEmpty();
            Iterator<Entry<Player, Integer>> playerEntries = set.entrySet().iterator();
            while (playerEntries.hasNext()) {
                Entry<Player, Integer> playerEntry = (Entry<Player, Integer>) playerEntries.next();
                Player player = (Player) playerEntry.getKey();
                int hotbarSlot = (Integer) playerEntry.getValue();
                if (clear || ToolboxHandler.withinRange(player, this)) {
                    Inventory playerInv = player.getInventory();
                    ItemStack playerStack = playerInv.getItem(hotbarSlot);
                    if (!clear && (playerStack.isEmpty() || ToolboxInventory.canItemsShareCompartment(playerStack, referenceItem))) {
                        int count = playerStack.getCount();
                        int targetAmount = (referenceItem.getMaxStackSize() + 1) / 2;
                        if (count < targetAmount) {
                            int amountToReplenish = targetAmount - count;
                            if (this.isOpenInContainer(player)) {
                                ItemStack extracted = this.inventory.takeFromCompartment(amountToReplenish, slot, true);
                                if (!extracted.isEmpty()) {
                                    ToolboxHandler.unequip(player, hotbarSlot, false);
                                    ToolboxHandler.syncData(player);
                                    continue;
                                }
                            }
                            ItemStack extracted = this.inventory.takeFromCompartment(amountToReplenish, slot, false);
                            if (!extracted.isEmpty()) {
                                update = true;
                                ItemStack template = playerStack.isEmpty() ? extracted : playerStack;
                                playerInv.setItem(hotbarSlot, ItemHandlerHelper.copyStackWithSize(template, count + extracted.getCount()));
                            }
                        }
                        if (count > targetAmount) {
                            int amountToDeposit = count - targetAmount;
                            ItemStack toDistribute = ItemHandlerHelper.copyStackWithSize(playerStack, amountToDeposit);
                            if (this.isOpenInContainer(player)) {
                                int deposited = amountToDeposit - this.inventory.distributeToCompartment(toDistribute, slot, true).getCount();
                                if (deposited > 0) {
                                    ToolboxHandler.unequip(player, hotbarSlot, true);
                                    ToolboxHandler.syncData(player);
                                    continue;
                                }
                            }
                            int deposited = amountToDeposit - this.inventory.distributeToCompartment(toDistribute, slot, false).getCount();
                            if (deposited > 0) {
                                update = true;
                                playerInv.setItem(hotbarSlot, ItemHandlerHelper.copyStackWithSize(playerStack, count - deposited));
                            }
                        }
                    } else {
                        player.getPersistentData().getCompound("CreateToolboxData").remove(String.valueOf(hotbarSlot));
                        playerEntries.remove();
                        if (player instanceof ServerPlayer) {
                            ToolboxHandler.syncData(player);
                        }
                    }
                }
            }
            if (clear) {
                toolboxSlots.remove();
            }
        }
        if (update) {
            this.sendData();
        }
    }

    private boolean isOpenInContainer(Player player) {
        return player.containerMenu instanceof ToolboxMenu && ((ToolboxMenu) player.containerMenu).contentHolder == this;
    }

    public void unequipTracked() {
        if (!this.f_58857_.isClientSide) {
            Set<ServerPlayer> affected = new HashSet();
            for (Entry<Integer, WeakHashMap<Player, Integer>> toolboxSlotEntry : this.connectedPlayers.entrySet()) {
                WeakHashMap<Player, Integer> set = (WeakHashMap<Player, Integer>) toolboxSlotEntry.getValue();
                for (Entry<Player, Integer> playerEntry : set.entrySet()) {
                    Player player = (Player) playerEntry.getKey();
                    int hotbarSlot = (Integer) playerEntry.getValue();
                    ToolboxHandler.unequip(player, hotbarSlot, false);
                    if (player instanceof ServerPlayer) {
                        affected.add((ServerPlayer) player);
                    }
                }
            }
            for (ServerPlayer player : affected) {
                ToolboxHandler.syncData(player);
            }
            this.connectedPlayers.clear();
        }
    }

    public void unequip(int slot, Player player, int hotbarSlot, boolean keepItems) {
        if (this.connectedPlayers.containsKey(slot)) {
            ((WeakHashMap) this.connectedPlayers.get(slot)).remove(player);
            if (!keepItems) {
                Inventory playerInv = player.getInventory();
                ItemStack playerStack = playerInv.getItem(hotbarSlot);
                ItemStack toInsert = ToolboxInventory.cleanItemNBT(playerStack.copy());
                ItemStack remainder = this.inventory.distributeToCompartment(toInsert, slot, false);
                if (remainder.getCount() != toInsert.getCount()) {
                    playerInv.setItem(hotbarSlot, remainder);
                }
            }
        }
    }

    private void tickAudio() {
        Vec3 vec = VecHelper.getCenterOf(this.f_58858_);
        if (this.lid.settled()) {
            if (this.openCount > 0 && this.lid.getChaseTarget() == 0.0F) {
                this.f_58857_.playLocalSound(vec.x, vec.y, vec.z, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 0.25F, this.f_58857_.random.nextFloat() * 0.1F + 1.2F, true);
                this.f_58857_.playLocalSound(vec.x, vec.y, vec.z, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.1F, this.f_58857_.random.nextFloat() * 0.1F + 1.1F, true);
            }
            if (this.openCount == 0 && this.lid.getChaseTarget() == 1.0F) {
                this.f_58857_.playLocalSound(vec.x, vec.y, vec.z, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.1F, this.f_58857_.random.nextFloat() * 0.1F + 1.1F, true);
            }
        } else if (this.openCount == 0 && this.lid.getChaseTarget() == 0.0F && this.lid.getValue(0.0F) > 0.0625F && this.lid.getValue(1.0F) < 0.0625F) {
            this.f_58857_.playLocalSound(vec.x, vec.y, vec.z, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 0.25F, this.f_58857_.random.nextFloat() * 0.1F + 1.2F, true);
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return this.isItemHandlerCap(cap) ? this.inventoryProvider.cast() : super.getCapability(cap, side);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        super.read(compound, clientPacket);
        if (compound.contains("UniqueId", 11)) {
            this.uniqueId = compound.getUUID("UniqueId");
        }
        if (compound.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }
        if (clientPacket) {
            this.openCount = compound.getInt("OpenCount");
        }
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        if (this.uniqueId == null) {
            this.uniqueId = UUID.randomUUID();
        }
        compound.put("Inventory", this.inventory.serializeNBT());
        compound.putUUID("UniqueId", this.uniqueId);
        if (this.customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
        super.write(compound, clientPacket);
        if (clientPacket) {
            compound.putInt("OpenCount", this.openCount);
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return ToolboxMenu.create(id, inv, this);
    }

    @Override
    public void lazyTick() {
        this.updateOpenCount();
        ToolboxHandler.onLoad(this);
        super.lazyTick();
    }

    void updateOpenCount() {
        if (!this.f_58857_.isClientSide) {
            if (this.openCount != 0) {
                int prevOpenCount = this.openCount;
                this.openCount = 0;
                for (Player playerentity : this.f_58857_.m_45976_(Player.class, new AABB(this.f_58858_).inflate(8.0))) {
                    if (playerentity.containerMenu instanceof ToolboxMenu && ((ToolboxMenu) playerentity.containerMenu).contentHolder == this) {
                        this.openCount++;
                    }
                }
                if (prevOpenCount != this.openCount) {
                    this.sendData();
                }
            }
        }
    }

    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }
            this.openCount++;
            this.sendData();
        }
    }

    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            this.openCount--;
            this.sendData();
        }
    }

    public void connectPlayer(int slot, Player player, int hotbarSlot) {
        if (!this.f_58857_.isClientSide) {
            WeakHashMap<Player, Integer> map = (WeakHashMap<Player, Integer>) this.connectedPlayers.computeIfAbsent(slot, WeakHashMap::new);
            Integer previous = (Integer) map.get(player);
            if (previous != null) {
                if (previous == hotbarSlot) {
                    return;
                }
                ToolboxHandler.unequip(player, previous, false);
            }
            map.put(player, hotbarSlot);
        }
    }

    public void readInventory(CompoundTag compound) {
        this.inventory.deserializeNBT(compound);
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public boolean isFullyInitialized() {
        return this.uniqueId != null;
    }

    public void setCustomName(Component customName) {
        this.customName = customName;
    }

    @Override
    public Component getDisplayName() {
        return (Component) (this.customName != null ? this.customName : ((ToolboxBlock) AllBlocks.TOOLBOXES.get(this.getColor()).get()).m_49954_());
    }

    @Override
    public Component getCustomName() {
        return this.customName;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Override
    public Component getName() {
        return this.customName;
    }

    @Override
    public void setBlockState(BlockState state) {
        super.m_155250_(state);
        this.colorProvider.reset();
    }
}