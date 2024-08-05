package snownee.jade.api;

import org.jetbrains.annotations.Nullable;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

public interface IEntityComponentProvider extends IToggleableProvider {

    @Nullable
    default IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
        return null;
    }

    void appendTooltip(ITooltip var1, EntityAccessor var2, IPluginConfig var3);
}