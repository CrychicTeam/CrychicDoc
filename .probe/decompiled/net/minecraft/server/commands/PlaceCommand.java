package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Optional;
import net.minecraft.ResourceLocationException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.TemplateMirrorArgument;
import net.minecraft.commands.arguments.TemplateRotationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class PlaceCommand {

    private static final SimpleCommandExceptionType ERROR_FEATURE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.place.feature.failed"));

    private static final SimpleCommandExceptionType ERROR_JIGSAW_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.place.jigsaw.failed"));

    private static final SimpleCommandExceptionType ERROR_STRUCTURE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.place.structure.failed"));

    private static final DynamicCommandExceptionType ERROR_TEMPLATE_INVALID = new DynamicCommandExceptionType(p_214582_ -> Component.translatable("commands.place.template.invalid", p_214582_));

    private static final SimpleCommandExceptionType ERROR_TEMPLATE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.place.template.failed"));

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_TEMPLATES = (p_214552_, p_214553_) -> {
        StructureTemplateManager $$2 = ((CommandSourceStack) p_214552_.getSource()).getLevel().getStructureManager();
        return SharedSuggestionProvider.suggestResource($$2.listTemplates(), p_214553_);
    };

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("place").requires(p_214560_ -> p_214560_.hasPermission(2))).then(Commands.literal("feature").then(((RequiredArgumentBuilder) Commands.argument("feature", ResourceKeyArgument.key(Registries.CONFIGURED_FEATURE)).executes(p_274824_ -> placeFeature((CommandSourceStack) p_274824_.getSource(), ResourceKeyArgument.getConfiguredFeature(p_274824_, "feature"), BlockPos.containing(((CommandSourceStack) p_274824_.getSource()).getPosition())))).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(p_248163_ -> placeFeature((CommandSourceStack) p_248163_.getSource(), ResourceKeyArgument.getConfiguredFeature(p_248163_, "feature"), BlockPosArgument.getLoadedBlockPos(p_248163_, "pos"))))))).then(Commands.literal("jigsaw").then(Commands.argument("pool", ResourceKeyArgument.key(Registries.TEMPLATE_POOL)).then(Commands.argument("target", ResourceLocationArgument.id()).then(((RequiredArgumentBuilder) Commands.argument("max_depth", IntegerArgumentType.integer(1, 7)).executes(p_274825_ -> placeJigsaw((CommandSourceStack) p_274825_.getSource(), ResourceKeyArgument.getStructureTemplatePool(p_274825_, "pool"), ResourceLocationArgument.getId(p_274825_, "target"), IntegerArgumentType.getInteger(p_274825_, "max_depth"), BlockPos.containing(((CommandSourceStack) p_274825_.getSource()).getPosition())))).then(Commands.argument("position", BlockPosArgument.blockPos()).executes(p_248167_ -> placeJigsaw((CommandSourceStack) p_248167_.getSource(), ResourceKeyArgument.getStructureTemplatePool(p_248167_, "pool"), ResourceLocationArgument.getId(p_248167_, "target"), IntegerArgumentType.getInteger(p_248167_, "max_depth"), BlockPosArgument.getLoadedBlockPos(p_248167_, "position"))))))))).then(Commands.literal("structure").then(((RequiredArgumentBuilder) Commands.argument("structure", ResourceKeyArgument.key(Registries.STRUCTURE)).executes(p_274826_ -> placeStructure((CommandSourceStack) p_274826_.getSource(), ResourceKeyArgument.getStructure(p_274826_, "structure"), BlockPos.containing(((CommandSourceStack) p_274826_.getSource()).getPosition())))).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(p_248168_ -> placeStructure((CommandSourceStack) p_248168_.getSource(), ResourceKeyArgument.getStructure(p_248168_, "structure"), BlockPosArgument.getLoadedBlockPos(p_248168_, "pos"))))))).then(Commands.literal("template").then(((RequiredArgumentBuilder) Commands.argument("template", ResourceLocationArgument.id()).suggests(SUGGEST_TEMPLATES).executes(p_274827_ -> placeTemplate((CommandSourceStack) p_274827_.getSource(), ResourceLocationArgument.getId(p_274827_, "template"), BlockPos.containing(((CommandSourceStack) p_274827_.getSource()).getPosition()), Rotation.NONE, Mirror.NONE, 1.0F, 0))).then(((RequiredArgumentBuilder) Commands.argument("pos", BlockPosArgument.blockPos()).executes(p_214596_ -> placeTemplate((CommandSourceStack) p_214596_.getSource(), ResourceLocationArgument.getId(p_214596_, "template"), BlockPosArgument.getLoadedBlockPos(p_214596_, "pos"), Rotation.NONE, Mirror.NONE, 1.0F, 0))).then(((RequiredArgumentBuilder) Commands.argument("rotation", TemplateRotationArgument.templateRotation()).executes(p_214594_ -> placeTemplate((CommandSourceStack) p_214594_.getSource(), ResourceLocationArgument.getId(p_214594_, "template"), BlockPosArgument.getLoadedBlockPos(p_214594_, "pos"), TemplateRotationArgument.getRotation(p_214594_, "rotation"), Mirror.NONE, 1.0F, 0))).then(((RequiredArgumentBuilder) Commands.argument("mirror", TemplateMirrorArgument.templateMirror()).executes(p_214592_ -> placeTemplate((CommandSourceStack) p_214592_.getSource(), ResourceLocationArgument.getId(p_214592_, "template"), BlockPosArgument.getLoadedBlockPos(p_214592_, "pos"), TemplateRotationArgument.getRotation(p_214592_, "rotation"), TemplateMirrorArgument.getMirror(p_214592_, "mirror"), 1.0F, 0))).then(((RequiredArgumentBuilder) Commands.argument("integrity", FloatArgumentType.floatArg(0.0F, 1.0F)).executes(p_214586_ -> placeTemplate((CommandSourceStack) p_214586_.getSource(), ResourceLocationArgument.getId(p_214586_, "template"), BlockPosArgument.getLoadedBlockPos(p_214586_, "pos"), TemplateRotationArgument.getRotation(p_214586_, "rotation"), TemplateMirrorArgument.getMirror(p_214586_, "mirror"), FloatArgumentType.getFloat(p_214586_, "integrity"), 0))).then(Commands.argument("seed", IntegerArgumentType.integer()).executes(p_214550_ -> placeTemplate((CommandSourceStack) p_214550_.getSource(), ResourceLocationArgument.getId(p_214550_, "template"), BlockPosArgument.getLoadedBlockPos(p_214550_, "pos"), TemplateRotationArgument.getRotation(p_214550_, "rotation"), TemplateMirrorArgument.getMirror(p_214550_, "mirror"), FloatArgumentType.getFloat(p_214550_, "integrity"), IntegerArgumentType.getInteger(p_214550_, "seed")))))))))));
    }

    public static int placeFeature(CommandSourceStack commandSourceStack0, Holder.Reference<ConfiguredFeature<?, ?>> holderReferenceConfiguredFeature1, BlockPos blockPos2) throws CommandSyntaxException {
        ServerLevel $$3 = commandSourceStack0.getLevel();
        ConfiguredFeature<?, ?> $$4 = holderReferenceConfiguredFeature1.value();
        ChunkPos $$5 = new ChunkPos(blockPos2);
        checkLoaded($$3, new ChunkPos($$5.x - 1, $$5.z - 1), new ChunkPos($$5.x + 1, $$5.z + 1));
        if (!$$4.place($$3, $$3.getChunkSource().getGenerator(), $$3.m_213780_(), blockPos2)) {
            throw ERROR_FEATURE_FAILED.create();
        } else {
            String $$6 = holderReferenceConfiguredFeature1.key().location().toString();
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.place.feature.success", $$6, blockPos2.m_123341_(), blockPos2.m_123342_(), blockPos2.m_123343_()), true);
            return 1;
        }
    }

    public static int placeJigsaw(CommandSourceStack commandSourceStack0, Holder<StructureTemplatePool> holderStructureTemplatePool1, ResourceLocation resourceLocation2, int int3, BlockPos blockPos4) throws CommandSyntaxException {
        ServerLevel $$5 = commandSourceStack0.getLevel();
        if (!JigsawPlacement.generateJigsaw($$5, holderStructureTemplatePool1, resourceLocation2, int3, blockPos4, false)) {
            throw ERROR_JIGSAW_FAILED.create();
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.place.jigsaw.success", blockPos4.m_123341_(), blockPos4.m_123342_(), blockPos4.m_123343_()), true);
            return 1;
        }
    }

    public static int placeStructure(CommandSourceStack commandSourceStack0, Holder.Reference<Structure> holderReferenceStructure1, BlockPos blockPos2) throws CommandSyntaxException {
        ServerLevel $$3 = commandSourceStack0.getLevel();
        Structure $$4 = holderReferenceStructure1.value();
        ChunkGenerator $$5 = $$3.getChunkSource().getGenerator();
        StructureStart $$6 = $$4.generate(commandSourceStack0.registryAccess(), $$5, $$5.getBiomeSource(), $$3.getChunkSource().randomState(), $$3.getStructureManager(), $$3.getSeed(), new ChunkPos(blockPos2), 0, $$3, p_214580_ -> true);
        if (!$$6.isValid()) {
            throw ERROR_STRUCTURE_FAILED.create();
        } else {
            BoundingBox $$7 = $$6.getBoundingBox();
            ChunkPos $$8 = new ChunkPos(SectionPos.blockToSectionCoord($$7.minX()), SectionPos.blockToSectionCoord($$7.minZ()));
            ChunkPos $$9 = new ChunkPos(SectionPos.blockToSectionCoord($$7.maxX()), SectionPos.blockToSectionCoord($$7.maxZ()));
            checkLoaded($$3, $$8, $$9);
            ChunkPos.rangeClosed($$8, $$9).forEach(p_289290_ -> $$6.placeInChunk($$3, $$3.structureManager(), $$5, $$3.m_213780_(), new BoundingBox(p_289290_.getMinBlockX(), $$3.m_141937_(), p_289290_.getMinBlockZ(), p_289290_.getMaxBlockX(), $$3.m_151558_(), p_289290_.getMaxBlockZ()), p_289290_));
            String $$10 = holderReferenceStructure1.key().location().toString();
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.place.structure.success", $$10, blockPos2.m_123341_(), blockPos2.m_123342_(), blockPos2.m_123343_()), true);
            return 1;
        }
    }

    public static int placeTemplate(CommandSourceStack commandSourceStack0, ResourceLocation resourceLocation1, BlockPos blockPos2, Rotation rotation3, Mirror mirror4, float float5, int int6) throws CommandSyntaxException {
        ServerLevel $$7 = commandSourceStack0.getLevel();
        StructureTemplateManager $$8 = $$7.getStructureManager();
        Optional<StructureTemplate> $$9;
        try {
            $$9 = $$8.get(resourceLocation1);
        } catch (ResourceLocationException var13) {
            throw ERROR_TEMPLATE_INVALID.create(resourceLocation1);
        }
        if ($$9.isEmpty()) {
            throw ERROR_TEMPLATE_INVALID.create(resourceLocation1);
        } else {
            StructureTemplate $$12 = (StructureTemplate) $$9.get();
            checkLoaded($$7, new ChunkPos(blockPos2), new ChunkPos(blockPos2.offset($$12.getSize())));
            StructurePlaceSettings $$13 = new StructurePlaceSettings().setMirror(mirror4).setRotation(rotation3);
            if (float5 < 1.0F) {
                $$13.clearProcessors().addProcessor(new BlockRotProcessor(float5)).setRandom(StructureBlockEntity.createRandom((long) int6));
            }
            boolean $$14 = $$12.placeInWorld($$7, blockPos2, blockPos2, $$13, StructureBlockEntity.createRandom((long) int6), 2);
            if (!$$14) {
                throw ERROR_TEMPLATE_FAILED.create();
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.place.template.success", resourceLocation1, blockPos2.m_123341_(), blockPos2.m_123342_(), blockPos2.m_123343_()), true);
                return 1;
            }
        }
    }

    private static void checkLoaded(ServerLevel serverLevel0, ChunkPos chunkPos1, ChunkPos chunkPos2) throws CommandSyntaxException {
        if (ChunkPos.rangeClosed(chunkPos1, chunkPos2).filter(p_214542_ -> !serverLevel0.m_46749_(p_214542_.getWorldPosition())).findAny().isPresent()) {
            throw BlockPosArgument.ERROR_NOT_LOADED.create();
        }
    }
}