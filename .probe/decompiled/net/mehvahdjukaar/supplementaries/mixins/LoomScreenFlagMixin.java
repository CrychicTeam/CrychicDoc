package net.mehvahdjukaar.supplementaries.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.FlagBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.common.items.FlagItem;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LoomScreen.class })
public abstract class LoomScreenFlagMixin extends AbstractContainerScreen<LoomMenu> {

    @Unique
    private final CyclingSlotBackground supplementaries$bannerFlagBG = new CyclingSlotBackground(0);

    @Shadow
    private List<Pair<Holder<BannerPattern>, DyeColor>> resultBannerPatterns;

    @Shadow
    private boolean hasMaxPatterns;

    @Shadow
    private ItemStack bannerStack;

    protected LoomScreenFlagMixin(LoomMenu loomMenu, Inventory inventory, Component component) {
        super(loomMenu, inventory, component);
    }

    @WrapOperation(method = { "containerChanged" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;", ordinal = 0) })
    public Item containerChanged(ItemStack instance, Operation<Item> original) {
        return instance.getItem() instanceof FlagItem fi ? BannerBlock.byColor(fi.getColor()).asItem() : (Item) original.call(new Object[] { instance });
    }

    @Inject(method = { "renderBg" }, at = { @At("TAIL") })
    public void renderBg(GuiGraphics graphics, float ticks, int mouseX, int mouseY, CallbackInfo ci) {
        this.supplementaries$bannerFlagBG.render(this.f_97732_, graphics, ticks, this.f_97735_, this.f_97736_);
        if (this.resultBannerPatterns != null && !this.hasMaxPatterns && this.bannerStack.getItem() instanceof FlagItem) {
            int i = this.f_97735_;
            int j = this.f_97736_;
            MultiBufferSource.BufferSource renderTypeBuffer = this.f_96541_.renderBuffers().bufferSource();
            PoseStack pose = graphics.pose();
            pose.pushPose();
            pose.translate((double) i + 139.0, (double) j + 52.0, 0.0);
            pose.scale(24.0F, -24.0F, 1.0F);
            pose.translate(0.5, 0.5, 0.5);
            pose.mulPose(RotHlpr.Y90);
            pose.mulPose(RotHlpr.X90);
            pose.scale(1.125F, 1.125F, 1.125F);
            pose.translate(-1.0, -0.5, -1.1875);
            Lighting.setupForFlatItems();
            FlagBlockTileRenderer.renderPatterns(pose, renderTypeBuffer, this.resultBannerPatterns, 15728880);
            pose.popPose();
            renderTypeBuffer.endBatch();
            Lighting.setupFor3DItems();
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.supplementaries$bannerFlagBG.tick(ModTextures.BANNER_SLOT_ICONS);
    }
}