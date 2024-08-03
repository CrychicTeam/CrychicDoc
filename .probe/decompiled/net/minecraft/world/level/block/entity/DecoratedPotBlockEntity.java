package net.minecraft.world.level.block.entity;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DecoratedPotBlockEntity extends BlockEntity {

    public static final String TAG_SHERDS = "sherds";

    private DecoratedPotBlockEntity.Decorations decorations = DecoratedPotBlockEntity.Decorations.EMPTY;

    public DecoratedPotBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.DECORATED_POT, blockPos0, blockState1);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        this.decorations.save(compoundTag0);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.decorations = DecoratedPotBlockEntity.Decorations.load(compoundTag0);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING);
    }

    public DecoratedPotBlockEntity.Decorations getDecorations() {
        return this.decorations;
    }

    public void setFromItem(ItemStack itemStack0) {
        this.decorations = DecoratedPotBlockEntity.Decorations.load(BlockItem.getBlockEntityData(itemStack0));
    }

    public static record Decorations(Item f_283886_, Item f_283809_, Item f_283873_, Item f_283810_) {

        private final Item back;

        private final Item left;

        private final Item right;

        private final Item front;

        public static final DecoratedPotBlockEntity.Decorations EMPTY = new DecoratedPotBlockEntity.Decorations(Items.BRICK, Items.BRICK, Items.BRICK, Items.BRICK);

        public Decorations(Item f_283886_, Item f_283809_, Item f_283873_, Item f_283810_) {
            this.back = f_283886_;
            this.left = f_283809_;
            this.right = f_283873_;
            this.front = f_283810_;
        }

        public CompoundTag save(CompoundTag p_285011_) {
            ListTag $$1 = new ListTag();
            this.sorted().forEach(p_285298_ -> $$1.add(StringTag.valueOf(BuiltInRegistries.ITEM.getKey(p_285298_).toString())));
            p_285011_.put("sherds", $$1);
            return p_285011_;
        }

        public Stream<Item> sorted() {
            return Stream.of(this.back, this.left, this.right, this.front);
        }

        public static DecoratedPotBlockEntity.Decorations load(@Nullable CompoundTag p_284959_) {
            if (p_284959_ != null && p_284959_.contains("sherds", 9)) {
                ListTag $$1 = p_284959_.getList("sherds", 8);
                return new DecoratedPotBlockEntity.Decorations(itemFromTag($$1, 0), itemFromTag($$1, 1), itemFromTag($$1, 2), itemFromTag($$1, 3));
            } else {
                return EMPTY;
            }
        }

        private static Item itemFromTag(ListTag p_285179_, int p_285060_) {
            if (p_285060_ >= p_285179_.size()) {
                return Items.BRICK;
            } else {
                Tag $$2 = p_285179_.get(p_285060_);
                return BuiltInRegistries.ITEM.get(new ResourceLocation($$2.getAsString()));
            }
        }
    }
}