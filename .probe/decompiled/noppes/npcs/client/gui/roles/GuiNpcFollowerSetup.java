package noppes.npcs.client.gui.roles;

import java.util.HashMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.containers.ContainerNPCFollowerSetup;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpcRoleSave;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiButtonYesNo;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;

public class GuiNpcFollowerSetup extends GuiContainerNPCInterface2<ContainerNPCFollowerSetup> {

    private RoleFollower role;

    private static final ResourceLocation field_110422_t = new ResourceLocation("textures/gui/followersetup.png");

    public GuiNpcFollowerSetup(ContainerNPCFollowerSetup container, Inventory inv, Component titleIn) {
        super(NoppesUtil.getLastNpc(), container, inv, titleIn);
        this.f_97727_ = 200;
        this.role = (RoleFollower) this.npc.role;
        this.setBackground("followersetup.png");
    }

    @Override
    public void init() {
        super.init();
        for (int i = 0; i < 3; i++) {
            int x = this.guiLeft + 66;
            int y = this.guiTop + 37;
            y += i * 25;
            GuiTextFieldNop tf = new GuiTextFieldNop(i, this, x, y, 24, 20, "1");
            tf.numbersOnly = true;
            tf.setMinMaxDefault(1, Integer.MAX_VALUE, 1);
            this.addTextField(tf);
        }
        int i = 0;
        for (int day : this.role.rates.values()) {
            this.getTextField(i).m_94144_(day + "");
            i++;
        }
        Component text = Component.translatable("follower.hireText").append(" {days} ").append(Component.translatable("follower.days"));
        if (!this.role.dialogHire.isEmpty()) {
            text = Component.translatable(this.role.dialogHire);
        }
        this.addTextField(new GuiTextFieldNop(3, this, this.guiLeft + 100, this.guiTop + 6, 286, 20, text));
        text = Component.translatable("follower.farewellText").append(" {player}");
        if (!this.role.dialogFarewell.isEmpty()) {
            text = Component.translatable(this.role.dialogFarewell);
        }
        this.addTextField(new GuiTextFieldNop(4, this, this.guiLeft + 100, this.guiTop + 30, 286, 20, text));
        this.addLabel(new GuiLabel(7, "follower.infiniteDays", this.guiLeft + 180, this.guiTop + 80));
        this.addButton(new GuiButtonYesNo(this, 7, this.guiLeft + 260, this.guiTop + 75, this.role.infiniteDays));
        this.addLabel(new GuiLabel(8, "follower.guiDisabled", this.guiLeft + 180, this.guiTop + 104));
        this.addButton(new GuiButtonYesNo(this, 8, this.guiLeft + 260, this.guiTop + 99, this.role.disableGui));
        this.addLabel(new GuiLabel(9, "follower.allowSoulstone", this.guiLeft + 180, this.guiTop + 128));
        this.addButton(new GuiButtonYesNo(this, 9, this.guiLeft + 260, this.guiTop + 123, !this.role.refuseSoulStone));
        this.addButton(new GuiButtonNop(this, 10, this.guiLeft + 195, this.guiTop + 147, 100, 20, "gui.reset"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 7) {
            this.role.infiniteDays = ((GuiButtonYesNo) guibutton).getBoolean();
        }
        if (guibutton.id == 8) {
            this.role.disableGui = ((GuiButtonYesNo) guibutton).getBoolean();
        }
        if (guibutton.id == 9) {
            this.role.refuseSoulStone = !((GuiButtonYesNo) guibutton).getBoolean();
        }
        if (guibutton.id == 10) {
            this.role.killed();
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
    }

    @Override
    public void save() {
        HashMap<Integer, Integer> map = new HashMap();
        for (int i = 0; i < this.role.inventory.getContainerSize(); i++) {
            ItemStack item = this.role.inventory.getItem(i);
            if (item != null && !item.isEmpty()) {
                int days = 1;
                if (!this.getTextField(i).isEmpty() && this.getTextField(i).isInteger()) {
                    days = this.getTextField(i).getInteger();
                }
                if (days <= 0) {
                    days = 1;
                }
                map.put(i, days);
            }
        }
        this.role.rates = map;
        this.role.dialogHire = this.getTextField(3).m_94155_();
        this.role.dialogFarewell = this.getTextField(4).m_94155_();
        Packets.sendServer(new SPacketNpcRoleSave(this.role.save(new CompoundTag())));
    }
}