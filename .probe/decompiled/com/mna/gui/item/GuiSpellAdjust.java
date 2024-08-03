package com.mna.gui.item;

import com.google.common.collect.UnmodifiableIterator;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiable;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiJEIDisable;
import com.mna.gui.block.GuiInscriptionTable;
import com.mna.gui.containers.item.ContainerSpellAdjustments;
import com.mna.gui.widgets.AttributeButton;
import com.mna.gui.widgets.ImageButtonWithAlphaBlend;
import com.mna.gui.widgets.RGBPicker;
import com.mna.network.ClientMessageDispatcher;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GuiSpellAdjust extends GuiJEIDisable<ContainerSpellAdjustments> {

    ArrayList<ImageButton> shapeAttributeButtons;

    ArrayList<ImageButton> componentAttributeButtons;

    final float textScaleFactor = (Boolean) Minecraft.getInstance().options.forceUnicodeFont.get() ? 1.0F : 0.7F;

    static final int textColor = FastColor.ARGB32.color(255, 49, 49, 49);

    static final int textColorLight = FastColor.ARGB32.color(255, 200, 200, 200);

    final int colWidth = 38;

    final int rowHeight = 13;

    final int texSize = 32;

    int spellNameWidth = 1;

    String spellName = "";

    final List<Component> currentTooltip;

    IPlayerRoteSpells playerRote = null;

    IPlayerProgression playerProgression = null;

    RGBPicker color;

    public GuiSpellAdjust(ContainerSpellAdjustments screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 256;
        this.f_97727_ = 256;
        this.currentTooltip = new ArrayList();
    }

    @Override
    protected void init() {
        super.m_7856_();
        this.playerRote = (IPlayerRoteSpells) this.f_96541_.player.getCapability(PlayerRoteSpellsProvider.ROTE).orElse(null);
        this.playerProgression = (IPlayerProgression) this.f_96541_.player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        this.shapeAttributeButtons = new ArrayList();
        this.componentAttributeButtons = new ArrayList();
        this.spellName = ((ContainerSpellAdjustments) this.f_97732_).getSpellName();
        this.spellNameWidth = this.f_96547_.width(this.spellName);
        this.m_142416_(new ImageButtonWithAlphaBlend(this.f_97735_ + 223, this.f_97736_ + 7, 16, 16, 223, 152, 0, GuiTextures.Items.SPELL_VALUES, 256, 256, button -> {
            this.m_7379_();
            ClientMessageDispatcher.sendRequestOpenSpellNameAndIconGUI();
        }));
        int overrideColor = ((ContainerSpellAdjustments) this.f_97732_).getRecipe().getParticleColorOverride();
        this.color = new RGBPicker(this.f_97735_ + 80, this.f_97736_ + 202, this::adjustSpellColors);
        this.color.setValue(FastColor.ARGB32.red(overrideColor), FastColor.ARGB32.green(overrideColor), FastColor.ARGB32.blue(overrideColor), FastColor.ARGB32.alpha(overrideColor));
        this.m_142416_(this.color);
        this.setupShapeAttributeButtons(((ContainerSpellAdjustments) this.f_97732_).getShape().getPart());
        this.setupComponentAttributeButtons(((ContainerSpellAdjustments) this.f_97732_).getComponent().getPart());
    }

    private void setupShapeAttributeButtons(Shape shape) {
        this.clearAttributeButtons(this.shapeAttributeButtons);
        if (shape != null && !((ContainerSpellAdjustments) this.f_97732_).isTranscribed()) {
            this.setupAttributeButtons(122, 45, this.shapeAttributeButtons, shape);
        }
    }

    private void setupAttributeButtons(int xStart, int yStart, ArrayList<ImageButton> addTo, IModifiable<? extends ISpellComponent> modifiable) {
        int count = 0;
        for (UnmodifiableIterator var6 = modifiable.getModifiableAttributes().iterator(); var6.hasNext(); count++) {
            AttributeValuePair attribute = (AttributeValuePair) var6.next();
            boolean attributeModifiable = false;
            for (int i = 0; i < 3; i++) {
                Modifier modifier = ((ContainerSpellAdjustments) this.f_97732_).getModifier(i);
                if (modifier != null && modifier.modifiesType(attribute.getAttribute())) {
                    attributeModifiable = true;
                    break;
                }
            }
            int row = (int) Math.floor((double) (count / 2));
            int col = count % 2;
            int xOffset = xStart + 38 * col;
            int yOffset = yStart + 13 * row;
            if (attributeModifiable) {
                ImageButtonWithAlphaBlend upButton = (ImageButtonWithAlphaBlend) this.m_142416_(new ImageButtonWithAlphaBlend(this.f_97735_ + xOffset + 2, this.f_97736_ + yOffset - 2, 6, 3, 1, 49, 0, GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, 128, 128, button -> {
                    ((ContainerSpellAdjustments) this.f_97732_).increaseAttribute(this.f_96541_.player, modifiable, attribute.getAttribute(), this.f_96541_.level, Screen.hasShiftDown());
                    GuiInscriptionTable.checkAndShowShiftTooltip();
                }));
                ImageButtonWithAlphaBlend downButton = (ImageButtonWithAlphaBlend) this.m_142416_(new ImageButtonWithAlphaBlend(this.f_97735_ + xOffset + 2, this.f_97736_ + yOffset + 3, 6, 3, 1, 54, 0, GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, 128, 128, button -> {
                    ((ContainerSpellAdjustments) this.f_97732_).decreaseAttribute(this.f_96541_.player, modifiable, attribute.getAttribute(), this.f_96541_.level, Screen.hasShiftDown());
                    GuiInscriptionTable.checkAndShowShiftTooltip();
                }));
                addTo.add(upButton);
                addTo.add(downButton);
            }
            Point texCoord = (Point) GuiTextures.Attribute_Icon_Mappings.get(attribute.getAttribute());
            int blitSize = 8;
            float scaleFactor = (float) blitSize / 52.0F;
            ImageButton iconButton = (ImageButton) this.m_142416_(new AttributeButton(this.f_97735_ + xOffset - 8, this.f_97736_ + yOffset - 2, blitSize, blitSize, (int) ((float) texCoord.x * scaleFactor), (int) ((float) texCoord.y * scaleFactor), 0, GuiTextures.Widgets.ATTRIBUTE_ICONS, (int) (208.0F * scaleFactor), (int) (208.0F * scaleFactor), button -> {
            }, new String[] { attribute.getAttribute().getLocaleKey(), ((ISpellComponent) modifiable).getDescriptionTooltip(attribute.getAttribute()) }, this::addTooltipLine));
            addTo.add(iconButton);
        }
    }

    private void clearAttributeButtons(ArrayList<ImageButton> attributeButtons) {
        for (ImageButton button : attributeButtons) {
            this.m_169411_(button);
        }
        attributeButtons.clear();
    }

    private void setupComponentAttributeButtons(SpellEffect component) {
        this.clearAttributeButtons(this.componentAttributeButtons);
        if (component != null && !((ContainerSpellAdjustments) this.f_97732_).isTranscribed()) {
            this.setupAttributeButtons(122, 92, this.componentAttributeButtons, component);
        }
    }

    private void adjustSpellColors(Integer[] colors) {
        ((ContainerSpellAdjustments) this.f_97732_).getRecipe().setParticleColorOverride(FastColor.ARGB32.color(colors[3], colors[0], colors[1], colors[2]));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.color.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) ? true : super.m_7979_(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void removed() {
        super.m_7861_();
        if (((ContainerSpellAdjustments) this.f_97732_).getRecipe().isValid()) {
            ClientMessageDispatcher.sendSpellAdjustmentMessage(((ContainerSpellAdjustments) this.f_97732_).getRecipe(), ((ContainerSpellAdjustments) this.f_97732_).getHand());
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.currentTooltip.clear();
        RenderSystem.enableBlend();
        super.m_88315_(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (this.currentTooltip != null) {
            pGuiGraphics.renderTooltip(this.f_96547_, this.currentTooltip, Optional.empty(), mouseX, mouseY);
        }
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
        RenderSystem.disableBlend();
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        int xPos = 133;
        int yPos = 45;
        int count = 0;
        int text_y = 12;
        for (FormattedText line : this.f_96547_.getSplitter().splitLines(this.spellName, 150, Style.EMPTY)) {
            String textLine = line.getString();
            int snw = this.f_96547_.width(textLine);
            pGuiGraphics.drawString(this.f_96547_, textLine, this.f_97726_ / 2 - snw / 2, text_y, textColor, false);
            text_y += 9;
        }
        ItemStack stack = ((ContainerSpellAdjustments) this.f_97732_).getSpellStack();
        pGuiGraphics.renderItem(stack, 7, 7);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        if (((ContainerSpellAdjustments) this.f_97732_).getShape() != null) {
            pGuiGraphics.blit(((ContainerSpellAdjustments) this.f_97732_).getShape().getPart().getGuiIcon(), 150, 88, 0.0F, 0.0F, 64, 64, 64, 64);
            for (UnmodifiableIterator var24 = ((ContainerSpellAdjustments) this.f_97732_).getShape().getContainedAttributes().iterator(); var24.hasNext(); count++) {
                Attribute attribute = (Attribute) var24.next();
                int row = (int) Math.floor((double) (count / 2));
                int col = count % 2;
                this.drawAttributeValue(pGuiGraphics, xPos + col * 38, yPos + row * 13, ((ContainerSpellAdjustments) this.f_97732_).getShape().getValueWithoutMultipliers(attribute));
            }
        }
        yPos += 47;
        count = 0;
        if (((ContainerSpellAdjustments) this.f_97732_).getComponent() != null) {
            pGuiGraphics.blit(((ContainerSpellAdjustments) this.f_97732_).getComponent().getPart().getGuiIcon(), 150, 182, 0.0F, 0.0F, 64, 64, 64, 64);
            for (UnmodifiableIterator var25 = ((ContainerSpellAdjustments) this.f_97732_).getComponent().getContainedAttributes().iterator(); var25.hasNext(); count++) {
                Attribute attribute = (Attribute) var25.next();
                int row = (int) Math.floor((double) (count / 2));
                int col = count % 2;
                this.drawAttributeValue(pGuiGraphics, xPos + col * 38, yPos + row * 13, ((ContainerSpellAdjustments) this.f_97732_).getComponent().getValueWithoutMultipliers(attribute));
            }
        }
        for (int i = 0; i < 3; i++) {
            Modifier m = ((ContainerSpellAdjustments) this.f_97732_).getModifier(i);
            if (m != null) {
                pGuiGraphics.blit(m.getGuiIcon(), 150 + 74 * i, 276, 0.0F, 0.0F, 64, 64, 64, 64);
            }
        }
        pGuiGraphics.pose().popPose();
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(this.textScaleFactor, this.textScaleFactor, this.textScaleFactor);
        String header = I18n.get("gui.mna.mana_cost") + ":";
        String manaCost = String.format("%.1f", ((ContainerSpellAdjustments) this.f_97732_).getManaCost(this.f_96541_.player));
        String complexityHeader = I18n.get("gui.mna.complexity") + ":";
        String complexity = String.format("%.1f / %d", ((ContainerSpellAdjustments) this.f_97732_).getComplexity(this.f_96541_.player), this.playerProgression.getTierMaxComplexity());
        float manaCostWidth = (float) this.f_96547_.width(manaCost) * this.textScaleFactor;
        float headerWidth = (float) this.f_96547_.width(header) * this.textScaleFactor;
        float complexityHeaderWidth = (float) this.f_96547_.width(complexityHeader) * this.textScaleFactor;
        float complexityWidth = (float) this.f_96547_.width(complexity) * this.textScaleFactor;
        float padding = 2.0F;
        float summaryXPos = (float) (this.f_97726_ / 2) - (complexityHeaderWidth + complexityWidth + padding) / 2.0F;
        float summaryYPos = this.f_96541_.options.forceUnicodeFont.get() ? 180.0F : 182.0F;
        pGuiGraphics.drawString(this.f_96547_, complexityHeader, (int) (summaryXPos / this.textScaleFactor), (int) (summaryYPos / this.textScaleFactor), textColor, false);
        summaryXPos += complexityHeaderWidth + padding;
        if (((ContainerSpellAdjustments) this.f_97732_).getComplexity(this.f_96541_.player) <= (float) this.playerProgression.getTierMaxComplexity()) {
            pGuiGraphics.drawString(this.f_96547_, complexity, (int) (summaryXPos / this.textScaleFactor), (int) (summaryYPos / this.textScaleFactor), textColor, false);
        } else {
            pGuiGraphics.drawString(this.f_96547_, complexity, (int) (summaryXPos / this.textScaleFactor), (int) (summaryYPos / this.textScaleFactor), ChatFormatting.RED.getColor(), false);
        }
        summaryXPos = (float) (this.f_97726_ / 2) - (headerWidth + manaCostWidth + padding) / 2.0F;
        summaryYPos = (float) (this.f_96541_.options.forceUnicodeFont.get() ? 180 : 184) + 9.0F * this.textScaleFactor;
        pGuiGraphics.drawString(this.f_96547_, header, (int) (summaryXPos / this.textScaleFactor), (int) (summaryYPos / this.textScaleFactor), textColor, false);
        summaryXPos += headerWidth + padding;
        pGuiGraphics.drawString(this.f_96547_, manaCost, (int) (summaryXPos / this.textScaleFactor), (int) (summaryYPos / this.textScaleFactor), textColor, false);
        pGuiGraphics.pose().popPose();
    }

    private void drawAttributeValue(GuiGraphics pGuiGraphics, int x, int y, float value) {
        String valueString = String.format("%.1f", value);
        pGuiGraphics.drawString(this.f_96547_, valueString, (int) ((float) x / 0.5F), (int) ((float) y / 0.5F), textColor, false);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = this.f_97735_;
        int j = this.f_97736_;
        pGuiGraphics.blit(GuiTextures.Items.SPELL_VALUES, i, j, 0.0F, 145.0F, 247, 30, this.f_97726_, this.f_97727_);
        pGuiGraphics.blit(GuiTextures.Items.SPELL_VALUES, i + this.f_97726_ / 2 - 50, j + 170, 0.0F, 176.0F, 100, 30, this.f_97726_, this.f_97727_);
        pGuiGraphics.blit(GuiTextures.Items.SPELL_VALUES, i + this.f_97726_ / 2 - 60, j + 35, 0.0F, 0.0F, 120, 144, this.f_97726_, this.f_97727_);
    }

    public void addTooltipLine(Component comp) {
        this.currentTooltip.add(comp);
    }
}