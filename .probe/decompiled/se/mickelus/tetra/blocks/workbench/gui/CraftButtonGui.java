package se.mickelus.tetra.blocks.workbench.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class CraftButtonGui extends GuiClickable {

    private final GuiStringOutline label;

    private final GuiTexture backdrop;

    private boolean enabled = true;

    private List<Component> tooltip;

    private int labelColor = 16777215;

    private int backdropColor = 16777215;

    public CraftButtonGui(int x, int y, Runnable onClickHandler) {
        super(x, y, 46, 15, onClickHandler);
        this.backdrop = new GuiTexture(0, 0, this.width, this.height, 176, 16, GuiTextures.workbench);
        this.backdrop.setAttachment(GuiAttachment.middleCenter);
        this.addChild(this.backdrop);
        this.label = new GuiStringOutline(0, 1, I18n.get("tetra.workbench.schematic_detail.craft"));
        this.label.setAttachment(GuiAttachment.middleCenter);
        this.addChild(this.label);
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        return this.enabled && super.onMouseClick(x, y, button);
    }

    public void update(UpgradeSchematic schematic, Player player, ItemStack itemStack, ItemStack previewStack, ItemStack[] materials, String slot, Map<ToolAction, Integer> availableTools) {
        this.enabled = schematic.canApplyUpgrade(player, itemStack, materials, slot, availableTools);
        this.tooltip = new ArrayList();
        if (this.enabled) {
            this.labelColor = 16777215;
            this.backdropColor = 16777215;
            if (!schematic.willReplace(itemStack, materials, slot)) {
                float severity = schematic.getSeverity(itemStack, materials, slot);
                List<String> destabilizationChance = this.getDestabilizationChance(previewStack.isEmpty() ? itemStack : previewStack, severity);
                if (!destabilizationChance.isEmpty()) {
                    this.backdropColor = 15619481;
                    this.tooltip.add(Component.translatable("tetra.workbench.schematic_detail.destabilize_tooltip").withStyle(ChatFormatting.GRAY));
                    destabilizationChance.stream().map(Component::m_237113_).forEach(this.tooltip::add);
                }
            } else {
                boolean willRepair = (Boolean) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getRepairSlot(itemStack)).map(repairSlot -> repairSlot.equals(slot)).orElse(false) && itemStack.isDamageableItem() && (double) itemStack.getDamageValue() * 1.0 / (double) itemStack.getMaxDamage() > 0.0;
                if (willRepair) {
                    this.tooltip.add(Component.translatable("tetra.workbench.schematic_detail.repair_tooltip"));
                }
            }
        } else {
            this.labelColor = 8355711;
            this.backdropColor = 16733525;
            if (!schematic.isMaterialsValid(itemStack, slot, materials)) {
                if (this.hasEmptyMaterial(schematic, materials)) {
                    this.tooltip.add(Component.translatable("tetra.workbench.schematic_detail.no_material_tooltip"));
                    this.backdropColor = 8355711;
                } else if (this.hasInsufficientQuantities(schematic, itemStack, slot, materials)) {
                    this.tooltip.add(Component.translatable("tetra.workbench.schematic_detail.material_count_tooltip"));
                } else {
                    this.tooltip.add(Component.translatable("tetra.workbench.schematic_detail.material_tooltip"));
                }
            } else {
                if (schematic.isIntegrityViolation(player, itemStack, materials, slot)) {
                    this.tooltip.add(Component.translatable("tetra.workbench.schematic_detail.integrity_tooltip"));
                }
                if (!schematic.checkTools(itemStack, materials, availableTools)) {
                    this.tooltip.add(Component.translatable("tetra.workbench.schematic_detail.tools_tooltip"));
                }
                if (!player.isCreative() && player.experienceLevel < schematic.getExperienceCost(itemStack, materials, slot)) {
                    this.tooltip.add(Component.translatable("tetra.workbench.schematic_detail.level_tooltip"));
                }
            }
        }
        this.updateColors();
    }

    private List<String> getDestabilizationChance(ItemStack itemStack, float severity) {
        return (List<String>) ((Stream) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getMajorModules(itemStack)).map(Arrays::stream).orElseGet(Stream::empty)).filter(Objects::nonNull).filter(module -> module.getMagicCapacity(itemStack) < 0).map(module -> String.format("  %s%s: %s%.0f%%", ChatFormatting.WHITE, module.getName(itemStack), ChatFormatting.YELLOW, module.getDestabilizationChance(itemStack, severity) * 100.0F)).collect(Collectors.toList());
    }

    private boolean hasEmptyMaterial(UpgradeSchematic schematic, ItemStack[] materials) {
        for (int i = 0; i < schematic.getNumMaterialSlots(); i++) {
            if (materials[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInsufficientQuantities(UpgradeSchematic schematic, ItemStack itemStack, String slot, ItemStack[] materials) {
        for (int i = 0; i < schematic.getNumMaterialSlots(); i++) {
            if (schematic.acceptsMaterial(itemStack, slot, i, materials[i])) {
                int requiredCount = schematic.getRequiredQuantity(itemStack, i, materials[i]);
                if (!materials[i].isEmpty() && requiredCount > materials[i].getCount()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onFocus() {
        this.updateColors();
    }

    @Override
    protected void onBlur() {
        this.updateColors();
    }

    private void updateColors() {
        if (this.enabled && this.hasFocus()) {
            this.label.setColor(16777164);
            this.backdrop.setColor(16777164);
        } else {
            this.label.setColor(this.labelColor);
            this.backdrop.setColor(this.backdropColor);
        }
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : null;
    }
}