package net.blay09.mods.waystones.api;

import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.blay09.mods.waystones.core.WarpMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface InternalMethods {

    Either<IWaystoneTeleportContext, WaystoneTeleportError> createDefaultTeleportContext(Entity var1, IWaystone var2, WarpMode var3, @Nullable IWaystone var4);

    Either<IWaystoneTeleportContext, WaystoneTeleportError> createCustomTeleportContext(Entity var1, IWaystone var2);

    Either<List<Entity>, WaystoneTeleportError> tryTeleportToWaystone(Entity var1, IWaystone var2, WarpMode var3, IWaystone var4);

    Either<List<Entity>, WaystoneTeleportError> tryTeleport(IWaystoneTeleportContext var1);

    Either<List<Entity>, WaystoneTeleportError> forceTeleportToWaystone(Entity var1, IWaystone var2);

    List<Entity> forceTeleport(IWaystoneTeleportContext var1);

    Optional<IWaystone> getWaystoneAt(Level var1, BlockPos var2);

    Optional<IWaystone> getWaystone(Level var1, UUID var2);

    ItemStack createAttunedShard(IWaystone var1);

    ItemStack createBoundScroll(IWaystone var1);

    Optional<IWaystone> placeWaystone(Level var1, BlockPos var2, WaystoneStyle var3);

    Optional<IWaystone> placeSharestone(Level var1, BlockPos var2, DyeColor var3);

    Optional<IWaystone> placeWarpPlate(Level var1, BlockPos var2);

    Optional<IWaystone> getBoundWaystone(ItemStack var1);

    void setBoundWaystone(ItemStack var1, @Nullable IWaystone var2);
}