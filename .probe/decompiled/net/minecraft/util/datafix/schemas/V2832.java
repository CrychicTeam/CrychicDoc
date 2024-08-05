package net.minecraft.util.datafix.schemas;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;

public class V2832 extends NamespacedSchema {

    public V2832(int int0, Schema schema1) {
        super(int0, schema1);
    }

    public void registerTypes(Schema schema0, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate1, Map<String, Supplier<TypeTemplate>> mapStringSupplierTypeTemplate2) {
        super.registerTypes(schema0, mapStringSupplierTypeTemplate1, mapStringSupplierTypeTemplate2);
        schema0.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema0)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema0), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema0))), "Sections", DSL.list(DSL.optionalFields("biomes", DSL.optionalFields("palette", DSL.list(References.BIOME.in(schema0))), "block_states", DSL.optionalFields("palette", DSL.list(References.BLOCK_STATE.in(schema0))))), "Structures", DSL.optionalFields("Starts", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema0))))));
        schema0.registerType(false, References.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, () -> DSL.constType(m_17310_()));
        schema0.registerType(false, References.WORLD_GEN_SETTINGS, () -> DSL.fields("dimensions", DSL.compoundList(DSL.constType(m_17310_()), DSL.fields("generator", DSL.taggedChoiceLazy("type", DSL.string(), ImmutableMap.of("minecraft:debug", DSL::remainder, "minecraft:flat", (Supplier) () -> DSL.optionalFields("settings", DSL.optionalFields("biome", References.BIOME.in(schema0), "layers", DSL.list(DSL.optionalFields("block", References.BLOCK_NAME.in(schema0))))), "minecraft:noise", (Supplier) () -> DSL.optionalFields("biome_source", DSL.taggedChoiceLazy("type", DSL.string(), ImmutableMap.of("minecraft:fixed", (Supplier) () -> DSL.fields("biome", References.BIOME.in(schema0)), "minecraft:multi_noise", (Supplier) () -> DSL.or(DSL.fields("preset", References.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST.in(schema0)), DSL.list(DSL.fields("biome", References.BIOME.in(schema0)))), "minecraft:checkerboard", (Supplier) () -> DSL.fields("biomes", DSL.list(References.BIOME.in(schema0))), "minecraft:the_end", DSL::remainder)), "settings", DSL.or(DSL.constType(DSL.string()), DSL.optionalFields("default_block", References.BLOCK_NAME.in(schema0), "default_fluid", References.BLOCK_NAME.in(schema0))))))))));
    }
}