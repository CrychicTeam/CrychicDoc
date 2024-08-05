package org.violetmoon.quark.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.quark.content.automation.module.FeedingTroughModule;

@Mixin({ TemptingSensor.class })
public class TemptingSensorMixin {

    @ModifyExpressionValue(method = { "doTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/PathfinderMob;)V" }, at = { @At(value = "INVOKE", target = "Ljava/util/stream/Stream;collect(Ljava/util/stream/Collector;)Ljava/lang/Object;") })
    public Object quark$findTroughs(Object playersErased, ServerLevel level, PathfinderMob mob) {
        List<Player> players = (List<Player>) playersErased;
        if (mob instanceof Animal animal) {
            Player first = players.isEmpty() ? null : (Player) players.get(0);
            if (first == null) {
                Player replacement = FeedingTroughModule.modifyTemptingSensor((TemptingSensor) this, animal, level);
                if (replacement != null) {
                    players.add(replacement);
                }
            }
        }
        return players;
    }
}