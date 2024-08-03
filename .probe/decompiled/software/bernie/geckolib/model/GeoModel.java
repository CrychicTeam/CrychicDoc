package software.bernie.geckolib.model;

import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus.Internal;
import software.bernie.geckolib.GeckoLibException;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoModel;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationProcessor;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.util.RenderUtils;

public abstract class GeoModel<T extends GeoAnimatable> implements CoreGeoModel<T> {

    private final AnimationProcessor<T> processor = new AnimationProcessor<>(this);

    private BakedGeoModel currentModel = null;

    private double animTime;

    private double lastGameTickTime;

    private long lastRenderedInstance = -1L;

    public abstract ResourceLocation getModelResource(T var1);

    public abstract ResourceLocation getTextureResource(T var1);

    public abstract ResourceLocation getAnimationResource(T var1);

    public boolean crashIfBoneMissing() {
        return false;
    }

    public RenderType getRenderType(T animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(texture);
    }

    public final BakedGeoModel getBakedGeoModel(String location) {
        return this.getBakedModel(new ResourceLocation(location));
    }

    public BakedGeoModel getBakedModel(ResourceLocation location) {
        BakedGeoModel model = (BakedGeoModel) GeckoLibCache.getBakedModels().get(location);
        if (model == null) {
            throw new GeckoLibException(location, "Unable to find model");
        } else {
            if (model != this.currentModel) {
                this.processor.setActiveModel(model);
                this.currentModel = model;
            }
            return this.currentModel;
        }
    }

    @Override
    public Optional<GeoBone> getBone(String name) {
        return Optional.ofNullable((GeoBone) this.getAnimationProcessor().getBone(name));
    }

    @Override
    public Animation getAnimation(T animatable, String name) {
        ResourceLocation location = this.getAnimationResource(animatable);
        BakedAnimations bakedAnimations = (BakedAnimations) GeckoLibCache.getBakedAnimations().get(location);
        if (bakedAnimations == null) {
            throw new GeckoLibException(location, "Unable to find animation.");
        } else {
            return bakedAnimations.getAnimation(name);
        }
    }

    @Override
    public AnimationProcessor<T> getAnimationProcessor() {
        return this.processor;
    }

    public void addAdditionalStateData(T animatable, long instanceId, BiConsumer<DataTicket<T>, T> dataConsumer) {
    }

    @Internal
    @Override
    public void handleAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        Minecraft mc = Minecraft.getInstance();
        AnimatableManager<T> animatableManager = animatable.getAnimatableInstanceCache().getManagerForId(instanceId);
        Double currentTick = animationState.getData(DataTickets.TICK);
        if (currentTick == null) {
            currentTick = animatable instanceof Entity entity ? (double) entity.tickCount : RenderUtils.getCurrentTick();
        }
        if (animatableManager.getFirstTickTime() == -1.0) {
            animatableManager.startedAt(currentTick + (double) mc.getFrameTime());
        }
        double currentFrameTime = animatable instanceof Entity ? currentTick + (double) mc.getFrameTime() : currentTick - animatableManager.getFirstTickTime();
        boolean isReRender = !animatableManager.isFirstTick() && currentFrameTime == animatableManager.getLastUpdateTime();
        if (!isReRender || instanceId != this.lastRenderedInstance) {
            if (!mc.isPaused() || animatable.shouldPlayAnimsWhileGamePaused()) {
                animatableManager.updatedAt(currentFrameTime);
                double lastUpdateTime = animatableManager.getLastUpdateTime();
                this.animTime = this.animTime + (lastUpdateTime - this.lastGameTickTime);
                this.lastGameTickTime = lastUpdateTime;
            }
            animationState.animationTick = this.animTime;
            this.lastRenderedInstance = instanceId;
            AnimationProcessor<T> processor = this.getAnimationProcessor();
            processor.preAnimationSetup(animationState.getAnimatable(), this.animTime);
            if (!processor.getRegisteredBones().isEmpty()) {
                processor.tickAnimation(animatable, this, animatableManager, this.animTime, animationState, this.crashIfBoneMissing());
            }
            this.setCustomAnimations(animatable, instanceId, animationState);
        }
    }

    @Override
    public void applyMolangQueries(T animatable, double animTime) {
        MolangParser parser = MolangParser.INSTANCE;
        Minecraft mc = Minecraft.getInstance();
        parser.setMemoizedValue("query.life_time", () -> animTime / 20.0);
        parser.setMemoizedValue("query.actor_count", mc.level::m_104813_);
        parser.setMemoizedValue("query.time_of_day", () -> (double) ((float) mc.level.m_46468_() / 24000.0F));
        parser.setMemoizedValue("query.moon_phase", mc.level::m_46941_);
        if (animatable instanceof Entity entity) {
            parser.setMemoizedValue("query.distance_from_camera", () -> mc.gameRenderer.getMainCamera().getPosition().distanceTo(entity.position()));
            parser.setMemoizedValue("query.is_on_ground", () -> (double) RenderUtils.booleanToFloat(entity.onGround()));
            parser.setMemoizedValue("query.is_in_water", () -> (double) RenderUtils.booleanToFloat(entity.isInWater()));
            parser.setMemoizedValue("query.is_in_water_or_rain", () -> (double) RenderUtils.booleanToFloat(entity.isInWaterRainOrBubble()));
            if (entity instanceof LivingEntity livingEntity) {
                parser.setMemoizedValue("query.health", livingEntity::m_21223_);
                parser.setMemoizedValue("query.max_health", livingEntity::m_21233_);
                parser.setMemoizedValue("query.is_on_fire", () -> (double) RenderUtils.booleanToFloat(livingEntity.m_6060_()));
                parser.setMemoizedValue("query.ground_speed", () -> {
                    Vec3 velocity = livingEntity.m_20184_();
                    return (double) Mth.sqrt((float) (velocity.x * velocity.x + velocity.z * velocity.z));
                });
                parser.setMemoizedValue("query.yaw_speed", () -> (double) livingEntity.getViewYRot((float) animTime - livingEntity.getViewYRot((float) animTime - 0.1F)));
            }
        }
    }
}