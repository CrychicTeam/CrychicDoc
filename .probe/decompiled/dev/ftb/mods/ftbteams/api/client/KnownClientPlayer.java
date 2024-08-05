package dev.ftb.mods.ftbteams.api.client;

import com.mojang.authlib.GameProfile;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;

public record KnownClientPlayer(UUID id, String name, boolean online, UUID teamId, GameProfile profile, CompoundTag extraData) {

    public boolean isInternalTeam() {
        return this.teamId.equals(this.id);
    }

    public boolean isOnlineAndNotInParty() {
        return this.online && this.isInternalTeam();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            KnownClientPlayer that = (KnownClientPlayer) o;
            return this.id.equals(that.id);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.id });
    }
}