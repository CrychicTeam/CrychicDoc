package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V1929 extends NamespacedSchema {

    public V1929(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerEntities(schema0);
        schema0.register($$1, "minecraft:wandering_trader", p_17818_ -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema0)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema0), "buyB", References.ITEM_STACK.in(schema0), "sell", References.ITEM_STACK.in(schema0)))), V100.equipment(schema0)));
        schema0.register($$1, "minecraft:trader_llama", p_17815_ -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "SaddleItem", References.ITEM_STACK.in(schema0), "DecorItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        return $$1;
    }
}