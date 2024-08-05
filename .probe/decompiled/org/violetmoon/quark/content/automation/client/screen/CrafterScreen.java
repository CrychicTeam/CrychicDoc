package org.violetmoon.quark.content.automation.client.screen;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.automation.inventory.CrafterMenu;

public class CrafterScreen extends AbstractContainerScreen<CrafterMenu> {

    private static final ResourceLocation TEXTURE = Quark.asResource("textures/gui/container/crafter.png");

    public CrafterScreen(CrafterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void init() {
        super.init();
        this.f_97728_ = (this.f_97726_ - this.f_96547_.width(this.f_96539_)) / 2;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.m_280273_(context);
        super.render(context, mouseX, mouseY, delta);
        this.m_280072_(context, mouseX, mouseY);
        if (((CrafterMenu) this.f_97732_).m_142621_().isEmpty() && this.f_97734_ != null && this.f_97734_.getItem().isEmpty() && this.f_97734_.container == ((CrafterMenu) this.f_97732_).crafter && !this.isBlocked(this.f_97734_)) {
            context.renderComponentTooltip(this.f_96547_, List.of(Component.translatable("quark.misc.disable_slot")), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (((CrafterMenu) this.f_97732_).m_142621_().isEmpty() && this.f_97734_ != null && this.f_97734_.getItem().isEmpty() && this.f_97734_.container == ((CrafterMenu) this.f_97732_).crafter) {
            this.f_96541_.gameMode.handleInventoryButtonClick(((CrafterMenu) this.f_97732_).f_38840_, this.f_97734_.getContainerSlot());
            this.f_96541_.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    public boolean isBlocked(Slot slot) {
        return slot.container == ((CrafterMenu) this.f_97732_).crafter && (((CrafterMenu) this.f_97732_).delegate.get(0) & 1 << 1 + slot.getContainerSlot()) != 0;
    }

    @Override
    public void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = this.f_97735_;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        context.blit(TEXTURE, i, j, 0, 0, this.f_97726_, this.f_97727_);
        int del = ((CrafterMenu) this.f_97732_).delegate.get(0);
        if ((del & 1) != 0) {
            context.blit(TEXTURE, i + 97, j + 38, 176, 18, 16, 11);
        }
        for (int s = 0; s < 9; s++) {
            if ((del & 1 << s + 1) != 0) {
                Slot slot = (Slot) ((CrafterMenu) this.f_97732_).f_38839_.get(s + 1);
                context.blit(TEXTURE, i + slot.x - 1, j + slot.y - 1, 176, 0, 18, 18);
            }
        }
    }
}