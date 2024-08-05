package dev.ftb.mods.ftbxmodcompat.generic.permissions;

import dev.ftb.mods.ftblibrary.integration.permissions.PermissionProvider;
import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import net.minecraft.server.level.ServerPlayer;

public class FTBRanksProvider implements PermissionProvider {

    @Override
    public int getIntegerPermission(ServerPlayer player, String nodeName, int def) {
        return FTBRanksAPI.getPermissionValue(player, nodeName).asInteger().orElse(def);
    }

    @Override
    public boolean getBooleanPermission(ServerPlayer player, String nodeName, boolean def) {
        return (Boolean) FTBRanksAPI.getPermissionValue(player, nodeName).asBoolean().orElse(def);
    }

    @Override
    public String getStringPermission(ServerPlayer player, String nodeName, String def) {
        return (String) FTBRanksAPI.getPermissionValue(player, nodeName).asString().orElse(def);
    }

    @Override
    public String getName() {
        return "FTB Ranks";
    }
}