package dev.xkmc.l2hostility.init.loot;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2hostility.content.item.curio.misc.LootingCharm;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import java.util.Objects;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class TraitGLMProvider extends GlobalLootModifierProvider {

    public static final RegistryEntry<LootItemConditionType> TRAIT_AND_LEVEL = L2Hostility.REGISTRATE.simple("trait_and_level", Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(new TraitSerializer<>(TraitLootCondition.class)));

    public static final RegistryEntry<LootItemConditionType> MOB_LEVEL = L2Hostility.REGISTRATE.simple("mob_level", Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(new TraitSerializer<>(MobCapLootCondition.class)));

    public static final RegistryEntry<LootItemConditionType> HAS_ITEM = L2Hostility.REGISTRATE.simple("player_has_item", Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(new TraitSerializer<>(PlayerHasItemCondition.class)));

    public static final RegistryEntry<LootItemConditionType> MIN_HEALTH = L2Hostility.REGISTRATE.simple("min_health", Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(new TraitSerializer<>(MobHealthCondition.class)));

    public static final RegistryEntry<Codec<TraitLootModifier>> TRAIT_SCALED = L2Hostility.REGISTRATE.simple("trait_scaled", ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> TraitLootModifier.CODEC);

    public static final RegistryEntry<Codec<EnvyLootModifier>> LOOT_ENVY = L2Hostility.REGISTRATE.simple("loot_envy", ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> EnvyLootModifier.CODEC);

    public static final RegistryEntry<Codec<GluttonyLootModifier>> LOOT_GLUTTONY = L2Hostility.REGISTRATE.simple("loot_gluttony", ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> GluttonyLootModifier.CODEC);

    public static void register() {
    }

    public TraitGLMProvider(DataGenerator gen) {
        super(gen.getPackOutput(), "l2hostility");
    }

    @Override
    protected void start() {
        LootingCharm loot1 = (LootingCharm) LHItems.LOOT_1.get();
        LootingCharm loot2 = (LootingCharm) LHItems.LOOT_2.get();
        LootingCharm loot3 = (LootingCharm) LHItems.LOOT_3.get();
        LootingCharm loot4 = (LootingCharm) LHItems.LOOT_4.get();
        this.add("loot_envy", new EnvyLootModifier(LootTableTemplate.byPlayer().build(), new PlayerHasItemCondition((Item) LHItems.CURSE_ENVY.get())));
        this.add("loot_gluttony", new GluttonyLootModifier(LootTableTemplate.byPlayer().build(), new PlayerHasItemCondition((Item) LHItems.CURSE_GLUTTONY.get())));
        this.add((MobTrait) LHTraits.TANK.get(), loot1, new ItemStack(Items.DIAMOND, 4), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.TANK.get(), loot1, new ItemStack(Items.NETHERITE_SCRAP, 1), 3, 0.0, 0.1);
        this.add((MobTrait) LHTraits.SPEEDY.get(), loot1, new ItemStack(Items.RABBIT_FOOT, 2), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.SPEEDY.get(), loot3, new ItemStack(LCItems.CAPTURED_WIND, 1), 3, 0.0, 0.1, 100);
        this.add((MobTrait) LHTraits.PROTECTION.get(), loot1, new ItemStack(Items.SCUTE, 4), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.PROTECTION.get(), loot1, new ItemStack(Items.SHULKER_SHELL, 1), 3, 0.0, 0.1);
        this.add((MobTrait) LHTraits.INVISIBLE.get(), loot1, new ItemStack(Items.PHANTOM_MEMBRANE, 4), 1, 0.25, 0.0);
        this.add((MobTrait) LHTraits.FIERY.get(), loot1, new ItemStack(Items.BLAZE_ROD, 8), 1, 0.25, 0.0);
        this.add((MobTrait) LHTraits.REGEN.get(), loot1, new ItemStack(Items.GHAST_TEAR, 4), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.REGEN.get(), loot3, new ItemStack(LCMats.TOTEMIC_GOLD.getNugget(), 4), 3, 0.0, 0.1);
        this.add((MobTrait) LHTraits.REGEN.get(), loot2, new ItemStack((ItemLike) LCItems.LIFE_ESSENCE.get(), 1), 3, -0.2, 0.08);
        this.add((MobTrait) LHTraits.ADAPTIVE.get(), loot2, new ItemStack((ItemLike) LCItems.CURSED_DROPLET.get(), 1), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.REFLECT.get(), loot2, new ItemStack((ItemLike) LCItems.EXPLOSION_SHARD.get(), 1), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.DEMENTOR.get(), loot4, new ItemStack((ItemLike) LCItems.SUN_MEMBRANE.get(), 1), 1, 0.2, 0.1);
        this.add((MobTrait) LHTraits.DISPELL.get(), loot4, new ItemStack((ItemLike) LCItems.RESONANT_FEATHER.get(), 1), 1, 0.2, 0.1);
        this.add((MobTrait) LHTraits.UNDYING.get(), loot1, new ItemStack(Items.TOTEM_OF_UNDYING, 1), 1, 1.0, 0.0);
        this.add((MobTrait) LHTraits.UNDYING.get(), loot2, new ItemStack((ItemLike) LCItems.LIFE_ESSENCE.get(), 1), 1, 0.5, 0.0);
        this.add((MobTrait) LHTraits.ENDER.get(), loot4, new ItemStack((ItemLike) LCItems.VOID_EYE.get(), 1), 1, 0.2, 0.1);
        this.add((MobTrait) LHTraits.REPELLING.get(), loot3, new ItemStack((ItemLike) LCItems.FORCE_FIELD.get(), 1), 1, 0.2, 0.1);
        this.add((MobTrait) LHTraits.WEAKNESS.get(), loot1, new ItemStack(Items.FERMENTED_SPIDER_EYE, 8), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.SLOWNESS.get(), loot1, new ItemStack(Items.COBWEB, 4), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.POISON.get(), loot1, new ItemStack(Items.SPIDER_EYE, 8), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.WITHER.get(), loot1, new ItemStack(Items.WITHER_ROSE, 8), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.WITHER.get(), loot1, new ItemStack(Items.WITHER_SKELETON_SKULL, 1), 3, 0.0, 0.1);
        this.add((MobTrait) LHTraits.LEVITATION.get(), loot2, new ItemStack(LCItems.CAPTURED_BULLET, 1), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.BLIND.get(), loot1, new ItemStack(Items.INK_SAC, 8), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.CONFUSION.get(), loot1, new ItemStack(Items.PUFFERFISH, 4), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.SOUL_BURNER.get(), loot2, new ItemStack(LCItems.SOUL_FLAME, 2), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.FREEZING.get(), loot2, new ItemStack(LCItems.HARD_ICE, 2), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.CURSED.get(), loot1, PotionUtils.setPotion(Items.POTION.getDefaultInstance(), (Potion) Objects.requireNonNull(ForgeRegistries.POTIONS.getValue(new ResourceLocation("l2complements", "curse")))), 1, 0.0, 0.2);
        this.add((MobTrait) LHTraits.CURSED.get(), loot2, new ItemStack(LCItems.CURSED_DROPLET, 1), 3, 0.0, 0.05);
        this.add((MobTrait) LHTraits.CORROSION.get(), loot2, new ItemStack(LCItems.CURSED_DROPLET, 1), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.EROSION.get(), loot2, new ItemStack(LCItems.CURSED_DROPLET, 1), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.KILLER_AURA.get(), loot4, new ItemStack(LCItems.EMERALD, 1), 1, 0.0, 0.02);
        this.add((MobTrait) LHTraits.RAGNAROK.get(), loot4, new ItemStack(LCMats.ETERNIUM.getNugget(), 4), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.SHULKER.get(), loot2, new ItemStack(LCItems.CAPTURED_BULLET, 1), 1, 0.0, 0.2);
        this.add((MobTrait) LHTraits.GRENADE.get(), loot3, new ItemStack(LCItems.STORM_CORE, 1), 3, 0.0, 0.1);
        this.add((MobTrait) LHTraits.GRENADE.get(), loot1, new ItemStack(Items.GUNPOWDER, 4), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.GRENADE.get(), loot1, new ItemStack(Items.CREEPER_HEAD, 1), 5, 0.25, 0.0);
        this.add((MobTrait) LHTraits.DRAIN.get(), loot2, new ItemStack(LHItems.WITCH_DROPLET, 1), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.GROWTH.get(), loot1, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1), 1, 0.0, 0.02);
        this.add((MobTrait) LHTraits.SPLIT.get(), loot2, new ItemStack(LCItems.GUARDIAN_EYE, 1), 1, 0.0, 0.05);
        this.add((MobTrait) LHTraits.GRAVITY.get(), loot1, new ItemStack(Items.DRAGON_BREATH, 4), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.MOONWALK.get(), loot1, new ItemStack(Items.DRAGON_BREATH, 4), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.STRIKE.get(), loot2, new ItemStack(LCItems.EXPLOSION_SHARD, 1), 1, 0.0, 0.1);
        this.add((MobTrait) LHTraits.PULLING.get(), loot3, new ItemStack(LCItems.BLACKSTONE_CORE, 1), 1, 0.0, 0.05);
        this.add((MobTrait) LHTraits.REPRINT.get(), loot3, new ItemStack((ItemLike) LHItems.BOOK_COPY.get()), 1, 1.0, 0.0);
        this.add((MobTrait) LHTraits.DISPELL.get(), loot4, new ItemStack((ItemLike) LHItems.IMAGINE_BREAKER.get()), 3, 1.0, 0.0);
        this.add((MobTrait) LHTraits.ARENA.get(), loot4, new ItemStack((ItemLike) LHItems.CHAOS_INGOT.get()), 1, 0.5, 0.0);
        this.add((MobTrait) LHTraits.MASTER.get(), loot4, new ItemStack((ItemLike) LHItems.CHAOS_INGOT.get(), 4), 1, 1.0, 0.0);
        this.add((MobTrait) LHTraits.TANK.get(), new ItemStack(LCMats.SHULKERATE.getNugget(), 6), 0.0, 0.1, LootTableTemplate.byPlayer().build(), new TraitLootCondition((MobTrait) LHTraits.TANK.get(), 3, 5), new TraitLootCondition((MobTrait) LHTraits.PROTECTION.get(), 1, 3), new PlayerHasItemCondition(loot2));
        this.add((MobTrait) LHTraits.TANK.get(), new ItemStack(LCMats.SHULKERATE.getIngot(), 2), 0.0, 0.1, LootTableTemplate.byPlayer().build(), new TraitLootCondition((MobTrait) LHTraits.TANK.get(), 3, 5), new TraitLootCondition((MobTrait) LHTraits.PROTECTION.get(), 4, 5), new PlayerHasItemCondition(loot2));
        this.add((MobTrait) LHTraits.SPEEDY.get(), new ItemStack(LCMats.SCULKIUM.getNugget(), 4), 0.0, 0.1, LootTableTemplate.byPlayer().build(), new TraitLootCondition((MobTrait) LHTraits.SPEEDY.get(), 3, 5), new TraitLootCondition((MobTrait) LHTraits.TANK.get(), 3, 5), new PlayerHasItemCondition(loot3));
        this.add((MobTrait) LHTraits.DEMENTOR.get(), new ItemStack((ItemLike) LHItems.CHAOS_INGOT.get(), 1), 1.0, 0.0, LootTableTemplate.byPlayer().build(), new TraitLootCondition((MobTrait) LHTraits.KILLER_AURA.get(), 1, 5), new TraitLootCondition((MobTrait) LHTraits.RAGNAROK.get(), 1, 5), new PlayerHasItemCondition(loot4));
    }

    private void add(MobTrait trait, Item curio, ItemStack stack, int start, double chance, double bonus, int min) {
        this.add(trait, stack, chance, bonus, LootTableTemplate.byPlayer().build(), new TraitLootCondition(trait, start, 5), new MobCapLootCondition(min), new PlayerHasItemCondition(curio));
    }

    private void add(MobTrait trait, Item curio, ItemStack stack, int start, double chance, double bonus) {
        this.add(trait, stack, chance, bonus, LootTableTemplate.byPlayer().build(), new TraitLootCondition(trait, start, 5), new PlayerHasItemCondition(curio));
    }

    private void add(MobTrait trait, ItemStack stack, double chance, double bonus, LootItemCondition... conditions) {
        String name = trait.getRegistryName().getPath() + "_drop_" + ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
        this.add(name, new TraitLootModifier(trait, chance, bonus, stack, conditions));
    }
}