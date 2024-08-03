package dev.xkmc.l2artifacts.content.core;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2artifacts.content.client.tab.DarkTextColorRanks;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PlayerOnlySetEffect;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.events.ArtifactEffectEvents;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.util.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class ArtifactSet extends NamedEntry<ArtifactSet> implements IArtifactFeature.ItemIcon {

    private static int[] remapRanks(int[] rank) {
        for (int i = rank.length - 2; i >= 0; i--) {
            rank[i] += rank[i + 1];
        }
        int[] ranks = new int[6];
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j < rank.length; j++) {
                if (rank[j] >= i) {
                    ranks[i] = Math.max(ranks[i], j);
                }
            }
        }
        return ranks;
    }

    private MutableComponent getCountDesc(int count) {
        int max = ((SetEntry) L2Artifacts.REGISTRATE.SET_MAP.get(this.getRegistryName())).items.length;
        return LangData.getTranslate("set." + count, max);
    }

    public ArtifactSet() {
        super(ArtifactTypeRegistry.SET);
    }

    public Optional<ArtifactSet.SetContext> getCountAndIndex(@Nullable SlotContext context) {
        LivingEntity e = (LivingEntity) (context == null ? Proxy.getPlayer() : context.entity());
        Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(e).resolve();
        if (opt.isPresent()) {
            List<SlotResult> list = ((ICuriosItemHandler) opt.get()).findCurios(stack -> {
                if (stack.getItem() instanceof BaseArtifact artifactx && artifactx.set.get() == this) {
                    return true;
                }
                return false;
            });
            int[] rank = new int[ArtifactConfig.COMMON.maxRank.get() + 1];
            int index = -1;
            int count = 0;
            for (SlotResult result : list) {
                Item var11 = result.stack().getItem();
                if (var11 instanceof BaseArtifact) {
                    BaseArtifact artifact = (BaseArtifact) var11;
                    if (artifact.set.get() == this) {
                        rank[artifact.rank]++;
                        if (context != null && context.identifier().equals(result.slotContext().identifier()) && context.index() == result.slotContext().index()) {
                            index = count;
                        }
                        count++;
                    }
                }
            }
            return Optional.of(new ArtifactSet.SetContext(list.size(), remapRanks(rank), index));
        } else {
            return Optional.empty();
        }
    }

    public Optional<ArtifactSet.SetContext> getSetCount(LivingEntity e) {
        Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(e).resolve();
        if (opt.isPresent()) {
            List<SlotResult> list = ((ICuriosItemHandler) opt.get()).findCurios(stack -> {
                if (stack.getItem() instanceof BaseArtifact artifactx && artifactx.set.get() == this) {
                    return true;
                }
                return false;
            });
            int[] rank = new int[ArtifactConfig.COMMON.maxRank.get() + 1];
            int count = 0;
            for (SlotResult result : list) {
                Item var9 = result.stack().getItem();
                if (var9 instanceof BaseArtifact) {
                    BaseArtifact artifact = (BaseArtifact) var9;
                    if (artifact.set.get() == this) {
                        rank[artifact.rank]++;
                        count++;
                    }
                }
            }
            return Optional.of(new ArtifactSet.SetContext(list.size(), remapRanks(rank), -1));
        } else {
            return Optional.empty();
        }
    }

    public void update(SlotContext context) {
        LivingEntity e = context.entity();
        Optional<ArtifactSet.SetContext> result = this.getCountAndIndex(context);
        if (result.isPresent()) {
            ArtifactSetConfig config = ArtifactSetConfig.getInstance();
            for (ArtifactSetConfig.Entry ent : (ArrayList) config.map.get(this)) {
                ent.effect.update(e, ent, ((ArtifactSet.SetContext) result.get()).ranks()[ent.count], ((ArtifactSet.SetContext) result.get()).count() >= ent.count);
            }
        }
    }

    public void tick(SlotContext context) {
        LivingEntity e = context.entity();
        Optional<ArtifactSet.SetContext> result = this.getCountAndIndex(context);
        if (result.isPresent() && ((ArtifactSet.SetContext) result.get()).current_index() == 0) {
            ArtifactSetConfig config = ArtifactSetConfig.getInstance();
            for (ArtifactSetConfig.Entry ent : (ArrayList) config.map.get(this)) {
                ent.effect.tick(e, ent, ((ArtifactSet.SetContext) result.get()).ranks()[ent.count], ((ArtifactSet.SetContext) result.get()).count() >= ent.count);
            }
        }
    }

    public <T> void propagateEvent(SlotContext context, T event, ArtifactEffectEvents.EventConsumer<T> cons) {
        LivingEntity e = context.entity();
        Optional<ArtifactSet.SetContext> result = this.getCountAndIndex(context);
        if (result.isPresent() && ((ArtifactSet.SetContext) result.get()).current_index() == 0) {
            ArtifactSetConfig config = ArtifactSetConfig.getInstance();
            for (ArtifactSetConfig.Entry ent : (ArrayList) config.map.get(this)) {
                if (((ArtifactSet.SetContext) result.get()).count() >= ent.count) {
                    cons.apply(ent.effect, e, ent, ((ArtifactSet.SetContext) result.get()).ranks()[ent.count], event);
                }
            }
        }
    }

    public <T> boolean propagateEvent(SlotContext context, T event, ArtifactEffectEvents.EventPredicate<T> cons) {
        LivingEntity e = context.entity();
        boolean ans = false;
        Optional<ArtifactSet.SetContext> result = this.getCountAndIndex(context);
        if (result.isPresent() && ((ArtifactSet.SetContext) result.get()).current_index() == 0) {
            ArtifactSetConfig config = ArtifactSetConfig.getInstance();
            for (ArtifactSetConfig.Entry ent : (ArrayList) config.map.get(this)) {
                if (((ArtifactSet.SetContext) result.get()).count() >= ent.count) {
                    ans |= cons.apply(ent.effect, e, ent, ((ArtifactSet.SetContext) result.get()).ranks()[ent.count], event);
                }
            }
        }
        return ans;
    }

    public List<MutableComponent> getAllDescs(ItemStack stack, boolean show) {
        List<MutableComponent> ans = new ArrayList();
        BaseArtifact artifact = (BaseArtifact) stack.getItem();
        ArtifactSetConfig config = ArtifactSetConfig.getInstance();
        ArrayList<ArtifactSetConfig.Entry> list = (ArrayList<ArtifactSetConfig.Entry>) config.map.get(this);
        if (Proxy.getPlayer() != null) {
            Optional<ArtifactSet.SetContext> opt = this.getSetCount(Proxy.getPlayer());
            if (opt.isPresent()) {
                ArtifactSet.SetContext ctx = (ArtifactSet.SetContext) opt.get();
                ans.add(LangData.SET.get(Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.YELLOW)));
                if (show) {
                    for (ArtifactSetConfig.Entry ent : list) {
                        ChatFormatting color_count = ctx.count() < ent.count ? ChatFormatting.GRAY : ChatFormatting.GREEN;
                        ChatFormatting color_title = ctx.count() >= ent.count && ctx.ranks()[ent.count] >= artifact.rank ? ChatFormatting.GREEN : ChatFormatting.GRAY;
                        ChatFormatting color_desc = ctx.count() >= ent.count && ctx.ranks()[ent.count] >= artifact.rank ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_GRAY;
                        ans.add(this.getCountDesc(ent.count).withStyle(color_count).append(ent.effect.getDesc().withStyle(color_title)));
                        for (MutableComponent comp : ent.effect.getDetailedDescription(artifact.rank)) {
                            ans.add(comp.withStyle(color_desc));
                        }
                    }
                }
            }
        }
        if (!show) {
            boolean playerOnly = false;
            for (ArtifactSetConfig.Entry e : list) {
                playerOnly |= e.effect instanceof PlayerOnlySetEffect;
            }
            if (playerOnly) {
                ans.add(LangData.PLAYER_ONLY.get().withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        return ans;
    }

    public List<Pair<List<Component>, List<Component>>> addComponents(ArtifactSet.SetContext ctx) {
        List<Pair<List<Component>, List<Component>>> ans = new ArrayList();
        ans.add(Pair.of(List.of(LangData.ALL_SET_EFFECTS.get(this.getDesc(), ctx.count())), List.of()));
        ArtifactSetConfig config = ArtifactSetConfig.getInstance();
        for (ArtifactSetConfig.Entry ent : (ArrayList) config.map.get(this)) {
            if (ctx.count() >= ent.count) {
                int rank = ctx.ranks[ent.count];
                List<Component> a = List.of(this.getCountDesc(ent.count), ent.effect.getDesc().withStyle(DarkTextColorRanks.getDark(rank)));
                List<Component> b = new ArrayList();
                b.add(this.getCountDesc(ent.count).append(ent.effect.getDesc().withStyle(DarkTextColorRanks.getLight(rank))));
                b.addAll(ent.effect.getDetailedDescription(rank).stream().map(comp -> comp.withStyle(DarkTextColorRanks.getDark(rank))).toList());
                ans.add(Pair.of(a, b));
            }
        }
        return ans;
    }

    @Override
    public Item getItemIcon() {
        ItemEntry<BaseArtifact>[][] arr = ((SetEntry) L2Artifacts.REGISTRATE.SET_MAP.get(this.getRegistryName())).items;
        return (Item) arr[0][arr[0].length - 1].get();
    }

    @Nullable
    @Override
    public NonNullList<ItemStack> getTooltipItems() {
        ItemEntry<BaseArtifact>[][] arr = ((SetEntry) L2Artifacts.REGISTRATE.SET_MAP.get(this.getRegistryName())).items;
        NonNullList<ItemStack> ans = NonNullList.create();
        for (ItemEntry<BaseArtifact>[] ar : arr) {
            ans.add(ar[ar.length - 1].asStack());
        }
        return ans;
    }

    public static record SetContext(int count, int[] ranks, int current_index) {
    }
}