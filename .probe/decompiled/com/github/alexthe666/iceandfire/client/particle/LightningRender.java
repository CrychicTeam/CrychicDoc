package com.github.alexthe666.iceandfire.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Matrix4f;

public class LightningRender {

    private static final float REFRESH_TIME = 3.0F;

    private static final double MAX_OWNER_TRACK_TIME = 100.0;

    private LightningRender.Timestamp refreshTimestamp = new LightningRender.Timestamp();

    private final Random random = new Random();

    private final Minecraft minecraft = Minecraft.getInstance();

    private final Map<Object, LightningRender.BoltOwnerData> boltOwners = new Object2ObjectOpenHashMap();

    public void render(float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn) {
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.lightning());
        Matrix4f matrix = matrixStackIn.last().pose();
        LightningRender.Timestamp timestamp = new LightningRender.Timestamp(this.minecraft.level.m_46467_(), partialTicks);
        boolean refresh = timestamp.isPassed(this.refreshTimestamp, 0.33333334F);
        if (refresh) {
            this.refreshTimestamp = timestamp;
        }
        Iterator<Entry<Object, LightningRender.BoltOwnerData>> iter = this.boltOwners.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Object, LightningRender.BoltOwnerData> entry = (Entry<Object, LightningRender.BoltOwnerData>) iter.next();
            LightningRender.BoltOwnerData data = (LightningRender.BoltOwnerData) entry.getValue();
            if (refresh) {
                data.bolts.removeIf(bolt -> bolt.tick(timestamp));
            }
            if (data.bolts.isEmpty() && data.lastBolt != null && data.lastBolt.getSpawnFunction().isConsecutive()) {
                data.addBolt(new LightningRender.BoltInstance(data.lastBolt, timestamp), timestamp);
            }
            data.bolts.forEach(bolt -> bolt.render(matrix, buffer, timestamp));
            if (data.bolts.isEmpty() && timestamp.isPassed(data.lastUpdateTimestamp, 100.0)) {
                iter.remove();
            }
        }
    }

    public void update(Object owner, LightningBoltData newBoltData, float partialTicks) {
        if (this.minecraft.level != null) {
            LightningRender.BoltOwnerData data = (LightningRender.BoltOwnerData) this.boltOwners.computeIfAbsent(owner, o -> new LightningRender.BoltOwnerData());
            data.lastBolt = newBoltData;
            LightningRender.Timestamp timestamp = new LightningRender.Timestamp(this.minecraft.level.m_46467_(), partialTicks);
            if ((!data.lastBolt.getSpawnFunction().isConsecutive() || data.bolts.isEmpty()) && timestamp.isPassed(data.lastBoltTimestamp, data.lastBoltDelay)) {
                data.addBolt(new LightningRender.BoltInstance(newBoltData, timestamp), timestamp);
            }
            data.lastUpdateTimestamp = timestamp;
        }
    }

    public class BoltInstance {

        private final LightningBoltData bolt;

        private final List<LightningBoltData.BoltQuads> renderQuads;

        private final LightningRender.Timestamp createdTimestamp;

        public BoltInstance(LightningBoltData bolt, LightningRender.Timestamp timestamp) {
            this.bolt = bolt;
            this.renderQuads = bolt.generate();
            this.createdTimestamp = timestamp;
        }

        public void render(Matrix4f matrix, VertexConsumer buffer, LightningRender.Timestamp timestamp) {
            float lifeScale = timestamp.subtract(this.createdTimestamp).value() / (float) this.bolt.getLifespan();
            Pair<Integer, Integer> bounds = this.bolt.getFadeFunction().getRenderBounds(this.renderQuads.size(), lifeScale);
            for (int i = (Integer) bounds.getLeft(); i < bounds.getRight(); i++) {
                ((LightningBoltData.BoltQuads) this.renderQuads.get(i)).getVecs().forEach(v -> buffer.vertex(matrix, (float) v.x, (float) v.y, (float) v.z).color(this.bolt.getColor().x(), this.bolt.getColor().y(), this.bolt.getColor().z(), this.bolt.getColor().w()).endVertex());
            }
        }

        public boolean tick(LightningRender.Timestamp timestamp) {
            return timestamp.isPassed(this.createdTimestamp, (double) this.bolt.getLifespan());
        }
    }

    public class BoltOwnerData {

        private final Set<LightningRender.BoltInstance> bolts = new ObjectOpenHashSet();

        private LightningBoltData lastBolt;

        private LightningRender.Timestamp lastBoltTimestamp = LightningRender.this.new Timestamp();

        private LightningRender.Timestamp lastUpdateTimestamp = LightningRender.this.new Timestamp();

        private double lastBoltDelay;

        private void addBolt(LightningRender.BoltInstance instance, LightningRender.Timestamp timestamp) {
            this.bolts.add(instance);
            this.lastBoltDelay = (double) instance.bolt.getSpawnFunction().getSpawnDelay(LightningRender.this.random);
            this.lastBoltTimestamp = timestamp;
        }
    }

    public class Timestamp {

        private final long ticks;

        private final float partial;

        public Timestamp() {
            this(0L, 0.0F);
        }

        public Timestamp(long ticks, float partial) {
            this.ticks = ticks;
            this.partial = partial;
        }

        public LightningRender.Timestamp subtract(LightningRender.Timestamp other) {
            long newTicks = this.ticks - other.ticks;
            float newPartial = this.partial - other.partial;
            if (newPartial < 0.0F) {
                newPartial++;
                newTicks--;
            }
            return LightningRender.this.new Timestamp(newTicks, newPartial);
        }

        public float value() {
            return (float) this.ticks + this.partial;
        }

        public boolean isPassed(LightningRender.Timestamp prev, double duration) {
            long ticksPassed = this.ticks - prev.ticks;
            if ((double) ticksPassed > duration) {
                return true;
            } else {
                duration -= (double) ticksPassed;
                return duration >= 1.0 ? false : (double) (this.partial - prev.partial) >= duration;
            }
        }
    }
}