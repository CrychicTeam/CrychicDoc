package snownee.kiwi.client.model;

import com.google.common.base.Predicates;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.geometry.BlockGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.block.def.BlockDefinition;
import snownee.kiwi.block.entity.RetextureBlockEntity;
import snownee.kiwi.util.NBTHelper;

@EventBusSubscriber(bus = Bus.MOD, value = { Dist.CLIENT })
@OnlyIn(Dist.CLIENT)
public class RetextureModel implements IDynamicBakedModel {

    public static ModelProperty<Map<String, BlockDefinition>> TEXTURES = new ModelProperty<>();

    private final ModelBaker baker;

    private final ModelState variant;

    private final ResourceLocation modelLocation;

    private ItemOverrides overrideList;

    private final Cache<String, BakedModel> baked = CacheBuilder.newBuilder().expireAfterAccess(500L, TimeUnit.SECONDS).build();

    private final BlockGeometryBakingContext baseConfiguration;

    private final String particleKey;

    public RetextureModel(ModelBaker baker, ModelState variant, ResourceLocation modelLocation, BlockGeometryBakingContext baseConfiguration, String particleKey, boolean inventory) {
        this.baker = baker;
        this.variant = variant;
        this.modelLocation = modelLocation;
        this.baseConfiguration = baseConfiguration;
        this.overrideList = (ItemOverrides) (inventory ? new RetextureModel.Overrides(this) : ItemOverrides.EMPTY);
        this.particleKey = particleKey;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.baseConfiguration.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.baseConfiguration.isGui3d();
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    public TextureAtlasSprite getParticleIcon(ModelData data) {
        if (data.<Map<String, BlockDefinition>>get(TEXTURES) != null) {
            BlockDefinition supplier = (BlockDefinition) data.get(TEXTURES).get(this.particleKey);
            if (supplier != null) {
                Material material = supplier.renderMaterial(null);
                return (TextureAtlasSprite) this.baker.getModelTextureGetter().apply(material);
            }
        }
        return this.getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return (TextureAtlasSprite) this.baker.getModelTextureGetter().apply(this.baseConfiguration.getMaterial("particle"));
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.overrideList;
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.baseConfiguration.getTransforms();
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
        Map<String, BlockDefinition> overrides = extraData.get(TEXTURES);
        if (overrides == null) {
            overrides = Collections.EMPTY_MAP;
        }
        boolean noSupplier = true;
        if (renderType != null) {
            for (BlockDefinition supplier : overrides.values()) {
                if (supplier != null) {
                    noSupplier = false;
                    if (supplier.canRenderInLayer(renderType)) {
                        BakedModel model = this.getModel(overrides);
                        return model.getQuads(state, side, rand, extraData, renderType);
                    }
                }
            }
        }
        if (renderType != null && (!noSupplier || renderType != RenderType.solid())) {
            return Collections.EMPTY_LIST;
        } else {
            BakedModel model = this.getModel(overrides);
            return model.getQuads(state, side, rand, extraData, renderType);
        }
    }

    public BakedModel getModel(Map<String, BlockDefinition> overrides) {
        String key = generateKey(overrides);
        try {
            return (BakedModel) this.baked.get(key, () -> {
                RetextureModel.ModelConfiguration configuration = new RetextureModel.ModelConfiguration(this.baseConfiguration, overrides);
                return this.baseConfiguration.getCustomGeometry().bake(configuration, this.baker, this.baker.getModelTextureGetter()::apply, this.variant, this.overrideList, this.modelLocation);
            });
        } catch (Exception var4) {
            var4.printStackTrace();
            return Minecraft.getInstance().getModelManager().getMissingModel();
        }
    }

    private static String generateKey(Map<String, BlockDefinition> overrides) {
        return overrides == null ? "" : StringUtils.join(overrides.entrySet(), ',');
    }

    @Override
    public boolean usesBlockLight() {
        return this.baseConfiguration.useBlockLight();
    }

    public static int getColor(Map<String, BlockDefinition> textures, BlockState state, BlockAndTintGetter level, BlockPos pos, int index) {
        BlockDefinition supplier = (BlockDefinition) textures.get(Integer.toString(index));
        return supplier != null ? supplier.getColor(state, level, pos, index) : -1;
    }

    public static class Geometry implements IUnbakedGeometry<RetextureModel.Geometry> {

        private final Function<ModelBaker, BlockModel> blockModel;

        private final String particle;

        private final boolean inventory;

        public Geometry(Function<ModelBaker, BlockModel> blockModel, String particle, boolean inventory) {
            this.blockModel = blockModel;
            this.particle = particle;
            this.inventory = inventory;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext owner, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
            return new RetextureModel(baker, modelTransform, modelLocation, ((BlockModel) this.blockModel.apply(baker)).customData, this.particle, this.inventory);
        }
    }

    public static class Loader implements IGeometryLoader<RetextureModel.Geometry> {

        public static final RetextureModel.Loader INSTANCE = new RetextureModel.Loader();

        private Loader() {
        }

        public RetextureModel.Geometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
            Function<ModelBaker, BlockModel> blockModel = baker -> (BlockModel) baker.getModel(new ResourceLocation(GsonHelper.getAsString(jsonObject, "base")));
            return new RetextureModel.Geometry(blockModel, GsonHelper.getAsString(jsonObject, "particle", "0"), GsonHelper.getAsBoolean(jsonObject, "inventory", true));
        }
    }

    public static class ModelConfiguration implements IGeometryBakingContext {

        private final IGeometryBakingContext baseConfiguration;

        private final Map<String, BlockDefinition> overrides;

        public ModelConfiguration(IGeometryBakingContext baseConfiguration, Map<String, BlockDefinition> overrides) {
            this.baseConfiguration = baseConfiguration;
            this.overrides = overrides;
        }

        @Override
        public String getModelName() {
            return this.baseConfiguration.getModelName();
        }

        @Override
        public boolean hasMaterial(String name) {
            return this.baseConfiguration.hasMaterial(name);
        }

        @Override
        public Material getMaterial(String name) {
            if (name.charAt(0) == '#') {
                String ref = name.substring(1);
                int i = ref.lastIndexOf(95);
                if (i != -1) {
                    String ref0 = ref.substring(0, i);
                    BlockDefinition supplier = (BlockDefinition) this.overrides.get(ref0);
                    if (supplier != null) {
                        Direction direction = Direction.byName(ref.substring(i + 1));
                        return supplier.renderMaterial(direction);
                    }
                }
                BlockDefinition supplier = (BlockDefinition) this.overrides.get(ref);
                if (supplier != null) {
                    return supplier.renderMaterial(null);
                }
            }
            return this.baseConfiguration.getMaterial(name);
        }

        @Override
        public boolean isGui3d() {
            return this.baseConfiguration.isGui3d();
        }

        @Override
        public boolean useBlockLight() {
            return this.baseConfiguration.useBlockLight();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return this.baseConfiguration.useAmbientOcclusion();
        }

        @Override
        public ItemTransforms getTransforms() {
            return this.baseConfiguration.getTransforms();
        }

        @Override
        public Transformation getRootTransform() {
            return this.baseConfiguration.getRootTransform();
        }

        @Nullable
        @Override
        public ResourceLocation getRenderTypeHint() {
            return null;
        }

        @Override
        public boolean isComponentVisible(String component, boolean fallback) {
            return this.baseConfiguration.isComponentVisible(component, fallback);
        }
    }

    public static class Overrides extends ItemOverrides {

        private final RetextureModel baked;

        private final Cache<ItemStack, BakedModel> cache = CacheBuilder.newBuilder().maximumSize(100L).expireAfterWrite(300L, TimeUnit.SECONDS).weakKeys().build();

        public Overrides(RetextureModel model) {
            this.baked = model;
        }

        @Override
        public BakedModel resolve(BakedModel model, ItemStack stack, ClientLevel worldIn, LivingEntity entityIn, int seed) {
            if (model instanceof RetextureModel) {
                try {
                    model = (BakedModel) this.cache.get(stack, () -> this.baked.getModel(overridesFromItem(stack)));
                } catch (ExecutionException var7) {
                    var7.printStackTrace();
                }
            }
            return model;
        }

        public static Map<String, BlockDefinition> overridesFromItem(ItemStack stack) {
            CompoundTag data = NBTHelper.of(stack).getTag("BlockEntityTag.Overrides");
            if (data == null) {
                data = new CompoundTag();
            }
            Set<String> keySet = data.getAllKeys();
            Map<String, BlockDefinition> overrides = Maps.newHashMapWithExpectedSize(keySet.size());
            keySet.forEach(k -> overrides.put(k, null));
            RetextureBlockEntity.readTextures(overrides, data, Predicates.alwaysTrue());
            return overrides;
        }

        public ImmutableList<ItemOverrides.BakedOverride> getOverrides() {
            return ImmutableList.of();
        }
    }
}