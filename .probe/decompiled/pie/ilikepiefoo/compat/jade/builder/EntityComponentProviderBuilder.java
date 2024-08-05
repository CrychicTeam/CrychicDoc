package pie.ilikepiefoo.compat.jade.builder;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import pie.ilikepiefoo.compat.jade.ITooltipWrapper;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

public class EntityComponentProviderBuilder extends ToggleableProviderBuilder {

    private EntityComponentProviderBuilder.IconRetriever iconRetriever;

    private EntityComponentProviderBuilder.TooltipRetriever tooltipRetriever;

    public EntityComponentProviderBuilder(ResourceLocation uniqueIdentifier) {
        super(uniqueIdentifier);
    }

    public EntityComponentProviderBuilder iconRetriever(EntityComponentProviderBuilder.IconRetriever iconRetriever) {
        return this.setIconRetriever(iconRetriever);
    }

    public EntityComponentProviderBuilder icon(EntityComponentProviderBuilder.IconRetriever iconRetriever) {
        return this.setIconRetriever(iconRetriever);
    }

    public EntityComponentProviderBuilder tooltipRetriever(EntityComponentProviderBuilder.TooltipRetriever tooltipRetriever) {
        return this.setTooltipRetriever(tooltipRetriever);
    }

    public EntityComponentProviderBuilder tooltip(EntityComponentProviderBuilder.TooltipRetriever tooltipRetriever) {
        return this.setTooltipRetriever(tooltipRetriever);
    }

    public EntityComponentProviderBuilder.IconRetriever getIconRetriever() {
        return this.iconRetriever;
    }

    public EntityComponentProviderBuilder setIconRetriever(EntityComponentProviderBuilder.IconRetriever iconRetriever) {
        this.iconRetriever = iconRetriever;
        return this;
    }

    public EntityComponentProviderBuilder.TooltipRetriever getTooltipRetriever() {
        return this.tooltipRetriever;
    }

    public EntityComponentProviderBuilder setTooltipRetriever(EntityComponentProviderBuilder.TooltipRetriever tooltipRetriever) {
        this.tooltipRetriever = tooltipRetriever;
        return this;
    }

    public interface IconRetriever {

        @Nullable
        IElement getIcon(EntityAccessor var1, IPluginConfig var2, IElement var3);
    }

    public interface TooltipRetriever {

        void appendTooltip(ITooltipWrapper var1, EntityAccessor var2, IPluginConfig var3);
    }
}