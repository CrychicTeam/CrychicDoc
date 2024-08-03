package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.OnInject;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

@SerialClass
public class ArtifactStats {

    @SerialField
    public ArtifactSlot slot;

    @SerialField
    public int rank;

    @SerialField
    public int level;

    @SerialField
    public int exp;

    @SerialField
    public int old_level;

    @SerialField
    public StatEntry main_stat;

    @SerialField
    public ArrayList<StatEntry> sub_stats = new ArrayList();

    public final Map<ResourceLocation, StatEntry> map = new HashMap();

    public static ArtifactStats generate(ArtifactSlot slot, int rank, Upgrade upgrade, RandomSource random) {
        ArtifactStats ans = new ArtifactStats(slot, rank);
        slot.generate(ans, upgrade, random);
        return ans;
    }

    @Deprecated
    public ArtifactStats() {
    }

    public ArtifactStats(ArtifactSlot slot, int rank) {
        this.slot = slot;
        this.rank = rank;
    }

    @OnInject
    public void onInject() {
        this.main_stat.init(this.slot);
        this.map.put(this.main_stat.type, this.main_stat);
        for (StatEntry ent : this.sub_stats) {
            ent.init(this.slot);
            this.map.put(ent.type, ent);
        }
    }

    public void add(StatEntry entry) {
        if (!this.map.containsKey(entry.type)) {
            if (this.main_stat == null) {
                this.main_stat = entry;
            } else {
                this.sub_stats.add(entry);
            }
            entry.init(this.slot);
            this.map.put(entry.type, entry);
        }
    }

    public void add(ResourceLocation type, double value) {
        if (this.map.containsKey(type)) {
            ((StatEntry) this.map.get(type)).addMultiplier(value);
        } else {
            this.add(new StatEntry(this.slot, type, value));
        }
    }

    public Multimap<Attribute, AttributeModifier> buildAttributes(String uuidBase) {
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        for (StatEntry ent : this.map.values()) {
            ent.getType().getModifier(builder, ent, UUID.nameUUIDFromBytes((uuidBase + ent.type).getBytes()));
        }
        return builder.build();
    }

    public void addExp(int exp, RandomSource random) {
        this.exp += exp;
        int max_level;
        for (max_level = ArtifactUpgradeManager.getMaxLevel(this.rank); this.level < max_level; this.level++) {
            int max_exp = ArtifactUpgradeManager.getExpForLevel(this.rank, this.level);
            if (this.exp < max_exp) {
                break;
            }
            this.exp -= max_exp;
        }
        if (this.level == max_level) {
            this.exp = 0;
        }
    }
}