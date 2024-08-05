package noppes.npcs.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ Entity.class })
public interface EntityIMixin {

    @Accessor("removalReason")
    Entity.RemovalReason removal();

    @Accessor("removalReason")
    void removal(Entity.RemovalReason var1);

    @Accessor
    void setLevel(Level var1);
}