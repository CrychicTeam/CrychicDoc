package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationPattern {

    public static final Codec<ResourceLocationPattern> CODEC = RecordCodecBuilder.create(p_261684_ -> p_261684_.group(ExtraCodecs.PATTERN.optionalFieldOf("namespace").forGetter(p_261529_ -> p_261529_.namespacePattern), ExtraCodecs.PATTERN.optionalFieldOf("path").forGetter(p_261660_ -> p_261660_.pathPattern)).apply(p_261684_, ResourceLocationPattern::new));

    private final Optional<Pattern> namespacePattern;

    private final Predicate<String> namespacePredicate;

    private final Optional<Pattern> pathPattern;

    private final Predicate<String> pathPredicate;

    private final Predicate<ResourceLocation> locationPredicate;

    private ResourceLocationPattern(Optional<Pattern> optionalPattern0, Optional<Pattern> optionalPattern1) {
        this.namespacePattern = optionalPattern0;
        this.namespacePredicate = (Predicate<String>) optionalPattern0.map(Pattern::asPredicate).orElse((Predicate) p_261999_ -> true);
        this.pathPattern = optionalPattern1;
        this.pathPredicate = (Predicate<String>) optionalPattern1.map(Pattern::asPredicate).orElse((Predicate) p_261815_ -> true);
        this.locationPredicate = p_261854_ -> this.namespacePredicate.test(p_261854_.getNamespace()) && this.pathPredicate.test(p_261854_.getPath());
    }

    public Predicate<String> namespacePredicate() {
        return this.namespacePredicate;
    }

    public Predicate<String> pathPredicate() {
        return this.pathPredicate;
    }

    public Predicate<ResourceLocation> locationPredicate() {
        return this.locationPredicate;
    }
}