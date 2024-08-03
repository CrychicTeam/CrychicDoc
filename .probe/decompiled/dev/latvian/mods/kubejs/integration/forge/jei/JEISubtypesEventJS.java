package dev.latvian.mods.kubejs.integration.forge.jei;

import dev.latvian.mods.kubejs.event.EventJS;
import java.util.function.Function;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class JEISubtypesEventJS extends EventJS {

    private final ISubtypeRegistration registration;

    public JEISubtypesEventJS(ISubtypeRegistration r) {
        this.registration = r;
    }

    public void registerInterpreter(Item item, JEISubtypesEventJS.Interpreter interpreter) {
        this.registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, (stack, context) -> {
            Object o = interpreter.apply(stack);
            return o == null ? "" : o.toString();
        });
    }

    public void useNBT(Ingredient items) {
        this.registration.useNbtForSubtypes((Item[]) items.kjs$getItemTypes().toArray(new Item[0]));
    }

    public void useNBTKey(Ingredient items, String key) {
        JEISubtypesEventJS.NBTKeyInterpreter in = new JEISubtypesEventJS.NBTKeyInterpreter(key);
        for (Item item : items.kjs$getItemTypes()) {
            this.registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, in);
        }
    }

    @FunctionalInterface
    public interface Interpreter extends Function<ItemStack, Object> {
    }

    private static class NBTKeyInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

        private final String key;

        private NBTKeyInterpreter(String k) {
            this.key = k;
        }

        public String apply(ItemStack stack, UidContext context) {
            CompoundTag nbt = stack.getTag();
            return nbt != null && nbt.contains(this.key) ? String.valueOf(nbt.get(this.key)) : "";
        }
    }
}