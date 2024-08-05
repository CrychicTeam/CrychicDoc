package top.theillusivec4.curios.api.type.util;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

@Deprecated(forRemoval = true, since = "1.20.1")
@ScheduledForRemoval(inVersion = "1.22")
public interface IIconHelper {

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    void clearIcons();

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    void addIcon(String var1, ResourceLocation var2);

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    ResourceLocation getIcon(String var1);
}