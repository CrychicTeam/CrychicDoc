package dev.ftb.mods.ftblibrary.util;

import dev.ftb.mods.ftblibrary.core.CompoundContainerFTBL;
import net.minecraft.world.Container;

public final class ContainerKey {

    public final Container container;

    public ContainerKey(Container c) {
        this.container = c;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ContainerKey that = (ContainerKey) o;
            if (this.container instanceof CompoundContainerFTBL a && that.container instanceof CompoundContainerFTBL b) {
                Container a1 = a.getContainer1FTBL();
                Container a2 = a.getContainer2FTBL();
                Container b1 = b.getContainer1FTBL();
                Container b2 = b.getContainer2FTBL();
                return a1 == b1 && a2 == b2 || a1 == b2 && a2 == b1;
            }
            return this.container == that.container;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.container instanceof CompoundContainerFTBL c ? c.getContainer1FTBL().hashCode() ^ c.getContainer2FTBL().hashCode() : this.container.hashCode();
    }
}