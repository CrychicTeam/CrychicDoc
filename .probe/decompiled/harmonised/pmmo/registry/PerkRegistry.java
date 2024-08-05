package harmonised.pmmo.registry;

import com.google.common.base.Preconditions;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.perks.Perk;
import harmonised.pmmo.config.PerksConfig;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.MsLoggy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;

public class PerkRegistry {

    private final Map<ResourceLocation, Perk> perks = new HashMap();

    private final List<PerkRegistry.TickSchedule> tickTracker = new ArrayList();

    private final List<PerkRegistry.PerkCooldown> coolTracker = new ArrayList();

    public void registerPerk(ResourceLocation perkID, Perk perk) {
        Preconditions.checkNotNull(perkID);
        Preconditions.checkNotNull(perk);
        this.perks.put(perkID, perk);
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.API, "Registered Perk: " + perkID.toString());
    }

    public void registerClientClone(ResourceLocation perkID, Perk perk) {
        Preconditions.checkNotNull(perkID);
        Preconditions.checkNotNull(perk);
        Perk clientCopy = new Perk(perk.conditions(), perk.propertyDefaults(), (a, b) -> new CompoundTag(), (a, b, c) -> new CompoundTag(), (a, b) -> new CompoundTag(), perk.description(), perk.status());
        this.perks.putIfAbsent(perkID, clientCopy);
    }

    public MutableComponent getDescription(ResourceLocation id) {
        return ((Perk) this.perks.getOrDefault(id, Perk.empty())).description();
    }

    public List<MutableComponent> getStatusLines(ResourceLocation id, Player player, CompoundTag settings) {
        return (List<MutableComponent>) ((Perk) this.perks.getOrDefault(id, Perk.empty())).status().apply(player, settings);
    }

    public CompoundTag executePerk(EventType cause, Player player, @NotNull CompoundTag dataIn) {
        if (player == null) {
            return new CompoundTag();
        } else {
            CompoundTag output = new CompoundTag();
            ((List) PerksConfig.PERK_SETTINGS.get().getOrDefault(cause, new ArrayList())).forEach(src -> {
                ResourceLocation perkID = new ResourceLocation(src.getString("perk"));
                Perk perk = (Perk) this.perks.getOrDefault(perkID, Perk.empty());
                CompoundTag fullSrc = new CompoundTag().merge(perk.propertyDefaults().copy()).merge(src.copy()).merge(dataIn.copy()).merge(output.copy());
                fullSrc.putInt("level", fullSrc.contains("skill") ? Core.get(player.m_9236_()).getData().getPlayerSkillLevel(fullSrc.getString("skill"), player.m_20148_()) : 0);
                if (perk.canActivate(player, fullSrc)) {
                    MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Perk Executed: %s".formatted(fullSrc.toString()));
                    CompoundTag executionOutput = perk.start(player, fullSrc);
                    this.tickTracker.add(new PerkRegistry.TickSchedule(perk, player, fullSrc.copy(), new AtomicInteger(0)));
                    if (fullSrc.contains("cooldown") && this.isPerkCooledDown(player, fullSrc)) {
                        this.coolTracker.add(new PerkRegistry.PerkCooldown(perkID, player, fullSrc, player.m_9236_().getGameTime()));
                    }
                    output.merge(executionOutput);
                }
            });
            return output;
        }
    }

    public void executePerkTicks(TickEvent.LevelTickEvent event) {
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.PERKS, "Perk Tick Tracker:" + MsLoggy.listToString(this.tickTracker));
        this.coolTracker.removeIf(tracker -> tracker.cooledDown(event.level));
        new ArrayList(this.tickTracker).forEach(schedule -> {
            if (schedule.perk().canActivate(schedule.player(), schedule.src())) {
                if (schedule.shouldTick()) {
                    schedule.tick();
                } else {
                    schedule.perk().stop(schedule.player(), schedule.src());
                    this.tickTracker.remove(schedule);
                }
            } else {
                this.tickTracker.remove(schedule);
            }
        });
    }

    public boolean isPerkCooledDown(Player player, CompoundTag src) {
        ResourceLocation perkID = new ResourceLocation(src.getString("perk"));
        return this.coolTracker.stream().noneMatch(cd -> cd.player().equals(player) && cd.perkID().equals(perkID));
    }

    private static record PerkCooldown(ResourceLocation perkID, Player player, CompoundTag src, long lastUse) {

        public boolean cooledDown(Level level) {
            return level.getGameTime() > this.lastUse + (long) this.src.getInt("cooldown");
        }
    }

    private static record TickSchedule(Perk perk, Player player, CompoundTag src, AtomicInteger ticksElapsed) {

        public boolean shouldTick() {
            return this.src.contains("duration") && this.ticksElapsed.get() <= this.src.getInt("duration");
        }

        public void tick() {
            this.ticksElapsed().getAndIncrement();
            this.perk.tick(this.player, this.src, this.ticksElapsed.get());
        }
    }
}