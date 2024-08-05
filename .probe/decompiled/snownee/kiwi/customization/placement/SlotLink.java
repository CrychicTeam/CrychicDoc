package snownee.kiwi.customization.placement;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.Kiwi;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.KBlockUtils;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.util.codec.CustomizationCodecs;

public record SlotLink(String from, String to, int interest, List<SlotLink.TagTest> testTag, SlotLink.ResultAction onLinkFrom, SlotLink.ResultAction onLinkTo, SlotLink.ResultAction onUnlinkFrom, SlotLink.ResultAction onUnlinkTo) {

    public static final Codec<String> PRIMARY_TAG_CODEC = ExtraCodecs.validate(Codec.STRING, s -> s.startsWith("*") ? DataResult.success(s) : DataResult.error(() -> "Primary tag must start with *"));

    public static final Codec<SlotLink> CODEC = RecordCodecBuilder.create(instance -> instance.group(PRIMARY_TAG_CODEC.fieldOf("from").forGetter(SlotLink::from), PRIMARY_TAG_CODEC.fieldOf("to").forGetter(SlotLink::to), Codec.INT.optionalFieldOf("interest", 100).forGetter(SlotLink::interest), CustomizationCodecs.strictOptionalField(SlotLink.TagTest.CODEC.listOf(), "test_tag", List.of()).forGetter(SlotLink::testTag), fromToPairCodec("on_link").forGetter($ -> Pair.of($.onLinkFrom, $.onLinkTo)), fromToPairCodec("on_unlink").forGetter($ -> Pair.of($.onUnlinkFrom, $.onUnlinkTo))).apply(instance, SlotLink::create));

    private static ImmutableMap<Pair<String, String>, SlotLink> LOOKUP = ImmutableMap.of();

    private static MapCodec<Pair<SlotLink.ResultAction, SlotLink.ResultAction>> fromToPairCodec(String fieldName) {
        Codec<Pair<SlotLink.ResultAction, SlotLink.ResultAction>> pairCodec = RecordCodecBuilder.create(instance -> instance.group(CustomizationCodecs.strictOptionalField(SlotLink.ResultAction.CODEC, "from", SlotLink.ResultAction.EMPTY).forGetter(Pair::getFirst), CustomizationCodecs.strictOptionalField(SlotLink.ResultAction.CODEC, "to", SlotLink.ResultAction.EMPTY).forGetter(Pair::getSecond)).apply(instance, Pair::of));
        return pairCodec.optionalFieldOf(fieldName, Pair.of(SlotLink.ResultAction.EMPTY, SlotLink.ResultAction.EMPTY));
    }

    private static void renewData(SlotLink.Preparation preparation) {
        Map<Pair<String, String>, SlotLink> map = Maps.newHashMapWithExpectedSize(preparation.slotLinks.size());
        Set<String> primaryTags = preparation.slotProviders.knownPrimaryTags();
        for (SlotLink link : preparation.slotLinks.values()) {
            if (!primaryTags.contains(link.from)) {
                Kiwi.LOGGER.error("Unknown primary tag in \"from\": %s".formatted(link.from));
            } else if (!primaryTags.contains(link.to)) {
                Kiwi.LOGGER.error("Unknown primary tag in \"to\": %s".formatted(link.to));
            } else {
                Pair<String, String> key = link.from.compareTo(link.to) <= 0 ? Pair.of(link.from, link.to) : Pair.of(link.to, link.from);
                SlotLink oldLink = (SlotLink) map.put(key, link);
                if (oldLink != null) {
                    Kiwi.LOGGER.error("Duplicate link: %s and %s".formatted(link, oldLink));
                }
            }
        }
        LOOKUP = ImmutableMap.copyOf(map);
    }

    public static SlotLink create(String from, String to, int interest, List<SlotLink.TagTest> testTag, Pair<SlotLink.ResultAction, SlotLink.ResultAction> onLink, Pair<SlotLink.ResultAction, SlotLink.ResultAction> onUnlink) {
        return new SlotLink(from, to, interest, testTag, (SlotLink.ResultAction) onLink.getFirst(), (SlotLink.ResultAction) onLink.getSecond(), (SlotLink.ResultAction) onUnlink.getFirst(), (SlotLink.ResultAction) onUnlink.getSecond());
    }

    @Nullable
    public static SlotLink find(PlaceSlot slot1, PlaceSlot slot2) {
        String key1 = slot1.primaryTag();
        String key2 = slot2.primaryTag();
        Pair<String, String> key = isUprightLink(slot1, slot2) ? Pair.of(key1, key2) : Pair.of(key2, key1);
        return (SlotLink) LOOKUP.get(key);
    }

    @Nullable
    public static SlotLink.MatchResult find(Collection<PlaceSlot> slots1, Collection<PlaceSlot> slots2) {
        if (!slots1.isEmpty() && !slots2.isEmpty()) {
            int maxInterest = -1;
            SlotLink matchedLink = null;
            boolean isUpright = false;
            for (PlaceSlot slot1 : slots1) {
                for (PlaceSlot slot2 : slots2) {
                    SlotLink link = find(slot1, slot2);
                    if (link != null && link.interest() > maxInterest && link.matches(slot1, slot2)) {
                        maxInterest = link.interest();
                        matchedLink = link;
                        isUpright = isUprightLink(slot1, slot2) == link.from().compareTo(link.to()) <= 0;
                    }
                }
            }
            return matchedLink == null ? null : new SlotLink.MatchResult(matchedLink, isUpright);
        } else {
            return null;
        }
    }

    @Nullable
    public static SlotLink.MatchResult find(BlockState ourState, BlockState theirState, Direction direction) {
        Collection<PlaceSlot> slots1 = PlaceSlot.find(ourState, direction);
        Collection<PlaceSlot> slots2 = PlaceSlot.find(theirState, direction.getOpposite());
        return find(slots1, slots2);
    }

    public static boolean isUprightLink(PlaceSlot slot1, PlaceSlot slot2) {
        return slot1.primaryTag().compareTo(slot2.primaryTag()) <= 0;
    }

    public boolean matches(PlaceSlot slot1, PlaceSlot slot2) {
        for (SlotLink.TagTest test : this.testTag) {
            String s1 = (String) slot1.tags().get(test.key);
            String s2 = (String) slot2.tags().get(test.key);
            if (s1 == null || s2 == null) {
                return false;
            }
            if (!test.operator.test().test(s1, s2)) {
                return false;
            }
        }
        return true;
    }

    public static record MatchResult(SlotLink link, boolean isUpright) {

        public SlotLink.ResultAction onLinkFrom() {
            return this.isUpright ? this.link.onLinkFrom : this.link.onLinkTo;
        }

        public SlotLink.ResultAction onLinkTo() {
            return this.isUpright ? this.link.onLinkTo : this.link.onLinkFrom;
        }

        public SlotLink.ResultAction onUnlinkTo() {
            return this.isUpright ? this.link.onUnlinkTo : this.link.onUnlinkFrom;
        }
    }

    public static record Preparation(Map<ResourceLocation, SlotLink> slotLinks, PlaceSlotProvider.Preparation slotProviders) {

        public static SlotLink.Preparation of(Supplier<Map<ResourceLocation, SlotLink>> slotLinksSupplier, PlaceSlotProvider.Preparation slotProviders) {
            Map<ResourceLocation, SlotLink> slotLinks = Platform.isDataGen() ? Map.of() : (Map) slotLinksSupplier.get();
            return new SlotLink.Preparation(slotLinks, slotProviders);
        }

        public void finish() {
            SlotLink.renewData(this);
        }
    }

    public static record ResultAction(Map<String, String> setProperties, boolean reflow) {

        public static final MapCodec<SlotLink.ResultAction> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(CustomizationCodecs.strictOptionalField(Codec.unboundedMap(Codec.STRING, Codec.STRING), "set_properties", Map.of()).forGetter(SlotLink.ResultAction::setProperties), Codec.BOOL.optionalFieldOf("reflow", false).forGetter(SlotLink.ResultAction::reflow)).apply(instance, SlotLink.ResultAction::new));

        public static final Codec<SlotLink.ResultAction> CODEC = MAP_CODEC.codec();

        private static final SlotLink.ResultAction EMPTY = new SlotLink.ResultAction(Map.of(), false);

        public BlockState apply(Level level, BlockPos pos, BlockState blockState) {
            for (Entry<String, String> entry : this.setProperties.entrySet()) {
                blockState = KBlockUtils.setValueByString(blockState, (String) entry.getKey(), (String) entry.getValue());
            }
            if (this.reflow) {
                KBlockSettings settings = KBlockSettings.of(blockState.m_60734_());
                if (settings != null && settings.placeChoices != null) {
                    blockState = settings.placeChoices.getStateForPlacement(level, pos, blockState);
                }
            }
            return blockState;
        }
    }

    public static record TagTest(String key, TagTestOperator operator) {

        public static final Codec<SlotLink.TagTest> CODEC = CustomizationCodecs.withAlternative(RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("key").forGetter(SlotLink.TagTest::key), TagTestOperator.CODEC.fieldOf("operator").forGetter(SlotLink.TagTest::operator)).apply(instance, SlotLink.TagTest::new)), ExtraCodecs.NON_EMPTY_STRING.xmap(s -> new SlotLink.TagTest(s, TagTestOperator.EQUAL), SlotLink.TagTest::key));
    }
}