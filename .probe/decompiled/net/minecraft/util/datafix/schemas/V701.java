package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;

public class V701 extends Schema {

    public V701(int int0, Schema schema1) {
        super(int0, schema1);
    }

    protected static void registerMob(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> V100.equipment(schema0));
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerEntities(schema0);
        registerMob(schema0, $$1, "WitherSkeleton");
        registerMob(schema0, $$1, "Stray");
        return $$1;
    }
}