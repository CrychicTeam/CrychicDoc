package net.mehvahdjukaar.amendments.reg;

import java.util.Locale;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.SuppCompat;
import net.mehvahdjukaar.moonlight.api.block.MimicBlockTile;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.jetbrains.annotations.Nullable;

public class ModBlockProperties {

    public static final ModelDataKey<BlockState> MIMIC = MimicBlockTile.MIMIC_KEY;

    public static final ModelDataKey<ItemStack> ITEM = new ModelDataKey<>(ItemStack.class);

    public static final EnumProperty<ModBlockProperties.SignAttachment> SIGN_ATTACHMENT = EnumProperty.create("sign_attachment", ModBlockProperties.SignAttachment.class);

    public static final EnumProperty<ModBlockProperties.BlockAttachment> BLOCK_ATTACHMENT = EnumProperty.create("attachment", ModBlockProperties.BlockAttachment.class);

    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 15);

    public static final BooleanProperty SOLID = BooleanProperty.create("solid");

    public static final BooleanProperty BOILING = BooleanProperty.create("boiling");

    public static final IntegerProperty LEVEL_1_4 = IntegerProperty.create("level", 1, 4);

    public static enum BlockAttachment implements StringRepresentable {

        BLOCK("block"),
        BEAM("beam"),
        WALL("wall"),
        PALISADE("palisade"),
        POST("post"),
        STICK("stick");

        private final String name;

        private BlockAttachment(String name) {
            this.name = name;
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
        public static ModBlockProperties.BlockAttachment get(BlockState state, BlockPos pos, LevelReader level, Direction facing) {
            if (state.m_60783_(level, pos, facing)) {
                return BLOCK;
            } else {
                ModBlockProperties.PostType postType = ModBlockProperties.PostType.get(state, true);
                if (postType != null) {
                    return switch(postType) {
                        case BEAM ->
                            BEAM;
                        case WALL ->
                            WALL;
                        case PALISADE ->
                            PALISADE;
                        case POST ->
                            POST;
                    };
                } else {
                    return (!CompatHandler.SUPPLEMENTARIES || !SuppCompat.isVerticalStick(state, facing)) && (!(state.m_60734_() instanceof EndRodBlock) || ((Direction) state.m_61143_(EndRodBlock.f_52588_)).getAxis() != Direction.Axis.Y) ? null : STICK;
                }
            }
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
                type = POST;
            } else if (state.m_204336_(ModTags.PALISADES)) {
                type = PALISADE;
            } else if (state.m_204336_(ModTags.WALLS)) {
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
            return type;
        }
    }

    public static enum SignAttachment implements StringRepresentable {

        CEILING("ceiling"),
        BLOCK_BLOCK(ModBlockProperties.BlockAttachment.BLOCK, ModBlockProperties.BlockAttachment.BLOCK),
        BLOCK_BEAM(ModBlockProperties.BlockAttachment.BLOCK, ModBlockProperties.BlockAttachment.BEAM),
        BLOCK_WALL(ModBlockProperties.BlockAttachment.BLOCK, ModBlockProperties.BlockAttachment.WALL),
        BLOCK_PALISADE(ModBlockProperties.BlockAttachment.BLOCK, ModBlockProperties.BlockAttachment.PALISADE),
        BLOCK_POST(ModBlockProperties.BlockAttachment.BLOCK, ModBlockProperties.BlockAttachment.POST),
        BEAM_BLOCK(ModBlockProperties.BlockAttachment.BEAM, ModBlockProperties.BlockAttachment.BLOCK),
        BEAM_BEAM(ModBlockProperties.BlockAttachment.BEAM, ModBlockProperties.BlockAttachment.BEAM),
        BEAM_WALL(ModBlockProperties.BlockAttachment.BEAM, ModBlockProperties.BlockAttachment.WALL),
        BEAM_PALISADE(ModBlockProperties.BlockAttachment.BEAM, ModBlockProperties.BlockAttachment.PALISADE),
        BEAM_POST(ModBlockProperties.BlockAttachment.BEAM, ModBlockProperties.BlockAttachment.POST),
        WALL_BLOCK(ModBlockProperties.BlockAttachment.WALL, ModBlockProperties.BlockAttachment.BLOCK),
        WALL_BEAM(ModBlockProperties.BlockAttachment.WALL, ModBlockProperties.BlockAttachment.BEAM),
        WALL_WALL(ModBlockProperties.BlockAttachment.WALL, ModBlockProperties.BlockAttachment.WALL),
        WALL_PALISADE(ModBlockProperties.BlockAttachment.WALL, ModBlockProperties.BlockAttachment.PALISADE),
        WALL_POST(ModBlockProperties.BlockAttachment.WALL, ModBlockProperties.BlockAttachment.POST),
        PALISADE_BLOCK(ModBlockProperties.BlockAttachment.PALISADE, ModBlockProperties.BlockAttachment.BLOCK),
        PALISADE_BEAM(ModBlockProperties.BlockAttachment.PALISADE, ModBlockProperties.BlockAttachment.BEAM),
        PALISADE_WALL(ModBlockProperties.BlockAttachment.PALISADE, ModBlockProperties.BlockAttachment.WALL),
        PALISADE_PALISADE(ModBlockProperties.BlockAttachment.PALISADE, ModBlockProperties.BlockAttachment.PALISADE),
        PALISADE_POST(ModBlockProperties.BlockAttachment.PALISADE, ModBlockProperties.BlockAttachment.POST),
        POST_BLOCK(ModBlockProperties.BlockAttachment.POST, ModBlockProperties.BlockAttachment.BLOCK),
        POST_BEAM(ModBlockProperties.BlockAttachment.POST, ModBlockProperties.BlockAttachment.BEAM),
        POST_WALL(ModBlockProperties.BlockAttachment.POST, ModBlockProperties.BlockAttachment.WALL),
        POST_PALISADE(ModBlockProperties.BlockAttachment.POST, ModBlockProperties.BlockAttachment.PALISADE),
        POST_POST(ModBlockProperties.BlockAttachment.POST, ModBlockProperties.BlockAttachment.POST),
        STICK_BLOCK(ModBlockProperties.BlockAttachment.STICK, ModBlockProperties.BlockAttachment.BLOCK),
        STICK_BEAM(ModBlockProperties.BlockAttachment.STICK, ModBlockProperties.BlockAttachment.BEAM),
        STICK_WALL(ModBlockProperties.BlockAttachment.STICK, ModBlockProperties.BlockAttachment.WALL),
        STICK_PALISADE(ModBlockProperties.BlockAttachment.STICK, ModBlockProperties.BlockAttachment.PALISADE),
        STICK_POST(ModBlockProperties.BlockAttachment.STICK, ModBlockProperties.BlockAttachment.POST),
        STICK_STICK(ModBlockProperties.BlockAttachment.STICK, ModBlockProperties.BlockAttachment.STICK),
        BLOCK_STICK(ModBlockProperties.BlockAttachment.BLOCK, ModBlockProperties.BlockAttachment.STICK),
        BEAM_STICK(ModBlockProperties.BlockAttachment.BEAM, ModBlockProperties.BlockAttachment.STICK),
        WALL_STICK(ModBlockProperties.BlockAttachment.WALL, ModBlockProperties.BlockAttachment.STICK),
        PALISADE_STICK(ModBlockProperties.BlockAttachment.PALISADE, ModBlockProperties.BlockAttachment.STICK),
        POST_STICK(ModBlockProperties.BlockAttachment.POST, ModBlockProperties.BlockAttachment.STICK);

        public final ModBlockProperties.BlockAttachment left;

        public final ModBlockProperties.BlockAttachment right;

        private final String name;

        private SignAttachment(ModBlockProperties.BlockAttachment left, ModBlockProperties.BlockAttachment right) {
            this.name = left.name + "_" + right.name;
            this.left = left;
            this.right = right;
        }

        private SignAttachment(String name) {
            this.name = name;
            this.left = ModBlockProperties.BlockAttachment.BLOCK;
            this.right = ModBlockProperties.BlockAttachment.BLOCK;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public ModBlockProperties.SignAttachment withAttachment(boolean left, @Nullable ModBlockProperties.BlockAttachment attachment) {
            if (attachment == null) {
                attachment = ModBlockProperties.BlockAttachment.BLOCK;
            }
            String s = left ? attachment.name + "_" + this.right : this.left + "_" + attachment.name;
            return valueOf(s.toUpperCase(Locale.ROOT));
        }
    }
}