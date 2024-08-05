package software.bernie.geckolib.core.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Objects;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.core.object.PlayState;

public class AnimationState<T extends GeoAnimatable> {

    private final T animatable;

    private final float limbSwing;

    private final float limbSwingAmount;

    private final float partialTick;

    private final boolean isMoving;

    private final Map<DataTicket<?>, Object> extraData = new Object2ObjectOpenHashMap();

    protected AnimationController<T> controller;

    public double animationTick;

    public AnimationState(T animatable, float limbSwing, float limbSwingAmount, float partialTick, boolean isMoving) {
        this.animatable = animatable;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.partialTick = partialTick;
        this.isMoving = isMoving;
    }

    public double getAnimationTick() {
        return this.animationTick;
    }

    public T getAnimatable() {
        return this.animatable;
    }

    public float getLimbSwing() {
        return this.limbSwing;
    }

    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }

    public float getPartialTick() {
        return this.partialTick;
    }

    public boolean isMoving() {
        return this.isMoving;
    }

    public AnimationController<T> getController() {
        return this.controller;
    }

    public AnimationState<T> withController(AnimationController<T> controller) {
        this.controller = controller;
        return this;
    }

    public Map<DataTicket<?>, ?> getExtraData() {
        return this.extraData;
    }

    public <D> D getData(DataTicket<D> dataTicket) {
        return dataTicket.getData(this.extraData);
    }

    public <D> void setData(DataTicket<D> dataTicket, D data) {
        this.extraData.put(dataTicket, data);
    }

    public void setAnimation(RawAnimation animation) {
        this.getController().setAnimation(animation);
    }

    public PlayState setAndContinue(RawAnimation animation) {
        this.getController().setAnimation(animation);
        return PlayState.CONTINUE;
    }

    public boolean isCurrentAnimation(RawAnimation animation) {
        return Objects.equals(this.getController().currentRawAnimation, animation);
    }

    public boolean isCurrentAnimationStage(String name) {
        return this.getController().getCurrentAnimation() != null && this.getController().getCurrentAnimation().animation().name().equals(name);
    }

    public void resetCurrentAnimation() {
        this.getController().forceAnimationReset();
    }

    public void setControllerSpeed(float speed) {
        this.getController().setAnimationSpeed((double) speed);
    }
}