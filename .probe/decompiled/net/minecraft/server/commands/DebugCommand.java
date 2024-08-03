package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Locale;
import net.minecraft.Util;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.profiling.ProfileResults;
import org.slf4j.Logger;

public class DebugCommand {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final SimpleCommandExceptionType ERROR_NOT_RUNNING = new SimpleCommandExceptionType(Component.translatable("commands.debug.notRunning"));

    private static final SimpleCommandExceptionType ERROR_ALREADY_RUNNING = new SimpleCommandExceptionType(Component.translatable("commands.debug.alreadyRunning"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("debug").requires(p_180073_ -> p_180073_.hasPermission(3))).then(Commands.literal("start").executes(p_180069_ -> start((CommandSourceStack) p_180069_.getSource())))).then(Commands.literal("stop").executes(p_136918_ -> stop((CommandSourceStack) p_136918_.getSource())))).then(((LiteralArgumentBuilder) Commands.literal("function").requires(p_180071_ -> p_180071_.hasPermission(3))).then(Commands.argument("name", FunctionArgument.functions()).suggests(FunctionCommand.SUGGEST_FUNCTION).executes(p_136908_ -> traceFunction((CommandSourceStack) p_136908_.getSource(), FunctionArgument.getFunctions(p_136908_, "name"))))));
    }

    private static int start(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        MinecraftServer $$1 = commandSourceStack0.getServer();
        if ($$1.isTimeProfilerRunning()) {
            throw ERROR_ALREADY_RUNNING.create();
        } else {
            $$1.startTimeProfiler();
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.debug.started"), true);
            return 0;
        }
    }

    private static int stop(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        MinecraftServer $$1 = commandSourceStack0.getServer();
        if (!$$1.isTimeProfilerRunning()) {
            throw ERROR_NOT_RUNNING.create();
        } else {
            ProfileResults $$2 = $$1.stopTimeProfiler();
            double $$3 = (double) $$2.getNanoDuration() / (double) TimeUtil.NANOSECONDS_PER_SECOND;
            double $$4 = (double) $$2.getTickDuration() / $$3;
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.debug.stopped", String.format(Locale.ROOT, "%.2f", $$3), $$2.getTickDuration(), String.format(Locale.ROOT, "%.2f", $$4)), true);
            return (int) $$4;
        }
    }

    private static int traceFunction(CommandSourceStack commandSourceStack0, Collection<CommandFunction> collectionCommandFunction1) {
        int $$2 = 0;
        MinecraftServer $$3 = commandSourceStack0.getServer();
        String $$4 = "debug-trace-" + Util.getFilenameFormattedDateTime() + ".txt";
        try {
            Path $$5 = $$3.getFile("debug").toPath();
            Files.createDirectories($$5);
            Writer $$6 = Files.newBufferedWriter($$5.resolve($$4), StandardCharsets.UTF_8);
            try {
                PrintWriter $$7 = new PrintWriter($$6);
                for (CommandFunction $$8 : collectionCommandFunction1) {
                    $$7.println($$8.getId());
                    DebugCommand.Tracer $$9 = new DebugCommand.Tracer($$7);
                    $$2 += commandSourceStack0.getServer().getFunctions().execute($$8, commandSourceStack0.withSource($$9).withMaximumPermission(2), $$9);
                }
            } catch (Throwable var12) {
                if ($$6 != null) {
                    try {
                        $$6.close();
                    } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                    }
                }
                throw var12;
            }
            if ($$6 != null) {
                $$6.close();
            }
        } catch (IOException | UncheckedIOException var13) {
            LOGGER.warn("Tracing failed", var13);
            commandSourceStack0.sendFailure(Component.translatable("commands.debug.function.traceFailed"));
        }
        int $$11 = $$2;
        if (collectionCommandFunction1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.debug.function.success.single", $$11, ((CommandFunction) collectionCommandFunction1.iterator().next()).getId(), $$4), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.debug.function.success.multiple", $$11, collectionCommandFunction1.size(), $$4), true);
        }
        return $$2;
    }

    static class Tracer implements ServerFunctionManager.TraceCallbacks, CommandSource {

        public static final int INDENT_OFFSET = 1;

        private final PrintWriter output;

        private int lastIndent;

        private boolean waitingForResult;

        Tracer(PrintWriter printWriter0) {
            this.output = printWriter0;
        }

        private void indentAndSave(int int0) {
            this.printIndent(int0);
            this.lastIndent = int0;
        }

        private void printIndent(int int0) {
            for (int $$1 = 0; $$1 < int0 + 1; $$1++) {
                this.output.write("    ");
            }
        }

        private void newLine() {
            if (this.waitingForResult) {
                this.output.println();
                this.waitingForResult = false;
            }
        }

        @Override
        public void onCommand(int int0, String string1) {
            this.newLine();
            this.indentAndSave(int0);
            this.output.print("[C] ");
            this.output.print(string1);
            this.waitingForResult = true;
        }

        @Override
        public void onReturn(int int0, String string1, int int2) {
            if (this.waitingForResult) {
                this.output.print(" -> ");
                this.output.println(int2);
                this.waitingForResult = false;
            } else {
                this.indentAndSave(int0);
                this.output.print("[R = ");
                this.output.print(int2);
                this.output.print("] ");
                this.output.println(string1);
            }
        }

        @Override
        public void onCall(int int0, ResourceLocation resourceLocation1, int int2) {
            this.newLine();
            this.indentAndSave(int0);
            this.output.print("[F] ");
            this.output.print(resourceLocation1);
            this.output.print(" size=");
            this.output.println(int2);
        }

        @Override
        public void onError(int int0, String string1) {
            this.newLine();
            this.indentAndSave(int0 + 1);
            this.output.print("[E] ");
            this.output.print(string1);
        }

        @Override
        public void sendSystemMessage(Component component0) {
            this.newLine();
            this.printIndent(this.lastIndent + 1);
            this.output.print("[M] ");
            this.output.println(component0.getString());
        }

        @Override
        public boolean acceptsSuccess() {
            return true;
        }

        @Override
        public boolean acceptsFailure() {
            return true;
        }

        @Override
        public boolean shouldInformAdmins() {
            return false;
        }

        @Override
        public boolean alwaysAccepts() {
            return true;
        }
    }
}