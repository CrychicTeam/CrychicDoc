package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.gui.data.EntityLinkData;
import com.github.alexthe666.citadel.client.gui.data.EntityRenderData;
import com.github.alexthe666.citadel.client.gui.data.ImageData;
import com.github.alexthe666.citadel.client.gui.data.ItemRenderData;
import com.github.alexthe666.citadel.client.gui.data.LineData;
import com.github.alexthe666.citadel.client.gui.data.LinkData;
import com.github.alexthe666.citadel.client.gui.data.RecipeData;
import com.github.alexthe666.citadel.client.gui.data.TabulaRenderData;
import com.github.alexthe666.citadel.client.gui.data.Whitespace;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.TabulaModelHandler;
import com.github.alexthe666.citadel.recipe.SpecialRecipeInGuideBook;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Axis;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.StringConcatFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class GuiBasicBook extends Screen {

    private static final ResourceLocation BOOK_PAGE_TEXTURE = new ResourceLocation("citadel:textures/gui/book/book_pages.png");

    private static final ResourceLocation BOOK_BINDING_TEXTURE = new ResourceLocation("citadel:textures/gui/book/book_binding.png");

    private static final ResourceLocation BOOK_WIDGET_TEXTURE = new ResourceLocation("citadel:textures/gui/book/widgets.png");

    private static final ResourceLocation BOOK_BUTTONS_TEXTURE = new ResourceLocation("citadel:textures/gui/book/link_buttons.png");

    protected final List<LineData> lines = new ArrayList();

    protected final List<LinkData> links = new ArrayList();

    protected final List<ItemRenderData> itemRenders = new ArrayList();

    protected final List<RecipeData> recipes = new ArrayList();

    protected final List<TabulaRenderData> tabulaRenders = new ArrayList();

    protected final List<EntityRenderData> entityRenders = new ArrayList();

    protected final List<EntityLinkData> entityLinks = new ArrayList();

    protected final List<ImageData> images = new ArrayList();

    protected final List<Whitespace> yIndexesToSkip = new ArrayList();

    private final Map<String, TabulaModel> renderedTabulaModels = new HashMap();

    private final Map<String, Entity> renderedEntites = new HashMap();

    private final Map<String, ResourceLocation> textureMap = new HashMap();

    protected ItemStack bookStack;

    protected int xSize = 390;

    protected int ySize = 320;

    protected int currentPageCounter = 0;

    protected int maxPagesFromPrinting = 0;

    protected int linesFromJSON = 0;

    protected int linesFromPrinting = 0;

    protected ResourceLocation prevPageJSON;

    protected ResourceLocation currentPageJSON;

    protected ResourceLocation currentPageText = null;

    protected BookPageButton buttonNextPage;

    protected BookPageButton buttonPreviousPage;

    protected BookPage internalPage = null;

    protected String writtenTitle = "";

    protected int preservedPageIndex = 0;

    protected String entityTooltip;

    private int mouseX;

    private int mouseY;

    public GuiBasicBook(ItemStack bookStack, Component title) {
        super(title);
        this.bookStack = bookStack;
        this.currentPageJSON = this.getRootPage();
    }

    public static void drawTabulaModelOnScreen(GuiGraphics guiGraphics, TabulaModel model, ResourceLocation tex, int posX, int posY, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY) {
        float f = (float) Math.atan((double) (mouseX / 40.0F));
        float f1 = (float) Math.atan((double) (mouseY / 40.0F));
        PoseStack matrixstack = new PoseStack();
        matrixstack.translate((double) ((float) posX), (double) ((float) posY), 120.0);
        matrixstack.scale(scale, scale, scale);
        Quaternionf quaternion = Axis.ZP.rotationDegrees(0.0F);
        Quaternionf quaternion1 = Axis.XP.rotationDegrees(f1 * 20.0F);
        if (follow) {
            quaternion.mul(quaternion1);
        }
        matrixstack.mulPose(quaternion);
        if (follow) {
            matrixstack.mulPose(Axis.YP.rotationDegrees(180.0F + f * 40.0F));
        }
        matrixstack.mulPose(Axis.XP.rotationDegrees((float) (-xRot)));
        matrixstack.mulPose(Axis.YP.rotationDegrees((float) yRot));
        matrixstack.mulPose(Axis.ZP.rotationDegrees((float) zRot));
        EntityRenderDispatcher entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conjugate();
        entityrenderermanager.overrideCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        MultiBufferSource.BufferSource irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            VertexConsumer ivertexbuilder = irendertypebuffer$impl.getBuffer(RenderType.entityCutoutNoCull(tex));
            model.resetToDefaultPose();
            model.m_7695_(matrixstack, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        });
        Lighting.setupFor3DItems();
    }

    public void drawEntityOnScreen(GuiGraphics guiGraphics, MultiBufferSource bufferSource, int posX, int posY, float zOff, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY, Entity entity) {
        float customYaw = (float) posX - mouseX;
        float customPitch = (float) posY - mouseY;
        float f = (float) Math.atan((double) (customYaw / 40.0F));
        float f1 = (float) Math.atan((double) (customPitch / 40.0F));
        if (follow) {
            float setX = f1 * 20.0F;
            float setY = f * 20.0F;
            entity.setXRot(setX);
            entity.setYRot(setY);
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).yBodyRot = setY;
                ((LivingEntity) entity).yBodyRotO = setY;
                ((LivingEntity) entity).yHeadRot = setY;
                ((LivingEntity) entity).yHeadRotO = setY;
            }
        } else {
            f = 0.0F;
            f1 = 0.0F;
        }
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) posX, (float) posY, zOff);
        guiGraphics.pose().mulPoseMatrix(new Matrix4f().scaling(scale, scale, -scale));
        Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
        Quaternionf quaternion1 = Axis.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        quaternion.mul(Axis.XN.rotationDegrees((float) xRot));
        quaternion.mul(Axis.YP.rotationDegrees((float) yRot));
        quaternion.mul(Axis.ZP.rotationDegrees((float) zRot));
        guiGraphics.pose().mulPose(quaternion);
        Vector3f light0 = new Vector3f(1.0F, -1.0F, -1.0F).normalize();
        Vector3f light1 = new Vector3f(-1.0F, 1.0F, 1.0F).normalize();
        RenderSystem.setShaderLights(light0, light1);
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conjugate();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, guiGraphics.pose(), bufferSource, 240));
        entityrenderdispatcher.setRenderShadow(true);
        entity.setYRot(0.0F);
        entity.setXRot(0.0F);
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).yBodyRot = 0.0F;
            ((LivingEntity) entity).yHeadRotO = 0.0F;
            ((LivingEntity) entity).yHeadRot = 0.0F;
        }
        guiGraphics.flush();
        entityrenderdispatcher.setRenderShadow(true);
        guiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    @Override
    protected void init() {
        super.init();
        this.playBookOpeningSound();
        this.addNextPreviousButtons();
        this.addLinkButtons();
    }

    private void addNextPreviousButtons() {
        int k = (this.f_96543_ - this.xSize) / 2;
        int l = (this.f_96544_ - this.ySize + 128) / 2;
        this.buttonPreviousPage = (BookPageButton) this.m_142416_(new BookPageButton(this, k + 10, l + 180, false, p_214208_1_ -> this.onSwitchPage(false), true));
        this.buttonNextPage = (BookPageButton) this.m_142416_(new BookPageButton(this, k + 365, l + 180, true, p_214205_1_ -> this.onSwitchPage(true), true));
    }

    private void addLinkButtons() {
        this.f_169369_.clear();
        this.m_169413_();
        this.addNextPreviousButtons();
        int k = (this.f_96543_ - this.xSize) / 2;
        int l = (this.f_96544_ - this.ySize + 128) / 2;
        for (LinkData linkData : this.links) {
            if (linkData.getPage() == this.currentPageCounter) {
                int maxLength = Math.max(100, Minecraft.getInstance().font.width(linkData.getTitleText()) + 20);
                this.yIndexesToSkip.add(new Whitespace(linkData.getPage(), linkData.getX() - maxLength / 2, linkData.getY(), 100, 20));
                this.m_142416_(new LinkButton(this, k + linkData.getX() - maxLength / 2, l + linkData.getY(), maxLength, 20, Component.translatable(linkData.getTitleText()), linkData.getDisplayItem(), p_213021_1_ -> {
                    this.prevPageJSON = this.currentPageJSON;
                    this.currentPageJSON = new ResourceLocation(this.getTextFileDirectory() + linkData.getLinkedPage());
                    this.preservedPageIndex = this.currentPageCounter;
                    this.currentPageCounter = 0;
                    this.addNextPreviousButtons();
                }));
            }
            if (linkData.getPage() > this.maxPagesFromPrinting) {
                this.maxPagesFromPrinting = linkData.getPage();
            }
        }
        for (EntityLinkData linkData : this.entityLinks) {
            if (linkData.getPage() == this.currentPageCounter) {
                this.yIndexesToSkip.add(new Whitespace(linkData.getPage(), linkData.getX() - 12, linkData.getY(), 100, 20));
                this.m_142416_(new EntityLinkButton(this, linkData, k, l, p_213021_1_ -> {
                    this.prevPageJSON = this.currentPageJSON;
                    this.currentPageJSON = new ResourceLocation(this.getTextFileDirectory() + linkData.getLinkedPage());
                    this.preservedPageIndex = this.currentPageCounter;
                    this.currentPageCounter = 0;
                    this.addNextPreviousButtons();
                }));
            }
            if (linkData.getPage() > this.maxPagesFromPrinting) {
                this.maxPagesFromPrinting = linkData.getPage();
            }
        }
    }

    private void onSwitchPage(boolean next) {
        if (next) {
            if (this.currentPageCounter < this.maxPagesFromPrinting) {
                this.currentPageCounter++;
            }
        } else if (this.currentPageCounter > 0) {
            this.currentPageCounter--;
        } else if (this.internalPage != null && !this.internalPage.getParent().isEmpty()) {
            this.prevPageJSON = this.currentPageJSON;
            this.currentPageJSON = new ResourceLocation(this.getTextFileDirectory() + this.internalPage.getParent());
            this.currentPageCounter = this.preservedPageIndex;
            this.preservedPageIndex = 0;
        }
        this.refreshSpacing();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
        this.mouseX = x;
        this.mouseY = y;
        int bindingColor = this.getBindingColor();
        int bindingR = bindingColor >> 16 & 0xFF;
        int bindingG = bindingColor >> 8 & 0xFF;
        int bindingB = bindingColor & 0xFF;
        this.m_280273_(guiGraphics);
        int k = (this.f_96543_ - this.xSize) / 2;
        int l = (this.f_96544_ - this.ySize + 128) / 2;
        BookBlit.blitWithColor(guiGraphics, this.getBookBindingTexture(), k, l, 0.0F, 0.0F, this.xSize, this.ySize, this.xSize, this.ySize, bindingR, bindingG, bindingB, 255);
        BookBlit.blitWithColor(guiGraphics, this.getBookPageTexture(), k, l, 0.0F, 0.0F, this.xSize, this.ySize, this.xSize, this.ySize, 255, 255, 255, 255);
        if (this.internalPage == null || this.currentPageJSON != this.prevPageJSON || this.prevPageJSON == null) {
            this.internalPage = this.generatePage(this.currentPageJSON);
            if (this.internalPage != null) {
                this.refreshSpacing();
            }
        }
        if (this.internalPage != null) {
            this.writePageText(guiGraphics, x, y);
        }
        super.render(guiGraphics, x, y, partialTicks);
        this.prevPageJSON = this.currentPageJSON;
        if (this.internalPage != null) {
            guiGraphics.pose().pushPose();
            this.renderOtherWidgets(guiGraphics, x, y, this.internalPage);
            guiGraphics.pose().popPose();
        }
        if (this.entityTooltip != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 550.0F);
            guiGraphics.renderTooltip(this.f_96547_, Minecraft.getInstance().font.split(Component.translatable(this.entityTooltip), Math.max(this.f_96543_ / 2 - 43, 170)), x, y);
            this.entityTooltip = null;
            guiGraphics.pose().popPose();
        }
    }

    private void refreshSpacing() {
        if (this.internalPage != null) {
            String lang = Minecraft.getInstance().getLanguageManager().getSelected().toLowerCase();
            this.currentPageText = new ResourceLocation(this.getTextFileDirectory() + lang + "/" + this.internalPage.getTextFileToReadFrom());
            boolean invalid = false;
            try {
                InputStream is = Minecraft.getInstance().getResourceManager().m_215595_(this.currentPageText);
                is.close();
            } catch (Exception var4) {
                invalid = true;
                Citadel.LOGGER.warn("Could not find language file for translation, defaulting to english");
                this.currentPageText = new ResourceLocation(this.getTextFileDirectory() + "en_us/" + this.internalPage.getTextFileToReadFrom());
            }
            this.readInPageWidgets(this.internalPage);
            this.addWidgetSpacing();
            this.addLinkButtons();
            this.readInPageText(this.currentPageText);
        }
    }

    private Item getItemByRegistryName(String registryName) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
    }

    private Recipe getRecipeByName(String registryName) {
        try {
            RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
            if (manager.byKey(new ResourceLocation(registryName)).isPresent()) {
                return (Recipe) manager.byKey(new ResourceLocation(registryName)).get();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        return null;
    }

    private void addWidgetSpacing() {
        this.yIndexesToSkip.clear();
        for (ItemRenderData itemRenderData : this.itemRenders) {
            Item item = this.getItemByRegistryName(itemRenderData.getItem());
            if (item != null) {
                this.yIndexesToSkip.add(new Whitespace(itemRenderData.getPage(), itemRenderData.getX(), itemRenderData.getY(), (int) (itemRenderData.getScale() * 17.0), (int) (itemRenderData.getScale() * 15.0)));
            }
        }
        for (RecipeData recipeData : this.recipes) {
            Recipe recipe = this.getRecipeByName(recipeData.getRecipe());
            if (recipe != null) {
                this.yIndexesToSkip.add(new Whitespace(recipeData.getPage(), recipeData.getX(), recipeData.getY() - (int) (recipeData.getScale() * 15.0), (int) (recipeData.getScale() * 35.0), (int) (recipeData.getScale() * 60.0), true));
            }
        }
        for (ImageData imageData : this.images) {
            if (imageData != null) {
                this.yIndexesToSkip.add(new Whitespace(imageData.getPage(), imageData.getX(), imageData.getY(), (int) (imageData.getScale() * (double) imageData.getWidth()), (int) (imageData.getScale() * (double) imageData.getHeight() * 0.8F)));
            }
        }
        if (!this.writtenTitle.isEmpty()) {
            this.yIndexesToSkip.add(new Whitespace(0, 20, 5, 70, 15));
        }
    }

    private void renderOtherWidgets(GuiGraphics guiGraphics, int x, int y, BookPage page) {
        int color = this.getBindingColor();
        int r = (color & 0xFF0000) >> 16;
        int g = (color & 0xFF00) >> 8;
        int b = color & 0xFF;
        int k = (this.f_96543_ - this.xSize) / 2;
        int l = (this.f_96544_ - this.ySize + 128) / 2;
        for (ImageData imageData : this.images) {
            if (imageData.getPage() == this.currentPageCounter && imageData != null) {
                ResourceLocation tex = (ResourceLocation) this.textureMap.get(imageData.getTexture());
                if (tex == null) {
                    tex = new ResourceLocation(imageData.getTexture());
                    this.textureMap.put(imageData.getTexture(), tex);
                }
                float scale = (float) imageData.getScale();
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float) (k + imageData.getX()), (float) (l + imageData.getY()), 0.0F);
                guiGraphics.pose().scale(scale, scale, scale);
                guiGraphics.blit(tex, 0, 0, imageData.getU(), imageData.getV(), imageData.getWidth(), imageData.getHeight());
                guiGraphics.pose().popPose();
            }
        }
        for (RecipeData recipeData : this.recipes) {
            if (recipeData.getPage() == this.currentPageCounter) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float) (k + recipeData.getX()), (float) (l + recipeData.getY()), 0.0F);
                float scale = (float) recipeData.getScale();
                guiGraphics.pose().scale(scale, scale, scale);
                guiGraphics.blit(this.getBookWidgetTexture(), 0, 0, 0, 88, 116, 53);
                guiGraphics.pose().popPose();
            }
        }
        for (TabulaRenderData tabulaRenderData : this.tabulaRenders) {
            if (tabulaRenderData.getPage() == this.currentPageCounter) {
                TabulaModel model = null;
                ResourceLocation texture;
                if (this.textureMap.get(tabulaRenderData.getTexture()) != null) {
                    texture = (ResourceLocation) this.textureMap.get(tabulaRenderData.getTexture());
                } else {
                    texture = (ResourceLocation) this.textureMap.put(tabulaRenderData.getTexture(), new ResourceLocation(tabulaRenderData.getTexture()));
                }
                if (this.renderedTabulaModels.get(tabulaRenderData.getModel()) != null) {
                    model = (TabulaModel) this.renderedTabulaModels.get(tabulaRenderData.getModel());
                } else {
                    try {
                        model = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/" + tabulaRenderData.getModel().split(":")[0] + "/" + tabulaRenderData.getModel().split(":")[1]));
                    } catch (Exception var20) {
                        Citadel.LOGGER.warn("Could not load in tabula model for book at " + tabulaRenderData.getModel());
                    }
                    this.renderedTabulaModels.put(tabulaRenderData.getModel(), model);
                }
                if (model != null && texture != null) {
                    float scale = (float) tabulaRenderData.getScale();
                    drawTabulaModelOnScreen(guiGraphics, model, texture, k + tabulaRenderData.getX(), l + tabulaRenderData.getY(), 30.0F * scale, tabulaRenderData.isFollow_cursor(), tabulaRenderData.getRot_x(), tabulaRenderData.getRot_y(), tabulaRenderData.getRot_z(), (float) this.mouseX, (float) this.mouseY);
                }
            }
        }
        for (EntityRenderData data : this.entityRenders) {
            if (data.getPage() == this.currentPageCounter) {
                Entity modelx = null;
                EntityType type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(data.getEntity()));
                if (type != null) {
                    modelx = (Entity) this.renderedEntites.putIfAbsent(data.getEntity(), type.create(Minecraft.getInstance().level));
                }
                if (modelx != null) {
                    float scale = (float) data.getScale();
                    modelx.tickCount = Minecraft.getInstance().player.f_19797_;
                    if (data.getEntityData() != null) {
                        try {
                            CompoundTag tag = TagParser.parseTag(data.getEntityData());
                            modelx.load(tag);
                        } catch (CommandSyntaxException var19) {
                            var19.printStackTrace();
                        }
                    }
                    this.drawEntityOnScreen(guiGraphics, guiGraphics.bufferSource(), k + data.getX(), l + data.getY(), 1050.0F, 30.0F * scale, data.isFollow_cursor(), data.getRot_x(), data.getRot_y(), data.getRot_z(), (float) this.mouseX, (float) this.mouseY, modelx);
                }
            }
        }
        for (RecipeData recipeDatax : this.recipes) {
            if (recipeDatax.getPage() == this.currentPageCounter) {
                Recipe recipe = this.getRecipeByName(recipeDatax.getRecipe());
                if (recipe != null) {
                    this.renderRecipe(guiGraphics, recipe, recipeDatax, k, l);
                }
            }
        }
        for (ItemRenderData itemRenderData : this.itemRenders) {
            if (itemRenderData.getPage() == this.currentPageCounter) {
                Item item = this.getItemByRegistryName(itemRenderData.getItem());
                if (item != null) {
                    float scale = (float) itemRenderData.getScale();
                    ItemStack stack = new ItemStack(item);
                    if (itemRenderData.getItemTag() != null && !itemRenderData.getItemTag().isEmpty()) {
                        CompoundTag tag = null;
                        try {
                            tag = TagParser.parseTag(itemRenderData.getItemTag());
                        } catch (CommandSyntaxException var18) {
                            var18.printStackTrace();
                        }
                        stack.setTag(tag);
                    }
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate((float) k, (float) l, 0.0F);
                    guiGraphics.pose().scale(scale, scale, scale);
                    guiGraphics.renderItem(stack, itemRenderData.getX(), itemRenderData.getY());
                    guiGraphics.pose().popPose();
                }
            }
        }
    }

    protected void renderRecipe(GuiGraphics guiGraphics, Recipe recipe, RecipeData recipeData, int k, int l) {
        int playerTicks = Minecraft.getInstance().player.f_19797_;
        float scale = (float) recipeData.getScale();
        NonNullList<Ingredient> ingredients = recipe instanceof SpecialRecipeInGuideBook ? ((SpecialRecipeInGuideBook) recipe).getDisplayIngredients() : recipe.getIngredients();
        NonNullList<ItemStack> displayedStacks = NonNullList.create();
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ing = ingredients.get(i);
            ItemStack stack = ItemStack.EMPTY;
            if (!ing.isEmpty()) {
                if (ing.getItems().length > 1) {
                    int currentIndex = (int) ((float) playerTicks / 20.0F % (float) ing.getItems().length);
                    stack = ing.getItems()[currentIndex];
                } else {
                    stack = ing.getItems()[0];
                }
            }
            if (!stack.isEmpty()) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float) k, (float) l, 32.0F);
                guiGraphics.pose().translate((float) ((int) ((float) recipeData.getX() + (float) (i % 3 * 20) * scale)), (float) ((int) ((float) recipeData.getY() + (float) (i / 3 * 20) * scale)), 0.0F);
                guiGraphics.pose().scale(scale, scale, scale);
                guiGraphics.renderItem(stack, 0, 0);
                guiGraphics.pose().popPose();
            }
            displayedStacks.add(i, stack);
        }
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) k, (float) l, 32.0F);
        float finScale = scale * 1.5F;
        guiGraphics.pose().translate((float) recipeData.getX() + 70.0F * finScale, (float) recipeData.getY() + 10.0F * finScale, 0.0F);
        guiGraphics.pose().scale(finScale, finScale, finScale);
        ItemStack result = recipe.getResultItem(Minecraft.getInstance().level.m_9598_());
        if (recipe instanceof SpecialRecipeInGuideBook) {
            result = ((SpecialRecipeInGuideBook) recipe).getDisplayResultFor(displayedStacks);
        }
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
        guiGraphics.renderItem(result, 0, 0);
        guiGraphics.pose().popPose();
    }

    protected void writePageText(GuiGraphics guiGraphics, int x, int y) {
        Font font = this.f_96547_;
        int k = (this.f_96543_ - this.xSize) / 2;
        int l = (this.f_96544_ - this.ySize + 128) / 2;
        for (LineData line : this.lines) {
            if (line.getPage() == this.currentPageCounter) {
                guiGraphics.drawString(font, line.getText(), k + 10 + line.getxIndex(), l + 10 + line.getyIndex() * 12, this.getTextColor(), false);
            }
        }
        if (this.currentPageCounter == 0 && !this.writtenTitle.isEmpty()) {
            String actualTitle = I18n.get(this.writtenTitle);
            guiGraphics.pose().pushPose();
            float scale = 2.0F;
            if (font.width(actualTitle) > 80) {
                scale = 2.0F - Mth.clamp((float) (font.width(actualTitle) - 80) * 0.011F, 0.0F, 1.95F);
            }
            guiGraphics.pose().translate((float) (k + 10), (float) (l + 10), 0.0F);
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.drawString(font, actualTitle, 0, 0, this.getTitleColor(), false);
            guiGraphics.pose().popPose();
        }
        this.buttonNextPage.f_93624_ = this.currentPageCounter < this.maxPagesFromPrinting;
        this.buttonPreviousPage.f_93624_ = this.currentPageCounter > 0 || !this.currentPageJSON.equals(this.getRootPage());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected void playBookOpeningSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
    }

    protected void playBookClosingSound() {
    }

    protected abstract int getBindingColor();

    protected int getWidgetColor() {
        return this.getBindingColor();
    }

    protected int getTextColor() {
        return 3158064;
    }

    protected int getTitleColor() {
        return 12233880;
    }

    public abstract ResourceLocation getRootPage();

    public abstract String getTextFileDirectory();

    protected ResourceLocation getBookPageTexture() {
        return BOOK_PAGE_TEXTURE;
    }

    protected ResourceLocation getBookBindingTexture() {
        return BOOK_BINDING_TEXTURE;
    }

    protected ResourceLocation getBookWidgetTexture() {
        return BOOK_WIDGET_TEXTURE;
    }

    protected void playPageFlipSound() {
    }

    @Nullable
    protected BookPage generatePage(ResourceLocation res) {
        Optional<Resource> resource = null;
        BookPage page = null;
        try {
            resource = Minecraft.getInstance().getResourceManager().m_213713_(res);
            try {
                resource = Minecraft.getInstance().getResourceManager().m_213713_(res);
                if (resource.isPresent()) {
                    BufferedReader inputstream = ((Resource) resource.get()).openAsReader();
                    page = BookPage.deserialize(inputstream);
                }
            } catch (IOException var5) {
                var5.printStackTrace();
            }
            return page;
        } catch (Exception var6) {
            return null;
        }
    }

    protected void readInPageWidgets(BookPage page) {
        this.links.clear();
        this.itemRenders.clear();
        this.recipes.clear();
        this.tabulaRenders.clear();
        this.entityRenders.clear();
        this.images.clear();
        this.entityLinks.clear();
        this.links.addAll(page.getLinkedButtons());
        this.entityLinks.addAll(page.getLinkedEntities());
        this.itemRenders.addAll(page.getItemRenders());
        this.recipes.addAll(page.getRecipes());
        this.tabulaRenders.addAll(page.getTabulaRenders());
        this.entityRenders.addAll(page.getEntityRenders());
        this.images.addAll(page.getImages());
        this.writtenTitle = page.generateTitle();
    }

    protected void readInPageText(ResourceLocation res) {
        Resource resource = null;
        int xIndex = 0;
        int actualTextX = 0;
        int yIndex = 0;
        try {
            BufferedReader bufferedreader = Minecraft.getInstance().getResourceManager().m_215597_(res);
            try {
                List<String> readStrings = IOUtils.readLines(bufferedreader);
                this.linesFromJSON = readStrings.size();
                this.lines.clear();
                List<String> splitBySpaces = new ArrayList();
                for (String line : readStrings) {
                    splitBySpaces.addAll(Arrays.asList(line.split(" ")));
                }
                String lineToPrint = "";
                this.linesFromPrinting = 0;
                int page = 0;
                for (int i = 0; i < splitBySpaces.size(); i++) {
                    String word = (String) splitBySpaces.get(i);
                    int cutoffPoint = xIndex > 100 ? 30 : 35;
                    boolean newline = word.equals("<NEWLINE>");
                    for (Whitespace indexes : this.yIndexesToSkip) {
                        int indexPage = indexes.getPage();
                        if (indexPage == page) {
                            int buttonX = indexes.getX();
                            int buttonY = indexes.getY();
                            int width = indexes.getWidth();
                            int height = indexes.getHeight();
                            if (indexes.isDown()) {
                                if ((float) yIndex >= (float) buttonY / 12.0F && (float) yIndex <= (float) (buttonY + height) / 12.0F && (buttonX < 90 && xIndex < 90 || buttonX >= 90 && xIndex >= 90)) {
                                    yIndex += 2;
                                }
                            } else if ((float) yIndex >= (float) (buttonY - height) / 12.0F && (float) yIndex <= (float) (buttonY + height) / 12.0F && (buttonX < 90 && xIndex < 90 || buttonX >= 90 && xIndex >= 90)) {
                                yIndex++;
                            }
                        }
                    }
                    boolean last = i == splitBySpaces.size() - 1;
                    actualTextX += word.length() + 1;
                    if (lineToPrint.length() + word.length() + 1 < cutoffPoint && !newline) {
                        lineToPrint = lineToPrint + " " + word;
                        if (last) {
                            this.linesFromPrinting++;
                            this.lines.add(new LineData(xIndex, yIndex, lineToPrint, page));
                            yIndex++;
                            actualTextX = 0;
                            if (newline) {
                                yIndex++;
                            }
                        }
                    } else {
                        this.linesFromPrinting++;
                        if (yIndex > 13) {
                            if (xIndex > 0) {
                                page++;
                                xIndex = 0;
                                yIndex = 0;
                            } else {
                                xIndex = 200;
                                yIndex = 0;
                            }
                        }
                        if (last) {
                            lineToPrint = lineToPrint + " " + word;
                        }
                        this.lines.add(new LineData(xIndex, yIndex, lineToPrint, page));
                        yIndex++;
                        actualTextX = 0;
                        if (newline) {
                            yIndex++;
                        }
                        ???;
                    }
                }
                this.maxPagesFromPrinting = page;
            } catch (Exception var22) {
                var22.printStackTrace();
            }
        } catch (Exception var23) {
            Citadel.LOGGER.warn("Could not load in page .txt from json from page, page: " + res);
        }
    }

    public void setEntityTooltip(String hoverText) {
        this.entityTooltip = hoverText;
    }

    public ResourceLocation getBookButtonsTexture() {
        return BOOK_BUTTONS_TEXTURE;
    }
}