package net.mehvahdjukaar.supplementaries.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.mehvahdjukaar.moonlight.api.map.MapDataRegistry;
import net.mehvahdjukaar.moonlight.api.map.MapHelper;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.core.map.MapDataInternal;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapMarkerCommand {

    private static final DynamicCommandExceptionType ERROR_STRUCTURE_INVALID = new DynamicCommandExceptionType(object -> Component.translatable("commands.locate.structure.invalid", object));

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        return ((LiteralArgumentBuilder) Commands.literal("add_marker").requires(cs -> cs.hasPermission(2))).then(Commands.argument("marker", ResourceArgument.resource(context, MapDataRegistry.REGISTRY_KEY)).executes(MapMarkerCommand::addMapMarker));
    }

    public static int addMapMarker(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = (CommandSourceStack) context.getSource();
        ServerLevel level = source.getLevel();
        MapDecorationType<?, ? extends MapBlockMarker<?>> decoration = (MapDecorationType<?, ? extends MapBlockMarker<?>>) ResourceArgument.getResource(context, "marker", MapDataInternal.KEY).value();
        ServerPlayer p = source.getPlayer();
        if (p != null) {
            ItemStack stack = p.m_21205_();
            MapItemSavedData data = MapHelper.getMapData(stack, level, p);
            if (data != null) {
                MapHelper.addDecorationToMap(stack, p.m_20097_(), decoration, 0);
            }
        }
        return 0;
    }
}