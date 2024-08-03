package se.mickelus.tetra.items.modular;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.module.schematic.requirement.IntegerPredicate;

@ParametersAreNonnullByDefault
public class EffectItemPredicate extends ItemPredicate {

    ItemEffect effect;

    IntegerPredicate level;

    public EffectItemPredicate() {
    }

    public EffectItemPredicate(JsonObject jsonObject) {
        if (jsonObject.has("effect")) {
            this.effect = ItemEffect.get(jsonObject.get("effect").getAsString());
            if (jsonObject.has("level")) {
                this.level = IntegerPredicate.Deserializer.deserialize(jsonObject);
            }
        } else {
            throw new JsonParseException("Missing required field 'effect' when parsing 'tetra:effect_predicate'");
        }
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        if (this.effect == null || itemStack.isEmpty() || !(itemStack.getItem() instanceof IModularItem item)) {
            return false;
        } else {
            return this.level != null ? this.level.test(item.getEffectLevel(itemStack, this.effect)) : item.getEffects(itemStack).contains(this.effect);
        }
    }
}