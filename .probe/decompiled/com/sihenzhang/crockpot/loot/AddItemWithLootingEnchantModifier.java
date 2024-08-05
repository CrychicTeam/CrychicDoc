package com.sihenzhang.crockpot.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class AddItemWithLootingEnchantModifier extends LootModifier {

    public static final Supplier<Codec<AddItemWithLootingEnchantModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(inst.group(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item), Codec.INT.fieldOf("count").forGetter(m -> m.count), Codec.INT.fieldOf("limit").forGetter(m -> m.limit))).apply(inst, AddItemWithLootingEnchantModifier::new)));

    private static final UniformGenerator RANDOM_NUMBER_GENERATOR = UniformGenerator.between(0.0F, 1.0F);

    private final Item item;

    private final int count;

    private final int limit;

    public AddItemWithLootingEnchantModifier(LootItemCondition[] conditionsIn, Item item, int count, int limit) {
        super(conditionsIn);
        this.item = item;
        this.count = count;
        this.limit = limit;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ItemStack result = new ItemStack(this.item, this.count);
        int lootingModifier = context.getLootingModifier();
        if (lootingModifier > 0) {
            float bonus = (float) lootingModifier * RANDOM_NUMBER_GENERATOR.getFloat(context);
            result.grow(Math.round(bonus));
            if (this.limit > 0 && result.getCount() > this.limit) {
                result.setCount(this.limit);
            }
        }
        generatedLoot.add(result);
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return (Codec<? extends IGlobalLootModifier>) CODEC.get();
    }
}