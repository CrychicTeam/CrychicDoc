package com.craisinlord.integrated_api.modinit;

import com.craisinlord.integrated_api.modinit.registry.RegistryEntry;
import com.craisinlord.integrated_api.modinit.registry.ResourcefulRegistries;
import com.craisinlord.integrated_api.modinit.registry.ResourcefulRegistry;
import com.craisinlord.integrated_api.world.structures.pieces.IASinglePoolElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;

public final class IAStructurePieces {

    public static final ResourcefulRegistry<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = ResourcefulRegistries.create(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, "integrated_api");

    public static final ResourcefulRegistry<StructurePieceType> STRUCTURE_PIECE = ResourcefulRegistries.create(BuiltInRegistries.STRUCTURE_PIECE, "integrated_api");

    public static final RegistryEntry<StructurePoolElementType<IASinglePoolElement>> IA_POOL_ELEMENT = STRUCTURE_POOL_ELEMENT.register("integrated_api_single_pool_element", () -> () -> IASinglePoolElement.CODEC);
}