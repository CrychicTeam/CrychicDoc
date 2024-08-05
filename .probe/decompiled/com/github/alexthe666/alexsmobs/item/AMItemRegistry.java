package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCockroachEgg;
import com.github.alexthe666.alexsmobs.entity.EntityEmuEgg;
import com.github.alexthe666.alexsmobs.entity.EntityEnderiophageRocket;
import com.github.alexthe666.alexsmobs.entity.EntitySharkToothArrow;
import com.github.alexthe666.alexsmobs.entity.EntityTossedItem;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.server.block.LecternBooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AMItemRegistry {

    public static final AMArmorMaterial ROADRUNNER_ARMOR_MATERIAL = new AMArmorMaterial("roadrunner", 18, new int[] { 3, 3, 3, 3 }, 20, SoundEvents.ARMOR_EQUIP_TURTLE, 0.0F);

    public static final AMArmorMaterial CROCODILE_ARMOR_MATERIAL = new AMArmorMaterial("crocodile", 22, new int[] { 2, 5, 7, 3 }, 25, SoundEvents.ARMOR_EQUIP_TURTLE, 1.0F);

    public static final AMArmorMaterial CENTIPEDE_ARMOR_MATERIAL = new AMArmorMaterial("centipede", 20, new int[] { 6, 6, 6, 6 }, 22, SoundEvents.ARMOR_EQUIP_TURTLE, 0.5F);

    public static final AMArmorMaterial MOOSE_ARMOR_MATERIAL = new AMArmorMaterial("moose", 19, new int[] { 3, 3, 3, 3 }, 21, SoundEvents.ARMOR_EQUIP_TURTLE, 0.5F);

    public static final AMArmorMaterial RACCOON_ARMOR_MATERIAL = new AMArmorMaterial("raccoon", 17, new int[] { 3, 3, 3, 3 }, 21, SoundEvents.ARMOR_EQUIP_LEATHER, 2.5F);

    public static final AMArmorMaterial SOMBRERO_ARMOR_MATERIAL = new AMArmorMaterial("sombrero", 14, new int[] { 2, 2, 2, 2 }, 30, SoundEvents.ARMOR_EQUIP_LEATHER, 0.5F);

    public static final AMArmorMaterial SPIKED_TURTLE_SHELL_ARMOR_MATERIAL = new AMArmorMaterial("spiked_turtle_shell", 35, new int[] { 3, 3, 3, 3 }, 30, SoundEvents.ARMOR_EQUIP_TURTLE, 1.0F, 0.2F);

    public static final AMArmorMaterial FEDORA_ARMOR_MATERIAL = new AMArmorMaterial("fedora", 10, new int[] { 2, 2, 2, 2 }, 30, SoundEvents.ARMOR_EQUIP_LEATHER, 0.5F);

    public static final AMArmorMaterial EMU_ARMOR_MATERIAL = new AMArmorMaterial("emu", 9, new int[] { 4, 4, 4, 4 }, 20, SoundEvents.ARMOR_EQUIP_LEATHER, 0.5F);

    public static final AMArmorMaterial TARANTULA_HAWK_ELYTRA_MATERIAL = new AMArmorMaterial("tarantula_hawk_elytra", 9, new int[] { 3, 3, 3, 3 }, 5, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F);

    public static final AMArmorMaterial FROSTSTALKER_ARMOR_MATERIAL = new AMArmorMaterial("froststalker", 9, new int[] { 3, 3, 3, 3 }, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.5F);

    public static final AMArmorMaterial ROCKY_ARMOR_MATERIAL = new AMArmorMaterial("rocky_roller", 20, new int[] { 2, 5, 7, 3 }, 10, SoundEvents.ARMOR_EQUIP_TURTLE, 0.5F);

    public static final AMArmorMaterial FLYING_FISH_MATERIAL = new AMArmorMaterial("flying_fish", 9, new int[] { 1, 1, 1, 1 }, 8, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F);

    public static final AMArmorMaterial NOVELTY_HAT_MATERIAL = new AMArmorMaterial("novelty_hat", 10, new int[] { 2, 2, 2, 2 }, 30, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F);

    public static final AMArmorMaterial KIMONO_MATERIAL = new AMArmorMaterial("kimono", 8, new int[] { 3, 3, 3, 3 }, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F);

    public static final DeferredRegister<Item> DEF_REG = DeferredRegister.create(ForgeRegistries.ITEMS, "alexsmobs");

    public static final RegistryObject<Item> TAB_ICON = DEF_REG.register("tab_icon", () -> new ItemTabIcon(new Item.Properties()));

    public static final RegistryObject<Item> ANIMAL_DICTIONARY = DEF_REG.register("animal_dictionary", () -> new ItemAnimalDictionary(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BEAR_FUR = DEF_REG.register("bear_fur", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BEAR_DUST = DEF_REG.register("bear_dust", () -> new ItemBearDust(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> ROADRUNNER_FEATHER = DEF_REG.register("roadrunner_feather", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ROADDRUNNER_BOOTS = DEF_REG.register("roadrunner_boots", () -> new ItemModArmor(ROADRUNNER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS));

    public static final RegistryObject<Item> LAVA_BOTTLE = DEF_REG.register("lava_bottle", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BONE_SERPENT_TOOTH = DEF_REG.register("bone_serpent_tooth", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> GAZELLE_HORN = DEF_REG.register("gazelle_horn", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> CROCODILE_SCUTE = DEF_REG.register("crocodile_scute", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CROCODILE_CHESTPLATE = DEF_REG.register("crocodile_chestplate", () -> new ItemModArmor(CROCODILE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE));

    public static final RegistryObject<Item> MAGGOT = DEF_REG.register("maggot", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build())));

    public static final RegistryObject<Item> BANANA = DEF_REG.register("banana", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build())));

    public static final RegistryObject<Item> ANCIENT_DART = DEF_REG.register("ancient_dart", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> HALO = DEF_REG.register("halo", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> BLOOD_SAC = DEF_REG.register("blood_sac", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MOSQUITO_PROBOSCIS = DEF_REG.register("mosquito_proboscis", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BLOOD_SPRAYER = DEF_REG.register("blood_sprayer", () -> new ItemBloodSprayer(new Item.Properties().durability(100)));

    public static final RegistryObject<Item> RATTLESNAKE_RATTLE = DEF_REG.register("rattlesnake_rattle", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHORUS_ON_A_STICK = DEF_REG.register("chorus_on_a_stick", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SHARK_TOOTH = DEF_REG.register("shark_tooth", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SHARK_TOOTH_ARROW = DEF_REG.register("shark_tooth_arrow", () -> new ItemModArrow(new Item.Properties()));

    public static final RegistryObject<Item> LOBSTER_TAIL = DEF_REG.register("lobster_tail", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.4F).meat().build())));

    public static final RegistryObject<Item> COOKED_LOBSTER_TAIL = DEF_REG.register("cooked_lobster_tail", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.65F).meat().build())));

    public static final RegistryObject<Item> LOBSTER_BUCKET = DEF_REG.register("lobster_bucket", () -> new ItemModFishBucket(AMEntityRegistry.LOBSTER, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> KOMODO_SPIT = DEF_REG.register("komodo_spit", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KOMODO_SPIT_BOTTLE = DEF_REG.register("komodo_spit_bottle", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> POISON_BOTTLE = DEF_REG.register("poison_bottle", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SOPA_DE_MACACO = DEF_REG.register("sopa_de_macaco", () -> new BowlFoodItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4F).meat().build()).stacksTo(1)));

    public static final RegistryObject<Item> CENTIPEDE_LEG = DEF_REG.register("centipede_leg", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CENTIPEDE_LEGGINGS = DEF_REG.register("centipede_leggings", () -> new ItemModArmor(CENTIPEDE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS));

    public static final RegistryObject<Item> MOSQUITO_LARVA = DEF_REG.register("mosquito_larva", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MOOSE_ANTLER = DEF_REG.register("moose_antler", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MOOSE_HEADGEAR = DEF_REG.register("moose_headgear", () -> new ItemModArmor(MOOSE_ARMOR_MATERIAL, ArmorItem.Type.HELMET));

    public static final RegistryObject<Item> MOOSE_RIBS = DEF_REG.register("moose_ribs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).meat().build())));

    public static final RegistryObject<Item> COOKED_MOOSE_RIBS = DEF_REG.register("cooked_moose_ribs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.85F).meat().build())));

    public static final RegistryObject<Item> MIMICREAM = DEF_REG.register("mimicream", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RACCOON_TAIL = DEF_REG.register("raccoon_tail", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FRONTIER_CAP = DEF_REG.register("frontier_cap", () -> new ItemModArmor(RACCOON_ARMOR_MATERIAL, ArmorItem.Type.HELMET));

    public static final RegistryObject<Item> BLOBFISH = DEF_REG.register("blobfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.4F).meat().effect(new MobEffectInstance(MobEffects.POISON, 120, 0), 1.0F).build())));

    public static final RegistryObject<Item> BLOBFISH_BUCKET = DEF_REG.register("blobfish_bucket", () -> new ItemModFishBucket(AMEntityRegistry.BLOBFISH, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> FISH_OIL = DEF_REG.register("fish_oil", () -> new ItemFishOil(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(new FoodProperties.Builder().nutrition(0).saturationMod(0.2F).build())));

    public static final RegistryObject<Item> MARACA = DEF_REG.register("maraca", () -> new ItemMaraca(new Item.Properties()));

    public static final RegistryObject<Item> SOMBRERO = DEF_REG.register("sombrero", () -> new ItemModArmor(SOMBRERO_ARMOR_MATERIAL, ArmorItem.Type.HELMET));

    public static final RegistryObject<Item> COCKROACH_WING_FRAGMENT = DEF_REG.register("cockroach_wing_fragment", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COCKROACH_WING = DEF_REG.register("cockroach_wing", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COCKROACH_OOTHECA = DEF_REG.register("cockroach_ootheca", () -> new ItemAnimalEgg(new Item.Properties()));

    public static final RegistryObject<Item> ACACIA_BLOSSOM = DEF_REG.register("acacia_blossom", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SOUL_HEART = DEF_REG.register("soul_heart", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SPIKED_SCUTE = DEF_REG.register("spiked_scute", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SPIKED_TURTLE_SHELL = DEF_REG.register("spiked_turtle_shell", () -> new ItemModArmor(SPIKED_TURTLE_SHELL_ARMOR_MATERIAL, ArmorItem.Type.HELMET));

    public static final RegistryObject<Item> SHRIMP_FRIED_RICE = DEF_REG.register("shrimp_fried_rice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(1.0F).build())));

    public static final RegistryObject<Item> GUSTER_EYE = DEF_REG.register("guster_eye", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> POCKET_SAND = DEF_REG.register("pocket_sand", () -> new ItemPocketSand(new Item.Properties().durability(220)));

    public static final RegistryObject<Item> WARPED_MUSCLE = DEF_REG.register("warped_muscle", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HEMOLYMPH_SAC = DEF_REG.register("hemolymph_sac", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HEMOLYMPH_BLASTER = DEF_REG.register("hemolymph_blaster", () -> new ItemHemolymphBlaster(new Item.Properties().durability(150)));

    public static final RegistryObject<Item> WARPED_MIXTURE = DEF_REG.register("warped_mixture", () -> new Item(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).craftRemainder(Items.GLASS_BOTTLE)));

    public static final RegistryObject<Item> STRADDLITE = DEF_REG.register("straddlite", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> STRADPOLE_BUCKET = DEF_REG.register("stradpole_bucket", () -> new ItemModFishBucket(AMEntityRegistry.STRADPOLE, Fluids.LAVA, new Item.Properties()));

    public static final RegistryObject<Item> STRADDLEBOARD = DEF_REG.register("straddleboard", () -> new ItemStraddleboard(new Item.Properties().fireResistant().durability(220)));

    public static final RegistryObject<Item> EMU_EGG = DEF_REG.register("emu_egg", () -> new ItemAnimalEgg(new Item.Properties().stacksTo(8)));

    public static final RegistryObject<Item> BOILED_EMU_EGG = DEF_REG.register("boiled_emu_egg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1.0F).meat().build())));

    public static final RegistryObject<Item> EMU_FEATHER = DEF_REG.register("emu_feather", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> EMU_LEGGINGS = DEF_REG.register("emu_leggings", () -> new ItemModArmor(EMU_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS));

    public static final RegistryObject<Item> PLATYPUS_BUCKET = DEF_REG.register("platypus_bucket", () -> new ItemModFishBucket(AMEntityRegistry.PLATYPUS, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> FEDORA = DEF_REG.register("fedora", () -> new ItemModArmor(FEDORA_ARMOR_MATERIAL, ArmorItem.Type.HELMET));

    public static final RegistryObject<Item> DROPBEAR_CLAW = DEF_REG.register("dropbear_claw", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KANGAROO_MEAT = DEF_REG.register("kangaroo_meat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).meat().build())));

    public static final RegistryObject<Item> COOKED_KANGAROO_MEAT = DEF_REG.register("cooked_kangaroo_meat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.85F).meat().build())));

    public static final RegistryObject<Item> KANGAROO_HIDE = DEF_REG.register("kangaroo_hide", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KANGAROO_BURGER = DEF_REG.register("kangaroo_burger", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(1.0F).meat().build())));

    public static final RegistryObject<Item> AMBERGRIS = DEF_REG.register("ambergris", () -> new ItemFuel(new Item.Properties(), 12800));

    public static final RegistryObject<Item> CACHALOT_WHALE_TOOTH = DEF_REG.register("cachalot_whale_tooth", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ECHOLOCATOR = DEF_REG.register("echolocator", () -> new ItemEcholocator(new Item.Properties().durability(100), ItemEcholocator.EchoType.ECHOLOCATION));

    public static final RegistryObject<Item> ENDOLOCATOR = DEF_REG.register("endolocator", () -> new ItemEcholocator(new Item.Properties().durability(25), ItemEcholocator.EchoType.ENDER));

    public static final RegistryObject<Item> GONGYLIDIA = DEF_REG.register("gongylidia", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(1.2F).build())));

    public static final RegistryObject<Item> LEAFCUTTER_ANT_PUPA = DEF_REG.register("leafcutter_ant_pupa", () -> new ItemLeafcutterPupa(new Item.Properties()));

    public static final RegistryObject<Item> ENDERIOPHAGE_ROCKET = DEF_REG.register("enderiophage_rocket", () -> new ItemEnderiophageRocket(new Item.Properties()));

    public static final RegistryObject<Item> FALCONRY_GLOVE_INVENTORY = DEF_REG.register("falconry_glove_inventory", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> FALCONRY_GLOVE_HAND = DEF_REG.register("falconry_glove_hand", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> FALCONRY_GLOVE = DEF_REG.register("falconry_glove", () -> new ItemFalconryGlove(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> FALCONRY_HOOD = DEF_REG.register("falconry_hood", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TARANTULA_HAWK_WING_FRAGMENT = DEF_REG.register("tarantula_hawk_wing_fragment", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TARANTULA_HAWK_WING = DEF_REG.register("tarantula_hawk_wing", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TARANTULA_HAWK_ELYTRA = DEF_REG.register("tarantula_hawk_elytra", () -> new ItemTarantulaHawkElytra(new Item.Properties().durability(800).rarity(Rarity.UNCOMMON), TARANTULA_HAWK_ELYTRA_MATERIAL));

    public static final RegistryObject<Item> MYSTERIOUS_WORM = DEF_REG.register("mysterious_worm", () -> new ItemMysteriousWorm(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> VOID_WORM_MANDIBLE = DEF_REG.register("void_worm_mandible", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> VOID_WORM_EYE = DEF_REG.register("void_worm_eye", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> DIMENSIONAL_CARVER = DEF_REG.register("dimensional_carver", () -> new ItemDimensionalCarver(new Item.Properties().durability(20).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SHATTERED_DIMENSIONAL_CARVER = DEF_REG.register("shattered_dimensional_carver", () -> new ItemShatteredDimensionalCarver(new Item.Properties().durability(4).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> SERRATED_SHARK_TOOTH = DEF_REG.register("serrated_shark_tooth", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FRILLED_SHARK_BUCKET = DEF_REG.register("frilled_shark_bucket", () -> new ItemModFishBucket(AMEntityRegistry.FRILLED_SHARK, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> SHIELD_OF_THE_DEEP = DEF_REG.register("shield_of_the_deep", () -> new ItemShieldOfTheDeep(new Item.Properties().durability(400).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> MIMIC_OCTOPUS_BUCKET = DEF_REG.register("mimic_octopus_bucket", () -> new ItemModFishBucket(AMEntityRegistry.MIMIC_OCTOPUS, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> FROSTSTALKER_HORN = DEF_REG.register("froststalker_horn", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FROSTSTALKER_HELMET = DEF_REG.register("froststalker_helmet", () -> new ItemModArmor(FROSTSTALKER_ARMOR_MATERIAL, ArmorItem.Type.HELMET));

    public static final RegistryObject<Item> PIGSHOES = DEF_REG.register("pigshoes", () -> new ItemPigshoes(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> STRADDLE_HELMET = DEF_REG.register("straddle_helmet", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> STRADDLE_SADDLE = DEF_REG.register("straddle_saddle", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COSMIC_COD = DEF_REG.register("cosmic_cod", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.3F).effect(new MobEffectInstance(AMEffectRegistry.ENDER_FLU.get(), 12000), 0.15F).build())));

    public static final RegistryObject<Item> SHED_SNAKE_SKIN = DEF_REG.register("shed_snake_skin", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> VINE_LASSO_INVENTORY = DEF_REG.register("vine_lasso_inventory", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> VINE_LASSO_HAND = DEF_REG.register("vine_lasso_hand", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> VINE_LASSO = DEF_REG.register("vine_lasso", () -> new ItemVineLasso(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ROCKY_SHELL = DEF_REG.register("rocky_shell", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ROCKY_CHESTPLATE = DEF_REG.register("rocky_chestplate", () -> new ItemModArmor(ROCKY_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE));

    public static final RegistryObject<Item> POTTED_FLUTTER = DEF_REG.register("potted_flutter", () -> new ItemFlutterPot(new Item.Properties()));

    public static final RegistryObject<Item> TERRAPIN_BUCKET = DEF_REG.register("terrapin_bucket", () -> new ItemModFishBucket(AMEntityRegistry.TERRAPIN, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> COMB_JELLY_BUCKET = DEF_REG.register("comb_jelly_bucket", () -> new ItemModFishBucket(AMEntityRegistry.COMB_JELLY, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> RAINBOW_JELLY = DEF_REG.register("rainbow_jelly", () -> new ItemRainbowJelly(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build())));

    public static final RegistryObject<Item> COSMIC_COD_BUCKET = DEF_REG.register("cosmic_cod_bucket", () -> new ItemCosmicCodBucket(new Item.Properties()));

    public static final RegistryObject<Item> MUNGAL_SPORES = DEF_REG.register("mungal_spores", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BISON_FUR = DEF_REG.register("bison_fur", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LOST_TENTACLE = DEF_REG.register("lost_tentacle", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SQUID_GRAPPLE = DEF_REG.register("squid_grapple", () -> new ItemSquidGrapple(new Item.Properties().durability(450)));

    public static final RegistryObject<Item> DEVILS_HOLE_PUPFISH_BUCKET = DEF_REG.register("devils_hole_pupfish_bucket", () -> new ItemModFishBucket(AMEntityRegistry.DEVILS_HOLE_PUPFISH, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> PUPFISH_LOCATOR = DEF_REG.register("pupfish_locator", () -> new ItemEcholocator(new Item.Properties().durability(200), ItemEcholocator.EchoType.PUPFISH));

    public static final RegistryObject<Item> SMALL_CATFISH_BUCKET = DEF_REG.register("small_catfish_bucket", () -> new ItemModFishBucket(AMEntityRegistry.CATFISH, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> MEDIUM_CATFISH_BUCKET = DEF_REG.register("medium_catfish_bucket", () -> new ItemModFishBucket(AMEntityRegistry.CATFISH, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> LARGE_CATFISH_BUCKET = DEF_REG.register("large_catfish_bucket", () -> new ItemModFishBucket(AMEntityRegistry.CATFISH, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> RAW_CATFISH = DEF_REG.register("raw_catfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).meat().build())));

    public static final RegistryObject<Item> COOKED_CATFISH = DEF_REG.register("cooked_catfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.5F).meat().build())));

    public static final RegistryObject<Item> FLYING_FISH = DEF_REG.register("flying_fish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.4F).meat().build())));

    public static final RegistryObject<Item> FLYING_FISH_BOOTS = DEF_REG.register("flying_fish_boots", () -> new ItemModArmor(FLYING_FISH_MATERIAL, ArmorItem.Type.BOOTS));

    public static final RegistryObject<Item> FLYING_FISH_BUCKET = DEF_REG.register("flying_fish_bucket", () -> new ItemModFishBucket(AMEntityRegistry.FLYING_FISH, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> FISH_BONES = DEF_REG.register("fish_bones", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SKELEWAG_SWORD_INVENTORY = DEF_REG.register("skelewag_sword_inventory", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> SKELEWAG_SWORD_HAND = DEF_REG.register("skelewag_sword_hand", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> SKELEWAG_SWORD = DEF_REG.register("skelewag_sword", () -> new ItemSkelewagSword(new Item.Properties().stacksTo(1).durability(430)));

    public static final RegistryObject<Item> NOVELTY_HAT = DEF_REG.register("novelty_hat", () -> new ItemModArmor(NOVELTY_HAT_MATERIAL, ArmorItem.Type.HELMET));

    public static final RegistryObject<Item> MUDSKIPPER_BUCKET = DEF_REG.register("mudskipper_bucket", () -> new ItemModFishBucket(AMEntityRegistry.MUDSKIPPER, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> FARSEER_ARM = DEF_REG.register("farseer_arm", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> SKREECHER_SOUL = DEF_REG.register("skreecher_soul", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GHOSTLY_PICKAXE = DEF_REG.register("ghostly_pickaxe", () -> new ItemGhostlyPickaxe(new Item.Properties()));

    public static final RegistryObject<Item> ELASTIC_TENDON = DEF_REG.register("elastic_tendon", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TENDON_WHIP = DEF_REG.register("tendon_whip", () -> new ItemTendonWhip(new Item.Properties()));

    public static final RegistryObject<Item> UNSETTLING_KIMONO = DEF_REG.register("unsettling_kimono", () -> new ItemModArmor(KIMONO_MATERIAL, ArmorItem.Type.CHESTPLATE));

    public static final RegistryObject<Item> STINK_BOTTLE = DEF_REG.register("stink_bottle", () -> new ItemStinkBottle(AMBlockRegistry.SKUNK_SPRAY, new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> STINK_RAY_HAND = DEF_REG.register("stink_ray_hand", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> STINK_RAY_INVENTORY = DEF_REG.register("stink_ray_inventory", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> STINK_RAY_EMPTY_HAND = DEF_REG.register("stink_ray_empty_hand", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> STINK_RAY_EMPTY_INVENTORY = DEF_REG.register("stink_ray_empty_inventory", () -> new ItemInventoryOnly(new Item.Properties()));

    public static final RegistryObject<Item> STINK_RAY = DEF_REG.register("stink_ray", () -> new ItemStinkRay(new Item.Properties().durability(5)));

    public static final RegistryObject<Item> BANANA_SLUG_SLIME = DEF_REG.register("banana_slug_slime", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MOSQUITO_REPELLENT_STEW = DEF_REG.register("mosquito_repellent_stew", () -> new BowlFoodItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).alwaysEat().saturationMod(0.3F).effect(() -> new MobEffectInstance(AMEffectRegistry.MOSQUITO_REPELLENT.get(), 24000), 1.0F).build()).stacksTo(1)));

    public static final RegistryObject<Item> TRIOPS_BUCKET = DEF_REG.register("triops_bucket", () -> new ItemModFishBucket(AMEntityRegistry.TRIOPS, Fluids.WATER, new Item.Properties()));

    public static final RegistryObject<Item> MUSIC_DISC_THIME = DEF_REG.register("music_disc_thime", () -> new RecordItem(14, AMSoundRegistry.MUSIC_DISC_THIME, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 3820));

    public static final RegistryObject<Item> MUSIC_DISC_DAZE = DEF_REG.register("music_disc_daze", () -> new RecordItem(14, AMSoundRegistry.MUSIC_DISC_DAZE, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 6280));

    public static void initSpawnEggs() {
        DEF_REG.register("spawn_egg_grizzly_bear", () -> new ForgeSpawnEggItem(AMEntityRegistry.GRIZZLY_BEAR, 6896172, 9920836, new Item.Properties()));
        DEF_REG.register("spawn_egg_roadrunner", () -> new ForgeSpawnEggItem(AMEntityRegistry.ROADRUNNER, 3812902, 16509390, new Item.Properties()));
        DEF_REG.register("spawn_egg_bone_serpent", () -> new ForgeSpawnEggItem(AMEntityRegistry.BONE_SERPENT, 15063492, 16736312, new Item.Properties()));
        DEF_REG.register("spawn_egg_gazelle", () -> new ForgeSpawnEggItem(AMEntityRegistry.GAZELLE, 14526069, 2894117, new Item.Properties()));
        DEF_REG.register("spawn_egg_crocodile", () -> new ForgeSpawnEggItem(AMEntityRegistry.CROCODILE, 7571776, 10920286, new Item.Properties()));
        DEF_REG.register("spawn_egg_fly", () -> new ForgeSpawnEggItem(AMEntityRegistry.FLY, 4604481, 8990254, new Item.Properties()));
        DEF_REG.register("spawn_egg_hummingbird", () -> new ForgeSpawnEggItem(AMEntityRegistry.HUMMINGBIRD, 3300991, 4499295, new Item.Properties()));
        DEF_REG.register("spawn_egg_orca", () -> new ForgeSpawnEggItem(AMEntityRegistry.ORCA, 2894892, 14080228, new Item.Properties()));
        DEF_REG.register("spawn_egg_sunbird", () -> new ForgeSpawnEggItem(AMEntityRegistry.SUNBIRD, 16148815, 16768416, new Item.Properties()));
        DEF_REG.register("spawn_egg_gorilla", () -> new ForgeSpawnEggItem(AMEntityRegistry.GORILLA, 5856093, 1842209, new Item.Properties()));
        DEF_REG.register("spawn_egg_crimson_mosquito", () -> new ForgeSpawnEggItem(AMEntityRegistry.CRIMSON_MOSQUITO, 5455935, 12655130, new Item.Properties()));
        DEF_REG.register("spawn_egg_rattlesnake", () -> new ForgeSpawnEggItem(AMEntityRegistry.RATTLESNAKE, 13547924, 9665115, new Item.Properties()));
        DEF_REG.register("spawn_egg_endergrade", () -> new ForgeSpawnEggItem(AMEntityRegistry.ENDERGRADE, 7889587, 8502763, new Item.Properties()));
        DEF_REG.register("spawn_egg_hammerhead_shark", () -> new ForgeSpawnEggItem(AMEntityRegistry.HAMMERHEAD_SHARK, 9081525, 12173016, new Item.Properties()));
        DEF_REG.register("spawn_egg_lobster", () -> new ForgeSpawnEggItem(AMEntityRegistry.LOBSTER, 12857635, 14507832, new Item.Properties()));
        DEF_REG.register("spawn_egg_komodo_dragon", () -> new ForgeSpawnEggItem(AMEntityRegistry.KOMODO_DRAGON, 7629903, 5653041, new Item.Properties()));
        DEF_REG.register("spawn_egg_capuchin_monkey", () -> new ForgeSpawnEggItem(AMEntityRegistry.CAPUCHIN_MONKEY, 2433311, 15850163, new Item.Properties()));
        DEF_REG.register("spawn_egg_centipede", () -> new ForgeSpawnEggItem(AMEntityRegistry.CENTIPEDE_HEAD, 3418926, 7550025, new Item.Properties()));
        DEF_REG.register("spawn_egg_warped_toad", () -> new ForgeSpawnEggItem(AMEntityRegistry.WARPED_TOAD, 2070158, 16690285, new Item.Properties()));
        DEF_REG.register("spawn_egg_moose", () -> new ForgeSpawnEggItem(AMEntityRegistry.MOOSE, 3551274, 13939075, new Item.Properties()));
        DEF_REG.register("spawn_egg_mimicube", () -> new ForgeSpawnEggItem(AMEntityRegistry.MIMICUBE, 9076929, 6180719, new Item.Properties()));
        DEF_REG.register("spawn_egg_raccoon", () -> new ForgeSpawnEggItem(AMEntityRegistry.RACCOON, 8749694, 2762534, new Item.Properties()));
        DEF_REG.register("spawn_egg_blobfish", () -> new ForgeSpawnEggItem(AMEntityRegistry.BLOBFISH, 14403261, 10386047, new Item.Properties()));
        DEF_REG.register("spawn_egg_seal", () -> new ForgeSpawnEggItem(AMEntityRegistry.SEAL, 4734002, 6707532, new Item.Properties()));
        DEF_REG.register("spawn_egg_cockroach", () -> new ForgeSpawnEggItem(AMEntityRegistry.COCKROACH, 854281, 4334622, new Item.Properties()));
        DEF_REG.register("spawn_egg_shoebill", () -> new ForgeSpawnEggItem(AMEntityRegistry.SHOEBILL, 8553090, 14005386, new Item.Properties()));
        DEF_REG.register("spawn_egg_elephant", () -> new ForgeSpawnEggItem(AMEntityRegistry.ELEPHANT, 9275783, 15590865, new Item.Properties()));
        DEF_REG.register("spawn_egg_soul_vulture", () -> new ForgeSpawnEggItem(AMEntityRegistry.SOUL_VULTURE, 2303533, 5764351, new Item.Properties()));
        DEF_REG.register("spawn_egg_snow_leopard", () -> new ForgeSpawnEggItem(AMEntityRegistry.SNOW_LEOPARD, 11313811, 2498589, new Item.Properties()));
        DEF_REG.register("spawn_egg_spectre", () -> new ForgeSpawnEggItem(AMEntityRegistry.SPECTRE, 13160687, 8884719, new Item.Properties()));
        DEF_REG.register("spawn_egg_crow", () -> new ForgeSpawnEggItem(AMEntityRegistry.CROW, 856348, 1843248, new Item.Properties()));
        DEF_REG.register("spawn_egg_alligator_snapping_turtle", () -> new ForgeSpawnEggItem(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE, 7101522, 4548902, new Item.Properties()));
        DEF_REG.register("spawn_egg_mungus", () -> new ForgeSpawnEggItem(AMEntityRegistry.MUNGUS, 8612493, 4539724, new Item.Properties()));
        DEF_REG.register("spawn_egg_mantis_shrimp", () -> new ForgeSpawnEggItem(AMEntityRegistry.MANTIS_SHRIMP, 14370904, 1415454, new Item.Properties()));
        DEF_REG.register("spawn_egg_guster", () -> new ForgeSpawnEggItem(AMEntityRegistry.GUSTER, 16307354, 16740874, new Item.Properties()));
        DEF_REG.register("spawn_egg_warped_mosco", () -> new ForgeSpawnEggItem(AMEntityRegistry.WARPED_MOSCO, 3288920, 5988081, new Item.Properties()));
        DEF_REG.register("spawn_egg_straddler", () -> new ForgeSpawnEggItem(AMEntityRegistry.STRADDLER, 6119278, 13478022, new Item.Properties()));
        DEF_REG.register("spawn_egg_stradpole", () -> new ForgeSpawnEggItem(AMEntityRegistry.STRADPOLE, 6119278, 5728907, new Item.Properties()));
        DEF_REG.register("spawn_egg_emu", () -> new ForgeSpawnEggItem(AMEntityRegistry.EMU, 6705990, 3881272, new Item.Properties()));
        DEF_REG.register("spawn_egg_platypus", () -> new ForgeSpawnEggItem(AMEntityRegistry.PLATYPUS, 8212542, 3554115, new Item.Properties()));
        DEF_REG.register("spawn_egg_dropbear", () -> new ForgeSpawnEggItem(AMEntityRegistry.DROPBEAR, 9055541, 6333347, new Item.Properties()));
        DEF_REG.register("spawn_egg_tasmanian_devil", () -> new ForgeSpawnEggItem(AMEntityRegistry.TASMANIAN_DEVIL, 2434086, 11056319, new Item.Properties()));
        DEF_REG.register("spawn_egg_kangaroo", () -> new ForgeSpawnEggItem(AMEntityRegistry.KANGAROO, 13540709, 14597536, new Item.Properties()));
        DEF_REG.register("spawn_egg_cachalot_whale", () -> new ForgeSpawnEggItem(AMEntityRegistry.CACHALOT_WHALE, 9738393, 6252142, new Item.Properties()));
        DEF_REG.register("spawn_egg_leafcutter_ant", () -> new ForgeSpawnEggItem(AMEntityRegistry.LEAFCUTTER_ANT, 9846819, 10901808, new Item.Properties()));
        DEF_REG.register("spawn_egg_enderiophage", () -> new ForgeSpawnEggItem(AMEntityRegistry.ENDERIOPHAGE, 8859011, 16179917, new Item.Properties()));
        DEF_REG.register("spawn_egg_bald_eagle", () -> new ForgeSpawnEggItem(AMEntityRegistry.BALD_EAGLE, 3284760, 16053492, new Item.Properties()));
        DEF_REG.register("spawn_egg_tiger", () -> new ForgeSpawnEggItem(AMEntityRegistry.TIGER, 13066542, 2765363, new Item.Properties()));
        DEF_REG.register("spawn_egg_tarantula_hawk", () -> new ForgeSpawnEggItem(AMEntityRegistry.TARANTULA_HAWK, 2312035, 14908216, new Item.Properties()));
        DEF_REG.register("spawn_egg_void_worm", () -> new ForgeSpawnEggItem(AMEntityRegistry.VOID_WORM, 987174, 1481131, new Item.Properties()));
        DEF_REG.register("spawn_egg_frilled_shark", () -> new ForgeSpawnEggItem(AMEntityRegistry.FRILLED_SHARK, 7498603, 8863037, new Item.Properties()));
        DEF_REG.register("spawn_egg_mimic_octopus", () -> new ForgeSpawnEggItem(AMEntityRegistry.MIMIC_OCTOPUS, 16772060, 1907743, new Item.Properties()));
        DEF_REG.register("spawn_egg_seagull", () -> new ForgeSpawnEggItem(AMEntityRegistry.SEAGULL, 13226716, 16767056, new Item.Properties()));
        DEF_REG.register("spawn_egg_froststalker", () -> new ForgeSpawnEggItem(AMEntityRegistry.FROSTSTALKER, 7899841, 10601471, new Item.Properties()));
        DEF_REG.register("spawn_egg_tusklin", () -> new ForgeSpawnEggItem(AMEntityRegistry.TUSKLIN, 7559233, 15262421, new Item.Properties()));
        DEF_REG.register("spawn_egg_laviathan", () -> new ForgeSpawnEggItem(AMEntityRegistry.LAVIATHAN, 14058326, 3946823, new Item.Properties()));
        DEF_REG.register("spawn_egg_cosmaw", () -> new ForgeSpawnEggItem(AMEntityRegistry.COSMAW, 7630269, 14073827, new Item.Properties()));
        DEF_REG.register("spawn_egg_toucan", () -> new ForgeSpawnEggItem(AMEntityRegistry.TOUCAN, 16092979, 1974579, new Item.Properties()));
        DEF_REG.register("spawn_egg_maned_wolf", () -> new ForgeSpawnEggItem(AMEntityRegistry.MANED_WOLF, 12286535, 4204314, new Item.Properties()));
        DEF_REG.register("spawn_egg_anaconda", () -> new ForgeSpawnEggItem(AMEntityRegistry.ANACONDA, 5659682, 13858367, new Item.Properties()));
        DEF_REG.register("spawn_egg_anteater", () -> new ForgeSpawnEggItem(AMEntityRegistry.ANTEATER, 4996922, 13417652, new Item.Properties()));
        DEF_REG.register("spawn_egg_rocky_roller", () -> new ForgeSpawnEggItem(AMEntityRegistry.ROCKY_ROLLER, 11568495, 10064260, new Item.Properties()));
        DEF_REG.register("spawn_egg_flutter", () -> new ForgeSpawnEggItem(AMEntityRegistry.FLUTTER, 7377453, 13663203, new Item.Properties()));
        DEF_REG.register("spawn_egg_gelada_monkey", () -> new ForgeSpawnEggItem(AMEntityRegistry.GELADA_MONKEY, 11570276, 16731987, new Item.Properties()));
        DEF_REG.register("spawn_egg_jerboa", () -> new ForgeSpawnEggItem(AMEntityRegistry.JERBOA, 14599562, 14589328, new Item.Properties()));
        DEF_REG.register("spawn_egg_terrapin", () -> new ForgeSpawnEggItem(AMEntityRegistry.TERRAPIN, 7237168, 9606727, new Item.Properties()));
        DEF_REG.register("spawn_egg_comb_jelly", () -> new ForgeSpawnEggItem(AMEntityRegistry.COMB_JELLY, 13625854, 7274379, new Item.Properties()));
        DEF_REG.register("spawn_egg_cosmic_cod", () -> new ForgeSpawnEggItem(AMEntityRegistry.COSMIC_COD, 6915527, 14864895, new Item.Properties()));
        DEF_REG.register("spawn_egg_bunfungus", () -> new ForgeSpawnEggItem(AMEntityRegistry.BUNFUNGUS, 7302545, 13183785, new Item.Properties()));
        DEF_REG.register("spawn_egg_bison", () -> new ForgeSpawnEggItem(AMEntityRegistry.BISON, 4995630, 8021318, new Item.Properties()));
        DEF_REG.register("spawn_egg_giant_squid", () -> new ForgeSpawnEggItem(AMEntityRegistry.GIANT_SQUID, 11225933, 14056811, new Item.Properties()));
        DEF_REG.register("spawn_egg_devils_hole_pupfish", () -> new ForgeSpawnEggItem(AMEntityRegistry.DEVILS_HOLE_PUPFISH, 5667780, 7095413, new Item.Properties()));
        DEF_REG.register("spawn_egg_catfish", () -> new ForgeSpawnEggItem(AMEntityRegistry.CATFISH, 8419159, 9073766, new Item.Properties()));
        DEF_REG.register("spawn_egg_flying_fish", () -> new ForgeSpawnEggItem(AMEntityRegistry.FLYING_FISH, 8109293, 6848947, new Item.Properties()));
        DEF_REG.register("spawn_egg_skelewag", () -> new ForgeSpawnEggItem(AMEntityRegistry.SKELEWAG, 14286001, 3821360, new Item.Properties()));
        DEF_REG.register("spawn_egg_rain_frog", () -> new ForgeSpawnEggItem(AMEntityRegistry.RAIN_FROG, 12629403, 8086863, new Item.Properties()));
        DEF_REG.register("spawn_egg_potoo", () -> new ForgeSpawnEggItem(AMEntityRegistry.POTOO, 9205587, 16760898, new Item.Properties()));
        DEF_REG.register("spawn_egg_mudskipper", () -> new ForgeSpawnEggItem(AMEntityRegistry.MUDSKIPPER, 6320202, 4817004, new Item.Properties()));
        DEF_REG.register("spawn_egg_rhinoceros", () -> new ForgeSpawnEggItem(AMEntityRegistry.RHINOCEROS, 10589588, 8549492, new Item.Properties()));
        DEF_REG.register("spawn_egg_sugar_glider", () -> new ForgeSpawnEggItem(AMEntityRegistry.SUGAR_GLIDER, 8814977, 15461344, new Item.Properties()));
        DEF_REG.register("spawn_egg_farseer", () -> new ForgeSpawnEggItem(AMEntityRegistry.FARSEER, 3356495, 9568089, new Item.Properties()));
        DEF_REG.register("spawn_egg_skreecher", () -> new ForgeSpawnEggItem(AMEntityRegistry.SKREECHER, 477271, 8386815, new Item.Properties()));
        DEF_REG.register("spawn_egg_underminer", () -> new ForgeSpawnEggItem(AMEntityRegistry.UNDERMINER, 14082815, 7111876, new Item.Properties()));
        DEF_REG.register("spawn_egg_murmur", () -> new ForgeSpawnEggItem(AMEntityRegistry.MURMUR, 8406088, 11906972, new Item.Properties()));
        DEF_REG.register("spawn_egg_skunk", () -> new ForgeSpawnEggItem(AMEntityRegistry.SKUNK, 2239798, 15001074, new Item.Properties()));
        DEF_REG.register("spawn_egg_banana_slug", () -> new ForgeSpawnEggItem(AMEntityRegistry.BANANA_SLUG, 16764997, 16773491, new Item.Properties()));
        DEF_REG.register("spawn_egg_blue_jay", () -> new ForgeSpawnEggItem(AMEntityRegistry.BLUE_JAY, 6273022, 2702146, new Item.Properties()));
        DEF_REG.register("spawn_egg_caiman", () -> new ForgeSpawnEggItem(AMEntityRegistry.CAIMAN, 6051377, 12305500, new Item.Properties()));
        DEF_REG.register("spawn_egg_triops", () -> new ForgeSpawnEggItem(AMEntityRegistry.TRIOPS, 9861460, 13267280, new Item.Properties()));
        registerPatternItem("bear");
        registerPatternItem("australia_0");
        registerPatternItem("australia_1");
        registerPatternItem("new_mexico");
        registerPatternItem("brazil");
        for (int i = 0; i <= 10; i++) {
            DEF_REG.register("dimensional_carver_shard_" + i, () -> new ItemInventoryOnly(new Item.Properties()));
        }
    }

    private static void registerPatternItem(String name) {
        TagKey<BannerPattern> bannerPatternTagKey = TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation("alexsmobs", "pattern_for_" + name));
        DEF_REG.register("banner_pattern_" + name, () -> new BannerPatternItem(bannerPatternTagKey, new Item.Properties().stacksTo(1)));
    }

    public static void init() {
        CROCODILE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(CROCODILE_SCUTE.get()));
        ROADRUNNER_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(ROADRUNNER_FEATHER.get()));
        CENTIPEDE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(CENTIPEDE_LEG.get()));
        MOOSE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(MOOSE_ANTLER.get()));
        RACCOON_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(RACCOON_TAIL.get()));
        SOMBRERO_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(Items.HAY_BLOCK));
        SPIKED_TURTLE_SHELL_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(SPIKED_SCUTE.get()));
        FEDORA_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(Items.LEATHER));
        EMU_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(EMU_FEATHER.get()));
        ROCKY_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(ROCKY_SHELL.get()));
        FLYING_FISH_MATERIAL.setRepairMaterial(Ingredient.of(FLYING_FISH.get()));
        NOVELTY_HAT_MATERIAL.setRepairMaterial(Ingredient.of(Items.BONE));
        KIMONO_MATERIAL.setRepairMaterial(Ingredient.of(ItemTags.WOOL));
        LecternBooks.BOOKS.put(ANIMAL_DICTIONARY.getId(), new LecternBooks.BookData(6318886, 16644333));
    }

    public static void initDispenser() {
        DispenserBlock.registerBehavior(SHARK_TOOTH_ARROW.get(), new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntitySharkToothArrow entityarrow = new EntitySharkToothArrow(AMEntityRegistry.SHARK_TOOTH_ARROW.get(), position.x(), position.y(), position.z(), worldIn);
                entityarrow.f_36705_ = AbstractArrow.Pickup.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(ANCIENT_DART.get(), new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityTossedItem tossedItem = new EntityTossedItem(worldIn, position.x(), position.y(), position.z());
                tossedItem.setDart(true);
                return tossedItem;
            }
        });
        DispenserBlock.registerBehavior(COCKROACH_OOTHECA.get(), new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                return new EntityCockroachEgg(worldIn, position.x(), position.y(), position.z());
            }
        });
        DispenserBlock.registerBehavior(EMU_EGG.get(), new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                return new EntityEmuEgg(worldIn, position.x(), position.y(), position.z());
            }
        });
        DispenserBlock.registerBehavior(ENDERIOPHAGE_ROCKET.get(), new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                return new EntityEnderiophageRocket(worldIn, position.x(), position.y(), position.z(), stackIn);
            }
        });
        DispenseItemBehavior bucketDispenseBehavior = new DefaultDispenseItemBehavior() {

            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource blockSource, ItemStack stack) {
                DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem) stack.getItem();
                BlockPos blockpos = blockSource.getPos().relative((Direction) blockSource.getBlockState().m_61143_(DispenserBlock.FACING));
                Level level = blockSource.getLevel();
                if (dispensiblecontaineritem.emptyContents((Player) null, level, blockpos, (BlockHitResult) null)) {
                    dispensiblecontaineritem.checkExtraContent((Player) null, level, stack, blockpos);
                    return new ItemStack(Items.BUCKET);
                } else {
                    return this.defaultDispenseItemBehavior.dispense(blockSource, stack);
                }
            }
        };
        DispenserBlock.registerBehavior(LOBSTER_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(BLOBFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(STRADPOLE_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(PLATYPUS_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(FRILLED_SHARK_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(MIMIC_OCTOPUS_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(TERRAPIN_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(COMB_JELLY_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(COSMIC_COD_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(DEVILS_HOLE_PUPFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(SMALL_CATFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(MEDIUM_CATFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(LARGE_CATFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(FLYING_FISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(MUDSKIPPER_BUCKET.get(), bucketDispenseBehavior);
        ComposterBlock.COMPOSTABLES.put(BANANA.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(AMBlockRegistry.BANANA_PEEL.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ACACIA_BLOSSOM.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(GONGYLIDIA.get(), 0.9F);
    }

    static {
        initSpawnEggs();
    }
}