package mezz.jei.common.input.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public enum JeiKeyModifier {

    CONTROL_OR_COMMAND {

        @Override
        public boolean isActive(JeiKeyConflictContext context) {
            return Screen.hasControlDown();
        }

        @Override
        public Component getCombinedName(InputConstants.Key key) {
            return Minecraft.ON_OSX ? Component.translatable("jei.key.combo.command", key.getDisplayName()) : Component.translatable("jei.key.combo.control", key.getDisplayName());
        }
    }
    , SHIFT {

        @Override
        public boolean isActive(JeiKeyConflictContext context) {
            return Screen.hasShiftDown();
        }

        @Override
        public Component getCombinedName(InputConstants.Key key) {
            return Component.translatable("jei.key.combo.shift", key.getDisplayName());
        }
    }
    , ALT {

        @Override
        public boolean isActive(JeiKeyConflictContext context) {
            return Screen.hasAltDown();
        }

        @Override
        public Component getCombinedName(InputConstants.Key key) {
            return Component.translatable("jei.key.combo.alt", key.getDisplayName());
        }
    }
    , NONE {

        @Override
        public boolean isActive(JeiKeyConflictContext context) {
            return context.conflicts(JeiKeyConflictContext.IN_GAME) ? true : !CONTROL_OR_COMMAND.isActive(context) && !SHIFT.isActive(context) && !ALT.isActive(context);
        }

        @Override
        public Component getCombinedName(InputConstants.Key key) {
            return key.getDisplayName();
        }
    }
    ;

    public abstract boolean isActive(JeiKeyConflictContext var1);

    public abstract Component getCombinedName(InputConstants.Key var1);
}