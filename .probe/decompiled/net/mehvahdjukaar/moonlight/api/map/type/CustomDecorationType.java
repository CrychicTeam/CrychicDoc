package net.mehvahdjukaar.moonlight.api.map.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.MapDataRegistry;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class CustomDecorationType<D extends CustomMapDecoration, M extends MapBlockMarker<D>> implements MapDecorationType<D, M> {

    public static final Codec<CustomDecorationType<?, ?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("custom_type").forGetter(CustomDecorationType::getCustomFactoryID)).apply(instance, MapDataRegistry::getCustomType));

    @Internal
    public ResourceLocation factoryId;

    private final BiFunction<MapDecorationType<?, ?>, FriendlyByteBuf, D> decorationFactory;

    @NotNull
    private final Function<CustomDecorationType<D, M>, M> markerFactory;

    @Nullable
    private final BiFunction<BlockGetter, BlockPos, M> markerFromWorldFactory;

    private CustomDecorationType(ResourceLocation typeId, BiFunction<MapDecorationType<?, ?>, FriendlyByteBuf, D> decorationFactory, Function<CustomDecorationType<D, M>, M> markerFactory, @Nullable BiFunction<BlockGetter, BlockPos, M> markerFromWorldFactory) {
        this.factoryId = typeId;
        this.markerFactory = markerFactory;
        this.markerFromWorldFactory = markerFromWorldFactory;
        this.decorationFactory = decorationFactory;
    }

    public static <D extends CustomMapDecoration, M extends MapBlockMarker<D>> CustomDecorationType<D, M> withWorldMarker(Function<CustomDecorationType<D, M>, M> markerFactory, @Nullable BiFunction<BlockGetter, BlockPos, M> markerFromWorldFactory, BiFunction<MapDecorationType<?, ?>, FriendlyByteBuf, D> decorationFactory) {
        return new CustomDecorationType<>(null, decorationFactory, markerFactory, markerFromWorldFactory);
    }

    @Deprecated(forRemoval = true)
    public static <D extends CustomMapDecoration, M extends MapBlockMarker<D>> CustomDecorationType<D, M> withWorldMarker(ResourceLocation typeId, Supplier<M> markerFactory, @Nullable BiFunction<BlockGetter, BlockPos, M> markerFromWorldFactory, BiFunction<MapDecorationType<?, ?>, FriendlyByteBuf, D> decorationFactory) {
        return new CustomDecorationType<>(typeId, decorationFactory, t -> (MapBlockMarker) markerFactory.get(), markerFromWorldFactory);
    }

    @Deprecated(forRemoval = true)
    public static <D extends CustomMapDecoration, M extends MapBlockMarker<D>> CustomDecorationType<D, M> simple(ResourceLocation typeId, Supplier<M> markerFactory, BiFunction<MapDecorationType<?, ?>, FriendlyByteBuf, D> decorationFactory) {
        return new CustomDecorationType<>(typeId, decorationFactory, t -> (MapBlockMarker) markerFactory.get(), null);
    }

    public static <D extends CustomMapDecoration, M extends MapBlockMarker<D>> CustomDecorationType<D, M> simple(Function<CustomDecorationType<D, M>, M> markerFactory, BiFunction<MapDecorationType<?, ?>, FriendlyByteBuf, D> decorationFactory) {
        return new CustomDecorationType<>(null, decorationFactory, markerFactory, null);
    }

    @Override
    public ResourceLocation getCustomFactoryID() {
        return this.factoryId;
    }

    @Override
    public boolean isFromWorld() {
        return this.markerFromWorldFactory != null;
    }

    @Nullable
    @Override
    public D loadDecorationFromBuffer(FriendlyByteBuf buffer) {
        try {
            return (D) this.decorationFactory.apply(this, buffer);
        } catch (Exception var3) {
            Moonlight.LOGGER.warn("Failed to load custom map decoration for decoration type" + this + ": " + var3);
            return null;
        }
    }

    @Nullable
    @Override
    public M loadMarkerFromNBT(CompoundTag compound) {
        M marker = (M) this.markerFactory.apply(this);
        try {
            marker.loadFromNBT(compound);
            return marker;
        } catch (Exception var4) {
            Moonlight.LOGGER.warn("Failed to load world map marker for decoration type" + this + ": " + var4);
            return null;
        }
    }

    @Nullable
    @Override
    public M getWorldMarkerFromWorld(BlockGetter reader, BlockPos pos) {
        return (M) (this.markerFromWorldFactory != null ? this.markerFromWorldFactory.apply(reader, pos) : null);
    }

    @Override
    public M createEmptyMarker() {
        return (M) this.markerFactory.apply(this);
    }
}