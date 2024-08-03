package com.mna.villagers;

import com.google.common.collect.ImmutableSet;
import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.spells.collections.Shapes;
import com.mna.blocks.BlockInit;
import com.mna.enchantments.framework.EnchantmentInit;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.ItemTornJournalPage;
import com.mna.spells.crafting.SpellRecipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class VillagerRegistry {

    public static DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, "mna");

    public static DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, "mna");

    public static final RegistryObject<PoiType> SPELLMONGER_POI = POI_TYPES.register("spellmonger_poi", () -> new PoiType(ImmutableSet.copyOf(BlockInit.RUNEFORGE.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final RegistryObject<PoiType> RITUALIST_POI = POI_TYPES.register("ritualist_poi", () -> new PoiType(ImmutableSet.copyOf(BlockInit.RUNESCRIBING_TABLE.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final RegistryObject<PoiType> MANAWEAVER_POI = POI_TYPES.register("manaweaver_poi", () -> new PoiType(ImmutableSet.copyOf(BlockInit.MANAWEAVING_ALTAR.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final RegistryObject<VillagerProfession> SPELLMONGER = PROFESSIONS.register("spellmonger", () -> new VillagerProfession("spellmonger", x -> x.get() == SPELLMONGER_POI.get(), x -> x.get() == SPELLMONGER_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_SHEPHERD));

    public static final RegistryObject<VillagerProfession> RITUALIST = PROFESSIONS.register("ritualist", () -> new VillagerProfession("ritualist", x -> x.get() == RITUALIST_POI.get(), x -> x.get() == RITUALIST_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_SHEPHERD));

    public static final RegistryObject<VillagerProfession> MANAWEAVER = PROFESSIONS.register("manaweaver", () -> new VillagerProfession("manaweaver_poi", x -> x.get() == MANAWEAVER_POI.get(), x -> x.get() == MANAWEAVER_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_SHEPHERD));

    @EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
    static class VillagerTradesHandler {

        @SubscribeEvent
        public static void registerTrades(VillagerTradesEvent event) {
            if (!GeneralConfigValues.DisableVillagerRegistration) {
                if (event.getType() == VillagerRegistry.SPELLMONGER.get()) {
                    addSpellmongerTrades(event);
                } else if (event.getType() == VillagerRegistry.RITUALIST.get()) {
                    addRunesmithTrades(event);
                } else if (event.getType() == VillagerRegistry.MANAWEAVER.get()) {
                    addManaweaverTrades(event);
                }
            }
            if (event.getType() == VillagerProfession.LIBRARIAN) {
                if (GeneralConfigValues.ModifyVillagerTrades) {
                    removeLibrarianTrades(event);
                    ManaAndArtifice.LOGGER.info("Mana And Artifice >> Applied villager librarian balance changes.");
                } else {
                    ManaAndArtifice.LOGGER.info("Mana And Artifice >> Skipped villager librarian balance changes as per config.");
                }
            }
        }

        private static void addSpellmongerTrades(VillagerTradesEvent event) {
            ((List) event.getTrades().get(1)).add(new RandomTradeBuilder(8, 10, 0.05F).setPrice(ItemInit.VINTEUM_INGOT.get(), 5, 5).setForSale(ItemInit.GUIDE_BOOK.get(), 1, 1).build());
            ((List) event.getTrades().get(5)).add(new RandomTradeBuilder(1, 10, 0.05F).setPrice(ItemInit.CHIMERITE_GEM.get(), 3, 8).setForSale(ItemInit.MANA_CRYSTAL_FRAGMENT.get(), 1, 1).build());
            addRecipeTrades(event, 1, 1, r -> pickRandom(r, ItemInit.ARCANIST_INK.get()));
            addRecipeTrades(event, 2, 1, r -> pickRandom(r, ItemInit.HERBALIST_POUCH.get(), ItemInit.SPELL_BOOK.get(), ItemInit.BED_CHARM.get(), ItemInit.FALL_CHARM.get(), ItemInit.BURN_CHARM.get(), ItemInit.DROWN_CHARM.get(), ItemInit.VINTEUM_DUST.get()));
            addRecipeTrades(event, 3, 1, r -> pickRandom(r, ItemInit.HERBALIST_POUCH.get(), ItemInit.SPELL_BOOK.get(), ItemInit.BED_CHARM.get(), ItemInit.FALL_CHARM.get(), ItemInit.BURN_CHARM.get(), ItemInit.DROWN_CHARM.get(), ItemInit.VINTEUM_DUST.get()));
            addRecipeTrades(event, 4, 1, r -> pickRandom(r, ItemInit.WITHERBONE.get(), ItemInit.LIVING_FLAME.get(), ItemInit.IRONBARK.get()));
            addRecipeTrades(event, 5, 1, 1, 1, 32, 64, r -> {
                ItemStack stack = new ItemStack(ItemInit.SPELL.get());
                SpellRecipe recipe = new SpellRecipe();
                recipe.setShape(Shapes.RUNE);
                ((IForgeRegistry) Registries.SpellEffect.get()).getValues().stream().skip((long) ((double) ((IForgeRegistry) Registries.SpellEffect.get()).getValues().size() * Math.random())).findFirst().ifPresent(c -> {
                    recipe.addComponent(c);
                    recipe.writeToNBT(stack.getOrCreateTag());
                });
                return recipe.isValid() ? stack : ItemStack.EMPTY;
            });
            System.out.println("Registered Spellmonger Trades");
        }

        private static void addRunesmithTrades(VillagerTradesEvent event) {
            addRecipeTrades(event, 1, 1, r -> pickRandom(r, ItemInit.CLAY_RUNE_PLATE.get(), ItemInit.WIZARD_CHALK.get()));
            addRecipeTrades(event, 1, 1, r -> pickRandom(r, ItemInit.RUNE_MARKING.get()));
            addRecipeTrades(event, 2, 1, r -> pickRandom(r, ItemInit.MUNDANE_AMULET.get(), ItemInit.MUNDANE_BRACELET.get(), ItemInit.MUNDANE_RING.get(), ItemInit.INSCRIPTIONIST_POUCH.get()));
            addRecipeTrades(event, 3, 1, r -> pickRandom(r, ItemInit.CONSTRUCT_RUNE_ROD.get(), ItemInit.CONSTRUCT_RUNE_CLAW.get(), ItemInit.CONSTRUCT_RUNE_HAMMER.get(), ItemInit.CONSTRUCT_RUNE_AXE.get()));
            addRecipeTrades(event, 4, 1, r -> pickRandom(r, ItemInit.CONSTRUCT_RUNE_TORSO.get(), ItemInit.CONSTRUCT_RUNE_HIPS.get(), ItemInit.CONSTRUCT_RUNE_HEAD.get()));
            ((List) event.getTrades().get(5)).add(new RandomTradeBuilder(1, 10, 0.05F).setPrice(ItemInit.RUNESMITH_HAMMER.get(), 1, 1).setPrice2(ItemInit.TRANSMUTED_SILVER.get(), 3, 8).setForSale(r -> {
                ItemStack stack = new ItemStack(ItemInit.RUNESMITH_HAMMER.get());
                HashMap<Enchantment, Integer> enchants = new HashMap();
                if (r.nextBoolean()) {
                    enchants.put(EnchantmentInit.MANA_REPAIR.get(), 1);
                } else {
                    enchants.put(Enchantments.UNBREAKING, 3 + r.nextInt(3));
                }
                EnchantmentHelper.setEnchantments(enchants, stack);
                return stack;
            }).build());
            ((List) event.getTrades().get(5)).add(new RandomTradeBuilder(1, 10, 0.05F).setPrice(ItemInit.RUNESMITH_CHISEL.get(), 1, 1).setPrice2(ItemInit.TRANSMUTED_SILVER.get(), 3, 8).setForSale(r -> {
                ItemStack stack = new ItemStack(ItemInit.RUNESMITH_CHISEL.get());
                HashMap<Enchantment, Integer> enchants = new HashMap();
                if (r.nextBoolean()) {
                    enchants.put(EnchantmentInit.MANA_REPAIR.get(), 1);
                } else {
                    enchants.put(Enchantments.UNBREAKING, 3 + r.nextInt(3));
                }
                EnchantmentHelper.setEnchantments(enchants, stack);
                return stack;
            }).build());
            System.out.println("Registered Runesmith Trades");
        }

        private static void addManaweaverTrades(VillagerTradesEvent event) {
            ((List) event.getTrades().get(1)).add(new RandomTradeBuilder(8, 1, 0.05F).setPrice(ItemInit.TRANSMUTED_SILVER.get(), 6, 12).setForSale(BlockItem.m_41439_(BlockInit.ARCANE_SANDSTONE.get()), 8, 32).build());
            ((List) event.getTrades().get(1)).add(new RandomTradeBuilder(8, 1, 0.05F).setPrice(ItemInit.TRANSMUTED_SILVER.get(), 6, 12).setForSale(BlockItem.m_41439_(BlockInit.ARCANE_STONE.get()), 8, 32).build());
            addRecipeTrades(event, 1, 2, r -> pickRandom(r, ItemInit.RITUAL_FOCUS_MINOR.get(), ItemInit.MANAWEAVER_WAND.get(), BlockItem.m_41439_(BlockInit.ARCANE_STONE.get()), BlockItem.m_41439_(BlockInit.ARCANE_SANDSTONE.get())));
            addRecipeTrades(event, 2, 1, r -> pickRandom(r, ItemInit.WEAVERS_POUCH.get(), ItemInit.INFUSED_SILK.get(), ItemInit.ANIMUS_DUST.get()));
            addRecipeTrades(event, 3, 1, r -> pickRandom(r, ItemInit.WEAVERS_POUCH.get(), ItemInit.INFUSED_SILK.get(), ItemInit.ANIMUS_DUST.get(), ItemInit.RITUAL_FOCUS_LESSER.get()));
            addRecipeTrades(event, 4, 1, r -> pickRandom(r, ItemInit.RUNIC_SILK.get(), ItemInit.SORCEROUS_SEWING_SET.get()));
            addRecipeTrades(event, 5, 1, r -> pickRandom(r, ItemInit.MANA_TEA.get(), ItemInit.WELLSPRING_DOWSING_ROD.get()));
            System.out.println("Registered Manaweaver Trades");
        }

        private static void addRecipeTrades(VillagerTradesEvent event, int level, int numRolls, Function<Random, ItemStack> generator) {
            addRecipeTrades(event, level, numRolls, 8, 10, 6, 14, generator);
        }

        private static void addRecipeTrades(VillagerTradesEvent event, int level, int numRolls, int maxTrades, int xp, int minSalePrice, int maxSalePrice, Function<Random, ItemStack> generator) {
            for (int r = 0; r < numRolls; r++) {
                ((List) event.getTrades().get(level)).add(new RandomTradeBuilder(maxTrades, xp, 0.05F).setPrice(ItemInit.TRANSMUTED_SILVER.get(), minSalePrice, maxSalePrice).setForSale(generator).build());
            }
        }

        private static void removeLibrarianTrades(VillagerTradesEvent event) {
            event.getTrades().forEach((i, t) -> {
                if (i <= 2) {
                    List<VillagerTrades.ItemListing> toRemove = new ArrayList();
                    for (VillagerTrades.ItemListing trade : t) {
                        if (trade instanceof VillagerTrades.EnchantBookForEmeralds) {
                            toRemove.add(trade);
                        }
                    }
                    t.removeAll(toRemove);
                }
            });
        }

        @SubscribeEvent
        public static void registerTrades(WandererTradesEvent event) {
            event.getGenericTrades().add(new RandomTradeBuilder(4, 10, 0.05F).setEmeraldPrice(1).setForSale(ItemInit.HEALING_POULTICE.get(), 3, 5).build());
            event.getGenericTrades().add(new RandomTradeBuilder(4, 10, 0.05F).setEmeraldPrice(1).setForSale(ItemInit.INFUSED_SILK.get(), 3, 5).build());
            event.getGenericTrades().add(new RandomTradeBuilder(4, 10, 0.05F).setEmeraldPrice(1).setForSale(ItemInit.MANA_TEA.get(), 3, 5).build());
            event.getGenericTrades().add(new RandomTradeBuilder(4, 10, 0.05F).setEmeraldPrice(1).setForSale(ItemInit.HERBALIST_POUCH.get(), 1, 1).build());
            event.getRareTrades().add(new RandomTradeBuilder(1, 10, 0.05F).setEmeraldPrice(1).setForSale(ItemInit.BED_CHARM.get(), 1, 1).build());
            event.getRareTrades().add(new RandomTradeBuilder(4, 10, 0.05F).setEmeraldPrice(1).setForSale(ItemInit.FALL_CHARM.get(), 1, 1).build());
            event.getRareTrades().add(new RandomTradeBuilder(4, 10, 0.05F).setEmeraldPrice(3, 5).setForSale(r -> ItemTornJournalPage.getRandomPage(RandomSource.create())).build());
            event.getRareTrades().add(new RandomTradeBuilder(8, 10, 0.05F).setPrice(ItemInit.VINTEUM_INGOT.get(), 5, 5).setForSale(ItemInit.GUIDE_BOOK.get(), 1, 1).build());
            System.out.println("Registered Wandering Trader Trades");
        }

        public static ItemStack pickRandom(Random random, int minQty, int maxQty, Item... items) {
            if (items.length == 0) {
                return ItemStack.EMPTY;
            } else {
                Item item = items[(int) (random.nextDouble() * (double) items.length)];
                ItemStack stack = new ItemStack(item);
                stack.setCount(random.nextInt(minQty, maxQty));
                return stack;
            }
        }

        public static ItemStack pickRandom(Random random, Item... items) {
            if (items.length == 0) {
                return ItemStack.EMPTY;
            } else {
                Item item = items[(int) (random.nextDouble() * (double) items.length)];
                return new ItemStack(item);
            }
        }
    }
}