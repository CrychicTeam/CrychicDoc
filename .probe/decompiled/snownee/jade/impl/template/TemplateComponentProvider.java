package snownee.jade.impl.template;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.IToggleableProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

public abstract class TemplateComponentProvider<T extends Accessor<?>> implements IToggleableProvider {

    private final ResourceLocation uid;

    private final boolean required;

    private final boolean enabledByDefault;

    private final int defaultPriority;

    private BiFunction<T, IElement, IElement> iconFunction = (accessor, currentIcon) -> null;

    private BiConsumer<ITooltip, T> tooltipFunction = (tooltip, accessor) -> {
    };

    protected TemplateComponentProvider(ResourceLocation uid, boolean required, boolean enabledByDefault, int defaultPriority) {
        this.uid = uid;
        this.required = required;
        this.enabledByDefault = enabledByDefault;
        this.defaultPriority = defaultPriority;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    @Override
    public boolean enabledByDefault() {
        return this.enabledByDefault;
    }

    @Override
    public int getDefaultPriority() {
        return this.defaultPriority;
    }

    @Override
    public ResourceLocation getUid() {
        return this.uid;
    }

    @Nullable
    public IElement getIcon(T accessor, IPluginConfig config, IElement currentIcon) {
        return (IElement) this.iconFunction.apply(accessor, currentIcon);
    }

    public void appendTooltip(ITooltip tooltip, T accessor, IPluginConfig config) {
        this.tooltipFunction.accept(tooltip, accessor);
    }

    public void setIconFunction(BiFunction<T, IElement, IElement> iconFunction) {
        this.iconFunction = iconFunction;
    }

    public void setTooltipFunction(BiConsumer<ITooltip, T> tooltipFunction) {
        this.tooltipFunction = tooltipFunction;
    }
}