package com.mna.blocks.tileentities;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.BlockInit;
import com.mna.blocks.manaweaving.ManaweaveProjectorBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.manaweaving.Manaweave;
import com.mna.events.EventDispatcher;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

public class ManaweaveProjectorTile extends BlockEntity {

    private static final String NBT_PATTERN_ID = "manaweave_pattern_id";

    public static final float MAX_MANA = 100.0F;

    private static final float maxStealPerSecond = 5.0F;

    private static final int RADIUS = 2;

    private static final int CRYSTAL_RADIUS = 8;

    private int tickCount = 0;

    private float mana = 0.0F;

    private ResourceLocation __patternRLoc = null;

    private ManaweavingPattern __pattern;

    private boolean spawning = false;

    private LivingEntity activator;

    private ArrayList<BlockPos> knownCrystalPositions = new ArrayList();

    private ArrayList<BlockPos> toRemove = new ArrayList();

    public ManaweaveProjectorTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.MANAWEAVE_PROJECTOR.get(), pos, state);
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, ManaweaveProjectorTile tile) {
        tile.tickCount++;
        if (level.isClientSide()) {
            tile.spawnParticles();
        } else {
            tile.runLogic_server();
        }
    }

    private void spawnParticles() {
        BlockPos blockPos = this.m_58899_();
        if ((Boolean) this.m_58900_().m_61143_(ManaweaveProjectorBlock.PROJECTING)) {
            this.m_58904_().addParticle(new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get()), (double) ((float) blockPos.m_123341_() + 0.5F), (double) ((float) blockPos.m_123342_() + 0.5F), (double) ((float) blockPos.m_123343_() + 0.5F), -0.1F + Math.random() * 0.2F, 0.15F, -0.1F + Math.random() * 0.2F);
        } else if ((Integer) this.m_58900_().m_61143_(ManaweaveProjectorBlock.FILL_LEVEL) != 4) {
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
        if (!this.spawning) {
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
        } else {
            this.mana--;
            if (this.mana <= 0.0F) {
                this.mana = 0.0F;
                if (!EventDispatcher.DispatchManaweavePatternDrawn(this.getPattern(), this.activator)) {
                    return;
                }
                BlockPos spawnPos = this.m_58899_();
                Manaweave weave = new Manaweave(this.m_58904_());
                weave.m_6034_((double) spawnPos.m_123341_() + 0.5, (double) (spawnPos.m_123342_() + 1), (double) spawnPos.m_123343_() + 0.5);
                weave.setPattern(this.getPattern().m_6423_());
                weave.setCaster(this.activator, InteractionHand.MAIN_HAND);
                this.m_58904_().m_7967_(weave);
                this.activator = null;
                this.spawning = false;
                this.scanForNearbyCrystals(8);
            }
            this.updateBlockState();
        }
    }

    private void updateBlockState() {
        if (!this.m_58904_().isClientSide()) {
            BlockState state = this.m_58900_();
            boolean hasChange = false;
            if ((Boolean) state.m_61143_(ManaweaveProjectorBlock.PROJECTING) != this.spawning) {
                state = (BlockState) state.m_61124_(ManaweaveProjectorBlock.PROJECTING, this.spawning);
                hasChange = true;
            }
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

    public void trySpawnManaweaveEntity(Player player) {
        if (!(this.mana < 100.0F) && !this.m_58904_().isClientSide() && this.getPattern() != null && !this.spawning) {
            this.activator = player;
            this.spawning = true;
            this.updateBlockState();
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

    private void lookupManaweavePattern(ResourceLocation rLoc) {
        this.__pattern = ManaweavingPatternHelper.GetManaweavingRecipe(this.m_58904_(), rLoc);
    }

    public float getMana() {
        return this.mana;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putFloat("mana", this.mana);
        compound.putString("manaweave_pattern_id", this.__pattern != null ? this.__pattern.m_6423_().toString() : (this.__patternRLoc != null ? this.__patternRLoc.toString() : ""));
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("mana")) {
            this.mana = compound.getFloat("mana");
        }
        if (compound.contains("manaweave_pattern_id")) {
            this.__patternRLoc = new ResourceLocation(compound.getString("manaweave_pattern_id"));
        }
        super.load(compound);
    }

    @Nullable
    public ManaweavingPattern getPattern() {
        if (this.__pattern == null) {
            this.lookupManaweavePattern(this.__patternRLoc != null ? this.__patternRLoc : new ResourceLocation("mna", "manaweave_patterns/triangle"));
        }
        return this.__pattern;
    }

    public void setPattern(ResourceLocation rLoc, @Nullable Player player) {
        if (player != null) {
            ManaweavingPattern newPattern = ManaweavingPatternHelper.GetManaweavingRecipe(this.m_58904_(), rLoc);
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            if (progression != null && newPattern != null && newPattern.getTier() > progression.getTier()) {
                player.m_213846_(Component.translatable("item.mna.manaweaver_wand.low_tier"));
                return;
            }
        }
        this.__patternRLoc = rLoc;
        this.__pattern = null;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag base = super.getUpdateTag();
        base.putString("manaweave_pattern_id", this.__pattern != null ? this.__pattern.m_6423_().toString() : (this.__patternRLoc != null ? this.__patternRLoc.toString() : ""));
        return base;
    }

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        this.setPattern(new ResourceLocation(tag.getString("manaweave_pattern_id")), null);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag data = pkt.getTag();
        this.setPattern(new ResourceLocation(data.getString("manaweave_pattern_id")), null);
    }
}