package mezz.jei.gui.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mezz.jei.core.search.PrefixInfo;
import mezz.jei.gui.ingredients.IListElement;
import mezz.jei.gui.ingredients.IListElementInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ElementSearchLowMem implements IElementSearch {

    private static final Logger LOGGER = LogManager.getLogger();

    private final List<IListElementInfo<?>> elementInfoList = new ArrayList();

    @Override
    public Set<IListElementInfo<?>> getSearchResults(ElementPrefixParser.TokenInfo tokenInfo) {
        String token = tokenInfo.token();
        if (token.isEmpty()) {
            return Set.of();
        } else {
            PrefixInfo<IListElementInfo<?>> prefixInfo = tokenInfo.prefixInfo();
            return (Set<IListElementInfo<?>>) this.elementInfoList.stream().filter(elementInfo -> matches(token, prefixInfo, elementInfo)).collect(Collectors.toSet());
        }
    }

    private static boolean matches(String word, PrefixInfo<IListElementInfo<?>> prefixInfo, IListElementInfo<?> elementInfo) {
        IListElement<?> element = elementInfo.getElement();
        if (element.isVisible()) {
            for (String string : prefixInfo.getStrings(elementInfo)) {
                if (string.contains(word)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void add(IListElementInfo<?> info) {
        this.elementInfoList.add(info);
    }

    public List<IListElementInfo<?>> getAllIngredients() {
        return Collections.unmodifiableList(this.elementInfoList);
    }

    @Override
    public void logStatistics() {
        LOGGER.info("ElementSearchLowMem Element Count: {}", this.elementInfoList.size());
    }
}