package se.mickelus.tetra.client.keymap;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class TetraKeyMappings {

    public static final String bindingGroup = "tetra.binding.group";

    public static final KeyMapping accessBinding = new KeyMapping("tetra.toolbelt.binding.access", TetraKeyMappings.TetraKeyConflictContext.toolbelt, InputConstants.Type.KEYSYM, 66, "tetra.binding.group");

    public static final KeyMapping restockBinding = new KeyMapping("tetra.toolbelt.binding.restock", TetraKeyMappings.TetraKeyConflictContext.toolbelt, KeyModifier.SHIFT, InputConstants.Type.KEYSYM, 66, "tetra.binding.group");

    public static final KeyMapping openBinding = new KeyMapping("tetra.toolbelt.binding.open", TetraKeyMappings.TetraKeyConflictContext.toolbelt, KeyModifier.ALT, InputConstants.Type.KEYSYM, 66, "tetra.binding.group");

    public static final KeyMapping secondaryUseBinding = new KeyMapping("tetra.toolbelt.binding.secondary_use", TetraKeyMappings.TetraKeyConflictContext.secondaryInteraction, InputConstants.Type.KEYSYM, 86, "tetra.binding.group");

    static enum TetraKeyConflictContext implements IKeyConflictContext {

        toolbelt {

            @Override
            public boolean isActive() {
                return Minecraft.getInstance().screen == null;
            }

            @Override
            public boolean conflicts(IKeyConflictContext other) {
                return this == other;
            }
        }
        , secondaryInteraction {

            @Override
            public boolean isActive() {
                return Minecraft.getInstance().screen == null;
            }

            @Override
            public boolean conflicts(IKeyConflictContext other) {
                return this == other;
            }
        }

    }
}