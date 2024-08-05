package dev.ftb.mods.ftbquests.quest;

import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ImageIcon;
import dev.ftb.mods.ftblibrary.math.PixelBuffer;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class QuestShape extends Icon {

    private static final Map<String, QuestShape> MAP = new LinkedHashMap();

    private static QuestShape defaultShape;

    public static NameMap<String> idMap;

    public static NameMap<String> idMapWithDefault;

    private final String id;

    private final ImageIcon background;

    private final ImageIcon outline;

    private final ImageIcon shape;

    private PixelBuffer shapePixels;

    public QuestShape(String id) {
        this.id = id;
        this.background = new ImageIcon(new ResourceLocation("ftbquests", "textures/shapes/" + this.id + "/background.png"));
        this.outline = new ImageIcon(new ResourceLocation("ftbquests", "textures/shapes/" + this.id + "/outline.png"));
        this.shape = new ImageIcon(new ResourceLocation("ftbquests", "textures/shapes/" + this.id + "/shape.png"));
    }

    public static void reload(List<String> list) {
        MAP.clear();
        list.forEach(s -> MAP.put(s, new QuestShape(s)));
        defaultShape = (QuestShape) MAP.values().iterator().next();
        idMap = NameMap.of((String) list.get(0), list).baseNameKey("ftbquests.quest.shape").create();
        list.add(0, "default");
        idMapWithDefault = NameMap.of((String) list.get(0), list).baseNameKey("ftbquests.quest.shape").create();
    }

    public static QuestShape get(String id) {
        return (QuestShape) MAP.getOrDefault(id, defaultShape);
    }

    public String toString() {
        return "quest_shape:" + this.id;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        this.background.draw(graphics, x, y, w, h);
        this.outline.draw(graphics, x, y, w, h);
    }

    public ImageIcon getBackground() {
        return this.background;
    }

    public ImageIcon getOutline() {
        return this.outline;
    }

    public ImageIcon getShape() {
        return this.shape;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    public PixelBuffer getShapePixels() {
        if (this.shapePixels == null) {
            try {
                ResourceLocation shapeLoc = new ResourceLocation("ftbquests", "textures/shapes/" + this.id + "/shape.png");
                Resource resource = (Resource) Minecraft.getInstance().getResourceManager().m_213713_(shapeLoc).get();
                InputStream stream = resource.open();
                try {
                    this.shapePixels = PixelBuffer.from(stream);
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
            } catch (Exception var8) {
                this.shapePixels = new PixelBuffer(1, 1);
                this.shapePixels.setRGB(0, 0, -1);
            }
        }
        return this.shapePixels;
    }

    public static Map<String, QuestShape> map() {
        return Collections.unmodifiableMap(MAP);
    }
}