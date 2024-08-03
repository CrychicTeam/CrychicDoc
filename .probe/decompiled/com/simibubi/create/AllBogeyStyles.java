package com.simibubi.create;

import com.google.common.collect.ImmutableMap;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import com.simibubi.create.content.trains.bogey.StandardBogeyRenderer;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.entry.BlockEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class AllBogeyStyles {

    public static final Map<ResourceLocation, BogeyStyle> BOGEY_STYLES = new HashMap();

    public static final Map<ResourceLocation, Map<ResourceLocation, BogeyStyle>> CYCLE_GROUPS = new HashMap();

    private static final Map<ResourceLocation, BogeyStyle> EMPTY_GROUP = ImmutableMap.of();

    public static final String STANDARD_CYCLE_GROUP = "standard";

    public static final BogeyStyle STANDARD = create("standard", "standard").commonRenderer(() -> StandardBogeyRenderer.CommonStandardBogeyRenderer::new).displayName(Components.translatable("create.bogey.style.standard")).size(BogeySizes.SMALL, () -> StandardBogeyRenderer.SmallStandardBogeyRenderer::new, AllBlocks.SMALL_BOGEY).size(BogeySizes.LARGE, () -> StandardBogeyRenderer.LargeStandardBogeyRenderer::new, AllBlocks.LARGE_BOGEY).build();

    public static Map<ResourceLocation, BogeyStyle> getCycleGroup(ResourceLocation cycleGroup) {
        return (Map<ResourceLocation, BogeyStyle>) CYCLE_GROUPS.getOrDefault(cycleGroup, EMPTY_GROUP);
    }

    private static AllBogeyStyles.BogeyStyleBuilder create(String name, String cycleGroup) {
        return create(Create.asResource(name), Create.asResource(cycleGroup));
    }

    public static AllBogeyStyles.BogeyStyleBuilder create(ResourceLocation name, ResourceLocation cycleGroup) {
        return new AllBogeyStyles.BogeyStyleBuilder(name, cycleGroup);
    }

    public static void register() {
    }

    public static class BogeyStyleBuilder {

        protected final Map<BogeySizes.BogeySize, Supplier<BogeyStyle.SizeRenderData>> sizeRenderers = new HashMap();

        protected final Map<BogeySizes.BogeySize, ResourceLocation> sizes = new HashMap();

        protected final ResourceLocation name;

        protected final ResourceLocation cycleGroup;

        protected Component displayName = Lang.translateDirect("bogey.style.invalid");

        protected ResourceLocation soundType = AllSoundEvents.TRAIN2.getId();

        protected CompoundTag defaultData = new CompoundTag();

        protected ParticleOptions contactParticle = ParticleTypes.CRIT;

        protected ParticleOptions smokeParticle = ParticleTypes.POOF;

        protected Optional<Supplier<? extends BogeyRenderer.CommonRenderer>> commonRenderer = Optional.empty();

        public BogeyStyleBuilder(ResourceLocation name, ResourceLocation cycleGroup) {
            this.name = name;
            this.cycleGroup = cycleGroup;
        }

        public AllBogeyStyles.BogeyStyleBuilder displayName(Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public AllBogeyStyles.BogeyStyleBuilder soundType(ResourceLocation soundType) {
            this.soundType = soundType;
            return this;
        }

        public AllBogeyStyles.BogeyStyleBuilder defaultData(CompoundTag defaultData) {
            this.defaultData = defaultData;
            return this;
        }

        public AllBogeyStyles.BogeyStyleBuilder size(BogeySizes.BogeySize size, Supplier<Supplier<? extends BogeyRenderer>> renderer, BlockEntry<? extends AbstractBogeyBlock<?>> blockEntry) {
            this.size(size, renderer, blockEntry.getId());
            return this;
        }

        public AllBogeyStyles.BogeyStyleBuilder size(BogeySizes.BogeySize size, Supplier<Supplier<? extends BogeyRenderer>> renderer, ResourceLocation location) {
            this.sizes.put(size, location);
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.sizeRenderers.put(size, (Supplier) () -> new BogeyStyle.SizeRenderData((Supplier<? extends BogeyRenderer>) renderer.get(), (BogeyRenderer) ((Supplier) renderer.get()).get())));
            return this;
        }

        public AllBogeyStyles.BogeyStyleBuilder contactParticle(ParticleOptions contactParticle) {
            this.contactParticle = contactParticle;
            return this;
        }

        public AllBogeyStyles.BogeyStyleBuilder smokeParticle(ParticleOptions smokeParticle) {
            this.smokeParticle = smokeParticle;
            return this;
        }

        public AllBogeyStyles.BogeyStyleBuilder commonRenderer(Supplier<Supplier<? extends BogeyRenderer.CommonRenderer>> commonRenderer) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.commonRenderer = Optional.of((Supplier) commonRenderer.get()));
            return this;
        }

        public BogeyStyle build() {
            BogeyStyle entry = new BogeyStyle(this.name, this.cycleGroup, this.displayName, this.soundType, this.contactParticle, this.smokeParticle, this.defaultData, this.sizes, this.sizeRenderers, this.commonRenderer);
            AllBogeyStyles.BOGEY_STYLES.put(this.name, entry);
            ((Map) AllBogeyStyles.CYCLE_GROUPS.computeIfAbsent(this.cycleGroup, l -> new HashMap())).put(this.name, entry);
            return entry;
        }
    }
}