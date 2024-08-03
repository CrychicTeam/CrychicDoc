package io.github.edwinmindcraft.origins.api.origin;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import io.github.edwinmindcraft.calio.api.network.CodecSet;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public final class OriginLayer implements Comparable<OriginLayer> {

    public static final Codec<Holder<OriginLayer>> HOLDER_REFERENCE = CalioCodecHelper.holderRef(OriginsDynamicRegistries.LAYERS_REGISTRY, SerializableDataTypes.IDENTIFIER);

    public static final Codec<OriginLayer> CODEC = RecordCodecBuilder.create(instance -> instance.group(CalioCodecHelper.INT.fieldOf("order").forGetter(OriginLayer::order), CalioCodecHelper.setOf(ConditionedOrigin.CODEC).fieldOf("origins").forGetter(OriginLayer::conditionedOrigins), CalioCodecHelper.BOOL.fieldOf("enabled").forGetter(OriginLayer::enabled), CalioCodecHelper.COMPONENT_CODEC.fieldOf("name").forGetter(OriginLayer::name), CalioCodecHelper.COMPONENT_CODEC.fieldOf("missing_name").forGetter(OriginLayer::missingName), CalioCodecHelper.COMPONENT_CODEC.fieldOf("missing_description").forGetter(OriginLayer::missingDescription), CalioCodecHelper.BOOL.fieldOf("allow_random").forGetter(OriginLayer::allowRandom), CalioCodecHelper.BOOL.fieldOf("allow_random_unchoosable").forGetter(OriginLayer::allowRandomUnchoosable), CalioCodecHelper.setOf(ResourceLocation.CODEC).fieldOf("random_exclusions").forGetter(OriginLayer::randomExclusions), Origin.optional("default").forGetter(OriginLayer::defaultOrigin), CalioCodecHelper.BOOL.fieldOf("auto_choose").forGetter(OriginLayer::autoChoose), CalioCodecHelper.optionalField(CalioCodecHelper.BOOL, "hidden", false).forGetter(OriginLayer::hidden), CalioCodecHelper.optionalField(GuiTitle.CODEC, "gui_title", GuiTitle.DEFAULT).forGetter(OriginLayer::title)).apply(instance, OriginLayer::new));

    public static final CodecSet<OriginLayer> CODEC_SET = CalioCodecHelper.forDynamicRegistry(OriginsDynamicRegistries.LAYERS_REGISTRY, SerializableDataTypes.IDENTIFIER, CODEC);

    private final int order;

    private final Set<ConditionedOrigin> conditionedOrigins;

    private final boolean enabled;

    private final Component name;

    private final Component missingName;

    private final Component missingDescription;

    private final boolean allowRandom;

    private final boolean allowRandomUnchoosable;

    private final Set<ResourceLocation> randomExclusions;

    private final Holder<Origin> defaultOrigin;

    private final boolean autoChoose;

    private final boolean hidden;

    private final GuiTitle title;

    public OriginLayer(int order, Set<ConditionedOrigin> conditionedOrigins, boolean enabled, Component name, Component missingName, Component missingDescription, boolean allowRandom, boolean allowRandomUnchoosable, Set<ResourceLocation> randomExclusions, Holder<Origin> defaultOrigin, boolean autoChoose, boolean hidden, GuiTitle title) {
        this.order = order;
        this.conditionedOrigins = conditionedOrigins;
        this.enabled = enabled;
        this.name = name;
        this.missingName = missingName;
        this.missingDescription = missingDescription;
        this.allowRandom = allowRandom;
        this.allowRandomUnchoosable = allowRandomUnchoosable;
        this.randomExclusions = randomExclusions;
        this.defaultOrigin = defaultOrigin;
        this.autoChoose = autoChoose;
        this.hidden = hidden;
        this.title = title;
    }

    public OriginLayer cleanup(ICalioDynamicRegistryManager registries) {
        Registry<Origin> registry = registries.get(OriginsDynamicRegistries.ORIGINS_REGISTRY);
        return new OriginLayer(this.order(), (Set<ConditionedOrigin>) this.conditionedOrigins().stream().map(x -> x.cleanup(registries)).filter(x -> !x.isEmpty()).collect(ImmutableSet.toImmutableSet()), this.enabled(), this.name(), this.missingName(), this.missingDescription(), this.allowRandom(), this.allowRandomUnchoosable(), (Set<ResourceLocation>) this.randomExclusions().stream().filter(registry::m_7804_).collect(ImmutableSet.toImmutableSet()), this.defaultOrigin(), this.autoChoose(), this.hidden(), this.title());
    }

    public boolean hasDefaultOrigin() {
        return !this.defaultOrigin().is(OriginRegisters.EMPTY.getId());
    }

    public Set<Holder<Origin>> origins() {
        return (Set<Holder<Origin>>) this.conditionedOrigins().stream().flatMap(ConditionedOrigin::stream).collect(Collectors.toSet());
    }

    public Set<Holder<Origin>> origins(Player player) {
        return (Set<Holder<Origin>>) this.conditionedOrigins().stream().flatMap(x -> x.stream(player)).collect(Collectors.toSet());
    }

    public boolean empty() {
        return this.conditionedOrigins().stream().flatMap(ConditionedOrigin::stream).findAny().isEmpty();
    }

    public boolean empty(Player player) {
        return this.conditionedOrigins().stream().flatMap(x -> x.stream(player)).findAny().isEmpty();
    }

    public List<Holder<Origin>> randomOrigins(Player player) {
        return (List<Holder<Origin>>) this.conditionedOrigins().stream().flatMap(x -> x.stream(player)).filter(o -> !this.randomExclusions().contains(((ResourceKey) o.unwrapKey().orElseThrow()).location())).filter(id -> this.allowRandomUnchoosable() || ((Origin) id.value()).isChoosable()).collect(Collectors.toList());
    }

    public boolean contains(ResourceLocation origin) {
        return this.conditionedOrigins().stream().anyMatch(x -> x.origins().stream().flatMap(HolderSet::m_203614_).anyMatch(holder -> holder.is(origin)));
    }

    public boolean contains(ResourceKey<Origin> origin) {
        return this.conditionedOrigins().stream().anyMatch(x -> x.origins().stream().flatMap(HolderSet::m_203614_).anyMatch(holder -> holder.is(origin)));
    }

    public boolean contains(ResourceLocation origin, Player player) {
        return this.conditionedOrigins().stream().anyMatch(x -> ConfiguredEntityCondition.check(x.condition(), player) && x.origins().stream().flatMap(HolderSet::m_203614_).anyMatch(holder -> holder.is(origin)));
    }

    public boolean contains(ResourceKey<Origin> origin, Player player) {
        return this.conditionedOrigins().stream().anyMatch(x -> ConfiguredEntityCondition.check(x.condition(), player) && x.origins().stream().flatMap(HolderSet::m_203614_).anyMatch(holder -> holder.is(origin)));
    }

    @NotNull
    public Optional<Holder<Origin>> getAutomaticOrigin(Player player) {
        if (!this.autoChoose()) {
            return Optional.empty();
        } else {
            List<Holder<Origin>> origins = this.origins(player).stream().filter(x -> ((Origin) x.value()).isChoosable()).toList();
            if (this.allowRandom() && origins.isEmpty()) {
                return this.selectRandom(player);
            } else {
                return origins.size() > 1 ? Optional.empty() : origins.stream().findFirst();
            }
        }
    }

    public Optional<Holder<Origin>> selectRandom(Player player) {
        if (!this.allowRandom()) {
            return Optional.empty();
        } else {
            List<Holder<Origin>> candidates = this.conditionedOrigins.stream().flatMap(x -> x.stream(player)).filter(x -> this.allowRandomUnchoosable() || ((Origin) x.value()).isChoosable()).toList();
            if (candidates.isEmpty()) {
                return Optional.empty();
            } else {
                return candidates.size() == 1 ? Optional.of((Holder) candidates.get(0)) : Optional.of((Holder) candidates.get(player.m_217043_().nextInt(candidates.size())));
            }
        }
    }

    public int getOriginOptionCount(Player playerEntity) {
        long choosableOrigins = this.origins(playerEntity).stream().filter(x -> ((Origin) x.value()).isChoosable()).count();
        if (this.allowRandom() && this.randomOrigins(playerEntity).size() > 0) {
            choosableOrigins++;
        }
        return Math.toIntExact(choosableOrigins);
    }

    public int compareTo(@NotNull OriginLayer o) {
        return Integer.compare(this.order(), o.order());
    }

    public int order() {
        return this.order;
    }

    public Set<ConditionedOrigin> conditionedOrigins() {
        return this.conditionedOrigins;
    }

    public boolean enabled() {
        return this.enabled;
    }

    public Component name() {
        return this.name;
    }

    public Component missingName() {
        return this.missingName;
    }

    public Component missingDescription() {
        return this.missingDescription;
    }

    public boolean allowRandom() {
        return this.allowRandom;
    }

    public boolean allowRandomUnchoosable() {
        return this.allowRandomUnchoosable;
    }

    public Set<ResourceLocation> randomExclusions() {
        return this.randomExclusions;
    }

    @NotNull
    public Holder<Origin> defaultOrigin() {
        return this.defaultOrigin;
    }

    public boolean autoChoose() {
        return this.autoChoose;
    }

    public boolean hidden() {
        return this.hidden;
    }

    public GuiTitle title() {
        return this.title;
    }

    public String toString() {
        return "OriginLayer[order=" + this.order + ", conditionedOrigins=" + this.conditionedOrigins + ", enabled=" + this.enabled + ", name=" + this.name + ", missingName=" + this.missingName + ", missingDescription=" + this.missingDescription + ", allowRandom=" + this.allowRandom + ", allowRandomUnchoosable=" + this.allowRandomUnchoosable + ", randomExclusions=" + this.randomExclusions + ", defaultOrigin=" + this.defaultOrigin + ", autoChoose=" + this.autoChoose + ", hidden=" + this.hidden + ", title=" + this.title + "]";
    }
}