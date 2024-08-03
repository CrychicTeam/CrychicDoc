package net.minecraft.server.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.datafixers.util.Unit;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.thread.ProcessorMailbox;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class ResetChunksCommand {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("resetchunks").requires(p_183683_ -> p_183683_.hasPermission(2))).executes(p_183693_ -> resetChunks((CommandSourceStack) p_183693_.getSource(), 0, true))).then(((RequiredArgumentBuilder) Commands.argument("range", IntegerArgumentType.integer(0, 5)).executes(p_183689_ -> resetChunks((CommandSourceStack) p_183689_.getSource(), IntegerArgumentType.getInteger(p_183689_, "range"), true))).then(Commands.argument("skipOldChunks", BoolArgumentType.bool()).executes(p_183669_ -> resetChunks((CommandSourceStack) p_183669_.getSource(), IntegerArgumentType.getInteger(p_183669_, "range"), BoolArgumentType.getBool(p_183669_, "skipOldChunks"))))));
    }

    private static int resetChunks(CommandSourceStack commandSourceStack0, int int1, boolean boolean2) {
        ServerLevel $$3 = commandSourceStack0.getLevel();
        ServerChunkCache $$4 = $$3.getChunkSource();
        $$4.chunkMap.debugReloadGenerator();
        Vec3 $$5 = commandSourceStack0.getPosition();
        ChunkPos $$6 = new ChunkPos(BlockPos.containing($$5));
        int $$7 = $$6.z - int1;
        int $$8 = $$6.z + int1;
        int $$9 = $$6.x - int1;
        int $$10 = $$6.x + int1;
        for (int $$11 = $$7; $$11 <= $$8; $$11++) {
            for (int $$12 = $$9; $$12 <= $$10; $$12++) {
                ChunkPos $$13 = new ChunkPos($$12, $$11);
                LevelChunk $$14 = $$4.m_62227_($$12, $$11, false);
                if ($$14 != null && (!boolean2 || !$$14.m_187675_())) {
                    for (BlockPos $$15 : BlockPos.betweenClosed($$13.getMinBlockX(), $$3.m_141937_(), $$13.getMinBlockZ(), $$13.getMaxBlockX(), $$3.m_151558_() - 1, $$13.getMaxBlockZ())) {
                        $$3.m_7731_($$15, Blocks.AIR.defaultBlockState(), 16);
                    }
                }
            }
        }
        ProcessorMailbox<Runnable> $$16 = ProcessorMailbox.create(Util.backgroundExecutor(), "worldgen-resetchunks");
        long $$17 = System.currentTimeMillis();
        int $$18 = (int1 * 2 + 1) * (int1 * 2 + 1);
        UnmodifiableIterator var33 = ImmutableList.of(ChunkStatus.BIOMES, ChunkStatus.NOISE, ChunkStatus.SURFACE, ChunkStatus.CARVERS, ChunkStatus.FEATURES, ChunkStatus.INITIALIZE_LIGHT).iterator();
        while (var33.hasNext()) {
            ChunkStatus $$19 = (ChunkStatus) var33.next();
            long $$20 = System.currentTimeMillis();
            CompletableFuture<Unit> $$21 = CompletableFuture.supplyAsync(() -> Unit.INSTANCE, $$16::m_6937_);
            for (int $$22 = $$6.z - int1; $$22 <= $$6.z + int1; $$22++) {
                for (int $$23 = $$6.x - int1; $$23 <= $$6.x + int1; $$23++) {
                    ChunkPos $$24 = new ChunkPos($$23, $$22);
                    LevelChunk $$25 = $$4.m_62227_($$23, $$22, false);
                    if ($$25 != null && (!boolean2 || !$$25.m_187675_())) {
                        List<ChunkAccess> $$26 = Lists.newArrayList();
                        int $$27 = Math.max(1, $$19.getRange());
                        for (int $$28 = $$24.z - $$27; $$28 <= $$24.z + $$27; $$28++) {
                            for (int $$29 = $$24.x - $$27; $$29 <= $$24.x + $$27; $$29++) {
                                ChunkAccess $$30 = $$4.getChunk($$29, $$28, $$19.getParent(), true);
                                ChunkAccess $$31;
                                if ($$30 instanceof ImposterProtoChunk) {
                                    $$31 = new ImposterProtoChunk(((ImposterProtoChunk) $$30).getWrapped(), true);
                                } else if ($$30 instanceof LevelChunk) {
                                    $$31 = new ImposterProtoChunk((LevelChunk) $$30, true);
                                } else {
                                    $$31 = $$30;
                                }
                                $$26.add($$31);
                            }
                        }
                        $$21 = $$21.thenComposeAsync(p_280957_ -> $$19.generate($$16::m_6937_, $$3, $$4.getGenerator(), $$3.getStructureManager(), $$4.getLightEngine(), p_183691_ -> {
                            throw new UnsupportedOperationException("Not creating full chunks here");
                        }, $$26).thenApply(p_183681_ -> {
                            if ($$19 == ChunkStatus.NOISE) {
                                p_183681_.left().ifPresent(p_183671_ -> Heightmap.primeHeightmaps(p_183671_, ChunkStatus.POST_FEATURES));
                            }
                            return Unit.INSTANCE;
                        }), $$16::m_6937_);
                    }
                }
            }
            commandSourceStack0.getServer().m_18701_($$21::isDone);
            LOGGER.debug($$19 + " took " + (System.currentTimeMillis() - $$20) + " ms");
        }
        long $$34 = System.currentTimeMillis();
        for (int $$35 = $$6.z - int1; $$35 <= $$6.z + int1; $$35++) {
            for (int $$36 = $$6.x - int1; $$36 <= $$6.x + int1; $$36++) {
                ChunkPos $$37 = new ChunkPos($$36, $$35);
                LevelChunk $$38 = $$4.m_62227_($$36, $$35, false);
                if ($$38 != null && (!boolean2 || !$$38.m_187675_())) {
                    for (BlockPos $$39 : BlockPos.betweenClosed($$37.getMinBlockX(), $$3.m_141937_(), $$37.getMinBlockZ(), $$37.getMaxBlockX(), $$3.m_151558_() - 1, $$37.getMaxBlockZ())) {
                        $$4.blockChanged($$39);
                    }
                }
            }
        }
        LOGGER.debug("blockChanged took " + (System.currentTimeMillis() - $$34) + " ms");
        long $$40 = System.currentTimeMillis() - $$17;
        commandSourceStack0.sendSuccess(() -> Component.literal(String.format(Locale.ROOT, "%d chunks have been reset. This took %d ms for %d chunks, or %02f ms per chunk", $$18, $$40, $$18, (float) $$40 / (float) $$18)), true);
        return 1;
    }
}