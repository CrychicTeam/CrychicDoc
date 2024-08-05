package io.redspace.ironsspellbooks.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.registries.LootRegistry;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class RandomizeSpellFunction extends LootItemConditionalFunction {

    final NumberProvider qualityRange;

    final SpellFilter applicableSpells;

    protected RandomizeSpellFunction(LootItemCondition[] lootConditions, NumberProvider qualityRange, SpellFilter spellFilter) {
        super(lootConditions);
        this.qualityRange = qualityRange;
        this.applicableSpells = spellFilter;
    }

    public static LootItemConditionalFunction.Builder<?> create(NumberProvider quality, SpellFilter filter) {
        return m_80683_(functions -> new RandomizeSpellFunction(functions, quality, filter));
    }

    public static LootItemConditionalFunction.Builder<?> allSpells(NumberProvider quality) {
        return m_80683_(functions -> new RandomizeSpellFunction(functions, quality, new SpellFilter()));
    }

    @Override
    protected ItemStack run(ItemStack itemStack, LootContext lootContext) {
        if (itemStack.getItem() instanceof Scroll || Utils.canImbue(itemStack)) {
            List<AbstractSpell> applicableSpells = this.applicableSpells.getApplicableSpells();
            if (applicableSpells.isEmpty()) {
                itemStack.setCount(0);
                return itemStack;
            }
            NavigableMap<Integer, AbstractSpell> spellList = this.getWeightedSpellList(applicableSpells);
            int total = (Integer) spellList.floorKey(Integer.MAX_VALUE);
            AbstractSpell abstractSpell = SpellRegistry.none();
            if (!spellList.isEmpty()) {
                abstractSpell = (AbstractSpell) spellList.higherEntry(lootContext.getRandom().nextInt(total)).getValue();
            }
            int maxLevel = abstractSpell.getMaxLevel();
            float quality = this.qualityRange.getFloat(lootContext);
            int spellLevel = 1 + Math.round(quality * (float) (maxLevel - 1));
            if (itemStack.getItem() instanceof Scroll) {
                ISpellContainer.createScrollContainer(abstractSpell, spellLevel, itemStack);
            } else {
                ISpellContainer.createImbuedContainer(abstractSpell, spellLevel, itemStack);
            }
        }
        return itemStack;
    }

    private NavigableMap<Integer, AbstractSpell> getWeightedSpellList(List<AbstractSpell> entries) {
        int total = 0;
        NavigableMap<Integer, AbstractSpell> weightedSpells = new TreeMap();
        for (AbstractSpell entry : entries) {
            if (entry != SpellRegistry.none() && entry.isEnabled()) {
                total += this.getWeightFromRarity(SpellRarity.values()[entry.getMinRarity()]);
                weightedSpells.put(total, entry);
            }
        }
        return weightedSpells;
    }

    public <N extends NumberProvider> N getQualityRange() {
        return (N) this.qualityRange;
    }

    private int getWeightFromRarity(SpellRarity rarity) {
        return switch(rarity) {
            case COMMON ->
                40;
            case UNCOMMON ->
                30;
            case RARE ->
                15;
            case EPIC ->
                8;
            case LEGENDARY ->
                4;
        };
    }

    @Override
    public LootItemFunctionType getType() {
        return LootRegistry.RANDOMIZE_SPELL_FUNCTION.get();
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomizeSpellFunction> {

        public void serialize(JsonObject json, RandomizeSpellFunction scrollFunction, JsonSerializationContext jsonDeserializationContext) {
            super.serialize(json, scrollFunction, jsonDeserializationContext);
            JsonObject quality = new JsonObject();
            scrollFunction.qualityRange.getType().m_79331_().serialize(quality, scrollFunction.getQualityRange(), jsonDeserializationContext);
            json.add("quality", quality);
            scrollFunction.applicableSpells.serialize(json);
        }

        public RandomizeSpellFunction deserialize(JsonObject json, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] lootConditions) {
            NumberProvider numberProvider = GsonHelper.getAsObject(json, "quality", jsonDeserializationContext, NumberProvider.class);
            SpellFilter applicableSpells = SpellFilter.deserializeSpellFilter(json);
            return new RandomizeSpellFunction(lootConditions, numberProvider, applicableSpells);
        }
    }
}