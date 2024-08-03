package noppes.npcs.client.gui.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.ModelData;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.mainmenu.GuiNpcDisplay;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMenuSave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiSliderNop;
import noppes.npcs.shared.client.gui.listeners.ISliderListener;

public abstract class GuiCreationScreenInterface extends GuiNPCInterface implements ISliderListener {

    public static String Message = "";

    public LivingEntity entity;

    private boolean saving = false;

    protected boolean hasSaving = true;

    public int active = 0;

    private Player player;

    public int xOffset = 0;

    public ModelData playerdata;

    protected CompoundTag original = new CompoundTag();

    private static float rotation = 0.5F;

    public GuiCreationScreenInterface(EntityNPCInterface npc) {
        super(npc);
        this.playerdata = ((EntityCustomNpc) npc).modelData;
        this.original = this.playerdata.save();
        this.imageWidth = 400;
        this.imageHeight = 240;
        this.xOffset = 140;
        this.player = Minecraft.getInstance().player;
        this.drawDefaultBackground = true;
    }

    @Override
    public void init() {
        super.m_7856_();
        this.entity = this.playerdata.getEntity(this.npc);
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 62, this.guiTop, 60, 20, "gui.entity") {

            @Override
            public void onClick(double x, double y) {
                GuiCreationScreenInterface.this.openGui(new GuiCreationEntities(GuiCreationScreenInterface.this.npc));
            }
        });
        if (this.entity == null) {
            this.addButton(new GuiButtonNop(this, 2, this.guiLeft, this.guiTop + 23, 60, 20, "gui.parts") {

                @Override
                public void onClick(double x, double y) {
                }
            });
        } else if (!(this.entity instanceof EntityNPCInterface)) {
            GuiCreationExtra gui = new GuiCreationExtra(this.npc);
            gui.playerdata = this.playerdata;
            if (!gui.getData(this.entity).isEmpty()) {
                this.addButton(new GuiButtonNop(this, 2, this.guiLeft, this.guiTop + 23, 60, 20, "gui.extra") {

                    @Override
                    public void onClick(double x, double y) {
                        GuiCreationScreenInterface.this.openGui(new GuiCreationExtra(GuiCreationScreenInterface.this.npc));
                    }
                });
            } else if (this.active == 2) {
                this.f_96541_.setScreen(new GuiCreationEntities(this.npc));
                return;
            }
        }
        if (this.entity == null) {
            this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 62, this.guiTop + 23, 60, 20, "gui.scale") {

                @Override
                public void onClick(double x, double y) {
                    GuiCreationScreenInterface.this.openGui(new GuiCreationScale(GuiCreationScreenInterface.this.npc));
                }
            });
        }
        if (this.hasSaving) {
            this.addButton(new GuiButtonNop(this, 4, this.guiLeft, this.guiTop + this.imageHeight - 24, 60, 20, "gui.save") {

                @Override
                public void onClick(double x, double y) {
                    GuiCreationScreenInterface.this.setSubGui(new GuiPresetSave(GuiCreationScreenInterface.this, GuiCreationScreenInterface.this.playerdata));
                }
            });
            this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 62, this.guiTop + this.imageHeight - 24, 60, 20, "gui.load") {

                @Override
                public void onClick(double x, double y) {
                    GuiCreationScreenInterface.this.openGui(new GuiCreationLoad(GuiCreationScreenInterface.this.npc));
                }
            });
        }
        if (this.getButton(this.active) == null) {
            this.openGui(new GuiCreationEntities(this.npc));
        } else {
            this.getButton(this.active).f_93623_ = false;
            this.addButton(new GuiButtonNop(this, 66, this.guiLeft + this.imageWidth - 20, this.guiTop, 20, 20, "X") {

                @Override
                public void onClick(double x, double y) {
                    GuiCreationScreenInterface.this.save();
                    NoppesUtil.openGUI(GuiCreationScreenInterface.this.player, new GuiNpcDisplay(GuiCreationScreenInterface.this.npc));
                }
            });
            this.addLabel(new GuiLabel(0, Message, this.guiLeft + 120, this.guiTop + this.imageHeight - 10, 16711680, this.imageWidth - 120, 20));
            this.addSlider(new GuiSliderNop(this, 500, this.guiLeft + this.xOffset + 142, this.guiTop + 210, 120, 20, rotation));
        }
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        if (!this.saving) {
            super.m_6375_(i, j, k);
        }
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float f) {
        super.m_88315_(graphics, x, y, f);
        this.entity = this.playerdata.getEntity(this.npc);
        LivingEntity entity = this.entity;
        if (entity == null) {
            LivingEntity var6 = this.npc;
        } else {
            EntityUtil.Copy(this.npc, entity);
        }
        this.drawNpc(graphics, this.npc, this.xOffset + 200, 200, 2.0F, (int) (-rotation * 360.0F - 180.0F));
    }

    @Override
    public void onClose() {
    }

    @Override
    public void save() {
        CompoundTag newCompound = this.playerdata.save();
        Packets.sendServer(new SPacketMenuSave(EnumMenuType.DISPLAY, this.npc.display.save(new CompoundTag())));
        Packets.sendServer(new SPacketMenuSave(EnumMenuType.MODEL, newCompound));
    }

    public void openGui(Screen gui) {
        this.f_96541_.setScreen(gui);
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        this.init();
    }

    @Override
    public void mouseDragged(GuiSliderNop slider) {
        if (slider.id == 500) {
            rotation = slider.sliderValue;
            slider.setString((int) (rotation * 360.0F) + "");
        }
    }

    @Override
    public void mousePressed(GuiSliderNop slider) {
    }

    @Override
    public void mouseReleased(GuiSliderNop slider) {
    }
}