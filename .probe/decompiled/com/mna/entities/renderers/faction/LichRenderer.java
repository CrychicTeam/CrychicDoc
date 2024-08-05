package com.mna.entities.renderers.faction;

import com.mna.capabilities.entity.MAPFX;
import com.mna.capabilities.entity.MAPFXProvider;
import com.mna.entities.boss.WitherLich;
import com.mna.entities.models.boss.LichModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.common.util.LazyOptional;

public class LichRenderer extends MAGeckoRenderer<WitherLich> {

    public LichRenderer(EntityRendererProvider.Context context) {
        super(context, new LichModel());
    }

    public boolean shouldRender(WitherLich pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        LazyOptional<MAPFX> pfxCap = pLivingEntity.getCapability(MAPFXProvider.MAPFX);
        return pfxCap.isPresent() && ((MAPFX) pfxCap.resolve().get()).getFlag(pLivingEntity, MAPFX.Flag.MIST_FORM) ? false : super.m_5523_(pLivingEntity, pCamera, pCamX, pCamY, pCamZ);
    }
}