package net.zanckor.questapi.example.client.screen.dialog;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.zanckor.questapi.api.screen.AbstractDialog;
import net.zanckor.questapi.api.screen.NpcType;
import net.zanckor.questapi.example.client.screen.button.TextButton;
import net.zanckor.questapi.mod.common.network.SendQuestPacket;
import net.zanckor.questapi.mod.common.network.packet.dialogoption.AddQuest;
import net.zanckor.questapi.mod.common.network.packet.dialogoption.DialogRequestPacket;
import net.zanckor.questapi.mod.common.network.packet.screen.OpenVanillaEntityScreen;
import net.zanckor.questapi.mod.common.util.MCUtilClient;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogOption;

public class MinimalistDialogScreen extends AbstractDialog {

    int dialogID;

    String text;

    int textDisplayDelay;

    int textDisplaySize;

    int optionSize;

    HashMap<Integer, List<Integer>> optionIntegers;

    HashMap<Integer, List<String>> optionStrings;

    double xScreenPos;

    double yScreenPos;

    double scale;

    int imageWidth;

    int imageHeight;

    int xButtonPosition;

    int yButtonPosition;

    Entity entity;

    UUID npcUUID;

    String resourceLocation;

    Item item;

    NpcType npcType;

    private static final ResourceLocation DIALOG = new ResourceLocation("questapi", "textures/gui/minimalist_dialog_background.png");

    public MinimalistDialogScreen(Component component) {
        super(component);
    }

    @Override
    public Screen modifyScreen(int dialogID, String text, int optionSize, HashMap<Integer, List<Integer>> optionIntegers, HashMap<Integer, List<String>> optionStrings, UUID npcUUID, String resourceLocation, Item item, NpcType npcType) {
        this.dialogID = dialogID;
        this.text = I18n.get(text);
        this.optionSize = optionSize;
        this.optionIntegers = optionIntegers;
        this.optionStrings = optionStrings;
        this.npcType = npcType;
        switch(npcType) {
            case RESOURCE_LOCATION:
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(resourceLocation));
                this.entity = entityType.create(Minecraft.getInstance().level);
                this.resourceLocation = resourceLocation;
                break;
            case UUID:
                this.entity = MCUtilClient.getEntityByUUID(npcUUID);
                break;
            case ITEM:
                this.item = item;
        }
        this.npcUUID = npcUUID;
        return this;
    }

    @Override
    protected void init() {
        super.m_7856_();
        this.textDisplaySize = 0;
        this.imageWidth = (int) ((double) this.f_96543_ / 1.35);
        this.imageHeight = this.f_96543_ / 8;
        this.xScreenPos = (double) this.imageWidth / 1.5;
        this.yScreenPos = (double) this.f_96543_ / 3.0;
        this.scale = (double) ((float) this.f_96543_ / 700.0F);
        this.xButtonPosition = (int) (this.xScreenPos - (double) (this.imageWidth / 3));
        this.yButtonPosition = (int) (this.yScreenPos * 1.225);
        for (int i = 0; i < this.optionSize; i++) {
            int stringLength = (int) ((double) ((String) ((List) this.optionStrings.get(i)).get(0)).length() * 2.6 * this.scale);
            int index = i;
            if ((double) (this.xButtonPosition + stringLength) > (double) this.f_96543_ / 1.3) {
                this.yButtonPosition = (int) ((double) this.yButtonPosition + 20.0 * this.scale);
                this.xButtonPosition = (int) (this.xScreenPos - (double) this.imageWidth / 2.75);
            }
            this.m_142416_(new TextButton(this.xButtonPosition, this.yButtonPosition, stringLength, 20, (float) this.f_96543_ / 800.0F, Component.literal(I18n.get((String) ((List) this.optionStrings.get(i)).get(0))).withStyle(ChatFormatting.WHITE), 26, button -> this.button(index)));
            this.xButtonPosition = (int) ((double) this.xButtonPosition + (double) (stringLength + 25) * this.scale);
        }
        this.m_142416_(new TextButton((int) ((double) this.imageWidth * 1.4), (int) ((double) this.imageHeight * 1.1), 20, 20, (float) this.f_96543_ / 675.0F, Component.literal("↩"), 26, button -> {
            if (this.npcUUID != null) {
                SendQuestPacket.TO_SERVER(new OpenVanillaEntityScreen(this.npcUUID));
            }
        }));
    }

    @Override
    public void tick() {
        super.m_86600_();
        if (this.textDisplaySize < this.text.length()) {
            if (this.textDisplayDelay == 0) {
                MCUtilClient.playSound(SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, 0.975F, 1.025F);
                this.textDisplaySize++;
                if (this.textDisplaySize < this.text.length()) {
                    String var1 = Character.toString(this.text.charAt(this.textDisplaySize));
                    switch(var1) {
                        case ".":
                        case "?":
                        case "!":
                            this.textDisplayDelay = 9;
                            break;
                        case ",":
                            this.textDisplayDelay = 5;
                    }
                }
            } else {
                this.textDisplayDelay--;
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int xPosition = this.f_96543_ / 4;
        int yPosition = (int) (this.yScreenPos + this.yScreenPos / 16.0);
        PoseStack poseStack = graphics.pose();
        graphics.setColor(1.0F, 1.0F, 1.0F, 0.5F);
        graphics.blit(DIALOG, (int) (this.xScreenPos - (double) (this.imageWidth / 2)), (int) this.yScreenPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        MCUtilClient.renderLine(graphics, poseStack, 80, (float) xPosition, (float) yPosition, (float) this.f_96543_ / 675.0F, 16.0F, Component.literal(this.text.substring(0, this.textDisplaySize)).withStyle(ChatFormatting.WHITE), this.f_96547_);
        switch(this.npcType) {
            case RESOURCE_LOCATION:
            case UUID:
                if (this.entity != null) {
                    MCUtilClient.renderEntity(this.xScreenPos / 2.7, this.yScreenPos * 1.325, (double) this.f_96543_ / 24.0, (this.xScreenPos / 2.7 - (double) mouseX) / 4.0, (this.yScreenPos * 1.3 - (double) mouseY) / 4.0, (LivingEntity) this.entity);
                }
                break;
            case ITEM:
                MCUtilClient.renderItem(this.item.getDefaultInstance(), (int) (this.xScreenPos / 2.65), (int) (this.yScreenPos * 1.185), (double) this.f_96543_ / 150.0, 0.0, poseStack);
        }
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    private void button(int optionID) {
        EnumDialogOption optionType = EnumDialogOption.valueOf((String) ((List) this.optionStrings.get(optionID)).get(1));
        switch(optionType) {
            case OPEN_DIALOG:
            case CLOSE_DIALOG:
                SendQuestPacket.TO_SERVER(new DialogRequestPacket(optionType, optionID, this.entity, this.item, this.npcType));
                break;
            case ADD_QUEST:
                SendQuestPacket.TO_SERVER(new AddQuest(optionType, optionID));
        }
    }

    @Override
    public boolean mouseClicked(double xPosition, double yPosition, int button) {
        this.textDisplaySize = this.text.length();
        return super.m_6375_(xPosition, yPosition, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}