package me.shedaniel.autoconfig.gui;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefaultGuiTransformers {

    private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();

    private DefaultGuiTransformers() {
    }

    public static GuiRegistry apply(GuiRegistry registry) {
        registry.registerAnnotationTransformer((guis, i18n, field, config, defaults, guiProvider) -> (List<AbstractConfigListEntry>) guis.stream().peek(gui -> {
            if (!(gui instanceof TextListEntry)) {
                ConfigEntry.Gui.Tooltip tooltip = (ConfigEntry.Gui.Tooltip) field.getAnnotation(ConfigEntry.Gui.Tooltip.class);
                if (tooltip.count() == 0) {
                    tryRemoveTooltip(gui);
                } else if (tooltip.count() == 1) {
                    tryApplyTooltip(gui, new Component[] { Component.translatable(String.format("%s.%s", i18n, "@Tooltip")) });
                } else {
                    tryApplyTooltip(gui, (Component[]) IntStream.range(0, tooltip.count()).boxed().map(i -> String.format("%s.%s[%d]", i18n, "@Tooltip", i)).map(Component::m_237115_).toArray(Component[]::new));
                }
            }
        }).collect(Collectors.toList()), ConfigEntry.Gui.Tooltip.class);
        registry.registerAnnotationTransformer((guis, i18n, field, config, defaults, guiProvider) -> (List<AbstractConfigListEntry>) guis.stream().peek(gui -> {
            if (!(gui instanceof TextListEntry)) {
                Comment tooltip = (Comment) field.getAnnotation(Comment.class);
                Component[] text = new Component[] { Component.literal(tooltip.value()) };
                tryApplyTooltip(gui, text);
            }
        }).collect(Collectors.toList()), field -> !field.isAnnotationPresent(ConfigEntry.Gui.Tooltip.class), Comment.class);
        registry.registerAnnotationTransformer((guis, i18n, field, config, defaults, guiProvider) -> (List<AbstractConfigListEntry>) guis.stream().peek(gui -> {
            if (!(gui instanceof TextListEntry)) {
                tryRemoveTooltip(gui);
            }
        }).collect(Collectors.toList()), ConfigEntry.Gui.NoTooltip.class);
        registry.registerAnnotationTransformer((guis, i18n, field, config, defaults, guiProvider) -> {
            ArrayList<AbstractConfigListEntry> ret = new ArrayList(guis);
            String text = String.format("%s.%s", i18n, "@PrefixText");
            TextListEntry element = ENTRY_BUILDER.startTextDescription(Component.translatable(text)).build();
            String s = Component.translatable(i18n).getString().toLowerCase(Locale.ROOT);
            if (!s.isEmpty()) {
                element.appendSearchTags(Lists.newArrayList(s.split(" ")));
            }
            ret.add(0, element);
            return Collections.unmodifiableList(ret);
        }, ConfigEntry.Gui.PrefixText.class);
        registry.registerAnnotationTransformer((guis, i18n, field, config, defaults, guiProvider) -> {
            for (AbstractConfigListEntry gui : guis) {
                gui.setRequiresRestart(((ConfigEntry.Gui.RequiresRestart) field.getAnnotation(ConfigEntry.Gui.RequiresRestart.class)).value());
            }
            return guis;
        }, ConfigEntry.Gui.RequiresRestart.class);
        return registry;
    }

    private static void tryApplyTooltip(AbstractConfigListEntry gui, Component[] text) {
        if (gui instanceof TooltipListEntry tooltipGui) {
            tooltipGui.setTooltipSupplier(() -> Optional.of(text));
        }
    }

    private static void tryRemoveTooltip(AbstractConfigListEntry gui) {
        if (gui instanceof TooltipListEntry tooltipGui) {
            tooltipGui.setTooltipSupplier(() -> Optional.empty());
        }
    }
}