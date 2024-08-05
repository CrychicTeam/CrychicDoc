package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ParticleArgument implements ArgumentType<ParticleOptions> {

    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "particle with options");

    public static final DynamicCommandExceptionType ERROR_UNKNOWN_PARTICLE = new DynamicCommandExceptionType(p_103941_ -> Component.translatable("particle.notFound", p_103941_));

    private final HolderLookup<ParticleType<?>> particles;

    public ParticleArgument(CommandBuildContext commandBuildContext0) {
        this.particles = commandBuildContext0.holderLookup(Registries.PARTICLE_TYPE);
    }

    public static ParticleArgument particle(CommandBuildContext commandBuildContext0) {
        return new ParticleArgument(commandBuildContext0);
    }

    public static ParticleOptions getParticle(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (ParticleOptions) commandContextCommandSourceStack0.getArgument(string1, ParticleOptions.class);
    }

    public ParticleOptions parse(StringReader stringReader0) throws CommandSyntaxException {
        return readParticle(stringReader0, this.particles);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static ParticleOptions readParticle(StringReader stringReader0, HolderLookup<ParticleType<?>> holderLookupParticleType1) throws CommandSyntaxException {
        ParticleType<?> $$2 = readParticleType(stringReader0, holderLookupParticleType1);
        return readParticle(stringReader0, (ParticleType<ParticleOptions>) $$2);
    }

    private static ParticleType<?> readParticleType(StringReader stringReader0, HolderLookup<ParticleType<?>> holderLookupParticleType1) throws CommandSyntaxException {
        ResourceLocation $$2 = ResourceLocation.read(stringReader0);
        ResourceKey<ParticleType<?>> $$3 = ResourceKey.create(Registries.PARTICLE_TYPE, $$2);
        return (ParticleType<?>) ((Holder.Reference) holderLookupParticleType1.m_254902_($$3).orElseThrow(() -> ERROR_UNKNOWN_PARTICLE.create($$2))).value();
    }

    private static <T extends ParticleOptions> T readParticle(StringReader stringReader0, ParticleType<T> particleTypeT1) throws CommandSyntaxException {
        return particleTypeT1.getDeserializer().fromCommand(particleTypeT1, stringReader0);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return SharedSuggestionProvider.suggestResource(this.particles.listElementIds().map(ResourceKey::m_135782_), suggestionsBuilder1);
    }
}