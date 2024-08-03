package fr.frinn.custommachinery.client.screen.creation;

import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.ComponentEditBox;
import fr.frinn.custommachinery.client.screen.widget.ToggleImageButton;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ComponentStylePopup extends PopupScreen {

    public static final ResourceLocation WIDGETS = new ResourceLocation("custommachinery", "textures/gui/creation/style_widget.png");

    private final ComponentEditBox editBox;

    public ComponentStylePopup(BaseScreen parent, ComponentEditBox editBox) {
        super(parent, 64, 82);
        this.editBox = editBox;
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(new ImageButton(this.x + 5, this.y + 5, 9, 9, 0, 0, 9, new ResourceLocation("custommachinery", "textures/gui/config/exit_button.png"), 9, 18, buttonx -> this.parent.closePopup(this)));
        GridLayout colorsLayout = new GridLayout(this.x, this.y + 5);
        colorsLayout.defaultCellSetting().alignHorizontallyCenter();
        GridLayout.RowHelper row = colorsLayout.rowSpacing(3).columnSpacing(3).createRowHelper(4);
        row.addChild(new StringWidget(Component.translatable("custommachinery.gui.popup.style"), this.mc.font), 4);
        for (int i = 0; i < 16; i++) {
            ChatFormatting format = ChatFormatting.getById(i);
            if (format != null) {
                ImageButton button = new ImageButton(0, 0, 10, 10, i * 10, 0, WIDGETS, b -> this.editBox.setStyle(this.editBox.getStyle().applyFormat(format)));
                row.addChild(button);
                button.m_257544_(Tooltip.create(Component.translatable(format.getName())));
            }
        }
        colorsLayout.arrangeElements();
        colorsLayout.m_264134_(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        });
        colorsLayout.m_252865_(this.x + this.xSize / 2 - colorsLayout.m_5711_() / 2);
        GridLayout styleLayout = new GridLayout(this.x, this.y + this.ySize - 15);
        styleLayout.defaultCellSetting().alignHorizontallyCenter();
        row = styleLayout.rowSpacing(3).columnSpacing(2).createRowHelper(5);
        AtomicInteger index = new AtomicInteger(16);
        for (ChatFormatting format : List.of(ChatFormatting.BOLD, ChatFormatting.ITALIC, ChatFormatting.UNDERLINE, ChatFormatting.STRIKETHROUGH, ChatFormatting.OBFUSCATED)) {
            ToggleImageButton button = new ToggleImageButton(0, 0, 10, 10, index.getAndIncrement() * 10, 0, WIDGETS, b -> this.editBox.invert(format));
            row.addChild(button);
            button.m_257544_(Tooltip.create(Component.translatable(format.getName())));
            if (format == ChatFormatting.BOLD) {
                button.setToggle(this.editBox.getStyle().isBold());
            } else if (format == ChatFormatting.ITALIC) {
                button.setToggle(this.editBox.getStyle().isItalic());
            } else if (format == ChatFormatting.UNDERLINE) {
                button.setToggle(this.editBox.getStyle().isUnderlined());
            } else if (format == ChatFormatting.STRIKETHROUGH) {
                button.setToggle(this.editBox.getStyle().isStrikethrough());
            } else if (format == ChatFormatting.OBFUSCATED) {
                button.setToggle(this.editBox.getStyle().isObfuscated());
            }
        }
        styleLayout.arrangeElements();
        styleLayout.m_264134_(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        });
        styleLayout.m_252865_(this.x + this.xSize / 2 - styleLayout.m_5711_() / 2);
    }
}