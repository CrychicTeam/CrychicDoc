package se.mickelus.tetra.client.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.textures.UnitTextureAtlasSprite;

public final class UnresolvedItemModel implements IUnbakedGeometry<UnresolvedItemModel> {

    private final ItemTransforms cameraTransforms;

    ModularOverrideList overrideList;

    private Map<String, ItemTransforms> transformVariants = Collections.emptyMap();

    public UnresolvedItemModel(ItemTransforms cameraTransforms, Map<String, ItemTransforms> transformVariants) {
        this(cameraTransforms);
        this.transformVariants = transformVariants != null ? transformVariants : Collections.emptyMap();
    }

    public UnresolvedItemModel(ItemTransforms cameraTransforms) {
        this.cameraTransforms = cameraTransforms;
    }

    protected ItemTransforms getCameraTransforms(String transformVariant) {
        if (transformVariant != null && this.transformVariants.containsKey(transformVariant)) {
            ItemTransforms variant = (ItemTransforms) this.transformVariants.get(transformVariant);
            return new ItemTransforms(variant.hasTransform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND) ? variant.thirdPersonLeftHand : this.cameraTransforms.thirdPersonLeftHand, variant.hasTransform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) ? variant.thirdPersonRightHand : this.cameraTransforms.thirdPersonRightHand, variant.hasTransform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND) ? variant.firstPersonLeftHand : this.cameraTransforms.firstPersonLeftHand, variant.hasTransform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) ? variant.firstPersonRightHand : this.cameraTransforms.firstPersonRightHand, variant.hasTransform(ItemDisplayContext.HEAD) ? variant.head : this.cameraTransforms.head, variant.hasTransform(ItemDisplayContext.GUI) ? variant.gui : this.cameraTransforms.gui, variant.hasTransform(ItemDisplayContext.GROUND) ? variant.ground : this.cameraTransforms.ground, variant.hasTransform(ItemDisplayContext.FIXED) ? variant.fixed : this.cameraTransforms.fixed, variant.moddedTransforms);
        } else {
            return this.cameraTransforms;
        }
    }

    public void clearCache() {
        Optional.ofNullable(this.overrideList).ifPresent(ModularOverrideList::clearCache);
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        this.overrideList = new ModularOverrideList(this, context, baker, spriteGetter, modelState, modelLocation);
        return new UnresolvedItemModel.Baked(this.overrideList);
    }

    private static class Baked extends SimpleBakedModel {

        private static final Material MISSING_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation());

        public Baked(ItemOverrides itemOverrideList) {
            super(List.of(), Map.of(), false, false, false, UnitTextureAtlasSprite.INSTANCE, ItemTransforms.NO_TRANSFORMS, itemOverrideList, RenderTypeGroup.EMPTY);
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return MISSING_TEXTURE.sprite();
        }

        public List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous) {
            return List.of();
        }
    }
}