package icyllis.modernui.mc.text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ibm.icu.text.Bidi;
import com.mojang.blaze3d.font.SpaceProvider;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.JsonOps;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.Bitmap;
import icyllis.modernui.graphics.font.BakedGlyph;
import icyllis.modernui.graphics.font.GlyphManager;
import icyllis.modernui.graphics.text.Font;
import icyllis.modernui.graphics.text.FontCollection;
import icyllis.modernui.graphics.text.FontFamily;
import icyllis.modernui.graphics.text.OutlineFont;
import icyllis.modernui.mc.FontResourceManager;
import icyllis.modernui.mc.ModernUIClient;
import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.text.mixin.AccessFontManager;
import icyllis.modernui.text.TextDirectionHeuristic;
import icyllis.modernui.text.TextDirectionHeuristics;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.util.Pools;
import java.awt.font.GlyphVector;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.providers.BitmapProvider;
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition;
import net.minecraft.client.gui.font.providers.ProviderReferenceDefinition;
import net.minecraft.client.gui.font.providers.TrueTypeGlyphProviderDefinition;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.DependencySorter;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class TextLayoutEngine extends FontResourceManager implements MuiModApi.OnWindowResizeListener, MuiModApi.OnDebugDumpListener {

    public static final Marker MARKER = MarkerManager.getMarker("TextLayout");

    public static volatile boolean sFixedResolution = false;

    public static volatile int sTextDirection = 1;

    public static volatile int sCacheLifespan = 6;

    public static boolean sCurrentInWorldRendering;

    public static volatile boolean sUseTextShadersInWorld = true;

    public static volatile boolean sRawUseTextShadersInWorld = true;

    public static final ResourceLocation SANS_SERIF = ModernUIMod.location("sans-serif");

    public static final ResourceLocation SERIF = ModernUIMod.location("serif");

    public static final ResourceLocation MONOSPACED = ModernUIMod.location("monospace");

    public static final int COMPUTE_ADVANCES = 1;

    public static final int COMPUTE_LINE_BOUNDARIES = 4;

    public static final int DEFAULT_FONT_BEHAVIOR_IGNORE_ALL = 0;

    public static final int DEFAULT_FONT_BEHAVIOR_KEEP_ASCII = 1;

    public static final int DEFAULT_FONT_BEHAVIOR_KEEP_OTHER = 2;

    public static final int DEFAULT_FONT_BEHAVIOR_KEEP_ALL = 3;

    public static final int DEFAULT_FONT_BEHAVIOR_ONLY_INCLUDE = 4;

    public static final int DEFAULT_FONT_BEHAVIOR_ONLY_EXCLUDE = 5;

    public static volatile int sDefaultFontBehavior = 0;

    public static volatile List<? extends String> sDefaultFontRuleSet;

    public static volatile boolean sUseComponentCache = true;

    public static volatile boolean sAllowAsyncLayout = true;

    private final VanillaLayoutKey mVanillaLookupKey = new VanillaLayoutKey();

    private Map<VanillaLayoutKey, TextLayout> mVanillaCache = new HashMap();

    private Map<MutableComponent, TextLayout> mComponentCache = new HashMap();

    private final FormattedLayoutKey.Lookup mFormattedLayoutKey = new FormattedLayoutKey.Lookup();

    private Map<FormattedLayoutKey, TextLayout> mFormattedCache = new HashMap();

    private final TextLayoutProcessor mProcessor = new TextLayoutProcessor(this);

    private final Pools.Pool<TextLayoutProcessor> mProcessorPool = Pools.newSynchronizedPool(3);

    private final Map<TextLayoutEngine.FontStrikeDesc, TextLayoutEngine.FastCharSet> mFastCharMap = new HashMap();

    private final Function<TextLayoutEngine.FontStrikeDesc, TextLayoutEngine.FastCharSet> mCacheFastChars = this::cacheFastChars;

    private final HashMap<ResourceLocation, FontCollection> mFontCollections = new HashMap();

    private FontCollection mRawDefaultFontCollection;

    private final ConcurrentHashMap<ResourceLocation, FontCollection> mRegisteredFonts = new ConcurrentHashMap();

    public static final int MIN_PIXEL_DENSITY_FOR_SDF = 4;

    private volatile int mResLevel = 2;

    private TextDirectionHeuristic mTextDirectionHeuristic = TextDirectionHeuristics.FIRSTSTRONG_LTR;

    private FontManager mVanillaFontManager;

    private final ModernTextRenderer mTextRenderer;

    private final ModernStringSplitter mStringSplitter;

    private Boolean mForceUnicodeFont;

    private int mTimer;

    public TextLayoutEngine() {
        this.mGlyphManager.addAtlasInvalidationCallback(invalidationInfo -> {
            if (invalidationInfo.resize()) {
                TextRenderType.clear(false);
            } else {
                this.reload();
            }
        });
        this.mTextRenderer = new ModernTextRenderer(this);
        this.mStringSplitter = new ModernStringSplitter(this, (ch, style) -> {
            throw new UnsupportedOperationException("Modern Text Engine");
        });
        ModernUI.LOGGER.info(ModernUI.MARKER, "Created TextLayoutEngine");
    }

    @Nonnull
    public static TextLayoutEngine getInstance() {
        return (TextLayoutEngine) FontResourceManager.getInstance();
    }

    @Nonnull
    public GlyphManager getGlyphManager() {
        return this.mGlyphManager;
    }

    @Nonnull
    public ModernTextRenderer getTextRenderer() {
        return this.mTextRenderer;
    }

    @Nonnull
    public ModernStringSplitter getStringSplitter() {
        return this.mStringSplitter;
    }

    public FontCollection getRawDefaultFontCollection() {
        return this.mRawDefaultFontCollection;
    }

    public void clear() {
        int count = this.getCacheCount();
        this.mVanillaCache.clear();
        this.mComponentCache.clear();
        this.mFormattedCache.clear();
        this.mVanillaCache = new HashMap();
        this.mComponentCache = new HashMap();
        this.mFormattedCache = new HashMap();
        this.mFastCharMap.clear();
        TextRenderType.clear(false);
        if (count > 0) {
            ModernUI.LOGGER.debug(MARKER, "Cleanup {} text layout entries", count);
        }
    }

    @RenderThread
    public void reload() {
        this.clear();
        ModernUI ctx = ModernUI.getInstance();
        int scale;
        if (ctx != null) {
            scale = Math.round(ctx.getResources().getDisplayMetrics().density * 2.0F);
        } else {
            scale = 2;
        }
        int oldLevel = this.mResLevel;
        if (sFixedResolution) {
            this.mResLevel = 2;
        } else {
            this.mResLevel = Math.min(scale, 8);
        }
        Options opts = Minecraft.getInstance().options;
        if (opts != null) {
            this.mForceUnicodeFont = opts.forceUnicodeFont().get();
        }
        Locale locale = ModernUI.getSelectedLocale();
        boolean layoutRtl = TextUtils.getLayoutDirectionFromLocale(locale) == 1;
        this.mTextDirectionHeuristic = switch(sTextDirection) {
            case 2 ->
                TextDirectionHeuristics.ANYRTL_LTR;
            case 3 ->
                TextDirectionHeuristics.LTR;
            case 4 ->
                TextDirectionHeuristics.RTL;
            case 5 ->
                TextDirectionHeuristics.LOCALE;
            case 6 ->
                TextDirectionHeuristics.FIRSTSTRONG_LTR;
            case 7 ->
                TextDirectionHeuristics.FIRSTSTRONG_RTL;
            default ->
                layoutRtl ? TextDirectionHeuristics.FIRSTSTRONG_RTL : TextDirectionHeuristics.FIRSTSTRONG_LTR;
        };
        this.mFontCollections.putIfAbsent(SANS_SERIF, Typeface.SANS_SERIF);
        this.mFontCollections.putIfAbsent(SERIF, Typeface.SERIF);
        this.mFontCollections.putIfAbsent(MONOSPACED, Typeface.MONOSPACED);
        this.mFontCollections.putIfAbsent(new ResourceLocation(SANS_SERIF.getPath()), Typeface.SANS_SERIF);
        this.mFontCollections.putIfAbsent(new ResourceLocation(SERIF.getPath()), Typeface.SERIF);
        this.mFontCollections.putIfAbsent(new ResourceLocation(MONOSPACED.getPath()), Typeface.MONOSPACED);
        if (sDefaultFontBehavior != 0 && (sDefaultFontBehavior != 4 || sDefaultFontRuleSet != null && !sDefaultFontRuleSet.isEmpty())) {
            LinkedHashSet<FontFamily> defaultFonts = new LinkedHashSet();
            this.populateDefaultFonts(defaultFonts, sDefaultFontBehavior);
            defaultFonts.addAll(ModernUI.getSelectedTypeface().getFamilies());
            this.mFontCollections.put(Minecraft.DEFAULT_FONT, new FontCollection((FontFamily[]) defaultFonts.toArray(new FontFamily[0])));
        } else {
            this.mFontCollections.put(Minecraft.DEFAULT_FONT, ModernUI.getSelectedTypeface());
        }
        if (this.mVanillaFontManager != null) {
            Map<ResourceLocation, FontSet> fontSets = ((AccessFontManager) this.mVanillaFontManager).getFontSets();
            if (fontSets.get(Minecraft.DEFAULT_FONT) instanceof StandardFontSet standardFontSet) {
                standardFontSet.reload((FontCollection) this.mFontCollections.get(Minecraft.DEFAULT_FONT), this.mResLevel);
            }
            for (java.util.Map.Entry<ResourceLocation, FontSet> e : fontSets.entrySet()) {
                if (!((ResourceLocation) e.getKey()).equals(Minecraft.DEFAULT_FONT) && e.getValue() instanceof StandardFontSet standardFontSet) {
                    standardFontSet.invalidateCache(this.mResLevel);
                }
            }
        }
        ModernUI.LOGGER.info(MARKER, "Reloaded text layout engine, res level: {} to {}, locale: {}, layout RTL: {}", oldLevel, this.mResLevel, locale, layoutRtl);
    }

    @RenderThread
    @Override
    public void reloadAll() {
        super.reloadAll();
        this.reload();
    }

    @Override
    public void onWindowResize(int width, int height, int newScale, int oldScale) {
        if (Core.getRenderThread() != null) {
            boolean reload = newScale != oldScale;
            if (!reload) {
                Boolean forceUnicodeFont = Minecraft.getInstance().options.forceUnicodeFont().get();
                reload = !Objects.equals(this.mForceUnicodeFont, forceUnicodeFont);
            }
            if (reload) {
                this.reload();
            }
        }
    }

    @Override
    public void onDebugDump(@Nonnull PrintWriter pw) {
        pw.print("TextLayoutEngine: ");
        pw.print("CacheCount=" + this.getCacheCount());
        long memorySize = (long) this.getCacheMemorySize();
        pw.println(", CacheSize=" + TextUtils.binaryCompact(memorySize) + " (" + memorySize + " bytes)");
    }

    @Nonnull
    public TextLayoutEngine injectFontManager(@Nonnull FontManager manager) {
        this.mVanillaFontManager = manager;
        return this;
    }

    private void populateDefaultFonts(Set<FontFamily> set, int behavior) {
        if (this.mRawDefaultFontCollection != null) {
            if (behavior != 4 && behavior != 5) {
                for (FontFamily family : this.mRawDefaultFontCollection.getFamilies()) {
                    String var13 = family.getFamilyName();
                    switch(var13) {
                        case "minecraft:font/nonlatin_european.png":
                        case "minecraft:font/accented.png":
                        case "minecraft:font/ascii.png":
                        case "minecraft:include/space / minecraft:space":
                            if ((behavior & 1) != 0) {
                                set.add(family);
                            }
                            break;
                        default:
                            if ((behavior & 2) != 0) {
                                set.add(family);
                            }
                    }
                }
            } else {
                Pattern pattern = null;
                List<? extends String> rules = sDefaultFontRuleSet;
                if (rules != null && !rules.isEmpty()) {
                    try {
                        pattern = Pattern.compile((String) rules.stream().distinct().collect(Collectors.joining("|")));
                    } catch (PatternSyntaxException var10) {
                        ModernUI.LOGGER.warn(MARKER, "Illegal default font rules: {}", rules, var10);
                    }
                }
                boolean exclusive = behavior == 5;
                for (FontFamily family : this.mRawDefaultFontCollection.getFamilies()) {
                    String name = family.getFamilyName();
                    boolean matches = pattern != null && pattern.matcher(name).matches();
                    if (matches ^ exclusive) {
                        set.add(family);
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public CompletableFuture<Void> reload(@Nonnull PreparableReloadListener.PreparationBarrier preparationBarrier, @Nonnull ResourceManager resourceManager, @Nonnull ProfilerFiller preparationProfiler, @Nonnull ProfilerFiller reloadProfiler, @Nonnull Executor preparationExecutor, @Nonnull Executor reloadExecutor) {
        preparationProfiler.startTick();
        preparationProfiler.endTick();
        return this.prepareResources(resourceManager, preparationExecutor).thenCompose(preparationBarrier::m_6769_).thenAcceptAsync(results -> {
            reloadProfiler.startTick();
            reloadProfiler.push("reload");
            this.applyResources(results);
            reloadProfiler.pop();
            reloadProfiler.endTick();
        }, reloadExecutor);
    }

    @Nonnull
    private CompletableFuture<TextLayoutEngine.LoadResults> prepareResources(@Nonnull ResourceManager resourceManager, @Nonnull Executor preparationExecutor) {
        TextLayoutEngine.LoadResults results = new TextLayoutEngine.LoadResults();
        CompletableFuture<Void> loadFonts = CompletableFuture.runAsync(() -> loadFonts(resourceManager, results), preparationExecutor);
        CompletableFuture<Void> loadEmojis = CompletableFuture.runAsync(() -> loadEmojis(resourceManager, results), preparationExecutor);
        CompletableFuture<Void> loadShortcodes = CompletableFuture.runAsync(() -> loadShortcodes(resourceManager, results), preparationExecutor);
        return CompletableFuture.allOf(loadFonts, loadEmojis, loadShortcodes).thenApply(__ -> results);
    }

    private void applyResources(@Nonnull TextLayoutEngine.LoadResults results) {
        this.closeFonts();
        this.mFontCollections.clear();
        this.mFontCollections.putAll(this.mRegisteredFonts);
        this.mFontCollections.putAll(results.mFontCollections);
        this.mRawDefaultFontCollection = (FontCollection) this.mFontCollections.get(Minecraft.DEFAULT_FONT);
        if (this.mVanillaFontManager != null) {
            Map<ResourceLocation, FontSet> fontSets = ((AccessFontManager) this.mVanillaFontManager).getFontSets();
            fontSets.values().forEach(FontSet::close);
            fontSets.clear();
            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            this.mFontCollections.forEach((fontName, fontCollection) -> {
                StandardFontSet fontSetx = new StandardFontSet(textureManager, fontName);
                fontSetx.reload(fontCollection, this.mResLevel);
                fontSets.put(fontName, fontSetx);
            });
            StandardFontSet fontSet = new StandardFontSet(textureManager, Minecraft.UNIFORM_FONT);
            fontSet.reload(ModernUI.getSelectedTypeface(), this.mResLevel);
            fontSets.put(Minecraft.UNIFORM_FONT, fontSet);
        } else {
            ModernUI.LOGGER.warn(MARKER, "Where is font manager?");
        }
        if (this.mRawDefaultFontCollection == null) {
            throw new IllegalStateException("Default font failed to load");
        } else {
            super.applyResources(results);
        }
    }

    @Override
    public void close() {
        this.closeFonts();
        TextRenderType.clear(true);
    }

    private void closeFonts() {
        for (FontCollection fontCollection : this.mFontCollections.values()) {
            for (FontFamily family : fontCollection.getFamilies()) {
                if (family.getClosestMatch(0) instanceof BitmapFont bitmapFont) {
                    bitmapFont.close();
                }
            }
        }
        if (this.mRawDefaultFontCollection != null) {
            for (FontFamily familyx : this.mRawDefaultFontCollection.getFamilies()) {
                if (familyx.getClosestMatch(0) instanceof BitmapFont bitmapFont) {
                    bitmapFont.close();
                }
            }
        }
    }

    @Override
    public void onFontRegistered(@Nonnull FontFamily f) {
        super.onFontRegistered(f);
        String name = f.getFamilyName();
        try {
            String newName = name.toLowerCase(Locale.ROOT).replaceAll(" ", "-");
            FontCollection fc = new FontCollection(f);
            ResourceLocation location = ModernUIMod.location(newName);
            if (this.mRegisteredFonts.putIfAbsent(location, fc) == null) {
                ModernUI.LOGGER.info(MARKER, "Redirect registered font '{}' to '{}'", name, location);
                this.mRegisteredFonts.putIfAbsent(new ResourceLocation(newName), fc);
            }
        } catch (Exception var6) {
            ModernUI.LOGGER.warn(MARKER, "Failed to redirect registered font '{}'", name);
        }
    }

    private static boolean isUnicodeFont(@Nonnull ResourceLocation name) {
        if (name.equals(Minecraft.UNIFORM_FONT)) {
            return true;
        } else {
            return name.getNamespace().equals("minecraft") ? name.getPath().equals("include/unifont") : false;
        }
    }

    private static void loadFonts(@Nonnull ResourceManager resources, @Nonnull TextLayoutEngine.LoadResults results) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        ArrayList<TextLayoutEngine.RawFontBundle> bundles = new ArrayList();
        for (java.util.Map.Entry<ResourceLocation, List<Resource>> entry : resources.listResourceStacks("font", res -> res.getPath().endsWith(".json")).entrySet()) {
            ResourceLocation location = (ResourceLocation) entry.getKey();
            String path = location.getPath();
            ResourceLocation name = location.withPath(path.substring(5, path.length() - 5));
            if (!isUnicodeFont(name)) {
                TextLayoutEngine.RawFontBundle bundle = new TextLayoutEngine.RawFontBundle(name);
                bundles.add(bundle);
                for (Resource resource : (List) entry.getValue()) {
                    try {
                        BufferedReader reader = resource.openAsReader();
                        try {
                            JsonArray providers = GsonHelper.getAsJsonArray((JsonObject) Objects.requireNonNull((JsonObject) GsonHelper.fromJson(gson, reader, JsonObject.class)), "providers");
                            for (int i = 0; i < providers.size(); i++) {
                                JsonObject metadata = GsonHelper.convertToJsonObject(providers.get(i), "providers[" + i + "]");
                                GlyphProviderDefinition definition = Util.getOrThrow(GlyphProviderDefinition.CODEC.parse(JsonOps.INSTANCE, metadata), JsonParseException::new);
                                loadSingleFont(resources, name, bundle, resource.sourcePackId(), i, metadata, definition);
                            }
                            ModernUI.LOGGER.info(MARKER, "Loaded raw font '{}' in pack: '{}'", name, resource.sourcePackId());
                        } catch (Throwable var18) {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (Throwable var17) {
                                    var18.addSuppressed(var17);
                                }
                            }
                            throw var18;
                        }
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (Exception var19) {
                        ModernUI.LOGGER.warn(MARKER, "Failed to load font '{}' in pack: '{}'", name, resource.sourcePackId(), var19);
                    }
                }
                ModernUI.LOGGER.info(MARKER, "Loaded raw font bundle: '{}', font set: [{}]", location, bundle.families.stream().map(object -> object instanceof FontFamily family ? family.getFamilyName() : object.toString()).collect(Collectors.joining(",")));
            }
        }
        DependencySorter<ResourceLocation, TextLayoutEngine.RawFontBundle> sorter = new DependencySorter<>();
        for (TextLayoutEngine.RawFontBundle bundle : bundles) {
            sorter.addEntry(bundle.name, bundle);
        }
        HashMap<ResourceLocation, FontCollection> map = new HashMap();
        sorter.orderByDependencies((namex, bundlex) -> {
            if (!isUnicodeFont(namex)) {
                LinkedHashSet<FontFamily> set = new LinkedHashSet();
                for (Object object : bundlex.families) {
                    if (object instanceof FontFamily family) {
                        set.add(family);
                    } else {
                        ResourceLocation reference = (ResourceLocation) object;
                        FontCollection resolved = (FontCollection) map.get(reference);
                        if (resolved != null) {
                            set.addAll(resolved.getFamilies());
                        } else {
                            ModernUI.LOGGER.warn(MARKER, "Failed to resolve font: {}", reference);
                        }
                    }
                }
                if (!set.isEmpty()) {
                    map.put(namex, new FontCollection((FontFamily[]) set.toArray(new FontFamily[0])));
                    ModernUI.LOGGER.info(MARKER, "Loaded font: '{}', font set: [{}]", namex, set.stream().map(FontFamily::getFamilyName).collect(Collectors.joining(",")));
                } else {
                    ModernUI.LOGGER.warn(MARKER, "Ignore font: '{}', because it's empty", namex);
                }
            }
        });
        results.mFontCollections = map;
    }

    private static void loadSingleFont(@Nonnull ResourceManager resources, ResourceLocation name, TextLayoutEngine.RawFontBundle bundle, String sourcePackId, int index, JsonObject metadata, @Nonnull GlyphProviderDefinition definition) {
        switch(definition.type()) {
            case BITMAP:
                BitmapFont bitmapFont = BitmapFont.create((BitmapProvider.Definition) definition, resources);
                bundle.families.add(new FontFamily(bitmapFont));
                break;
            case TTF:
                TrueTypeGlyphProviderDefinition ttf = (TrueTypeGlyphProviderDefinition) definition;
                if (metadata.has("size")) {
                    ModernUI.LOGGER.info(MARKER, "Ignore 'size={}' of providers[{}] in font '{}' in pack: '{}'", ttf.size(), index, name, sourcePackId);
                }
                if (metadata.has("oversample")) {
                    ModernUI.LOGGER.info(MARKER, "Ignore 'oversample={}' of providers[{}] in font '{}' in pack: '{}'", ttf.oversample(), index, name, sourcePackId);
                }
                if (metadata.has("shift")) {
                    ModernUI.LOGGER.info(MARKER, "Ignore 'shift={}' of providers[{}] in font '{}' in pack: '{}'", ttf.shift(), index, name, sourcePackId);
                }
                if (metadata.has("skip")) {
                    ModernUI.LOGGER.info(MARKER, "Ignore 'skip={}' of providers[{}] in font '{}' in pack: '{}'", ttf.skip(), index, name, sourcePackId);
                }
                bundle.families.add(createTTF(ttf.location(), resources));
                break;
            case SPACE:
                SpaceFont spaceFont = SpaceFont.create(name, (SpaceProvider.Definition) definition);
                bundle.families.add(new FontFamily(spaceFont));
                break;
            case REFERENCE:
                ResourceLocation reference = ((ProviderReferenceDefinition) definition).id();
                if (!isUnicodeFont(reference)) {
                    bundle.families.add(reference);
                    bundle.dependencies.add(reference);
                }
                break;
            default:
                ModernUI.LOGGER.info(MARKER, "Unknown provider type '{}' in font '{}' in pack: '{}'", definition.type(), name, sourcePackId);
        }
    }

    @Nonnull
    private static FontFamily createTTF(@Nonnull ResourceLocation file, ResourceManager resources) {
        ResourceLocation location = file.withPrefix("font/");
        try {
            InputStream stream = resources.m_215595_(location);
            FontFamily var4;
            try {
                var4 = FontFamily.createFamily(stream, false);
            } catch (Throwable var7) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if (stream != null) {
                stream.close();
            }
            return var4;
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }
    }

    public static int adjustPixelDensityForSDF(int resLevel) {
        return Math.max(resLevel, 4);
    }

    @Nonnull
    public TextLayout lookupVanillaLayout(@Nonnull String text) {
        return this.lookupVanillaLayout(text, Style.EMPTY, 0);
    }

    @Nonnull
    public TextLayout lookupVanillaLayout(@Nonnull String text, @Nonnull Style style) {
        return this.lookupVanillaLayout(text, style, 0);
    }

    @Nonnull
    public TextLayout lookupVanillaLayout(@Nonnull String text, @Nonnull Style style, int computeFlags) {
        if (text.isEmpty()) {
            return TextLayout.EMPTY;
        } else if (!RenderSystem.isOnRenderThread()) {
            if (sAllowAsyncLayout) {
                TextLayoutProcessor proc = this.mProcessorPool.acquire();
                if (proc == null) {
                    proc = new TextLayoutProcessor(this);
                }
                TextLayout layout = proc.createVanillaLayout(text, style, this.mResLevel, computeFlags);
                this.mProcessorPool.release(proc);
                return layout;
            } else {
                return (TextLayout) Minecraft.getInstance().m_18691_(() -> this.lookupVanillaLayout(text, style, computeFlags)).join();
            }
        } else {
            TextLayout layout = (TextLayout) this.mVanillaCache.get(this.mVanillaLookupKey.update(text, style));
            int nowFlags = 0;
            if (layout != null) {
                nowFlags = layout.mComputedFlags;
                if ((layout.mComputedFlags & computeFlags) == computeFlags) {
                    return layout.get();
                }
            }
            layout = this.mProcessor.createVanillaLayout(text, style, this.mResLevel, nowFlags | computeFlags);
            this.mVanillaCache.put(this.mVanillaLookupKey.copy(), layout);
            return layout;
        }
    }

    @Nonnull
    public TextLayout lookupFormattedLayout(@Nonnull FormattedText text) {
        return this.lookupFormattedLayout(text, Style.EMPTY, 0);
    }

    @Nonnull
    public TextLayout lookupFormattedLayout(@Nonnull FormattedText text, @Nonnull Style style) {
        return this.lookupFormattedLayout(text, style, 0);
    }

    @Nonnull
    public TextLayout lookupFormattedLayout(@Nonnull FormattedText text, @Nonnull Style style, int computeFlags) {
        if (text == CommonComponents.EMPTY || text == FormattedText.EMPTY) {
            return TextLayout.EMPTY;
        } else if (RenderSystem.isOnRenderThread()) {
            int nowFlags;
            label43: {
                label53: {
                    nowFlags = 0;
                    TextLayout layout;
                    if (style.isEmpty() && sUseComponentCache && text instanceof MutableComponent component) {
                        layout = (TextLayout) this.mComponentCache.get(component);
                        if (layout == null) {
                            break label43;
                        }
                        nowFlags = layout.mComputedFlags;
                        if ((layout.mComputedFlags & computeFlags) != computeFlags) {
                            break label43;
                        }
                    } else {
                        layout = (TextLayout) this.mFormattedCache.get(this.mFormattedLayoutKey.update(text, style));
                        if (layout == null) {
                            break label53;
                        }
                        nowFlags = layout.mComputedFlags;
                        if ((layout.mComputedFlags & computeFlags) != computeFlags) {
                            break label53;
                        }
                    }
                    return layout.get();
                }
                TextLayout var8 = this.mProcessor.createTextLayout(text, style, this.mResLevel, nowFlags | computeFlags);
                this.mFormattedCache.put(this.mFormattedLayoutKey.copy(), var8);
                return var8;
            }
            TextLayout var9 = this.mProcessor.createTextLayout(text, Style.EMPTY, this.mResLevel, nowFlags | computeFlags);
            this.mComponentCache.put(component, var9);
            return var9;
        } else if (sAllowAsyncLayout) {
            TextLayoutProcessor proc = this.mProcessorPool.acquire();
            if (proc == null) {
                proc = new TextLayoutProcessor(this);
            }
            TextLayout layout = proc.createTextLayout(text, style, this.mResLevel, computeFlags);
            this.mProcessorPool.release(proc);
            return layout;
        } else {
            return (TextLayout) Minecraft.getInstance().m_18691_(() -> this.lookupFormattedLayout(text, style, computeFlags)).join();
        }
    }

    @Nonnull
    public TextLayout lookupFormattedLayout(@Nonnull FormattedCharSequence sequence) {
        return this.lookupFormattedLayout(sequence, 0);
    }

    @Nonnull
    public TextLayout lookupFormattedLayout(@Nonnull FormattedCharSequence sequence, int computeFlags) {
        if (sequence == FormattedCharSequence.EMPTY) {
            return TextLayout.EMPTY;
        } else if (!RenderSystem.isOnRenderThread()) {
            if (sAllowAsyncLayout) {
                TextLayoutProcessor proc = this.mProcessorPool.acquire();
                if (proc == null) {
                    proc = new TextLayoutProcessor(this);
                }
                TextLayout layout = proc.createSequenceLayout(sequence, this.mResLevel, computeFlags);
                this.mProcessorPool.release(proc);
                return layout;
            } else {
                return (TextLayout) Minecraft.getInstance().m_18691_(() -> this.lookupFormattedLayout(sequence, computeFlags)).join();
            }
        } else {
            int nowFlags = 0;
            if (!(sequence instanceof FormattedTextWrapper)) {
                TextLayout layout = (TextLayout) this.mFormattedCache.get(this.mFormattedLayoutKey.update(sequence));
                if (layout != null) {
                    nowFlags = layout.mComputedFlags;
                    if ((layout.mComputedFlags & computeFlags) == computeFlags) {
                        return layout.get();
                    }
                }
                layout = this.mProcessor.createSequenceLayout(sequence, this.mResLevel, nowFlags | computeFlags);
                this.mFormattedCache.put(this.mFormattedLayoutKey.copy(), layout);
                return layout;
            } else {
                FormattedText text = ((FormattedTextWrapper) sequence).mText;
                if (text != CommonComponents.EMPTY && text != FormattedText.EMPTY) {
                    label52: {
                        label67: {
                            TextLayout layout;
                            if (sUseComponentCache && text instanceof MutableComponent component) {
                                layout = (TextLayout) this.mComponentCache.get(component);
                                if (layout == null) {
                                    break label52;
                                }
                                nowFlags = layout.mComputedFlags;
                                if ((layout.mComputedFlags & computeFlags) != computeFlags) {
                                    break label52;
                                }
                            } else {
                                layout = (TextLayout) this.mFormattedCache.get(this.mFormattedLayoutKey.update(text, Style.EMPTY));
                                if (layout == null) {
                                    break label67;
                                }
                                nowFlags = layout.mComputedFlags;
                                if ((layout.mComputedFlags & computeFlags) != computeFlags) {
                                    break label67;
                                }
                            }
                            return layout.get();
                        }
                        TextLayout var11 = this.mProcessor.createTextLayout(text, Style.EMPTY, this.mResLevel, nowFlags | computeFlags);
                        this.mFormattedCache.put(this.mFormattedLayoutKey.copy(), var11);
                        return var11;
                    }
                    TextLayout var12 = this.mProcessor.createTextLayout(text, Style.EMPTY, this.mResLevel, nowFlags | computeFlags);
                    this.mComponentCache.put(component, var12);
                    return var12;
                } else {
                    return TextLayout.EMPTY;
                }
            }
        }
    }

    @Deprecated
    public boolean handleSequence(FormattedCharSequence sequence, ReorderTextHandler.IConsumer consumer) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    public FontCollection getFontCollection(@Nonnull ResourceLocation fontName) {
        if (this.mForceUnicodeFont == Boolean.TRUE && fontName.equals(Minecraft.DEFAULT_FONT)) {
            fontName = Minecraft.UNIFORM_FONT;
        }
        FontCollection fontCollection;
        return (FontCollection) ((fontCollection = (FontCollection) this.mFontCollections.get(fontName)) != null ? fontCollection : ModernUI.getSelectedTypeface());
    }

    public void dumpBitmapFonts() {
        String basePath = Bitmap.saveDialogGet(Bitmap.SaveFormat.PNG, null, "BitmapFont");
        if (basePath != null) {
            basePath = basePath.substring(0, basePath.length() - 4);
        }
        int index = 0;
        for (FontCollection fc : this.mFontCollections.values()) {
            for (FontFamily family : fc.getFamilies()) {
                Font font = family.getClosestMatch(0);
                if (font instanceof BitmapFont) {
                    BitmapFont bmf = (BitmapFont) font;
                    if (basePath != null) {
                        bmf.dumpAtlas(basePath + "_" + index + ".png");
                        index++;
                    } else {
                        bmf.dumpAtlas(null);
                    }
                }
            }
        }
    }

    @Nullable
    public BakedGlyph lookupGlyph(Font font, int devSize, int glyphId) {
        return (BakedGlyph) (font instanceof BitmapFont bitmapFont ? bitmapFont.getGlyph(glyphId) : this.mGlyphManager.lookupGlyph(font, devSize, glyphId));
    }

    public int getEmojiTexture() {
        return this.mGlyphManager.getCurrentTexture(2);
    }

    public int getStandardTexture() {
        return this.mGlyphManager.getCurrentTexture(0);
    }

    @Deprecated
    @Nullable
    private BakedGlyph lookupEmoji(@Nonnull char[] buf, int start, int end) {
        return null;
    }

    public void onEndClientTick() {
        if (this.mTimer == 0) {
            int lifespan = sCacheLifespan;
            Predicate<TextLayout> ticker = layout -> layout.tick(lifespan);
            this.mVanillaCache.values().removeIf(ticker);
            this.mComponentCache.values().removeIf(ticker);
            this.mFormattedCache.values().removeIf(ticker);
            boolean useTextShadersEffective = sRawUseTextShadersInWorld && !ModernUIClient.areShadersEnabled();
            if (sUseTextShadersInWorld != useTextShadersEffective) {
                this.reload();
                sUseTextShadersInWorld = useTextShadersEffective;
            }
        }
        this.mTimer = (this.mTimer + 1) % 20;
    }

    public int getCacheCount() {
        return this.mVanillaCache.size() + this.mComponentCache.size() + this.mFormattedCache.size();
    }

    public int getCacheMemorySize() {
        int size = 0;
        for (TextLayout n : this.mVanillaCache.values()) {
            size += n.getMemorySize();
        }
        for (TextLayout n : this.mComponentCache.values()) {
            size += n.getMemorySize();
        }
        for (java.util.Map.Entry<FormattedLayoutKey, TextLayout> e : this.mFormattedCache.entrySet()) {
            size += ((FormattedLayoutKey) e.getKey()).getMemorySize();
            size += ((TextLayout) e.getValue()).getMemorySize();
        }
        return size;
    }

    public int getResLevel() {
        return this.mResLevel;
    }

    @Nonnull
    public TextDirectionHeuristic getTextDirectionHeuristic() {
        return this.mTextDirectionHeuristic;
    }

    @Nullable
    public TextLayoutEngine.FastCharSet lookupFastChars(@Nonnull Font font, int resLevel) {
        if (font == this.mEmojiFont) {
            return null;
        } else {
            if (font instanceof BitmapFont) {
                resLevel = 1;
            }
            return (TextLayoutEngine.FastCharSet) this.mFastCharMap.computeIfAbsent(new TextLayoutEngine.FontStrikeDesc(font, resLevel), this.mCacheFastChars);
        }
    }

    @Nullable
    private TextLayoutEngine.FastCharSet cacheFastChars(@Nonnull TextLayoutEngine.FontStrikeDesc desc) {
        java.awt.Font awtFont = null;
        BitmapFont bitmapFont = null;
        int deviceFontSize = 1;
        if (desc.font instanceof OutlineFont) {
            deviceFontSize = TextLayoutProcessor.computeFontSize((float) desc.resLevel);
            awtFont = ((OutlineFont) desc.font).chooseFont(deviceFontSize);
        } else {
            if (!(desc.font instanceof BitmapFont)) {
                return null;
            }
            bitmapFont = (BitmapFont) desc.font;
        }
        BakedGlyph[] glyphs = new BakedGlyph[189];
        float[] offsets = new float[glyphs.length];
        char[] chars = new char[1];
        int n = 0;
        for (int i = 0; i < 10; i++) {
            chars[0] = (char) (48 + i);
            float advance;
            BakedGlyph glyph;
            if (awtFont != null) {
                GlyphVector vector = this.mGlyphManager.createGlyphVector(awtFont, chars);
                if (vector.getNumGlyphs() == 0) {
                    if (i == 0) {
                        ModernUI.LOGGER.warn(MARKER, awtFont + " does not support ASCII digits");
                        return null;
                    }
                    continue;
                }
                advance = (float) vector.getGlyphPosition(1).getX() / (float) desc.resLevel;
                glyph = this.mGlyphManager.lookupGlyph(desc.font, deviceFontSize, vector.getGlyphCode(0));
                if (glyph == null) {
                    if (i == 0) {
                        ModernUI.LOGGER.warn(MARKER, awtFont + " does not support ASCII digits");
                        return null;
                    }
                    continue;
                }
            } else {
                BitmapFont.Glyph gl = bitmapFont.getGlyph(chars[0]);
                if (gl == null) {
                    if (i == 0) {
                        ModernUI.LOGGER.warn(MARKER, bitmapFont + " does not support ASCII digits");
                        return null;
                    }
                    continue;
                }
                advance = gl.advance;
                glyph = gl;
            }
            glyphs[i] = glyph;
            if (i == 0) {
                offsets[n] = advance;
            } else {
                offsets[n] = (offsets[0] - advance) / 2.0F;
            }
            n++;
        }
        char[][] ranges = new char[][] { { '!', '0' }, { ':', '\u007f' }, { '¡', 'Ā' } };
        for (char[] range : ranges) {
            char c = range[0];
            for (char e = range[1]; c < e; c++) {
                chars[0] = c;
                float advancex;
                BakedGlyph glyphx;
                if (awtFont != null) {
                    GlyphVector vectorx = this.mGlyphManager.createGlyphVector(awtFont, chars);
                    if (vectorx.getNumGlyphs() == 0) {
                        continue;
                    }
                    advancex = (float) vectorx.getGlyphPosition(1).getX() / (float) desc.resLevel;
                    if (advancex - 1.0F > offsets[0]) {
                        continue;
                    }
                    glyphx = this.mGlyphManager.lookupGlyph(desc.font, deviceFontSize, vectorx.getGlyphCode(0));
                } else {
                    BitmapFont.Glyph gl = bitmapFont.getGlyph(chars[0]);
                    if (gl == null) {
                        continue;
                    }
                    advancex = gl.advance;
                    if (advancex - 1.0F > offsets[0]) {
                        continue;
                    }
                    glyphx = gl;
                }
                if (glyphx != null) {
                    glyphs[n] = glyphx;
                    offsets[n] = (offsets[0] - advancex) / 2.0F;
                    n++;
                }
            }
        }
        if (n < glyphs.length) {
            glyphs = (BakedGlyph[]) Arrays.copyOf(glyphs, n);
            offsets = Arrays.copyOf(offsets, n);
        }
        return new TextLayoutEngine.FastCharSet(glyphs, offsets);
    }

    @Deprecated
    private void cacheDigitGlyphs() {
    }

    @Nullable
    @Deprecated
    private TextLayout generateAndCache(VanillaLayoutKey key, @Nonnull CharSequence string, @Nonnull Style style) {
        return !RenderSystem.isOnRenderThread() ? (TextLayout) Minecraft.getInstance().m_18691_(() -> this.generateAndCache(key, string, style)).join() : null;
    }

    @Nonnull
    @Deprecated
    private TextLayoutEngine.Entry getOrCacheString(@Nonnull String str) {
        RenderSystem.assertOnRenderThread();
        char[] text = str.toCharArray();
        TextLayoutEngine.Entry entry = new TextLayoutEngine.Entry();
        int length = this.extractFormattingCodes(entry, str, text);
        List<TextLayoutEngine.Glyph> glyphList = new ArrayList();
        entry.advance = this.layoutBidiString(glyphList, text, 0, length, entry.codes);
        entry.glyphs = new TextLayoutEngine.Glyph[glyphList.size()];
        entry.glyphs = (TextLayoutEngine.Glyph[]) glyphList.toArray(entry.glyphs);
        Arrays.sort(entry.glyphs);
        int colorIndex = 0;
        int shift = 0;
        for (int glyphIndex = 0; glyphIndex < entry.glyphs.length; glyphIndex++) {
            TextLayoutEngine.Glyph var10 = entry.glyphs[glyphIndex];
        }
        TextLayoutEngine.Key key = new TextLayoutEngine.Key();
        key.str = str;
        return entry;
    }

    @Deprecated
    private void layoutFont(TextLayoutProcessor data, char[] text, int start, int limit, int flag, Font font, boolean random, byte effect) {
    }

    @Deprecated
    private void layoutRandom(TextLayoutProcessor data, char[] text, int start, int limit, int flag, Font font, byte effect) {
    }

    @Deprecated
    private void insertColorState(@Nonnull TextLayoutProcessor data) {
    }

    @Deprecated
    private int extractFormattingCodes(TextLayoutEngine.Entry cacheEntry, @Nonnull String str, char[] text) {
        List<TextLayoutEngine.FormattingCode> codeList = new ArrayList();
        int start = 0;
        int shift = 0;
        byte fontStyle = 0;
        byte renderStyle = 0;
        int next;
        for (byte colorCode = -1; (next = str.indexOf(167, start)) != -1 && next + 1 < str.length(); shift += 2) {
            System.arraycopy(text, next - shift + 2, text, next - shift, text.length - next - 2);
            int code = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(str.charAt(next + 1)));
            switch(code) {
                case 16:
                    break;
                case 17:
                    fontStyle = (byte) (fontStyle | 1);
                    break;
                case 18:
                    renderStyle = (byte) (renderStyle | 2);
                    cacheEntry.needExtraRender = true;
                    break;
                case 19:
                    renderStyle = (byte) (renderStyle | 1);
                    cacheEntry.needExtraRender = true;
                    break;
                case 20:
                    fontStyle = (byte) (fontStyle | 2);
                    break;
                case 21:
                    fontStyle = 0;
                    renderStyle = 0;
                    colorCode = -1;
                    break;
                default:
                    if (code >= 0) {
                        colorCode = (byte) code;
                    }
            }
            TextLayoutEngine.FormattingCode formatting = new TextLayoutEngine.FormattingCode();
            formatting.stringIndex = next;
            formatting.stripIndex = next - shift;
            formatting.color = Color3i.fromFormattingCode(colorCode);
            formatting.fontStyle = fontStyle;
            formatting.renderEffect = renderStyle;
            codeList.add(formatting);
            start = next + 2;
        }
        cacheEntry.codes = (TextLayoutEngine.FormattingCode[]) codeList.toArray(new TextLayoutEngine.FormattingCode[0]);
        return text.length - shift;
    }

    @Deprecated
    private float layoutBidiString(List<TextLayoutEngine.Glyph> glyphList, char[] text, int start, int limit, TextLayoutEngine.FormattingCode[] codes) {
        float advance = 0.0F;
        if (!Bidi.requiresBidi(text, start, limit)) {
            return this.layoutStyle(glyphList, text, start, limit, 0, advance, codes);
        } else {
            Bidi bidi = new Bidi(text, start, null, 0, limit - start, 126);
            if (bidi.isRightToLeft()) {
                return this.layoutStyle(glyphList, text, start, limit, 0, advance, codes);
            } else {
                int runCount = bidi.getRunCount();
                byte[] levels = new byte[runCount];
                Integer[] ranges = new Integer[runCount];
                for (int index = 0; index < runCount; index++) {
                    levels[index] = (byte) bidi.getRunLevel(index);
                    ranges[index] = index;
                }
                Bidi.reorderVisually(levels, 0, ranges, 0, runCount);
                for (int visualIndex = 0; visualIndex < runCount; visualIndex++) {
                    int logicalIndex = ranges[visualIndex];
                    int layoutFlag = 0;
                    advance = this.layoutStyle(glyphList, text, start + bidi.getRunStart(logicalIndex), start + bidi.getRunLimit(logicalIndex), layoutFlag, advance, codes);
                }
                return advance;
            }
        }
    }

    @Deprecated
    private float layoutStyle(List<TextLayoutEngine.Glyph> glyphList, char[] text, int start, int limit, int layoutFlags, float advance, TextLayoutEngine.FormattingCode[] codes) {
        int currentFontStyle = 0;
        return advance;
    }

    @Deprecated
    private float layoutString(List<TextLayoutEngine.Glyph> glyphList, char[] text, int start, int limit, int layoutFlags, float advance, int style) {
        while (start < limit) {
            int next = 0;
            if (next == start) {
                next++;
            }
            start = next;
        }
        return advance;
    }

    @Deprecated
    private float layoutFont(List<TextLayoutEngine.Glyph> glyphList, char[] text, int start, int limit, int layoutFlags, float advance, Font font) {
        GlyphVector vector = null;
        TextLayoutEngine.Glyph glyph = null;
        int numGlyphs = 1;
        return advance;
    }

    @Deprecated
    private static class Entry {

        public WeakReference<TextLayoutEngine.Key> keyRef;

        public float advance;

        public TextLayoutEngine.Glyph[] glyphs;

        public TextLayoutEngine.FormattingCode[] codes;

        public boolean needExtraRender;
    }

    public static class FastCharSet extends BakedGlyph {

        public final BakedGlyph[] glyphs;

        public final float[] offsets;

        public FastCharSet(BakedGlyph[] glyphs, float[] offsets) {
            this.glyphs = glyphs;
            this.offsets = offsets;
        }
    }

    private static record FontStrikeDesc(Font font, int resLevel) {
    }

    @Deprecated
    private static class FormattingCode implements Comparable<Integer> {

        public static final byte UNDERLINE = 1;

        public static final byte STRIKETHROUGH = 2;

        public int stringIndex;

        public int stripIndex;

        public byte fontStyle;

        @Nullable
        public Color3i color;

        public byte renderEffect;

        public int compareTo(@Nonnull Integer i) {
            return Integer.compare(this.stringIndex, i);
        }
    }

    @Deprecated
    private static class Glyph implements Comparable<TextLayoutEngine.Glyph> {

        int stringIndex;

        int texture;

        int x;

        int y;

        float advance;

        public int compareTo(TextLayoutEngine.Glyph o) {
            return Integer.compare(this.stringIndex, o.stringIndex);
        }
    }

    @Deprecated
    private static class Key {

        public String str;

        public int hashCode() {
            int code = 0;
            int length = this.str.length();
            boolean colorCode = false;
            for (int index = 0; index < length; index++) {
                char c = this.str.charAt(index);
                if (c >= '0' && c <= '9' && !colorCode) {
                    c = '0';
                }
                code = code * 31 + c;
                colorCode = c == 167;
            }
            return code;
        }

        public boolean equals(Object o) {
            if (o != null && this.getClass() == o.getClass()) {
                String other = o.toString();
                int length = this.str.length();
                if (length != other.length()) {
                    return false;
                } else {
                    boolean colorCode = false;
                    for (int index = 0; index < length; index++) {
                        char c1 = this.str.charAt(index);
                        char c2 = other.charAt(index);
                        if (c1 != c2 && (c1 < '0' || c1 > '9' || c2 < '0' || c2 > '9' || colorCode)) {
                            return false;
                        }
                        colorCode = c1 == 167;
                    }
                    return true;
                }
            } else {
                return false;
            }
        }

        public String toString() {
            return this.str;
        }
    }

    private static final class LoadResults extends FontResourceManager.LoadResults {

        volatile Map<ResourceLocation, FontCollection> mFontCollections;
    }

    private static final class RawFontBundle implements DependencySorter.Entry<ResourceLocation> {

        final ResourceLocation name;

        Set<Object> families = new LinkedHashSet();

        Set<ResourceLocation> dependencies = new HashSet();

        RawFontBundle(ResourceLocation name) {
            this.name = name;
        }

        @Override
        public void visitRequiredDependencies(@Nonnull Consumer<ResourceLocation> visitor) {
            this.dependencies.forEach(visitor);
        }

        @Override
        public void visitOptionalDependencies(@Nonnull Consumer<ResourceLocation> visitor) {
        }
    }
}