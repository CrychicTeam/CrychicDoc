package dev.xkmc.l2hostility.content.item.beacon;

import com.google.common.collect.Lists;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.util.Proxy;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HostilityBeaconScreen extends AbstractContainerScreen<HostilityBeaconMenu> implements ContainerListener {

    static final ResourceLocation BEACON_LOCATION = new ResourceLocation("l2hostility", "textures/gui/container/beacon.png");

    private static final Component PRIMARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.primary");

    private final List<HostilityBeaconScreen.BeaconButton> beaconButtons = Lists.newArrayList();

    int primary = -1;

    public HostilityBeaconScreen(HostilityBeaconMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.f_97726_ = 230;
        this.f_97727_ = 219;
        menu.m_38893_(this);
    }

    @Override
    public void slotChanged(AbstractContainerMenu self, int index, ItemStack stack) {
    }

    @Override
    public void dataChanged(AbstractContainerMenu self, int index, int value) {
        this.primary = ((HostilityBeaconMenu) this.f_97732_).getPrimaryEffect();
    }

    private <T extends AbstractWidget & HostilityBeaconScreen.BeaconButton> void addBeaconButton(T btn) {
        this.m_142416_(btn);
        this.beaconButtons.add(btn);
    }

    @Override
    protected void init() {
        super.init();
        this.beaconButtons.clear();
        this.addBeaconButton(new HostilityBeaconScreen.BeaconConfirmButton(this.f_97735_ + 164, this.f_97736_ + 107));
        this.addBeaconButton(new HostilityBeaconScreen.BeaconCancelButton(this.f_97735_ + 190, this.f_97736_ + 107));
        for (int i = 0; i <= 2; i++) {
            int n = HostilityBeaconBlockEntity.BEACON_EFFECTS[i].length;
            int w = n * 22 + (n - 1) * 2;
            for (int j = 0; j < n; j++) {
                HostilityBeaconScreen.BeaconPowerButton btn = new HostilityBeaconScreen.BeaconPowerButton(this.f_97735_ + 76 + j * 24 - w / 2, this.f_97736_ + 22 + i * 25, i * 2 + j, i);
                btn.f_93623_ = false;
                this.addBeaconButton(btn);
            }
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.updateButtons();
    }

    void updateButtons() {
        int i = ((HostilityBeaconMenu) this.f_97732_).getLevels();
        this.beaconButtons.forEach(p_169615_ -> p_169615_.updateStatus(i));
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
        guiGraphics0.drawCenteredString(this.f_96547_, PRIMARY_EFFECT_LABEL, 62, 10, 14737632);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics0.blit(BEACON_LOCATION, i, j, 0, 0, this.f_97726_, this.f_97727_);
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate(0.0F, 0.0F, 100.0F);
        guiGraphics0.renderItem(new ItemStack(LHItems.CHAOS_INGOT), i + 42 + 44, j + 109);
        guiGraphics0.renderItem(new ItemStack(LHItems.MIRACLE_INGOT), i + 42 + 66, j + 109);
        guiGraphics0.pose().popPose();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
        this.m_280072_(guiGraphics0, int1, int2);
    }

    protected boolean click(int btn) {
        if (((HostilityBeaconMenu) this.f_97732_).clickMenuButton(Proxy.getClientPlayer(), btn) && Minecraft.getInstance().gameMode != null) {
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(((HostilityBeaconMenu) this.f_97732_).f_38840_, btn);
            return true;
        } else {
            return false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    interface BeaconButton {

        void updateStatus(int var1);
    }

    @OnlyIn(Dist.CLIENT)
    class BeaconCancelButton extends HostilityBeaconScreen.BeaconSpriteScreenButton {

        public BeaconCancelButton(int int0, int int1) {
            super(int0, int1, 112, 220, CommonComponents.GUI_CANCEL);
        }

        @Override
        public void onPress() {
            HostilityBeaconScreen.this.f_96541_.player.closeContainer();
        }

        @Override
        public void updateStatus(int int0) {
        }
    }

    @OnlyIn(Dist.CLIENT)
    class BeaconConfirmButton extends HostilityBeaconScreen.BeaconSpriteScreenButton {

        public BeaconConfirmButton(int int0, int int1) {
            super(int0, int1, 90, 220, CommonComponents.GUI_DONE);
        }

        @Override
        public void onPress() {
            HostilityBeaconScreen.this.click(HostilityBeaconScreen.this.primary);
            HostilityBeaconScreen.this.f_96541_.player.closeContainer();
        }

        @Override
        public void updateStatus(int int0) {
            this.f_93623_ = ((HostilityBeaconMenu) HostilityBeaconScreen.this.f_97732_).hasPayment() && HostilityBeaconScreen.this.primary != -1;
        }
    }

    @OnlyIn(Dist.CLIENT)
    class BeaconPowerButton extends HostilityBeaconScreen.BeaconScreenButton {

        protected final int tier;

        private int effect;

        private TextureAtlasSprite sprite;

        public BeaconPowerButton(int x, int y, int eff, int tier) {
            super(x, y);
            this.tier = tier;
            this.setEffect(eff);
        }

        protected MobEffect getEffect() {
            return HostilityBeaconBlockEntity.BEACON_EFFECTS[this.effect / 2][this.effect % 2];
        }

        protected void setEffect(int eff) {
            this.effect = eff;
            this.sprite = Minecraft.getInstance().getMobEffectTextures().get(this.getEffect());
            this.m_257544_(Tooltip.create(this.createEffectDescription(this.getEffect()), null));
        }

        protected MutableComponent createEffectDescription(MobEffect eff) {
            return Component.translatable(eff.getDescriptionId());
        }

        @Override
        public void onPress() {
            if (!this.isSelected()) {
                HostilityBeaconScreen.this.primary = this.effect;
                HostilityBeaconScreen.this.updateButtons();
            }
        }

        @Override
        protected void renderIcon(GuiGraphics g) {
            g.blit(this.m_252754_() + 2, this.m_252907_() + 2, 0, 18, 18, this.sprite);
        }

        @Override
        public void updateStatus(int lv) {
            this.f_93623_ = this.tier < lv;
            this.setSelected(this.effect == HostilityBeaconScreen.this.primary);
        }

        @Override
        protected MutableComponent createNarrationMessage() {
            return this.createEffectDescription(this.getEffect());
        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class BeaconScreenButton extends AbstractButton implements HostilityBeaconScreen.BeaconButton {

        private boolean selected;

        protected BeaconScreenButton(int int0, int int1) {
            super(int0, int1, 22, 22, CommonComponents.EMPTY);
        }

        protected BeaconScreenButton(int int0, int int1, Component component2) {
            super(int0, int1, 22, 22, component2);
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
            int i = 219;
            int j = 0;
            if (!this.f_93623_) {
                j += this.f_93618_ * 2;
            } else if (this.selected) {
                j += this.f_93618_ * 1;
            } else if (this.m_198029_()) {
                j += this.f_93618_ * 3;
            }
            guiGraphics0.blit(HostilityBeaconScreen.BEACON_LOCATION, this.m_252754_(), this.m_252907_(), j, 219, this.f_93618_, this.f_93619_);
            this.renderIcon(guiGraphics0);
        }

        protected abstract void renderIcon(GuiGraphics var1);

        public boolean isSelected() {
            return this.selected;
        }

        public void setSelected(boolean boolean0) {
            this.selected = boolean0;
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
            this.m_168802_(narrationElementOutput0);
        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class BeaconSpriteScreenButton extends HostilityBeaconScreen.BeaconScreenButton {

        private final int iconX;

        private final int iconY;

        protected BeaconSpriteScreenButton(int int0, int int1, int int2, int int3, Component component4) {
            super(int0, int1, component4);
            this.iconX = int2;
            this.iconY = int3;
        }

        @Override
        protected void renderIcon(GuiGraphics guiGraphics0) {
            guiGraphics0.blit(HostilityBeaconScreen.BEACON_LOCATION, this.m_252754_() + 2, this.m_252907_() + 2, this.iconX, this.iconY, 18, 18);
        }
    }
}