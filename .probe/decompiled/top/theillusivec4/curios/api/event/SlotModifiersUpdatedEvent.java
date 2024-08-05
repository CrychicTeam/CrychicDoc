package top.theillusivec4.curios.api.event;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

public class SlotModifiersUpdatedEvent extends LivingEvent {

    private final Set<String> types;

    public SlotModifiersUpdatedEvent(LivingEntity livingEntity, Set<String> types) {
        super(livingEntity);
        this.types = types;
    }

    public Set<String> getTypes() {
        return ImmutableSet.copyOf(this.types);
    }
}