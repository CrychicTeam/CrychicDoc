package net.minecraft.client.gui.screens.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

public class BeaconScreen extends AbstractContainerScreen<BeaconMenu> {

    static final ResourceLocation BEACON_LOCATION = new ResourceLocation("textures/gui/container/beacon.png");

    private static final Component PRIMARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.primary");

    private static final Component SECONDARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.secondary");

    private final List<BeaconScreen.BeaconButton> beaconButtons = Lists.newArrayList();

    @Nullable
    MobEffect primary;

    @Nullable
    MobEffect secondary;

    public BeaconScreen(final BeaconMenu beaconMenu0, Inventory inventory1, Component component2) {
        super(beaconMenu0, inventory1, component2);
        this.f_97726_ = 230;
        this.f_97727_ = 219;
        beaconMenu0.m_38893_(new ContainerListener() {

            @Override
            public void slotChanged(AbstractContainerMenu p_97973_, int p_97974_, ItemStack p_97975_) {
            }

            @Override
            public void dataChanged(AbstractContainerMenu p_169628_, int p_169629_, int p_169630_) {
                BeaconScreen.this.primary = beaconMenu0.getPrimaryEffect();
                BeaconScreen.this.secondary = beaconMenu0.getSecondaryEffect();
            }
        });
    }

    private <T extends AbstractWidget & BeaconScreen.BeaconButton> void addBeaconButton(T t0) {
        this.m_142416_(t0);
        this.beaconButtons.add(t0);
    }

    @Override
    protected void init() {
        super.init();
        this.beaconButtons.clear();
        this.addBeaconButton(new BeaconScreen.BeaconConfirmButton(this.f_97735_ + 164, this.f_97736_ + 107));
        this.addBeaconButton(new BeaconScreen.BeaconCancelButton(this.f_97735_ + 190, this.f_97736_ + 107));
        for (int $$0 = 0; $$0 <= 2; $$0++) {
            int $$1 = BeaconBlockEntity.BEACON_EFFECTS[$$0].length;
            int $$2 = $$1 * 22 + ($$1 - 1) * 2;
            for (int $$3 = 0; $$3 < $$1; $$3++) {
                MobEffect $$4 = BeaconBlockEntity.BEACON_EFFECTS[$$0][$$3];
                BeaconScreen.BeaconPowerButton $$5 = new BeaconScreen.BeaconPowerButton(this.f_97735_ + 76 + $$3 * 24 - $$2 / 2, this.f_97736_ + 22 + $$0 * 25, $$4, true, $$0);
                $$5.f_93623_ = false;
                this.addBeaconButton($$5);
            }
        }
        int $$6 = 3;
        int $$7 = BeaconBlockEntity.BEACON_EFFECTS[3].length + 1;
        int $$8 = $$7 * 22 + ($$7 - 1) * 2;
        for (int $$9 = 0; $$9 < $$7 - 1; $$9++) {
            MobEffect $$10 = BeaconBlockEntity.BEACON_EFFECTS[3][$$9];
            BeaconScreen.BeaconPowerButton $$11 = new BeaconScreen.BeaconPowerButton(this.f_97735_ + 167 + $$9 * 24 - $$8 / 2, this.f_97736_ + 47, $$10, false, 3);
            $$11.f_93623_ = false;
            this.addBeaconButton($$11);
        }
        BeaconScreen.BeaconPowerButton $$12 = new BeaconScreen.BeaconUpgradePowerButton(this.f_97735_ + 167 + ($$7 - 1) * 24 - $$8 / 2, this.f_97736_ + 47, BeaconBlockEntity.BEACON_EFFECTS[0][0]);
        $$12.f_93624_ = false;
        this.addBeaconButton($$12);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.updateButtons();
    }

    void updateButtons() {
        int $$0 = ((BeaconMenu) this.f_97732_).getLevels();
        this.beaconButtons.forEach(p_169615_ -> p_169615_.updateStatus($$0));
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
        guiGraphics0.drawCenteredString(this.f_96547_, PRIMARY_EFFECT_LABEL, 62, 10, 14737632);
        guiGraphics0.drawCenteredString(this.f_96547_, SECONDARY_EFFECT_LABEL, 169, 10, 14737632);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int $$4 = (this.f_96543_ - this.f_97726_) / 2;
        int $$5 = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics0.blit(BEACON_LOCATION, $$4, $$5, 0, 0, this.f_97726_, this.f_97727_);
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate(0.0F, 0.0F, 100.0F);
        guiGraphics0.renderItem(new ItemStack(Items.NETHERITE_INGOT), $$4 + 20, $$5 + 109);
        guiGraphics0.renderItem(new ItemStack(Items.EMERALD), $$4 + 41, $$5 + 109);
        guiGraphics0.renderItem(new ItemStack(Items.DIAMOND), $$4 + 41 + 22, $$5 + 109);
        guiGraphics0.renderItem(new ItemStack(Items.GOLD_INGOT), $$4 + 42 + 44, $$5 + 109);
        guiGraphics0.renderItem(new ItemStack(Items.IRON_INGOT), $$4 + 42 + 66, $$5 + 109);
        guiGraphics0.pose().popPose();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
        this.m_280072_(guiGraphics0, int1, int2);
    }

    interface BeaconButton {

        void updateStatus(int var1);
    }

    class BeaconCancelButton extends BeaconScreen.BeaconSpriteScreenButton {

        public BeaconCancelButton(int int0, int int1) {
            super(int0, int1, 112, 220, CommonComponents.GUI_CANCEL);
        }

        @Override
        public void onPress() {
            BeaconScreen.this.f_96541_.player.closeContainer();
        }

        @Override
        public void updateStatus(int int0) {
        }
    }

    class BeaconConfirmButton extends BeaconScreen.BeaconSpriteScreenButton {

        public BeaconConfirmButton(int int0, int int1) {
            super(int0, int1, 90, 220, CommonComponents.GUI_DONE);
        }

        @Override
        public void onPress() {
            BeaconScreen.this.f_96541_.getConnection().send(new ServerboundSetBeaconPacket(Optional.ofNullable(BeaconScreen.this.primary), Optional.ofNullable(BeaconScreen.this.secondary)));
            BeaconScreen.this.f_96541_.player.closeContainer();
        }

        @Override
        public void updateStatus(int int0) {
            this.f_93623_ = ((BeaconMenu) BeaconScreen.this.f_97732_).hasPayment() && BeaconScreen.this.primary != null;
        }
    }

    class BeaconPowerButton extends BeaconScreen.BeaconScreenButton {

        private final boolean isPrimary;

        protected final int tier;

        private MobEffect effect;

        private TextureAtlasSprite sprite;

        public BeaconPowerButton(int int0, int int1, MobEffect mobEffect2, boolean boolean3, int int4) {
            super(int0, int1);
            this.isPrimary = boolean3;
            this.tier = int4;
            this.setEffect(mobEffect2);
        }

        protected void setEffect(MobEffect mobEffect0) {
            this.effect = mobEffect0;
            this.sprite = Minecraft.getInstance().getMobEffectTextures().get(mobEffect0);
            this.m_257544_(Tooltip.create(this.createEffectDescription(mobEffect0), null));
        }

        protected MutableComponent createEffectDescription(MobEffect mobEffect0) {
            return Component.translatable(mobEffect0.getDescriptionId());
        }

        @Override
        public void onPress() {
            if (!this.m_98024_()) {
                if (this.isPrimary) {
                    BeaconScreen.this.primary = this.effect;
                } else {
                    BeaconScreen.this.secondary = this.effect;
                }
                BeaconScreen.this.updateButtons();
            }
        }

        @Override
        protected void renderIcon(GuiGraphics guiGraphics0) {
            guiGraphics0.blit(this.m_252754_() + 2, this.m_252907_() + 2, 0, 18, 18, this.sprite);
        }

        @Override
        public void updateStatus(int int0) {
            this.f_93623_ = this.tier < int0;
            this.m_98031_(this.effect == (this.isPrimary ? BeaconScreen.this.primary : BeaconScreen.this.secondary));
        }

        @Override
        protected MutableComponent createNarrationMessage() {
            return this.createEffectDescription(this.effect);
        }
    }

    abstract static class BeaconScreenButton extends AbstractButton implements BeaconScreen.BeaconButton {

        private boolean selected;

        protected BeaconScreenButton(int int0, int int1) {
            super(int0, int1, 22, 22, CommonComponents.EMPTY);
        }

        protected BeaconScreenButton(int int0, int int1, Component component2) {
            super(int0, int1, 22, 22, component2);
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
            int $$4 = 219;
            int $$5 = 0;
            if (!this.f_93623_) {
                $$5 += this.f_93618_ * 2;
            } else if (this.selected) {
                $$5 += this.f_93618_ * 1;
            } else if (this.m_198029_()) {
                $$5 += this.f_93618_ * 3;
            }
            guiGraphics0.blit(BeaconScreen.BEACON_LOCATION, this.m_252754_(), this.m_252907_(), $$5, 219, this.f_93618_, this.f_93619_);
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

    abstract static class BeaconSpriteScreenButton extends BeaconScreen.BeaconScreenButton {

        private final int iconX;

        private final int iconY;

        protected BeaconSpriteScreenButton(int int0, int int1, int int2, int int3, Component component4) {
            super(int0, int1, component4);
            this.iconX = int2;
            this.iconY = int3;
        }

        @Override
        protected void renderIcon(GuiGraphics guiGraphics0) {
            guiGraphics0.blit(BeaconScreen.BEACON_LOCATION, this.m_252754_() + 2, this.m_252907_() + 2, this.iconX, this.iconY, 18, 18);
        }
    }

    class BeaconUpgradePowerButton extends BeaconScreen.BeaconPowerButton {

        public BeaconUpgradePowerButton(int int0, int int1, MobEffect mobEffect2) {
            super(int0, int1, mobEffect2, false, 3);
        }

        @Override
        protected MutableComponent createEffectDescription(MobEffect mobEffect0) {
            return Component.translatable(mobEffect0.getDescriptionId()).append(" II");
        }

        @Override
        public void updateStatus(int int0) {
            if (BeaconScreen.this.primary != null) {
                this.f_93624_ = true;
                this.m_169649_(BeaconScreen.this.primary);
                super.updateStatus(int0);
            } else {
                this.f_93624_ = false;
            }
        }
    }
}