package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiItem;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.blocks.workbench.gui.ToolRequirementGui;
import se.mickelus.tetra.gui.GuiItemRolling;
import se.mickelus.tetra.gui.GuiSynergyIndicator;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.CraftingContext;
import se.mickelus.tetra.module.schematic.OutcomePreview;
import se.mickelus.tetra.module.schematic.SchematicType;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;
import se.mickelus.tetra.properties.PropertyHelper;

@ParametersAreNonnullByDefault
public class HoloVariantDetailGui extends GuiElement {

    private final GuiHorizontalLayoutGroup header;

    private final KeyframeAnimation foldAnimation;

    private final KeyframeAnimation unfoldAnimation;

    private final GuiString variantLabel;

    private final GuiSynergyIndicator synergyIndicator;

    private final GuiElement requiredTools;

    private final GuiItemRolling material;

    private final HoloStatsGui stats;

    private final Map<ToolAction, Integer> availableToolLevels;

    private final HoloImprovementButton improvementButton;

    private final HoloImprovementListGui improvements;

    private final KeyframeAnimation openAnimation;

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    private final List<OutcomeStack> selectedOutcomes;

    private final int originalY;

    private Runnable populateImprovements;

    private OutcomePreview variantOutcome;

    private OutcomePreview currentOutcome;

    private String slot;

    private OutcomePreview hoveredImprovement;

    public HoloVariantDetailGui(int x, int y, int width, Consumer<OutcomePreview> onVariantOpen) {
        super(x, y, width, 100);
        this.originalY = y;
        this.selectedOutcomes = new LinkedList();
        this.header = new GuiHorizontalLayoutGroup(0, 0, 20, 5);
        this.addChild(this.header);
        this.variantLabel = new GuiString(0, 0, "");
        this.header.addChild(this.variantLabel);
        this.synergyIndicator = new GuiSynergyIndicator(0, -1, true);
        this.header.addChild(this.synergyIndicator);
        GuiElement materialWrapper = new HoloVariantDetailGui.MaterialWrapper(0, -4);
        this.material = new GuiItemRolling(0, 0).setCountVisibility(GuiItem.CountMode.always);
        materialWrapper.addChild(this.material);
        this.header.addChild(materialWrapper);
        this.requiredTools = new GuiElement(0, -3, width, this.height);
        this.header.addChild(this.requiredTools);
        Player player = Minecraft.getInstance().player;
        this.availableToolLevels = (Map<ToolAction, Integer>) Stream.of(PropertyHelper.getPlayerToolLevels(player), PropertyHelper.getToolbeltToolLevels(player)).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Math::max));
        this.stats = new HoloStatsGui(-5, 24);
        this.addChild(this.stats);
        this.improvementButton = new HoloImprovementButton(0, 64, () -> onVariantOpen.accept(this.variantOutcome));
        this.improvementButton.setAttachment(GuiAttachment.topCenter);
        this.addChild(this.improvementButton);
        this.improvements = new HoloImprovementListGui(0, 78, width, 0, this::onImprovementHover, this::onImprovementBlur, this::onImprovementSelect);
        this.addChild(this.improvements);
        this.openAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY((float) (y - 4), (float) y)).withDelay(120);
        this.showAnimation = new KeyframeAnimation(60, this).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY((float) y));
        this.hideAnimation = new KeyframeAnimation(60, this).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateY((float) (y - 5))).onStop(complete -> {
            if (complete) {
                this.isVisible = false;
            }
        });
        this.foldAnimation = new KeyframeAnimation(60, this).applyTo(new Applier.TranslateY(0.0F));
        this.unfoldAnimation = new KeyframeAnimation(100, this).applyTo(new Applier.TranslateY((float) y));
    }

    public void updateVariant(OutcomePreview selectedOutcome, OutcomePreview hoveredOutcome, String slot) {
        this.variantOutcome = selectedOutcome;
        this.currentOutcome = selectedOutcome;
        this.slot = slot;
        if (selectedOutcome == null && hoveredOutcome == null) {
            this.hide();
        } else {
            OutcomePreview baseOutcome = hoveredOutcome != null ? hoveredOutcome : selectedOutcome;
            this.variantLabel.setString(I18n.get(ItemModule.getName(baseOutcome.moduleKey, baseOutcome.variantKey)));
            this.synergyIndicator.update(baseOutcome.itemStack, slot);
            Player player = Minecraft.getInstance().player;
            ItemStack improvementStack = baseOutcome.itemStack;
            CraftingContext context = new CraftingContext(null, null, null, player, improvementStack, slot, new ResourceLocation[0]);
            UpgradeSchematic[] improvementSchematics = (UpgradeSchematic[]) Arrays.stream(SchematicRegistry.getSchematics(context)).filter(improvementSchematic -> SchematicType.improvement.equals(improvementSchematic.getType())).toArray(UpgradeSchematic[]::new);
            this.improvementButton.updateCount(improvementSchematics.length);
            this.populateImprovements = () -> {
                this.improvements.updateSchematics(improvementStack, slot, improvementSchematics);
                this.populateImprovements = null;
            };
            this.requiredTools.clearChildren();
            baseOutcome.tools.getLevelMap().forEach((tool, level) -> {
                ToolRequirementGui requirement = new ToolRequirementGui(this.requiredTools.getNumChildren() * 20, 0, tool, "tetra.tool." + tool.name() + ".craft_requirement");
                requirement.updateRequirement(level, (Integer) this.availableToolLevels.getOrDefault(tool, 0));
                this.requiredTools.addChild(requirement);
            });
            this.material.setItems(baseOutcome.materials);
            this.updateStats(selectedOutcome, hoveredOutcome);
            this.header.forceLayout();
            this.show();
        }
    }

    public void onImprovementSelect(OutcomeStack selectedStack) {
        boolean wasRemoved = this.selectedOutcomes.removeIf(stackx -> stackx.equals(selectedStack));
        if (!wasRemoved) {
            this.selectedOutcomes.add(selectedStack);
        }
        this.currentOutcome = this.variantOutcome.clone();
        for (OutcomeStack stack : this.selectedOutcomes) {
            for (OutcomePreview preview : stack.schematic.getPreviews(this.currentOutcome.itemStack, this.slot)) {
                if (preview.equals(stack.preview)) {
                    this.currentOutcome = preview;
                    break;
                }
            }
        }
        this.selectedOutcomes.removeIf(stackx -> !stackx.preview.isApplied(this.currentOutcome.itemStack, this.slot));
        this.improvements.updateSelection(this.currentOutcome.itemStack, this.selectedOutcomes);
        this.updateStats(this.currentOutcome, this.currentOutcome);
    }

    private void onImprovementHover(OutcomePreview improvement) {
        this.updateStats(this.currentOutcome, improvement);
        this.hoveredImprovement = improvement;
    }

    private void onImprovementBlur(OutcomePreview improvement) {
        if (improvement.equals(this.hoveredImprovement)) {
            this.updateStats(this.currentOutcome, null);
            this.hoveredImprovement = null;
        }
    }

    public void updateStats(OutcomePreview selectedOutcome, OutcomePreview hoveredOutcome) {
        ItemStack baseStack = hoveredOutcome != null ? hoveredOutcome.itemStack : (selectedOutcome != null ? selectedOutcome.itemStack : ItemStack.EMPTY);
        this.stats.update(selectedOutcome != null ? selectedOutcome.itemStack : baseStack, baseStack, null, null, Minecraft.getInstance().player);
    }

    public void animateOpen() {
        this.stats.realignBars();
        this.openAnimation.start();
    }

    public void show() {
        this.hideAnimation.stop();
        this.setVisible(true);
        this.showAnimation.start();
    }

    public void hide() {
        this.showAnimation.stop();
        this.hideAnimation.start();
    }

    public void forceHide() {
        this.setY(this.originalY);
        this.setOpacity(0.0F);
        this.improvements.forceHide();
        this.improvementButton.setVisible(false);
    }

    public void showImprovements() {
        if (this.populateImprovements != null) {
            this.populateImprovements.run();
        }
        this.unfoldAnimation.stop();
        this.foldAnimation.start();
        this.improvements.show();
        this.improvementButton.hide();
    }

    public void hideImprovements() {
        this.currentOutcome = this.variantOutcome;
        this.selectedOutcomes.clear();
        if (this.currentOutcome != null) {
            this.updateStats(this.currentOutcome, null);
        }
        this.foldAnimation.stop();
        this.unfoldAnimation.start();
        this.improvements.hide();
        this.improvementButton.show();
    }

    static class MaterialWrapper extends GuiElement {

        public MaterialWrapper(int x, int y) {
            super(x, y, 16, 16);
        }

        @Override
        public List<Component> getTooltipLines() {
            if (this.hasFocus()) {
                List<Component> tooltip = super.getTooltipLines();
                if (tooltip != null && tooltip.size() > 0) {
                    return ImmutableList.of(Component.translatable("tetra.holo.craft.material_requirement", tooltip.get(0)));
                }
            }
            return null;
        }
    }
}