package yesman.epicfight.api.client.animation.property;

import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.Layer;

@OnlyIn(Dist.CLIENT)
public class ClientAnimationProperties {

    public static final AnimationProperty.StaticAnimationProperty<Layer.LayerType> LAYER_TYPE = new AnimationProperty.StaticAnimationProperty<>();

    public static final AnimationProperty.StaticAnimationProperty<Layer.Priority> PRIORITY = new AnimationProperty.StaticAnimationProperty<>();

    public static final AnimationProperty.StaticAnimationProperty<JointMaskEntry> JOINT_MASK = new AnimationProperty.StaticAnimationProperty<>();

    public static final AnimationProperty.StaticAnimationProperty<List<TrailInfo>> TRAIL_EFFECT = new AnimationProperty.StaticAnimationProperty<>();

    public static final AnimationProperty.StaticAnimationProperty<StaticAnimation> MULTILAYER_ANIMATION = new AnimationProperty.StaticAnimationProperty<>();
}