package net.blay09.mods.waystones;

import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IAttunementItem;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.IWaystoneTeleportContext;
import net.blay09.mods.waystones.api.InternalMethods;
import net.blay09.mods.waystones.api.WaystoneStyle;
import net.blay09.mods.waystones.api.WaystoneTeleportError;
import net.blay09.mods.waystones.api.WaystonesAPI;
import net.blay09.mods.waystones.block.ModBlocks;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTeleportContext;
import net.blay09.mods.waystones.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

public class InternalMethodsImpl implements InternalMethods {

    @Override
    public Either<IWaystoneTeleportContext, WaystoneTeleportError> createDefaultTeleportContext(Entity entity, IWaystone waystone, WarpMode warpMode, @Nullable IWaystone fromWaystone) {
        return WaystonesAPI.createCustomTeleportContext(entity, waystone).ifLeft(context -> {
            context.setWarpMode(warpMode);
            context.getLeashedEntities().addAll(PlayerWaystoneManager.findLeashedAnimals(entity));
            context.setFromWaystone(fromWaystone);
            context.setWarpItem(PlayerWaystoneManager.findWarpItem(entity, warpMode));
            context.setCooldown(PlayerWaystoneManager.getCooldownPeriod(warpMode, waystone));
            context.setXpCost(PlayerWaystoneManager.getExperienceLevelCost(entity, waystone, warpMode, context));
        });
    }

    @Override
    public Either<IWaystoneTeleportContext, WaystoneTeleportError> createCustomTeleportContext(Entity entity, IWaystone waystone) {
        if (!waystone.isValid()) {
            return Either.right(new WaystoneTeleportError.InvalidWaystone(waystone));
        } else {
            MinecraftServer server = entity.getServer();
            if (server == null) {
                return Either.right(new WaystoneTeleportError.NotOnServer());
            } else {
                ServerLevel targetLevel = server.getLevel(waystone.getDimension());
                if (targetLevel == null) {
                    return Either.right(new WaystoneTeleportError.InvalidDimension(waystone.getDimension()));
                } else {
                    return !waystone.isValidInLevel(targetLevel) ? Either.right(new WaystoneTeleportError.MissingWaystone(waystone)) : Either.left(new WaystoneTeleportContext(entity, waystone, waystone.resolveDestination(targetLevel)));
                }
            }
        }
    }

    @Override
    public Either<List<Entity>, WaystoneTeleportError> tryTeleportToWaystone(Entity entity, IWaystone waystone, WarpMode warpMode, IWaystone fromWaystone) {
        return PlayerWaystoneManager.tryTeleportToWaystone(entity, waystone, warpMode, fromWaystone);
    }

    @Override
    public Either<List<Entity>, WaystoneTeleportError> tryTeleport(IWaystoneTeleportContext context) {
        return PlayerWaystoneManager.tryTeleport(context);
    }

    @Override
    public Either<List<Entity>, WaystoneTeleportError> forceTeleportToWaystone(Entity entity, IWaystone waystone) {
        return this.createDefaultTeleportContext(entity, waystone, WarpMode.CUSTOM, null).mapLeft(this::forceTeleport);
    }

    @Override
    public List<Entity> forceTeleport(IWaystoneTeleportContext context) {
        return PlayerWaystoneManager.doTeleport(context);
    }

    @Override
    public Optional<IWaystone> getWaystoneAt(Level level, BlockPos pos) {
        return WaystoneManager.get(level.getServer()).getWaystoneAt(level, pos);
    }

    @Override
    public Optional<IWaystone> getWaystone(Level level, UUID uuid) {
        return WaystoneManager.get(level.getServer()).getWaystoneById(uuid);
    }

    @Override
    public ItemStack createAttunedShard(IWaystone warpPlate) {
        ItemStack itemStack = new ItemStack(ModItems.attunedShard);
        this.setBoundWaystone(itemStack, warpPlate);
        return itemStack;
    }

    @Override
    public ItemStack createBoundScroll(IWaystone waystone) {
        ItemStack itemStack = new ItemStack(ModItems.boundScroll);
        this.setBoundWaystone(itemStack, waystone);
        return itemStack;
    }

    @Override
    public Optional<IWaystone> placeWaystone(Level level, BlockPos pos, WaystoneStyle style) {
        Block block = Balm.getRegistries().getBlock(style.getBlockRegistryName());
        level.setBlock(pos, (BlockState) block.defaultBlockState().m_61124_(WaystoneBlock.HALF, DoubleBlockHalf.LOWER), 3);
        level.setBlock(pos.above(), (BlockState) block.defaultBlockState().m_61124_(WaystoneBlock.HALF, DoubleBlockHalf.UPPER), 3);
        return this.getWaystoneAt(level, pos);
    }

    @Override
    public Optional<IWaystone> placeSharestone(Level level, BlockPos pos, @Nullable DyeColor color) {
        Block sharestone = color != null ? ModBlocks.scopedSharestones[color.ordinal()] : ModBlocks.sharestone;
        level.setBlock(pos, (BlockState) sharestone.defaultBlockState().m_61124_(WaystoneBlock.HALF, DoubleBlockHalf.LOWER), 3);
        level.setBlock(pos.above(), (BlockState) sharestone.defaultBlockState().m_61124_(WaystoneBlock.HALF, DoubleBlockHalf.UPPER), 3);
        return this.getWaystoneAt(level, pos);
    }

    @Override
    public Optional<IWaystone> placeWarpPlate(Level level, BlockPos pos) {
        level.setBlock(pos, ModBlocks.warpPlate.defaultBlockState(), 3);
        return this.getWaystoneAt(level, pos);
    }

    @Override
    public Optional<IWaystone> getBoundWaystone(ItemStack itemStack) {
        return itemStack.getItem() instanceof IAttunementItem attunementItem ? Optional.ofNullable(attunementItem.getWaystoneAttunedTo(Balm.getHooks().getServer(), itemStack)) : Optional.empty();
    }

    @Override
    public void setBoundWaystone(ItemStack itemStack, @Nullable IWaystone waystone) {
        if (itemStack.getItem() instanceof IAttunementItem attunementItem) {
            attunementItem.setWaystoneAttunedTo(itemStack, waystone);
        }
    }
}