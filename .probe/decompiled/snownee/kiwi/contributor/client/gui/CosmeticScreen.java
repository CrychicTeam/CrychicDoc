package snownee.kiwi.contributor.client.gui;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.kiwi.KiwiClientConfig;
import snownee.kiwi.config.ConfigHandler;
import snownee.kiwi.config.KiwiConfigManager;
import snownee.kiwi.contributor.Contributors;

public class CosmeticScreen extends Screen {

    private CosmeticScreen.List list;

    @Nullable
    private ResourceLocation currentCosmetic;

    private CosmeticScreen.Entry selectedEntry;

    public CosmeticScreen() {
        super(Component.translatable("gui.kiwi.cosmetic"));
    }

    @Override
    protected void init() {
        this.currentCosmetic = (ResourceLocation) Contributors.PLAYER_COSMETICS.get(getPlayerName());
        this.list = new CosmeticScreen.List(this.f_96541_, 150, this.f_96544_, 0, this.f_96544_, 20);
        this.list.m_93507_(20);
        this.list.addEntry(this.selectedEntry = new CosmeticScreen.Entry(this, null));
        String playerName = getPlayerName();
        boolean added = false;
        for (ResourceLocation tier : Contributors.getRenderableTiers()) {
            if (Contributors.isContributor(tier.getNamespace(), playerName, tier.getPath())) {
                CosmeticScreen.Entry entry = new CosmeticScreen.Entry(this, tier);
                this.list.addEntry(entry);
                added = true;
                if (tier.equals(this.currentCosmetic)) {
                    this.selectedEntry = entry;
                }
            }
        }
        if (!added) {
            this.f_96541_.setScreen(null);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float pTicks) {
        this.m_280273_(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, pTicks);
        this.list.m_88315_(guiGraphics, mouseX, mouseY, pTicks);
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        this.list.m_6375_(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
        return super.m_6375_(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        this.list.m_7979_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
        return super.m_7979_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
    }

    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        this.list.m_6348_(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        return super.m_6348_(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        this.list.m_6050_(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
        return super.m_6050_(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        this.list.m_7933_(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void onClose() {
        super.onClose();
        this.list = null;
        ConfigHandler cfg = KiwiConfigManager.getHandler(KiwiClientConfig.class);
        if (this.currentCosmetic != null && this.selectedEntry.id == null) {
            KiwiClientConfig.contributorCosmetic = "";
            cfg.save();
            Contributors.changeCosmetic();
        } else if (this.selectedEntry != null && !Objects.equals(this.selectedEntry.id, this.currentCosmetic)) {
            KiwiClientConfig.contributorCosmetic = this.selectedEntry.id.toString();
            cfg.save();
            Contributors.changeCosmetic();
        }
    }

    private static String getPlayerName() {
        return Minecraft.getInstance().getUser().getName();
    }

    private static class Entry extends ObjectSelectionList.Entry<CosmeticScreen.Entry> {

        private final CosmeticScreen parent;

        @Nullable
        private final ResourceLocation id;

        private final String name;

        public Entry(CosmeticScreen parent, ResourceLocation id) {
            this.parent = parent;
            this.id = id;
            this.name = id == null ? "-" : I18n.get(Util.makeDescriptionId("cosmetic", id));
        }

        @Override
        public void render(GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hover, float partialTicks) {
            int color = hover ? 16777130 : 16777215;
            if (this == this.parent.selectedEntry) {
                color = 16777079;
            }
            guiGraphics.drawString(this.parent.f_96547_, this.name, left + 43, top + 2, color);
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            this.parent.selectedEntry = this;
            return false;
        }

        @Override
        public Component getNarration() {
            return Component.literal(this.name);
        }
    }

    private static class List extends ObjectSelectionList<CosmeticScreen.Entry> {

        public List(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
            super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        }

        public int addEntry(CosmeticScreen.Entry cosmeticScreenEntry0) {
            return super.m_7085_(cosmeticScreenEntry0);
        }
    }
}