package dev.latvian.mods.kubejs.block;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.callbacks.AfterEntityFallenOnBlockCallbackJS;
import dev.latvian.mods.kubejs.block.callbacks.BlockExplodedCallbackJS;
import dev.latvian.mods.kubejs.block.callbacks.BlockStateMirrorCallbackJS;
import dev.latvian.mods.kubejs.block.callbacks.BlockStateModifyCallbackJS;
import dev.latvian.mods.kubejs.block.callbacks.BlockStateModifyPlacementCallbackJS;
import dev.latvian.mods.kubejs.block.callbacks.BlockStateRotateCallbackJS;
import dev.latvian.mods.kubejs.block.callbacks.CanBeReplacedCallbackJS;
import dev.latvian.mods.kubejs.block.callbacks.EntityFallenOnBlockCallbackJS;
import dev.latvian.mods.kubejs.block.callbacks.EntitySteppedOnBlockCallbackJS;
import dev.latvian.mods.kubejs.block.entity.BlockEntityBuilder;
import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class BlockBuilder extends BuilderBase<Block> {

    private static final Consumer<LootBuilder> EMPTY = loot -> {
    };

    private static final BlockBehaviour.StatePredicate ALWAYS_FALSE_STATE_PREDICATE = (blockState, blockGetter, blockPos) -> false;

    private static final BlockBehaviour.StateArgumentPredicate<?> ALWAYS_FALSE_STATE_ARG_PREDICATE = (blockState, blockGetter, blockPos, type) -> false;

    public transient SoundType soundType;

    public transient Function<BlockState, MapColor> mapColorFn;

    public transient float hardness;

    public transient float resistance;

    public transient float lightLevel;

    public transient boolean opaque;

    public transient boolean fullBlock;

    public transient boolean requiresTool;

    public transient String renderType;

    public transient BlockTintFunction tint;

    public final transient JsonObject textures;

    public transient String model;

    public transient BlockItemBuilder itemBuilder;

    public transient List<AABB> customShape;

    public transient boolean noCollision;

    public transient boolean notSolid;

    public transient float slipperiness = Float.NaN;

    public transient float speedFactor = Float.NaN;

    public transient float jumpFactor = Float.NaN;

    public Consumer<RandomTickCallbackJS> randomTickCallback;

    public Consumer<LootBuilder> lootTable;

    public JsonObject blockstateJson;

    public JsonObject modelJson;

    public transient boolean noValidSpawns;

    public transient boolean suffocating;

    public transient boolean viewBlocking;

    public transient boolean redstoneConductor;

    public transient boolean transparent;

    public transient NoteBlockInstrument instrument;

    public transient Set<Property<?>> blockStateProperties;

    public transient Consumer<BlockStateModifyCallbackJS> defaultStateModification;

    public transient Consumer<BlockStateModifyPlacementCallbackJS> placementStateModification;

    public transient Predicate<CanBeReplacedCallbackJS> canBeReplacedFunction;

    public transient Consumer<EntitySteppedOnBlockCallbackJS> stepOnCallback;

    public transient Consumer<EntityFallenOnBlockCallbackJS> fallOnCallback;

    public transient Consumer<AfterEntityFallenOnBlockCallbackJS> afterFallenOnCallback;

    public transient Consumer<BlockExplodedCallbackJS> explodedCallback;

    public transient Consumer<BlockStateRotateCallbackJS> rotateStateModification;

    public transient Consumer<BlockStateMirrorCallbackJS> mirrorStateModification;

    public transient Consumer<BlockRightClickedEventJS> rightClick;

    public transient BlockEntityInfo blockEntityInfo;

    public BlockBuilder(ResourceLocation i) {
        super(i);
        this.soundType = SoundType.WOOD;
        this.mapColorFn = MapColorHelper.NONE;
        this.hardness = 1.5F;
        this.resistance = 3.0F;
        this.lightLevel = 0.0F;
        this.opaque = true;
        this.fullBlock = false;
        this.requiresTool = false;
        this.renderType = "solid";
        this.textures = new JsonObject();
        this.textureAll(this.id.getNamespace() + ":block/" + this.id.getPath());
        this.model = "";
        this.itemBuilder = this.getOrCreateItemBuilder();
        this.itemBuilder.blockBuilder = this;
        this.customShape = new ArrayList();
        this.noCollision = false;
        this.notSolid = false;
        this.randomTickCallback = null;
        this.lootTable = null;
        this.blockstateJson = null;
        this.modelJson = null;
        this.noValidSpawns = false;
        this.suffocating = true;
        this.viewBlocking = true;
        this.redstoneConductor = true;
        this.transparent = false;
        this.blockStateProperties = new HashSet();
        this.defaultStateModification = null;
        this.placementStateModification = null;
        this.canBeReplacedFunction = null;
    }

    @Override
    public final RegistryInfo getRegistryType() {
        return RegistryInfo.BLOCK;
    }

    public Block transformObject(Block obj) {
        obj.kjs$setBlockBuilder(this);
        return obj;
    }

    @Override
    public void createAdditionalObjects() {
        if (this.itemBuilder != null) {
            RegistryInfo.ITEM.addBuilder(this.itemBuilder);
        }
        if (this.blockEntityInfo != null) {
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(new BlockEntityBuilder(this.id, this.blockEntityInfo));
        }
    }

    @Info("Sets the display name for this object, e.g. `Stone`.\n\nThis will be overridden by a lang file if it exists.\n")
    @Override
    public BuilderBase<Block> displayName(Component name) {
        if (this.itemBuilder != null) {
            this.itemBuilder.displayName(name);
        }
        return super.displayName(name);
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        if (this.lootTable != EMPTY) {
            LootBuilder lootBuilder = new LootBuilder(null);
            lootBuilder.type = "minecraft:block";
            if (this.lootTable != null) {
                this.lootTable.accept(lootBuilder);
            } else if (this.get().asItem() != Items.AIR) {
                lootBuilder.addPool(pool -> {
                    pool.survivesExplosion();
                    pool.addItem(new ItemStack(this.get()));
                });
            }
            JsonObject json = lootBuilder.toJson();
            generator.json(this.newID("loot_tables/blocks/", ""), json);
        }
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (this.blockstateJson != null) {
            generator.json(this.newID("blockstates/", ""), this.blockstateJson);
        } else {
            generator.blockState(this.id, this::generateBlockStateJson);
        }
        if (this.modelJson != null) {
            generator.json(this.newID("models/block/", ""), this.modelJson);
        } else {
            this.generateBlockModelJsons(generator);
        }
        if (this.itemBuilder != null) {
            if (this.itemBuilder.modelJson != null) {
                generator.json(this.newID("models/item/", ""), this.itemBuilder.modelJson);
            } else {
                generator.itemModel(this.itemBuilder.id, this::generateItemModelJson);
            }
        }
    }

    protected void generateItemModelJson(ModelGenerator m) {
        if (!this.model.isEmpty()) {
            m.parent(this.model);
        } else {
            m.parent(this.newID("block/", "").toString());
        }
    }

    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(this.id, mg -> {
            String particle = this.textures.get("particle").getAsString();
            if (this.areAllTexturesEqual(this.textures, particle)) {
                mg.parent("minecraft:block/cube_all");
                mg.texture("all", particle);
            } else {
                mg.parent("block/cube");
                mg.textures(this.textures);
            }
            if (this.tint != null || !this.customShape.isEmpty()) {
                List<AABB> boxes = new ArrayList(this.customShape);
                if (boxes.isEmpty()) {
                    boxes.add(new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
                }
                for (AABB box : boxes) {
                    mg.element(e -> {
                        e.box(box);
                        for (Direction direction : Direction.values()) {
                            e.face(direction, face -> {
                                face.tex("#" + direction.getSerializedName());
                                face.cull();
                                if (this.tint != null) {
                                    face.tintindex(0);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        bs.simpleVariant("", this.model.isEmpty() ? this.id.getNamespace() + ":block/" + this.id.getPath() : this.model);
    }

    protected boolean areAllTexturesEqual(JsonObject tex, String t) {
        for (Direction direction : Direction.values()) {
            if (!tex.get(direction.getSerializedName()).getAsString().equals(t)) {
                return false;
            }
        }
        return true;
    }

    public BlockBuilder material(String material) {
        ConsoleJS.STARTUP.warn("blockBuilder.material(string) is no longer supported! Use .soundType(SoundType) and .mapColor(MapColor) instead!");
        return this;
    }

    @Info("Sets the block's sound type. Defaults to wood.")
    public BlockBuilder soundType(SoundType m) {
        if (m != null && m != SoundType.EMPTY) {
            this.soundType = m;
            return this;
        } else {
            this.soundType = SoundType.EMPTY;
            ConsoleJS.STARTUP.error("Invalid sound type!");
            ConsoleJS.STARTUP.warn("Valid sound types: " + SoundTypeWrapper.INSTANCE.getMap().keySet());
            return this;
        }
    }

    public BlockBuilder noSoundType() {
        return this.soundType(SoundType.EMPTY);
    }

    public BlockBuilder woodSoundType() {
        return this.soundType(SoundType.WOOD);
    }

    public BlockBuilder stoneSoundType() {
        return this.soundType(SoundType.STONE);
    }

    public BlockBuilder gravelSoundType() {
        return this.soundType(SoundType.GRAVEL);
    }

    public BlockBuilder grassSoundType() {
        return this.soundType(SoundType.GRASS);
    }

    public BlockBuilder sandSoundType() {
        return this.soundType(SoundType.SAND);
    }

    public BlockBuilder cropSoundType() {
        return this.soundType(SoundType.CROP);
    }

    public BlockBuilder glassSoundType() {
        return this.soundType(SoundType.GLASS);
    }

    @Info("Sets the block's map color. Defaults to NONE.")
    public BlockBuilder mapColor(MapColor m) {
        this.mapColorFn = MapColorHelper.reverse(m);
        return this;
    }

    @Info("Sets the block's map color dynamically per block state. If unset, defaults to NONE.")
    public BlockBuilder dynamicMapColor(@Nullable Function<BlockState, Object> m) {
        this.mapColorFn = (Function<BlockState, MapColor>) (m == null ? MapColorHelper.NONE : s -> MapColorHelper.of(m.apply(s)));
        return this;
    }

    @Info("Sets the hardness of the block. Defaults to 1.5.\n\nSetting this to -1 will make the block unbreakable like bedrock.\n")
    public BlockBuilder hardness(float h) {
        this.hardness = h;
        return this;
    }

    @Info("Sets the blast resistance of the block. Defaults to 3.\n")
    public BlockBuilder resistance(float r) {
        this.resistance = r;
        return this;
    }

    @Info("Makes the block unbreakable.")
    public BlockBuilder unbreakable() {
        this.hardness = -1.0F;
        this.resistance = Float.MAX_VALUE;
        return this;
    }

    @Info("Sets the light level of the block. Defaults to 0 (no light).")
    public BlockBuilder lightLevel(float light) {
        this.lightLevel = light;
        return this;
    }

    @Info("Sets the opacity of the block. Opaque blocks do not let light through.")
    public BlockBuilder opaque(boolean o) {
        this.opaque = o;
        return this;
    }

    @Info("Sets the block should be a full block or not, like cactus or doors.")
    public BlockBuilder fullBlock(boolean f) {
        this.fullBlock = f;
        return this;
    }

    @Info("Makes the block require a tool to have drops when broken.")
    public BlockBuilder requiresTool(boolean f) {
        this.requiresTool = f;
        return this;
    }

    @Info("Makes the block require a tool to have drops when broken.")
    public BlockBuilder requiresTool() {
        return this.requiresTool(true);
    }

    @Info("Sets the render type of the block. Can be `cutout`, `cutout_mipped`, `translucent`, or `basic`.\n")
    public BlockBuilder renderType(String l) {
        this.renderType = l;
        return this;
    }

    @Info("Set the color of a specific layer of the block.\n")
    public BlockBuilder color(int index, BlockTintFunction color) {
        if (!(this.tint instanceof BlockTintFunction.Mapped)) {
            this.tint = new BlockTintFunction.Mapped();
        }
        ((BlockTintFunction.Mapped) this.tint).map.put(index, color);
        return this;
    }

    @Info("Set the color of a specific layer of the block.\n")
    public BlockBuilder color(BlockTintFunction color) {
        this.tint = color;
        return this;
    }

    @Info("Texture the block on all sides with the same texture.\n")
    public BlockBuilder textureAll(String tex) {
        for (Direction direction : Direction.values()) {
            this.textureSide(direction, tex);
        }
        this.textures.addProperty("particle", tex);
        return this;
    }

    @Info("Texture a specific side of the block.\n")
    public BlockBuilder textureSide(Direction direction, String tex) {
        return this.texture(direction.getSerializedName(), tex);
    }

    @Info("Texture a specific texture key of the block.\n")
    public BlockBuilder texture(String id, String tex) {
        this.textures.addProperty(id, tex);
        return this;
    }

    @Info("Set the block's model.\n")
    public BlockBuilder model(String m) {
        this.model = m;
        if (this.itemBuilder != null) {
            this.itemBuilder.parentModel(m);
        }
        return this;
    }

    @Info("Modifies the block's item representation.\n")
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            this.itemBuilder = null;
            this.lootTable = EMPTY;
        } else {
            if (this.itemBuilder == null) {
                this.itemBuilder = this.getOrCreateItemBuilder();
                this.itemBuilder.blockBuilder = this;
                ScriptType.STARTUP.console.warn("`item` is called with non-null builder callback after block item is set to null! Creating another block item as fallback.");
            }
            i.accept(this.itemBuilder);
        }
        return this;
    }

    @HideFromJS
    protected BlockItemBuilder getOrCreateItemBuilder() {
        return this.itemBuilder == null ? (this.itemBuilder = new BlockItemBuilder(this.id)) : this.itemBuilder;
    }

    @Info("Set the block to have no corresponding item.\n")
    public BlockBuilder noItem() {
        return this.item(null);
    }

    @Info("Set the shape of the block.")
    public BlockBuilder box(double x0, double y0, double z0, double x1, double y1, double z1, boolean scale16) {
        if (scale16) {
            this.customShape.add(new AABB(x0 / 16.0, y0 / 16.0, z0 / 16.0, x1 / 16.0, y1 / 16.0, z1 / 16.0));
        } else {
            this.customShape.add(new AABB(x0, y0, z0, x1, y1, z1));
        }
        return this;
    }

    @Info("Set the shape of the block.")
    public BlockBuilder box(double x0, double y0, double z0, double x1, double y1, double z1) {
        return this.box(x0, y0, z0, x1, y1, z1, true);
    }

    public static VoxelShape createShape(List<AABB> boxes) {
        if (boxes.isEmpty()) {
            return Shapes.block();
        } else {
            VoxelShape shape = Shapes.create((AABB) boxes.get(0));
            for (int i = 1; i < boxes.size(); i++) {
                shape = Shapes.or(shape, Shapes.create((AABB) boxes.get(i)));
            }
            return shape;
        }
    }

    @Info("Makes the block not collide with entities.")
    public BlockBuilder noCollision() {
        this.noCollision = true;
        return this;
    }

    @Info("Makes the block not be solid.")
    public BlockBuilder notSolid() {
        this.notSolid = true;
        return this;
    }

    @Deprecated(forRemoval = true)
    public BlockBuilder setWaterlogged(boolean waterlogged) {
        ScriptType.STARTUP.console.warn("\"BlockBuilder.waterlogged\" is a deprecated property! Please use \"BlockBuilder.property(BlockProperties.WATERLOGGED)\" instead.");
        if (waterlogged) {
            this.property(BlockStateProperties.WATERLOGGED);
        }
        return this;
    }

    @Deprecated(forRemoval = true)
    public boolean getWaterlogged() {
        ScriptType.STARTUP.console.warn("\"BlockBuilder.waterlogged\" is a deprecated property! Please use \"BlockBuilder.property(BlockProperties.WATERLOGGED)\" instead.");
        return this.canBeWaterlogged();
    }

    @Info("Makes the block can be waterlogged.")
    public BlockBuilder waterlogged() {
        return this.property(BlockStateProperties.WATERLOGGED);
    }

    @Info("Checks if the block can be waterlogged.")
    public boolean canBeWaterlogged() {
        return this.blockStateProperties.contains(BlockStateProperties.WATERLOGGED);
    }

    @Info("Clears all drops for the block.")
    public BlockBuilder noDrops() {
        this.lootTable = EMPTY;
        return this;
    }

    @Info("Set how slippery the block is.")
    public BlockBuilder slipperiness(float f) {
        this.slipperiness = f;
        return this;
    }

    @Info("Set how fast you can walk on the block.\n\nAny value above 1 will make you walk insanely fast as your speed is multiplied by this value each tick.\n\nRecommended values are between 0.1 and 1, useful for mimicking soul sand or ice.\n")
    public BlockBuilder speedFactor(float f) {
        this.speedFactor = f;
        return this;
    }

    @Info("Set how high you can jump on the block.")
    public BlockBuilder jumpFactor(float f) {
        this.jumpFactor = f;
        return this;
    }

    @Info("Sets random tick callback for this black.")
    public BlockBuilder randomTick(@Nullable Consumer<RandomTickCallbackJS> randomTickCallback) {
        this.randomTickCallback = randomTickCallback;
        return this;
    }

    @Info("Makes mobs not spawn on the block.")
    public BlockBuilder noValidSpawns(boolean b) {
        this.noValidSpawns = b;
        return this;
    }

    @Info("Makes the block suffocating.")
    public BlockBuilder suffocating(boolean b) {
        this.suffocating = b;
        return this;
    }

    @Info("Makes the block view blocking.")
    public BlockBuilder viewBlocking(boolean b) {
        this.viewBlocking = b;
        return this;
    }

    @Info("Makes the block a redstone conductor.")
    public BlockBuilder redstoneConductor(boolean b) {
        this.redstoneConductor = b;
        return this;
    }

    @Info("Makes the block transparent.")
    public BlockBuilder transparent(boolean b) {
        this.transparent = b;
        return this;
    }

    @Info("Helper method for setting the render type of the block to `cutout` correctly.")
    public BlockBuilder defaultCutout() {
        return this.renderType("cutout").notSolid().noValidSpawns(true).suffocating(false).viewBlocking(false).redstoneConductor(false).transparent(true);
    }

    @Info("Helper method for setting the render type of the block to `translucent` correctly.")
    public BlockBuilder defaultTranslucent() {
        return this.defaultCutout().renderType("translucent");
    }

    @Info("Note block instrument.")
    public BlockBuilder instrument(NoteBlockInstrument i) {
        this.instrument = i;
        return this;
    }

    @Info("Tags both the block and the item with the given tag.")
    public BlockBuilder tag(ResourceLocation tag) {
        return this.tagBoth(tag);
    }

    @Info("Tags both the block and the item with the given tag.")
    public BlockBuilder tagBoth(ResourceLocation tag) {
        this.tagBlock(tag);
        this.tagItem(tag);
        return this;
    }

    @Info("Tags the block with the given tag.")
    public BlockBuilder tagBlock(ResourceLocation tag) {
        super.tag(tag);
        return this;
    }

    @Info("Tags the item with the given tag.")
    public BlockBuilder tagItem(ResourceLocation tag) {
        this.itemBuilder.tag(tag);
        return this;
    }

    @Info("Set the default state of the block.")
    public BlockBuilder defaultState(Consumer<BlockStateModifyCallbackJS> callbackJS) {
        this.defaultStateModification = callbackJS;
        return this;
    }

    @Info("Set the callback for determining the blocks state when placed.")
    public BlockBuilder placementState(Consumer<BlockStateModifyPlacementCallbackJS> callbackJS) {
        this.placementStateModification = callbackJS;
        return this;
    }

    @Info("Set if the block can be replaced by something else.")
    public BlockBuilder canBeReplaced(Predicate<CanBeReplacedCallbackJS> callbackJS) {
        this.canBeReplacedFunction = callbackJS;
        return this;
    }

    @Info("Set what happens when an entity steps on the block\nThis is called every tick for every entity standing on the block, so be careful what you do here.\n")
    public BlockBuilder steppedOn(Consumer<EntitySteppedOnBlockCallbackJS> callbackJS) {
        this.stepOnCallback = callbackJS;
        return this;
    }

    @Info("Set what happens when an entity falls on the block. Do not use this for moving them, use bounce instead!")
    public BlockBuilder fallenOn(Consumer<EntityFallenOnBlockCallbackJS> callbackJS) {
        this.fallOnCallback = callbackJS;
        return this;
    }

    @Info("Bounces entities that land on this block by bounciness * their fall velocity.\nDo not make bounciness negative, as that is a recipe for a long and laggy trip to the void\n")
    public BlockBuilder bounciness(float bounciness) {
        return this.afterFallenOn(ctx -> ctx.bounce(bounciness));
    }

    @Info("Set how this block bounces/moves entities that land on top of this. Do not use this to modify the block, use fallOn instead!\nUse ctx.bounce(height) or ctx.setVelocity(x, y, z) to change the entities velocity.\n")
    public BlockBuilder afterFallenOn(Consumer<AfterEntityFallenOnBlockCallbackJS> callbackJS) {
        this.afterFallenOnCallback = callbackJS;
        return this;
    }

    @Info("Set how this block reacts after an explosion. Note the block has already been destroyed at this point")
    public BlockBuilder exploded(Consumer<BlockExplodedCallbackJS> callbackJS) {
        this.explodedCallback = callbackJS;
        return this;
    }

    @Info("Add a blockstate property to the block.\n\nFor example, facing, lit, etc.\n")
    public BlockBuilder property(Property<?> property) {
        if (property.getPossibleValues().size() <= 1) {
            throw new IllegalArgumentException(String.format("Block \"%s\" has an illegal Blockstate Property \"%s\" which has <= 1 possible values. (%d possible values)", this.id, property.getName(), property.getPossibleValues().size()));
        } else {
            this.blockStateProperties.add(property);
            return this;
        }
    }

    @Info("Set the callback used for determining how the block rotates")
    public BlockBuilder rotateState(Consumer<BlockStateRotateCallbackJS> callbackJS) {
        this.rotateStateModification = callbackJS;
        return this;
    }

    @Info("Set the callback used for determining how the block is mirrored")
    public BlockBuilder mirrorState(Consumer<BlockStateMirrorCallbackJS> callbackJS) {
        this.mirrorStateModification = callbackJS;
        return this;
    }

    @Info("Set the callback used for right-clicking on the block")
    public BlockBuilder rightClick(Consumer<BlockRightClickedEventJS> callbackJS) {
        this.rightClick = callbackJS;
        return this;
    }

    @Info("Creates a Block Entity for this block")
    public BlockBuilder blockEntity(Consumer<BlockEntityInfo> callback) {
        this.blockEntityInfo = new BlockEntityInfo(this);
        callback.accept(this.blockEntityInfo);
        return this;
    }

    public BlockBehaviour.Properties createProperties() {
        KubeJSBlockProperties properties = new KubeJSBlockProperties(this);
        properties.m_60918_(this.soundType);
        properties.m_284495_(this.mapColorFn);
        if (this.resistance >= 0.0F) {
            properties.m_60913_(this.hardness, this.resistance);
        } else {
            properties.m_60978_(this.hardness);
        }
        properties.m_60953_(state -> (int) (this.lightLevel * 15.0F));
        if (this.noCollision) {
            properties.m_60910_();
        }
        if (this.notSolid) {
            properties.m_60955_();
        }
        if (this.requiresTool) {
            properties.m_60999_();
        }
        if (this.lootTable == EMPTY) {
            properties.m_222994_();
        }
        if (!Float.isNaN(this.slipperiness)) {
            properties.m_60911_(this.slipperiness);
        }
        if (!Float.isNaN(this.speedFactor)) {
            properties.m_60956_(this.speedFactor);
        }
        if (!Float.isNaN(this.jumpFactor)) {
            properties.m_60967_(this.jumpFactor);
        }
        if (this.noValidSpawns) {
            properties.m_60922_(UtilsJS.cast(ALWAYS_FALSE_STATE_ARG_PREDICATE));
        }
        if (!this.suffocating) {
            properties.m_60960_(ALWAYS_FALSE_STATE_PREDICATE);
        }
        if (!this.viewBlocking) {
            properties.m_60971_(ALWAYS_FALSE_STATE_PREDICATE);
        }
        if (!this.redstoneConductor) {
            properties.m_60924_(ALWAYS_FALSE_STATE_PREDICATE);
        }
        if (this.randomTickCallback != null) {
            properties.m_60977_();
        }
        if (this.instrument != null) {
            properties.m_280658_(this.instrument);
        }
        return properties;
    }
}