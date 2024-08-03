package dev.xkmc.modulargolems.init.registrate;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2itemselector.init.data.L2ISTagGen;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.modulargolems.compat.materials.common.CompatManager;
import dev.xkmc.modulargolems.content.client.armor.GolemModelPaths;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemPartType;
import dev.xkmc.modulargolems.content.entity.humanoid.HumaniodGolemPartType;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemPartType;
import dev.xkmc.modulargolems.content.item.card.ConfigCard;
import dev.xkmc.modulargolems.content.item.card.DefaultFilterCard;
import dev.xkmc.modulargolems.content.item.card.EntityTypeFilterCard;
import dev.xkmc.modulargolems.content.item.card.NameFilterCard;
import dev.xkmc.modulargolems.content.item.card.PathRecordCard;
import dev.xkmc.modulargolems.content.item.card.UuidFilterCard;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemArmorItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemBeaconItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemWeaponItem;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.item.wand.CommandWandItem;
import dev.xkmc.modulargolems.content.item.wand.DispenseWand;
import dev.xkmc.modulargolems.content.item.wand.RetrievalWandItem;
import dev.xkmc.modulargolems.content.item.wand.RiderWandItem;
import dev.xkmc.modulargolems.content.item.wand.SquadWandItem;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.material.GolemWeaponType;
import dev.xkmc.modulargolems.init.material.VanillaGolemWeaponMaterial;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class GolemItems {

    public static final RegistryEntry<CreativeModeTab> TAB = ModularGolems.REGISTRATE.buildL2CreativeTab("golems", "Modular Golems", b -> b.icon(GolemItems.HOLDER_GOLEM::asStack));

    public static final ItemEntry<Item> GOLEM_TEMPLATE = ModularGolems.REGISTRATE.item("metal_golem_template", Item::new).defaultModel().defaultLang().register();

    public static final ItemEntry<Item> EMPTY_UPGRADE;

    public static final ItemEntry<GolemPart<MetalGolemEntity, MetalGolemPartType>> GOLEM_BODY;

    public static final ItemEntry<GolemPart<MetalGolemEntity, MetalGolemPartType>> GOLEM_ARM;

    public static final ItemEntry<GolemPart<MetalGolemEntity, MetalGolemPartType>> GOLEM_LEGS;

    public static final ItemEntry<GolemHolder<MetalGolemEntity, MetalGolemPartType>> HOLDER_GOLEM;

    public static final ItemEntry<GolemPart<HumanoidGolemEntity, HumaniodGolemPartType>> HUMANOID_BODY;

    public static final ItemEntry<GolemPart<HumanoidGolemEntity, HumaniodGolemPartType>> HUMANOID_ARMS;

    public static final ItemEntry<GolemPart<HumanoidGolemEntity, HumaniodGolemPartType>> HUMANOID_LEGS;

    public static final ItemEntry<GolemHolder<HumanoidGolemEntity, HumaniodGolemPartType>> HOLDER_HUMANOID;

    public static final ItemEntry<GolemPart<DogGolemEntity, DogGolemPartType>> DOG_BODY;

    public static final ItemEntry<GolemPart<DogGolemEntity, DogGolemPartType>> DOG_LEGS;

    public static final ItemEntry<GolemHolder<DogGolemEntity, DogGolemPartType>> HOLDER_DOG;

    public static final ItemEntry<SimpleUpgradeItem> FIRE_IMMUNE;

    public static final ItemEntry<SimpleUpgradeItem> THUNDER_IMMUNE;

    public static final ItemEntry<SimpleUpgradeItem> RECYCLE;

    public static final ItemEntry<SimpleUpgradeItem> DIAMOND;

    public static final ItemEntry<SimpleUpgradeItem> NETHERITE;

    public static final ItemEntry<SimpleUpgradeItem> QUARTZ;

    public static final ItemEntry<SimpleUpgradeItem> GOLD;

    public static final ItemEntry<SimpleUpgradeItem> ENCHANTED_GOLD;

    public static final ItemEntry<SimpleUpgradeItem> FLOAT;

    public static final ItemEntry<SimpleUpgradeItem> SPONGE;

    public static final ItemEntry<SimpleUpgradeItem> SWIM;

    public static final ItemEntry<SimpleUpgradeItem> PLAYER_IMMUNE;

    public static final ItemEntry<SimpleUpgradeItem> ENDER_SIGHT;

    public static final ItemEntry<SimpleUpgradeItem> BELL;

    public static final ItemEntry<SimpleUpgradeItem> SPEED;

    public static final ItemEntry<SimpleUpgradeItem> SLOW;

    public static final ItemEntry<SimpleUpgradeItem> WEAK;

    public static final ItemEntry<SimpleUpgradeItem> WITHER;

    public static final ItemEntry<SimpleUpgradeItem> EMERALD;

    public static final ItemEntry<SimpleUpgradeItem> PICKUP;

    public static final ItemEntry<SimpleUpgradeItem> PICKUP_MENDING;

    public static final ItemEntry<SimpleUpgradeItem> PICKUP_NO_DESTROY;

    public static final ItemEntry<SimpleUpgradeItem> TALENTED;

    public static final ItemEntry<SimpleUpgradeItem> CAULDRON;

    public static final ItemEntry<SimpleUpgradeItem> MOUNT_UPGRADE;

    public static final ItemEntry<SimpleUpgradeItem> SIZE_UPGRADE;

    public static final ItemEntry<RetrievalWandItem> RETRIEVAL_WAND = ModularGolems.REGISTRATE.item("retrieval_wand", p -> new RetrievalWandItem(p.stacksTo(1), null)).model((ctx, pvd) -> pvd.handheld(ctx)).defaultLang().tag(new TagKey[] { MGTagGen.GOLEM_INTERACT }).register();

    public static final ItemEntry<RetrievalWandItem> OMNI_RETRIVAL = ModularGolems.REGISTRATE.item("omnipotent_wand_retrieval", p -> new RetrievalWandItem(p.stacksTo(1), RETRIEVAL_WAND)).model((ctx, pvd) -> pvd.handheld(ctx, pvd.modLoc("item/omnipotent_wand"))).lang("Omnipotent Wand: Retrieval").tag(new TagKey[] { L2ISTagGen.SELECTABLE, MGTagGen.GOLEM_INTERACT }).removeTab(TAB.getKey()).register();

    public static final ItemEntry<CommandWandItem> COMMAND_WAND = ModularGolems.REGISTRATE.item("command_wand", p -> new CommandWandItem(p.stacksTo(1), null)).model((ctx, pvd) -> pvd.handheld(ctx)).defaultLang().tag(new TagKey[] { MGTagGen.GOLEM_INTERACT }).register();

    public static final ItemEntry<CommandWandItem> OMNI_COMMAND = ModularGolems.REGISTRATE.item("omnipotent_wand_command", p -> new CommandWandItem(p.stacksTo(1), COMMAND_WAND)).model((ctx, pvd) -> pvd.handheld(ctx, pvd.modLoc("item/omnipotent_wand"))).lang("Omnipotent Wand: Command").tag(new TagKey[] { L2ISTagGen.SELECTABLE, MGTagGen.GOLEM_INTERACT }).register();

    public static final ItemEntry<DispenseWand> DISPENSE_WAND = ModularGolems.REGISTRATE.item("summon_wand", p -> new DispenseWand(p.stacksTo(1), null)).model((ctx, pvd) -> pvd.handheld(ctx)).defaultLang().tag(new TagKey[] { MGTagGen.GOLEM_INTERACT }).register();

    public static final ItemEntry<DispenseWand> OMNI_DISPENSE = ModularGolems.REGISTRATE.item("omnipotent_wand_summon", p -> new DispenseWand(p.stacksTo(1), DISPENSE_WAND)).model((ctx, pvd) -> pvd.handheld(ctx, pvd.modLoc("item/omnipotent_wand"))).lang("Omnipotent Wand: Summon").tag(new TagKey[] { L2ISTagGen.SELECTABLE, MGTagGen.GOLEM_INTERACT }).removeTab(TAB.getKey()).register();

    public static final ItemEntry<RiderWandItem> RIDER_WAND = ModularGolems.REGISTRATE.item("rider_wand", p -> new RiderWandItem(p.stacksTo(1), null)).model((ctx, pvd) -> pvd.handheld(ctx)).defaultLang().tag(new TagKey[] { MGTagGen.GOLEM_INTERACT }).register();

    public static final ItemEntry<RiderWandItem> OMNI_RIDER = ModularGolems.REGISTRATE.item("omnipotent_wand_rider", p -> new RiderWandItem(p.stacksTo(1), RIDER_WAND)).model((ctx, pvd) -> pvd.handheld(ctx, pvd.modLoc("item/omnipotent_wand"))).lang("Omnipotent Wand: Rider").tag(new TagKey[] { L2ISTagGen.SELECTABLE, MGTagGen.GOLEM_INTERACT }).removeTab(TAB.getKey()).register();

    public static final ItemEntry<SquadWandItem> SQUAD_WAND = ModularGolems.REGISTRATE.item("squad_wand", p -> new SquadWandItem(p.stacksTo(1), null)).model((ctx, pvd) -> pvd.handheld(ctx)).defaultLang().tag(new TagKey[] { MGTagGen.GOLEM_INTERACT }).register();

    public static final ItemEntry<SquadWandItem> OMNI_SQUAD = ModularGolems.REGISTRATE.item("omnipotent_wand_squad", p -> new SquadWandItem(p.stacksTo(1), SQUAD_WAND)).model((ctx, pvd) -> pvd.handheld(ctx, pvd.modLoc("item/omnipotent_wand"))).lang("Omnipotent Wand: Squad").tag(new TagKey[] { L2ISTagGen.SELECTABLE, MGTagGen.GOLEM_INTERACT }).removeTab(TAB.getKey()).register();

    public static final ItemEntry<MetalGolemArmorItem> GOLEMGUARD_HELMET = ModularGolems.REGISTRATE.item("roman_guard_helmet", p -> new MetalGolemArmorItem(p.stacksTo(1), ArmorItem.Type.HELMET, 8, 4.0F, GolemModelPaths.HELMETS)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/equipments/" + ctx.getName()) })).defaultLang().register();

    public static final ItemEntry<MetalGolemArmorItem> WINDSPIRIT_HELMET = ModularGolems.REGISTRATE.item("wind_spirit_helmet", p -> new MetalGolemArmorItem(p.stacksTo(1), ArmorItem.Type.HELMET, 11, 6.0F, GolemModelPaths.HELMETS)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/equipments/" + ctx.getName()) })).defaultLang().register();

    public static final ItemEntry<MetalGolemArmorItem> BARBARICFLAMEVANGUARD_HELMET = ModularGolems.REGISTRATE.item("barbaric_vanguard_helmet", p -> new MetalGolemArmorItem(p.stacksTo(1).fireResistant(), ArmorItem.Type.HELMET, 14, 8.0F, GolemModelPaths.HELMETS)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/equipments/" + ctx.getName()) })).defaultLang().register();

    public static final ItemEntry<MetalGolemArmorItem> GOLEMGUARD_CHESTPLATE = ModularGolems.REGISTRATE.item("roman_guard_chestplate", p -> new MetalGolemArmorItem(p.stacksTo(1), ArmorItem.Type.CHESTPLATE, 10, 4.0F, GolemModelPaths.CHESTPLATES)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/equipments/" + ctx.getName()) })).defaultLang().register();

    public static final ItemEntry<MetalGolemArmorItem> WINDSPIRIT_CHESTPLATE = ModularGolems.REGISTRATE.item("wind_spirit_chestplate", p -> new MetalGolemArmorItem(p.stacksTo(1), ArmorItem.Type.CHESTPLATE, 14, 6.0F, GolemModelPaths.CHESTPLATES)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/equipments/" + ctx.getName()) })).defaultLang().register();

    public static final ItemEntry<MetalGolemArmorItem> BARBARICFLAMEVANGUARD_CHESTPLATE = ModularGolems.REGISTRATE.item("barbaric_vanguard_chestplate", p -> new MetalGolemArmorItem(p.stacksTo(1).fireResistant(), ArmorItem.Type.CHESTPLATE, 18, 8.0F, GolemModelPaths.CHESTPLATES)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/equipments/" + ctx.getName()) })).defaultLang().register();

    public static final ItemEntry<MetalGolemArmorItem> GOLEMGUARD_SHINGUARD = ModularGolems.REGISTRATE.item("roman_guard_shinguard", p -> new MetalGolemArmorItem(p.stacksTo(1), ArmorItem.Type.LEGGINGS, 6, 4.0F, GolemModelPaths.LEGGINGS)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/equipments/" + ctx.getName()) })).defaultLang().register();

    public static final ItemEntry<MetalGolemArmorItem> WINDSPIRIT_SHINGUARD = ModularGolems.REGISTRATE.item("wind_spirit_shinguard", p -> new MetalGolemArmorItem(p.stacksTo(1), ArmorItem.Type.LEGGINGS, 8, 6.0F, GolemModelPaths.LEGGINGS)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/equipments/" + ctx.getName()) })).defaultLang().register();

    public static final ItemEntry<MetalGolemArmorItem> BARBARICFLAMEVANGUARD_SHINGUARD = ModularGolems.REGISTRATE.item("barbaric_vanguard_shinguard", p -> new MetalGolemArmorItem(p.stacksTo(1).fireResistant(), ArmorItem.Type.LEGGINGS, 10, 8.0F, GolemModelPaths.LEGGINGS)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/equipments/" + ctx.getName()) })).defaultLang().register();

    public static final ItemEntry<MetalGolemWeaponItem>[][] METALGOLEM_WEAPON = GolemWeaponType.build(VanillaGolemWeaponMaterial.values());

    public static final ItemEntry<MetalGolemBeaconItem>[] METALGOLEM_BEACONS = new ItemEntry[5];

    public static final ItemEntry<ConfigCard>[] CARD;

    public static final ItemEntry<PathRecordCard> CARD_PATH;

    public static final ItemEntry<NameFilterCard> CARD_NAME;

    public static final ItemEntry<EntityTypeFilterCard> CARD_TYPE;

    public static final ItemEntry<UuidFilterCard> CARD_UUID;

    public static final ItemEntry<DefaultFilterCard> CARD_DEF;

    public static ItemBuilder<SimpleUpgradeItem, L2Registrate> regModUpgrade(String id, Supplier<RegistryEntry<? extends GolemModifier>> mod, int lv, boolean foil, String modid) {
        ItemBuilder<SimpleUpgradeItem, L2Registrate> reg = regUpgradeImpl(id, mod, lv, foil, modid);
        reg.setData(ProviderType.ITEM_TAGS, (a, b) -> b.addTag(MGTagGen.GOLEM_UPGRADES).m_176839_(reg.get().getId()));
        return reg;
    }

    public static ItemBuilder<SimpleUpgradeItem, L2Registrate> regModUpgrade(String id, Supplier<RegistryEntry<? extends GolemModifier>> mod, String modid) {
        return regModUpgrade(id, mod, 1, false, modid);
    }

    private static ItemBuilder<SimpleUpgradeItem, L2Registrate> regUpgrade(String id, Supplier<RegistryEntry<? extends GolemModifier>> mod) {
        return regUpgrade(id, mod, 1, false);
    }

    private static ItemBuilder<SimpleUpgradeItem, L2Registrate> regUpgrade(String id, Supplier<RegistryEntry<? extends GolemModifier>> mod, int level, boolean foil) {
        return regUpgradeImpl(id, mod, level, foil, "modulargolems").tag(new TagKey[] { MGTagGen.GOLEM_UPGRADES });
    }

    private static ItemBuilder<SimpleUpgradeItem, L2Registrate> regUpgradeImpl(String id, Supplier<RegistryEntry<? extends GolemModifier>> mod, int level, boolean foil, String modid) {
        return ModularGolems.REGISTRATE.item(id, p -> new SimpleUpgradeItem(p, ((RegistryEntry) mod.get())::get, level, foil)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { new ResourceLocation(modid, "item/upgrades/" + id) }).override().predicate(new ResourceLocation("modulargolems", "blue_arrow"), 0.5F).model(((ItemModelBuilder) pvd.getBuilder(pvd.name(ctx) + "_purple")).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation(modid, "item/upgrades/" + id)).texture("layer1", new ResourceLocation("modulargolems", "item/purple_arrow"))).end().override().predicate(new ResourceLocation("modulargolems", "blue_arrow"), 1.0F).model(((ItemModelBuilder) pvd.getBuilder(pvd.name(ctx) + "_blue")).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation(modid, "item/upgrades/" + id)).texture("layer1", new ResourceLocation("modulargolems", "item/blue_arrow"))).end());
    }

    public static void register() {
    }

    static {
        for (int i = 0; i < 5; i++) {
            int lv = i + 1;
            METALGOLEM_BEACONS[i] = ModularGolems.REGISTRATE.item("golem_beacon_level_" + lv, p -> new MetalGolemBeaconItem(p.stacksTo(1), lv)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/equipments/" + ctx.getName()) })).removeTab(TAB.getKey()).register();
        }
        CARD = new ItemEntry[16];
        for (int i = 0; i < 16; i++) {
            DyeColor color = DyeColor.byId(i);
            String name = color.getName();
            CARD[i] = ModularGolems.REGISTRATE.item(name + "_config_card", p -> new ConfigCard(p.stacksTo(1), color)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/card/" + name) })).tag(new TagKey[] { MGTagGen.CONFIG_CARD }).defaultLang().register();
        }
        CARD_NAME = ModularGolems.REGISTRATE.item("target_filter_name", p -> new NameFilterCard(p.stacksTo(1))).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/card/name") })).tag(new TagKey[] { MGTagGen.GOLEM_INTERACT }).lang("Target Filter: Datapack").register();
        CARD_TYPE = ModularGolems.REGISTRATE.item("target_filter_type", p -> new EntityTypeFilterCard(p.stacksTo(1))).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/card/type") })).tag(new TagKey[] { MGTagGen.GOLEM_INTERACT }).lang("Target Filter: Entity Type").register();
        CARD_UUID = ModularGolems.REGISTRATE.item("target_filter_uuid", p -> new UuidFilterCard(p.stacksTo(1))).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/card/uuid") })).tag(new TagKey[] { MGTagGen.GOLEM_INTERACT }).lang("Target Filter: Entity UUID").register();
        CARD_DEF = ModularGolems.REGISTRATE.item("target_filter_default", p -> new DefaultFilterCard(p.stacksTo(1))).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/card/default") })).tag(new TagKey[] { MGTagGen.GOLEM_INTERACT }).lang("Target Filter: Default Target").register();
        CARD_PATH = ModularGolems.REGISTRATE.item("patrol_path_recorder", p -> new PathRecordCard(p.stacksTo(1))).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/card/path") })).tag(new TagKey[] { MGTagGen.GOLEM_INTERACT, MGTagGen.CURIO_PATH }).lang("Patrol Path Recorder").register();
        EMPTY_UPGRADE = ModularGolems.REGISTRATE.item("empty_upgrade", Item::new).defaultModel().defaultLang().register();
        FIRE_IMMUNE = regUpgrade("fire_immune", () -> GolemModifiers.FIRE_IMMUNE).lang("Fire Immune Upgrade").register();
        THUNDER_IMMUNE = regUpgrade("thunder_immune", () -> GolemModifiers.THUNDER_IMMUNE).lang("Thunder Immune Upgrade").register();
        RECYCLE = regUpgrade("recycle", () -> GolemModifiers.RECYCLE).lang("Recycle Ugpgrade").register();
        DIAMOND = regUpgrade("diamond", () -> GolemModifiers.ARMOR).lang("Diamond Upgrade").register();
        NETHERITE = regUpgrade("netherite", () -> GolemModifiers.TOUGH).lang("Netherite Upgrade").register();
        QUARTZ = regUpgrade("quartz", () -> GolemModifiers.DAMAGE).lang("Quartz Upgrade").register();
        GOLD = regUpgrade("gold", () -> GolemModifiers.REGEN).lang("Golden Apple Upgrade").register();
        ENCHANTED_GOLD = regUpgrade("enchanted_gold", () -> GolemModifiers.REGEN, 2, true).lang("Enchanted Golden Apple Upgrade").register();
        FLOAT = regUpgrade("float", () -> GolemModifiers.FLOAT).lang("Float Upgrade").register();
        SPONGE = regUpgrade("sponge", () -> GolemModifiers.EXPLOSION_RES).lang("Sponge Upgrade").register();
        SWIM = regUpgrade("swim", () -> GolemModifiers.SWIM).lang("Swim Upgrade").register();
        PLAYER_IMMUNE = regUpgrade("player", () -> GolemModifiers.PLAYER_IMMUNE).lang("Player Immune Upgrade").register();
        ENDER_SIGHT = regUpgrade("ender_sight", () -> GolemModifiers.ENDER_SIGHT).lang("Ender Sight Upgrade").register();
        BELL = regUpgrade("bell", () -> GolemModifiers.BELL).lang("Bell Upgrade").register();
        SPEED = regUpgrade("speed", () -> GolemModifiers.SPEED).lang("Speed Upgrade").register();
        SLOW = regUpgrade("slow", () -> GolemModifiers.SLOW).lang("Potion Upgrade: Slowness").register();
        WEAK = regUpgrade("weak", () -> GolemModifiers.WEAK).lang("Potion Upgrade: Weakness").register();
        WITHER = regUpgrade("wither", () -> GolemModifiers.WITHER).lang("Potion Upgrade: Wither").register();
        EMERALD = regUpgrade("emerald", () -> GolemModifiers.EMERALD).lang("Emerald Upgrade").register();
        PICKUP = regUpgrade("pickup", () -> GolemModifiers.PICKUP).lang("Pickup Upgrade").register();
        PICKUP_MENDING = regUpgrade("pickup_mending", () -> GolemModifiers.PICKUP_MENDING).lang("Pickup Augment: Mending").register();
        PICKUP_NO_DESTROY = regUpgrade("pickup_no_destroy", () -> GolemModifiers.PICKUP_NODESTROY).lang("Pickup Augment: No Destroy").register();
        TALENTED = regUpgrade("talented", () -> GolemModifiers.TALENTED).lang("Meta Upgrade: Talented").register();
        CAULDRON = regUpgrade("cauldron", () -> GolemModifiers.CAULDRON).lang("Meta Upgrade: Cauldron").register();
        MOUNT_UPGRADE = regUpgrade("mount_upgrade", () -> GolemModifiers.MOUNT_UPGRADE).lang("Mount Upgrade").register();
        SIZE_UPGRADE = regUpgrade("size_upgrade", () -> GolemModifiers.SIZE_UPGRADE).lang("Size Upgrade").register();
        CompatManager.register();
        HOLDER_GOLEM = ((ItemBuilder) ModularGolems.REGISTRATE.item("metal_golem_holder", p -> new GolemHolder(p.fireResistant(), GolemTypes.TYPE_GOLEM)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemHolder) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_HOLDERS }).defaultLang().register();
        HOLDER_HUMANOID = ((ItemBuilder) ModularGolems.REGISTRATE.item("humanoid_golem_holder", p -> new GolemHolder(p.fireResistant(), GolemTypes.TYPE_HUMANOID)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemHolder) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_HOLDERS }).defaultLang().register();
        HOLDER_DOG = ((ItemBuilder) ModularGolems.REGISTRATE.item("dog_golem_holder", p -> new GolemHolder(p.fireResistant(), GolemTypes.TYPE_DOG)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemHolder) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_HOLDERS }).defaultLang().register();
        GOLEM_BODY = ((ItemBuilder) ModularGolems.REGISTRATE.item("metal_golem_body", p -> new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_GOLEM, MetalGolemPartType.BODY, 9)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemPart) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_PARTS }).defaultLang().register();
        GOLEM_ARM = ((ItemBuilder) ModularGolems.REGISTRATE.item("metal_golem_arm", p -> new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_GOLEM, MetalGolemPartType.LEFT, 9)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemPart) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_PARTS }).defaultLang().register();
        GOLEM_LEGS = ((ItemBuilder) ModularGolems.REGISTRATE.item("metal_golem_legs", p -> new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_GOLEM, MetalGolemPartType.LEG, 9)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemPart) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_PARTS }).defaultLang().register();
        HUMANOID_BODY = ((ItemBuilder) ModularGolems.REGISTRATE.item("humanoid_golem_body", p -> new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_HUMANOID, HumaniodGolemPartType.BODY, 6)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemPart) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_PARTS }).defaultLang().register();
        HUMANOID_ARMS = ((ItemBuilder) ModularGolems.REGISTRATE.item("humanoid_golem_arms", p -> new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_HUMANOID, HumaniodGolemPartType.ARMS, 6)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemPart) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_PARTS }).defaultLang().register();
        HUMANOID_LEGS = ((ItemBuilder) ModularGolems.REGISTRATE.item("humanoid_golem_legs", p -> new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_HUMANOID, HumaniodGolemPartType.LEGS, 6)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemPart) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_PARTS }).defaultLang().register();
        DOG_BODY = ((ItemBuilder) ModularGolems.REGISTRATE.item("dog_golem_body", p -> new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_DOG, DogGolemPartType.BODY, 6)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemPart) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_PARTS }).defaultLang().register();
        DOG_LEGS = ((ItemBuilder) ModularGolems.REGISTRATE.item("dog_golem_legs", p -> new GolemPart<>(p.fireResistant(), GolemTypes.TYPE_DOG, DogGolemPartType.LEGS, 3)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).transform(e -> e.tab(TAB.getKey(), x -> ((GolemPart) e.getEntry()).fillItemCategory(x)))).tag(new TagKey[] { MGTagGen.GOLEM_PARTS }).defaultLang().register();
        CompatManager.lateRegister();
    }
}