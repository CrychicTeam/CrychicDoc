package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V703 extends Schema {

    public V703(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerEntities(schema0);
        $$1.remove("EntityHorse");
        schema0.register($$1, "Horse", () -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema0), "SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        schema0.register($$1, "Donkey", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        schema0.register($$1, "Mule", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        schema0.register($$1, "ZombieHorse", () -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        schema0.register($$1, "SkeletonHorse", () -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema0), V100.equipment(schema0)));
        return $$1;
    }
}