package net.mehvahdjukaar.moonlight.api.fluids;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.fluids.forge.SoftFluidImpl;
import net.mehvahdjukaar.moonlight.api.misc.StrOpt;
import net.mehvahdjukaar.moonlight.api.misc.Triplet;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class SoftFluid {

    private final Component name;

    private final SoftFluid.LazyFluidSet equivalentFluids;

    private final FluidContainerList containerList;

    private final FoodProvider food;

    private final List<String> NBTFromItem;

    public final boolean isGenerated;

    private final ResourceLocation stillTexture;

    private final ResourceLocation flowingTexture;

    @Nullable
    private final ResourceLocation useTexturesFrom;

    private final int luminosity;

    private final int emissivity;

    private final int tintColor;

    private final SoftFluid.TintMethod tintMethod;

    protected int averageTextureTint = -1;

    public static final int BOTTLE_COUNT = SoftFluid.Capacity.BOTTLE.getValue();

    public static final int BOWL_COUNT = SoftFluid.Capacity.BOWL.getValue();

    public static final int BUCKET_COUNT = SoftFluid.Capacity.BUCKET.getValue();

    public static final int WATER_BUCKET_COUNT = 3;

    public static final Codec<Holder<SoftFluid>> HOLDER_CODEC = RegistryFileCodec.create(SoftFluidRegistry.KEY, SoftFluid.CODEC);

    public static final Codec<Component> COMPONENT_CODEC = Codec.either(ExtraCodecs.COMPONENT, Codec.STRING).xmap(either -> (Component) either.map(c -> c, Component::m_237115_), Either::left);

    public static final Codec<SoftFluid> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("still_texture").forGetter(SoftFluid::getStillTexture), ResourceLocation.CODEC.fieldOf("flowing_texture").forGetter(SoftFluid::getFlowingTexture), StrOpt.of(COMPONENT_CODEC, "translation_key").forGetter(getHackyOptional(SoftFluid::getTranslatedName)), StrOpt.of(Codec.intRange(0, 15), "luminosity").forGetter(getHackyOptional(SoftFluid::getLuminosity)), StrOpt.of(Codec.intRange(0, 15), "emissivity").forGetter(getHackyOptional(SoftFluid::getEmissivity)), StrOpt.of(ColorUtils.CODEC, "color").forGetter(getHackyOptional(SoftFluid::getTintColor)), StrOpt.of(SoftFluid.TintMethod.CODEC, "tint_method").forGetter(getHackyOptional(SoftFluid::getTintMethod)), FoodProvider.CODEC.optionalFieldOf("food").forGetter(getHackyOptional(SoftFluid::getFoodProvider)), StrOpt.of(Codec.STRING.listOf(), "preserved_tags_from_item").forGetter(getHackyOptional(SoftFluid::getNbtKeyFromItem)), StrOpt.of(FluidContainerList.Category.CODEC.listOf(), "containers").forGetter(f -> f.getContainerList().encodeList()), StrOpt.of(Codec.STRING.listOf(), "equivalent_fluids", new ArrayList()).forGetter(s -> s.equivalentFluids.keys), StrOpt.of(ResourceLocation.CODEC, "use_texture_from").forGetter(s -> Optional.ofNullable(s.getTextureOverride()))).apply(instance, SoftFluid::create));

    private static final SoftFluid DEFAULT_DUMMY = new SoftFluid(new SoftFluid.Builder(new ResourceLocation(""), new ResourceLocation("")));

    private SoftFluid(SoftFluid.Builder builder) {
        this.tintMethod = builder.tintMethod;
        this.equivalentFluids = new SoftFluid.LazyFluidSet(builder.equivalentFluids);
        this.luminosity = builder.luminosity;
        this.emissivity = builder.emissivity;
        this.containerList = builder.containerList;
        this.food = builder.food;
        this.name = builder.name;
        this.NBTFromItem = builder.NBTFromItem;
        this.useTexturesFrom = builder.useTexturesFrom;
        ResourceLocation still = builder.stillTexture;
        ResourceLocation flowing = builder.flowingTexture;
        int tint = builder.tintColor;
        if (this.useTexturesFrom != null && PlatHelper.getPhysicalSide().isClient()) {
            Triplet<ResourceLocation, ResourceLocation, Integer> data = getRenderingData(this.useTexturesFrom);
            if (data != null) {
                still = data.left();
                flowing = data.middle();
                tint = data.right();
            }
        }
        this.stillTexture = still;
        this.flowingTexture = flowing;
        this.tintColor = tint;
        this.isGenerated = builder.isFromData;
    }

    @Nullable
    public ResourceLocation getTextureOverride() {
        return this.useTexturesFrom;
    }

    public FoodProvider getFoodProvider() {
        return this.food;
    }

    public Component getTranslatedName() {
        return this.name;
    }

    @Deprecated(forRemoval = true)
    public String getTranslationKey() {
        return "fluid";
    }

    public boolean isEnabled() {
        return !this.equivalentFluids.isEmpty() || !this.containerList.getPossibleFilled().isEmpty();
    }

    @Deprecated(forRemoval = true)
    public String getFromMod() {
        return "minecraft";
    }

    public Fluid getVanillaFluid() {
        Iterator var1 = this.getEquivalentFluids().iterator();
        return var1.hasNext() ? (Fluid) var1.next() : Fluids.EMPTY;
    }

    @Deprecated(forRemoval = true)
    public Fluid getForgeFluid() {
        return this.getVanillaFluid();
    }

    public List<String> getNbtKeyFromItem() {
        return this.NBTFromItem;
    }

    public List<Fluid> getEquivalentFluids() {
        return this.equivalentFluids.getFluids();
    }

    public boolean isEquivalent(Fluid fluid) {
        return this.equivalentFluids.getFluids().contains(fluid);
    }

    @Deprecated(forRemoval = true)
    @Internal
    public boolean isEmpty() {
        return this == SoftFluidRegistry.empty();
    }

    public boolean isEmptyFluid() {
        return this == SoftFluidRegistry.empty();
    }

    public Optional<Item> getFilledContainer(Item emptyContainer) {
        return this.containerList.getFilled(emptyContainer);
    }

    public Optional<Item> getEmptyContainer(Item filledContainer) {
        return this.containerList.getEmpty(filledContainer);
    }

    public FluidContainerList getContainerList() {
        return this.containerList;
    }

    public int getLuminosity() {
        return this.luminosity;
    }

    public int getEmissivity() {
        return this.emissivity;
    }

    public int getTintColor() {
        return this.tintColor;
    }

    public int getAverageTextureTintColor() {
        return this.averageTextureTint;
    }

    public SoftFluid.TintMethod getTintMethod() {
        return this.tintMethod;
    }

    public boolean isColored() {
        return this.tintColor != -1;
    }

    public ResourceLocation getFlowingTexture() {
        return this.flowingTexture;
    }

    public ResourceLocation getStillTexture() {
        return this.stillTexture;
    }

    public boolean isFood() {
        return !this.food.isEmpty();
    }

    protected static SoftFluid create(ResourceLocation still, ResourceLocation flowing, Optional<Component> translation, Optional<Integer> luminosity, Optional<Integer> emissivity, Optional<Integer> color, Optional<SoftFluid.TintMethod> tint, Optional<FoodProvider> food, Optional<List<String>> nbtKeys, Optional<List<FluidContainerList.Category>> containers, List<String> equivalent, Optional<ResourceLocation> textureFrom) {
        SoftFluid.Builder builder = new SoftFluid.Builder(still, flowing);
        translation.ifPresent(builder::translation);
        luminosity.ifPresent(builder::luminosity);
        emissivity.ifPresent(builder::emissivity);
        color.ifPresent(builder::color);
        tint.ifPresent(builder::tintMethod);
        food.ifPresent(builder::food);
        nbtKeys.ifPresent(k -> k.forEach(xva$0 -> builder.keepNBTFromItem(xva$0)));
        containers.ifPresent(b -> builder.containers(new FluidContainerList(b)));
        builder.equivalentFluids.addAll(equivalent);
        textureFrom.ifPresent(builder::copyTexturesFrom);
        return builder.build();
    }

    private static <T> Function<SoftFluid, Optional<T>> getHackyOptional(Function<SoftFluid, T> getter) {
        return f -> {
            T value = (T) getter.apply(f);
            T def = (T) getter.apply(DEFAULT_DUMMY);
            return value != null && !value.equals(def) ? Optional.of(value) : Optional.empty();
        };
    }

    @Internal
    @ExpectPlatform
    @Transformed
    public static void addFluidSpecificAttributes(SoftFluid.Builder builder, Fluid fluid) {
        SoftFluidImpl.addFluidSpecificAttributes(builder, fluid);
    }

    @Internal
    @Nullable
    @ExpectPlatform
    @Transformed
    public static Triplet<ResourceLocation, ResourceLocation, Integer> getRenderingData(ResourceLocation useTexturesFrom) {
        return SoftFluidImpl.getRenderingData(useTexturesFrom);
    }

    public static class Builder {

        private ResourceLocation stillTexture;

        private ResourceLocation flowingTexture;

        private Component name = Component.translatable("fluid.moonlight.generic_fluid");

        private int luminosity = 0;

        private int emissivity = 0;

        private int tintColor = -1;

        private SoftFluid.TintMethod tintMethod = SoftFluid.TintMethod.STILL_AND_FLOWING;

        private FoodProvider food = FoodProvider.EMPTY;

        private FluidContainerList containerList = new FluidContainerList();

        private final List<String> NBTFromItem = new ArrayList();

        private final List<String> equivalentFluids = new ArrayList();

        private boolean isFromData = true;

        private ResourceLocation useTexturesFrom;

        public Builder(ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
        }

        public Builder(Fluid fluid) {
            this(new ResourceLocation("block/water_still"), new ResourceLocation("minecraft:block/water_flowing"));
            this.copyTexturesFrom(Utils.getID(fluid));
            this.addEqFluid(fluid);
            this.isFromData = false;
            SoftFluid.addFluidSpecificAttributes(this, fluid);
        }

        public final SoftFluid.Builder textures(ResourceLocation still, ResourceLocation flow) {
            this.stillTexture = still;
            this.flowingTexture = flow;
            return this;
        }

        @Deprecated(forRemoval = true)
        public final SoftFluid.Builder translationKey(String translationKey) {
            if (translationKey != null) {
                this.name = Component.translatable(translationKey);
            }
            return this;
        }

        public final SoftFluid.Builder translation(Component component) {
            if (component != null) {
                this.name = component;
            }
            return this;
        }

        public final SoftFluid.Builder keepNBTFromItem(String... NBTkey) {
            this.NBTFromItem.addAll(Arrays.asList(NBTkey));
            return this;
        }

        public final SoftFluid.Builder color(int tintColor) {
            this.tintColor = tintColor;
            return this;
        }

        public final SoftFluid.Builder noTint() {
            this.tintMethod = SoftFluid.TintMethod.NO_TINT;
            return this;
        }

        public final SoftFluid.Builder tinted() {
            this.tintMethod = SoftFluid.TintMethod.STILL_AND_FLOWING;
            return this;
        }

        public final SoftFluid.Builder onlyFlowingTinted() {
            this.tintMethod = SoftFluid.TintMethod.FLOWING;
            return this;
        }

        public final SoftFluid.Builder tintMethod(SoftFluid.TintMethod tint) {
            this.tintMethod = tint;
            return this;
        }

        public final SoftFluid.Builder luminosity(int luminosity) {
            this.luminosity = luminosity;
            this.emissivity = luminosity;
            return this;
        }

        public final SoftFluid.Builder emissivity(int emissivity) {
            this.emissivity = emissivity;
            return this;
        }

        public final SoftFluid.Builder addEqFluid(Fluid fluid) {
            if (fluid != null && fluid != Fluids.EMPTY) {
                this.equivalentFluids.add(BuiltInRegistries.FLUID.getKey(fluid).toString());
                Item i = fluid.getBucket();
                if (i != Items.AIR && i != Items.BUCKET) {
                    this.bucket(i);
                }
            }
            return this;
        }

        public final SoftFluid.Builder copyTexturesFrom(ResourceLocation fluidRes) {
            this.useTexturesFrom = fluidRes;
            return this;
        }

        public final SoftFluid.Builder copyTexturesFrom(String fluidRes) {
            return this.copyTexturesFrom(new ResourceLocation(fluidRes));
        }

        public final SoftFluid.Builder containerItem(Item filledItem, Item emptyItem, int itemCapacity) {
            if (filledItem != Items.AIR) {
                this.containerList.add(emptyItem, filledItem, itemCapacity);
            }
            return this;
        }

        public final SoftFluid.Builder containerItem(Item filledItem, Item emptyItem, int itemCapacity, SoundEvent fillSound, SoundEvent emptySound) {
            if (filledItem != Items.AIR) {
                this.containerList.add(emptyItem, filledItem, itemCapacity, fillSound, emptySound);
            }
            return this;
        }

        public final SoftFluid.Builder containers(FluidContainerList containerList) {
            this.containerList = containerList;
            return this;
        }

        public final SoftFluid.Builder emptyHandContainerItem(Item filledItem, int itemCapacity) {
            return filledItem != Items.AIR ? this.containerItem(filledItem, Items.AIR, itemCapacity) : this;
        }

        public final SoftFluid.Builder bottle(Item item) {
            this.containerItem(item, Items.GLASS_BOTTLE, SoftFluid.BOTTLE_COUNT);
            return this;
        }

        public final SoftFluid.Builder drink(Item item) {
            return this.bottle(item).food(item, SoftFluid.BOTTLE_COUNT);
        }

        public final SoftFluid.Builder bucket(Item item) {
            this.containerItem(item, Items.BUCKET, SoftFluid.BUCKET_COUNT, SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY);
            return this;
        }

        public final SoftFluid.Builder bowl(Item item) {
            this.containerItem(item, Items.BOWL, SoftFluid.BOWL_COUNT);
            return this;
        }

        public final SoftFluid.Builder stew(Item item) {
            return this.bowl(item).food(item, SoftFluid.BOWL_COUNT);
        }

        public final SoftFluid.Builder food(Item item) {
            return this.food(item, 1);
        }

        public final SoftFluid.Builder food(Item item, int foodDivider) {
            if (item != null) {
                this.food(FoodProvider.create(item, foodDivider));
            }
            return this;
        }

        public final SoftFluid.Builder food(FoodProvider foodProvider) {
            this.food = foodProvider;
            return this;
        }

        public SoftFluid build() {
            return new SoftFluid(this);
        }

        @Deprecated(forRemoval = true)
        public final SoftFluid.Builder fromMod(String s) {
            return this;
        }
    }

    public static enum Capacity implements StringRepresentable {

        BOTTLE(1, 1), BOWL(2, 1), BUCKET(4, 3), BLOCK(4, 4);

        public final int value;

        public static final Codec<SoftFluid.Capacity> CODEC = StringRepresentable.fromEnum(SoftFluid.Capacity::values);

        public static final Codec<Integer> INT_CODEC = Codec.either(Codec.INT, CODEC).xmap(either -> (Integer) either.map(i -> i, SoftFluid.Capacity::getValue), Either::left);

        private Capacity(int forge, int fabric) {
            this.value = PlatHelper.getPlatform().isForge() ? forge : fabric;
        }

        @Override
        public String getSerializedName() {
            return this.name().toUpperCase(Locale.ROOT);
        }

        public int getValue() {
            return this.value;
        }
    }

    private static class LazyFluidSet {

        protected static final SoftFluid.LazyFluidSet EMPTY = new SoftFluid.LazyFluidSet(Collections.emptyList());

        private final List<String> keys;

        private final List<Fluid> fluids;

        private final List<TagKey<Fluid>> tags = new ArrayList();

        private LazyFluidSet(List<String> keys) {
            this.keys = keys;
            LinkedHashSet<Fluid> set = new LinkedHashSet();
            for (String key : keys) {
                if (key.startsWith("#")) {
                    this.tags.add(TagKey.create(Registries.FLUID, new ResourceLocation(key.substring(1))));
                } else {
                    BuiltInRegistries.FLUID.m_6612_(new ResourceLocation(key)).ifPresent(set::add);
                }
            }
            this.fluids = List.of((Fluid[]) set.toArray(new Fluid[0]));
        }

        public static SoftFluid.LazyFluidSet merge(SoftFluid.LazyFluidSet first, SoftFluid.LazyFluidSet second) {
            if (first.isEmpty()) {
                return second;
            } else if (second.isEmpty()) {
                return first;
            } else {
                List<String> keys = new ArrayList(first.keys);
                keys.addAll(second.keys);
                return new SoftFluid.LazyFluidSet(keys);
            }
        }

        public List<Fluid> getFluids() {
            if (this.tags.isEmpty()) {
                return this.fluids;
            } else {
                ArrayList<Fluid> list = new ArrayList(this.fluids);
                for (TagKey<Fluid> tag : this.tags) {
                    BuiltInRegistries.FLUID.m_206058_(tag).forEach(e -> list.add((Fluid) e.value()));
                }
                return list;
            }
        }

        public boolean isEmpty() {
            return this.getFluids().isEmpty();
        }
    }

    public static enum TintMethod implements StringRepresentable {

        NO_TINT, FLOWING, STILL_AND_FLOWING;

        public static final Codec<SoftFluid.TintMethod> CODEC = StringRepresentable.fromEnum(SoftFluid.TintMethod::values);

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public boolean appliesToFlowing() {
            return this == FLOWING || this == STILL_AND_FLOWING;
        }

        public boolean appliesToStill() {
            return this == STILL_AND_FLOWING;
        }
    }
}