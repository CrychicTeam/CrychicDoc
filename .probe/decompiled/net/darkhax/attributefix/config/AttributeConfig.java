package net.darkhax.attributefix.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import net.darkhax.attributefix.Constants;
import net.darkhax.attributefix.mixin.AccessorRangedAttribute;
import net.darkhax.attributefix.temp.RegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class AttributeConfig {

    @Expose
    private Map<String, AttributeConfig.Entry> attributes = new HashMap();

    private static final Map<Attribute, Double> NEW_DEFAULT_VALUES = ImmutableMap.of(Attributes.MAX_HEALTH, 1000000.0, Attributes.ARMOR, 1000000.0, Attributes.ARMOR_TOUGHNESS, 1000000.0, Attributes.ATTACK_DAMAGE, 1000000.0, Attributes.ATTACK_KNOCKBACK, 1000000.0);

    public void applyChanges(RegistryHelper<Attribute> registry) {
        Constants.LOG.info("Applying changes for {} attributes.", this.attributes.size());
        for (java.util.Map.Entry<String, AttributeConfig.Entry> configEntry : this.attributes.entrySet()) {
            ResourceLocation attributeId = ResourceLocation.tryParse((String) configEntry.getKey());
            if (attributeId != null && registry.exists(attributeId)) {
                Attribute attribute = registry.get(attributeId);
                if (attribute instanceof RangedAttribute) {
                    RangedAttribute ranged = (RangedAttribute) attribute;
                    double minValue = ((AttributeConfig.Entry) configEntry.getValue()).min.value;
                    double maxValue = ((AttributeConfig.Entry) configEntry.getValue()).max.value;
                    if (minValue > maxValue) {
                        Constants.LOG.error("Attribute {} was configured to have a minimum value higher than it's maximum. This is not permitted!", attributeId);
                    } else {
                        AccessorRangedAttribute accessor = (AccessorRangedAttribute) attribute;
                        if (minValue != ranged.getMinValue()) {
                            Constants.LOG.debug("Modifying minimum value for {} from {} to {}.", new Object[] { attributeId, Constants.FORMAT.format(ranged.getMinValue()), Constants.FORMAT.format(minValue) });
                            accessor.attributefix$setMinValue(minValue);
                        }
                        if (maxValue != ranged.getMaxValue()) {
                            Constants.LOG.debug("Modifying maximum value for {} from {} to {}.", new Object[] { attributeId, Constants.FORMAT.format(ranged.getMaxValue()), Constants.FORMAT.format(maxValue) });
                            accessor.attributefix$setMaxValue(maxValue);
                        }
                    }
                }
            }
        }
    }

    public static AttributeConfig load(File configFile, RegistryHelper<Attribute> registry) {
        AttributeConfig config = new AttributeConfig();
        for (Attribute attribute : registry.getValues()) {
            if (attribute instanceof RangedAttribute ranged) {
                ResourceLocation id = registry.getId(attribute);
                config.attributes.put(id.toString(), new AttributeConfig.Entry(id, ranged));
            }
        }
        Constants.LOG.info("Loaded values for {} compatible attributes.", config.attributes.size());
        if (configFile.exists()) {
            try {
                FileReader reader = new FileReader(configFile);
                try {
                    Map<String, AttributeConfig.Entry> configValues = ((AttributeConfig) Constants.GSON.fromJson(reader, AttributeConfig.class)).attributes;
                    for (java.util.Map.Entry<String, AttributeConfig.Entry> configEntry : configValues.entrySet()) {
                        ResourceLocation attributeId = ResourceLocation.tryParse((String) configEntry.getKey());
                        if (attributeId == null) {
                            Constants.LOG.error("Attribute ID '{}' is not a valid. This entry will be ignored.", configEntry.getKey());
                        } else if (!registry.exists(attributeId)) {
                            Constants.LOG.error("Attribute ID '{}' does not belong to a known attribute. This entry will be ignored.", configEntry.getKey());
                        }
                        if (((AttributeConfig.Entry) configEntry.getValue()).min.value > ((AttributeConfig.Entry) configEntry.getValue()).max.value) {
                            Constants.LOG.error("Attribute ID '{}' has a max value that is less than its minimum value!", configEntry.getKey());
                        }
                        config.attributes.put((String) configEntry.getKey(), (AttributeConfig.Entry) configEntry.getValue());
                    }
                    Constants.LOG.info("Loaded {} values from config.", configValues.size());
                } catch (Throwable var12) {
                    try {
                        reader.close();
                    } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                    }
                    throw var12;
                }
                reader.close();
            } catch (Exception var13) {
                Constants.LOG.error("Could not read config file {}. Defaults will be used.", configFile.getAbsolutePath());
                Constants.LOG.trace("Failed to read config file.", var13);
            }
        } else {
            Constants.LOG.info("Creating a new config file at {}.", configFile.getAbsolutePath());
            configFile.getParentFile().mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(configFile);
            try {
                Constants.GSON.toJson(config, writer);
                Constants.LOG.info("Saving config file. {} entries.", config.attributes.size());
            } catch (Throwable var9) {
                try {
                    writer.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }
                throw var9;
            }
            writer.close();
        } catch (Exception var10) {
            Constants.LOG.error("Could not write config file '{}'!", configFile.getAbsolutePath());
            Constants.LOG.trace("Failed to read config file.", var10);
        }
        return config;
    }

    public static class DoubleValue {

        @Expose
        @SerializedName("default")
        private double defaultValue;

        @Expose
        private double value;

        public DoubleValue(double defaultValue, double value) {
            this.defaultValue = defaultValue;
            this.value = value;
        }
    }

    public static class Entry {

        @Expose
        private boolean enabled;

        @Expose
        private AttributeConfig.DoubleValue min;

        @Expose
        private AttributeConfig.DoubleValue max;

        public Entry(ResourceLocation id, RangedAttribute attribute) {
            this.enabled = "minecraft".equals(id.getNamespace());
            this.min = new AttributeConfig.DoubleValue(attribute.getMinValue(), attribute.getMinValue());
            this.max = new AttributeConfig.DoubleValue(attribute.getMaxValue(), (Double) AttributeConfig.NEW_DEFAULT_VALUES.getOrDefault(attribute, attribute.getMaxValue()));
        }

        public boolean isEnabled() {
            return this.isEnabled();
        }
    }
}