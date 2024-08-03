package fr.frinn.custommachinery.client.element;

import com.google.common.collect.Lists;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.common.component.ExperienceMachineComponent;
import fr.frinn.custommachinery.common.guielement.ExperienceGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.ExperienceUtils;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.impl.guielement.TexturedGuiElementWidget;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ExperienceGuiElementWidget extends TexturedGuiElementWidget<ExperienceGuiElement> {

    private static final Component TITLE = Component.translatable("custommachinery.gui.element.experience.name");

    public ExperienceGuiElementWidget(ExperienceGuiElement element, IMachineScreen screen) {
        super(element, screen, TITLE);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.getElement().getMode().isDisplayBar()) {
            super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        } else {
            Optional<ExperienceMachineComponent> component = this.getScreen().getTile().getComponentManager().getComponent((MachineComponentType<ExperienceMachineComponent>) Registration.EXPERIENCE_MACHINE_COMPONENT.get());
            int level = (Integer) component.map(ExperienceMachineComponent::getLevels).orElse(0);
            int xp = (Integer) component.map(ExperienceMachineComponent::getXp).orElse(0);
            String levels = level + "";
            int xPos = this.m_252754_() + this.f_93618_ / 2 - Minecraft.getInstance().font.width(levels) / 2;
            graphics.drawString(Minecraft.getInstance().font, levels, xPos, this.m_252907_(), 8453920, true);
            graphics.fill(this.m_252754_(), this.m_252907_() + 9, this.m_252754_() + this.f_93618_, this.m_252907_() + 12, -16777216);
            int xpDiff = xp - ExperienceUtils.getXpFromLevel(level);
            if (xpDiff > 0) {
                double percent = (double) xpDiff / (double) ExperienceUtils.getXpNeededForNextLevel(level);
                graphics.fill(this.m_252754_() + 1, this.m_252907_() + 10, this.m_252754_() + 1 + Math.max((int) Math.ceil((double) this.f_93618_ * percent) - 2, 0), this.m_252907_() + 11, -8323296);
            }
        }
    }

    @Override
    public List<Component> getTooltips() {
        List<Component> tooltips = Lists.newArrayList();
        this.getScreen().getTile().getComponentManager().getComponent((MachineComponentType) Registration.EXPERIENCE_MACHINE_COMPONENT.get()).ifPresent(component -> {
            if (this.getElement().getMode().isDisplay()) {
                tooltips.add(TITLE);
                switch(this.getElement().getDisplayMode()) {
                    case LITERAL:
                        {
                            String value = Utils.format(component.getXp());
                            String capacityValue = Utils.format(component.getCapacity()) + " XP";
                            tooltips.add(Component.translatable("custommachinery.gui.element.experience.tooltip", value, capacityValue).withStyle(ChatFormatting.GRAY));
                            break;
                        }
                    case LEVEL:
                        {
                            String value = component.getLevels() + "";
                            String capacityValue = component.getCapacityLevels() + " levels";
                            tooltips.add(Component.translatable("custommachinery.gui.element.experience.tooltip", value, capacityValue).withStyle(ChatFormatting.GRAY));
                            break;
                        }
                    case BOTH:
                        String literal = Utils.format(component.getXp());
                        String capacityLiteral = Utils.format(component.getCapacity()) + "XP";
                        String level = component.getLevels() + "";
                        String capacityLevel = component.getCapacityLevels() + " levels";
                        tooltips.addAll(Lists.newArrayList(new MutableComponent[] { Component.translatable("custommachinery.gui.element.experience.tooltip", literal, capacityLiteral).withStyle(ChatFormatting.GRAY), Component.translatable("custommachinery.gui.element.experience.tooltip", level, capacityLevel).withStyle(ChatFormatting.GRAY) }));
                }
            } else {
                tooltips.add(this.getElement().getMode().title());
            }
        });
        return tooltips;
    }
}