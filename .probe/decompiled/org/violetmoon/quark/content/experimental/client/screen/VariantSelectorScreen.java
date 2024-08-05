package org.violetmoon.quark.content.experimental.client.screen;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.experimental.module.VariantSelectorModule;

public class VariantSelectorScreen extends Screen {

    private float timeIn = 0.0F;

    private int slotSelected = -1;

    private final Minecraft mc;

    private final KeyMapping key;

    private final String currentVariant;

    private final List<Pair<String, Block>> variants;

    private final List<VariantSelectorScreen.DrawStack> drawStacks = new ArrayList();

    public VariantSelectorScreen(Block originalBlock, KeyMapping key, String currentVariant, List<String> visibleVariants) {
        super(Component.empty());
        this.mc = Minecraft.getInstance();
        this.key = key;
        this.currentVariant = currentVariant;
        this.variants = new ArrayList();
        this.variants.add(Pair.of("", originalBlock));
        for (String v : visibleVariants) {
            Block variantBlock = VariantSelectorModule.getVariantBlockFromOriginal(originalBlock, v);
            this.variants.add(Pair.of(v, variantBlock));
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mx, int my, float delta) {
        super.render(guiGraphics, mx, my, delta);
        this.timeIn += delta;
        int x = this.f_96543_ / 2;
        int y = this.f_96544_ / 2;
        int maxRadius = 50;
        int segments = this.variants.size();
        float degPer = (float) (Math.PI * 2) / (float) segments;
        float pad = -((float) Math.PI / (float) segments) + (float) (Math.PI / 2);
        double angle = mouseAngle(x, y, mx, my);
        double dist = (double) ((x - mx) * (x - mx) + (y - my) * (y - my));
        if (angle < (double) pad) {
            angle = (Math.PI * 2) + (double) pad;
        }
        this.slotSelected = -1;
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::m_172811_);
        this.drawStacks.clear();
        buf.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        for (int seg = 0; seg < segments; seg++) {
            Pair<String, Block> pair = (Pair<String, Block>) this.variants.get(seg);
            String variant = (String) pair.getFirst();
            Block variantBlock = (Block) pair.getSecond();
            boolean variantExists = variantBlock != null;
            boolean rightVariant = variant.equals(this.currentVariant);
            float start = (float) seg * degPer + pad;
            float end = (float) (seg + 1) * degPer + pad;
            boolean mouseInSector = variantExists && (double) start < angle && angle < (double) end && dist > 64.0;
            float radius = Math.max(0.0F, Math.min((this.timeIn - (float) seg * 6.0F / (float) segments) * 40.0F, (float) maxRadius));
            if (mouseInSector || rightVariant) {
                radius *= 1.1F;
            }
            if (!variantExists) {
                radius *= 0.9F;
            }
            int gs = 57;
            if (seg % 2 == 0) {
                gs += 41;
            }
            int r = gs;
            int b = gs;
            int a = 68;
            int g;
            if (variantExists) {
                g = gs + 34;
                a = 153;
            } else {
                r = gs / 4;
                g = gs / 4;
                b = gs / 4;
            }
            if (seg == 0) {
                buf.m_5483_((double) x, (double) y, 0.0).color(r, g, b, a).endVertex();
            }
            if (mouseInSector) {
                this.slotSelected = seg;
                r = 0;
                b = 170;
                g = 170;
            } else if (rightVariant) {
                b = 0;
                r = 0;
                g = 170;
            }
            float sxp = (float) x + Mth.cos(start) * radius;
            float syp = (float) y + Mth.sin(start) * radius;
            float exp = (float) x + Mth.cos(end) * radius;
            float eyp = (float) y + Mth.sin(end) * radius;
            buf.m_5483_((double) sxp, (double) syp, 0.0).color(r, g, b, a).endVertex();
            buf.m_5483_((double) exp, (double) eyp, 0.0).color(r, g, b, a).endVertex();
            float center = ((float) seg + 0.5F) * degPer + pad;
            float cxp = (float) x + Mth.cos(center) * radius;
            float cyp = (float) y + Mth.sin(center) * radius;
            ItemStack variantStack = variantExists ? new ItemStack(variantBlock) : ItemStack.EMPTY;
            double mod = 0.6;
            int xdp = (int) ((double) (cxp - (float) x) * mod + (double) x);
            int ydp = (int) ((double) (cyp - (float) y) * mod + (double) y);
            this.drawStacks.add(new VariantSelectorScreen.DrawStack(variantStack, xdp - 8, ydp - 8));
        }
        tess.end();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        for (VariantSelectorScreen.DrawStack ds : this.drawStacks) {
            if (!ds.stack().isEmpty()) {
                guiGraphics.renderItem(ds.stack(), ds.x(), ds.y());
            }
        }
        RenderSystem.disableBlend();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isKeyDown(this.key)) {
            this.mc.setScreen(null);
            if (this.slotSelected == -1 && this.timeIn < 10.0F) {
                this.slotSelected = 0;
            }
            if (this.slotSelected != -1) {
                String variant = (String) ((Pair) this.variants.get(this.slotSelected)).getFirst();
                VariantSelectorModule.Client.setClientVariant(variant, true);
                this.mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }
        ImmutableSet<KeyMapping> set = ImmutableSet.of(this.mc.options.keyUp, this.mc.options.keyLeft, this.mc.options.keyDown, this.mc.options.keyRight, this.mc.options.keyShift, this.mc.options.keySprint, new KeyMapping[] { this.mc.options.keyJump });
        UnmodifiableIterator var2 = set.iterator();
        while (var2.hasNext()) {
            KeyMapping k = (KeyMapping) var2.next();
            KeyMapping.set(k.getKey(), this.isKeyDown(k));
        }
    }

    public boolean isKeyDown(KeyMapping keybind) {
        InputConstants.Key key = keybind.getKey();
        return key.getType() == InputConstants.Type.MOUSE ? keybind.isDown() : InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static double mouseAngle(int x, int y, int mx, int my) {
        return (Mth.atan2((double) (my - y), (double) (mx - x)) + (Math.PI * 2)) % (Math.PI * 2);
    }

    private static record DrawStack(ItemStack stack, int x, int y) {
    }
}