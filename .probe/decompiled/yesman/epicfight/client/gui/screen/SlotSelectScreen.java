package yesman.epicfight.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import yesman.epicfight.skill.SkillContainer;

public class SlotSelectScreen extends Screen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation("epicfight", "textures/gui/screen/slot_select.png");

    private final SkillBookScreen parent;

    private final List<SkillContainer> containers;

    public SlotSelectScreen(Set<SkillContainer> containers, SkillBookScreen parent) {
        super(Component.empty());
        this.parent = parent;
        this.containers = new ArrayList(containers);
        Collections.sort(this.containers, (c1, c2) -> {
            if (c1.getSlotId() > c2.getSlotId()) {
                return 1;
            } else {
                return c1.getSlotId() < c2.getSlotId() ? -1 : 0;
            }
        });
    }

    @Override
    protected void init() {
        this.parent.m_6575_(this.f_96541_, this.f_96543_, this.f_96544_);
        int k = this.f_96543_ / 2 - 80;
        int l = this.f_96544_ / 2 - 45;
        for (SkillContainer container : this.containers) {
            String slotName = container.getSlot().toString().toLowerCase(Locale.ROOT);
            String skillName = container.getSkill() == null ? "Empty" : Component.translatable(container.getSkill().getTranslationKey()).getString();
            SlotSelectScreen.SlotButton slotbutton = new SlotSelectScreen.SlotButton(k, l, 167, 17, Component.literal(slotName + ": " + skillName), button -> {
                this.parent.learnSkill(container);
                this.onClose();
            });
            l += 22;
            this.m_142416_(slotbutton);
        }
    }

    @Override
    public void onClose() {
        if (this.parent != null) {
            this.f_96541_.setScreen(this.parent);
        } else {
            super.onClose();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int posX = (this.f_96543_ - 184) / 2;
        int posY = (this.f_96544_ - 150) / 2;
        this.parent.render(guiGraphics, mouseX, mouseY, partialTicks, true);
        guiGraphics.pose().translate(0.0F, 0.0F, 50.0F);
        RenderSystem.setShader(GameRenderer::m_172820_);
        guiGraphics.blit(BACKGROUND, posX, posY, 0, 0, 191, 154);
        Component component = Component.translatable("gui.epicfight.select_slot_tooltip");
        int lineHeight = 0;
        for (FormattedCharSequence s : this.f_96547_.split(component, 250)) {
            guiGraphics.drawString(this.f_96547_, s, this.f_96543_ / 2 - 84, this.f_96544_ / 2 - 66 + lineHeight, 3158064, false);
            lineHeight += 10;
        }
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    class SlotButton extends Button {

        public SlotButton(int x, int y, int width, int height, Component title, Button.OnPress pressedAction) {
            super(x, y, width, height, title, pressedAction, Button.DEFAULT_NARRATION);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            this.f_93622_ = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            int y = !this.f_93622_ && this.f_93623_ ? 154 : 171;
            guiGraphics.blit(SlotSelectScreen.BACKGROUND, this.m_252754_(), this.m_252907_(), 0, y, this.f_93618_, this.f_93619_);
            guiGraphics.drawString(SlotSelectScreen.this.f_96547_, this.m_6035_(), this.m_252754_() + 3, this.m_252907_() + 3, -1, false);
        }
    }
}