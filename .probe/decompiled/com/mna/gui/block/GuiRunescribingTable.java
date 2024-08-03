package com.mna.gui.block;

import com.mna.ManaAndArtifice;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.sound.SFX;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerRunescribingTable;
import com.mna.guide.recipe.RecipeRunescribing;
import com.mna.items.ItemInit;
import com.mna.recipes.runeforging.RunescribingRecipe;
import com.mna.tools.math.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.mutable.MutableInt;

public class GuiRunescribingTable extends AbstractContainerScreen<ContainerRunescribingTable> {

    public static final int LEFT_PANEL_WIDTH = 198;

    public static final int RIGHT_PANEL_WIDTH = 111;

    public static final int PANEL_PADDING = 5;

    public static final int PANEL_HEIGHT = 238;

    long mutex_h = 0L;

    long mutex_v = 0L;

    RecipeRunescribing recipe;

    boolean hasRequiredItems;

    public GuiRunescribingTable(ContainerRunescribingTable screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 314;
        this.f_97727_ = 256;
        this.recipe = new RecipeRunescribing(324, 70);
    }

    @Override
    protected void init() {
        super.init();
        this.reinitializeButtons();
    }

    private void reinitializeButtons() {
        this.m_169413_();
        this.mutex_h = ((ContainerRunescribingTable) this.f_97732_).getHMutex();
        this.mutex_v = ((ContainerRunescribingTable) this.f_97732_).getVMutex();
        if (((ContainerRunescribingTable) this.f_97732_).hasRequiredItems()) {
            long count = 0L;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 8; j++) {
                    long loopCount = count;
                    this.m_142416_(new GuiRunescribingTable.ChiselImageButton(this.f_97735_ + 49 + i * 16, this.f_97736_ + 13 + j * 16, 4, 16, 215, 224, 16, GuiTextures.WizardLab.RUNESCRIBING_TABLE, 256, 256, (this.mutex_v & 1L << (int) loopCount) != 0L, button -> {
                        boolean value = ((GuiRunescribingTable.ChiselImageButton) button).getValue();
                        if (!value || ((ContainerRunescribingTable) this.f_97732_).canUndo(ManaAndArtifice.instance.proxy.getClientPlayer())) {
                            ((GuiRunescribingTable.ChiselImageButton) button).toggle();
                            if (!value) {
                                this.mutex_v |= 1L << (int) loopCount;
                            } else {
                                this.mutex_v &= ~(1L << (int) loopCount);
                            }
                            this.onMutexChanged(this.f_96541_.player, value);
                        }
                    }));
                    this.m_142416_(new GuiRunescribingTable.ChiselImageButton(this.f_97735_ + 36 + j * 16, this.f_97736_ + 27 + i * 16, 16, 4, 224, 211, 4, GuiTextures.WizardLab.RUNESCRIBING_TABLE, 256, 256, (this.mutex_h & 1L << (int) loopCount) != 0L, button -> {
                        boolean value = ((GuiRunescribingTable.ChiselImageButton) button).getValue();
                        if (!value || ((ContainerRunescribingTable) this.f_97732_).canUndo(ManaAndArtifice.instance.proxy.getClientPlayer())) {
                            ((GuiRunescribingTable.ChiselImageButton) button).toggle();
                            if (!value) {
                                this.mutex_h |= 1L << (int) loopCount;
                            } else {
                                this.mutex_h &= ~(1L << (int) loopCount);
                            }
                            this.onMutexChanged(this.f_96541_.player, value);
                        }
                    }));
                    count++;
                }
            }
            this.setGuideRecipe();
        }
    }

    private void setGuideRecipe() {
        if (this.recipe != null) {
            ItemStack stack = ((ContainerRunescribingTable) this.f_97732_).m_38853_(3).getItem();
            if (!stack.isEmpty()) {
                RunescribingRecipe r = ItemInit.RECIPE_SCRAP_RUNESCRIBING.get().getRecipe(stack, this.f_96541_.level);
                if (r != null) {
                    this.recipe.init(new ResourceLocation[] { r.m_6423_() });
                    this.recipe.disablePaperBackground();
                    this.recipe.disableLabels();
                    this.recipe.disableOutputRender();
                    this.recipe.f_93623_ = true;
                    this.recipe.f_93624_ = true;
                    return;
                }
            }
            this.recipe.f_93623_ = false;
            this.recipe.f_93624_ = false;
        }
    }

    private void onMutexChanged(Player player, boolean isRemove) {
        if (player != null) {
            player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> ((ContainerRunescribingTable) this.f_97732_).writeMutex(this.mutex_h, this.mutex_v, player, p.getTier(), isRemove));
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
        boolean requiredItems = ((ContainerRunescribingTable) this.f_97732_).hasRequiredItems();
        if (!requiredItems && this.m_6702_().size() > 0 || requiredItems && !this.hasRequiredItems) {
            this.reinitializeButtons();
        }
        this.hasRequiredItems = requiredItems;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        ItemStack xpBottleStack = new ItemStack(Items.EXPERIENCE_BOTTLE);
        pGuiGraphics.renderItem(xpBottleStack, 10, 79);
        pGuiGraphics.pose().pushPose();
        float scale = 0.479F;
        pGuiGraphics.pose().scale(scale, scale, scale);
        pGuiGraphics.pose().translate(108.0F, -90.0F, 0.0F);
        this.setGuideRecipe();
        if (this.recipe != null && this.recipe.f_93623_) {
            this.recipe.m_88315_(pGuiGraphics, mouseX, mouseY, 0.0F);
        }
        pGuiGraphics.pose().popPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (((ContainerRunescribingTable) this.f_97732_).hasPattern()) {
            long count = 0L;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((this.mutex_h & 1L << (int) count) != 0L) {
                        pGuiGraphics.blit(GuiTextures.WizardLab.RUNESCRIBING_TABLE, 34 + j * 16, 27 + i * 16, 224, 215, 16, 4);
                    }
                    if ((this.mutex_v & 1L << (int) count) != 0L) {
                        pGuiGraphics.blit(GuiTextures.WizardLab.RUNESCRIBING_TABLE, 49 + i * 16, 13 + j * 16, 215, 240, 4, 16);
                    }
                    count++;
                }
            }
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    pGuiGraphics.blit(GuiTextures.WizardLab.RUNESCRIBING_TABLE, 34 + x * 16, 12 + y * 16, 2, 2, 250.0F, 208.0F, 6, 6, 256, 256);
                }
            }
            ItemStack stack = ((ContainerRunescribingTable) this.f_97732_).m_38853_(3).getItem();
            if (!stack.isEmpty() && this.recipe != null) {
                int tier = this.recipe.getTier();
                MutableInt playerTier = new MutableInt(0);
                this.f_96541_.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
                int color = tier <= playerTier.getValue() ? FastColor.ARGB32.color(255, 0, 128, 0) : FastColor.ARGB32.color(255, 255, 0, 0);
                Component name = Component.translatable(this.recipe.getOutputTranslationKey());
                Component tierPrompt = Component.translatable("gui.mna.item-tier", tier);
                String nameContents = name.getString();
                scale = 0.65F;
                int nameLen = (int) ((float) this.f_96547_.width(nameContents) * scale);
                boolean adjusted;
                for (adjusted = false; nameLen > 70; adjusted = true) {
                    nameContents = nameContents.substring(1);
                    nameLen = (int) ((float) this.f_96547_.width(nameContents) * scale);
                }
                if (adjusted) {
                    nameContents = "..." + nameContents;
                    nameLen = (int) ((float) this.f_96547_.width(nameContents) * scale);
                }
                int tierLen = (int) ((float) this.f_96547_.width(tierPrompt) * scale);
                int textX = 219;
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().scale(scale, scale, scale);
                pGuiGraphics.drawString(this.f_96547_, nameContents, (int) ((float) (textX + 40 - nameLen / 2) / scale), (int) (115.5F / scale), FastColor.ARGB32.color(255, 255, 255, 255), false);
                pGuiGraphics.drawString(this.f_96547_, tierPrompt, (int) ((float) (textX + 40 - tierLen / 2) / scale), (int) (125.5F / scale), color, false);
                pGuiGraphics.pose().popPose();
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = this.f_97735_;
        int j = this.f_97736_;
        pGuiGraphics.blit(GuiTextures.WizardLab.RUNESCRIBING_TABLE, i, j, 0, 0, 198, 238);
        if (((ContainerRunescribingTable) this.f_97732_).m_38853_(0).getItem().isEmpty()) {
            pGuiGraphics.blit(GuiTextures.WizardLab.RUNESCRIBING_TABLE, i + 8, j + 8, 240, 0, 16, 16);
        }
        if (((ContainerRunescribingTable) this.f_97732_).m_38853_(1).getItem().isEmpty()) {
            pGuiGraphics.blit(GuiTextures.WizardLab.RUNESCRIBING_TABLE, i + 8, j + 32, 240, 17, 16, 16);
        }
        if (((ContainerRunescribingTable) this.f_97732_).hasPattern()) {
            float scale = 3.815F;
            pGuiGraphics.blit(GuiTextures.WizardLab.RUNESCRIBING_TABLE, i + 31, j + 9, (float) ((int) (220.0F * scale)), (float) ((int) (220.0F * scale)), (int) (36.0F * scale), (int) (36.0F * scale), (int) (256.0F * scale), (int) (256.0F * scale));
        } else {
            this.m_169413_();
        }
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        float xpPct = GeneralConfigValues.ExperiencePerRunescribeUndo <= 0 ? 1.0F : MathUtils.clamp01(player.isCreative() ? 1.0F : (float) player.totalExperience / (float) GeneralConfigValues.ExperiencePerRunescribeUndo);
        pGuiGraphics.blit(GuiTextures.WizardLab.RUNESCRIBING_TABLE, this.f_97735_ + 9, this.f_97736_ + 75, 236, 0, 3, (int) (20.0F * xpPct));
        pGuiGraphics.blit(GuiTextures.WizardLab.RUNESCRIBING_EXTRAS, i + 198 + 5, j, 0, 0, 111, 238);
        ItemStack stack = ((ContainerRunescribingTable) this.f_97732_).m_38853_(3).getItem();
        if (!stack.isEmpty()) {
            pGuiGraphics.blit(GuiTextures.WizardLab.RUNESCRIBING_EXTRAS, i + 198 + 5 + 17, j + 30, 178, 178, 78, 78);
        }
    }

    @Override
    protected void slotClicked(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slotIn, slotId, mouseButton, type);
        this.reinitializeButtons();
    }

    public class ChiselImageButton extends ImageButton {

        private int padding = 2;

        private boolean isSet = false;

        public ChiselImageButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int p_i51135_9_, int p_i51135_10_, boolean initiallySet, Button.OnPress onPressIn) {
            super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, p_i51135_9_, p_i51135_10_, onPressIn);
            this.isSet = initiallySet;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            if (this.f_93624_) {
                this.f_93622_ = this.isMouseOver((double) mouseX, (double) mouseY) || this.isSet;
                if (this.f_93622_) {
                    this.m_87963_(pGuiGraphics, mouseX, mouseY, partialTicks);
                }
            }
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return this.f_93618_ > this.f_93619_ ? this.f_93623_ && this.f_93624_ && mouseX >= (double) this.m_252754_() + (double) this.padding && mouseY >= (double) this.m_252907_() && mouseX < (double) (this.m_252754_() + this.f_93618_ - this.padding) && mouseY < (double) (this.m_252907_() + this.f_93619_) : this.f_93623_ && this.f_93624_ && mouseX >= (double) this.m_252754_() && mouseY >= (double) this.m_252907_() + (double) this.padding && mouseX < (double) (this.m_252754_() + this.f_93618_) && mouseY < (double) (this.m_252907_() + this.f_93619_ - this.padding);
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            soundHandler.play(SimpleSoundInstance.forUI(SFX.Gui.CHISEL, 1.0F));
        }

        public boolean getValue() {
            return this.isSet;
        }

        public void toggle() {
            this.isSet = !this.isSet;
        }
    }
}