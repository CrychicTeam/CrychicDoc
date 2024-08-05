package fr.frinn.custommachinery.common.util.transfer;

import fr.frinn.custommachinery.impl.component.config.RelativeSide;
import fr.frinn.custommachinery.impl.component.config.SideMode;

public interface ICommonEnergyHandler {

    void configChanged(RelativeSide var1, SideMode var2, SideMode var3);

    void invalidate();

    void tick();
}