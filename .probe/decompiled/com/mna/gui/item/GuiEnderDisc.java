package com.mna.gui.item;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiJEIDisable;
import com.mna.gui.containers.item.ContainerEnderDisc;
import com.mna.items.ItemInit;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class GuiEnderDisc extends GuiJEIDisable<ContainerEnderDisc> {

    private int[] rune_indices = new int[8];

    private int remove_index_hover = -1;

    private ArrayList<GuiEnderDisc.EnderDiscTabButton> index_buttons;

    EditBox nameBox;

    Component nameValue;

    ItemStack dragStack = ItemStack.EMPTY;

    private ItemStack[] rune_stacks = new ItemStack[] { new ItemStack(Items.AIR), new ItemStack(ItemInit.STONE_RUNE_BLACK.get()), new ItemStack(ItemInit.STONE_RUNE_BLUE.get()), new ItemStack(ItemInit.STONE_RUNE_BROWN.get()), new ItemStack(ItemInit.STONE_RUNE_CYAN.get()), new ItemStack(ItemInit.STONE_RUNE_GRAY.get()), new ItemStack(ItemInit.STONE_RUNE_GREEN.get()), new ItemStack(ItemInit.STONE_RUNE_LIGHT_BLUE.get()), new ItemStack(ItemInit.STONE_RUNE_LIGHT_GRAY.get()), new ItemStack(ItemInit.STONE_RUNE_LIME.get()), new ItemStack(ItemInit.STONE_RUNE_MAGENTA.get()), new ItemStack(ItemInit.STONE_RUNE_ORANGE.get()), new ItemStack(ItemInit.STONE_RUNE_PINK.get()), new ItemStack(ItemInit.STONE_RUNE_PURPLE.get()), new ItemStack(ItemInit.STONE_RUNE_RED.get()), new ItemStack(ItemInit.STONE_RUNE_WHITE.get()), new ItemStack(ItemInit.STONE_RUNE_YELLOW.get()) };

    public GuiEnderDisc(ContainerEnderDisc screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 255;
        this.f_97727_ = 191;
    }

    @Override
    protected void init() {
        super.m_7856_();
        this.index_buttons = new ArrayList();
        int x = this.f_97735_;
        int y = this.f_97736_;
        x = this.f_97735_ + 92;
        y = this.f_97736_ + 124;
        for (int i = 0; i < 8; i++) {
            this.addIndexButton(x, y, i);
            x += 20;
        }
        this.m_142416_(new GuiEnderDisc.EnderDiscDimensionButton(this.f_97735_ + 61, this.f_97736_ + 142, btn -> this.f_96541_.gameMode.handleInventoryButtonClick(((ContainerEnderDisc) this.f_97732_).f_38840_, 0)));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 141, this.f_97736_ + 40, this.rune_stacks[12], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 161, this.f_97736_ + 40, this.rune_stacks[3], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 181, this.f_97736_ + 40, this.rune_stacks[14], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 201, this.f_97736_ + 40, this.rune_stacks[11], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 221, this.f_97736_ + 40, this.rune_stacks[16], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 131, this.f_97736_ + 60, this.rune_stacks[10], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 151, this.f_97736_ + 60, this.rune_stacks[15], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 171, this.f_97736_ + 60, this.rune_stacks[8], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 191, this.f_97736_ + 60, this.rune_stacks[5], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 211, this.f_97736_ + 60, this.rune_stacks[1], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 231, this.f_97736_ + 60, this.rune_stacks[9], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 141, this.f_97736_ + 80, this.rune_stacks[13], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 161, this.f_97736_ + 80, this.rune_stacks[2], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 181, this.f_97736_ + 80, this.rune_stacks[7], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 201, this.f_97736_ + 80, this.rune_stacks[4], this::onGlyphButtonClicked));
        this.m_142416_(new GuiEnderDisc.GlyphButton(this.f_97735_ + 221, this.f_97736_ + 80, this.rune_stacks[6], this::onGlyphButtonClicked));
        this.m_142416_(new ImageButton(this.f_97735_ + 84, this.f_97736_ + 161, 7, 7, 249, 242, 7, GuiTextures.Items.ENDER_DISC, b -> {
            ResourceLocation empty = ForgeRegistries.ITEMS.getKey(Items.AIR);
            for (int ix = 0; ix < 8; ix++) {
                ((ContainerEnderDisc) this.f_97732_).setPattern(empty, ix);
            }
            this.setCurPattern(((ContainerEnderDisc) this.f_97732_).getCurPatternIndex());
        }));
        this.nameBox = new EditBox(this.f_96541_.font, this.f_97735_ + 98, this.f_97736_ + 146, 144, 16, this.nameValue);
        this.nameBox.setMaxLength(23);
        this.nameBox.setResponder(this::patternNameChanged);
        this.setCurPattern(((ContainerEnderDisc) this.f_97732_).getCurPatternIndex());
        this.nameBox.setValue(((ContainerEnderDisc) this.f_97732_).getCurPatternName());
        this.m_142416_(this.nameBox);
        this.nameBox.setCanLoseFocus(false);
        this.nameBox.setFocused(true);
        this.m_7522_(this.nameBox);
    }

    public void onGlyphButtonClicked(Button button) {
        if (button instanceof GuiEnderDisc.GlyphButton gb) {
            ItemStack stack = gb.getStack();
            this.dragStack = stack.copy();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.m_7379_();
            return true;
        } else if (this.nameBox.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            return this.nameBox.m_93696_() && this.nameBox.isVisible() && keyCode != 256 ? true : super.m_7933_(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.remove_index_hover != -1) {
            ResourceLocation empty = ForgeRegistries.ITEMS.getKey(Items.AIR);
            if (!((ResourceLocation) ((ContainerEnderDisc) this.f_97732_).getCurPattern().get(this.remove_index_hover)).equals(empty)) {
                ((ContainerEnderDisc) this.f_97732_).setPattern(empty, this.remove_index_hover);
                this.setCurPattern(((ContainerEnderDisc) this.f_97732_).getCurPatternIndex());
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }
        if (button == 1 && this.nameBox.isMouseOver(mouseX, mouseY)) {
            this.nameBox.setValue("");
        }
        super.m_6375_(mouseX, mouseY, button);
        this.m_7522_(this.nameBox);
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!this.dragStack.isEmpty()) {
            if (mouseY - (double) this.f_97736_ >= 167.0 && mouseY - (double) this.f_97736_ <= 183.0) {
                int index = (int) Math.floor(mouseX - (double) this.f_97735_ - 100.0) / 18;
                if (index >= 0 && index < 8) {
                    ((ContainerEnderDisc) this.f_97732_).setPattern(ForgeRegistries.ITEMS.getKey(Items.AIR), index);
                    this.setCurPattern(((ContainerEnderDisc) this.f_97732_).getCurPatternIndex());
                }
            }
            this.m_6702_().forEach(child -> {
                if (child instanceof GuiEnderDisc.GlyphButton && child.isMouseOver(mouseX, mouseY) && ((GuiEnderDisc.GlyphButton) child).getStack().getItem() == this.dragStack.getItem()) {
                    int indexx = 0;
                    while (indexx < this.rune_indices.length && this.rune_indices[indexx] != 0) {
                        indexx++;
                    }
                    ((ContainerEnderDisc) this.f_97732_).setPattern(ForgeRegistries.ITEMS.getKey(this.dragStack.getItem()), indexx);
                    this.setCurPattern(((ContainerEnderDisc) this.f_97732_).getCurPatternIndex());
                }
            });
            this.dragStack = ItemStack.EMPTY;
        }
        return super.m_6348_(mouseX, mouseY, button);
    }

    private void patternNameChanged(String newName) {
        ((ContainerEnderDisc) this.f_97732_).setName(newName);
    }

    private void setCurPattern(int index) {
        ((ContainerEnderDisc) this.f_97732_).changePatternIndex(index);
        ArrayList<ResourceLocation> curPattern = ((ContainerEnderDisc) this.f_97732_).getCurPattern();
        for (int i = 0; i < this.rune_indices.length && curPattern.size() > i; i++) {
            ResourceLocation rLoc = (ResourceLocation) curPattern.get(i);
            int count = 0;
            for (ItemStack stack : this.rune_stacks) {
                if (ForgeRegistries.ITEMS.getKey(stack.getItem()).equals(rLoc)) {
                    this.rune_indices[i] = count;
                    break;
                }
                count++;
            }
        }
        ((GuiEnderDisc.EnderDiscTabButton) this.index_buttons.get(index)).setHighlighted(true);
        this.nameBox.setValue(((ContainerEnderDisc) this.f_97732_).getCurPatternName());
    }

    private void addIndexButton(int x, int y, int index) {
        this.index_buttons.add((GuiEnderDisc.EnderDiscTabButton) this.m_142416_(new GuiEnderDisc.EnderDiscTabButton(x, y, 18 * index, 220, b -> {
            this.setCurPattern(index);
            this.index_buttons.forEach(ib -> ib.setHighlighted(b == ib));
        }, String.format("%d", index + 1))));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (!this.dragStack.isEmpty()) {
            pGuiGraphics.renderItem(this.dragStack, mouseX - 8, mouseY - 8);
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(GuiTextures.Items.ENDER_DISC, i, j, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        this.remove_index_hover = -1;
        for (int i = 0; i < this.rune_indices.length; i++) {
            this.renderActiveGlyph(pGuiGraphics, 100 + i * 18, 167, mouseX, mouseY, i);
        }
    }

    private void renderActiveGlyph(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, int index) {
        pGuiGraphics.renderItem(this.rune_stacks[this.rune_indices[index]], x, y);
        if (mouseX >= this.f_97735_ + x && mouseX <= this.f_97735_ + x + 16 && mouseY >= this.f_97736_ + y && mouseY <= this.f_97736_ + y + 16) {
            this.remove_index_hover = index;
        }
    }

    class EnderDiscDimensionButton extends ImageButton {

        private final int v = 244;

        public EnderDiscDimensionButton(int xIn, int yIn, Button.OnPress onPressIn) {
            super(xIn, yIn, 12, 12, 0, 0, 0, GuiTextures.Items.ENDER_DISC, 256, 256, onPressIn, null);
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            int u = 237;
            String var6 = ((ContainerEnderDisc) GuiEnderDisc.this.f_97732_).getCurDimension().toString();
            switch(var6) {
                case "mna:dimension_current":
                    u = 189;
                    break;
                case "minecraft:overworld":
                    u = 201;
                    break;
                case "minecraft:the_nether":
                    u = 213;
                    break;
                case "minecraft:the_end":
                    u = 225;
            }
            RenderSystem.enableDepthTest();
            pGuiGraphics.blit(GuiTextures.Items.ENDER_DISC, this.m_252754_(), this.m_252907_(), (float) u, 244.0F, this.f_93618_, this.f_93619_, 256, 256);
        }
    }

    class EnderDiscTabButton extends ImageButton {

        private boolean highlighted = false;

        private String text;

        private final int TEXT_COLOR = FastColor.ARGB32.color(255, 40, 40, 40);

        public EnderDiscTabButton(int xIn, int yIn, int uIn, int vIn, Button.OnPress onPressIn, String text) {
            super(xIn, yIn, 18, 18, uIn, vIn, 18, GuiTextures.Items.ENDER_DISC, 256, 256, onPressIn);
            this.text = text;
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            super.renderWidget(pGuiGraphics, mouseX, mouseY, partialTicks);
            pGuiGraphics.drawString(GuiEnderDisc.this.f_96547_, this.text, (int) ((float) (this.m_252754_() + this.f_93618_ / 2 - GuiEnderDisc.this.f_96547_.width(this.text) / 2) + 0.5F), (int) ((float) (this.m_252907_() + this.f_93619_ / 2 - 9 / 2) + 0.5F), this.TEXT_COLOR, false);
        }

        @Override
        public boolean isHoveredOrFocused() {
            return this.highlighted;
        }

        public void setHighlighted(boolean highlighted) {
            this.highlighted = highlighted;
        }
    }

    class GlyphButton extends Button {

        private final ItemStack renderStack;

        public GlyphButton(int x, int y, ItemStack stack, Button.OnPress clickHandler) {
            super(x, y, 16, 16, Component.literal(""), clickHandler, f_252438_);
            this.renderStack = stack;
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            pGuiGraphics.renderItem(this.renderStack, this.m_252754_(), this.m_252907_());
        }

        public ItemStack getStack() {
            return this.renderStack;
        }
    }
}