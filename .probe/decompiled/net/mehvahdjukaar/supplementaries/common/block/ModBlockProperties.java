package net.mehvahdjukaar.supplementaries.common.block;

import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.block.MimicBlockTile;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.FluidContainerList;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.client.BlackboardManager;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BookPileBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SignPostBlockTile;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.DecoBlocksCompat;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.jetbrains.annotations.Nullable;

public class ModBlockProperties {

    public static final BooleanProperty EXTENDING = BooleanProperty.create("extending");

    public static final BooleanProperty HAS_WATER = BooleanProperty.create("has_water");

    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public static final BooleanProperty KNOT = BooleanProperty.create("knot");

    public static final BooleanProperty TIPPED = BooleanProperty.create("tipped");

    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");

    public static final BooleanProperty AXIS_Y = BooleanProperty.create("axis_y");

    public static final BooleanProperty AXIS_X = BooleanProperty.create("axis_x");

    public static final BooleanProperty AXIS_Z = BooleanProperty.create("axis_z");

    public static final BooleanProperty FLOOR = BooleanProperty.create("floor");

    public static final BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");

    public static final BooleanProperty ANTIQUE = BooleanProperty.create("ye_olde");

    public static final BooleanProperty TREASURE = BooleanProperty.create("treasure");

    public static final BooleanProperty PACKED = BooleanProperty.create("packed");

    public static final BooleanProperty GLOWING = BooleanProperty.create("glowing");

    public static final BooleanProperty WATCHED = BooleanProperty.create("watched");

    public static final BooleanProperty CULLED = BooleanProperty.create("culled");

    public static final BooleanProperty HAS_BLOCK = BooleanProperty.create("has_block");

    public static final BooleanProperty ROTATING = BooleanProperty.create("rotating");

    public static final IntegerProperty HOUR = IntegerProperty.create("hour", 0, 23);

    public static final IntegerProperty LIGHT_LEVEL_0_15 = IntegerProperty.create("light_level", 0, 15);

    public static final IntegerProperty LIGHT_LEVEL_5_15 = IntegerProperty.create("light_level", 5, 15);

    public static final IntegerProperty LIGHT_LEVEL_0_7 = IntegerProperty.create("light_level", 0, 7);

    public static final IntegerProperty WIND_STRENGTH = IntegerProperty.create("wind_strength", 0, 3);

    public static final IntegerProperty PANCAKES_1_8 = IntegerProperty.create("pancakes", 1, 8);

    public static final IntegerProperty ROTATION_4 = IntegerProperty.create("rotation", 0, 4);

    public static final IntegerProperty BURNING = IntegerProperty.create("burning", 0, 8);

    public static final IntegerProperty BOOKS = IntegerProperty.create("books", 1, 4);

    public static final IntegerProperty FINITE_FLUID_LEVEL = IntegerProperty.create("level", 1, 16);

    public static final IntegerProperty BALLS = IntegerProperty.create("balls", 1, 4);

    public static final EnumProperty<ModBlockProperties.Topping> TOPPING = EnumProperty.create("topping", ModBlockProperties.Topping.class);

    public static final EnumProperty<ModBlockProperties.Winding> WINDING = EnumProperty.create("winding", ModBlockProperties.Winding.class);

    public static final EnumProperty<ModBlockProperties.PostType> POST_TYPE = EnumProperty.create("type", ModBlockProperties.PostType.class);

    public static final EnumProperty<ModBlockProperties.RakeDirection> RAKE_DIRECTION = EnumProperty.create("shape", ModBlockProperties.RakeDirection.class);

    public static final EnumProperty<ModBlockProperties.DisplayStatus> ITEM_STATUS = EnumProperty.create("item_status", ModBlockProperties.DisplayStatus.class);

    public static final EnumProperty<ModBlockProperties.Rune> RUNE = EnumProperty.create("rune", ModBlockProperties.Rune.class);

    public static final EnumProperty<ModBlockProperties.Bunting> NORTH_BUNTING = EnumProperty.create("north", ModBlockProperties.Bunting.class);

    public static final EnumProperty<ModBlockProperties.Bunting> SOUTH_BUNTING = EnumProperty.create("south", ModBlockProperties.Bunting.class);

    public static final EnumProperty<ModBlockProperties.Bunting> WEST_BUNTING = EnumProperty.create("west", ModBlockProperties.Bunting.class);

    public static final EnumProperty<ModBlockProperties.Bunting> EAST_BUNTING = EnumProperty.create("east", ModBlockProperties.Bunting.class);

    public static final ModelDataKey<BlockState> MIMIC = MimicBlockTile.MIMIC_KEY;

    public static final ModelDataKey<Boolean> FANCY = new ModelDataKey<>(Boolean.class);

    public static final ModelDataKey<Boolean> FRAMED = new ModelDataKey<>(Boolean.class);

    public static final ModelDataKey<Boolean> SLIM = new ModelDataKey<>(Boolean.class);

    public static final ModelDataKey<SignPostBlockTile.Sign> SIGN_UP = new ModelDataKey<>(SignPostBlockTile.Sign.class);

    public static final ModelDataKey<SignPostBlockTile.Sign> SIGN_DOWN = new ModelDataKey<>(SignPostBlockTile.Sign.class);

    public static final ModelDataKey<BlockState> FLOWER_0 = new ModelDataKey<>(BlockState.class);

    public static final ModelDataKey<BlockState> FLOWER_1 = new ModelDataKey<>(BlockState.class);

    public static final ModelDataKey<BlockState> FLOWER_2 = new ModelDataKey<>(BlockState.class);

    public static final ModelDataKey<SoftFluid> FLUID = new ModelDataKey<>(SoftFluid.class);

    public static final ModelDataKey<Integer> FLUID_COLOR = new ModelDataKey<>(Integer.class);

    public static final ModelDataKey<Float> FILL_LEVEL = new ModelDataKey<>(Float.class);

    public static final ModelDataKey<BlackboardManager.Key> BLACKBOARD = new ModelDataKey<>(BlackboardManager.Key.class);

    public static final ModelDataKey<BookPileBlockTile.BooksList> BOOKS_KEY = new ModelDataKey<>(BookPileBlockTile.BooksList.class);

    public static enum Bunting implements StringRepresentable {

        NONE, ROPE, BUNTING;

        public String toString() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String getSerializedName() {
            return this.toString();
        }

        public boolean isConnected() {
            return this != NONE;
        }

        public boolean hasBunting() {
            return this == BUNTING;
        }
    }

    public static enum DisplayStatus implements StringRepresentable {

        NONE, EMPTY, FULL;

        public String toString() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String getSerializedName() {
            return this.toString();
        }

        public boolean hasTile() {
            return this != NONE;
        }

        public boolean hasItem() {
            return this == FULL;
        }
    }

    public static enum PostType implements StringRepresentable {

        POST("post", 4), PALISADE("palisade", 6), WALL("wall", 8), BEAM("beam", 10);

        private final String name;

        private final int width;

        private final float offset;

        private PostType(String name, int width) {
            this.name = name;
            this.width = width;
            this.offset = (8.0F - (float) width / 2.0F) / 16.0F;
        }

        public int getWidth() {
            return this.width;
        }

        public float getOffset() {
            return this.offset;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        @Nullable
        public static ModBlockProperties.PostType get(BlockState state) {
            return get(state, false);
        }

        @Nullable
        public static ModBlockProperties.PostType get(BlockState state, boolean needsFullHeight) {
            ModBlockProperties.PostType type = null;
            if (state.m_204336_(ModTags.POSTS)) {
                if (!state.m_61138_(BlockStateProperties.AXIS) || state.m_61143_(BlockStateProperties.AXIS) == Direction.Axis.Y) {
                    type = POST;
                }
            } else if (!state.m_204336_(ModTags.PALISADES) && (!CompatHandler.DECO_BLOCKS || !DecoBlocksCompat.isPalisade(state))) {
                if (state.m_204336_(ModTags.WALLS)) {
                    if (state.m_60734_() instanceof WallBlock && !(Boolean) state.m_61143_(WallBlock.UP)) {
                        if (needsFullHeight && (state.m_61143_(WallBlock.NORTH_WALL) == WallSide.LOW || state.m_61143_(WallBlock.WEST_WALL) == WallSide.LOW)) {
                            return null;
                        }
                        type = PALISADE;
                    } else {
                        type = WALL;
                    }
                } else if (state.m_204336_(ModTags.BEAMS)) {
                    if (state.m_61138_(BlockStateProperties.ATTACHED) && (Boolean) state.m_61143_(BlockStateProperties.ATTACHED)) {
                        type = null;
                    } else {
                        type = BEAM;
                    }
                }
            } else {
                type = PALISADE;
            }
            return type;
        }
    }

    public static enum RakeDirection implements StringRepresentable {

        NORTH_SOUTH("north_south", Direction.NORTH, Direction.SOUTH),
        EAST_WEST("east_west", Direction.EAST, Direction.WEST),
        SOUTH_EAST("south_east", Direction.SOUTH, Direction.EAST),
        SOUTH_WEST("south_west", Direction.SOUTH, Direction.WEST),
        NORTH_WEST("north_west", Direction.NORTH, Direction.WEST),
        NORTH_EAST("north_east", Direction.NORTH, Direction.EAST);

        private final List<Direction> directions;

        private final String name;

        private RakeDirection(String name, Direction dir1, Direction dir2) {
            this.name = name;
            this.directions = Arrays.asList(dir1, dir2);
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public List<Direction> getDirections() {
            return this.directions;
        }

        public static ModBlockProperties.RakeDirection fromDirections(List<Direction> directions) {
            for (ModBlockProperties.RakeDirection shape : values()) {
                if (new HashSet(shape.getDirections()).containsAll(directions)) {
                    return shape;
                }
            }
            return ((Direction) directions.get(0)).getAxis() == Direction.Axis.Z ? NORTH_SOUTH : EAST_WEST;
        }
    }

    public static enum Rune implements StringRepresentable {

        A("a"),
        B("b"),
        C("c"),
        D("d"),
        E("e"),
        F("f"),
        G("g"),
        H("h"),
        I("i"),
        J("j"),
        K("k"),
        L("l"),
        M("m"),
        N("n"),
        O("o"),
        P("p"),
        Q("q"),
        R("r"),
        S("s"),
        T("t"),
        U("u"),
        V("v"),
        W("w"),
        X("x"),
        Y("y"),
        Z("z");

        private final String name;

        private Rune(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public static enum Topping implements StringRepresentable {

        NONE("none"), HONEY("honey"), SYRUP("syrup"), CHOCOLATE("chocolate"), JAM("jam");

        private final String name;

        private Topping(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public static Pair<ModBlockProperties.Topping, Item> fromFluidItem(Item item) {
            Pair<SoftFluidStack, FluidContainerList.Category> holder = SoftFluidStack.fromItem(item.getDefaultInstance());
            if (holder == null) {
                return null;
            } else {
                SoftFluid s = ((SoftFluidStack) holder.getFirst()).fluid();
                FluidContainerList.Category cat = (FluidContainerList.Category) holder.getSecond();
                if (!cat.isEmpty() && cat.getAmount() == 1) {
                    ModBlockProperties.Topping t = fromFluid(s);
                    return t != NONE ? Pair.of(t, cat.getEmptyContainer()) : null;
                } else {
                    return null;
                }
            }
        }

        public static ModBlockProperties.Topping fromFluid(SoftFluid s) {
            if (s.isEmptyFluid()) {
                return NONE;
            } else if (s == BuiltInSoftFluids.HONEY.get()) {
                return HONEY;
            } else {
                String name = Utils.getID(s).getPath();
                if (name.contains("jam")) {
                    return JAM;
                } else if (name.equals("chocolate")) {
                    return CHOCOLATE;
                } else {
                    return name.equals("syrup") ? SYRUP : NONE;
                }
            }
        }

        public static Pair<ModBlockProperties.Topping, Item> fromItem(ItemStack stack) {
            Item item = stack.getItem();
            Pair<ModBlockProperties.Topping, Item> ff = fromFluidItem(item);
            if (ff != null) {
                return ff;
            } else if (stack.is(Items.SWEET_BERRIES)) {
                return Pair.of(JAM, null);
            } else if (stack.is(ModTags.SYRUP)) {
                return Pair.of(SYRUP, null);
            } else if (item instanceof HoneyBottleItem) {
                return Pair.of(HONEY, null);
            } else {
                Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM.m_203431_(ModTags.CHOCOLATE_BARS);
                return (item != Items.COCOA_BEANS || !tag.isEmpty() && !((HolderSet.Named) tag.get()).m_203614_().findAny().isEmpty()) && !stack.is(ModTags.CHOCOLATE_BARS) ? Pair.of(NONE, null) : Pair.of(CHOCOLATE, null);
            }
        }
    }

    public static enum Winding implements StringRepresentable {

        NONE("none"), CHAIN("chain"), ROPE("rope");

        private final String name;

        private Winding(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}