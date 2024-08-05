package com.mna.blocks.tileentities;

import com.mna.ManaAndArtifice;
import com.mna.blocks.tileentities.init.TileEntityInit;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CoffinTile extends BlockEntity {

    private Optional<UUID> player = Optional.empty();

    private Player cachedPlayer;

    public CoffinTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.COFFIN.get(), pos, state);
    }

    public void setRitualPlayer(Player player) {
        if (player == null) {
            this.player = Optional.empty();
        } else {
            this.player = Optional.of(player.getGameProfile().getId());
        }
        this.cachedPlayer = player;
    }

    public Player getRitualPlayer() {
        if (this.cachedPlayer == null && this.player.isPresent()) {
            this.cachedPlayer = this.m_58904_().m_46003_((UUID) this.player.get());
        }
        return this.cachedPlayer;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        if (this.player.isPresent()) {
            compound.putString("player", ((UUID) this.player.get()).toString());
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("player")) {
            try {
                UUID playerId = UUID.fromString(nbt.getString("player"));
                this.player = Optional.of(playerId);
            } catch (Exception var3) {
                ManaAndArtifice.LOGGER.warn("Unable to load player UUID for ritual coffin; it will simply act as a normal coffin.  This shouldn't happen, the save file was likely edited incorrectly.");
            }
        }
    }
}