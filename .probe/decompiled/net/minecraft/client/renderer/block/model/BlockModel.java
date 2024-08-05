package net.minecraft.client.renderer.block.model;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BuiltInModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemDisplayContext;
import org.slf4j.Logger;

public class BlockModel implements UnbakedModel {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final FaceBakery FACE_BAKERY = new FaceBakery();

    @VisibleForTesting
    static final Gson GSON = new GsonBuilder().registerTypeAdapter(BlockModel.class, new BlockModel.Deserializer()).registerTypeAdapter(BlockElement.class, new BlockElement.Deserializer()).registerTypeAdapter(BlockElementFace.class, new BlockElementFace.Deserializer()).registerTypeAdapter(BlockFaceUV.class, new BlockFaceUV.Deserializer()).registerTypeAdapter(ItemTransform.class, new ItemTransform.Deserializer()).registerTypeAdapter(ItemTransforms.class, new ItemTransforms.Deserializer()).registerTypeAdapter(ItemOverride.class, new ItemOverride.Deserializer()).create();

    private static final char REFERENCE_CHAR = '#';

    public static final String PARTICLE_TEXTURE_REFERENCE = "particle";

    private static final boolean DEFAULT_AMBIENT_OCCLUSION = true;

    private final List<BlockElement> elements;

    @Nullable
    private final BlockModel.GuiLight guiLight;

    @Nullable
    private final Boolean hasAmbientOcclusion;

    private final ItemTransforms transforms;

    private final List<ItemOverride> overrides;

    public String name = "";

    @VisibleForTesting
    protected final Map<String, Either<Material, String>> textureMap;

    @Nullable
    protected BlockModel parent;

    @Nullable
    protected ResourceLocation parentLocation;

    public static BlockModel fromStream(Reader reader0) {
        return GsonHelper.fromJson(GSON, reader0, BlockModel.class);
    }

    public static BlockModel fromString(String string0) {
        return fromStream(new StringReader(string0));
    }

    public BlockModel(@Nullable ResourceLocation resourceLocation0, List<BlockElement> listBlockElement1, Map<String, Either<Material, String>> mapStringEitherMaterialString2, @Nullable Boolean boolean3, @Nullable BlockModel.GuiLight blockModelGuiLight4, ItemTransforms itemTransforms5, List<ItemOverride> listItemOverride6) {
        this.elements = listBlockElement1;
        this.hasAmbientOcclusion = boolean3;
        this.guiLight = blockModelGuiLight4;
        this.textureMap = mapStringEitherMaterialString2;
        this.parentLocation = resourceLocation0;
        this.transforms = itemTransforms5;
        this.overrides = listItemOverride6;
    }

    public List<BlockElement> getElements() {
        return this.elements.isEmpty() && this.parent != null ? this.parent.getElements() : this.elements;
    }

    public boolean hasAmbientOcclusion() {
        if (this.hasAmbientOcclusion != null) {
            return this.hasAmbientOcclusion;
        } else {
            return this.parent != null ? this.parent.hasAmbientOcclusion() : true;
        }
    }

    public BlockModel.GuiLight getGuiLight() {
        if (this.guiLight != null) {
            return this.guiLight;
        } else {
            return this.parent != null ? this.parent.getGuiLight() : BlockModel.GuiLight.SIDE;
        }
    }

    public boolean isResolved() {
        return this.parentLocation == null || this.parent != null && this.parent.isResolved();
    }

    public List<ItemOverride> getOverrides() {
        return this.overrides;
    }

    private ItemOverrides getItemOverrides(ModelBaker modelBaker0, BlockModel blockModel1) {
        return this.overrides.isEmpty() ? ItemOverrides.EMPTY : new ItemOverrides(modelBaker0, blockModel1, this.overrides);
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        Set<ResourceLocation> $$0 = Sets.newHashSet();
        for (ItemOverride $$1 : this.overrides) {
            $$0.add($$1.getModel());
        }
        if (this.parentLocation != null) {
            $$0.add(this.parentLocation);
        }
        return $$0;
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> functionResourceLocationUnbakedModel0) {
        Set<UnbakedModel> $$1 = Sets.newLinkedHashSet();
        for (BlockModel $$2 = this; $$2.parentLocation != null && $$2.parent == null; $$2 = $$2.parent) {
            $$1.add($$2);
            UnbakedModel $$3 = (UnbakedModel) functionResourceLocationUnbakedModel0.apply($$2.parentLocation);
            if ($$3 == null) {
                LOGGER.warn("No parent '{}' while loading model '{}'", this.parentLocation, $$2);
            }
            if ($$1.contains($$3)) {
                LOGGER.warn("Found 'parent' loop while loading model '{}' in chain: {} -> {}", new Object[] { $$2, $$1.stream().map(Object::toString).collect(Collectors.joining(" -> ")), this.parentLocation });
                $$3 = null;
            }
            if ($$3 == null) {
                $$2.parentLocation = ModelBakery.MISSING_MODEL_LOCATION;
                $$3 = (UnbakedModel) functionResourceLocationUnbakedModel0.apply($$2.parentLocation);
            }
            if (!($$3 instanceof BlockModel)) {
                throw new IllegalStateException("BlockModel parent has to be a block model.");
            }
            $$2.parent = (BlockModel) $$3;
        }
        this.overrides.forEach(p_247932_ -> {
            UnbakedModel $$2x = (UnbakedModel) functionResourceLocationUnbakedModel0.apply(p_247932_.getModel());
            if (!Objects.equals($$2x, this)) {
                $$2x.resolveParents(functionResourceLocationUnbakedModel0);
            }
        });
    }

    @Override
    public BakedModel bake(ModelBaker modelBaker0, Function<Material, TextureAtlasSprite> functionMaterialTextureAtlasSprite1, ModelState modelState2, ResourceLocation resourceLocation3) {
        return this.bake(modelBaker0, this, functionMaterialTextureAtlasSprite1, modelState2, resourceLocation3, true);
    }

    public BakedModel bake(ModelBaker modelBaker0, BlockModel blockModel1, Function<Material, TextureAtlasSprite> functionMaterialTextureAtlasSprite2, ModelState modelState3, ResourceLocation resourceLocation4, boolean boolean5) {
        TextureAtlasSprite $$6 = (TextureAtlasSprite) functionMaterialTextureAtlasSprite2.apply(this.getMaterial("particle"));
        if (this.getRootModel() == ModelBakery.BLOCK_ENTITY_MARKER) {
            return new BuiltInModel(this.getTransforms(), this.getItemOverrides(modelBaker0, blockModel1), $$6, this.getGuiLight().lightLikeBlock());
        } else {
            SimpleBakedModel.Builder $$7 = new SimpleBakedModel.Builder(this, this.getItemOverrides(modelBaker0, blockModel1), boolean5).particle($$6);
            for (BlockElement $$8 : this.getElements()) {
                for (Direction $$9 : $$8.faces.keySet()) {
                    BlockElementFace $$10 = (BlockElementFace) $$8.faces.get($$9);
                    TextureAtlasSprite $$11 = (TextureAtlasSprite) functionMaterialTextureAtlasSprite2.apply(this.getMaterial($$10.texture));
                    if ($$10.cullForDirection == null) {
                        $$7.addUnculledFace(bakeFace($$8, $$10, $$11, $$9, modelState3, resourceLocation4));
                    } else {
                        $$7.addCulledFace(Direction.rotate(modelState3.getRotation().getMatrix(), $$10.cullForDirection), bakeFace($$8, $$10, $$11, $$9, modelState3, resourceLocation4));
                    }
                }
            }
            return $$7.build();
        }
    }

    private static BakedQuad bakeFace(BlockElement blockElement0, BlockElementFace blockElementFace1, TextureAtlasSprite textureAtlasSprite2, Direction direction3, ModelState modelState4, ResourceLocation resourceLocation5) {
        return FACE_BAKERY.bakeQuad(blockElement0.from, blockElement0.to, blockElementFace1, textureAtlasSprite2, direction3, modelState4, blockElement0.rotation, blockElement0.shade, resourceLocation5);
    }

    public boolean hasTexture(String string0) {
        return !MissingTextureAtlasSprite.getLocation().equals(this.getMaterial(string0).texture());
    }

    public Material getMaterial(String string0) {
        if (isTextureReference(string0)) {
            string0 = string0.substring(1);
        }
        List<String> $$1 = Lists.newArrayList();
        while (true) {
            Either<Material, String> $$2 = this.findTextureEntry(string0);
            Optional<Material> $$3 = $$2.left();
            if ($$3.isPresent()) {
                return (Material) $$3.get();
            }
            string0 = (String) $$2.right().get();
            if ($$1.contains(string0)) {
                LOGGER.warn("Unable to resolve texture due to reference chain {}->{} in {}", new Object[] { Joiner.on("->").join($$1), string0, this.name });
                return new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation());
            }
            $$1.add(string0);
        }
    }

    private Either<Material, String> findTextureEntry(String string0) {
        for (BlockModel $$1 = this; $$1 != null; $$1 = $$1.parent) {
            Either<Material, String> $$2 = (Either<Material, String>) $$1.textureMap.get(string0);
            if ($$2 != null) {
                return $$2;
            }
        }
        return Either.left(new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation()));
    }

    static boolean isTextureReference(String string0) {
        return string0.charAt(0) == '#';
    }

    public BlockModel getRootModel() {
        return this.parent == null ? this : this.parent.getRootModel();
    }

    public ItemTransforms getTransforms() {
        ItemTransform $$0 = this.getTransform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
        ItemTransform $$1 = this.getTransform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
        ItemTransform $$2 = this.getTransform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
        ItemTransform $$3 = this.getTransform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
        ItemTransform $$4 = this.getTransform(ItemDisplayContext.HEAD);
        ItemTransform $$5 = this.getTransform(ItemDisplayContext.GUI);
        ItemTransform $$6 = this.getTransform(ItemDisplayContext.GROUND);
        ItemTransform $$7 = this.getTransform(ItemDisplayContext.FIXED);
        return new ItemTransforms($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7);
    }

    private ItemTransform getTransform(ItemDisplayContext itemDisplayContext0) {
        return this.parent != null && !this.transforms.hasTransform(itemDisplayContext0) ? this.parent.getTransform(itemDisplayContext0) : this.transforms.getTransform(itemDisplayContext0);
    }

    public String toString() {
        return this.name;
    }

    public static class Deserializer implements JsonDeserializer<BlockModel> {

        public BlockModel deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = jsonElement0.getAsJsonObject();
            List<BlockElement> $$4 = this.getElements(jsonDeserializationContext2, $$3);
            String $$5 = this.getParentName($$3);
            Map<String, Either<Material, String>> $$6 = this.getTextureMap($$3);
            Boolean $$7 = this.getAmbientOcclusion($$3);
            ItemTransforms $$8 = ItemTransforms.NO_TRANSFORMS;
            if ($$3.has("display")) {
                JsonObject $$9 = GsonHelper.getAsJsonObject($$3, "display");
                $$8 = (ItemTransforms) jsonDeserializationContext2.deserialize($$9, ItemTransforms.class);
            }
            List<ItemOverride> $$10 = this.getOverrides(jsonDeserializationContext2, $$3);
            BlockModel.GuiLight $$11 = null;
            if ($$3.has("gui_light")) {
                $$11 = BlockModel.GuiLight.getByName(GsonHelper.getAsString($$3, "gui_light"));
            }
            ResourceLocation $$12 = $$5.isEmpty() ? null : new ResourceLocation($$5);
            return new BlockModel($$12, $$4, $$6, $$7, $$11, $$8, $$10);
        }

        protected List<ItemOverride> getOverrides(JsonDeserializationContext jsonDeserializationContext0, JsonObject jsonObject1) {
            List<ItemOverride> $$2 = Lists.newArrayList();
            if (jsonObject1.has("overrides")) {
                for (JsonElement $$4 : GsonHelper.getAsJsonArray(jsonObject1, "overrides")) {
                    $$2.add((ItemOverride) jsonDeserializationContext0.deserialize($$4, ItemOverride.class));
                }
            }
            return $$2;
        }

        private Map<String, Either<Material, String>> getTextureMap(JsonObject jsonObject0) {
            ResourceLocation $$1 = TextureAtlas.LOCATION_BLOCKS;
            Map<String, Either<Material, String>> $$2 = Maps.newHashMap();
            if (jsonObject0.has("textures")) {
                JsonObject $$3 = GsonHelper.getAsJsonObject(jsonObject0, "textures");
                for (Entry<String, JsonElement> $$4 : $$3.entrySet()) {
                    $$2.put((String) $$4.getKey(), parseTextureLocationOrReference($$1, ((JsonElement) $$4.getValue()).getAsString()));
                }
            }
            return $$2;
        }

        private static Either<Material, String> parseTextureLocationOrReference(ResourceLocation resourceLocation0, String string1) {
            if (BlockModel.isTextureReference(string1)) {
                return Either.right(string1.substring(1));
            } else {
                ResourceLocation $$2 = ResourceLocation.tryParse(string1);
                if ($$2 == null) {
                    throw new JsonParseException(string1 + " is not valid resource location");
                } else {
                    return Either.left(new Material(resourceLocation0, $$2));
                }
            }
        }

        private String getParentName(JsonObject jsonObject0) {
            return GsonHelper.getAsString(jsonObject0, "parent", "");
        }

        @Nullable
        protected Boolean getAmbientOcclusion(JsonObject jsonObject0) {
            return jsonObject0.has("ambientocclusion") ? GsonHelper.getAsBoolean(jsonObject0, "ambientocclusion") : null;
        }

        protected List<BlockElement> getElements(JsonDeserializationContext jsonDeserializationContext0, JsonObject jsonObject1) {
            List<BlockElement> $$2 = Lists.newArrayList();
            if (jsonObject1.has("elements")) {
                for (JsonElement $$3 : GsonHelper.getAsJsonArray(jsonObject1, "elements")) {
                    $$2.add((BlockElement) jsonDeserializationContext0.deserialize($$3, BlockElement.class));
                }
            }
            return $$2;
        }
    }

    public static enum GuiLight {

        FRONT("front"), SIDE("side");

        private final String name;

        private GuiLight(String p_111525_) {
            this.name = p_111525_;
        }

        public static BlockModel.GuiLight getByName(String p_111528_) {
            for (BlockModel.GuiLight $$1 : values()) {
                if ($$1.name.equals(p_111528_)) {
                    return $$1;
                }
            }
            throw new IllegalArgumentException("Invalid gui light: " + p_111528_);
        }

        public boolean lightLikeBlock() {
            return this == SIDE;
        }
    }

    public static class LoopException extends RuntimeException {

        public LoopException(String string0) {
            super(string0);
        }
    }
}