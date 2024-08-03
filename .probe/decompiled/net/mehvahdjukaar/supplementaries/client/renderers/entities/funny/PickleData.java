package net.mehvahdjukaar.supplementaries.client.renderers.entities.funny;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.PicklePacket;
import net.mehvahdjukaar.supplementaries.common.utils.Credits;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PickleData {

    private static final UUID ME = UUID.fromString("898b3a39-e486-405c-a873-d6b472dc3ba2");

    protected static final Map<UUID, PickleData.PickleValues> PICKLE_PLAYERS = new HashMap();

    private static final PickleData.PickleValues DEF;

    public static void onPlayerLogOff() {
        for (PickleData.PickleValues val : PICKLE_PLAYERS.values()) {
            val.reset();
        }
    }

    public static void onPlayerLogin(Player player) {
        for (Entry<UUID, PickleData.PickleValues> e : PICKLE_PLAYERS.entrySet()) {
            boolean on = ((PickleData.PickleValues) e.getValue()).isOn();
            UUID id = (UUID) e.getKey();
            if (on) {
                ModNetwork.CHANNEL.sendToClientPlayer((ServerPlayer) player, new PicklePacket(id, on, ((PickleData.PickleValues) e.getValue()).isJar));
            }
        }
    }

    public static boolean isDev(UUID id, boolean isJar) {
        return PICKLE_PLAYERS.containsKey(id);
    }

    public static void set(UUID id, boolean on, boolean isJar) {
        ((PickleData.PickleValues) PICKLE_PLAYERS.getOrDefault(id, DEF)).toggle(on, isJar);
    }

    public static boolean isActiveAndTick(UUID id, PlayerRenderer renderer) {
        return ((PickleData.PickleValues) PICKLE_PLAYERS.getOrDefault(id, DEF)).isOnAndTick(renderer);
    }

    public static boolean isActive(UUID id) {
        return ((PickleData.PickleValues) PICKLE_PLAYERS.getOrDefault(id, DEF)).isOn();
    }

    static {
        for (UUID id : Credits.INSTANCE.getDevs()) {
            PickleData.PickleValues value = new PickleData.PickleValues();
            PICKLE_PLAYERS.put(id, value);
            if (ME.equals(id)) {
                value.toggle(true, true);
            }
        }
        DEF = new PickleData.PickleValues();
    }

    public static class PickleValues {

        private PickleData.PickleValues.State state = PickleData.PickleValues.State.OFF;

        private boolean isJar = false;

        private float oldShadowSize = 1.0F;

        public void toggle(boolean on, boolean isJar) {
            this.isJar = isJar;
            if (on) {
                this.state = PickleData.PickleValues.State.FIRST_ON;
            } else {
                this.state = PickleData.PickleValues.State.FIRST_OFF;
            }
        }

        public void reset() {
            this.state = PickleData.PickleValues.State.OFF;
        }

        public boolean isOnAndTick(PlayerRenderer renderer) {
            switch(this.state) {
                case ON:
                    if (this.isJar) {
                        ((PlayerModel) renderer.m_7200_()).f_102808_.visible = false;
                        ((PlayerModel) renderer.m_7200_()).f_102809_.visible = false;
                        return false;
                    }
                    return true;
                case FIRST_ON:
                    this.oldShadowSize = renderer.f_114477_;
                    this.state = PickleData.PickleValues.State.ON;
                    if (this.isJar) {
                        ((PlayerModel) renderer.m_7200_()).f_102808_.visible = false;
                        ((PlayerModel) renderer.m_7200_()).f_102809_.visible = false;
                        return false;
                    }
                    renderer.f_114477_ = 0.0F;
                    return true;
                case FIRST_OFF:
                    renderer.f_114477_ = this.oldShadowSize;
                    ((PlayerModel) renderer.m_7200_()).f_102808_.visible = true;
                    ((PlayerModel) renderer.m_7200_()).f_102809_.visible = true;
                    this.state = PickleData.PickleValues.State.OFF;
                    return true;
                default:
                    return false;
            }
        }

        public boolean isOn() {
            return this.state == PickleData.PickleValues.State.ON || this.state == PickleData.PickleValues.State.FIRST_ON;
        }

        private static enum State {

            ON, OFF, FIRST_ON, FIRST_OFF
        }
    }
}