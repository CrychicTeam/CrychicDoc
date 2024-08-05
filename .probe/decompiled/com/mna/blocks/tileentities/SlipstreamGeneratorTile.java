package com.mna.blocks.tileentities;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.BlockInit;
import com.mna.blocks.artifice.SlipstreamGeneratorBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SlipstreamGeneratorTile extends BlockEntity {

    private ArrayList<Entity> levitatingEntities;

    private int updateTicks = 1;

    private Direction levitateDir = null;

    private boolean playersOnly = true;

    private AABB bb = null;

    private static final int EFFECT_HEIGHT = 50;

    private static final int MAX_STACK = 5;

    private static final float VELOCITY_SCALE = 0.5999999F;

    private static final float STOP_THRESHOLD = 0.2F;

    private static final float VELOCITY_ADD = 0.2F;

    private static final float PLAYER_VELOCITY_ADD = 0.75F;

    public SlipstreamGeneratorTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.SLIPSTREAM_GENERATOR.get(), pos, state);
        this.levitatingEntities = new ArrayList();
    }

    private Direction getLevitateDir() {
        if (this.levitateDir == null) {
            BlockState state = this.m_58900_();
            if (state.m_61138_(SlipstreamGeneratorBlock.FACING)) {
                this.levitateDir = (Direction) state.m_61143_(SlipstreamGeneratorBlock.FACING);
            } else {
                this.levitateDir = Direction.UP;
            }
        }
        return this.levitateDir;
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, SlipstreamGeneratorTile tile) {
        if (tile.updateTicks++ > 10) {
            tile.refreshPushAxis();
            tile.refreshEntityList();
            tile.updateTicks = 0;
        }
        if (tile.bb != null) {
            Iterator<Entity> it = tile.levitatingEntities.iterator();
            while (it.hasNext()) {
                Entity entity = (Entity) it.next();
                if (entity instanceof Player) {
                    if (!tile.levitatePlayer((Player) entity, tile.getLevitateDir().getAxis())) {
                        it.remove();
                    }
                } else if (!tile.pushEntity(entity)) {
                    it.remove();
                }
            }
        }
    }

    private boolean pushEntity(Entity entity) {
        if (!this.entityIsValid(entity)) {
            return false;
        } else {
            entity.setDeltaMovement(Vec3.ZERO);
            entity.setDeltaMovement((double) ((float) this.getLevitateDir().getStepX() * 0.2F), (double) ((float) this.getLevitateDir().getStepY() * 0.2F), (double) ((float) this.getLevitateDir().getStepZ() * 0.2F));
            return true;
        }
    }

    private boolean levitatePlayer(Player player, Direction.Axis axis) {
        if (!this.entityIsValid(player)) {
            return false;
        } else {
            this.slowPlayerDown(player, false);
            if (!player.m_6144_()) {
                if (axis == Direction.Axis.Y) {
                    this.levitatePlayerYAxis(player);
                } else {
                    this.levitatePlayerHorizontally(player, axis);
                }
            }
            if (this.m_58904_().isClientSide()) {
                this.spawnParticles(player);
            }
            return true;
        }
    }

    private void slowPlayerDown(Player player, boolean xz) {
        double x = player.m_20184_().x;
        double y = player.m_20184_().y * 0.5999999F;
        double z = player.m_20184_().z;
        if (this.getLevitateDir().getAxis() == Direction.Axis.X) {
            x *= 0.5999999F;
        } else if (this.getLevitateDir().getAxis() == Direction.Axis.Z) {
            z *= 0.5999999F;
        }
        player.m_20334_(x, y, z);
        if (Math.abs(player.m_20184_().y) < 0.2F) {
            player.m_5997_(0.0, -player.m_20184_().y, 0.0);
            player.f_19789_ = 0.0F;
        } else {
            player.f_19789_--;
        }
        if (this.getLevitateDir().getAxis() == Direction.Axis.X && Math.abs(player.m_20184_().x) < 0.2F) {
            player.m_5997_(-player.m_20184_().x, 0.0, 0.0);
        }
        if (this.getLevitateDir().getAxis() == Direction.Axis.Z && Math.abs(player.m_20184_().z) < 0.2F) {
            player.m_5997_(0.0, 0.0, -player.m_20184_().z);
        }
    }

    private void levitatePlayerYAxis(Player player) {
        float pitch = player.m_146909_();
        float factor = (pitch > 0.0F ? pitch - 10.0F : pitch + 10.0F) / -180.0F;
        if (Math.abs(pitch) > 10.0F) {
            player.m_6478_(MoverType.PLAYER, new Vec3(0.0, (double) factor, 0.0));
        }
    }

    private void levitatePlayerHorizontally(Player player, Direction.Axis axis) {
        float factorX = 0.0F;
        float factorZ = 0.0F;
        Vec3 posX = new Vec3(1.0, 0.0, 0.0);
        Vec3 negX = new Vec3(-1.0, 0.0, 0.0);
        Vec3 posZ = new Vec3(0.0, 0.0, 1.0);
        Vec3 negZ = new Vec3(0.0, 0.0, -1.0);
        Vec3 playerH = new Vec3(player.m_20154_().x, 0.0, player.m_20154_().z);
        if (axis == Direction.Axis.X) {
            double dotPosX = posX.dot(playerH);
            double dotNegX = negX.dot(playerH);
            if (dotPosX > 0.2) {
                factorX = (float) dotPosX;
            } else if (dotNegX > 0.2) {
                factorX = (float) dotNegX * -1.0F;
            }
            factorX *= 0.75F;
        } else if (axis == Direction.Axis.Z) {
            double dotPosZ = posZ.dot(playerH);
            double dotNegZ = negZ.dot(playerH);
            if (dotPosZ > 0.2) {
                factorZ = (float) dotPosZ;
            } else if (dotNegZ > 0.2) {
                factorZ = (float) dotNegZ * -1.0F;
            }
            factorZ *= 0.75F;
        }
        double deltaY = (double) this.m_58899_().m_123342_() - player.m_20186_() + 0.5;
        float yThreshold = 0.1F;
        if (Math.abs(deltaY) < (double) yThreshold) {
            deltaY = 0.0;
        } else if (deltaY != 0.0) {
            deltaY = (double) yThreshold * Math.signum(deltaY);
        }
        if (Math.abs(factorX) > 0.0F || Math.abs(factorZ) > 0.0F || deltaY > 0.0) {
            player.m_6478_(MoverType.PLAYER, new Vec3((double) factorX, deltaY, (double) factorZ));
        }
    }

    private void spawnParticles(Player player) {
        float pitch = player.m_146909_();
        float factor = (pitch > 0.0F ? pitch - 10.0F : pitch + 10.0F) / -180.0F;
        if (player.m_6144_()) {
            factor = 0.01F;
        }
        for (int i = 0; i < 5; i++) {
            this.m_58904_().addParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.2F).setColor(10, 10, 10), player.m_20185_(), player.m_6144_() ? player.m_20186_() : player.m_20186_() + (double) factor + Math.random() * (double) player.m_20206_(), player.m_20189_(), 0.1F, (double) factor, 1.0);
        }
    }

    private boolean entityIsValid(Entity player) {
        if (player != null && player.isAlive()) {
            Vec3 myPos = new Vec3((double) this.m_58899_().m_123341_() + 0.5, (double) this.m_58899_().m_123342_() + 0.5, (double) this.m_58899_().m_123343_() + 0.5);
            if (player.getBoundingBox().intersects(this.bb)) {
                BlockHitResult result = this.f_58857_.m_45547_(new ClipContext(player.getEyePosition(0.0F), myPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
                if (result.getType() == HitResult.Type.MISS) {
                    return true;
                }
                if (result.getType() == HitResult.Type.BLOCK) {
                    return result.getBlockPos().equals(this.m_58899_());
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private void refreshEntityList() {
        this.levitatingEntities.clear();
        if (this.bb != null) {
            if (this.playersOnly) {
                this.levitatingEntities.addAll((Collection) this.m_58904_().m_6907_().stream().filter(p -> this.entityIsValid(p) && !this.levitatingEntities.contains(p)).collect(Collectors.toList()));
            } else {
                this.levitatingEntities.addAll(this.m_58904_().m_45976_(Entity.class, this.bb));
            }
        }
    }

    private void refreshPushAxis() {
        this.bb = null;
        int axisStacks = this.countStacksAlongAxis(this.getLevitateDir().getOpposite());
        BlockPos bbOffset = this.m_58899_().relative(this.getLevitateDir(), (axisStacks + 1) * 50);
        this.bb = new AABB(this.m_58899_(), bbOffset.offset(this.getLevitateDir().getStepX() == 0 ? 1 : 0, this.getLevitateDir().getStepY() == 0 ? 1 : 0, this.getLevitateDir().getStepZ() == 0 ? 1 : 0));
    }

    private int countStacksAlongAxis(Direction axis) {
        int count = 0;
        for (BlockPos checkPos = this.m_58899_().offset(axis.getNormal()); this.m_58904_().getBlockState(checkPos).m_60734_() == BlockInit.SLIPSTREAM_GENERATOR.get() && count < 5; checkPos = checkPos.offset(axis.getNormal())) {
            count++;
        }
        return count;
    }
}