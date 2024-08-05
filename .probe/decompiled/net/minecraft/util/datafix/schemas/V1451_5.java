package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;

public class V1451_5 extends NamespacedSchema {

    public V1451_5(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerBlockEntities(schema0);
        $$1.remove("minecraft:flower_pot");
        $$1.remove("minecraft:noteblock");
        return $$1;
    }
}