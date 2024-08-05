package com.corosus.coroutil.loader.forge;

import com.corosus.coroutil.util.CULog;
import com.corosus.modconfig.ConfigComment;
import com.corosus.modconfig.ConfigParams;
import com.corosus.modconfig.CoroConfigRegistry;
import com.corosus.modconfig.IConfigCategory;
import com.corosus.modconfig.ModConfigData;
import java.lang.reflect.Field;
import java.util.HashMap;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ModConfigDataForge extends ModConfigData {

    public HashMap<String, ForgeConfigSpec.ConfigValue<String>> valsStringConfig = new HashMap();

    public HashMap<String, ForgeConfigSpec.ConfigValue<Integer>> valsIntegerConfig = new HashMap();

    public HashMap<String, ForgeConfigSpec.ConfigValue<Double>> valsDoubleConfig = new HashMap();

    public HashMap<String, ForgeConfigSpec.ConfigValue<Boolean>> valsBooleanConfig = new HashMap();

    public ModConfigDataForge(String savePath, String parStr, Class parClass, IConfigCategory parConfig) {
        super(savePath, parStr, parClass, parConfig);
    }

    @Override
    public String getConfigString(String fieldName) {
        return (String) ((ForgeConfigSpec.ConfigValue) this.valsStringConfig.get(fieldName)).get();
    }

    @Override
    public Integer getConfigInteger(String fieldName) {
        return (Integer) ((ForgeConfigSpec.ConfigValue) this.valsIntegerConfig.get(fieldName)).get();
    }

    @Override
    public Double getConfigDouble(String fieldName) {
        return (Double) ((ForgeConfigSpec.ConfigValue) this.valsDoubleConfig.get(fieldName)).get();
    }

    @Override
    public Boolean getConfigBoolean(String fieldName) {
        return (Boolean) ((ForgeConfigSpec.ConfigValue) this.valsBooleanConfig.get(fieldName)).get();
    }

    @Override
    public <T> void setConfig(String fieldName, T obj) {
        if (obj instanceof String) {
            ((ForgeConfigSpec.ConfigValue) this.valsStringConfig.get(fieldName)).set((T) ((String) obj));
            ((ForgeConfigSpec.ConfigValue) this.valsStringConfig.get(fieldName)).save();
        } else if (obj instanceof Integer) {
            ((ForgeConfigSpec.ConfigValue) this.valsIntegerConfig.get(fieldName)).set((T) ((Integer) obj));
            ((ForgeConfigSpec.ConfigValue) this.valsIntegerConfig.get(fieldName)).save();
        } else if (obj instanceof Double) {
            ((ForgeConfigSpec.ConfigValue) this.valsDoubleConfig.get(fieldName)).set((T) ((Double) obj));
            ((ForgeConfigSpec.ConfigValue) this.valsDoubleConfig.get(fieldName)).save();
        } else if (obj instanceof Boolean) {
            ((ForgeConfigSpec.ConfigValue) this.valsBooleanConfig.get(fieldName)).set((T) ((Boolean) obj));
            ((ForgeConfigSpec.ConfigValue) this.valsBooleanConfig.get(fieldName)).save();
        }
    }

    @Override
    public void writeConfigFile(boolean resetConfig) {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        BUILDER.comment("General mod settings").push("general");
        Field[] fields = this.configClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            this.addToConfig(BUILDER, field, name);
        }
        CULog.dbg("writeConfigFile invoked for " + this.configID + ", resetConfig: " + resetConfig);
        BUILDER.pop();
        ForgeConfigSpec CONFIG = BUILDER.build();
        ModLoadingContext.get().registerConfig(Type.COMMON, CONFIG, this.saveFilePath + ".toml");
    }

    private void addToConfig(ForgeConfigSpec.Builder builder, Field field, String name) {
        String comment = "-";
        double min = -Double.MAX_VALUE;
        double max = Double.MAX_VALUE;
        ConfigComment anno_comment = (ConfigComment) field.getAnnotation(ConfigComment.class);
        if (anno_comment != null) {
            comment = anno_comment.value()[0];
        }
        ConfigParams anno_params = (ConfigParams) field.getAnnotation(ConfigParams.class);
        if (anno_params != null) {
            comment = anno_params.comment();
            min = anno_params.min();
            max = anno_params.max();
        }
        Object obj = CoroConfigRegistry.instance().getField(this.configID, name);
        if (obj instanceof String) {
            this.valsStringConfig.put(name, builder.comment(comment).define(name, (String) obj));
        } else if (obj instanceof Integer) {
            this.valsIntegerConfig.put(name, builder.comment(comment).defineInRange(name, (Integer) obj, (int) min, (int) max));
        } else if (obj instanceof Double) {
            this.valsDoubleConfig.put(name, builder.comment(comment).defineInRange(name, (Double) obj, min, max));
        } else if (obj instanceof Boolean) {
            this.valsBooleanConfig.put(name, builder.comment(comment).define(name, (Boolean) obj));
        }
        this.setFieldBasedOnType(name, obj);
    }
}