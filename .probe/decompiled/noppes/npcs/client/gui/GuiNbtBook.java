package noppes.npcs.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNbtBookBlockSave;
import noppes.npcs.packets.server.SPacketNbtBookEntitySave;
import noppes.npcs.shared.client.gui.GuiTextAreaScreen;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNbtBook extends GuiNPCInterface implements IGuiData {

    private BlockPos pos;

    private BlockEntity tile;

    private BlockState state;

    private ItemStack blockStack;

    private int entityId;

    private Entity entity;

    private CompoundTag originalCompound;

    private CompoundTag compound;

    private String faultyText = null;

    private String errorMessage = null;

    public GuiNbtBook(BlockPos pos) {
        this.pos = pos;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.m_7856_();
        int y = this.guiTop + 40;
        if (this.state != null) {
            this.addLabel(new GuiLabel(11, "x: " + this.pos.m_123341_() + ", y: " + this.pos.m_123342_() + ", z: " + this.pos.m_123343_(), this.guiLeft + 60, this.guiTop + 6));
            this.addLabel(new GuiLabel(12, "id: " + ForgeRegistries.BLOCKS.getKey(this.state.m_60734_()), this.guiLeft + 60, this.guiTop + 16));
        }
        if (this.entity != null) {
            this.addLabel(new GuiLabel(12, "id: " + this.entity.getType().getDescriptionId(), this.guiLeft + 60, this.guiTop + 6));
        }
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 38, this.guiTop + 144, 180, 20, "nbt.edit"));
        this.getButton(0).f_93623_ = this.compound != null && !this.compound.isEmpty();
        this.addLabel(new GuiLabel(0, "", this.guiLeft + 4, this.guiTop + 167));
        this.addLabel(new GuiLabel(1, "", this.guiLeft + 4, this.guiTop + 177));
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 128, this.guiTop + 190, 120, 20, "gui.close"));
        this.addButton(new GuiButtonNop(this, 67, this.guiLeft + 4, this.guiTop + 190, 120, 20, "gui.save"));
        if (this.errorMessage != null) {
            this.getButton(67).f_93623_ = false;
            int i = this.errorMessage.indexOf(" at: ");
            if (i > 0) {
                this.getLabel(0).m_93666_(Component.translatable(this.errorMessage.substring(0, i)));
                this.getLabel(1).m_93666_(Component.translatable(this.errorMessage.substring(i)));
            } else {
                this.getLabel(0).m_93666_(Component.translatable(this.errorMessage));
            }
        }
        if (this.getButton(67).f_93623_ && this.originalCompound != null) {
            this.getButton(67).f_93623_ = !this.originalCompound.equals(this.compound);
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            if (this.faultyText != null) {
                this.setSubGui(new GuiTextAreaScreen(this.compound.toString(), this.faultyText).enableHighlighting());
            } else {
                this.setSubGui(new GuiTextAreaScreen(this.compound.toString()).enableHighlighting());
            }
        }
        if (id == 67) {
            this.getLabel(0).m_93666_(Component.translatable("Saved"));
            if (this.compound.equals(this.originalCompound)) {
                return;
            }
            if (this.tile == null) {
                Packets.sendServer(new SPacketNbtBookEntitySave(this.entityId, this.compound));
                return;
            }
            Packets.sendServer(new SPacketNbtBookBlockSave(this.pos, this.compound));
            this.originalCompound = this.compound.copy();
            this.getButton(67).f_93623_ = false;
        }
        if (id == 66) {
            this.close();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        if (!this.hasSubGui()) {
            PoseStack matrixStack = graphics.pose();
            if (this.state != null) {
                matrixStack.pushPose();
                matrixStack.translate((float) (this.guiLeft + 4), (float) (this.guiTop + 4), 0.0F);
                matrixStack.scale(3.0F, 3.0F, 3.0F);
                graphics.renderItem(this.blockStack, 0, 0);
                graphics.renderItemDecorations(this.f_96547_, this.blockStack, 0, 0);
                matrixStack.popPose();
            }
            if (this.entity instanceof LivingEntity) {
                this.drawNpc(graphics, (LivingEntity) this.entity, 20, 80, 1.0F, 0);
            }
        }
    }

    @Override
    public void subGuiClosed(Screen gui) {
        if (gui instanceof GuiTextAreaScreen) {
            try {
                this.compound = TagParser.parseTag(((GuiTextAreaScreen) gui).text);
                this.errorMessage = this.faultyText = null;
            } catch (CommandSyntaxException var3) {
                this.errorMessage = var3.getLocalizedMessage();
                this.faultyText = ((GuiTextAreaScreen) gui).text;
            }
            this.init();
        }
    }

    @Override
    public void save() {
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        if (compound.contains("EntityId")) {
            this.entityId = compound.getInt("EntityId");
            this.entity = this.player.m_9236_().getEntity(this.entityId);
        } else {
            this.tile = this.player.m_9236_().getBlockEntity(this.pos);
            this.state = this.player.m_9236_().getBlockState(this.pos);
            this.blockStack = this.state.m_60734_().getCloneItemStack(this.player.m_9236_(), this.pos, this.state);
        }
        this.originalCompound = compound.getCompound("Data");
        this.compound = this.originalCompound.copy();
        this.init();
    }
}