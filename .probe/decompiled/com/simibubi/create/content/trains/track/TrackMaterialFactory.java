package com.simibubi.create.content.trains.track;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

public class TrackMaterialFactory {

    private final ResourceLocation id;

    private String langName;

    private NonNullSupplier<NonNullSupplier<? extends TrackBlock>> trackBlock;

    private Ingredient sleeperIngredient = Ingredient.EMPTY;

    private Ingredient railsIngredient = Ingredient.fromValues(Stream.of(new Ingredient.TagValue(AllTags.forgeItemTag("nuggets/iron")), new Ingredient.TagValue(AllTags.forgeItemTag("nuggets/zinc"))));

    private ResourceLocation particle;

    private TrackMaterial.TrackType trackType = TrackMaterial.TrackType.STANDARD;

    @Nullable
    private TrackMaterial.TrackType.TrackBlockFactory customFactory = null;

    @OnlyIn(Dist.CLIENT)
    private TrackMaterial.TrackModelHolder modelHolder;

    @OnlyIn(Dist.CLIENT)
    private PartialModel tieModel;

    @OnlyIn(Dist.CLIENT)
    private PartialModel leftSegmentModel;

    @OnlyIn(Dist.CLIENT)
    private PartialModel rightSegmentModel;

    public TrackMaterialFactory(ResourceLocation id) {
        this.id = id;
    }

    public static TrackMaterialFactory make(ResourceLocation id) {
        return new TrackMaterialFactory(id);
    }

    public TrackMaterialFactory lang(String langName) {
        this.langName = langName;
        return this;
    }

    public TrackMaterialFactory block(NonNullSupplier<NonNullSupplier<? extends TrackBlock>> trackBlock) {
        this.trackBlock = trackBlock;
        return this;
    }

    public TrackMaterialFactory defaultModels() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.modelHolder = TrackMaterial.TrackModelHolder.DEFAULT);
        return this;
    }

    public TrackMaterialFactory sleeper(Ingredient sleeperIngredient) {
        this.sleeperIngredient = sleeperIngredient;
        return this;
    }

    public TrackMaterialFactory sleeper(ItemLike... items) {
        this.sleeperIngredient = Ingredient.of(items);
        return this;
    }

    public TrackMaterialFactory rails(Ingredient railsIngredient) {
        this.railsIngredient = railsIngredient;
        return this;
    }

    public TrackMaterialFactory rails(ItemLike... items) {
        this.railsIngredient = Ingredient.of(items);
        return this;
    }

    public TrackMaterialFactory noRecipeGen() {
        this.railsIngredient = Ingredient.EMPTY;
        this.sleeperIngredient = Ingredient.EMPTY;
        return this;
    }

    public TrackMaterialFactory particle(ResourceLocation particle) {
        this.particle = particle;
        return this;
    }

    public TrackMaterialFactory trackType(TrackMaterial.TrackType trackType) {
        this.trackType = trackType;
        return this;
    }

    public TrackMaterialFactory standardModels() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            String namespace = this.id.getNamespace();
            String prefix = "block/track/" + this.id.getPath() + "/";
            this.tieModel = new PartialModel(new ResourceLocation(namespace, prefix + "tie"));
            this.leftSegmentModel = new PartialModel(new ResourceLocation(namespace, prefix + "segment_left"));
            this.rightSegmentModel = new PartialModel(new ResourceLocation(namespace, prefix + "segment_right"));
        });
        return this;
    }

    public TrackMaterialFactory customModels(Supplier<Supplier<PartialModel>> tieModel, Supplier<Supplier<PartialModel>> leftSegmentModel, Supplier<Supplier<PartialModel>> rightSegmentModel) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            this.tieModel = (PartialModel) ((Supplier) tieModel.get()).get();
            this.leftSegmentModel = (PartialModel) ((Supplier) leftSegmentModel.get()).get();
            this.rightSegmentModel = (PartialModel) ((Supplier) rightSegmentModel.get()).get();
        });
        return this;
    }

    public TrackMaterialFactory customBlockFactory(TrackMaterial.TrackType.TrackBlockFactory factory) {
        this.customFactory = factory;
        return this;
    }

    public TrackMaterial build() {
        assert this.trackBlock != null;
        assert this.langName != null;
        assert this.particle != null;
        assert this.trackType != null;
        assert this.sleeperIngredient != null;
        assert this.railsIngredient != null;
        assert this.id != null;
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            assert this.modelHolder != null;
            if (this.tieModel != null || this.leftSegmentModel != null || this.rightSegmentModel != null) {
                assert this.tieModel != null && this.leftSegmentModel != null && this.rightSegmentModel != null;
                this.modelHolder = new TrackMaterial.TrackModelHolder(this.tieModel, this.leftSegmentModel, this.rightSegmentModel);
            }
        });
        return new TrackMaterial(this.id, this.langName, this.trackBlock, this.particle, this.sleeperIngredient, this.railsIngredient, this.trackType, () -> () -> this.modelHolder, this.customFactory);
    }
}