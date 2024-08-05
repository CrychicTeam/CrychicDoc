package forge.me.thosea.badoptimizations.mixin.entitydata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = { Player.class }, priority = 700)
public abstract class MixinPlayer extends MixinEntity {

    @Shadow
    @Final
    private static EntityDataAccessor<Float> DATA_PLAYER_ABSORPTION_ID;

    @Shadow
    @Final
    protected static EntityDataAccessor<CompoundTag> DATA_SHOULDER_LEFT;

    @Shadow
    @Final
    protected static EntityDataAccessor<CompoundTag> DATA_SHOULDER_RIGHT;

    @Shadow
    @Final
    protected static EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION;

    @Shadow
    @Final
    private static EntityDataAccessor<Integer> DATA_SCORE_ID;

    @Shadow
    @Final
    protected static EntityDataAccessor<Byte> DATA_PLAYER_MAIN_HAND;

    private boolean bo$isCapeEnabled = false;

    private boolean bo$isJacketEnabled = false;

    private boolean bo$isLeftSleeveEnabled = false;

    private boolean bo$isRightSleeveEnabled = false;

    private boolean bo$isLeftPantsLegEnabled = false;

    private boolean bo$isRightPantsLegEnabled = false;

    private boolean bo$isHatEnabled = false;

    private HumanoidArm bo$mainArm = HumanoidArm.RIGHT;

    private float bo$absorptionAmount = 0.0F;

    private int bo$score = 0;

    private CompoundTag bo$shoulderEntityLeft = new CompoundTag();

    private CompoundTag bo$shoulderEntityRight = new CompoundTag();

    @Overwrite
    public boolean isModelPartShown(PlayerModelPart modelPart) {
        return switch(modelPart) {
            case CAPE ->
                this.bo$isCapeEnabled;
            case JACKET ->
                this.bo$isJacketEnabled;
            case LEFT_SLEEVE ->
                this.bo$isLeftSleeveEnabled;
            case RIGHT_SLEEVE ->
                this.bo$isRightSleeveEnabled;
            case LEFT_PANTS_LEG ->
                this.bo$isLeftPantsLegEnabled;
            case RIGHT_PANTS_LEG ->
                this.bo$isRightPantsLegEnabled;
            case HAT ->
                this.bo$isHatEnabled;
        };
    }

    @Overwrite
    public float getAbsorptionAmount() {
        return this.bo$absorptionAmount;
    }

    @Overwrite
    public int getScore() {
        return this.bo$score;
    }

    @Overwrite
    public HumanoidArm getMainArm() {
        return this.bo$mainArm;
    }

    @Overwrite
    public CompoundTag getShoulderEntityLeft() {
        return this.bo$shoulderEntityLeft;
    }

    @Overwrite
    public CompoundTag getShoulderEntityRight() {
        return this.bo$shoulderEntityRight;
    }

    @Override
    public void bo$refreshEntityData(int data) {
        super.bo$refreshEntityData(data);
        if (data == DATA_PLAYER_ABSORPTION_ID.getId()) {
            this.bo$absorptionAmount = this.f_19804_.get(DATA_PLAYER_ABSORPTION_ID);
        } else if (data == DATA_SCORE_ID.getId()) {
            this.bo$score = this.f_19804_.get(DATA_SCORE_ID);
        } else if (data == DATA_PLAYER_MODE_CUSTOMISATION.getId()) {
            byte parts = this.f_19804_.get(DATA_PLAYER_MODE_CUSTOMISATION);
            this.bo$isCapeEnabled = (parts & 1) == 1;
            this.bo$isJacketEnabled = (parts & 2) == 2;
            this.bo$isLeftSleeveEnabled = (parts & 4) == 4;
            this.bo$isRightSleeveEnabled = (parts & 8) == 8;
            this.bo$isLeftPantsLegEnabled = (parts & 16) == 16;
            this.bo$isRightPantsLegEnabled = (parts & 32) == 32;
            this.bo$isHatEnabled = (parts & 64) == 64;
        } else if (data == DATA_PLAYER_MAIN_HAND.getId()) {
            this.bo$mainArm = this.f_19804_.get(DATA_PLAYER_MAIN_HAND) == 0 ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        } else if (data == DATA_SHOULDER_LEFT.getId()) {
            this.bo$shoulderEntityLeft = this.f_19804_.get(DATA_SHOULDER_LEFT);
        } else if (data == DATA_SHOULDER_RIGHT.getId()) {
            this.bo$shoulderEntityRight = this.f_19804_.get(DATA_SHOULDER_RIGHT);
        }
    }
}