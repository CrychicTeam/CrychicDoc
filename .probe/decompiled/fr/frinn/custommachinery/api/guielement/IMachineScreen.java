package fr.frinn.custommachinery.api.guielement;

import fr.frinn.custommachinery.api.machine.ICustomMachine;
import fr.frinn.custommachinery.api.machine.MachineTile;

public interface IMachineScreen {

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    MachineTile getTile();

    ICustomMachine getMachine();
}