package noobanidus.mods.lootr.network.client;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;
import noobanidus.mods.lootr.network.CloseCart;
import noobanidus.mods.lootr.network.OpenCart;
import noobanidus.mods.lootr.network.UpdateModelData;

public class ClientHandlers {

    public static void handleUpdateModel(UpdateModelData message, Supplier<NetworkEvent.Context> context) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            LootrAPI.LOG.info("Unable to update model data for location '" + message.pos + "' as level is null.");
        } else {
            Player player = Minecraft.getInstance().player;
            if (player == null) {
                LootrAPI.LOG.info("Unable to update model data for location '" + message.pos + "' as player is null.");
            } else {
                SectionPos pos = SectionPos.of(message.pos);
                Minecraft.getInstance().levelRenderer.setSectionDirty(pos.x(), pos.y(), pos.z());
            }
        }
    }

    public static void handleOpenCart(OpenCart message, Supplier<NetworkEvent.Context> context) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            LootrAPI.LOG.info("Unable to mark entity with id '" + message.entityId + "' as opened as level is null.");
        } else {
            Entity cart = level.getEntity(message.entityId);
            if (cart == null) {
                LootrAPI.LOG.info("Unable to mark entity with id '" + message.entityId + "' as opened as entity is null.");
            } else if (!(cart instanceof LootrChestMinecartEntity)) {
                LootrAPI.LOG.info("Unable to mark entity with id '" + message.entityId + "' as opened as entity is not a Lootr minecart.");
            } else {
                ((LootrChestMinecartEntity) cart).setOpened();
            }
        }
    }

    public static void handleCloseCart(CloseCart message, Supplier<NetworkEvent.Context> context) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            LootrAPI.LOG.info("Unable to mark entity with id '" + message.entityId + "' as closed as level is null.");
        } else {
            Entity cart = level.getEntity(message.entityId);
            if (cart == null) {
                LootrAPI.LOG.info("Unable to mark entity with id '" + message.entityId + "' as closed as entity is null.");
            } else if (!(cart instanceof LootrChestMinecartEntity)) {
                LootrAPI.LOG.info("Unable to mark entity with id '" + message.entityId + "' as closed as entity is not a Lootr minecart.");
            } else {
                ((LootrChestMinecartEntity) cart).setClosed();
            }
        }
    }
}