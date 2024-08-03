package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.core.StatEntry;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2serial.serialization.SerialClass;
import it.unimi.dsi.fastutil.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@SerialClass
public class AttributeFilter extends ArtifactFilter<StatTypeConfig> {

    public AttributeFilter(IArtifactFilter parent, LangData desc, Collection<StatTypeConfig> reg) {
        super(parent, desc, reg, (item, type) -> (Boolean) BaseArtifact.getStats(item.stack()).map(x -> x.map.containsKey(type.getID())).orElse(false));
    }

    @Override
    public Comparator<GenericItemStack<BaseArtifact>> getComparator() {
        Comparator<GenericItemStack<BaseArtifact>> ans = Comparator.comparingInt(e -> (Integer) BaseArtifact.getStats(e.stack()).map(x -> -this.item_priority[this.revMap.get(x.main_stat.getType())]).orElse(this.item_priority.length));
        List<Pair<StatTypeConfig, Integer>> list = new ArrayList(this.allEntries.stream().map(e -> Pair.of(e, (Integer) this.revMap.get(e))).filter(e -> this.getSelected((Integer) e.second())).toList());
        list.sort(Comparator.comparingInt(e -> this.item_priority[e.second()]));
        for (Pair<StatTypeConfig, Integer> p : list) {
            ans = ans.thenComparingDouble(e -> (Double) BaseArtifact.getStats(e.stack()).map(x -> (StatEntry) x.map.get(((StatTypeConfig) p.left()).getID())).map(s -> -s.getValue()).orElse(0.0));
        }
        return ans;
    }
}