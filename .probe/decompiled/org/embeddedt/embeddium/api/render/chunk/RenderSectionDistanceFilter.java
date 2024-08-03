package org.embeddedt.embeddium.api.render.chunk;

@FunctionalInterface
public interface RenderSectionDistanceFilter {

    RenderSectionDistanceFilter DEFAULT = (dx, dy, dz, maxDistance) -> dx * dx + dz * dz < maxDistance * maxDistance && Math.abs(dy) < maxDistance;

    boolean isWithinDistance(float var1, float var2, float var3, float var4);
}