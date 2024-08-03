package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.HeightmapTypeArgument;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.commands.data.DataAccessor;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;

public class ExecuteCommand {

    private static final int MAX_TEST_AREA = 32768;

    private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((p_137129_, p_137130_) -> Component.translatable("commands.execute.blocks.toobig", p_137129_, p_137130_));

    private static final SimpleCommandExceptionType ERROR_CONDITIONAL_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.execute.conditional.fail"));

    private static final DynamicCommandExceptionType ERROR_CONDITIONAL_FAILED_COUNT = new DynamicCommandExceptionType(p_137127_ -> Component.translatable("commands.execute.conditional.fail_count", p_137127_));

    private static final BinaryOperator<ResultConsumer<CommandSourceStack>> CALLBACK_CHAINER = (p_137045_, p_137046_) -> (p_180160_, p_180161_, p_180162_) -> {
        p_137045_.onCommandComplete(p_180160_, p_180161_, p_180162_);
        p_137046_.onCommandComplete(p_180160_, p_180161_, p_180162_);
    };

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_PREDICATE = (p_278905_, p_278906_) -> {
        LootDataManager $$2 = ((CommandSourceStack) p_278905_.getSource()).getServer().getLootData();
        return SharedSuggestionProvider.suggestResource($$2.getKeys(LootDataType.PREDICATE), p_278906_);
    };

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        LiteralCommandNode<CommandSourceStack> $$2 = commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) Commands.literal("execute").requires(p_137197_ -> p_137197_.hasPermission(2)));
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("execute").requires(p_137103_ -> p_137103_.hasPermission(2))).then(Commands.literal("run").redirect(commandDispatcherCommandSourceStack0.getRoot()))).then(addConditionals($$2, Commands.literal("if"), true, commandBuildContext1))).then(addConditionals($$2, Commands.literal("unless"), false, commandBuildContext1))).then(Commands.literal("as").then(Commands.argument("targets", EntityArgument.entities()).fork($$2, p_137299_ -> {
            List<CommandSourceStack> $$1 = Lists.newArrayList();
            for (Entity $$2x : EntityArgument.getOptionalEntities(p_137299_, "targets")) {
                $$1.add(((CommandSourceStack) p_137299_.getSource()).withEntity($$2x));
            }
            return $$1;
        })))).then(Commands.literal("at").then(Commands.argument("targets", EntityArgument.entities()).fork($$2, p_284653_ -> {
            List<CommandSourceStack> $$1 = Lists.newArrayList();
            for (Entity $$2x : EntityArgument.getOptionalEntities(p_284653_, "targets")) {
                $$1.add(((CommandSourceStack) p_284653_.getSource()).withLevel((ServerLevel) $$2x.level()).withPosition($$2x.position()).withRotation($$2x.getRotationVector()));
            }
            return $$1;
        })))).then(((LiteralArgumentBuilder) Commands.literal("store").then(wrapStores($$2, Commands.literal("result"), true))).then(wrapStores($$2, Commands.literal("success"), false)))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("positioned").then(Commands.argument("pos", Vec3Argument.vec3()).redirect($$2, p_137295_ -> ((CommandSourceStack) p_137295_.getSource()).withPosition(Vec3Argument.getVec3(p_137295_, "pos")).withAnchor(EntityAnchorArgument.Anchor.FEET)))).then(Commands.literal("as").then(Commands.argument("targets", EntityArgument.entities()).fork($$2, p_137293_ -> {
            List<CommandSourceStack> $$1 = Lists.newArrayList();
            for (Entity $$2x : EntityArgument.getOptionalEntities(p_137293_, "targets")) {
                $$1.add(((CommandSourceStack) p_137293_.getSource()).withPosition($$2x.position()));
            }
            return $$1;
        })))).then(Commands.literal("over").then(Commands.argument("heightmap", HeightmapTypeArgument.heightmap()).redirect($$2, p_274814_ -> {
            Vec3 $$1 = ((CommandSourceStack) p_274814_.getSource()).getPosition();
            ServerLevel $$2x = ((CommandSourceStack) p_274814_.getSource()).getLevel();
            double $$3 = $$1.x();
            double $$4 = $$1.z();
            if (!$$2x.m_7232_(SectionPos.blockToSectionCoord($$3), SectionPos.blockToSectionCoord($$4))) {
                throw BlockPosArgument.ERROR_NOT_LOADED.create();
            } else {
                int $$5 = $$2x.m_6924_(HeightmapTypeArgument.getHeightmap(p_274814_, "heightmap"), Mth.floor($$3), Mth.floor($$4));
                return ((CommandSourceStack) p_274814_.getSource()).withPosition(new Vec3($$3, (double) $$5, $$4));
            }
        }))))).then(((LiteralArgumentBuilder) Commands.literal("rotated").then(Commands.argument("rot", RotationArgument.rotation()).redirect($$2, p_137291_ -> ((CommandSourceStack) p_137291_.getSource()).withRotation(RotationArgument.getRotation(p_137291_, "rot").getRotation((CommandSourceStack) p_137291_.getSource()))))).then(Commands.literal("as").then(Commands.argument("targets", EntityArgument.entities()).fork($$2, p_137289_ -> {
            List<CommandSourceStack> $$1 = Lists.newArrayList();
            for (Entity $$2x : EntityArgument.getOptionalEntities(p_137289_, "targets")) {
                $$1.add(((CommandSourceStack) p_137289_.getSource()).withRotation($$2x.getRotationVector()));
            }
            return $$1;
        }))))).then(((LiteralArgumentBuilder) Commands.literal("facing").then(Commands.literal("entity").then(Commands.argument("targets", EntityArgument.entities()).then(Commands.argument("anchor", EntityAnchorArgument.anchor()).fork($$2, p_137287_ -> {
            List<CommandSourceStack> $$1 = Lists.newArrayList();
            EntityAnchorArgument.Anchor $$2x = EntityAnchorArgument.getAnchor(p_137287_, "anchor");
            for (Entity $$3 : EntityArgument.getOptionalEntities(p_137287_, "targets")) {
                $$1.add(((CommandSourceStack) p_137287_.getSource()).facing($$3, $$2x));
            }
            return $$1;
        }))))).then(Commands.argument("pos", Vec3Argument.vec3()).redirect($$2, p_137285_ -> ((CommandSourceStack) p_137285_.getSource()).facing(Vec3Argument.getVec3(p_137285_, "pos")))))).then(Commands.literal("align").then(Commands.argument("axes", SwizzleArgument.swizzle()).redirect($$2, p_137283_ -> ((CommandSourceStack) p_137283_.getSource()).withPosition(((CommandSourceStack) p_137283_.getSource()).getPosition().align(SwizzleArgument.getSwizzle(p_137283_, "axes"))))))).then(Commands.literal("anchored").then(Commands.argument("anchor", EntityAnchorArgument.anchor()).redirect($$2, p_137281_ -> ((CommandSourceStack) p_137281_.getSource()).withAnchor(EntityAnchorArgument.getAnchor(p_137281_, "anchor")))))).then(Commands.literal("in").then(Commands.argument("dimension", DimensionArgument.dimension()).redirect($$2, p_137279_ -> ((CommandSourceStack) p_137279_.getSource()).withLevel(DimensionArgument.getDimension(p_137279_, "dimension")))))).then(Commands.literal("summon").then(Commands.argument("entity", ResourceArgument.resource(commandBuildContext1, Registries.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).redirect($$2, p_269759_ -> spawnEntityAndRedirect((CommandSourceStack) p_269759_.getSource(), ResourceArgument.getSummonableEntityType(p_269759_, "entity")))))).then(createRelationOperations($$2, Commands.literal("on"))));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> wrapStores(LiteralCommandNode<CommandSourceStack> literalCommandNodeCommandSourceStack0, LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilderCommandSourceStack1, boolean boolean2) {
        literalArgumentBuilderCommandSourceStack1.then(Commands.literal("score").then(Commands.argument("targets", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("objective", ObjectiveArgument.objective()).redirect(literalCommandNodeCommandSourceStack0, p_137271_ -> storeValue((CommandSourceStack) p_137271_.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(p_137271_, "targets"), ObjectiveArgument.getObjective(p_137271_, "objective"), boolean2)))));
        literalArgumentBuilderCommandSourceStack1.then(Commands.literal("bossbar").then(((RequiredArgumentBuilder) Commands.argument("id", ResourceLocationArgument.id()).suggests(BossBarCommands.SUGGEST_BOSS_BAR).then(Commands.literal("value").redirect(literalCommandNodeCommandSourceStack0, p_137259_ -> storeValue((CommandSourceStack) p_137259_.getSource(), BossBarCommands.getBossBar(p_137259_), true, boolean2)))).then(Commands.literal("max").redirect(literalCommandNodeCommandSourceStack0, p_137247_ -> storeValue((CommandSourceStack) p_137247_.getSource(), BossBarCommands.getBossBar(p_137247_), false, boolean2)))));
        for (DataCommands.DataProvider $$3 : DataCommands.TARGET_PROVIDERS) {
            $$3.wrap(literalArgumentBuilderCommandSourceStack1, p_137101_ -> p_137101_.then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("path", NbtPathArgument.nbtPath()).then(Commands.literal("int").then(Commands.argument("scale", DoubleArgumentType.doubleArg()).redirect(literalCommandNodeCommandSourceStack0, p_180216_ -> storeData((CommandSourceStack) p_180216_.getSource(), $$3.access(p_180216_), NbtPathArgument.getPath(p_180216_, "path"), p_180219_ -> IntTag.valueOf((int) ((double) p_180219_ * DoubleArgumentType.getDouble(p_180216_, "scale"))), boolean2))))).then(Commands.literal("float").then(Commands.argument("scale", DoubleArgumentType.doubleArg()).redirect(literalCommandNodeCommandSourceStack0, p_180209_ -> storeData((CommandSourceStack) p_180209_.getSource(), $$3.access(p_180209_), NbtPathArgument.getPath(p_180209_, "path"), p_180212_ -> FloatTag.valueOf((float) ((double) p_180212_ * DoubleArgumentType.getDouble(p_180209_, "scale"))), boolean2))))).then(Commands.literal("short").then(Commands.argument("scale", DoubleArgumentType.doubleArg()).redirect(literalCommandNodeCommandSourceStack0, p_180199_ -> storeData((CommandSourceStack) p_180199_.getSource(), $$3.access(p_180199_), NbtPathArgument.getPath(p_180199_, "path"), p_180202_ -> ShortTag.valueOf((short) ((int) ((double) p_180202_ * DoubleArgumentType.getDouble(p_180199_, "scale")))), boolean2))))).then(Commands.literal("long").then(Commands.argument("scale", DoubleArgumentType.doubleArg()).redirect(literalCommandNodeCommandSourceStack0, p_180189_ -> storeData((CommandSourceStack) p_180189_.getSource(), $$3.access(p_180189_), NbtPathArgument.getPath(p_180189_, "path"), p_180192_ -> LongTag.valueOf((long) ((double) p_180192_ * DoubleArgumentType.getDouble(p_180189_, "scale"))), boolean2))))).then(Commands.literal("double").then(Commands.argument("scale", DoubleArgumentType.doubleArg()).redirect(literalCommandNodeCommandSourceStack0, p_180179_ -> storeData((CommandSourceStack) p_180179_.getSource(), $$3.access(p_180179_), NbtPathArgument.getPath(p_180179_, "path"), p_180182_ -> DoubleTag.valueOf((double) p_180182_ * DoubleArgumentType.getDouble(p_180179_, "scale")), boolean2))))).then(Commands.literal("byte").then(Commands.argument("scale", DoubleArgumentType.doubleArg()).redirect(literalCommandNodeCommandSourceStack0, p_180156_ -> storeData((CommandSourceStack) p_180156_.getSource(), $$3.access(p_180156_), NbtPathArgument.getPath(p_180156_, "path"), p_180165_ -> ByteTag.valueOf((byte) ((int) ((double) p_180165_ * DoubleArgumentType.getDouble(p_180156_, "scale")))), boolean2))))));
        }
        return literalArgumentBuilderCommandSourceStack1;
    }

    private static CommandSourceStack storeValue(CommandSourceStack commandSourceStack0, Collection<String> collectionString1, Objective objective2, boolean boolean3) {
        Scoreboard $$4 = commandSourceStack0.getServer().getScoreboard();
        return commandSourceStack0.withCallback((p_137136_, p_137137_, p_137138_) -> {
            for (String $$7 : collectionString1) {
                Score $$8 = $$4.getOrCreatePlayerScore($$7, objective2);
                int $$9 = boolean3 ? p_137138_ : (p_137137_ ? 1 : 0);
                $$8.setScore($$9);
            }
        }, CALLBACK_CHAINER);
    }

    private static CommandSourceStack storeValue(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1, boolean boolean2, boolean boolean3) {
        return commandSourceStack0.withCallback((p_137185_, p_137186_, p_137187_) -> {
            int $$6 = boolean3 ? p_137187_ : (p_137186_ ? 1 : 0);
            if (boolean2) {
                customBossEvent1.setValue($$6);
            } else {
                customBossEvent1.setMax($$6);
            }
        }, CALLBACK_CHAINER);
    }

    private static CommandSourceStack storeData(CommandSourceStack commandSourceStack0, DataAccessor dataAccessor1, NbtPathArgument.NbtPath nbtPathArgumentNbtPath2, IntFunction<Tag> intFunctionTag3, boolean boolean4) {
        return commandSourceStack0.withCallback((p_137153_, p_137154_, p_137155_) -> {
            try {
                CompoundTag $$7 = dataAccessor1.getData();
                int $$8 = boolean4 ? p_137155_ : (p_137154_ ? 1 : 0);
                nbtPathArgumentNbtPath2.set($$7, (Tag) intFunctionTag3.apply($$8));
                dataAccessor1.setData($$7);
            } catch (CommandSyntaxException var9) {
            }
        }, CALLBACK_CHAINER);
    }

    private static boolean isChunkLoaded(ServerLevel serverLevel0, BlockPos blockPos1) {
        ChunkPos $$2 = new ChunkPos(blockPos1);
        LevelChunk $$3 = serverLevel0.getChunkSource().getChunkNow($$2.x, $$2.z);
        return $$3 == null ? false : $$3.getFullStatus() == FullChunkStatus.ENTITY_TICKING && serverLevel0.areEntitiesLoaded($$2.toLong());
    }

    private static ArgumentBuilder<CommandSourceStack, ?> addConditionals(CommandNode<CommandSourceStack> commandNodeCommandSourceStack0, LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilderCommandSourceStack1, boolean boolean2, CommandBuildContext commandBuildContext3) {
        ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) literalArgumentBuilderCommandSourceStack1.then(Commands.literal("block").then(Commands.argument("pos", BlockPosArgument.blockPos()).then(addConditional(commandNodeCommandSourceStack0, Commands.argument("block", BlockPredicateArgument.blockPredicate(commandBuildContext3)), boolean2, p_137277_ -> BlockPredicateArgument.getBlockPredicate(p_137277_, "block").test(new BlockInWorld(((CommandSourceStack) p_137277_.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(p_137277_, "pos"), true))))))).then(Commands.literal("biome").then(Commands.argument("pos", BlockPosArgument.blockPos()).then(addConditional(commandNodeCommandSourceStack0, Commands.argument("biome", ResourceOrTagArgument.resourceOrTag(commandBuildContext3, Registries.BIOME)), boolean2, p_277265_ -> ResourceOrTagArgument.getResourceOrTag(p_277265_, "biome", Registries.BIOME).test(((CommandSourceStack) p_277265_.getSource()).getLevel().m_204166_(BlockPosArgument.getLoadedBlockPos(p_277265_, "pos")))))))).then(Commands.literal("loaded").then(addConditional(commandNodeCommandSourceStack0, Commands.argument("pos", BlockPosArgument.blockPos()), boolean2, p_269757_ -> isChunkLoaded(((CommandSourceStack) p_269757_.getSource()).getLevel(), BlockPosArgument.getBlockPos(p_269757_, "pos")))))).then(Commands.literal("dimension").then(addConditional(commandNodeCommandSourceStack0, Commands.argument("dimension", DimensionArgument.dimension()), boolean2, p_264789_ -> DimensionArgument.getDimension(p_264789_, "dimension") == ((CommandSourceStack) p_264789_.getSource()).getLevel())))).then(Commands.literal("score").then(Commands.argument("target", ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("targetObjective", ObjectiveArgument.objective()).then(Commands.literal("=").then(Commands.argument("source", ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(addConditional(commandNodeCommandSourceStack0, Commands.argument("sourceObjective", ObjectiveArgument.objective()), boolean2, p_137275_ -> checkScore(p_137275_, Integer::equals)))))).then(Commands.literal("<").then(Commands.argument("source", ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(addConditional(commandNodeCommandSourceStack0, Commands.argument("sourceObjective", ObjectiveArgument.objective()), boolean2, p_137273_ -> checkScore(p_137273_, (p_180204_, p_180205_) -> p_180204_ < p_180205_)))))).then(Commands.literal("<=").then(Commands.argument("source", ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(addConditional(commandNodeCommandSourceStack0, Commands.argument("sourceObjective", ObjectiveArgument.objective()), boolean2, p_137261_ -> checkScore(p_137261_, (p_180194_, p_180195_) -> p_180194_ <= p_180195_)))))).then(Commands.literal(">").then(Commands.argument("source", ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(addConditional(commandNodeCommandSourceStack0, Commands.argument("sourceObjective", ObjectiveArgument.objective()), boolean2, p_137249_ -> checkScore(p_137249_, (p_180184_, p_180185_) -> p_180184_ > p_180185_)))))).then(Commands.literal(">=").then(Commands.argument("source", ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(addConditional(commandNodeCommandSourceStack0, Commands.argument("sourceObjective", ObjectiveArgument.objective()), boolean2, p_137234_ -> checkScore(p_137234_, (p_180167_, p_180168_) -> p_180167_ >= p_180168_)))))).then(Commands.literal("matches").then(addConditional(commandNodeCommandSourceStack0, Commands.argument("range", RangeArgument.intRange()), boolean2, p_137216_ -> checkScore(p_137216_, RangeArgument.Ints.getRange(p_137216_, "range"))))))))).then(Commands.literal("blocks").then(Commands.argument("start", BlockPosArgument.blockPos()).then(Commands.argument("end", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder) Commands.argument("destination", BlockPosArgument.blockPos()).then(addIfBlocksConditional(commandNodeCommandSourceStack0, Commands.literal("all"), boolean2, false))).then(addIfBlocksConditional(commandNodeCommandSourceStack0, Commands.literal("masked"), boolean2, true))))))).then(Commands.literal("entity").then(((RequiredArgumentBuilder) Commands.argument("entities", EntityArgument.entities()).fork(commandNodeCommandSourceStack0, p_137232_ -> expect(p_137232_, boolean2, !EntityArgument.getOptionalEntities(p_137232_, "entities").isEmpty()))).executes(createNumericConditionalHandler(boolean2, p_137189_ -> EntityArgument.getOptionalEntities(p_137189_, "entities").size()))))).then(Commands.literal("predicate").then(addConditional(commandNodeCommandSourceStack0, Commands.argument("predicate", ResourceLocationArgument.id()).suggests(SUGGEST_PREDICATE), boolean2, p_137054_ -> checkCustomPredicate((CommandSourceStack) p_137054_.getSource(), ResourceLocationArgument.getPredicate(p_137054_, "predicate")))));
        for (DataCommands.DataProvider $$4 : DataCommands.SOURCE_PROVIDERS) {
            literalArgumentBuilderCommandSourceStack1.then($$4.wrap(Commands.literal("data"), p_137092_ -> p_137092_.then(((RequiredArgumentBuilder) Commands.argument("path", NbtPathArgument.nbtPath()).fork(commandNodeCommandSourceStack0, p_180175_ -> expect(p_180175_, boolean2, checkMatchingData($$4.access(p_180175_), NbtPathArgument.getPath(p_180175_, "path")) > 0))).executes(createNumericConditionalHandler(boolean2, p_180152_ -> checkMatchingData($$4.access(p_180152_), NbtPathArgument.getPath(p_180152_, "path")))))));
        }
        return literalArgumentBuilderCommandSourceStack1;
    }

    private static Command<CommandSourceStack> createNumericConditionalHandler(boolean boolean0, ExecuteCommand.CommandNumericPredicate executeCommandCommandNumericPredicate1) {
        return boolean0 ? p_288391_ -> {
            int $$2 = executeCommandCommandNumericPredicate1.test(p_288391_);
            if ($$2 > 0) {
                ((CommandSourceStack) p_288391_.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass_count", $$2), false);
                return $$2;
            } else {
                throw ERROR_CONDITIONAL_FAILED.create();
            }
        } : p_288393_ -> {
            int $$2 = executeCommandCommandNumericPredicate1.test(p_288393_);
            if ($$2 == 0) {
                ((CommandSourceStack) p_288393_.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass"), false);
                return 1;
            } else {
                throw ERROR_CONDITIONAL_FAILED_COUNT.create($$2);
            }
        };
    }

    private static int checkMatchingData(DataAccessor dataAccessor0, NbtPathArgument.NbtPath nbtPathArgumentNbtPath1) throws CommandSyntaxException {
        return nbtPathArgumentNbtPath1.countMatching(dataAccessor0.getData());
    }

    private static boolean checkScore(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, BiPredicate<Integer, Integer> biPredicateIntegerInteger1) throws CommandSyntaxException {
        String $$2 = ScoreHolderArgument.getName(commandContextCommandSourceStack0, "target");
        Objective $$3 = ObjectiveArgument.getObjective(commandContextCommandSourceStack0, "targetObjective");
        String $$4 = ScoreHolderArgument.getName(commandContextCommandSourceStack0, "source");
        Objective $$5 = ObjectiveArgument.getObjective(commandContextCommandSourceStack0, "sourceObjective");
        Scoreboard $$6 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getScoreboard();
        if ($$6.hasPlayerScore($$2, $$3) && $$6.hasPlayerScore($$4, $$5)) {
            Score $$7 = $$6.getOrCreatePlayerScore($$2, $$3);
            Score $$8 = $$6.getOrCreatePlayerScore($$4, $$5);
            return biPredicateIntegerInteger1.test($$7.getScore(), $$8.getScore());
        } else {
            return false;
        }
    }

    private static boolean checkScore(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, MinMaxBounds.Ints minMaxBoundsInts1) throws CommandSyntaxException {
        String $$2 = ScoreHolderArgument.getName(commandContextCommandSourceStack0, "target");
        Objective $$3 = ObjectiveArgument.getObjective(commandContextCommandSourceStack0, "targetObjective");
        Scoreboard $$4 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getScoreboard();
        return !$$4.hasPlayerScore($$2, $$3) ? false : minMaxBoundsInts1.matches($$4.getOrCreatePlayerScore($$2, $$3).getScore());
    }

    private static boolean checkCustomPredicate(CommandSourceStack commandSourceStack0, LootItemCondition lootItemCondition1) {
        ServerLevel $$2 = commandSourceStack0.getLevel();
        LootParams $$3 = new LootParams.Builder($$2).withParameter(LootContextParams.ORIGIN, commandSourceStack0.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, commandSourceStack0.getEntity()).create(LootContextParamSets.COMMAND);
        LootContext $$4 = new LootContext.Builder($$3).create(null);
        $$4.pushVisitedElement(LootContext.createVisitedEntry(lootItemCondition1));
        return lootItemCondition1.test($$4);
    }

    private static Collection<CommandSourceStack> expect(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, boolean boolean1, boolean boolean2) {
        return (Collection<CommandSourceStack>) (boolean2 == boolean1 ? Collections.singleton((CommandSourceStack) commandContextCommandSourceStack0.getSource()) : Collections.emptyList());
    }

    private static ArgumentBuilder<CommandSourceStack, ?> addConditional(CommandNode<CommandSourceStack> commandNodeCommandSourceStack0, ArgumentBuilder<CommandSourceStack, ?> argumentBuilderCommandSourceStack1, boolean boolean2, ExecuteCommand.CommandPredicate executeCommandCommandPredicate3) {
        return argumentBuilderCommandSourceStack1.fork(commandNodeCommandSourceStack0, p_137214_ -> expect(p_137214_, boolean2, executeCommandCommandPredicate3.test(p_137214_))).executes(p_288396_ -> {
            if (boolean2 == executeCommandCommandPredicate3.test(p_288396_)) {
                ((CommandSourceStack) p_288396_.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass"), false);
                return 1;
            } else {
                throw ERROR_CONDITIONAL_FAILED.create();
            }
        });
    }

    private static ArgumentBuilder<CommandSourceStack, ?> addIfBlocksConditional(CommandNode<CommandSourceStack> commandNodeCommandSourceStack0, ArgumentBuilder<CommandSourceStack, ?> argumentBuilderCommandSourceStack1, boolean boolean2, boolean boolean3) {
        return argumentBuilderCommandSourceStack1.fork(commandNodeCommandSourceStack0, p_137180_ -> expect(p_137180_, boolean2, checkRegions(p_137180_, boolean3).isPresent())).executes(boolean2 ? p_137210_ -> checkIfRegions(p_137210_, boolean3) : p_137165_ -> checkUnlessRegions(p_137165_, boolean3));
    }

    private static int checkIfRegions(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, boolean boolean1) throws CommandSyntaxException {
        OptionalInt $$2 = checkRegions(commandContextCommandSourceStack0, boolean1);
        if ($$2.isPresent()) {
            ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass_count", $$2.getAsInt()), false);
            return $$2.getAsInt();
        } else {
            throw ERROR_CONDITIONAL_FAILED.create();
        }
    }

    private static int checkUnlessRegions(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, boolean boolean1) throws CommandSyntaxException {
        OptionalInt $$2 = checkRegions(commandContextCommandSourceStack0, boolean1);
        if ($$2.isPresent()) {
            throw ERROR_CONDITIONAL_FAILED_COUNT.create($$2.getAsInt());
        } else {
            ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass"), false);
            return 1;
        }
    }

    private static OptionalInt checkRegions(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, boolean boolean1) throws CommandSyntaxException {
        return checkRegions(((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(commandContextCommandSourceStack0, "start"), BlockPosArgument.getLoadedBlockPos(commandContextCommandSourceStack0, "end"), BlockPosArgument.getLoadedBlockPos(commandContextCommandSourceStack0, "destination"), boolean1);
    }

    private static OptionalInt checkRegions(ServerLevel serverLevel0, BlockPos blockPos1, BlockPos blockPos2, BlockPos blockPos3, boolean boolean4) throws CommandSyntaxException {
        BoundingBox $$5 = BoundingBox.fromCorners(blockPos1, blockPos2);
        BoundingBox $$6 = BoundingBox.fromCorners(blockPos3, blockPos3.offset($$5.getLength()));
        BlockPos $$7 = new BlockPos($$6.minX() - $$5.minX(), $$6.minY() - $$5.minY(), $$6.minZ() - $$5.minZ());
        int $$8 = $$5.getXSpan() * $$5.getYSpan() * $$5.getZSpan();
        if ($$8 > 32768) {
            throw ERROR_AREA_TOO_LARGE.create(32768, $$8);
        } else {
            int $$9 = 0;
            for (int $$10 = $$5.minZ(); $$10 <= $$5.maxZ(); $$10++) {
                for (int $$11 = $$5.minY(); $$11 <= $$5.maxY(); $$11++) {
                    for (int $$12 = $$5.minX(); $$12 <= $$5.maxX(); $$12++) {
                        BlockPos $$13 = new BlockPos($$12, $$11, $$10);
                        BlockPos $$14 = $$13.offset($$7);
                        BlockState $$15 = serverLevel0.m_8055_($$13);
                        if (!boolean4 || !$$15.m_60713_(Blocks.AIR)) {
                            if ($$15 != serverLevel0.m_8055_($$14)) {
                                return OptionalInt.empty();
                            }
                            BlockEntity $$16 = serverLevel0.m_7702_($$13);
                            BlockEntity $$17 = serverLevel0.m_7702_($$14);
                            if ($$16 != null) {
                                if ($$17 == null) {
                                    return OptionalInt.empty();
                                }
                                if ($$17.getType() != $$16.getType()) {
                                    return OptionalInt.empty();
                                }
                                CompoundTag $$18 = $$16.saveWithoutMetadata();
                                CompoundTag $$19 = $$17.saveWithoutMetadata();
                                if (!$$18.equals($$19)) {
                                    return OptionalInt.empty();
                                }
                            }
                            $$9++;
                        }
                    }
                }
            }
            return OptionalInt.of($$9);
        }
    }

    private static RedirectModifier<CommandSourceStack> expandOneToOneEntityRelation(Function<Entity, Optional<Entity>> functionEntityOptionalEntity0) {
        return p_264786_ -> {
            CommandSourceStack $$2 = (CommandSourceStack) p_264786_.getSource();
            Entity $$3 = $$2.getEntity();
            return (Collection) ($$3 == null ? List.of() : (Collection) ((Optional) functionEntityOptionalEntity0.apply($$3)).filter(p_264783_ -> !p_264783_.isRemoved()).map(p_264775_ -> List.of($$2.withEntity(p_264775_))).orElse(List.of()));
        };
    }

    private static RedirectModifier<CommandSourceStack> expandOneToManyEntityRelation(Function<Entity, Stream<Entity>> functionEntityStreamEntity0) {
        return p_264780_ -> {
            CommandSourceStack $$2 = (CommandSourceStack) p_264780_.getSource();
            Entity $$3 = $$2.getEntity();
            return $$3 == null ? List.of() : ((Stream) functionEntityStreamEntity0.apply($$3)).filter(p_264784_ -> !p_264784_.isRemoved()).map($$2::m_81329_).toList();
        };
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createRelationOperations(CommandNode<CommandSourceStack> commandNodeCommandSourceStack0, LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilderCommandSourceStack1) {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) literalArgumentBuilderCommandSourceStack1.then(Commands.literal("owner").fork(commandNodeCommandSourceStack0, expandOneToOneEntityRelation(p_269758_ -> p_269758_ instanceof OwnableEntity $$1 ? Optional.ofNullable($$1.getOwner()) : Optional.empty())))).then(Commands.literal("leasher").fork(commandNodeCommandSourceStack0, expandOneToOneEntityRelation(p_264782_ -> p_264782_ instanceof Mob $$1 ? Optional.ofNullable($$1.getLeashHolder()) : Optional.empty())))).then(Commands.literal("target").fork(commandNodeCommandSourceStack0, expandOneToOneEntityRelation(p_272389_ -> p_272389_ instanceof Targeting $$1 ? Optional.ofNullable($$1.getTarget()) : Optional.empty())))).then(Commands.literal("attacker").fork(commandNodeCommandSourceStack0, expandOneToOneEntityRelation(p_272388_ -> p_272388_ instanceof Attackable $$1 ? Optional.ofNullable($$1.getLastAttacker()) : Optional.empty())))).then(Commands.literal("vehicle").fork(commandNodeCommandSourceStack0, expandOneToOneEntityRelation(p_264776_ -> Optional.ofNullable(p_264776_.getVehicle()))))).then(Commands.literal("controller").fork(commandNodeCommandSourceStack0, expandOneToOneEntityRelation(p_274815_ -> Optional.ofNullable(p_274815_.getControllingPassenger()))))).then(Commands.literal("origin").fork(commandNodeCommandSourceStack0, expandOneToOneEntityRelation(p_266631_ -> p_266631_ instanceof TraceableEntity $$1 ? Optional.ofNullable($$1.getOwner()) : Optional.empty())))).then(Commands.literal("passengers").fork(commandNodeCommandSourceStack0, expandOneToManyEntityRelation(p_264777_ -> p_264777_.getPassengers().stream())));
    }

    private static CommandSourceStack spawnEntityAndRedirect(CommandSourceStack commandSourceStack0, Holder.Reference<EntityType<?>> holderReferenceEntityType1) throws CommandSyntaxException {
        Entity $$2 = SummonCommand.createEntity(commandSourceStack0, holderReferenceEntityType1, commandSourceStack0.getPosition(), new CompoundTag(), true);
        return commandSourceStack0.withEntity($$2);
    }

    @FunctionalInterface
    interface CommandNumericPredicate {

        int test(CommandContext<CommandSourceStack> var1) throws CommandSyntaxException;
    }

    @FunctionalInterface
    interface CommandPredicate {

        boolean test(CommandContext<CommandSourceStack> var1) throws CommandSyntaxException;
    }
}