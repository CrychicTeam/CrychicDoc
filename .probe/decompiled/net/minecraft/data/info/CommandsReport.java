package net.minecraft.data.info;

import com.mojang.brigadier.CommandDispatcher;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.ArgumentUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

public class CommandsReport implements DataProvider {

    private final PackOutput output;

    private final CompletableFuture<HolderLookup.Provider> registries;

    public CommandsReport(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1) {
        this.output = packOutput0;
        this.registries = completableFutureHolderLookupProvider1;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        Path $$1 = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("commands.json");
        return this.registries.thenCompose(p_256367_ -> {
            CommandDispatcher<CommandSourceStack> $$3 = new Commands(Commands.CommandSelection.ALL, Commands.createValidationContext(p_256367_)).getDispatcher();
            return DataProvider.saveStable(cachedOutput0, ArgumentUtils.serializeNodeToJson($$3, $$3.getRoot()), $$1);
        });
    }

    @Override
    public final String getName() {
        return "Command Syntax";
    }
}