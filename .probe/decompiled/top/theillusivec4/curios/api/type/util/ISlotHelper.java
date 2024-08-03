package top.theillusivec4.curios.api.type.util;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

@Deprecated(forRemoval = true, since = "1.20.1")
@ScheduledForRemoval(inVersion = "1.22")
public interface ISlotHelper {

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    void addSlotType(ISlotType var1);

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    void clear();

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    Optional<ISlotType> getSlotType(String var1);

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    Collection<ISlotType> getSlotTypes();

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    Collection<ISlotType> getSlotTypes(LivingEntity var1);

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    Set<String> getSlotTypeIds();

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    int getSlotsForType(LivingEntity var1, String var2);

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    void setSlotsForType(String var1, LivingEntity var2, int var3);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    SortedMap<ISlotType, ICurioStacksHandler> createSlots(LivingEntity var1);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    SortedMap<ISlotType, ICurioStacksHandler> createSlots();

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    void growSlotType(String var1, LivingEntity var2);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    void growSlotType(String var1, int var2, LivingEntity var3);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    void shrinkSlotType(String var1, LivingEntity var2);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    void shrinkSlotType(String var1, int var2, LivingEntity var3);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    void unlockSlotType(String var1, LivingEntity var2);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    void lockSlotType(String var1, LivingEntity var2);
}