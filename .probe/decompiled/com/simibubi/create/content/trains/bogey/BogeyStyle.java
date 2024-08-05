package com.simibubi.create.content.trains.bogey;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class BogeyStyle {

    public final ResourceLocation name;

    public final ResourceLocation cycleGroup;

    public final Component displayName;

    public final ResourceLocation soundType;

    public final ParticleOptions contactParticle;

    public final ParticleOptions smokeParticle;

    public final CompoundTag defaultData;

    private Optional<Supplier<? extends BogeyRenderer.CommonRenderer>> commonRendererFactory;

    private Map<BogeySizes.BogeySize, ResourceLocation> sizes;

    @OnlyIn(Dist.CLIENT)
    private Map<BogeySizes.BogeySize, BogeyStyle.SizeRenderData> sizeRenderers;

    @OnlyIn(Dist.CLIENT)
    private Optional<BogeyRenderer.CommonRenderer> commonRenderer;

    public BogeyStyle(ResourceLocation name, ResourceLocation cycleGroup, Component displayName, ResourceLocation soundType, ParticleOptions contactParticle, ParticleOptions smokeParticle, CompoundTag defaultData, Map<BogeySizes.BogeySize, ResourceLocation> sizes, Map<BogeySizes.BogeySize, Supplier<BogeyStyle.SizeRenderData>> sizeRenderers, Optional<Supplier<? extends BogeyRenderer.CommonRenderer>> commonRenderer) {
        this.name = name;
        this.cycleGroup = cycleGroup;
        this.displayName = displayName;
        this.soundType = soundType;
        this.contactParticle = contactParticle;
        this.smokeParticle = smokeParticle;
        this.defaultData = defaultData;
        this.sizes = sizes;
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            this.sizeRenderers = new HashMap();
            sizeRenderers.forEach((k, v) -> this.sizeRenderers.put(k, (BogeyStyle.SizeRenderData) v.get()));
            this.commonRendererFactory = commonRenderer;
            this.commonRenderer = commonRenderer.map(Supplier::get);
        });
    }

    public Map<ResourceLocation, BogeyStyle> getCycleGroup() {
        return AllBogeyStyles.getCycleGroup(this.cycleGroup);
    }

    public Block getNextBlock(BogeySizes.BogeySize currentSize) {
        return (Block) Stream.iterate(currentSize.increment(), BogeySizes.BogeySize::increment).filter(this.sizes::containsKey).findFirst().map(this::getBlockOfSize).orElse(this.getBlockOfSize(currentSize));
    }

    public Block getBlockOfSize(BogeySizes.BogeySize size) {
        return ForgeRegistries.BLOCKS.getValue((ResourceLocation) this.sizes.get(size));
    }

    public Set<BogeySizes.BogeySize> validSizes() {
        return this.sizes.keySet();
    }

    @NotNull
    public SoundEvent getSoundType() {
        AllSoundEvents.SoundEntry entry = (AllSoundEvents.SoundEntry) AllSoundEvents.ALL.get(this.soundType);
        if (entry == null || entry.getMainEvent() == null) {
            entry = AllSoundEvents.TRAIN2;
        }
        return entry.getMainEvent();
    }

    @OnlyIn(Dist.CLIENT)
    public BogeyRenderer createRendererInstance(BogeySizes.BogeySize size) {
        return ((BogeyStyle.SizeRenderData) this.sizeRenderers.get(size)).createRenderInstance();
    }

    @OnlyIn(Dist.CLIENT)
    public BogeyRenderer getInWorldRenderInstance(BogeySizes.BogeySize size) {
        BogeyStyle.SizeRenderData sizeData = (BogeyStyle.SizeRenderData) this.sizeRenderers.get(size);
        return (BogeyRenderer) (sizeData != null ? sizeData.getInWorldInstance() : BackupBogeyRenderer.INSTANCE);
    }

    public Optional<BogeyRenderer.CommonRenderer> getInWorldCommonRenderInstance() {
        return this.commonRenderer;
    }

    public Optional<BogeyRenderer.CommonRenderer> getNewCommonRenderInstance() {
        return this.commonRendererFactory.map(Supplier::get);
    }

    public BogeyInstance createInstance(CarriageBogey bogey, BogeySizes.BogeySize size, MaterialManager materialManager) {
        return new BogeyInstance(bogey, this, size, materialManager);
    }

    @OnlyIn(Dist.CLIENT)
    public static record SizeRenderData(Supplier<? extends BogeyRenderer> rendererFactory, BogeyRenderer instance) {

        public BogeyRenderer createRenderInstance() {
            return (BogeyRenderer) this.rendererFactory.get();
        }

        public BogeyRenderer getInWorldInstance() {
            return this.instance;
        }
    }
}