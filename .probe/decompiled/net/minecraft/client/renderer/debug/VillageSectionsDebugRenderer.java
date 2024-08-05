package net.minecraft.client.renderer.debug;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Set;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;

public class VillageSectionsDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private static final int MAX_RENDER_DIST_FOR_VILLAGE_SECTIONS = 60;

    private final Set<SectionPos> villageSections = Sets.newHashSet();

    VillageSectionsDebugRenderer() {
    }

    @Override
    public void clear() {
        this.villageSections.clear();
    }

    public void setVillageSection(SectionPos sectionPos0) {
        this.villageSections.add(sectionPos0);
    }

    public void setNotVillageSection(SectionPos sectionPos0) {
        this.villageSections.remove(sectionPos0);
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        BlockPos $$5 = BlockPos.containing(double2, double3, double4);
        this.villageSections.forEach(p_269747_ -> {
            if ($$5.m_123314_(p_269747_.center(), 60.0)) {
                highlightVillageSection(poseStack0, multiBufferSource1, p_269747_);
            }
        });
    }

    private static void highlightVillageSection(PoseStack poseStack0, MultiBufferSource multiBufferSource1, SectionPos sectionPos2) {
        int $$3 = 1;
        BlockPos $$4 = sectionPos2.center();
        BlockPos $$5 = $$4.offset(-1, -1, -1);
        BlockPos $$6 = $$4.offset(1, 1, 1);
        DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, $$5, $$6, 0.2F, 1.0F, 0.2F, 0.15F);
    }
}