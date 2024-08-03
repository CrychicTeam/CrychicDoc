package noppes.npcs.client.parts;

import com.google.gson.JsonObject;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.constants.BodyPart;
import noppes.npcs.shared.common.util.NopVector3f;

public class MpmPart {

    public boolean isEnabled;

    public ResourceLocation id;

    public ResourceLocation parentId;

    public String name;

    public ResourceLocation texture;

    public String menu;

    public PartRenderType renderType;

    public PartBehaviorType animationType;

    public BodyPart bodyPart;

    public List<BodyPart> hiddenParts;

    public NopVector3f translate = NopVector3f.ZERO;

    public NopVector3f scale = NopVector3f.ZERO;

    public NopVector3f rotatePoint = NopVector3f.ZERO;

    public NopVector3f rotate = NopVector3f.ZERO;

    public int previewRotation = 45;

    public boolean disableCustomTextures;

    public boolean defaultUsePlayerSkins;

    public String author;

    public MpmPartAnimation animationData = new MpmPartAnimation();

    public void load(JsonObject renderData) {
    }

    public ModelPartWrapper getPart(String name) {
        return null;
    }
}