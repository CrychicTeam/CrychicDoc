package dev.xkmc.l2artifacts.content.search.augment;

import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.core.StatEntry;
import dev.xkmc.l2artifacts.content.search.filter.FilterScreen;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenuScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.l2library.base.menu.stacked.StackedRenderHandle;
import dev.xkmc.l2library.util.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class AugmentMenuScreen extends BaseContainerScreen<AugmentMenu> implements IFilterScreen {

    private static final int MAX_TIME = 60;

    private static final ChatFormatting MAIN = ChatFormatting.DARK_GREEN;

    private static final ChatFormatting SUB = ChatFormatting.BLUE;

    private static final ChatFormatting LIT = ChatFormatting.RED;

    private boolean pressed = false;

    private float time = 0.0F;

    @Nullable
    private ArtifactStats old = null;

    @Nullable
    private ArtifactStats current = null;

    private ItemStack oldStack = ItemStack.EMPTY;

    private boolean keep = false;

    private static int lerpColor(float perc, int fg, int bg) {
        int c0 = Math.round(Mth.lerp(perc, (float) (fg & 0xFF), (float) (bg & 0xFF)));
        fg >>= 8;
        bg >>= 8;
        int c1 = Math.round(Mth.lerp(perc, (float) (fg & 0xFF), (float) (bg & 0xFF)));
        fg >>= 8;
        bg >>= 8;
        int c2 = Math.round(Mth.lerp(perc, (float) (fg & 0xFF), (float) (bg & 0xFF)));
        return c2 << 16 | c1 << 8 | c0;
    }

    public AugmentMenuScreen(AugmentMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, LangData.TAB_AUGMENT.get().withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected final void init() {
        super.m_7856_();
        new FilterTabManager(this, ((AugmentMenu) this.f_97732_).token).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, FilterTabManager.AUGMENT);
    }

    @Override
    protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
        MenuLayoutConfig.ScreenRenderer sr = ((AugmentMenu) this.f_97732_).sprite.get().getRenderer(this);
        sr.start(g);
        int mask = ((AugmentMenu) this.f_97732_).mask.get();
        if ((mask & 1) > 0) {
            sr.draw(g, "in_0", "toggle_slot_1", -1, -1);
        }
        if ((mask & 2) > 0) {
            sr.draw(g, "in_1", "toggle_slot_1", -1, -1);
        }
        if ((mask & 4) > 0) {
            sr.draw(g, "in_2", "toggle_slot_1", -1, -1);
        }
        if (((AugmentMenu) this.f_97732_).container.getItem(1).isEmpty()) {
            sr.draw(g, "in_0", "altas_stat_container");
        }
        if (((AugmentMenu) this.f_97732_).container.getItem(2).isEmpty()) {
            sr.draw(g, "in_1", "altas_boost_main");
        }
        if (((AugmentMenu) this.f_97732_).container.getItem(3).isEmpty()) {
            sr.draw(g, "in_2", "altas_boost_sub");
        }
        MenuLayoutConfig.Rect rect = ((AugmentMenu) this.f_97732_).sprite.get().getComp("upgrade");
        if (this.m_6774_(rect.x, rect.y, rect.w, rect.h, (double) mx, (double) my)) {
            if (this.pressed) {
                sr.draw(g, "upgrade", "upgrade_on");
            }
            FilterScreen.renderHighlight(g, this.f_97735_ + rect.x, this.f_97736_ + rect.y, rect.w, rect.h, -2130706433);
        }
        if (this.time > 0.0F) {
            this.time = this.time - Minecraft.getInstance().getDeltaFrameTime();
            if (this.time <= 0.0F) {
                this.time = 0.0F;
                this.old = null;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        this.pressed = true;
        return super.m_6375_(mx, my, btn);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int btn) {
        this.pressed = false;
        MenuLayoutConfig.Rect rect = ((AugmentMenu) this.f_97732_).sprite.get().getComp("upgrade");
        if (this.m_6774_(rect.x, rect.y, rect.w, rect.h, mx, my)) {
            this.old = this.current;
            this.time = 60.0F;
            this.keep = this.click(0);
        }
        return super.m_6348_(mx, my, btn);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mx, int my) {
        g.drawString(this.f_96547_, this.f_96539_, this.f_97728_, this.f_97729_, 4210752, false);
        g.drawString(this.f_96547_, this.f_169604_.copy().withStyle(ChatFormatting.GRAY), this.f_97730_, this.f_97731_, 4210752, false);
        g.pose().pushPose();
        g.pose().translate(0.0F, 45.0F, 0.0F);
        StackedRenderHandle handle = new StackedRenderHandle(this, g, ((AugmentMenu) this.f_97732_).sprite.get(), 0);
        int exp = ((AugmentMenu) this.f_97732_).experience.get();
        int cost = ((AugmentMenu) this.f_97732_).exp_cost.get();
        if (cost > 0) {
            String str = RecycleMenuScreen.formatNumber(cost) + "/" + RecycleMenuScreen.formatNumber(exp);
            handle.drawText(LangData.TAB_INFO_EXP_COST.get(Component.literal(str).withStyle(cost <= exp ? ChatFormatting.DARK_GREEN : ChatFormatting.RED)).withStyle(ChatFormatting.GRAY), false);
            ItemStack stack = ((AugmentMenu) this.f_97732_).container.getItem(0);
            if (stack != this.oldStack) {
                this.oldStack = stack;
                if (this.keep) {
                    this.keep = false;
                } else {
                    this.old = null;
                }
            }
            Optional<ArtifactStats> opt = BaseArtifact.getStats(stack);
            if (opt.isPresent()) {
                ArtifactStats stat = (ArtifactStats) opt.get();
                List<Component[]> table = new ArrayList();
                table.add(this.addEntry(true, stat.main_stat, this.old == null ? null : this.old.main_stat, false, (((AugmentMenu) this.f_97732_).mask.get() & 2) > 0));
                boolean display = this.old != null && this.old.sub_stats.size() == stat.sub_stats.size();
                for (int i = 0; i < stat.sub_stats.size(); i++) {
                    int I = i;
                    boolean stat_exist = (((AugmentMenu) this.f_97732_).mask.get() & 1) > 0;
                    boolean lit_name = stat_exist && (Boolean) StatContainerItem.getType(((AugmentMenu) this.f_97732_).container.getItem(1)).map(e -> e.equals(((StatEntry) stat.sub_stats.get(I)).type)).orElse(false);
                    boolean boost_sub = (((AugmentMenu) this.f_97732_).mask.get() & 4) > 0;
                    boolean lit_stat = boost_sub && (!stat_exist || lit_name);
                    table.add(this.addEntry(false, (StatEntry) stat.sub_stats.get(i), !display ? null : (StatEntry) this.old.sub_stats.get(i), lit_name, lit_stat));
                }
                handle.drawTable((Component[][]) table.toArray(Component[][]::new), this.f_97726_, false);
                this.current = stat;
            }
        } else {
            handle.drawText(LangData.TAB_INFO_EXP.get(exp).withStyle(ChatFormatting.GRAY), false);
        }
        handle.flushText();
        g.pose().popPose();
    }

    private Component[] addEntry(boolean main, StatEntry entry, @Nullable StatEntry old, boolean lit_name, boolean lit_stat) {
        Component[] ans = new Component[3];
        if (Proxy.getClientPlayer().f_19797_ % 20 < 10) {
            lit_stat = false;
            lit_name = false;
        }
        ans[0] = Component.translatable(entry.getType().attr.getDescriptionId()).withStyle(lit_name ? LIT : (main ? MAIN : SUB));
        ans[1] = entry.getType().getValueText(entry.getValue()).withStyle(lit_stat ? LIT : (main ? MAIN : SUB));
        if (old != null) {
            double diff = entry.getValue() - old.getValue();
            if (diff > 0.001) {
                Integer fg = ChatFormatting.DARK_PURPLE.getColor();
                assert fg != null;
                float perc = 1.0F - this.time / 60.0F;
                int c = lerpColor(perc, fg, 3355182);
                ans[2] = entry.getType().getValueText(diff).withStyle(Style.EMPTY.withColor(c));
            } else {
                ans[2] = Component.empty();
            }
        } else {
            ans[2] = Component.empty();
        }
        entry.getTooltip();
        return ans;
    }

    @Override
    public int screenWidth() {
        return this.f_96543_;
    }

    @Override
    public int screenHeight() {
        return this.f_96544_;
    }
}