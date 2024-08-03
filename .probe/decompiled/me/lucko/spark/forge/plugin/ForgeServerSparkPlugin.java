package me.lucko.spark.forge.plugin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.lucko.spark.common.monitor.ping.PlayerPingProvider;
import me.lucko.spark.common.platform.MetadataProvider;
import me.lucko.spark.common.platform.PlatformInfo;
import me.lucko.spark.common.platform.serverconfig.ServerConfigProvider;
import me.lucko.spark.common.platform.world.WorldInfoProvider;
import me.lucko.spark.common.sampler.ThreadDumper;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.common.tick.TickReporter;
import me.lucko.spark.forge.ForgeCommandSender;
import me.lucko.spark.forge.ForgeExtraMetadataProvider;
import me.lucko.spark.forge.ForgePlatformInfo;
import me.lucko.spark.forge.ForgePlayerPingProvider;
import me.lucko.spark.forge.ForgeServerConfigProvider;
import me.lucko.spark.forge.ForgeSparkMod;
import me.lucko.spark.forge.ForgeTickHook;
import me.lucko.spark.forge.ForgeTickReporter;
import me.lucko.spark.forge.ForgeWorldInfoProvider;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

public class ForgeServerSparkPlugin extends ForgeSparkPlugin implements Command<CommandSourceStack>, SuggestionProvider<CommandSourceStack> {

    private static final PermissionNode.PermissionResolver<Boolean> DEFAULT_PERMISSION_VALUE = (player, playerUUID, context) -> {
        if (player == null) {
            return false;
        } else {
            MinecraftServer server = player.m_20194_();
            return server != null && server.isSingleplayerOwner(player.m_36316_()) ? true : player.m_20310_(4);
        }
    };

    private final MinecraftServer server;

    private final ThreadDumper gameThreadDumper;

    private Map<String, PermissionNode<Boolean>> registeredPermissions = Collections.emptyMap();

    public static void register(ForgeSparkMod mod, ServerAboutToStartEvent event) {
        ForgeServerSparkPlugin plugin = new ForgeServerSparkPlugin(mod, event.getServer());
        plugin.enable();
    }

    public ForgeServerSparkPlugin(ForgeSparkMod mod, MinecraftServer server) {
        super(mod);
        this.server = server;
        this.gameThreadDumper = new ThreadDumper.Specific(server.getRunningThread());
    }

    @Override
    public void enable() {
        super.enable();
        this.registerCommands(this.server.getCommands().getDispatcher());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void disable() {
        super.disable();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onDisable(ServerStoppingEvent event) {
        if (event.getServer() == this.server) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onPermissionGather(PermissionGatherEvent.Nodes e) {
        List<String> permissions = (List<String>) this.platform.getCommands().stream().map(me.lucko.spark.common.command.Command::primaryAlias).collect(Collectors.toList());
        permissions.add("all");
        Builder<String, PermissionNode<Boolean>> builder = ImmutableMap.builder();
        Map<String, PermissionNode<?>> alreadyRegistered = (Map<String, PermissionNode<?>>) e.getNodes().stream().collect(Collectors.toMap(PermissionNode::getNodeName, Function.identity()));
        for (String permission : permissions) {
            String permissionString = "spark." + permission;
            PermissionNode<?> existing = (PermissionNode<?>) alreadyRegistered.get(permissionString);
            if (existing != null) {
                builder.put(permissionString, existing);
            } else {
                PermissionNode<Boolean> node = new PermissionNode<>("spark", permission, PermissionTypes.BOOLEAN, DEFAULT_PERMISSION_VALUE);
                e.addNodes(node);
                builder.put(permissionString, node);
            }
        }
        this.registeredPermissions = builder.build();
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent e) {
        this.registerCommands(e.getDispatcher());
    }

    private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        registerCommands(dispatcher, this, this, new String[] { "spark" });
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String[] args = processArgs(context, false, new String[] { "/spark", "spark" });
        if (args == null) {
            return 0;
        } else {
            CommandSource source = (CommandSource) (((CommandSourceStack) context.getSource()).getEntity() != null ? ((CommandSourceStack) context.getSource()).getEntity() : ((CommandSourceStack) context.getSource()).getServer());
            this.platform.executeCommand(new ForgeCommandSender(source, this), args);
            return 1;
        }
    }

    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        String[] args = processArgs(context, true, new String[] { "/spark", "spark" });
        return args == null ? Suggestions.empty() : this.generateSuggestions(new ForgeCommandSender(((CommandSourceStack) context.getSource()).getPlayerOrException(), this), args, builder);
    }

    @Override
    public boolean hasPermission(CommandSource sender, String permission) {
        if (sender instanceof ServerPlayer) {
            if (permission.equals("spark")) {
                permission = "spark.all";
            }
            PermissionNode<Boolean> permissionNode = (PermissionNode<Boolean>) this.registeredPermissions.get(permission);
            if (permissionNode == null) {
                throw new IllegalStateException("spark permission not registered: " + permission);
            } else {
                return PermissionAPI.getPermission((ServerPlayer) sender, permissionNode);
            }
        } else {
            return true;
        }
    }

    @Override
    public Stream<ForgeCommandSender> getCommandSenders() {
        return Stream.concat(this.server.getPlayerList().getPlayers().stream(), Stream.of(this.server)).map(sender -> new ForgeCommandSender(sender, this));
    }

    @Override
    public void executeSync(Runnable task) {
        this.server.executeIfPossible(task);
    }

    @Override
    public ThreadDumper getDefaultThreadDumper() {
        return this.gameThreadDumper;
    }

    @Override
    public TickHook createTickHook() {
        return new ForgeTickHook(TickEvent.Type.SERVER);
    }

    @Override
    public TickReporter createTickReporter() {
        return new ForgeTickReporter(TickEvent.Type.SERVER);
    }

    @Override
    public PlayerPingProvider createPlayerPingProvider() {
        return new ForgePlayerPingProvider(this.server);
    }

    @Override
    public ServerConfigProvider createServerConfigProvider() {
        return new ForgeServerConfigProvider();
    }

    @Override
    public MetadataProvider createExtraMetadataProvider() {
        return new ForgeExtraMetadataProvider(this.server.getPackRepository());
    }

    @Override
    public WorldInfoProvider createWorldInfoProvider() {
        return new ForgeWorldInfoProvider.Server(this.server);
    }

    @Override
    public PlatformInfo getPlatformInfo() {
        return new ForgePlatformInfo(PlatformInfo.Type.SERVER);
    }

    @Override
    public String getCommandName() {
        return "spark";
    }
}