package com.corosus.modconfig;

import com.corosus.coroutil.util.OldUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ModConfigData {

    public String configID;

    public Class configClass;

    public IConfigCategory configInstance;

    public HashMap<String, String> valsString = new HashMap();

    public HashMap<String, Integer> valsInteger = new HashMap();

    public HashMap<String, Double> valsDouble = new HashMap();

    public HashMap<String, Boolean> valsBoolean = new HashMap();

    public List<ConfigEntryInfo> configData = new ArrayList();

    public String saveFilePath;

    public ModConfigData(String savePath, String parStr, Class parClass, IConfigCategory parConfig) {
        this.configID = parStr;
        this.configClass = parClass;
        this.configInstance = parConfig;
        this.saveFilePath = savePath;
    }

    public void updateHashMaps() {
        Field[] fields = this.configClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            this.processField(name);
        }
    }

    public void updateConfigFieldValues() {
        Field[] fields = this.configClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            this.processFieldFromForgeConfig(name);
        }
    }

    private void processFieldFromForgeConfig(String fieldName) {
        try {
            Object obj = CoroConfigRegistry.instance().getField(this.configID, fieldName);
            if (obj instanceof String) {
                this.valsString.put(fieldName, (String) obj);
                this.setFieldBasedOnType(fieldName, this.getConfigString(fieldName));
            } else if (obj instanceof Integer) {
                this.valsInteger.put(fieldName, (Integer) obj);
                this.setFieldBasedOnType(fieldName, this.getConfigInteger(fieldName));
            } else if (obj instanceof Double) {
                this.valsDouble.put(fieldName, (Double) obj);
                this.setFieldBasedOnType(fieldName, this.getConfigDouble(fieldName));
            } else if (obj instanceof Boolean) {
                this.valsBoolean.put(fieldName, (Boolean) obj);
                this.setFieldBasedOnType(fieldName, this.getConfigBoolean(fieldName));
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public void initData() {
        this.valsString.clear();
        this.valsInteger.clear();
        this.valsDouble.clear();
        this.valsBoolean.clear();
        this.updateHashMaps();
    }

    public boolean updateField(String name, Object obj) {
        if (this.setFieldBasedOnType(name, obj)) {
            this.writeConfigFile(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean setFieldBasedOnType(String name, Object obj) {
        try {
            if (this.valsString.containsKey(name)) {
                OldUtil.setPrivateValue(this.configClass, this.configInstance, name, (String) obj);
                this.inputField(name, (String) obj);
            } else if (this.valsInteger.containsKey(name)) {
                OldUtil.setPrivateValue(this.configClass, this.configInstance, name, Integer.valueOf(obj.toString()));
                this.inputField(name, Integer.valueOf(obj.toString()));
            } else if (this.valsDouble.containsKey(name)) {
                OldUtil.setPrivateValue(this.configClass, this.configInstance, name, Double.valueOf(obj.toString()));
                this.inputField(name, Double.valueOf(obj.toString()));
            } else {
                if (!this.valsBoolean.containsKey(name)) {
                    return false;
                }
                OldUtil.setPrivateValue(this.configClass, this.configInstance, name, Boolean.valueOf(obj.toString()));
                this.inputField(name, Boolean.valueOf(obj.toString()));
            }
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    private void processField(String fieldName) {
        try {
            Object obj = CoroConfigRegistry.instance().getField(this.configID, fieldName);
            if (obj instanceof String) {
                this.valsString.put(fieldName, (String) obj);
            } else if (obj instanceof Integer) {
                this.valsInteger.put(fieldName, (Integer) obj);
            } else if (obj instanceof Double) {
                this.valsDouble.put(fieldName, (Double) obj);
            } else if (obj instanceof Boolean) {
                this.valsBoolean.put(fieldName, (Boolean) obj);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    private void inputField(String fieldName, Object obj) {
        if (obj instanceof String) {
            this.valsString.put(fieldName, (String) obj);
        } else if (obj instanceof Integer) {
            this.valsInteger.put(fieldName, (Integer) obj);
        } else if (obj instanceof Double) {
            this.valsDouble.put(fieldName, (Double) obj);
        } else if (obj instanceof Boolean) {
            this.valsBoolean.put(fieldName, (Boolean) obj);
        }
    }

    public abstract void writeConfigFile(boolean var1);

    public void updateConfigFileWithRuntimeValues() {
        Field[] fields = this.configClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            this.saveField(name);
        }
    }

    private void saveField(String fieldName) {
        try {
            Object obj = CoroConfigRegistry.instance().getField(this.configID, fieldName);
            if (obj instanceof String) {
                this.valsString.put(fieldName, (String) obj);
            } else if (obj instanceof Integer) {
                this.valsInteger.put(fieldName, (Integer) obj);
            } else if (obj instanceof Double) {
                this.valsDouble.put(fieldName, (Double) obj);
            } else if (obj instanceof Boolean) {
                this.valsBoolean.put(fieldName, (Boolean) obj);
            }
            this.setConfig(fieldName, obj);
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public abstract String getConfigString(String var1);

    public abstract Integer getConfigInteger(String var1);

    public abstract Double getConfigDouble(String var1);

    public abstract Boolean getConfigBoolean(String var1);

    public abstract <T> void setConfig(String var1, T var2);
}