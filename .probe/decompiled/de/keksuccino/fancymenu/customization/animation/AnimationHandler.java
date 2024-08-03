package de.keksuccino.fancymenu.customization.animation;

import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.customization.animation.exceptions.AnimationNotFoundException;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.file.FileUtils;
import de.keksuccino.fancymenu.util.properties.PropertiesParser;
import de.keksuccino.fancymenu.util.properties.PropertyContainer;
import de.keksuccino.fancymenu.util.properties.PropertyContainerSet;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.rendering.animation.IAnimationRenderer;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnimationHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final File ANIMATIONS_DIR = FileUtils.createDirectory(new File(FancyMenu.MOD_DIR, "/animations"));

    private static final Map<String, AnimationData> ANIMATIONS = new HashMap();

    private static final List<String> EXTERNAL_ANIMATION_NAMES = new ArrayList();

    protected static boolean preloadCompleted = false;

    protected static boolean initialized = false;

    public static void init() {
        if (!initialized) {
            EventHandler.INSTANCE.registerListenersOf(new AnimationHandlerEvents());
        }
        initialized = true;
    }

    public static void register(@NotNull IAnimationRenderer animation, @NotNull String name, @NotNull AnimationData.Type type) {
        if (!ANIMATIONS.containsKey(name)) {
            ANIMATIONS.put(name, new AnimationData(animation, name, type));
            if (type == AnimationData.Type.EXTERNAL) {
                EXTERNAL_ANIMATION_NAMES.add(name);
            }
        } else {
            LOGGER.error("[FANCYMENU] Failed to register animation! Animation with same name already exists: " + name);
        }
    }

    public static void unregister(@NotNull IAnimationRenderer animation) {
        AnimationData d = null;
        for (AnimationData a : ANIMATIONS.values()) {
            if (a.animation == animation) {
                d = a;
                break;
            }
        }
        if (d != null) {
            unregister(d.name);
        }
    }

    public static void unregister(@NotNull String name) {
        if (animationExists(name)) {
            ANIMATIONS.remove(name);
            EXTERNAL_ANIMATION_NAMES.remove(name);
        }
    }

    public static void discoverAndRegisterExternalAnimations() {
        preloadCompleted = false;
        clearExternalAnimations();
        File[] filesArray = ANIMATIONS_DIR.listFiles();
        if (filesArray != null) {
            for (File a : filesArray) {
                String mainAudio = null;
                String introAudio = null;
                int fps = 0;
                boolean loop = true;
                boolean replayIntro = false;
                List<String> frameNamesMain = new ArrayList();
                List<String> frameNamesIntro = new ArrayList();
                if (a.isDirectory()) {
                    File p = new File(a.getAbsolutePath().replace("\\", "/") + "/animation.properties");
                    if (p.exists()) {
                        PropertyContainerSet props = PropertiesParser.deserializeSetFromFile(p.getPath());
                        if (props != null) {
                            List<PropertyContainer> metas = props.getContainersOfType("animation-meta");
                            if (!metas.isEmpty()) {
                                PropertyContainer m = (PropertyContainer) metas.get(0);
                                String name = m.getValue("name");
                                if (name != null) {
                                    String fpsString = m.getValue("fps");
                                    if (fpsString != null && MathUtils.isInteger(fpsString)) {
                                        fps = Integer.parseInt(fpsString);
                                    }
                                    String loopString = m.getValue("loop");
                                    if (loopString != null && loopString.equalsIgnoreCase("false")) {
                                        loop = false;
                                    }
                                    String replayString = m.getValue("replayintro");
                                    if (replayString != null && replayString.equalsIgnoreCase("true")) {
                                        replayIntro = true;
                                    }
                                    String resourceNamespace = m.getValue("namespace");
                                    if (resourceNamespace != null) {
                                        List<PropertyContainer> mainFrameSecs = props.getContainersOfType("frames-main");
                                        if (!mainFrameSecs.isEmpty()) {
                                            PropertyContainer mainFrames = (PropertyContainer) mainFrameSecs.get(0);
                                            Map<String, String> mainFramesMap = mainFrames.getProperties();
                                            List<String> mainFrameKeys = new ArrayList();
                                            for (Entry<String, String> me : mainFramesMap.entrySet()) {
                                                if (((String) me.getKey()).startsWith("frame_")) {
                                                    String frameNumber = ((String) me.getKey()).split("_", 2)[1];
                                                    if (MathUtils.isInteger(frameNumber)) {
                                                        mainFrameKeys.add((String) me.getKey());
                                                    }
                                                }
                                            }
                                            mainFrameKeys.sort((o1, o2) -> {
                                                String n1 = o1.split("_", 2)[1];
                                                String n2 = o2.split("_", 2)[1];
                                                int i1 = Integer.parseInt(n1);
                                                int i2 = Integer.parseInt(n2);
                                                return Integer.compare(i1, i2);
                                            });
                                            for (String s : mainFrameKeys) {
                                                frameNamesMain.add("frames_main/" + (String) mainFramesMap.get(s));
                                            }
                                            List<PropertyContainer> introFrameSecs = props.getContainersOfType("frames-intro");
                                            if (!introFrameSecs.isEmpty()) {
                                                PropertyContainer introFrames = (PropertyContainer) introFrameSecs.get(0);
                                                Map<String, String> introFramesMap = introFrames.getProperties();
                                                List<String> introFrameKeys = new ArrayList();
                                                for (Entry<String, String> mex : introFramesMap.entrySet()) {
                                                    if (((String) mex.getKey()).startsWith("frame_")) {
                                                        String frameNumber = ((String) mex.getKey()).split("_", 2)[1];
                                                        if (MathUtils.isInteger(frameNumber)) {
                                                            introFrameKeys.add((String) mex.getKey());
                                                        }
                                                    }
                                                }
                                                introFrameKeys.sort((o1, o2) -> {
                                                    String n1 = o1.split("_", 2)[1];
                                                    String n2 = o2.split("_", 2)[1];
                                                    int i1 = Integer.parseInt(n1);
                                                    int i2 = Integer.parseInt(n2);
                                                    return Integer.compare(i1, i2);
                                                });
                                                for (String s : introFrameKeys) {
                                                    frameNamesIntro.add("frames_intro/" + (String) introFramesMap.get(s));
                                                }
                                            }
                                            File audio1 = new File(a.getAbsolutePath().replace("\\", "/") + "/audio/mainaudio.wav");
                                            if (audio1.exists()) {
                                                mainAudio = audio1.getPath();
                                            }
                                            File audio2 = new File(a.getAbsolutePath().replace("\\", "/") + "/audio/introaudio.wav");
                                            if (audio2.exists()) {
                                                introAudio = audio2.getPath();
                                            }
                                            IAnimationRenderer in = null;
                                            IAnimationRenderer an = null;
                                            if (!frameNamesIntro.isEmpty() && !frameNamesMain.isEmpty()) {
                                                in = new ResourcePackAnimationRenderer(resourceNamespace, frameNamesIntro, fps, loop, 0, 0, 100, 100);
                                                an = new ResourcePackAnimationRenderer(resourceNamespace, frameNamesMain, fps, loop, 0, 0, 100, 100);
                                            } else if (!frameNamesMain.isEmpty()) {
                                                an = new ResourcePackAnimationRenderer(resourceNamespace, frameNamesMain, fps, loop, 0, 0, 100, 100);
                                            }
                                            try {
                                                if (in != null) {
                                                    AdvancedAnimation ani = new AdvancedAnimation(in, an, introAudio, mainAudio, replayIntro);
                                                    ani.propertiesPath = a.getPath();
                                                    register(ani, name, AnimationData.Type.EXTERNAL);
                                                    ani.prepareAnimation();
                                                    LOGGER.info("[FANCYMENU] Animation found: " + name);
                                                } else if (an != null) {
                                                    AdvancedAnimation ani = new AdvancedAnimation(null, an, introAudio, mainAudio, false);
                                                    ani.propertiesPath = a.getPath();
                                                    register(ani, name, AnimationData.Type.EXTERNAL);
                                                    ani.prepareAnimation();
                                                    LOGGER.info("[FANCYMENU] Animation found:  " + name);
                                                } else {
                                                    LOGGER.error("[FANCYMENU] Failed to register animation: " + name);
                                                }
                                            } catch (AnimationNotFoundException var32) {
                                                var32.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @NotNull
    public static List<String> getExternalAnimationNames() {
        return new ArrayList(EXTERNAL_ANIMATION_NAMES);
    }

    private static void clearExternalAnimations() {
        for (String s : EXTERNAL_ANIMATION_NAMES) {
            ANIMATIONS.remove(s);
        }
    }

    public static boolean animationExists(@NotNull String name) {
        return ANIMATIONS.containsKey(name);
    }

    @NotNull
    public static List<IAnimationRenderer> getAnimations() {
        List<IAnimationRenderer> renderers = new ArrayList();
        for (Entry<String, AnimationData> m : ANIMATIONS.entrySet()) {
            renderers.add(((AnimationData) m.getValue()).animation);
        }
        return renderers;
    }

    @Nullable
    public static IAnimationRenderer getAnimation(String name) {
        return animationExists(name) ? ((AnimationData) ANIMATIONS.get(name)).animation : null;
    }

    public static void resetAnimations() {
        for (AnimationData d : ANIMATIONS.values()) {
            d.animation.resetAnimation();
        }
    }

    public static void resetAnimationSounds() {
        for (AnimationData d : ANIMATIONS.values()) {
            if (d.animation instanceof AdvancedAnimation) {
                ((AdvancedAnimation) d.animation).resetAudio();
            }
        }
    }

    public static void stopAnimationSounds() {
        for (AnimationData d : ANIMATIONS.values()) {
            if (d.animation instanceof AdvancedAnimation) {
                ((AdvancedAnimation) d.animation).stopAudio();
            }
        }
    }

    public static void updateAnimationSizes() {
        for (IAnimationRenderer a : getAnimations()) {
            if (a instanceof ResourcePackAnimationRenderer) {
                ((ResourcePackAnimationRenderer) a).setupAnimationSize();
            } else if (a instanceof AdvancedAnimation) {
                IAnimationRenderer main = ((AdvancedAnimation) a).getMainAnimationRenderer();
                if (main instanceof ResourcePackAnimationRenderer) {
                    ((ResourcePackAnimationRenderer) main).setupAnimationSize();
                }
                IAnimationRenderer intro = ((AdvancedAnimation) a).getIntroAnimationRenderer();
                if (intro instanceof ResourcePackAnimationRenderer) {
                    ((ResourcePackAnimationRenderer) intro).setupAnimationSize();
                }
            }
        }
    }

    public static void preloadAnimations(boolean ignoreAlreadyPreloaded) {
        boolean errors = false;
        if (!preloadCompleted || ignoreAlreadyPreloaded) {
            LOGGER.info("[FANCYMENU] Preloading animations! This could cause the loading screen to freeze for a while..");
            try {
                List<ResourcePackAnimationRenderer> l = new ArrayList();
                for (IAnimationRenderer r : getAnimations()) {
                    if (r instanceof AdvancedAnimation) {
                        IAnimationRenderer main = ((AdvancedAnimation) r).getMainAnimationRenderer();
                        IAnimationRenderer intro = ((AdvancedAnimation) r).getIntroAnimationRenderer();
                        if (main instanceof ResourcePackAnimationRenderer) {
                            l.add((ResourcePackAnimationRenderer) main);
                        }
                        if (intro instanceof ResourcePackAnimationRenderer) {
                            l.add((ResourcePackAnimationRenderer) intro);
                        }
                    } else if (r instanceof ResourcePackAnimationRenderer) {
                        l.add((ResourcePackAnimationRenderer) r);
                    }
                }
                for (ResourcePackAnimationRenderer rx : l) {
                    for (ResourceLocation rl : rx.getAnimationFrames()) {
                        TextureManager t = Minecraft.getInstance().getTextureManager();
                        t.getTexture(rl);
                    }
                }
            } catch (Exception var8) {
                var8.printStackTrace();
                errors = true;
            }
            if (!errors) {
                LOGGER.info("[FANCYMENU] Finished preloading animations!");
            } else {
                LOGGER.warn("[FANCYMENU] Finished preloading animations with errors! Check your animations!");
            }
            preloadCompleted = true;
        }
    }

    public static boolean preloadingCompleted() {
        return preloadCompleted;
    }
}