package net.blay09.mods.waystones.core;

import java.util.UUID;
import net.blay09.mods.waystones.api.IMutableWaystone;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.TeleportDestination;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class WaystoneProxy implements IWaystone, IMutableWaystone {

    private final MinecraftServer server;

    private final UUID waystoneUid;

    private IWaystone backingWaystone;

    public WaystoneProxy(@Nullable MinecraftServer server, UUID waystoneUid) {
        this.server = server;
        this.waystoneUid = waystoneUid;
    }

    @Override
    public boolean isValidInLevel(ServerLevel level) {
        return this.getBackingWaystone().isValidInLevel(level);
    }

    @Override
    public TeleportDestination resolveDestination(ServerLevel level) {
        return this.getBackingWaystone().resolveDestination(level);
    }

    @Override
    public boolean isValid() {
        return WaystoneManager.get(this.server).getWaystoneById(this.waystoneUid).isPresent();
    }

    public IWaystone getBackingWaystone() {
        if (this.backingWaystone == null) {
            this.backingWaystone = (IWaystone) WaystoneManager.get(this.server).getWaystoneById(this.waystoneUid).orElse(InvalidWaystone.INSTANCE);
        }
        return this.backingWaystone;
    }

    @Override
    public UUID getOwnerUid() {
        return this.getBackingWaystone().getOwnerUid();
    }

    @Override
    public UUID getWaystoneUid() {
        return this.waystoneUid;
    }

    @Override
    public String getName() {
        return this.getBackingWaystone().getName();
    }

    @Override
    public ResourceKey<Level> getDimension() {
        return this.getBackingWaystone().getDimension();
    }

    @Override
    public boolean wasGenerated() {
        return this.getBackingWaystone().wasGenerated();
    }

    @Override
    public WaystoneOrigin getOrigin() {
        return this.getBackingWaystone().getOrigin();
    }

    @Override
    public boolean isGlobal() {
        return this.getBackingWaystone().isGlobal();
    }

    @Override
    public boolean isOwner(Player player) {
        return this.getBackingWaystone().isOwner(player);
    }

    @Override
    public BlockPos getPos() {
        return this.getBackingWaystone().getPos();
    }

    @Override
    public ResourceLocation getWaystoneType() {
        return this.getBackingWaystone().getWaystoneType();
    }

    @Override
    public void setName(String name) {
        IWaystone backingWaystone = this.getBackingWaystone();
        if (backingWaystone instanceof IMutableWaystone) {
            ((IMutableWaystone) backingWaystone).setName(name);
        }
    }

    @Override
    public void setGlobal(boolean global) {
        IWaystone backingWaystone = this.getBackingWaystone();
        if (backingWaystone instanceof IMutableWaystone) {
            ((IMutableWaystone) backingWaystone).setGlobal(global);
        }
    }

    @Override
    public void setDimension(ResourceKey<Level> dimension) {
        IWaystone backingWaystone = this.getBackingWaystone();
        if (backingWaystone instanceof IMutableWaystone) {
            ((IMutableWaystone) backingWaystone).setDimension(dimension);
        }
    }

    @Override
    public void setPos(BlockPos pos) {
        IWaystone backingWaystone = this.getBackingWaystone();
        if (backingWaystone instanceof IMutableWaystone) {
            ((IMutableWaystone) backingWaystone).setPos(pos);
        }
    }

    @Override
    public void setOwnerUid(UUID ownerUid) {
        IWaystone backingWaystone = this.getBackingWaystone();
        if (backingWaystone instanceof IMutableWaystone) {
            ((IMutableWaystone) backingWaystone).setOwnerUid(ownerUid);
        }
    }
}