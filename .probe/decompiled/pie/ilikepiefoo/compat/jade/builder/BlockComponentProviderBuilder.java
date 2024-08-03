package pie.ilikepiefoo.compat.jade.builder;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import pie.ilikepiefoo.compat.jade.ITooltipWrapper;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

public class BlockComponentProviderBuilder extends ToggleableProviderBuilder {

    private BlockComponentProviderBuilder.IconRetriever iconRetriever;

    private BlockComponentProviderBuilder.TooltipRetriever tooltipRetriever;

    public BlockComponentProviderBuilder(ResourceLocation uniqueIdentifier) {
        super(uniqueIdentifier);
    }

    public BlockComponentProviderBuilder iconRetriever(BlockComponentProviderBuilder.IconRetriever iconRetriever) {
        return this.setIconRetriever(iconRetriever);
    }

    public BlockComponentProviderBuilder icon(BlockComponentProviderBuilder.IconRetriever iconRetriever) {
        return this.setIconRetriever(iconRetriever);
    }

    public BlockComponentProviderBuilder tooltipRetriever(BlockComponentProviderBuilder.TooltipRetriever tooltipRetriever) {
        return this.setTooltipRetriever(tooltipRetriever);
    }

    public BlockComponentProviderBuilder tooltip(BlockComponentProviderBuilder.TooltipRetriever tooltipRetriever) {
        return this.setTooltipRetriever(tooltipRetriever);
    }

    public BlockComponentProviderBuilder.IconRetriever getIconRetriever() {
        return this.iconRetriever;
    }

    public BlockComponentProviderBuilder setIconRetriever(BlockComponentProviderBuilder.IconRetriever iconRetriever) {
        this.iconRetriever = iconRetriever;
        return this;
    }

    public BlockComponentProviderBuilder.TooltipRetriever getTooltipRetriever() {
        return this.tooltipRetriever;
    }

    public BlockComponentProviderBuilder setTooltipRetriever(BlockComponentProviderBuilder.TooltipRetriever tooltipRetriever) {
        this.tooltipRetriever = tooltipRetriever;
        return this;
    }

    public interface IconRetriever {

        @Nullable
        IElement getIcon(BlockAccessor var1, IPluginConfig var2, IElement var3);
    }

    public interface TooltipRetriever {

        void appendTooltip(ITooltipWrapper var1, BlockAccessor var2, IPluginConfig var3);
    }
}