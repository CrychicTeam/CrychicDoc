package dev.xkmc.l2weaponry.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2weaponry.compat.aerial.AHCompat;
import dev.xkmc.l2weaponry.compat.dragons.DragonCompat;
import dev.xkmc.l2weaponry.compat.twilightforest.TFCompat;
import dev.xkmc.l2weaponry.compat.undergarden.UGCompat;
import dev.xkmc.l2weaponry.content.item.legendary.AbyssAxe;
import dev.xkmc.l2weaponry.content.item.legendary.AbyssDagger;
import dev.xkmc.l2weaponry.content.item.legendary.AbyssHammer;
import dev.xkmc.l2weaponry.content.item.legendary.AbyssMachete;
import dev.xkmc.l2weaponry.content.item.legendary.BlackAxe;
import dev.xkmc.l2weaponry.content.item.legendary.BlackHammer;
import dev.xkmc.l2weaponry.content.item.legendary.BloodClaw;
import dev.xkmc.l2weaponry.content.item.legendary.CheaterClaw;
import dev.xkmc.l2weaponry.content.item.legendary.CheaterMachete;
import dev.xkmc.l2weaponry.content.item.legendary.EnderDagger;
import dev.xkmc.l2weaponry.content.item.legendary.EnderJavelin;
import dev.xkmc.l2weaponry.content.item.legendary.EnderMachete;
import dev.xkmc.l2weaponry.content.item.legendary.EnderSpear;
import dev.xkmc.l2weaponry.content.item.legendary.FlameAxe;
import dev.xkmc.l2weaponry.content.item.legendary.FrozenSpear;
import dev.xkmc.l2weaponry.content.item.legendary.HolyAxe;
import dev.xkmc.l2weaponry.content.item.legendary.HolyHammer;
import dev.xkmc.l2weaponry.content.item.legendary.StormJavelin;
import dev.xkmc.l2weaponry.init.L2Weaponry;
import dev.xkmc.l2weaponry.init.materials.LWGenItem;
import dev.xkmc.l2weaponry.init.materials.LWToolMats;
import dev.xkmc.l2weaponry.init.materials.LWToolTypes;
import dev.xkmc.l2weaponry.init.materials.LegendaryToolFactory;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

@MethodsReturnNonnullByDefault
public class LWItems {

    public static final List<Item> BLOCK_DECO = new ArrayList();

    public static final List<Item> THROW_DECO = new ArrayList();

    public static final List<Item> CLAW_DECO = new ArrayList();

    public static final List<Item> NUNCHAKU_DECO = new ArrayList();

    public static final RegistryEntry<CreativeModeTab> TAB = L2Weaponry.REGISTRATE.buildL2CreativeTab("weaponry", "L2 Weaponry", b -> b.icon(LWItems.GEN_ITEM[LWToolMats.SCULKIUM.ordinal()][LWToolTypes.PLATE_SHIELD.ordinal()]::asStack));

    public static final RegistryEntry<Attribute> SHIELD_DEFENSE = L2Weaponry.REGISTRATE.simple("shield_defense", ForgeRegistries.ATTRIBUTES.getRegistryKey(), () -> new RangedAttribute("attribute.name.shield_defense", 0.0, 0.0, 1000.0).m_22084_(true));

    public static final RegistryEntry<Attribute> REFLECT_TIME = L2Weaponry.REGISTRATE.simple("reflect_time", ForgeRegistries.ATTRIBUTES.getRegistryKey(), () -> new RangedAttribute("attribute.name.reflect_time", 0.0, 0.0, 1000.0).m_22084_(true));

    public static final ItemEntry<Item> HANDLE = L2Weaponry.REGISTRATE.item("reinforced_handle", Item::new).defaultModel().defaultLang().register();

    public static final ItemEntry<StormJavelin> STORM_JAVELIN = regLegendary("poseidon_madness", StormJavelin::new, LWToolTypes.JAVELIN, LWToolMats.POSEIDITE, Rarity.EPIC, false);

    public static final ItemEntry<FlameAxe> FLAME_AXE = regLegendary("axe_of_cursed_flame", FlameAxe::new, LWToolTypes.BATTLE_AXE, LWToolMats.NETHERITE, Rarity.UNCOMMON, true);

    public static final ItemEntry<FrozenSpear> FROZEN_SPEAR = regLegendary("spear_of_winter_storm", FrozenSpear::new, LWToolTypes.SPEAR, LWToolMats.IRON, Rarity.UNCOMMON, true);

    public static final ItemEntry<BlackHammer> BLACK_HAMMER = regLegendary("hammer_of_incarceration", BlackHammer::new, LWToolTypes.HAMMER, LWToolMats.NETHERITE, Rarity.UNCOMMON, true);

    public static final ItemEntry<EnderSpear> ENDER_SPEAR = regLegendary("haunting_demon_of_the_end", EnderSpear::new, LWToolTypes.SPEAR, LWToolMats.SHULKERATE, Rarity.EPIC, true);

    public static final ItemEntry<EnderJavelin> ENDER_JAVELIN = regLegendary("void_escape", EnderJavelin::new, LWToolTypes.JAVELIN, LWToolMats.SHULKERATE, Rarity.RARE, false);

    public static final ItemEntry<EnderDagger> ENDER_DAGGER = regLegendary("shadow_hunter", EnderDagger::new, LWToolTypes.DAGGER, LWToolMats.SHULKERATE, Rarity.EPIC, false);

    public static final ItemEntry<EnderMachete> ENDER_MACHETE = regLegendary("shadow_shredder", EnderMachete::new, LWToolTypes.MACHETE, LWToolMats.SHULKERATE, Rarity.RARE, false);

    public static final ItemEntry<AbyssDagger> ABYSS_DAGGER = regLegendary("abyss_shock", AbyssDagger::new, LWToolTypes.DAGGER, LWToolMats.SCULKIUM, Rarity.RARE, false);

    public static final ItemEntry<AbyssMachete> ABYSS_MACHETE = regLegendary("abyss_resonance", AbyssMachete::new, LWToolTypes.MACHETE, LWToolMats.SCULKIUM, Rarity.RARE, false);

    public static final ItemEntry<AbyssHammer> ABYSS_HAMMER = regLegendary("abyss_echo", AbyssHammer::new, LWToolTypes.HAMMER, LWToolMats.SCULKIUM, Rarity.RARE, true);

    public static final ItemEntry<AbyssAxe> ABYSS_AXE = regLegendary("abyss_terror", AbyssAxe::new, LWToolTypes.BATTLE_AXE, LWToolMats.SCULKIUM, Rarity.RARE, true);

    public static final ItemEntry<BlackAxe> BLACK_AXE = regLegendary("barbaric_hallow", BlackAxe::new, LWToolTypes.THROWING_AXE, LWToolMats.NETHERITE, Rarity.RARE, false);

    public static final ItemEntry<BloodClaw> BLOOD_CLAW = regLegendary("vampire_desire", BloodClaw::new, LWToolTypes.CLAW, LWToolMats.TOTEMIC_GOLD, Rarity.RARE, false);

    public static final ItemEntry<CheaterClaw> CHEATER_CLAW = regLegendary("claw_of_determination", CheaterClaw::new, LWToolTypes.CLAW, LWToolMats.ETERNIUM, Rarity.EPIC, false);

    public static final ItemEntry<CheaterMachete> CHEATER_MACHETE = regLegendary("blade_of_illusion", CheaterMachete::new, LWToolTypes.MACHETE, LWToolMats.ETERNIUM, Rarity.EPIC, false);

    public static final ItemEntry<HolyAxe> HOLY_AXE = regLegendary("dogmatic_standoff", HolyAxe::new, LWToolTypes.BATTLE_AXE, LWToolMats.TOTEMIC_GOLD, Rarity.RARE, true);

    public static final ItemEntry<HolyHammer> HOLY_HAMMER = regLegendary("dogmatic_punishment", HolyHammer::new, LWToolTypes.HAMMER, LWToolMats.TOTEMIC_GOLD, Rarity.RARE, true);

    public static final ItemEntry<Item>[][] GEN_ITEM = LWGenItem.generate(LWToolMats.values());

    private static <T extends Item> ItemEntry<T> regLegendary(String name, LegendaryToolFactory<T> fac, LWToolTypes type, LWToolMats mat, Rarity r, boolean is3D) {
        return L2Weaponry.REGISTRATE.item(name, p -> type.legendary(fac).parse(mat, p.rarity(r))).model((ctx, pvd) -> LWGenItem.model(type, mat, ctx, pvd, "legendary", name, is3D)).tag(new TagKey[] { type.tag }).defaultLang().register();
    }

    public static void register() {
    }

    static {
        if (ModList.get().isLoaded("twilightforest")) {
            TFCompat.register();
        }
        if (ModList.get().isLoaded("iceandfire")) {
            DragonCompat.register();
        }
        if (ModList.get().isLoaded("undergarden")) {
            UGCompat.register();
        }
        if (ModList.get().isLoaded("aerialhell")) {
            AHCompat.register();
        }
    }
}