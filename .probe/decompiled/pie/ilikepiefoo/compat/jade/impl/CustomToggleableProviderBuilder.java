package pie.ilikepiefoo.compat.jade.impl;

import pie.ilikepiefoo.compat.jade.builder.ToggleableProviderBuilder;
import snownee.jade.api.IToggleableProvider;

public class CustomToggleableProviderBuilder<T extends ToggleableProviderBuilder> extends CustomJadeProvider<T> implements IToggleableProvider {

    public CustomToggleableProviderBuilder(T builder) {
        super(builder);
    }

    @Override
    public boolean isRequired() {
        return this.builder.isRequired();
    }

    @Override
    public boolean enabledByDefault() {
        return this.builder.isEnabledByDefault();
    }
}