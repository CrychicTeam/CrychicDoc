package com.mna.blocks.tileentities;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.MultiblockTile;
import com.mna.api.capabilities.WellspringNode;
import com.mna.api.events.GenericProgressionEvent;
import com.mna.api.events.ProgressionEventIDs;
import com.mna.api.tools.RLoc;
import com.mna.blocks.artifice.RefractionLensBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.network.ClientMessageDispatcher;
import com.mna.network.messages.to_client.ShowDidYouKnow;
import com.mna.tools.render.MultiblockRenderer;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.IForgeRegistry;

public class RefractionLensTile extends MultiblockTile {

    private Affinity affinity;

    private UUID placedBy;

    private ResourceLocation placedByFaction;

    private boolean initialRequest = false;

    public RefractionLensTile(BlockEntityType<?> type, BlockPos pos, BlockState state, Affinity aff) {
        super(type, pos, state, RLoc.create("multiblock/wellspring_capture"));
        this.affinity = aff;
        this.placedBy = null;
        this.placedByFaction = null;
    }

    public RefractionLensTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        this(type, pos, state, Affinity.UNKNOWN);
    }

    public RefractionLensTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.REFRACTION_LENS.get(), pos, state);
    }

    public RefractionLensTile(Affinity affinity, BlockPos pos, BlockState state) {
        this(TileEntityInit.REFRACTION_LENS.get(), pos, state, affinity);
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, RefractionLensTile tile) {
        boolean wasValid = tile.structureMatched;
        MultiblockTile.Tick(level, pos, state, tile);
        if (level.isClientSide()) {
            if (!tile.initialRequest) {
                tile.initialRequest = true;
                ClientMessageDispatcher.sendRequestWellspringNetworkSyncMessage(true);
            }
        } else if (wasValid && !tile.structureMatched) {
            tile.removePowerFromNetwork();
        } else if (!wasValid && tile.structureMatched) {
            tile.addPowerToNetwork();
        }
    }

    public void onBlockBroken() {
        if (!this.m_58904_().isClientSide()) {
            this.removePowerFromNetwork();
        }
    }

    @Override
    protected BlockPos getMatchOrigin() {
        return this.m_58899_().below(4);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.putString("affinity", this.affinity.name());
        if (this.placedBy != null) {
            nbt.putString("placedBy", this.placedBy.toString());
        }
        if (this.placedByFaction != null) {
            nbt.putString("placedByFaction", this.placedByFaction.toString());
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        if (nbt.contains("affinity")) {
            this.affinity = Affinity.valueOf(nbt.getString("affinity"));
        }
        if (nbt.contains("placedByFaction")) {
            this.placedByFaction = new ResourceLocation(nbt.getString("placedByFaction"));
        }
        if (nbt.contains("placedBy")) {
            this.placedBy = UUID.fromString(nbt.getString("placedBy"));
        }
        super.m_142466_(nbt);
    }

    private void addPowerToNetwork() {
        if (this.placedBy != null && this.f_58857_ instanceof ServerLevel) {
            Player crafter = this.f_58857_.m_46003_(this.placedBy);
            if (this.placedByFaction == null) {
                if (crafter != null) {
                    crafter.m_213846_(Component.translatable("gui.mna.wellspring-no-faction"));
                }
            } else {
                if (crafter != null) {
                    MinecraftForge.EVENT_BUS.post(new GenericProgressionEvent(crafter, ProgressionEventIDs.CAPTURE_WELLSPRING));
                    if (crafter instanceof ServerPlayer) {
                        CustomAdvancementTriggers.CAPTURE_WELLSPRING.trigger((ServerPlayer) crafter, this.affinity);
                    }
                }
                this.m_58904_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
                    Optional<WellspringNode> entry = m.getWellspringRegistry().getNodeAt(this.m_58899_());
                    entry.ifPresent(n -> {
                        if (n.getAffinity().getShiftAffinity() != this.affinity && n.getAffinity() != Affinity.UNKNOWN) {
                            this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(RefractionLensBlock.INVALID, true));
                            ManaAndArtifice.instance.proxy.showDidYouKnow(crafter, ShowDidYouKnow.Messages.LENS_CAPTURE);
                        } else if (!m.getWellspringRegistry().claimNode((ServerLevel) this.m_58904_(), this.placedBy, this.placedByFaction, this.m_58899_(), this.affinity)) {
                            this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(RefractionLensBlock.INVALID, true));
                        } else {
                            this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(RefractionLensBlock.INVALID, false));
                        }
                    });
                    if (!entry.isPresent()) {
                        this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(RefractionLensBlock.INVALID, true));
                    }
                });
            }
        }
    }

    private void removePowerFromNetwork() {
        this.m_58904_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> m.getWellspringRegistry().unclaimNode((ServerLevel) this.f_58857_, this.m_58899_()));
    }

    public void setPlacedBy(Player player) {
        if (!this.m_58904_().isClientSide()) {
            this.placedBy = player.m_20148_();
            player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                if (p.getAlliedFaction() != null) {
                    this.placedByFaction = ((IForgeRegistry) Registries.Factions.get()).getKey(p.getAlliedFaction());
                }
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void setGhostMultiblock() {
        this.getDefinition().ifPresent(r -> {
            BlockPos anchorPos = this.getMatchOrigin();
            MultiblockRenderer.setMultiblock(r, Component.translatable(r.getId().toString()), false);
            MultiblockRenderer.anchorTo(anchorPos, Rotation.NONE);
        });
    }
}