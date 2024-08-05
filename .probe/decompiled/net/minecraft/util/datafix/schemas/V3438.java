package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;

public class V3438 extends NamespacedSchema {

    public V3438(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerBlockEntities(schema0);
        $$1.put("minecraft:brushable_block", (Supplier) $$1.remove("minecraft:suspicious_sand"));
        schema0.registerSimple($$1, "minecraft:calibrated_sculk_sensor");
        return $$1;
    }
}