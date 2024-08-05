package software.bernie.geckolib.core.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.core.state.BoneSnapshot;

public class AnimatableManager<T extends GeoAnimatable> {

    private final Map<String, BoneSnapshot> boneSnapshotCollection = new Object2ObjectOpenHashMap();

    private final Map<String, AnimationController<T>> animationControllers;

    private Map<DataTicket<?>, Object> extraData;

    private double lastUpdateTime;

    private boolean isFirstTick = true;

    private double firstTickTime = -1.0;

    public AnimatableManager(GeoAnimatable animatable) {
        AnimatableManager.ControllerRegistrar registrar = new AnimatableManager.ControllerRegistrar();
        animatable.registerControllers(registrar);
        this.animationControllers = registrar.<T>build();
    }

    public void addController(AnimationController controller) {
        this.getAnimationControllers().put(controller.getName(), controller);
    }

    public void removeController(String name) {
        this.getAnimationControllers().remove(name);
    }

    public Map<String, AnimationController<T>> getAnimationControllers() {
        return this.animationControllers;
    }

    public Map<String, BoneSnapshot> getBoneSnapshotCollection() {
        return this.boneSnapshotCollection;
    }

    public void clearSnapshotCache() {
        this.getBoneSnapshotCollection().clear();
    }

    public double getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void updatedAt(double updateTime) {
        this.lastUpdateTime = updateTime;
    }

    public double getFirstTickTime() {
        return this.firstTickTime;
    }

    public void startedAt(double time) {
        this.firstTickTime = time;
    }

    public boolean isFirstTick() {
        return this.isFirstTick;
    }

    protected void finishFirstTick() {
        this.isFirstTick = false;
    }

    public <D> void setData(DataTicket<D> dataTicket, D data) {
        if (this.extraData == null) {
            this.extraData = new Object2ObjectOpenHashMap();
        }
        this.extraData.put(dataTicket, data);
    }

    public <D> D getData(DataTicket<D> dataTicket) {
        return this.extraData != null ? dataTicket.getData(this.extraData) : null;
    }

    public void tryTriggerAnimation(String animName) {
        for (AnimationController<?> controller : this.getAnimationControllers().values()) {
            if (controller.tryTriggerAnimation(animName)) {
                return;
            }
        }
    }

    public void tryTriggerAnimation(String controllerName, String animName) {
        AnimationController<?> controller = (AnimationController<?>) this.getAnimationControllers().get(controllerName);
        if (controller != null) {
            controller.tryTriggerAnimation(animName);
        }
    }

    public static final class ControllerRegistrar {

        private final List<AnimationController<? extends GeoAnimatable>> controllers = new ObjectArrayList(4);

        public AnimatableManager.ControllerRegistrar add(AnimationController<?>... controllers) {
            this.controllers.addAll(Arrays.asList(controllers));
            return this;
        }

        public AnimatableManager.ControllerRegistrar remove(String name) {
            this.controllers.removeIf(controller -> controller.getName().equals(name));
            return this;
        }

        private <T extends GeoAnimatable> Object2ObjectArrayMap<String, AnimationController<T>> build() {
            Object2ObjectArrayMap<String, AnimationController<?>> map = new Object2ObjectArrayMap(this.controllers.size());
            this.controllers.forEach(controller -> map.put(controller.getName(), controller));
            return (Object2ObjectArrayMap<String, AnimationController<T>>) map;
        }
    }
}