package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V1906 extends NamespacedSchema {

    public V1906(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerBlockEntities(schema0);
        registerInventory(schema0, $$1, "minecraft:barrel");
        registerInventory(schema0, $$1, "minecraft:smoker");
        registerInventory(schema0, $$1, "minecraft:blast_furnace");
        schema0.register($$1, "minecraft:lectern", p_17774_ -> DSL.optionalFields("Book", References.ITEM_STACK.in(schema0)));
        schema0.registerSimple($$1, "minecraft:bell");
        return $$1;
    }

    protected static void registerInventory(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0))));
    }
}