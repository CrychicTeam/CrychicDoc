package me.steinborn.krypton.mixin.shared.network.microopt;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ServerEntity.class })
public class EntityTrackerEntryMixin {

    @Redirect(method = { "<init>" }, at = @At(value = "INVOKE", target = "Ljava/util/Collections;emptyList()Ljava/util/List;"))
    public List<Entity> construct$initialPassengersListIsGuavaImmutableList() {
        return ImmutableList.of();
    }
}