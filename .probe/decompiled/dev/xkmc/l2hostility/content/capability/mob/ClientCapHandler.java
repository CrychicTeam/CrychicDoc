package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ClientCapHandler {

    public static void handle(MobCapSyncToClient packet) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            if (level.getEntity(packet.id) instanceof LivingEntity le) {
                if (MobTraitCap.HOLDER.isProper(le)) {
                    TagCodec.fromTag(packet.tag, MobTraitCap.class, (MobTraitCap) MobTraitCap.HOLDER.get(le), SerialField::toClient);
                }
            }
        }
    }
}