package yesman.epicfight.api.animation.types;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.TransformSheet;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.client.animation.property.JointMask;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.api.client.model.ItemSkin;
import yesman.epicfight.api.client.model.ItemSkins;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.model.JsonModelLoader;
import yesman.epicfight.api.utils.TypeFlexibleHashMap;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class StaticAnimation extends DynamicAnimation {

    protected final Map<AnimationProperty<?>, Object> properties = Maps.newHashMap();

    protected final StateSpectrum.Blueprint stateSpectrumBlueprint = new StateSpectrum.Blueprint();

    protected final ResourceLocation resourceLocation;

    protected final ResourceLocation registryName;

    protected final Armature armature;

    protected final int namespaceId;

    protected final int animationId;

    private final StateSpectrum stateSpectrum = new StateSpectrum();

    public StaticAnimation() {
        super(0.0F, false);
        this.namespaceId = -1;
        this.animationId = -1;
        this.resourceLocation = null;
        this.registryName = null;
        this.armature = null;
    }

    public StaticAnimation(boolean repeatPlay, String path, Armature armature) {
        this(0.15F, repeatPlay, path, armature);
    }

    public StaticAnimation(float convertTime, boolean isRepeat, String path, Armature armature) {
        super(convertTime, isRepeat);
        AnimationManager animationManager = EpicFightMod.getInstance().animationManager;
        this.namespaceId = animationManager.getNamespaceHash();
        this.animationId = animationManager.getIdCounter();
        EpicFightMod.LOGGER.info("Assigned animation id " + this.animationId + " to " + path);
        int colon = path.indexOf(58);
        String modid = colon == -1 ? animationManager.getModid() : path.substring(0, colon);
        String folderPath = colon == -1 ? path : path.substring(colon + 1);
        animationManager.getIdMap().put(this.animationId, this);
        this.resourceLocation = new ResourceLocation(modid, "animmodels/animations/" + folderPath);
        this.registryName = new ResourceLocation(modid, folderPath);
        animationManager.getNameMap().put(new ResourceLocation(animationManager.getModid(), folderPath), this);
        this.armature = armature;
    }

    public StaticAnimation(float convertTime, boolean repeatPlay, String path, Armature armature, boolean notRegisteredInAnimationManager) {
        super(convertTime, repeatPlay);
        AnimationManager animationManager = EpicFightMod.getInstance().animationManager;
        this.namespaceId = animationManager.getModid().hashCode();
        this.animationId = -1;
        int colon = path.indexOf(58);
        String modid = colon == -1 ? animationManager.getModid() : path.substring(0, colon);
        String folderPath = colon == -1 ? path : path.substring(colon + 1);
        this.resourceLocation = new ResourceLocation(modid, "animmodels/animations/" + folderPath);
        this.registryName = new ResourceLocation(modid, folderPath);
        this.armature = armature;
    }

    public StaticAnimation(ResourceLocation baseAnimPath, float convertTime, boolean repeatPlay, String path, Armature armature, boolean notRegisteredInAnimationManager) {
        super(convertTime, repeatPlay);
        this.namespaceId = baseAnimPath.getNamespace().hashCode();
        this.animationId = -1;
        this.resourceLocation = new ResourceLocation(baseAnimPath.getNamespace(), "animmodels/animations/" + path);
        this.registryName = new ResourceLocation(baseAnimPath.getNamespace(), path);
        this.armature = armature;
    }

    public static void load(ResourceManager resourceManager, ResourceLocation rl, StaticAnimation animation) {
        new JsonModelLoader(resourceManager, rl).loadStaticAnimation(animation);
    }

    public static void load(ResourceManager resourceManager, StaticAnimation animation) {
        ResourceLocation path = new ResourceLocation(animation.resourceLocation.getNamespace(), animation.resourceLocation.getPath() + ".json");
        new JsonModelLoader(resourceManager, path).loadStaticAnimation(animation);
    }

    public static void loadBothSide(ResourceManager resourceManager, StaticAnimation animation) {
        ResourceLocation path = new ResourceLocation(animation.resourceLocation.getNamespace(), animation.resourceLocation.getPath() + ".json");
        new JsonModelLoader(resourceManager, path).loadStaticAnimationBothSide(animation);
    }

    public void loadAnimation(ResourceManager resourceManager) {
        try {
            int id = Integer.parseInt(this.resourceLocation.getPath().substring(22));
            StaticAnimation animation = EpicFightMod.getInstance().animationManager.findAnimationById(this.namespaceId, id);
            ResourceLocation path = new ResourceLocation(animation.resourceLocation.getNamespace(), animation.resourceLocation.getPath() + ".json");
            load(resourceManager, path, this);
            this.jointTransforms = animation.jointTransforms;
        } catch (NumberFormatException var5) {
            load(resourceManager, this);
        } catch (Exception var6) {
            EpicFightMod.LOGGER.warn("Failed to load animation: " + this.resourceLocation);
            var6.printStackTrace();
        }
        this.onLoaded();
    }

    protected void onLoaded() {
        this.stateSpectrum.readFrom(this.stateSpectrumBlueprint);
    }

    @Override
    public void begin(LivingEntityPatch<?> entitypatch) {
        this.getProperty(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS).ifPresent(events -> {
            for (AnimationEvent event : events) {
                event.executeIfRightSide(entitypatch, this);
            }
        });
        if (entitypatch.isLogicalClient()) {
            this.getProperty(ClientAnimationProperties.TRAIL_EFFECT).ifPresent(trailInfos -> {
                int idx = 0;
                for (TrailInfo trailInfo : trailInfos) {
                    double eid = Double.longBitsToDouble((long) entitypatch.getOriginal().m_19879_());
                    double modid = Double.longBitsToDouble((long) this.namespaceId);
                    double animid = Double.longBitsToDouble((long) this.animationId);
                    double jointId = Double.longBitsToDouble((long) this.armature.searchJointByName(trailInfo.joint).getId());
                    double index = Double.longBitsToDouble((long) (idx++));
                    if (trailInfo.hand != null) {
                        ItemStack stack = entitypatch.getOriginal().getItemInHand(trailInfo.hand);
                        ItemSkin itemSkin = ItemSkins.getItemSkin(stack.getItem());
                        if (itemSkin != null) {
                            trailInfo = itemSkin.trailInfo.overwrite(trailInfo);
                        }
                    }
                    if (trailInfo.particle != null) {
                        entitypatch.getOriginal().m_9236_().addParticle(trailInfo.particle, eid, modid, animid, jointId, index, 0.0);
                    }
                }
            });
        }
    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, DynamicAnimation nextAnimation, boolean isEnd) {
        this.getProperty(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS).ifPresent(events -> {
            for (AnimationEvent event : events) {
                event.executeIfRightSide(entitypatch, this);
            }
        });
    }

    @Override
    public void tick(LivingEntityPatch<?> entitypatch) {
        this.getProperty(AnimationProperty.StaticAnimationProperty.EVENTS).ifPresent(events -> {
            for (AnimationEvent event : events) {
                event.executeIfRightSide(entitypatch, this);
            }
        });
        this.getProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS).ifPresent(events -> {
            AnimationPlayer player = entitypatch.<Animator>getAnimator().getPlayerFor(this);
            if (player != null) {
                float prevElapsed = player.getPrevElapsedTime();
                float elapsed = player.getElapsedTime();
                for (AnimationEvent.TimeStampedEvent event : events) {
                    event.executeIfRightSide(entitypatch, this, prevElapsed, elapsed);
                }
            }
        });
        this.getProperty(AnimationProperty.StaticAnimationProperty.TIME_PERIOD_EVENTS).ifPresent(events -> {
            AnimationPlayer player = entitypatch.<Animator>getAnimator().getPlayerFor(this);
            if (player != null) {
                float prevElapsed = player.getPrevElapsedTime();
                float elapsed = player.getElapsedTime();
                for (AnimationEvent.TimePeriodEvent event : events) {
                    event.executeIfRightSide(entitypatch, this, prevElapsed, elapsed);
                }
            }
        });
    }

    @Override
    public EntityState getState(LivingEntityPatch<?> entitypatch, float time) {
        return new EntityState(this.getStatesMap(entitypatch, time));
    }

    @Override
    public TypeFlexibleHashMap<EntityState.StateFactor<?>> getStatesMap(LivingEntityPatch<?> entitypatch, float time) {
        return this.stateSpectrum.getStateMap(entitypatch, time);
    }

    @Override
    public <T> T getState(EntityState.StateFactor<T> stateFactor, LivingEntityPatch<?> entitypatch, float time) {
        return this.stateSpectrum.getSingleState(stateFactor, entitypatch, time);
    }

    @Override
    public boolean isJointEnabled(LivingEntityPatch<?> entitypatch, Layer.Priority layer, String joint) {
        return !super.isJointEnabled(entitypatch, layer, joint) ? false : (Boolean) this.getProperty(ClientAnimationProperties.JOINT_MASK).map(bindModifier -> !bindModifier.isMasked(entitypatch.getCurrentLivingMotion(), joint)).orElse(true);
    }

    @Override
    public JointMask.BindModifier getBindModifier(LivingEntityPatch<?> entitypatch, Layer.Priority layer, String joint) {
        return (JointMask.BindModifier) this.getProperty(ClientAnimationProperties.JOINT_MASK).map(jointMaskEntry -> {
            List<JointMask> list = jointMaskEntry.getMask(entitypatch.getCurrentLivingMotion());
            int position = list.indexOf(JointMask.of(joint));
            return position >= 0 ? ((JointMask) list.get(position)).getBindModifier() : null;
        }).orElse(null);
    }

    @Override
    public void modifyPose(DynamicAnimation animation, Pose pose, LivingEntityPatch<?> entitypatch, float time, float partialTicks) {
        AnimationProperty.PoseModifier modifier = (AnimationProperty.PoseModifier) this.getProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER).orElse(null);
        if (modifier != null) {
            modifier.modify(animation, pose, entitypatch, time, partialTicks);
        }
    }

    @Override
    public boolean isStaticAnimation() {
        return true;
    }

    @Override
    public int getNamespaceId() {
        return this.namespaceId;
    }

    @Override
    public int getId() {
        return this.animationId;
    }

    public boolean equals(Object obj) {
        return !(obj instanceof StaticAnimation staticAnimation) ? super.equals(obj) : this.getNamespaceId() == staticAnimation.getNamespaceId() && this.getId() == staticAnimation.getId();
    }

    public boolean between(StaticAnimation a1, StaticAnimation a2) {
        return a1.getNamespaceId() != a2.getNamespaceId() ? false : a1.getId() <= this.getId() && a2.getId() >= this.getId();
    }

    public boolean in(StaticAnimation[] animations) {
        for (StaticAnimation animation : animations) {
            if (this.equals(animation)) {
                return true;
            }
        }
        return false;
    }

    public ResourceLocation getLocation() {
        return this.resourceLocation;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }

    public Armature getArmature() {
        return this.armature;
    }

    @Override
    public float getPlaySpeed(LivingEntityPatch<?> entitypatch) {
        return 1.0F;
    }

    @Override
    public TransformSheet getCoord() {
        return (TransformSheet) this.getProperty(AnimationProperty.ActionAnimationProperty.COORD).orElse(super.getCoord());
    }

    public String toString() {
        String classPath = this.getClass().toString();
        return classPath.substring(classPath.lastIndexOf(".") + 1) + " " + this.getLocation();
    }

    public <V> StaticAnimation addProperty(AnimationProperty.StaticAnimationProperty<V> propertyType, V value) {
        this.properties.put(propertyType, value);
        return this;
    }

    public StaticAnimation addEvents(AnimationProperty.StaticAnimationProperty<?> key, AnimationEvent... events) {
        this.properties.put(key, events);
        return this;
    }

    public <V extends AnimationEvent> StaticAnimation addEvents(AnimationEvent.TimeStampedEvent... events) {
        this.properties.put(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, events);
        return this;
    }

    public <V extends AnimationEvent> StaticAnimation addEvents(AnimationEvent.TimePeriodEvent... events) {
        this.properties.put(AnimationProperty.StaticAnimationProperty.TIME_PERIOD_EVENTS, events);
        return this;
    }

    @Override
    public <V> Optional<V> getProperty(AnimationProperty<V> propertyType) {
        return Optional.ofNullable(this.properties.get(propertyType));
    }

    @OnlyIn(Dist.CLIENT)
    public Layer.Priority getPriority() {
        return (Layer.Priority) this.getProperty(ClientAnimationProperties.PRIORITY).orElse(Layer.Priority.LOWEST);
    }

    @OnlyIn(Dist.CLIENT)
    public Layer.LayerType getLayerType() {
        return (Layer.LayerType) this.getProperty(ClientAnimationProperties.LAYER_TYPE).orElse(Layer.LayerType.BASE_LAYER);
    }

    public StaticAnimation newTimePair(float start, float end) {
        this.stateSpectrumBlueprint.newTimePair(start, end);
        return this;
    }

    public StaticAnimation newConditionalTimePair(Function<LivingEntityPatch<?>, Integer> condition, float start, float end) {
        this.stateSpectrumBlueprint.newConditionalTimePair(condition, start, end);
        return this;
    }

    public <T> StaticAnimation addState(EntityState.StateFactor<T> factor, T val) {
        this.stateSpectrumBlueprint.addState(factor, val);
        return this;
    }

    public <T> StaticAnimation removeState(EntityState.StateFactor<T> factor) {
        this.stateSpectrumBlueprint.removeState(factor);
        return this;
    }

    public <T> StaticAnimation addConditionalState(int metadata, EntityState.StateFactor<T> factor, T val) {
        this.stateSpectrumBlueprint.addConditionalState(metadata, factor, val);
        return this;
    }

    public <T> StaticAnimation addStateRemoveOld(EntityState.StateFactor<T> factor, T val) {
        this.stateSpectrumBlueprint.addStateRemoveOld(factor, val);
        return this;
    }

    public <T> StaticAnimation addStateIfNotExist(EntityState.StateFactor<T> factor, T val) {
        this.stateSpectrumBlueprint.addStateIfNotExist(factor, val);
        return this;
    }
}