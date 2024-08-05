package snownee.kiwi.customization.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import snownee.kiwi.customization.CustomizationClient;
import snownee.kiwi.customization.block.family.BlockFamilies;
import snownee.kiwi.customization.block.family.BlockFamily;
import snownee.kiwi.customization.network.CApplyBuilderRulePacket;
import snownee.kiwi.customization.network.CConvertItemPacket;
import snownee.kiwi.util.ClientProxy;
import snownee.kiwi.util.KHolder;

public class BuildersButton {

    private static final BuilderModePreview PREVIEW_RENDERER = new BuilderModePreview();

    private static boolean builderMode;

    public static BuilderModePreview getPreviewRenderer() {
        return PREVIEW_RENDERER;
    }

    public static boolean isBuilderModeOn() {
        if (builderMode && CustomizationClient.buildersButtonKey != null && CustomizationClient.buildersButtonKey.m_90862_()) {
            builderMode = false;
        }
        return builderMode;
    }

    public static boolean onLongPress() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && !player.m_5833_()) {
            builderMode = !builderMode;
            RandomSource random = RandomSource.create();
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, (random.nextFloat() - random.nextFloat()) * 0.35F + 0.9F));
            return true;
        } else {
            return false;
        }
    }

    public static boolean onDoublePress() {
        return false;
    }

    public static boolean onShortPress() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return false;
        } else {
            Screen screen = mc.screen;
            if (screen instanceof ConvertScreen) {
                screen.onClose();
                return true;
            } else {
                if (screen instanceof AbstractContainerScreen<?> containerScreen && containerScreen.getMenu().getCarried().isEmpty()) {
                    Slot slot = ClientProxy.getSlotUnderMouse(containerScreen);
                    if (slot != null && slot.hasItem() && slot.allowModification(player)) {
                        if (screen instanceof CreativeModeInventoryScreen && slot.container != player.m_150109_()) {
                            return false;
                        }
                        List<CConvertItemPacket.Group> groups = findConvertGroups(player, slot.getItem());
                        if (groups.isEmpty()) {
                            return false;
                        }
                        ClientProxy.pushScreen(mc, new ConvertScreen(screen, slot, slot.index, groups));
                        return true;
                    }
                    return false;
                }
                if (screen != null) {
                    return false;
                } else {
                    List<CConvertItemPacket.Group> groups = findConvertGroups(player, player.m_21205_());
                    if (!groups.isEmpty()) {
                        mc.setScreen(new ConvertScreen(null, null, player.m_150109_().selected, groups));
                        return true;
                    } else {
                        groups = findConvertGroups(player, player.m_21206_());
                        if (!groups.isEmpty()) {
                            mc.setScreen(new ConvertScreen(null, null, 40, groups));
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
    }

    public static List<CConvertItemPacket.Group> findConvertGroups(Player player, ItemStack itemStack) {
        List<KHolder<BlockFamily>> families = BlockFamilies.findQuickSwitch(itemStack.getItem(), player.isCreative());
        if (families.isEmpty()) {
            return List.of();
        } else {
            List<CConvertItemPacket.Group> groups = Lists.newArrayListWithExpectedSize(families.size());
            Set<Item> addedItems = Sets.newHashSet();
            float ratio = BlockFamilies.getConvertRatio(itemStack.getItem());
            for (KHolder<BlockFamily> family : families) {
                CConvertItemPacket.Group group = new CConvertItemPacket.Group();
                boolean cascading = family.value().switchAttrs().cascading();
                List<CConvertItemPacket.Entry> unresolved = (List<CConvertItemPacket.Entry>) (cascading ? Lists.newLinkedList() : List.of());
                Set<BlockFamily> iteratedFamilies = (Set<BlockFamily>) (cascading ? Sets.newHashSet(new BlockFamily[] { family.value() }) : Set.of());
                Set<Item> iteratedItems = (Set<Item>) (cascading ? Sets.newHashSet() : Set.of());
                for (Item item : family.value().items().toList()) {
                    float convertRatio = BlockFamilies.getConvertRatio(item);
                    CConvertItemPacket.Entry entry = new CConvertItemPacket.Entry(ratio / convertRatio);
                    Pair<KHolder<BlockFamily>, Item> pair = Pair.of(family, item);
                    entry.steps().add(pair);
                    if (cascading) {
                        unresolved.add(entry);
                        iteratedItems.add(item);
                    }
                    if (!addedItems.contains(item)) {
                        group.entries().add(entry);
                    }
                    addedItems.add(item);
                }
                while (!unresolved.isEmpty()) {
                    CConvertItemPacket.Entry parentEntry = (CConvertItemPacket.Entry) unresolved.remove(0);
                    Pair<KHolder<BlockFamily>, Item> lastStep = (Pair<KHolder<BlockFamily>, Item>) parentEntry.steps().get(parentEntry.steps().size() - 1);
                    Item lastItem = (Item) lastStep.getSecond();
                    ratio = BlockFamilies.getConvertRatio(lastItem);
                    for (KHolder<BlockFamily> nextFamily : BlockFamilies.findQuickSwitch(lastItem, player.isCreative())) {
                        if (iteratedFamilies.add(nextFamily.value())) {
                            for (Item nextItem : nextFamily.value().items().toList()) {
                                if (iteratedItems.add(nextItem)) {
                                    float convertRatiox = BlockFamilies.getConvertRatio(nextItem);
                                    CConvertItemPacket.Entry entryx = new CConvertItemPacket.Entry(parentEntry.ratio() * ratio / convertRatiox);
                                    entryx.steps().addAll(parentEntry.steps());
                                    entryx.steps().add(Pair.of(nextFamily, nextItem));
                                    if (!addedItems.contains(nextItem)) {
                                        group.entries().add(entryx);
                                        addedItems.add(nextItem);
                                    }
                                    if (entryx.steps().size() < 4 && nextFamily.value().switchAttrs().cascading()) {
                                        unresolved.add(entryx);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!group.entries().isEmpty()) {
                    groups.add(group);
                }
            }
            if (player.isCreative()) {
                return groups;
            } else {
                Predicate<CConvertItemPacket.Entry> filter = entryxx -> entryxx.ratio() >= 1.0F ? false : !player.getInventory().m_216874_($ -> $.is(entryx.item()));
                int count = 0;
                for (CConvertItemPacket.Group group : groups) {
                    group.entries().removeIf(filter);
                    count += group.entries().size();
                }
                if (count <= 1) {
                    return List.of();
                } else {
                    groups.removeIf(groupx -> groupx.entries().isEmpty());
                    return groups;
                }
            }
        }
    }

    public static boolean startDestroyBlock(BlockPos pos, Direction face) {
        LocalPlayer player = ensureBuilderMode();
        return player != null;
    }

    public static boolean performUseItemOn(InteractionHand hand, BlockHitResult hitResult) {
        LocalPlayer player = ensureBuilderMode();
        if (player == null) {
            return false;
        } else if (hand == InteractionHand.OFF_HAND) {
            return false;
        } else {
            BuilderModePreview preview = getPreviewRenderer();
            KHolder<BuilderRule> rule = preview.rule;
            BlockPos pos = preview.pos;
            List<BlockPos> positions = preview.positions;
            if (rule != null && !positions.isEmpty() && hitResult.getBlockPos().equals(pos)) {
                CApplyBuilderRulePacket.send(new UseOnContext(player, hand, hitResult), rule, positions);
                return true;
            } else {
                return true;
            }
        }
    }

    private static LocalPlayer ensureBuilderMode() {
        return !isBuilderModeOn() ? null : Minecraft.getInstance().player;
    }

    public static void renderDebugText(List<String> left, List<String> right) {
        if (isBuilderModeOn() && !Minecraft.getInstance().options.renderDebug) {
            left.add("Builder Mode is on, long press %s to toggle".formatted(CustomizationClient.buildersButtonKey.m_90863_().getString()));
        }
    }

    public static boolean cancelRenderHighlight() {
        return !getPreviewRenderer().positions.isEmpty();
    }
}