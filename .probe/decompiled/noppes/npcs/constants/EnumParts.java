package noppes.npcs.constants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.client.parts.MpmPartData;
import noppes.npcs.shared.SharedReferences;
import noppes.npcs.shared.common.util.ColorUtil;

public enum EnumParts {

    HEAD("head"),
    BODY("body"),
    PARTICLES("particles"),
    ARM_LEFT("armleft"),
    ARM_RIGHT("armright"),
    LEG_LEFT("legleft"),
    LEG_RIGHT("legright"),
    EYES("eyes");

    public String name;

    public int patterns = 1;

    private EnumParts(String name) {
        this.name = name;
    }

    public static EnumParts FromName(String name) {
        for (EnumParts e : values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }

    public static MpmPartData convertOldPart(CompoundTag compound) {
        MpmPartData part = new MpmPartData();
        part.color = ColorUtil.colorToRgb(compound.getInt("Color"));
        part.usePlayerSkin = compound.getBoolean("PlayerTexture");
        String name = compound.getString("PartName");
        byte type = compound.getByte("Type");
        byte pattern = compound.getByte("Pattern");
        if (name.equals("beard")) {
            if (type == 0) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/beard.json");
            }
            if (type == 1) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/beard_braided.json");
            }
            if (type == 2) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/beard_long.json");
            }
            if (type == 3) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/beard_small.json");
            }
        }
        if (name.equals("breasts")) {
            part.partId = new ResourceLocation(SharedReferences.modid(), type == 0 ? "parts/body/breasts.json" : "parts/body/breasts" + (type + 1) + ".json");
        }
        if (name.equals("claws")) {
            part.partId = new ResourceLocation(SharedReferences.modid(), "parts/arms/claws.json");
            if (pattern > 0) {
                part.partId = new ResourceLocation(SharedReferences.modid(), pattern == 1 ? "parts/arms/claws_left.json" : "parts/arms/claws_right.json");
            }
        }
        if (name.equals("ears")) {
            part.partId = new ResourceLocation(SharedReferences.modid(), type == 1 ? "parts/head/ears_bunny.json" : "parts/head/ears_mouse.json");
        }
        if (name.equals("fin")) {
            part.partId = new ResourceLocation(SharedReferences.modid(), type == 1 ? "parts/body/fin.json" : "parts/body/fin2.json");
        }
        if (name.equals("hair")) {
            if (type == 0) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/hair_long.json");
            }
            if (type == 1) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/hair_thin.json");
            }
            if (type == 2) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/hair_styled.json");
            }
            if (type == 3) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/hair_ponytail.json");
            }
        }
        if (name.equals("horns")) {
            if (type == 0) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/horns/horns_bull.json");
            }
            if (type == 1) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/horns/horns_antlers.json");
            }
            if (type == 2) {
                part.partId = new ResourceLocation(SharedReferences.modid(), pattern == 0 ? "parts/horns/horns_antenna_backwards.json" : "parts/horns/horns_antenna_forwards.json");
            }
        }
        if (name.equals("legs")) {
            if (type == 0) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/legs/legs_naga.json");
            }
            if (type == 1) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/legs/legs_spider.json");
            }
            if (type == 2) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/legs/legs_horse.json");
            }
            if (type == 3) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/legs/legs_mermaid.json");
            }
            if (type == 4) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/legs/legs_digitigrade.json");
            }
            if (type == 5) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/legs/legs_none.json");
            }
        }
        if (name.equals("mohawk")) {
            part.partId = new ResourceLocation(SharedReferences.modid(), type == 1 ? "parts/head/mohawk.json" : "parts/head/mohawk2.json");
        }
        if (name.equals("skirt")) {
            part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/skirt.json");
        }
        if (name.equals("snout")) {
            if (type == 0) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/snout_small.json");
            }
            if (type == 1) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/snout_medium.json");
            }
            if (type == 2) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/snout_large.json");
            }
            if (type == 3) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/snout_bunny.json");
            }
            if (type == 4) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/head/snout_beak.json");
            }
        }
        if (name.equals("tail")) {
            if (type == 0) {
                part.partId = new ResourceLocation(SharedReferences.modid(), pattern == 0 ? "parts/body/tail.json" : "parts/body/tail_two.json");
            }
            if (type == 1) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/tail_dragon.json");
            }
            if (type == 2) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/tail_horse.json");
            }
            if (type == 3) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/tail_squirrel.json");
            }
            if (type == 4) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/tail_fin.json");
            }
            if (type == 5) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/tail_rodent.json");
            }
            if (type == 6) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/tail_bird.json");
            }
            if (type == 7) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/tail_fox.json");
            }
        }
        if (name.equals("wings")) {
            if (type == 0) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/wings_angel.json");
            }
            if (type == 1) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/wings_reptile.json");
            }
            if (type == 2) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/wings_reaper.json");
            }
            if (type == 3) {
                part.partId = new ResourceLocation(SharedReferences.modid(), "parts/body/wings_fairy.json");
            }
        }
        return part;
    }
}