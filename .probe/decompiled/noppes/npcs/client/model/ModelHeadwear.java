package noppes.npcs.client.model;

import net.minecraft.resources.ResourceLocation;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.shared.client.model.Model2DRenderer;
import noppes.npcs.shared.client.model.NopModelPart;

public class ModelHeadwear extends ModelScaleRenderer {

    public ModelHeadwear() {
        super(null, EnumParts.HEAD);
        ResourceLocation location = new ResourceLocation("");
        Model2DRenderer right = new Model2DRenderer(64, 64, 32, 8, 8, 8, location);
        right.setPos(-4.641F, 0.8F, 4.64F);
        right.setScale(0.58F);
        right.setThickness(0.65F);
        this.setRotation(right, 0.0F, (float) (Math.PI / 2), 0.0F);
        this.addChild(right);
        Model2DRenderer left = new Model2DRenderer(64, 64, 48, 8, 8, 8, location);
        left.setPos(4.639F, 0.8F, -4.64F);
        left.setScale(0.58F);
        left.setThickness(0.65F);
        this.setRotation(left, 0.0F, (float) (-Math.PI / 2), 0.0F);
        this.addChild(left);
        Model2DRenderer front = new Model2DRenderer(64, 64, 40, 8, 8, 8, location);
        front.setPos(-4.64F, 0.801F, -4.641F);
        front.setScale(0.58F);
        front.setThickness(0.65F);
        this.setRotation(front, 0.0F, 0.0F, 0.0F);
        this.addChild(front);
        Model2DRenderer back = new Model2DRenderer(64, 64, 56, 8, 8, 8, location);
        back.setPos(4.64F, 0.801F, 4.639F);
        back.setScale(0.58F);
        back.setThickness(0.65F);
        this.setRotation(back, 0.0F, (float) Math.PI, 0.0F);
        this.addChild(back);
        Model2DRenderer top = new Model2DRenderer(64, 64, 40, 0, 8, 8, location);
        top.setPos(-4.64F, -8.5F, -4.64F);
        top.setScale(0.5799F);
        top.setThickness(0.65F);
        this.setRotation(top, (float) (-Math.PI / 2), 0.0F, 0.0F);
        this.addChild(top);
        Model2DRenderer bottom = new Model2DRenderer(64, 64, 48, 0, 8, 8, location);
        bottom.setPos(-4.64F, 0.0F, -4.64F);
        bottom.setScale(0.5799F);
        bottom.setThickness(0.65F);
        this.setRotation(bottom, (float) (-Math.PI / 2), 0.0F, 0.0F);
        this.addChild(bottom);
    }

    @Override
    public void setRotation(NopModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}