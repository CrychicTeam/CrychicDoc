package com.simibubi.create.content.trains.bogey;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;

public class BogeySizes {

    private static final Collection<BogeySizes.BogeySize> BOGEY_SIZES = new HashSet();

    public static final BogeySizes.BogeySize SMALL = new BogeySizes.BogeySize("create", "small", 0.40625F);

    public static final BogeySizes.BogeySize LARGE = new BogeySizes.BogeySize("create", "large", 0.78125F);

    public static BogeySizes.BogeySize addSize(String modId, String name, float size) {
        ResourceLocation location = new ResourceLocation(modId, name);
        return addSize(location, size);
    }

    public static BogeySizes.BogeySize addSize(ResourceLocation location, float size) {
        BogeySizes.BogeySize customSize = new BogeySizes.BogeySize(location, size);
        BOGEY_SIZES.add(customSize);
        return customSize;
    }

    public static List<BogeySizes.BogeySize> getAllSizesSmallToLarge() {
        return (List<BogeySizes.BogeySize>) BOGEY_SIZES.stream().sorted(Comparator.comparing(BogeySizes.BogeySize::wheelRadius)).collect(Collectors.toList());
    }

    public static List<BogeySizes.BogeySize> getAllSizesLargeToSmall() {
        List<BogeySizes.BogeySize> sizes = getAllSizesSmallToLarge();
        Collections.reverse(sizes);
        return sizes;
    }

    public static int count() {
        return BOGEY_SIZES.size();
    }

    public static void init() {
    }

    static {
        BOGEY_SIZES.add(SMALL);
        BOGEY_SIZES.add(LARGE);
    }

    public static record BogeySize(ResourceLocation location, float wheelRadius) {

        public BogeySize(String modId, String name, float wheelRadius) {
            this(new ResourceLocation(modId, name), wheelRadius);
        }

        public BogeySizes.BogeySize increment() {
            List<BogeySizes.BogeySize> values = BogeySizes.getAllSizesSmallToLarge();
            int ordinal = values.indexOf(this);
            return (BogeySizes.BogeySize) values.get((ordinal + 1) % values.size());
        }

        public boolean is(BogeySizes.BogeySize size) {
            return size.location == this.location;
        }
    }
}