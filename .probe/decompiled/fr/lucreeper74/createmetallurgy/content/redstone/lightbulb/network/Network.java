package fr.lucreeper74.createmetallurgy.content.redstone.lightbulb.network;

import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.level.Level;

public class Network {

    public final Set<INetworkNode> nodes = new HashSet();

    final Level level;

    public Network(Level level) {
        this.level = level;
    }

    public void addNode(INetworkNode actor) {
        this.nodes.add(actor);
    }

    public void removeNode(INetworkNode actor) {
        this.nodes.remove(actor);
        if (this.nodes.isEmpty()) {
            CreateMetallurgy.NETWORK_HANDLER.networkList.remove(actor.getAddress());
        }
    }

    public void transmit(INetworkNode actor) {
        int power = 0;
        for (INetworkNode other : this.nodes) {
            if (other.isAlive() && power < 15) {
                power = Math.max(other.getTransmittedSignal(), power);
            }
        }
        for (INetworkNode node : this.nodes) {
            node.setReceivedSignal(power);
        }
    }
}