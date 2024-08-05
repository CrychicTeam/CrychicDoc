package snownee.kiwi.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import snownee.kiwi.Kiwi;
import snownee.kiwi.KiwiClientConfig;
import snownee.kiwi.config.KiwiConfigManager;
import snownee.kiwi.item.ModItem;

public final class TooltipEvents {

    public static final String disableDebugTooltipCommand = "@kiwi disable debugTooltip";

    private static final TooltipEvents.DebugTooltipCache cache = new TooltipEvents.DebugTooltipCache();

    private static boolean firstSeenDebugTooltip = true;

    private static long latestPressF3;

    private static boolean holdAlt;

    private static long holdAltStart;

    private static boolean showTagsBeforeAlt;

    private TooltipEvents() {
    }

    public static void globalTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag flag) {
        if (KiwiClientConfig.globalTooltip) {
            ModItem.addTip(stack, tooltip, flag);
        }
    }

    public static void debugTooltip(ItemStack itemStack, List<Component> tooltip, TooltipFlag flag) {
        if (Kiwi.areTagsUpdated() && flag.isAdvanced()) {
            CompoundTag nbt = itemStack.getTag();
            Minecraft mc = Minecraft.getInstance();
            long millis = Util.getMillis();
            if (mc.player != null && millis - latestPressF3 > 500L && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 292)) {
                latestPressF3 = millis;
                MutableComponent component = Component.literal(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString());
                mc.keyboardHandler.setClipboard(component.getString());
                if (nbt != null) {
                    component.append(NbtUtils.toPrettyComponent(nbt));
                }
                component.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, component.getString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click"))).withInsertion(component.getString()));
                mc.player.displayClientMessage(component, false);
                mc.options.renderDebug = !mc.options.renderDebug;
            }
            if (KiwiClientConfig.nbtTooltip && Screen.hasShiftDown() && nbt != null) {
                trySendTipMsg(mc);
                tooltip.removeIf(c -> c.getContents() instanceof TranslatableContents && "item.nbt_tags".equals(((TranslatableContents) c.getContents()).getKey()));
                if (cache.nbt != nbt) {
                    cache.nbt = nbt;
                    cache.formattedNbt = NbtUtils.toPrettyComponent(cache.nbt);
                }
                tooltip.add(cache.formattedNbt);
            } else if (KiwiClientConfig.tagsTooltip) {
                cache.maybeUpdateTags(itemStack);
                boolean alt = Screen.hasAltDown();
                if (!holdAlt && alt) {
                    holdAltStart = millis;
                    showTagsBeforeAlt = cache.showTags;
                } else if (holdAlt && !alt && cache.showTags && millis - holdAltStart < 500L) {
                    cache.pageNow = cache.pageNow + (Screen.hasControlDown() ? -1 : 1);
                    cache.needUpdatePreferredType = true;
                }
                if (alt && millis - holdAltStart >= 500L) {
                    cache.showTags = !showTagsBeforeAlt;
                    cache.lastShowTags = millis;
                }
                holdAlt = alt;
                if (!cache.pages.isEmpty()) {
                    trySendTipMsg(mc);
                    cache.appendTagsTooltip(tooltip);
                }
            }
        }
    }

    private static void trySendTipMsg(Minecraft mc) {
        if (firstSeenDebugTooltip && mc.player != null) {
            firstSeenDebugTooltip = false;
            if (KiwiClientConfig.debugTooltipMsg) {
                MutableComponent clickHere = Component.translatable("tip.kiwi.click_here").withStyle($ -> $.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "@kiwi disable debugTooltip")));
                mc.player.sendSystemMessage(Component.translatable("tip.kiwi.debug_tooltip", clickHere.withStyle(ChatFormatting.AQUA)));
                KiwiClientConfig.debugTooltipMsg = false;
                KiwiConfigManager.getHandler(KiwiClientConfig.class).save();
            }
        }
    }

    private static class DebugTooltipCache {

        private final List<String> pageTypes = Lists.newArrayList();

        private final List<List<String>> pages = Lists.newArrayList();

        private int pageNow = 0;

        private ItemStack itemStack = ItemStack.EMPTY;

        private CompoundTag nbt;

        private Component formattedNbt;

        private boolean showTags;

        private long lastShowTags;

        private String preferredType;

        public boolean needUpdatePreferredType;

        public void maybeUpdateTags(ItemStack itemStack) {
            if (this.itemStack != itemStack) {
                this.itemStack = itemStack;
                this.pages.clear();
                this.pageTypes.clear();
                this.pageNow = 0;
                this.addPages("item", itemStack.getTags().map(TagKey::f_203868_));
                Item item = itemStack.getItem();
                Block block = Block.byItem(item);
                if (block != Blocks.AIR) {
                    this.addPages("block", getTags(BuiltInRegistries.BLOCK, block));
                }
                if (item instanceof SpawnEggItem spawnEggItem) {
                    EntityType<?> type = spawnEggItem.getType(itemStack.getTag());
                    this.addPages("entity_type", getTags(BuiltInRegistries.ENTITY_TYPE, type));
                } else if (item instanceof BucketItem bucketItem) {
                    this.addPages("fluid", getTags(BuiltInRegistries.FLUID, bucketItem.getFluid()));
                }
                for (int i = 0; i < this.pages.size(); i++) {
                    if (((String) this.pageTypes.get(i)).equals(this.preferredType)) {
                        this.pageNow = i;
                        break;
                    }
                }
            }
        }

        private static <T> Stream<ResourceLocation> getTags(Registry<T> registry, T object) {
            return registry.getResourceKey(object).flatMap(registry::m_203636_).stream().flatMap(Holder::m_203616_).map(TagKey::f_203868_);
        }

        public void addPages(String type, Stream<ResourceLocation> stream) {
            List<String> tags = stream.map(Object::toString).sorted().toList();
            if (!tags.isEmpty()) {
                int i = 0;
                List<String> page = Lists.newArrayList();
                for (String tag : tags) {
                    page.add("#" + tag);
                    if (++i == KiwiClientConfig.tagsTooltipTagsPerPage) {
                        this.pages.add(page);
                        this.pageTypes.add(type);
                        page = Lists.newArrayList();
                        i = 0;
                    }
                }
                if (!page.isEmpty()) {
                    this.pages.add(page);
                    this.pageTypes.add(type);
                }
            }
        }

        public void appendTagsTooltip(List<Component> tooltip) {
            if (!this.pages.isEmpty()) {
                if (this.showTags && Util.getMillis() - this.lastShowTags > 60000L) {
                    this.showTags = false;
                }
                if (!this.showTags) {
                    if (KiwiClientConfig.tagsTooltipAppendKeybindHint) {
                        this.findIdLine(tooltip, i -> tooltip.set(i, ((Component) tooltip.get(i)).copy().append(" (alt)")));
                    }
                } else {
                    this.lastShowTags = Util.getMillis();
                    List<Component> sub = Lists.newArrayList();
                    this.pageNow = Math.floorMod(this.pageNow, this.pages.size());
                    if (this.needUpdatePreferredType) {
                        this.needUpdatePreferredType = false;
                        this.preferredType = (String) this.pageTypes.get(this.pageNow);
                    }
                    for (String tag : (List) this.pages.get(this.pageNow)) {
                        sub.add(Component.literal(tag).withStyle(ChatFormatting.DARK_GRAY));
                    }
                    int index = this.findIdLine(tooltip, i -> {
                        String type = (String) this.pageTypes.get(this.pageNow);
                        tooltip.set(i, ((Component) tooltip.get(i)).copy().append(" (%s/%s...%s)".formatted(this.pageNow + 1, this.pages.size(), type)));
                    });
                    index = index == -1 ? tooltip.size() : index + 1;
                    tooltip.addAll(index, sub);
                }
            }
        }

        private int findIdLine(List<Component> tooltip, IntConsumer consumer) {
            String id = BuiltInRegistries.ITEM.getKey(this.itemStack.getItem()).toString();
            for (int i = 0; i < tooltip.size(); i++) {
                Component component = (Component) tooltip.get(i);
                if (component.getString().equals(id)) {
                    consumer.accept(i);
                    return i;
                }
            }
            return -1;
        }
    }
}