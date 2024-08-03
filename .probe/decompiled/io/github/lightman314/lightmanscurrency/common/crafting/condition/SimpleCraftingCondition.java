package io.github.lightman314.lightmanscurrency.common.crafting.condition;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class SimpleCraftingCondition implements ICondition {

    private final ResourceLocation id;

    private final Supplier<Boolean> test;

    protected SimpleCraftingCondition(ResourceLocation id, Supplier<Boolean> test) {
        this.id = id;
        this.test = test;
    }

    @Override
    public ResourceLocation getID() {
        return this.id;
    }

    @Override
    public boolean test(ICondition.IContext context) {
        try {
            return (Boolean) this.test.get();
        } catch (Throwable var3) {
            LightmansCurrency.LogError("SimpleCraftingCondition '" + this.id + "' encountered an error during the test.", var3);
            return false;
        }
    }
}