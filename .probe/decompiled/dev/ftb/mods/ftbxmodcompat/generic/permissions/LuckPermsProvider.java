package dev.ftb.mods.ftbxmodcompat.generic.permissions;

import dev.ftb.mods.ftblibrary.integration.permissions.PermissionProvider;
import java.util.Optional;
import java.util.UUID;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.minecraft.server.level.ServerPlayer;

public class LuckPermsProvider implements PermissionProvider {

    @Override
    public int getIntegerPermission(ServerPlayer player, String nodeName, int def) {
        return (Integer) getMetaData(player.m_20148_(), nodeName).map(Integer::parseInt).orElse(def);
    }

    @Override
    public boolean getBooleanPermission(ServerPlayer player, String nodeName, boolean def) {
        return (Boolean) getMetaData(player.m_20148_(), nodeName).map(Boolean::parseBoolean).orElse(def);
    }

    @Override
    public String getStringPermission(ServerPlayer player, String nodeName, String def) {
        return (String) getMetaData(player.m_20148_(), nodeName).orElse(def);
    }

    @Override
    public String getName() {
        return "LuckPerms";
    }

    private static Optional<String> getMetaData(UUID uuid, String meta) {
        LuckPerms luckperms = net.luckperms.api.LuckPermsProvider.get();
        Optional<String> metaValue = Optional.empty();
        try {
            User user = luckperms.getUserManager().getUser(uuid);
            if (user != null) {
                Optional<QueryOptions> context = luckperms.getContextManager().getQueryOptions(user);
                if (context.isPresent()) {
                    metaValue = Optional.ofNullable(user.getCachedData().getMetaData((QueryOptions) context.get()).getMetaValue(meta));
                }
            }
        } catch (IllegalStateException var6) {
            System.err.println("Error on fetching user with luckperms");
            System.err.println(var6.getMessage());
        }
        return metaValue;
    }
}