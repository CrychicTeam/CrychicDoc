package harmonised.pmmo.client.events;

import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.util.MsLoggy;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "pmmo", bus = Bus.FORGE, value = { Dist.CLIENT })
public class ClientTickHandler {

    private static int ticksElapsed = 0;

    public static final List<ClientTickHandler.GainEntry> xpGains = new ArrayList();

    public static void tickGUI() {
        ticksElapsed++;
    }

    public static boolean isRefreshTick() {
        return ticksElapsed >= 15;
    }

    public static void resetTicks() {
        ticksElapsed = 0;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        ticksElapsed++;
        tickDownGainList();
    }

    public static void tickDownGainList() {
        xpGains.forEach(ClientTickHandler.GainEntry::downTick);
        xpGains.removeIf(entry -> entry.duration <= 0);
    }

    public static void addToGainList(String skill, long amount) {
        SkillData skillData = (SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault());
        if (!Config.GAIN_BLACKLIST.get().contains(skill) && (!skillData.isSkillGroup() || !skillData.getGroup().containsKey(skill))) {
            if (xpGains.stream().anyMatch(entry -> entry.skill.equals(skill))) {
                ClientTickHandler.GainEntry existingEntry = (ClientTickHandler.GainEntry) xpGains.stream().filter(entry -> entry.skill.equals(skill)).findFirst().get();
                xpGains.remove(existingEntry);
                xpGains.add(new ClientTickHandler.GainEntry(skill, existingEntry.value + amount));
            } else {
                xpGains.add(new ClientTickHandler.GainEntry(skill, amount));
            }
        }
    }

    public static class GainEntry {

        public int duration;

        private final String skill;

        private final long value;

        public GainEntry(String skill, long value) {
            this.skill = skill;
            this.duration = MsLoggy.DEBUG.logAndReturn(Config.GAIN_LIST_LINGER_DURATION.get(), MsLoggy.LOG_CODE.GUI, "Gain Duration Set as: {}");
            this.value = value;
        }

        public void downTick() {
            this.duration--;
        }

        public Component display() {
            double fade = (double) this.duration / (double) Config.GAIN_LIST_LINGER_DURATION.get().intValue();
            return Component.literal((this.value >= 0L ? "+" : "") + this.value + " ").append(Component.translatable("pmmo." + this.skill)).setStyle(CoreUtils.getSkillStyle(this.skill, fade));
        }

        public String toString() {
            return "Duration:" + this.duration + "|" + this.display().toString();
        }
    }
}