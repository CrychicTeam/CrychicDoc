package dev.xkmc.l2artifacts.content.config;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2library.capability.conditionals.Context;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

@SerialClass
public class ArtifactSetConfig extends BaseConfig {

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<ArtifactSet, ArrayList<ArtifactSetConfig.Entry>> map = new HashMap();

    public static ArtifactSetConfig getInstance() {
        return NetworkManager.ARTIFACT_SETS.getMerged();
    }

    public static ArtifactSetConfig construct(ArtifactSet set, Consumer<ArtifactSetConfig.SetBuilder> builder) {
        ArtifactSetConfig config = new ArtifactSetConfig();
        ArrayList<ArtifactSetConfig.Entry> list = new ArrayList();
        builder.accept((ArtifactSetConfig.SetBuilder) (count, effect) -> list.add(new ArtifactSetConfig.Entry(count, effect)));
        config.map.put(set, list);
        return config;
    }

    @Override
    protected void postMerge() {
        this.map.values().forEach(e -> e.sort(null));
        this.map.forEach((k, v) -> v.forEach(e -> e.validate(k)));
    }

    @SerialClass
    public static class Entry implements Comparable<ArtifactSetConfig.Entry>, Context {

        @SerialField
        public int count;

        @SerialField
        public SetEffect effect;

        private String name;

        public UUID[] id;

        @Deprecated
        public Entry() {
        }

        Entry(int count, SetEffect effect) {
            this.count = count;
            this.effect = effect;
        }

        public int compareTo(@NotNull ArtifactSetConfig.Entry o) {
            int ans = Integer.compare(this.count, o.count);
            return ans != 0 ? ans : this.effect.getID().compareTo(o.effect.getID());
        }

        public void validate(ArtifactSet set) {
            String str = set.getID() + "_" + this.effect.getID();
            this.name = RegistrateLangProvider.toEnglishName(this.effect.getRegistryName().getPath());
            this.id = new UUID[this.effect.ids];
            for (int i = 0; i < this.effect.ids; i++) {
                this.id[i] = MathHelper.getUUIDFromString(str + "_" + i);
            }
        }

        public String getName() {
            return this.name;
        }
    }

    public interface SetBuilder {

        void add_impl(int var1, SetEffect var2);

        default ArtifactSetConfig.SetBuilder add(int count, SetEffect effect) {
            this.add_impl(count, effect);
            return this;
        }
    }
}