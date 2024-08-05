package dev.xkmc.l2complements.init.registrate;

import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2complements.content.effect.force.ArmorReduceEffect;
import dev.xkmc.l2complements.content.effect.force.CurseEffect;
import dev.xkmc.l2complements.content.effect.force.FlameEffect;
import dev.xkmc.l2complements.content.effect.force.IceEffect;
import dev.xkmc.l2complements.content.effect.force.StoneCageEffect;
import dev.xkmc.l2complements.content.effect.skill.BleedEffect;
import dev.xkmc.l2complements.content.effect.skill.CleanseEffect;
import dev.xkmc.l2complements.content.effect.skill.EmeraldPopeEffect;
import dev.xkmc.l2complements.init.L2Complements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.ForgeRegistries;

public class LCEffects {

    public static final List<RegistryEntry<? extends Potion>> POTION_LIST = new ArrayList();

    public static final Map<String, String> NAME_CACHE = new HashMap();

    public static final RegistryEntry<EmeraldPopeEffect> EMERALD = genEffect("emerald_splash", () -> new EmeraldPopeEffect(MobEffectCategory.NEUTRAL, 65280), "Attack all surrounding enemies. Damage is based on currently player stats and weapons.");

    public static final RegistryEntry<FlameEffect> FLAME = genEffect("flame", "Soul Burning", () -> new FlameEffect(MobEffectCategory.HARMFUL, 16711680), "Continuously damage the entity. Bypass fire resistance, but fire-based mobs are immune to this.");

    public static final RegistryEntry<IceEffect> ICE = genEffect("frozen", "Frost", () -> new IceEffect(MobEffectCategory.HARMFUL, 8355839), "Slow down entity, and freeze them as if they are on powdered snow.");

    public static final RegistryEntry<ArmorReduceEffect> ARMOR_REDUCE = genEffect("armor_reduce", "Armor Corrosion", () -> new ArmorReduceEffect(MobEffectCategory.HARMFUL, 16777215), "Reduce armor value significantly.");

    public static final RegistryEntry<StoneCageEffect> STONE_CAGE = genEffect("stone_cage", "Incarceration", () -> new StoneCageEffect(MobEffectCategory.HARMFUL, 0), "Immobilize the entity. Making it cannot move and unaffected by external forces.");

    public static final RegistryEntry<CurseEffect> CURSE = genEffect("curse", "Cursed", () -> new CurseEffect(MobEffectCategory.HARMFUL, 4144959), "Make the entity cannot heal.");

    public static final RegistryEntry<BleedEffect> BLEED = genEffect("bleed", "Bleed", () -> new BleedEffect(MobEffectCategory.HARMFUL, 8323072), "Make the entity lose attack and speed, and damage the entity every 3 seconds. Stacks when applied.");

    public static final RegistryEntry<CleanseEffect> CLEANSE = genEffect("cleanse", "Cleansed", () -> new CleanseEffect(MobEffectCategory.NEUTRAL, 16777087), "Clear all potion effects and make the entity immune to potion effects.");

    private static final List<Runnable> TEMP = new ArrayList();

    private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
        return ((NoConfigBuilder) L2Complements.REGISTRATE.effect(name, sup, desc).lang(MobEffect::m_19481_)).register();
    }

    private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, String lang, NonNullSupplier<T> sup, String desc) {
        NAME_CACHE.put(name, lang);
        return ((NoConfigBuilder) L2Complements.REGISTRATE.effect(name, sup, desc).lang(MobEffect::m_19481_, lang)).register();
    }

    public static void registerBrewingRecipe() {
        TEMP.forEach(Runnable::run);
    }

    public static void register() {
        regPotion3("flame", FLAME::get, LCItems.SOUL_FLAME::get, 400, 600, 1000, 0, 1);
        regPotion2("frozen", ICE::get, LCItems.HARD_ICE::get, 3600, 9600);
        regPotion2("stone_cage", STONE_CAGE::get, LCItems.BLACKSTONE_CORE::get, 1200, 3600);
        regPotion2("curse", CURSE::get, LCItems.CURSED_DROPLET::get, 3600, 9600);
        regPotion2("cleanse", CLEANSE::get, LCItems.LIFE_ESSENCE::get, 3600, 9600);
        regPotion3("armor_reduce", ARMOR_REDUCE::get, 600, 1200, 3600, 0, 1, () -> Items.MAGMA_CREAM, Potions.WEAKNESS, Potions.LONG_WEAKNESS, null, () -> Items.FERMENTED_SPIDER_EYE, Potions.FIRE_RESISTANCE, Potions.LONG_FIRE_RESISTANCE, null);
        regPotion2("levitation", () -> MobEffects.LEVITATION, LCItems.CAPTURED_BULLET::get, 200, 600);
        regPotion3("resistance", () -> MobEffects.DAMAGE_RESISTANCE, LCItems.EXPLOSION_SHARD::get, 400, 600, 1200, 1, 2);
        regEmeraldPotion(EMERALD::get, LCItems.EMERALD::get);
    }

    private static <T extends Potion> RegistryEntry<T> genPotion(String name, NonNullSupplier<T> sup) {
        RegistryEntry<T> ans = ((NoConfigBuilder) L2Complements.REGISTRATE.entry(name, cb -> new NoConfigBuilder(L2Complements.REGISTRATE, L2Complements.REGISTRATE, name, cb, ForgeRegistries.Keys.POTIONS, sup))).register();
        POTION_LIST.add(ans);
        return ans;
    }

    private static void regPotion2(String id, Supplier<MobEffect> sup, Supplier<Item> item, int dur, int durLong) {
        RegistryEntry<Potion> potion = genPotion(id, () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), dur)));
        RegistryEntry<Potion> longPotion = genPotion("long_" + id, () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), durLong)));
        TEMP.add((Runnable) () -> {
            PotionBrewing.addMix(Potions.AWKWARD, (Item) item.get(), (Potion) potion.get());
            PotionBrewing.addMix((Potion) potion.get(), Items.REDSTONE, (Potion) longPotion.get());
        });
    }

    private static void regPotion3(String id, Supplier<MobEffect> sup, Supplier<Item> item, int durStrong, int dur, int durLong, int amp, int ampStrong) {
        RegistryEntry<Potion> potion = genPotion(id, () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), dur, amp)));
        RegistryEntry<Potion> longPotion = genPotion("long_" + id, () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), durLong, amp)));
        RegistryEntry<Potion> strongPotion = genPotion("strong_" + id, () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), durStrong, ampStrong)));
        TEMP.add((Runnable) () -> {
            PotionBrewing.addMix(Potions.AWKWARD, (Item) item.get(), (Potion) potion.get());
            PotionBrewing.addMix((Potion) potion.get(), Items.REDSTONE, (Potion) longPotion.get());
            PotionBrewing.addMix((Potion) potion.get(), Items.GLOWSTONE_DUST, (Potion) strongPotion.get());
        });
    }

    private static void regPotion3(String id, Supplier<MobEffect> sup, int durStrong, int dur, int durLong, int amp, int ampStrong, Supplier<Item> a, Potion ap, Potion lap, @Nullable Potion sap, Supplier<Item> b, Potion bp, Potion lbp, @Nullable Potion sbp) {
        RegistryEntry<Potion> potion = genPotion(id, () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), dur, amp)));
        RegistryEntry<Potion> longPotion = genPotion("long_" + id, () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), durLong, amp)));
        RegistryEntry<Potion> strongPotion = genPotion("strong_" + id, () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), durStrong, ampStrong)));
        TEMP.add((Runnable) () -> {
            PotionBrewing.addMix((Potion) potion.get(), Items.REDSTONE, (Potion) longPotion.get());
            PotionBrewing.addMix((Potion) potion.get(), Items.GLOWSTONE_DUST, (Potion) strongPotion.get());
            PotionBrewing.addMix(ap, (Item) a.get(), (Potion) potion.get());
            PotionBrewing.addMix(lap, (Item) a.get(), (Potion) longPotion.get());
            PotionBrewing.addMix(bp, (Item) b.get(), (Potion) potion.get());
            PotionBrewing.addMix(lbp, (Item) b.get(), (Potion) longPotion.get());
            if (sap != null) {
                PotionBrewing.addMix(sap, (Item) a.get(), (Potion) strongPotion.get());
            }
            if (sbp != null) {
                PotionBrewing.addMix(sbp, (Item) b.get(), (Potion) strongPotion.get());
            }
        });
    }

    private static void regEmeraldPotion(Supplier<MobEffect> sup, Supplier<Item> item) {
        RegistryEntry<Potion> potion = genPotion("emerald_splash", () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), 1200, 0)));
        RegistryEntry<Potion> longPotion = genPotion("long_emerald_splash", () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), 2400, 0)));
        RegistryEntry<Potion> strongPotion = genPotion("strong_emerald_splash", () -> new Potion(new MobEffectInstance((MobEffect) sup.get(), 1200, 1)));
        TEMP.add((Runnable) () -> {
            PotionBrewing.addMix(Potions.AWKWARD, (Item) item.get(), (Potion) potion.get());
            PotionBrewing.addMix((Potion) potion.get(), (Item) LCItems.FORCE_FIELD.get(), (Potion) longPotion.get());
            PotionBrewing.addMix((Potion) potion.get(), (Item) LCItems.RESONANT_FEATHER.get(), (Potion) strongPotion.get());
        });
    }
}