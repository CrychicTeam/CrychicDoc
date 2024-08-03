package harmonised.pmmo.api.perks;

import harmonised.pmmo.core.Core;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.function.TriFunction;

public record Perk(BiPredicate<Player, CompoundTag> conditions, CompoundTag propertyDefaults, BiFunction<Player, CompoundTag, CompoundTag> start, TriFunction<Player, CompoundTag, Integer, CompoundTag> tick, BiFunction<Player, CompoundTag, CompoundTag> stop, MutableComponent description, BiFunction<Player, CompoundTag, List<MutableComponent>> status) {

    public static final BiPredicate<Player, CompoundTag> VALID_CONTEXT = (player, src) -> {
        if (src.contains("cooldown") && !Core.get(player.m_9236_()).getPerkRegistry().isPerkCooledDown(player, src)) {
            return false;
        } else {
            boolean chanceSucceed = false;
            if (src.contains("chance") && src.getDouble("chance") < player.m_9236_().random.nextDouble()) {
                return false;
            } else {
                if (src.contains("chance")) {
                    chanceSucceed = true;
                }
                if (src.contains("skill")) {
                    if (src.contains("firework_skill") && !src.getString("skill").equals(src.getString("firework_skill"))) {
                        return false;
                    }
                    int skillLevel = src.getInt("level");
                    if (src.contains("max_level") && skillLevel > src.getInt("max_level")) {
                        return false;
                    }
                    if (src.contains("min_level") && skillLevel < src.getInt("min_level")) {
                        return false;
                    }
                    boolean modulus = src.contains("per_x_level");
                    boolean milestone = src.contains("milestones");
                    if (modulus || milestone) {
                        boolean modulus_match = modulus;
                        boolean milestone_match = milestone;
                        if (modulus && skillLevel % Math.max(1, src.getInt("per_x_level")) != 0) {
                            modulus_match = false;
                        }
                        if (milestone && !src.getList("milestones", 6).stream().map(tag -> ((DoubleTag) tag).getAsInt()).toList().contains(skillLevel)) {
                            milestone_match = false;
                        }
                        if (!modulus_match && !milestone_match) {
                            return false;
                        }
                    }
                }
                if (chanceSucceed && src.contains("chance_message")) {
                    String msg = src.getString("chance_message");
                    player.m_213846_(Component.literal(msg));
                }
                return true;
            }
        }
    };

    public static Perk.Builder begin() {
        return new Perk.Builder();
    }

    public static Perk empty() {
        return new Perk.Builder().build();
    }

    public boolean canActivate(Player player, CompoundTag settings) {
        return VALID_CONTEXT.test(player, settings) && this.conditions().test(player, settings);
    }

    public CompoundTag start(Player player, CompoundTag nbt) {
        return (CompoundTag) this.start.apply(player, nbt);
    }

    public CompoundTag tick(Player player, CompoundTag nbt, int elapsedTicks) {
        return (CompoundTag) this.tick.apply(player, nbt, elapsedTicks);
    }

    public CompoundTag stop(Player player, CompoundTag nbt) {
        return (CompoundTag) this.stop.apply(player, nbt);
    }

    public static class Builder {

        BiPredicate<Player, CompoundTag> conditions = (p, n) -> true;

        CompoundTag propertyDefaults = new CompoundTag();

        BiFunction<Player, CompoundTag, CompoundTag> start = (p, c) -> new CompoundTag();

        TriFunction<Player, CompoundTag, Integer, CompoundTag> tick = (p, c, i) -> new CompoundTag();

        BiFunction<Player, CompoundTag, CompoundTag> stop = (p, c) -> new CompoundTag();

        MutableComponent description = Component.empty();

        BiFunction<Player, CompoundTag, List<MutableComponent>> status = (p, s) -> List.of();

        protected Builder() {
        }

        public Perk.Builder addConditions(BiPredicate<Player, CompoundTag> conditions) {
            this.conditions = conditions;
            return this;
        }

        public Perk.Builder addDefaults(CompoundTag defaults) {
            this.propertyDefaults = defaults;
            return this;
        }

        public Perk.Builder setStart(BiFunction<Player, CompoundTag, CompoundTag> start) {
            this.start = start;
            return this;
        }

        public Perk.Builder setTick(TriFunction<Player, CompoundTag, Integer, CompoundTag> tick) {
            this.tick = tick;
            return this;
        }

        public Perk.Builder setStop(BiFunction<Player, CompoundTag, CompoundTag> stop) {
            this.stop = stop;
            return this;
        }

        public Perk.Builder setDescription(MutableComponent description) {
            this.description = description;
            return this;
        }

        public Perk.Builder setStatus(BiFunction<Player, CompoundTag, List<MutableComponent>> status) {
            this.status = status;
            return this;
        }

        public Perk build() {
            return new Perk(this.conditions, this.propertyDefaults, this.start, this.tick, this.stop, this.description, this.status);
        }
    }
}