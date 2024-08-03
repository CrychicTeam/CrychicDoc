package fr.frinn.custommachinery.client.screen.creation.gui;

import fr.frinn.custommachinery.PlatformHelper;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.api.machine.ICustomMachine;
import fr.frinn.custommachinery.api.machine.MachineTile;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.popup.ConfirmPopup;
import fr.frinn.custommachinery.common.init.CustomMachineBlock;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.Comparators;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import fr.frinn.custommachinery.impl.guielement.GuiElementWidgetSupplierRegistry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class GuiEditorWidget extends AbstractWidget implements ContainerEventHandler {

    private final MachineEditScreen parent;

    private final IMachineScreen dummyScreen = new GuiEditorWidget.DummyScreen();

    private final List<IGuiElement> elements = new ArrayList();

    private final List<GuiEditorWidget.WidgetEditorWidget<?>> widgets = new ArrayList();

    private final Button config;

    private final Button priorityUp;

    private final Button priorityDown;

    private final Button delete;

    private boolean dragging;

    private GuiEventListener focused;

    public GuiEditorWidget(MachineEditScreen parent, int x, int y, int width, int height, List<IGuiElement> baseElements) {
        super(x, y, width, height, Component.empty());
        this.parent = parent;
        baseElements.stream().sorted(Comparators.GUI_ELEMENTS_COMPARATOR.reversed()).forEach(this::addElement);
        this.config = Button.builder(Component.empty(), button -> {
            if (this.getFocused() instanceof GuiEditorWidget.WidgetEditorWidget<?> widget) {
                this.config(widget);
            }
        }).size(5, 5).tooltip(Tooltip.create(Component.translatable("custommachinery.gui.creation.gui.config"))).build();
        this.priorityUp = Button.builder(Component.empty(), button -> this.changePriority(1)).size(5, 5).build();
        this.priorityDown = Button.builder(Component.empty(), button -> this.changePriority(-1)).size(5, 5).build();
        this.delete = Button.builder(Component.empty(), button -> this.delete()).size(5, 5).tooltip(Tooltip.create(Component.translatable("custommachinery.gui.creation.delete"))).build();
    }

    public void addElement(IGuiElement element) {
        if (GuiElementWidgetSupplierRegistry.hasWidgetSupplier(element.getType()) && GuiElementBuilderRegistry.hasBuilder(element.getType())) {
            this.elements.add(element);
            this.widgets.add(this.getWidget(element));
        }
    }

    public void addCreatedElement(IGuiElement element) {
        if (GuiElementWidgetSupplierRegistry.hasWidgetSupplier(element.getType()) && GuiElementBuilderRegistry.hasBuilder(element.getType())) {
            this.elements.add(element);
            GuiEditorWidget.WidgetEditorWidget<?> widget = this.getWidget(element);
            widget.m_264152_(this.m_252754_() + (this.m_5711_() + widget.m_5711_()) / 2, this.m_252907_() + (this.m_93694_() + widget.m_93694_()) / 2);
            this.widgets.add(widget);
            this.setFocused(widget);
        }
    }

    public void hideButtons() {
        this.config.f_93624_ = false;
        this.priorityUp.f_93624_ = false;
        this.priorityDown.f_93624_ = false;
        this.delete.f_93624_ = false;
    }

    public void showButtons(GuiEditorWidget.WidgetEditorWidget<?> widget) {
        this.config.m_264152_(widget.m_252754_() - 1, widget.m_252907_() - 7);
        this.config.f_93624_ = true;
        this.priorityUp.m_264152_(widget.m_252754_() + 5, widget.m_252907_() - 7);
        this.priorityUp.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.gui.priorityUp").append("\n").append(Component.translatable("custommachinery.gui.creation.gui.priority.value", widget.properties.getPriority()).withStyle(ChatFormatting.GRAY))));
        this.priorityUp.f_93624_ = true;
        this.priorityDown.m_264152_(widget.m_252754_() + 11, widget.m_252907_() - 7);
        this.priorityDown.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.gui.priorityDown").append("\n").append(Component.translatable("custommachinery.gui.creation.gui.priority.value", widget.properties.getPriority()).withStyle(ChatFormatting.GRAY))));
        this.priorityDown.f_93624_ = true;
        this.delete.m_264152_(widget.m_252754_() + 17, widget.m_252907_() - 7);
        this.delete.f_93624_ = true;
    }

    public <T extends IGuiElement> void config(GuiEditorWidget.WidgetEditorWidget<T> widget) {
        this.parent.openPopup(widget.builder.makeConfigPopup(this.parent, widget.properties, widget.widget.getElement(), widget::refreshWidget));
    }

    private void changePriority(int delta) {
        if (this.getFocused() instanceof GuiEditorWidget.WidgetEditorWidget<?> widget) {
            widget.properties.setPriority(widget.properties.getPriority() + delta);
            widget.refreshWidget(null);
            List<GuiEditorWidget.WidgetEditorWidget<?>> sorted = this.widgets.stream().sorted(Comparator.comparingInt(w -> w.properties.getPriority())).toList();
            this.widgets.clear();
            this.widgets.addAll(sorted);
            this.priorityUp.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.gui.priorityUp").append("\n").append(Component.translatable("custommachinery.gui.creation.gui.priority.value", widget.properties.getPriority()).withStyle(ChatFormatting.GRAY))));
            this.priorityDown.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.gui.priorityDown").append("\n").append(Component.translatable("custommachinery.gui.creation.gui.priority.value", widget.properties.getPriority()).withStyle(ChatFormatting.GRAY))));
            this.parent.setChanged();
        }
    }

    private void delete() {
        if (this.getFocused() instanceof GuiEditorWidget.WidgetEditorWidget<?> widget) {
            ConfirmPopup popup = new ConfirmPopup(this.parent, 128, 96, () -> {
                this.widgets.remove(widget);
                this.setFocused(null);
                this.parent.getBuilder().getGuiElements().remove(widget.widget.getElement());
                this.parent.setChanged();
            });
            popup.title(Component.translatable("custommachinery.gui.popup.warning").withStyle(ChatFormatting.DARK_RED));
            popup.text(Component.translatable("custommachinery.gui.creation.gui.delete.popup"));
            this.parent.openPopup(popup);
        }
    }

    private <T extends IGuiElement> GuiEditorWidget.WidgetEditorWidget<T> getWidget(T element) {
        AbstractGuiElementWidget<T> widget = (AbstractGuiElementWidget<T>) GuiElementWidgetSupplierRegistry.getWidgetSupplier(element.getType()).get(element, this.dummyScreen);
        IGuiElementBuilder<T> builder = GuiElementBuilderRegistry.getBuilder((GuiElementType<T>) element.getType());
        return new GuiEditorWidget.WidgetEditorWidget<>(widget, builder);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(this.m_252754_() - 2, this.m_252907_() - 2, this.m_252754_() + this.m_5711_() + 2, this.m_252907_() + this.m_93694_() + 2, FastColor.ARGB32.color(255, 0, 0, 0));
        graphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), FastColor.ARGB32.color(255, 198, 198, 198));
        graphics.pose().pushPose();
        this.widgets.forEach(widget -> widget.m_88315_(graphics, mouseX, mouseY, partialTick));
        graphics.pose().popPose();
        this.config.m_88315_(graphics, mouseX, mouseY, partialTick);
        this.priorityUp.m_88315_(graphics, mouseX, mouseY, partialTick);
        this.priorityDown.m_88315_(graphics, mouseX, mouseY, partialTick);
        this.delete.m_88315_(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        this.widgets.forEach(widget -> widget.setX(x + widget.widget.getElement().getX()));
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        this.widgets.forEach(widget -> widget.setY(y + widget.widget.getElement().getY()));
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return this.widgets;
    }

    @Override
    public boolean isDragging() {
        return this.dragging;
    }

    @Override
    public void setDragging(boolean isDragging) {
        this.dragging = isDragging;
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        if (this.focused != null) {
            this.focused.setFocused(false);
        }
        this.focused = focused;
        if (focused != null) {
            focused.setFocused(true);
        }
        if (focused instanceof GuiEditorWidget.WidgetEditorWidget<?> widget) {
            this.showButtons(widget);
        } else {
            this.hideButtons();
        }
    }

    @Override
    public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
        for (GuiEventListener guiEventListener : this.children()) {
            if (guiEventListener.isMouseOver(mouseX, mouseY)) {
                return Optional.of(guiEventListener);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.config.m_6375_(mouseX, mouseY, button)) {
            return true;
        } else if (this.priorityUp.m_6375_(mouseX, mouseY, button)) {
            return true;
        } else if (this.priorityDown.m_6375_(mouseX, mouseY, button)) {
            return true;
        } else if (this.delete.m_6375_(mouseX, mouseY, button)) {
            return true;
        } else {
            for (GuiEventListener guiEventListener : this.children()) {
                if (guiEventListener.mouseClicked(mouseX, mouseY, button)) {
                    this.setFocused(guiEventListener);
                    if (button == 0) {
                        this.setDragging(true);
                    }
                    return true;
                }
            }
            this.setFocused(null);
            return false;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.setDragging(false);
        return this.getFocused() != null && this.getFocused().mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.getFocused() != null && this.isDragging() && button == 0 ? this.getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY) : false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.getChildAt(mouseX, mouseY).filter(arg -> arg.mouseScrolled(mouseX, mouseY, delta)).isPresent();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.getFocused() != null && this.getFocused().keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return this.getFocused() != null && this.getFocused().keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return this.getFocused() != null && this.getFocused().charTyped(codePoint, modifiers);
    }

    private static enum DragType {

        DEFAULT, UP_RESIZE, DOWN_RESIZE, LEFT_RESIZE, RIGHT_RESIZE
    }

    private class DummyScreen implements IMachineScreen {

        private final MachineTile dummy = PlatformHelper.createMachineTile(BlockPos.ZERO, ((CustomMachineBlock) Registration.CUSTOM_MACHINE_BLOCK.get()).m_49966_());

        @Override
        public int getX() {
            return GuiEditorWidget.this.m_252754_();
        }

        @Override
        public int getY() {
            return GuiEditorWidget.this.m_252907_();
        }

        @Override
        public int getWidth() {
            return GuiEditorWidget.this.m_5711_();
        }

        @Override
        public int getHeight() {
            return GuiEditorWidget.this.m_93694_();
        }

        @Override
        public MachineTile getTile() {
            return this.dummy;
        }

        @Override
        public ICustomMachine getMachine() {
            return this.dummy.getMachine();
        }
    }

    public class WidgetEditorWidget<T extends IGuiElement> extends AbstractWidget {

        private final IGuiElementBuilder<T> builder;

        private final MutableProperties properties;

        private AbstractGuiElementWidget<T> widget;

        private GuiEditorWidget.DragType dragType = GuiEditorWidget.DragType.DEFAULT;

        private double dragX = 0.0;

        private double dragY = 0.0;

        public WidgetEditorWidget(AbstractGuiElementWidget<T> widget, IGuiElementBuilder<T> builder) {
            super(widget.m_252754_(), widget.m_252907_(), widget.m_5711_(), widget.m_93694_(), widget.m_6035_());
            this.widget = widget;
            this.builder = builder;
            this.properties = new MutableProperties(widget.getElement().getProperties());
        }

        public void refreshWidget(@Nullable T from) {
            T element = this.widget.getElement();
            T newElement = from != null ? from : this.builder.make(this.properties.build(), element);
            this.widget = (AbstractGuiElementWidget<T>) GuiElementWidgetSupplierRegistry.getWidgetSupplier(element.getType()).get(newElement, GuiEditorWidget.this.dummyScreen);
            this.widget.m_264152_(this.m_252754_(), this.m_252907_());
            this.f_93618_ = this.widget.m_5711_();
            this.f_93619_ = this.widget.m_93694_();
            GuiEditorWidget.this.parent.getBuilder().getGuiElements().remove(element);
            GuiEditorWidget.this.parent.getBuilder().getGuiElements().add(newElement);
        }

        private GuiEditorWidget.DragType getDragType(double mouseX, double mouseY) {
            if (!this.m_5953_(mouseX, mouseY)) {
                return GuiEditorWidget.DragType.DEFAULT;
            } else if (mouseX >= (double) this.m_252754_() && mouseX <= (double) (this.m_252754_() + 1) && mouseY >= (double) this.m_252907_() && mouseY <= (double) (this.m_252907_() + this.m_93694_())) {
                return GuiEditorWidget.DragType.LEFT_RESIZE;
            } else if (mouseX >= (double) (this.m_252754_() + this.m_5711_() - 1) && mouseX <= (double) (this.m_252754_() + this.m_5711_()) && mouseY >= (double) this.m_252907_() && mouseY <= (double) (this.m_252907_() + this.m_93694_())) {
                return GuiEditorWidget.DragType.RIGHT_RESIZE;
            } else if (mouseX >= (double) this.m_252754_() && mouseX <= (double) (this.m_252754_() + this.m_5711_()) && mouseY >= (double) this.m_252907_() && mouseY <= (double) (this.m_252907_() + 1)) {
                return GuiEditorWidget.DragType.UP_RESIZE;
            } else {
                return mouseX >= (double) this.m_252754_() && mouseX <= (double) (this.m_252754_() + this.m_5711_()) && mouseY >= (double) (this.m_252907_() + this.m_93694_() - 1) && mouseY <= (double) (this.m_252907_() + this.m_93694_()) ? GuiEditorWidget.DragType.DOWN_RESIZE : GuiEditorWidget.DragType.DEFAULT;
            }
        }

        private void checkCursorShape(int mouseX, int mouseY) {
            if (this.dragType == GuiEditorWidget.DragType.DEFAULT) {
                switch(this.getDragType((double) mouseX, (double) mouseY)) {
                    case LEFT_RESIZE:
                    case RIGHT_RESIZE:
                        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), GLFW.glfwCreateStandardCursor(221189));
                        break;
                    case UP_RESIZE:
                    case DOWN_RESIZE:
                        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), GLFW.glfwCreateStandardCursor(221190));
                        break;
                    default:
                        GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), GLFW.glfwCreateStandardCursor(208897));
                }
            }
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.pose().pushPose();
            switch(this.dragType) {
                case LEFT_RESIZE:
                    graphics.pose().translate((double) (-this.m_252754_()) * -this.dragX / (double) this.m_5711_() + this.dragX, 0.0, 0.0);
                    graphics.pose().scale((float) (-this.dragX / (double) this.m_5711_()) + 1.0F, 1.0F, 1.0F);
                    break;
                case RIGHT_RESIZE:
                    graphics.pose().translate((double) (-this.m_252754_()) * this.dragX / (double) this.m_5711_(), 0.0, 0.0);
                    graphics.pose().scale((float) (this.dragX / (double) this.m_5711_()) + 1.0F, 1.0F, 1.0F);
                    break;
                case UP_RESIZE:
                    graphics.pose().translate(0.0, (double) (-this.m_252907_()) * -this.dragY / (double) this.m_93694_() + this.dragY, 0.0);
                    graphics.pose().scale(1.0F, (float) (-this.dragY / (double) this.m_93694_()) + 1.0F, 1.0F);
                    break;
                case DOWN_RESIZE:
                    graphics.pose().translate(0.0, (double) (-this.m_252907_()) * this.dragY / (double) this.m_93694_(), 0.0);
                    graphics.pose().scale(1.0F, (float) (this.dragY / (double) this.m_93694_()) + 1.0F, 1.0F);
                    break;
                case DEFAULT:
                    graphics.pose().translate(this.dragX, this.dragY, 0.0);
            }
            if (this.m_93696_()) {
                graphics.fill(this.m_252754_() - 1, this.m_252907_() - 1, this.m_252754_() + this.m_5711_() + 1, this.m_252907_() + this.m_93694_() + 1, FastColor.ARGB32.color(255, 255, 0, 0));
                graphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), FastColor.ARGB32.color(255, 198, 198, 198));
                this.checkCursorShape(mouseX, mouseY);
            }
            this.widget.render(graphics, Integer.MAX_VALUE, Integer.MAX_VALUE, partialTick);
            graphics.pose().popPose();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }

        @Override
        public void setX(int x) {
            super.setX(x);
            this.properties.setX(x - GuiEditorWidget.this.m_252754_());
            this.refreshWidget(null);
        }

        @Override
        public void setY(int y) {
            super.setY(y);
            this.properties.setY(y - GuiEditorWidget.this.m_252907_());
            this.refreshWidget(null);
        }

        @Override
        public void setWidth(int width) {
            super.setWidth(width);
            this.properties.setWidth(width);
            this.refreshWidget(null);
        }

        public void setHeight(int height) {
            this.f_93619_ = height;
            this.properties.setHeight(height);
            this.refreshWidget(null);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            this.dragType = this.getDragType(mouseX, mouseY);
        }

        @Override
        protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
            GuiEditorWidget.this.hideButtons();
            switch(this.dragType) {
                case LEFT_RESIZE:
                case UP_RESIZE:
                case DEFAULT:
                    this.dragX = Mth.clamp(this.dragX + dragX, (double) (GuiEditorWidget.this.m_252754_() - this.m_252754_()), (double) (GuiEditorWidget.this.m_252754_() + GuiEditorWidget.this.m_5711_() - this.m_252754_() - this.m_5711_()));
                    this.dragY = Mth.clamp(this.dragY + dragY, (double) (GuiEditorWidget.this.m_252907_() - this.m_252907_()), (double) (GuiEditorWidget.this.m_252907_() + GuiEditorWidget.this.m_93694_() - this.m_252907_() - this.m_93694_()));
                    break;
                case RIGHT_RESIZE:
                    this.dragX = Mth.clamp(this.dragX + dragX, (double) (-this.m_5711_()), (double) (GuiEditorWidget.this.m_252754_() + GuiEditorWidget.this.m_5711_() - this.m_252754_() - this.m_5711_()));
                    break;
                case DOWN_RESIZE:
                    this.dragY = Mth.clamp(this.dragY + dragY, (double) (-this.m_93694_()), (double) (GuiEditorWidget.this.m_252907_() + GuiEditorWidget.this.m_93694_() - this.m_252907_() - this.m_93694_()));
            }
        }

        @Override
        public void onRelease(double mouseX, double mouseY) {
            if (this.dragX != 0.0 || this.dragY != 0.0) {
                switch(this.dragType) {
                    case LEFT_RESIZE:
                        this.setX(this.m_252754_() + (int) this.dragX);
                        this.setWidth(this.m_5711_() - (int) this.dragX);
                        break;
                    case RIGHT_RESIZE:
                        this.setWidth(this.m_5711_() + (int) this.dragX);
                        break;
                    case UP_RESIZE:
                        this.setY(this.m_252907_() + (int) this.dragY);
                        this.setHeight(this.m_93694_() - (int) this.dragY);
                        break;
                    case DOWN_RESIZE:
                        this.setHeight(this.m_93694_() + (int) this.dragY);
                        break;
                    case DEFAULT:
                        this.setX(this.m_252754_() + (int) this.dragX);
                        this.setY(this.m_252907_() + (int) this.dragY);
                }
                GuiEditorWidget.this.showButtons(this);
                GuiEditorWidget.this.parent.setChanged();
                this.dragType = GuiEditorWidget.DragType.DEFAULT;
                this.dragX = 0.0;
                this.dragY = 0.0;
                GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), GLFW.glfwCreateStandardCursor(208897));
            }
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            int move = Screen.hasShiftDown() ? 5 : (Screen.hasControlDown() ? 10 : 1);
            return switch(keyCode) {
                case 261 ->
                    {
                    }
                case 262 ->
                    {
                    }
                case 263 ->
                    {
                    }
                case 264 ->
                    {
                    }
                case 265 ->
                    {
                    }
                default ->
                    false;
            };
        }
    }
}