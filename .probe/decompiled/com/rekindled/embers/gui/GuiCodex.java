package com.rekindled.embers.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.rekindled.embers.EmbersClientEvents;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.render.SneakyBufferSourceWrapper;
import com.rekindled.embers.research.ResearchBase;
import com.rekindled.embers.research.ResearchCategory;
import com.rekindled.embers.research.ResearchManager;
import com.rekindled.embers.research.subtypes.ResearchSwitchCategory;
import com.rekindled.embers.util.Misc;
import com.rekindled.embers.util.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class GuiCodex extends Screen {

    public double mouseX = 0.0;

    public double mouseY = 0.0;

    public double smoothMouseX = 0.0;

    public double smoothMouseY = 0.0;

    public int selectedIndex = -1;

    public int selectedPageIndex = -1;

    public ResearchCategory researchCategory;

    public ResearchBase researchPage;

    public float ticks = 1.0F;

    public boolean showLeftArrow = false;

    public boolean showRightArrow = false;

    public int tooltipX = 0;

    public int tooltipY = 0;

    ItemStack tooltipStack = null;

    public boolean renderTooltip = false;

    public int framesExisted = 0;

    public float[] raise = null;

    public float[] raiseTargets = null;

    public String[] sentences = null;

    LinkedList<ResearchCategory> lastCategories = new LinkedList();

    public boolean nextPageSelected;

    public boolean previousPageSelected;

    public String searchString = "";

    public int searchDelay;

    public ArrayList<ResearchBase> searchResult = new ArrayList();

    public static ResourceLocation INDEX = new ResourceLocation("embers", "textures/gui/codex_index.png");

    public static ResourceLocation PARTS = new ResourceLocation("embers", "textures/gui/codex_parts.png");

    public static GuiCodex instance = new GuiCodex();

    public GuiCodex() {
        super(Component.translatable("gui.embers.codex.title"));
    }

    public void markTooltipForRender(ItemStack stack, int x, int y) {
        this.renderTooltip = true;
        this.tooltipX = x;
        this.tooltipY = y;
        this.tooltipStack = stack;
    }

    public void doRenderTooltip(GuiGraphics graphics) {
        if (this.renderTooltip) {
            graphics.renderTooltip(this.f_96547_, this.tooltipStack, this.tooltipX, this.tooltipY);
            this.renderTooltip = false;
        }
    }

    public void pushLastCategory(ResearchCategory category) {
        ListIterator<ResearchCategory> iterator = this.lastCategories.listIterator();
        boolean clear = false;
        while (iterator.hasNext()) {
            ResearchCategory lastCategory = (ResearchCategory) iterator.next();
            if (lastCategory == category) {
                clear = true;
            }
            if (clear) {
                iterator.remove();
            }
        }
        this.lastCategories.add(category);
    }

    public ResearchCategory popLastCategory() {
        return this.lastCategories.isEmpty() ? null : (ResearchCategory) this.lastCategories.removeLast();
    }

    public ResearchCategory peekLastCategory() {
        return this.lastCategories.isEmpty() ? null : (ResearchCategory) this.lastCategories.getLast();
    }

    public void renderItemStackAt(GuiGraphics graphics, ItemStack stack, int x, int y, int mouseX, int mouseY) {
        if (!stack.isEmpty()) {
            graphics.renderFakeItem(stack, x, y);
            graphics.renderItemDecorations(this.f_96547_, stack, x, y, stack.getCount() != 1 ? Integer.toString(stack.getCount()) : "");
            if (mouseX >= x && mouseY >= y && mouseX < x + 16 && mouseY < y + 16) {
                this.markTooltipForRender(stack, mouseX, mouseY);
            }
        }
    }

    public void renderItemStackMinusTooltipAt(GuiGraphics graphics, ItemStack stack, int x, int y) {
        if (!stack.isEmpty()) {
            graphics.renderFakeItem(stack, x, y);
            RenderSystem.enableBlend();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.researchCategory != null) {
            if (this.researchPage != null) {
                this.researchPage = null;
                this.playSound(EmbersSounds.CODEX_PAGE_CLOSE.get());
                return false;
            } else {
                this.researchCategory = this.popLastCategory();
                this.playSound(this.researchCategory == null ? EmbersSounds.CODEX_CATEGORY_CLOSE.get() : EmbersSounds.CODEX_CATEGORY_SWITCH.get());
                return false;
            }
        } else {
            if (this.researchPage != null && this.researchPage.hasMultiplePages()) {
                if (Minecraft.getInstance().options.keyLeft.matches(keyCode, scanCode)) {
                    this.switchPreviousPage();
                    return false;
                }
                if (Minecraft.getInstance().options.keyRight.matches(keyCode, scanCode)) {
                    this.switchNextPage();
                    return false;
                }
            } else if (this.researchPage == null && keyCode == 259 && !this.searchString.isEmpty()) {
                this.setSearchString(this.searchString.substring(0, this.searchString.length() - 1));
            }
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.researchCategory == null;
    }

    @Override
    public boolean charTyped(char typedChar, int modifiers) {
        if (this.researchPage == null && !Character.isISOControl(typedChar)) {
            this.setSearchString(this.searchString + typedChar);
        }
        return super.m_5534_(typedChar, modifiers);
    }

    private void setSearchString(String string) {
        this.searchString = string;
        this.searchDelay = 20;
    }

    private void switchNextPage() {
        this.researchPage = this.researchPage.getNextPage();
        this.playSound(EmbersSounds.CODEX_PAGE_SWITCH.get());
    }

    private void switchPreviousPage() {
        this.researchPage = this.researchPage.getPreviousPage();
        this.playSound(EmbersSounds.CODEX_PAGE_SWITCH.get());
    }

    public void playSound(SoundEvent sound) {
        this.playSound(sound, 0.75F);
    }

    public void playSound(SoundEvent sound, float pitch) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, pitch, 1.0F));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.selectedIndex != -1 && this.researchCategory == null) {
            ResearchCategory selectedCategory = (ResearchCategory) ResearchManager.researches.get(this.selectedIndex);
            if (selectedCategory.isChecked()) {
                this.researchCategory = selectedCategory;
                this.playSound(EmbersSounds.CODEX_CATEGORY_OPEN.get());
            }
        }
        if (this.selectedPageIndex != -1 && this.researchPage == null) {
            ResearchBase selectedResearchPage = (ResearchBase) this.researchCategory.researches.get(this.selectedPageIndex);
            if (mouseButton == 0) {
                if (selectedResearchPage.onOpen(this)) {
                    this.researchPage = selectedResearchPage;
                    this.playSound(EmbersSounds.CODEX_PAGE_OPEN.get());
                }
            } else if (mouseButton == 1) {
                boolean newChecked = !selectedResearchPage.isChecked();
                selectedResearchPage.check(newChecked);
                if (newChecked) {
                    this.playSound(EmbersSounds.CODEX_CHECK.get());
                } else {
                    this.playSound(EmbersSounds.CODEX_UNCHECK.get());
                }
                if (selectedResearchPage instanceof ResearchSwitchCategory selectedCategory) {
                    for (ResearchBase research : selectedCategory.targetCategory.researches) {
                        ResearchManager.sendCheckmark(research, newChecked);
                    }
                } else {
                    ResearchManager.sendCheckmark(selectedResearchPage, newChecked);
                }
            }
        }
        if (this.researchPage != null && this.researchPage.hasMultiplePages()) {
            if (this.nextPageSelected) {
                this.switchNextPage();
            } else if (this.previousPageSelected) {
                this.switchPreviousPage();
            }
        }
        return false;
    }

    public static void drawText(Font font, GuiGraphics graphics, FormattedCharSequence s, int x, int y, int color) {
        int shadowColor = Misc.intColor(64, 0, 0, 0);
        graphics.drawString(font, s, x - 1, y, shadowColor, false);
        graphics.drawString(font, s, x + 1, y, shadowColor, false);
        graphics.drawString(font, s, x, y - 1, shadowColor, false);
        graphics.drawString(font, s, x, y + 1, shadowColor, false);
        graphics.drawString(font, s, x, y, color, false);
    }

    public static void drawTextGlowing(Font font, GuiGraphics graphics, FormattedCharSequence s, int x, int y) {
        float sine = 0.5F * ((float) Math.sin(Math.toRadians((double) (4.0F * ((float) EmbersClientEvents.ticks + Minecraft.getInstance().getPartialTick())))) + 1.0F);
        int shadowColor = Misc.intColor(64, 0, 0, 0);
        graphics.drawString(font, s, x - 1, y, shadowColor, false);
        graphics.drawString(font, s, x + 1, y, shadowColor, false);
        graphics.drawString(font, s, x, y - 1, shadowColor, false);
        graphics.drawString(font, s, x, y + 1, shadowColor, false);
        int shadowColor2 = Misc.intColor(40, 0, 0, 0);
        graphics.drawString(font, s, x - 2, y, shadowColor2, false);
        graphics.drawString(font, s, x + 2, y, shadowColor2, false);
        graphics.drawString(font, s, x, y - 2, shadowColor2, false);
        graphics.drawString(font, s, x, y + 2, shadowColor2, false);
        graphics.drawString(font, s, x - 1, y + 1, shadowColor2, false);
        graphics.drawString(font, s, x + 1, y - 1, shadowColor2, false);
        graphics.drawString(font, s, x - 1, y - 1, shadowColor2, false);
        graphics.drawString(font, s, x + 1, y + 1, shadowColor2, false);
        graphics.drawString(font, s, x, y, Misc.intColor(255, 64 + (int) (64.0F * sine), 16), false);
    }

    public void drawModalRectGlowing(GuiGraphics graphics, ResourceLocation texture, int x, int y, int textureX, int textureY, int width, int height) {
        float sine = 0.5F * ((float) Math.sin(Math.toRadians((double) (4.0F * ((float) EmbersClientEvents.ticks + Minecraft.getInstance().getPartialTick())))) + 1.0F);
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 0.2509804F);
        graphics.blit(texture, x - 1, y, textureX, textureY, width, height);
        graphics.blit(texture, x + 1, y, textureX, textureY, width, height);
        graphics.blit(texture, x, y - 1, textureX, textureY, width, height);
        graphics.blit(texture, x, y + 1, textureX, textureY, width, height);
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 0.15686275F);
        graphics.blit(texture, x - 2, y, textureX, textureY, width, height);
        graphics.blit(texture, x + 2, y, textureX, textureY, width, height);
        graphics.blit(texture, x, y - 2, textureX, textureY, width, height);
        graphics.blit(texture, x, y + 2, textureX, textureY, width, height);
        graphics.blit(texture, x - 1, y + 1, textureX, textureY, width, height);
        graphics.blit(texture, x + 1, y - 1, textureX, textureY, width, height);
        graphics.blit(texture, x - 1, y - 1, textureX, textureY, width, height);
        graphics.blit(texture, x + 1, y + 1, textureX, textureY, width, height);
        RenderSystem.setShaderColor(1.0F, (64.0F + 64.0F * sine) / 255.0F, 0.0627451F, 1.0F);
        graphics.blit(texture, x, y, textureX, textureY, width, height);
    }

    public static void drawTextGlowingAura(Font font, GuiGraphics graphics, FormattedCharSequence s, int x, int y) {
        float sine = 0.5F * ((float) Math.sin(Math.toRadians((double) (4.0F * ((float) EmbersClientEvents.ticks + Minecraft.getInstance().getPartialTick())))) + 1.0F);
        Matrix4f matrix = graphics.pose().last().pose();
        MultiBufferSource buffer = new SneakyBufferSourceWrapper(graphics.bufferSource());
        int shadowColor = Misc.intColor(40, 255, 64 + (int) (64.0F * sine), 16);
        font.drawInBatch(s, (float) (x - 1), (float) y, shadowColor, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) (x - 1), (float) y, shadowColor, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) (x + 1), (float) y, shadowColor, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) x, (float) (y - 1), shadowColor, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) x, (float) (y + 1), shadowColor, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        int shadowColor2 = Misc.intColor(40, 127, 32 + (int) (32.0F * sine), 8);
        font.drawInBatch(s, (float) (x - 2), (float) y, shadowColor2, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) (x + 2), (float) y, shadowColor2, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) x, (float) (y - 2), shadowColor2, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) x, (float) (y + 2), shadowColor2, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) (x - 1), (float) (y + 1), shadowColor2, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) (x + 1), (float) (y - 1), shadowColor2, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) (x - 1), (float) (y - 1), shadowColor2, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) (x + 1), (float) (y + 1), shadowColor2, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(s, (float) x, (float) y, Misc.intColor(255, 64 + (int) (64.0F * sine), 16), false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
    }

    public static void drawCenteredText(Font font, GuiGraphics graphics, FormattedCharSequence s, int x, int y, int color) {
        drawText(font, graphics, s, x - font.width(s) / 2, y, color);
    }

    public static void drawCenteredTextGlowing(Font font, GuiGraphics graphics, FormattedCharSequence s, int x, int y) {
        drawTextGlowing(font, graphics, s, x - font.width(s) / 2, y);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        boolean showTooltips = true;
        boolean showSearchString = this.searchDelay >= 0 && !this.searchString.isEmpty();
        boolean doUpdateSynced = this.ticks > partialTicks;
        this.ticks = partialTicks;
        int numResearches = ResearchManager.researches.size();
        if (this.raise == null) {
            this.raise = new float[numResearches];
            for (int i = 0; i < this.raise.length; i++) {
                this.raise[i] = 0.0F;
            }
        }
        if (this.raiseTargets == null) {
            this.raiseTargets = new float[numResearches];
            for (int i = 0; i < this.raiseTargets.length; i++) {
                this.raiseTargets[i] = 0.0F;
            }
        }
        this.m_280273_(graphics);
        RenderSystem.enableBlend();
        int basePosX = (int) ((float) this.f_96543_ / 2.0F) - 96;
        int basePosY = (int) ((float) this.f_96544_ / 2.0F) - 128;
        this.mouseX = (double) mouseX;
        this.mouseY = (double) mouseY;
        int lastSelectedIndex = this.selectedIndex;
        this.selectedIndex = -1;
        this.selectedPageIndex = -1;
        if (this.researchCategory == null) {
            graphics.blit(INDEX, basePosX, basePosY, 0, 0, 192, 256);
            graphics.blit(PARTS, basePosX - 16, basePosY - 16, 0, 0, 48, 48);
            graphics.blit(PARTS, basePosX + 160, basePosY - 16, 48, 0, 48, 48);
            graphics.blit(PARTS, basePosX + 160, basePosY + 224, 96, 0, 48, 48);
            graphics.blit(PARTS, basePosX - 16, basePosY + 224, 144, 0, 48, 48);
            graphics.blit(PARTS, basePosX + 72, basePosY - 16, 0, 48, 48, 48);
            graphics.blit(PARTS, basePosX + 72, basePosY + 224, 0, 48, 48, 48);
            graphics.blit(PARTS, basePosX - 16, basePosY + 64, 0, 48, 48, 48);
            graphics.blit(PARTS, basePosX + 160, basePosY + 64, 0, 48, 48, 48);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            float sine = 0.5F + 0.25F * ((float) Math.sin(Math.toRadians((double) (4.0F * ((float) EmbersClientEvents.ticks + Minecraft.getInstance().getPartialTick())))) + 1.0F);
            RenderSystem.setShaderColor(1.0F, 0.2509804F, 0.0627451F, sine);
            for (float i = 0.0F; i < 4.0F; i++) {
                graphics.blit(PARTS, basePosX - 16, basePosY + 224, 192, 0, 48, 48);
            }
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            for (float i = 0.0F; i < (float) numResearches; i++) {
                float mouseDir = (float) Math.toDegrees(Math.atan2((double) (mouseY - (basePosY + 88)), (double) (mouseX - (basePosX + 96)))) + 90.0F;
                float distSq = (float) ((mouseX - (basePosX + 96)) * (mouseX - (basePosX + 96)) + (mouseY - (basePosY + 96)) * (mouseY - (basePosY + 96)));
                float angle = i * (360.0F / (float) numResearches);
                boolean selected = false;
                float diff = Math.min(Math.min(Math.abs(mouseDir - angle), Math.abs(mouseDir - 360.0F - angle)), Math.abs(mouseDir + 360.0F) - angle);
                ResearchCategory category = (ResearchCategory) ResearchManager.researches.get((int) i);
                boolean alreadyGlowing = category.researches.stream().anyMatch(entry -> this.searchResult.contains(entry));
                if (diff < 180.0F / (float) numResearches && distSq < 16000.0F) {
                    if (lastSelectedIndex != (int) i) {
                        if (category.isChecked() && !alreadyGlowing) {
                            this.playSound(EmbersSounds.CODEX_CATEGORY_SELECT.get());
                        } else {
                            this.playSound(EmbersSounds.CODEX_CATEGORY_UNSELECT.get());
                        }
                    }
                    selected = true;
                    this.selectedIndex = (int) i;
                    if (this.raise[(int) i] < 1.0F && doUpdateSynced) {
                        this.raise[(int) i] = this.raiseTargets[(int) i];
                        this.raiseTargets[(int) i] = this.raiseTargets[(int) i] * 0.5F + 0.5F;
                    }
                } else {
                    if (lastSelectedIndex == (int) i) {
                        this.playSound(EmbersSounds.CODEX_CATEGORY_UNSELECT.get());
                    }
                    if (doUpdateSynced) {
                        this.raise[(int) i] = this.raiseTargets[(int) i];
                        this.raiseTargets[(int) i] = this.raiseTargets[(int) i] * 0.5F;
                    }
                }
                float instRaise = this.raise[(int) i] * (1.0F - partialTicks) + this.raiseTargets[(int) i] * partialTicks;
                graphics.pose().pushPose();
                graphics.pose().translate((float) (basePosX + 96), (float) (basePosY + 88), 0.0F);
                graphics.pose().mulPose(Axis.ZP.rotationDegrees(angle));
                boolean glowing = alreadyGlowing || selected && category.isChecked();
                graphics.blit(category.getIndexTexture(), -16, (int) (-88.0F - 12.0F * instRaise), 192, 112, 32, 64);
                graphics.blit(category.getIndexTexture(), -6, (int) (-80.0F - 12.0F * instRaise), (int) category.getIconU() + (glowing ? 16 : 0), (int) category.getIconV(), 12, 12);
                graphics.pose().popPose();
            }
            graphics.blit(INDEX, basePosX + 64, basePosY + 56, 192, 176, 64, 64);
            if (!showSearchString && this.selectedIndex >= 0) {
                ResearchCategory category = (ResearchCategory) ResearchManager.researches.get(this.selectedIndex);
                drawCenteredTextGlowing(this.f_96547_, graphics, Component.literal(category.getName()).getVisualOrderText(), basePosX + 96, basePosY + 207);
            } else if (!this.searchString.isEmpty()) {
                drawCenteredTextGlowing(this.f_96547_, graphics, Component.literal(this.getSearchStringPrint()).getVisualOrderText(), basePosX + 96, basePosY + 207);
            } else {
                drawCenteredTextGlowing(this.f_96547_, graphics, Component.translatable("embers.research.null").getVisualOrderText(), basePosX + 96, basePosY + 207);
            }
            if (this.selectedIndex >= 0) {
                ResearchCategory category = (ResearchCategory) ResearchManager.researches.get(this.selectedIndex);
                List<Component> tooltip = category.getTooltip(showTooltips);
                if (!tooltip.isEmpty()) {
                    this.renderEmberTooltip(graphics, tooltip, mouseX, mouseY);
                }
            } else if (mouseX > basePosX - 16 && mouseY > basePosY + 224 && mouseX < basePosX - 16 + 48 && mouseY < basePosY + 224 + 48) {
                List<Component> tooltip = new ArrayList();
                for (String line : I18n.get("embers.research.controls").split(";")) {
                    tooltip.add(Component.literal(line));
                }
                this.renderEmberTooltip(graphics, tooltip, mouseX, mouseY);
            }
        } else if (this.researchPage == null) {
            float showSpeed = 0.3F;
            boolean playUnlockSound = false;
            boolean playLockSound = false;
            basePosX = (int) ((float) this.f_96543_ / 2.0F) - 192;
            basePosY = (int) ((float) this.f_96544_ / 2.0F) - 136;
            int basePosY2 = Math.min(this.f_96544_ - 33, basePosY + 272);
            graphics.blit(this.researchCategory.getBackgroundTexture(), basePosX, basePosY, 0, 0.0F, 0.0F, 384, 272, 512, 512);
            for (int i = 0; i < this.researchCategory.researches.size(); i++) {
                ResearchBase r = (ResearchBase) this.researchCategory.researches.get(i);
                if (!r.isHidden()) {
                    r.shownAmount = r.shownTarget;
                    if (r.areAncestorsChecked()) {
                        if (r.shownTarget <= 0.0F) {
                            playUnlockSound = true;
                        }
                        r.shownTarget = Math.min(1.0F, r.shownTarget + partialTicks * 0.1F * showSpeed);
                    } else {
                        if (r.shownTarget >= 1.0F) {
                            playLockSound = true;
                        }
                        r.shownTarget = Math.max(0.0F, r.shownTarget - partialTicks * 0.1F * showSpeed);
                    }
                    boolean isShown = (double) r.shownAmount >= 1.0;
                    if (isShown && mouseX >= basePosX + r.x - 24 && mouseY >= basePosY + r.y - 24 && mouseX <= basePosX + r.x + 24 && mouseY <= basePosY + r.y + 24) {
                        this.selectedPageIndex = i;
                        if (r.selectedAmount < 1.0F) {
                            r.selectedAmount = r.selectionTarget;
                            r.selectionTarget = r.selectionTarget * (1.0F - partialTicks) + (r.selectionTarget * 0.8F + 0.2F) * partialTicks;
                        }
                    } else if (r.selectedAmount > 0.0F) {
                        r.selectedAmount = r.selectionTarget;
                        r.selectionTarget = r.selectionTarget * (1.0F - partialTicks) + r.selectionTarget * 0.9F * partialTicks;
                    }
                    if (isShown && (this.searchResult.contains(r) || ResearchManager.isPathToLock(r) && this.searchResult.isEmpty())) {
                        Tesselator tess = Tesselator.getInstance();
                        BufferBuilder b = tess.getBuilder();
                        float x = (float) r.x;
                        float y = (float) r.y;
                        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                        RenderSystem.setShader(GameRenderer::m_172811_);
                        int index = this.searchResult.indexOf(r);
                        float amt = (float) Mth.clamp(-this.searchDelay, 0, 10) / 10.0F;
                        amt = (float) ((double) amt * Mth.clampedLerp(0.5, 1.0, (double) ((float) (this.searchResult.size() - index) / (float) this.searchResult.size())));
                        for (float j = 0.0F; j < 3.0F; j++) {
                            float coeff = (j + 1.0F) / 3.0F;
                            b.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
                            RenderUtil.renderHighlightCircle(b, (double) ((float) basePosX + x), (double) ((float) basePosY + y), (double) ((25.0F + 20.0F * coeff * coeff) * amt));
                            tess.end();
                        }
                        RenderSystem.defaultBlendFunc();
                    }
                    if (isShown && r.selectedAmount > 0.1F) {
                        Tesselator tess = Tesselator.getInstance();
                        BufferBuilder b = tess.getBuilder();
                        float x = (float) r.x;
                        float y = (float) r.y;
                        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                        RenderSystem.setShader(GameRenderer::m_172811_);
                        float amt = r.selectedAmount;
                        for (float j = 0.0F; j < 8.0F; j++) {
                            float coeff = (j + 1.0F) / 8.0F;
                            b.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
                            RenderUtil.renderHighlightCircle(b, (double) ((float) basePosX + x), (double) ((float) basePosY + y), (double) ((25.0F + 20.0F * coeff * coeff) * amt));
                            tess.end();
                        }
                        RenderSystem.defaultBlendFunc();
                    }
                    if (r.ancestors.size() > 0) {
                        for (int l = 0; l < r.ancestors.size(); l++) {
                            Tesselator tess = Tesselator.getInstance();
                            BufferBuilder b = tess.getBuilder();
                            ResearchBase ancestor = (ResearchBase) r.ancestors.get(l);
                            float x1 = (float) r.x;
                            float y1 = (float) r.y;
                            float x2 = (float) ancestor.x;
                            float y2 = (float) ancestor.y;
                            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                            RenderSystem.setShader(GameRenderer::m_172811_);
                            for (float j = 0.0F; j < 8.0F; j++) {
                                float coeff = (float) Math.pow((double) ((j + 1.0F) / 8.0F), 1.5);
                                float appearCoeff = Math.min(r.shownAmount, ancestor.shownAmount);
                                b.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
                                RenderUtil.renderWavyEmberLine(b, (double) ((float) basePosX + x1), (double) ((float) basePosY + y1), (double) ((float) basePosX + x2), (double) ((float) basePosY + y2), (double) (4.0F * coeff), (double) appearCoeff, new Color(255, 64, 16));
                                tess.end();
                            }
                            RenderSystem.defaultBlendFunc();
                        }
                    }
                }
            }
            for (int ix = 0; ix < this.researchCategory.researches.size(); ix++) {
                ResearchBase r = (ResearchBase) this.researchCategory.researches.get(ix);
                if (!r.isHidden() && (double) r.shownAmount > 0.5) {
                    RenderSystem.setShaderTexture(0, r.getIconBackground());
                    int u = (int) r.getIconBackgroundU();
                    int v = (int) r.getIconBackgroundV();
                    graphics.blit(r.getIconBackground(), basePosX + r.x - 24, basePosY + r.y - 24, 0, (float) u, (float) v, 48, 48, 512, 512);
                    if (!r.isChecked()) {
                        RenderSystem.setShaderTexture(0, ResearchManager.PAGE_ICONS);
                        int uOverlay = 240;
                        int vOverlay = 0;
                        graphics.blit(r.getIconBackground(), basePosX + r.x - 24, basePosY + r.y - 24, 0, (float) uOverlay, (float) vOverlay, 48, 48, 512, 512);
                    }
                    this.renderItemStackMinusTooltipAt(graphics, r.getIcon(), basePosX + r.x - 8, basePosY + r.y - 8);
                }
            }
            graphics.blit(this.researchCategory.getBackgroundTexture(), basePosX, basePosY2, 0, 0.0F, 272.0F, 384, 33, 512, 512);
            if (!showSearchString && this.selectedPageIndex >= 0) {
                ResearchBase research = (ResearchBase) this.researchCategory.researches.get(this.selectedPageIndex);
                drawCenteredTextGlowing(this.f_96547_, graphics, Component.literal(research.getName()).getVisualOrderText(), basePosX + 192, basePosY2 + 13);
            } else if (!this.searchString.isEmpty()) {
                drawCenteredTextGlowing(this.f_96547_, graphics, Component.literal(this.getSearchStringPrint()).getVisualOrderText(), basePosX + 192, basePosY2 + 13);
            }
            for (int ixx = 0; ixx < this.researchCategory.researches.size(); ixx++) {
                ResearchBase r = (ResearchBase) this.researchCategory.researches.get(ixx);
                if (!r.isHidden() && (double) r.shownAmount > 0.0 && r.shownAmount < 1.0F) {
                    Tesselator tess = Tesselator.getInstance();
                    BufferBuilder b = tess.getBuilder();
                    float x = (float) r.x;
                    float y = (float) r.y;
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                    RenderSystem.setShader(GameRenderer::m_172811_);
                    float amt = (float) Math.sin((double) r.shownAmount * Math.PI);
                    for (float j = 0.0F; j < 8.0F; j++) {
                        float coeff = (j + 1.0F) / 8.0F;
                        b.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
                        RenderUtil.renderHighlightCircle(b, (double) ((float) basePosX + x), (double) ((float) basePosY + y), (double) ((25.0F + 20.0F * coeff * coeff) * amt));
                        tess.end();
                    }
                    RenderSystem.defaultBlendFunc();
                }
            }
            if (this.selectedPageIndex >= 0) {
                ResearchBase page = (ResearchBase) this.researchCategory.researches.get(this.selectedPageIndex);
                List<Component> tooltip = page.getTooltip(showTooltips);
                if (!tooltip.isEmpty()) {
                    this.renderEmberTooltip(graphics, tooltip, mouseX, mouseY);
                }
            }
            if (playLockSound) {
                this.playSound(EmbersSounds.CODEX_LOCK.get(), showSpeed);
            }
            if (playUnlockSound) {
                this.playSound(EmbersSounds.CODEX_UNLOCK.get(), showSpeed);
            }
        } else {
            graphics.blit(this.researchPage.getBackground(), basePosX, basePosY, 0, 0, 192, 256);
            drawCenteredTextGlowing(this.f_96547_, graphics, Component.literal(this.researchPage.getTitle()).getVisualOrderText(), basePosX + 96, basePosY + 19);
            this.researchPage.renderPageContent(graphics, this, basePosX, basePosY, this.f_96547_);
            if (this.researchPage.hasMultiplePages()) {
                this.nextPageSelected = false;
                this.previousPageSelected = false;
                int arrowY = basePosY + 256 - 13;
                RenderSystem.enableBlend();
                if (this.researchPage.getNextPage() != this.researchPage) {
                    int rightArrowX = basePosX + 192 - 9 - 8;
                    this.drawModalRectGlowing(graphics, this.researchPage.getBackground(), rightArrowX, arrowY, 192, 24, 18, 13);
                    this.nextPageSelected = mouseX >= rightArrowX - 3 && mouseY >= arrowY - 3 && mouseX <= rightArrowX + 3 + 18 && mouseY <= arrowY + 3 + 13;
                }
                if (this.researchPage.getPreviousPage() != this.researchPage) {
                    int leftArrowX = basePosX - 9 + 8;
                    this.drawModalRectGlowing(graphics, this.researchPage.getBackground(), leftArrowX, arrowY, 192, 37, 18, 13);
                    this.previousPageSelected = mouseX >= leftArrowX - 3 && mouseY >= arrowY - 3 && mouseX <= leftArrowX + 3 + 18 && mouseY <= arrowY + 3 + 13;
                }
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
        this.doRenderTooltip(graphics);
        RenderSystem.disableBlend();
    }

    private String getSearchStringPrint() {
        String searchStringFormat;
        if (this.searchDelay > 0) {
            searchStringFormat = "";
        } else if (this.searchResult.isEmpty()) {
            searchStringFormat = ChatFormatting.DARK_GRAY.toString();
        } else {
            searchStringFormat = ChatFormatting.GREEN.toString();
        }
        return searchStringFormat + this.searchString;
    }

    public void renderEmberTooltip(GuiGraphics graphics, List<Component> text, int x, int y) {
        List<ClientTooltipComponent> components = ForgeHooksClient.gatherTooltipComponents(ItemStack.EMPTY, text, x, this.f_96543_, this.f_96544_, this.f_96547_);
        drawHoveringTextGlowing(graphics, components, x, y, this.f_96543_, this.f_96544_, -1, this.f_96547_);
    }

    public float getVert(float i, float f1, float f2) {
        float coeff = Math.abs(i) + (float) EmbersClientEvents.ticks + Minecraft.getInstance().getPartialTick();
        return Math.abs(10.0F * (1.0F - Math.abs(i / 80.0F)) * (float) (Math.sin((double) (coeff * f1)) + 0.4F * Math.sin((double) (coeff * f2))));
    }

    @Override
    public void onClose() {
        super.onClose();
        for (ResearchCategory category : ResearchManager.researches) {
            for (ResearchBase base : category.researches) {
                base.selectedAmount = 0.0F;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.searchDelay--;
        if (this.searchDelay == 0) {
            this.searchResult.clear();
            Map<ResearchBase, Integer> results = ResearchManager.findByTag(this.searchString);
            results.entrySet().stream().sorted((x, y) -> ((Integer) y.getValue()).compareTo((Integer) x.getValue())).map(Entry::getKey).forEach(result -> this.searchResult.add(result));
        }
    }

    public static void drawHoveringTextGlowing(GuiGraphics graphics, List<ClientTooltipComponent> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, Font font) {
        if (!textLines.isEmpty()) {
            int i = 0;
            int j = textLines.size() == 1 ? -2 : 0;
            for (ClientTooltipComponent clienttooltipcomponent : textLines) {
                int k = clienttooltipcomponent.getWidth(font);
                if (k > i) {
                    i = k;
                }
                j += clienttooltipcomponent.getHeight();
            }
            int j2 = mouseX + 12;
            int k2 = mouseY - 12;
            if (j2 + i > screenWidth) {
                j2 -= 28 + i;
            }
            if (k2 + j + 6 > screenHeight) {
                k2 = screenHeight - j - 6;
            }
            graphics.pose().pushPose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            RenderSystem.setShader(GameRenderer::m_172811_);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            Matrix4f matrix4f = graphics.pose().last().pose();
            int backgroundColor = new Color(0, 0, 0, 128).getRGB();
            float sine = 0.5F * ((float) Math.sin(Math.toRadians((double) (4.0F * ((float) EmbersClientEvents.ticks + Minecraft.getInstance().getPartialTick())))) + 1.0F);
            float cosine = 0.5F * ((float) Math.cos(Math.toRadians((double) (4.0F * ((float) EmbersClientEvents.ticks + Minecraft.getInstance().getPartialTick())))) + 1.0F);
            int borderColorStart = new Color(255, 64 + (int) (64.0F * sine), 16, 128).getRGB();
            int borderColorEnd = new Color(255, 64 + (int) (64.0F * cosine), 16, 128).getRGB();
            graphics.fillGradient(j2 - 3, k2 - 4, j2 + i + 3, k2 - 3, 400, backgroundColor, backgroundColor);
            graphics.fillGradient(j2 - 3, k2 + j + 3, j2 + i + 3, k2 + j + 4, 400, backgroundColor, backgroundColor);
            graphics.fillGradient(j2 - 3, k2 - 3, j2 + i + 3, k2 + j + 3, 400, backgroundColor, backgroundColor);
            graphics.fillGradient(j2 - 4, k2 - 3, j2 - 3, k2 + j + 3, 400, backgroundColor, backgroundColor);
            graphics.fillGradient(j2 + i + 3, k2 - 3, j2 + i + 4, k2 + j + 3, 400, backgroundColor, backgroundColor);
            graphics.fillGradient(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + j + 3 - 1, 400, borderColorStart, borderColorEnd);
            graphics.fillGradient(j2 + i + 2, k2 - 3 + 1, j2 + i + 3, k2 + j + 3 - 1, 400, borderColorStart, borderColorEnd);
            graphics.fillGradient(j2 - 3, k2 - 3, j2 + i + 3, k2 - 3 + 1, 400, borderColorStart, borderColorStart);
            graphics.fillGradient(j2 - 3, k2 + j + 2, j2 + i + 3, k2 + j + 3, 400, borderColorEnd, borderColorEnd);
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            BufferUploader.drawWithShader(bufferbuilder.end());
            RenderSystem.disableBlend();
            MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            graphics.pose().translate(0.0, 0.0, 400.0);
            int l1 = k2;
            for (int i2 = 0; i2 < textLines.size(); i2++) {
                ClientTooltipComponent clienttooltipcomponent1 = (ClientTooltipComponent) textLines.get(i2);
                clienttooltipcomponent1.renderText(font, j2, l1, matrix4f, multibuffersource$buffersource);
                l1 += clienttooltipcomponent1.getHeight() + (i2 == 0 ? 2 : 0);
            }
            multibuffersource$buffersource.endBatch();
            graphics.pose().popPose();
        }
    }
}