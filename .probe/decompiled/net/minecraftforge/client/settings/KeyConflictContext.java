package net.minecraftforge.client.settings;

import net.minecraft.client.Minecraft;

public enum KeyConflictContext implements IKeyConflictContext {

    UNIVERSAL {

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    }
    , GUI {

        @Override
        public boolean isActive() {
            return Minecraft.getInstance().screen != null;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return this == other;
        }
    }
    , IN_GAME {

        @Override
        public boolean isActive() {
            return !GUI.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return this == other;
        }
    }

}