package snownee.loquat.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import snownee.loquat.LoquatConfig;
import snownee.loquat.command.argument.AreaArgument;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.RestrictInstance;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.select.SelectionManager;

public class ListCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) Commands.literal("list").executes(ctx -> {
            CommandSourceStack source = (CommandSourceStack) ctx.getSource();
            AreaManager manager = AreaManager.of(source.getLevel());
            Vec3 position = source.getPosition();
            List<Area> areas = manager.areas().stream().filter($ -> $.getCenter().distanceTo(position) <= (double) LoquatConfig.nearbyRange).toList();
            return list(ctx, areas);
        })).then(Commands.argument("areas", AreaArgument.areas()).executes(ctx -> list(ctx, AreaArgument.getAreas(ctx, "areas"))));
    }

    private static int list(CommandContext<CommandSourceStack> ctx, Collection<? extends Area> areas) throws CommandSyntaxException {
        CommandSourceStack source = (CommandSourceStack) ctx.getSource();
        AreaManager manager = AreaManager.of(source.getLevel());
        ServerPlayer player = source.getPlayer();
        List<UUID> selectedAreas = player == null ? List.of() : SelectionManager.of(player).getSelectedAreas();
        source.sendSystemMessage(Component.translatable("loquat.command.list.total", areas.size()).withStyle(ChatFormatting.YELLOW).append(Component.translatable("loquat.command.list.refresh").withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + ctx.getInput())))));
        for (Area area : areas) {
            Vec3 center = area.getCenter();
            MutableComponent component = Component.empty();
            component.append(Component.translatable("loquat.command.list.pos", (int) center.x, (int) center.y, (int) center.z).withStyle(style -> {
                style = style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/execute in %s run tp @s %.2f %.2f %.2f".formatted(source.getLevel().m_46472_().location(), center.x, center.y, center.z)));
                if (selectedAreas.contains(area.getUuid())) {
                    style = style.withColor(ChatFormatting.GREEN);
                }
                return style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("loquat.command.list.teleport")));
            }));
            component.append(" ");
            component.append(Component.translatable("loquat.command.list.highlight").withStyle(clickEvent("highlight", source, area, null)));
            component.append(" ");
            List<MutableComponent> lines = Lists.newArrayList();
            lines.add(Component.literal("UUID: ").append(((UUID) Objects.requireNonNull(area.getUuid())).toString()));
            if (!area.getTags().isEmpty()) {
                String tags = Joiner.on(", ").join(area.getTags());
                lines.add(Component.translatable("loquat.command.list.more.tags", tags));
            }
            if (area.getAttachedData() != null) {
                lines.add(Component.translatable("loquat.command.list.more.data", NbtUtils.toPrettyComponent(area.getAttachedData())));
            }
            printRestrictions(lines, manager.getFallbackRestriction(), area, "global");
            if (player != null) {
                RestrictInstance restrictInstance = manager.getOrCreateRestrictInstance(player.m_6302_());
                printRestrictions(lines, restrictInstance, area, "player");
            }
            Component info = (Component) lines.stream().reduce((a, b) -> a.append("\n").append(b)).orElseGet(Component::m_237119_);
            component.append(Component.translatable("loquat.command.list.more").withStyle(clickEvent("info", source, area, info.getString())).withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, info))));
            component.append(" ");
            component.append(Component.translatable("loquat.command.list.select").withStyle(clickEvent("select", source, area, null)));
            source.sendSystemMessage(component);
        }
        return areas.size();
    }

    private static void printRestrictions(List<MutableComponent> lines, RestrictInstance restrictInstance, Area area, String key) {
        List<MutableComponent> behaviors = Lists.newArrayList();
        for (RestrictInstance.RestrictBehavior behavior : RestrictInstance.RestrictBehavior.VALUES) {
            if (restrictInstance.isRestricted(area, behavior)) {
                behaviors.add(behavior.getDisplayName().withStyle(ChatFormatting.RED));
            }
        }
        if (!behaviors.isEmpty()) {
            behaviors.stream().reduce((a, b) -> a.append(", ").append(b)).ifPresent(component -> lines.add(Component.translatable("loquat.command.list.more.restrict." + key, component)));
        }
    }

    private static UnaryOperator<Style> clickEvent(String type, CommandSourceStack source, Area area, @Nullable String extra) {
        return style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "@loquat %s %s %s %s".formatted(type, source.getLevel().m_46472_().location(), area.getUuid(), extra)));
    }
}