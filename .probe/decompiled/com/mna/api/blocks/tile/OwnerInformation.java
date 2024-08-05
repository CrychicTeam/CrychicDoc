package com.mna.api.blocks.tile;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;

public class OwnerInformation {

    private UUID owner = null;

    private String _cachedTeamName = null;

    private Player _cachedOwner = null;

    private Team _cachedTeam = null;

    public void setOwner(Player player) {
        if (player.getGameProfile() != null && player.getGameProfile().getId() != null) {
            this._cachedOwner = player;
            this.owner = player.getGameProfile().getId();
            this._cachedTeam = player.m_5647_();
            this._cachedTeamName = player.m_5647_() != null ? player.m_5647_().getName() : "";
        }
    }

    @Nullable
    public Player getOwner(Level level) {
        if (this.owner != null && (this._cachedOwner == null || !this._cachedOwner.m_6084_())) {
            this._cachedOwner = level.m_46003_(this.owner);
        }
        return this._cachedOwner;
    }

    @Nullable
    public Team getOwnerTeam(Level level) {
        Player owner = this.getOwner(level);
        if (owner != null) {
            this._cachedTeam = owner.m_5647_();
        } else if (this._cachedTeamName != null && (this._cachedTeam == null || !this._cachedTeam.getName().equals(this._cachedTeamName))) {
            this._cachedTeam = level.getScoreboard().getPlayerTeam(this._cachedTeamName);
        }
        return this._cachedTeam;
    }

    public boolean isFriendlyTo(LivingEntity living) {
        if (living == this.getOwner(living.m_9236_())) {
            return true;
        } else {
            Team team = this.getOwnerTeam(living.m_9236_());
            return team != null && living.m_20031_(team);
        }
    }

    public void save(CompoundTag tag) {
        CompoundTag ownerInfo = new CompoundTag();
        if (this.owner != null) {
            ownerInfo.putString("owner_uuid", this.owner.toString());
        }
        if (this._cachedTeamName != null) {
            ownerInfo.putString("owner_team", this._cachedTeamName);
        }
        tag.put("owner_info", ownerInfo);
    }

    public void load(CompoundTag tag) {
        if (tag.contains("owner_info")) {
            CompoundTag ownerInfo = tag.getCompound("owner_info");
            if (ownerInfo.contains("owner_uuid")) {
                try {
                    this.owner = UUID.fromString(ownerInfo.getString("owner_uuid"));
                } catch (Throwable var4) {
                    this.owner = null;
                }
            }
            if (ownerInfo.contains("owner_team")) {
                this._cachedTeamName = ownerInfo.getString("owner_team");
            }
        }
    }

    @Nullable
    public UUID getOwnerId() {
        return this.owner;
    }
}