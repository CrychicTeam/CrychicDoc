package snownee.kiwi.config;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.ColorFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.DoubleFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder;
import me.shedaniel.clothconfig2.impl.builders.FloatFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder;
import me.shedaniel.clothconfig2.impl.builders.LongFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.LongSliderBuilder;
import me.shedaniel.clothconfig2.impl.builders.StringListBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import me.shedaniel.clothconfig2.impl.builders.TextDescriptionBuilder;
import me.shedaniel.clothconfig2.impl.builders.TextFieldBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import snownee.kiwi.util.LocalizableItem;
import snownee.kiwi.util.Util;

public class ClothConfigIntegration {

    private static final Component requiresRestart = Component.translatable("kiwi.config.requiresRestart").withStyle(ChatFormatting.RED);

    public static Screen create(Screen parent, String namespace) {
        ConfigBuilder builder = ConfigBuilder.create();
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        builder.setParentScreen(parent);
        List<ConfigHandler> configs = KiwiConfigManager.allConfigs.stream().filter($ -> $.getModId().equals(namespace)).toList();
        Joiner joiner = Joiner.on('.');
        for (ConfigHandler config : configs) {
            String titleKey = "kiwi.config." + config.getTranslationKey();
            Component title = Component.translatable(titleKey);
            ConfigCategory category = builder.getOrCreateCategory(title);
            Map<String, Consumer<AbstractConfigListEntry<?>>> subCatsMap = Maps.newHashMap();
            List<SubCategoryBuilder> subCats = Lists.newArrayList();
            subCatsMap.put("", category::addEntry);
            for (ConfigHandler.Value<?> value : config.getValueMap().values()) {
                ConfigUI.Hide hide = value.getAnnotation(ConfigUI.Hide.class);
                if (hide == null) {
                    List<String> path = Lists.newArrayList(value.path.split("\\."));
                    titleKey = (String) path.remove(path.size() - 1);
                    String subCatKey = joiner.join(path);
                    Consumer<AbstractConfigListEntry<?>> subCat = (Consumer<AbstractConfigListEntry<?>>) subCatsMap.computeIfAbsent(subCatKey, $ -> {
                        String key0 = namespace + ".config." + $;
                        Component title0;
                        if (I18n.exists(key0)) {
                            title0 = Component.translatable(key0);
                        } else {
                            title0 = Component.literal(Util.friendlyText((String) path.get(path.size() - 1)));
                        }
                        SubCategoryBuilder builder0 = entryBuilder.startSubCategory(title0);
                        builder0.setExpanded(true);
                        subCats.add(builder0);
                        return builder0::add;
                    });
                    ConfigUI.TextDescription description = value.getAnnotation(ConfigUI.TextDescription.class);
                    putDescription(subCat, entryBuilder, description, false);
                    if (I18n.exists(value.translation)) {
                        title = Component.translatable(value.translation);
                    } else {
                        title = Component.literal(Util.friendlyText(titleKey));
                    }
                    AbstractConfigListEntry<?> entry = null;
                    Class<?> type = value.getType();
                    if (type == boolean.class) {
                        BooleanToggleBuilder toggle = entryBuilder.startBooleanToggle(title, (Boolean) value.value);
                        toggle.setTooltip(createComment(value));
                        toggle.setSaveConsumer(value::accept);
                        toggle.setDefaultValue((Boolean) value.defValue);
                        entry = toggle.build();
                    } else if (type == int.class) {
                        ConfigUI.Color color = value.getAnnotation(ConfigUI.Color.class);
                        if (color != null) {
                            ColorFieldBuilder field = entryBuilder.startAlphaColorField(title, (Integer) value.value);
                            field.setAlphaMode(color.alpha());
                            field.setTooltip(createComment(value));
                            field.setSaveConsumer(value::accept);
                            field.setDefaultValue((Integer) value.defValue);
                            entry = field.build();
                        } else if (value.getAnnotation(ConfigUI.Slider.class) != null) {
                            IntSliderBuilder field = entryBuilder.startIntSlider(title, (Integer) value.value, (int) value.min, (int) value.max);
                            field.setTooltip(createComment(value));
                            field.setSaveConsumer(value::accept);
                            field.setDefaultValue((Integer) value.defValue);
                            entry = field.build();
                        } else {
                            IntFieldBuilder field = entryBuilder.startIntField(title, (Integer) value.value);
                            field.setTooltip(createComment(value));
                            if (!Double.isNaN(value.min)) {
                                field.setMin((int) value.min);
                            }
                            if (!Double.isNaN(value.max)) {
                                field.setMax((int) value.max);
                            }
                            field.setSaveConsumer(value::accept);
                            field.setDefaultValue((Integer) value.defValue);
                            entry = field.build();
                        }
                    } else if (type == double.class) {
                        DoubleFieldBuilder fieldx = entryBuilder.startDoubleField(title, (Double) value.value);
                        fieldx.setTooltip(createComment(value));
                        if (!Double.isNaN(value.min)) {
                            fieldx.setMin(value.min);
                        }
                        if (!Double.isNaN(value.max)) {
                            fieldx.setMax(value.max);
                        }
                        fieldx.setSaveConsumer(value::accept);
                        fieldx.setDefaultValue((Double) value.defValue);
                        entry = fieldx.build();
                    } else if (type == float.class) {
                        FloatFieldBuilder fieldxx = entryBuilder.startFloatField(title, (Float) value.value);
                        fieldxx.setTooltip(createComment(value));
                        if (!Double.isNaN(value.min)) {
                            fieldxx.setMin((float) value.min);
                        }
                        if (!Double.isNaN(value.max)) {
                            fieldxx.setMax((float) value.max);
                        }
                        fieldxx.setSaveConsumer(value::accept);
                        fieldxx.setDefaultValue((Float) value.defValue);
                        entry = fieldxx.build();
                    } else if (type == long.class) {
                        if (value.getAnnotation(ConfigUI.Slider.class) != null) {
                            LongSliderBuilder fieldxxx = entryBuilder.startLongSlider(title, (Long) value.value, (long) value.min, (long) value.max);
                            fieldxxx.setTooltip(createComment(value));
                            fieldxxx.setSaveConsumer(value::accept);
                            fieldxxx.setDefaultValue((Long) value.defValue);
                            entry = fieldxxx.build();
                        } else {
                            LongFieldBuilder fieldxxx = entryBuilder.startLongField(title, (Long) value.value);
                            fieldxxx.setTooltip(createComment(value));
                            if (!Double.isNaN(value.min)) {
                                fieldxxx.setMin((long) value.min);
                            }
                            if (!Double.isNaN(value.max)) {
                                fieldxxx.setMax((long) value.max);
                            }
                            fieldxxx.setSaveConsumer(value::accept);
                            fieldxxx.setDefaultValue((Long) value.defValue);
                            entry = fieldxxx.build();
                        }
                    } else if (type == String.class) {
                        TextFieldBuilder fieldxxxx = entryBuilder.startTextField(title, (String) value.value);
                        fieldxxxx.setTooltip(createComment(value));
                        fieldxxxx.setSaveConsumer(value::accept);
                        fieldxxxx.setDefaultValue((String) value.defValue);
                        entry = fieldxxxx.build();
                    } else if (Enum.class.isAssignableFrom(type)) {
                        EnumSelectorBuilder<Enum<?>> fieldxxxx = entryBuilder.startEnumSelector(title, (Class<Enum<?>>) type, (Enum<?>) value.value);
                        fieldxxxx.setSaveConsumer(value::accept);
                        fieldxxxx.setDefaultValue((Enum<?>) value.defValue);
                        fieldxxxx.setEnumNameProvider($ -> $ instanceof LocalizableItem item ? item.getDisplayName().copy() : Component.literal($.name()));
                        fieldxxxx.setTooltipSupplier((Function<Enum<?>, Optional<Component[]>>) ($ -> {
                            List<Component> tooltip = Lists.newArrayList();
                            if ($ instanceof LocalizableItem item && item.getDescription() != null) {
                                tooltip.add(item.getDisplayName().copy().append(" - ").append(item.getDescription()));
                            }
                            createComment(value).map(Arrays::asList).ifPresent(tooltip::addAll);
                            return tooltip.isEmpty() ? Optional.empty() : Optional.of((Component[]) tooltip.toArray(Component[]::new));
                        }));
                        entry = fieldxxxx.build();
                    } else if (List.class.isAssignableFrom(type)) {
                        ConfigUI.ItemType itemType = (ConfigUI.ItemType) value.field.getAnnotation(ConfigUI.ItemType.class);
                        if (itemType.value() == String.class) {
                            StringListBuilder fieldxxxx = entryBuilder.startStrList(title, (List<String>) value.value);
                            fieldxxxx.setTooltip(createComment(value));
                            fieldxxxx.setSaveConsumer(value::accept);
                            fieldxxxx.setDefaultValue((List<String>) value.defValue);
                            entry = fieldxxxx.build();
                        }
                    }
                    if (entry != null) {
                        entry.setRequiresRestart(value.requiresRestart);
                        subCat.accept(entry);
                    }
                    putDescription(subCat, entryBuilder, description, true);
                }
            }
            subCats.forEach($ -> category.addEntry($.build()));
        }
        builder.setSavingRunnable(() -> configs.forEach(ConfigHandler::save));
        return builder.build();
    }

    private static void putDescription(Consumer<AbstractConfigListEntry<?>> subCat, ConfigEntryBuilder entryBuilder, ConfigUI.TextDescription description, boolean after) {
        if (description != null && description.after() == after) {
            Component component = Component.translatable(description.value());
            TextDescriptionBuilder builder = entryBuilder.startTextDescription(component);
            subCat.accept(builder.build());
        }
    }

    private static Optional<Component[]> createComment(ConfigHandler.Value<?> value) {
        List<Component> tooltip = Lists.newArrayList();
        String key = value.translation + ".desc";
        if (I18n.exists(key) && !I18n.get(key).isEmpty()) {
            tooltip.add(Component.translatable(key));
        }
        if (value.requiresRestart) {
            tooltip.add(requiresRestart);
        }
        return tooltip.isEmpty() ? Optional.empty() : Optional.of((Component[]) tooltip.toArray(Component[]::new));
    }

    static void init() {
        for (String mod : KiwiConfigManager.allConfigs.stream().map(ConfigHandler::getModId).distinct().toList()) {
            ModList.get().getModContainerById(mod).ifPresent($ -> $.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> create(screen, mod))));
        }
    }
}