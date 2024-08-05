package com.simibubi.create.content.contraptions.render;

import com.jozufozu.flywheel.event.BeginFrameEvent;
import com.jozufozu.flywheel.event.RenderLayerEvent;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.ref.Reference;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public abstract class ContraptionRenderingWorld<C extends ContraptionRenderInfo> {

    protected final Level world;

    private int removalTimer;

    protected final Int2ObjectMap<C> renderInfos = new Int2ObjectOpenHashMap();

    protected final List<C> visible = new ObjectArrayList();

    public ContraptionRenderingWorld(LevelAccessor world) {
        this.world = (Level) world;
    }

    public boolean invalidate(Contraption contraption) {
        int entityId = contraption.entity.m_19879_();
        C removed = (C) this.renderInfos.remove(entityId);
        if (removed != null) {
            removed.invalidate();
            this.visible.remove(removed);
            return true;
        } else {
            return false;
        }
    }

    public void renderLayer(RenderLayerEvent event) {
        for (C c : this.visible) {
            c.setupMatrices(event.stack, event.camX, event.camY, event.camZ);
        }
    }

    protected abstract C create(Contraption var1);

    public void tick() {
        this.removalTimer++;
        if (this.removalTimer >= 20) {
            this.removeDeadRenderers();
            this.removalTimer = 0;
        }
        ContraptionHandler.loadedContraptions.get(this.world).values().stream().map(Reference::get).filter(Objects::nonNull).map(AbstractContraptionEntity::getContraption).filter(Objects::nonNull).forEach(this::getRenderInfo);
    }

    public void beginFrame(BeginFrameEvent event) {
        this.renderInfos.int2ObjectEntrySet().stream().map(Entry::getValue).forEach(renderInfo -> renderInfo.beginFrame(event));
        this.collectVisible();
    }

    protected void collectVisible() {
        this.visible.clear();
        this.renderInfos.int2ObjectEntrySet().stream().map(Entry::getValue).filter(ContraptionRenderInfo::isVisible).forEach(this.visible::add);
    }

    public C getRenderInfo(Contraption c) {
        int entityId = c.entity.m_19879_();
        C renderInfo = (C) this.renderInfos.get(entityId);
        if (renderInfo == null) {
            renderInfo = this.create(c);
            this.renderInfos.put(entityId, renderInfo);
        }
        return renderInfo;
    }

    public void delete() {
        ObjectIterator var1 = this.renderInfos.values().iterator();
        while (var1.hasNext()) {
            C renderer = (C) var1.next();
            renderer.invalidate();
        }
        this.renderInfos.clear();
    }

    public void removeDeadRenderers() {
        this.renderInfos.values().removeIf(ContraptionRenderInfo::isDead);
    }
}