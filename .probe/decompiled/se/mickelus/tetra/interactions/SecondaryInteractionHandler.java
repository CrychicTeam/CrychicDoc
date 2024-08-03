package se.mickelus.tetra.interactions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import se.mickelus.tetra.TetraMod;

public class SecondaryInteractionHandler {

    private static Map<String, SecondaryInteraction> interactions = new HashMap();

    public static void registerInteraction(SecondaryInteraction interaction) {
        interactions.put(interaction.getKey(), interaction);
    }

    public static SecondaryInteraction getInteraction(String key) {
        return (SecondaryInteraction) interactions.get(key);
    }

    public static Collection<SecondaryInteraction> getInteractions() {
        return interactions.values();
    }

    public static SecondaryInteraction findRelevantAction(Player player, BlockPos pos, Entity target) {
        return (SecondaryInteraction) interactions.values().stream().filter(interaction -> interaction.canPerform(player, player.m_9236_(), pos, target)).findFirst().orElse(null);
    }

    public static void dispatchInteraction(SecondaryInteraction interaction, Player player, BlockPos pos, Entity target) {
        if (interaction.getPerformSide().runClient()) {
            interaction.perform(player, player.m_9236_(), pos, target);
        }
        if (interaction.getPerformSide().runServer()) {
            TetraMod.packetHandler.sendToServer(new SecondaryInteractionPacket(interaction.getKey(), pos, target));
        }
    }
}