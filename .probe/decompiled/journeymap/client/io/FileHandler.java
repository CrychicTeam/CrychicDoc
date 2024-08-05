package journeymap.client.io;

import com.google.common.base.Joiner;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.data.WorldData;
import journeymap.client.log.JMLogger;
import journeymap.common.CommonConstants;
import journeymap.common.Journeymap;
import journeymap.common.LoaderHooks;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.log.LogFormatter;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.commons.io.FileUtils;

public class FileHandler {

    public static final String DEV_MINECRAFT_DIR = "run/";

    public static final String ASSETS_JOURNEYMAP = "/assets/journeymap";

    public static final String ASSETS_JOURNEYMAP_UI = "/assets/journeymap/ui";

    public static final String ASSETS_WEBMAP = "/assets/journeymap/web";

    public static final File MinecraftDirectory = getMinecraftDirectory();

    public static final File JourneyMapDirectory = new File(MinecraftDirectory, Constants.JOURNEYMAP_DIR);

    public static final File StandardConfigDirectory = new File(MinecraftDirectory, Constants.CONFIG_DIR);

    public static File getMinecraftDirectory() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null ? minecraft.gameDirectory : new File("run/");
    }

    public static Path getDimPath(File worldDir, ResourceKey<Level> dimensionKey) {
        return new File(worldDir, getDimNameForPath(worldDir, dimensionKey)).toPath();
    }

    public static String getDimNameForPath(File worldDir, ResourceKey<Level> dimensionKey) {
        if (Level.OVERWORLD.equals(dimensionKey) && new File(worldDir, "DIM0").exists()) {
            return "DIM0";
        } else if (Level.END.equals(dimensionKey) && new File(worldDir, "DIM1").exists()) {
            return "DIM1";
        } else if (Level.NETHER.equals(dimensionKey) && new File(worldDir, "DIM-1").exists()) {
            return "DIM-1";
        } else {
            return "minecraft".equals(dimensionKey.location().getNamespace()) ? DimensionHelper.getDimName(dimensionKey) : CommonConstants.getSafeString(dimensionKey.location().toString(), "~");
        }
    }

    public static File getMCWorldDir(Minecraft minecraft) {
        if (minecraft.isLocalServer()) {
            String lastMCFolderName = minecraft.getSingleplayerServer().f_129744_.getLevelId();
            return new File(getMinecraftDirectory(), "saves" + File.separator + lastMCFolderName);
        } else {
            return null;
        }
    }

    public static File getWorldSaveDir(Minecraft minecraft) {
        if (minecraft.hasSingleplayerServer()) {
            try {
                ResourceKey<Level> provider = minecraft.level.m_46472_();
                File savesDir = new File(getMinecraftDirectory(), "saves");
                File worldSaveDir = new File(savesDir, minecraft.getSingleplayerServer().f_129744_.getLevelId());
                DimensionType.getStorageFolder(provider, worldSaveDir.toPath());
                File dir = DimensionType.getStorageFolder(provider, worldSaveDir.toPath()).toFile();
                dir.mkdirs();
                return dir;
            } catch (Throwable var5) {
                Journeymap.getLogger().error("Error getting world save dir: %s", var5);
            }
        }
        return null;
    }

    public static File getMCWorldDir(Minecraft minecraft, ResourceKey<Level> dimension) {
        File worldDir = getMCWorldDir(minecraft);
        return DimensionType.getStorageFolder(dimension, worldDir.toPath()).toFile();
    }

    public static File getJourneyMapDir() {
        return JourneyMapDirectory;
    }

    public static File getJMWorldDir(Minecraft minecraft) {
        return minecraft.level == null ? null : getJMWorldDir(minecraft, JourneymapClient.getInstance().getCurrentWorldId());
    }

    public static File getAddonDataPath(Minecraft minecraft) {
        return new File(getJMWorldDir(minecraft), "addon-data");
    }

    public static synchronized File getJMWorldDir(Minecraft minecraft, String worldId) {
        if (minecraft.level == null) {
            return null;
        } else {
            File worldDirectory = null;
            try {
                worldDirectory = getJMWorldDirForWorldId(minecraft, worldId);
                if (worldDirectory == null) {
                    worldDirectory = getJMWorldDirForWorldId(minecraft, null);
                }
                if (worldDirectory != null && !worldDirectory.exists()) {
                    worldDirectory.mkdirs();
                }
                return worldDirectory;
            } catch (Exception var4) {
                Journeymap.getLogger().log(org.apache.logging.log4j.Level.ERROR, LogFormatter.toString(var4));
                throw new RuntimeException(var4);
            }
        }
    }

    public static File getJMWorldDirForWorldId(Minecraft minecraft, String worldId) {
        if (minecraft != null && minecraft.level != null) {
            File testWorldDirectory = null;
            try {
                String suffix = getWorldDirectoryName(minecraft, worldId);
                if (!minecraft.hasSingleplayerServer()) {
                    testWorldDirectory = new File(MinecraftDirectory, Constants.MP_DATA_DIR + suffix);
                } else {
                    File legacyDirectory = new File(MinecraftDirectory, Constants.SP_DATA_DIR + suffix);
                    testWorldDirectory = new File(MinecraftDirectory, Constants.SP_DATA_DIR + getWorldSaveFolderName());
                    if (!testWorldDirectory.exists() && legacyDirectory.exists()) {
                        testWorldDirectory = legacyDirectory;
                    }
                }
            } catch (Exception var5) {
                Journeymap.getLogger().log(org.apache.logging.log4j.Level.ERROR, LogFormatter.toString(var5));
            }
            return testWorldDirectory;
        } else {
            return null;
        }
    }

    public static String getWorldDirectoryName(Minecraft minecraft) {
        return getWorldDirectoryName(minecraft, JourneymapClient.getInstance().getCurrentWorldId());
    }

    public static String getWorldDirectoryName(Minecraft minecraft, String worldId) {
        if (minecraft != null && minecraft.level != null) {
            String worldName = CommonConstants.getSafeString(WorldData.getWorldName(minecraft), "~");
            if (worldId != null) {
                worldId = worldId.replaceAll("\\W+", "~");
            }
            String suffix = worldId != null ? "_" + worldId : "";
            return worldName + suffix;
        } else {
            return null;
        }
    }

    private static String getWorldSaveFolderName() {
        File file = Minecraft.getInstance().getSingleplayerServer().m_129843_(LevelResource.ROOT).toFile();
        String worldName = file.getParent().substring(file.getParent().lastIndexOf(File.separator) + 1);
        String worldId = JourneymapClient.getInstance().getCurrentWorldId();
        if (worldId != null) {
            worldId = worldId.replaceAll("\\W+", "~");
        }
        String suffix = worldId != null ? "_" + worldId : "";
        return worldName + suffix;
    }

    public static File getWaypointDir() {
        return getWaypointDir(getJMWorldDir(Minecraft.getInstance()));
    }

    public static File getWaypointDir(File jmWorldDir) {
        File waypointDir = new File(jmWorldDir, "waypoints");
        if (!waypointDir.isDirectory()) {
            waypointDir.delete();
        }
        if (!waypointDir.exists()) {
            waypointDir.mkdirs();
        }
        return waypointDir;
    }

    public static Properties getLangFile(String fileName) {
        try {
            InputStream is = JourneymapClient.class.getResourceAsStream("/assets/journeymap/lang/" + fileName);
            if (is == null) {
                File file = new File("../src/main/resources/assets/journeymap/lang/" + fileName);
                if (!file.exists()) {
                    Journeymap.getLogger().warn("Language file not found: " + fileName);
                    return null;
                }
                is = new FileInputStream(file);
            }
            Properties properties = new Properties();
            properties.load(is);
            is.close();
            return properties;
        } catch (IOException var3) {
            String error = "Could not get language file " + fileName + ": " + var3.getMessage();
            Journeymap.getLogger().error(error);
            return null;
        }
    }

    public static <M> M getMessageModel(Class<M> model, String filePrefix) {
        try {
            String lang = Minecraft.getInstance().getLanguageManager().getSelected();
            InputStream is = getMessageModelInputStream(filePrefix, lang);
            if (is == null && !lang.equals("en_US")) {
                is = getMessageModelInputStream(filePrefix, "en_US");
            }
            if (is == null) {
                Journeymap.getLogger().warn("Message file not found: " + filePrefix);
                return null;
            } else {
                return (M) new GsonBuilder().create().fromJson(new InputStreamReader(is), model);
            }
        } catch (Throwable var4) {
            String error = "Could not get Message model " + filePrefix + ": " + var4.getMessage();
            Journeymap.getLogger().error(error);
            return null;
        }
    }

    public static InputStream getMessageModelInputStream(String filePrefix, String lang) {
        String file = String.format("/assets/journeymap/lang/message/%s-%s.json", filePrefix, lang);
        return JourneymapClient.class.getResourceAsStream(file);
    }

    public static File getWorldConfigDir(boolean fallbackToStandardConfigDir) {
        File worldDir = getJMWorldDirForWorldId(Minecraft.getInstance(), null);
        if (worldDir != null && worldDir.exists()) {
            File worldConfigDir = new File(worldDir, "config");
            if (worldConfigDir.exists()) {
                return worldConfigDir;
            }
        }
        return fallbackToStandardConfigDir ? StandardConfigDirectory : null;
    }

    public static boolean isInJar() {
        return isInJar(JourneymapClient.class.getProtectionDomain().getCodeSource().getLocation());
    }

    public static boolean isInJar(URL location) {
        if ("jar".equals(location.getProtocol())) {
            return true;
        } else {
            if ("file".equals(location.getProtocol())) {
                String fromPath = URLDecoder.decode(location.getPath(), StandardCharsets.UTF_8).replace("file://", "");
                File file = new File(fromPath);
                if (file.exists() && (file.getName().endsWith(".jar") || file.getName().endsWith(".jar"))) {
                    return true;
                }
            }
            return false;
        }
    }

    public static File copyColorPaletteHtmlFile(File toDir, String fileName) {
        try {
            final File outFile = new File(toDir, fileName);
            String htmlPath = "/assets/journeymap/ui/" + fileName;
            InputStream inputStream = JourneymapClient.class.getResource(htmlPath).openStream();
            ByteSink out = new ByteSink() {

                public OutputStream openStream() throws IOException {
                    return new FileOutputStream(outFile);
                }
            };
            out.writeFrom(inputStream);
            return outFile;
        } catch (Throwable var6) {
            Journeymap.getLogger().warn("Couldn't copy color palette html: " + var6);
            return null;
        }
    }

    public static void open(File file) {
        String path = file.getAbsolutePath();
        if (Util.getPlatform() == Util.OS.OSX) {
            try {
                Runtime.getRuntime().exec(new String[] { "/usr/bin/open", path });
                return;
            } catch (IOException var6) {
                Journeymap.getLogger().error("Could not open path with /usr/bin/open: " + path + " : " + LogFormatter.toString(var6));
            }
        } else if (Util.getPlatform() == Util.OS.WINDOWS) {
            String cmd = String.format("cmd.exe /C start \"Open file\" \"%s\"", path);
            try {
                Runtime.getRuntime().exec(cmd);
                return;
            } catch (IOException var5) {
                Journeymap.getLogger().error("Could not open path with cmd.exe: " + path + " : " + LogFormatter.toString(var5));
            }
        }
        try {
            Class desktopClass = Class.forName("java.awt.Desktop");
            Object method = desktopClass.getMethod("getDesktop").invoke(null);
            desktopClass.getMethod("browse", URI.class).invoke(method, file.toURI());
        } catch (Throwable var4) {
            Journeymap.getLogger().error("Could not open path with Desktop: " + path + " : " + LogFormatter.toString(var4));
            String url = "file://" + path;
            Util.getPlatform().openUri(url);
        }
    }

    public static boolean copyResources(File targetDirectory, ResourceLocation location, String setName, boolean overwrite) {
        String fromPath = null;
        File toDir = null;
        try {
            String domain = location.getNamespace();
            URL fileLocation = null;
            if (domain.equals("minecraft")) {
                fileLocation = Minecraft.class.getProtectionDomain().getCodeSource().getLocation();
            } else {
                fileLocation = LoaderHooks.getModFileLocation(domain);
            }
            if (fileLocation != null) {
                String assetsPath;
                if (location.getPath().startsWith("assets/")) {
                    assetsPath = location.getPath();
                } else {
                    assetsPath = String.format("assets/%s/%s", domain, location.getPath());
                }
                return copyResources(targetDirectory, fileLocation, assetsPath, setName, overwrite);
            }
        } catch (Throwable var9) {
            Journeymap.getLogger().error(String.format("Couldn't get resource set from %s to %s: %s", fromPath, toDir, var9));
        }
        return false;
    }

    public static boolean copyResources(File targetDirectory, URL resourceDir, String assetsPath, String setName, boolean overwrite) {
        String fromPath = null;
        File toDir = null;
        try {
            toDir = new File(targetDirectory, setName);
            boolean inJar = isInJar(resourceDir);
            if (inJar) {
                if ("jar".equals(resourceDir.getProtocol())) {
                    fromPath = URLDecoder.decode(resourceDir.getPath(), StandardCharsets.UTF_8).split("file:")[1].split("!/")[0];
                } else {
                    fromPath = new File(resourceDir.getPath()).getPath();
                }
                return copyFromZip(fromPath, assetsPath, toDir, overwrite);
            }
            File fromDir = new File(resourceDir.getFile(), assetsPath);
            if (fromDir.exists()) {
                fromPath = fromDir.getPath();
                return copyFromDirectory(fromDir, toDir, overwrite);
            }
            Journeymap.getLogger().error(String.format("Couldn't locate icons for %s: %s", setName, fromDir));
        } catch (Throwable var9) {
            Journeymap.getLogger().error(String.format("Couldn't unzip resource set from %s to %s: %s", fromPath, toDir, var9));
        }
        return false;
    }

    static boolean copyFromZip(String zipFilePath, String zipEntryName, File destDir, boolean overWrite) throws Throwable {
        String fromPath = URLDecoder.decode(zipFilePath, StandardCharsets.UTF_8).replace("file://", "");
        if (zipEntryName.startsWith("/")) {
            zipEntryName = zipEntryName.substring(1);
        }
        ZipFile zipFile = new ZipFile(fromPath);
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(fromPath));
        ZipEntry entry = zipIn.getNextEntry();
        boolean success = false;
        try {
            while (entry != null) {
                if (entry.getName().startsWith(zipEntryName)) {
                    File toFile = new File(destDir, entry.getName().split(zipEntryName)[1]);
                    if ((overWrite || !toFile.exists()) && !entry.isDirectory()) {
                        Files.createParentDirs(toFile);
                        new FileHandler.ZipEntryByteSource(zipFile, entry).copyTo(Files.asByteSink(toFile, new FileWriteMode[0]));
                        success = true;
                    }
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } finally {
            zipIn.close();
        }
        return success;
    }

    static boolean copyFromDirectory(File fromDir, File toDir, boolean overWrite) throws IOException {
        if (!toDir.exists() && !toDir.mkdirs()) {
            throw new IOException("Couldn't create directory: " + toDir);
        } else {
            File[] files = fromDir.listFiles();
            if (files == null) {
                throw new IOException(fromDir + " nas no files");
            } else {
                boolean success = true;
                for (File from : files) {
                    File to = new File(toDir, from.getName());
                    if (from.isDirectory()) {
                        if (!copyFromDirectory(from, to, overWrite)) {
                            success = false;
                        }
                    } else if (overWrite || !to.exists()) {
                        Files.copy(from, to);
                        if (!to.exists()) {
                            success = false;
                        }
                    }
                }
                return success;
            }
        }
    }

    public static boolean delete(File file) {
        if (!file.exists()) {
            return true;
        } else if (file.isFile()) {
            return file.delete();
        } else {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException var2) {
                Journeymap.getLogger().error(String.format("Could not delete file:", LogFormatter.toString(var2)));
            }
            return file.exists();
        }
    }

    public static NativeImage getIconFromFile(File parentdir, String setName, String iconPath) {
        NativeImage img = null;
        if (iconPath == null) {
            iconPath = "null";
        }
        File iconFile = null;
        try {
            String filePath = Joiner.on(File.separatorChar).join(setName, iconPath.replace('/', File.separatorChar), new Object[0]);
            iconFile = new File(parentdir, filePath);
            if (iconFile.exists()) {
                InputStream is = new FileInputStream(iconFile.getPath());
                img = NativeImage.read(is);
            }
        } catch (Exception var7) {
            JMLogger.throwLogOnce("Couldn't load iconset file: " + iconFile, var7);
        }
        return img;
    }

    private static class ZipEntryByteSource extends ByteSource {

        final ZipFile file;

        final ZipEntry entry;

        ZipEntryByteSource(ZipFile file, ZipEntry entry) {
            this.file = file;
            this.entry = entry;
        }

        public InputStream openStream() throws IOException {
            return this.file.getInputStream(this.entry);
        }

        public String toString() {
            return String.format("ZipEntryByteSource( %s / %s )", this.file, this.entry);
        }
    }
}