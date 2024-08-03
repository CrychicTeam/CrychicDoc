package team.lodestar.lodestone.systems.particle;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;

public class ParticleEffectSpawner {

    private final WorldParticleBuilder builder;

    private final Consumer<WorldParticleBuilder> particleSpawner;

    private final WorldParticleBuilder bloomBuilder;

    private final Consumer<WorldParticleBuilder> bloomSpawner;

    public ParticleEffectSpawner(WorldParticleBuilder builder, Consumer<WorldParticleBuilder> particleSpawner, @Nullable WorldParticleBuilder bloomBuilder, @Nullable Consumer<WorldParticleBuilder> bloomSpawner) {
        this.builder = builder;
        this.particleSpawner = particleSpawner;
        this.bloomBuilder = bloomBuilder;
        this.bloomSpawner = bloomSpawner;
    }

    public ParticleEffectSpawner(Level level, Vec3 pos, WorldParticleBuilder builder, WorldParticleBuilder bloomBuilder) {
        this(builder, b -> b.spawn(level, pos.x, pos.y, pos.z), bloomBuilder, b -> b.spawn(level, pos.x, pos.y, pos.z));
    }

    public ParticleEffectSpawner(Level level, Vec3 pos, WorldParticleBuilder builder) {
        this(builder, b -> b.spawn(level, pos.x, pos.y, pos.z), null, null);
    }

    public ParticleEffectSpawner(WorldParticleBuilder builder, Consumer<WorldParticleBuilder> particleSpawner) {
        this(builder, particleSpawner, null, null);
    }

    public WorldParticleBuilder getBuilder() {
        return this.builder;
    }

    public ParticleEffectSpawner act(Consumer<WorldParticleBuilder> builderConsumer) {
        builderConsumer.accept(this.bloomBuilder);
        builderConsumer.accept(this.builder);
        return this;
    }

    public ParticleEffectSpawner actRaw(Consumer<WorldParticleBuilder> builderConsumer) {
        builderConsumer.accept(this.builder);
        return this;
    }

    public WorldParticleBuilder getBloomBuilder() {
        return this.bloomBuilder;
    }

    public void spawnParticles() {
        this.particleSpawner.accept(this.builder);
        if (this.bloomSpawner != null) {
            this.bloomSpawner.accept(this.bloomBuilder);
        }
    }

    public void spawnParticlesRaw() {
        this.particleSpawner.accept(this.builder);
    }
}