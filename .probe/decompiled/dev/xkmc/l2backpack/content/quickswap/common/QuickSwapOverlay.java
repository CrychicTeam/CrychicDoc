package dev.xkmc.l2backpack.content.quickswap.common;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2backpack.content.quickswap.type.ISideInfoRenderer;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapManager;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import dev.xkmc.l2backpack.events.BackpackSel;
import dev.xkmc.l2backpack.init.data.BackpackConfig;
import dev.xkmc.l2itemselector.init.data.L2Keys;
import dev.xkmc.l2library.base.overlay.SelectionSideBar;
import dev.xkmc.l2library.base.overlay.SideBar;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.util.Wrappers;
import java.util.List;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class QuickSwapOverlay extends SelectionSideBar<ISwapEntry<?>, QuickSwapOverlay.BackpackSignature> {

    public static QuickSwapOverlay INSTANCE = new QuickSwapOverlay();

    public QuickSwapOverlay() {
        super(40.0F, 3.0F);
    }

    @Override
    public boolean isScreenOn() {
        LocalPlayer player = Proxy.getClientPlayer();
        return player == null ? false : BackpackSel.INSTANCE.isClientActive(player);
    }

    public static boolean hasShiftDown() {
        return L2Keys.hasShiftDown();
    }

    public static boolean hasAltDown() {
        return L2Keys.hasAltDown();
    }

    @Override
    protected boolean isOnHold() {
        return hasShiftDown() || hasAltDown() || L2Keys.SWAP.map.isDown();
    }

    @Override
    public Pair<List<ISwapEntry<?>>, Integer> getItems() {
        LocalPlayer player = Proxy.getClientPlayer();
        assert player != null;
        IQuickSwapToken<?> token = QuickSwapManager.getToken(player, hasAltDown());
        assert token != null;
        List<? extends ISwapEntry<?>> list = (List<? extends ISwapEntry<?>>) token.getList();
        int selected = token.getSelected();
        return Pair.of((List) Wrappers.cast(list), selected);
    }

    public static boolean activePopup(@Nullable QuickSwapType type) {
        return type != null && type.activePopup();
    }

    public QuickSwapOverlay.BackpackSignature getSignature() {
        LocalPlayer player = Proxy.getClientPlayer();
        assert player != null;
        IQuickSwapToken<?> token = QuickSwapManager.getToken(player, hasAltDown());
        assert token != null;
        int selected = token.getSelected();
        boolean ignoreOther = false;
        QuickSwapType type = token.type();
        if (!hasShiftDown()) {
            ignoreOther = !activePopup(type);
        }
        int focus = player.m_150109_().selected;
        ItemStack sel = type.getSignatureItem(player);
        return new QuickSwapOverlay.BackpackSignature(selected, ignoreOther, type, focus, sel);
    }

    public boolean isAvailable(ISwapEntry<?> token) {
        LocalPlayer player = Proxy.getClientPlayer();
        assert player != null;
        QuickSwapType type = token.token().type();
        return type.isAvailable(player, token);
    }

    @Override
    public boolean onCenter() {
        return BackpackConfig.CLIENT.previewOnCenter.get();
    }

    protected void renderEntry(SelectionSideBar.Context ctx, ISwapEntry<?> token, int i, int selected) {
        LocalPlayer player = Proxy.getClientPlayer();
        assert player != null;
        QuickSwapType type = token.token().type();
        type.renderSelected(ctx, player, token, ctx.x0(), 18 * i + ctx.y0(), selected == i && this.ease_time == this.max_ease, this.onCenter());
    }

    @Override
    public void renderContent(SelectionSideBar.Context ctx) {
        super.renderContent(ctx);
        LocalPlayer player = Proxy.getClientPlayer();
        assert player != null;
        Pair<List<ISwapEntry<?>>, Integer> pair = this.getItems();
        ISwapEntry<? extends ISwapEntry<?>> hover = (ISwapEntry<? extends ISwapEntry<?>>) ((List) pair.getFirst()).get((Integer) pair.getSecond());
        if (this.ease_time == this.max_ease && hover.token().type() instanceof ISideInfoRenderer rtype) {
            int x = ctx.x0();
            int y = 45 + ctx.y0();
            if (this.onCenter()) {
                x -= 18;
            } else {
                x += 18 * hover.asList().size();
            }
            rtype.renderSide(ctx, x, y, player, hover);
        }
    }

    @Override
    protected int getXOffset(int width) {
        float progress = (this.max_ease - this.ease_time) / this.max_ease;
        return this.onCenter() ? Math.round((float) width / 2.0F + 54.0F + 1.0F + progress * (float) width / 2.0F) : Math.round((float) (width - 36) + progress * 20.0F);
    }

    @Override
    protected int getYOffset(int height) {
        return height / 2 - 81 + 1;
    }

    public static record BackpackSignature(int backpackSelect, boolean ignoreOther, @Nullable QuickSwapType type, int playerSelect, ItemStack stack) implements SideBar.Signature<QuickSwapOverlay.BackpackSignature> {

        public boolean shouldRefreshIdle(SideBar<?> sideBar, @Nullable QuickSwapOverlay.BackpackSignature old) {
            if (this.ignoreOther) {
                return old == null ? false : old.type == this.type && old.backpackSelect != this.backpackSelect();
            } else {
                return !this.equals(old);
            }
        }
    }
}