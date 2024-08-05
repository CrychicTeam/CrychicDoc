package net.mehvahdjukaar.supplementaries.dynamicpack;

import com.google.gson.JsonParser;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.assets.LangBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter;
import net.mehvahdjukaar.moonlight.api.resources.textures.SpriteUtils;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.client.GlobeManager;
import net.mehvahdjukaar.supplementaries.client.renderers.color.ColorHelper;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class ClientDynamicResourcesGenerator extends DynClientResourcesGenerator {

    public static final ClientDynamicResourcesGenerator INSTANCE = new ClientDynamicResourcesGenerator();

    public ClientDynamicResourcesGenerator() {
        super(new DynamicTexturePack(Supplementaries.res("generated_pack")));
        this.dynamicPack.addNamespaces(new String[] { "minecraft" });
        this.dynamicPack.setGenerateDebugResources(PlatHelper.isDev() || (Boolean) CommonConfigs.General.DEBUG_RESOURCES.get());
    }

    @Override
    public Logger getLogger() {
        return Supplementaries.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {
        GlobeManager.refreshColorsAndTextures(manager);
        ColorHelper.refreshBubbleColors(manager);
        if ((Boolean) CommonConfigs.Redstone.ENDERMAN_HEAD_ENABLED.get()) {
            try (TextureImage text = TextureImage.open(manager, new ResourceLocation("entity/enderman/enderman"));
                TextureImage eyeText = TextureImage.open(manager, new ResourceLocation("entity/enderman/enderman_eyes"))) {
                this.dynamicPack.addAndCloseTexture(Supplementaries.res("entity/enderman_head"), text, false);
                this.dynamicPack.addAndCloseTexture(Supplementaries.res("entity/enderman_head_eyes"), eyeText, false);
            } catch (Exception var16) {
            }
        }
        if ((Boolean) CommonConfigs.Tools.ROPE_ARROW_ENABLED.get()) {
            RPUtils.appendModelOverride(manager, this.dynamicPack, new ResourceLocation("crossbow"), e -> e.add(new ItemOverride(new ResourceLocation("item/crossbow_rope_arrow"), List.of(new ItemOverride.Predicate(new ResourceLocation("charged"), 1.0F), new ItemOverride.Predicate(Supplementaries.res("rope_arrow"), 1.0F)))));
        }
        if ((Boolean) CommonConfigs.Tools.ANTIQUE_INK_ENABLED.get()) {
            RPUtils.appendModelOverride(manager, this.dynamicPack, new ResourceLocation("written_book"), e -> e.add(new ItemOverride(new ResourceLocation("item/written_book_tattered"), List.of(new ItemOverride.Predicate(Supplementaries.res("antique_ink"), 1.0F)))));
            RPUtils.appendModelOverride(manager, this.dynamicPack, new ResourceLocation("filled_map"), e -> e.add(new ItemOverride(new ResourceLocation("item/antique_map"), List.of(new ItemOverride.Predicate(Supplementaries.res("antique_ink"), 1.0F)))));
        }
        RPUtils.appendModelOverride(manager, this.dynamicPack, Supplementaries.res("globe"), e -> {
            int i = 0;
            for (ResourceLocation text : GlobeManager.Type.textures) {
                String name = text.getPath().split("/")[3].split("\\.")[0];
                e.add(new ItemOverride(Supplementaries.res("item/" + name), List.of(new ItemOverride.Predicate(Supplementaries.res("type"), (float) i))));
                i++;
                this.dynamicPack.addItemModel(Supplementaries.res(name), JsonParser.parseString("{\n    \"parent\": \"item/generated\",\n    \"textures\": {\n        \"layer0\": \"supplementaries:item/globes/" + name + "\"    }\n}\n"));
            }
        });
        StaticResource spItemModel = StaticResource.getOrLog(manager, ResType.ITEM_MODELS.getPath(Supplementaries.res("sign_post_oak")));
        StaticResource spBlockModel = StaticResource.getOrLog(manager, ResType.BLOCK_MODELS.getPath(Supplementaries.res("sign_posts/sign_post_oak")));
        ModRegistry.SIGN_POST_ITEMS.forEach((wood, sign) -> {
            String id = Utils.getID((Item) sign).getPath();
            try {
                this.addSimilarJsonResource(manager, spItemModel, "sign_post_oak", id);
                this.addSimilarJsonResource(manager, spBlockModel, "sign_post_oak", id);
            } catch (Exception var8) {
                this.getLogger().error("Failed to generate Sign Post item model for {} : {}", sign, var8);
            }
        });
        try (TextureImage template = TextureImage.open(manager, Supplementaries.res("item/sign_posts/template"))) {
            Respriter respriter = Respriter.of(template);
            ModRegistry.SIGN_POST_ITEMS.forEach((wood, sign) -> {
                ResourceLocation textureRes = Supplementaries.res("item/sign_posts/" + Utils.getID((Item) sign).getPath());
                if (!this.alreadyHasTextureAtLocation(manager, textureRes)) {
                    TextureImage newImage = null;
                    Item signItem = wood.getItemOfThis("sign");
                    if (signItem != null) {
                        try (TextureImage vanillaSign = TextureImage.open(manager, RPUtils.findFirstItemTextureLocation(manager, signItem));
                            TextureImage signMask = TextureImage.open(manager, Supplementaries.res("item/hanging_signs/sign_board_mask"))) {
                            List<Palette> targetPalette = Palette.fromAnimatedImage(vanillaSign, signMask);
                            newImage = respriter.recolor(targetPalette);
                            try (TextureImage scribbles = recolorFromVanilla(manager, vanillaSign, Supplementaries.res("item/hanging_signs/sign_scribbles_mask"), Supplementaries.res("item/sign_posts/scribbles_template"))) {
                                newImage.applyOverlay(scribbles);
                            } catch (Exception var21x) {
                                this.getLogger().error("Could not properly color Sign Post item texture for {} : {}", sign, var21x);
                            }
                        } catch (Exception var24) {
                        }
                    }
                    if (newImage == null) {
                        try (TextureImage plankPalette = TextureImage.open(manager, RPUtils.findFirstBlockTextureLocation(manager, wood.planks))) {
                            Palette targetPalette = SpriteUtils.extrapolateWoodItemPalette(plankPalette);
                            newImage = respriter.recolor(targetPalette);
                        } catch (Exception var19x) {
                            this.getLogger().error("Failed to generate Sign Post item texture for for {} : {}", sign, var19x);
                        }
                    }
                    if (newImage != null) {
                        this.dynamicPack.addAndCloseTexture(textureRes, newImage);
                    }
                }
            });
        } catch (Exception var13) {
            this.getLogger().error("Could not generate any Sign Post item texture : ", var13);
        }
        try (TextureImage template = TextureImage.open(manager, Supplementaries.res("block/sign_posts/sign_post_oak"))) {
            Respriter respriter = Respriter.of(template);
            ModRegistry.SIGN_POST_ITEMS.forEach((wood, sign) -> {
                ResourceLocation textureRes = Supplementaries.res("block/sign_posts/" + Utils.getID((Item) sign).getPath());
                if (!this.alreadyHasTextureAtLocation(manager, textureRes)) {
                    try (TextureImage plankTexture = TextureImage.open(manager, RPUtils.findFirstBlockTextureLocation(manager, wood.planks))) {
                        Palette palette = Palette.fromImage(plankTexture);
                        TextureImage newImage = respriter.recolor(palette);
                        this.dynamicPack.addAndCloseTexture(textureRes, newImage);
                    } catch (Exception var11x) {
                        this.getLogger().error("Failed to generate Sign Post block texture for for {} : {}", sign, var11x);
                    }
                }
            });
        } catch (Exception var11) {
            this.getLogger().error("Could not generate any Sign Post block texture : ", var11);
        }
    }

    @Nullable
    public static TextureImage recolorFromVanilla(ResourceManager manager, TextureImage vanillaTexture, ResourceLocation vanillaMask, ResourceLocation templateTexture) {
        try {
            TextureImage var8;
            try (TextureImage scribbleMask = TextureImage.open(manager, vanillaMask);
                TextureImage template = TextureImage.open(manager, templateTexture)) {
                Respriter respriter = Respriter.of(template);
                Palette palette = Palette.fromImage(vanillaTexture, scribbleMask);
                var8 = respriter.recolor(palette);
            }
            return var8;
        } catch (Exception var13) {
            return null;
        }
    }

    @Override
    public void addDynamicTranslations(AfterLanguageLoadEvent lang) {
        ModRegistry.SIGN_POST_ITEMS.forEach((type, item) -> LangBuilder.addDynamicEntry(lang, "item.supplementaries.sign_post", type, item));
    }
}