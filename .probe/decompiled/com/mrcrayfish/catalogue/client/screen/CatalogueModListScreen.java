package com.mrcrayfish.catalogue.client.screen;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mrcrayfish.catalogue.Constants;
import com.mrcrayfish.catalogue.client.ClientHelper;
import com.mrcrayfish.catalogue.client.IModData;
import com.mrcrayfish.catalogue.client.screen.widget.CatalogueCheckBoxButton;
import com.mrcrayfish.catalogue.client.screen.widget.CatalogueIconButton;
import com.mrcrayfish.catalogue.platform.ClientServices;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Matrix4f;

public class CatalogueModListScreen extends Screen {

    private static final Comparator<CatalogueModListScreen.ModListEntry> SORT = Comparator.comparing(o -> o.getData().getDisplayName());

    private static final ResourceLocation MISSING_BANNER = new ResourceLocation("catalogue", "textures/gui/missing_banner.png");

    private static final ResourceLocation VERSION_CHECK_ICONS = new ResourceLocation("forge", "textures/gui/version_check_icons.png");

    private static final Map<String, Pair<ResourceLocation, CatalogueModListScreen.Dimension>> BANNER_CACHE = new HashMap();

    private static final Map<String, Pair<ResourceLocation, CatalogueModListScreen.Dimension>> IMAGE_ICON_CACHE = new HashMap();

    private static final Map<String, Item> ITEM_ICON_CACHE = new HashMap();

    private static final Map<String, IModData> CACHED_MODS = new HashMap();

    private static ResourceLocation cachedBackground;

    private static boolean loaded = false;

    private final Screen parentScreen;

    private EditBox searchTextField;

    private CatalogueModListScreen.ModList modList;

    private IModData selectedModData;

    private Button modFolderButton;

    private Button configButton;

    private Button websiteButton;

    private Button issueButton;

    private Checkbox updatesButton;

    private CatalogueModListScreen.StringList descriptionList;

    private int tooltipYOffset;

    private List<? extends FormattedCharSequence> activeTooltip;

    public CatalogueModListScreen(Screen parent) {
        super(CommonComponents.EMPTY);
        this.parentScreen = parent;
        if (!loaded) {
            ClientServices.PLATFORM.getAllModData().forEach(data -> CACHED_MODS.put(data.getModId(), data));
            loaded = true;
        }
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parentScreen);
    }

    @Override
    protected void init() {
        super.init();
        this.searchTextField = new EditBox(this.f_96547_, 11, 25, 148, 20, CommonComponents.EMPTY);
        this.searchTextField.setResponder(s -> {
            this.updateSearchField(s);
            this.modList.filterAndUpdateList(s);
            this.updateSelectedModList();
        });
        this.m_7787_(this.searchTextField);
        this.modList = new CatalogueModListScreen.ModList();
        this.modList.m_93507_(10);
        this.modList.m_93496_(false);
        this.m_7787_(this.modList);
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, btn -> this.f_96541_.setScreen(null)).pos(10, this.modList.getBottom() + 8).size(127, 20).build());
        this.modFolderButton = (Button) this.m_142416_(new CatalogueIconButton(140, this.modList.getBottom() + 8, 0, 0, onPress -> Util.getPlatform().openFile(ClientServices.PLATFORM.getModDirectory())));
        int padding = 10;
        int contentLeft = this.modList.getRight() + 12 + padding;
        int contentWidth = this.f_96543_ - contentLeft - padding;
        int buttonWidth = (contentWidth - padding) / 3;
        this.configButton = (Button) this.m_142416_(new CatalogueIconButton(contentLeft, 105, 10, 0, buttonWidth, Component.translatable("catalogue.gui.config"), onPress -> {
            if (this.selectedModData != null) {
                this.selectedModData.openConfigScreen(this);
            }
        }));
        this.configButton.f_93624_ = false;
        this.websiteButton = (Button) this.m_142416_(new CatalogueIconButton(contentLeft + buttonWidth + 5, 105, 20, 0, buttonWidth, Component.literal("Website"), onPress -> this.openLink(this.selectedModData.getHomepage())));
        this.websiteButton.f_93624_ = false;
        this.issueButton = (Button) this.m_142416_(new CatalogueIconButton(contentLeft + buttonWidth + buttonWidth + 10, 105, 30, 0, buttonWidth, Component.literal("Submit Bug"), onPress -> this.openLink(this.selectedModData.getIssueTracker())));
        this.issueButton.f_93624_ = false;
        this.descriptionList = new CatalogueModListScreen.StringList(contentWidth, this.f_96544_ - 135 - 55, contentLeft, 130);
        this.descriptionList.m_93496_(false);
        this.descriptionList.m_93488_(false);
        this.m_7787_(this.descriptionList);
        this.updatesButton = (Checkbox) this.m_142416_(new CatalogueCheckBoxButton(this.modList.getRight() - 14, 7, button -> {
            this.modList.filterAndUpdateList(this.searchTextField.getValue());
            this.updateSelectedModList();
        }));
        this.modList.filterAndUpdateList(this.searchTextField.getValue());
        if (this.selectedModData != null) {
            this.setSelectedModData(this.selectedModData);
            this.updateSelectedModList();
            CatalogueModListScreen.ModListEntry entry = this.modList.getEntryFromInfo(this.selectedModData);
            if (entry != null) {
                this.modList.centerScrollOn(entry);
            }
        }
        this.updateSearchField(this.searchTextField.getValue());
    }

    private void openLink(@Nullable String url) {
        if (url != null) {
            Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
            this.m_5561_(style);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.activeTooltip = null;
        this.m_280273_(graphics);
        this.drawModList(graphics, mouseX, mouseY, partialTicks);
        this.drawModInfo(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        Optional<IModData> optional = Optional.ofNullable((IModData) CACHED_MODS.get("catalogue"));
        optional.ifPresent(this::loadAndCacheLogo);
        Pair<ResourceLocation, CatalogueModListScreen.Dimension> pair = (Pair<ResourceLocation, CatalogueModListScreen.Dimension>) BANNER_CACHE.get("catalogue");
        if (pair != null && pair.getLeft() != null) {
            ResourceLocation textureId = (ResourceLocation) pair.getLeft();
            CatalogueModListScreen.Dimension size = (CatalogueModListScreen.Dimension) pair.getRight();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(textureId, 10, 9, 10, 10, 0.0F, 0.0F, size.width, size.height, size.width, size.height);
        }
        if (ClientHelper.isMouseWithin(10, 9, 10, 10, mouseX, mouseY)) {
            this.setActiveTooltip(Component.translatable("catalogue.gui.info"));
            this.tooltipYOffset = 10;
        }
        if (this.modFolderButton.m_5953_((double) mouseX, (double) mouseY)) {
            this.setActiveTooltip(Component.translatable("catalogue.gui.open_mods_folder"));
        }
        if (this.activeTooltip != null) {
            graphics.renderTooltip(this.f_96547_, this.activeTooltip, mouseX, mouseY + this.tooltipYOffset);
            this.tooltipYOffset = 0;
        }
    }

    private void updateSelectedModList() {
        CatalogueModListScreen.ModListEntry selectedEntry = this.modList.getEntryFromInfo(this.selectedModData);
        if (selectedEntry != null) {
            this.modList.m_6987_(selectedEntry);
        }
    }

    private void updateSearchField(String value) {
        if (value.isEmpty()) {
            this.searchTextField.setSuggestion(Component.translatable("catalogue.gui.search").append(Component.literal("...")).getString());
        } else {
            Optional<IModData> optional = CACHED_MODS.values().stream().filter(data -> data.getDisplayName().toLowerCase(Locale.ENGLISH).startsWith(value.toLowerCase(Locale.ENGLISH))).min(Comparator.comparing(IModData::getDisplayName));
            if (optional.isPresent()) {
                int length = value.length();
                String displayName = ((IModData) optional.get()).getDisplayName();
                this.searchTextField.setSuggestion(displayName.substring(length));
            } else {
                this.searchTextField.setSuggestion("");
            }
        }
    }

    private void drawModList(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (ClientServices.PLATFORM.isForge()) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(VERSION_CHECK_ICONS, this.modList.getRight() - 24, 10, 24.0F, 0.0F, 8, 8, 64, 16);
        }
        this.modList.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawString(this.f_96547_, ClientServices.COMPONENT.createTitle().withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.WHITE), 70, 10, 16777215);
        this.searchTextField.m_88315_(graphics, mouseX, mouseY, partialTicks);
        if (ClientHelper.isMouseWithin(this.modList.getRight() - 14, 7, 14, 14, mouseX, mouseY)) {
            this.setActiveTooltip(ClientServices.COMPONENT.createFilterUpdates());
            this.tooltipYOffset = 10;
        }
    }

    private void drawModInfo(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int listRight = this.modList.getRight();
        graphics.vLine(listRight + 11, -1, this.f_96544_, -9408400);
        graphics.fill(listRight + 12, 0, this.f_96543_, this.f_96544_, 1711276032);
        this.descriptionList.render(graphics, mouseX, mouseY, partialTicks);
        int contentLeft = listRight + 12 + 10;
        int contentWidth = this.f_96543_ - contentLeft - 10;
        if (this.selectedModData != null) {
            this.drawBackground(graphics, this.f_96543_ - contentLeft + 10, listRight + 12, 0);
            this.drawBanner(graphics, contentWidth, contentLeft, 10, this.f_96543_ - (listRight + 12 + 10) - 10, 50);
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            poseStack.translate((float) contentLeft, 70.0F, 0.0F);
            poseStack.scale(2.0F, 2.0F, 2.0F);
            graphics.drawString(this.f_96547_, this.selectedModData.getDisplayName(), 0, 0, 16777215);
            poseStack.popPose();
            Component modId = Component.literal("Mod ID: " + this.selectedModData.getModId()).withStyle(ChatFormatting.DARK_GRAY);
            int modIdWidth = this.f_96547_.width(modId);
            graphics.drawString(this.f_96547_, modId, contentLeft + contentWidth - modIdWidth, 92, 16777215);
            this.drawStringWithLabel(graphics, "catalogue.gui.version", this.selectedModData.getVersion().toString(), contentLeft, 92, contentWidth, mouseX, mouseY, ChatFormatting.GRAY, ChatFormatting.WHITE);
            IModData.Update update = this.selectedModData.getUpdate();
            if (ClientServices.PLATFORM.isForge() && update != null && update.url() != null && !update.url().isBlank()) {
                Component version = ClientServices.COMPONENT.createVersion(this.selectedModData.getVersion());
                int versionWidth = this.f_96547_.width(version);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                int vOffset = update.animated() && (System.currentTimeMillis() / 800L & 1L) == 1L ? 8 : 0;
                graphics.blit(VERSION_CHECK_ICONS, contentLeft + versionWidth + 5, 92, (float) (update.texOffset() * 8), (float) vOffset, 8, 8, 64, 16);
                if (ClientHelper.isMouseWithin(contentLeft + versionWidth + 5, 92, 8, 8, mouseX, mouseY)) {
                    Component message = ClientServices.COMPONENT.createFormatted("catalogue.gui.update_available", update.url());
                    this.setActiveTooltip(message);
                }
            }
            int labelOffset = this.f_96544_ - 20;
            String license = this.selectedModData.getLicense();
            this.drawStringWithLabel(graphics, "catalogue.gui.licenses", license, contentLeft, labelOffset, contentWidth, mouseX, mouseY, ChatFormatting.GRAY, ChatFormatting.WHITE);
            labelOffset -= 15;
            String credits = this.selectedModData.getCredits();
            if (credits != null) {
                this.drawStringWithLabel(graphics, ClientServices.COMPONENT.getCreditsKey(), credits, contentLeft, labelOffset, contentWidth, mouseX, mouseY, ChatFormatting.GRAY, ChatFormatting.WHITE);
                labelOffset -= 15;
            }
            String authors = this.selectedModData.getAuthors();
            if (authors != null) {
                this.drawStringWithLabel(graphics, "catalogue.gui.authors", authors, contentLeft, labelOffset, contentWidth, mouseX, mouseY, ChatFormatting.GRAY, ChatFormatting.WHITE);
            }
        } else {
            Component message = Component.translatable("catalogue.gui.no_selection").withStyle(ChatFormatting.GRAY);
            graphics.drawCenteredString(this.f_96547_, message, contentLeft + contentWidth / 2, this.f_96544_ / 2 - 5, 16777215);
        }
    }

    private void drawStringWithLabel(GuiGraphics graphics, String format, String text, int x, int y, int maxWidth, int mouseX, int mouseY, ChatFormatting labelColor, ChatFormatting contentColor) {
        Component formatted = ClientServices.COMPONENT.createFormatted(format, text);
        String rawString = formatted.getString();
        String label = rawString.substring(0, rawString.indexOf(":") + 1);
        String content = rawString.substring(rawString.indexOf(":") + 1);
        if (this.f_96547_.width(formatted) > maxWidth) {
            content = this.f_96547_.plainSubstrByWidth(content, maxWidth - this.f_96547_.width(label) - 7) + "...";
            MutableComponent credits = Component.literal(label).withStyle(labelColor);
            credits.append(Component.literal(content).withStyle(contentColor));
            graphics.drawString(this.f_96547_, credits, x, y, 16777215);
            if (ClientHelper.isMouseWithin(x, y, maxWidth, 9, mouseX, mouseY)) {
                this.setActiveTooltip(Component.literal(text));
            }
        } else {
            graphics.drawString(this.f_96547_, Component.literal(label).withStyle(labelColor).append(Component.literal(content).withStyle(contentColor)), x, y, 16777215);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (ClientHelper.isMouseWithin(10, 9, 10, 10, (int) mouseX, (int) mouseY) && button == 0) {
            this.openLink("https://www.curseforge.com/minecraft/mc-mods/catalogue");
            return true;
        } else {
            if (ClientServices.PLATFORM.isForge() && this.selectedModData != null) {
                int contentLeft = this.modList.getRight() + 12 + 10;
                Component version = ClientServices.COMPONENT.createVersion(this.selectedModData.getVersion());
                int versionWidth = this.f_96547_.width(version);
                if (ClientHelper.isMouseWithin(contentLeft + versionWidth + 5, 92, 8, 8, (int) mouseX, (int) mouseY)) {
                    IModData.Update update = this.selectedModData.getUpdate();
                    if (update != null && update.url() != null && !update.url().isBlank()) {
                        Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, update.url()));
                        this.m_5561_(style);
                    }
                }
            }
            return super.m_6375_(mouseX, mouseY, button);
        }
    }

    private void setActiveTooltip(Component content) {
        this.activeTooltip = this.f_96547_.split(content, Math.min(200, this.f_96543_));
        this.tooltipYOffset = 0;
    }

    private void setSelectedModData(IModData data) {
        this.selectedModData = data;
        this.loadAndCacheLogo(data);
        this.loadAndCacheBackground(data);
        this.configButton.f_93624_ = true;
        this.websiteButton.f_93624_ = true;
        this.issueButton.f_93624_ = true;
        this.configButton.f_93623_ = data.hasConfig();
        this.websiteButton.f_93623_ = data.getHomepage() != null;
        this.issueButton.f_93623_ = data.getIssueTracker() != null;
        int contentLeft = this.modList.getRight() + 12 + 10;
        int contentWidth = this.f_96543_ - contentLeft - 10;
        int labelCount = this.getLabelCount(data);
        this.descriptionList.m_93437_(contentWidth, this.f_96544_ - 135 - 10 - labelCount * 15, 130, this.f_96544_ - 10 - labelCount * 15);
        this.descriptionList.m_93507_(contentLeft);
        this.descriptionList.setTextFromInfo(data);
        this.descriptionList.m_93410_(0.0);
    }

    private int getLabelCount(IModData selectedModData) {
        int count = 1;
        if (selectedModData.getCredits() != null && !selectedModData.getCredits().isBlank()) {
            count++;
        }
        if (selectedModData.getAuthors() != null && !selectedModData.getAuthors().isBlank()) {
            count++;
        }
        return count;
    }

    private void drawBackground(GuiGraphics graphics, int contentWidth, int x, int y) {
        if (this.selectedModData != null) {
            if (cachedBackground != null) {
                RenderSystem.setShader(GameRenderer::m_172814_);
                RenderSystem.setShaderTexture(0, cachedBackground);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.enableBlend();
                Matrix4f matrix = graphics.pose().last().pose();
                BufferBuilder builder = Tesselator.getInstance().getBuilder();
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
                builder.m_252986_(matrix, (float) x, (float) y, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 0.0F).endVertex();
                builder.m_252986_(matrix, (float) x, (float) (y + 128), 0.0F).color(0.0F, 0.0F, 0.0F, 0.0F).uv(0.0F, 1.0F).endVertex();
                builder.m_252986_(matrix, (float) (x + contentWidth), (float) (y + 128), 0.0F).color(0.0F, 0.0F, 0.0F, 0.0F).uv(1.0F, 1.0F).endVertex();
                builder.m_252986_(matrix, (float) (x + contentWidth), (float) y, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 0.0F).endVertex();
                BufferUploader.drawWithShader(builder.end());
                RenderSystem.disableBlend();
            }
        }
    }

    private void drawBanner(GuiGraphics graphics, int contentWidth, int x, int y, int maxWidth, int maxHeight) {
        if (this.selectedModData != null) {
            ResourceLocation logoResource = MISSING_BANNER;
            CatalogueModListScreen.Dimension size = new CatalogueModListScreen.Dimension(600, 120);
            if (BANNER_CACHE.containsKey(this.selectedModData.getModId())) {
                Pair<ResourceLocation, CatalogueModListScreen.Dimension> logoInfo = (Pair<ResourceLocation, CatalogueModListScreen.Dimension>) BANNER_CACHE.get(this.selectedModData.getModId());
                if (logoInfo.getLeft() != null) {
                    logoResource = (ResourceLocation) logoInfo.getLeft();
                    size = (CatalogueModListScreen.Dimension) logoInfo.getRight();
                }
            }
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            int width = size.width;
            int height = size.height;
            if (size.width > maxWidth) {
                width = maxWidth;
                height = maxWidth * size.height / size.width;
            }
            if (height > maxHeight) {
                height = maxHeight;
                width = maxHeight * size.width / size.height;
            }
            x += (contentWidth - width) / 2;
            y += (maxHeight - height) / 2;
            graphics.blit(logoResource, x, y, width, height, 0.0F, 0.0F, size.width, size.height, size.width, size.height);
            RenderSystem.disableBlend();
        }
    }

    private void loadAndCacheLogo(IModData data) {
        if (!BANNER_CACHE.containsKey(data.getModId())) {
            BANNER_CACHE.put(data.getModId(), Pair.of(null, new CatalogueModListScreen.Dimension(0, 0)));
            String banner = data.getBanner();
            if (banner != null && !banner.isEmpty()) {
                if (ClientServices.PLATFORM.isForge() && (banner.contains("/") || banner.contains("\\"))) {
                    Constants.LOG.warn("Skipped loading logo file from {}. The file name '{}' contained illegal characters '/' or '\\'", data.getDisplayName(), banner);
                    return;
                }
                ClientServices.PLATFORM.loadNativeImage(data.getModId(), banner, image -> {
                    if (image.getWidth() <= 1200 && image.getHeight() <= 240) {
                        TextureManager textureManager = this.f_96541_.getTextureManager();
                        BANNER_CACHE.put(data.getModId(), Pair.of(textureManager.register("modlogo", this.createLogoTexture(image, data.isLogoSmooth())), new CatalogueModListScreen.Dimension(image.getWidth(), image.getHeight())));
                    } else {
                        Constants.LOG.warn("Failed to load banner image for {} as it exceeds the maximum size of 1200x240px", data.getModId());
                    }
                });
            }
        }
    }

    private void loadAndCacheIcon(IModData data) {
        if (!IMAGE_ICON_CACHE.containsKey(data.getModId())) {
            IMAGE_ICON_CACHE.put(data.getModId(), Pair.of(null, new CatalogueModListScreen.Dimension(0, 0)));
            String imageIcon = data.getImageIcon();
            if (imageIcon != null && !imageIcon.isEmpty()) {
                if (!ClientServices.PLATFORM.isForge() || !imageIcon.contains("/") && !imageIcon.contains("\\")) {
                    ClientServices.PLATFORM.loadNativeImage(data.getModId(), imageIcon, image -> {
                        TextureManager textureManager = this.f_96541_.getTextureManager();
                        IMAGE_ICON_CACHE.put(data.getModId(), Pair.of(textureManager.register("catalogueicon", this.createLogoTexture(image, false)), new CatalogueModListScreen.Dimension(image.getWidth(), image.getHeight())));
                    });
                } else {
                    Constants.LOG.warn("Skipped loading Catalogue icon file from {}. The file name '{}' contained illegal characters '/' or '\\'", data.getDisplayName(), imageIcon);
                }
            } else {
                String logoFile = data.getBanner();
                if (logoFile != null && !logoFile.isEmpty()) {
                    if (ClientServices.PLATFORM.isForge() && (logoFile.contains("/") || logoFile.contains("\\"))) {
                        Constants.LOG.warn("Skipped loading logo file from {}. The file name '{}' contained illegal characters '/' or '\\'", data.getDisplayName(), imageIcon);
                        return;
                    }
                    ClientServices.PLATFORM.loadNativeImage(data.getModId(), imageIcon, image -> {
                        if (image.getWidth() == image.getHeight()) {
                            TextureManager textureManager = this.f_96541_.getTextureManager();
                            String modId = data.getModId();
                            if (BANNER_CACHE.containsKey(modId) && ((Pair) BANNER_CACHE.get(modId)).getLeft() != null) {
                                IMAGE_ICON_CACHE.put(modId, (Pair) BANNER_CACHE.get(modId));
                                return;
                            }
                            DynamicTexture texture = this.createLogoTexture(image, data.isLogoSmooth());
                            CatalogueModListScreen.Dimension size = new CatalogueModListScreen.Dimension(image.getWidth(), image.getHeight());
                            ResourceLocation textureId = textureManager.register("catalogueicon", texture);
                            IMAGE_ICON_CACHE.put(modId, Pair.of(textureId, size));
                            BANNER_CACHE.put(modId, Pair.of(textureId, size));
                        }
                    });
                }
            }
        }
    }

    private void loadAndCacheBackground(IModData data) {
        if (cachedBackground != null) {
            TextureManager textureManager = this.f_96541_.getTextureManager();
            textureManager.release(cachedBackground);
        }
        cachedBackground = null;
        String background = data.getBackground();
        if (background != null && !background.isEmpty()) {
            if (ClientServices.PLATFORM.isForge() && (background.contains("/") || background.contains("\\"))) {
                Constants.LOG.warn("Skipped loading Catalogue background file from {}. The file name '{}' contained illegal characters '/' or '\\'", data.getDisplayName(), background);
                return;
            }
            ClientServices.PLATFORM.loadNativeImage(data.getModId(), background, image -> {
                if (image.getWidth() == 512 && image.getHeight() == 256) {
                    TextureManager textureManager = this.f_96541_.getTextureManager();
                    cachedBackground = textureManager.register("cataloguebackground", this.createLogoTexture(image, false));
                }
            });
        }
    }

    private DynamicTexture createLogoTexture(final NativeImage image, final boolean smooth) {
        return new DynamicTexture(image) {

            @Override
            public void upload() {
                this.m_117966_();
                image.upload(0, 0, 0, 0, 0, image.getWidth(), image.getHeight(), smooth, false, false, false);
            }
        };
    }

    private static record Dimension(int width, int height) {
    }

    private class ModList extends ObjectSelectionList<CatalogueModListScreen.ModListEntry> {

        public ModList() {
            super(CatalogueModListScreen.this.f_96541_, 150, CatalogueModListScreen.this.f_96544_, 46, CatalogueModListScreen.this.f_96544_ - 35, 26);
        }

        @Override
        protected int getScrollbarPosition() {
            return this.f_93393_ + this.f_93388_ - 6;
        }

        @Override
        public int getRowLeft() {
            return this.f_93393_;
        }

        @Override
        public int getRowWidth() {
            return this.f_93388_;
        }

        public void filterAndUpdateList(String text) {
            Predicate<IModData> filter = ClientServices.PLATFORM.isForge() ? data -> !CatalogueModListScreen.this.updatesButton.selected() || data.getUpdate() != null : data -> data.getType() == IModData.Type.DEFAULT || data.getModId().equals("minecraft") || data.getModId().equals("fabric-api") || CatalogueModListScreen.this.updatesButton.selected();
            List<CatalogueModListScreen.ModListEntry> entries = (List<CatalogueModListScreen.ModListEntry>) CatalogueModListScreen.CACHED_MODS.values().stream().filter(info -> info.getDisplayName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase(Locale.ENGLISH))).filter(filter).map(info -> CatalogueModListScreen.this.new ModListEntry(info, this)).sorted(CatalogueModListScreen.SORT).collect(Collectors.toList());
            this.m_5988_(entries);
            this.m_93410_(0.0);
        }

        @Nullable
        public CatalogueModListScreen.ModListEntry getEntryFromInfo(IModData data) {
            return (CatalogueModListScreen.ModListEntry) this.m_6702_().stream().filter(entry -> entry.data == data).findFirst().orElse(null);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.enableScissor(this.getRowLeft(), this.getTop(), this.getRowLeft() + this.getWidth(), this.getBottom());
            super.m_88315_(graphics, mouseX, mouseY, partialTicks);
            graphics.disableScissor();
        }

        @Override
        public boolean keyPressed(int key, int scanCode, int modifiers) {
            if (key == 257 && this.m_93511_() != null) {
                CatalogueModListScreen.this.setSelectedModData(((CatalogueModListScreen.ModListEntry) this.m_93511_()).data);
                SoundManager handler = Minecraft.getInstance().getSoundManager();
                handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            } else {
                return super.m_7933_(key, scanCode, modifiers);
            }
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput0) {
        }

        public void centerScrollOn(CatalogueModListScreen.ModListEntry entry) {
            super.m_93494_(entry);
        }

        public int getLeft() {
            return this.f_93393_;
        }

        public int getRight() {
            return this.f_93392_;
        }

        public int getTop() {
            return this.f_93390_;
        }

        public int getBottom() {
            return this.f_93391_;
        }

        public int getWidth() {
            return this.f_93388_;
        }
    }

    private class ModListEntry extends ObjectSelectionList.Entry<CatalogueModListScreen.ModListEntry> {

        private final IModData data;

        private final CatalogueModListScreen.ModList list;

        private final ItemStack icon;

        public ModListEntry(IModData data, CatalogueModListScreen.ModList list) {
            this.data = data;
            this.list = list;
            this.icon = new ItemStack(this.getItemIcon());
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            graphics.drawString(CatalogueModListScreen.this.f_96547_, this.getFormattedModName(), left + 24, top + 2, 16777215);
            graphics.drawString(CatalogueModListScreen.this.f_96547_, Component.literal(this.data.getVersion().toString()).withStyle(ChatFormatting.GRAY), left + 24, top + 12, 16777215);
            CatalogueModListScreen.this.loadAndCacheIcon(this.data);
            if (CatalogueModListScreen.IMAGE_ICON_CACHE.containsKey(this.data.getModId()) && ((Pair) CatalogueModListScreen.IMAGE_ICON_CACHE.get(this.data.getModId())).getLeft() != null) {
                ResourceLocation logoResource = TextureManager.INTENTIONAL_MISSING_TEXTURE;
                CatalogueModListScreen.Dimension size = new CatalogueModListScreen.Dimension(16, 16);
                Pair<ResourceLocation, CatalogueModListScreen.Dimension> logoInfo = (Pair<ResourceLocation, CatalogueModListScreen.Dimension>) CatalogueModListScreen.IMAGE_ICON_CACHE.get(this.data.getModId());
                if (logoInfo != null && logoInfo.getLeft() != null) {
                    logoResource = (ResourceLocation) logoInfo.getLeft();
                    size = (CatalogueModListScreen.Dimension) logoInfo.getRight();
                }
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.enableBlend();
                graphics.blit(logoResource, left + 4, top + 2, 16, 16, 0.0F, 0.0F, size.width, size.height, size.width, size.height);
                RenderSystem.disableBlend();
            } else {
                try {
                    graphics.renderItem(this.icon, left + 4, top + 2);
                } catch (Exception var14) {
                    CatalogueModListScreen.ITEM_ICON_CACHE.put(this.data.getModId(), Items.GRASS_BLOCK);
                }
            }
            IModData.Update update = this.data.getUpdate();
            if (ClientServices.PLATFORM.isForge() && update != null) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                int vOffset = update.animated() && (System.currentTimeMillis() / 800L & 1L) == 1L ? 8 : 0;
                graphics.blit(CatalogueModListScreen.VERSION_CHECK_ICONS, left + rowWidth - 8 - 10, top + 6, (float) (update.texOffset() * 8), (float) vOffset, 8, 8, 64, 16);
            }
        }

        private Item getItemIcon() {
            if (CatalogueModListScreen.ITEM_ICON_CACHE.containsKey(this.data.getModId())) {
                return (Item) CatalogueModListScreen.ITEM_ICON_CACHE.get(this.data.getModId());
            } else {
                CatalogueModListScreen.ITEM_ICON_CACHE.put(this.data.getModId(), Items.GRASS_BLOCK);
                if (this.data.getModId().equals("forge")) {
                    Item item = Items.ANVIL;
                    CatalogueModListScreen.ITEM_ICON_CACHE.put("forge", item);
                    return item;
                } else {
                    String itemIcon = this.data.getItemIcon();
                    if (itemIcon != null && !itemIcon.isEmpty()) {
                        ResourceLocation resource = ResourceLocation.tryParse(itemIcon);
                        if (resource != null) {
                            Item item = BuiltInRegistries.ITEM.get(resource);
                            if (item != null && item != Items.AIR) {
                                CatalogueModListScreen.ITEM_ICON_CACHE.put(this.data.getModId(), item);
                                return item;
                            }
                        }
                    }
                    Optional<Item> optional = BuiltInRegistries.ITEM.m_123024_().filter(itemx -> itemx.builtInRegistryHolder().key().location().getNamespace().equals(this.data.getModId())).findFirst();
                    if (optional.isPresent()) {
                        Item item = (Item) optional.get();
                        if (item != Items.AIR) {
                            CatalogueModListScreen.ITEM_ICON_CACHE.put(this.data.getModId(), item);
                            return item;
                        }
                    }
                    return Items.GRASS_BLOCK;
                }
            }
        }

        private Component getFormattedModName() {
            String name = this.data.getDisplayName();
            int width = this.list.getRowWidth() - (this.list.m_93518_() > 0 ? 30 : 24);
            if (CatalogueModListScreen.this.f_96547_.width(name) > width) {
                name = CatalogueModListScreen.this.f_96547_.plainSubstrByWidth(name, width - 10) + "...";
            }
            MutableComponent title = Component.literal(name);
            if (this.data.getModId().equals("forge") || this.data.getModId().equals("minecraft")) {
                title.withStyle(ChatFormatting.DARK_GRAY);
            }
            return title;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            CatalogueModListScreen.this.setSelectedModData(this.data);
            this.list.m_6987_(this);
            return false;
        }

        public IModData getData() {
            return this.data;
        }

        @Override
        public Component getNarration() {
            return Component.literal(this.data.getDisplayName());
        }
    }

    private class StringEntry extends ObjectSelectionList.Entry<CatalogueModListScreen.StringEntry> {

        private final String line;

        public StringEntry(String line) {
            this.line = line;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            graphics.drawString(CatalogueModListScreen.this.f_96547_, this.line, left, top, 16777215);
        }

        @Override
        public Component getNarration() {
            return Component.literal(this.line);
        }
    }

    private class StringList extends AbstractSelectionList<CatalogueModListScreen.StringEntry> {

        public StringList(int width, int height, int left, int top) {
            super(CatalogueModListScreen.this.f_96541_, width, CatalogueModListScreen.this.f_96544_, top, top + height, 10);
            this.m_93507_(left);
        }

        public void setTextFromInfo(IModData data) {
            this.m_93516_();
            CatalogueModListScreen.this.f_96547_.getSplitter().splitLines(data.getDescription().trim(), this.getRowWidth(), Style.EMPTY).forEach(text -> this.m_7085_(CatalogueModListScreen.this.new StringEntry(text.getString().replace("\n", "").replace("\r", "").trim())));
        }

        public void setSelected(@Nullable CatalogueModListScreen.StringEntry entry) {
        }

        @Override
        protected int getScrollbarPosition() {
            return this.f_93393_ + this.f_93388_ - 7;
        }

        @Override
        public int getRowLeft() {
            return this.f_93393_;
        }

        @Override
        public int getRowWidth() {
            return this.f_93388_ - 10;
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.enableScissor(this.f_93393_, this.f_93390_, this.f_93393_ + this.f_93388_, this.f_93391_);
            super.render(graphics, mouseX, mouseY, partialTicks);
            graphics.disableScissor();
        }

        @Override
        public void updateNarration(NarrationElementOutput output) {
        }
    }
}