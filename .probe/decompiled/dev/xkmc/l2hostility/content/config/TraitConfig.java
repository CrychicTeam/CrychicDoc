package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

@SerialClass
public class TraitConfig extends BaseConfig {

    public static final TraitConfig DEFAULT = new TraitConfig(new ResourceLocation("l2hostility", "default"), 10, 100, 1, 10);

    @SerialField
    public int min_level;

    @SerialField
    public int cost;

    @SerialField
    public int max_rank;

    @SerialField
    public int weight;

    @Deprecated
    public TraitConfig() {
    }

    public TraitConfig(ResourceLocation rl, int cost, int weight, int maxRank, int minLevel) {
        this.id = rl;
        this.cost = cost;
        this.weight = weight;
        this.max_rank = maxRank;
        this.min_level = minLevel;
        this.addBlacklist(e -> {
        });
        this.addWhitelist(e -> {
        });
    }

    public TagKey<EntityType<?>> getBlacklistTag() {
        assert this.id != null;
        ResourceLocation tag = new ResourceLocation(this.id.getNamespace(), this.id.getPath() + "_blacklist");
        return TagKey.create(Registries.ENTITY_TYPE, tag);
    }

    public TagKey<EntityType<?>> getWhitelistTag() {
        assert this.id != null;
        ResourceLocation tag = new ResourceLocation(this.id.getNamespace(), this.id.getPath() + "_whitelist");
        return TagKey.create(Registries.ENTITY_TYPE, tag);
    }

    public TraitConfig addWhitelist(Consumer<IntrinsicHolderTagsProvider.IntrinsicTagAppender<EntityType<?>>> pvd) {
        TagKey<EntityType<?>> tag = this.getWhitelistTag();
        LHTagGen.ENTITY_TAG_BUILDER.put(tag.location(), (Consumer) e -> pvd.accept(e.addTag(tag)));
        return this;
    }

    public TraitConfig addBlacklist(Consumer<IntrinsicHolderTagsProvider.IntrinsicTagAppender<EntityType<?>>> pvd) {
        TagKey<EntityType<?>> tag = this.getBlacklistTag();
        LHTagGen.ENTITY_TAG_BUILDER.put(tag.location(), (Consumer) e -> pvd.accept(e.addTag(tag)));
        return this;
    }

    public boolean allows(EntityType<?> type) {
        TagKey<EntityType<?>> blacklist = this.getBlacklistTag();
        TagKey<EntityType<?>> whitelist = this.getWhitelistTag();
        ITagManager<EntityType<?>> manager = ForgeRegistries.ENTITY_TYPES.tags();
        assert manager != null;
        boolean def = true;
        if (!manager.getTag(whitelist).isEmpty()) {
            if (type.is(whitelist)) {
                return true;
            }
            def = false;
        }
        if (!manager.getTag(blacklist).isEmpty()) {
            if (type.is(blacklist)) {
                return false;
            }
            def = true;
        }
        return def;
    }
}