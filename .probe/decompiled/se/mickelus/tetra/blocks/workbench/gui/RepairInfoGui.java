package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Collections;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.tetra.gui.GuiItemRolling;

public class RepairInfoGui extends GuiElement {

    private final GuiString repairTitle = new GuiString(-8, 0, I18n.get("item.tetra.modular.repair_material.label"));

    private final GuiString noRepairLabel;

    private final GuiRect noRepairBackdrop;

    private final GuiItemRolling repairMaterial;

    private boolean canRepair;

    private final List<Component> tooltip;

    private final List<Component> noRepairtooltip;

    public RepairInfoGui(int x, int y) {
        super(x, y, 49, 16);
        this.repairTitle.setAttachment(GuiAttachment.middleCenter);
        this.addChild(this.repairTitle);
        this.noRepairBackdrop = new GuiRect(0, 0, 49, 16, 0);
        this.noRepairBackdrop.setVisible(false);
        this.addChild(this.noRepairBackdrop);
        this.noRepairLabel = new GuiString(1, 0, ChatFormatting.DARK_GRAY + I18n.get("item.tetra.modular.repair_material.empty"));
        this.noRepairLabel.setAttachment(GuiAttachment.middleCenter);
        this.noRepairLabel.setVisible(false);
        this.addChild(this.noRepairLabel);
        this.repairMaterial = new GuiItemRolling(0, 0);
        this.repairMaterial.setAttachment(GuiAttachment.topRight);
        this.addChild(this.repairMaterial);
        this.tooltip = Collections.singletonList(Component.translatable("item.tetra.modular.repair_material.tooltip"));
        this.noRepairtooltip = Collections.singletonList(Component.translatable("item.tetra.modular.repair_material.empty_tooltip"));
    }

    public void update(ItemStack[] repairItemStacks) {
        this.repairMaterial.setItems(repairItemStacks);
        this.canRepair = repairItemStacks.length > 0;
        this.repairTitle.setVisible(this.canRepair);
        this.repairMaterial.setVisible(this.canRepair);
        this.noRepairLabel.setVisible(!this.canRepair);
        this.noRepairBackdrop.setVisible(!this.canRepair);
    }

    @Override
    public List<Component> getTooltipLines() {
        List<Component> childTooltip = super.getTooltipLines();
        if (this.hasFocus() && childTooltip == null) {
            return this.canRepair ? this.tooltip : this.noRepairtooltip;
        } else {
            return childTooltip;
        }
    }
}