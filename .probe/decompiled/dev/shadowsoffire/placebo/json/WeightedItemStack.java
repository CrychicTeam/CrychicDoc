package dev.shadowsoffire.placebo.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shadowsoffire.placebo.codec.PlaceboCodecs;
import java.util.List;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class WeightedItemStack extends WeightedEntry.IntrusiveBase {

    public static final Codec<WeightedItemStack> CODEC = RecordCodecBuilder.create(inst -> inst.group(ItemAdapter.CODEC.fieldOf("stack").forGetter(w -> w.stack), Weight.CODEC.fieldOf("weight").forGetter(w -> w.m_142631_()), PlaceboCodecs.nullableField(Codec.FLOAT, "drop_chance", -1.0F).forGetter(w -> w.dropChance)).apply(inst, WeightedItemStack::new));

    public static final Codec<List<WeightedItemStack>> LIST_CODEC = CODEC.listOf();

    final ItemStack stack;

    final float dropChance;

    public WeightedItemStack(ItemStack stack, Weight weight, float dropChance) {
        super(weight);
        this.stack = stack;
        this.dropChance = dropChance;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public String toString() {
        return "Stack: " + this.stack.toString() + " @ Weight: " + this.m_142631_().asInt();
    }

    public void apply(LivingEntity entity, EquipmentSlot slot) {
        entity.setItemSlot(slot, this.stack.copy());
        if (this.dropChance >= 0.0F && entity instanceof Mob mob) {
            mob.setDropChance(slot, this.dropChance);
        }
    }
}