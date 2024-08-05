package net.mehvahdjukaar.moonlight.api.map.type;

import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface MapDecorationType<D extends CustomMapDecoration, M extends MapBlockMarker<D>> {

    @Internal
    boolean isFromWorld();

    default ResourceLocation getCustomFactoryID() {
        return new ResourceLocation("");
    }

    @Nullable
    D loadDecorationFromBuffer(FriendlyByteBuf var1);

    M createEmptyMarker();

    @Nullable
    M loadMarkerFromNBT(CompoundTag var1);

    @Nullable
    M getWorldMarkerFromWorld(BlockGetter var1, BlockPos var2);

    default int getDefaultMapColor() {
        return 1;
    }

    default Optional<HolderSet<Structure>> getAssociatedStructure() {
        return Optional.empty();
    }
}