package journeymap.client.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import journeymap.client.Constants;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.properties.config.StringField;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;

public class IconSetFileHandler {

    public static final String MOB_ICON_SET_DEFAULT = "Default";

    private static final Set<String> modUpdatedSetNames = new HashSet();

    private static final Set<ResourceLocation> entityIconLocations = new HashSet();

    public static void initialize() {
        modUpdatedSetNames.add("Default");
    }

    public static boolean registerEntityIconDirectory(ResourceLocation resourceLocation) {
        boolean valid = addEntityIcons(resourceLocation, "Default", false);
        if (valid) {
            entityIconLocations.add(resourceLocation);
        }
        return valid;
    }

    public static void ensureEntityIconSet(String setName) {
        ensureEntityIconSet(setName, false);
    }

    public static void ensureEntityIconSet(String setName, boolean overwrite) {
        if (!modUpdatedSetNames.contains(setName)) {
            for (ResourceLocation resourceLocation : entityIconLocations) {
                addEntityIcons(resourceLocation, setName, overwrite);
            }
            modUpdatedSetNames.add(setName);
        }
        try {
            PackRepository rpr = Minecraft.getInstance().getResourcePackRepository();
            for (Pack entry : rpr.getAvailablePacks()) {
                PackResources pack = entry.open();
                for (String domain : pack.getNamespaces(PackType.CLIENT_RESOURCES)) {
                    ResourceLocation domainEntityIcons = new ResourceLocation(domain, "textures/entity_icons");
                    if (pack.getResource(PackType.CLIENT_RESOURCES, domainEntityIcons) != null) {
                        addEntityIcons(domainEntityIcons, setName, true);
                    }
                }
            }
        } catch (Throwable var9) {
            Journeymap.getLogger().error(String.format("Can't get entity icon from resource packs: %s", LogFormatter.toString(var9)));
        }
    }

    private static boolean addEntityIcons(ResourceLocation resourceLocation, String setName, boolean overwrite) {
        boolean result = false;
        try {
            result = FileHandler.copyResources(getEntityIconDir(), resourceLocation, setName, overwrite);
        } catch (Throwable var5) {
            Journeymap.getLogger().error("Error adding entity icons: " + var5.getMessage(), var5);
        }
        Journeymap.getLogger().info(String.format("Added entity icons from %s. Success: %s", resourceLocation, result));
        return result;
    }

    public static File getEntityIconDir() {
        File dir = new File(FileHandler.getMinecraftDirectory(), Constants.ENTITY_ICON_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static ArrayList<String> getEntityIconSetNames() {
        return getIconSetNames(getEntityIconDir(), Collections.singletonList("Default"));
    }

    public static ArrayList<String> getIconSetNames(File parentDir, List<String> defaultIconSets) {
        try {
            for (String iconSetName : defaultIconSets) {
                File iconSetDir = new File(parentDir, iconSetName);
                if (iconSetDir.exists() && !iconSetDir.isDirectory()) {
                    iconSetDir.delete();
                }
                iconSetDir.mkdirs();
            }
        } catch (Throwable var7) {
            Journeymap.getLogger().error("Could not prepare iconset directories for " + parentDir + ": " + LogFormatter.toString(var7));
        }
        ArrayList<String> names = new ArrayList();
        for (File iconSetDir : parentDir.listFiles()) {
            if (iconSetDir.isDirectory()) {
                names.add(iconSetDir.getName());
            }
        }
        Collections.sort(names);
        return names;
    }

    public static class IconSetValuesProvider implements StringField.ValuesProvider {

        @Override
        public List<String> getStrings() {
            return (List<String>) (Minecraft.getInstance() != null ? IconSetFileHandler.getEntityIconSetNames() : Collections.singletonList("Default"));
        }

        @Override
        public String getDefaultString() {
            return "Default";
        }
    }
}