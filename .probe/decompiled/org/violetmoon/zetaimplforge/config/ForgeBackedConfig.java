package org.violetmoon.zetaimplforge.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.common.ForgeConfigSpec;
import org.violetmoon.zeta.config.IZetaConfigInternals;
import org.violetmoon.zeta.config.SectionDefinition;
import org.violetmoon.zeta.config.ValueDefinition;

public class ForgeBackedConfig implements IZetaConfigInternals {

    private final Map<ValueDefinition<?>, ForgeConfigSpec.ConfigValue<?>> definitionsToValues = new HashMap();

    private long debounceTime = System.currentTimeMillis();

    public ForgeBackedConfig(SectionDefinition rootSection, ForgeConfigSpec.Builder forgeBuilder) {
        this.walkSection(rootSection, forgeBuilder, true);
    }

    private void walkSection(SectionDefinition sect, ForgeConfigSpec.Builder builder, boolean root) {
        if (!root) {
            builder.comment(sect.commentToArray());
            builder.push(sect.name);
        }
        for (ValueDefinition<?> value : sect.getValues()) {
            this.addValue(value, builder);
        }
        for (SectionDefinition subsection : sect.getSubsections()) {
            this.walkSection(subsection, builder, false);
        }
        if (!root) {
            builder.pop();
        }
    }

    private <T> void addValue(ValueDefinition<T> val, ForgeConfigSpec.Builder builder) {
        builder.comment(val.commentToArray());
        ForgeConfigSpec.ConfigValue<?> forge;
        if (val.defaultValue instanceof List<?> list) {
            forge = builder.defineList(val.name, (List<? extends T>) list, val::validate);
        } else {
            forge = builder.define(List.of(val.name), () -> val.defaultValue, val::validate, val.defaultValue.getClass());
        }
        this.definitionsToValues.put(val, forge);
    }

    @Override
    public <T> T get(ValueDefinition<T> definition) {
        ForgeConfigSpec.ConfigValue<T> forge = (ForgeConfigSpec.ConfigValue<T>) this.definitionsToValues.get(definition);
        return forge.get();
    }

    @Override
    public <T> void set(ValueDefinition<T> definition, T value) {
        ForgeConfigSpec.ConfigValue<T> forge = (ForgeConfigSpec.ConfigValue<T>) this.definitionsToValues.get(definition);
        this.debounceTime = System.currentTimeMillis();
        forge.set(value);
    }

    @Override
    public void flush() {
        this.debounceTime = 0L;
        ((ForgeConfigSpec.ConfigValue) this.definitionsToValues.values().iterator().next()).save();
    }

    @Override
    public long debounceTime() {
        return this.debounceTime;
    }
}