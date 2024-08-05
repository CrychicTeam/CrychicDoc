package net.minecraftforge.client.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

public class RegisterItemDecorationsEvent extends Event implements IModBusEvent {

    private final Map<Item, List<IItemDecorator>> decorators;

    @Internal
    public RegisterItemDecorationsEvent(Map<Item, List<IItemDecorator>> decorators) {
        this.decorators = decorators;
    }

    public void register(ItemLike itemLike, IItemDecorator decorator) {
        List<IItemDecorator> itemDecoratorList = (List<IItemDecorator>) this.decorators.computeIfAbsent(itemLike.asItem(), item -> new ArrayList());
        itemDecoratorList.add(decorator);
    }
}