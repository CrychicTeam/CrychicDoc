package net.zanckor.questapi.example.client.screen.dialog;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
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

public class DialogScreen extends AbstractDialog {

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

    private static final ResourceLocation DIALOG = new ResourceLocation("questapi", "textures/gui/dialog_background.png");

    private static final ResourceLocation BUTTON = new ResourceLocation("questapi", "textures/gui/dialog_button.png");

    public DialogScreen(Component component) {
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
        this.imageWidth = this.f_96543_ / 2;
        this.imageHeight = (int) ((double) this.f_96543_ / 2.7);
        this.xScreenPos = (double) (this.f_96543_ - this.imageWidth);
        this.yScreenPos = (double) this.f_96543_ / 11.0;
        this.scale = (double) ((float) this.f_96543_ / 675.0F);
        this.xButtonPosition = (int) ((double) this.f_96543_ / 3.55);
        this.yButtonPosition = (int) (this.yScreenPos * 3.6);
        for (int i = 0; i < this.optionSize; i++) {
            int stringLength = (((String) ((List) this.optionStrings.get(i)).get(0)).length() + 1) * 5;
            int index = i;
            if ((double) (this.xButtonPosition + stringLength) > (double) this.f_96543_ / 1.4) {
                this.xButtonPosition = (int) ((double) this.f_96543_ / 3.55);
                this.yButtonPosition = (int) ((double) this.yButtonPosition + 20.0 * this.scale);
            }
            this.m_142416_(new TextButton(this.xButtonPosition, this.yButtonPosition, (int) ((double) stringLength * this.scale), 20, (float) this.f_96543_ / 700.0F, Component.literal(I18n.get((String) ((List) this.optionStrings.get(i)).get(0))), 26, button -> this.button(index)));
            this.xButtonPosition = (int) ((double) this.xButtonPosition + (double) ((String) ((List) this.optionStrings.get(i)).get(0)).length() * 5.5 * this.scale);
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
        int xPosition = (int) ((double) this.f_96543_ / 2.41);
        int yPosition = (int) (this.yScreenPos + this.yScreenPos / 1.45);
        PoseStack poseStack = graphics.pose();
        graphics.blit(DIALOG, (int) (this.xScreenPos - (double) (this.imageWidth / 2)), (int) this.yScreenPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        MCUtilClient.renderText(graphics, poseStack, (double) xPosition, (double) yPosition, 26.0F, (float) this.f_96543_ / 675.0F, 42, this.text.substring(0, this.textDisplaySize), this.f_96547_);
        switch(this.npcType) {
            case RESOURCE_LOCATION:
            case UUID:
                if (this.entity != null) {
                    MCUtilClient.renderEntity(this.xScreenPos / 1.4575, this.yScreenPos * 3.41, (double) this.f_96543_ / 12.0, (this.xScreenPos / 1.4575 - (double) mouseX) / 4.0, (this.yScreenPos * 2.5 - (double) mouseY) / 4.0, (LivingEntity) this.entity);
                }
                break;
            case ITEM:
                MCUtilClient.renderItem(this.item.getDefaultInstance(), (int) (this.xScreenPos / 1.4575), (int) (this.yScreenPos * 2.5), (double) this.f_96543_ / 150.0, 0.0, poseStack);
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