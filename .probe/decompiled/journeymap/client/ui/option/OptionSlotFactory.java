package journeymap.client.ui.option;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import journeymap.client.Constants;
import journeymap.client.api.option.KeyedEnum;
import journeymap.client.properties.ClientCategory;
import journeymap.client.ui.component.CheckBox;
import journeymap.client.ui.component.FloatSliderButton;
import journeymap.client.ui.component.IntSliderButton;
import journeymap.client.ui.component.PropertyDropdownButton;
import journeymap.client.ui.component.Slot;
import journeymap.client.ui.component.TextFieldButton;
import journeymap.common.Journeymap;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.ConfigField;
import journeymap.common.properties.config.CustomField;
import journeymap.common.properties.config.EnumField;
import journeymap.common.properties.config.FloatField;
import journeymap.common.properties.config.IntegerField;
import journeymap.common.properties.config.StringField;

public class OptionSlotFactory {

    protected static final Charset UTF8 = Charset.forName("UTF-8");

    protected static BufferedWriter docWriter;

    protected static File docFile;

    protected static boolean generateDocs = false;

    public static List<CategorySlot> getOptionSlots(Map<Category, List<SlotMetadata>> toolbars, Map<Category, PropertiesBase> slotMap) {
        return getOptionSlots(toolbars, slotMap, false, false);
    }

    public static List<CategorySlot> getOptionSlots(Map<Category, List<SlotMetadata>> toolbars, Map<Category, PropertiesBase> slotMap, boolean viewOnly, boolean useTargetCategory) {
        HashMap<Category, List<SlotMetadata>> mergedMap = new HashMap();
        slotMap.forEach((category, propertiesBase) -> addSlots(mergedMap, category, propertiesBase, useTargetCategory));
        List<CategorySlot> categories = new ArrayList();
        for (Entry<Category, List<SlotMetadata>> entry : mergedMap.entrySet()) {
            Category category = (Category) entry.getKey();
            CategorySlot categorySlot = new CategorySlot(category);
            for (SlotMetadata val : (List) entry.getValue()) {
                categorySlot.add(new ButtonListSlot(categorySlot).add(val));
            }
            if (toolbars.containsKey(category)) {
                ButtonListSlot toolbarSlot = new ButtonListSlot(categorySlot);
                for (SlotMetadata toolbar : (List) toolbars.get(category)) {
                    toolbarSlot.add(toolbar);
                }
                categorySlot.add(toolbarSlot);
            }
            categories.add(categorySlot);
        }
        if (viewOnly) {
            mergedMap.values().forEach(slotList -> slotList.forEach(slot -> slot.button.setEnabled(false)));
        }
        Collections.sort(categories);
        int count = 0;
        for (CategorySlot categorySlot : categories) {
            count += categorySlot.size();
        }
        if (generateDocs) {
            ensureDocFile();
            for (Slot rootSlot : categories) {
                CategorySlot categorySlot = (CategorySlot) rootSlot;
                if (categorySlot.category != ClientCategory.MiniMap2) {
                    doc(categorySlot);
                    docTable(true);
                    categorySlot.sort();
                    for (SlotMetadata childSlot : categorySlot.getAllChildMetadata()) {
                        doc(childSlot, categorySlot.getCategory() == ClientCategory.Advanced);
                    }
                    docTable(false);
                }
            }
            endDoc();
        }
        return categories;
    }

    protected static void addSlots(HashMap<Category, List<SlotMetadata>> mergedMap, Category inheritedCategory, PropertiesBase properties, boolean useTargetCategory) {
        Class<? extends PropertiesBase> propertiesClass = properties.getClass();
        HashMap<Category, List<SlotMetadata>> slots = buildSlots(null, inheritedCategory, propertiesClass, properties, useTargetCategory);
        for (Entry<Category, List<SlotMetadata>> entry : slots.entrySet()) {
            Category category = (Category) entry.getKey();
            if (category == Category.Inherit) {
                if (useTargetCategory) {
                    continue;
                }
                category = inheritedCategory;
            } else if (category != inheritedCategory && !useTargetCategory && category.isUnique()) {
                continue;
            }
            List<SlotMetadata> slotMetadataList = null;
            if (mergedMap.containsKey(category)) {
                slotMetadataList = (List<SlotMetadata>) mergedMap.get(category);
            } else {
                slotMetadataList = new ArrayList();
                mergedMap.put(category, slotMetadataList);
            }
            slotMetadataList.addAll((Collection) entry.getValue());
        }
    }

    protected static HashMap<Category, List<SlotMetadata>> buildSlots(HashMap<Category, List<SlotMetadata>> map, Category inheritedCategory, Class<? extends PropertiesBase> propertiesClass, PropertiesBase properties, boolean useTargetCategory) {
        if (map == null) {
            map = new HashMap();
        }
        for (ConfigField configField : properties.getConfigFields().values()) {
            if (configField.getCategory() != Category.Hidden && (!useTargetCategory || configField.getCategory() == inheritedCategory)) {
                SlotMetadata slotMetadata = null;
                if (configField instanceof BooleanField) {
                    slotMetadata = getBooleanSlotMetadata((BooleanField) configField);
                } else if (configField instanceof IntegerField) {
                    slotMetadata = getIntegerSlotMetadata((IntegerField) configField);
                } else if (configField instanceof StringField) {
                    slotMetadata = getStringSlotMetadata((StringField) configField);
                } else if (configField instanceof EnumField) {
                    slotMetadata = getEnumSlotMetadata((EnumField) configField);
                } else if (configField instanceof CustomField) {
                    slotMetadata = getTextSlotMetadata((CustomField) configField);
                } else if (configField instanceof FloatField) {
                    slotMetadata = getFloatSlotMetadata((FloatField) configField);
                }
                if (slotMetadata != null) {
                    slotMetadata.setOrder(configField.getSortOrder());
                    Category category = configField.getCategory();
                    if (Category.Inherit.equals(category)) {
                        category = inheritedCategory;
                    }
                    List<SlotMetadata> list = (List<SlotMetadata>) map.get(category);
                    if (list == null) {
                        list = new ArrayList();
                        map.put(category, list);
                    }
                    list.add(slotMetadata);
                } else {
                    Journeymap.getLogger().warn(String.format("Unable to create config gui for %s in %s", properties.getClass().getSimpleName(), configField));
                }
            }
        }
        return map;
    }

    static String getTooltip(ConfigField configField) {
        String tooltipKey = configField.getKey() + ".tooltip";
        String tooltip = Constants.getString(tooltipKey);
        if (tooltipKey.equals(tooltip)) {
            tooltip = null;
        }
        return tooltip;
    }

    static SlotMetadata<Boolean> getBooleanSlotMetadata(BooleanField field) {
        String name = Constants.getString(field.getKey());
        String tooltip = getTooltip(field);
        String defaultTip = Constants.getString("jm.config.default", field.getDefaultValue());
        boolean advanced = field.getCategory() == ClientCategory.Advanced;
        CheckBox button = new CheckBox(name, field);
        SlotMetadata<Boolean> slotMetadata = new SlotMetadata<>(button, name, tooltip, defaultTip, field.getDefaultValue(), advanced);
        slotMetadata.setMasterPropertyForCategory(field.isCategoryMaster());
        if (field.isCategoryMaster()) {
            button.setLabelColors(Integer.valueOf(65535), null, null);
        }
        return slotMetadata;
    }

    static SlotMetadata<Integer> getIntegerSlotMetadata(IntegerField field) {
        String name = Constants.getString(field.getKey());
        String tooltip = getTooltip(field);
        String defaultTip = Constants.getString("jm.config.default_numeric", field.getMinValue(), field.getMaxValue(), field.getDefaultValue());
        boolean advanced = field.getCategory() == ClientCategory.Advanced;
        IntSliderButton button = new IntSliderButton(field, name + " : ", "", true);
        button.setDefaultStyle(false);
        button.setDrawBackground(false);
        return new SlotMetadata<>(button, name, tooltip, defaultTip, field.getDefaultValue(), advanced);
    }

    static SlotMetadata<Float> getFloatSlotMetadata(FloatField field) {
        String name = Constants.getString(field.getKey());
        String tooltip = getTooltip(field);
        String defaultTip = Constants.getString("jm.config.default_numeric", field.getMinValue(), field.getMaxValue(), field.getDefaultValue());
        boolean advanced = field.getCategory() == ClientCategory.Advanced;
        FloatSliderButton button = new FloatSliderButton(field, name + " : ", "", field.getMinValue(), field.getMaxValue());
        button.setDefaultStyle(false);
        button.setDrawBackground(false);
        return new SlotMetadata<>(button, name, tooltip, defaultTip, field.getDefaultValue(), advanced);
    }

    static SlotMetadata<String> getStringSlotMetadata(StringField field) {
        try {
            String name = Constants.getString(field.getKey());
            String tooltip = getTooltip(field);
            boolean advanced = field.getCategory() == ClientCategory.Advanced;
            PropertyDropdownButton<String> button = null;
            String defaultTip = null;
            if (LocationFormat.IdProvider.class.isAssignableFrom(field.getValuesProviderClass())) {
                button = new LocationFormat.Button(field);
                defaultTip = Constants.getString("jm.config.default", ((LocationFormat.Button) button).getLabel(field.getDefaultValue()));
            } else {
                button = new PropertyDropdownButton<>(field.getValidValues(), name, field);
                defaultTip = Constants.getString("jm.config.default", Constants.getString(field.getDefaultValue()));
            }
            button.setDefaultStyle(false);
            button.setDrawBackground(false);
            SlotMetadata<String> slotMetadata = new SlotMetadata<>(button, name, tooltip, defaultTip, field.getDefaultValue(), advanced);
            slotMetadata.setValueList(field.getValidValues());
            return slotMetadata;
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    private static SlotMetadata getTextSlotMetadata(CustomField field) {
        try {
            String name = Constants.getString(field.getKey());
            String tooltip = getTooltip(field);
            boolean advanced = field.getCategory() == ClientCategory.Advanced;
            TextFieldButton button = null;
            String defaultTip = null;
            button = new TextFieldButton(field);
            defaultTip = Constants.getString("jm.config.default", field.getDefaultValue());
            return new SlotMetadata<>(button, name, tooltip, defaultTip, field.getDefaultValue(), advanced);
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    static SlotMetadata<Enum> getEnumSlotMetadata(EnumField field) {
        try {
            String name = Constants.getString(field.getKey());
            String tooltip = getTooltip(field);
            boolean advanced = field.getCategory() == ClientCategory.Advanced;
            PropertyDropdownButton<Enum> button = new PropertyDropdownButton<>(field.getValidValues(), name, field);
            String defaultValue = Constants.getString(((KeyedEnum) field.getDefaultValue()).getKey());
            String defaultTip = Constants.getString("jm.config.default", defaultValue);
            button.setDefaultStyle(false);
            button.setDrawBackground(false);
            SlotMetadata<Enum> slotMetadata = new SlotMetadata<>(button, name, tooltip, defaultTip, field.getDefaultValue(), advanced);
            slotMetadata.setValueList(Arrays.asList(field.getValidValues()));
            return slotMetadata;
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    static void ensureDocFile() {
        if (docFile == null) {
            docFile = new File(Constants.JOURNEYMAP_DIR, "journeymap-options-wiki.txt");
            try {
                if (docFile.exists()) {
                    docFile.delete();
                }
                Files.createParentDirs(docFile);
                docWriter = Files.newWriter(docFile, UTF8);
                docWriter.append(String.format("<!-- Generated %s -->", new Date()));
                docWriter.newLine();
                docWriter.append("=== Overview ===");
                docWriter.newLine();
                docWriter.append("{{version|5.0.0|page}}");
                docWriter.newLine();
                docWriter.append("This page lists all of the available options which can be configured in-game using the JourneyMap [[Options Manager]].");
                docWriter.append("(Note: All of this information can also be obtained from the tooltips within the [[Options Manager]] itself.) <br clear/> <br clear/>");
                docWriter.newLine();
            } catch (IOException var1) {
                var1.printStackTrace();
            }
        }
    }

    static void doc(CategorySlot categorySlot) {
        try {
            docWriter.newLine();
            docWriter.append(String.format("==%s==", categorySlot.getCategory().getName().replace("Preset 1", "Preset (1 and 2)")));
            docWriter.newLine();
            docWriter.append(String.format("''%s''", ((SlotMetadata) categorySlot.getMetadata().iterator().next()).tooltip.replace("Preset 1", "Preset (1 and 2)")));
            docWriter.newLine();
            docWriter.newLine();
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    static void docTable(boolean start) {
        try {
            if (start) {
                docWriter.append("{| class=\"wikitable\" style=\"cellpadding=\"4\"");
                docWriter.newLine();
                docWriter.append("! scope=\"col\" | Option");
                docWriter.newLine();
                docWriter.append("! scope=\"col\" | Purpose");
                docWriter.newLine();
                docWriter.append("! scope=\"col\" | Range / Default Value");
                docWriter.newLine();
                docWriter.append("|-");
                docWriter.newLine();
            } else {
                docWriter.append("|}");
                docWriter.newLine();
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    static void doc(SlotMetadata slotMetadata, boolean advanced) {
        try {
            String color = advanced ? "red" : "black";
            docWriter.append(String.format("| style=\"text-align:right; white-space: nowrap; font-weight:bold; padding:6px; color:%s\" | %s", color, slotMetadata.getName()));
            docWriter.newLine();
            docWriter.append(String.format("| %s ", slotMetadata.tooltip));
            if (slotMetadata.getValueList() != null) {
                docWriter.append(String.format("<br/><em>Choices available:</em> <code>%s</code>", Joiner.on(", ").join(slotMetadata.getValueList())));
            }
            docWriter.newLine();
            docWriter.append(String.format("| <code>%s</code>", slotMetadata.range.replace("[", "").replace("]", "").trim()));
            docWriter.newLine();
            docWriter.append("|-");
            docWriter.newLine();
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    static void endDoc() {
        try {
            docFile = null;
            docWriter.flush();
            docWriter.close();
        } catch (IOException var1) {
            var1.printStackTrace();
        }
    }
}