package journeymap.common.properties.config;

import journeymap.common.properties.catagory.Category;

public class BooleanField extends ConfigField<Boolean> {

    public static final String ATTR_CATEGORY_MASTER = "isMaster";

    protected BooleanField() {
    }

    public BooleanField(Category category, boolean defaultValue) {
        this(category, null, defaultValue);
    }

    public BooleanField(Category category, String key, boolean defaultValue) {
        this(category, key, defaultValue, false);
    }

    public BooleanField(Category category, String key, boolean defaultValue, int sortOrder) {
        this(category, key, defaultValue, false);
        this.sortOrder(sortOrder);
    }

    public BooleanField(Category category, String key, boolean defaultValue, boolean isMaster, int sortOrder) {
        this(category, key, defaultValue, isMaster);
        this.sortOrder(sortOrder);
    }

    public BooleanField(Category category, String key, boolean defaultValue, boolean isMaster) {
        super(category, key);
        this.defaultValue(Boolean.valueOf(defaultValue));
        this.setToDefault();
        this.categoryMaster(isMaster);
    }

    public Boolean getDefaultValue() {
        return this.getBooleanAttr("default");
    }

    public BooleanField set(Boolean value) {
        this.put("value", value);
        return this;
    }

    public Boolean get() {
        return this.getBooleanAttr("value");
    }

    public boolean toggle() {
        this.set(!this.get());
        return this.get();
    }

    public boolean toggleAndSave() {
        this.set(!this.get());
        this.save();
        return this.get();
    }

    public boolean isCategoryMaster() {
        return this.getBooleanAttr("isMaster");
    }

    public BooleanField categoryMaster(boolean isMaster) {
        this.put("isMaster", Boolean.valueOf(isMaster));
        return this;
    }

    public BooleanField setParent(String fieldName, Object value) {
        return (BooleanField) super.setParent(fieldName, value);
    }
}