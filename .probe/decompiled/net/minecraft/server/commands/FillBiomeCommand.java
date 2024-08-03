package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.apache.commons.lang3.mutable.MutableInt;

public class FillBiomeCommand {

    public static final SimpleCommandExceptionType ERROR_NOT_LOADED = new SimpleCommandExceptionType(Component.translatable("argument.pos.unloaded"));

    private static final Dynamic2CommandExceptionType ERROR_VOLUME_TOO_LARGE = new Dynamic2CommandExceptionType((p_262025_, p_261647_) -> Component.translatable("commands.fillbiome.toobig", p_262025_, p_261647_));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("fillbiome").requires(p_261890_ -> p_261890_.hasPermission(2))).then(Commands.argument("from", BlockPosArgument.blockPos()).then(Commands.argument("to", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder) Commands.argument("biome", ResourceArgument.resource(commandBuildContext1, Registries.BIOME)).executes(p_262554_ -> fill((CommandSourceStack) p_262554_.getSource(), BlockPosArgument.getLoadedBlockPos(p_262554_, "from"), BlockPosArgument.getLoadedBlockPos(p_262554_, "to"), ResourceArgument.getResource(p_262554_, "biome", Registries.BIOME), p_262543_ -> true))).then(Commands.literal("replace").then(Commands.argument("filter", ResourceOrTagArgument.resourceOrTag(commandBuildContext1, Registries.BIOME)).executes(p_262544_ -> fill((CommandSourceStack) p_262544_.getSource(), BlockPosArgument.getLoadedBlockPos(p_262544_, "from"), BlockPosArgument.getLoadedBlockPos(p_262544_, "to"), ResourceArgument.getResource(p_262544_, "biome", Registries.BIOME), ResourceOrTagArgument.getResourceOrTag(p_262544_, "filter", Registries.BIOME)::test))))))));
    }

    private static int quantize(int int0) {
        return QuartPos.toBlock(QuartPos.fromBlock(int0));
    }

    private static BlockPos quantize(BlockPos blockPos0) {
        return new BlockPos(quantize(blockPos0.m_123341_()), quantize(blockPos0.m_123342_()), quantize(blockPos0.m_123343_()));
    }

    private static BiomeResolver makeResolver(MutableInt mutableInt0, ChunkAccess chunkAccess1, BoundingBox boundingBox2, Holder<Biome> holderBiome3, Predicate<Holder<Biome>> predicateHolderBiome4) {
        return (p_262550_, p_262551_, p_262552_, p_262553_) -> {
            int $$9 = QuartPos.toBlock(p_262550_);
            int $$10 = QuartPos.toBlock(p_262551_);
            int $$11 = QuartPos.toBlock(p_262552_);
            Holder<Biome> $$12 = chunkAccess1.getNoiseBiome(p_262550_, p_262551_, p_262552_);
            if (boundingBox2.isInside($$9, $$10, $$11) && predicateHolderBiome4.test($$12)) {
                mutableInt0.increment();
                return holderBiome3;
            } else {
                return $$12;
            }
        };
    }

    private static int fill(CommandSourceStack commandSourceStack0, BlockPos blockPos1, BlockPos blockPos2, Holder.Reference<Biome> holderReferenceBiome3, Predicate<Holder<Biome>> predicateHolderBiome4) throws CommandSyntaxException {
        BlockPos $$5 = quantize(blockPos1);
        BlockPos $$6 = quantize(blockPos2);
        BoundingBox $$7 = BoundingBox.fromCorners($$5, $$6);
        int $$8 = $$7.getXSpan() * $$7.getYSpan() * $$7.getZSpan();
        int $$9 = commandSourceStack0.getLevel().m_46469_().getInt(GameRules.RULE_COMMAND_MODIFICATION_BLOCK_LIMIT);
        if ($$8 > $$9) {
            throw ERROR_VOLUME_TOO_LARGE.create($$9, $$8);
        } else {
            ServerLevel $$10 = commandSourceStack0.getLevel();
            List<ChunkAccess> $$11 = new ArrayList();
            for (int $$12 = SectionPos.blockToSectionCoord($$7.minZ()); $$12 <= SectionPos.blockToSectionCoord($$7.maxZ()); $$12++) {
                for (int $$13 = SectionPos.blockToSectionCoord($$7.minX()); $$13 <= SectionPos.blockToSectionCoord($$7.maxX()); $$13++) {
                    ChunkAccess $$14 = $$10.m_6522_($$13, $$12, ChunkStatus.FULL, false);
                    if ($$14 == null) {
                        throw ERROR_NOT_LOADED.create();
                    }
                    $$11.add($$14);
                }
            }
            MutableInt $$15 = new MutableInt(0);
            for (ChunkAccess $$16 : $$11) {
                $$16.fillBiomesFromNoise(makeResolver($$15, $$16, $$7, holderReferenceBiome3, predicateHolderBiome4), $$10.getChunkSource().randomState().sampler());
                $$16.setUnsaved(true);
            }
            $$10.getChunkSource().chunkMap.resendBiomesForChunks($$11);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.fillbiome.success.count", $$15.getValue(), $$7.minX(), $$7.minY(), $$7.minZ(), $$7.maxX(), $$7.maxY(), $$7.maxZ()), true);
            return $$15.getValue();
        }
    }
}