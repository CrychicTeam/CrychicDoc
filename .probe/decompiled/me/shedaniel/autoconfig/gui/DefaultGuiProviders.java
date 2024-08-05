package me.shedaniel.autoconfig.gui;

import com.google.common.collect.Lists;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry;
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.ArrayUtils;

@OnlyIn(Dist.CLIENT)
public class DefaultGuiProviders {

    private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();

    private static final Function<Enum<?>, Component> DEFAULT_NAME_PROVIDER = t -> Component.translatable(t instanceof SelectionListEntry.Translatable ? ((SelectionListEntry.Translatable) t).getKey() : t.toString());

    private DefaultGuiProviders() {
    }

    public static GuiRegistry apply(GuiRegistry registry) {
        registry.registerAnnotationProvider((i18n, field, config, defaults, guiProvider) -> Collections.emptyList(), ConfigEntry.Gui.Excluded.class);
        registry.registerAnnotationProvider((i18n, field, config, defaults, guiProvider) -> {
            ConfigEntry.BoundedDiscrete bounds = (ConfigEntry.BoundedDiscrete) field.getAnnotation(ConfigEntry.BoundedDiscrete.class);
            return Collections.singletonList(ENTRY_BUILDER.startIntSlider(Component.translatable(i18n), Utils.getUnsafely(field, config, 0), (int) bounds.min(), (int) bounds.max()).setDefaultValue(() -> Utils.getUnsafely(field, defaults)).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build());
        }, field -> field.getType() == int.class || field.getType() == Integer.class, ConfigEntry.BoundedDiscrete.class);
        registry.registerAnnotationProvider((i18n, field, config, defaults, guiProvider) -> {
            ConfigEntry.BoundedDiscrete bounds = (ConfigEntry.BoundedDiscrete) field.getAnnotation(ConfigEntry.BoundedDiscrete.class);
            return Collections.singletonList(ENTRY_BUILDER.startLongSlider(Component.translatable(i18n), Utils.getUnsafely(field, config, 0L), bounds.min(), bounds.max()).setDefaultValue(() -> Utils.getUnsafely(field, defaults)).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build());
        }, field -> field.getType() == long.class || field.getType() == Long.class, ConfigEntry.BoundedDiscrete.class);
        registry.registerAnnotationProvider((i18n, field, config, defaults, guiProvider) -> {
            ConfigEntry.ColorPicker colorPicker = (ConfigEntry.ColorPicker) field.getAnnotation(ConfigEntry.ColorPicker.class);
            return Collections.singletonList(ENTRY_BUILDER.startColorField(Component.translatable(i18n), Utils.getUnsafely(field, config, 0)).setAlphaMode(colorPicker.allowAlpha()).setDefaultValue(() -> Utils.getUnsafely(field, defaults)).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build());
        }, field -> field.getType() == int.class || field.getType() == Integer.class, ConfigEntry.ColorPicker.class);
        registry.registerAnnotationProvider(DefaultGuiProviders::getChildren, field -> !field.getType().isPrimitive(), ConfigEntry.Gui.TransitiveObject.class);
        registry.registerAnnotationProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startSubCategory(Component.translatable(i18n), getChildren(i18n, field, config, defaults, guiProvider)).setExpanded(((ConfigEntry.Gui.CollapsibleObject) field.getAnnotation(ConfigEntry.Gui.CollapsibleObject.class)).startExpanded()).build()), field -> !field.getType().isPrimitive(), ConfigEntry.Gui.CollapsibleObject.class);
        registry.registerPredicateProvider((i18n, field, config, defaults, guiProvider) -> {
            Object[] enumConstants = field.getType().getEnumConstants();
            Enum[] enums = new Enum[enumConstants.length];
            for (int i = 0; i < enumConstants.length; i++) {
                enums[i] = (Enum) enumConstants[i];
            }
            return Collections.singletonList(ENTRY_BUILDER.startSelector(Component.translatable(i18n), enums, Utils.getUnsafely(field, config, Utils.getUnsafely(field, defaults))).setDefaultValue((Supplier<Enum>) (() -> Utils.getUnsafely(field, defaults))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build());
        }, field -> field.getType().isEnum() && field.isAnnotationPresent(ConfigEntry.Gui.EnumHandler.class) && ((ConfigEntry.Gui.EnumHandler) field.getAnnotation(ConfigEntry.Gui.EnumHandler.class)).option() == ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON);
        registry.registerPredicateProvider((i18n, field, config, defaults, guiProvider) -> {
            List<Enum<?>> enums = Arrays.asList((Enum[]) field.getType().getEnumConstants());
            return Collections.singletonList(ENTRY_BUILDER.startDropdownMenu(Component.translatable(i18n), DropdownMenuBuilder.TopCellElementBuilder.of((Enum) Utils.getUnsafely(field, config, (Enum) Utils.getUnsafely(field, defaults)), str -> {
                String s = Component.literal(str).getString();
                for (Enum<?> constant : enums) {
                    if (((Component) DEFAULT_NAME_PROVIDER.apply(constant)).getString().equals(s)) {
                        return constant;
                    }
                }
                return null;
            }, DEFAULT_NAME_PROVIDER), DropdownMenuBuilder.CellCreatorBuilder.of(DEFAULT_NAME_PROVIDER)).setSelections(enums).setDefaultValue((Supplier<Object>) (() -> Utils.getUnsafely(field, defaults))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build());
        }, field -> field.getType().isEnum());
        registry.registerPredicateProvider((i18n, field, config, defaults, registry1) -> Collections.singletonList(ENTRY_BUILDER.startIntList(Component.translatable(i18n), Utils.getUnsafely(field, config)).setDefaultValue((Supplier<List<Integer>>) (() -> Utils.getUnsafely(field, defaults))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build()), isListOfType(Integer.class));
        registry.registerPredicateProvider((i18n, field, config, defaults, registry1) -> Collections.singletonList(ENTRY_BUILDER.startLongList(Component.translatable(i18n), Utils.getUnsafely(field, config)).setDefaultValue((Supplier<List<Long>>) (() -> Utils.getUnsafely(field, defaults))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build()), isListOfType(Long.class));
        registry.registerPredicateProvider((i18n, field, config, defaults, registry1) -> Collections.singletonList(ENTRY_BUILDER.startFloatList(Component.translatable(i18n), Utils.getUnsafely(field, config)).setDefaultValue((Supplier<List<Float>>) (() -> Utils.getUnsafely(field, defaults))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build()), isListOfType(Float.class));
        registry.registerPredicateProvider((i18n, field, config, defaults, registry1) -> Collections.singletonList(ENTRY_BUILDER.startDoubleList(Component.translatable(i18n), Utils.getUnsafely(field, config)).setDefaultValue((Supplier<List<Double>>) (() -> Utils.getUnsafely(field, defaults))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build()), isListOfType(Double.class));
        registry.registerPredicateProvider((i18n, field, config, defaults, registry1) -> Collections.singletonList(ENTRY_BUILDER.startStrList(Component.translatable(i18n), Utils.getUnsafely(field, config)).setDefaultValue((Supplier<List<String>>) (() -> Utils.getUnsafely(field, defaults))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build()), isListOfType(String.class));
        registry.registerPredicateProvider((i18n, field, config, defaults, registry1) -> {
            List<Object> configValue = Utils.getUnsafely(field, config);
            Class<?> fieldTypeParam = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            Object defaultElemValue = Utils.constructUnsafely(fieldTypeParam);
            String remainingI13n = i18n.substring(0, i18n.indexOf(".option") + ".option".length());
            String classI13n = String.format("%s.%s", remainingI13n, fieldTypeParam.getSimpleName());
            return Collections.singletonList(new NestedListListEntry(Component.translatable(i18n), configValue, false, null, newValue -> Utils.setUnsafely(field, config, newValue), () -> Utils.getUnsafely(field, defaults), ENTRY_BUILDER.getResetButtonKey(), true, false, (elem, nestedListListEntry) -> {
                if (elem == null) {
                    Object newDefaultElemValue = Utils.constructUnsafely(fieldTypeParam);
                    return new MultiElementListEntry<>(Component.translatable(classI13n), newDefaultElemValue, getChildren(classI13n, fieldTypeParam, newDefaultElemValue, defaultElemValue, registry1), true);
                } else {
                    return new MultiElementListEntry<>(Component.translatable(classI13n), elem, getChildren(classI13n, fieldTypeParam, elem, defaultElemValue, registry1), true);
                }
            }));
        }, isNotListOfType(Integer.class, Long.class, Float.class, Double.class, String.class));
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startBooleanToggle(Component.translatable(i18n), Utils.getUnsafely(field, config, false)).setDefaultValue(() -> Utils.getUnsafely(field, defaults)).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).setYesNoTextSupplier(bool -> {
            String key = i18n + ".boolean." + bool;
            String translate = I18n.get(key);
            return translate.equals(key) ? Component.translatable("text.cloth-config.boolean.value." + bool) : Component.literal(translate);
        }).build()), boolean.class, Boolean.class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startIntField(Component.translatable(i18n), Utils.getUnsafely(field, config, 0)).setDefaultValue(() -> Utils.getUnsafely(field, defaults)).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build()), int.class, Integer.class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startIntList(Component.translatable(i18n), Lists.newArrayList((Integer[]) Utils.getUnsafely(field, config, new Integer[0]))).setDefaultValue((Supplier<List<Integer>>) (() -> defaults == null ? null : Lists.newArrayList((Integer[]) Utils.getUnsafely(field, defaults)))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue.toArray(new Integer[0]))).build()), Integer[].class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startIntList(Component.translatable(i18n), Lists.newArrayList((Iterable) IntStream.of(Utils.getUnsafely(field, config, new int[0])).boxed().collect(Collectors.toList()))).setDefaultValue((Supplier<List<Integer>>) (() -> defaults == null ? null : Lists.newArrayList(Arrays.asList(ArrayUtils.toObject(Utils.getUnsafely(field, defaults)))))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue.stream().mapToInt(Integer::intValue).toArray())).build()), int[].class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startLongField(Component.translatable(i18n), Utils.getUnsafely(field, config, 0L)).setDefaultValue(() -> Utils.getUnsafely(field, defaults)).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build()), long.class, Long.class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startLongList(Component.translatable(i18n), Lists.newArrayList((Long[]) Utils.getUnsafely(field, config, new Long[0]))).setDefaultValue((Supplier<List<Long>>) (() -> defaults == null ? null : Lists.newArrayList((Long[]) Utils.getUnsafely(field, defaults)))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue.toArray(new Long[0]))).build()), Long[].class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startLongList(Component.translatable(i18n), Lists.newArrayList((Iterable) LongStream.of(Utils.getUnsafely(field, config, new long[0])).boxed().collect(Collectors.toList()))).setDefaultValue((Supplier<List<Long>>) (() -> defaults == null ? null : Lists.newArrayList(Arrays.asList(ArrayUtils.toObject(Utils.getUnsafely(field, defaults)))))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue.stream().mapToLong(Long::longValue).toArray())).build()), long[].class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startFloatField(Component.translatable(i18n), Utils.getUnsafely(field, config, 0.0F)).setDefaultValue(() -> Utils.getUnsafely(field, defaults)).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build()), float.class, Float.class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startFloatList(Component.translatable(i18n), Lists.newArrayList((Float[]) Utils.getUnsafely(field, config, new Float[0]))).setDefaultValue((Supplier<List<Float>>) (() -> defaults == null ? null : Lists.newArrayList((Float[]) Utils.getUnsafely(field, defaults)))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue.toArray(new Float[0]))).build()), Float[].class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startFloatList(Component.translatable(i18n), Lists.newArrayList(Arrays.asList(ArrayUtils.toObject(Utils.getUnsafely(field, config, new float[0]))))).setDefaultValue((Supplier<List<Float>>) (() -> defaults == null ? null : Lists.newArrayList(Arrays.asList(ArrayUtils.toObject(Utils.getUnsafely(field, defaults)))))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, ArrayUtils.toPrimitive((Float[]) newValue.toArray(new Float[0])))).build()), float[].class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startDoubleField(Component.translatable(i18n), Utils.getUnsafely(field, config, 0.0)).setDefaultValue(() -> Utils.getUnsafely(field, defaults)).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build()), double.class, Double.class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startDoubleList(Component.translatable(i18n), Lists.newArrayList((Double[]) Utils.getUnsafely(field, config, new Double[0]))).setDefaultValue((Supplier<List<Double>>) (() -> defaults == null ? null : Lists.newArrayList((Double[]) Utils.getUnsafely(field, defaults)))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue.toArray(new Double[0]))).build()), Double[].class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startDoubleList(Component.translatable(i18n), Lists.newArrayList(Arrays.asList(ArrayUtils.toObject(Utils.getUnsafely(field, config, new double[0]))))).setDefaultValue((Supplier<List<Double>>) (() -> defaults == null ? null : Lists.newArrayList(Arrays.asList(ArrayUtils.toObject(Utils.getUnsafely(field, defaults)))))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, ArrayUtils.toPrimitive((Double[]) newValue.toArray(new Double[0])))).build()), double[].class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startStrField(Component.translatable(i18n), Utils.getUnsafely(field, config, "")).setDefaultValue((Supplier<String>) (() -> Utils.getUnsafely(field, defaults))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue)).build()), String.class);
        registry.registerTypeProvider((i18n, field, config, defaults, guiProvider) -> Collections.singletonList(ENTRY_BUILDER.startStrList(Component.translatable(i18n), Lists.newArrayList((String[]) Utils.getUnsafely(field, config, new String[0]))).setDefaultValue((Supplier<List<String>>) (() -> defaults == null ? null : Lists.newArrayList((String[]) Utils.getUnsafely(field, defaults)))).setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue.toArray(new String[0]))).build()), String[].class);
        registry.registerPredicateProvider((i18n, field, config, defaults, registry1) -> {
            Object configValue = Utils.getUnsafely(field, config);
            List<Object> configValueAsList = new ArrayList(Array.getLength(configValue));
            for (int i = 0; i < Array.getLength(configValue); i++) {
                configValueAsList.add(Array.get(configValue, i));
            }
            Class<?> fieldTypeParam = field.getType().getComponentType();
            Object defaultElemValue = Utils.constructUnsafely(fieldTypeParam);
            String remainingI13n = i18n.substring(0, i18n.indexOf(".option") + ".option".length());
            String classI13n = String.format("%s.%s", remainingI13n, fieldTypeParam.getSimpleName());
            return Collections.singletonList(new NestedListListEntry(Component.translatable(i18n), configValueAsList, false, null, newValue -> {
                Object[] newArray = (Object[]) Array.newInstance(fieldTypeParam, newValue.size());
                for (int i = 0; i < newValue.size(); i++) {
                    Array.set(newArray, i, newValue.get(i));
                }
                Utils.setUnsafely(field, config, newArray);
            }, () -> {
                Object o = Utils.getUnsafely(field, defaults);
                List<Object> asList = new ArrayList(Array.getLength(o));
                for (int i = 0; i < Array.getLength(o); i++) {
                    asList.add(Array.get(o, i));
                }
                return asList;
            }, ENTRY_BUILDER.getResetButtonKey(), true, false, (elem, nestedListListEntry) -> {
                if (elem == null) {
                    Object newDefaultElemValue = Utils.constructUnsafely(fieldTypeParam);
                    return new MultiElementListEntry<>(Component.translatable(classI13n), newDefaultElemValue, getChildren(classI13n, fieldTypeParam, newDefaultElemValue, defaultElemValue, registry1), true);
                } else {
                    return new MultiElementListEntry<>(Component.translatable(classI13n), elem, getChildren(classI13n, fieldTypeParam, elem, defaultElemValue, registry1), true);
                }
            }));
        }, field -> field.getType().isArray() && field.getType() != String[].class && field.getType() != int[].class && field.getType() != Integer[].class && field.getType() != long[].class && field.getType() != Long[].class && field.getType() != float[].class && field.getType() != Float[].class && field.getType() != double[].class && field.getType() != Double[].class);
        return registry;
    }

    private static List<AbstractConfigListEntry> getChildren(String i18n, Field field, Object config, Object defaults, GuiRegistryAccess guiProvider) {
        return getChildren(i18n, field.getType(), Utils.getUnsafely(field, config), Utils.getUnsafely(field, defaults), guiProvider);
    }

    private static List<AbstractConfigListEntry> getChildren(String i18n, Class<?> fieldType, Object iConfig, Object iDefaults, GuiRegistryAccess guiProvider) {
        return (List<AbstractConfigListEntry>) Arrays.stream(fieldType.getDeclaredFields()).map(iField -> {
            String iI13n = String.format("%s.%s", i18n, iField.getName());
            return guiProvider.getAndTransform(iI13n, iField, iConfig, iDefaults, guiProvider);
        }).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private static Predicate<Field> isListOfType(Type... types) {
        return field -> {
            if (List.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                return args.length == 1 && Stream.of(types).anyMatch(type -> Objects.equals(args[0], type));
            } else {
                return false;
            }
        };
    }

    private static Predicate<Field> isNotListOfType(Type... types) {
        return field -> {
            if (List.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                return args.length == 1 && Stream.of(types).noneMatch(type -> Objects.equals(args[0], type));
            } else {
                return false;
            }
        };
    }
}