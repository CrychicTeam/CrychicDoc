package com.mna.blocks.tileentities;

import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.EldrinCapacitorTile;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.particles.types.movers.ParticleOrbitMover;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EldrinConduitTile extends EldrinCapacitorTile {

    public static final float CRYSTAL_OFFSET_LESSER = 1.5F;

    public static final float CRYSTAL_OFFSET = 0.75F;

    private boolean isLesser;

    public EldrinConduitTile(BlockEntityType<?> type, Affinity affinity, float capacity, boolean isLesser, BlockPos pos, BlockState state) {
        super(type, pos, state, capacity, affinity);
        this.isLesser = isLesser;
    }

    public EldrinConduitTile(Affinity affinity, float capacity, boolean isLesser, BlockPos pos, BlockState state) {
        this(TileEntityInit.ELDRIN_CONDUIT_TILE.get(), affinity, capacity, isLesser, pos, state);
    }

    public EldrinConduitTile(BlockPos pos, BlockState state) {
        this(Affinity.UNKNOWN, 1.0F, false, pos, state);
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, EldrinConduitTile tile) {
        if (level.isClientSide()) {
            switch((Affinity) tile.getAffinities().get(0)) {
                case FIRE:
                case HELLFIRE:
                    tile.spawnFireParticles();
                    break;
                case WATER:
                case ICE:
                    tile.spawnWaterParticles();
                    break;
                case WIND:
                    tile.spawnWindParticles();
                    break;
                case EARTH:
                    tile.spawnEarthParticles();
                    break;
                case ENDER:
                    tile.spawnEnderParticles();
                    break;
                case ARCANE:
                default:
                    tile.spawnArcaneParticles();
            }
            int[] color = ((Affinity) tile.getAffinities().get(0)).getColor();
            for (int i = 0; i < 5; i++) {
                tile.m_58904_().addParticle(new MAParticleType(ParticleInit.LIGHT_VELOCITY.get()).setColor(color[0], color[1], color[2]), (double) ((float) tile.m_58899_().m_123341_() + 0.4F) + Math.random() * 0.2F, (double) ((float) tile.m_58899_().m_123342_() + 1.4F), (double) ((float) tile.m_58899_().m_123343_() + 0.4F) + Math.random() * 0.2F, 0.0, 0.01, 0.0);
            }
        } else {
            EldrinCapacitorTile.Tick(level, pos, state, tile);
        }
    }

    @Override
    public float getChargeRate() {
        return this.isLesser ? 0.5F : 5.0F;
    }

    @Override
    public float getChargeRadius() {
        return this.isLesser ? 8.0F : 16.0F;
    }

    public boolean isLesser() {
        return this.isLesser;
    }

    private void spawnFireParticles() {
        this.m_58904_().addParticle(new MAParticleType(ParticleInit.FLAME.get()), (double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + this.getCrystalOffset()), (double) ((float) this.m_58899_().m_123343_() + 0.5F), 0.0, 0.01, 0.0);
    }

    private void spawnWaterParticles() {
        Vec3 center = new Vec3((double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + this.getCrystalOffset()), (double) ((float) this.m_58899_().m_123343_() + 0.5F));
        this.m_58904_().addParticle(new MAParticleType(ParticleInit.WATER.get()).setMaxAge(2).setMover(new ParticleOrbitMover(center.x, center.y, center.z, 0.25, 0.0, 0.05)), center.x, center.y, center.z, 0.0, 0.0, 0.0);
    }

    private void spawnWindParticles() {
        Vec3 center = new Vec3((double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + this.getCrystalOffset() - 0.2F), (double) ((float) this.m_58899_().m_123343_() + 0.5F));
        this.m_58904_().addParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setMaxAge(2).setScale(0.04F).setColor(10, 10, 10).setMover(new ParticleOrbitMover(center.x, center.y, center.z, 0.25, 0.01, 0.05)), center.x, center.y, center.z, 0.0, 0.0, 0.0);
    }

    private void spawnEarthParticles() {
        Vec3 center = new Vec3((double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + this.getCrystalOffset() + 0.35F), (double) ((float) this.m_58899_().m_123343_() + 0.5F));
        this.m_58904_().addParticle(new MAParticleType(ParticleInit.DUST.get()).setGravity(0.002F), center.x, center.y, center.z, 0.0, 0.0, 0.0);
    }

    private void spawnArcaneParticles() {
        this.m_58904_().addParticle(new MAParticleType(ParticleInit.ARCANE.get()).setScale(0.1F).setMaxAge(13), (double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + this.getCrystalOffset()), (double) ((float) this.m_58899_().m_123343_() + 0.5F), 0.0, 0.0, 0.0);
    }

    private void spawnEnderParticles() {
        BlockPos bp = this.m_58899_();
        float angle = (float) (this.m_58904_().getGameTime() % 36L * 10L);
        Vec3 rotationOffset = new Vec3(0.25, 0.0, 0.0);
        Vec3 point = rotationOffset.yRot((float) ((double) angle * Math.PI / 180.0));
        this.m_58904_().addParticle(new MAParticleType(ParticleInit.ENDER.get()), (double) ((float) bp.m_123341_() + 0.5F) + point.x, (double) ((float) bp.m_123342_() + this.getCrystalOffset()), (double) ((float) bp.m_123343_() + 0.5F) + point.z, (double) ((float) bp.m_123341_() + 0.5F), (double) ((float) bp.m_123342_() + this.getCrystalOffset()), (double) ((float) bp.m_123343_() + 0.5F));
    }

    private float getCrystalOffset() {
        return this.isLesser() ? 1.5F : 0.75F;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("lesser", this.isLesser);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("lesser")) {
            this.isLesser = pTag.getBoolean("lesser");
        }
    }
}