package net.minecraft.world.level.levelgen.structure.pieces;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public record StructurePieceSerializationContext(ResourceManager f_192762_, RegistryAccess f_192763_, StructureTemplateManager f_226956_) {

    private final ResourceManager resourceManager;

    private final RegistryAccess registryAccess;

    private final StructureTemplateManager structureTemplateManager;

    public StructurePieceSerializationContext(ResourceManager f_192762_, RegistryAccess f_192763_, StructureTemplateManager f_226956_) {
        this.resourceManager = f_192762_;
        this.registryAccess = f_192763_;
        this.structureTemplateManager = f_226956_;
    }

    public static StructurePieceSerializationContext fromLevel(ServerLevel p_192771_) {
        MinecraftServer $$1 = p_192771_.getServer();
        return new StructurePieceSerializationContext($$1.getResourceManager(), $$1.registryAccess(), $$1.getStructureManager());
    }
}