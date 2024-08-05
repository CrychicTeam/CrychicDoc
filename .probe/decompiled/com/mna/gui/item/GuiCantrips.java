package com.mna.gui.item;

import com.mna.ManaAndArtifice;
import com.mna.api.cantrips.ICantrip;
import com.mna.api.capabilities.IPlayerCantrip;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.sound.SFX;
import com.mna.cantrips.CantripRegistry;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.gui.containers.item.ContainerCantrips;
import com.mna.items.ItemInit;
import com.mna.items.manaweaving.ItemManaweaverWand;
import com.mna.network.ClientMessageDispatcher;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.tools.render.GuiRenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

public class GuiCantrips extends AbstractContainerScreen<ContainerCantrips> {

    ArrayList<ManaweavingPattern> playerKnownPatterns;

    IPlayerProgression progression;

    IPlayerMagic magic;

    int numPages;

    int page;

    ICantrip selectedCantrip;

    ICantrip hoveredCantrip;

    Point[] buttonPositions = new Point[] { new Point(94, 0), new Point(130, 0), new Point(166, 0), new Point(94, 39), new Point(130, 39), new Point(166, 39) };

    Point[] cantripPositions;

    ImageButton[] adjustButtons = new ImageButton[6];

    HashMap<ResourceLocation, Integer[]> cantripPatterns;

    int patternSpacing = 13;

    public GuiCantrips(ContainerCantrips screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
        this.f_97726_ = 194;
        this.f_97727_ = 256;
    }

    @Override
    protected void init() {
        super.init();
        this.playerKnownPatterns = new ArrayList();
        this.progression = (IPlayerProgression) this.f_96541_.player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        this.magic = (IPlayerMagic) this.f_96541_.player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        this.f_96541_.level.getRecipeManager().<CraftingContainer, ManaweavingPattern>getAllRecipesFor(RecipeInit.MANAWEAVING_PATTERN_TYPE.get()).stream().forEach(p -> {
            if (p.getTier() <= this.progression.getTier()) {
                this.playerKnownPatterns.add(p);
            }
        });
        this.cantripPatterns = new HashMap();
        RecipeManager clientRecipeManager = this.f_96541_.level.getRecipeManager();
        CantripRegistry.INSTANCE.getCantrips(this.f_96541_.player).forEach(cantrip -> {
            if (cantrip.getTier() <= this.progression.getTier()) {
                this.cantripPatterns.put(cantrip.getId(), new Integer[3]);
                for (int j = 0; j < 3; j++) {
                    Optional<IPlayerCantrip> playerCantrip = this.magic.getCantripData().getCantrip(cantrip.getId());
                    Optional<? extends Recipe<?>> pattern = clientRecipeManager.byKey(((IPlayerCantrip) playerCantrip.get()).getPattern(j));
                    if (pattern.isPresent() && pattern.get() instanceof ManaweavingPattern) {
                        ((Integer[]) this.cantripPatterns.get(cantrip.getId()))[j] = pattern != null ? this.playerKnownPatterns.indexOf(pattern.get()) : -1;
                    } else {
                        ((Integer[]) this.cantripPatterns.get(cantrip.getId()))[j] = -1;
                    }
                }
            }
        });
        this.numPages = (int) Math.ceil((double) (this.cantripPatterns.size() / 8));
        if (this.cantripPatterns.size() % 8 == 0) {
            this.numPages--;
        }
        this.setupPage(0);
    }

    private List<ICantrip> getCantripsForPage(int page) {
        if (page > this.numPages) {
            return new ArrayList();
        } else {
            int skip = page * this.cantripPositions.length;
            return (List<ICantrip>) (skip >= this.cantripPatterns.size() ? new ArrayList() : (List) CantripRegistry.INSTANCE.getCantrips(this.f_96541_.player).stream().skip((long) skip).limit(8L).collect(Collectors.toList()));
        }
    }

    private void setupPage(int page) {
        this.page = page;
        this.m_169413_();
        int i = this.f_97735_;
        int j = this.f_97736_;
        if (this.page > 0) {
            this.m_142416_(new GuiCantrips.PaperImageButton(i + 178, j + 68, 14, 9, 242, 0, 9, GuiTextures.Items.CANTRIPS, 256, 256, button -> this.setupPage(this.page - 1)));
        }
        if (this.page < this.numPages) {
            this.m_142416_(new GuiCantrips.PaperImageButton(i + 178, j + 154, 14, 9, 242, 20, 9, GuiTextures.Items.CANTRIPS, 256, 256, button -> this.setupPage(this.page + 1)));
        }
        this.initRowSelectionButtons();
    }

    @Override
    public void removed() {
        super.removed();
        this.cantripPatterns.entrySet().forEach(data -> {
            ArrayList<ResourceLocation> patternIDs = new ArrayList();
            for (int j = 0; j < ((Integer[]) data.getValue()).length; j++) {
                int patternIndex = ((Integer[]) data.getValue())[j];
                if (patternIndex < 0 || patternIndex >= this.playerKnownPatterns.size()) {
                    break;
                }
                patternIDs.add(((ManaweavingPattern) this.playerKnownPatterns.get(patternIndex)).m_6423_());
            }
            this.magic.getCantripData().setPattern((ResourceLocation) data.getKey(), patternIDs);
        });
        ClientMessageDispatcher.sendCantripUpdateMessage(this.magic);
    }

    private void initRowSelectionButtons() {
        if (this.selectedCantrip != null) {
            int xOffset = 0;
            for (int i = 0; i < 3; i++) {
                int count = i;
                this.m_142416_(new ImageButton(this.f_97735_ + 94 + xOffset, this.f_97736_ + 0, 14, 9, 242, 0, 9, GuiTextures.Items.CANTRIPS, 256, 256, button -> {
                    int idx = ((Integer[]) this.cantripPatterns.get(this.selectedCantrip.getId()))[count] - 1;
                    if (idx < (count < 2 ? 0 : -1)) {
                        idx = this.playerKnownPatterns.size() - 1;
                    }
                    ((Integer[]) this.cantripPatterns.get(this.selectedCantrip.getId()))[count] = idx;
                }));
                this.m_142416_(new ImageButton(this.f_97735_ + 94 + xOffset, this.f_97736_ + 39, 14, 9, 242, 20, 9, GuiTextures.Items.CANTRIPS, 256, 256, button -> {
                    int idx = ((Integer[]) this.cantripPatterns.get(this.selectedCantrip.getId()))[count] + 1;
                    if (idx >= this.playerKnownPatterns.size()) {
                        idx = count < 2 ? 0 : -1;
                    }
                    ((Integer[]) this.cantripPatterns.get(this.selectedCantrip.getId()))[count] = idx;
                }));
                xOffset += 36;
            }
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.hoveredCantrip = null;
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        MutableObject<ICantrip> tt = new MutableObject(null);
        RenderSystem.setShader(GameRenderer::m_172817_);
        int topLeftCornerX = (this.f_96543_ - this.f_97726_) / 2;
        int topLeftCornerY = (this.f_96544_ - this.f_97727_) / 2;
        ItemStack mainHand = ManaAndArtifice.instance.proxy.getClientPlayer().m_21205_();
        ItemStack offHand = ManaAndArtifice.instance.proxy.getClientPlayer().m_21206_();
        ItemStack wandStack = mainHand.getItem() instanceof ItemManaweaverWand ? mainHand : offHand;
        if (wandStack.getItem() == ItemInit.MANAWEAVER_WAND.get()) {
            pGuiGraphics.blit(GuiTextures.Items.CANTRIPS_WANDS, topLeftCornerX, topLeftCornerY, 40, 0, 21, 256);
        } else if (wandStack.getItem() == ItemInit.MANAWEAVER_WAND_ADVANCED.get()) {
            pGuiGraphics.blit(GuiTextures.Items.CANTRIPS_WANDS, topLeftCornerX, topLeftCornerY, 0, 0, 33, 256);
        } else if (wandStack.getItem() == ItemInit.MANAWEAVER_WAND_IMPROVISED.get()) {
            pGuiGraphics.blit(GuiTextures.Items.CANTRIPS_WANDS, topLeftCornerX, topLeftCornerY, 68, 0, 34, 256);
        }
        pGuiGraphics.blit(GuiTextures.Items.CANTRIPS, topLeftCornerX, topLeftCornerY, 0, 0, 194, 256);
        if (this.selectedCantrip != null && ((ContainerCantrips) this.f_97732_).cantripHasSlot(this.selectedCantrip.getId())) {
            pGuiGraphics.blit(GuiTextures.Items.CANTRIPS, this.f_97735_ + 7, this.f_97736_ + 31, 38, 70, 20, 20);
        }
        this.calculateCantripPositions();
        MutableInt index = new MutableInt(0);
        this.getCantripsForPage(this.page).stream().forEach(cantrip -> {
            Point pt = this.cantripPositions[index.getAndIncrement()];
            if (this.renderCantripBGAt(cantrip, pt.x, pt.y, mouseX, mouseY, pGuiGraphics, false)) {
                tt.setValue(cantrip);
                this.hoveredCantrip = cantrip;
            }
            this.drawManaweavePatternCombo(pGuiGraphics, pt.x + 29, pt.y + 3, (Integer[]) this.cantripPatterns.get(cantrip.getId()));
        });
        if (this.selectedCantrip != null) {
            if (this.renderCantripBGAt(this.selectedCantrip, this.f_97735_ + 45, this.f_97736_ + 8, mouseX, mouseY, pGuiGraphics, true)) {
                tt.setValue(this.selectedCantrip);
            }
            int prevSpacing = this.patternSpacing;
            this.patternSpacing = 36;
            this.drawManaweavePatternCombo(pGuiGraphics, this.f_97735_ + 110, this.f_97736_ + 13, (Integer[]) this.cantripPatterns.get(this.selectedCantrip.getId()), 0.155F);
            this.patternSpacing = prevSpacing;
            String selectedName = Component.translatable("cantrip." + this.selectedCantrip.getId().toString().replace(":", ".")).withStyle(ChatFormatting.LIGHT_PURPLE).getString();
            pGuiGraphics.drawString(this.f_96547_, selectedName, this.f_97735_ + 40, this.f_97736_ + 53, 16777215, false);
        }
        if (tt.getValue() != null) {
            this.renderCantripTooltip(pGuiGraphics, (ICantrip) tt.getValue(), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int p_230451_2_, int p_230451_3_) {
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (this.hoveredCantrip != null) {
            this.selectedCantrip = this.hoveredCantrip;
            ((ContainerCantrips) this.f_97732_).enableSlotsForCantrip(this.selectedCantrip.getId());
            this.setupPage(this.page);
        }
        return super.mouseClicked(double0, double1, int2);
    }

    private void calculateCantripPositions() {
        int topLeftCornerX = (this.f_96543_ - this.f_97726_) / 2;
        int topLeftCornerY = (this.f_96544_ - this.f_97727_) / 2;
        this.cantripPositions = new Point[] { new Point(topLeftCornerX + 40, topLeftCornerY + 72), new Point(topLeftCornerX + 40, topLeftCornerY + 96), new Point(topLeftCornerX + 40, topLeftCornerY + 120), new Point(topLeftCornerX + 40, topLeftCornerY + 144), new Point(topLeftCornerX + 110, topLeftCornerY + 72), new Point(topLeftCornerX + 110, topLeftCornerY + 96), new Point(topLeftCornerX + 110, topLeftCornerY + 120), new Point(topLeftCornerX + 110, topLeftCornerY + 144) };
    }

    private boolean renderCantripBGAt(ICantrip cantrip, int x, int y, int mouseX, int mouseY, GuiGraphics pGuiGraphics, boolean large) {
        boolean hovered = false;
        if (this.progression.getTier() >= cantrip.getTier()) {
            int size = large ? 32 : 16;
            pGuiGraphics.blit(cantrip.getIcon(), x, y, 0, 0.0F, 0.0F, size, size, size, size);
            if (this.isMouseWithin(x, y, large ? x + size : x + size + 42, y + size, mouseX, mouseY)) {
                hovered = true;
            }
        }
        return hovered;
    }

    private boolean isMouseWithin(int x, int y, int x2, int y2, int mouseX, int mouseY) {
        return x <= mouseX && x2 >= mouseX && y <= mouseY && y2 >= mouseY;
    }

    private void renderCantripTooltip(GuiGraphics pGuiGraphics, ICantrip cantrip, int mouseX, int mouseY) {
        List<Component> ttLines = new ArrayList();
        ttLines.add(Component.translatable("cantrip." + cantrip.getId().toString().replace(":", ".")).withStyle(ChatFormatting.LIGHT_PURPLE));
        ttLines.add(Component.translatable("cantrip.mna.tier", cantrip.getTier()).withStyle(ChatFormatting.AQUA));
        ttLines.add(Component.translatable("cantrip." + cantrip.getId().toString().replace(":", ".") + ".desc").withStyle(ChatFormatting.ITALIC));
        pGuiGraphics.renderComponentTooltip(this.f_96547_, ttLines, mouseX, mouseY);
    }

    private void drawManaweavePatternCombo(GuiGraphics pGuiGraphics, int x, int y, Integer[] patterns) {
        this.drawManaweavePatternCombo(pGuiGraphics, x, y, patterns, 0.07F);
    }

    private void drawManaweavePatternCombo(GuiGraphics pGuiGraphics, int x, int y, Integer[] patterns, float scale) {
        if (patterns != null) {
            for (int i = 0; i < patterns.length; i++) {
                Integer index = patterns[i];
                if (index != null && index >= 0) {
                    ManaweavingPattern p = (ManaweavingPattern) this.playerKnownPatterns.get(index);
                    GuiRenderUtils.renderManaweavePattern(pGuiGraphics, (int) ((float) x / scale), (int) ((float) y / scale), scale, p);
                }
                x += this.patternSpacing;
            }
        }
    }

    public class PaperImageButton extends ImageButton {

        private Component tooltip;

        public PaperImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, Button.OnPress onPressIn) {
            super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);
        }

        public PaperImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int p_i51136_9_, int p_i51136_10_, Button.OnPress onPressIn, Component textIn) {
            super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, p_i51136_9_, p_i51136_10_, onPressIn, textIn);
        }

        public PaperImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int p_i51135_9_, int p_i51135_10_, Button.OnPress onPressIn) {
            super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, p_i51135_9_, p_i51135_10_, onPressIn);
        }

        public GuiCantrips.PaperImageButton setTooltip(Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            super.renderWidget(pGuiGraphics, mouseX, mouseY, partialTicks);
            if (this.f_93623_ && this.f_93624_ && this.m_198029_() && this.tooltip != null) {
                GuiGuideBook.currentTooltip.add(this.tooltip);
            }
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            soundHandler.play(SimpleSoundInstance.forUI(SFX.Gui.PAGE_FLIP, (float) (0.8 + Math.random() * 0.4)));
        }
    }
}