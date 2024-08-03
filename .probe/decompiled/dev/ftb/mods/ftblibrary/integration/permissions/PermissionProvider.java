package dev.ftb.mods.ftblibrary.integration.permissions;

import net.minecraft.server.level.ServerPlayer;

public interface PermissionProvider {

    int getIntegerPermission(ServerPlayer var1, String var2, int var3);

    boolean getBooleanPermission(ServerPlayer var1, String var2, boolean var3);

    String getStringPermission(ServerPlayer var1, String var2, String var3);

    String getName();
}