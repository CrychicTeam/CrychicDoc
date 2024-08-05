package dev.xkmc.l2library.serial.ingredients;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

@SerialClass
public class PotionIngredient extends BaseIngredient<PotionIngredient> {

    public static final BaseIngredient.Serializer<PotionIngredient> INSTANCE = new BaseIngredient.Serializer<>(PotionIngredient.class, new ResourceLocation("l2library", "potion"));

    @SerialField
    public Potion potion;

    @Deprecated
    public PotionIngredient() {
    }

    protected PotionIngredient validate() {
        return new PotionIngredient(this.potion);
    }

    public PotionIngredient(Potion potion) {
        super(PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
        this.potion = potion;
    }

    @Override
    public boolean test(ItemStack stack) {
        return PotionUtils.getPotion(stack) == this.potion;
    }

    @Override
    public BaseIngredient.Serializer<PotionIngredient> getSerializer() {
        return INSTANCE;
    }
}