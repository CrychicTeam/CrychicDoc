package net.minecraft.world.entity.vehicle;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public interface ContainerEntity extends Container, MenuProvider {

    Vec3 position();

    @Nullable
    ResourceLocation getLootTable();

    void setLootTable(@Nullable ResourceLocation var1);

    long getLootTableSeed();

    void setLootTableSeed(long var1);

    NonNullList<ItemStack> getItemStacks();

    void clearItemStacks();

    Level level();

    boolean isRemoved();

    @Override
    default boolean isEmpty() {
        return this.isChestVehicleEmpty();
    }

    default void addChestVehicleSaveData(CompoundTag compoundTag0) {
        if (this.getLootTable() != null) {
            compoundTag0.putString("LootTable", this.getLootTable().toString());
            if (this.getLootTableSeed() != 0L) {
                compoundTag0.putLong("LootTableSeed", this.getLootTableSeed());
            }
        } else {
            ContainerHelper.saveAllItems(compoundTag0, this.getItemStacks());
        }
    }

    default void readChestVehicleSaveData(CompoundTag compoundTag0) {
        this.clearItemStacks();
        if (compoundTag0.contains("LootTable", 8)) {
            this.setLootTable(new ResourceLocation(compoundTag0.getString("LootTable")));
            this.setLootTableSeed(compoundTag0.getLong("LootTableSeed"));
        } else {
            ContainerHelper.loadAllItems(compoundTag0, this.getItemStacks());
        }
    }

    default void chestVehicleDestroyed(DamageSource damageSource0, Level level1, Entity entity2) {
        if (level1.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            Containers.dropContents(level1, entity2, this);
            if (!level1.isClientSide) {
                Entity $$3 = damageSource0.getDirectEntity();
                if ($$3 != null && $$3.getType() == EntityType.PLAYER) {
                    PiglinAi.angerNearbyPiglins((Player) $$3, true);
                }
            }
        }
    }

    default InteractionResult interactWithContainerVehicle(Player player0) {
        player0.openMenu(this);
        return !player0.m_9236_().isClientSide ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
    }

    default void unpackChestVehicleLootTable(@Nullable Player player0) {
        MinecraftServer $$1 = this.level().getServer();
        if (this.getLootTable() != null && $$1 != null) {
            LootTable $$2 = $$1.getLootData().m_278676_(this.getLootTable());
            if (player0 != null) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer) player0, this.getLootTable());
            }
            this.setLootTable(null);
            LootParams.Builder $$3 = new LootParams.Builder((ServerLevel) this.level()).withParameter(LootContextParams.ORIGIN, this.position());
            if (player0 != null) {
                $$3.withLuck(player0.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player0);
            }
            $$2.fill(this, $$3.create(LootContextParamSets.CHEST), this.getLootTableSeed());
        }
    }

    default void clearChestVehicleContent() {
        this.unpackChestVehicleLootTable(null);
        this.getItemStacks().clear();
    }

    default boolean isChestVehicleEmpty() {
        for (ItemStack $$0 : this.getItemStacks()) {
            if (!$$0.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    default ItemStack removeChestVehicleItemNoUpdate(int int0) {
        this.unpackChestVehicleLootTable(null);
        ItemStack $$1 = this.getItemStacks().get(int0);
        if ($$1.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.getItemStacks().set(int0, ItemStack.EMPTY);
            return $$1;
        }
    }

    default ItemStack getChestVehicleItem(int int0) {
        this.unpackChestVehicleLootTable(null);
        return this.getItemStacks().get(int0);
    }

    default ItemStack removeChestVehicleItem(int int0, int int1) {
        this.unpackChestVehicleLootTable(null);
        return ContainerHelper.removeItem(this.getItemStacks(), int0, int1);
    }

    default void setChestVehicleItem(int int0, ItemStack itemStack1) {
        this.unpackChestVehicleLootTable(null);
        this.getItemStacks().set(int0, itemStack1);
        if (!itemStack1.isEmpty() && itemStack1.getCount() > this.m_6893_()) {
            itemStack1.setCount(this.m_6893_());
        }
    }

    default SlotAccess getChestVehicleSlot(final int int0) {
        return int0 >= 0 && int0 < this.m_6643_() ? new SlotAccess() {

            @Override
            public ItemStack get() {
                return ContainerEntity.this.getChestVehicleItem(int0);
            }

            @Override
            public boolean set(ItemStack p_219964_) {
                ContainerEntity.this.setChestVehicleItem(int0, p_219964_);
                return true;
            }
        } : SlotAccess.NULL;
    }

    default boolean isChestVehicleStillValid(Player player0) {
        return !this.isRemoved() && this.position().closerThan(player0.m_20182_(), 8.0);
    }
}