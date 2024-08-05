package mezz.jei.gui.search;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.config.IIngredientFilterConfig;
import mezz.jei.common.util.Translator;
import mezz.jei.core.search.LimitedStringStorage;
import mezz.jei.core.search.PrefixInfo;
import mezz.jei.core.search.SearchMode;
import mezz.jei.core.search.suffixtree.GeneralizedSuffixTree;
import mezz.jei.gui.ingredients.IListElementInfo;

public class ElementPrefixParser {

    public static final PrefixInfo<IListElementInfo<?>> NO_PREFIX = new PrefixInfo<>('\u0000', () -> SearchMode.ENABLED, i -> List.of(i.getName()), GeneralizedSuffixTree::new);

    private final Char2ObjectMap<PrefixInfo<IListElementInfo<?>>> map = new Char2ObjectOpenHashMap();

    public ElementPrefixParser(IIngredientManager ingredientManager, IIngredientFilterConfig config, IColorHelper colorHelper) {
        this.addPrefix(new PrefixInfo<>('@', config::getModNameSearchMode, IListElementInfo::getModNameStrings, LimitedStringStorage::new));
        this.addPrefix(new PrefixInfo<>('#', config::getTooltipSearchMode, e -> e.getTooltipStrings(config, ingredientManager), GeneralizedSuffixTree::new));
        this.addPrefix(new PrefixInfo<>('$', config::getTagSearchMode, e -> e.getTagStrings(ingredientManager), LimitedStringStorage::new));
        this.addPrefix(new PrefixInfo<>('^', config::getColorSearchMode, e -> {
            Iterable<Integer> colors = e.getColors(ingredientManager);
            return StreamSupport.stream(colors.spliterator(), false).map(colorHelper::getClosestColorName).map(Translator::toLowercaseWithLocale).distinct().toList();
        }, LimitedStringStorage::new));
        this.addPrefix(new PrefixInfo<>('&', config::getResourceLocationSearchMode, element -> List.of(element.getResourceLocation().toString()), GeneralizedSuffixTree::new));
    }

    private void addPrefix(PrefixInfo<IListElementInfo<?>> info) {
        this.map.put(info.getPrefix(), info);
    }

    public Collection<PrefixInfo<IListElementInfo<?>>> allPrefixInfos() {
        Collection<PrefixInfo<IListElementInfo<?>>> values = new ArrayList(this.map.values());
        values.add(NO_PREFIX);
        return values;
    }

    public Optional<ElementPrefixParser.TokenInfo> parseToken(String token) {
        if (token.isEmpty()) {
            return Optional.empty();
        } else {
            char firstChar = token.charAt(0);
            PrefixInfo<IListElementInfo<?>> prefixInfo = (PrefixInfo<IListElementInfo<?>>) this.map.get(firstChar);
            if (prefixInfo == null || prefixInfo.getMode() == SearchMode.DISABLED) {
                return Optional.of(new ElementPrefixParser.TokenInfo(token, NO_PREFIX));
            } else {
                return token.length() == 1 ? Optional.empty() : Optional.of(new ElementPrefixParser.TokenInfo(token.substring(1), prefixInfo));
            }
        }
    }

    public static record TokenInfo(String token, PrefixInfo<IListElementInfo<?>> prefixInfo) {
    }
}