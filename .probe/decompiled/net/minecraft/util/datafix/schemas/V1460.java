package net.minecraft.util.datafix.schemas;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.types.templates.Hook.HookFunction;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V1460 extends NamespacedSchema {

    public V1460(int int0, Schema schema1) {
        super(int0, schema1);
    }

    protected static void registerMob(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> V100.equipment(schema0));
    }

    protected static void registerInventory(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0))));
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = Maps.newHashMap();
        schema0.registerSimple($$1, "minecraft:area_effect_cloud");
        registerMob(schema0, $$1, "minecraft:armor_stand");
        schema0.register($$1, "minecraft:arrow", p_17683_ -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema0)));
        registerMob(schema0, $$1, "minecraft:bat");
        registerMob(schema0, $$1, "minecraft:blaze");
        schema0.registerSimple($$1, "minecraft:boat");
        registerMob(schema0, $$1, "minecraft:cave_spider");
        schema0.register($$1, "minecraft:chest_minecart", p_17680_ -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0), "Items", DSL.list(References.ITEM_STACK.in(schema0))));
        registerMob(schema0, $$1, "minecraft:chicken");
        schema0.register($$1, "minecraft:commandblock_minecart", p_17677_ -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0)));
        registerMob(schema0, $$1, "minecraft:cow");
        registerMob(schema0, $$1, "minecraft:creeper");
        schema0.register($$1, "minecraft:donkey", p_17674_ -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        schema0.registerSimple($$1, "minecraft:dragon_fireball");
        schema0.registerSimple($$1, "minecraft:egg");
        registerMob(schema0, $$1, "minecraft:elder_guardian");
        schema0.registerSimple($$1, "minecraft:ender_crystal");
        registerMob(schema0, $$1, "minecraft:ender_dragon");
        schema0.register($$1, "minecraft:enderman", p_17671_ -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(schema0), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:endermite");
        schema0.registerSimple($$1, "minecraft:ender_pearl");
        schema0.registerSimple($$1, "minecraft:evocation_fangs");
        registerMob(schema0, $$1, "minecraft:evocation_illager");
        schema0.registerSimple($$1, "minecraft:eye_of_ender_signal");
        schema0.register($$1, "minecraft:falling_block", p_17668_ -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(schema0), "TileEntityData", References.BLOCK_ENTITY.in(schema0)));
        schema0.registerSimple($$1, "minecraft:fireball");
        schema0.register($$1, "minecraft:fireworks_rocket", p_17665_ -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema0)));
        schema0.register($$1, "minecraft:furnace_minecart", p_17654_ -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0)));
        registerMob(schema0, $$1, "minecraft:ghast");
        registerMob(schema0, $$1, "minecraft:giant");
        registerMob(schema0, $$1, "minecraft:guardian");
        schema0.register($$1, "minecraft:hopper_minecart", p_17651_ -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0), "Items", DSL.list(References.ITEM_STACK.in(schema0))));
        schema0.register($$1, "minecraft:horse", p_17648_ -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema0), "SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:husk");
        schema0.registerSimple($$1, "minecraft:illusion_illager");
        schema0.register($$1, "minecraft:item", p_17645_ -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema0)));
        schema0.register($$1, "minecraft:item_frame", p_17642_ -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema0)));
        schema0.registerSimple($$1, "minecraft:leash_knot");
        schema0.register($$1, "minecraft:llama", p_17639_ -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "SaddleItem", References.ITEM_STACK.in(schema0), "DecorItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        schema0.registerSimple($$1, "minecraft:llama_spit");
        registerMob(schema0, $$1, "minecraft:magma_cube");
        schema0.register($$1, "minecraft:minecart", p_17634_ -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0)));
        registerMob(schema0, $$1, "minecraft:mooshroom");
        schema0.register($$1, "minecraft:mule", p_17629_ -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:ocelot");
        schema0.registerSimple($$1, "minecraft:painting");
        schema0.registerSimple($$1, "minecraft:parrot");
        registerMob(schema0, $$1, "minecraft:pig");
        registerMob(schema0, $$1, "minecraft:polar_bear");
        schema0.register($$1, "minecraft:potion", p_17624_ -> DSL.optionalFields("Potion", References.ITEM_STACK.in(schema0)));
        registerMob(schema0, $$1, "minecraft:rabbit");
        registerMob(schema0, $$1, "minecraft:sheep");
        registerMob(schema0, $$1, "minecraft:shulker");
        schema0.registerSimple($$1, "minecraft:shulker_bullet");
        registerMob(schema0, $$1, "minecraft:silverfish");
        registerMob(schema0, $$1, "minecraft:skeleton");
        schema0.register($$1, "minecraft:skeleton_horse", p_17619_ -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:slime");
        schema0.registerSimple($$1, "minecraft:small_fireball");
        schema0.registerSimple($$1, "minecraft:snowball");
        registerMob(schema0, $$1, "minecraft:snowman");
        schema0.register($$1, "minecraft:spawner_minecart", p_17614_ -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0), References.UNTAGGED_SPAWNER.in(schema0)));
        schema0.register($$1, "minecraft:spectral_arrow", p_17609_ -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema0)));
        registerMob(schema0, $$1, "minecraft:spider");
        registerMob(schema0, $$1, "minecraft:squid");
        registerMob(schema0, $$1, "minecraft:stray");
        schema0.registerSimple($$1, "minecraft:tnt");
        schema0.register($$1, "minecraft:tnt_minecart", p_17604_ -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema0)));
        registerMob(schema0, $$1, "minecraft:vex");
        schema0.register($$1, "minecraft:villager", p_17598_ -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema0)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema0), "buyB", References.ITEM_STACK.in(schema0), "sell", References.ITEM_STACK.in(schema0)))), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:villager_golem");
        registerMob(schema0, $$1, "minecraft:vindication_illager");
        registerMob(schema0, $$1, "minecraft:witch");
        registerMob(schema0, $$1, "minecraft:wither");
        registerMob(schema0, $$1, "minecraft:wither_skeleton");
        schema0.registerSimple($$1, "minecraft:wither_skull");
        registerMob(schema0, $$1, "minecraft:wolf");
        schema0.registerSimple($$1, "minecraft:xp_bottle");
        schema0.registerSimple($$1, "minecraft:xp_orb");
        registerMob(schema0, $$1, "minecraft:zombie");
        schema0.register($$1, "minecraft:zombie_horse", p_17592_ -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:zombie_pigman");
        registerMob(schema0, $$1, "minecraft:zombie_villager");
        return $$1;
    }

    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = Maps.newHashMap();
        registerInventory(schema0, $$1, "minecraft:furnace");
        registerInventory(schema0, $$1, "minecraft:chest");
        registerInventory(schema0, $$1, "minecraft:trapped_chest");
        schema0.registerSimple($$1, "minecraft:ender_chest");
        schema0.register($$1, "minecraft:jukebox", p_17586_ -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema0)));
        registerInventory(schema0, $$1, "minecraft:dispenser");
        registerInventory(schema0, $$1, "minecraft:dropper");
        schema0.registerSimple($$1, "minecraft:sign");
        schema0.register($$1, "minecraft:mob_spawner", p_17574_ -> References.UNTAGGED_SPAWNER.in(schema0));
        schema0.register($$1, "minecraft:piston", p_17559_ -> DSL.optionalFields("blockState", References.BLOCK_STATE.in(schema0)));
        registerInventory(schema0, $$1, "minecraft:brewing_stand");
        schema0.registerSimple($$1, "minecraft:enchanting_table");
        schema0.registerSimple($$1, "minecraft:end_portal");
        schema0.registerSimple($$1, "minecraft:beacon");
        schema0.registerSimple($$1, "minecraft:skull");
        schema0.registerSimple($$1, "minecraft:daylight_detector");
        registerInventory(schema0, $$1, "minecraft:hopper");
        schema0.registerSimple($$1, "minecraft:comparator");
        schema0.registerSimple($$1, "minecraft:banner");
        schema0.registerSimple($$1, "minecraft:structure_block");
        schema0.registerSimple($$1, "minecraft:end_gateway");
        schema0.registerSimple($$1, "minecraft:command_block");
        registerInventory(schema0, $$1, "minecraft:shulker_box");
        schema0.registerSimple($$1, "minecraft:bed");
        return $$1;
    }

    public void registerTypes(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate2) {
        schema0.registerType(false, References.LEVEL, DSL::remainder);
        schema0.registerType(false, References.RECIPE, () -> DSL.constType(m_17310_()));
        schema0.registerType(false, References.PLAYER, () -> DSL.optionalFields("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(schema0)), "Inventory", DSL.list(References.ITEM_STACK.in(schema0)), "EnderItems", DSL.list(References.ITEM_STACK.in(schema0)), DSL.optionalFields("ShoulderEntityLeft", References.ENTITY_TREE.in(schema0), "ShoulderEntityRight", References.ENTITY_TREE.in(schema0), "recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(schema0)), "toBeDisplayed", DSL.list(References.RECIPE.in(schema0))))));
        schema0.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema0)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema0), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema0))), "Sections", DSL.list(DSL.optionalFields("Palette", DSL.list(References.BLOCK_STATE.in(schema0)))))));
        schema0.registerType(true, References.BLOCK_ENTITY, () -> DSL.taggedChoiceLazy("id", m_17310_(), mapStringSupplierTypeTemplate2));
        schema0.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Passengers", DSL.list(References.ENTITY_TREE.in(schema0)), References.ENTITY.in(schema0)));
        schema0.registerType(true, References.ENTITY, () -> DSL.taggedChoiceLazy("id", m_17310_(), mapStringSupplierTypeTemplate1));
        schema0.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(schema0), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(schema0), "BlockEntityTag", References.BLOCK_ENTITY.in(schema0), "CanDestroy", DSL.list(References.BLOCK_NAME.in(schema0)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema0)), "Items", DSL.list(References.ITEM_STACK.in(schema0)))), V705.ADD_NAMES, HookFunction.IDENTITY));
        schema0.registerType(false, References.HOTBAR, () -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(schema0))));
        schema0.registerType(false, References.OPTIONS, DSL::remainder);
        schema0.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(schema0))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(schema0))), "palette", DSL.list(References.BLOCK_STATE.in(schema0))));
        schema0.registerType(false, References.BLOCK_NAME, () -> DSL.constType(m_17310_()));
        schema0.registerType(false, References.ITEM_NAME, () -> DSL.constType(m_17310_()));
        schema0.registerType(false, References.BLOCK_STATE, DSL::remainder);
        Supplier<TypeTemplate> $$3 = () -> DSL.compoundList(References.ITEM_NAME.in(schema0), DSL.constType(DSL.intType()));
        schema0.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(schema0), DSL.constType(DSL.intType())), "minecraft:crafted", (TypeTemplate) $$3.get(), "minecraft:used", (TypeTemplate) $$3.get(), "minecraft:broken", (TypeTemplate) $$3.get(), "minecraft:picked_up", (TypeTemplate) $$3.get(), DSL.optionalFields("minecraft:dropped", (TypeTemplate) $$3.get(), "minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(schema0), DSL.constType(DSL.intType())), "minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(schema0), DSL.constType(DSL.intType())), "minecraft:custom", DSL.compoundList(DSL.constType(m_17310_()), DSL.constType(DSL.intType()))))));
        schema0.registerType(false, References.SAVED_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema0)), "Objectives", DSL.list(References.OBJECTIVE.in(schema0)), "Teams", DSL.list(References.TEAM.in(schema0)))));
        schema0.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
        Map<String, Supplier<TypeTemplate>> $$4 = V1451_6.createCriterionTypes(schema0);
        schema0.registerType(false, References.OBJECTIVE, () -> DSL.hook(DSL.optionalFields("CriteriaType", DSL.taggedChoiceLazy("type", DSL.string(), $$4)), V1451_6.UNPACK_OBJECTIVE_ID, V1451_6.REPACK_OBJECTIVE_ID));
        schema0.registerType(false, References.TEAM, DSL::remainder);
        schema0.registerType(true, References.UNTAGGED_SPAWNER, () -> DSL.optionalFields("SpawnPotentials", DSL.list(DSL.fields("Entity", References.ENTITY_TREE.in(schema0))), "SpawnData", References.ENTITY_TREE.in(schema0)));
        schema0.registerType(false, References.ADVANCEMENTS, () -> DSL.optionalFields("minecraft:adventure/adventuring_time", DSL.optionalFields("criteria", DSL.compoundList(References.BIOME.in(schema0), DSL.constType(DSL.string()))), "minecraft:adventure/kill_a_mob", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema0), DSL.constType(DSL.string()))), "minecraft:adventure/kill_all_mobs", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema0), DSL.constType(DSL.string()))), "minecraft:husbandry/bred_all_animals", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema0), DSL.constType(DSL.string())))));
        schema0.registerType(false, References.BIOME, () -> DSL.constType(m_17310_()));
        schema0.registerType(false, References.ENTITY_NAME, () -> DSL.constType(m_17310_()));
        schema0.registerType(false, References.POI_CHUNK, DSL::remainder);
        schema0.registerType(false, References.WORLD_GEN_SETTINGS, DSL::remainder);
        schema0.registerType(false, References.ENTITY_CHUNK, () -> DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema0))));
    }
}