package software.bernie.geckolib.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.network.SerializableDataTicket;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class DataTickets {

    private static final Map<String, SerializableDataTicket<?>> SERIALIZABLE_TICKETS = new ConcurrentHashMap();

    public static final DataTicket<BlockEntity> BLOCK_ENTITY = new DataTicket<>("block_entity", BlockEntity.class);

    public static final DataTicket<ItemStack> ITEMSTACK = new DataTicket<>("itemstack", ItemStack.class);

    public static final DataTicket<Entity> ENTITY = new DataTicket<>("entity", Entity.class);

    public static final DataTicket<EquipmentSlot> EQUIPMENT_SLOT = new DataTicket<>("equipment_slot", EquipmentSlot.class);

    public static final DataTicket<EntityModelData> ENTITY_MODEL_DATA = new DataTicket<>("entity_model_data", EntityModelData.class);

    public static final DataTicket<Double> TICK = new DataTicket<>("tick", Double.class);

    public static final DataTicket<ItemDisplayContext> ITEM_RENDER_PERSPECTIVE = new DataTicket<>("item_render_perspective", ItemDisplayContext.class);

    public static final SerializableDataTicket<Integer> ANIM_STATE = GeckoLibUtil.addDataTicket(SerializableDataTicket.ofInt(new ResourceLocation("geckolib", "anim_state")));

    public static final SerializableDataTicket<String> ANIM = GeckoLibUtil.addDataTicket(SerializableDataTicket.ofString(new ResourceLocation("geckolib", "anim")));

    public static final SerializableDataTicket<Integer> USE_TICKS = GeckoLibUtil.addDataTicket(SerializableDataTicket.ofInt(new ResourceLocation("geckolib", "use_ticks")));

    public static final SerializableDataTicket<Boolean> ACTIVE = GeckoLibUtil.addDataTicket(SerializableDataTicket.ofBoolean(new ResourceLocation("geckolib", "active")));

    public static final SerializableDataTicket<Boolean> OPEN = GeckoLibUtil.addDataTicket(SerializableDataTicket.ofBoolean(new ResourceLocation("geckolib", "open")));

    public static final SerializableDataTicket<Boolean> CLOSED = GeckoLibUtil.addDataTicket(SerializableDataTicket.ofBoolean(new ResourceLocation("geckolib", "closed")));

    public static final SerializableDataTicket<Direction> DIRECTION = GeckoLibUtil.addDataTicket(SerializableDataTicket.ofEnum(new ResourceLocation("geckolib", "direction"), Direction.class));

    @Nullable
    public static SerializableDataTicket<?> byName(String id) {
        return (SerializableDataTicket<?>) SERIALIZABLE_TICKETS.getOrDefault(id, null);
    }

    public static <D> SerializableDataTicket<D> registerSerializable(SerializableDataTicket<D> ticket) {
        SerializableDataTicket<?> existingTicket = (SerializableDataTicket<?>) SERIALIZABLE_TICKETS.putIfAbsent(ticket.id(), ticket);
        if (existingTicket != null) {
            GeckoLib.LOGGER.error("Duplicate SerializableDataTicket registered! This will cause issues. Existing: " + existingTicket.id() + ", New: " + ticket.id());
        }
        return ticket;
    }
}