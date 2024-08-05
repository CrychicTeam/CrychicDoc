package net.minecraft.server.commands;

import com.google.common.base.Stopwatch;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.time.Duration;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.slf4j.Logger;

public class LocateCommand {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final DynamicCommandExceptionType ERROR_STRUCTURE_NOT_FOUND = new DynamicCommandExceptionType(p_201831_ -> Component.translatable("commands.locate.structure.not_found", p_201831_));

    private static final DynamicCommandExceptionType ERROR_STRUCTURE_INVALID = new DynamicCommandExceptionType(p_207534_ -> Component.translatable("commands.locate.structure.invalid", p_207534_));

    private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType(p_214514_ -> Component.translatable("commands.locate.biome.not_found", p_214514_));

    private static final DynamicCommandExceptionType ERROR_POI_NOT_FOUND = new DynamicCommandExceptionType(p_214512_ -> Component.translatable("commands.locate.poi.not_found", p_214512_));

    private static final int MAX_STRUCTURE_SEARCH_RADIUS = 100;

    private static final int MAX_BIOME_SEARCH_RADIUS = 6400;

    private static final int BIOME_SAMPLE_RESOLUTION_HORIZONTAL = 32;

    private static final int BIOME_SAMPLE_RESOLUTION_VERTICAL = 64;

    private static final int POI_SEARCH_RADIUS = 256;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("locate").requires(p_214470_ -> p_214470_.hasPermission(2))).then(Commands.literal("structure").then(Commands.argument("structure", ResourceOrTagKeyArgument.resourceOrTagKey(Registries.STRUCTURE)).executes(p_258233_ -> locateStructure((CommandSourceStack) p_258233_.getSource(), ResourceOrTagKeyArgument.getResourceOrTagKey(p_258233_, "structure", Registries.STRUCTURE, ERROR_STRUCTURE_INVALID)))))).then(Commands.literal("biome").then(Commands.argument("biome", ResourceOrTagArgument.resourceOrTag(commandBuildContext1, Registries.BIOME)).executes(p_258232_ -> locateBiome((CommandSourceStack) p_258232_.getSource(), ResourceOrTagArgument.getResourceOrTag(p_258232_, "biome", Registries.BIOME)))))).then(Commands.literal("poi").then(Commands.argument("poi", ResourceOrTagArgument.resourceOrTag(commandBuildContext1, Registries.POINT_OF_INTEREST_TYPE)).executes(p_258234_ -> locatePoi((CommandSourceStack) p_258234_.getSource(), ResourceOrTagArgument.getResourceOrTag(p_258234_, "poi", Registries.POINT_OF_INTEREST_TYPE))))));
    }

    private static Optional<? extends HolderSet.ListBacked<Structure>> getHolders(ResourceOrTagKeyArgument.Result<Structure> resourceOrTagKeyArgumentResultStructure0, Registry<Structure> registryStructure1) {
        return (Optional<? extends HolderSet.ListBacked<Structure>>) resourceOrTagKeyArgumentResultStructure0.unwrap().map(p_258231_ -> registryStructure1.getHolder(p_258231_).map(p_214491_ -> HolderSet.direct(p_214491_)), registryStructure1::m_203431_);
    }

    private static int locateStructure(CommandSourceStack commandSourceStack0, ResourceOrTagKeyArgument.Result<Structure> resourceOrTagKeyArgumentResultStructure1) throws CommandSyntaxException {
        Registry<Structure> $$2 = commandSourceStack0.getLevel().m_9598_().registryOrThrow(Registries.STRUCTURE);
        HolderSet<Structure> $$3 = (HolderSet<Structure>) getHolders(resourceOrTagKeyArgumentResultStructure1, $$2).orElseThrow(() -> ERROR_STRUCTURE_INVALID.create(resourceOrTagKeyArgumentResultStructure1.asPrintable()));
        BlockPos $$4 = BlockPos.containing(commandSourceStack0.getPosition());
        ServerLevel $$5 = commandSourceStack0.getLevel();
        Stopwatch $$6 = Stopwatch.createStarted(Util.TICKER);
        Pair<BlockPos, Holder<Structure>> $$7 = $$5.getChunkSource().getGenerator().findNearestMapStructure($$5, $$3, $$4, 100, false);
        $$6.stop();
        if ($$7 == null) {
            throw ERROR_STRUCTURE_NOT_FOUND.create(resourceOrTagKeyArgumentResultStructure1.asPrintable());
        } else {
            return showLocateResult(commandSourceStack0, resourceOrTagKeyArgumentResultStructure1, $$4, $$7, "commands.locate.structure.success", false, $$6.elapsed());
        }
    }

    private static int locateBiome(CommandSourceStack commandSourceStack0, ResourceOrTagArgument.Result<Biome> resourceOrTagArgumentResultBiome1) throws CommandSyntaxException {
        BlockPos $$2 = BlockPos.containing(commandSourceStack0.getPosition());
        Stopwatch $$3 = Stopwatch.createStarted(Util.TICKER);
        Pair<BlockPos, Holder<Biome>> $$4 = commandSourceStack0.getLevel().findClosestBiome3d(resourceOrTagArgumentResultBiome1, $$2, 6400, 32, 64);
        $$3.stop();
        if ($$4 == null) {
            throw ERROR_BIOME_NOT_FOUND.create(resourceOrTagArgumentResultBiome1.asPrintable());
        } else {
            return showLocateResult(commandSourceStack0, resourceOrTagArgumentResultBiome1, $$2, $$4, "commands.locate.biome.success", true, $$3.elapsed());
        }
    }

    private static int locatePoi(CommandSourceStack commandSourceStack0, ResourceOrTagArgument.Result<PoiType> resourceOrTagArgumentResultPoiType1) throws CommandSyntaxException {
        BlockPos $$2 = BlockPos.containing(commandSourceStack0.getPosition());
        ServerLevel $$3 = commandSourceStack0.getLevel();
        Stopwatch $$4 = Stopwatch.createStarted(Util.TICKER);
        Optional<Pair<Holder<PoiType>, BlockPos>> $$5 = $$3.getPoiManager().findClosestWithType(resourceOrTagArgumentResultPoiType1, $$2, 256, PoiManager.Occupancy.ANY);
        $$4.stop();
        if ($$5.isEmpty()) {
            throw ERROR_POI_NOT_FOUND.create(resourceOrTagArgumentResultPoiType1.asPrintable());
        } else {
            return showLocateResult(commandSourceStack0, resourceOrTagArgumentResultPoiType1, $$2, ((Pair) $$5.get()).swap(), "commands.locate.poi.success", false, $$4.elapsed());
        }
    }

    private static String getElementName(Pair<BlockPos, ? extends Holder<?>> pairBlockPosExtendsHolder0) {
        return (String) ((Holder) pairBlockPosExtendsHolder0.getSecond()).unwrapKey().map(p_214498_ -> p_214498_.location().toString()).orElse("[unregistered]");
    }

    public static int showLocateResult(CommandSourceStack commandSourceStack0, ResourceOrTagArgument.Result<?> resourceOrTagArgumentResult1, BlockPos blockPos2, Pair<BlockPos, ? extends Holder<?>> pairBlockPosExtendsHolder3, String string4, boolean boolean5, Duration duration6) {
        String $$7 = (String) resourceOrTagArgumentResult1.unwrap().map(p_248147_ -> resourceOrTagArgumentResult1.asPrintable(), p_248143_ -> resourceOrTagArgumentResult1.asPrintable() + " (" + getElementName(pairBlockPosExtendsHolder3) + ")");
        return showLocateResult(commandSourceStack0, blockPos2, pairBlockPosExtendsHolder3, string4, boolean5, $$7, duration6);
    }

    public static int showLocateResult(CommandSourceStack commandSourceStack0, ResourceOrTagKeyArgument.Result<?> resourceOrTagKeyArgumentResult1, BlockPos blockPos2, Pair<BlockPos, ? extends Holder<?>> pairBlockPosExtendsHolder3, String string4, boolean boolean5, Duration duration6) {
        String $$7 = (String) resourceOrTagKeyArgumentResult1.unwrap().map(p_214463_ -> p_214463_.location().toString(), p_248145_ -> "#" + p_248145_.location() + " (" + getElementName(pairBlockPosExtendsHolder3) + ")");
        return showLocateResult(commandSourceStack0, blockPos2, pairBlockPosExtendsHolder3, string4, boolean5, $$7, duration6);
    }

    private static int showLocateResult(CommandSourceStack commandSourceStack0, BlockPos blockPos1, Pair<BlockPos, ? extends Holder<?>> pairBlockPosExtendsHolder2, String string3, boolean boolean4, String string5, Duration duration6) {
        BlockPos $$7 = (BlockPos) pairBlockPosExtendsHolder2.getFirst();
        int $$8 = boolean4 ? Mth.floor(Mth.sqrt((float) blockPos1.m_123331_($$7))) : Mth.floor(dist(blockPos1.m_123341_(), blockPos1.m_123343_(), $$7.m_123341_(), $$7.m_123343_()));
        String $$9 = boolean4 ? String.valueOf($$7.m_123342_()) : "~";
        Component $$10 = ComponentUtils.wrapInSquareBrackets(Component.translatable("chat.coordinates", $$7.m_123341_(), $$9, $$7.m_123343_())).withStyle(p_214489_ -> p_214489_.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + $$7.m_123341_() + " " + $$9 + " " + $$7.m_123343_())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.coordinates.tooltip"))));
        commandSourceStack0.sendSuccess(() -> Component.translatable(string3, string5, $$10, $$8), false);
        LOGGER.info("Locating element " + string5 + " took " + duration6.toMillis() + " ms");
        return $$8;
    }

    private static float dist(int int0, int int1, int int2, int int3) {
        int $$4 = int2 - int0;
        int $$5 = int3 - int1;
        return Mth.sqrt((float) ($$4 * $$4 + $$5 * $$5));
    }
}