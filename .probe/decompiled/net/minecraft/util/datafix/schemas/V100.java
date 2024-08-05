package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V100 extends Schema {

    public V100(int int0, Schema schema1) {
        super(int0, schema1);
    }

    protected static TypeTemplate equipment(Schema schema0) {
        return DSL.optionalFields("ArmorItems", DSL.list(References.ITEM_STACK.in(schema0)), "HandItems", DSL.list(References.ITEM_STACK.in(schema0)));
    }

    protected static void registerMob(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> equipment(schema0));
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerEntities(schema0);
        registerMob(schema0, $$1, "ArmorStand");
        registerMob(schema0, $$1, "Creeper");
        registerMob(schema0, $$1, "Skeleton");
        registerMob(schema0, $$1, "Spider");
        registerMob(schema0, $$1, "Giant");
        registerMob(schema0, $$1, "Zombie");
        registerMob(schema0, $$1, "Slime");
        registerMob(schema0, $$1, "Ghast");
        registerMob(schema0, $$1, "PigZombie");
        schema0.register($$1, "Enderman", p_17348_ -> DSL.optionalFields("carried", References.BLOCK_NAME.in(schema0), equipment(schema0)));
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
        schema0.register($$1, "EntityHorse", p_17343_ -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "ArmorItem", References.ITEM_STACK.in(schema0), "SaddleItem", References.ITEM_STACK.in(schema0), equipment(schema0)));
        registerMob(schema0, $$1, "Rabbit");
        schema0.register($$1, "Villager", p_17334_ -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema0)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema0), "buyB", References.ITEM_STACK.in(schema0), "sell", References.ITEM_STACK.in(schema0)))), equipment(schema0)));
        registerMob(schema0, $$1, "Shulker");
        schema0.registerSimple($$1, "AreaEffectCloud");
        schema0.registerSimple($$1, "ShulkerBullet");
        return $$1;
    }

    public void registerTypes(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate2) {
        super.registerTypes(schema0, mapStringSupplierTypeTemplate1, mapStringSupplierTypeTemplate2);
        schema0.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(schema0))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(schema0))), "palette", DSL.list(References.BLOCK_STATE.in(schema0))));
        schema0.registerType(false, References.BLOCK_STATE, DSL::remainder);
    }
}