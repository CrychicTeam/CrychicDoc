package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiItem;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.blocks.workbench.gui.ToolRequirementGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.GuiItemRolling;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.ZOffsetGui;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.data.TierData;

@ParametersAreNonnullByDefault
public class HoloMaterialDetailGui extends GuiElement {

    private final GuiElement content;

    private final GuiString label;

    private final GuiTexture labelHighlight;

    private final GuiItemRolling icon;

    private final GuiElement modifiers;

    private final GuiElement requiredTools;

    private final KeyframeAnimation openAnimation;

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    private boolean hasSelected = false;

    public HoloMaterialDetailGui(int x, int y, int width) {
        super(x, y, width, 100);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addChild(new HoloCrossGui(74 + i * 40 + j % 2 * 20, j * 20 + 32, (int) (Math.random() * 800.0) + i * 1000, 0.3F - (float) i * 0.08F).setAttachment(GuiAttachment.topCenter));
                this.addChild(new HoloCrossGui(-(74 + i * 40 + j % 2 * 20), j * 20 + 32, (int) (Math.random() * 800.0) + i * 1000, 0.3F - (float) i * 0.08F).setAttachment(GuiAttachment.topCenter));
            }
        }
        this.content = new GuiElement(0, 0, width, 100);
        this.addChild(this.content);
        this.requiredTools = new GuiHorizontalLayoutGroup(0, 70, 20, 4);
        this.requiredTools.setAttachment(GuiAttachment.topCenter);
        this.content.addChild(this.requiredTools);
        this.content.addChild(new HoloMaterialStatGui(30, 20, "primary", LabelGetterBasic.singleDecimalLabel, data -> data.primary).setAttachment(GuiAttachment.topCenter));
        this.content.addChild(new HoloMaterialStatGui(50, 40, "secondary", LabelGetterBasic.singleDecimalLabel, data -> data.secondary).setAttachment(GuiAttachment.topCenter));
        this.content.addChild(new HoloMaterialStatGui(30, 60, "tertiary", LabelGetterBasic.singleDecimalLabel, data -> data.tertiary).setAttachment(GuiAttachment.topCenter));
        this.content.addChild(new HoloMaterialStatGui(70, 20, "tool_level", LabelGetterBasic.integerLabel, data -> (float) data.toolLevel).setAttachment(GuiAttachment.topCenter));
        this.content.addChild(new HoloMaterialStatGui(70, 60, "tool_efficiency", LabelGetterBasic.singleDecimalLabel, data -> data.toolEfficiency).setAttachment(GuiAttachment.topCenter));
        this.content.addChild(new HoloMaterialStatGui(-30, 20, "durability", LabelGetterBasic.integerLabel, data -> data.durability).setAttachment(GuiAttachment.topCenter));
        this.content.addChild(new HoloMaterialIntegrityStatGui(-50, 40).setAttachment(GuiAttachment.topCenter));
        this.content.addChild(new HoloMaterialStatGui(-30, 60, "magic_capacity", LabelGetterBasic.integerLabel, data -> (float) data.magicCapacity).setAttachment(GuiAttachment.topCenter));
        this.content.addChild(new GuiTexture(0, 40, 29, 29, 97, 0, GuiTextures.workbench).setColor(2236962).setAttachment(GuiAttachment.topCenter));
        this.labelHighlight = new GuiTexture(0, 44, 3, 21, 110, 4, GuiTextures.workbench);
        this.labelHighlight.setOpacity(0.7F);
        this.labelHighlight.setAttachment(GuiAttachment.topCenter);
        this.content.addChild(this.labelHighlight);
        this.icon = new GuiItemRolling(0, 46).setCountVisibility(GuiItem.CountMode.never);
        this.icon.setAttachment(GuiAttachment.topCenter);
        this.content.addChild(this.icon);
        ZOffsetGui labelOffset = new ZOffsetGui(0, 50, 200.0);
        labelOffset.setAttachment(GuiAttachment.topCenter);
        this.label = new GuiStringOutline(0, 0, "");
        this.label.setAttachment(GuiAttachment.topCenter);
        labelOffset.addChild(this.label);
        this.content.addChild(labelOffset);
        this.modifiers = new GuiElement(0, 0, 0, 0);
        this.modifiers.setAttachment(GuiAttachment.topCenter);
        this.content.addChild(this.modifiers);
        this.openAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY((float) (y - 4), (float) y)).withDelay(120);
        this.showAnimation = new KeyframeAnimation(60, this.content).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY(0.0F));
        this.hideAnimation = new KeyframeAnimation(60, this.content).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateY(-5.0F)).onStop(complete -> {
            if (complete) {
                this.isVisible = false;
            }
        });
    }

    public void update(MaterialData selected, MaterialData hovered) {
        MaterialData current = selected != null ? selected : hovered;
        MaterialData preview = hovered != null ? hovered : current;
        if (this.hasSelected == (selected == null)) {
            if (selected != null) {
                this.getChildren(HoloCrossGui.class).forEach(HoloCrossGui::animateOpen);
            } else {
                this.getChildren(HoloCrossGui.class).forEach(elementx -> {
                    elementx.stopAnimations();
                    elementx.setOpacity(0.0F);
                });
            }
        }
        this.hasSelected = selected != null;
        if (current != null) {
            String labelString = I18n.exists("tetra.material." + preview.key) ? I18n.get("tetra.material." + preview.key) : preview.key;
            this.label.setString(Minecraft.getInstance().font.plainSubstrByWidth(labelString, 70));
            this.labelHighlight.setColor(preview.tints.glyph);
            this.icon.setItems(preview.material.getApplicableItemStacks());
            this.requiredTools.clearChildren();
            ((Stream) Optional.ofNullable(preview.requiredTools).map(TierData::getLevelMap).map(Map::entrySet).map(Collection::stream).orElseGet(Stream::empty)).map(entry -> new ToolRequirementGui(0, 0, (ToolAction) entry.getKey(), "tetra.tool." + ((ToolAction) entry.getKey()).name() + ".material_requirement").setTooltipRequirementVisibility(false).updateRequirement((Integer) entry.getValue(), 0)).forEach(this.requiredTools::addChild);
            this.content.getChildren(HoloMaterialStatGui.class).forEach(stat -> stat.update(current, preview));
            this.modifiers.clearChildren();
            Set<ItemEffect> currentEffects = current.effects.getValues();
            Set<ItemEffect> previewEffects = preview.effects.getValues();
            Stream.concat(currentEffects.stream(), previewEffects.stream()).distinct().map(effect -> new HoloMaterialEffectGui(0, 0, effect.getKey(), currentEffects.contains(effect), previewEffects.contains(effect))).forEach(this.modifiers::addChild);
            Set<String> currentImprovements = current.improvements.keySet();
            Set<String> previewImprovements = preview.improvements.keySet();
            Stream.concat(currentImprovements.stream(), previewImprovements.stream()).distinct().map(improvement -> new HoloMaterialImprovementGui(0, 0, improvement, currentImprovements.contains(improvement), previewImprovements.contains(improvement))).forEach(this.modifiers::addChild);
            Collection<String> currentFeatures = Arrays.asList(current.features);
            Collection<String> previewFeatures = Arrays.asList(preview.features);
            Stream.concat(currentFeatures.stream(), previewFeatures.stream()).distinct().map(feature -> new HoloMaterialFeatureGui(0, 0, feature, currentFeatures.contains(feature), previewFeatures.contains(feature))).forEach(this.modifiers::addChild);
            for (int i = 0; i < this.modifiers.getNumChildren(); i++) {
                GuiElement element = this.modifiers.getChild(i);
                int offset = i > 0 ? i + 2 : i + 1;
                int y = offset % 3;
                element.setX(60 + y % 2 * 20 + offset / 3 * 40);
                element.setY(20 + y * 20);
            }
            this.show();
        } else {
            this.hide();
        }
    }

    public void animateOpen() {
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
}