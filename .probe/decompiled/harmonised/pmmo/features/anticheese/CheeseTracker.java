package harmonised.pmmo.features.anticheese;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.util.MsLoggy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "pmmo", bus = Bus.FORGE)
public class CheeseTracker {

    private static final Map<Player, Map<EventType, CheeseTracker.AFKTracker>> AFK_DATA = new HashMap();

    private static final Map<Player, Map<EventType, CheeseTracker.DiminishTracker>> DIMINISH_DATA = new HashMap();

    private static final Map<Player, Map<EventType, CheeseTracker.NormTracker>> NORMALIZED_DATA = new HashMap();

    public static void applyAntiCheese(EventType event, ResourceLocation source, Player player, Map<String, Long> awardIn) {
        if (player != null && event != null && player instanceof ServerPlayer) {
            CheeseTracker.Setting setting = (CheeseTracker.Setting) AntiCheeseConfig.SETTINGS_AFK.get().get(event);
            if (setting != null) {
                setting.applyAFK(event, source, player, awardIn);
            }
            if ((setting = (CheeseTracker.Setting) AntiCheeseConfig.SETTINGS_DIMINISHING.get().get(event)) != null) {
                setting.applyDiminuation(event, source, player, awardIn);
            }
            if ((setting = (CheeseTracker.Setting) AntiCheeseConfig.SETTINGS_NORMALIZED.get().get(event)) != null) {
                setting.applyNormalization(event, source, player, awardIn);
            }
        }
    }

    @SubscribeEvent
    public static void playerWatcher(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END && event.side != LogicalSide.CLIENT) {
            AFK_DATA.forEach((player, map) -> map.forEach((type, tracker) -> {
                if (player != null && !tracker.meetsAFKCriteria(player)) {
                    tracker.cooldown();
                }
            }));
            DIMINISH_DATA.forEach((player, map) -> map.forEach((type, tracker) -> tracker.cooloff()));
            NORMALIZED_DATA.forEach((player, map) -> map.forEach((type, tracker) -> tracker.retainTimeRemaining--));
        }
    }

    private static class AFKTracker {

        int durationAFK = 0;

        int minDuration = 0;

        int cooldownBy = 1;

        int tolerance = 0;

        boolean strictFacing;

        BlockPos lastPos;

        Vec3 lastLookAngle;

        public AFKTracker(Player player, int minDuration, int cooldownBy, int tolerance, boolean strictFacing) {
            this.lastPos = player.m_20183_();
            this.lastLookAngle = player.m_20154_();
            this.minDuration = minDuration;
            this.cooldownBy = cooldownBy;
            this.tolerance = tolerance;
            this.strictFacing = strictFacing;
        }

        public CheeseTracker.AFKTracker update(Player player) {
            if (this.meetsAFKCriteria(player)) {
                this.durationAFK++;
            } else if (this.durationAFK <= 0) {
                this.lastLookAngle = player.m_20154_();
                this.lastPos = player.m_20183_();
            }
            return this;
        }

        public void cooldown() {
            if (this.durationAFK > 0) {
                this.durationAFK = this.durationAFK - this.cooldownBy;
            }
        }

        public boolean meetsAFKCriteria(Player player) {
            BlockPos curPos = player.m_20183_();
            return (!this.strictFacing || this.lastLookAngle.equals(player.m_20154_())) && Math.abs(this.lastPos.m_123341_() - curPos.m_123341_()) < this.tolerance && Math.abs(this.lastPos.m_123342_() - curPos.m_123342_()) < this.tolerance && Math.abs(this.lastPos.m_123343_() - curPos.m_123343_()) < this.tolerance;
        }

        public boolean isAFK() {
            return MsLoggy.DEBUG.logAndReturn(this.durationAFK >= this.minDuration, MsLoggy.LOG_CODE.FEATURE, "isAFK:{}({}:{})", this.durationAFK, this.minDuration);
        }

        public int getAFKDuration() {
            return this.durationAFK - this.minDuration;
        }
    }

    private static class DiminishTracker {

        public int persistedTime;

        public int cooloffLeft;

        private final int timeToClearReduction;

        public DiminishTracker(int timeToClear) {
            this.timeToClearReduction = timeToClear;
        }

        public void cooloff() {
            if (--this.cooloffLeft <= 0) {
                this.persistedTime = 0;
            }
        }

        public void diminish() {
            this.persistedTime++;
            this.cooloffLeft = this.timeToClearReduction;
        }
    }

    private static class NormTracker {

        public final Map<String, Long> norms = new HashMap();

        public int retainTimeRemaining;

        public NormTracker(int defaultRetention) {
            this.retainTimeRemaining = defaultRetention;
        }
    }

    public static record Setting(List<String> source, int minTime, int retention, int toleranceFlat, double reduction, int cooloff, double tolerancePercent, boolean strictTolerance) {

        public static final String SOURCE = "source";

        public static final String MIN_TIME_TO_APPLY = "min_time_to_apply";

        public static final String REDUCTION = "reduction";

        public static final String COOLOFF = "cooloff_amount";

        public static final String TOLERANCE_PERCENT = "tolerance_percent";

        public static final String TOLERANCE_FLAT = "tolerance_flat";

        public static final String RETENTION = "retention_duration";

        public static final String STRICT = "strict_tolerance";

        public static final Codec<CheeseTracker.Setting> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.listOf().optionalFieldOf("source").forGetter(s -> Optional.of(s.source)), Codec.INT.optionalFieldOf("min_time_to_apply").forGetter(s -> Optional.of(s.minTime)), Codec.INT.optionalFieldOf("retention_duration").forGetter(s -> Optional.of(s.retention)), Codec.INT.optionalFieldOf("tolerance_flat").forGetter(s -> Optional.of(s.toleranceFlat)), Codec.DOUBLE.optionalFieldOf("reduction").forGetter(s -> Optional.of(s.reduction)), Codec.INT.optionalFieldOf("cooloff_amount").forGetter(s -> Optional.of(s.cooloff)), Codec.DOUBLE.optionalFieldOf("tolerance_percent").forGetter(s -> Optional.of(s.tolerancePercent)), Codec.BOOL.optionalFieldOf("strict_tolerance").forGetter(s -> Optional.of(s.strictTolerance))).apply(instance, (src, min, ret, flat, red, cool, per, strict) -> new CheeseTracker.Setting((List<String>) src.orElse(new ArrayList()), (Integer) min.orElse(0), (Integer) ret.orElse(0), (Integer) flat.orElse(0), (Double) red.orElse(1.0), (Integer) cool.orElse(1), (Double) per.orElse(0.0), (Boolean) strict.orElse(true))));

        public static CheeseTracker.Setting.Builder build() {
            return new CheeseTracker.Setting.Builder();
        }

        public void applyAFK(EventType event, ResourceLocation source, Player player, Map<String, Long> awardIn) {
            CheeseTracker.AFKTracker afkData = ((CheeseTracker.AFKTracker) ((Map) CheeseTracker.AFK_DATA.computeIfAbsent(player, p -> new HashMap())).computeIfAbsent(event, e -> new CheeseTracker.AFKTracker(player, this.minTime(), this.cooloff(), this.toleranceFlat(), this.strictTolerance()))).update(player);
            if ((this.source().isEmpty() || this.source().contains(source.toString())) && afkData.isAFK()) {
                awardIn.keySet().forEach(skill -> {
                    MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.XP, "AFK reduction factor: {}", this.reduction * (double) afkData.getAFKDuration());
                    awardIn.compute(skill, (key, xp) -> {
                        long reductionAmount = Double.valueOf((double) xp.longValue() * this.reduction * (double) afkData.getAFKDuration()).longValue();
                        return xp - (AntiCheeseConfig.AFK_CAN_SUBTRACT.get() ? reductionAmount : (reductionAmount > xp ? xp : reductionAmount));
                    });
                });
            }
        }

        public void applyDiminuation(EventType event, ResourceLocation source, Player player, Map<String, Long> awardIn) {
            CheeseTracker.DiminishTracker tracker = (CheeseTracker.DiminishTracker) ((Map) CheeseTracker.DIMINISH_DATA.computeIfAbsent(player, p -> new HashMap())).computeIfAbsent(event, e -> new CheeseTracker.DiminishTracker(this.retention));
            if (this.source().isEmpty() || this.source().contains(source.toString())) {
                tracker.diminish();
                awardIn.keySet().forEach(skill -> {
                    double reductionScale = 1.0 - this.reduction * (double) tracker.persistedTime;
                    awardIn.compute(skill, (key, xp) -> Double.valueOf((double) xp.longValue() * Math.max(0.0, reductionScale)).longValue());
                });
            }
        }

        public void applyNormalization(EventType event, ResourceLocation source, Player player, Map<String, Long> awardIn) {
            if (this.source().isEmpty() || this.source().contains(source.toString())) {
                CheeseTracker.NormTracker norms = (CheeseTracker.NormTracker) ((Map) CheeseTracker.NORMALIZED_DATA.computeIfAbsent(player, p -> new HashMap())).computeIfAbsent(event, e -> new CheeseTracker.NormTracker(this.retention));
                norms.retainTimeRemaining = this.retention;
                awardIn.forEach((skill, value) -> {
                    long norm = (Long) norms.norms.computeIfAbsent(skill, s -> value);
                    long acceptableVariance = Double.valueOf(Math.min((double) norm + Math.max(1.0, (double) norm * this.tolerancePercent), (double) (norm + (long) this.toleranceFlat))).longValue();
                    norms.norms.put(skill, value > acceptableVariance ? acceptableVariance : value);
                });
                awardIn.putAll(norms.norms);
            }
        }

        public static class Builder {

            private final List<String> source = new ArrayList();

            private int minTime = 0;

            private int retention = 0;

            private int toleranceFlat = 0;

            private double reduction = 0.0;

            private int cooloff = 0;

            private double tolerancePercent = 0.0;

            private boolean strictTolerance = true;

            protected Builder() {
            }

            public CheeseTracker.Setting.Builder source(String entry) {
                this.source.add(entry);
                return this;
            }

            public CheeseTracker.Setting.Builder source(String... entries) {
                this.source.addAll(Arrays.asList(entries));
                return this;
            }

            public CheeseTracker.Setting.Builder minTime(int min) {
                this.minTime = min;
                return this;
            }

            public CheeseTracker.Setting.Builder retention(int ret) {
                this.retention = ret;
                return this;
            }

            public CheeseTracker.Setting.Builder reduction(double red) {
                this.reduction = red;
                return this;
            }

            public CheeseTracker.Setting.Builder cooloff(int cool) {
                this.cooloff = cool;
                return this;
            }

            public CheeseTracker.Setting.Builder tolerance(int flat) {
                this.toleranceFlat = flat;
                return this;
            }

            public CheeseTracker.Setting.Builder tolerance(double percent) {
                this.tolerancePercent = percent;
                return this;
            }

            public CheeseTracker.Setting.Builder setStrictness(boolean isStrict) {
                this.strictTolerance = isStrict;
                return this;
            }

            public CheeseTracker.Setting build() {
                return new CheeseTracker.Setting(this.source, this.minTime, this.retention, this.toleranceFlat, this.reduction, this.cooloff, this.tolerancePercent, this.strictTolerance);
            }
        }
    }
}