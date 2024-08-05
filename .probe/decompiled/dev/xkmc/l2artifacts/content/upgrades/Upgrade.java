package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public class Upgrade {

    @SerialField
    public int main;

    @SerialField
    public int sub;

    @SerialField
    public ArrayList<ResourceLocation> stats = new ArrayList();

    public boolean removeMain() {
        if (this.main > 0) {
            this.main--;
            return true;
        } else {
            return false;
        }
    }

    public boolean removeSub() {
        if (this.sub > 0) {
            this.sub--;
            return true;
        } else {
            return false;
        }
    }

    public void add(Upgrade.Type type) {
        if (type == Upgrade.Type.BOOST_MAIN_STAT) {
            this.main++;
        }
        if (type == Upgrade.Type.BOOST_SUB_STAT) {
            this.sub++;
        }
    }

    public static enum Type {

        BOOST_MAIN_STAT, BOOST_SUB_STAT, SET_SUB_STAT
    }
}