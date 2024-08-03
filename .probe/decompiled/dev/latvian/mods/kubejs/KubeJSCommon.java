package dev.latvian.mods.kubejs;

import dev.latvian.mods.kubejs.script.ConsoleLine;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.data.ExportablePackResources;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class KubeJSCommon {

    public void init() {
    }

    public void clientSetup() {
    }

    public void reloadClientInternal() {
    }

    public void handleDataFromServerPacket(String channel, @Nullable CompoundTag data) {
    }

    @Nullable
    public Player getClientPlayer() {
        return null;
    }

    public void paint(CompoundTag tag) {
    }

    public void reloadTextures() {
    }

    public void reloadLang() {
    }

    public void generateTypings(CommandSourceStack source) {
    }

    public void reloadConfig() {
        CommonProperties.reload();
        DevProperties.reload();
    }

    public void reloadStartupScripts(boolean dedicated) {
    }

    public void export(List<ExportablePackResources> packs) {
    }

    public void openErrors(ScriptType type) {
    }

    public void openErrors(ScriptType type, List<ConsoleLine> errors, List<ConsoleLine> warnings) {
    }
}