package dev.latvian.mods.kubejs.block.custom;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.RandomTickCallbackJS;
import dev.latvian.mods.kubejs.block.SeedItemBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CropBlockBuilder extends BlockBuilder {

    public transient int age = 7;

    protected transient List<VoxelShape> shapeByAge = Collections.nCopies(8, Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0));

    public transient boolean dropSeed;

    public transient ToDoubleFunction<RandomTickCallbackJS> growSpeedCallback = null;

    public transient ToIntFunction<RandomTickCallbackJS> fertilizerCallback = null;

    public transient CropBlockBuilder.SurviveCallback surviveCallback = null;

    public transient List<Pair<Object, Double>> outputs;

    public CropBlockBuilder(ResourceLocation i) {
        super(i);
        this.renderType = "cutout";
        this.noCollision = true;
        this.itemBuilder = new SeedItemBuilder(this.newID("", "_seed"));
        this.itemBuilder.blockBuilder = this;
        this.hardness = 0.0F;
        this.resistance = 0.0F;
        this.dropSeed = true;
        this.outputs = new ArrayList();
        this.notSolid = true;
        this.soundType(SoundType.CROP);
        this.mapColor(MapColor.PLANT);
        this.lootTable = loot -> {
            JsonObject condition = new JsonObject();
            condition.addProperty("condition", "minecraft:block_state_property");
            condition.addProperty("block", this.newID("", "").toString());
            JsonObject properties = new JsonObject();
            properties.addProperty("age", String.valueOf(this.age));
            condition.add("properties", properties);
            JsonObject function = new JsonObject();
            function.addProperty("function", "minecraft:apply_bonus");
            function.addProperty("enchantment", "minecraft:fortune");
            function.addProperty("formula", "minecraft:binomial_with_bonus_count");
            JsonObject parameters = new JsonObject();
            parameters.addProperty("extra", 3);
            parameters.addProperty("probability", 0.5714286);
            function.add("parameters", parameters);
            if (this.dropSeed) {
                loot.addPool(bonuses -> {
                    bonuses.rolls = ConstantValue.exactly(1.0F);
                    bonuses.bonusRolls = ConstantValue.exactly(0.0F);
                    bonuses.addItem(new ItemStack(this.itemBuilder.get())).addCondition(condition).addFunction(function);
                    bonuses.addItem(new ItemStack(this.itemBuilder.get()));
                });
            }
            for (Pair<Object, Double> output : this.outputs) {
                loot.addPool(crops -> {
                    crops.rolls = ConstantValue.exactly(1.0F);
                    crops.bonusRolls = ConstantValue.exactly(0.0F);
                    crops.addItem(ItemStackJS.of(output.getFirst())).addCondition(condition).randomChance((Double) output.getSecond());
                });
            }
        };
        for (int a = 0; a <= this.age; a++) {
            this.texture(String.valueOf(a), this.id.getNamespace() + ":block/" + this.id.getPath() + a);
        }
        this.tagBlock(BlockTags.CROPS.location());
        if (Platform.isForge()) {
            this.tagItem(new ResourceLocation("forge", "seeds"));
        }
    }

    @Info("Add a crop output with a 100% chance.")
    public CropBlockBuilder crop(Object output) {
        this.crop(output, 1.0);
        return this;
    }

    @Info("Add a crop output with a specific chance.")
    public CropBlockBuilder crop(Object output, double chance) {
        this.outputs.add(new Pair(output, chance));
        return this;
    }

    @Info("Set the age of the crop. Note that the box will be the same for all ages (A full block size).")
    public CropBlockBuilder age(int age) {
        this.age(age, builder -> {
        });
        return this;
    }

    @Info("Set if the crop should drop seeds when harvested.")
    public CropBlockBuilder dropSeed(boolean dropSeed) {
        this.dropSeed = dropSeed;
        return this;
    }

    @Info("Set the age of the crop and the shape of the crop at that age.")
    public CropBlockBuilder age(int age, Consumer<CropBlockBuilder.ShapeBuilder> builder) {
        this.age = age;
        CropBlockBuilder.ShapeBuilder shapes = new CropBlockBuilder.ShapeBuilder(age);
        builder.accept(shapes);
        this.shapeByAge = shapes.getShapes();
        for (int i = 0; i <= age; i++) {
            this.texture(String.valueOf(i), this.id.getNamespace() + ":block/" + this.id.getPath() + i);
        }
        return this;
    }

    @Override
    public BlockBuilder texture(String id, String tex) {
        try {
            int intId = (int) Double.parseDouble(id);
            return super.texture(String.valueOf(intId), tex);
        } catch (NumberFormatException var4) {
            return super.texture(id, tex);
        }
    }

    public CropBlockBuilder bonemeal(ToIntFunction<RandomTickCallbackJS> bonemealCallback) {
        this.fertilizerCallback = bonemealCallback;
        return this;
    }

    public CropBlockBuilder survive(CropBlockBuilder.SurviveCallback surviveCallback) {
        this.surviveCallback = surviveCallback;
        return this;
    }

    public CropBlockBuilder growTick(ToDoubleFunction<RandomTickCallbackJS> growSpeedCallback) {
        this.growSpeedCallback = growSpeedCallback;
        return this;
    }

    @Override
    public BlockBuilder randomTick(@Nullable Consumer<RandomTickCallbackJS> randomTickCallback) {
        KubeJS.LOGGER.warn("randomTick is overridden by growTick to return grow speed, use it instead.");
        return this;
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (int i = 0; i <= this.age; i++) {
            bs.simpleVariant("age=%s".formatted(i), this.model.isEmpty() ? this.id.getNamespace() + ":block/" + this.id.getPath() + i : this.model);
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (int i = 0; i <= this.age; i++) {
            int fi = i;
            generator.blockModel(this.newID("", String.valueOf(i)), m -> {
                m.parent("minecraft:block/crop");
                m.texture("crop", this.textures.get(String.valueOf(fi)).getAsString());
            });
        }
    }

    public Block createObject() {
        final IntegerProperty ageProperty = IntegerProperty.create("age", 0, this.age);
        return new BasicCropBlockJS(this) {

            @Override
            public IntegerProperty getAgeProperty() {
                return ageProperty;
            }

            @Override
            protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
                builder.add(ageProperty);
            }

            @Override
            public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
                double f = CropBlockBuilder.this.growSpeedCallback == null ? -1.0 : CropBlockBuilder.this.growSpeedCallback.applyAsDouble(new RandomTickCallbackJS(new BlockContainerJS(serverLevel, blockPos), random));
                int age = this.m_52305_(blockState);
                if (age < this.m_7419_()) {
                    if (f < 0.0) {
                        f = (double) m_52272_(this, serverLevel, blockPos);
                    }
                    if (f > 0.0 && random.nextInt((int) (25.0 / f) + 1) == 0) {
                        serverLevel.m_7731_(blockPos, this.m_52289_(age + 1), 2);
                    }
                }
            }

            @Override
            public void growCrops(Level level, BlockPos blockPos, BlockState blockState) {
                if (CropBlockBuilder.this.fertilizerCallback == null) {
                    super.growCrops(level, blockPos, blockState);
                } else {
                    int effect = CropBlockBuilder.this.fertilizerCallback.applyAsInt(new RandomTickCallbackJS(new BlockContainerJS(level, blockPos), level.random));
                    if (effect > 0) {
                        level.setBlock(blockPos, this.m_52289_(Integer.min(this.m_52305_(blockState) + effect, this.m_7419_())), 2);
                    }
                }
            }

            @Override
            public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
                return CropBlockBuilder.this.surviveCallback != null ? CropBlockBuilder.this.surviveCallback.survive(blockState, levelReader, blockPos) : super.canSurvive(blockState, levelReader, blockPos);
            }
        };
    }

    public static class ShapeBuilder {

        private final List<VoxelShape> shapes;

        public ShapeBuilder(int age) {
            this.shapes = new ArrayList(Collections.nCopies(age + 1, Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)));
        }

        @Info("Describe the shape of the crop at a specific age.\n\nmin/max coordinates are double values between 0 and 16.\n")
        public CropBlockBuilder.ShapeBuilder shape(int age, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
            this.shapes.set(age, Block.box(minX, minY, minZ, maxX, maxY, maxZ));
            return this;
        }

        public List<VoxelShape> getShapes() {
            return List.copyOf(this.shapes);
        }
    }

    @FunctionalInterface
    public interface SurviveCallback {

        boolean survive(BlockState var1, LevelReader var2, BlockPos var3);
    }
}