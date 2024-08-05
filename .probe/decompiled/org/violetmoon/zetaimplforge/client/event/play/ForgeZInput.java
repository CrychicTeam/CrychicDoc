package org.violetmoon.zetaimplforge.client.event.play;

import net.minecraftforge.client.event.InputEvent;
import org.violetmoon.zeta.client.event.play.ZInput;

public class ForgeZInput implements ZInput {

    public static class Key extends ForgeZInput implements ZInput.Key {

        private final InputEvent.Key e;

        public Key(InputEvent.Key e) {
            this.e = e;
        }

        @Override
        public int getKey() {
            return this.e.getKey();
        }

        @Override
        public int getAction() {
            return this.e.getAction();
        }

        @Override
        public int getScanCode() {
            return this.e.getScanCode();
        }
    }

    public static class MouseButton extends ForgeZInput implements ZInput.MouseButton {

        private final InputEvent.MouseButton e;

        public MouseButton(InputEvent.MouseButton e) {
            this.e = e;
        }

        @Override
        public int getButton() {
            return this.e.getButton();
        }

        @Override
        public int getAction() {
            return this.e.getAction();
        }
    }
}