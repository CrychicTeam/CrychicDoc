package net.minecraft.client.multiplayer;

import com.google.common.collect.Lists;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ClientSuggestionProvider implements SharedSuggestionProvider {

    private final ClientPacketListener connection;

    private final Minecraft minecraft;

    private int pendingSuggestionsId = -1;

    @Nullable
    private CompletableFuture<Suggestions> pendingSuggestionsFuture;

    private final Set<String> customCompletionSuggestions = new HashSet();

    public ClientSuggestionProvider(ClientPacketListener clientPacketListener0, Minecraft minecraft1) {
        this.connection = clientPacketListener0;
        this.minecraft = minecraft1;
    }

    @Override
    public Collection<String> getOnlinePlayerNames() {
        List<String> $$0 = Lists.newArrayList();
        for (PlayerInfo $$1 : this.connection.getOnlinePlayers()) {
            $$0.add($$1.getProfile().getName());
        }
        return $$0;
    }

    @Override
    public Collection<String> getCustomTabSugggestions() {
        if (this.customCompletionSuggestions.isEmpty()) {
            return this.getOnlinePlayerNames();
        } else {
            Set<String> $$0 = new HashSet(this.getOnlinePlayerNames());
            $$0.addAll(this.customCompletionSuggestions);
            return $$0;
        }
    }

    @Override
    public Collection<String> getSelectedEntities() {
        return (Collection<String>) (this.minecraft.hitResult != null && this.minecraft.hitResult.getType() == HitResult.Type.ENTITY ? Collections.singleton(((EntityHitResult) this.minecraft.hitResult).getEntity().getStringUUID()) : Collections.emptyList());
    }

    @Override
    public Collection<String> getAllTeams() {
        return this.connection.getLevel().getScoreboard().getTeamNames();
    }

    @Override
    public Stream<ResourceLocation> getAvailableSounds() {
        return this.minecraft.getSoundManager().getAvailableSounds().stream();
    }

    @Override
    public Stream<ResourceLocation> getRecipeNames() {
        return this.connection.getRecipeManager().getRecipeIds();
    }

    @Override
    public boolean hasPermission(int int0) {
        LocalPlayer $$1 = this.minecraft.player;
        return $$1 != null ? $$1.m_20310_(int0) : int0 == 0;
    }

    @Override
    public CompletableFuture<Suggestions> suggestRegistryElements(ResourceKey<? extends Registry<?>> resourceKeyExtendsRegistry0, SharedSuggestionProvider.ElementSuggestionType sharedSuggestionProviderElementSuggestionType1, SuggestionsBuilder suggestionsBuilder2, CommandContext<?> commandContext3) {
        return (CompletableFuture<Suggestions>) this.registryAccess().registry(resourceKeyExtendsRegistry0).map(p_212427_ -> {
            this.m_212335_(p_212427_, sharedSuggestionProviderElementSuggestionType1, suggestionsBuilder2);
            return suggestionsBuilder2.buildFuture();
        }).orElseGet(() -> this.customSuggestion(commandContext3));
    }

    @Override
    public CompletableFuture<Suggestions> customSuggestion(CommandContext<?> commandContext0) {
        if (this.pendingSuggestionsFuture != null) {
            this.pendingSuggestionsFuture.cancel(false);
        }
        this.pendingSuggestionsFuture = new CompletableFuture();
        int $$1 = ++this.pendingSuggestionsId;
        this.connection.send(new ServerboundCommandSuggestionPacket($$1, commandContext0.getInput()));
        return this.pendingSuggestionsFuture;
    }

    private static String prettyPrint(double double0) {
        return String.format(Locale.ROOT, "%.2f", double0);
    }

    private static String prettyPrint(int int0) {
        return Integer.toString(int0);
    }

    @Override
    public Collection<SharedSuggestionProvider.TextCoordinates> getRelevantCoordinates() {
        HitResult $$0 = this.minecraft.hitResult;
        if ($$0 != null && $$0.getType() == HitResult.Type.BLOCK) {
            BlockPos $$1 = ((BlockHitResult) $$0).getBlockPos();
            return Collections.singleton(new SharedSuggestionProvider.TextCoordinates(prettyPrint($$1.m_123341_()), prettyPrint($$1.m_123342_()), prettyPrint($$1.m_123343_())));
        } else {
            return SharedSuggestionProvider.super.getRelevantCoordinates();
        }
    }

    @Override
    public Collection<SharedSuggestionProvider.TextCoordinates> getAbsoluteCoordinates() {
        HitResult $$0 = this.minecraft.hitResult;
        if ($$0 != null && $$0.getType() == HitResult.Type.BLOCK) {
            Vec3 $$1 = $$0.getLocation();
            return Collections.singleton(new SharedSuggestionProvider.TextCoordinates(prettyPrint($$1.x), prettyPrint($$1.y), prettyPrint($$1.z)));
        } else {
            return SharedSuggestionProvider.super.getAbsoluteCoordinates();
        }
    }

    @Override
    public Set<ResourceKey<Level>> levels() {
        return this.connection.levels();
    }

    @Override
    public RegistryAccess registryAccess() {
        return this.connection.registryAccess();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return this.connection.enabledFeatures();
    }

    public void completeCustomSuggestions(int int0, Suggestions suggestions1) {
        if (int0 == this.pendingSuggestionsId) {
            this.pendingSuggestionsFuture.complete(suggestions1);
            this.pendingSuggestionsFuture = null;
            this.pendingSuggestionsId = -1;
        }
    }

    public void modifyCustomCompletions(ClientboundCustomChatCompletionsPacket.Action clientboundCustomChatCompletionsPacketAction0, List<String> listString1) {
        switch(clientboundCustomChatCompletionsPacketAction0) {
            case ADD:
                this.customCompletionSuggestions.addAll(listString1);
                break;
            case REMOVE:
                listString1.forEach(this.customCompletionSuggestions::remove);
                break;
            case SET:
                this.customCompletionSuggestions.clear();
                this.customCompletionSuggestions.addAll(listString1);
        }
    }
}