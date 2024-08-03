package net.minecraft.world.level.block.entity;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class BrushableBlockEntity extends BlockEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String LOOT_TABLE_TAG = "LootTable";

    private static final String LOOT_TABLE_SEED_TAG = "LootTableSeed";

    private static final String HIT_DIRECTION_TAG = "hit_direction";

    private static final String ITEM_TAG = "item";

    private static final int BRUSH_COOLDOWN_TICKS = 10;

    private static final int BRUSH_RESET_TICKS = 40;

    private static final int REQUIRED_BRUSHES_TO_BREAK = 10;

    private int brushCount;

    private long brushCountResetsAtTick;

    private long coolDownEndsAtTick;

    private ItemStack item = ItemStack.EMPTY;

    @Nullable
    private Direction hitDirection;

    @Nullable
    private ResourceLocation lootTable;

    private long lootTableSeed;

    public BrushableBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.BRUSHABLE_BLOCK, blockPos0, blockState1);
    }

    public boolean brush(long long0, Player player1, Direction direction2) {
        if (this.hitDirection == null) {
            this.hitDirection = direction2;
        }
        this.brushCountResetsAtTick = long0 + 40L;
        if (long0 >= this.coolDownEndsAtTick && this.f_58857_ instanceof ServerLevel) {
            this.coolDownEndsAtTick = long0 + 10L;
            this.unpackLootTable(player1);
            int $$3 = this.getCompletionState();
            if (++this.brushCount >= 10) {
                this.brushingCompleted(player1);
                return true;
            } else {
                this.f_58857_.m_186460_(this.m_58899_(), this.m_58900_().m_60734_(), 40);
                int $$4 = this.getCompletionState();
                if ($$3 != $$4) {
                    BlockState $$5 = this.m_58900_();
                    BlockState $$6 = (BlockState) $$5.m_61124_(BlockStateProperties.DUSTED, $$4);
                    this.f_58857_.setBlock(this.m_58899_(), $$6, 3);
                }
                return false;
            }
        } else {
            return false;
        }
    }

    public void unpackLootTable(Player player0) {
        if (this.lootTable != null && this.f_58857_ != null && !this.f_58857_.isClientSide() && this.f_58857_.getServer() != null) {
            LootTable $$1 = this.f_58857_.getServer().getLootData().m_278676_(this.lootTable);
            if (player0 instanceof ServerPlayer $$2) {
                CriteriaTriggers.GENERATE_LOOT.trigger($$2, this.lootTable);
            }
            LootParams $$3 = new LootParams.Builder((ServerLevel) this.f_58857_).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.f_58858_)).withLuck(player0.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player0).create(LootContextParamSets.CHEST);
            ObjectArrayList<ItemStack> $$4 = $$1.getRandomItems($$3, this.lootTableSeed);
            this.item = switch($$4.size()) {
                case 0 ->
                    ItemStack.EMPTY;
                case 1 ->
                    (ItemStack) $$4.get(0);
                default ->
                    {
                        LOGGER.warn("Expected max 1 loot from loot table " + this.lootTable + " got " + $$4.size());
                        ???;
                    }
            };
            this.lootTable = null;
            this.m_6596_();
        }
    }

    private void brushingCompleted(Player player0) {
        if (this.f_58857_ != null && this.f_58857_.getServer() != null) {
            this.dropContent(player0);
            BlockState $$1 = this.m_58900_();
            this.f_58857_.m_46796_(3008, this.m_58899_(), Block.getId($$1));
            Block $$4;
            if (this.m_58900_().m_60734_() instanceof BrushableBlock $$3) {
                $$4 = $$3.getTurnsInto();
            } else {
                $$4 = Blocks.AIR;
            }
            this.f_58857_.setBlock(this.f_58858_, $$4.defaultBlockState(), 3);
        }
    }

    private void dropContent(Player player0) {
        if (this.f_58857_ != null && this.f_58857_.getServer() != null) {
            this.unpackLootTable(player0);
            if (!this.item.isEmpty()) {
                double $$1 = (double) EntityType.ITEM.getWidth();
                double $$2 = 1.0 - $$1;
                double $$3 = $$1 / 2.0;
                Direction $$4 = (Direction) Objects.requireNonNullElse(this.hitDirection, Direction.UP);
                BlockPos $$5 = this.f_58858_.relative($$4, 1);
                double $$6 = (double) $$5.m_123341_() + 0.5 * $$2 + $$3;
                double $$7 = (double) $$5.m_123342_() + 0.5 + (double) (EntityType.ITEM.getHeight() / 2.0F);
                double $$8 = (double) $$5.m_123343_() + 0.5 * $$2 + $$3;
                ItemEntity $$9 = new ItemEntity(this.f_58857_, $$6, $$7, $$8, this.item.split(this.f_58857_.random.nextInt(21) + 10));
                $$9.m_20256_(Vec3.ZERO);
                this.f_58857_.m_7967_($$9);
                this.item = ItemStack.EMPTY;
            }
        }
    }

    public void checkReset() {
        if (this.f_58857_ != null) {
            if (this.brushCount != 0 && this.f_58857_.getGameTime() >= this.brushCountResetsAtTick) {
                int $$0 = this.getCompletionState();
                this.brushCount = Math.max(0, this.brushCount - 2);
                int $$1 = this.getCompletionState();
                if ($$0 != $$1) {
                    this.f_58857_.setBlock(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(BlockStateProperties.DUSTED, $$1), 3);
                }
                int $$2 = 4;
                this.brushCountResetsAtTick = this.f_58857_.getGameTime() + 4L;
            }
            if (this.brushCount == 0) {
                this.hitDirection = null;
                this.brushCountResetsAtTick = 0L;
                this.coolDownEndsAtTick = 0L;
            } else {
                this.f_58857_.m_186460_(this.m_58899_(), this.m_58900_().m_60734_(), (int) (this.brushCountResetsAtTick - this.f_58857_.getGameTime()));
            }
        }
    }

    private boolean tryLoadLootTable(CompoundTag compoundTag0) {
        if (compoundTag0.contains("LootTable", 8)) {
            this.lootTable = new ResourceLocation(compoundTag0.getString("LootTable"));
            this.lootTableSeed = compoundTag0.getLong("LootTableSeed");
            return true;
        } else {
            return false;
        }
    }

    private boolean trySaveLootTable(CompoundTag compoundTag0) {
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

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag $$0 = super.getUpdateTag();
        if (this.hitDirection != null) {
            $$0.putInt("hit_direction", this.hitDirection.ordinal());
        }
        $$0.put("item", this.item.save(new CompoundTag()));
        return $$0;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        if (!this.tryLoadLootTable(compoundTag0) && compoundTag0.contains("item")) {
            this.item = ItemStack.of(compoundTag0.getCompound("item"));
        }
        if (compoundTag0.contains("hit_direction")) {
            this.hitDirection = Direction.values()[compoundTag0.getInt("hit_direction")];
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        if (!this.trySaveLootTable(compoundTag0)) {
            compoundTag0.put("item", this.item.save(new CompoundTag()));
        }
    }

    public void setLootTable(ResourceLocation resourceLocation0, long long1) {
        this.lootTable = resourceLocation0;
        this.lootTableSeed = long1;
    }

    private int getCompletionState() {
        if (this.brushCount == 0) {
            return 0;
        } else if (this.brushCount < 3) {
            return 1;
        } else {
            return this.brushCount < 6 ? 2 : 3;
        }
    }

    @Nullable
    public Direction getHitDirection() {
        return this.hitDirection;
    }

    public ItemStack getItem() {
        return this.item;
    }
}