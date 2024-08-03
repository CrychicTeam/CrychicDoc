package com.simibubi.create.foundation.outliner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Outliner {

    private final Map<Object, Outliner.OutlineEntry> outlines = Collections.synchronizedMap(new HashMap());

    private final Map<Object, Outliner.OutlineEntry> outlinesView = Collections.unmodifiableMap(this.outlines);

    public Outline.OutlineParams showValueBox(Object slot, ValueBox box) {
        this.outlines.put(slot, new Outliner.OutlineEntry(box));
        return box.getParams();
    }

    public Outline.OutlineParams showLine(Object slot, Vec3 start, Vec3 end) {
        if (!this.outlines.containsKey(slot)) {
            LineOutline outline = new LineOutline();
            this.addOutline(slot, outline);
        }
        Outliner.OutlineEntry entry = (Outliner.OutlineEntry) this.outlines.get(slot);
        entry.ticksTillRemoval = 1;
        ((LineOutline) entry.outline).set(start, end);
        return entry.outline.getParams();
    }

    public Outline.OutlineParams endChasingLine(Object slot, Vec3 start, Vec3 end, float chasingProgress, boolean lockStart) {
        if (!this.outlines.containsKey(slot)) {
            LineOutline.EndChasingLineOutline outline = new LineOutline.EndChasingLineOutline(lockStart);
            this.addOutline(slot, outline);
        }
        Outliner.OutlineEntry entry = (Outliner.OutlineEntry) this.outlines.get(slot);
        entry.ticksTillRemoval = 1;
        ((LineOutline.EndChasingLineOutline) entry.outline).setProgress(chasingProgress).set(start, end);
        return entry.outline.getParams();
    }

    public Outline.OutlineParams showAABB(Object slot, AABB bb, int ttl) {
        this.createAABBOutlineIfMissing(slot, bb);
        ChasingAABBOutline outline = this.getAndRefreshAABB(slot, ttl);
        outline.prevBB = outline.targetBB = outline.bb = bb;
        return outline.getParams();
    }

    public Outline.OutlineParams showAABB(Object slot, AABB bb) {
        this.createAABBOutlineIfMissing(slot, bb);
        ChasingAABBOutline outline = this.getAndRefreshAABB(slot);
        outline.prevBB = outline.targetBB = outline.bb = bb;
        return outline.getParams();
    }

    public Outline.OutlineParams chaseAABB(Object slot, AABB bb) {
        this.createAABBOutlineIfMissing(slot, bb);
        ChasingAABBOutline outline = this.getAndRefreshAABB(slot);
        outline.targetBB = bb;
        return outline.getParams();
    }

    public Outline.OutlineParams showCluster(Object slot, Iterable<BlockPos> selection) {
        BlockClusterOutline outline = new BlockClusterOutline(selection);
        this.addOutline(slot, outline);
        return outline.getParams();
    }

    public Outline.OutlineParams showItem(Object slot, Vec3 pos, ItemStack stack) {
        ItemOutline outline = new ItemOutline(pos, stack);
        Outliner.OutlineEntry entry = new Outliner.OutlineEntry(outline);
        this.outlines.put(slot, entry);
        return entry.getOutline().getParams();
    }

    public void keep(Object slot) {
        if (this.outlines.containsKey(slot)) {
            ((Outliner.OutlineEntry) this.outlines.get(slot)).ticksTillRemoval = 1;
        }
    }

    public void remove(Object slot) {
        this.outlines.remove(slot);
    }

    public Optional<Outline.OutlineParams> edit(Object slot) {
        this.keep(slot);
        return this.outlines.containsKey(slot) ? Optional.of(((Outliner.OutlineEntry) this.outlines.get(slot)).getOutline().getParams()) : Optional.empty();
    }

    public Map<Object, Outliner.OutlineEntry> getOutlines() {
        return this.outlinesView;
    }

    private void addOutline(Object slot, Outline outline) {
        this.outlines.put(slot, new Outliner.OutlineEntry(outline));
    }

    private void createAABBOutlineIfMissing(Object slot, AABB bb) {
        if (!this.outlines.containsKey(slot) || !(((Outliner.OutlineEntry) this.outlines.get(slot)).outline instanceof AABBOutline)) {
            ChasingAABBOutline outline = new ChasingAABBOutline(bb);
            this.addOutline(slot, outline);
        }
    }

    private ChasingAABBOutline getAndRefreshAABB(Object slot) {
        return this.getAndRefreshAABB(slot, 1);
    }

    private ChasingAABBOutline getAndRefreshAABB(Object slot, int ttl) {
        Outliner.OutlineEntry entry = (Outliner.OutlineEntry) this.outlines.get(slot);
        entry.ticksTillRemoval = ttl;
        return (ChasingAABBOutline) entry.getOutline();
    }

    public void tickOutlines() {
        Iterator<Outliner.OutlineEntry> iterator = this.outlines.values().iterator();
        while (iterator.hasNext()) {
            Outliner.OutlineEntry entry = (Outliner.OutlineEntry) iterator.next();
            entry.tick();
            if (!entry.isAlive()) {
                iterator.remove();
            }
        }
    }

    public void renderOutlines(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera, float pt) {
        this.outlines.forEach((key, entry) -> {
            Outline outline = entry.getOutline();
            Outline.OutlineParams params = outline.getParams();
            params.alpha = 1.0F;
            if (entry.isFading()) {
                int prevTicks = entry.ticksTillRemoval + 1;
                float fadeticks = 8.0F;
                float lastAlpha = prevTicks >= 0 ? 1.0F : 1.0F + (float) prevTicks / fadeticks;
                float currentAlpha = 1.0F + (float) entry.ticksTillRemoval / fadeticks;
                float alpha = Mth.lerp(pt, lastAlpha, currentAlpha);
                params.alpha = alpha * alpha * alpha;
                if (params.alpha < 0.125F) {
                    return;
                }
            }
            outline.render(ms, buffer, camera, pt);
        });
    }

    public static class OutlineEntry {

        public static final int FADE_TICKS = 8;

        private final Outline outline;

        private int ticksTillRemoval = 1;

        public OutlineEntry(Outline outline) {
            this.outline = outline;
        }

        public Outline getOutline() {
            return this.outline;
        }

        public int getTicksTillRemoval() {
            return this.ticksTillRemoval;
        }

        public boolean isAlive() {
            return this.ticksTillRemoval >= -8;
        }

        public boolean isFading() {
            return this.ticksTillRemoval < 0;
        }

        public void tick() {
            this.ticksTillRemoval--;
            this.outline.tick();
        }
    }
}