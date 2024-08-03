package lio.playeranimatorapi.playeranims;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import java.util.ArrayList;
import java.util.List;
import lio.playeranimatorapi.data.PlayerAnimationData;
import lio.playeranimatorapi.modifier.AbstractCameraModifier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class CustomModifierLayer<T extends IAnimation> extends ModifierLayer implements IAnimation {

    public static final Vec3f voidVector = new Vec3f(0.0F, -1.0E7F, 0.0F);

    public List<AbstractCameraModifier> cameraModifiers = new ArrayList();

    public int modifierCount = 0;

    public boolean hasModifier;

    public boolean cameraAnimEnabled;

    public boolean important = false;

    public PlayerAnimationData data;

    public ResourceLocation currentAnim;

    public KeyframeAnimationPlayer animPlayer;

    public AbstractClientPlayer player;

    private float speed = 1.0F;

    public void setAnimationData(PlayerAnimationData data) {
        this.data = data;
        this.important = data.important();
    }

    public void setAnimPlayer(KeyframeAnimationPlayer animPlayer) {
        this.animPlayer = animPlayer;
    }

    public void setCurrentAnimationLocation(ResourceLocation animation) {
        this.currentAnim = animation;
    }

    public CustomModifierLayer(@Nullable T animation, AbstractClientPlayer player, AbstractModifier... modifiers) {
        this.hasModifier = false;
        this.player = player;
    }

    @Override
    public void tick() {
        super.tick();
    }

    public void addModifier(@NotNull AbstractModifier modifier) {
        this.addModifier(modifier, this.modifierCount);
        this.modifierCount++;
        this.hasModifier = true;
        if (modifier instanceof AbstractCameraModifier) {
            this.cameraAnimEnabled = true;
            this.cameraModifiers.add((AbstractCameraModifier) modifier);
        }
        if (modifier instanceof SpeedModifier) {
            this.speed = this.speed * ((SpeedModifier) modifier).speed;
        }
    }

    public void removeAllModifiers() {
        for (int i = this.modifierCount - 1; i >= 0; i--) {
            this.removeModifier(i);
        }
        this.modifierCount = 0;
        this.cameraAnimEnabled = false;
        this.hasModifier = false;
        this.speed = 1.0F;
        this.cameraModifiers = new ArrayList();
    }

    public void replaceAnimationWithFade(AbstractFadeModifier fadeModifier, KeyframeAnimationPlayer newAnimation) {
        this.setAnimPlayer(newAnimation);
        this.replaceAnimationWithFade(fadeModifier, (T) newAnimation, false);
    }

    public void replaceAnimation(KeyframeAnimationPlayer newAnimation) {
        this.setAnimPlayer(newAnimation);
        this.setAnimation((T) newAnimation);
        this.linkModifiers();
    }

    @NotNull
    @Override
    public Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        Vec3f transform = super.get3DTransform(modelName, type, tickDelta, value0);
        if (type == TransformType.POSITION && (modelName.equals("leftItem") && !this.data.parts().leftItem.isVisible || modelName.equals("rightItem") && !this.data.parts().rightItem.isVisible)) {
            transform = transform.add(voidVector);
        }
        return transform;
    }

    public float getSpeed() {
        return this.speed;
    }

    public CustomModifierLayer(AbstractClientPlayer player) {
        this(null, player);
    }
}