package noppes.npcs;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class ModelPartData {

    private static Map<String, ResourceLocation> resources = new HashMap();

    public int color = 16777215;

    public int colorPattern = 16777215;

    public byte type = 0;

    public byte pattern = 0;

    public boolean playerTexture = false;

    public String name;

    private ResourceLocation location;

    public ModelPartData(String name) {
        this.name = name;
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putByte("Type", this.type);
        compound.putInt("Color", this.color);
        compound.putBoolean("PlayerTexture", this.playerTexture);
        compound.putByte("Pattern", this.pattern);
        return compound;
    }

    public void load(CompoundTag compound) {
        if (!compound.contains("Type")) {
            this.type = -1;
        } else {
            this.type = compound.getByte("Type");
            this.color = compound.getInt("Color");
            this.playerTexture = compound.getBoolean("PlayerTexture");
            this.pattern = compound.getByte("Pattern");
            this.location = null;
        }
    }

    public ResourceLocation getResource() {
        if (this.location != null) {
            return this.location;
        } else {
            String texture = this.name + "/" + this.type;
            if ((this.location = (ResourceLocation) resources.get(texture)) != null) {
                return this.location;
            } else {
                this.location = new ResourceLocation("moreplayermodels:textures/" + texture + ".png");
                resources.put(texture, this.location);
                return this.location;
            }
        }
    }

    public void setType(int type) {
        this.type = (byte) type;
        this.location = null;
    }

    public String toString() {
        return "Color: " + this.color + " Type: " + this.type;
    }

    public String getColor() {
        String str = Integer.toHexString(this.color);
        while (str.length() < 6) {
            str = "0" + str;
        }
        return str;
    }
}