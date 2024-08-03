package vectorwing.farmersdelight.integration.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ToolAction;
import org.openzen.zencode.java.ZenCodeType.Caster;
import org.openzen.zencode.java.ZenCodeType.Expansion;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;

@Document("mods/FarmersDelight/ToolActionIngredient")
@ZenRegister
@Name("mods.farmersdelight.ToolActionIngredient")
public class CTToolActionIngredient implements IIngredient {

    public static final String PREFIX = "toolingredient";

    private final ToolActionIngredient ingredient;

    public CTToolActionIngredient(ToolAction toolAction) {
        this(new ToolActionIngredient(toolAction));
    }

    public CTToolActionIngredient(ToolActionIngredient ingredient) {
        this.ingredient = ingredient;
    }

    public boolean matches(IItemStack stack, boolean ignoreDamage) {
        return this.ingredient.test(stack.getInternal());
    }

    public Ingredient asVanillaIngredient() {
        return this.ingredient;
    }

    public String getCommandString() {
        return "<toolingredient:" + this.ingredient.toolAction.name() + ">";
    }

    public IItemStack[] getItems() {
        ItemStack[] stacks = this.ingredient.m_43908_();
        IItemStack[] out = new IItemStack[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            out[i] = new MCItemStackMutable(stacks[i]);
        }
        return out;
    }

    @Expansion("crafttweaker.api.tool.ToolAction")
    @ZenRegister
    public static class ExpandToolAction {

        @Method
        @Caster(implicit = true)
        public static IIngredient asIIngredient(ToolAction internal) {
            return new CTToolActionIngredient(internal);
        }
    }
}