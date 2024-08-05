package noppes.npcs.controllers.data;

import java.util.ArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class PlayerMailData {

    public ArrayList<PlayerMail> playermail = new ArrayList();

    public void loadNBTData(CompoundTag compound) {
        ArrayList<PlayerMail> newmail = new ArrayList();
        ListTag list = compound.getList("MailData", 10);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                PlayerMail mail = new PlayerMail();
                mail.readNBT(list.getCompound(i));
                newmail.add(mail);
            }
            this.playermail = newmail;
        }
    }

    public CompoundTag saveNBTData(CompoundTag compound) {
        ListTag list = new ListTag();
        for (PlayerMail mail : this.playermail) {
            list.add(mail.writeNBT());
        }
        compound.put("MailData", list);
        return compound;
    }

    public boolean hasMail() {
        for (PlayerMail mail : this.playermail) {
            if (!mail.beenRead) {
                return true;
            }
        }
        return false;
    }
}