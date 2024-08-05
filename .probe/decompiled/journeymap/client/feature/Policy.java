package journeymap.client.feature;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;

public class Policy {

    static Minecraft mc = Minecraft.getInstance();

    final Feature feature;

    final boolean allowInSingleplayer;

    final boolean allowInMultiplayer;

    public Policy(Feature feature, boolean allowInSingleplayer, boolean allowInMultiplayer) {
        this.feature = feature;
        this.allowInSingleplayer = allowInSingleplayer;
        this.allowInMultiplayer = allowInMultiplayer;
    }

    public static Set<Policy> bulkCreate(boolean allowInSingleplayer, boolean allowInMultiplayer) {
        return bulkCreate(Feature.all(), allowInSingleplayer, allowInMultiplayer);
    }

    public static Set<Policy> bulkCreate(EnumSet<Feature> features, boolean allowInSingleplayer, boolean allowInMultiplayer) {
        Set<Policy> policies = new HashSet();
        for (Feature feature : features) {
            policies.add(new Policy(feature, allowInSingleplayer, allowInMultiplayer));
        }
        return policies;
    }

    public boolean isCurrentlyAllowed() {
        if (this.allowInSingleplayer == this.allowInMultiplayer) {
            return this.allowInSingleplayer;
        } else {
            IntegratedServer server = mc.getSingleplayerServer();
            boolean isSinglePlayer = server != null && !server.isPublished();
            return this.allowInSingleplayer && isSinglePlayer ? true : this.allowInMultiplayer && !isSinglePlayer;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Policy policy = (Policy) o;
            if (this.allowInMultiplayer != policy.allowInMultiplayer) {
                return false;
            } else {
                return this.allowInSingleplayer != policy.allowInSingleplayer ? false : this.feature == policy.feature;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.feature.hashCode();
        result = 31 * result + (this.allowInSingleplayer ? 1 : 0);
        return 31 * result + (this.allowInMultiplayer ? 1 : 0);
    }
}