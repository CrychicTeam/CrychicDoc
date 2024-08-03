package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.client.gui.bestiary.ChangePageButton;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GuiMyrmexStaff extends Screen {

    private static final ResourceLocation JUNGLE_TEXTURE = new ResourceLocation("iceandfire:textures/gui/myrmex_staff_jungle.png");

    private static final ResourceLocation DESERT_TEXTURE = new ResourceLocation("iceandfire:textures/gui/myrmex_staff_desert.png");

    private static final WorldGenMyrmexHive.RoomType[] ROOMS = new WorldGenMyrmexHive.RoomType[] { WorldGenMyrmexHive.RoomType.FOOD, WorldGenMyrmexHive.RoomType.NURSERY, WorldGenMyrmexHive.RoomType.EMPTY };

    private static final int ROOMS_PER_PAGE = 5;

    private final List<GuiMyrmexStaff.Room> allRoomPos = Lists.newArrayList();

    private final List<MyrmexDeleteButton> allRoomButtonPos = Lists.newArrayList();

    public ChangePageButton previousPage;

    public ChangePageButton nextPage;

    int ticksSinceDeleted = 0;

    int currentPage = 0;

    private final boolean jungle;

    private int hiveCount;

    public GuiMyrmexStaff(ItemStack staff) {
        super(Component.translatable("myrmex_staff_screen"));
        this.jungle = staff.getItem() == IafItemRegistry.MYRMEX_JUNGLE_STAFF.get();
    }

    @Override
    protected void init() {
        super.init();
        this.f_169369_.clear();
        this.allRoomButtonPos.clear();
        int i = (this.f_96543_ - 248) / 2;
        int j = (this.f_96544_ - 166) / 2;
        int x_translate = 193;
        int y_translate = 37;
        if (ClientProxy.getReferedClientHive() != null) {
            this.populateRoomMap();
            this.m_7787_(Button.builder(ClientProxy.getReferedClientHive().reproduces ? Component.translatable("myrmex.message.disablebreeding") : Component.translatable("myrmex.message.enablebreeding"), p_214132_1_ -> {
                boolean opposite = !ClientProxy.getReferedClientHive().reproduces;
                ClientProxy.getReferedClientHive().reproduces = opposite;
            }).pos(i + 124, j + 15).size(120, 20).build());
            this.m_7787_(this.previousPage = new ChangePageButton(i + 5, j + 150, false, this.jungle ? 2 : 1, p_214132_1_ -> {
                if (this.currentPage > 0) {
                    this.currentPage--;
                }
            }));
            this.m_7787_(this.nextPage = new ChangePageButton(i + 225, j + 150, true, this.jungle ? 2 : 1, p_214132_1_ -> {
                if (this.currentPage < this.allRoomButtonPos.size() / 5) {
                    this.currentPage++;
                }
            }));
            int totalRooms = this.allRoomPos.size();
            for (int rooms = 0; rooms < this.allRoomPos.size(); rooms++) {
                int yIndex = rooms % 5;
                BlockPos pos = ((GuiMyrmexStaff.Room) this.allRoomPos.get(rooms)).pos;
                MyrmexDeleteButton button = new MyrmexDeleteButton(i + x_translate, j + y_translate + yIndex * 22, pos, Component.translatable("myrmex.message.delete"), p_214132_1_ -> {
                    if (this.ticksSinceDeleted <= 0) {
                        ClientProxy.getReferedClientHive().removeRoom(pos);
                        this.ticksSinceDeleted = 5;
                    }
                });
                button.f_93624_ = rooms < 5 * (this.currentPage + 1) && rooms >= 5 * this.currentPage;
                this.m_7787_(button);
                this.allRoomButtonPos.add(button);
            }
            if (totalRooms <= 5 * this.currentPage && this.currentPage > 0) {
                this.currentPage--;
            }
        }
    }

    private void populateRoomMap() {
        this.allRoomPos.clear();
        for (WorldGenMyrmexHive.RoomType type : ROOMS) {
            for (BlockPos pos : ClientProxy.getReferedClientHive().getRooms(type)) {
                String name = type == WorldGenMyrmexHive.RoomType.FOOD ? "food" : (type == WorldGenMyrmexHive.RoomType.NURSERY ? "nursery" : "misc");
                this.allRoomPos.add(new GuiMyrmexStaff.Room(pos, name));
            }
        }
        for (BlockPos pos : ClientProxy.getReferedClientHive().getEntrances().keySet()) {
            this.allRoomPos.add(new GuiMyrmexStaff.Room(pos, "enterance_surface"));
        }
        for (BlockPos pos : ClientProxy.getReferedClientHive().getEntranceBottoms().keySet()) {
            this.allRoomPos.add(new GuiMyrmexStaff.Room(pos, "enterance_bottom"));
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics ms) {
        super.renderBackground(ms);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
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
        if (this.ticksSinceDeleted > 0) {
            this.ticksSinceDeleted--;
        }
        this.hiveCount = 0;
        for (int rooms = 0; rooms < this.allRoomButtonPos.size(); rooms++) {
            if (rooms < 5 * (this.currentPage + 1) && rooms >= 5 * this.currentPage) {
                this.drawRoomInfo(ms, ((GuiMyrmexStaff.Room) this.allRoomPos.get(rooms)).string, ((GuiMyrmexStaff.Room) this.allRoomPos.get(rooms)).pos, i, j, color);
            }
        }
        if (ClientProxy.getReferedClientHive() != null) {
            if (!ClientProxy.getReferedClientHive().colonyName.isEmpty()) {
                String title = I18n.get("myrmex.message.colony_named", ClientProxy.getReferedClientHive().colonyName);
                this.getMinecraft().font.drawInBatch(title, (float) (i + 40 - title.length() / 2), (float) (j - 3), color, false, ms.pose().last().pose(), ms.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            } else {
                this.getMinecraft().font.drawInBatch(I18n.get("myrmex.message.colony"), (float) (i + 80), (float) (j - 3), color, false, ms.pose().last().pose(), ms.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            }
            int opinion = ClientProxy.getReferedClientHive().getPlayerReputation(Minecraft.getInstance().player.m_20148_());
            this.getMinecraft().font.drawInBatch(I18n.get("myrmex.message.hive_opinion", opinion), (float) i, (float) (j + 12), color, false, ms.pose().last().pose(), ms.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            this.getMinecraft().font.drawInBatch(I18n.get("myrmex.message.rooms"), (float) i, (float) (j + 25), color, false, ms.pose().last().pose(), ms.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }

    @Override
    public void removed() {
        if (ClientProxy.getReferedClientHive() != null) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageGetMyrmexHive(ClientProxy.getReferedClientHive().toNBT()));
        }
    }

    private void drawRoomInfo(GuiGraphics ms, String type, BlockPos pos, int i, int j, int color) {
        String translate = "myrmex.message.room." + type;
        this.getMinecraft().font.drawInBatch(I18n.get(translate, pos.m_123341_(), pos.m_123342_(), pos.m_123343_()), (float) i, (float) (j + 36 + this.hiveCount * 22), color, false, ms.pose().last().pose(), ms.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        this.hiveCount++;
    }

    private class Room {

        public BlockPos pos;

        public String string;

        public Room(BlockPos pos, String string) {
            this.pos = pos;
            this.string = string;
        }
    }
}