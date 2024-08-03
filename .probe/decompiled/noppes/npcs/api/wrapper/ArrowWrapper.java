package noppes.npcs.api.wrapper;

import net.minecraft.world.entity.projectile.AbstractArrow;
import noppes.npcs.api.entity.IArrow;

public class ArrowWrapper<T extends AbstractArrow> extends EntityWrapper<T> implements IArrow {

    public ArrowWrapper(T entity) {
        super(entity);
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public boolean typeOf(int type) {
        return type == 4 ? true : super.typeOf(type);
    }
}