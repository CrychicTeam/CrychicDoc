package fr.frinn.custommachinery.client.screen.creation.tabs;

import com.google.common.collect.ImmutableList;
import fr.frinn.custommachinery.api.crafting.ProcessorType;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.widget.ComponentEditBox;
import fr.frinn.custommachinery.common.init.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;

public class BaseInfoTab extends MachineEditTab {

    public BaseInfoTab(MachineEditScreen parent) {
        super(Component.translatable("custommachinery.gui.creation.tab.base_info"), parent);
        Font font = this.parent.mc.font;
        GridLayout.RowHelper row = this.f_267367_.rowSpacing(8).createRowHelper(2);
        row.defaultCellSetting().paddingHorizontal(0);
        row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.base_info.id").append(Component.literal(this.parent.getBuilder().getLocation().getId().toString())), Minecraft.getInstance().font), 2, row.newCellSettings().alignHorizontallyCenter());
        row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.base_info.name"), font), row.newCellSettings().alignVerticallyMiddle());
        ComponentEditBox nameEdit = new ComponentEditBox(Minecraft.getInstance().font, 0, 0, 100, 20, Component.literal("name"));
        nameEdit.m_257771_(Component.literal("name"));
        nameEdit.setComponent(this.parent.getBuilder().getName());
        nameEdit.setComponentResponder(name -> {
            this.parent.setChanged();
            this.parent.getBuilder().setName(name);
        });
        row.addChild(nameEdit);
        row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.base_info.processor"), font), row.newCellSettings().alignVerticallyMiddle());
        row.addChild(CycleButton.<Object>builder(processor -> Component.literal(processor.getId().getPath())).withValues(ImmutableList.copyOf(Registration.PROCESSOR_REGISTRY)).withInitialValue((ProcessorType) Registration.MACHINE_PROCESSOR.get()).displayOnlyValue().create(0, 0, 100, 20, Component.literal("Machine processor"), (button, processor) -> {
            this.parent.getBuilder().setProcessor(processor);
            this.parent.setChanged();
        }), row.newCellSettings().alignHorizontallyRight());
    }
}