package com.simibubi.create.content.trains.entity;

import com.simibubi.create.Create;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TrainIconType {

    public static Map<ResourceLocation, TrainIconType> REGISTRY = new HashMap();

    ResourceLocation sheet;

    ResourceLocation id;

    int x;

    int y;

    public static final int ENGINE = -1;

    public static final int FLIPPED_ENGINE = -2;

    public static void register(ResourceLocation id, ResourceLocation sheet, int x, int y) {
        REGISTRY.put(id, new TrainIconType(id, sheet, x, y));
    }

    public TrainIconType(ResourceLocation id, ResourceLocation sheet, int x, int y) {
        this.id = id;
        this.sheet = sheet;
        this.x = x;
        this.y = y;
    }

    public static TrainIconType byId(ResourceLocation id) {
        return (TrainIconType) REGISTRY.getOrDefault(id, getDefault());
    }

    public static TrainIconType getDefault() {
        return (TrainIconType) REGISTRY.get(Create.asResource("traditional"));
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @OnlyIn(Dist.CLIENT)
    public int render(int lengthOrEngine, GuiGraphics graphics, int x, int y) {
        int offset = this.getIconOffset(lengthOrEngine);
        int width = this.getIconWidth(lengthOrEngine);
        graphics.blit(this.sheet, x, y, 0, (float) (this.x + offset), (float) this.y, width, 10, 256, 256);
        return width;
    }

    public int getIconWidth(int lengthOrEngine) {
        if (lengthOrEngine == -2) {
            return 19;
        } else if (lengthOrEngine == -1) {
            return 19;
        } else if (lengthOrEngine < 3) {
            return 7;
        } else {
            return lengthOrEngine < 9 ? 13 : 19;
        }
    }

    public int getIconOffset(int lengthOrEngine) {
        if (lengthOrEngine == -2) {
            return 0;
        } else if (lengthOrEngine == -1) {
            return 62;
        } else if (lengthOrEngine < 3) {
            return 34;
        } else {
            return lengthOrEngine < 9 ? 20 : 42;
        }
    }

    static {
        ResourceLocation sheet = Create.asResource("textures/gui/assemble.png");
        register(Create.asResource("traditional"), sheet, 2, 205);
        register(Create.asResource("electric"), sheet, 2, 216);
        register(Create.asResource("modern"), sheet, 2, 227);
    }
}