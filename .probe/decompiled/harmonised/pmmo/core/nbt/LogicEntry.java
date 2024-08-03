package harmonised.pmmo.core.nbt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record LogicEntry(BehaviorToPrevious behavior, boolean addCases, List<LogicEntry.Case> cases) {

    public static final Codec<LogicEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(BehaviorToPrevious.CODEC.fieldOf("behavior_to_previous").forGetter(LogicEntry::behavior), Codec.BOOL.fieldOf("should_cases_add").forGetter(LogicEntry::addCases), Codec.list(LogicEntry.Case.CODEC).fieldOf("cases").forGetter(LogicEntry::cases)).apply(instance, LogicEntry::new));

    public static record Case(List<String> paths, List<LogicEntry.Criteria> criteria) {

        public static final Codec<LogicEntry.Case> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.list(Codec.STRING).fieldOf("paths").forGetter(LogicEntry.Case::paths), Codec.list(LogicEntry.Criteria.CODEC).fieldOf("criteria").forGetter(LogicEntry.Case::criteria)).apply(instance, LogicEntry.Case::new));
    }

    public static record Criteria(Operator operator, Optional<List<String>> comparators, Map<String, Double> skillMap) {

        public static final Codec<LogicEntry.Criteria> CODEC = RecordCodecBuilder.create(instance -> instance.group(Operator.CODEC.fieldOf("operator").forGetter(LogicEntry.Criteria::operator), Codec.list(Codec.STRING).optionalFieldOf("comparators").forGetter(LogicEntry.Criteria::comparators), Codec.unboundedMap(Codec.STRING, Codec.DOUBLE).fieldOf("value").forGetter(LogicEntry.Criteria::skillMap)).apply(instance, LogicEntry.Criteria::new));
    }
}