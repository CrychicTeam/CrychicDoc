package pie.ilikepiefoo.compat.jade.impl;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import org.jetbrains.annotations.Nullable;
import pie.ilikepiefoo.compat.jade.ITooltipWrapper;
import pie.ilikepiefoo.compat.jade.builder.EntityComponentProviderBuilder;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

public class CustomEntityComponentProvider extends CustomToggleableProviderBuilder<EntityComponentProviderBuilder> implements IEntityComponentProvider {

    public CustomEntityComponentProvider(EntityComponentProviderBuilder builder) {
        super(builder);
    }

    @Nullable
    @Override
    public IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
        if (this.builder.getIconRetriever() == null) {
            return null;
        } else {
            try {
                return this.builder.getIconRetriever().getIcon(accessor, config, currentIcon);
            } catch (Throwable var5) {
                ConsoleJS.CLIENT.error("Error while executing entity component provider icon retriever", var5);
                return null;
            }
        }
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (this.builder.getTooltipRetriever() != null) {
            try {
                this.builder.getTooltipRetriever().appendTooltip(ITooltipWrapper.of(iTooltip), blockAccessor, iPluginConfig);
            } catch (Throwable var5) {
                ConsoleJS.CLIENT.error("Error while executing entity component provider tooltip retriever", var5);
            }
        }
    }
}