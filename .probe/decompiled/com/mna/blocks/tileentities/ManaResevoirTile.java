package com.mna.blocks.tileentities;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.BlockInit;
import com.mna.blocks.manaweaving.ManaweaveProjectorBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

public class ManaResevoirTile extends BlockEntity {

    public static final float MAX_MANA = 100.0F;

    private static final float maxStealPerSecond = 5.0F;

    private static final int RADIUS = 2;

    private static final int CRYSTAL_RADIUS = 8;

    private int tickCount = 0;

    private float mana = 0.0F;

    private ArrayList<BlockPos> knownCrystalPositions = new ArrayList();

    private ArrayList<BlockPos> toRemove = new ArrayList();

    public ManaResevoirTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.MANA_RESEVOIR.get(), pos, state);
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, ManaResevoirTile tile) {
        tile.tickCount++;
        if (level.isClientSide()) {
            tile.spawnParticles();
        } else {
            tile.runLogic_server();
        }
    }

    private void spawnParticles() {
        BlockPos blockPos = this.m_58899_();
        if ((Integer) this.m_58900_().m_61143_(ManaweaveProjectorBlock.FILL_LEVEL) != 4) {
            if (this.tickCount % 40 == 0) {
                this.scanForNearbyCrystals(8);
            }
            if (this.tickCount % 2 == 0) {
                for (Player player : this.f_58857_.m_45976_(Player.class, new AABB(this.m_58899_()).inflate(2.0))) {
                    Vec3 pos = player.m_20182_();
                    this.m_58904_().addParticle(new MAParticleType(ParticleInit.SPARKLE_LERP_POINT.get()), pos.x(), pos.y() + 1.2F, pos.z(), (double) ((float) blockPos.m_123341_() + 0.5F), (double) ((float) blockPos.m_123342_() + 0.5F), (double) ((float) blockPos.m_123343_() + 0.5F));
                }
                this.toRemove.clear();
                for (BlockPos pos : this.knownCrystalPositions) {
                    if (this.m_58904_().isLoaded(pos)) {
                        if (this.m_58904_().getBlockState(pos).m_60734_() == BlockInit.MANA_CRYSTAL.get()) {
                            this.m_58904_().addParticle(new MAParticleType(ParticleInit.SPARKLE_LERP_POINT.get()), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.2F), (double) ((float) pos.m_123343_() + 0.5F), (double) ((float) blockPos.m_123341_() + 0.5F), (double) ((float) blockPos.m_123342_() + 0.5F), (double) ((float) blockPos.m_123343_() + 0.5F));
                        } else {
                            this.toRemove.add(pos);
                        }
                    }
                }
                this.knownCrystalPositions.removeAll(this.toRemove);
            }
        }
    }

    private void runLogic_server() {
        if (this.mana < 100.0F && !this.m_58904_().m_276867_(this.m_58899_())) {
            if (this.tickCount % 40 == 0) {
                this.scanForNearbyCrystals(8);
            }
            if (this.tickCount % 2 == 0) {
                this.getManaFromAllNearby();
                this.getManaFromCrystals();
                this.updateBlockState();
            }
        }
    }

    private void updateBlockState() {
        if (!this.m_58904_().isClientSide()) {
            BlockState state = this.m_58900_();
            boolean hasChange = false;
            int fillAmt = (int) Math.floor((double) (this.mana / 100.0F * 4.0F));
            if ((Integer) state.m_61143_(ManaweaveProjectorBlock.FILL_LEVEL) != fillAmt) {
                state = (BlockState) state.m_61124_(ManaweaveProjectorBlock.FILL_LEVEL, fillAmt);
                hasChange = true;
            }
            if (hasChange) {
                this.m_58904_().setBlock(this.m_58899_(), state, 2);
                this.m_58904_().updateNeighbourForOutputSignal(this.f_58858_, this.m_58900_().m_60734_());
            }
        }
    }

    private void getManaFromAllNearby() {
        for (Player player : this.f_58857_.m_45976_(Player.class, new AABB(this.m_58899_()).inflate(2.0))) {
            this.getManaFromPlayer(player);
        }
    }

    private void getManaFromCrystals() {
        this.toRemove.clear();
        for (BlockPos pos : this.knownCrystalPositions) {
            if (this.m_58904_().isLoaded(pos)) {
                if (this.m_58904_().getBlockState(pos).m_60734_() == BlockInit.MANA_CRYSTAL.get()) {
                    this.mana = Math.min(this.mana + 0.5F, 100.0F);
                } else {
                    this.toRemove.add(pos);
                }
            }
        }
        this.knownCrystalPositions.removeAll(this.toRemove);
    }

    private void scanForNearbyCrystals(int radius) {
        this.knownCrystalPositions.clear();
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    BlockPos pos = this.m_58899_().offset(i, j, k);
                    if (this.m_58904_().isLoaded(pos) && this.f_58857_.getBlockState(pos).m_60734_() == BlockInit.MANA_CRYSTAL.get()) {
                        this.knownCrystalPositions.add(pos);
                    }
                }
            }
        }
    }

    private void getManaFromPlayer(Player player) {
        float stealAmount = Math.min(100.0F - this.mana, 5.0F);
        if (stealAmount != 0.0F) {
            LazyOptional<IPlayerMagic> magicContainer = player.getCapability(PlayerMagicProvider.MAGIC);
            if (magicContainer.isPresent()) {
                IPlayerMagic magic = magicContainer.orElse(null);
                if (magic.getCastingResource().getAmount() < stealAmount) {
                    stealAmount = magic.getCastingResource().getAmount();
                }
                magic.getCastingResource().consume(player, stealAmount);
                this.mana += stealAmount;
            }
        }
    }

    public float getMana() {
        return this.mana;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putFloat("mana", this.mana);
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("mana")) {
            this.mana = compound.getFloat("mana");
        }
        super.load(compound);
    }
}