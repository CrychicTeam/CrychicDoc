package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V1022 extends Schema {

    public V1022(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public void registerTypes(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate2) {
        super.registerTypes(schema0, mapStringSupplierTypeTemplate1, mapStringSupplierTypeTemplate2);
        schema0.registerType(false, References.RECIPE, () -> DSL.constType(NamespacedSchema.namespacedString()));
        schema0.registerType(false, References.PLAYER, () -> DSL.optionalFields("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(schema0)), "Inventory", DSL.list(References.ITEM_STACK.in(schema0)), "EnderItems", DSL.list(References.ITEM_STACK.in(schema0)), DSL.optionalFields("ShoulderEntityLeft", References.ENTITY_TREE.in(schema0), "ShoulderEntityRight", References.ENTITY_TREE.in(schema0), "recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(schema0)), "toBeDisplayed", DSL.list(References.RECIPE.in(schema0))))));
        schema0.registerType(false, References.HOTBAR, () -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(schema0))));
    }
}