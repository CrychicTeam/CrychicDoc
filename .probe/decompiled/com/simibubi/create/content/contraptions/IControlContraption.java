package com.simibubi.create.content.contraptions;

import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;

public interface IControlContraption {

    boolean isAttachedTo(AbstractContraptionEntity var1);

    void attach(ControlledContraptionEntity var1);

    void onStall();

    boolean isValid();

    BlockPos getBlockPosition();

    public static enum MovementMode implements INamedIconOptions {

        MOVE_PLACE(AllIcons.I_MOVE_PLACE), MOVE_PLACE_RETURNED(AllIcons.I_MOVE_PLACE_RETURNED), MOVE_NEVER_PLACE(AllIcons.I_MOVE_NEVER_PLACE);

        private String translationKey;

        private AllIcons icon;

        private MovementMode(AllIcons icon) {
            this.icon = icon;
            this.translationKey = "contraptions.movement_mode." + Lang.asId(this.name());
        }

        @Override
        public AllIcons getIcon() {
            return this.icon;
        }

        @Override
        public String getTranslationKey() {
            return this.translationKey;
        }
    }

    public static enum RotationMode implements INamedIconOptions {

        ROTATE_PLACE(AllIcons.I_ROTATE_PLACE), ROTATE_PLACE_RETURNED(AllIcons.I_ROTATE_PLACE_RETURNED), ROTATE_NEVER_PLACE(AllIcons.I_ROTATE_NEVER_PLACE);

        private String translationKey;

        private AllIcons icon;

        private RotationMode(AllIcons icon) {
            this.icon = icon;
            this.translationKey = "contraptions.movement_mode." + Lang.asId(this.name());
        }

        @Override
        public AllIcons getIcon() {
            return this.icon;
        }

        @Override
        public String getTranslationKey() {
            return this.translationKey;
        }
    }
}