package journeymap.client.api.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import journeymap.client.api.event.RegistryEvent;
import journeymap.client.api.option.BooleanOption;
import journeymap.client.api.option.Config;
import journeymap.client.api.option.CustomIntegerOption;
import journeymap.client.api.option.CustomTextOption;
import journeymap.client.api.option.EnumOption;
import journeymap.client.api.option.FloatOption;
import journeymap.client.api.option.IntegerOption;
import journeymap.client.api.option.Option;
import journeymap.client.api.option.OptionCategory;
import journeymap.client.api.option.OptionsRegistry;
import journeymap.client.properties.AddonProperties;
import journeymap.client.properties.ClientCategory;
import journeymap.common.Journeymap;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.ConfigField;
import journeymap.common.properties.config.CustomField;
import journeymap.common.properties.config.EnumField;
import journeymap.common.properties.config.FloatField;
import journeymap.common.properties.config.IntegerField;

public class OptionsDisplayFactory {

    public static final Map<String, AddonProperties> PROPERTIES_REGISTRY = new HashMap();

    public static final Map<String, Map<String, ConfigField<?>>> MOD_FIELD_REGISTRY = new HashMap();

    private final List<AddonProperties> addonPropertiesList = new ArrayList();

    public OptionsDisplayFactory() {
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(new RegistryEvent.InfoSlotRegistryEvent(new InfoSlotFactory()));
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(new RegistryEvent.OptionsRegistryEvent());
    }

    public static void register(String modId, AddonProperties prop) {
        PROPERTIES_REGISTRY.put(modId, prop);
    }

    public static void register(String modId, Map<String, ConfigField<?>> fieldMap) {
        MOD_FIELD_REGISTRY.put(modId, fieldMap);
    }

    public static Map<String, ConfigField<?>> getAllFields() {
        return (Map<String, ConfigField<?>>) MOD_FIELD_REGISTRY.values().stream().flatMap(map -> map.entrySet().stream()).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    public OptionsDisplayFactory buildAddonProperties() {
        OptionsRegistry.OPTION_REGISTRY.forEach((modId, map) -> {
            AddonProperties prop = new AddonProperties();
            Map<String, ConfigField<?>> fieldMap = new HashMap();
            map.forEach((name, option) -> {
                ConfigField field = this.createField(option);
                if (field != null) {
                    fieldMap.put(option.getFieldName(), field);
                }
            });
            prop.setName(modId);
            prop.setFieldMap(fieldMap);
            this.addonPropertiesList.add(prop);
            register(modId, fieldMap);
            register(modId, prop);
        });
        return this;
    }

    public OptionsDisplayFactory load() {
        this.addonPropertiesList.forEach(PropertiesBase::load);
        return this;
    }

    public void save() {
        this.addonPropertiesList.forEach(PropertiesBase::save);
    }

    private ConfigField createField(Option option) {
        try {
            ConfigField configField = null;
            int sortOrder = option.getSortOrder();
            Category category = this.getCategory(option);
            if (option instanceof BooleanOption booleanOption) {
                configField = new BooleanField(category, option.getLabel(), booleanOption.getDefaultValue(), booleanOption.isMaster(), sortOrder);
            } else if (option instanceof EnumOption enumOption) {
                configField = new EnumField(category, option.getLabel(), (Enum) enumOption.getDefaultValue(), sortOrder);
            } else if (option instanceof CustomTextOption customOption) {
                configField = new CustomField(category, option.getLabel(), customOption.getDefaultValue(), sortOrder);
            } else if (option instanceof CustomIntegerOption customOption) {
                configField = new CustomField(category, option.getLabel(), customOption.getMinValue(), customOption.getMaxValue(), customOption.getDefaultValue(), sortOrder, customOption.getAllowNeg());
            } else if (option instanceof IntegerOption integerOption) {
                configField = new IntegerField(category, option.getLabel(), integerOption.getMinValue(), integerOption.getMaxValue(), integerOption.getDefaultValue(), sortOrder);
            } else {
                if (!(option instanceof FloatOption floatOption)) {
                    throw new UnsupportedOperationException("Type not supported for " + option.getClass().getSimpleName());
                }
                configField = new FloatField(category, option.getLabel(), floatOption.getMinValue(), floatOption.getMaxValue(), floatOption.getDefaultValue(), floatOption.getIncrementValue(), floatOption.getPrecision(), sortOrder);
            }
            Method method = Option.class.getDeclaredMethod("setConfig", Config.class);
            method.setAccessible(true);
            method.invoke(option, configField);
            return configField;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException var6) {
            Journeymap.getLogger().error("Unable to get ConfigField from option:{}", option.getClass().getName(), var6);
            return null;
        }
    }

    private Category getCategory(Option option) {
        OptionCategory optionCategory = option.getCategory();
        return "Hidden".equalsIgnoreCase(optionCategory.getLabel()) ? Category.Hidden : ClientCategory.create(optionCategory.getModId(), optionCategory.getLabel(), optionCategory.getToolTip());
    }
}