package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V1928 extends NamespacedSchema {

    public V1928(int int0, Schema schema1) {
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
        $$1.remove("minecraft:illager_beast");
        registerMob(schema0, $$1, "minecraft:ravager");
        return $$1;
    }
}