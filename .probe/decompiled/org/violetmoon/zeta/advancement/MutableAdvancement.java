package org.violetmoon.zeta.advancement;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import org.violetmoon.zeta.api.IMutableAdvancement;

public class MutableAdvancement implements IMutableAdvancement {

    final Advancement advancement;

    public Map<String, Criterion> criteria;

    public List<List<String>> requirements;

    public MutableAdvancement(Advancement advancement) {
        this.advancement = advancement;
        this.mutabilize();
    }

    @Override
    public void addRequiredCriterion(String name, Criterion criterion) {
        this.criteria.put(name, criterion);
        this.requirements.add(Lists.newArrayList(new String[] { name }));
    }

    @Override
    public void addOrCriterion(String name, Criterion criterion) {
        this.criteria.put(name, criterion);
        ((List) this.requirements.get(0)).add(name);
    }

    @Override
    public Criterion getCriterion(String title) {
        return (Criterion) this.criteria.get(title);
    }

    private void mutabilize() {
        this.criteria = Maps.newHashMap(this.advancement.criteria);
        this.requirements = new ArrayList();
        String[][] arr = this.advancement.requirements;
        for (String[] req : arr) {
            List<String> reqList = new ArrayList(Arrays.asList(req));
            this.requirements.add(reqList);
        }
    }

    public void commit() {
        this.advancement.criteria = ImmutableMap.copyOf(this.criteria);
        List<String[]> requirementArrays = new ArrayList();
        for (List<String> list : this.requirements) {
            String[] arr = (String[]) list.toArray(new String[list.size()]);
            requirementArrays.add(arr);
        }
        String[][] arr = (String[][]) requirementArrays.toArray(new String[0][requirementArrays.size()]);
        this.advancement.requirements = arr;
    }
}