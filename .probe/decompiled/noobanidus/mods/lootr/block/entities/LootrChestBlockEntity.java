package noobanidus.mods.lootr.block.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.data.SpecialChestInventory;
import noobanidus.mods.lootr.init.ModBlockEntities;
import noobanidus.mods.lootr.util.ChestUtil;
import org.jetbrains.annotations.NotNull;

public class LootrChestBlockEntity extends ChestBlockEntity implements ILootBlockEntity {

    private final ChestLidController chestLidController = new ChestLidController();

    public Set<UUID> openers = new HashSet();

    protected ResourceLocation savedLootTable = null;

    protected long seed = -1L;

    protected boolean opened;

    protected UUID tileId;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            LootrChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            LootrChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int i, int j) {
            LootrChestBlockEntity.this.m_142151_(level, pos, state, i, j);
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu menu && menu.getContainer() instanceof SpecialChestInventory chest) {
                return LootrChestBlockEntity.this.getTileId().equals(chest.getTileId());
            }
            return false;
        }
    };

    private boolean savingToItem = false;

    protected LootrChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public LootrChestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(ModBlockEntities.LOOTR_CHEST.get(), pWorldPosition, pBlockState);
    }

    public static <T extends BlockEntity> void lootrLidAnimateTick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
        ((LootrChestBlockEntity) pBlockEntity).chestLidController.tickLid();
    }

    protected static void playSound(Level pLevel, BlockPos pPos, BlockState pState, SoundEvent pSound) {
        ChestType chesttype = (ChestType) pState.m_61143_(ChestBlock.TYPE);
        if (chesttype != ChestType.LEFT) {
            double d0 = (double) pPos.m_123341_() + 0.5;
            double d1 = (double) pPos.m_123342_() + 0.5;
            double d2 = (double) pPos.m_123343_() + 0.5;
            if (chesttype == ChestType.RIGHT) {
                Direction direction = ChestBlock.getConnectedDirection(pState);
                d0 += (double) direction.getStepX() * 0.5;
                d2 += (double) direction.getStepZ() * 0.5;
            }
            pLevel.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, pLevel.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    public static int getOpenCount(BlockGetter pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos);
        if (blockstate.m_155947_()) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof LootrChestBlockEntity) {
                return ((LootrChestBlockEntity) blockentity).openersCounter.getOpenerCount();
            }
        }
        return 0;
    }

    @Override
    public void setLootTable(ResourceLocation lootTableIn, long seedIn) {
        super.m_59626_(lootTableIn, seedIn);
        this.savedLootTable = lootTableIn;
        this.seed = seedIn;
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
            if (this.seed == 0L && compound.contains("LootTableSeed", 4)) {
                this.seed = compound.getLong("LootTableSeed");
            }
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
        super.load(compound);
    }

    @Override
    public void saveToItem(ItemStack itemstack) {
        this.savingToItem = true;
        super.m_187476_(itemstack);
        this.savingToItem = false;
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
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
    public boolean triggerEvent(int pId, int pType) {
        if (pId == 1) {
            this.chestLidController.shouldBeOpen(pType > 0);
            return true;
        } else {
            return super.triggerEvent(pId, pType);
        }
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

    @Override
    public void recheckOpen() {
        if (!this.f_58859_) {
            this.openersCounter.recheckOpeners(this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    @Override
    public float getOpenNess(float pPartialTicks) {
        return this.chestLidController.getOpenness(pPartialTicks);
    }

    @Override
    public void updatePacketViaState() {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            BlockState state = this.f_58857_.getBlockState(this.m_58899_());
            this.f_58857_.sendBlockUpdated(this.m_58899_(), state, state, 8);
        }
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

    @Override
    public void unpackLootTable(@Nullable Player player) {
    }

    @Override
    public void unpackLootTable(Player player, Container inventory, ResourceLocation overrideTable, long seed) {
        if (this.f_58857_ != null && this.savedLootTable != null && this.f_58857_.getServer() != null) {
            LootTable loottable = this.f_58857_.getServer().getLootData().m_278676_(overrideTable != null ? overrideTable : this.savedLootTable);
            if (loottable == LootTable.EMPTY) {
                LootrAPI.LOG.error("Unable to fill loot chest in " + this.f_58857_.dimension().location() + " at " + this.f_58858_ + " as the loot table '" + (overrideTable != null ? overrideTable : this.savedLootTable) + "' couldn't be resolved! Please search the loot table in `latest.log` to see if there are errors in loading.");
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

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return LazyOptional.empty();
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
    public UUID getTileId() {
        if (this.tileId == null) {
            this.tileId = UUID.randomUUID();
        }
        return this.tileId;
    }

    public boolean isOpened() {
        return this.opened;
    }

    @Override
    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}