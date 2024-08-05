package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@SerialClass
public class ArtifactChestToken implements IArtifactFilter {

    public final ItemStack stack;

    public final int invSlot;

    public final List<ItemStack> list;

    public final List<ArtifactFilter<?>> filters = new ArrayList();

    @SerialField
    public final ArtifactFilter<RankToken> rank;

    @SerialField
    public final ArtifactFilter<ArtifactSet> set;

    @SerialField
    public final ArtifactFilter<ArtifactSlot> slot;

    @SerialField
    public final ArtifactFilter<StatTypeConfig> stat;

    public int exp = 0;

    @Nullable
    private List<GenericItemStack<BaseArtifact>> cahce = null;

    public static ArtifactChestToken of(Player player, int invSlot) {
        ItemStack stack = player.getInventory().getItem(invSlot);
        List<ItemStack> list = ArtifactChestItem.getContent(stack);
        ArtifactChestToken ans = new ArtifactChestToken(stack, list, invSlot);
        TagCodec.fromTag(ArtifactChestItem.getFilter(stack), ArtifactChestToken.class, ans, e -> true);
        ans.exp = ArtifactChestItem.getExp(stack);
        return ans;
    }

    private ArtifactChestToken(ItemStack stack, List<ItemStack> list, int invSlot) {
        this.list = list;
        this.stack = stack;
        this.invSlot = invSlot;
        this.rank = this.addFilter(e -> new RankFilter(e, LangData.FILTER_RANK));
        this.set = this.addFilter(e -> new SimpleArtifactFilter<>(e, LangData.FILTER_SET, ArtifactTypeRegistry.SET.get(), i -> (ArtifactSet) i.set.get()));
        this.slot = this.addFilter(e -> new SimpleArtifactFilter<>(e, LangData.FILTER_SLOT, ArtifactTypeRegistry.SLOT.get(), i -> (ArtifactSlot) i.slot.get()));
        this.stat = this.addFilter(e -> new AttributeFilter(e, LangData.FILTER_STAT, StatTypeConfig.getValues()));
        TagCodec.fromTag(ArtifactChestItem.getFilter(stack), ArtifactChestToken.class, this, e -> true);
    }

    private <T extends IArtifactFeature> ArtifactFilter<T> addFilter(Function<IArtifactFilter, ArtifactFilter<T>> gen) {
        ArtifactFilter<T> ans = (ArtifactFilter<T>) gen.apply(this.filters.isEmpty() ? this : (IArtifactFilter) this.filters.get(this.filters.size() - 1));
        this.filters.add(ans);
        return ans;
    }

    @Override
    public void update() {
        this.cahce = null;
        this.rank.clearCache();
        this.set.clearCache();
        this.slot.clearCache();
        this.stat.clearCache();
    }

    public void prioritize(int ind) {
        ((ArtifactFilter) this.filters.get(ind)).sort_priority = 0;
        List<ArtifactFilter<?>> list = new ArrayList(this.filters);
        list.sort(Comparator.comparingInt(e -> e.sort_priority));
        for (int i = 0; i < list.size(); i++) {
            ((ArtifactFilter) list.get(i)).sort_priority = i + 1;
        }
    }

    @Override
    public Comparator<GenericItemStack<BaseArtifact>> getComparator() {
        ArrayList<ArtifactFilter<?>> list = new ArrayList(this.filters);
        list.sort(Comparator.comparingInt(ex -> ex.sort_priority));
        Comparator<GenericItemStack<BaseArtifact>> ans = null;
        assert !list.isEmpty();
        for (ArtifactFilter<?> e : list) {
            if (ans == null) {
                ans = e.getComparator();
            } else {
                ans = ans.thenComparing(e.getComparator());
            }
        }
        return ans;
    }

    @Deprecated
    @Override
    public Stream<GenericItemStack<BaseArtifact>> getAvailableArtifacts() {
        return this.list.stream().map(e -> new GenericItemStack<>((BaseArtifact) e.getItem(), e));
    }

    public List<GenericItemStack<BaseArtifact>> getFiltered() {
        if (this.cahce != null) {
            return this.cahce;
        } else {
            this.cahce = this.stat.getAvailableArtifacts().sorted(this.getComparator()).toList();
            return this.cahce;
        }
    }

    public void save() {
        ArtifactChestItem.setContent(this.stack, this.list);
    }
}