package dev.xkmc.l2artifacts.content.search.shape;

import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.content.upgrades.UpgradeBoostItem;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.PredSlot;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ShapeMenu extends BaseContainerMenu<ShapeMenu> implements IFilterMenu {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "shape");

    public final ArtifactChestToken token;

    public final Player player;

    public static ShapeMenu fromNetwork(MenuType<ShapeMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        int i = buf.readInt();
        return new ShapeMenu(wid, plInv, ArtifactChestToken.of(plInv.player, i));
    }

    public ShapeMenu(int wid, Inventory plInv, ArtifactChestToken token) {
        super((MenuType<?>) ArtifactMenuRegistry.MT_SHAPE.get(), wid, plInv, MANAGER, e -> new BaseContainerMenu.BaseContainer<>(15, e).setMax(1), true);
        this.token = token;
        this.player = plInv.player;
        this.addResultSlot(ShapeSlots.OUTPUT.slot(), e -> false);
        this.addSlot(ShapeSlots.ARTIFACT_MAIN.slot(), e -> {
            if (e.getItem() instanceof BaseArtifact art) {
                int var4 = art.rank;
                Optional<ArtifactStats> opt = BaseArtifact.getStats(e);
                return opt.isEmpty() ? false : ((ArtifactStats) opt.get()).level == ArtifactUpgradeManager.getMaxLevel(var4);
            } else {
                return false;
            }
        });
        this.addSlot(ShapeSlots.BOOST_MAIN.slot(), e -> !this.getMainItem().isEmpty() && e.getItem() instanceof UpgradeBoostItem boost && boost.rank == ((BaseArtifact) this.getMainItem().getItem()).rank && boost.type == Upgrade.Type.BOOST_MAIN_STAT, s -> s.setInputLockPred(this::mainSlotsLocked));
        this.addSlot(ShapeSlots.ARTIFACT_SUB.slot(), this::isAllowedAsSubArtifact, (i, e) -> e.setInputLockPred(() -> this.subArtifactSlotLocked(i)));
        this.addSlot(ShapeSlots.STAT_SUB.slot(), (i, e) -> {
            ItemStack sub = ShapeSlots.ARTIFACT_SUB.get(this, i).m_7993_();
            if (sub.isEmpty()) {
                return false;
            } else {
                BaseArtifact item = (BaseArtifact) sub.getItem();
                if (e.getItem() != ArtifactItems.ITEM_STAT[item.rank - 1].get()) {
                    return false;
                } else {
                    Optional<ResourceLocation> statOpt = StatContainerItem.getType(e);
                    Optional<ArtifactStats> subOpt = BaseArtifact.getStats(sub);
                    if (statOpt.isEmpty()) {
                        return false;
                    } else {
                        return subOpt.isEmpty() ? false : ((ArtifactStats) subOpt.get()).main_stat.type.equals(statOpt.get());
                    }
                }
            }
        }, (i, s) -> s.setInputLockPred(() -> this.subMatSlotLocked(i)));
        this.addSlot(ShapeSlots.BOOST_SUB.slot(), (i, e) -> {
            ItemStack sub = ShapeSlots.ARTIFACT_SUB.get(this, i).m_7993_();
            if (sub.isEmpty()) {
                return false;
            } else {
                BaseArtifact item = (BaseArtifact) sub.getItem();
                return e.getItem() == ArtifactItems.ITEM_BOOST_SUB[item.rank - 1].get();
            }
        }, (i, s) -> s.setInputLockPred(() -> this.subMatSlotLocked(i)));
    }

    protected void addResultSlot(String name, Predicate<ItemStack> pred) {
        this.sprite.get().getSlot(name, (x, y) -> new ShapeResultSlot(this.container, this.added++, x, y, pred), (x$0, x$1, x$2, x$3) -> this.addSlot(x$0, x$1, x$2, x$3));
    }

    public PredSlot getAsPredSlot(ShapeSlots slot) {
        return super.getAsPredSlot(slot.slot());
    }

    public PredSlot getAsPredSlot(ShapeSlots slot, int i) {
        return super.getAsPredSlot(slot.slot(), i, 0);
    }

    private ItemStack getMainItem() {
        return ShapeSlots.ARTIFACT_MAIN.get(this).m_7993_();
    }

    private boolean mainSlotsLocked() {
        return this.getMainItem().isEmpty() || BaseArtifact.getStats(this.getMainItem()).isEmpty();
    }

    private boolean subArtifactSlotLocked(int i) {
        if (this.mainSlotsLocked()) {
            return true;
        } else {
            BaseArtifact mainItem = (BaseArtifact) this.getMainItem().getItem();
            int rank = mainItem.rank;
            return i >= rank - 1;
        }
    }

    private boolean subMatSlotLocked(int i) {
        if (this.subArtifactSlotLocked(i)) {
            return true;
        } else {
            ItemStack sub = ShapeSlots.ARTIFACT_SUB.get(this, i).m_7993_();
            if (sub.isEmpty()) {
                return true;
            } else {
                Optional<ArtifactStats> subOpt = BaseArtifact.getStats(sub);
                return subOpt.isEmpty();
            }
        }
    }

    private boolean isAllowedAsSubArtifact(int index, ItemStack e) {
        ItemStack main = this.getMainItem();
        if (main.isEmpty()) {
            return false;
        } else {
            BaseArtifact mainItem = (BaseArtifact) this.getMainItem().getItem();
            if (e.getItem() instanceof BaseArtifact eItem) {
                if (eItem.set.get() != mainItem.set.get()) {
                    return false;
                } else {
                    int rank = mainItem.rank;
                    if (index >= rank - 1) {
                        return false;
                    } else {
                        Optional<ArtifactStats> mainOpt = BaseArtifact.getStats(main);
                        if (mainOpt.isEmpty()) {
                            return false;
                        } else if (((ArtifactStats) mainOpt.get()).level < ArtifactUpgradeManager.getMaxLevel(rank)) {
                            return false;
                        } else {
                            ResourceLocation mainType = ((ArtifactStats) mainOpt.get()).main_stat.type;
                            Optional<ArtifactStats> eOpt = BaseArtifact.getStats(e);
                            if (eOpt.isEmpty()) {
                                return false;
                            } else if (((ArtifactStats) eOpt.get()).level < ArtifactUpgradeManager.getMaxLevel(rank)) {
                                return false;
                            } else {
                                ResourceLocation eType = ((ArtifactStats) eOpt.get()).main_stat.type;
                                if (mainType.equals(eType)) {
                                    return false;
                                } else {
                                    for (int i = 0; i < rank - 1; i++) {
                                        if (i != index) {
                                            ItemStack other = ShapeSlots.ARTIFACT_SUB.get(this, i).m_7993_();
                                            if (!other.isEmpty()) {
                                                Optional<ArtifactStats> subOpt = BaseArtifact.getStats(other);
                                                assert subOpt.isPresent();
                                                ResourceLocation subType = ((ArtifactStats) subOpt.get()).main_stat.type;
                                                if (subType.equals(eType)) {
                                                    return false;
                                                }
                                            }
                                        }
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else {
                return false;
            }
        }
    }

    @Override
    protected void securedServerSlotChange(Container cont) {
        ShapeSlots.BOOST_MAIN.get(this).updateEject(this.player);
        for (int i = 0; i < 4; i++) {
            ShapeSlots.ARTIFACT_SUB.get(this, i).updateEject(this.player);
        }
        for (int i = 0; i < 4; i++) {
            ShapeSlots.STAT_SUB.get(this, i).updateEject(this.player);
        }
        for (int i = 0; i < 4; i++) {
            ShapeSlots.BOOST_SUB.get(this, i).updateEject(this.player);
        }
        boolean outputChanged = ShapeSlots.OUTPUT.get(this).clearDirty();
        if (!this.getMainItem().isEmpty()) {
            BaseArtifact artifact = (BaseArtifact) this.getMainItem().getItem();
            int rank = artifact.rank;
            boolean pass = !ShapeSlots.BOOST_MAIN.get(this).m_7993_().isEmpty();
            for (int i = 0; i < rank - 1; i++) {
                pass &= !ShapeSlots.ARTIFACT_SUB.get(this, i).m_7993_().isEmpty();
                pass &= !ShapeSlots.STAT_SUB.get(this, i).m_7993_().isEmpty();
                pass &= !ShapeSlots.BOOST_SUB.get(this, i).m_7993_().isEmpty();
            }
            if (!outputChanged) {
                if (pass) {
                    ItemStack result = new ItemStack(artifact, 1);
                    RandomSource r = this.player.m_217043_();
                    ArtifactStats stat = new ArtifactStats((ArtifactSlot) artifact.slot.get(), rank);
                    Optional<ArtifactStats> mainOpt = BaseArtifact.getStats(this.getMainItem());
                    assert mainOpt.isPresent();
                    ResourceLocation mainStat = ((ArtifactStats) mainOpt.get()).main_stat.type;
                    stat.add(mainStat, StatTypeConfig.get(mainStat).getInitialValue(r, true));
                    for (int i = 0; i < rank - 1; i++) {
                        Optional<ArtifactStats> subOpt = BaseArtifact.getStats(ShapeSlots.ARTIFACT_SUB.get(this, i).m_7993_());
                        assert subOpt.isPresent();
                        ResourceLocation subStat = ((ArtifactStats) subOpt.get()).main_stat.type;
                        stat.add(subStat, StatTypeConfig.get(subStat).getInitialValue(r, true));
                    }
                    TagCodec.toTag(ItemCompoundTag.of(result).getSubTag("ArtifactData").getOrCreate(), stat);
                    ShapeSlots.OUTPUT.get(this).m_5852_(result);
                } else {
                    ShapeSlots.OUTPUT.get(this).m_5852_(ItemStack.EMPTY);
                }
                ShapeSlots.OUTPUT.get(this).clearDirty();
            }
            if (outputChanged && pass) {
                ShapeSlots.ARTIFACT_MAIN.get(this).m_6201_(1);
                ShapeSlots.BOOST_MAIN.get(this).m_6201_(1);
                for (int i = 0; i < rank - 1; i++) {
                    ShapeSlots.ARTIFACT_SUB.get(this, i).m_6201_(1);
                    ShapeSlots.STAT_SUB.get(this, i).m_6201_(1);
                    ShapeSlots.BOOST_SUB.get(this, i).m_6201_(1);
                }
            }
        } else {
            ShapeSlots.OUTPUT.get(this).m_5852_(ItemStack.EMPTY);
            ShapeSlots.OUTPUT.get(this).clearDirty();
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int data) {
        return false;
    }

    @Override
    protected boolean shouldClear(Container container, int slot) {
        return slot != 0;
    }
}