package fr.frinn.custommachinery.client.element;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.common.guielement.SlotGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.integration.config.CMConfig;
import fr.frinn.custommachinery.common.util.CycleTimer;
import fr.frinn.custommachinery.common.util.GhostItem;
import fr.frinn.custommachinery.impl.guielement.TexturedGuiElementWidget;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class SlotGuiElementWidget extends TexturedGuiElementWidget<SlotGuiElement> {

    private static final CycleTimer timer = new CycleTimer(() -> CMConfig.get().itemSlotCycleTime);

    public SlotGuiElementWidget(SlotGuiElement element, IMachineScreen screen) {
        super(element, screen, Component.literal("Slot"));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        GhostItem ghost = this.getElement().getGhost();
        if (ghost != GhostItem.EMPTY && !ghost.items().isEmpty() && (ghost.alwaysRender() || this.isSlotEmpty())) {
            timer.onDraw();
            List<Item> items = ghost.items().stream().flatMap(ingredient -> ingredient.getAll().stream()).toList();
            RenderSystem.setShaderColor((float) ghost.color().getRed() / 255.0F, (float) ghost.color().getGreen() / 255.0F, (float) ghost.color().getBlue() / 255.0F, (float) ghost.color().getAlpha() / 255.0F);
            graphics.renderItem(timer.getOrDefault(items, Items.AIR).getDefaultInstance(), this.m_252754_() + 1, this.m_252907_() + 1);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return false;
    }

    private boolean isSlotEmpty() {
        return (Boolean) this.getScreen().getTile().getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(this.getElement().getComponentId())).map(component -> component.getItemStack().isEmpty()).orElse(true);
    }
}