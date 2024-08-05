package com.almostreliable.lootjs.filters;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import net.minecraft.resources.ResourceLocation;

public interface ResourceLocationFilter extends Predicate<ResourceLocation> {

    public static record ByLocation(ResourceLocation location) implements ResourceLocationFilter {

        public boolean test(ResourceLocation resourceLocation) {
            return this.location.equals(resourceLocation);
        }
    }

    public static record ByPattern(Pattern pattern) implements ResourceLocationFilter {

        public boolean test(ResourceLocation resourceLocation) {
            return this.pattern.matcher(resourceLocation.toString()).matches();
        }
    }

    public static record Or(List<ResourceLocationFilter> filters) implements ResourceLocationFilter {

        public boolean test(ResourceLocation resourceLocation) {
            return this.filters.stream().anyMatch(filter -> filter.test(resourceLocation));
        }
    }
}