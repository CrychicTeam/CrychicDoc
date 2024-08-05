package dev.shadowsoffire.placebo.patreon;

import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.packets.PatreonDisableMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TrailsManager {

    static Map<UUID, PatreonUtils.PatreonParticleType> TRAILS = new HashMap();

    public static final KeyMapping TOGGLE = new KeyMapping("placebo.toggleTrails", 329, "key.categories.placebo");

    public static final Set<UUID> DISABLED = new HashSet();

    public static void init() {
        new Thread(() -> {
            Placebo.LOGGER.info("Loading patreon trails data...");
            try {
                URL url = new URL("https://raw.githubusercontent.com/Shadows-of-Fire/Placebo/1.16/PatreonTrails.txt");
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    try {
                        String s;
                        while ((s = reader.readLine()) != null) {
                            String[] split = s.split(" ", 2);
                            if (split.length != 2) {
                                Placebo.LOGGER.error("Invalid patreon trail entry {} will be ignored.", s);
                            } else {
                                TRAILS.put(UUID.fromString(split[0]), PatreonUtils.PatreonParticleType.valueOf(split[1]));
                            }
                        }
                        reader.close();
                    } catch (Throwable var5) {
                        try {
                            reader.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                        throw var5;
                    }
                    reader.close();
                } catch (IOException var6) {
                    Placebo.LOGGER.error("Exception loading patreon trails data!");
                    var6.printStackTrace();
                }
            } catch (Exception var7) {
            }
            Placebo.LOGGER.info("Loaded {} patreon trails.", TRAILS.size());
            if (TRAILS.size() > 0) {
                MinecraftForge.EVENT_BUS.register(TrailsManager.class);
            }
        }, "Placebo Patreon Trail Loader").start();
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e) {
        PatreonUtils.PatreonParticleType t = null;
        if (e.phase == TickEvent.Phase.END && Minecraft.getInstance().level != null) {
            for (Player player : Minecraft.getInstance().level.players()) {
                if (!player.m_20145_() && player.f_19797_ * 3 % 2 == 0 && !DISABLED.contains(player.m_20148_()) && (t = (PatreonUtils.PatreonParticleType) TRAILS.get(player.m_20148_())) != null) {
                    ClientLevel world = (ClientLevel) player.m_9236_();
                    RandomSource rand = world.f_46441_;
                    ParticleOptions type = (ParticleOptions) t.type.get();
                    world.addParticle(type, player.m_20185_() + rand.nextDouble() * 0.4 - 0.2, player.m_20186_() + 0.1, player.m_20189_() + rand.nextDouble() * 0.4 - 0.2, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void keys(InputEvent.Key e) {
        if (e.getAction() == 1 && TOGGLE.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().getConnection() != null) {
            Placebo.CHANNEL.sendToServer(new PatreonDisableMessage(0));
        }
    }
}