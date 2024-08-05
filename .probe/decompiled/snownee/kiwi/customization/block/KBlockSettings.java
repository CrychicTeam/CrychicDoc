package snownee.kiwi.customization.block;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.customization.block.behavior.CanSurviveHandler;
import snownee.kiwi.customization.block.component.DirectionalComponent;
import snownee.kiwi.customization.block.component.HorizontalComponent;
import snownee.kiwi.customization.block.component.KBlockComponent;
import snownee.kiwi.customization.block.component.WaterLoggableComponent;
import snownee.kiwi.customization.duck.KBlockProperties;
import snownee.kiwi.customization.placement.PlaceChoices;
import snownee.kiwi.customization.shape.BlockShapeType;
import snownee.kiwi.customization.shape.ConfiguringShape;
import snownee.kiwi.customization.shape.ShapeGenerator;

public class KBlockSettings {

    public final boolean customPlacement;

    public final GlassType glassType;

    @Nullable
    public final CanSurviveHandler canSurviveHandler;

    @Nullable
    public final ToIntFunction<BlockState> analogOutputSignal;

    public final Map<KBlockComponent.Type<?>, KBlockComponent> components;

    @Nullable
    private ShapeGenerator[] shapes;

    public PlaceChoices placeChoices;

    private KBlockSettings(KBlockSettings.Builder builder) {
        this.customPlacement = builder.customPlacement;
        this.glassType = builder.glassType;
        this.canSurviveHandler = builder.canSurviveHandler;
        this.analogOutputSignal = builder.getAnalogOutputSignal();
        this.components = Map.copyOf(builder.components);
        for (BlockShapeType type : BlockShapeType.VALUES) {
            this.setShape(type, builder.getShape(type));
        }
    }

    public static KBlockSettings empty() {
        return new KBlockSettings(builder());
    }

    public static KBlockSettings.Builder builder() {
        return new KBlockSettings.Builder(BlockBehaviour.Properties.of());
    }

    public static KBlockSettings.Builder copyProperties(Block block) {
        return new KBlockSettings.Builder(BlockBehaviour.Properties.copy(block));
    }

    public static KBlockSettings.Builder copyProperties(Block block, MapColor mapColor) {
        return new KBlockSettings.Builder(BlockBehaviour.Properties.copy(block).mapColor(mapColor));
    }

    public static KBlockSettings of(Object block) {
        return ((KBlockProperties) ((BlockBehaviour) block).properties).kiwi$getSettings();
    }

    public static VoxelShape getGlassFaceShape(BlockState blockState, Direction direction) {
        KBlockSettings settings = of(blockState.m_60734_());
        if (settings == null) {
            VoxelShape shape = blockState.m_60651_(EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
            return Block.isShapeFullBlock(shape) ? Shapes.block() : Shapes.empty();
        } else if (settings.glassType == null) {
            return Shapes.empty();
        } else {
            VoxelShape shape = blockState.m_60651_(EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
            return shape.isEmpty() ? Shapes.empty() : Shapes.getFaceShape(shape, direction);
        }
    }

    public boolean hasComponent(KBlockComponent.Type<?> type) {
        return this.components.containsKey(type);
    }

    public <T extends KBlockComponent> T getComponent(KBlockComponent.Type<T> type) {
        return (T) this.components.get(type);
    }

    public void injectProperties(Block block, StateDefinition.Builder<Block, BlockState> builder) {
        for (KBlockComponent component : this.components.values()) {
            component.injectProperties(block, builder);
        }
    }

    public BlockState registerDefaultState(BlockState state) {
        for (KBlockComponent component : this.components.values()) {
            state = component.registerDefaultState(state);
        }
        return state;
    }

    public BlockState getStateForPlacement(BlockState blockState, BlockPlaceContext context) {
        for (KBlockComponent component : this.components.values()) {
            blockState = component.getStateForPlacement(this, blockState, context);
            if (blockState == null || !blockState.m_60713_(blockState.m_60734_())) {
                return blockState;
            }
        }
        if (this.placeChoices != null && (!this.placeChoices.skippable() || !context.m_7078_())) {
            blockState = this.placeChoices.getStateForPlacement(context.m_43725_(), context.getClickedPos(), blockState);
        }
        return blockState;
    }

    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        for (KBlockComponent component : this.components.values()) {
            pState = component.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
        }
        return pState;
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        for (KBlockComponent component : this.components.values()) {
            pState = component.rotate(pState, pRotation);
        }
        return pState;
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        for (KBlockComponent component : this.components.values()) {
            pState = component.mirror(pState, pMirror);
        }
        return pState;
    }

    public boolean useShapeForLightOcclusion(BlockState pState) {
        for (KBlockComponent component : this.components.values()) {
            if (component.useShapeForLightOcclusion(pState)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public Boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        for (KBlockComponent component : this.components.values()) {
            Boolean result = component.canBeReplaced(state, context);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public ConfiguringShape removeIfPossible(BlockShapeType shapeType) {
        if (this.getShape(shapeType) instanceof ConfiguringShape shape) {
            this.setShape(shapeType, null);
            return shape;
        } else {
            return null;
        }
    }

    public ShapeGenerator getShape(BlockShapeType shapeType) {
        return this.shapes != null ? this.shapes[shapeType.ordinal()] : null;
    }

    private void setShape(BlockShapeType shapeType, @Nullable ShapeGenerator shape) {
        if (shape != null) {
            if (this.shapes == null) {
                this.shapes = new ShapeGenerator[BlockShapeType.VALUES.size()];
            }
            this.shapes[shapeType.ordinal()] = shape;
        } else if (this.shapes != null) {
            this.shapes[shapeType.ordinal()] = null;
        }
    }

    public static class Builder {

        private final BlockBehaviour.Properties properties;

        private boolean customPlacement;

        @Nullable
        private GlassType glassType;

        private final ShapeGenerator[] shapes = new ShapeGenerator[BlockShapeType.VALUES.size()];

        @Nullable
        private CanSurviveHandler canSurviveHandler;

        private final Map<KBlockComponent.Type<?>, KBlockComponent> components = Maps.newLinkedHashMap();

        @Nullable
        private ToIntFunction<BlockState> analogOutputSignal;

        private Builder(BlockBehaviour.Properties properties) {
            this.properties = properties;
        }

        public BlockBehaviour.Properties get() {
            KBlockSettings settings = new KBlockSettings(this);
            ((KBlockProperties) this.properties).kiwi$setSettings(settings);
            return this.properties;
        }

        public KBlockSettings.Builder configure(Consumer<BlockBehaviour.Properties> configurator) {
            configurator.accept(this.properties);
            return this;
        }

        public KBlockSettings.Builder noOcclusion() {
            this.properties.noOcclusion();
            return this;
        }

        public KBlockSettings.Builder noCollision() {
            this.properties.noCollission();
            return this;
        }

        public KBlockSettings.Builder customPlacement() {
            this.customPlacement = true;
            return this;
        }

        public KBlockSettings.Builder glassType(GlassType glassType) {
            this.glassType = glassType;
            return this;
        }

        public KBlockSettings.Builder shape(BlockShapeType type, ShapeGenerator shape) {
            this.shapes[type.ordinal()] = shape;
            return this;
        }

        @Nullable
        private ShapeGenerator getShape(BlockShapeType type) {
            return this.shapes[type.ordinal()];
        }

        public KBlockSettings.Builder canSurviveHandler(CanSurviveHandler canSurviveHandler) {
            this.canSurviveHandler = canSurviveHandler;
            return this;
        }

        public KBlockSettings.Builder component(KBlockComponent component) {
            KBlockComponent before = (KBlockComponent) this.components.put(component.type(), component);
            Preconditions.checkState(before == null, "Component %s is already present", component.type());
            return this;
        }

        public KBlockSettings.Builder waterLoggable() {
            return this.component(WaterLoggableComponent.getInstance());
        }

        public KBlockSettings.Builder horizontal() {
            return this.component(HorizontalComponent.getInstance(false));
        }

        public KBlockSettings.Builder directional() {
            return this.component(DirectionalComponent.getInstance(false));
        }

        public boolean hasComponent(KBlockComponent.Type<?> type) {
            return this.components.containsKey(type);
        }

        public KBlockSettings.Builder removeComponent(KBlockComponent.Type<?> type) {
            this.components.remove(type);
            return this;
        }

        @Nullable
        public ToIntFunction<BlockState> getAnalogOutputSignal() {
            if (this.analogOutputSignal != null) {
                return this.analogOutputSignal;
            } else {
                for (KBlockComponent component : this.components.values()) {
                    if (component.hasAnalogOutputSignal()) {
                        return component::getAnalogOutputSignal;
                    }
                }
                return null;
            }
        }
    }

    @Deprecated
    public static record MoreInfo(ResourceLocation shape, ResourceLocation collisionShape, ResourceLocation interactionShape) {
    }
}