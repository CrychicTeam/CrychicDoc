package dev.shadowsoffire.attributeslib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shadowsoffire.attributeslib.util.Comparators;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class ModifierSource<T> implements Comparable<ModifierSource<T>> {

    protected final ModifierSourceType<T> type;

    protected final Comparator<T> comparator;

    protected final T data;

    public ModifierSource(ModifierSourceType<T> type, Comparator<T> comparator, T data) {
        this.type = type;
        this.comparator = comparator;
        this.data = data;
    }

    public abstract void render(GuiGraphics var1, Font var2, int var3, int var4);

    public ModifierSourceType<T> getType() {
        return this.type;
    }

    public final T getData() {
        return this.data;
    }

    public int compareTo(ModifierSource<T> o) {
        return this.comparator.compare(this.getData(), o.getData());
    }

    public static class EffectModifierSource extends ModifierSource<MobEffectInstance> {

        public EffectModifierSource(MobEffectInstance data) {
            super(ModifierSourceType.MOB_EFFECT, Comparator.comparing(MobEffectInstance::m_19544_, Comparators.idComparator(BuiltInRegistries.MOB_EFFECT)), data);
        }

        @Override
        public void render(GuiGraphics gfx, Font font, int x, int y) {
            MobEffectTextureManager texMgr = Minecraft.getInstance().getMobEffectTextures();
            MobEffect effect = this.data.getEffect();
            TextureAtlasSprite sprite = texMgr.get(effect);
            float scale = 0.5F;
            PoseStack stack = gfx.pose();
            stack.pushPose();
            stack.scale(scale, scale, 1.0F);
            stack.translate((float) x / scale, (float) y / scale, 0.0F);
            gfx.blit(0, 0, 0, 18, 18, sprite);
            stack.popPose();
        }
    }

    public static class ItemModifierSource extends ModifierSource<ItemStack> {

        public ItemModifierSource(ItemStack data) {
            super(ModifierSourceType.EQUIPMENT, Comparator.comparing(LivingEntity::m_147233_).reversed().thenComparing(Comparator.comparing(ItemStack::m_41720_, Comparators.idComparator(BuiltInRegistries.ITEM))), data);
        }

        @Override
        public void render(GuiGraphics gfx, Font font, int x, int y) {
            PoseStack pose = gfx.pose();
            pose.pushPose();
            float scale = 0.5F;
            pose.scale(scale, scale, 1.0F);
            pose.translate(1.0F + (float) x / scale, 1.0F + (float) y / scale, 0.0F);
            gfx.renderFakeItem(this.data, 0, 0);
            pose.popPose();
        }
    }
}