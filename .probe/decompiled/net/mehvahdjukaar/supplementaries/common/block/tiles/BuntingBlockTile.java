package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.EnumMap;
import java.util.Map;
import net.mehvahdjukaar.moonlight.api.block.DynamicRenderedItemDisplayTile;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.moonlight.api.client.util.LOD;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BuntingBlock;
import net.mehvahdjukaar.supplementaries.common.items.BuntingItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BuntingBlockTile extends DynamicRenderedItemDisplayTile {

    public static final ModelDataKey<DyeColor> NORTH_BUNTING = new ModelDataKey<>(DyeColor.class);

    public static final ModelDataKey<DyeColor> SOUTH_BUNTING = new ModelDataKey<>(DyeColor.class);

    public static final ModelDataKey<DyeColor> EAST_BUNTING = new ModelDataKey<>(DyeColor.class);

    public static final ModelDataKey<DyeColor> WEST_BUNTING = new ModelDataKey<>(DyeColor.class);

    private final Map<Direction, DyeColor> buntings = new EnumMap(Direction.class);

    public BuntingBlockTile(BlockPos pos, BlockState state) {
        super(null, pos, state, 4);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Bunting");
    }

    @Override
    public void updateClientVisualsOnLoad() {
        this.buntings.clear();
        for (Direction d : Direction.Plane.HORIZONTAL) {
            ItemStack stack = this.m_8020_(d.get2DDataValue());
            if (stack.getItem() instanceof BuntingItem) {
                DyeColor color = BuntingItem.getColor(stack);
                if (color != null) {
                    this.buntings.put(d, color);
                }
            }
        }
        if (this.buntings.isEmpty()) {
            Supplementaries.error();
        }
        this.requestModelReload();
    }

    @Override
    public void updateTileOnInventoryChanged() {
        BlockState state = this.m_58900_();
        if (this.m_7983_()) {
            this.f_58857_.setBlockAndUpdate(this.f_58858_, BuntingBlock.toRope(state));
        } else {
            BlockState state2 = state;
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                EnumProperty<ModBlockProperties.Bunting> prop = (EnumProperty<ModBlockProperties.Bunting>) BuntingBlock.HORIZONTAL_FACING_TO_PROPERTY_MAP.get(dir);
                ModBlockProperties.Bunting old = (ModBlockProperties.Bunting) state2.m_61143_(prop);
                boolean isEmpty = this.m_8020_(dir.get2DDataValue()).isEmpty();
                state2 = (BlockState) state2.m_61124_(prop, isEmpty ? (old == ModBlockProperties.Bunting.NONE ? ModBlockProperties.Bunting.NONE : ModBlockProperties.Bunting.ROPE) : ModBlockProperties.Bunting.BUNTING);
            }
            if (state != state2) {
                this.f_58857_.setBlockAndUpdate(this.f_58858_, state2);
            }
        }
    }

    @Override
    public boolean needsToUpdateClientWhenChanged() {
        return false;
    }

    public Map<Direction, DyeColor> getBuntings() {
        return this.buntings;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return stack.getItem() instanceof BuntingItem && this.m_8020_(index).isEmpty() && BuntingBlock.canSupportBunting(this.m_58900_(), index);
    }

    @Override
    public boolean isNeverFancy() {
        return false;
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        super.addExtraModelData(builder);
        builder.with(NORTH_BUNTING, (DyeColor) this.buntings.getOrDefault(Direction.NORTH, null));
        builder.with(SOUTH_BUNTING, (DyeColor) this.buntings.getOrDefault(Direction.SOUTH, null));
        builder.with(EAST_BUNTING, (DyeColor) this.buntings.getOrDefault(Direction.EAST, null));
        builder.with(WEST_BUNTING, (DyeColor) this.buntings.getOrDefault(Direction.WEST, null));
    }

    @Override
    protected boolean getFancyDistance(Vec3 cameraPos) {
        LOD lod = new LOD(cameraPos, this.m_58899_());
        return lod.isNear();
    }
}