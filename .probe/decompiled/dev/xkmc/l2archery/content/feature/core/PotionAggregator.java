package dev.xkmc.l2archery.content.feature.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class PotionAggregator {

    private final Map<MobEffect, TreeMap<Integer, MobEffectInstance>> map = new LinkedHashMap();

    public void add(MobEffectInstance ins) {
        TreeMap<Integer, MobEffectInstance> sub = (TreeMap<Integer, MobEffectInstance>) this.map.computeIfAbsent(ins.getEffect(), e -> new TreeMap());
        MobEffectInstance old = (MobEffectInstance) sub.get(ins.getAmplifier());
        if (old != null) {
            if (old.getDuration() < ins.getDuration()) {
                sub.put(ins.getAmplifier(), ins);
            }
        } else {
            sub.put(ins.getAmplifier(), ins);
        }
    }

    public List<MobEffectInstance> build() {
        List<MobEffectInstance> ans = new ArrayList();
        for (Entry<MobEffect, TreeMap<Integer, MobEffectInstance>> ent : this.map.entrySet()) {
            TreeMap<Integer, MobEffectInstance> sub = (TreeMap<Integer, MobEffectInstance>) ent.getValue();
            int duration = 0;
            for (Entry<Integer, MobEffectInstance> ins : sub.descendingMap().entrySet()) {
                if (((MobEffectInstance) ins.getValue()).getDuration() > duration) {
                    ans.add((MobEffectInstance) ins.getValue());
                    duration = ((MobEffectInstance) ins.getValue()).getDuration();
                }
            }
        }
        return ans;
    }

    public void addAll(List<MobEffectInstance> instances) {
        for (MobEffectInstance ins : instances) {
            this.add(ins);
        }
    }
}