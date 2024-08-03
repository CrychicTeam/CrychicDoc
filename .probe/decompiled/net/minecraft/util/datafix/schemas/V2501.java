package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V2501 extends NamespacedSchema {

    public V2501(int int0, Schema schema1) {
        super(int0, schema1);
    }

    private static void registerFurnace(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, String string2) {
        schema0.register(mapStringSupplierTypeTemplate1, string2, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema0)), "RecipesUsed", DSL.compoundList(References.RECIPE.in(schema0), DSL.constType(DSL.intType()))));
    }

    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema0) {
        Map<String, Supplier<TypeTemplate>> $$1 = super.registerBlockEntities(schema0);
        registerFurnace(schema0, $$1, "minecraft:furnace");
        registerFurnace(schema0, $$1, "minecraft:smoker");
        registerFurnace(schema0, $$1, "minecraft:blast_furnace");
        return $$1;
    }
}