package dev.shadowsoffire.placebo.json;

import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.reload.WeightedDynamicRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;

public class GearSetRegistry extends WeightedDynamicRegistry<GearSet> {

    public static final GearSetRegistry INSTANCE = new GearSetRegistry();

    public GearSetRegistry() {
        super(Placebo.LOGGER, "gear_sets", false, false);
    }

    public <T extends Predicate<GearSet>> GearSet getRandomSet(RandomSource rand, float luck, @Nullable List<GearSet.SetPredicate> armorSets) {
        if (armorSets != null && !armorSets.isEmpty()) {
            List<GearSet> valid = (List<GearSet>) this.registry.values().stream().filter(e -> {
                for (Predicate<GearSet> f : armorSets) {
                    if (f.test(e)) {
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());
            if (valid.isEmpty()) {
                Placebo.LOGGER.error("Failed to locate any gear sets matching the following predicates: ");
                armorSets.forEach(s -> Placebo.LOGGER.error(s.toString()));
                return this.getRandomItem(rand, luck);
            } else {
                List<WeightedEntry.Wrapper<GearSet>> list = new ArrayList(valid.size());
                valid.stream().map(l -> l.wrap(luck)).forEach(list::add);
                return (GearSet) WeightedRandom.getRandomItem(rand, list).map(WeightedEntry.Wrapper::m_146310_).orElse(null);
            }
        } else {
            return this.getRandomItem(rand, luck);
        }
    }

    @Override
    protected void registerBuiltinCodecs() {
        this.registerDefaultCodec(new ResourceLocation("placebo", "gear_set"), GearSet.CODEC);
    }
}