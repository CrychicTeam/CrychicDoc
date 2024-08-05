package com.mna.blocks.tileentities;

import com.mna.ManaAndArtifice;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.sorcery.TransitoryTileBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TransitoryTile extends BlockEntity {

    private float topFaceAlpha = 0.0F;

    private boolean wasSteppedOn = false;

    private byte neighborMatrix = 0;

    private int age = 0;

    private int color = -1;

    public TransitoryTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public TransitoryTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.TRANSITORY_TILE.get(), pos, state);
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, TransitoryTile tile) {
        tile.age++;
        if (tile.m_58904_().isClientSide()) {
            if (tile.m_58904_().m_45976_(Entity.class, new AABB(tile.f_58858_.above())).size() > 0) {
                tile.topFaceAlpha = 1.0F;
            } else {
                tile.topFaceAlpha -= 0.05F;
            }
            if (!tile.wasSteppedOn && tile.topFaceAlpha > 0.0F) {
                tile.particleBurst();
            }
            tile.wasSteppedOn = tile.topFaceAlpha > 0.0F;
            for (Direction d : Direction.values()) {
                if (!tile.hasNeighborOnSide(d)) {
                    tile.SpawnParticlesForFace(pos, d);
                }
            }
        } else if (tile.age > (Integer) tile.m_58900_().m_61143_(TransitoryTileBlock.DURATION) * 20) {
            tile.m_58904_().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    private void SpawnParticlesForFace(BlockPos pos, Direction face) {
        if (!ManaAndArtifice.instance.proxy.isGamePaused()) {
            Vec3 offset = null;
            RandomSource rand = this.f_58857_.random;
            switch(face) {
                case DOWN:
                    if (rand.nextBoolean()) {
                        if (!this.hasNeighborOnSide(Direction.EAST) && !this.hasNeighborOnSide(Direction.WEST)) {
                            offset = new Vec3(rand.nextBoolean() ? 1.0 : 0.0, 0.0, rand.nextDouble());
                        } else if (!this.hasNeighborOnSide(Direction.EAST)) {
                            offset = new Vec3(1.0, 0.0, rand.nextDouble());
                        } else if (!this.hasNeighborOnSide(Direction.WEST)) {
                            offset = new Vec3(0.0, 0.0, rand.nextDouble());
                        }
                    } else if (!this.hasNeighborOnSide(Direction.NORTH) && !this.hasNeighborOnSide(Direction.SOUTH)) {
                        offset = new Vec3(rand.nextDouble(), 0.0, rand.nextBoolean() ? 1.0 : 0.0);
                    } else if (!this.hasNeighborOnSide(Direction.NORTH)) {
                        offset = new Vec3(rand.nextDouble(), 0.0, 0.0);
                    } else if (!this.hasNeighborOnSide(Direction.SOUTH)) {
                        offset = new Vec3(rand.nextDouble(), 0.0, 1.0);
                    }
                    break;
                case EAST:
                    if (rand.nextBoolean()) {
                        if (!this.hasNeighborOnSide(Direction.UP) && !this.hasNeighborOnSide(Direction.DOWN)) {
                            offset = new Vec3(1.0, rand.nextBoolean() ? 1.0 : 0.0, rand.nextDouble());
                        } else if (!this.hasNeighborOnSide(Direction.UP)) {
                            offset = new Vec3(1.0, 1.0, rand.nextDouble());
                        } else if (!this.hasNeighborOnSide(Direction.DOWN)) {
                            offset = new Vec3(1.0, 0.0, rand.nextDouble());
                        }
                    } else if (!this.hasNeighborOnSide(Direction.NORTH) && !this.hasNeighborOnSide(Direction.SOUTH)) {
                        offset = new Vec3(1.0, rand.nextDouble(), rand.nextBoolean() ? 1.0 : 0.0);
                    } else if (!this.hasNeighborOnSide(Direction.NORTH)) {
                        offset = new Vec3(1.0, rand.nextDouble(), 0.0);
                    } else if (!this.hasNeighborOnSide(Direction.SOUTH)) {
                        offset = new Vec3(1.0, rand.nextDouble(), 1.0);
                    }
                    break;
                case WEST:
                    if (rand.nextBoolean()) {
                        if (!this.hasNeighborOnSide(Direction.UP) && !this.hasNeighborOnSide(Direction.DOWN)) {
                            offset = new Vec3(0.0, rand.nextBoolean() ? 1.0 : 0.0, rand.nextDouble());
                        } else if (!this.hasNeighborOnSide(Direction.UP)) {
                            offset = new Vec3(0.0, 1.0, rand.nextDouble());
                        } else if (!this.hasNeighborOnSide(Direction.DOWN)) {
                            offset = new Vec3(0.0, 0.0, rand.nextDouble());
                        }
                    } else if (!this.hasNeighborOnSide(Direction.NORTH) && !this.hasNeighborOnSide(Direction.SOUTH)) {
                        offset = new Vec3(0.0, rand.nextDouble(), rand.nextBoolean() ? 1.0 : 0.0);
                    } else if (!this.hasNeighborOnSide(Direction.NORTH)) {
                        offset = new Vec3(0.0, rand.nextDouble(), 0.0);
                    } else if (!this.hasNeighborOnSide(Direction.SOUTH)) {
                        offset = new Vec3(0.0, rand.nextDouble(), 1.0);
                    }
                    break;
                case NORTH:
                    if (rand.nextBoolean()) {
                        if (!this.hasNeighborOnSide(Direction.UP) && !this.hasNeighborOnSide(Direction.DOWN)) {
                            offset = new Vec3(rand.nextDouble(), rand.nextBoolean() ? 1.0 : 0.0, 0.0);
                        } else if (!this.hasNeighborOnSide(Direction.UP)) {
                            offset = new Vec3(rand.nextDouble(), 1.0, 0.0);
                        } else if (!this.hasNeighborOnSide(Direction.DOWN)) {
                            offset = new Vec3(rand.nextDouble(), 0.0, 0.0);
                        }
                    } else if (!this.hasNeighborOnSide(Direction.EAST) && !this.hasNeighborOnSide(Direction.WEST)) {
                        offset = new Vec3(rand.nextBoolean() ? 1.0 : 0.0, rand.nextDouble(), 0.0);
                    } else if (!this.hasNeighborOnSide(Direction.EAST)) {
                        offset = new Vec3(1.0, rand.nextDouble(), 0.0);
                    } else if (!this.hasNeighborOnSide(Direction.WEST)) {
                        offset = new Vec3(0.0, rand.nextDouble(), 0.0);
                    }
                    break;
                case SOUTH:
                    if (rand.nextBoolean()) {
                        if (!this.hasNeighborOnSide(Direction.UP) && !this.hasNeighborOnSide(Direction.DOWN)) {
                            offset = new Vec3(rand.nextDouble(), rand.nextBoolean() ? 1.0 : 0.0, 1.0);
                        } else if (!this.hasNeighborOnSide(Direction.UP)) {
                            offset = new Vec3(rand.nextDouble(), 1.0, 1.0);
                        } else if (!this.hasNeighborOnSide(Direction.DOWN)) {
                            offset = new Vec3(rand.nextDouble(), 0.0, 1.0);
                        }
                    } else if (!this.hasNeighborOnSide(Direction.EAST) && !this.hasNeighborOnSide(Direction.WEST)) {
                        offset = new Vec3(rand.nextBoolean() ? 1.0 : 0.0, rand.nextDouble(), 1.0);
                    } else if (!this.hasNeighborOnSide(Direction.EAST)) {
                        offset = new Vec3(1.0, rand.nextDouble(), 1.0);
                    } else if (!this.hasNeighborOnSide(Direction.WEST)) {
                        offset = new Vec3(0.0, rand.nextDouble(), 1.0);
                    }
                    break;
                case UP:
                    if (rand.nextBoolean()) {
                        if (!this.hasNeighborOnSide(Direction.EAST) && !this.hasNeighborOnSide(Direction.WEST)) {
                            offset = new Vec3(rand.nextBoolean() ? 1.0 : 0.0, 1.0, rand.nextDouble());
                        } else if (!this.hasNeighborOnSide(Direction.EAST)) {
                            offset = new Vec3(1.0, 1.0, rand.nextDouble());
                        } else if (!this.hasNeighborOnSide(Direction.WEST)) {
                            offset = new Vec3(0.0, 1.0, rand.nextDouble());
                        }
                    } else if (!this.hasNeighborOnSide(Direction.NORTH) && !this.hasNeighborOnSide(Direction.SOUTH)) {
                        offset = new Vec3(rand.nextDouble(), 1.0, rand.nextBoolean() ? 1.0 : 0.0);
                    } else if (!this.hasNeighborOnSide(Direction.NORTH)) {
                        offset = new Vec3(rand.nextDouble(), 1.0, 0.0);
                    } else if (!this.hasNeighborOnSide(Direction.SOUTH)) {
                        offset = new Vec3(rand.nextDouble(), 1.0, 1.0);
                    }
            }
            if (offset != null) {
                MAParticleType pfx = new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get());
                if (this.color != -1) {
                    pfx.setColor(FastColor.ARGB32.red(this.color), FastColor.ARGB32.green(this.color), FastColor.ARGB32.blue(this.color));
                }
                this.m_58904_().addParticle(pfx, (double) pos.m_123341_() + offset.x, (double) pos.m_123342_() + offset.y, (double) pos.m_123343_() + offset.z, 0.0, 0.0, 0.0);
            }
        }
    }

    public void setNeighborOnSide(Direction side) {
        if (side != null) {
            this.neighborMatrix = (byte) (this.neighborMatrix | 1 << side.ordinal());
            if (!this.m_58904_().isClientSide()) {
                this.m_58904_().sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
            }
        }
    }

    public void clearNeighborOnSide(Direction side) {
        if (side != null) {
            this.neighborMatrix = (byte) (this.neighborMatrix & ~(1 << side.ordinal()));
            if (!this.m_58904_().isClientSide()) {
                this.m_58904_().sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
            }
        }
    }

    public boolean hasNeighborOnSide(Direction side) {
        if (side == null) {
            return false;
        } else {
            byte value = (byte) (1 << side.ordinal());
            return (this.neighborMatrix & value) != 0;
        }
    }

    public float topFaceAlpha() {
        return this.topFaceAlpha;
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        data.putByte("neighbors", this.neighborMatrix);
        data.putInt("color", this.color);
    }

    @Override
    public void load(CompoundTag data) {
        super.load(data);
        if (data.contains("neighbors")) {
            this.neighborMatrix = data.getByte("neighbors");
        }
        if (data.contains("color")) {
            this.color = data.getInt("color");
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag data = pkt.getTag();
        if (data.contains("neighbors")) {
            this.neighborMatrix = data.getByte("neighbors");
        }
        if (data.contains("color")) {
            this.color = data.getInt("color");
        }
    }

    private void particleBurst() {
        float velocityMod = 0.2F;
        for (int i = 0; i < 5; i++) {
            MAParticleType pfx = new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get());
            if (this.color != -1) {
                pfx.setColor(FastColor.ARGB32.red(this.color), FastColor.ARGB32.green(this.color), FastColor.ARGB32.blue(this.color));
            }
            this.m_58904_().addParticle(pfx, (double) this.f_58858_.m_123341_() + Math.random(), (double) this.f_58858_.m_123342_() + Math.random(), (double) this.f_58858_.m_123343_() + Math.random(), (Math.random() - 0.5) * (double) velocityMod, Math.random() * (double) velocityMod, (Math.random() - 0.5) * (double) velocityMod);
        }
    }

    public void setColor(int color) {
        this.color = color;
        if (!this.m_58904_().isClientSide()) {
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        }
    }
}