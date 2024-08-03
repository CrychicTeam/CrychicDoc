package dev.xkmc.l2library.serial.ingredients;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

@SerialClass
public class MobEffectIngredient extends BaseIngredient<MobEffectIngredient> {

    public static final BaseIngredient.Serializer<MobEffectIngredient> INSTANCE = new BaseIngredient.Serializer<>(MobEffectIngredient.class, new ResourceLocation("l2library", "mob_effect"));

    @SerialField
    public Item item;

    @SerialField
    public MobEffect effect;

    @SerialField
    public int min_level;

    @SerialField
    public int min_time;

    @Deprecated
    public MobEffectIngredient() {
    }

    protected MobEffectIngredient validate() {
        return new MobEffectIngredient(this.item, this.effect, this.min_level, this.min_time);
    }

    public MobEffectIngredient(Item item, MobEffect effect, int minLevel, int minTime) {
        super(PotionUtils.setCustomEffects(new ItemStack(item), List.of(new MobEffectInstance(effect, minTime, minLevel))));
        this.item = item;
        this.effect = effect;
        this.min_level = minLevel;
        this.min_time = minTime;
    }

    @Override
    public boolean test(ItemStack stack) {
        return PotionUtils.getMobEffects(stack).stream().anyMatch(e -> e.getEffect() == this.effect && e.getAmplifier() >= this.min_level && e.getDuration() >= this.min_time);
    }

    @Override
    public BaseIngredient.Serializer<MobEffectIngredient> getSerializer() {
        return INSTANCE;
    }
}