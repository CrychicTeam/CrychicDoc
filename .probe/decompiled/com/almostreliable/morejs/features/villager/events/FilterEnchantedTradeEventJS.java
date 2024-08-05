package com.almostreliable.morejs.features.villager.events;

import dev.latvian.mods.kubejs.entity.LivingEntityEventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.enchantment.Enchantment;

public class FilterEnchantedTradeEventJS extends LivingEntityEventJS {

    private final AbstractVillager villager;

    private final RandomSource randomSource;

    private final List<Enchantment> enchantments;

    public FilterEnchantedTradeEventJS(AbstractVillager villager, RandomSource randomSource, List<Enchantment> enchantments) {
        this.villager = villager;
        this.randomSource = randomSource;
        this.enchantments = enchantments;
    }

    public boolean isVillager() {
        return this.getEntity() instanceof Villager;
    }

    public boolean isWanderer() {
        return this.getEntity() instanceof WanderingTrader;
    }

    public AbstractVillager getEntity() {
        return this.villager;
    }

    public void add(Enchantment... enchantments) {
        this.enchantments.addAll(Arrays.asList(enchantments));
    }

    public void remove(Enchantment... enchantments) {
        Set<Enchantment> filter = Set.of(enchantments);
        this.enchantments.removeIf(filter::contains);
    }

    public RandomSource getRandom() {
        return this.randomSource;
    }

    public List<Enchantment> getEnchantments() {
        return this.enchantments;
    }

    public void printEnchantments() {
        ConsoleJS.SERVER.info("Potential Enchantments: " + (String) this.enchantments.stream().flatMap(e -> BuiltInRegistries.ENCHANTMENT.getResourceKey(e).stream()).map(key -> key.location().toString()).sorted(String::compareToIgnoreCase).collect(Collectors.joining(", ")));
    }
}