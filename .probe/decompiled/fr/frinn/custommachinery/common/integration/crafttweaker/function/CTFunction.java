package fr.frinn.custommachinery.common.integration.crafttweaker.function;

import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import java.util.function.Function;
import org.openzen.zencode.java.ZenCodeType.Method;

public class CTFunction implements Function<ICraftingContext, CraftingResult> {

    private final Function<Context, CraftingResult> function;

    public CTFunction(Function<Context, CraftingResult> function) {
        this.function = function;
    }

    @Method
    public CraftingResult apply(ICraftingContext context) {
        return (CraftingResult) this.function.apply(new Context(context));
    }
}