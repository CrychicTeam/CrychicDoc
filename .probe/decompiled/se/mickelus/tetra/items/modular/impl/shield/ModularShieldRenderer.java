package se.mickelus.tetra.items.modular.impl.shield;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.module.data.ModuleModel;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class ModularShieldRenderer extends BlockEntityWithoutLevelRenderer {

    public static ModelLayerLocation layer = new ModelLayerLocation(new ResourceLocation("tetra", "item/shield"), "main");

    public static ModelLayerLocation bannerLayer = new ModelLayerLocation(new ResourceLocation("tetra", "item/shield_banner"), "main");

    private final EntityModelSet modelSet;

    public ModularShieldBannerModel bannerModel;

    private ModularShieldModel model;

    public ModularShieldRenderer(Minecraft minecraft) {
        super(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        this.modelSet = minecraft.getEntityModels();
        this.model = new ModularShieldModel(this.modelSet.bakeLayer(layer));
        this.bannerModel = new ModularShieldBannerModel(this.modelSet.bakeLayer(bannerLayer));
        ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(this);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager0) {
        this.model = new ModularShieldModel(this.modelSet.bakeLayer(layer));
        this.bannerModel = new ModularShieldBannerModel(this.modelSet.bakeLayer(bannerLayer));
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        Collection<ModuleModel> models = (Collection<ModuleModel>) CastOptional.cast(itemStack.getItem(), ModularShieldItem.class).map(item -> item.getModels(itemStack, null)).orElse(ImmutableList.of());
        models.forEach(modelData -> {
            ModelPart bannerPart = this.bannerModel.getModel(modelData.type);
            if (bannerPart != null) {
                if (itemStack.getTagElement("BlockEntityTag") != null) {
                    this.renderBanner(itemStack, bannerPart, matrixStack, buffer, combinedLight, combinedOverlay);
                }
            } else {
                ModelPart modelPart = this.model.getModel(modelData.type);
                if (modelPart != null) {
                    Material material = new Material(TextureAtlas.LOCATION_BLOCKS, modelData.location);
                    VertexConsumer vertexBuilder = material.sprite().wrap(ItemRenderer.getFoilBuffer(buffer, this.model.m_103119_(material.atlasLocation()), false, itemStack.hasFoil()));
                    float r = (float) (modelData.tint >> 16 & 0xFF) / 255.0F;
                    float g = (float) (modelData.tint >> 8 & 0xFF) / 255.0F;
                    float b = (float) (modelData.tint >> 0 & 0xFF) / 255.0F;
                    float a = (float) (modelData.tint >> 24 & 0xFF) / 255.0F;
                    a = a == 0.0F ? 1.0F : a;
                    modelPart.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, r, g, b, a);
                }
            }
        });
        matrixStack.popPose();
    }

    private void renderBanner(ItemStack itemStack, ModelPart modelRenderer, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(itemStack), BannerBlockEntity.getItemPatterns(itemStack));
        for (int i = 0; i < 17 && i < list.size(); i++) {
            Pair<Holder<BannerPattern>, DyeColor> pair = (Pair<Holder<BannerPattern>, DyeColor>) list.get(i);
            float[] tint = ((DyeColor) pair.getSecond()).getTextureDiffuseColors();
            ((Holder) pair.getFirst()).unwrapKey().map(Sheets::m_234349_).ifPresent(material -> {
                VertexConsumer vertexBuilder = material.sprite().wrap(ItemRenderer.getFoilBuffer(buffer, RenderType.entitySmoothCutout(material.atlasLocation()), false, itemStack.hasFoil()));
                modelRenderer.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, tint[0], tint[1], tint[2], 1.0F);
            });
        }
    }

    private void renderEtching(ItemStack itemStack, ModelPart modelRenderer, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(itemStack), BannerBlockEntity.getItemPatterns(itemStack));
        for (int i = 0; i < 17 && i < list.size(); i++) {
            Pair<Holder<BannerPattern>, DyeColor> pair = (Pair<Holder<BannerPattern>, DyeColor>) list.get(i);
            if (!BannerPatterns.BASE.equals(((Holder) pair.getFirst()).unwrapKey().orElse(null))) {
                float[] tint = ((DyeColor) pair.getSecond()).getTextureDiffuseColors();
                ((Holder) pair.getFirst()).unwrapKey().map(Sheets::m_234349_).ifPresent(material -> {
                    VertexConsumer vertexBuilder = material.sprite().wrap(ItemRenderer.getFoilBuffer(buffer, RenderType.entityNoOutline(material.atlasLocation()), false, itemStack.hasFoil()));
                    modelRenderer.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, tint[0], tint[1], tint[2], 0.7F);
                });
            }
        }
    }
}