package mezz.jei.gui.ingredients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.config.DebugConfig;
import mezz.jei.common.config.IClientConfig;
import mezz.jei.common.config.IIngredientFilterConfig;
import mezz.jei.gui.filter.IFilterTextSource;
import mezz.jei.gui.overlay.IIngredientGridSource;
import mezz.jei.gui.search.ElementPrefixParser;
import mezz.jei.gui.search.ElementSearch;
import mezz.jei.gui.search.ElementSearchLowMem;
import mezz.jei.gui.search.IElementSearch;
import net.minecraft.core.NonNullList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class IngredientFilter implements IIngredientGridSource, IIngredientManager.IIngredientListener {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Pattern QUOTE_PATTERN = Pattern.compile("\"");

    private static final Pattern FILTER_SPLIT_PATTERN = Pattern.compile("(-?\".*?(?:\"|$)|\\S+)");

    private final IFilterTextSource filterTextSource;

    private final IIngredientManager ingredientManager;

    private final IIngredientSorter sorter;

    private final IModIdHelper modIdHelper;

    private final IIngredientVisibility ingredientVisibility;

    private final IElementSearch elementSearch;

    private final ElementPrefixParser elementPrefixParser;

    private final Set<String> modNamesForSorting = new HashSet();

    @Nullable
    private List<ITypedIngredient<?>> ingredientListCached;

    private final List<IIngredientGridSource.SourceListChangedListener> listeners = new ArrayList();

    public IngredientFilter(IFilterTextSource filterTextSource, IClientConfig clientConfig, IIngredientFilterConfig config, IIngredientManager ingredientManager, IIngredientSorter sorter, NonNullList<IListElement<?>> ingredients, IModIdHelper modIdHelper, IIngredientVisibility ingredientVisibility, IColorHelper colorHelper) {
        this.filterTextSource = filterTextSource;
        this.ingredientManager = ingredientManager;
        this.sorter = sorter;
        this.modIdHelper = modIdHelper;
        this.ingredientVisibility = ingredientVisibility;
        this.elementPrefixParser = new ElementPrefixParser(ingredientManager, config, colorHelper);
        if (clientConfig.isLowMemorySlowSearchEnabled()) {
            this.elementSearch = new ElementSearchLowMem();
        } else {
            this.elementSearch = new ElementSearch(this.elementPrefixParser);
        }
        LOGGER.info("Adding {} ingredients", ingredients.size());
        ingredients.stream().map(i -> ListElementInfo.create(i, ingredientManager, modIdHelper)).flatMap(Optional::stream).forEach(this::addIngredient);
        LOGGER.info("Added {} ingredients", ingredients.size());
        this.filterTextSource.addListener(filterText -> {
            this.ingredientListCached = null;
            this.notifyListenersOfChange();
        });
    }

    public <V> void addIngredient(IListElementInfo<V> info) {
        IListElement<V> element = info.getElement();
        this.updateHiddenState(element);
        this.elementSearch.add(info);
        String modNameForSorting = info.getModNameForSorting();
        this.modNamesForSorting.add(modNameForSorting);
        this.invalidateCache();
    }

    public void invalidateCache() {
        this.ingredientListCached = null;
        this.sorter.invalidateCache();
    }

    public <V> Optional<IListElementInfo<V>> searchForMatchingElement(IIngredientHelper<V> ingredientHelper, ITypedIngredient<V> typedIngredient) {
        V ingredient = typedIngredient.getIngredient();
        IIngredientType<V> type = typedIngredient.getType();
        Function<ITypedIngredient<V>, String> uidFunction = i -> ingredientHelper.getUniqueId((V) i.getIngredient(), UidContext.Ingredient);
        String ingredientUid = (String) uidFunction.apply(typedIngredient);
        String lowercaseDisplayName = DisplayNameUtil.getLowercaseDisplayNameForSearch(ingredient, ingredientHelper);
        ElementPrefixParser.TokenInfo tokenInfo = new ElementPrefixParser.TokenInfo(lowercaseDisplayName, ElementPrefixParser.NO_PREFIX);
        return this.elementSearch.getSearchResults(tokenInfo).stream().map(elementInfo -> checkForMatch(elementInfo, type, ingredientUid, uidFunction)).flatMap(Optional::stream).findFirst();
    }

    public void updateHidden() {
        boolean changed = false;
        for (IListElementInfo<?> info : this.elementSearch.getAllIngredients()) {
            IListElement<?> element = info.getElement();
            changed |= this.updateHiddenState(element);
        }
        if (changed) {
            this.ingredientListCached = null;
            this.notifyListenersOfChange();
        }
    }

    public <V> boolean updateHiddenState(IListElement<V> element) {
        ITypedIngredient<V> typedIngredient = element.getTypedIngredient();
        boolean visible = this.ingredientVisibility.isIngredientVisible(typedIngredient);
        if (element.isVisible() != visible) {
            element.setVisible(visible);
            return true;
        } else {
            return false;
        }
    }

    public <V> void onIngredientVisibilityChanged(ITypedIngredient<V> ingredient, boolean visible) {
        IIngredientType<V> ingredientType = ingredient.getType();
        IIngredientHelper<V> ingredientHelper = this.ingredientManager.getIngredientHelper(ingredientType);
        this.searchForMatchingElement(ingredientHelper, ingredient).ifPresent(matchingElementInfo -> {
            IListElement<V> element = matchingElementInfo.getElement();
            if (element.isVisible() != visible) {
                element.setVisible(visible);
                this.notifyListenersOfChange();
            }
        });
    }

    @Override
    public List<ITypedIngredient<?>> getIngredientList() {
        String filterText = this.filterTextSource.getFilterText();
        filterText = filterText.toLowerCase();
        if (this.ingredientListCached == null) {
            this.ingredientListCached = this.getIngredientListUncached(filterText);
        }
        return this.ingredientListCached;
    }

    @Unmodifiable
    public List<IListElementInfo<?>> getIngredientListPreSort(Comparator<IListElementInfo<?>> directComparator) {
        return this.elementSearch.getAllIngredients().stream().sorted(directComparator).toList();
    }

    public Set<String> getModNamesForSorting() {
        return Collections.unmodifiableSet(this.modNamesForSorting);
    }

    public <T> List<T> getFilteredIngredients(IIngredientType<T> ingredientType) {
        return this.getIngredientList().stream().map(i -> i.getIngredient(ingredientType)).flatMap(Optional::stream).toList();
    }

    private List<ITypedIngredient<?>> getIngredientListUncached(String filterText) {
        String[] filters = filterText.split("\\|");
        List<IngredientFilter.SearchTokens> searchTokens = Arrays.stream(filters).map(this::parseSearchTokens).filter(s -> !s.toSearch.isEmpty()).toList();
        Stream<IListElementInfo<?>> elementInfoStream;
        if (searchTokens.isEmpty()) {
            elementInfoStream = this.elementSearch.getAllIngredients().parallelStream();
        } else {
            elementInfoStream = searchTokens.stream().map(this::getSearchResults).flatMap(Collection::stream).distinct();
        }
        return elementInfoStream.filter(info -> info.getElement().isVisible()).sorted(this.sorter.getComparator(this, this.ingredientManager)).map(IListElementInfo::getTypedIngredient).toList();
    }

    private static <T> Optional<IListElementInfo<T>> checkForMatch(IListElementInfo<?> info, IIngredientType<T> ingredientType, String uid, Function<ITypedIngredient<T>, String> uidFunction) {
        return optionalCast(info, ingredientType).filter(cast -> {
            ITypedIngredient<T> typedIngredient = cast.getTypedIngredient();
            String elementUid = (String) uidFunction.apply(typedIngredient);
            return uid.equals(elementUid);
        });
    }

    private static <T> Optional<IListElementInfo<T>> optionalCast(IListElementInfo<?> info, IIngredientType<T> ingredientType) {
        ITypedIngredient<?> typedIngredient = info.getTypedIngredient();
        return typedIngredient.getType() == ingredientType ? Optional.of(info) : Optional.empty();
    }

    @Override
    public <V> void onIngredientsAdded(IIngredientHelper<V> ingredientHelper, Collection<ITypedIngredient<V>> ingredients) {
        for (ITypedIngredient<V> value : ingredients) {
            Optional<IListElementInfo<V>> matchingElementInfo = this.searchForMatchingElement(ingredientHelper, value);
            if (matchingElementInfo.isPresent()) {
                IListElement<V> matchingElement = ((IListElementInfo) matchingElementInfo.get()).getElement();
                this.updateHiddenState(matchingElement);
                if (DebugConfig.isDebugModeEnabled()) {
                    LOGGER.debug("Updated ingredient: {}", ingredientHelper.getErrorInfo(value.getIngredient()));
                }
            } else {
                IListElement<V> element = IngredientListElementFactory.createOrderedElement(value);
                ListElementInfo.create(element, this.ingredientManager, this.modIdHelper).ifPresent(info -> {
                    this.addIngredient(info);
                    if (DebugConfig.isDebugModeEnabled()) {
                        LOGGER.debug("Added ingredient: {}", ingredientHelper.getErrorInfo(value.getIngredient()));
                    }
                });
            }
        }
        this.invalidateCache();
    }

    @Override
    public <V> void onIngredientsRemoved(IIngredientHelper<V> ingredientHelper, Collection<ITypedIngredient<V>> ingredients) {
        for (ITypedIngredient<V> typedIngredient : ingredients) {
            Optional<IListElementInfo<V>> matchingElementInfo = this.searchForMatchingElement(ingredientHelper, typedIngredient);
            if (matchingElementInfo.isEmpty()) {
                String errorInfo = ingredientHelper.getErrorInfo(typedIngredient.getIngredient());
                LOGGER.error("Could not find a matching ingredient to remove: {}", errorInfo);
            } else {
                if (DebugConfig.isDebugModeEnabled()) {
                    LOGGER.debug("Removed ingredient: {}", ingredientHelper.getErrorInfo(typedIngredient.getIngredient()));
                }
                IListElement<V> matchingElement = ((IListElementInfo) matchingElementInfo.get()).getElement();
                matchingElement.setVisible(false);
            }
        }
        this.invalidateCache();
    }

    private IngredientFilter.SearchTokens parseSearchTokens(String filterText) {
        IngredientFilter.SearchTokens searchTokens = new IngredientFilter.SearchTokens(new ArrayList(), new ArrayList());
        if (filterText.isEmpty()) {
            return searchTokens;
        } else {
            Matcher filterMatcher = FILTER_SPLIT_PATTERN.matcher(filterText);
            while (filterMatcher.find()) {
                String string = filterMatcher.group(1);
                boolean remove = string.startsWith("-");
                if (remove) {
                    string = string.substring(1);
                }
                string = QUOTE_PATTERN.matcher(string).replaceAll("");
                if (!string.isEmpty()) {
                    this.elementPrefixParser.parseToken(string).ifPresent(result -> {
                        if (remove) {
                            searchTokens.toRemove.add(result);
                        } else {
                            searchTokens.toSearch.add(result);
                        }
                    });
                }
            }
            return searchTokens;
        }
    }

    private Set<IListElementInfo<?>> getSearchResults(IngredientFilter.SearchTokens searchTokens) {
        List<Set<IListElementInfo<?>>> resultsPerToken = searchTokens.toSearch.stream().map(this.elementSearch::getSearchResults).toList();
        Set<IListElementInfo<?>> results = intersection(resultsPerToken);
        if (!results.isEmpty() && !searchTokens.toRemove.isEmpty()) {
            for (ElementPrefixParser.TokenInfo tokenInfo : searchTokens.toRemove) {
                Set<IListElementInfo<?>> resultsToRemove = this.elementSearch.getSearchResults(tokenInfo);
                results.removeAll(resultsToRemove);
                if (results.isEmpty()) {
                    break;
                }
            }
        }
        return results;
    }

    private static <T> Set<T> intersection(List<Set<T>> sets) {
        Set<T> smallestSet = (Set<T>) sets.stream().min(Comparator.comparing(Set::size)).orElseGet(Set::of);
        Set<T> results = Collections.newSetFromMap(new IdentityHashMap());
        results.addAll(smallestSet);
        for (Set<T> set : sets) {
            if (set != smallestSet && results.retainAll(set) && results.isEmpty()) {
                break;
            }
        }
        return results;
    }

    @Override
    public void addSourceListChangedListener(IIngredientGridSource.SourceListChangedListener listener) {
        this.listeners.add(listener);
    }

    private void notifyListenersOfChange() {
        for (IIngredientGridSource.SourceListChangedListener listener : this.listeners) {
            listener.onSourceListChanged();
        }
    }

    private static record SearchTokens(List<ElementPrefixParser.TokenInfo> toSearch, List<ElementPrefixParser.TokenInfo> toRemove) {
    }
}