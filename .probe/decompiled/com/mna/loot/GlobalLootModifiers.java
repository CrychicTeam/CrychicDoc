package com.mna.loot;

import com.mna.ManaAndArtifice;
import com.mna.api.tools.RLoc;
import com.mna.loot.modifiers.AddChimeriteModifier;
import com.mna.loot.modifiers.BeheadingModifier;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class GlobalLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, "mna");

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> DROPS_CHIMERITE = LOOT_MODIFIERS.register("drops_chimerite", AddChimeriteModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> BEHEADING = LOOT_MODIFIERS.register("beheading", BeheadingModifier.CODEC);

    @SubscribeEvent
    public static final void init(FMLCommonSetupEvent event) {
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RLoc.create("random_spell_staff"), new LootItemFunctionType(new RandomSpellStaff.Serializer()));
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RLoc.create("random_journal_page"), new LootItemFunctionType(new RandomJournalPage.Serializer()));
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RLoc.create("random_manaweave_recipe"), new LootItemFunctionType(new RandomManaweaveRecipe.Serializer()));
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RLoc.create("random_runescribe_recipe"), new LootItemFunctionType(new RandomRunescribeRecipe.Serializer()));
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RLoc.create("random_construct_part"), new LootItemFunctionType(new RandomConstructPart.Serializer()));
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RLoc.create("random_thaumaturgic_link"), new LootItemFunctionType(new RandomThaumaturgicLink.Serializer()));
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RLoc.create("random_joke_spell"), new LootItemFunctionType(new RandomJokeSpell.Serializer()));
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RLoc.create("random_artifact_spell"), new LootItemFunctionType(new RandomArtifactSpell.Serializer()));
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RLoc.create("random_silver_spell"), new LootItemFunctionType(new RandomSilverSpell.Serializer()));
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, RLoc.create("random_hat"), new LootItemFunctionType(new RandomHat.Serializer()));
        ManaAndArtifice.LOGGER.info("Mana And Artifice >> Registered loot functions");
    }
}