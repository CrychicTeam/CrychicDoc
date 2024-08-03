package com.mna.gui.item;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiJEIDisable;
import com.mna.gui.containers.item.ContainerSpellName;
import com.mna.items.ItemInit;
import com.mna.items.SpellIconList;
import com.mna.items.sorcery.ItemSpell;
import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GuiSpellNaming extends GuiJEIDisable<ContainerSpellName> {

    private int page = 0;

    private int numPages = 0;

    EditBox nameBox;

    Component nameValue;

    GuiSpellNaming.ModelButton currentButton;

    ArrayList<GuiSpellNaming.ModelButton> iconButtons;

    private Button prev;

    private Button next;

    public GuiSpellNaming(ContainerSpellName screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 256;
        this.f_97727_ = 256;
        this.iconButtons = new ArrayList();
        this.numPages = (int) Math.floor((double) ((float) SpellIconList.ALL.length / 100.0F));
    }

    @Override
    protected void init() {
        super.m_7856_();
        int i = this.f_97735_;
        int j = this.f_97736_;
        this.currentButton = new GuiSpellNaming.ModelButton(i + 12, j + 7, -1, btn -> {
        });
        this.m_169394_(this.currentButton);
        this.nameBox = new EditBox(this.f_96541_.font, i + this.f_97726_ / 2 - 80, j + 7, 160, 16, this.nameValue);
        this.nameBox.setValue(((ContainerSpellName) this.f_97732_).getName());
        this.nameBox.setMaxLength(60);
        this.nameBox.setResponder(this::nameChanged);
        this.nameBox.setCanLoseFocus(false);
        this.nameBox.setFocused(true);
        this.m_7522_(this.nameBox);
        this.page = 0;
        this.m_142416_(new ImageButton(this.f_97735_ + this.f_97726_ - 28, this.f_97736_ + 7, 15, 18, 222, 7, 0, GuiTextures.Items.SPELL_CUSTOMIZE, 256, 256, button -> this.m_7379_()));
        this.prev = (Button) this.m_142416_(new ImageButton(this.f_97735_ + 9, this.f_97736_ + this.f_97727_ - 18, 9, 14, 247, 31, 14, GuiTextures.Items.SPELL_CUSTOMIZE, 256, 256, button -> {
            this.page--;
            if (this.page <= 0) {
                this.page = 0;
                this.prev.f_93623_ = false;
            }
            this.next.f_93623_ = true;
            this.initIconButtons();
            this.m_7522_(this.nameBox);
        }));
        this.next = (Button) this.m_142416_(new ImageButton(this.f_97735_ + this.f_97726_ - 17, this.f_97736_ + this.f_97727_ - 18, 9, 14, 247, 60, 14, GuiTextures.Items.SPELL_CUSTOMIZE, 256, 256, button -> {
            this.page++;
            if (this.page >= this.numPages) {
                this.page = this.numPages;
                this.next.f_93623_ = false;
            }
            this.prev.f_93623_ = true;
            this.initIconButtons();
            this.m_7522_(this.nameBox);
        }));
        this.prev.f_93623_ = false;
        i += 3;
        j += 20;
        this.initIconButtons();
        this.m_142416_(this.nameBox);
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
        if (button == 1 && this.nameBox.isMouseOver(mouseX, mouseY)) {
            this.nameBox.setValue("");
        }
        super.m_6375_(mouseX, mouseY, button);
        this.m_7522_(this.nameBox);
        return false;
    }

    private void nameChanged(String newName) {
        ((ContainerSpellName) this.f_97732_).setName(newName);
    }

    private void initIconButtons() {
        for (GuiSpellNaming.ModelButton button : this.iconButtons) {
            this.m_169411_(button);
        }
        this.iconButtons.clear();
        int x = this.f_97735_ + 26;
        int y = this.f_97736_ + 40;
        int count = 1;
        for (int i = this.page * 100; i < SpellIconList.ALL.length; i++) {
            int idx = i;
            GuiSpellNaming.ModelButton btn = new GuiSpellNaming.ModelButton(x, y, i, button -> {
                ((ContainerSpellName) this.f_97732_).setIconIndex(idx);
                ItemSpell.setCustomIcon(this.currentButton.icon, idx);
            });
            this.m_142416_(btn);
            this.iconButtons.add(btn);
            x += 21;
            if (count % 10 == 0 && count != 0) {
                x = this.f_97735_ + 26;
                y += 21;
            }
            if (count == 100) {
                break;
            }
            count++;
        }
        ItemSpell.setCustomIcon(this.currentButton.icon, ((ContainerSpellName) this.f_97732_).getIconIndex());
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.m_280273_(pGuiGraphics);
        int i = this.f_97735_;
        int j = this.f_97736_;
        int bannerWidth = 246;
        int bannerHeight = 30;
        pGuiGraphics.blit(GuiTextures.Items.SPELL_CUSTOMIZE, i + this.f_97726_ / 2 - bannerWidth / 2, j, 0.0F, 0.0F, bannerWidth, bannerHeight, this.f_97726_, this.f_97727_);
        int iconsWidth = 219;
        int iconsHeight = 219;
        pGuiGraphics.blit(GuiTextures.Items.SPELL_CUSTOMIZE, i + this.f_97726_ / 2 - iconsWidth / 2, j + 33, 0.0F, 31.0F, iconsWidth, iconsHeight, this.f_97726_, this.f_97727_);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }

    class ModelButton extends Button {

        public ItemStack icon = new ItemStack(ItemInit.SPELL.get());

        public ModelButton(int x, int y, int index, Button.OnPress pressedAction) {
            super(x, y, 16, 16, Component.literal(""), pressedAction, new Button.CreateNarration() {

                @Override
                public MutableComponent createNarrationMessage(Supplier<MutableComponent> pMessageSupplier) {
                    return Component.empty();
                }
            });
            ItemSpell.setCustomIcon(this.icon, index);
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            pGuiGraphics.renderItem(this.icon, this.m_252754_(), this.m_252907_());
        }

        @Override
        public void setFocused(boolean focus) {
        }
    }
}