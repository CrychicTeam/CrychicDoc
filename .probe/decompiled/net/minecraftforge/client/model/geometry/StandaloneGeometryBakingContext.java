package net.minecraftforge.client.model.geometry;

import com.google.common.base.Predicates;
import com.mojang.math.Transformation;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class StandaloneGeometryBakingContext implements IGeometryBakingContext {

    public static final ResourceLocation LOCATION = new ResourceLocation("forge", "standalone");

    public static final StandaloneGeometryBakingContext INSTANCE = create(LOCATION);

    private final ResourceLocation modelName;

    private final Predicate<String> materialCheck;

    private final Function<String, Material> materialLookup;

    private final boolean isGui3d;

    private final boolean useBlockLight;

    private final boolean useAmbientOcclusion;

    private final ItemTransforms transforms;

    private final Transformation rootTransform;

    @Nullable
    private final ResourceLocation renderTypeHint;

    private final BiPredicate<String, Boolean> visibilityTest;

    public static StandaloneGeometryBakingContext create(ResourceLocation modelName) {
        return builder().build(modelName);
    }

    public static StandaloneGeometryBakingContext create(Map<String, ResourceLocation> textures) {
        return create(LOCATION, textures);
    }

    public static StandaloneGeometryBakingContext create(ResourceLocation modelName, Map<String, ResourceLocation> textures) {
        return builder().withTextures(textures, MissingTextureAtlasSprite.getLocation()).build(modelName);
    }

    private StandaloneGeometryBakingContext(ResourceLocation modelName, Predicate<String> materialCheck, Function<String, Material> materialLookup, boolean isGui3d, boolean useBlockLight, boolean useAmbientOcclusion, ItemTransforms transforms, Transformation rootTransform, @Nullable ResourceLocation renderTypeHint, BiPredicate<String, Boolean> visibilityTest) {
        this.modelName = modelName;
        this.materialCheck = materialCheck;
        this.materialLookup = materialLookup;
        this.isGui3d = isGui3d;
        this.useBlockLight = useBlockLight;
        this.useAmbientOcclusion = useAmbientOcclusion;
        this.transforms = transforms;
        this.rootTransform = rootTransform;
        this.renderTypeHint = renderTypeHint;
        this.visibilityTest = visibilityTest;
    }

    @Override
    public String getModelName() {
        return this.modelName.toString();
    }

    @Override
    public boolean hasMaterial(String name) {
        return this.materialCheck.test(name);
    }

    @Override
    public Material getMaterial(String name) {
        return (Material) this.materialLookup.apply(name);
    }

    @Override
    public boolean isGui3d() {
        return this.isGui3d;
    }

    @Override
    public boolean useBlockLight() {
        return this.useBlockLight;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.useAmbientOcclusion;
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.transforms;
    }

    @Override
    public Transformation getRootTransform() {
        return this.rootTransform;
    }

    @Nullable
    @Override
    public ResourceLocation getRenderTypeHint() {
        return this.renderTypeHint;
    }

    @Override
    public boolean isComponentVisible(String component, boolean fallback) {
        return this.visibilityTest.test(component, fallback);
    }

    public static StandaloneGeometryBakingContext.Builder builder() {
        return new StandaloneGeometryBakingContext.Builder();
    }

    public static StandaloneGeometryBakingContext.Builder builder(IGeometryBakingContext parent) {
        return new StandaloneGeometryBakingContext.Builder(parent);
    }

    public static final class Builder {

        private static final Material NO_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation());

        private Predicate<String> materialCheck = Predicates.alwaysFalse();

        private Function<String, Material> materialLookup = $ -> NO_MATERIAL;

        private boolean isGui3d = true;

        private boolean useBlockLight = true;

        private boolean useAmbientOcclusion = true;

        private ItemTransforms transforms = ItemTransforms.NO_TRANSFORMS;

        private Transformation rootTransform = Transformation.identity();

        @Nullable
        private ResourceLocation renderTypeHint;

        private BiPredicate<String, Boolean> visibilityTest = (c, def) -> def;

        private Builder() {
        }

        private Builder(IGeometryBakingContext parent) {
            this.materialCheck = parent::hasMaterial;
            this.materialLookup = parent::getMaterial;
            this.isGui3d = parent.isGui3d();
            this.useBlockLight = parent.useBlockLight();
            this.useAmbientOcclusion = parent.useAmbientOcclusion();
            this.transforms = parent.getTransforms();
            this.rootTransform = parent.getRootTransform();
            this.renderTypeHint = parent.getRenderTypeHint();
            this.visibilityTest = parent::isComponentVisible;
        }

        public StandaloneGeometryBakingContext.Builder withTextures(Map<String, ResourceLocation> textures, ResourceLocation defaultTexture) {
            return this.withTextures(TextureAtlas.LOCATION_BLOCKS, textures, defaultTexture);
        }

        public StandaloneGeometryBakingContext.Builder withTextures(ResourceLocation atlasLocation, Map<String, ResourceLocation> textures, ResourceLocation defaultTexture) {
            this.materialCheck = textures::containsKey;
            this.materialLookup = name -> new Material(atlasLocation, (ResourceLocation) textures.getOrDefault(name, defaultTexture));
            return this;
        }

        public StandaloneGeometryBakingContext.Builder withMaterials(Map<String, Material> materials, Material defaultMaterial) {
            this.materialCheck = materials::containsKey;
            this.materialLookup = name -> (Material) materials.getOrDefault(name, defaultMaterial);
            return this;
        }

        public StandaloneGeometryBakingContext.Builder withGui3d(boolean isGui3d) {
            this.isGui3d = isGui3d;
            return this;
        }

        public StandaloneGeometryBakingContext.Builder withUseBlockLight(boolean useBlockLight) {
            this.useBlockLight = useBlockLight;
            return this;
        }

        public StandaloneGeometryBakingContext.Builder withUseAmbientOcclusion(boolean useAmbientOcclusion) {
            this.useAmbientOcclusion = useAmbientOcclusion;
            return this;
        }

        public StandaloneGeometryBakingContext.Builder withTransforms(ItemTransforms transforms) {
            this.transforms = transforms;
            return this;
        }

        public StandaloneGeometryBakingContext.Builder withRootTransform(Transformation rootTransform) {
            this.rootTransform = rootTransform;
            return this;
        }

        public StandaloneGeometryBakingContext.Builder withRenderTypeHint(ResourceLocation renderTypeHint) {
            this.renderTypeHint = renderTypeHint;
            return this;
        }

        public StandaloneGeometryBakingContext.Builder withVisibleComponents(Object2BooleanMap<String> parts) {
            this.visibilityTest = parts::getOrDefault;
            return this;
        }

        public StandaloneGeometryBakingContext build(ResourceLocation modelName) {
            return new StandaloneGeometryBakingContext(modelName, this.materialCheck, this.materialLookup, this.isGui3d, this.useBlockLight, this.useAmbientOcclusion, this.transforms, this.rootTransform, this.renderTypeHint, this.visibilityTest);
        }
    }
}