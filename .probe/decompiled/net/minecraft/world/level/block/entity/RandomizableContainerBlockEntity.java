package net.minecraft.world.level.block.entity;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public abstract class RandomizableContainerBlockEntity extends BaseContainerBlockEntity {

    public static final String LOOT_TABLE_TAG = "LootTable";

    public static final String LOOT_TABLE_SEED_TAG = "LootTableSeed";

    @Nullable
    protected ResourceLocation lootTable;

    protected long lootTableSeed;

    protected RandomizableContainerBlockEntity(BlockEntityType<?> blockEntityType0, BlockPos blockPos1, BlockState blockState2) {
        super(blockEntityType0, blockPos1, blockState2);
    }

    public static void setLootTable(BlockGetter blockGetter0, RandomSource randomSource1, BlockPos blockPos2, ResourceLocation resourceLocation3) {
        BlockEntity $$4 = blockGetter0.getBlockEntity(blockPos2);
        if ($$4 instanceof RandomizableContainerBlockEntity) {
            ((RandomizableContainerBlockEntity) $$4).setLootTable(resourceLocation3, randomSource1.nextLong());
        }
    }

    protected boolean tryLoadLootTable(CompoundTag compoundTag0) {
        if (compoundTag0.contains("LootTable", 8)) {
            this.lootTable = new ResourceLocation(compoundTag0.getString("LootTable"));
            this.lootTableSeed = compoundTag0.getLong("LootTableSeed");
            return true;
        } else {
            return false;
        }
    }

    protected boolean trySaveLootTable(CompoundTag compoundTag0) {
        if (this.lootTable == null) {
            return false;
        } else {
            compoundTag0.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                compoundTag0.putLong("LootTableSeed", this.lootTableSeed);
            }
            return true;
        }
    }

    public void unpackLootTable(@Nullable Player player0) {
        if (this.lootTable != null && this.f_58857_.getServer() != null) {
            LootTable $$1 = this.f_58857_.getServer().getLootData().m_278676_(this.lootTable);
            if (player0 instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer) player0, this.lootTable);
            }
            this.lootTable = null;
            LootParams.Builder $$2 = new LootParams.Builder((ServerLevel) this.f_58857_).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.f_58858_));
            if (player0 != null) {
                $$2.withLuck(player0.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player0);
            }
            $$1.fill(this, $$2.create(LootContextParamSets.CHEST), this.lootTableSeed);
        }
    }

    public void setLootTable(ResourceLocation resourceLocation0, long long1) {
        this.lootTable = resourceLocation0;
        this.lootTableSeed = long1;
    }

    @Override
    public boolean isEmpty() {
        this.unpackLootTable(null);
        return this.getItems().stream().allMatch(ItemStack::m_41619_);
    }

    @Override
    public ItemStack getItem(int int0) {
        this.unpackLootTable(null);
        return this.getItems().get(int0);
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        this.unpackLootTable(null);
        ItemStack $$2 = ContainerHelper.removeItem(this.getItems(), int0, int1);
        if (!$$2.isEmpty()) {
            this.m_6596_();
        }
        return $$2;
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        this.unpackLootTable(null);
        return ContainerHelper.takeItem(this.getItems(), int0);
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        this.unpackLootTable(null);
        this.getItems().set(int0, itemStack1);
        if (itemStack1.getCount() > this.m_6893_()) {
            itemStack1.setCount(this.m_6893_());
        }
        this.m_6596_();
    }

    @Override
    public boolean stillValid(Player player0) {
        return Container.stillValidBlockEntity(this, player0);
    }

    @Override
    public void clearContent() {
        this.getItems().clear();
    }

    protected abstract NonNullList<ItemStack> getItems();

    protected abstract void setItems(NonNullList<ItemStack> var1);

    @Override
    public boolean canOpen(Player player0) {
        return super.canOpen(player0) && (this.lootTable == null || !player0.isSpectator());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int int0, Inventory inventory1, Player player2) {
        if (this.canOpen(player2)) {
            this.unpackLootTable(inventory1.player);
            return this.m_6555_(int0, inventory1);
        } else {
            return null;
        }
    }
}