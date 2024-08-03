package mezz.jei.common.util;

import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TagUtil {

    public static <VALUE, STACK> Optional<ResourceLocation> getTagEquivalent(Collection<STACK> stacks, Function<STACK, VALUE> stackToValue, Supplier<Stream<Pair<TagKey<VALUE>, HolderSet.Named<VALUE>>>> tagSupplier) {
        if (stacks.size() < 2) {
            return Optional.empty();
        } else {
            List<VALUE> values = stacks.stream().map(stackToValue).toList();
            return ((Stream) tagSupplier.get()).filter(e -> {
                HolderSet.Named<VALUE> tag = (HolderSet.Named<VALUE>) e.getSecond();
                int count = tag.m_203632_();
                return count == values.size() ? IntStream.range(0, count).allMatch(i -> {
                    VALUE tagValue = (VALUE) tag.m_203662_(i).value();
                    VALUE value = (VALUE) values.get(i);
                    return value.equals(tagValue);
                }) : false;
            }).map(e -> ((TagKey) e.getFirst()).location()).findFirst();
        }
    }
}