package fr.lucreeper74.createmetallurgy.content.light_bulb.network;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NetworkHandler {

    final Map<NetworkHandler.Address, Network> networkList = new HashMap();

    public Network addNetwork(Level level, NetworkHandler.Address address) {
        Network network = new Network(level);
        this.networkList.put(address, network);
        return network;
    }

    public Network getNetOf(Level level, INetworkNode actor) {
        NetworkHandler.Address key = actor.getAddress();
        if (!this.networkList.containsKey(key)) {
            this.addNetwork(level, key);
        }
        return (Network) this.networkList.get(actor.getAddress());
    }

    public static class Address {

        public static final NetworkHandler.Address EMPTY = new NetworkHandler.Address(ItemStack.EMPTY);

        private static final Map<Item, NetworkHandler.Address> addresses = new IdentityHashMap();

        private ItemStack stack;

        private Item item;

        public static NetworkHandler.Address of(ItemStack stack) {
            if (stack.isEmpty()) {
                return EMPTY;
            } else {
                return !stack.hasTag() ? (NetworkHandler.Address) addresses.computeIfAbsent(stack.getItem(), $ -> new NetworkHandler.Address(stack)) : new NetworkHandler.Address(stack);
            }
        }

        private Address(ItemStack stack) {
            this.stack = stack;
            this.item = stack.getItem();
        }

        public ItemStack getStack() {
            return this.stack;
        }

        public int hashCode() {
            return this.item.hashCode();
        }
    }
}