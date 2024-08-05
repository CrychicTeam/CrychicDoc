package snownee.jade.api.callback;

import snownee.jade.api.Accessor;
import snownee.jade.api.ITooltip;

@FunctionalInterface
public interface JadeTooltipCollectedCallback {

    void onTooltipCollected(ITooltip var1, Accessor<?> var2);
}