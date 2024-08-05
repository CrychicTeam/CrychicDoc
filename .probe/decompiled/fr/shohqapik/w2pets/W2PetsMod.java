package fr.shohqapik.w2pets;

import net.blay09.mods.waystones.api.WaystoneTeleportEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("w2pets")
public class W2PetsMod {

    public static final String MODID = "w2pets";

    public W2PetsMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWaystoneTeleport(WaystoneTeleportEvent.Pre event) {
        Entity teleportedEntity = event.getContext().getEntity();
        if (teleportedEntity instanceof Player player && !teleportedEntity.level().isClientSide()) {
            Level level = player.m_9236_();
            for (Entity entity : level.m_45933_(player, player.m_20191_().inflate(30.0, 10.0, 30.0))) {
                if (entity instanceof TamableAnimal) {
                    TamableAnimal tamableAnimal = (TamableAnimal) entity;
                    if (tamableAnimal.getOwnerUUID() != null && tamableAnimal.m_269323_() != null && !tamableAnimal.isInSittingPose() && tamableAnimal.getOwnerUUID().equals(player.m_20148_())) {
                        event.getContext().addAdditionalEntity(entity);
                    }
                }
            }
        }
    }
}