package net.minecraft.client.gui.screens.inventory;

import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class EffectRenderingInventoryScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public EffectRenderingInventoryScreen(T t0, Inventory inventory1, Component component2) {
        super(t0, inventory1, component2);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        super.render(guiGraphics0, int1, int2, float3);
        this.renderEffects(guiGraphics0, int1, int2);
    }

    public boolean canSeeEffects() {
        int $$0 = this.f_97735_ + this.f_97726_ + 2;
        int $$1 = this.f_96543_ - $$0;
        return $$1 >= 32;
    }

    private void renderEffects(GuiGraphics guiGraphics0, int int1, int int2) {
        int $$3 = this.f_97735_ + this.f_97726_ + 2;
        int $$4 = this.f_96543_ - $$3;
        Collection<MobEffectInstance> $$5 = this.f_96541_.player.m_21220_();
        if (!$$5.isEmpty() && $$4 >= 32) {
            boolean $$6 = $$4 >= 120;
            int $$7 = 33;
            if ($$5.size() > 5) {
                $$7 = 132 / ($$5.size() - 1);
            }
            Iterable<MobEffectInstance> $$8 = Ordering.natural().sortedCopy($$5);
            this.renderBackgrounds(guiGraphics0, $$3, $$7, $$8, $$6);
            this.renderIcons(guiGraphics0, $$3, $$7, $$8, $$6);
            if ($$6) {
                this.renderLabels(guiGraphics0, $$3, $$7, $$8);
            } else if (int1 >= $$3 && int1 <= $$3 + 33) {
                int $$9 = this.f_97736_;
                MobEffectInstance $$10 = null;
                for (MobEffectInstance $$11 : $$8) {
                    if (int2 >= $$9 && int2 <= $$9 + $$7) {
                        $$10 = $$11;
                    }
                    $$9 += $$7;
                }
                if ($$10 != null) {
                    List<Component> $$12 = List.of(this.getEffectName($$10), MobEffectUtil.formatDuration($$10, 1.0F));
                    guiGraphics0.renderTooltip(this.f_96547_, $$12, Optional.empty(), int1, int2);
                }
            }
        }
    }

    private void renderBackgrounds(GuiGraphics guiGraphics0, int int1, int int2, Iterable<MobEffectInstance> iterableMobEffectInstance3, boolean boolean4) {
        int $$5 = this.f_97736_;
        for (MobEffectInstance $$6 : iterableMobEffectInstance3) {
            if (boolean4) {
                guiGraphics0.blit(f_97725_, int1, $$5, 0, 166, 120, 32);
            } else {
                guiGraphics0.blit(f_97725_, int1, $$5, 0, 198, 32, 32);
            }
            $$5 += int2;
        }
    }

    private void renderIcons(GuiGraphics guiGraphics0, int int1, int int2, Iterable<MobEffectInstance> iterableMobEffectInstance3, boolean boolean4) {
        MobEffectTextureManager $$5 = this.f_96541_.getMobEffectTextures();
        int $$6 = this.f_97736_;
        for (MobEffectInstance $$7 : iterableMobEffectInstance3) {
            MobEffect $$8 = $$7.getEffect();
            TextureAtlasSprite $$9 = $$5.get($$8);
            guiGraphics0.blit(int1 + (boolean4 ? 6 : 7), $$6 + 7, 0, 18, 18, $$9);
            $$6 += int2;
        }
    }

    private void renderLabels(GuiGraphics guiGraphics0, int int1, int int2, Iterable<MobEffectInstance> iterableMobEffectInstance3) {
        int $$4 = this.f_97736_;
        for (MobEffectInstance $$5 : iterableMobEffectInstance3) {
            Component $$6 = this.getEffectName($$5);
            guiGraphics0.drawString(this.f_96547_, $$6, int1 + 10 + 18, $$4 + 6, 16777215);
            Component $$7 = MobEffectUtil.formatDuration($$5, 1.0F);
            guiGraphics0.drawString(this.f_96547_, $$7, int1 + 10 + 18, $$4 + 6 + 10, 8355711);
            $$4 += int2;
        }
    }

    private Component getEffectName(MobEffectInstance mobEffectInstance0) {
        MutableComponent $$1 = mobEffectInstance0.getEffect().getDisplayName().copy();
        if (mobEffectInstance0.getAmplifier() >= 1 && mobEffectInstance0.getAmplifier() <= 9) {
            $$1.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + (mobEffectInstance0.getAmplifier() + 1)));
        }
        return $$1;
    }
}