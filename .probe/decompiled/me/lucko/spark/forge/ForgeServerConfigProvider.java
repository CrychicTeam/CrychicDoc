package me.lucko.spark.forge;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Collection;
import java.util.Map;
import me.lucko.spark.common.platform.serverconfig.ConfigParser;
import me.lucko.spark.common.platform.serverconfig.PropertiesConfigParser;
import me.lucko.spark.common.platform.serverconfig.ServerConfigProvider;

public class ForgeServerConfigProvider extends ServerConfigProvider {

    private static final Map<String, ConfigParser> FILES = ImmutableMap.of("server.properties", PropertiesConfigParser.INSTANCE);

    private static final Collection<String> HIDDEN_PATHS;

    public ForgeServerConfigProvider() {
        super(FILES, HIDDEN_PATHS);
    }

    static {
        Builder<String> hiddenPaths = ImmutableSet.builder().add("server-ip").add("motd").add("resource-pack").add("rcon<dot>password").add("level-seed").addAll(getSystemPropertyList("spark.serverconfigs.hiddenpaths"));
        HIDDEN_PATHS = hiddenPaths.build();
    }
}