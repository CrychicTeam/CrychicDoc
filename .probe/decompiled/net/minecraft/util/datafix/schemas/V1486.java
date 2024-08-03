package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;

public class V1486 extends NamespacedSchema {

    public V1486(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerEntities(schema0);
        $$1.put("minecraft:cod", (Supplier) $$1.remove("minecraft:cod_mob"));
        $$1.put("minecraft:salmon", (Supplier) $$1.remove("minecraft:salmon_mob"));
        return $$1;
    }
}