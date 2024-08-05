package dev.kosmx.playerAnim.minecraftApi;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface PlayerAnimationFactory {

    PlayerAnimationFactory.FactoryHolder ANIMATION_DATA_FACTORY = new PlayerAnimationFactory.FactoryHolder();

    @Nullable
    IAnimation invoke(@NotNull AbstractClientPlayer var1);

    public static class FactoryHolder {

        private static final List<Function<AbstractClientPlayer, PlayerAnimationFactory.FactoryHolder.DataHolder>> factories = new ArrayList();

        private FactoryHolder() {
        }

        public void registerFactory(@Nullable ResourceLocation id, int priority, @NotNull PlayerAnimationFactory factory) {
            factories.add((Function) player -> (PlayerAnimationFactory.FactoryHolder.DataHolder) Optional.ofNullable(factory.invoke(player)).map(animation -> new PlayerAnimationFactory.FactoryHolder.DataHolder(id, priority, animation)).orElse(null));
        }

        @Internal
        public void prepareAnimations(AbstractClientPlayer player, AnimationStack playerStack, Map<ResourceLocation, IAnimation> animationMap) {
            for (Function<AbstractClientPlayer, PlayerAnimationFactory.FactoryHolder.DataHolder> factory : factories) {
                PlayerAnimationFactory.FactoryHolder.DataHolder dataHolder = (PlayerAnimationFactory.FactoryHolder.DataHolder) factory.apply(player);
                if (dataHolder != null) {
                    playerStack.addAnimLayer(dataHolder.priority(), dataHolder.animation());
                    if (dataHolder.id() != null) {
                        animationMap.put(dataHolder.id(), dataHolder.animation());
                    }
                }
            }
        }

        @Internal
        private static record DataHolder(@Nullable ResourceLocation id, int priority, @NotNull IAnimation animation) {
        }
    }
}