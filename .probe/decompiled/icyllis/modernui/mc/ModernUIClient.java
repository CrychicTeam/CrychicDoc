package icyllis.modernui.mc;

import icyllis.arc3d.engine.DriverBugWorkarounds;
import icyllis.modernui.ModernUI;
import icyllis.modernui.graphics.text.EmojiFont;
import icyllis.modernui.graphics.text.FontFamily;
import icyllis.modernui.graphics.text.FontPaint;
import icyllis.modernui.graphics.text.LayoutCache;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.view.WindowManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.lang3.StringUtils;

public abstract class ModernUIClient extends ModernUI {

    private static volatile ModernUIClient sInstance;

    private static volatile boolean sBootstrap;

    public static volatile boolean sInventoryPause;

    public static volatile boolean sRemoveTelemetrySession;

    public static volatile float sFontScale = 1.0F;

    public static volatile boolean sUseColorEmoji;

    public static volatile boolean sEmojiShortcodes = true;

    public static volatile String sFirstFontFamily;

    public static volatile List<? extends String> sFallbackFontFamilyList;

    public static volatile List<? extends String> sFontRegistrationList;

    protected volatile Typeface mTypeface;

    protected volatile FontFamily mFirstFontFamily;

    private static Properties getBootstrapProperties() {
        if (!sBootstrap) {
            synchronized (ModernUIClient.class) {
                if (!sBootstrap) {
                    Path path = MuiPlatform.get().getBootstrapPath();
                    if (Files.exists(path, new LinkOption[0])) {
                        try {
                            InputStream is = Files.newInputStream(path, StandardOpenOption.READ);
                            try {
                                props.load(is);
                            } catch (Throwable var8) {
                                if (is != null) {
                                    try {
                                        is.close();
                                    } catch (Throwable var6) {
                                        var8.addSuppressed(var6);
                                    }
                                }
                                throw var8;
                            }
                            if (is != null) {
                                is.close();
                            }
                        } catch (IOException var9) {
                            LOGGER.error(MARKER, "Failed to load bootstrap file", var9);
                        }
                    } else {
                        try {
                            Files.createFile(path);
                        } catch (IOException var7) {
                            LOGGER.error(MARKER, "Failed to create bootstrap file", var7);
                        }
                    }
                    sBootstrap = true;
                }
            }
        }
        return props;
    }

    protected ModernUIClient() {
        sInstance = this;
    }

    public static ModernUIClient getInstance() {
        return sInstance;
    }

    public static boolean areShadersEnabled() {
        if (ModernUIMod.isOptiFineLoaded() && OptiFineIntegration.isShaderPackLoaded()) {
            return true;
        } else {
            return ModernUIMod.isIrisApiLoaded() ? IrisApiIntegration.isShaderPackInUse() : false;
        }
    }

    public static String getBootstrapProperty(String key) {
        Properties props = getBootstrapProperties();
        return props != null ? props.getProperty(key) : null;
    }

    public static void setBootstrapProperty(String key, String value) {
        Properties props = getBootstrapProperties();
        if (props != null) {
            props.setProperty(key, value);
            try {
                OutputStream os = Files.newOutputStream(MuiPlatform.get().getBootstrapPath(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                try {
                    props.store(os, "Modern UI bootstrap file");
                } catch (Throwable var7) {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }
                    throw var7;
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException var8) {
                LOGGER.error(MARKER, "Failed to write bootstrap file", var8);
            }
        }
    }

    @Nullable
    public static DriverBugWorkarounds getGpuDriverBugWorkarounds() {
        Properties props = getBootstrapProperties();
        if (props != null) {
            Map<String, Boolean> map = new HashMap();
            props.forEach((k, v) -> {
                if (k instanceof String key && v instanceof String value) {
                    Boolean state;
                    if (!"true".equalsIgnoreCase(value) && !"yes".equalsIgnoreCase(value) && !"enable".equalsIgnoreCase(value)) {
                        if (!"false".equalsIgnoreCase(value) && !"no".equalsIgnoreCase(value) && !"disable".equalsIgnoreCase(value)) {
                            return;
                        }
                        state = Boolean.FALSE;
                    } else {
                        state = Boolean.TRUE;
                    }
                    if (key.startsWith("arc3d_driverBugWorkarounds_")) {
                        map.put(key.substring(27), state);
                    }
                }
            });
            if (!map.isEmpty()) {
                return new DriverBugWorkarounds(map);
            }
        }
        return null;
    }

    public static void loadFonts(String first, @Nonnull Collection<String> fallbacks, @Nonnull Set<FontFamily> selected, @Nonnull Consumer<FontFamily> firstSetter, boolean firstLoad) {
        if (firstLoad) {
            FontResourceManager fontManager = FontResourceManager.getInstance();
            List<? extends String> registrationList = sFontRegistrationList;
            if (registrationList != null) {
                Iterator fallback = new LinkedHashSet(registrationList).iterator();
                label176: while (true) {
                    File[] entries;
                    while (true) {
                        if (!fallback.hasNext()) {
                            break label176;
                        }
                        String value = (String) fallback.next();
                        File file = new File(value.replaceAll("\\\\", "/"));
                        if (file.isDirectory()) {
                            entries = file.listFiles((dir, name) -> name.endsWith(".ttf") || name.endsWith(".otf") || name.endsWith(".ttc") || name.endsWith(".otc"));
                            if (entries == null) {
                                continue;
                            }
                            break;
                        }
                        entries = new File[] { file };
                        break;
                    }
                    for (File entry : entries) {
                        try {
                            FontFamily[] families = FontFamily.createFamilies(entry, true);
                            for (FontFamily f : families) {
                                fontManager.onFontRegistered(f);
                                LOGGER.info(MARKER, "Registered font '{}', path '{}'", f.getFamilyName(), entry);
                            }
                        } catch (Exception var27) {
                            LOGGER.error(MARKER, "Failed to register font '{}'", entry, var27);
                        }
                    }
                }
            }
            Path directory = Minecraft.getInstance().getResourcePackDirectory();
            try {
                DirectoryStream<Path> paths = Files.newDirectoryStream(directory);
                try {
                    for (Path p : paths) {
                        String name = p.getFileName().toString();
                        if (name.endsWith(".ttf") || name.endsWith(".otf") || name.endsWith(".ttc") || name.endsWith(".otc")) {
                            p = p.toAbsolutePath();
                            try {
                                FontFamily[] families = FontFamily.createFamilies(p.toFile(), true);
                                for (FontFamily f : families) {
                                    fontManager.onFontRegistered(f);
                                    LOGGER.info(MARKER, "Registered font '{}', path '{}'", f.getFamilyName(), p);
                                }
                            } catch (Exception var24) {
                                LOGGER.error(MARKER, "Failed to register font '{}'", p, var24);
                            }
                        }
                    }
                } catch (Throwable var25) {
                    if (paths != null) {
                        try {
                            paths.close();
                        } catch (Throwable var21) {
                            var25.addSuppressed(var21);
                        }
                    }
                    throw var25;
                }
                if (paths != null) {
                    paths.close();
                }
            } catch (IOException var26) {
                LOGGER.error(MARKER, "Failed to open resource pack directory", var26);
            }
            ResourceManager resources = Minecraft.getInstance().getResourceManager();
            for (Entry<ResourceLocation, List<Resource>> entry : resources.listResourceStacks("font", res -> {
                if (!res.getNamespace().equals("modernui")) {
                    return false;
                } else {
                    String px = res.getPath();
                    return px.endsWith(".ttf") || px.endsWith(".otf") || px.endsWith(".ttc") || px.endsWith(".otc");
                }
            }).entrySet()) {
                for (Resource resource : (List) entry.getValue()) {
                    try {
                        InputStream inputStream = resource.open();
                        try {
                            FontFamily[] families = FontFamily.createFamilies(inputStream, true);
                            for (FontFamily f : families) {
                                fontManager.onFontRegistered(f);
                                LOGGER.info(MARKER, "Registered font '{}', location '{}' in pack: '{}'", f.getFamilyName(), entry.getKey(), resource.sourcePackId());
                            }
                        } catch (Throwable var22) {
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable var20) {
                                    var22.addSuppressed(var20);
                                }
                            }
                            throw var22;
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (Exception var23) {
                        LOGGER.error(MARKER, "Failed to register font '{}' in pack: '{}'", entry.getKey(), resource.sourcePackId(), var23);
                    }
                }
            }
        }
        boolean hasFail = loadSingleFont(first, selected, firstSetter);
        for (String fallback : fallbacks) {
            hasFail |= loadSingleFont(fallback, selected, null);
        }
        if (hasFail && ModernUIMod.isDeveloperMode()) {
            LOGGER.debug(MARKER, "Available system font families:\n{}", String.join("\n", FontFamily.getSystemFontMap().keySet()));
        }
    }

    private static boolean loadSingleFont(String value, @Nonnull Set<FontFamily> selected, @Nullable Consumer<FontFamily> firstSetter) {
        if (StringUtils.isEmpty(value)) {
            return true;
        } else {
            try {
                File f = new File(value.replaceAll("\\\\", "/"));
                FontFamily family = FontFamily.createFamily(f, false);
                selected.add(family);
                LOGGER.debug(MARKER, "Font '{}' was loaded with config value '{}' as LOCAL FILE", family.getFamilyName(), value);
                if (firstSetter != null) {
                    firstSetter.accept(family);
                }
                return true;
            } catch (Exception var5) {
                FontFamily family = FontFamily.getSystemFontWithAlias(value);
                if (family == null) {
                    Optional<FontFamily> optional = FontFamily.getSystemFontMap().values().stream().filter(f -> f.getFamilyName().equalsIgnoreCase(value)).findFirst();
                    if (optional.isPresent()) {
                        family = (FontFamily) optional.get();
                    }
                }
                if (family != null) {
                    selected.add(family);
                    LOGGER.debug(MARKER, "Font '{}' was loaded with config value '{}' as SYSTEM FONT", family.getFamilyName(), value);
                    if (firstSetter != null) {
                        firstSetter.accept(family);
                    }
                    return true;
                } else {
                    LOGGER.info(MARKER, "Font '{}' failed to load or invalid", value);
                    return false;
                }
            }
        }
    }

    @Nonnull
    @Override
    protected Typeface onGetSelectedTypeface() {
        if (this.mTypeface != null) {
            return this.mTypeface;
        } else {
            synchronized (this) {
                if (this.mTypeface == null) {
                    this.checkFirstLoadTypeface();
                    this.mTypeface = loadTypefaceInternal(this::setFirstFontFamily, true);
                    FontPaint paint = new FontPaint();
                    paint.setFont(this.mTypeface);
                    paint.setLocale(Locale.ROOT);
                    paint.setFontSize(12);
                    Minecraft.getInstance().m_6937_(() -> LayoutCache.getOrCreate(new char[] { 'M' }, 0, 1, 0, 1, false, paint, 0));
                    LOGGER.info(MARKER, "Loaded typeface: {}", this.mTypeface);
                }
            }
            return this.mTypeface;
        }
    }

    protected abstract void checkFirstLoadTypeface();

    public void reloadTypeface() {
        synchronized (this) {
            boolean firstLoad = this.mTypeface == null;
            this.mFirstFontFamily = null;
            this.mTypeface = loadTypefaceInternal(this::setFirstFontFamily, firstLoad);
            LOGGER.info(MARKER, "{} typeface: {}", firstLoad ? "Loaded" : "Reloaded", this.mTypeface);
        }
    }

    public void reloadFontStrike() {
        Minecraft.getInstance().m_18707_(() -> FontResourceManager.getInstance().reloadAll());
    }

    @Nonnull
    private static Typeface loadTypefaceInternal(@Nonnull Consumer<FontFamily> firstSetter, boolean firstLoad) {
        Set<FontFamily> families = new LinkedHashSet();
        if (sUseColorEmoji) {
            EmojiFont emojiFont = FontResourceManager.getInstance().getEmojiFont();
            if (emojiFont != null) {
                FontFamily colorEmojiFamily = new FontFamily(emojiFont);
                families.add(colorEmojiFamily);
            }
        }
        String first = sFirstFontFamily;
        List<? extends String> configs = sFallbackFontFamilyList;
        if (first != null || configs != null) {
            LinkedHashSet<String> fallbacks = new LinkedHashSet();
            if (configs != null) {
                fallbacks.addAll(configs);
            }
            if (first != null) {
                fallbacks.remove(first);
            }
            loadFonts(first, fallbacks, families, firstSetter, firstLoad);
        }
        return Typeface.createTypeface((FontFamily[]) families.toArray(new FontFamily[0]));
    }

    @Nullable
    public FontFamily getFirstFontFamily() {
        return this.mFirstFontFamily;
    }

    protected void setFirstFontFamily(FontFamily firstFontFamily) {
        this.mFirstFontFamily = firstFontFamily;
    }

    @Nonnull
    @Override
    public InputStream getResourceStream(@Nonnull String namespace, @Nonnull String path) throws IOException {
        return Minecraft.getInstance().getResourceManager().m_215595_(new ResourceLocation(namespace, path));
    }

    @Nonnull
    @Override
    public ReadableByteChannel getResourceChannel(@Nonnull String namespace, @Nonnull String path) throws IOException {
        return Channels.newChannel(this.getResourceStream(namespace, path));
    }

    @Override
    public WindowManager getWindowManager() {
        return UIManager.getInstance().getDecorView();
    }
}