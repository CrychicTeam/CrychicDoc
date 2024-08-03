package com.mna.gui.block;

import com.google.common.collect.UnmodifiableIterator;
import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiable;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.gui.base.SearchableGui;
import com.mna.gui.containers.block.ContainerInscriptionTable;
import com.mna.gui.widgets.AttributeButton;
import com.mna.gui.widgets.BorderedImageButton;
import com.mna.gui.widgets.ReagentList;
import com.mna.gui.widgets.SpellPartList;
import com.mna.network.messages.to_client.ShowDidYouKnow;
import com.mna.spells.crafting.ModifiedSpellPart;
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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;

public class GuiInscriptionTable extends SearchableGui<ContainerInscriptionTable> {

    ImageButton buildButton;

    ImageButton[] pieceWidgets;

    ImageButton activeShapeButton;

    ImageButton activeComponentButton;

    ImageButton[] activeModifierButtons;

    ArrayList<ImageButton> shapeAttributeButtons;

    ArrayList<ImageButton> componentAttributeButtons;

    private SpellPartList spellPartList;

    private ReagentList reagentList;

    final float textScaleFactor = 0.5F;

    final int textColor = 4467972;

    final int colWidth = 38;

    final int rowHeight = 13;

    final int texSize = 32;

    final List<Component> currentTooltip;

    MutableComponent craftMessage = null;

    InscriptionTableTile.CraftCheckResult lastResult;

    IPlayerProgression playerProgression;

    private static long lastModifierClickTime = -1L;

    private static long clickCount = 0L;

    public GuiInscriptionTable(ContainerInscriptionTable screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 256;
        this.f_97727_ = 256;
        this.activeModifierButtons = new ImageButton[3];
        this.currentTooltip = new ArrayList();
    }

    public void addTooltipLine(Component comp) {
        this.currentTooltip.add(comp);
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        if (this.spellPartList.m_5953_(p_mouseScrolled_1_, p_mouseScrolled_3_)) {
            return this.spellPartList.m_6050_(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
        } else {
            return this.reagentList.m_5953_(p_mouseScrolled_1_, p_mouseScrolled_3_) ? this.reagentList.m_6050_(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_) : true;
        }
    }

    @Override
    protected void init() {
        super.m_7856_();
        this.playerProgression = (IPlayerProgression) this.f_96541_.player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        this.spellPartList = new SpellPartList(true, false, this.f_97735_, this.f_97736_, 4, 53, 64, 111, s -> this.OnShapeClicked(s), c -> this.OnComponentClicked(c), m -> this.OnModifierClicked(m), tt -> this.currentTooltip.add(tt)).enableTierChecks();
        this.m_142416_(this.spellPartList);
        this.reagentList = new ReagentList(this.f_97735_, this.f_97736_, 196, 111, 51, 53, this::addTooltipLine);
        this.m_142416_(this.reagentList);
        int tbWidth = 66;
        int x = this.f_97735_ + 6;
        int y = this.f_97736_ + 30;
        this.shapeAttributeButtons = new ArrayList();
        this.componentAttributeButtons = new ArrayList();
        this.setupBuildButton();
        if (((ContainerInscriptionTable) this.f_97732_).getCurrentShape() != null) {
            this.setupShapeWidgetsFor(((ContainerInscriptionTable) this.f_97732_).getCurrentShape().getPart());
        }
        if (((ContainerInscriptionTable) this.f_97732_).getCurrentComponent() != null) {
            this.setupComponentWidgetsFor(((ContainerInscriptionTable) this.f_97732_).getCurrentComponent().getPart());
        }
        for (int i = 0; i < 3; i++) {
            if (((ContainerInscriptionTable) this.f_97732_).getCurrentModifier(i) != null) {
                this.setupModifierWidgetsFor(((ContainerInscriptionTable) this.f_97732_).getCurrentModifier(i), i);
            }
        }
        this.reagentList.reInit(((ContainerInscriptionTable) this.f_97732_).getCurrentReagents());
        this.initSearch(x, y, tbWidth, 16);
    }

    @Override
    protected void searchTermChanged(String newTerm) {
        this.currentSearchTerm = newTerm.toLowerCase();
        this.spellPartList.clear();
        this.spellPartList.reInit(this.currentSearchTerm);
        this.spellPartList.m_93410_(0.0);
    }

    private void setupShapeAttributeButtons(Shape shape) {
        this.clearAttributeButtons(this.shapeAttributeButtons);
        if (shape != null) {
            this.setupAttributeButtons(115, 34, this.shapeAttributeButtons, shape);
        }
    }

    private void setupComponentAttributeButtons(SpellEffect component) {
        this.clearAttributeButtons(this.componentAttributeButtons);
        if (component != null) {
            this.setupAttributeButtons(115, 81, this.componentAttributeButtons, component);
        }
    }

    private void setupAttributeButtons(int xStart, int yStart, ArrayList<ImageButton> addTo, IModifiable<? extends ISpellComponent> modifiable) {
        int count = 0;
        for (UnmodifiableIterator var6 = modifiable.getModifiableAttributes().iterator(); var6.hasNext(); count++) {
            AttributeValuePair attribute = (AttributeValuePair) var6.next();
            boolean attributeModifiable = false;
            for (int i = 0; i < 3; i++) {
                Modifier modifier = ((ContainerInscriptionTable) this.f_97732_).getCurrentModifier(i);
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
                ImageButton upButton = (ImageButton) this.m_142416_(new ImageButton(this.f_97735_ + xOffset + 16, this.f_97736_ + yOffset + 1, 6, 3, 1, 49, 0, GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, 128, 128, button -> {
                    ((ContainerInscriptionTable) this.f_97732_).increaseAttribute(this.f_96541_.player, modifiable, attribute.getAttribute(), this.f_96541_.level, Screen.hasShiftDown());
                    checkAndShowShiftTooltip();
                }));
                ImageButton downButton = (ImageButton) this.m_142416_(new ImageButton(this.f_97735_ + xOffset + 16, this.f_97736_ + yOffset + 6, 6, 3, 1, 54, 0, GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, 128, 128, button -> {
                    ((ContainerInscriptionTable) this.f_97732_).decreaseAttribute(this.f_96541_.player, modifiable, attribute.getAttribute(), this.f_96541_.level, Screen.hasShiftDown());
                    checkAndShowShiftTooltip();
                }));
                addTo.add(upButton);
                addTo.add(downButton);
            }
            Point texCoord = (Point) GuiTextures.Attribute_Icon_Mappings.get(attribute.getAttribute());
            int blitSize = 8;
            float scaleFactor = (float) blitSize / 52.0F;
            ImageButton iconButton = (ImageButton) this.m_142416_(new AttributeButton(this.f_97735_ + xOffset + 6, this.f_97736_ + yOffset + 1, blitSize, blitSize, (int) ((float) texCoord.x * scaleFactor), (int) ((float) texCoord.y * scaleFactor), 0, GuiTextures.Widgets.ATTRIBUTE_ICONS, (int) (208.0F * scaleFactor), (int) (208.0F * scaleFactor), button -> {
            }, new String[] { attribute.getAttribute().getLocaleKey(), ((ISpellComponent) modifiable).getDescriptionTooltip(attribute.getAttribute()) }, this::addTooltipLine));
            addTo.add(iconButton);
        }
    }

    public static void checkAndShowShiftTooltip() {
        Minecraft mc = Minecraft.getInstance();
        if (!Screen.hasShiftDown()) {
            if (mc.level.m_46467_() - lastModifierClickTime < 200L) {
                clickCount++;
                if (clickCount >= 4L) {
                    ManaAndArtifice.instance.proxy.showDidYouKnow(mc.player, ShowDidYouKnow.Messages.MODIFIER_SHIFT);
                }
            } else {
                clickCount = 0L;
            }
            lastModifierClickTime = mc.level.m_46467_();
        }
    }

    private void clearAttributeButtons(ArrayList<ImageButton> attributeButtons) {
        for (ImageButton button : attributeButtons) {
            this.m_169411_(button);
        }
        attributeButtons.clear();
    }

    private void setupBuildButton() {
        this.buildButton = (ImageButton) this.m_142416_(new ImageButton(this.f_97735_ + 81, this.f_97736_ + 9, 25, 14, 0, 20, 0, GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, 128, 128, button -> ((ContainerInscriptionTable) this.f_97732_).sendStartBuild()));
        this.buildButton.f_93624_ = false;
        this.buildButton.f_93623_ = false;
    }

    private void setupShapeWidgetsFor(Shape shape) {
        int texSize = 32;
        if (shape != null) {
            if (this.activeShapeButton != null) {
                this.m_169411_(this.activeShapeButton);
            }
            this.activeShapeButton = (ImageButton) this.m_142416_(new BorderedImageButton(this.f_97735_ + 82, this.f_97736_ + 36, texSize, texSize, 0, 0, 0, shape.getGuiIcon(), texSize, texSize, button -> {
                if (((ContainerInscriptionTable) this.f_97732_).setCurrentShape(null, this.f_96541_.level)) {
                    this.m_169411_(button);
                    this.setupShapeWidgetsFor(null);
                    this.reagentList.reInit(((ContainerInscriptionTable) this.f_97732_).getCurrentReagents());
                }
            }).setBorder(shape.isSilverSpell()));
        }
        this.setupShapeAttributeButtons(shape);
    }

    private void OnShapeClicked(Shape shape) {
        if (((ContainerInscriptionTable) this.f_97732_).setCurrentShape(shape, this.f_96541_.level)) {
            this.setupShapeWidgetsFor(shape);
            if (this.searchBox != null) {
                this.searchBox.setHighlightPos(0);
            }
            this.reagentList.reInit(((ContainerInscriptionTable) this.f_97732_).getCurrentReagents());
        }
    }

    private void setupComponentWidgetsFor(SpellEffect component) {
        int texSize = 32;
        if (component != null) {
            if (this.activeComponentButton != null) {
                this.m_169411_(this.activeComponentButton);
            }
            this.activeComponentButton = (ImageButton) this.m_142416_(new BorderedImageButton(this.f_97735_ + 82, this.f_97736_ + 83, 32, 32, 0, 0, 0, component.getGuiIcon(), 32, 32, button -> {
                if (((ContainerInscriptionTable) this.f_97732_).setCurrentComponent(null, this.f_96541_.level)) {
                    this.m_169411_(button);
                    this.setupComponentWidgetsFor(null);
                    this.reagentList.reInit(((ContainerInscriptionTable) this.f_97732_).getCurrentReagents());
                }
            }).setBorder(component.isSilverSpell()));
        }
        this.setupComponentAttributeButtons(component);
    }

    private void OnComponentClicked(SpellEffect component) {
        if (((ContainerInscriptionTable) this.f_97732_).setCurrentComponent(component, this.f_96541_.level)) {
            this.setupComponentWidgetsFor(component);
            if (this.searchBox != null) {
                this.searchBox.setHighlightPos(0);
            }
            this.reagentList.reInit(((ContainerInscriptionTable) this.f_97732_).getCurrentReagents());
        }
    }

    private void setupModifierWidgetsFor(Modifier modifier, int index) {
        if (modifier != null) {
            this.activeModifierButtons[index] = (ImageButton) this.m_142416_(new BorderedImageButton(this.f_97735_ + 82 + 37 * index, this.f_97736_ + 130, 32, 32, 0, 0, 0, modifier.getGuiIcon(), 32, 32, button -> {
                if (((ContainerInscriptionTable) this.f_97732_).setCurrentModifier(index, null, this.f_96541_.level)) {
                    this.m_169411_(button);
                    this.setupModifierWidgetsFor(null, index);
                    this.activeModifierButtons[index] = null;
                    this.reagentList.reInit(((ContainerInscriptionTable) this.f_97732_).getCurrentReagents());
                }
            }).setBorder(modifier.isSilverSpell()));
        }
        ModifiedSpellPart<Shape> curShape = ((ContainerInscriptionTable) this.f_97732_).getCurrentShape();
        if (curShape != null) {
            this.setupShapeAttributeButtons(curShape.getPart());
        }
        ModifiedSpellPart<SpellEffect> curComp = ((ContainerInscriptionTable) this.f_97732_).getCurrentComponent();
        if (curComp != null) {
            this.setupComponentAttributeButtons(curComp.getPart());
        }
    }

    private void OnModifierClicked(Modifier modifier) {
        if (this.searchBox != null) {
            this.searchBox.setHighlightPos(0);
        }
        for (int i = 0; i < 3; i++) {
            if (((ContainerInscriptionTable) this.f_97732_).getCurrentModifier(i) == modifier) {
                return;
            }
        }
        for (int ix = 0; ix < 3; ix++) {
            if (this.activeModifierButtons[ix] == null) {
                if (!((ContainerInscriptionTable) this.f_97732_).setCurrentModifier(ix, modifier, this.f_96541_.level)) {
                    return;
                }
                this.setupModifierWidgetsFor(modifier, ix);
                break;
            }
        }
        this.reagentList.reInit(((ContainerInscriptionTable) this.f_97732_).getCurrentReagents());
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.currentTooltip.clear();
        this.lastResult = ((ContainerInscriptionTable) this.f_97732_).isReadyToBuild(this.playerProgression);
        if (this.lastResult == InscriptionTableTile.CraftCheckResult.READY) {
            this.buildButton.f_93624_ = true;
            this.buildButton.f_93623_ = true;
            this.craftMessage = null;
        } else {
            this.buildButton.f_93624_ = false;
            this.buildButton.f_93623_ = false;
            if (!this.lastResult.getTranslationKey().isEmpty()) {
                this.craftMessage = Component.translatable(this.lastResult.getTranslationKey()).withStyle(ChatFormatting.RED);
            }
        }
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (this.currentTooltip.size() > 0) {
            pGuiGraphics.renderTooltip(this.f_96547_, this.currentTooltip, Optional.empty(), mouseX, mouseY);
        } else {
            this.m_280072_(pGuiGraphics, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        int xPos = 140;
        int yPos = 37;
        int count = 0;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        if (((ContainerInscriptionTable) this.f_97732_).getCurrentShape() != null) {
            for (UnmodifiableIterator s = ((ContainerInscriptionTable) this.f_97732_).getCurrentShape().getContainedAttributes().iterator(); s.hasNext(); count++) {
                Attribute attribute = (Attribute) s.next();
                int row = (int) Math.floor((double) (count / 2));
                int col = count % 2;
                this.drawAttributeValue(pGuiGraphics, xPos + col * 38, yPos + row * 13, ((ContainerInscriptionTable) this.f_97732_).getCurrentShape().getValueWithoutMultipliers(attribute));
            }
        }
        int var14 = 140;
        count = 0;
        int var15 = 84;
        if (((ContainerInscriptionTable) this.f_97732_).getCurrentComponent() != null) {
            for (UnmodifiableIterator var17 = ((ContainerInscriptionTable) this.f_97732_).getCurrentComponent().getContainedAttributes().iterator(); var17.hasNext(); count++) {
                Attribute attribute = (Attribute) var17.next();
                int row = (int) Math.floor((double) (count / 2));
                int col = count % 2;
                this.drawAttributeValue(pGuiGraphics, var14 + col * 38, var15 + row * 13, ((ContainerInscriptionTable) this.f_97732_).getCurrentComponent().getValueWithoutMultipliers(attribute));
            }
        }
        for (int i = 0; i < 3; i++) {
            if (((ContainerInscriptionTable) this.f_97732_).getCurrentModifier(i) != null) {
                String s = I18n.get(((ContainerInscriptionTable) this.f_97732_).getCurrentModifier(i).getRegistryName().toString());
                pGuiGraphics.drawString(this.f_96547_, s, (int) ((float) (81 + i * 37) / 0.5F), 244, 4467972, false);
            }
        }
        float summaryXPos = 202.0F;
        float summaryYPos = 33.0F;
        pGuiGraphics.drawString(this.f_96547_, I18n.get("gui.mna.complexity") + ":", (int) (summaryXPos / 0.5F), (int) (summaryYPos / 0.5F), 4467972, false);
        if (((ContainerInscriptionTable) this.f_97732_).getComplexity() <= (float) this.playerProgression.getTierMaxComplexity()) {
            pGuiGraphics.drawString(this.f_96547_, String.format("%.1f / %d", ((ContainerInscriptionTable) this.f_97732_).getComplexity(), this.playerProgression.getTierMaxComplexity()), (int) (summaryXPos / 0.5F), (int) ((summaryYPos + 5.0F) / 0.5F), 4467972, false);
        } else {
            pGuiGraphics.drawString(this.f_96547_, String.format("%.1f / %d", ((ContainerInscriptionTable) this.f_97732_).getComplexity(), this.playerProgression.getTierMaxComplexity()), (int) (summaryXPos / 0.5F), (int) ((summaryYPos + 5.0F) / 0.5F), ChatFormatting.DARK_RED.getColor(), false);
        }
        pGuiGraphics.drawString(this.f_96547_, I18n.get("gui.mna.mana_cost") + ":", (int) (summaryXPos / 0.5F), (int) ((summaryYPos + 13.0F) / 0.5F), 4467972, false);
        pGuiGraphics.drawString(this.f_96547_, String.format("%.1f", ((ContainerInscriptionTable) this.f_97732_).getManaCost(this.f_96541_.player)), (int) (summaryXPos / 0.5F), (int) ((summaryYPos + 18.0F) / 0.5F), 4467972, false);
        if (((ContainerInscriptionTable) this.f_97732_).getCraftTicks() <= 0) {
            RenderSystem.enableBlend();
            pGuiGraphics.blit(GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, (int) ((summaryXPos + 3.0F) / 0.5F), (int) ((summaryYPos + 25.0F) / 0.5F), 29.0F, 20.0F, 16, 16, 128, 128);
            pGuiGraphics.blit(GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, (int) ((summaryXPos + 18.0F) / 0.5F), (int) ((summaryYPos + 25.0F) / 0.5F), 45.0F, 20.0F, 16, 16, 128, 128);
            pGuiGraphics.blit(GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, (int) ((summaryXPos + 33.0F) / 0.5F), (int) ((summaryYPos + 25.0F) / 0.5F), 61.0F, 20.0F, 16, 16, 128, 128);
            pGuiGraphics.drawString(this.f_96547_, String.format("x%d", ((ContainerInscriptionTable) this.f_97732_).getRequiredInk()), (int) ((summaryXPos + 4.0F) / 0.5F), (int) ((summaryYPos + 35.0F) / 0.5F), 4467972, false);
            pGuiGraphics.drawString(this.f_96547_, String.format("x%d", ((ContainerInscriptionTable) this.f_97732_).getRequiredPaper()), (int) ((summaryXPos + 19.0F) / 0.5F), (int) ((summaryYPos + 35.0F) / 0.5F), 4467972, false);
            pGuiGraphics.drawString(this.f_96547_, String.format("x%d", ((ContainerInscriptionTable) this.f_97732_).getRequiredAsh()), (int) ((summaryXPos + 34.0F) / 0.5F), (int) ((summaryYPos + 35.0F) / 0.5F), 4467972, false);
            int offsetY = 44;
            if (this.craftMessage != null) {
                for (FormattedText prop : this.f_96547_.getSplitter().splitLines(this.craftMessage.getString(), 95, Style.EMPTY)) {
                    pGuiGraphics.drawString(this.f_96547_, prop.getString(), (int) (summaryXPos / 0.5F), (int) ((summaryYPos + (float) offsetY) / 0.5F), ChatFormatting.DARK_PURPLE.getColor(), false);
                    offsetY += 5;
                }
                offsetY += 5;
            }
            int var27 = 50;
            pGuiGraphics.drawString(this.f_96547_, I18n.get("gui.mna.spell_reagents"), (int) ((summaryXPos - 2.0F) / 0.5F), (int) ((summaryYPos + (float) var27 + 22.0F) / 0.5F), 4467972, false);
        }
        pGuiGraphics.pose().popPose();
        if (((ContainerInscriptionTable) this.f_97732_).getCraftTicks() > 0) {
            float pct = (float) ((ContainerInscriptionTable) this.f_97732_).getCraftTicksConsumed() / (float) ((ContainerInscriptionTable) this.f_97732_).getCraftTicks();
            if (pct > 0.0F) {
                pGuiGraphics.blit(GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, 110, 6, 0.0F, 0.0F, (int) (115.0F * pct), 20, 128, 128);
            }
        }
    }

    private void drawAttributeValue(GuiGraphics pGuiGraphics, int x, int y, float value) {
        String valueString = String.format("%.1f", value);
        pGuiGraphics.drawString(this.f_96547_, valueString, (int) ((float) x / 0.5F), (int) ((float) y / 0.5F), 4467972, false);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = this.f_97735_;
        int j = this.f_97736_;
        pGuiGraphics.blit(GuiTextures.WizardLab.INSCRIPTION_TABLE, i, j, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if (this.spellPartList.isScrolling()) {
            return this.spellPartList.m_7979_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
        } else {
            return this.reagentList.isScrolling() ? this.reagentList.m_7979_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_) : super.m_7979_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
        }
    }
}