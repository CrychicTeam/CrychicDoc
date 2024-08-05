package dev.xkmc.l2hostility.init.data;

import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2damagetracker.init.data.ArmorEffectConfig;
import dev.xkmc.l2hostility.compat.data.BoMDData;
import dev.xkmc.l2hostility.compat.data.CataclysmData;
import dev.xkmc.l2hostility.compat.data.IaFData;
import dev.xkmc.l2hostility.compat.data.MowzieData;
import dev.xkmc.l2hostility.compat.data.MutantMonsterData;
import dev.xkmc.l2hostility.compat.data.TFData;
import dev.xkmc.l2hostility.compat.gateway.GatewayConfigGen;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.List;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;

public class LHConfigGen extends ConfigDataProvider {

    public LHConfigGen(DataGenerator generator) {
        super(generator, "L2Hostility Config");
    }

    @Override
    public void add(ConfigDataProvider.Collector collector) {
        L2Hostility.REGISTRATE.CONFIGS.forEach(e -> e.accept(collector));
        collector.add(L2DamageTracker.ARMOR, new ResourceLocation("l2hostility", "equipments"), new ArmorEffectConfig().add(LHItems.CURSE_WRATH.getId().toString(), new MobEffect[] { MobEffects.BLINDNESS, MobEffects.DARKNESS, MobEffects.CONFUSION, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS }));
        collector.add(L2Hostility.DIFFICULTY, new ResourceLocation("l2hostility", "overworld"), new WorldDifficultyConfig().putDim(Level.OVERWORLD, 0, 0, 4.0, 1.0).putBiome(0, 5, 1.0, 0.0, Biomes.LUSH_CAVES, Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.TAIGA, Biomes.SNOWY_TAIGA).putBiome(0, 5, 1.0, 0.2, Biomes.DRIPSTONE_CAVES, Biomes.DARK_FOREST, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_SAVANNA).putBiome(0, 10, 1.0, 0.2, Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.MUSHROOM_FIELDS, Biomes.STONY_SHORE, Biomes.SWAMP, Biomes.MANGROVE_SWAMP).putBiome(0, 20, 1.0, 0.2, Biomes.SNOWY_SLOPES, Biomes.ICE_SPIKES, Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS, Biomes.STONY_PEAKS).putBiome(0, 50, 4.0, 0.5, Biomes.DEEP_DARK));
        collector.add(L2Hostility.DIFFICULTY, new ResourceLocation("l2hostility", "nether"), new WorldDifficultyConfig().putDim(Level.NETHER, 0, 20, 9.0, 1.2));
        collector.add(L2Hostility.DIFFICULTY, new ResourceLocation("l2hostility", "end"), new WorldDifficultyConfig().putDim(Level.END, 0, 40, 16.0, 1.5));
        collector.add(L2Hostility.ENTITY, new ResourceLocation("l2hostility", "bosses"), new EntityConfig().putEntity(0, 20, 1.0, 0.0, List.of(EntityType.ELDER_GUARDIAN), List.of(EntityConfig.trait((MobTrait) LHTraits.REPELLING.get(), 1, 1, 300, 0.5F))).putEntity(0, 20, 1.0, 0.0, List.of(EntityType.PIGLIN_BRUTE), List.of(EntityConfig.trait((MobTrait) LHTraits.PULLING.get(), 1, 1, 300, 0.5F))).putEntity(0, 20, 1.0, 0.0, List.of(EntityType.WARDEN), List.of(EntityConfig.trait((MobTrait) LHTraits.DISPELL.get(), 1, 1, 200, 1.0F), EntityConfig.trait((MobTrait) LHTraits.REPRINT.get(), 1, 1, 300, 1.0F))).putEntity(0, 50, 1.0, 0.0, List.of(EntityType.WITHER), List.of(EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 0, 1))).putEntity(100, 50, 1.0, 0.0, List.of(EntityType.ENDER_DRAGON), List.of()));
        collector.add(L2Hostility.WEAPON, new ResourceLocation("l2hostility", "armors"), new WeaponConfig().putArmor(0, 200, Items.AIR).putArmor(20, 100, Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS).putArmor(35, 100, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS).putArmor(45, 100, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS).putArmor(60, 200, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS).putArmor(80, 300, Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS).putArmor(100, 100, Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS));
        collector.add(L2Hostility.WEAPON, new ResourceLocation("l2hostility", "vanilla"), new WeaponConfig().putMeleeWeapon(0, 200, Items.AIR).putMeleeWeapon(30, 100, Items.IRON_AXE, Items.IRON_SWORD).putMeleeWeapon(50, 100, Items.DIAMOND_AXE, Items.DIAMOND_SWORD).putMeleeWeapon(70, 100, Items.NETHERITE_AXE, Items.NETHERITE_SWORD).putRangedWeapon(0, 100, Items.AIR).putWeaponEnch(30, 0.5F, Enchantments.SHARPNESS, Enchantments.POWER_ARROWS).putWeaponEnch(40, 0.2F, Enchantments.KNOCKBACK, Enchantments.PUNCH_ARROWS).putWeaponEnch(50, 0.1F, Enchantments.FIRE_ASPECT, Enchantments.FLAMING_ARROWS).putArmorEnch(30, 0.5F, Enchantments.ALL_DAMAGE_PROTECTION).putArmorEnch(30, 0.2F, Enchantments.PROJECTILE_PROTECTION, Enchantments.BLAST_PROTECTION, Enchantments.FIRE_PROTECTION, Enchantments.FALL_PROTECTION).putArmorEnch(70, 0.3F, Enchantments.BINDING_CURSE));
        collector.add(L2Hostility.WEAPON, new ResourceLocation("l2complements", "l2complements"), new WeaponConfig().putWeaponEnch(100, 0.02F, (Enchantment) LCEnchantments.CURSE_BLADE.get(), (Enchantment) LCEnchantments.SHARP_BLADE.get(), (Enchantment) LCEnchantments.FLAME_BLADE.get(), (Enchantment) LCEnchantments.ICE_BLADE.get()).putWeaponEnch(200, 0.01F, (Enchantment) LCEnchantments.VOID_TOUCH.get()).putArmorEnch(70, 0.2F, (Enchantment) LCEnchantments.STABLE_BODY.get(), (Enchantment) LCEnchantments.SNOW_WALKER.get()).putArmorEnch(100, 0.02F, (Enchantment) LCEnchantments.ICE_THORN.get(), (Enchantment) LCEnchantments.FLAME_THORN.get(), (Enchantment) LCEnchantments.SAFEGUARD.get()));
        collector.add(L2Hostility.WEAPON, new ResourceLocation("l2weaponry", "weapons"), new WeaponConfig().putMeleeWeapon(200, 10, (Item) LWItems.STORM_JAVELIN.get(), (Item) LWItems.FLAME_AXE.get(), (Item) LWItems.FROZEN_SPEAR.get()).putMeleeWeapon(300, 5, (Item) LWItems.ABYSS_MACHETE.get(), (Item) LWItems.HOLY_AXE.get(), (Item) LWItems.BLACK_AXE.get()).putMeleeWeapon(400, 2, (Item) LWItems.CHEATER_CLAW.get(), (Item) LWItems.CHEATER_MACHETE.get()));
        collector.add(L2Hostility.WEAPON, new ResourceLocation("l2archery", "bows"), new WeaponConfig().putRangedWeapon(50, 10, (Item) ArcheryItems.STARTER_BOW.get()).putRangedWeapon(70, 5, (Item) ArcheryItems.IRON_BOW.get(), (Item) ArcheryItems.MASTER_BOW.get()).putRangedWeapon(100, 2, (Item) ArcheryItems.FLAME_BOW.get(), (Item) ArcheryItems.BLACKSTONE_BOW.get(), (Item) ArcheryItems.TURTLE_BOW.get(), (Item) ArcheryItems.EAGLE_BOW.get(), (Item) ArcheryItems.EXPLOSION_BOW.get(), (Item) ArcheryItems.FROZE_BOW.get()));
        if (ModList.get().isLoaded("twilightforest")) {
            TFData.genConfig(collector);
        }
        if (ModList.get().isLoaded("cataclysm")) {
            CataclysmData.genConfig(collector);
        }
        if (ModList.get().isLoaded("bosses_of_mass_destruction")) {
            BoMDData.genConfig(collector);
        }
        if (ModList.get().isLoaded("iceandfire")) {
            IaFData.genConfig(collector);
        }
        if (ModList.get().isLoaded("gateways")) {
            GatewayConfigGen.genConfig(collector);
        }
        if (ModList.get().isLoaded("mutantmonsters")) {
            MutantMonsterData.genConfig(collector);
        }
        if (ModList.get().isLoaded("mowziesmobs")) {
            MowzieData.genConfig(collector);
        }
    }

    public static <T extends LivingEntity> void addEntity(ConfigDataProvider.Collector collector, int min, int base, RegistryObject<EntityType<T>> obj, EntityConfig.TraitBase... traits) {
        collector.add(L2Hostility.ENTITY, obj.getId(), new EntityConfig().putEntity(min, base, 0.0, 0.0, List.of(obj.get()), List.of(traits)));
    }

    public static <T extends LivingEntity> void addEntity(ConfigDataProvider.Collector collector, int min, int base, RegistryObject<EntityType<T>> obj, List<EntityConfig.TraitBase> traits, List<EntityConfig.ItemPool> items) {
        collector.add(L2Hostility.ENTITY, obj.getId(), new EntityConfig().putEntityAndItem(min, base, 0.0, 0.0, List.of(obj.get()), traits, items));
    }
}