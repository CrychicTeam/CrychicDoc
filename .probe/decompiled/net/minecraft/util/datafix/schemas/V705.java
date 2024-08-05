package net.minecraft.util.datafix.schemas;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.types.templates.Hook.HookFunction;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V705 extends NamespacedSchema {

    protected static final HookFunction ADD_NAMES = new HookFunction() {

        public <T> T apply(DynamicOps<T> p_18167_, T p_18168_) {
            return V99.addNames(new Dynamic(p_18167_, p_18168_), V704.ITEM_TO_BLOCKENTITY, "minecraft:armor_stand");
        }
    };

    public V705(int int0, Schema schema1) {
        super(int0, schema1);
    }

    protected static void registerMob(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> V100.equipment(schema0));
    }

    protected static void registerThrowableProjectile(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema0)));
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = Maps.newHashMap();
        schema0.registerSimple($$1, "minecraft:area_effect_cloud");
        registerMob(schema0, $$1, "minecraft:armor_stand");
        schema0.register($$1, "minecraft:arrow", p_18164_ -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema0)));
        registerMob(schema0, $$1, "minecraft:bat");
        registerMob(schema0, $$1, "minecraft:blaze");
        schema0.registerSimple($$1, "minecraft:boat");
        registerMob(schema0, $$1, "minecraft:cave_spider");
        schema0.register($$1, "minecraft:chest_minecart", p_18161_ -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0), "Items", DSL.list(References.ITEM_STACK.in(schema0))));
        registerMob(schema0, $$1, "minecraft:chicken");
        schema0.register($$1, "minecraft:commandblock_minecart", p_18158_ -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0)));
        registerMob(schema0, $$1, "minecraft:cow");
        registerMob(schema0, $$1, "minecraft:creeper");
        schema0.register($$1, "minecraft:donkey", p_18155_ -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        schema0.registerSimple($$1, "minecraft:dragon_fireball");
        registerThrowableProjectile(schema0, $$1, "minecraft:egg");
        registerMob(schema0, $$1, "minecraft:elder_guardian");
        schema0.registerSimple($$1, "minecraft:ender_crystal");
        registerMob(schema0, $$1, "minecraft:ender_dragon");
        schema0.register($$1, "minecraft:enderman", p_18146_ -> DSL.optionalFields("carried", References.BLOCK_NAME.in(schema0), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:endermite");
        registerThrowableProjectile(schema0, $$1, "minecraft:ender_pearl");
        schema0.registerSimple($$1, "minecraft:eye_of_ender_signal");
        schema0.register($$1, "minecraft:falling_block", p_18143_ -> DSL.optionalFields("Block", References.BLOCK_NAME.in(schema0), "TileEntityData", References.BLOCK_ENTITY.in(schema0)));
        registerThrowableProjectile(schema0, $$1, "minecraft:fireball");
        schema0.register($$1, "minecraft:fireworks_rocket", p_18140_ -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema0)));
        schema0.register($$1, "minecraft:furnace_minecart", p_18137_ -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0)));
        registerMob(schema0, $$1, "minecraft:ghast");
        registerMob(schema0, $$1, "minecraft:giant");
        registerMob(schema0, $$1, "minecraft:guardian");
        schema0.register($$1, "minecraft:hopper_minecart", p_18134_ -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0), "Items", DSL.list(References.ITEM_STACK.in(schema0))));
        schema0.register($$1, "minecraft:horse", p_18131_ -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema0), "SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:husk");
        schema0.register($$1, "minecraft:item", p_18128_ -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema0)));
        schema0.register($$1, "minecraft:item_frame", p_18125_ -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema0)));
        schema0.registerSimple($$1, "minecraft:leash_knot");
        registerMob(schema0, $$1, "minecraft:magma_cube");
        schema0.register($$1, "minecraft:minecart", p_18122_ -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0)));
        registerMob(schema0, $$1, "minecraft:mooshroom");
        schema0.register($$1, "minecraft:mule", p_18119_ -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:ocelot");
        schema0.registerSimple($$1, "minecraft:painting");
        schema0.registerSimple($$1, "minecraft:parrot");
        registerMob(schema0, $$1, "minecraft:pig");
        registerMob(schema0, $$1, "minecraft:polar_bear");
        schema0.register($$1, "minecraft:potion", p_18116_ -> DSL.optionalFields("Potion", References.ITEM_STACK.in(schema0), "inTile", References.BLOCK_NAME.in(schema0)));
        registerMob(schema0, $$1, "minecraft:rabbit");
        registerMob(schema0, $$1, "minecraft:sheep");
        registerMob(schema0, $$1, "minecraft:shulker");
        schema0.registerSimple($$1, "minecraft:shulker_bullet");
        registerMob(schema0, $$1, "minecraft:silverfish");
        registerMob(schema0, $$1, "minecraft:skeleton");
        schema0.register($$1, "minecraft:skeleton_horse", p_18113_ -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:slime");
        registerThrowableProjectile(schema0, $$1, "minecraft:small_fireball");
        registerThrowableProjectile(schema0, $$1, "minecraft:snowball");
        registerMob(schema0, $$1, "minecraft:snowman");
        schema0.register($$1, "minecraft:spawner_minecart", p_18110_ -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0), References.UNTAGGED_SPAWNER.in(schema0)));
        schema0.register($$1, "minecraft:spectral_arrow", p_18107_ -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema0)));
        registerMob(schema0, $$1, "minecraft:spider");
        registerMob(schema0, $$1, "minecraft:squid");
        registerMob(schema0, $$1, "minecraft:stray");
        schema0.registerSimple($$1, "minecraft:tnt");
        schema0.register($$1, "minecraft:tnt_minecart", p_18104_ -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema0)));
        schema0.register($$1, "minecraft:villager", p_18101_ -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema0)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema0), "buyB", References.ITEM_STACK.in(schema0), "sell", References.ITEM_STACK.in(schema0)))), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:villager_golem");
        registerMob(schema0, $$1, "minecraft:witch");
        registerMob(schema0, $$1, "minecraft:wither");
        registerMob(schema0, $$1, "minecraft:wither_skeleton");
        registerThrowableProjectile(schema0, $$1, "minecraft:wither_skull");
        registerMob(schema0, $$1, "minecraft:wolf");
        registerThrowableProjectile(schema0, $$1, "minecraft:xp_bottle");
        schema0.registerSimple($$1, "minecraft:xp_orb");
        registerMob(schema0, $$1, "minecraft:zombie");
        schema0.register($$1, "minecraft:zombie_horse", p_18092_ -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        registerMob(schema0, $$1, "minecraft:zombie_pigman");
        registerMob(schema0, $$1, "minecraft:zombie_villager");
        schema0.registerSimple($$1, "minecraft:evocation_fangs");
        registerMob(schema0, $$1, "minecraft:evocation_illager");
        schema0.registerSimple($$1, "minecraft:illusion_illager");
        schema0.register($$1, "minecraft:llama", p_18081_ -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "SaddleItem", References.ITEM_STACK.in(schema0), "DecorItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        schema0.registerSimple($$1, "minecraft:llama_spit");
        registerMob(schema0, $$1, "minecraft:vex");
        registerMob(schema0, $$1, "minecraft:vindication_illager");
        return $$1;
    }

    public void registerTypes(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate2) {
        super.registerTypes(schema0, mapStringSupplierTypeTemplate1, mapStringSupplierTypeTemplate2);
        schema0.registerType(true, References.ENTITY, () -> DSL.taggedChoiceLazy("id", m_17310_(), mapStringSupplierTypeTemplate1));
        schema0.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(schema0), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(schema0), "BlockEntityTag", References.BLOCK_ENTITY.in(schema0), "CanDestroy", DSL.list(References.BLOCK_NAME.in(schema0)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema0)), "Items", DSL.list(References.ITEM_STACK.in(schema0)))), ADD_NAMES, HookFunction.IDENTITY));
    }
}