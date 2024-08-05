package dev.xkmc.l2artifacts.content.client.tab;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.ArtifactClient;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.code.TextWrapper;
import dev.xkmc.l2tabs.tabs.contents.BaseTextScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class SetEffectScreen extends BaseTextScreen {

    @Nullable
    private List<Component> hover = null;

    protected SetEffectScreen(Component title) {
        super(title, new ResourceLocation("l2tabs:textures/gui/empty.png"));
    }

    public void init() {
        super.m_7856_();
        new TabManager(this).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, ArtifactClient.TAB_SET_EFFECTS);
    }

    public void render(GuiGraphics g, int mx, int my, float ptick) {
        super.m_88315_(g, mx, my, ptick);
        Player player = Proxy.getClientPlayer();
        int x = this.leftPos + 8;
        int y = this.topPos + 6;
        Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(player).resolve();
        if (!opt.isEmpty()) {
            List<SlotResult> slots = ((ICuriosItemHandler) opt.get()).findCurios(stackx -> stackx.getItem() instanceof BaseArtifact);
            List<FormattedCharSequence> seq = new ArrayList();
            this.hover = null;
            for (SlotResult sr : slots) {
                ItemStack stack = sr.stack();
                BaseArtifact base = (BaseArtifact) stack.getItem();
                ((ArtifactSet) base.set.get()).getCountAndIndex(sr.slotContext()).ifPresent(result -> {
                    if (result.current_index() <= 0) {
                        for (Pair<List<Component>, List<Component>> pair : ((ArtifactSet) base.set.get()).addComponents(result)) {
                            List<FormattedCharSequence> lines = TextWrapper.wrapText(Minecraft.getInstance().font, (List<Component>) pair.getFirst(), this.imageWidth - 16);
                            int row = this.topPos + 6 + seq.size() * 10;
                            int column = this.leftPos + 6;
                            seq.addAll(lines);
                            int rowAft = this.topPos + 6 + seq.size() * 10;
                            int columnAft = this.leftPos + 6 + lines.size() * 45;
                            if (my >= row && my < rowAft && mx >= column && mx < columnAft) {
                                this.hover = (List<Component>) pair.getSecond();
                            }
                        }
                    }
                });
            }
            for (FormattedCharSequence comp : seq) {
                g.drawString(this.f_96547_, comp, x, y, 0, false);
                y += 10;
            }
            if (this.hover != null) {
                g.renderTooltip(this.f_96547_, this.hover, Optional.empty(), mx, my);
            }
        }
    }
}