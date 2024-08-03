package noobanidus.mods.lootr.block.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.init.ModBlockEntities;
import noobanidus.mods.lootr.util.ChestUtil;
import org.jetbrains.annotations.NotNull;

public class LootrShulkerBlockEntity extends RandomizableContainerBlockEntity implements ILootBlockEntity {

    public Set<UUID> openers = new HashSet();

    protected ResourceLocation savedLootTable = null;

    protected long seed = -1L;

    protected UUID tileId;

    protected boolean opened;

    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);

    private int openCount;

    private ShulkerBoxBlockEntity.AnimationStatus animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSED;

    private float progress;

    private float progressOld;

    private boolean savingToItem = false;

    public LootrShulkerBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public LootrShulkerBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(ModBlockEntities.LOOTR_SHULKER.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, LootrShulkerBlockEntity pBlockEntity) {
        pBlockEntity.updateAnimation(pLevel, pPos, pState);
    }

    private static void doNeighborUpdates(Level pLevel, BlockPos pPos, BlockState pState) {
        pState.m_60701_(pLevel, pPos, 3);
    }

    private void updateAnimation(Level pLevel, BlockPos pPos, BlockState pState) {
        this.progressOld = this.progress;
        switch(this.animationStatus) {
            case CLOSED:
                this.progress = 0.0F;
                break;
            case OPENING:
                this.progress += 0.1F;
                if (this.progress >= 1.0F) {
                    this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.OPENED;
                    this.progress = 1.0F;
                    doNeighborUpdates(pLevel, pPos, pState);
                }
                this.moveCollidedEntities(pLevel, pPos, pState);
                break;
            case CLOSING:
                this.progress -= 0.1F;
                if (this.progress <= 0.0F) {
                    this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
                    this.progress = 0.0F;
                    doNeighborUpdates(pLevel, pPos, pState);
                }
                break;
            case OPENED:
                this.progress = 1.0F;
        }
    }

    public ShulkerBoxBlockEntity.AnimationStatus getAnimationStatus() {
        return this.animationStatus;
    }

    public AABB getBoundingBox(BlockState pState) {
        return Shulker.getProgressAabb((Direction) pState.m_61143_(ShulkerBoxBlock.FACING), 0.5F * this.getProgress(1.0F));
    }

    private void moveCollidedEntities(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pState.m_60734_() instanceof ShulkerBoxBlock) {
            Direction direction = (Direction) pState.m_61143_(ShulkerBoxBlock.FACING);
            AABB aabb = Shulker.getProgressDeltaAabb(direction, this.progressOld, this.progress).move(pPos);
            List<Entity> list = pLevel.m_45933_(null, aabb);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                        entity.move(MoverType.SHULKER_BOX, new Vec3((aabb.getXsize() + 0.01) * (double) direction.getStepX(), (aabb.getYsize() + 0.01) * (double) direction.getStepY(), (aabb.getZsize() + 0.01) * (double) direction.getStepZ()));
                    }
                }
            }
        }
    }

    @Override
    public int getContainerSize() {
        return this.itemStacks.size();
    }

    @Override
    public boolean triggerEvent(int pId, int pType) {
        if (pId == 1) {
            this.openCount = pType;
            if (pType == 0) {
                this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.CLOSING;
                doNeighborUpdates(this.m_58904_(), this.f_58858_, this.m_58900_());
            }
            if (pType == 1) {
                this.animationStatus = ShulkerBoxBlockEntity.AnimationStatus.OPENING;
                doNeighborUpdates(this.m_58904_(), this.f_58858_, this.m_58900_());
            }
            return true;
        } else {
            return super.m_7531_(pId, pType);
        }
    }

    @Override
    public void startOpen(Player pPlayer) {
        if (!pPlayer.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }
            this.openCount++;
            this.f_58857_.blockEvent(this.f_58858_, this.m_58900_().m_60734_(), 1, this.openCount);
            if (this.openCount == 1) {
                this.f_58857_.m_142346_(pPlayer, GameEvent.CONTAINER_OPEN, this.f_58858_);
                this.f_58857_.playSound(null, this.f_58858_, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    @Override
    public void stopOpen(Player pPlayer) {
        if (!pPlayer.isSpectator()) {
            this.openCount--;
            this.f_58857_.blockEvent(this.f_58858_, this.m_58900_().m_60734_(), 1, this.openCount);
            if (this.openCount <= 0) {
                this.f_58857_.m_142346_(pPlayer, GameEvent.CONTAINER_CLOSE, this.f_58858_);
                this.f_58857_.playSound(null, this.f_58858_, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F);
            }
            this.openers.add(pPlayer.m_20148_());
            this.m_6596_();
            this.updatePacketViaState();
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.shulkerBox");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return null;
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
            compound.putString("specialLootChest_table", this.savedLootTable.toString());
            compound.putString("LootTable", this.savedLootTable.toString());
        }
        if (this.seed != -1L) {
            compound.putLong("specialLootChest_seed", this.seed);
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
    protected NonNullList<ItemStack> getItems() {
        return this.itemStacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
        this.itemStacks = pItems;
    }

    public float getProgress(float pPartialTicks) {
        return Mth.lerp(pPartialTicks, this.progressOld, this.progress);
    }

    public boolean isClosed() {
        return this.animationStatus == ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
    }

    @Override
    public ResourceLocation getTable() {
        return this.savedLootTable;
    }

    @Override
    public BlockPos getPosition() {
        return this.m_58899_();
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return LazyOptional.empty();
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

    @Override
    public void updatePacketViaState() {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            BlockState state = this.f_58857_.getBlockState(this.m_58899_());
            this.f_58857_.sendBlockUpdated(this.m_58899_(), state, state, 8);
        }
    }

    @Override
    public void setOpened(boolean opened) {
        this.opened = true;
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
                LootrAPI.LOG.error("Unable to fill loot shulker in " + this.f_58857_.dimension().location() + " at " + this.f_58858_ + " as the loot table '" + (overrideTable != null ? overrideTable : this.savedLootTable) + "' couldn't be resolved! Please search the loot table in `latest.log` to see if there are errors in loading.");
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
    public void setLootTable(ResourceLocation lootTableIn, long seedIn) {
        super.setLootTable(lootTableIn, seedIn);
        this.savedLootTable = lootTableIn;
        this.seed = seedIn;
    }
}