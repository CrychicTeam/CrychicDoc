package noobanidus.mods.lootr.block.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.block.LootrBarrelBlock;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.data.SpecialChestInventory;
import noobanidus.mods.lootr.init.ModBlockEntities;
import noobanidus.mods.lootr.util.ChestUtil;
import noobanidus.mods.lootr.util.Getter;
import org.jetbrains.annotations.NotNull;

public class LootrBarrelBlockEntity extends RandomizableContainerBlockEntity implements ILootBlockEntity {

    public Set<UUID> openers = new HashSet();

    protected ResourceLocation savedLootTable = null;

    protected long seed = -1L;

    protected UUID tileId = null;

    protected boolean opened = false;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(Level leve, BlockPos pos, BlockState state) {
            LootrBarrelBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
            LootrBarrelBlockEntity.this.updateBlockState(state, true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            LootrBarrelBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
            LootrBarrelBlockEntity.this.updateBlockState(state, false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int p_155069_, int p_155070_) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (!(player.containerMenu instanceof ChestMenu) || !(((ChestMenu) player.containerMenu).getContainer() instanceof SpecialChestInventory data)) {
                return false;
            } else {
                return data.getTileId() == null ? data.getBlockEntity(LootrBarrelBlockEntity.this.m_58904_()) == LootrBarrelBlockEntity.this : data.getTileId().equals(LootrBarrelBlockEntity.this.getTileId());
            }
        }
    };

    private ModelData modelData = null;

    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    private boolean savingToItem = false;

    public LootrBarrelBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.LOOTR_BARREL.get(), pWorldPosition, pBlockState);
    }

    @NotNull
    public ModelData getModelData() {
        if (this.modelData == null) {
            this.modelData = ModelData.builder().with(LootrBarrelBlock.OPENED, false).build();
        }
        Player player = Getter.getPlayer();
        return player != null ? this.modelData.derive().with(LootrBarrelBlock.OPENED, this.openers.contains(player.m_20148_())).build() : this.modelData;
    }

    @Override
    public UUID getTileId() {
        if (this.tileId == null) {
            this.tileId = UUID.randomUUID();
        }
        return this.tileId;
    }

    @Override
    public void setLootTable(ResourceLocation lootTableIn, long seedIn) {
        this.savedLootTable = lootTableIn;
        this.seed = seedIn;
        super.setLootTable(lootTableIn, seedIn);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
    }

    @Override
    public void unpackLootTable(@Nullable Player player) {
    }

    @Override
    public void unpackLootTable(Player player, Container inventory, @Nullable ResourceLocation overrideTable, long seed) {
        if (this.f_58857_ != null && this.savedLootTable != null && this.f_58857_.getServer() != null) {
            LootTable loottable = this.f_58857_.getServer().getLootData().m_278676_(overrideTable != null ? overrideTable : this.savedLootTable);
            if (loottable == LootTable.EMPTY) {
                LootrAPI.LOG.error("Unable to fill loot barrel in " + this.f_58857_.dimension().location() + " at " + this.f_58858_ + " as the loot table '" + (overrideTable != null ? overrideTable : this.savedLootTable) + "' couldn't be resolved! Please search the loot table in `latest.log` to see if there are errors in loading.");
                if (ConfigManager.REPORT_UNRESOLVED_TABLES.get()) {
                    player.displayClientMessage(ChestUtil.getInvalidTable(overrideTable != null ? overrideTable : this.savedLootTable), false);
                }
            }
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer) player, overrideTable != null ? overrideTable : this.f_59605_);
            }
            LootParams.Builder builder = new LootParams.Builder((ServerLevel) this.f_58857_).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.f_58858_));
            if (player != null) {
                builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
            }
            loottable.fill(inventory, builder.create(LootContextParamSets.CHEST), LootrAPI.getLootSeed(seed == Long.MIN_VALUE ? this.seed : seed));
        }
    }

    @Override
    public ResourceLocation getTable() {
        return this.savedLootTable;
    }

    @Override
    public BlockPos getPosition() {
        return this.m_58899_();
    }

    @Override
    public long getSeed() {
        return this.seed;
    }

    @Override
    public Set<UUID> getOpeners() {
        return this.openers;
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("specialLootChest_table", 8)) {
            this.savedLootTable = new ResourceLocation(compound.getString("specialLootChest_table"));
        }
        if (compound.contains("specialLootChest_seed", 4)) {
            this.seed = compound.getLong("specialLootChest_seed");
        }
        if (this.savedLootTable == null && compound.contains("LootTable", 8)) {
            this.savedLootTable = new ResourceLocation(compound.getString("LootTable"));
            if (compound.contains("LootTableSeed", 4)) {
                this.seed = compound.getLong("LootTableSeed");
            }
            this.setLootTable(this.savedLootTable, this.seed);
        }
        if (compound.hasUUID("tileId")) {
            this.tileId = compound.getUUID("tileId");
        }
        if (this.tileId == null) {
            this.getTileId();
        }
        if (compound.contains("LootrOpeners")) {
            ListTag openers = compound.getList("LootrOpeners", 11);
            this.openers.clear();
            for (Tag item : openers) {
                this.openers.add(NbtUtils.loadUUID(item));
            }
        }
        this.requestModelDataUpdate();
        super.m_142466_(compound);
    }

    @Override
    public void saveToItem(ItemStack itemstack) {
        this.savingToItem = true;
        super.m_187476_(itemstack);
        this.savingToItem = false;
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.m_183515_(compound);
        if (this.savedLootTable != null) {
            compound.putString("LootTable", this.savedLootTable.toString());
        }
        if (this.seed != -1L) {
            compound.putLong("LootTableSeed", this.seed);
        }
        if (!LootrAPI.shouldDiscard() && !this.savingToItem) {
            compound.putUUID("tileId", this.getTileId());
            ListTag list = new ListTag();
            for (UUID opener : this.openers) {
                list.add(NbtUtils.createUUID(opener));
            }
            compound.put("LootrOpeners", list);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.barrel");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return null;
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return LazyOptional.empty();
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public void startOpen(Player pPlayer) {
        if (!this.f_58859_ && !pPlayer.isSpectator()) {
            this.openersCounter.incrementOpeners(pPlayer, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    @Override
    public void stopOpen(Player pPlayer) {
        if (!this.f_58859_ && !pPlayer.isSpectator()) {
            this.openersCounter.decrementOpeners(pPlayer, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    public void recheckOpen() {
        if (!this.f_58859_) {
            this.openersCounter.recheckOpeners(this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    protected void updateBlockState(BlockState pState, boolean pOpen) {
        this.f_58857_.setBlock(this.m_58899_(), (BlockState) pState.m_61124_(BarrelBlock.OPEN, pOpen), 3);
    }

    protected void playSound(BlockState pState, SoundEvent pSound) {
        Vec3i vec3i = ((Direction) pState.m_61143_(BarrelBlock.FACING)).getNormal();
        double d0 = (double) this.f_58858_.m_123341_() + 0.5 + (double) vec3i.getX() / 2.0;
        double d1 = (double) this.f_58858_.m_123342_() + 0.5 + (double) vec3i.getY() / 2.0;
        double d2 = (double) this.f_58858_.m_123343_() + 0.5 + (double) vec3i.getZ() / 2.0;
        this.f_58857_.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void updatePacketViaState() {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            BlockState state = this.f_58857_.getBlockState(this.m_58899_());
            this.f_58857_.sendBlockUpdated(this.m_58899_(), state, state, 8);
        }
    }

    @Override
    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag result = super.m_5995_();
        this.saveAdditional(result);
        return result;
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::m_5995_);
    }

    public void onDataPacket(@NotNull Connection net, @NotNull ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) {
            this.load(pkt.getTag());
        }
    }
}