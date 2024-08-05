package noobanidus.mods.lootr.client.block;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
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
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import noobanidus.mods.lootr.block.LootrBarrelBlock;
import noobanidus.mods.lootr.config.ConfigManager;
import org.jetbrains.annotations.NotNull;

public class BarrelModel implements IUnbakedGeometry<BarrelModel> {

    private final UnbakedModel opened;

    private final UnbakedModel unopened;

    private final UnbakedModel vanilla;

    private final UnbakedModel old_opened;

    private final UnbakedModel old_unopened;

    public BarrelModel(UnbakedModel opened, UnbakedModel unopened, UnbakedModel vanilla, UnbakedModel old_unopened, UnbakedModel old_opened) {
        this.opened = opened;
        this.unopened = unopened;
        this.vanilla = vanilla;
        this.old_opened = old_opened;
        this.old_unopened = old_unopened;
    }

    private static BakedModel buildModel(UnbakedModel entry, ModelState modelTransform, ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ResourceLocation modelLocation) {
        return entry.bake(bakery, spriteGetter, modelTransform, modelLocation);
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new BarrelModel.BarrelBakedModel(context.useAmbientOcclusion(), context.isGui3d(), context.useBlockLight(), (TextureAtlasSprite) spriteGetter.apply(context.getMaterial("particle")), overrides, buildModel(this.opened, modelTransform, bakery, spriteGetter, modelLocation), buildModel(this.unopened, modelTransform, bakery, spriteGetter, modelLocation), buildModel(this.vanilla, modelTransform, bakery, spriteGetter, modelLocation), buildModel(this.old_opened, modelTransform, bakery, spriteGetter, modelLocation), buildModel(this.old_unopened, modelTransform, bakery, spriteGetter, modelLocation), context.getTransforms());
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
        this.opened.resolveParents(modelGetter);
        this.unopened.resolveParents(modelGetter);
        this.vanilla.resolveParents(modelGetter);
        this.old_opened.resolveParents(modelGetter);
        this.old_unopened.resolveParents(modelGetter);
    }

    private static final class BarrelBakedModel implements IDynamicBakedModel {

        private final boolean ambientOcclusion;

        private final boolean gui3d;

        private final boolean isSideLit;

        private final TextureAtlasSprite particle;

        private final ItemOverrides overrides;

        private final BakedModel opened;

        private final BakedModel unopened;

        private final BakedModel vanilla;

        private final BakedModel old_opened;

        private final BakedModel old_unopened;

        private final ItemTransforms cameraTransforms;

        public BarrelBakedModel(boolean ambientOcclusion, boolean isGui3d, boolean isSideLit, TextureAtlasSprite particle, ItemOverrides overrides, BakedModel opened, BakedModel unopened, BakedModel vanilla, BakedModel old_opened, BakedModel old_unopened, ItemTransforms cameraTransforms) {
            this.isSideLit = isSideLit;
            this.cameraTransforms = cameraTransforms;
            this.ambientOcclusion = ambientOcclusion;
            this.gui3d = isGui3d;
            this.particle = particle;
            this.overrides = overrides;
            this.opened = opened;
            this.unopened = unopened;
            this.vanilla = vanilla;
            this.old_opened = old_opened;
            this.old_unopened = old_unopened;
        }

        @NotNull
        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @NotNull RenderType renderType) {
            BakedModel model;
            if (ConfigManager.isVanillaTextures()) {
                model = this.vanilla;
            } else if (extraData.has(LootrBarrelBlock.OPENED)) {
                if (extraData.get(LootrBarrelBlock.OPENED) == Boolean.TRUE) {
                    model = ConfigManager.isOldTextures() ? this.old_opened : this.opened;
                } else {
                    model = ConfigManager.isOldTextures() ? this.old_unopened : this.unopened;
                }
            } else {
                model = ConfigManager.isOldTextures() ? this.old_unopened : this.unopened;
            }
            return model.getQuads(state, side, rand, extraData, renderType);
        }

        @Override
        public boolean useAmbientOcclusion() {
            return this.ambientOcclusion;
        }

        @Override
        public boolean isGui3d() {
            return this.gui3d;
        }

        @Override
        public boolean usesBlockLight() {
            return this.isSideLit;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return this.particle;
        }

        public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
            if (ConfigManager.isVanillaTextures()) {
                return this.vanilla.getParticleIcon();
            } else if (data.get(LootrBarrelBlock.OPENED) == Boolean.TRUE) {
                return ConfigManager.isOldTextures() ? this.old_opened.getParticleIcon() : this.opened.getParticleIcon();
            } else {
                return ConfigManager.isOldTextures() ? this.old_unopened.getParticleIcon() : this.unopened.getParticleIcon();
            }
        }

        @Override
        public ItemTransforms getTransforms() {
            return this.cameraTransforms;
        }

        @Override
        public ItemOverrides getOverrides() {
            return this.overrides;
        }
    }

    public static final class Loader implements IGeometryLoader<BarrelModel> {

        public static final BarrelModel.Loader INSTANCE = new BarrelModel.Loader();

        private Loader() {
        }

        public BarrelModel read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
            UnbakedModel unopened = (UnbakedModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(modelContents, "unopened"), BlockModel.class);
            UnbakedModel opened = (UnbakedModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(modelContents, "opened"), BlockModel.class);
            UnbakedModel vanilla = (UnbakedModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(modelContents, "vanilla"), BlockModel.class);
            UnbakedModel old_unopened = (UnbakedModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(modelContents, "old_unopened"), BlockModel.class);
            UnbakedModel old_opened = (UnbakedModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(modelContents, "old_opened"), BlockModel.class);
            return new BarrelModel(opened, unopened, vanilla, old_unopened, old_opened);
        }
    }
}