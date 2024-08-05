package me.lucko.spark.forge.plugin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import me.lucko.spark.common.platform.MetadataProvider;
import me.lucko.spark.common.platform.PlatformInfo;
import me.lucko.spark.common.platform.world.WorldInfoProvider;
import me.lucko.spark.common.sampler.ThreadDumper;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.common.tick.TickReporter;
import me.lucko.spark.forge.ForgeCommandSender;
import me.lucko.spark.forge.ForgeExtraMetadataProvider;
import me.lucko.spark.forge.ForgePlatformInfo;
import me.lucko.spark.forge.ForgeSparkMod;
import me.lucko.spark.forge.ForgeTickHook;
import me.lucko.spark.forge.ForgeTickReporter;
import me.lucko.spark.forge.ForgeWorldInfoProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ForgeClientSparkPlugin extends ForgeSparkPlugin implements Command<CommandSourceStack>, SuggestionProvider<CommandSourceStack> {

    private final Minecraft minecraft;

    private final ThreadDumper gameThreadDumper;

    public static void register(ForgeSparkMod mod, FMLClientSetupEvent event) {
        ForgeClientSparkPlugin plugin = new ForgeClientSparkPlugin(mod, Minecraft.getInstance());
        plugin.enable();
    }

    public ForgeClientSparkPlugin(ForgeSparkMod mod, Minecraft minecraft) {
        super(mod);
        this.minecraft = minecraft;
        this.gameThreadDumper = new ThreadDumper.Specific(minecraft.gameThread);
    }

    @Override
    public void enable() {
        super.enable();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterClientCommandsEvent e) {
        registerCommands(e.getDispatcher(), this, this, new String[] { "sparkc", "sparkclient" });
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String[] args = processArgs(context, false, new String[] { "sparkc", "sparkclient" });
        if (args == null) {
            return 0;
        } else {
            this.platform.executeCommand(new ForgeCommandSender(((CommandSourceStack) context.getSource()).getEntity(), this), args);
            return 1;
        }
    }

    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        String[] args = processArgs(context, true, new String[] { "/sparkc", "/sparkclient" });
        return args == null ? Suggestions.empty() : this.generateSuggestions(new ForgeCommandSender(((CommandSourceStack) context.getSource()).getEntity(), this), args, builder);
    }

    @Override
    public boolean hasPermission(CommandSource sender, String permission) {
        return true;
    }

    @Override
    public Stream<ForgeCommandSender> getCommandSenders() {
        return Stream.of(new ForgeCommandSender(this.minecraft.player, this));
    }

    @Override
    public void executeSync(Runnable task) {
        this.minecraft.m_201446_(task);
    }

    @Override
    public ThreadDumper getDefaultThreadDumper() {
        return this.gameThreadDumper;
    }

    @Override
    public TickHook createTickHook() {
        return new ForgeTickHook(TickEvent.Type.CLIENT);
    }

    @Override
    public TickReporter createTickReporter() {
        return new ForgeTickReporter(TickEvent.Type.CLIENT);
    }

    @Override
    public WorldInfoProvider createWorldInfoProvider() {
        return new ForgeWorldInfoProvider.Client(this.minecraft);
    }

    @Override
    public MetadataProvider createExtraMetadataProvider() {
        return new ForgeExtraMetadataProvider(this.minecraft.getResourcePackRepository());
    }

    @Override
    public PlatformInfo getPlatformInfo() {
        return new ForgePlatformInfo(PlatformInfo.Type.CLIENT);
    }

    @Override
    public String getCommandName() {
        return "sparkc";
    }
}