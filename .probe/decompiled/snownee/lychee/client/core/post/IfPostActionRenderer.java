package snownee.lychee.client.core.post;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.chat.Component;
import snownee.lychee.core.post.If;

public class IfPostActionRenderer implements PostActionRenderer<If> {

    public List<Component> getTooltips(If action) {
        List<Component> list = this.getBaseTooltips(action);
        action.getConsequenceTooltips(list, action.successEntries, "tip.lychee.ifSuccess");
        action.getConsequenceTooltips(list, action.failureEntries, "tip.lychee.ifFailure");
        return list;
    }

    public List<Component> getBaseTooltips(If action) {
        return Lists.newArrayList();
    }
}