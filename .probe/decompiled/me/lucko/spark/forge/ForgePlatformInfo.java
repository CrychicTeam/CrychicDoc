package me.lucko.spark.forge;

import me.lucko.spark.common.platform.PlatformInfo;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.versions.mcp.MCPVersion;

public class ForgePlatformInfo implements PlatformInfo {

    private final PlatformInfo.Type type;

    public ForgePlatformInfo(PlatformInfo.Type type) {
        this.type = type;
    }

    @Override
    public PlatformInfo.Type getType() {
        return this.type;
    }

    @Override
    public String getName() {
        return "Forge";
    }

    @Override
    public String getVersion() {
        return ForgeVersion.getVersion();
    }

    @Override
    public String getMinecraftVersion() {
        return MCPVersion.getMCVersion();
    }
}