package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V1470 extends NamespacedSchema {

    public V1470(int int0, Schema schema1) {
        super(int0, schema1);
    }

    protected static void registerMob(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> V100.equipment(schema0));
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerEntities(schema0);
        registerMob(schema0, $$1, "minecraft:turtle");
        registerMob(schema0, $$1, "minecraft:cod_mob");
        registerMob(schema0, $$1, "minecraft:tropical_fish");
        registerMob(schema0, $$1, "minecraft:salmon_mob");
        registerMob(schema0, $$1, "minecraft:puffer_fish");
        registerMob(schema0, $$1, "minecraft:phantom");
        registerMob(schema0, $$1, "minecraft:dolphin");
        registerMob(schema0, $$1, "minecraft:drowned");
        schema0.register($$1, "minecraft:trident", p_17704_ -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema0)));
        return $$1;
    }
}