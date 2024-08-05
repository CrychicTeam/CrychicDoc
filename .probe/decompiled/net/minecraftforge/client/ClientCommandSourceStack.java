package net.minecraftforge.client;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;

public class ClientCommandSourceStack extends CommandSourceStack {

    public ClientCommandSourceStack(CommandSource source, Vec3 position, Vec2 rotation, int permission, String plainTextName, Component displayName, Entity executing) {
        super(source, position, rotation, null, permission, plainTextName, displayName, null, executing);
    }

    @Override
    public void sendSuccess(Supplier<Component> message, boolean sendToAdmins) {
        Minecraft.getInstance().player.sendSystemMessage((Component) message.get());
    }

    @Override
    public Collection<String> getAllTeams() {
        return Minecraft.getInstance().level.getScoreboard().getTeamNames();
    }

    @Override
    public Collection<String> getOnlinePlayerNames() {
        return (Collection<String>) Minecraft.getInstance().getConnection().getOnlinePlayers().stream().map(player -> player.getProfile().getName()).collect(Collectors.toList());
    }

    @Override
    public Stream<ResourceLocation> getRecipeNames() {
        return Minecraft.getInstance().getConnection().getRecipeManager().getRecipeIds();
    }

    @Override
    public Set<ResourceKey<Level>> levels() {
        return Minecraft.getInstance().getConnection().levels();
    }

    @Override
    public RegistryAccess registryAccess() {
        return Minecraft.getInstance().getConnection().registryAccess();
    }

    public Scoreboard getScoreboard() {
        return Minecraft.getInstance().level.getScoreboard();
    }

    public Advancement getAdvancement(ResourceLocation id) {
        return Minecraft.getInstance().getConnection().getAdvancements().getAdvancements().get(id);
    }

    public RecipeManager getRecipeManager() {
        return Minecraft.getInstance().getConnection().getRecipeManager();
    }

    public Level getUnsidedLevel() {
        return Minecraft.getInstance().level;
    }

    @Override
    public MinecraftServer getServer() {
        throw new UnsupportedOperationException("Attempted to get server in client command");
    }

    @Override
    public ServerLevel getLevel() {
        throw new UnsupportedOperationException("Attempted to get server level in client command");
    }
}