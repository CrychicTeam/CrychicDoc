package fr.frinn.custommachinery.client.screen;

import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.component.ISideConfigComponent;
import fr.frinn.custommachinery.api.guielement.IComponentGuiElement;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.client.screen.popup.ComponentConfigPopup;
import fr.frinn.custommachinery.client.screen.widget.TexturedButton;
import fr.frinn.custommachinery.client.screen.widget.config.ComponentConfigButtonWidget;
import fr.frinn.custommachinery.common.guielement.ConfigGuiElement;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

public class MachineConfigScreen extends BaseScreen {

    private final CustomMachineScreen parent;

    public MachineConfigScreen(CustomMachineScreen parent) {
        super(Component.translatable("custommachinery.gui.config.title", parent.getMachine().getName()), parent.getWidth(), parent.getHeight());
        this.parent = parent;
    }

    private List<IGuiElement> getConfigurableElements() {
        return this.parent.getTile().getGuiElements().stream().filter(element -> {
            if (element instanceof IComponentGuiElement<?> componentElement && componentElement.getComponent(this.parent.getTile().getComponentManager()).isPresent() && componentElement.getComponent(this.parent.getTile().getComponentManager()).get() instanceof ISideConfigComponent configComponent && configComponent.getConfig().isEnabled()) {
                return true;
            }
            return false;
        }).toList();
    }

    private ISideConfigComponent getComponentFromElement(IGuiElement element) {
        if (element instanceof IComponentGuiElement<?> componentGuiElement) {
            Optional<? extends IMachineComponent> optionalComponent = (Optional<? extends IMachineComponent>) componentGuiElement.getComponent(this.parent.getTile().getComponentManager());
            if (optionalComponent.isPresent()) {
                IMachineComponent component = (IMachineComponent) optionalComponent.get();
                if (component instanceof ISideConfigComponent) {
                    return (ISideConfigComponent) component;
                } else {
                    throw new IllegalArgumentException("Component of type: " + component.getType().getId() + " is not side configurable.");
                }
            } else {
                throw new IllegalArgumentException("Element of type: " + element.getType().getId() + " don't have a component in the machine: " + this.parent.getMachine().getId());
            }
        } else {
            throw new IllegalArgumentException("Element of type: " + element.getType().getId() + " is not a component element.");
        }
    }

    @Override
    protected void init() {
        super.init();
        this.parent.m_6575_(Minecraft.getInstance(), this.f_96543_, this.f_96544_);
        this.getConfigurableElements().forEach(element -> {
            ComponentConfigPopup popup = new ComponentConfigPopup(this, this.getComponentFromElement(element).getConfig());
            this.m_142416_(new ComponentConfigButtonWidget(this.x + element.getX(), this.y + element.getY(), element.getWidth(), element.getHeight(), Component.translatable("custommachinery.gui.config.tooltip"), button -> {
                this.closePopup(popup);
                this.openPopup(popup);
                Vector2i pos = getStartingPos(this.getConfigurableElements().indexOf(element));
                popup.move(pos.x * 20, pos.y * 20);
            }));
        });
        this.parent.getTile().getGuiElements().stream().filter(element -> element instanceof ConfigGuiElement).findFirst().map(element -> (ConfigGuiElement) element).ifPresent(element -> this.m_142416_(TexturedButton.builder(Component.translatable("custommachinery.gui.config.exit"), element.getTexture(), button -> Minecraft.getInstance().setScreen(this.parent)).bounds(this.x + element.getX(), this.y + element.getY(), element.getWidth(), element.getHeight()).hovered(element.getTextureHovered()).tooltip(Tooltip.create(Component.translatable("custommachinery.gui.config.exit"))).build()));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().pushPose();
        this.parent.render(graphics, Integer.MAX_VALUE, Integer.MAX_VALUE, partialTicks);
        graphics.pose().translate(0.0F, 0.0F, 400.0F);
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.pose().popPose();
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode) && keyCode != 256) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        } else {
            Minecraft.getInstance().setScreen(this.parent);
            return true;
        }
    }

    private static Vector2i getStartingPos(int np) {
        int dx = 0;
        int dy = 1;
        int segment_length = 1;
        int x = 0;
        int y = 0;
        int segment_passed = 0;
        if (np == 0) {
            return new Vector2i();
        } else {
            for (int n = 0; n < np; n++) {
                x += dx;
                y += dy;
                if (++segment_passed == segment_length) {
                    segment_passed = 0;
                    int buffer = dy;
                    dy = -dx;
                    dx = buffer;
                    if (buffer == 0) {
                        segment_length++;
                    }
                }
            }
            return new Vector2i(x, y);
        }
    }
}