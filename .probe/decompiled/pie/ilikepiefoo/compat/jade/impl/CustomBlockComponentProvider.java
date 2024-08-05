package pie.ilikepiefoo.compat.jade.impl;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import org.jetbrains.annotations.Nullable;
import pie.ilikepiefoo.compat.jade.ITooltipWrapper;
import pie.ilikepiefoo.compat.jade.builder.BlockComponentProviderBuilder;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

public class CustomBlockComponentProvider extends CustomToggleableProviderBuilder<BlockComponentProviderBuilder> implements IBlockComponentProvider {

    public CustomBlockComponentProvider(BlockComponentProviderBuilder builder) {
        super(builder);
    }

    @Nullable
    @Override
    public IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        if (this.builder.getIconRetriever() == null) {
            return null;
        } else {
            try {
                return this.builder.getIconRetriever().getIcon(accessor, config, currentIcon);
            } catch (Throwable var5) {
                ConsoleJS.CLIENT.error("Error while executing block component provider icon retriever", var5);
                return null;
            }
        }
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (this.builder.getTooltipRetriever() != null) {
            try {
                this.builder.getTooltipRetriever().appendTooltip(ITooltipWrapper.of(iTooltip), blockAccessor, iPluginConfig);
            } catch (Throwable var5) {
                ConsoleJS.CLIENT.error("Error while executing block component provider tooltip retriever", var5);
            }
        }
    }
}