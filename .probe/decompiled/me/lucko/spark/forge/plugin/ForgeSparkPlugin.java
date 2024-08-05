package me.lucko.spark.forge.plugin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.SparkPlugin;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import me.lucko.spark.common.sampler.source.SourceMetadata;
import me.lucko.spark.common.util.SparkThreadFactory;
import me.lucko.spark.forge.ForgeClassSourceLookup;
import me.lucko.spark.forge.ForgeSparkMod;
import net.minecraft.commands.CommandSource;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ForgeSparkPlugin implements SparkPlugin {

    private final ForgeSparkMod mod;

    private final Logger logger;

    protected final ScheduledExecutorService scheduler;

    protected SparkPlatform platform;

    protected ForgeSparkPlugin(ForgeSparkMod mod) {
        this.mod = mod;
        this.logger = LogManager.getLogger("spark");
        this.scheduler = Executors.newScheduledThreadPool(4, new SparkThreadFactory());
    }

    public void enable() {
        this.platform = new SparkPlatform(this);
        this.platform.enable();
    }

    public void disable() {
        this.platform.disable();
        this.scheduler.shutdown();
    }

    public abstract boolean hasPermission(CommandSource var1, String var2);

    @Override
    public String getVersion() {
        return this.mod.getVersion();
    }

    @Override
    public Path getPluginDirectory() {
        return this.mod.getConfigDirectory();
    }

    @Override
    public void executeAsync(Runnable task) {
        this.scheduler.execute(task);
    }

    @Override
    public void log(Level level, String msg) {
        if (level == Level.INFO) {
            this.logger.info(msg);
        } else if (level == Level.WARNING) {
            this.logger.warn(msg);
        } else {
            if (level != Level.SEVERE) {
                throw new IllegalArgumentException(level.getName());
            }
            this.logger.error(msg);
        }
    }

    @Override
    public ClassSourceLookup createClassSourceLookup() {
        return new ForgeClassSourceLookup();
    }

    @Override
    public Collection<SourceMetadata> getKnownSources() {
        return SourceMetadata.gather(ModList.get().getMods(), IModInfo::getModId, mod -> mod.getVersion().toString(), mod -> null);
    }

    protected CompletableFuture<Suggestions> generateSuggestions(CommandSender sender, String[] args, SuggestionsBuilder builder) {
        int lastSpaceIdx = builder.getRemaining().lastIndexOf(32);
        SuggestionsBuilder suggestions;
        if (lastSpaceIdx != -1) {
            suggestions = builder.createOffset(builder.getStart() + lastSpaceIdx + 1);
        } else {
            suggestions = builder;
        }
        return CompletableFuture.supplyAsync(() -> {
            for (String suggestion : this.platform.tabCompleteCommand(sender, args)) {
                suggestions.suggest(suggestion);
            }
            return suggestions.build();
        });
    }

    protected static <T> void registerCommands(CommandDispatcher<T> dispatcher, Command<T> executor, SuggestionProvider<T> suggestor, String... aliases) {
        if (aliases.length != 0) {
            String mainName = aliases[0];
            LiteralArgumentBuilder<T> command = (LiteralArgumentBuilder<T>) ((LiteralArgumentBuilder) LiteralArgumentBuilder.literal(mainName).executes(executor)).then(RequiredArgumentBuilder.argument("args", StringArgumentType.greedyString()).suggests(suggestor).executes(executor));
            LiteralCommandNode<T> node = dispatcher.register(command);
            for (int i = 1; i < aliases.length; i++) {
                dispatcher.register((LiteralArgumentBuilder) LiteralArgumentBuilder.literal(aliases[i]).redirect(node));
            }
        }
    }

    protected static String[] processArgs(CommandContext<?> context, boolean tabComplete, String... aliases) {
        String[] split = context.getInput().split(" ", tabComplete ? -1 : 0);
        return split.length != 0 && Arrays.asList(aliases).contains(split[0]) ? (String[]) Arrays.copyOfRange(split, 1, split.length) : null;
    }
}