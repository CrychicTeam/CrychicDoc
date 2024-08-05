package dev.xkmc.l2archery.content.feature;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2archery.content.feature.core.PotionAggregator;
import dev.xkmc.l2archery.content.feature.core.PotionArrowFeature;
import dev.xkmc.l2archery.content.feature.core.StatFeature;
import dev.xkmc.l2archery.content.feature.types.DefaultShootFeature;
import dev.xkmc.l2archery.content.feature.types.FlightControlFeature;
import dev.xkmc.l2archery.content.feature.types.OnHitFeature;
import dev.xkmc.l2archery.content.feature.types.OnPullFeature;
import dev.xkmc.l2archery.content.feature.types.OnShootFeature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class FeatureList {

    public FeatureList.Stage stage = FeatureList.Stage.INHERENT;

    private final List<BowArrowFeature> all = new ArrayList();

    private final Map<Class<?>, BowArrowFeature> map = new HashMap();

    private final List<BowArrowFeature> inherent = new ArrayList();

    private final List<BowArrowFeature> upgrade = new ArrayList();

    private final List<BowArrowFeature> enchant = new ArrayList();

    private final List<OnPullFeature> pull = new ArrayList();

    private final List<OnShootFeature> shot = new ArrayList();

    private final List<OnHitFeature> hit = new ArrayList();

    private FlightControlFeature flight = null;

    public static boolean canMerge(FeatureList bow, FeatureList arrow) {
        if (bow.flight != null && arrow.flight != null) {
            return false;
        } else {
            Map<Class<?>, BowArrowFeature> map = new HashMap();
            for (List<BowArrowFeature> list : List.of(bow.all, arrow.all)) {
                for (BowArrowFeature feature : list) {
                    Class<?> cls = feature.getClass();
                    if (map.containsKey(cls) && !((BowArrowFeature) map.get(cls)).allowDuplicate()) {
                        return false;
                    }
                    map.put(cls, feature);
                }
            }
            return true;
        }
    }

    public static FeatureList merge(FeatureList bow, FeatureList arrow) {
        FeatureList ans = new FeatureList();
        ans.shot.add(DefaultShootFeature.INSTANCE);
        for (BowArrowFeature f : bow.inherent) {
            ans.add(f);
        }
        for (BowArrowFeature f : arrow.inherent) {
            ans.add(f);
        }
        ans.stage = FeatureList.Stage.UPGRADE;
        for (BowArrowFeature f : bow.upgrade) {
            ans.add(f);
        }
        ans.stage = FeatureList.Stage.ENCHANT;
        for (BowArrowFeature f : bow.enchant) {
            ans.add(f);
        }
        return ans;
    }

    public boolean allow(BowArrowFeature feature) {
        Class<?> cls = feature.getClass();
        return !this.map.containsKey(cls) || ((BowArrowFeature) this.map.get(cls)).allowDuplicate();
    }

    public FeatureList add(BowArrowFeature feature) {
        if (!this.allow(feature)) {
            return this;
        } else {
            this.map.put(feature.getClass(), feature);
            this.all.add(feature);
            List<BowArrowFeature> list = switch(this.stage) {
                case INHERENT ->
                    this.inherent;
                case UPGRADE ->
                    this.upgrade;
                case ENCHANT ->
                    this.enchant;
            };
            list.add(feature);
            if (feature instanceof OnPullFeature f) {
                this.pull.add(f);
            }
            if (feature instanceof OnShootFeature f) {
                this.shot.add(f);
            }
            if (this.flight == null && feature instanceof FlightControlFeature f) {
                this.flight = f;
            }
            if (feature instanceof OnHitFeature f) {
                this.hit.add(f);
            }
            return this;
        }
    }

    public void addEffectsTooltip(List<Component> list) {
        PotionAggregator agg = new PotionAggregator();
        for (BowArrowFeature f : this.all) {
            if (f instanceof PotionArrowFeature p) {
                agg.addAll(p.instances());
            }
        }
        PotionArrowFeature.addTooltip(agg.build(), list);
    }

    public void addTooltip(List<Component> list) {
        for (Pair<List<BowArrowFeature>, ChatFormatting> l : List.of(Pair.of(this.inherent, ChatFormatting.GREEN), Pair.of(this.upgrade, ChatFormatting.GOLD), Pair.of(this.enchant, ChatFormatting.LIGHT_PURPLE))) {
            for (BowArrowFeature f : (List) l.getFirst()) {
                if (!(f instanceof StatFeature)) {
                    List<MutableComponent> temp = new ArrayList();
                    f.addTooltip(temp);
                    for (MutableComponent c : temp) {
                        if (c.getStyle().getColor() == null) {
                            c.withStyle((ChatFormatting) l.getSecond());
                        }
                        list.add(c);
                    }
                }
            }
        }
    }

    public List<BowArrowFeature> all() {
        return this.all;
    }

    public List<OnPullFeature> pull() {
        return this.pull;
    }

    public List<OnShootFeature> shot() {
        return this.shot;
    }

    public FlightControlFeature flight() {
        return this.flight == null ? FlightControlFeature.INSTANCE : this.flight;
    }

    public List<OnHitFeature> hit() {
        return this.hit;
    }

    public static enum Stage {

        INHERENT, UPGRADE, ENCHANT
    }
}