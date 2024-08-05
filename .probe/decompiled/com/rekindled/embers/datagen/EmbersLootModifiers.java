package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.compat.curios.CuriosCompat;
import com.rekindled.embers.util.AshenAmuletLootModifier;
import com.rekindled.embers.util.AugmentPredicate;
import com.rekindled.embers.util.GrandhammerLootModifier;
import com.rekindled.embers.util.MatchCurioLootCondition;
import com.rekindled.embers.util.SuperHeaterLootModifier;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class EmbersLootModifiers extends GlobalLootModifierProvider {

    public EmbersLootModifiers(PackOutput output) {
        super(output, "embers");
    }

    @Override
    protected void start() {
        this.add("grandhammer", new GrandhammerLootModifier(new LootItemCondition[] { MatchTool.toolMatches(ItemPredicate.Builder.item().of(RegistryManager.GRANDHAMMER.get())).build() }));
        this.add("superheater", new SuperHeaterLootModifier(new LootItemCondition[] { new MatchTool(new AugmentPredicate(RegistryManager.SUPERHEATER_AUGMENT, 1)) }));
        this.add("ashenamulet", new AshenAmuletLootModifier(new LootItemCondition[] { MatchCurioLootCondition.curioMatches(ItemPredicate.Builder.item().of(CuriosCompat.ASHEN_AMULET.get())).build() }));
    }
}