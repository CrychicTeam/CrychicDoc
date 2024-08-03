package fr.frinn.custommachinery.client.screen.creation.gui;

import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.SuggestedEditBox;
import fr.frinn.custommachinery.common.guielement.BackgroundGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class BackgroundEditorPopup extends PopupScreen {

    @Nullable
    private final BackgroundGuiElement background;

    private CycleButton<BackgroundEditorPopup.Mode> mode;

    private SuggestedEditBox texture;

    public BackgroundEditorPopup(MachineEditScreen parent) {
        super(parent, 256, 128);
        this.background = (BackgroundGuiElement) parent.getBuilder().getGuiElements().stream().filter(element -> element instanceof BackgroundGuiElement).map(element -> (BackgroundGuiElement) element).findFirst().orElse(null);
    }

    @Override
    protected void init() {
        super.init();
        BackgroundEditorPopup.Mode mode = BackgroundEditorPopup.Mode.CUSTOM;
        if (this.background != null && this.background.getTexture() != null) {
            if (this.background.getTexture().equals(BackgroundGuiElement.BASE_BACKGROUND)) {
                mode = BackgroundEditorPopup.Mode.DEFAULT;
            }
        } else {
            mode = BackgroundEditorPopup.Mode.NO_BACKGROUND;
        }
        GridLayout layout = new GridLayout(this.x + 5, this.y + 5).spacing(5);
        GridLayout.RowHelper row = layout.createRowHelper(2);
        LayoutSettings center = row.newCellSettings().alignHorizontallyCenter();
        row.addChild(new StringWidget(this.xSize - 10, 9, Component.translatable("custommachinery.gui.creation.gui.background"), this.f_96547_), 2, center);
        row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.background.mode"), this.f_96547_));
        this.mode = row.addChild(CycleButton.<BackgroundEditorPopup.Mode>builder(BackgroundEditorPopup.Mode::title).displayOnlyValue().withValues(BackgroundEditorPopup.Mode.values()).withInitialValue(mode).create(0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.background.mode"), (button, value) -> this.texture.m_94186_(value == BackgroundEditorPopup.Mode.CUSTOM)));
        row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.background.texture"), this.f_96547_));
        this.texture = row.addChild(new SuggestedEditBox(this.f_96547_, 0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.background.texture"), 5));
        this.texture.m_94199_(Integer.MAX_VALUE);
        if (this.background != null) {
            this.texture.m_94144_(this.background.getTexture().toString());
            this.texture.hideSuggestions();
        }
        this.texture.addSuggestions(Minecraft.getInstance().getResourceManager().listResources("textures", id -> true).keySet().stream().map(ResourceLocation::toString).toList());
        this.texture.m_94186_(mode == BackgroundEditorPopup.Mode.CUSTOM);
        row.addChild(Button.builder(Component.translatable("custommachinery.gui.creation.gui.background.close"), button -> this.close()).size(50, 20).build(), 2, center);
        layout.arrangeElements();
        layout.m_264134_(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        });
        this.ySize = layout.m_93694_() + 10;
    }

    private void close() {
        this.parent.closePopup(this);
    }

    @Override
    public void closed() {
        if (this.parent instanceof MachineEditScreen editScreen) {
            if (this.mode.getValue() == BackgroundEditorPopup.Mode.NO_BACKGROUND) {
                if (this.background != null) {
                    editScreen.getBuilder().getGuiElements().remove(this.background);
                }
            } else {
                ResourceLocation texture = switch((BackgroundEditorPopup.Mode) this.mode.getValue()) {
                    case DEFAULT ->
                        BackgroundGuiElement.BASE_BACKGROUND;
                    case CUSTOM ->
                        ResourceLocation.tryParse(this.texture.m_94155_());
                    case NO_BACKGROUND ->
                        null;
                };
                if (this.background != null) {
                    editScreen.getBuilder().getGuiElements().remove(this.background);
                    editScreen.getBuilder().getGuiElements().add(new BackgroundGuiElement(texture, this.background.getWidth(), this.background.getHeight()));
                } else {
                    editScreen.getBuilder().getGuiElements().add(new BackgroundGuiElement(texture, -1, -1));
                }
            }
        }
    }

    public static enum Mode {

        DEFAULT(Component.translatable("custommachinery.gui.creation.gui.background.default")), NO_BACKGROUND(Component.translatable("custommachinery.gui.creation.gui.background.disabled")), CUSTOM(Component.translatable("custommachinery.gui.creation.gui.background.custom"));

        private final Component title;

        private Mode(Component title) {
            this.title = title;
        }

        public Component title() {
            return this.title;
        }
    }
}