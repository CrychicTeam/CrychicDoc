package com.mna.loot.modifiers;

import com.google.common.base.Suppliers;
import com.mna.enchantments.framework.EnchantmentInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class BeheadingModifier extends LootModifier {

    public static final Supplier<Codec<BeheadingModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(Codec.FLOAT.fieldOf("chancePerLevel").forGetter(m -> m.chancePerLevel)).and(ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("mobType").forGetter(m -> m.mobType)).and(ForgeRegistries.ITEMS.getCodec().fieldOf("head").forGetter(m -> m.head)).apply(inst, BeheadingModifier::new)));

    private final float chancePerLevel;

    private final Item head;

    private final EntityType<?> mobType;

    public BeheadingModifier(LootItemCondition[] conditions, float chancePerLevel, EntityType<?> mobType, Item head) {
        super(conditions);
        this.chancePerLevel = chancePerLevel;
        this.head = head;
        this.mobType = mobType;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity dead = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        Entity killer = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (dead != null && killer != null && killer instanceof LivingEntity && dead.getType().equals(this.mobType)) {
            LivingEntity killerLiving = (LivingEntity) killer;
            int level = Math.max(killerLiving.getMainHandItem().getEnchantmentLevel(EnchantmentInit.BEHEADING.get()), killerLiving.getOffhandItem().getEnchantmentLevel(EnchantmentInit.BEHEADING.get()));
            if (level > 0 && Math.random() < (double) ((float) level * this.chancePerLevel) && generatedLoot.stream().noneMatch(i -> i.getItem() == this.head)) {
                generatedLoot.add(new ItemStack(this.head));
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return (Codec<? extends IGlobalLootModifier>) CODEC.get();
    }
}