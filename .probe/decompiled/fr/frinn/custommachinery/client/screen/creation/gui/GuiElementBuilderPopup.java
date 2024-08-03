package fr.frinn.custommachinery.client.screen.creation.gui;

import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.SuggestedEditBox;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public abstract class GuiElementBuilderPopup<T extends IGuiElement> extends PopupScreen {

    public final MutableProperties properties;

    @Nullable
    public final T baseElement;

    private final Consumer<T> onFinish;

    private Button confirm;

    public GuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable T from, Consumer<T> onFinish) {
        super(parent, 256, 196);
        this.properties = properties;
        this.baseElement = from;
        this.onFinish = onFinish;
    }

    public abstract T makeElement();

    public abstract void addWidgets(GridLayout.RowHelper var1);

    public Component canCreate() {
        return Component.empty();
    }

    private void save() {
        this.onFinish.accept(this.makeElement());
        this.parent.closePopup(this);
    }

    public void addId(GridLayout.RowHelper row) {
        row.addChild((T) (new StringWidget(Component.translatable("custommachinery.gui.creation.gui.id"), this.f_96547_)));
        SuggestedEditBox id = row.addChild(new SuggestedEditBox(this.f_96547_, 0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.id"), 5));
        id.setResponder(this.properties::setId);
        id.m_94144_(this.properties.getId());
        if (this.parent instanceof MachineEditScreen editScreen) {
            id.addSuggestions(editScreen.getBuilder().getComponents().stream().map(IMachineComponentTemplate::getId).filter(s -> !s.isEmpty()).toList());
        }
    }

    public void addPriority(GridLayout.RowHelper row) {
        row.addChild((T) (new StringWidget(Component.translatable("custommachinery.gui.creation.gui.priority"), this.f_96547_)));
        EditBox priority = row.addChild(new EditBox(this.f_96547_, 0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.priority")));
        priority.setResponder(value -> this.properties.setPriority(Integer.parseInt(value)));
        priority.setFilter(value -> {
            try {
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException var2x) {
                return false;
            }
        });
        priority.setValue(this.properties.getPriority() + "");
    }

    public void addTexture(GridLayout.RowHelper row, Component title, Consumer<ResourceLocation> responder, @Nullable ResourceLocation baseTexture) {
        row.addChild((T) (new StringWidget(title, this.f_96547_)));
        SuggestedEditBox texture = row.addChild(new SuggestedEditBox(this.f_96547_, 0, 0, 100, 20, title, 5));
        texture.m_94199_(Integer.MAX_VALUE);
        texture.setResponder(s -> responder.accept(s.isEmpty() ? null : ResourceLocation.tryParse(s)));
        texture.m_94144_(baseTexture == null ? "" : baseTexture.toString());
        texture.hideSuggestions();
        texture.addSuggestions(Minecraft.getInstance().getResourceManager().listResources("textures", id -> true).keySet().stream().map(ResourceLocation::toString).toList());
        texture.m_94153_(s -> ResourceLocation.tryParse(s) != null);
    }

    @Override
    protected void init() {
        super.init();
        GridLayout layout = new GridLayout(this.x, this.y);
        layout.defaultCellSetting().paddingTop(5).paddingHorizontal(5);
        GridLayout.RowHelper row = layout.createRowHelper(2);
        LayoutSettings center = row.newCellSettings().alignHorizontallyCenter();
        row.addChild((T) (new StringWidget(this.xSize, 9, Component.translatable("custommachinery.gui.creation.gui.edit"), this.f_96547_)), 2, center);
        this.addWidgets(row);
        this.confirm = row.addChild(Button.builder(Component.translatable("custommachinery.gui.popup.confirm").withStyle(ChatFormatting.GREEN), button -> this.save()).size(50, 20).build(), center);
        row.addChild((T) Button.builder(Component.translatable("custommachinery.gui.popup.cancel").withStyle(ChatFormatting.RED), button -> this.parent.closePopup(this)).size(50, 20).build(), center);
        layout.arrangeElements();
        this.ySize = layout.m_93694_() + 10;
        layout.m_264134_(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        });
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        Component canCreate = this.canCreate();
        if (canCreate.getString().isEmpty()) {
            this.confirm.f_93623_ = true;
        } else {
            this.confirm.f_93623_ = false;
            if (this.confirm.m_274382_()) {
                graphics.renderTooltip(this.f_96547_, canCreate, mouseX, mouseY);
            }
        }
    }
}