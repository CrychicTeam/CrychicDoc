package journeymap.client.properties;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import journeymap.client.api.impl.OptionsDisplayFactory;
import journeymap.client.io.FileHandler;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.ConfigField;

public class AddonProperties extends ClientPropertiesBase {

    private String name;

    private Map<String, ConfigField<?>> fields;

    public Category getParentCategory() {
        Category category = ClientCategory.valueOf("mod_" + this.name);
        if (category == null) {
            category = ClientCategory.create("mod_" + this.name, this.name);
        }
        return category;
    }

    @Override
    public String getFileName() {
        return String.format("addon.%s.config", this.getName());
    }

    @Override
    public File getFile() {
        if (this.sourceFile == null) {
            File path = new File(FileHandler.StandardConfigDirectory, "addons");
            this.sourceFile = new File(path, this.getFileName());
        }
        return this.sourceFile;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddonProperties setFieldMap(Map<String, ConfigField<?>> fields) {
        this.fields = fields;
        return this;
    }

    @Override
    public Map<String, ConfigField<?>> getConfigFields() {
        if (this.configFields == null) {
            this.fields = this.fields == null ? (Map) OptionsDisplayFactory.MOD_FIELD_REGISTRY.get(this.name) : this.fields;
            this.fields.forEach((name, field) -> {
                field.setOwner(name, this);
                Category category = field.getCategory();
                if (category != null) {
                    this.categories.add(category);
                }
            });
            this.configFields = Collections.unmodifiableMap(this.fields);
        }
        return this.configFields;
    }
}