package team.lodestar.lodestone.systems.model.obj.lod;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

public abstract class LODStrategy<T> implements LODBuilder<T> {

    public List<LevelOfDetail<T>> levelsOfDetail = new ArrayList();

    public static LODStrategy<Float> Distance(LODStrategy.LODBuilderSetup<Float> lodBuilderSetup) {
        return new LODStrategy.DistanceLODStrategy(lodBuilderSetup);
    }

    public static LODStrategy<Integer> Performance(LODStrategy.LODBuilderSetup<Integer> lodBuilderSetup) {
        return new LODStrategy.PerformanceLODStrategy(lodBuilderSetup);
    }

    public abstract LevelOfDetail<T> getLODLevel(Vector3f var1);

    public LODStrategy(LODStrategy.LODBuilderSetup<T> lodBuilderSetup) {
        lodBuilderSetup.lodBuilder(this);
    }

    private static class DistanceLODStrategy extends LODStrategy<Float> {

        public DistanceLODStrategy(LODStrategy.LODBuilderSetup<Float> lodBuilderSetup) {
            super(lodBuilderSetup);
        }

        @Override
        public LevelOfDetail<Float> getLODLevel(Vector3f modelPosition) {
            Vector3f cameraPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f();
            float distanceSq = cameraPosition.distanceSquared(modelPosition);
            for (LevelOfDetail<Float> levelOfDetail : this.levelsOfDetail) {
                if (distanceSq <= levelOfDetail.getArgument()) {
                    return levelOfDetail;
                }
            }
            return (LevelOfDetail<Float>) this.levelsOfDetail.get(this.levelsOfDetail.size() - 1);
        }

        public void create(Float argument, ResourceLocation modelLocation) {
            this.levelsOfDetail.add(new LevelOfDetail<>(new ObjModel(modelLocation), argument));
        }
    }

    public interface LODBuilderSetup<T> {

        void lodBuilder(LODBuilder<T> var1);
    }

    private static class PerformanceLODStrategy extends LODStrategy<Integer> {

        public PerformanceLODStrategy(LODStrategy.LODBuilderSetup<Integer> lodBuilderSetup) {
            super(lodBuilderSetup);
        }

        @Override
        public LevelOfDetail<Integer> getLODLevel(Vector3f modelPosition) {
            int fps = Minecraft.getInstance().getFps();
            int monitorRefreshRate = Minecraft.getInstance().getWindow().getRefreshRate();
            int optionsRefreshRate = Minecraft.getInstance().options.framerateLimit().get();
            int steps = this.levelsOfDetail.size();
            int step = Math.min(steps - 1, (int) Math.floor((double) fps / (double) monitorRefreshRate * (double) steps));
            return (LevelOfDetail<Integer>) this.levelsOfDetail.get(step);
        }

        public void create(Integer argument, ResourceLocation modelLocation) {
            this.levelsOfDetail.add(new LevelOfDetail<>(new ObjModel(modelLocation), argument));
        }
    }
}