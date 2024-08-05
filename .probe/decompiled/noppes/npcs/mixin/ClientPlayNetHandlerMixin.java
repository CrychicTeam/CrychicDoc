package noppes.npcs.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import noppes.npcs.CustomEntities;
import noppes.npcs.entity.EntityProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ClientPacketListener.class })
public class ClientPlayNetHandlerMixin {

    @Inject(at = { @At("TAIL") }, method = { "handleAddEntity" })
    private void handleAddEntity(ClientboundAddEntityPacket packet, CallbackInfo ci) {
        Entity entity = null;
        ClientLevel level = Minecraft.getInstance().level;
        if (packet.getType() == CustomEntities.entityProjectile) {
            entity = new EntityProjectile(CustomEntities.entityProjectile, level);
            Entity entity2 = level.getEntity(packet.getData());
            if (entity2 != null) {
                ((EntityProjectile) entity).m_5602_(entity2);
            }
        }
        if (entity != null) {
            int i = packet.getId();
            entity.setPos(packet.getX(), packet.getY(), packet.getZ());
            entity.moveTo(packet.getX(), packet.getY(), packet.getZ());
            entity.setXRot(packet.getXRot() * 360.0F / 256.0F);
            entity.setYRot(packet.getYRot() * 360.0F / 256.0F);
            entity.setId(i);
            entity.setUUID(packet.getUUID());
            Minecraft.getInstance().level.putNonPlayerEntity(i, entity);
        }
    }
}