package com.mna.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mna.Registries;
import com.mna.api.spells.SpellCraftingContext;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.items.ItemInit;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.IForgeRegistry;

public class RandomSilverSpell extends LootItemConditionalFunction {

    ResourceLocation factionID;

    protected RandomSilverSpell(LootItemCondition[] conditionsIn, ResourceLocation factionID, boolean thesis) {
        super(conditionsIn);
        this.factionID = factionID;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        ArrayList<ISpellComponent> candidates = new ArrayList();
        SpellCraftingContext scc = new SpellCraftingContext(null);
        ((IForgeRegistry) Registries.Shape.get()).forEach(s -> {
            if (s.isCraftable(scc)) {
                if (s.isSilverSpell() && (this.factionID == null || s.getFactionRequirement() == null || s.getFactionRequirement().is(this.factionID))) {
                    candidates.add(s);
                }
            }
        });
        ((IForgeRegistry) Registries.SpellEffect.get()).forEach(s -> {
            if (s.isCraftable(scc)) {
                if (s.isSilverSpell() && (this.factionID == null || s.getFactionRequirement() == null || s.getFactionRequirement().is(this.factionID))) {
                    candidates.add(s);
                }
            }
        });
        ((IForgeRegistry) Registries.Modifier.get()).forEach(s -> {
            if (s.isCraftable(scc)) {
                if (s.isSilverSpell() && (this.factionID == null || s.getFactionRequirement() == null || s.getFactionRequirement().is(this.factionID))) {
                    candidates.add(s);
                }
            }
        });
        if (candidates.size() == 0) {
            return stack;
        } else {
            ISpellComponent selected = (ISpellComponent) candidates.get((int) (Math.random() * (double) candidates.size()));
            ItemInit.SPELL_PART_THESIS.get().setComponent(stack, selected);
            return stack;
        }
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_CONTENTS;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomSilverSpell> {

        public RandomSilverSpell deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            ResourceLocation factionID = null;
            boolean thesis = true;
            if (object.has("faction")) {
                factionID = new ResourceLocation(object.get("faction").getAsString());
            }
            if (object.has("thesis")) {
                thesis = object.get("thesis").getAsBoolean();
            }
            return new RandomSilverSpell(conditionsIn, factionID, thesis);
        }
    }
}