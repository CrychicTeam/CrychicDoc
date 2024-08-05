package fr.lucreeper74.createmetallurgy.content.kinetics.foundrymixer;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogInstance;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import fr.lucreeper74.createmetallurgy.registries.CMPartialModels;
import net.minecraft.core.Direction;

public class FoundryMixerInstance extends EncasedCogInstance implements DynamicInstance {

    private final RotatingData mixerHead;

    private final OrientedData mixerPole;

    private final MechanicalMixerBlockEntity mixer;

    public FoundryMixerInstance(MaterialManager materialManager, MechanicalMixerBlockEntity blockEntity) {
        super(materialManager, blockEntity, false);
        this.mixer = blockEntity;
        this.mixerHead = (RotatingData) materialManager.defaultCutout().material(AllMaterialSpecs.ROTATING).getModel(CMPartialModels.FOUNDRY_MIXER_HEAD, this.blockState).createInstance();
        this.mixerHead.setRotationAxis(Direction.Axis.Y);
        this.mixerPole = (OrientedData) this.getOrientedMaterial().getModel(CMPartialModels.FOUNDRY_MIXER_POLE, this.blockState).createInstance();
        float renderedHeadOffset = this.getRenderedHeadOffset();
        this.transformPole(renderedHeadOffset);
        this.transformHead(renderedHeadOffset);
    }

    @Override
    protected Instancer<RotatingData> getCogModel() {
        return this.materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING).getModel(CMPartialModels.SHAFTLESS_STONE_COGWHEEL, ((KineticBlockEntity) this.blockEntity).m_58900_());
    }

    public void beginFrame() {
        float renderedHeadOffset = this.getRenderedHeadOffset();
        this.transformPole(renderedHeadOffset);
        this.transformHead(renderedHeadOffset);
    }

    private void transformHead(float renderedHeadOffset) {
        float speed = this.mixer.getRenderedHeadRotationSpeed(AnimationTickHolder.getPartialTicks());
        this.mixerHead.setPosition(this.getInstancePosition()).nudge(0.0F, -renderedHeadOffset, 0.0F).setRotationalSpeed(speed * 2.0F);
    }

    private void transformPole(float renderedHeadOffset) {
        this.mixerPole.setPosition(this.getInstancePosition()).nudge(0.0F, -renderedHeadOffset, 0.0F);
    }

    private float getRenderedHeadOffset() {
        return this.mixer.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks());
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos.below(), new FlatLit[] { this.mixerHead });
        this.relight(this.pos, new FlatLit[] { this.mixerPole });
    }

    @Override
    public void remove() {
        super.remove();
        this.mixerHead.delete();
        this.mixerPole.delete();
    }
}