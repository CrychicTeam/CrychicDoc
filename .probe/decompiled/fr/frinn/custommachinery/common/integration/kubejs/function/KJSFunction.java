package fr.frinn.custommachinery.common.integration.kubejs.function;

import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import java.util.function.Function;

public class KJSFunction implements Function<ICraftingContext, CraftingResult> {

    private final Function<Context, Result> function;

    public KJSFunction(Function<Context, Result> function) {
        this.function = function;
    }

    public CraftingResult apply(ICraftingContext context) {
        return ((Result) this.function.apply(new Context(context))).getInternal();
    }
}