package com.mna.blocks.tileentities;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.blocks.tile.IMultiblockDefinition;
import com.mna.api.blocks.tile.MultiblockTile;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.faction.IFaction;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import com.mna.recipes.multiblock.MultiblockDefinition;
import com.mna.tools.render.MultiblockRenderer;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;

public class SanctumTile extends MultiblockTile {

    private IFaction alignedFaction;

    public SanctumTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.CIRCLE_OF_POWER.get(), pos, state, null);
    }

    public void setFaction(IFaction faction) {
        this.alignedFaction = faction;
    }

    public boolean hasFaction() {
        return this.alignedFaction != null;
    }

    @Override
    protected Optional<IMultiblockDefinition> getDefinition() {
        ResourceLocation recipeId = this.alignedFaction.getSanctumStructure();
        if (this.hasFaction() && recipeId != null) {
            if (this._cachedMultiblockRecipe == null) {
                this.m_58904_().getRecipeManager().byKey(recipeId).ifPresent(r -> {
                    if (r instanceof MultiblockDefinition) {
                        this._cachedMultiblockRecipe = (MultiblockDefinition) r;
                    }
                });
            }
            return Optional.ofNullable(this._cachedMultiblockRecipe);
        } else {
            return Optional.empty();
        }
    }

    public static void ServerTick(Level level, BlockPos pos, BlockState state, SanctumTile tile) {
        MultiblockTile.Tick(level, pos, state, tile);
        if (!tile.m_58904_().isClientSide() && tile.structureMatched && tile.m_58904_().getGameTime() % 300L == 0L) {
            tile.getDefinition().ifPresent(r -> {
                if (tile.alignedFaction != null) {
                    float sanctumScale = 32.0F;
                    int hX = (int) ((Math.ceil((double) ((float) r.getSize().getX() / 2.0F)) + 1.0) * (double) sanctumScale);
                    int hZ = (int) ((Math.ceil((double) ((float) r.getSize().getZ() / 2.0F)) + 1.0) * (double) sanctumScale);
                    AABB bb = new AABB(pos).expandTowards((double) hZ, (double) ((float) (r.getSize().getY() + 1) * sanctumScale), (double) hZ).expandTowards((double) (-hX), 0.0, (double) (-hZ));
                    List<LivingEntity> entities = tile.m_58904_().m_45976_(LivingEntity.class, bb);
                    if (entities.size() > 0) {
                        entities.forEach(entity -> {
                            if (entity instanceof Player) {
                                entity.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(pr -> {
                                    if (pr.getAlliedFaction() == null || pr.getAlliedFaction() != tile.alignedFaction && !pr.getAlliedFaction().getAlliedFactions().contains(tile.alignedFaction)) {
                                        tile.setSacrifice(entity);
                                    } else {
                                        entity.addEffect(new MobEffectInstance(EffectInit.CIRCLE_OF_POWER.get(), 600));
                                    }
                                });
                            } else {
                                tile.setSacrifice(entity);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected boolean advancementSelector(LivingEntity candidate) {
        if (this.alignedFaction == null) {
            return false;
        } else if (!(candidate instanceof ServerPlayer)) {
            return false;
        } else {
            IPlayerProgression progression = (IPlayerProgression) candidate.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            return progression == null ? false : progression.getAlliedFaction() == this.alignedFaction;
        }
    }

    private void setSacrifice(LivingEntity entity) {
        if (this.alignedFaction != null) {
            float sacrificeDistance = 9.0F;
            if ((this.alignedFaction == Factions.UNDEAD || this.alignedFaction.getAlliedFactions().contains(Factions.UNDEAD)) && entity.m_20183_().m_123331_(this.m_58899_()) <= (double) sacrificeDistance) {
                entity.addEffect(new MobEffectInstance(EffectInit.SOUL_VULNERABILITY.get(), 100, 0, true, true));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void setGhostMultiblock() {
        this.getDefinition().ifPresent(r -> {
            BlockPos anchorPos = this.f_58858_.above();
            MultiblockRenderer.setMultiblock(r, Component.translatable(r.getId().toString()), false);
            MultiblockRenderer.anchorTo(anchorPos, Rotation.NONE);
        });
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag data = super.m_5995_();
        this.writeFactionToNBT(data);
        return data;
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag nbt = pkt.getTag();
        this.readFactionFromNBT(nbt);
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        this.writeFactionToNBT(data);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.m_142466_(nbt);
        this.readFactionFromNBT(nbt);
    }

    private void writeFactionToNBT(CompoundTag nbt) {
        if (this.alignedFaction != null) {
            nbt.putString("faction", ((IForgeRegistry) Registries.Factions.get()).getKey(this.alignedFaction).toString());
        }
    }

    private void readFactionFromNBT(CompoundTag nbt) {
        if (nbt.contains("faction")) {
            try {
                this.alignedFaction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(new ResourceLocation(nbt.getString("faction")));
            } catch (Exception var3) {
                ManaAndArtifice.LOGGER.error("Error parsing aligned faction on Circle of Power, setting to NONE.");
                ManaAndArtifice.LOGGER.error(var3);
                this.alignedFaction = null;
            }
        }
    }

    @Override
    protected BlockPos getMatchOrigin() {
        return this.m_58899_().above();
    }
}