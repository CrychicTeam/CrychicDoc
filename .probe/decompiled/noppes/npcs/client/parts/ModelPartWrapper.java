package noppes.npcs.client.parts;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import noppes.npcs.shared.client.model.NopModelPart;
import noppes.npcs.shared.common.util.NopVector3f;

public class ModelPartWrapper {

    protected ModelPart mcPart = null;

    protected NopModelPart mpmPart = null;

    public final NopVector3f oriPos;

    public final NopVector3f oriRot;

    public Map<Integer, AnimationContainer> animations = new HashMap();

    public ModelPartWrapper(ModelPart mcPart, NopVector3f oriPos, NopVector3f oriRot) {
        this.mcPart = mcPart;
        this.oriRot = oriRot;
        this.oriPos = oriPos;
    }

    public ModelPartWrapper(NopModelPart mpmPart, NopVector3f oriPos, NopVector3f oriRot) {
        this.mpmPart = mpmPart;
        this.oriRot = oriRot;
        this.oriPos = oriPos;
    }

    public NopVector3f getPos() {
        return this.mcPart != null ? new NopVector3f(this.mcPart.x, this.mcPart.y, this.mcPart.z) : new NopVector3f(this.mpmPart.x, this.mpmPart.y, this.mpmPart.z);
    }

    public void setPos(NopVector3f pos) {
        if (this.mcPart != null) {
            this.mcPart.setPos(pos.x, pos.y, pos.z);
        } else {
            this.mpmPart.setPos(pos.x, pos.y, pos.z);
        }
    }

    public NopVector3f getRot() {
        return this.mcPart != null ? new NopVector3f(this.mcPart.xRot, this.mcPart.yRot, this.mcPart.zRot) : new NopVector3f(this.mpmPart.xRot, this.mpmPart.yRot, this.mpmPart.zRot);
    }

    public void setRot(NopVector3f rot) {
        if (this.mcPart != null) {
            this.mcPart.setRotation(rot.x, rot.y, rot.z);
        } else {
            this.mpmPart.setRotation(rot);
        }
    }

    public void setVisible(boolean b) {
        if (this.mcPart != null) {
            this.mcPart.visible = b;
        } else {
            this.mpmPart.visible = b;
        }
    }
}