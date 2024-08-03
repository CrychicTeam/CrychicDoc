package me.fengming.renderjs.core;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import me.fengming.renderjs.core.render.CustomRenderType;
import net.minecraft.client.multiplayer.ClientRegistryLayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class Utils {

    private static final Map<String, RenderType> renderTypeMap = new HashMap();

    public static BlockState parseBlock(String s, boolean allowNbt) {
        try {
            return BlockStateParser.parseForBlock(ClientRegistryLayer.createRegistryAccess().compositeAccess().m_255025_(Registries.BLOCK), s, allowNbt).blockState();
        } catch (CommandSyntaxException var3) {
            throw new IllegalArgumentException("Error parsing a block: '" + s + "': ", var3);
        }
    }

    public static ItemStack parseItem(String s, int count) {
        try {
            ItemParser.ItemResult result = ItemParser.parseForItem(ClientRegistryLayer.createRegistryAccess().compositeAccess().m_255025_(Registries.ITEM), new StringReader(s));
            ItemStack item = new ItemStack(result.item());
            item.setCount(count == 0 ? 1 : count);
            item.setTag(result.nbt());
            return item;
        } catch (CommandSyntaxException var4) {
            throw new IllegalArgumentException("Error parsing a item: '" + s + "': ", var4);
        }
    }

    public static RenderType getRenderTypeById(String id) {
        return (RenderType) Objects.requireNonNull((RenderType) renderTypeMap.getOrDefault(id, null), "Error getting render type: '" + id + "'");
    }

    static {
        try {
            for (Field field : RenderType.class.getFields()) {
                if (field.getType().getName().equals(RenderType.class.getName())) {
                    renderTypeMap.put(field.getName().toLowerCase(), (RenderType) field.get(null));
                }
            }
        } catch (Exception var4) {
            throw new IllegalStateException("Error getting render type: ", var4);
        }
        renderTypeMap.put("block_layer_top", CustomRenderType.BLOCK_LAYER_TOP);
    }
}