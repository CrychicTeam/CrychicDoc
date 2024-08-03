package dev.ftb.mods.ftbquests.block.entity;

import dev.ftb.mods.ftbquests.item.LootCrateItem;
import dev.ftb.mods.ftbquests.quest.loot.LootCrate;
import dev.ftb.mods.ftbquests.quest.loot.WeightedReward;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LootCrateOpenerBlockEntity extends BlockEntity {

    private static final LootCrateOpenerBlockEntity.ItemEntry EMPTY_ENTRY = new LootCrateOpenerBlockEntity.ItemEntry(ItemStack.EMPTY);

    private static final int MAX_ITEM_TYPES = 64;

    private UUID owner = null;

    private final Map<LootCrateOpenerBlockEntity.ItemEntry, Integer> outputs = new LinkedHashMap();

    public LootCrateOpenerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super((BlockEntityType<?>) FTBQuestsBlockEntities.LOOT_CRATE_OPENER.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.outputs.clear();
        ListTag itemTag = compoundTag.getList("Items", 10);
        itemTag.forEach(el -> {
            if (el instanceof CompoundTag tag) {
                ItemStack stack = ItemStack.of(tag.getCompound("item"));
                int amount = tag.getInt("amount");
                this.outputs.put(new LootCrateOpenerBlockEntity.ItemEntry(stack), amount);
            }
        });
        this.owner = compoundTag.hasUUID("Owner") ? compoundTag.getUUID("Owner") : null;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ListTag itemTag = new ListTag();
        this.outputs.forEach((item, amount) -> {
            CompoundTag tag = new CompoundTag();
            tag.put("item", item.stack.save(new CompoundTag()));
            tag.putInt("amount", amount);
            itemTag.add(tag);
        });
        if (!itemTag.isEmpty()) {
            compoundTag.put("Items", itemTag);
        }
        if (this.owner != null) {
            compoundTag.putUUID("Owner", this.owner);
        }
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public int getOutputCount() {
        return this.outputs.values().stream().mapToInt(v -> v).sum();
    }

    protected int _getSlots() {
        return 2;
    }

    protected ItemStack _getStackInSlot(int slot) {
        return slot == 0 ? ItemStack.EMPTY : ((LootCrateOpenerBlockEntity.ItemEntry) this.outputs.keySet().stream().findFirst().orElse(EMPTY_ENTRY)).stack;
    }

    protected ItemStack _insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot == 0 && this.f_58857_ != null && this.f_58857_.getServer() != null && !this.f_58857_.isClientSide && this.outputs.size() < 64) {
            LootCrate crate = LootCrateItem.getCrate(stack);
            if (crate == null) {
                return stack;
            } else {
                ServerPlayer player = this.owner == null ? null : this.f_58857_.getServer().getPlayerList().getPlayer(this.owner);
                boolean update = false;
                int nAttempts = stack.getCount();
                for (WeightedReward wr : crate.getTable().generateWeightedRandomRewards(this.f_58857_.getRandom(), nAttempts, true)) {
                    List<ItemStack> stacks = new ArrayList();
                    if (wr.getReward().automatedClaimPre(this, stacks, this.f_58857_.random, this.owner, player)) {
                        update = true;
                        if (!simulate) {
                            for (ItemStack stack1 : stacks) {
                                LootCrateOpenerBlockEntity.ItemEntry entry = new LootCrateOpenerBlockEntity.ItemEntry(stack1);
                                int newAmount = (Integer) this.outputs.getOrDefault(entry, 0) + stack1.getCount();
                                this.outputs.put(entry, newAmount);
                            }
                            wr.getReward().automatedClaimPost(this, this.owner, player);
                        }
                    }
                }
                if (update && !simulate) {
                    this.m_6596_();
                }
                return ItemStack.EMPTY;
            }
        } else {
            return stack;
        }
    }

    protected boolean _isItemValid(int slot, ItemStack stack) {
        return slot == 0 && LootCrateItem.getCrate(stack) != null;
    }

    protected ItemStack _extractItem(int slot, int amount, boolean simulate) {
        if (this.f_58857_ != null && slot != 0 && amount > 0 && !this.outputs.isEmpty()) {
            LootCrateOpenerBlockEntity.ItemEntry entry = (LootCrateOpenerBlockEntity.ItemEntry) this.outputs.keySet().stream().findFirst().orElseThrow();
            ItemStack stack1 = entry.stack().copy();
            int count = (Integer) this.outputs.get(entry);
            int toExtract = Math.min(count, Math.min(amount, stack1.getMaxStackSize()));
            stack1.setCount(toExtract);
            if (!simulate && !this.f_58857_.isClientSide) {
                count -= toExtract;
                if (count <= 0) {
                    this.outputs.remove(entry);
                } else {
                    this.outputs.put(entry, count);
                }
                this.m_6596_();
            }
            return stack1;
        } else {
            return ItemStack.EMPTY;
        }
    }

    private static record ItemEntry(ItemStack stack) {

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                LootCrateOpenerBlockEntity.ItemEntry itemEntry = (LootCrateOpenerBlockEntity.ItemEntry) o;
                return this.stack.getItem() == itemEntry.stack.getItem() && (this.stack.getTag() == null || this.stack.getTag().equals(itemEntry.stack.getTag()));
            } else {
                return false;
            }
        }

        public int hashCode() {
            return this.stack.getTag() != null ? Objects.hash(new Object[] { this.stack.getItem(), this.stack.getTag().hashCode() }) : Objects.hash(new Object[] { this.stack.getItem() });
        }
    }
}