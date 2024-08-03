package noppes.npcs.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ PersistentEntitySectionManager.class })
public interface PersistentEntitySectionManagerMixin<T extends Entity> {

    @Accessor("sectionStorage")
    EntitySectionStorage<T> sectionStorage();
}