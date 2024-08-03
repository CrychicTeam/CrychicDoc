package de.keksuccino.konkrete.rendering.animation;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.math.MathUtils;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import org.apache.http.client.utils.URIBuilder;

public class AnimationRenderer implements IAnimationRenderer {

    private String resourceDir;

    private int fps;

    private boolean loop;

    private int width;

    private int height;

    private int x;

    private int y;

    protected List<ResourceLocation> resources = new ArrayList();

    private boolean stretch = false;

    private boolean hide = false;

    private volatile boolean done = false;

    private String modid;

    private static FileSystem jarFileSystem = null;

    private int frame = 0;

    private long prevTime = 0L;

    protected float opacity = 1.0F;

    public AnimationRenderer(String resourceDir, int fps, boolean loop, int posX, int posY, int width, int height, String modid) {
        this.fps = fps;
        this.loop = loop;
        this.x = posX;
        this.y = posY;
        this.width = width;
        this.height = height;
        this.resourceDir = resourceDir;
        this.modid = modid;
        this.loadAnimationFrames();
    }

    private void loadAnimationFrames() {
        try {
            for (String s : this.getAnimationResource(this.resourceDir, Minecraft.class)) {
                this.resources.add(ResourceLocation.of(s, "/".charAt(0)));
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    @Override
    public void render(GuiGraphics graphics) {
        if (this.resources != null && !this.resources.isEmpty()) {
            if (this.fps < 0) {
                this.fps = -1;
            }
            if (this.frame > this.resources.size() - 1) {
                if (this.loop) {
                    this.resetAnimation();
                } else {
                    this.done = true;
                    if (this.hide) {
                        return;
                    }
                    this.frame = this.resources.size() - 1;
                }
            }
            this.renderFrame(graphics);
            long time = System.currentTimeMillis();
            if (this.fps == -1) {
                this.updateFrame(time);
            } else if (this.prevTime + (long) (1000 / this.fps) <= time) {
                this.updateFrame(time);
            }
        }
    }

    private <T> List<String> getAnimationResource(String path, Class<T> c) throws URISyntaxException, IllegalArgumentException {
        List<String> ltemp = new ArrayList();
        List<String> l = new ArrayList();
        List<String> paths = new ArrayList();
        try {
            Field f = ModLoader.class.getDeclaredField("loadingModList");
            f.setAccessible(true);
            LoadingModList lml = (LoadingModList) f.get(ModLoader.get());
            File f1 = lml.getModFileById(this.modid).getFile().getFilePath().toFile();
            boolean isIDE = true;
            if (f1.getPath().endsWith(".jar")) {
                isIDE = false;
            }
            if (!isIDE) {
                if (jarFileSystem == null) {
                    URI uri = new URIBuilder(f1.toURI()).setScheme("jar:file").build();
                    jarFileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                }
                Path path2 = jarFileSystem.getPath("/assets/" + path + "/");
                Stream<Path> pathStream = Files.walk(path2, 1, new FileVisitOption[0]);
                pathStream.forEach(p -> paths.add(p.getFileName().toString()));
                pathStream.close();
            } else {
                URL url = c.getResource("/assets/" + path + "/");
                if (url == null) {
                    throw new IllegalArgumentException("Resource URL cannot be null!");
                }
                File f2 = new File(url.toURI());
                if (f2 == null || !f2.exists()) {
                    throw new IllegalArgumentException("Resource path don't exists!");
                }
                paths.addAll(Arrays.asList(f2.list()));
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }
        for (String s : paths) {
            if (s.toLowerCase().endsWith(".jpg") || s.toLowerCase().endsWith(".jpeg") || s.toLowerCase().endsWith(".png")) {
                ltemp.add(s);
            }
        }
        final CharacterFilter charFilter = CharacterFilter.getIntegerCharacterFiler();
        List<String> nonNumberNames = new ArrayList();
        List<String> numberNames = new ArrayList();
        for (String sx : ltemp) {
            String name = com.google.common.io.Files.getNameWithoutExtension(sx);
            if (name != null) {
                String digit = charFilter.filterForAllowedChars(name);
                if (!digit.equals("")) {
                    numberNames.add(sx);
                } else {
                    nonNumberNames.add(sx);
                }
            }
        }
        Collections.sort(nonNumberNames, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(numberNames, new Comparator<String>() {

            public int compare(String o1, String o2) {
                String s1 = com.google.common.io.Files.getNameWithoutExtension(o1);
                String s2 = com.google.common.io.Files.getNameWithoutExtension(o2);
                if (s1 != null && s2 != null) {
                    String n1 = charFilter.filterForAllowedChars(s1);
                    String n2 = charFilter.filterForAllowedChars(s2);
                    if (MathUtils.isInteger(n1) && MathUtils.isInteger(n2)) {
                        int i1 = Integer.parseInt(n1);
                        int i2 = Integer.parseInt(n2);
                        if (i1 > i2) {
                            return 1;
                        }
                        if (i1 < i2) {
                            return -1;
                        }
                        return 0;
                    }
                }
                return 0;
            }
        });
        for (String sxx : nonNumberNames) {
            l.add(path + "/" + sxx);
        }
        for (String sxx : numberNames) {
            l.add(path + "/" + sxx);
        }
        return l;
    }

    protected void renderFrame(GuiGraphics graphics) {
        int h = this.height;
        int w = this.width;
        int x2 = this.x;
        int y2 = this.y;
        if (this.stretch) {
            h = Minecraft.getInstance().screen.height;
            w = Minecraft.getInstance().screen.width;
            x2 = 0;
            y2 = 0;
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.opacity);
        graphics.blit((ResourceLocation) this.resources.get(this.frame), x2, y2, 0.0F, 0.0F, w, h, w, h);
        RenderSystem.disableBlend();
    }

    @Override
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    private void updateFrame(long time) {
        this.frame++;
        this.prevTime = time;
    }

    @Override
    public void resetAnimation() {
        this.frame = 0;
        this.prevTime = 0L;
        this.done = false;
    }

    @Override
    public void setStretchImageToScreensize(boolean b) {
        this.stretch = b;
    }

    @Override
    public void setHideAfterLastFrame(boolean b) {
        this.hide = b;
    }

    @Override
    public boolean isFinished() {
        return this.done;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int currentFrame() {
        return this.frame;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setPosX(int x) {
        this.x = x;
    }

    @Override
    public void setPosY(int y) {
        this.y = y;
    }

    @Override
    public int animationFrames() {
        return this.resources.size();
    }

    @Override
    public String getPath() {
        return this.resourceDir;
    }

    @Override
    public void setFPS(int fps) {
        this.fps = fps;
    }

    @Override
    public int getFPS() {
        return this.fps;
    }

    @Override
    public void setLooped(boolean b) {
        this.loop = b;
    }

    @Override
    public void prepareAnimation() {
    }

    @Override
    public boolean isGettingLooped() {
        return this.loop;
    }

    @Override
    public boolean isStretchedToStreensize() {
        return this.stretch;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getPosX() {
        return this.x;
    }

    @Override
    public int getPosY() {
        return this.y;
    }
}