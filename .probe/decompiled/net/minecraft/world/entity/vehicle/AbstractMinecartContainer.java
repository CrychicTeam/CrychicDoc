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
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbstractMinecartContainer extends AbstractMinecart implements ContainerEntity {

    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(36, ItemStack.EMPTY);

    @Nullable
    private ResourceLocation lootTable;

    private long lootTableSeed;

    protected AbstractMinecartContainer(EntityType<?> entityType0, Level level1) {
        super(entityType0, level1);
    }

    protected AbstractMinecartContainer(EntityType<?> entityType0, double double1, double double2, double double3, Level level4) {
        super(entityType0, level4, double1, double2, double3);
    }

    @Override
    public void destroy(DamageSource damageSource0) {
        super.destroy(damageSource0);
        this.m_219927_(damageSource0, this.m_9236_(), this);
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

    @Override
    public void remove(Entity.RemovalReason entityRemovalReason0) {
        if (!this.m_9236_().isClientSide && entityRemovalReason0.shouldDestroy()) {
            Containers.dropContents(this.m_9236_(), this, this);
        }
        super.m_142687_(entityRemovalReason0);
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
    public InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        return this.m_268996_(player0);
    }

    @Override
    protected void applyNaturalSlowdown() {
        float $$0 = 0.98F;
        if (this.lootTable == null) {
            int $$1 = 15 - AbstractContainerMenu.getRedstoneSignalFromContainer(this);
            $$0 += (float) $$1 * 0.001F;
        }
        if (this.m_20069_()) {
            $$0 *= 0.95F;
        }
        this.m_20256_(this.m_20184_().multiply((double) $$0, 0.0, (double) $$0));
    }

    @Override
    public void clearContent() {
        this.m_219953_();
    }

    public void setLootTable(ResourceLocation resourceLocation0, long long1) {
        this.lootTable = resourceLocation0;
        this.lootTableSeed = long1;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int int0, Inventory inventory1, Player player2) {
        if (this.lootTable != null && player2.isSpectator()) {
            return null;
        } else {
            this.m_219949_(inventory1.player);
            return this.createMenu(int0, inventory1);
        }
    }

    protected abstract AbstractContainerMenu createMenu(int var1, Inventory var2);

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
        this.itemStacks = NonNullList.withSize(this.m_6643_(), ItemStack.EMPTY);
    }
}