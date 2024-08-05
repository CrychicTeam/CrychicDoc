package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;

public class V1510 extends NamespacedSchema {

    public V1510(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerEntities(schema0);
        $$1.put("minecraft:command_block_minecart", (Supplier) $$1.remove("minecraft:commandblock_minecart"));
        $$1.put("minecraft:end_crystal", (Supplier) $$1.remove("minecraft:ender_crystal"));
        $$1.put("minecraft:snow_golem", (Supplier) $$1.remove("minecraft:snowman"));
        $$1.put("minecraft:evoker", (Supplier) $$1.remove("minecraft:evocation_illager"));
        $$1.put("minecraft:evoker_fangs", (Supplier) $$1.remove("minecraft:evocation_fangs"));
        $$1.put("minecraft:illusioner", (Supplier) $$1.remove("minecraft:illusion_illager"));
        $$1.put("minecraft:vindicator", (Supplier) $$1.remove("minecraft:vindication_illager"));
        $$1.put("minecraft:iron_golem", (Supplier) $$1.remove("minecraft:villager_golem"));
        $$1.put("minecraft:experience_orb", (Supplier) $$1.remove("minecraft:xp_orb"));
        $$1.put("minecraft:experience_bottle", (Supplier) $$1.remove("minecraft:xp_bottle"));
        $$1.put("minecraft:eye_of_ender", (Supplier) $$1.remove("minecraft:eye_of_ender_signal"));
        $$1.put("minecraft:firework_rocket", (Supplier) $$1.remove("minecraft:fireworks_rocket"));
        return $$1;
    }
}