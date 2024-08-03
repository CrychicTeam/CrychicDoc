package snownee.loquat.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import snownee.loquat.core.area.Area;

public class AreaSelectorParser {

    public static final SimpleCommandExceptionType ERROR_INVALID_NAME_OR_UUID = new SimpleCommandExceptionType(Component.translatable("loquat.argument.area.invalid"));

    private final StringReader reader;

    private BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions = EntitySelectorParser.SUGGEST_NOTHING;

    private Predicate<Area> predicate = area -> true;

    @Nullable
    private Area.Type<?> type;

    private boolean limited;

    private int maxResults;

    private boolean sorted;

    private MinMaxBounds.Doubles distance = MinMaxBounds.Doubles.ANY;

    @Nullable
    private UUID uuid;

    private BiConsumer<Vec3, List<? extends Area>> order = AreaSelector.ORDER_ARBITRARY;

    private boolean selectedAreas;

    @Nullable
    private Double x;

    @Nullable
    private Double y;

    @Nullable
    private Double z;

    @Nullable
    private Double deltaX;

    @Nullable
    private Double deltaY;

    @Nullable
    private Double deltaZ;

    public AreaSelectorParser(StringReader reader) {
        this.reader = reader;
    }

    public AreaSelector getSelector() {
        AABB aabb;
        if (this.deltaX == null && this.deltaY == null && this.deltaZ == null) {
            if (this.distance.m_55326_() != null) {
                double d0 = (Double) this.distance.m_55326_();
                aabb = new AABB(-d0, -d0, -d0, d0 + 1.0, d0 + 1.0, d0 + 1.0);
            } else {
                aabb = null;
            }
        } else {
            aabb = this.createAabb(this.deltaX == null ? 0.0 : this.deltaX, this.deltaY == null ? 0.0 : this.deltaY, this.deltaZ == null ? 0.0 : this.deltaZ);
        }
        UnaryOperator<Vec3> position;
        if (this.x == null && this.y == null && this.z == null) {
            position = UnaryOperator.identity();
        } else {
            double d0 = this.x == null ? 0.0 : this.x;
            double d1 = this.y == null ? 0.0 : this.y;
            double d2 = this.z == null ? 0.0 : this.z;
            position = vec3 -> new Vec3(this.x == null ? vec3.x : d0, this.y == null ? vec3.y : d1, this.z == null ? vec3.z : d2);
        }
        return new AreaSelector(this.maxResults, this.predicate, this.distance, position, aabb, this.order, this.selectedAreas, this.uuid);
    }

    private AABB createAabb(double dx, double dy, double dz) {
        boolean flag = dx < 0.0;
        boolean flag1 = dy < 0.0;
        boolean flag2 = dz < 0.0;
        double d0 = flag ? dx : 0.0;
        double d1 = flag1 ? dy : 0.0;
        double d2 = flag2 ? dz : 0.0;
        double d3 = (flag ? 0.0 : dx) + 1.0;
        double d4 = (flag1 ? 0.0 : dy) + 1.0;
        double d5 = (flag2 ? 0.0 : dz) + 1.0;
        return new AABB(d0, d1, d2, d3, d4, d5);
    }

    public AreaSelector parse() throws CommandSyntaxException {
        this.suggestions = this::suggestStart;
        if (this.reader.canRead() && this.reader.peek() == '@') {
            this.reader.skip();
            this.parseSelector();
        } else {
            this.parseNameOrUUID();
        }
        this.finalizePredicates();
        return this.getSelector();
    }

    public void finalizePredicates() {
    }

    protected void parseSelector() throws CommandSyntaxException {
        this.suggestions = this::suggestSelector;
        if (!this.reader.canRead()) {
            throw EntitySelectorParser.ERROR_MISSING_SELECTOR_TYPE.createWithContext(this.reader);
        } else {
            this.suggestions = this::suggestSelector;
            int i = this.reader.getCursor();
            char ch = this.reader.read();
            if (ch == 'p') {
                this.maxResults = 1;
                this.order = AreaSelector.ORDER_NEAREST;
            } else if (ch == 'a') {
                this.maxResults = Integer.MAX_VALUE;
                this.order = AreaSelector.ORDER_ARBITRARY;
            } else if (ch == 'r') {
                this.maxResults = 1;
                this.order = AreaSelector.ORDER_RANDOM;
            } else {
                if (ch != 's') {
                    this.reader.setCursor(i);
                    throw EntitySelectorParser.ERROR_UNKNOWN_SELECTOR_TYPE.createWithContext(this.reader, "@" + ch);
                }
                this.maxResults = Integer.MAX_VALUE;
                this.selectedAreas = true;
            }
            this.suggestions = this::suggestOpenOptions;
            if (this.reader.canRead() && this.reader.peek() == '[') {
                this.reader.skip();
                this.suggestions = this::suggestOptionsKeyOrClose;
                this.parseOptions();
            }
        }
    }

    protected void parseNameOrUUID() throws CommandSyntaxException {
        int i = this.reader.getCursor();
        String s = this.reader.readString();
        try {
            this.uuid = UUID.fromString(s);
        } catch (IllegalArgumentException var4) {
            if (s.isEmpty() || s.length() > 16) {
                this.reader.setCursor(i);
                throw ERROR_INVALID_NAME_OR_UUID.createWithContext(this.reader);
            }
        }
        this.maxResults = 1;
    }

    public void parseOptions() throws CommandSyntaxException {
        this.suggestions = this::suggestOptionsKey;
        this.reader.skipWhitespace();
        while (this.reader.canRead() && this.reader.peek() != ']') {
            this.reader.skipWhitespace();
            int i = this.reader.getCursor();
            String s = this.reader.readString();
            AreaSelectorOptions.Modifier modifier = AreaSelectorOptions.get(this, s, i);
            this.reader.skipWhitespace();
            if (!this.reader.canRead() || this.reader.peek() != '=') {
                this.reader.setCursor(i);
                throw EntitySelectorParser.ERROR_EXPECTED_OPTION_VALUE.createWithContext(this.reader, s);
            }
            this.reader.skip();
            this.reader.skipWhitespace();
            this.suggestions = EntitySelectorParser.SUGGEST_NOTHING;
            modifier.handle(this);
            this.reader.skipWhitespace();
            this.suggestions = this::suggestOptionsNextOrClose;
            if (this.reader.canRead()) {
                if (this.reader.peek() != ',') {
                    if (this.reader.peek() != ']') {
                        throw EntitySelectorParser.ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
                    }
                    break;
                }
                this.reader.skip();
                this.suggestions = this::suggestOptionsKey;
            }
        }
        if (this.reader.canRead()) {
            this.reader.skip();
            this.suggestions = EntitySelectorParser.SUGGEST_NOTHING;
        } else {
            throw EntitySelectorParser.ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
        }
    }

    public boolean shouldInvertValue() {
        this.reader.skipWhitespace();
        if (this.reader.canRead() && this.reader.peek() == '!') {
            this.reader.skip();
            this.reader.skipWhitespace();
            return true;
        } else {
            return false;
        }
    }

    public void thenTag() {
        boolean invert = this.shouldInvertValue();
        String s = this.reader.readUnquotedString();
        this.addPredicate(area -> s.isEmpty() ? area.getTags().isEmpty() != invert : area.getTags().contains(s) != invert);
    }

    public void addPredicate(Predicate<Area> predicate) {
        this.predicate = this.predicate.and(predicate);
    }

    public void thenType() {
    }

    public void thenLimit() throws CommandSyntaxException {
        int i = this.reader.getCursor();
        int j = this.reader.readInt();
        if (j < 1) {
            this.reader.setCursor(i);
            throw EntitySelectorOptions.ERROR_LIMIT_TOO_SMALL.createWithContext(this.getReader());
        } else {
            this.maxResults = j;
            this.limited = true;
        }
    }

    public boolean isTypeLimited() {
        return this.type != null;
    }

    public void thenDistance() throws CommandSyntaxException {
        int i = this.reader.getCursor();
        MinMaxBounds.Doubles range = MinMaxBounds.Doubles.fromReader(this.reader);
        if ((range.m_55305_() == null || !((Double) range.m_55305_() < 0.0)) && (range.m_55326_() == null || !((Double) range.m_55326_() < 0.0))) {
            this.distance = range;
        } else {
            this.reader.setCursor(i);
            throw EntitySelectorOptions.ERROR_RANGE_NEGATIVE.createWithContext(this.reader);
        }
    }

    public void thenSort() throws CommandSyntaxException {
        int i = this.reader.getCursor();
        String s = this.reader.readUnquotedString();
        this.setSuggestions((builder, builderConsumer) -> SharedSuggestionProvider.suggest(List.of("nearest", "furthest", "random", "arbitrary"), builder));
        switch(s) {
            case "nearest":
                this.order = AreaSelector.ORDER_NEAREST;
                break;
            case "furthest":
                this.order = AreaSelector.ORDER_FURTHEST;
                break;
            case "random":
                this.order = AreaSelector.ORDER_RANDOM;
                break;
            case "arbitrary":
                this.order = AreaSelector.ORDER_ARBITRARY;
                break;
            default:
                this.reader.setCursor(i);
                throw EntitySelectorOptions.ERROR_SORT_UNKNOWN.createWithContext(this.reader, s);
        }
        this.sorted = true;
    }

    public void thenNBT() throws CommandSyntaxException {
        boolean invert = this.shouldInvertValue();
        CompoundTag nbt = new TagParser(this.reader).readStruct();
        this.addPredicate(area -> NbtUtils.compareNbt(nbt, area.getAttachedData(), true) != invert);
    }

    private CompletableFuture<Suggestions> suggestStart(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> builderConsumer) {
        fillSelectorSuggestions(builder);
        builder.add(builder);
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestSelector(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> builderConsumer) {
        SuggestionsBuilder suggestionsbuilder = builder.createOffset(builder.getStart() - 1);
        return this.suggestStart(suggestionsbuilder, builderConsumer);
    }

    private static void fillSelectorSuggestions(SuggestionsBuilder builder) {
        builder.suggest("@p", Component.translatable("loquat.argument.area.selector.p"));
        builder.suggest("@a", Component.translatable("loquat.argument.area.selector.a"));
        builder.suggest("@r", Component.translatable("loquat.argument.area.selector.r"));
        builder.suggest("@s", Component.translatable("loquat.argument.area.selector.s"));
    }

    private CompletableFuture<Suggestions> suggestOpenOptions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> builderConsumer) {
        builder.suggest(String.valueOf('['));
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestOptionsKeyOrClose(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> builderConsumer) {
        builder.suggest(String.valueOf(']'));
        AreaSelectorOptions.suggestNames(this, builder);
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestOptionsKey(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> builderConsumer) {
        AreaSelectorOptions.suggestNames(this, builder);
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestOptionsNextOrClose(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> builderConsumer) {
        builder.suggest(String.valueOf(','));
        builder.suggest(String.valueOf(']'));
        return builder.buildFuture();
    }

    public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> builderConsumer) {
        return (CompletableFuture<Suggestions>) this.suggestions.apply(builder.createOffset(this.reader.getCursor()), builderConsumer);
    }

    public StringReader getReader() {
        return this.reader;
    }

    public void setSuggestions(BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions) {
        this.suggestions = suggestions;
    }

    public boolean isLimited() {
        return this.limited;
    }

    public int getMaxResults() {
        return this.maxResults;
    }

    public boolean isSorted() {
        return this.sorted;
    }

    public MinMaxBounds.Doubles getDistance() {
        return this.distance;
    }

    public boolean isSelectedAreas() {
        return this.selectedAreas;
    }

    @Nullable
    public Double getX() {
        return this.x;
    }

    public void setX(@Nullable Double x) {
        this.x = x;
    }

    @Nullable
    public Double getY() {
        return this.y;
    }

    public void setY(@Nullable Double y) {
        this.y = y;
    }

    @Nullable
    public Double getZ() {
        return this.z;
    }

    public void setZ(@Nullable Double z) {
        this.z = z;
    }

    @Nullable
    public Double getDeltaX() {
        return this.deltaX;
    }

    public void setDeltaX(@Nullable Double deltaX) {
        this.deltaX = deltaX;
    }

    @Nullable
    public Double getDeltaY() {
        return this.deltaY;
    }

    public void setDeltaY(@Nullable Double deltaY) {
        this.deltaY = deltaY;
    }

    @Nullable
    public Double getDeltaZ() {
        return this.deltaZ;
    }

    public void setDeltaZ(@Nullable Double deltaZ) {
        this.deltaZ = deltaZ;
    }
}