package dev.ftb.mods.ftbteams.client.gui;

import com.mojang.authlib.GameProfile;

public interface InvitationSetup {

    boolean isInvited(GameProfile var1);

    void setInvited(GameProfile var1, boolean var2);
}