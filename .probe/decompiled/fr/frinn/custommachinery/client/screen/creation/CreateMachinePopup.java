package fr.frinn.custommachinery.client.screen.creation;

import dev.architectury.platform.Platform;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.popup.InfoPopup;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.ComponentEditBox;
import fr.frinn.custommachinery.common.machine.MachineLocation;
import fr.frinn.custommachinery.common.network.CAddMachinePacket;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CreateMachinePopup extends PopupScreen {

    private Button create;

    private Button cancel;

    private EditBox id;

    private ComponentEditBox name;

    private CycleButton<MachineLocation.Loader> loader;

    protected CreateMachinePopup(BaseScreen parent) {
        super(parent, 128, 121);
    }

    public void create() {
        new CAddMachinePacket(this.id.getValue(), this.name.getComponent(), this.loader.getValue() == MachineLocation.Loader.KUBEJS).sendToServer();
        this.parent.closePopup(this);
        if (this.loader.getValue() == MachineLocation.Loader.DEFAULT) {
            this.parent.openPopup(new InfoPopup(this.parent, 144, 96).text(Component.translatable("custommachinery.gui.creation.popup.create.success.description")));
        } else if (this.loader.getValue() == MachineLocation.Loader.KUBEJS) {
            Minecraft.getInstance().getTutorial().addTimedToast(new TutorialToast(TutorialToast.Icons.MOUSE, Component.translatable("custommachinery.gui.creation.popup.create.success"), null, false), 50);
        }
    }

    @Override
    protected void init() {
        super.init();
        GridLayout layout = new GridLayout(this.x, this.y).rowSpacing(5);
        GridLayout.RowHelper row = layout.createRowHelper(2);
        LayoutSettings center = row.newCellSettings().alignHorizontallyCenter();
        row.addChild(new StringWidget(this.xSize, 10, Component.translatable("custommachinery.gui.creation.popup.create"), this.f_96547_), 2, row.newCellSettings().alignHorizontallyCenter().paddingTop(5));
        this.id = row.addChild(new EditBox(this.f_96547_, this.x + 10, this.y + 20, this.xSize - 20, 20, Component.literal("machine_id")), 2, center);
        this.id.setFilter(s -> {
            for (char c : s.toCharArray()) {
                if (!ResourceLocation.validPathChar(c)) {
                    return false;
                }
            }
            return true;
        });
        this.id.setHint(Component.literal("machine_id"));
        this.id.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.popup.create.id.tooltip")));
        this.name = row.addChild(new ComponentEditBox(this.f_96547_, this.x + 10, this.y + 43, this.xSize - 20, 20, Component.literal("Machine name")), 2, center);
        this.name.m_257771_(Component.literal("Machine name"));
        this.name.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.popup.create.name.tooltip")));
        CycleButton.Builder<MachineLocation.Loader> builder = CycleButton.<MachineLocation.Loader>builder(MachineLocation.Loader::getTranslatedName).withValues(MachineLocation.Loader.DEFAULT).withInitialValue(MachineLocation.Loader.DEFAULT).displayOnlyValue();
        if (Platform.isModLoaded("kubejs")) {
            builder.withValues(MachineLocation.Loader.DEFAULT, MachineLocation.Loader.KUBEJS);
        }
        builder.withTooltip(loader -> Tooltip.create(Component.translatable("custommachinery.gui.creation.popup.create.loader." + loader.name().toLowerCase(Locale.ROOT))));
        this.loader = row.addChild(builder.create(0, 0, this.xSize - 20, 20, Component.empty()), 2, center);
        this.create = row.addChild(Button.builder(Component.translatable("custommachinery.gui.creation.create").withStyle(ChatFormatting.GREEN), button -> this.create()).bounds(0, 0, 50, 20).build(), center);
        this.cancel = row.addChild(Button.builder(Component.translatable("custommachinery.gui.popup.cancel").withStyle(ChatFormatting.DARK_RED), button -> this.parent.closePopup(this)).bounds(0, 0, 50, 20).build(), center);
        layout.arrangeElements();
        layout.m_264134_(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        });
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.create.f_93623_ = !this.id.getValue().isEmpty() || !ResourceLocation.isValidResourceLocation("custommachinery:" + this.id.getValue()) || !this.name.m_94155_().isEmpty();
    }
}