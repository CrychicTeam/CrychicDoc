package com.mna.interop.lootr;

import java.util.Set;
import java.util.UUID;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;

public class LootBlockTileWrapper<T extends RandomizableContainerBlockEntity & ILootrBridge> extends RandomizableContainerBlockEntity implements ILootBlockEntity {

    private T tile;

    public LootBlockTileWrapper(T tile) {
        super(tile.m_58903_(), tile.m_58899_(), tile.m_58900_());
        this.tile = tile;
    }

    @Override
    public void unpackLootTable(Player player, Container container, ResourceLocation lootTable, long lootTableSeed) {
        if (lootTable != null && player != null && player.m_9236_().getServer() != null) {
            LootTable loottable = player.m_9236_().getServer().getLootData().m_278676_(lootTable);
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer) player, lootTable);
            }
            LootParams.Builder lootparams = new LootParams.Builder((ServerLevel) player.m_9236_()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.getPosition()));
            if (player != null) {
                lootparams.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
            }
            loottable.fill(container, lootparams.create(LootContextParamSets.CHEST), this.getSeed());
        }
    }

    @Override
    public ResourceLocation getTable() {
        return this.tile.getLootTable();
    }

    @Override
    public BlockPos getPosition() {
        return this.tile.m_58899_();
    }

    @Override
    public long getSeed() {
        return this.tile.getLootSeed();
    }

    @Override
    public Set<UUID> getOpeners() {
        return this.tile.getOpeners();
    }

    @Override
    public UUID getTileId() {
        return this.tile.getTileId();
    }

    @Override
    public void updatePacketViaState() {
        this.tile.updatePacketViaState();
    }

    @Override
    public void setOpened(boolean opened) {
        this.tile.setLootrOpened(opened);
    }

    @Override
    public int getContainerSize() {
        return this.tile.m_6643_();
    }

    @Override
    public Component getDisplayName() {
        return this.tile.m_5446_();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return NonNullList.create();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullListItemStack0) {
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Loot Block Tile Wrapper");
    }

    @Override
    protected AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return null;
    }
}