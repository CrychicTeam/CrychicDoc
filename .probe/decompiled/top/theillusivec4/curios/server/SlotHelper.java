package top.theillusivec4.curios.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.util.ISlotHelper;
import top.theillusivec4.curios.common.inventory.CurioStacksHandler;

public class SlotHelper implements ISlotHelper {

    private final Map<String, ISlotType> idToType = new HashMap();

    @Override
    public void addSlotType(ISlotType slotType) {
        this.idToType.put(slotType.getIdentifier(), slotType);
    }

    @Override
    public void clear() {
        this.idToType.clear();
    }

    @Override
    public Optional<ISlotType> getSlotType(String identifier) {
        return Optional.ofNullable((ISlotType) this.idToType.get(identifier));
    }

    @Override
    public Collection<ISlotType> getSlotTypes() {
        return Collections.unmodifiableCollection(this.idToType.values());
    }

    @Override
    public Collection<ISlotType> getSlotTypes(LivingEntity livingEntity) {
        return this.getSlotTypes();
    }

    @Override
    public SortedMap<ISlotType, ICurioStacksHandler> createSlots() {
        SortedMap<ISlotType, ICurioStacksHandler> curios = new TreeMap();
        this.getSlotTypes().forEach(type -> curios.put(type, new CurioStacksHandler(null, type.getIdentifier(), type.getSize(), type.isVisible(), type.hasCosmetic(), type.canToggleRendering(), type.getDropRule())));
        return curios;
    }

    @Override
    public SortedMap<ISlotType, ICurioStacksHandler> createSlots(LivingEntity livingEntity) {
        SortedMap<ISlotType, ICurioStacksHandler> curios = new TreeMap();
        CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).ifPresent(handler -> this.getSlotTypes().forEach(type -> curios.put(type, new CurioStacksHandler(handler, type.getIdentifier(), type.getSize(), type.isVisible(), type.hasCosmetic(), type.canToggleRendering(), type.getDropRule()))));
        return curios;
    }

    @Override
    public Set<String> getSlotTypeIds() {
        return Collections.unmodifiableSet(this.idToType.keySet());
    }

    @Override
    public int getSlotsForType(@Nonnull LivingEntity livingEntity, String identifier) {
        return (Integer) CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).map(handler -> (Integer) handler.getStacksHandler(identifier).map(ICurioStacksHandler::getSlots).orElse(0)).orElse(0);
    }

    @Override
    public void setSlotsForType(String id, LivingEntity livingEntity, int amount) {
        int difference = amount - this.getSlotsForType(livingEntity, id);
        if (difference > 0) {
            this.growSlotType(id, difference, livingEntity);
        } else if (difference < 0) {
            this.shrinkSlotType(id, Math.abs(difference), livingEntity);
        }
    }

    @Override
    public void growSlotType(String id, LivingEntity livingEntity) {
        this.growSlotType(id, 1, livingEntity);
    }

    @Override
    public void growSlotType(String id, int amount, LivingEntity livingEntity) {
        CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).ifPresent(handler -> handler.growSlotType(id, amount));
    }

    @Override
    public void shrinkSlotType(String id, LivingEntity livingEntity) {
        this.shrinkSlotType(id, 1, livingEntity);
    }

    @Override
    public void shrinkSlotType(String id, int amount, LivingEntity livingEntity) {
        CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).ifPresent(handler -> handler.shrinkSlotType(id, amount));
    }

    @Override
    public void unlockSlotType(String id, LivingEntity livingEntity) {
        CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).ifPresent(handler -> {
            if ((Boolean) handler.getStacksHandler(id).map(stacksHandler -> stacksHandler.getSlots() == 0).orElse(false)) {
                handler.growSlotType(id, 1);
            }
        });
    }

    @Override
    public void lockSlotType(String id, LivingEntity livingEntity) {
        CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).ifPresent(handler -> {
            int amount = (Integer) handler.getStacksHandler(id).map(ICurioStacksHandler::getSlots).orElse(0);
            if (amount > 0) {
                handler.shrinkSlotType(id, amount);
            }
        });
    }
}