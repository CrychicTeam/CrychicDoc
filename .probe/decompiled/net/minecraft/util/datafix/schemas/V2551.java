package net.minecraft.util.datafix.schemas;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V2551 extends NamespacedSchema {

    public V2551(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public void registerTypes(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate2) {
        super.registerTypes(schema0, mapStringSupplierTypeTemplate1, mapStringSupplierTypeTemplate2);
        schema0.registerType(false, References.WORLD_GEN_SETTINGS, () -> DSL.fields("dimensions", DSL.compoundList(DSL.constType(m_17310_()), DSL.fields("generator", DSL.taggedChoiceLazy("type", DSL.string(), ImmutableMap.of("minecraft:debug", DSL::remainder, "minecraft:flat", (Supplier) () -> DSL.optionalFields("settings", DSL.optionalFields("biome", References.BIOME.in(schema0), "layers", DSL.list(DSL.optionalFields("block", References.BLOCK_NAME.in(schema0))))), "minecraft:noise", (Supplier) () -> DSL.optionalFields("biome_source", DSL.taggedChoiceLazy("type", DSL.string(), ImmutableMap.of("minecraft:fixed", (Supplier) () -> DSL.fields("biome", References.BIOME.in(schema0)), "minecraft:multi_noise", (Supplier) () -> DSL.list(DSL.fields("biome", References.BIOME.in(schema0))), "minecraft:checkerboard", (Supplier) () -> DSL.fields("biomes", DSL.list(References.BIOME.in(schema0))), "minecraft:vanilla_layered", DSL::remainder, "minecraft:the_end", DSL::remainder)), "settings", DSL.or(DSL.constType(DSL.string()), DSL.optionalFields("default_block", References.BLOCK_NAME.in(schema0), "default_fluid", References.BLOCK_NAME.in(schema0))))))))));
    }
}