package net.minecraft.util.datafix.schemas;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.types.templates.Hook.HookFunction;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;
import org.slf4j.Logger;

public class V99 extends Schema {

    private static final Logger LOGGER = LogUtils.getLogger();

    static final Map<String, String> ITEM_TO_BLOCKENTITY = (Map<String, String>) DataFixUtils.make(Maps.newHashMap(), p_145919_ -> {
        p_145919_.put("minecraft:furnace", "Furnace");
        p_145919_.put("minecraft:lit_furnace", "Furnace");
        p_145919_.put("minecraft:chest", "Chest");
        p_145919_.put("minecraft:trapped_chest", "Chest");
        p_145919_.put("minecraft:ender_chest", "EnderChest");
        p_145919_.put("minecraft:jukebox", "RecordPlayer");
        p_145919_.put("minecraft:dispenser", "Trap");
        p_145919_.put("minecraft:dropper", "Dropper");
        p_145919_.put("minecraft:sign", "Sign");
        p_145919_.put("minecraft:mob_spawner", "MobSpawner");
        p_145919_.put("minecraft:noteblock", "Music");
        p_145919_.put("minecraft:brewing_stand", "Cauldron");
        p_145919_.put("minecraft:enhanting_table", "EnchantTable");
        p_145919_.put("minecraft:command_block", "CommandBlock");
        p_145919_.put("minecraft:beacon", "Beacon");
        p_145919_.put("minecraft:skull", "Skull");
        p_145919_.put("minecraft:daylight_detector", "DLDetector");
        p_145919_.put("minecraft:hopper", "Hopper");
        p_145919_.put("minecraft:banner", "Banner");
        p_145919_.put("minecraft:flower_pot", "FlowerPot");
        p_145919_.put("minecraft:repeating_command_block", "CommandBlock");
        p_145919_.put("minecraft:chain_command_block", "CommandBlock");
        p_145919_.put("minecraft:standing_sign", "Sign");
        p_145919_.put("minecraft:wall_sign", "Sign");
        p_145919_.put("minecraft:piston_head", "Piston");
        p_145919_.put("minecraft:daylight_detector_inverted", "DLDetector");
        p_145919_.put("minecraft:unpowered_comparator", "Comparator");
        p_145919_.put("minecraft:powered_comparator", "Comparator");
        p_145919_.put("minecraft:wall_banner", "Banner");
        p_145919_.put("minecraft:standing_banner", "Banner");
        p_145919_.put("minecraft:structure_block", "Structure");
        p_145919_.put("minecraft:end_portal", "Airportal");
        p_145919_.put("minecraft:end_gateway", "EndGateway");
        p_145919_.put("minecraft:shield", "Banner");
    });

    protected static final HookFunction ADD_NAMES = new HookFunction() {

        public <T> T apply(DynamicOps<T> p_18312_, T p_18313_) {
            return V99.addNames(new Dynamic(p_18312_, p_18313_), V99.ITEM_TO_BLOCKENTITY, "ArmorStand");
        }
    };

    public V99(int int0, Schema schema1) {
        super(int0, schema1);
    }

    protected static TypeTemplate equipment(Schema schema0) {
        return DSL.optionalFields("Equipment", DSL.list(References.ITEM_STACK.in(schema0)));
    }

    protected static void registerMob(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> equipment(schema0));
    }

    protected static void registerThrowableProjectile(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema0)));
    }

    protected static void registerMinecart(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0)));
    }

    protected static void registerInventory(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0))));
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = Maps.newHashMap();
        schema0.register($$1, "Item", p_18301_ -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema0)));
        schema0.registerSimple($$1, "XPOrb");
        registerThrowableProjectile(schema0, $$1, "ThrownEgg");
        schema0.registerSimple($$1, "LeashKnot");
        schema0.registerSimple($$1, "Painting");
        schema0.register($$1, "Arrow", p_18298_ -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema0)));
        schema0.register($$1, "TippedArrow", p_18295_ -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema0)));
        schema0.register($$1, "SpectralArrow", p_18292_ -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema0)));
        registerThrowableProjectile(schema0, $$1, "Snowball");
        registerThrowableProjectile(schema0, $$1, "Fireball");
        registerThrowableProjectile(schema0, $$1, "SmallFireball");
        registerThrowableProjectile(schema0, $$1, "ThrownEnderpearl");
        schema0.registerSimple($$1, "EyeOfEnderSignal");
        schema0.register($$1, "ThrownPotion", p_18289_ -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema0), "Potion", References.ITEM_STACK.in(schema0)));
        registerThrowableProjectile(schema0, $$1, "ThrownExpBottle");
        schema0.register($$1, "ItemFrame", p_18284_ -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema0)));
        registerThrowableProjectile(schema0, $$1, "WitherSkull");
        schema0.registerSimple($$1, "PrimedTnt");
        schema0.register($$1, "FallingSand", p_18279_ -> DSL.optionalFields("Block", References.BLOCK_NAME.in(schema0), "TileEntityData", References.BLOCK_ENTITY.in(schema0)));
        schema0.register($$1, "FireworksRocketEntity", p_18274_ -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema0)));
        schema0.registerSimple($$1, "Boat");
        schema0.register($$1, "Minecart", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0), "Items", DSL.list(References.ITEM_STACK.in(schema0))));
        registerMinecart(schema0, $$1, "MinecartRideable");
        schema0.register($$1, "MinecartChest", p_18269_ -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0), "Items", DSL.list(References.ITEM_STACK.in(schema0))));
        registerMinecart(schema0, $$1, "MinecartFurnace");
        registerMinecart(schema0, $$1, "MinecartTNT");
        schema0.register($$1, "MinecartSpawner", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0), References.UNTAGGED_SPAWNER.in(schema0)));
        schema0.register($$1, "MinecartHopper", p_18264_ -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0), "Items", DSL.list(References.ITEM_STACK.in(schema0))));
        registerMinecart(schema0, $$1, "MinecartCommandBlock");
        registerMob(schema0, $$1, "ArmorStand");
        registerMob(schema0, $$1, "Creeper");
        registerMob(schema0, $$1, "Skeleton");
        registerMob(schema0, $$1, "Spider");
        registerMob(schema0, $$1, "Giant");
        registerMob(schema0, $$1, "Zombie");
        registerMob(schema0, $$1, "Slime");
        registerMob(schema0, $$1, "Ghast");
        registerMob(schema0, $$1, "PigZombie");
        schema0.register($$1, "Enderman", p_18259_ -> DSL.optionalFields("carried", References.BLOCK_NAME.in(schema0), equipment(schema0)));
        registerMob(schema0, $$1, "CaveSpider");
        registerMob(schema0, $$1, "Silverfish");
        registerMob(schema0, $$1, "Blaze");
        registerMob(schema0, $$1, "LavaSlime");
        registerMob(schema0, $$1, "EnderDragon");
        registerMob(schema0, $$1, "WitherBoss");
        registerMob(schema0, $$1, "Bat");
        registerMob(schema0, $$1, "Witch");
        registerMob(schema0, $$1, "Endermite");
        registerMob(schema0, $$1, "Guardian");
        registerMob(schema0, $$1, "Pig");
        registerMob(schema0, $$1, "Sheep");
        registerMob(schema0, $$1, "Cow");
        registerMob(schema0, $$1, "Chicken");
        registerMob(schema0, $$1, "Squid");
        registerMob(schema0, $$1, "Wolf");
        registerMob(schema0, $$1, "MushroomCow");
        registerMob(schema0, $$1, "SnowMan");
        registerMob(schema0, $$1, "Ozelot");
        registerMob(schema0, $$1, "VillagerGolem");
        schema0.register($$1, "EntityHorse", p_18254_ -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "ArmorItem", References.ITEM_STACK.in(schema0), "SaddleItem", References.ITEM_STACK.in(schema0), equipment(schema0)));
        registerMob(schema0, $$1, "Rabbit");
        schema0.register($$1, "Villager", p_18245_ -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema0)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema0), "buyB", References.ITEM_STACK.in(schema0), "sell", References.ITEM_STACK.in(schema0)))), equipment(schema0)));
        schema0.registerSimple($$1, "EnderCrystal");
        schema0.registerSimple($$1, "AreaEffectCloud");
        schema0.registerSimple($$1, "ShulkerBullet");
        registerMob(schema0, $$1, "Shulker");
        return $$1;
    }

    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = Maps.newHashMap();
        registerInventory(schema0, $$1, "Furnace");
        registerInventory(schema0, $$1, "Chest");
        schema0.registerSimple($$1, "EnderChest");
        schema0.register($$1, "RecordPlayer", p_18235_ -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema0)));
        registerInventory(schema0, $$1, "Trap");
        registerInventory(schema0, $$1, "Dropper");
        schema0.registerSimple($$1, "Sign");
        schema0.register($$1, "MobSpawner", p_18223_ -> References.UNTAGGED_SPAWNER.in(schema0));
        schema0.registerSimple($$1, "Music");
        schema0.registerSimple($$1, "Piston");
        registerInventory(schema0, $$1, "Cauldron");
        schema0.registerSimple($$1, "EnchantTable");
        schema0.registerSimple($$1, "Airportal");
        schema0.registerSimple($$1, "Control");
        schema0.registerSimple($$1, "Beacon");
        schema0.registerSimple($$1, "Skull");
        schema0.registerSimple($$1, "DLDetector");
        registerInventory(schema0, $$1, "Hopper");
        schema0.registerSimple($$1, "Comparator");
        schema0.register($$1, "FlowerPot", p_18192_ -> DSL.optionalFields("Item", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema0))));
        schema0.registerSimple($$1, "Banner");
        schema0.registerSimple($$1, "Structure");
        schema0.registerSimple($$1, "EndGateway");
        return $$1;
    }

    public void registerTypes(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate2) {
        schema0.registerType(false, References.LEVEL, DSL::remainder);
        schema0.registerType(false, References.PLAYER, () -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema0)), "EnderItems", DSL.list(References.ITEM_STACK.in(schema0))));
        schema0.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema0)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema0), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema0))))));
        schema0.registerType(true, References.BLOCK_ENTITY, () -> DSL.taggedChoiceLazy("id", DSL.string(), mapStringSupplierTypeTemplate2));
        schema0.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Riding", References.ENTITY_TREE.in(schema0), References.ENTITY.in(schema0)));
        schema0.registerType(false, References.ENTITY_NAME, () -> DSL.constType(NamespacedSchema.namespacedString()));
        schema0.registerType(true, References.ENTITY, () -> DSL.taggedChoiceLazy("id", DSL.string(), mapStringSupplierTypeTemplate1));
        schema0.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema0)), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(schema0), "BlockEntityTag", References.BLOCK_ENTITY.in(schema0), "CanDestroy", DSL.list(References.BLOCK_NAME.in(schema0)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema0)), "Items", DSL.list(References.ITEM_STACK.in(schema0)))), ADD_NAMES, HookFunction.IDENTITY));
        schema0.registerType(false, References.OPTIONS, DSL::remainder);
        schema0.registerType(false, References.BLOCK_NAME, () -> DSL.or(DSL.constType(DSL.intType()), DSL.constType(NamespacedSchema.namespacedString())));
        schema0.registerType(false, References.ITEM_NAME, () -> DSL.constType(NamespacedSchema.namespacedString()));
        schema0.registerType(false, References.STATS, DSL::remainder);
        schema0.registerType(false, References.SAVED_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema0)), "Objectives", DSL.list(References.OBJECTIVE.in(schema0)), "Teams", DSL.list(References.TEAM.in(schema0)))));
        schema0.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
        schema0.registerType(false, References.OBJECTIVE, DSL::remainder);
        schema0.registerType(false, References.TEAM, DSL::remainder);
        schema0.registerType(true, References.UNTAGGED_SPAWNER, DSL::remainder);
        schema0.registerType(false, References.POI_CHUNK, DSL::remainder);
        schema0.registerType(false, References.WORLD_GEN_SETTINGS, DSL::remainder);
        schema0.registerType(false, References.ENTITY_CHUNK, () -> DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema0))));
    }

    protected static <T> T addNames(Dynamic<T> dynamicT0, Map<String, String> mapStringString1, String string2) {
        return (T) dynamicT0.update("tag", p_145917_ -> p_145917_.update("BlockEntityTag", p_145912_ -> {
            String $$3 = (String) dynamicT0.get("id").asString().result().map(NamespacedSchema::m_17311_).orElse("minecraft:air");
            if (!"minecraft:air".equals($$3)) {
                String $$4 = (String) mapStringString1.get($$3);
                if ($$4 != null) {
                    return p_145912_.set("id", dynamicT0.createString($$4));
                }
                LOGGER.warn("Unable to resolve BlockEntity for ItemStack: {}", $$3);
            }
            return p_145912_;
        }).update("EntityTag", p_145908_ -> {
            String $$3 = dynamicT0.get("id").asString("");
            return "minecraft:armor_stand".equals(NamespacedSchema.ensureNamespaced($$3)) ? p_145908_.set("id", dynamicT0.createString(string2)) : p_145908_;
        })).getValue();
    }
}