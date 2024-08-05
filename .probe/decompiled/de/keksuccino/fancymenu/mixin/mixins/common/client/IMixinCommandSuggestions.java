package de.keksuccino.fancymenu.mixin.mixins.common.client;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ CommandSuggestions.class })
public interface IMixinCommandSuggestions {

    @Accessor("allowSuggestions")
    boolean getAllowSuggestionsFancyMenu();

    @Accessor("keepSuggestions")
    boolean getKeepSuggestionsFancyMenu();

    @Accessor("currentParse")
    @Nullable
    ParseResults<SharedSuggestionProvider> getCurrentParseFancyMenu();

    @Accessor("currentParse")
    void setCurrentParseFancyMenu(ParseResults<SharedSuggestionProvider> var1);

    @Accessor("pendingSuggestions")
    @Nullable
    CompletableFuture<Suggestions> getPendingSuggestionsFancyMenu();

    @Accessor("pendingSuggestions")
    void setPendingSuggestionsFancyMenu(CompletableFuture<Suggestions> var1);

    @Accessor("commandUsage")
    List<FormattedCharSequence> getCommandUsageFancyMenu();

    @Accessor("suggestions")
    @Nullable
    CommandSuggestions.SuggestionsList getSuggestionsFancyMenu();

    @Accessor("suggestions")
    void setSuggestionsFancyMenu(CommandSuggestions.SuggestionsList var1);

    @Invoker("updateUsageInfo")
    void invokeUpdateUsageInfoFancyMenu();

    @Invoker("sortSuggestions")
    List<Suggestion> invokeSortSuggestionsFancyMenu(Suggestions var1);
}