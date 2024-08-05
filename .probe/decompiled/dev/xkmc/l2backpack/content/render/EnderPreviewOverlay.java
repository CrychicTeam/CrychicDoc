package dev.xkmc.l2backpack.content.render;

import dev.xkmc.l2backpack.content.capability.PickupMode;
import dev.xkmc.l2backpack.content.drawer.DrawerBlockEntity;
import dev.xkmc.l2backpack.content.remote.drawer.EnderDrawerBlockEntity;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestBlockEntity;
import dev.xkmc.l2backpack.events.TooltipUpdateEvents;
import dev.xkmc.l2backpack.init.data.LangData;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class EnderPreviewOverlay implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, GuiGraphics g, float partialTick, int screenWidth, int screenHeight) {
        LocalPlayer player = Proxy.getClientPlayer();
        BlockHitResult ray = RayTraceUtil.rayTraceBlock(player.m_9236_(), player, player.getBlockReach());
        if (ray.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ray.getBlockPos();
            BlockEntity entity = player.m_9236_().getBlockEntity(pos);
            int count = 0;
            Item item = Items.AIR;
            if (entity instanceof EnderDrawerBlockEntity drawer) {
                count = TooltipUpdateEvents.getCount(drawer.owner_id, drawer.item);
                item = drawer.getItem();
            } else if (entity instanceof DrawerBlockEntity drawer) {
                count = drawer.handler.count;
                item = drawer.getItem();
            }
            if (item != Items.AIR) {
                gui.setupOverlayRenderState(true, false);
                Component text = LangData.IDS.DRAWER_CONTENT.get(item.getDescription(), count < 0 ? "???" : count);
                renderText(gui, g, screenWidth / 2, screenHeight / 2 + 16, text);
            }
            if (entity instanceof WorldChestBlockEntity be && be.config != null && be.config.pickup() != PickupMode.NONE) {
                int off = 9;
                renderText(gui, g, screenWidth / 2, screenHeight / 2 + 16, be.config.pickup().getTooltip());
                renderText(gui, g, screenWidth / 2, screenHeight / 2 + 16 + off, be.config.destroy().getTooltip());
            }
        }
    }

    private static void renderText(ForgeGui gui, GuiGraphics g, int x, int y, Component text) {
        Font font = gui.m_93082_();
        x -= font.width(text) / 2;
        g.drawString(font, text, x, y, -1);
    }
}