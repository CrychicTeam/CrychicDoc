package noppes.npcs.blocks.tiles;

import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import noppes.npcs.CustomBlocks;
import noppes.npcs.blocks.BlockBorder;
import noppes.npcs.controllers.data.Availability;

public class TileBorder extends TileNpcEntity implements Predicate {

    public Availability availability = new Availability();

    public AABB boundingbox;

    public int rotation = 0;

    public int height = 10;

    public String message = "availability.areaNotAvailble";

    public TileBorder(BlockPos pos, BlockState state) {
        super(CustomBlocks.tile_border, pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.readExtraNBT(compound);
        if (this.m_58904_() != null) {
            this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) CustomBlocks.border.defaultBlockState().m_61124_(BlockBorder.ROTATION, this.rotation));
        }
    }

    public void readExtraNBT(CompoundTag compound) {
        this.availability.load(compound.getCompound("BorderAvailability"));
        this.rotation = compound.getInt("BorderRotation");
        this.height = compound.getInt("BorderHeight");
        this.message = compound.getString("BorderMessage");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        this.writeExtraNBT(compound);
        super.saveAdditional(compound);
    }

    public void writeExtraNBT(CompoundTag compound) {
        compound.put("BorderAvailability", this.availability.save(new CompoundTag()));
        compound.putInt("BorderRotation", this.rotation);
        compound.putInt("BorderHeight", this.height);
        compound.putString("BorderMessage", this.message);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileBorder tile) {
        if (!level.isClientSide) {
            AABB box = new AABB((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), (double) (pos.m_123341_() + 1), (double) (pos.m_123342_() + tile.height + 1), (double) (pos.m_123343_() + 1));
            for (Entity entity : level.m_6443_(Entity.class, box, tile)) {
                if (entity instanceof ThrownEnderpearl) {
                    ThrownEnderpearl pearl = (ThrownEnderpearl) entity;
                    if (pearl.m_19749_() instanceof Player && !tile.availability.isAvailable((Player) pearl.m_19749_())) {
                        entity.setRemoved(Entity.RemovalReason.DISCARDED);
                    }
                } else {
                    Player player = (Player) entity;
                    if (!tile.availability.isAvailable(player)) {
                        BlockPos pos2 = new BlockPos(tile.f_58858_);
                        if (tile.rotation == 2) {
                            pos2 = pos2.south();
                        } else if (tile.rotation == 0) {
                            pos2 = pos2.north();
                        } else if (tile.rotation == 1) {
                            pos2 = pos2.east();
                        } else if (tile.rotation == 3) {
                            pos2 = pos2.west();
                        }
                        while (!level.m_46859_(pos2)) {
                            pos2 = pos2.above();
                        }
                        player.m_6021_((double) pos2.m_123341_() + 0.5, (double) pos2.m_123342_(), (double) pos2.m_123343_() + 0.5);
                        if (!tile.message.isEmpty()) {
                            player.displayClientMessage(Component.translatable(tile.message), true);
                        }
                    }
                }
            }
        }
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.handleUpdateTag(pkt.getTag());
    }

    public void handleUpdateTag(CompoundTag compound) {
        this.rotation = compound.getInt("Rotation");
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("x", this.f_58858_.m_123341_());
        compound.putInt("y", this.f_58858_.m_123342_());
        compound.putInt("z", this.f_58858_.m_123343_());
        compound.putInt("Rotation", this.rotation);
        return compound;
    }

    public boolean isEntityApplicable(Entity var1) {
        return var1 instanceof ServerPlayer || var1 instanceof ThrownEnderpearl;
    }

    public boolean apply(Object ob) {
        return this.isEntityApplicable((Entity) ob);
    }
}