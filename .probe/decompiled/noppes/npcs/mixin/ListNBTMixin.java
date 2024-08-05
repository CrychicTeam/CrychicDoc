package noppes.npcs.mixin;

import java.util.List;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ListTag.class })
public interface ListNBTMixin {

    @Accessor("list")
    List<Tag> getList();
}