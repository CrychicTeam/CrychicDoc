package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V3327 extends NamespacedSchema {

    public V3327(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerBlockEntities(schema0);
        schema0.register($$1, "minecraft:decorated_pot", () -> DSL.optionalFields("shards", DSL.list(References.ITEM_NAME.in(schema0))));
        schema0.register($$1, "minecraft:suspicious_sand", () -> DSL.optionalFields("item", References.ITEM_STACK.in(schema0)));
        return $$1;
    }
}