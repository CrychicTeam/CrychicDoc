package noppes.npcs.client.gui.player.companion;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.constants.EnumCompanionJobs;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCompanionOpenInv;
import noppes.npcs.packets.server.SPacketNpcRoleGet;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiMenuTopIconButton;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class GuiNpcCompanionStats extends GuiNPCInterface implements IGuiData {

    private RoleCompanion role;

    private boolean isEating = false;

    public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    public GuiNpcCompanionStats(EntityNPCInterface npc) {
        super(npc);
        this.role = (RoleCompanion) npc.role;
        this.setBackground("companion.png");
        this.imageWidth = 171;
        this.imageHeight = 166;
        Packets.sendServer(new SPacketNpcRoleGet());
    }

    @Override
    public void init() {
        super.m_7856_();
        int y = this.guiTop + 10;
        this.addLabel(new GuiLabel(0, NoppesStringUtils.translate("gui.name", ": ", this.npc.display.getName()), this.guiLeft + 4, y));
        String var10004 = NoppesStringUtils.translate("companion.owner", ": ", this.role.ownerName);
        int var10005 = this.guiLeft + 4;
        y += 12;
        this.addLabel(new GuiLabel(1, var10004, var10005, y));
        var10004 = NoppesStringUtils.translate("companion.age", ": ", this.role.ticksActive / 18000L + " (", this.role.stage.name, ")");
        var10005 = this.guiLeft + 4;
        y += 12;
        this.addLabel(new GuiLabel(2, var10004, var10005, y));
        var10004 = NoppesStringUtils.translate("companion.strength", ": ", this.npc.stats.melee.getStrength());
        var10005 = this.guiLeft + 4;
        y += 12;
        this.addLabel(new GuiLabel(3, var10004, var10005, y));
        var10004 = NoppesStringUtils.translate("companion.level", ": ", this.role.getTotalLevel());
        var10005 = this.guiLeft + 4;
        y += 12;
        this.addLabel(new GuiLabel(4, var10004, var10005, y));
        var10004 = NoppesStringUtils.translate("job.name", ": ", "gui.none");
        var10005 = this.guiLeft + 4;
        y += 12;
        this.addLabel(new GuiLabel(5, var10004, var10005, y));
        addTopMenu(this.role, this, 1);
    }

    public static void addTopMenu(RoleCompanion role, Screen screen, int active) {
        if (screen instanceof GuiNPCInterface gui) {
            GuiMenuTopIconButton button;
            gui.addTopButton(button = new GuiMenuTopIconButton(gui, 1, gui.guiLeft + 4, gui.guiTop - 27, "menu.stats", new ItemStack(Items.BOOK)));
            GuiMenuTopIconButton var6;
            gui.addTopButton(var6 = new GuiMenuTopIconButton(gui, 2, button, "companion.talent", new ItemStack(Items.NETHER_STAR)));
            if (role.hasInv()) {
                gui.addTopButton(var6 = new GuiMenuTopIconButton(gui, 3, var6, "inv.inventory", new ItemStack(Blocks.CHEST)));
            }
            if (role.companionJobInterface.getType() != EnumCompanionJobs.NONE) {
                gui.addTopButton(new GuiMenuTopIconButton(gui, 4, var6, "job.name", new ItemStack(Items.CARROT)));
            }
            gui.getTopButton(active).active = true;
        }
        if (screen instanceof GuiContainerNPCInterface gui) {
            GuiMenuTopIconButton buttonx;
            gui.addTopButton(buttonx = new GuiMenuTopIconButton(gui, 1, gui.guiLeft + 4, gui.guiTop - 27, "menu.stats", new ItemStack(Items.BOOK)));
            GuiMenuTopIconButton var8;
            gui.addTopButton(var8 = new GuiMenuTopIconButton(gui, 2, buttonx, "companion.talent", new ItemStack(Items.NETHER_STAR)));
            if (role.hasInv()) {
                gui.addTopButton(var8 = new GuiMenuTopIconButton(gui, 3, var8, "inv.inventory", new ItemStack(Blocks.CHEST)));
            }
            if (role.companionJobInterface.getType() != EnumCompanionJobs.NONE) {
                gui.addTopButton(new GuiMenuTopIconButton(gui, 4, var8, "job.name", new ItemStack(Items.CARROT)));
            }
            gui.getTopButton(active).active = true;
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 2) {
            CustomNpcs.proxy.openGui(this.npc, EnumGuiType.CompanionTalent);
        }
        if (id == 3) {
            Packets.sendServer(new SPacketCompanionOpenInv());
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        if (this.isEating && !this.role.isEating()) {
            Packets.sendServer(new SPacketNpcRoleGet());
        }
        this.isEating = this.role.isEating();
        super.drawNpc(graphics, 34, 150);
        int y = this.drawHealth(graphics, this.guiTop + 88);
    }

    private int drawHealth(GuiGraphics graphics, int y) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
        int max = this.role.getTotalArmorValue();
        if (this.role.talents.containsKey(EnumCompanionTalent.ARMOR) || max > 0) {
            for (int i = 0; i < 10; i++) {
                int x = this.guiLeft + 66 + i * 10;
                if (i * 2 + 1 < max) {
                    graphics.blit(GUI_ICONS_LOCATION, x, y, 34, 9, 9, 9);
                }
                if (i * 2 + 1 == max) {
                    graphics.blit(GUI_ICONS_LOCATION, x, y, 25, 9, 9, 9);
                }
                if (i * 2 + 1 > max) {
                    graphics.blit(GUI_ICONS_LOCATION, x, y, 16, 9, 9, 9);
                }
            }
            y += 10;
        }
        max = Mth.ceil(this.npc.m_21233_());
        int k = (int) this.npc.m_21223_();
        float scale = 1.0F;
        if (max > 40) {
            scale = (float) max / 40.0F;
            k = (int) ((float) k / scale);
            max = 40;
        }
        for (int i = 0; i < max; i++) {
            int xx = this.guiLeft + 66 + i % 20 * 5;
            int offset = i / 20 * 10;
            graphics.blit(GUI_ICONS_LOCATION, xx, y + offset, 52 + i % 2 * 5, 9, i % 2 == 1 ? 4 : 5, 9);
            if (k > i) {
                graphics.blit(GUI_ICONS_LOCATION, xx, y + offset, 52 + i % 2 * 5, 0, i % 2 == 1 ? 4 : 5, 9);
            }
        }
        k = this.role.foodstats.getFoodLevel();
        y += 10;
        if (max > 20) {
            y += 10;
        }
        for (int ix = 0; ix < 20; ix++) {
            int xx = this.guiLeft + 66 + ix % 20 * 5;
            graphics.blit(GUI_ICONS_LOCATION, xx, y, 16 + ix % 2 * 5, 27, ix % 2 == 1 ? 4 : 5, 9);
            if (k > ix) {
                graphics.blit(GUI_ICONS_LOCATION, xx, y, 52 + ix % 2 * 5, 27, ix % 2 == 1 ? 4 : 5, 9);
            }
        }
        return y;
    }

    @Override
    public void save() {
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.role.load(compound);
    }
}