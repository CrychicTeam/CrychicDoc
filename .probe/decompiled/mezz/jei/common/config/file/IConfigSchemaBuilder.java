package mezz.jei.common.config.file;

public interface IConfigSchemaBuilder {

    IConfigCategoryBuilder addCategory(String var1);

    IConfigSchema build();
}