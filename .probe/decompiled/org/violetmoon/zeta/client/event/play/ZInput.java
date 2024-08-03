package org.violetmoon.zeta.client.event.play;

import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZInput extends IZetaPlayEvent {

    public interface Key extends ZInput {

        int getKey();

        int getScanCode();

        int getAction();
    }

    public interface MouseButton extends ZInput {

        int getButton();

        int getAction();
    }
}