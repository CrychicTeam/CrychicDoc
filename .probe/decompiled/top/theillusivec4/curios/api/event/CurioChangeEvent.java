package top.theillusivec4.curios.api.event;

import javax.annotation.Nonnull;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public class CurioChangeEvent extends LivingEvent {

    private final String type;

    private final ItemStack from;

    private final ItemStack to;

    private final int index;

    public CurioChangeEvent(LivingEntity living, String type, int index, @Nonnull ItemStack from, @Nonnull ItemStack to) {
        super(living);
        this.type = type;
        this.from = from;
        this.to = to;
        this.index = index;
    }

    public String getIdentifier() {
        return this.type;
    }

    public int getSlotIndex() {
        return this.index;
    }

    @Nonnull
    public ItemStack getFrom() {
        return this.from;
    }

    @Nonnull
    public ItemStack getTo() {
        return this.to;
    }
}