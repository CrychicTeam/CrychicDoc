package noppes.npcs.api.wrapper;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import noppes.npcs.api.IDamageSource;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;

public class DamageSourceWrapper implements IDamageSource {

    private DamageSource source;

    public DamageSourceWrapper(DamageSource source) {
        this.source = source;
    }

    @Override
    public String getType() {
        return this.source.getMsgId();
    }

    @Override
    public boolean isUnblockable() {
        return this.source.is(DamageTypeTags.BYPASSES_ARMOR);
    }

    @Override
    public boolean isProjectile() {
        return this.source.is(DamageTypeTags.IS_PROJECTILE);
    }

    @Override
    public DamageSource getMCDamageSource() {
        return this.source;
    }

    @Override
    public IEntity getTrueSource() {
        return NpcAPI.Instance().getIEntity(this.source.getEntity());
    }

    @Override
    public IEntity getImmediateSource() {
        return NpcAPI.Instance().getIEntity(this.source.getDirectEntity());
    }
}