package net.minecraftforge.versions.mcp;

import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MCPVersion {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String mcVersion = (String) JarVersionLookupHandler.getSpecificationVersion(MCPVersion.class).orElse(FMLLoader.versionInfo().mcVersion());

    private static final String mcpVersion;

    public static String getMCVersion() {
        return mcVersion;
    }

    public static String getMCPVersion() {
        return mcpVersion;
    }

    public static String getMCPandMCVersion() {
        return mcVersion + "-" + mcpVersion;
    }

    static {
        LOGGER.debug(Logging.CORE, "MCP Version package {} from {}", MCPVersion.class.getPackage(), MCPVersion.class.getClassLoader());
        if (mcVersion == null) {
            throw new RuntimeException("Missing MC version, cannot continue");
        } else {
            mcpVersion = (String) JarVersionLookupHandler.getImplementationVersion(MCPVersion.class).orElse(FMLLoader.versionInfo().mcpVersion());
            if (mcpVersion == null) {
                throw new RuntimeException("Missing MCP version, cannot continue");
            } else {
                LOGGER.debug(Logging.CORE, "Found MC version information {}", mcVersion);
                LOGGER.debug(Logging.CORE, "Found MCP version information {}", mcpVersion);
            }
        }
    }
}