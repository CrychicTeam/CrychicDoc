package com.mna.entities.models;

import com.mna.tools.math.Vector3;
import java.util.HashMap;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelPositionData {

    private HashMap<ModelPart, ModelPositionData.VectorPair> positionData = new HashMap();

    public ModelPositionData addPositionDegrees(ModelPart renderer, Vector3 startRotation, Vector3 endRotation) {
        startRotation = startRotation.scale((float) (-Math.PI / 180.0));
        endRotation = endRotation.scale((float) (-Math.PI / 180.0));
        this.positionData.put(renderer, new ModelPositionData.VectorPair(startRotation, endRotation));
        return this;
    }

    public ModelPositionData addPositionDegrees(ModelPart renderer, Vector3 endRotation) {
        Vector3 startRotation = new Vector3((double) renderer.xRot, (double) renderer.yRot, (double) renderer.zRot);
        endRotation = endRotation.scale((float) (-Math.PI / 180.0));
        this.positionData.put(renderer, new ModelPositionData.VectorPair(startRotation, endRotation));
        return this;
    }

    public ModelPositionData addPositionDegrees(ModelPart renderer) {
        Vector3 startRotation = new Vector3((double) renderer.xRot, (double) renderer.yRot, (double) renderer.zRot);
        this.positionData.put(renderer, new ModelPositionData.VectorPair(startRotation, startRotation));
        return this;
    }

    public void startLerp() {
        this.positionData.forEach((renderer, pair) -> pair.setStart(new Vector3((double) renderer.xRot, (double) renderer.yRot, (double) renderer.zRot)));
    }

    public void lerpRotations(float t) {
        this.positionData.forEach((renderer, pair) -> {
            Vector3 interp = pair.lerp(t);
            renderer.xRot = interp.x;
            renderer.yRot = interp.y;
            renderer.zRot = interp.z;
        });
    }

    private class VectorPair {

        private Vector3 start;

        private final Vector3 end;

        public VectorPair(Vector3 start, Vector3 end) {
            this.start = start;
            this.end = end;
        }

        public void setStart(Vector3 start) {
            this.start = start;
        }

        public Vector3 lerp(float t) {
            return Vector3.lerp(this.start, this.end, t);
        }
    }
}