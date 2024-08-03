package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PersistentDataSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ImmobileEffect extends PersistentDataSetEffect<ImmobileData> {

    private static final double TOLERANCE = 0.001;

    private final LinearFuncEntry protection;

    private final LinearFuncEntry threshold;

    public ImmobileEffect(LinearFuncEntry protection, LinearFuncEntry threshold) {
        super(0);
        this.protection = protection;
        this.threshold = threshold;
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            ImmobileData data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
            data.update(2, rank);
            double x = player.m_20185_();
            double y = player.m_20186_();
            double z = player.m_20189_();
            if (new Vec3(x, y, z).distanceTo(new Vec3(data.x, data.y, data.z)) < 0.001) {
                data.time++;
            } else {
                data.time = 0;
                data.x = x;
                data.y = y;
                data.z = z;
            }
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int prot = (int) Math.round(this.protection.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", this.threshold.getFromRank(rank) / 20.0, prot));
    }

    @Override
    public void playerReduceDamage(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        ImmobileData data = ConditionalData.HOLDER.get(player).getData(this.getKey());
        if (data != null) {
            if ((double) data.time >= this.threshold.getFromRank(rank)) {
                cache.addDealtModifier(DamageModifier.multTotal((float) this.protection.getFromRank(rank)));
            }
        }
    }

    public ImmobileData getData(ArtifactSetConfig.Entry ent) {
        return new ImmobileData();
    }
}