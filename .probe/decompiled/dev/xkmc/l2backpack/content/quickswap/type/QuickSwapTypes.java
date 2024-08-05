package dev.xkmc.l2backpack.content.quickswap.type;

import java.util.ArrayList;
import java.util.List;

public class QuickSwapTypes {

    public static final ArrowSwapType ARROW = new ArrowSwapType("arrow", 0);

    public static final ToolSwapType TOOL = new ToolSwapType("tool", 1);

    public static final ArmorSwapType ARMOR = new ArmorSwapType("armor", 2);

    public static final List<MatcherSwapType> MATCHER = new ArrayList(List.of(ARROW));

    private static final List<QuickSwapType> LIST = new ArrayList(List.of(ARROW, TOOL, ARMOR));

    public static synchronized int register(QuickSwapType type) {
        int ans = LIST.size();
        LIST.add(type);
        if (type instanceof MatcherSwapType matcher) {
            MATCHER.add(matcher);
        }
        return ans;
    }
}