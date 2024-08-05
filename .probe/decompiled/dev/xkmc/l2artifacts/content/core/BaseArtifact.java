package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;

public class BaseArtifact extends RankedItem {

    public static final String KEY = "ArtifactData";

    public static final String UPGRADE = "Upgrade";

    public final Supplier<ArtifactSet> set;

    public final Supplier<ArtifactSlot> slot;

    public static void upgrade(ItemStack stack, int exp, RandomSource random) {
        ItemCompoundTag tag = ItemCompoundTag.of(stack).getSubTag("ArtifactData");
        if (tag.isPresent()) {
            ArtifactStats stats = (ArtifactStats) TagCodec.fromTag(tag.getOrCreate(), ArtifactStats.class);
            assert stats != null;
            stats.addExp(exp, random);
            CompoundTag newTag = TagCodec.toTag(new CompoundTag(), stats);
            assert newTag != null;
            tag.setTag(newTag);
        }
    }

    public static Optional<ArtifactStats> getStats(ItemStack stack) {
        return CuriosApi.getCurio(stack).filter(e -> e instanceof ArtifactCurioCap).flatMap(e -> ((ArtifactCurioCap) e).getStats());
    }

    public static Optional<Upgrade> getUpgrade(ItemStack stack) {
        ItemCompoundTag tag = ItemCompoundTag.of(stack).getSubTag("Upgrade");
        return tag.isPresent() ? Optional.ofNullable((Upgrade) TagCodec.fromTag(tag.getOrCreate(), Upgrade.class)) : Optional.empty();
    }

    public static ItemStack setUpgrade(ItemStack stack, Upgrade upgrade) {
        CompoundTag tag = TagCodec.toTag(new CompoundTag(), upgrade);
        if (tag != null) {
            ItemCompoundTag.of(stack).getSubTag("Upgrade").setTag(tag);
        }
        return stack;
    }

    public BaseArtifact(Item.Properties properties, Supplier<ArtifactSet> set, Supplier<ArtifactSlot> slot, int rank) {
        super(properties.stacksTo(1), rank);
        this.set = set;
        this.slot = slot;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        return this.resolve(stack, level.isClientSide(), player.m_217043_());
    }

    public InteractionResultHolder<ItemStack> resolve(ItemStack stack, boolean isClient, RandomSource random) {
        ItemCompoundTag tag = ItemCompoundTag.of(stack).getSubTag("ArtifactData");
        Upgrade upgrade = (Upgrade) getUpgrade(stack).orElse(new Upgrade());
        if (!tag.isPresent()) {
            if (!isClient) {
                ArtifactStats stats = ArtifactStats.generate((ArtifactSlot) this.slot.get(), this.rank, upgrade, random);
                CompoundTag newTag = TagCodec.toTag(new CompoundTag(), stats);
                assert newTag != null;
                tag.setTag(newTag);
                setUpgrade(stack, upgrade);
            }
            return InteractionResultHolder.success(stack);
        } else {
            Optional<ArtifactStats> opt = getStats(stack);
            if (opt.isPresent()) {
                ArtifactStats stats = (ArtifactStats) opt.get();
                if (stats.level > stats.old_level) {
                    if (!isClient) {
                        for (int i = stats.old_level + 1; i <= stats.level; i++) {
                            ArtifactUpgradeManager.onUpgrade(stats, i, upgrade, random);
                        }
                        stats.old_level = stats.level;
                        CompoundTag newTag = TagCodec.toTag(new CompoundTag(), stats);
                        assert newTag != null;
                        tag.setTag(newTag);
                        setUpgrade(stack, upgrade);
                    }
                    return InteractionResultHolder.success(stack);
                }
            }
            return InteractionResultHolder.pass(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if (level != null && level.isClientSide()) {
            boolean shift = Screen.hasShiftDown();
            if (Proxy.getClientPlayer() != null) {
                ItemCompoundTag tag = ItemCompoundTag.of(stack).getSubTag("ArtifactData");
                if (!tag.isPresent()) {
                    list.add(LangData.RAW_ARTIFACT.get());
                } else {
                    getStats(stack).ifPresent(stats -> {
                        boolean max = stats.level == ArtifactUpgradeManager.getMaxLevel(stats.rank);
                        list.add(LangData.ARTIFACT_LEVEL.get(stats.level).withStyle(max ? ChatFormatting.GOLD : ChatFormatting.WHITE));
                        if (stats.level < ArtifactUpgradeManager.getMaxLevel(stats.rank) && shift) {
                            list.add(LangData.ARTIFACT_EXP.get(stats.exp, ArtifactUpgradeManager.getExpForLevel(stats.rank, stats.level)));
                        }
                        if (stats.level > stats.old_level) {
                            list.add(LangData.UPGRADE.get());
                        } else if (!shift) {
                            list.add(LangData.MAIN_STAT.get());
                            list.add(stats.main_stat.getTooltip());
                            if (!stats.sub_stats.isEmpty()) {
                                list.add(LangData.SUB_STAT.get());
                                for (StatEntry ent : stats.sub_stats) {
                                    list.add(ent.getTooltip());
                                }
                            }
                        }
                    });
                }
                list.addAll(((ArtifactSet) this.set.get()).getAllDescs(stack, shift));
                if (!shift) {
                    list.add(LangData.EXP_CONVERSION.get(ArtifactUpgradeManager.getExpForConversion(this.rank, (ArtifactStats) getStats(stack).orElse(null))));
                }
            }
            super.m_7373_(stack, level, list, flag);
            if (!shift) {
                list.add(LangData.SHIFT_TEXT.get());
            }
        }
    }

    @Nullable
    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, LazyOptional.of(() -> new ArtifactCurioCap(stack)));
            }
        };
    }
}