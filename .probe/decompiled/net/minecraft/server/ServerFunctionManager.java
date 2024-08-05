package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;

public class ServerFunctionManager {

    private static final Component NO_RECURSIVE_TRACES = Component.translatable("commands.debug.function.noRecursion");

    private static final ResourceLocation TICK_FUNCTION_TAG = new ResourceLocation("tick");

    private static final ResourceLocation LOAD_FUNCTION_TAG = new ResourceLocation("load");

    final MinecraftServer server;

    @Nullable
    private ServerFunctionManager.ExecutionContext context;

    private List<CommandFunction> ticking = ImmutableList.of();

    private boolean postReload;

    private ServerFunctionLibrary library;

    public ServerFunctionManager(MinecraftServer minecraftServer0, ServerFunctionLibrary serverFunctionLibrary1) {
        this.server = minecraftServer0;
        this.library = serverFunctionLibrary1;
        this.postReload(serverFunctionLibrary1);
    }

    public int getCommandLimit() {
        return this.server.getGameRules().getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH);
    }

    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        return this.server.getCommands().getDispatcher();
    }

    public void tick() {
        if (this.postReload) {
            this.postReload = false;
            Collection<CommandFunction> $$0 = this.library.getTag(LOAD_FUNCTION_TAG);
            this.executeTagFunctions($$0, LOAD_FUNCTION_TAG);
        }
        this.executeTagFunctions(this.ticking, TICK_FUNCTION_TAG);
    }

    private void executeTagFunctions(Collection<CommandFunction> collectionCommandFunction0, ResourceLocation resourceLocation1) {
        this.server.getProfiler().push(resourceLocation1::toString);
        for (CommandFunction $$2 : collectionCommandFunction0) {
            this.execute($$2, this.getGameLoopSender());
        }
        this.server.getProfiler().pop();
    }

    public int execute(CommandFunction commandFunction0, CommandSourceStack commandSourceStack1) {
        return this.execute(commandFunction0, commandSourceStack1, null);
    }

    public int execute(CommandFunction commandFunction0, CommandSourceStack commandSourceStack1, @Nullable ServerFunctionManager.TraceCallbacks serverFunctionManagerTraceCallbacks2) {
        if (this.context != null) {
            if (serverFunctionManagerTraceCallbacks2 != null) {
                this.context.reportError(NO_RECURSIVE_TRACES.getString());
                return 0;
            } else {
                this.context.delayFunctionCall(commandFunction0, commandSourceStack1);
                return 0;
            }
        } else {
            int var4;
            try {
                this.context = new ServerFunctionManager.ExecutionContext(serverFunctionManagerTraceCallbacks2);
                var4 = this.context.runTopCommand(commandFunction0, commandSourceStack1);
            } finally {
                this.context = null;
            }
            return var4;
        }
    }

    public void replaceLibrary(ServerFunctionLibrary serverFunctionLibrary0) {
        this.library = serverFunctionLibrary0;
        this.postReload(serverFunctionLibrary0);
    }

    private void postReload(ServerFunctionLibrary serverFunctionLibrary0) {
        this.ticking = ImmutableList.copyOf(serverFunctionLibrary0.getTag(TICK_FUNCTION_TAG));
        this.postReload = true;
    }

    public CommandSourceStack getGameLoopSender() {
        return this.server.createCommandSourceStack().withPermission(2).withSuppressedOutput();
    }

    public Optional<CommandFunction> get(ResourceLocation resourceLocation0) {
        return this.library.getFunction(resourceLocation0);
    }

    public Collection<CommandFunction> getTag(ResourceLocation resourceLocation0) {
        return this.library.getTag(resourceLocation0);
    }

    public Iterable<ResourceLocation> getFunctionNames() {
        return this.library.getFunctions().keySet();
    }

    public Iterable<ResourceLocation> getTagNames() {
        return this.library.getAvailableTags();
    }

    class ExecutionContext {

        private int depth;

        @Nullable
        private final ServerFunctionManager.TraceCallbacks tracer;

        private final Deque<ServerFunctionManager.QueuedCommand> commandQueue = Queues.newArrayDeque();

        private final List<ServerFunctionManager.QueuedCommand> nestedCalls = Lists.newArrayList();

        boolean abortCurrentDepth = false;

        ExecutionContext(ServerFunctionManager.TraceCallbacks serverFunctionManagerTraceCallbacks0) {
            this.tracer = serverFunctionManagerTraceCallbacks0;
        }

        void delayFunctionCall(CommandFunction commandFunction0, CommandSourceStack commandSourceStack1) {
            int $$2 = ServerFunctionManager.this.getCommandLimit();
            CommandSourceStack $$3 = this.wrapSender(commandSourceStack1);
            if (this.commandQueue.size() + this.nestedCalls.size() < $$2) {
                this.nestedCalls.add(new ServerFunctionManager.QueuedCommand($$3, this.depth, new CommandFunction.FunctionEntry(commandFunction0)));
            }
        }

        private CommandSourceStack wrapSender(CommandSourceStack commandSourceStack0) {
            IntConsumer $$1 = commandSourceStack0.getReturnValueConsumer();
            return $$1 instanceof ServerFunctionManager.ExecutionContext.AbortingReturnValueConsumer ? commandSourceStack0 : commandSourceStack0.withReturnValueConsumer(new ServerFunctionManager.ExecutionContext.AbortingReturnValueConsumer($$1));
        }

        int runTopCommand(CommandFunction commandFunction0, CommandSourceStack commandSourceStack1) {
            int $$2 = ServerFunctionManager.this.getCommandLimit();
            CommandSourceStack $$3 = this.wrapSender(commandSourceStack1);
            int $$4 = 0;
            CommandFunction.Entry[] $$5 = commandFunction0.getEntries();
            for (int $$6 = $$5.length - 1; $$6 >= 0; $$6--) {
                this.commandQueue.push(new ServerFunctionManager.QueuedCommand($$3, 0, $$5[$$6]));
            }
            while (!this.commandQueue.isEmpty()) {
                try {
                    ServerFunctionManager.QueuedCommand $$7 = (ServerFunctionManager.QueuedCommand) this.commandQueue.removeFirst();
                    ServerFunctionManager.this.server.getProfiler().push($$7::toString);
                    this.depth = $$7.depth;
                    $$7.execute(ServerFunctionManager.this, this.commandQueue, $$2, this.tracer);
                    if (!this.abortCurrentDepth) {
                        if (!this.nestedCalls.isEmpty()) {
                            Lists.reverse(this.nestedCalls).forEach(this.commandQueue::addFirst);
                        }
                    } else {
                        while (!this.commandQueue.isEmpty() && ((ServerFunctionManager.QueuedCommand) this.commandQueue.peek()).depth >= this.depth) {
                            this.commandQueue.removeFirst();
                        }
                        this.abortCurrentDepth = false;
                    }
                    this.nestedCalls.clear();
                } finally {
                    ServerFunctionManager.this.server.getProfiler().pop();
                }
                if (++$$4 >= $$2) {
                    return $$4;
                }
            }
            return $$4;
        }

        public void reportError(String string0) {
            if (this.tracer != null) {
                this.tracer.onError(this.depth, string0);
            }
        }

        class AbortingReturnValueConsumer implements IntConsumer {

            private final IntConsumer wrapped;

            AbortingReturnValueConsumer(IntConsumer intConsumer0) {
                this.wrapped = intConsumer0;
            }

            public void accept(int int0) {
                this.wrapped.accept(int0);
                ExecutionContext.this.abortCurrentDepth = true;
            }
        }
    }

    public static class QueuedCommand {

        private final CommandSourceStack sender;

        final int depth;

        private final CommandFunction.Entry entry;

        public QueuedCommand(CommandSourceStack commandSourceStack0, int int1, CommandFunction.Entry commandFunctionEntry2) {
            this.sender = commandSourceStack0;
            this.depth = int1;
            this.entry = commandFunctionEntry2;
        }

        public void execute(ServerFunctionManager serverFunctionManager0, Deque<ServerFunctionManager.QueuedCommand> dequeServerFunctionManagerQueuedCommand1, int int2, @Nullable ServerFunctionManager.TraceCallbacks serverFunctionManagerTraceCallbacks3) {
            try {
                this.entry.execute(serverFunctionManager0, this.sender, dequeServerFunctionManagerQueuedCommand1, int2, this.depth, serverFunctionManagerTraceCallbacks3);
            } catch (CommandSyntaxException var6) {
                if (serverFunctionManagerTraceCallbacks3 != null) {
                    serverFunctionManagerTraceCallbacks3.onError(this.depth, var6.getRawMessage().getString());
                }
            } catch (Exception var7) {
                if (serverFunctionManagerTraceCallbacks3 != null) {
                    serverFunctionManagerTraceCallbacks3.onError(this.depth, var7.getMessage());
                }
            }
        }

        public String toString() {
            return this.entry.toString();
        }
    }

    public interface TraceCallbacks {

        void onCommand(int var1, String var2);

        void onReturn(int var1, String var2, int var3);

        void onError(int var1, String var2);

        void onCall(int var1, ResourceLocation var2, int var3);
    }
}