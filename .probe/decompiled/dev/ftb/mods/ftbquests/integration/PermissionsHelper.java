package dev.ftb.mods.ftbquests.integration;

import dev.ftb.mods.ftblibrary.integration.permissions.PermissionHelper;
import net.minecraft.server.level.ServerPlayer;

public class PermissionsHelper {

    public static final String EDITOR_PERM = "ftbquests.editor";

    public static boolean hasEditorPermission(ServerPlayer player, boolean def) {
        return player.m_20310_(2) || PermissionHelper.INSTANCE.getProvider().getBooleanPermission(player, "ftbquests.editor", def);
    }
}