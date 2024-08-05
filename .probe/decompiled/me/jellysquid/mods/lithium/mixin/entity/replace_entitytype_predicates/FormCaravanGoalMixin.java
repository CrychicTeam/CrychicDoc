package me.jellysquid.mods.lithium.mixin.entity.replace_entitytype_predicates;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ LlamaFollowCaravanGoal.class })
public class FormCaravanGoalMixin {

    @Redirect(method = { "canStart()Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private List<Llama> getLlamasForCaravan(Level world, Entity excluded, AABB box, Predicate<? super Entity> predicate) {
        return world.m_6443_(Llama.class, box, entity -> entity != excluded);
    }
}