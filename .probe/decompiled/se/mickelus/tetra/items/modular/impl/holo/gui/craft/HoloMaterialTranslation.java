package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.MaterialMultiplier;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.module.schematic.SchematicType;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class HoloMaterialTranslation extends GuiElement {

    private final List<Component> emptyTooltipImprovement;

    private final List<Component> emptyTooltip;

    private final GuiTexture icon = new GuiTexture(0, 0, 9, 9, 224, 0, GuiTextures.workbench);

    private List<Component> tooltip;

    public HoloMaterialTranslation(int x, int y) {
        super(x, y, 9, 9);
        this.addChild(this.icon);
        this.emptyTooltipImprovement = Collections.singletonList(Component.translatable("tetra.holo.craft.empty_translation_improvement"));
        this.emptyTooltip = Collections.singletonList(Component.translatable("tetra.holo.craft.empty_translation_module"));
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : null;
    }

    public void update(UpgradeSchematic schematic) {
        MaterialMultiplier translation = schematic.getMaterialTranslation();
        if (translation != null) {
            LinkedList<String> primary = new LinkedList();
            LinkedList<String> secondary = new LinkedList();
            LinkedList<String> tertiary = new LinkedList();
            this.extractAttributes(translation.primaryAttributes, primary);
            this.extractAttributes(translation.secondaryAttributes, secondary);
            this.extractAttributes(translation.tertiaryAttributes, tertiary);
            this.extractEffects(translation.primaryEffects, primary);
            this.extractEffects(translation.secondaryEffects, secondary);
            this.extractEffects(translation.tertiaryEffects, tertiary);
            List<String> result = new LinkedList();
            if (schematic.getType() == SchematicType.improvement) {
                result.add(I18n.get("tetra.holo.craft.translation_improvement"));
            } else {
                result.add(I18n.get("tetra.holo.craft.translation_module"));
            }
            if (translation.durability != null || translation.integrity != null) {
                result.add(" ");
            }
            if (translation.durability != null) {
                result.add(this.getStatLine("tetra.stats.durability", translation.durability.intValue()));
            }
            if (translation.integrity != null) {
                result.add(this.getStatLine("tetra.stats.integrity", translation.integrity.intValue()));
            }
            this.extractTools(translation.tools, result);
            if (!primary.isEmpty()) {
                result.add(" ");
                result.add(ChatFormatting.WHITE + I18n.get("tetra.holo.craft.materials.stat.primary") + ":");
                result.addAll(primary);
            }
            if (!secondary.isEmpty()) {
                result.add(" ");
                result.add(ChatFormatting.WHITE + I18n.get("tetra.holo.craft.materials.stat.secondary") + ":");
                result.addAll(secondary);
            }
            if (!tertiary.isEmpty()) {
                result.add(" ");
                result.add(ChatFormatting.WHITE + I18n.get("tetra.holo.craft.materials.stat.tertiary") + ":");
                result.addAll(tertiary);
            }
            this.tooltip = (List<Component>) result.stream().map(Component::m_237113_).collect(Collectors.toList());
        } else if (schematic.getType() == SchematicType.improvement) {
            this.tooltip = this.emptyTooltipImprovement;
        } else {
            this.tooltip = this.emptyTooltip;
        }
    }

    private String getStatLine(String unlocalizedStat, int value) {
        return this.getStatLine(unlocalizedStat, value, null);
    }

    private String getStatLine(String unlocalizedStat, int value, @Nullable String unlocalizedSuffix) {
        if (I18n.exists(unlocalizedStat)) {
            StringBuilder line = new StringBuilder(ChatFormatting.GRAY.toString());
            line.append(I18n.get(unlocalizedStat));
            if (unlocalizedSuffix != null) {
                line.append(" ");
                line.append(I18n.get(unlocalizedSuffix));
            }
            if (value < 0) {
                line.append(ChatFormatting.RED);
                line.append(" -");
            } else {
                line.append(ChatFormatting.GREEN);
                line.append(" +");
            }
            line.append(I18n.get("enchantment.level." + Math.abs(value)));
            return line.toString();
        } else {
            return null;
        }
    }

    private void extractAttributes(Multimap<Attribute, AttributeModifier> attributes, List<String> result) {
        if (attributes != null) {
            attributes.entries().stream().map(entry -> this.getStatLine(((Attribute) entry.getKey()).getDescriptionId(), (int) ((AttributeModifier) entry.getValue()).getAmount(), ((AttributeModifier) entry.getValue()).getOperation() != AttributeModifier.Operation.ADDITION ? "tetra.attribute.multiplier" : null)).filter(Objects::nonNull).map(line -> "  " + line).forEach(result::add);
        }
    }

    private void extractEffects(EffectData effects, List<String> result) {
        if (effects != null) {
            effects.getValues().stream().map(effect -> this.extractEffectLevel(effect, effects)).filter(Objects::nonNull).map(line -> "  " + line).forEach(result::add);
            effects.getValues().stream().map(effect -> this.extractEffectEfficiency(effect, effects)).filter(Objects::nonNull).map(line -> "  " + line).forEach(result::add);
        }
    }

    private String extractEffectLevel(ItemEffect effect, EffectData effects) {
        int level = effects.getLevel(effect);
        if (level != 0) {
            String levelKey = "tetra.stats." + effect.getKey() + ".level";
            return this.getStatLine(I18n.exists(levelKey) ? levelKey : "tetra.stats." + effect.getKey(), level);
        } else {
            return null;
        }
    }

    private String extractEffectEfficiency(ItemEffect effect, EffectData effects) {
        int efficiency = (int) effects.getEfficiency(effect);
        return efficiency != 0 ? this.getStatLine("tetra.stats." + effect.getKey() + ".efficiency", efficiency) : null;
    }

    private void extractTools(ToolData tools, List<String> result) {
        if (tools != null) {
            result.add("");
            tools.getValues().stream().map(tool -> this.extractToolLevel(tool, tools)).filter(Objects::nonNull).forEach(result::add);
            tools.getValues().stream().map(tool -> this.extractToolEfficiency(tool, tools)).filter(Objects::nonNull).forEach(result::add);
        }
    }

    private String extractToolLevel(ToolAction tool, ToolData toolData) {
        int level = toolData.getLevel(tool);
        return level != 0 ? this.getStatLine("tetra.tool." + tool.name(), level, "tetra.stats.tier_suffix") : null;
    }

    private String extractToolEfficiency(ToolAction tool, ToolData toolData) {
        int efficiency = (int) toolData.getEfficiency(tool);
        return efficiency != 0 ? this.getStatLine("tetra.tool." + tool.name(), efficiency, "tetra.stats.efficiency_suffix") : null;
    }
}