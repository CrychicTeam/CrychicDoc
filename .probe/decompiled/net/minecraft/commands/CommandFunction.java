package net.minecraft.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerFunctionManager;

public class CommandFunction {

    private final CommandFunction.Entry[] entries;

    final ResourceLocation id;

    public CommandFunction(ResourceLocation resourceLocation0, CommandFunction.Entry[] commandFunctionEntry1) {
        this.id = resourceLocation0;
        this.entries = commandFunctionEntry1;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public CommandFunction.Entry[] getEntries() {
        return this.entries;
    }

    public static CommandFunction fromLines(ResourceLocation resourceLocation0, CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack1, CommandSourceStack commandSourceStack2, List<String> listString3) {
        List<CommandFunction.Entry> $$4 = Lists.newArrayListWithCapacity(listString3.size());
        for (int $$5 = 0; $$5 < listString3.size(); $$5++) {
            int $$6 = $$5 + 1;
            String $$7 = ((String) listString3.get($$5)).trim();
            StringReader $$8 = new StringReader($$7);
            if ($$8.canRead() && $$8.peek() != '#') {
                if ($$8.peek() == '/') {
                    $$8.skip();
                    if ($$8.peek() == '/') {
                        throw new IllegalArgumentException("Unknown or invalid command '" + $$7 + "' on line " + $$6 + " (if you intended to make a comment, use '#' not '//')");
                    }
                    String $$9 = $$8.readUnquotedString();
                    throw new IllegalArgumentException("Unknown or invalid command '" + $$7 + "' on line " + $$6 + " (did you mean '" + $$9 + "'? Do not use a preceding forwards slash.)");
                }
                try {
                    ParseResults<CommandSourceStack> $$10 = commandDispatcherCommandSourceStack1.parse($$8, commandSourceStack2);
                    if ($$10.getReader().canRead()) {
                        throw Commands.getParseException($$10);
                    }
                    $$4.add(new CommandFunction.CommandEntry($$10));
                } catch (CommandSyntaxException var10) {
                    throw new IllegalArgumentException("Whilst parsing command on line " + $$6 + ": " + var10.getMessage());
                }
            }
        }
        return new CommandFunction(resourceLocation0, (CommandFunction.Entry[]) $$4.toArray(new CommandFunction.Entry[0]));
    }

    public static class CacheableFunction {

        public static final CommandFunction.CacheableFunction NONE = new CommandFunction.CacheableFunction((ResourceLocation) null);

        @Nullable
        private final ResourceLocation id;

        private boolean resolved;

        private Optional<CommandFunction> function = Optional.empty();

        public CacheableFunction(@Nullable ResourceLocation resourceLocation0) {
            this.id = resourceLocation0;
        }

        public CacheableFunction(CommandFunction commandFunction0) {
            this.resolved = true;
            this.id = null;
            this.function = Optional.of(commandFunction0);
        }

        public Optional<CommandFunction> get(ServerFunctionManager serverFunctionManager0) {
            if (!this.resolved) {
                if (this.id != null) {
                    this.function = serverFunctionManager0.get(this.id);
                }
                this.resolved = true;
            }
            return this.function;
        }

        @Nullable
        public ResourceLocation getId() {
            return (ResourceLocation) this.function.map(p_78001_ -> p_78001_.id).orElse(this.id);
        }
    }

    public static class CommandEntry implements CommandFunction.Entry {

        private final ParseResults<CommandSourceStack> parse;

        public CommandEntry(ParseResults<CommandSourceStack> parseResultsCommandSourceStack0) {
            this.parse = parseResultsCommandSourceStack0;
        }

        @Override
        public void execute(ServerFunctionManager serverFunctionManager0, CommandSourceStack commandSourceStack1, Deque<ServerFunctionManager.QueuedCommand> dequeServerFunctionManagerQueuedCommand2, int int3, int int4, @Nullable ServerFunctionManager.TraceCallbacks serverFunctionManagerTraceCallbacks5) throws CommandSyntaxException {
            if (serverFunctionManagerTraceCallbacks5 != null) {
                String $$6 = this.parse.getReader().getString();
                serverFunctionManagerTraceCallbacks5.onCommand(int4, $$6);
                int $$7 = this.execute(serverFunctionManager0, commandSourceStack1);
                serverFunctionManagerTraceCallbacks5.onReturn(int4, $$6, $$7);
            } else {
                this.execute(serverFunctionManager0, commandSourceStack1);
            }
        }

        private int execute(ServerFunctionManager serverFunctionManager0, CommandSourceStack commandSourceStack1) throws CommandSyntaxException {
            return serverFunctionManager0.getDispatcher().execute(Commands.mapSource(this.parse, p_242934_ -> commandSourceStack1));
        }

        public String toString() {
            return this.parse.getReader().getString();
        }
    }

    @FunctionalInterface
    public interface Entry {

        void execute(ServerFunctionManager var1, CommandSourceStack var2, Deque<ServerFunctionManager.QueuedCommand> var3, int var4, int var5, @Nullable ServerFunctionManager.TraceCallbacks var6) throws CommandSyntaxException;
    }

    public static class FunctionEntry implements CommandFunction.Entry {

        private final CommandFunction.CacheableFunction function;

        public FunctionEntry(CommandFunction commandFunction0) {
            this.function = new CommandFunction.CacheableFunction(commandFunction0);
        }

        @Override
        public void execute(ServerFunctionManager serverFunctionManager0, CommandSourceStack commandSourceStack1, Deque<ServerFunctionManager.QueuedCommand> dequeServerFunctionManagerQueuedCommand2, int int3, int int4, @Nullable ServerFunctionManager.TraceCallbacks serverFunctionManagerTraceCallbacks5) {
            Util.ifElse(this.function.get(serverFunctionManager0), p_164900_ -> {
                CommandFunction.Entry[] $$6 = p_164900_.getEntries();
                if (serverFunctionManagerTraceCallbacks5 != null) {
                    serverFunctionManagerTraceCallbacks5.onCall(int4, p_164900_.getId(), $$6.length);
                }
                int $$7 = int3 - dequeServerFunctionManagerQueuedCommand2.size();
                int $$8 = Math.min($$6.length, $$7);
                for (int $$9 = $$8 - 1; $$9 >= 0; $$9--) {
                    dequeServerFunctionManagerQueuedCommand2.addFirst(new ServerFunctionManager.QueuedCommand(commandSourceStack1, int4 + 1, $$6[$$9]));
                }
            }, () -> {
                if (serverFunctionManagerTraceCallbacks5 != null) {
                    serverFunctionManagerTraceCallbacks5.onCall(int4, this.function.getId(), -1);
                }
            });
        }

        public String toString() {
            return "function " + this.function.getId();
        }
    }
}