package yesman.epicfight.api.utils;

import net.minecraft.core.IdMapper;

public class ClearableIdMapper<I> extends IdMapper<I> {

    public ClearableIdMapper() {
        super(512);
    }

    public ClearableIdMapper(int size) {
        super(size);
    }

    public void clear() {
        this.f_122654_.clear();
        this.f_122655_.clear();
        this.f_122653_ = 0;
    }
}