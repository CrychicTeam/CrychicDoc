package dev.kosmx.playerAnim.api.layered;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.core.util.Vec3f;
import org.jetbrains.annotations.NotNull;

public interface IAnimation {

    default void tick() {
    }

    boolean isActive();

    @NotNull
    Vec3f get3DTransform(@NotNull String var1, @NotNull TransformType var2, float var3, @NotNull Vec3f var4);

    void setupAnim(float var1);

    @NotNull
    default FirstPersonMode getFirstPersonMode(float tickDelta) {
        return FirstPersonMode.NONE;
    }

    @NotNull
    default FirstPersonConfiguration getFirstPersonConfiguration(float tickDelta) {
        return new FirstPersonConfiguration();
    }
}