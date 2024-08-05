package noppes.npcs.client.model;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class ModelClassicPlayer<T extends LivingEntity> extends PlayerModel<T> {

    public ModelClassicPlayer(ModelPart modelPart0, float scale) {
        super(modelPart0, false);
    }

    @Override
    public void setupAnim(T entity, float par1, float limbSwingAmount, float par3, float par4, float par5) {
        super.setupAnim(entity, par1, limbSwingAmount, par3, par4, par5);
        float j = 2.0F;
        if (entity.m_20142_()) {
            j = 1.0F;
        }
        this.f_102811_.xRot = this.f_102811_.xRot + Mth.cos(par1 * 0.6662F + (float) Math.PI) * j * limbSwingAmount;
        this.f_102812_.xRot = this.f_102812_.xRot + Mth.cos(par1 * 0.6662F) * j * limbSwingAmount;
        this.f_102812_.zRot = this.f_102812_.zRot + (Mth.cos(par1 * 0.2812F) - 1.0F) * limbSwingAmount;
        this.f_102811_.zRot = this.f_102811_.zRot + (Mth.cos(par1 * 0.2312F) + 1.0F) * limbSwingAmount;
        this.f_103374_.xRot = this.f_102812_.xRot;
        this.f_103374_.yRot = this.f_102812_.yRot;
        this.f_103374_.zRot = this.f_102812_.zRot;
        this.f_103375_.xRot = this.f_102811_.xRot;
        this.f_103375_.yRot = this.f_102811_.yRot;
        this.f_103375_.zRot = this.f_102811_.zRot;
    }
}