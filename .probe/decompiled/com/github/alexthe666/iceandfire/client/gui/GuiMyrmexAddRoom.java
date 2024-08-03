package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GuiMyrmexAddRoom extends Screen {

    private static final ResourceLocation JUNGLE_TEXTURE = new ResourceLocation("iceandfire:textures/gui/myrmex_staff_jungle.png");

    private static final ResourceLocation DESERT_TEXTURE = new ResourceLocation("iceandfire:textures/gui/myrmex_staff_desert.png");

    private final boolean jungle;

    private final BlockPos interactPos;

    private final Direction facing;

    public GuiMyrmexAddRoom(ItemStack staff, BlockPos interactPos, Direction facing) {
        super(Component.translatable("myrmex_add_room"));
        this.jungle = staff.getItem() == IafItemRegistry.MYRMEX_JUNGLE_STAFF.get();
        this.interactPos = interactPos;
        this.facing = facing;
        this.init();
    }

    public static void onGuiClosed() {
        IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageGetMyrmexHive(ClientProxy.getReferedClientHive().toNBT()));
    }

    @Override
    protected void init() {
        super.init();
        this.f_169369_.clear();
        int i = (this.f_96543_ - 248) / 2;
        int j = (this.f_96544_ - 166) / 2;
        if (ClientProxy.getReferedClientHive() != null) {
            Player player = Minecraft.getInstance().player;
            this.m_7787_(Button.builder(Component.translatable("myrmex.message.establishroom_food"), p_214132_1_ -> {
                ClientProxy.getReferedClientHive().addRoomWithMessage(player, this.interactPos, WorldGenMyrmexHive.RoomType.FOOD);
                onGuiClosed();
                Minecraft.getInstance().setScreen(null);
            }).pos(i + 50, j + 35).size(150, 20).build());
            this.m_7787_(Button.builder(Component.translatable("myrmex.message.establishroom_nursery"), p_214132_1_ -> {
                ClientProxy.getReferedClientHive().addRoomWithMessage(player, this.interactPos, WorldGenMyrmexHive.RoomType.NURSERY);
                onGuiClosed();
                Minecraft.getInstance().setScreen(null);
            }).pos(i + 50, j + 60).size(150, 20).build());
            this.m_7787_(Button.builder(Component.translatable("myrmex.message.establishroom_enterance_surface"), p_214132_1_ -> {
                ClientProxy.getReferedClientHive().addEnteranceWithMessage(player, false, this.interactPos, this.facing);
                onGuiClosed();
                Minecraft.getInstance().setScreen(null);
            }).pos(i + 50, j + 85).size(150, 20).build());
            this.m_7787_(Button.builder(Component.translatable("myrmex.message.establishroom_enterance_bottom"), p_214132_1_ -> {
                ClientProxy.getReferedClientHive().addEnteranceWithMessage(player, true, this.interactPos, this.facing);
                onGuiClosed();
                Minecraft.getInstance().setScreen(null);
            }).pos(i + 50, j + 110).size(150, 20).build());
            this.m_7787_(Button.builder(Component.translatable("myrmex.message.establishroom_misc"), p_214132_1_ -> {
                ClientProxy.getReferedClientHive().addRoomWithMessage(player, this.interactPos, WorldGenMyrmexHive.RoomType.EMPTY);
                onGuiClosed();
                Minecraft.getInstance().setScreen(null);
            }).pos(i + 50, j + 135).size(150, 20).build());
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics ms) {
        super.renderBackground(ms);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindForSetup(this.jungle ? JUNGLE_TEXTURE : DESERT_TEXTURE);
        int i = (this.f_96543_ - 248) / 2;
        int j = (this.f_96544_ - 166) / 2;
        ms.blit(this.jungle ? JUNGLE_TEXTURE : DESERT_TEXTURE, i, j, 0, 0, 248, 166);
    }

    @Override
    public void render(@NotNull GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        this.init();
        int i = (this.f_96543_ - 248) / 2 + 10;
        int j = (this.f_96544_ - 166) / 2 + 8;
        super.render(ms, mouseX, mouseY, partialTicks);
        int color = this.jungle ? 3533333 : 16760576;
        if (ClientProxy.getReferedClientHive() != null) {
            if (!ClientProxy.getReferedClientHive().colonyName.isEmpty()) {
                String title = I18n.get("myrmex.message.colony_named", ClientProxy.getReferedClientHive().colonyName);
                this.getMinecraft().font.drawInBatch(title, (float) (i + 40 - title.length() / 2), (float) (j - 3), color, false, ms.pose().last().pose(), ms.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            } else {
                this.getMinecraft().font.drawInBatch(I18n.get("myrmex.message.colony"), (float) (i + 80), (float) (j - 3), color, false, ms.pose().last().pose(), ms.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            }
            this.getMinecraft().font.drawInBatch(I18n.get("myrmex.message.create_new_room", this.interactPos.m_123341_(), this.interactPos.m_123342_(), this.interactPos.m_123343_()), (float) (i + 30), (float) (j + 6), color, false, ms.pose().last().pose(), ms.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}