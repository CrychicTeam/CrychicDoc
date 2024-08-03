package net.blay09.mods.waystones.core;

import com.google.common.collect.Lists;
import java.util.Objects;
import java.util.UUID;
import net.blay09.mods.waystones.api.IMutableWaystone;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.TeleportDestination;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.tag.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Waystone implements IWaystone, IMutableWaystone {

    private final ResourceLocation waystoneType;

    private final UUID waystoneUid;

    private final WaystoneOrigin origin;

    private ResourceKey<Level> dimension;

    private BlockPos pos;

    private String name = "";

    private boolean isGlobal;

    private UUID ownerUid;

    public Waystone(ResourceLocation waystoneType, UUID waystoneUid, ResourceKey<Level> dimension, BlockPos pos, WaystoneOrigin origin, @Nullable UUID ownerUid) {
        this.waystoneType = waystoneType;
        this.waystoneUid = waystoneUid;
        this.dimension = dimension;
        this.pos = pos;
        this.origin = origin;
        this.ownerUid = ownerUid;
    }

    @Override
    public UUID getWaystoneUid() {
        return this.waystoneUid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    @Override
    public WaystoneOrigin getOrigin() {
        return this.origin;
    }

    @Override
    public boolean isGlobal() {
        return this.isGlobal;
    }

    @Override
    public void setGlobal(boolean global) {
        this.isGlobal = global;
    }

    @Override
    public boolean isOwner(Player player) {
        return this.ownerUid == null || player.getGameProfile().getId().equals(this.ownerUid) || player.getAbilities().instabuild;
    }

    @Override
    public void setOwnerUid(@Nullable UUID ownerUid) {
        this.ownerUid = ownerUid;
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public UUID getOwnerUid() {
        return this.ownerUid;
    }

    @Override
    public void setDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
    }

    @Override
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public ResourceLocation getWaystoneType() {
        return this.waystoneType;
    }

    @Override
    public boolean isValidInLevel(ServerLevel level) {
        BlockState state = level.m_8055_(this.pos);
        return state.m_204336_(ModBlockTags.IS_TELEPORT_TARGET);
    }

    @Override
    public TeleportDestination resolveDestination(ServerLevel level) {
        BlockState state = level.m_8055_(this.pos);
        Direction direction = (Direction) state.m_61143_(WaystoneBlock.FACING);
        for (Direction candidate : Lists.newArrayList(new Direction[] { direction, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH })) {
            BlockPos offsetPos = this.pos.relative(candidate);
            BlockPos offsetPosUp = offsetPos.above();
            if (!level.m_8055_(offsetPos).m_60828_(level, offsetPos) && !level.m_8055_(offsetPosUp).m_60828_(level, offsetPosUp)) {
                direction = candidate;
                break;
            }
        }
        BlockPos targetPos = this.getWaystoneType().equals(WaystoneTypes.WARP_PLATE) ? this.getPos() : this.getPos().relative(direction);
        Vec3 location = new Vec3((double) targetPos.m_123341_() + 0.5, (double) targetPos.m_123342_() + 0.5, (double) targetPos.m_123343_() + 0.5);
        return new TeleportDestination(level, location, direction);
    }

    public static IWaystone read(FriendlyByteBuf buf) {
        UUID waystoneUid = buf.readUUID();
        ResourceLocation waystoneType = buf.readResourceLocation();
        String name = buf.readUtf();
        boolean isGlobal = buf.readBoolean();
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(buf.readUtf(250)));
        BlockPos pos = buf.readBlockPos();
        WaystoneOrigin origin = buf.readEnum(WaystoneOrigin.class);
        Waystone waystone = new Waystone(waystoneType, waystoneUid, dimension, pos, origin, null);
        waystone.setName(name);
        waystone.setGlobal(isGlobal);
        return waystone;
    }

    public static IWaystone read(CompoundTag compound) {
        UUID waystoneUid = NbtUtils.loadUUID((Tag) Objects.requireNonNull(compound.get("WaystoneUid")));
        String name = compound.getString("Name");
        ResourceKey<Level> dimensionType = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(compound.getString("World")));
        BlockPos pos = NbtUtils.readBlockPos(compound.getCompound("BlockPos"));
        boolean wasGenerated = compound.getBoolean("WasGenerated");
        WaystoneOrigin origin = wasGenerated ? WaystoneOrigin.WILDERNESS : WaystoneOrigin.UNKNOWN;
        if (compound.contains("Origin")) {
            try {
                origin = WaystoneOrigin.valueOf(compound.getString("Origin"));
            } catch (IllegalArgumentException var10) {
            }
        }
        UUID ownerUid = compound.contains("OwnerUid") ? NbtUtils.loadUUID((Tag) Objects.requireNonNull(compound.get("OwnerUid"))) : null;
        ResourceLocation waystoneType = compound.contains("Type") ? new ResourceLocation(compound.getString("Type")) : WaystoneTypes.WAYSTONE;
        Waystone waystone = new Waystone(waystoneType, waystoneUid, dimensionType, pos, origin, ownerUid);
        waystone.setName(name);
        waystone.setGlobal(compound.getBoolean("IsGlobal"));
        return waystone;
    }

    public static void write(FriendlyByteBuf buf, IWaystone waystone) {
        buf.writeUUID(waystone.getWaystoneUid());
        buf.writeResourceLocation(waystone.getWaystoneType());
        buf.writeUtf(waystone.getName());
        buf.writeBoolean(waystone.isGlobal());
        buf.writeResourceLocation(waystone.getDimension().location());
        buf.writeBlockPos(waystone.getPos());
        buf.writeEnum(waystone.getOrigin());
    }

    public static CompoundTag write(IWaystone waystone, CompoundTag compound) {
        compound.put("WaystoneUid", NbtUtils.createUUID(waystone.getWaystoneUid()));
        compound.putString("Type", waystone.getWaystoneType().toString());
        compound.putString("Name", waystone.getName());
        compound.putString("World", waystone.getDimension().location().toString());
        compound.put("BlockPos", NbtUtils.writeBlockPos(waystone.getPos()));
        compound.putString("Origin", waystone.getOrigin().name());
        if (waystone.getOwnerUid() != null) {
            compound.put("OwnerUid", NbtUtils.createUUID(waystone.getOwnerUid()));
        }
        compound.putBoolean("IsGlobal", waystone.isGlobal());
        return compound;
    }
}