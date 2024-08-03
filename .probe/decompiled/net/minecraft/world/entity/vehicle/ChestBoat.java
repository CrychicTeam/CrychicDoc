package net.minecraft.world.entity.vehicle;

import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class ChestBoat extends Boat implements HasCustomInventoryScreen, ContainerEntity {

    private static final int CONTAINER_SIZE = 27;

    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);

    @Nullable
    private ResourceLocation lootTable;

    private long lootTableSeed;

    public ChestBoat(EntityType<? extends Boat> entityTypeExtendsBoat0, Level level1) {
        super(entityTypeExtendsBoat0, level1);
    }

    public ChestBoat(Level level0, double double1, double double2, double double3) {
        this(EntityType.CHEST_BOAT, level0);
        this.m_6034_(double1, double2, double3);
        this.f_19854_ = double1;
        this.f_19855_ = double2;
        this.f_19856_ = double3;
    }

    @Override
    protected float getSinglePassengerXOffset() {
        return 0.15F;
    }

    @Override
    protected int getMaxPassengers() {
        return 1;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        this.m_219943_(compoundTag0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.m_219934_(compoundTag0);
    }

    @Override
    public void destroy(DamageSource damageSource0) {
        super.destroy(damageSource0);
        this.m_219927_(damageSource0, this.m_9236_(), this);
    }

    @Override
    public void remove(Entity.RemovalReason entityRemovalReason0) {
        if (!this.m_9236_().isClientSide && entityRemovalReason0.shouldDestroy()) {
            Containers.dropContents(this.m_9236_(), this, this);
        }
        super.m_142687_(entityRemovalReason0);
    }

    @Override
    public InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        if (this.m_7310_(player0) && !player0.isSecondaryUseActive()) {
            return super.interact(player0, interactionHand1);
        } else {
            InteractionResult $$2 = this.m_268996_(player0);
            if ($$2.consumesAction()) {
                this.m_146852_(GameEvent.CONTAINER_OPEN, player0);
                PiglinAi.angerNearbyPiglins(player0, true);
            }
            return $$2;
        }
    }

    @Override
    public void openCustomInventoryScreen(Player player0) {
        player0.openMenu(this);
        if (!player0.m_9236_().isClientSide) {
            this.m_146852_(GameEvent.CONTAINER_OPEN, player0);
            PiglinAi.angerNearbyPiglins(player0, true);
        }
    }

    @Override
    public Item getDropItem() {
        return switch(this.m_28554_()) {
            case SPRUCE ->
                Items.SPRUCE_CHEST_BOAT;
            case BIRCH ->
                Items.BIRCH_CHEST_BOAT;
            case JUNGLE ->
                Items.JUNGLE_CHEST_BOAT;
            case ACACIA ->
                Items.ACACIA_CHEST_BOAT;
            case CHERRY ->
                Items.CHERRY_CHEST_BOAT;
            case DARK_OAK ->
                Items.DARK_OAK_CHEST_BOAT;
            case MANGROVE ->
                Items.MANGROVE_CHEST_BOAT;
            case BAMBOO ->
                Items.BAMBOO_CHEST_RAFT;
            default ->
                Items.OAK_CHEST_BOAT;
        };
    }

    @Override
    public void clearContent() {
        this.m_219953_();
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public ItemStack getItem(int int0) {
        return this.m_219947_(int0);
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        return this.m_219936_(int0, int1);
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        return this.m_219945_(int0);
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        this.m_219940_(int0, itemStack1);
    }

    @Override
    public SlotAccess getSlot(int int0) {
        return this.m_219951_(int0);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.m_219954_(player0);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int int0, Inventory inventory1, Player player2) {
        if (this.lootTable != null && player2.isSpectator()) {
            return null;
        } else {
            this.unpackLootTable(inventory1.player);
            return ChestMenu.threeRows(int0, inventory1, this);
        }
    }

    public void unpackLootTable(@Nullable Player player0) {
        this.m_219949_(player0);
    }

    @Nullable
    @Override
    public ResourceLocation getLootTable() {
        return this.lootTable;
    }

    @Override
    public void setLootTable(@Nullable ResourceLocation resourceLocation0) {
        this.lootTable = resourceLocation0;
    }

    @Override
    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    @Override
    public void setLootTableSeed(long long0) {
        this.lootTableSeed = long0;
    }

    @Override
    public NonNullList<ItemStack> getItemStacks() {
        return this.itemStacks;
    }

    @Override
    public void clearItemStacks() {
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public void stopOpen(Player player0) {
        this.m_9236_().m_214171_(GameEvent.CONTAINER_CLOSE, this.m_20182_(), GameEvent.Context.of(player0));
    }
}