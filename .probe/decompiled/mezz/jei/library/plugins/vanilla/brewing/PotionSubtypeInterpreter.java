package mezz.jei.library.plugins.vanilla.brewing;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

public class PotionSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

    public static final PotionSubtypeInterpreter INSTANCE = new PotionSubtypeInterpreter();

    private PotionSubtypeInterpreter() {
    }

    public String apply(ItemStack itemStack, UidContext context) {
        if (!itemStack.hasTag()) {
            return "";
        } else {
            Potion potionType = PotionUtils.getPotion(itemStack);
            String potionTypeString = potionType.getName("");
            StringBuilder stringBuilder = new StringBuilder(potionTypeString);
            for (MobEffectInstance effect : PotionUtils.getMobEffects(itemStack)) {
                stringBuilder.append(";").append(effect);
            }
            return stringBuilder.toString();
        }
    }
}