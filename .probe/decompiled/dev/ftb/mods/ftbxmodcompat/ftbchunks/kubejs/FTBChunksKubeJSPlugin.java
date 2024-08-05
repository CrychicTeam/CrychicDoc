package dev.ftb.mods.ftbxmodcompat.ftbchunks.kubejs;

import dev.architectury.event.CompoundEventResult;
import dev.ftb.mods.ftbchunks.api.ClaimResult;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.ClaimResult.StandardProblem;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.commands.CommandSourceStack;

public class FTBChunksKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        ClaimedChunkEvent.BEFORE_CLAIM.register((source, chunk) -> this.before(source, chunk, "claim"));
        ClaimedChunkEvent.BEFORE_LOAD.register((source, chunk) -> this.before(source, chunk, "load"));
        ClaimedChunkEvent.BEFORE_UNCLAIM.register((source, chunk) -> this.before(source, chunk, "unclaim"));
        ClaimedChunkEvent.BEFORE_UNLOAD.register((source, chunk) -> this.before(source, chunk, "unload"));
        ClaimedChunkEvent.AFTER_CLAIM.register((source, chunk) -> this.after(source, chunk, "claim"));
        ClaimedChunkEvent.AFTER_LOAD.register((source, chunk) -> this.after(source, chunk, "load"));
        ClaimedChunkEvent.AFTER_UNCLAIM.register((source, chunk) -> this.after(source, chunk, "unclaim"));
        ClaimedChunkEvent.AFTER_UNLOAD.register((source, chunk) -> this.after(source, chunk, "unload"));
        FTBXModCompat.LOGGER.info("[FTB Chunks] Enabled KubeJS integration");
    }

    @Override
    public void registerEvents() {
        FTBChunksKubeJSEvents.EVENT_GROUP.register();
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(ClaimResult.class, (ctx, o) -> StandardProblem.valueOf(o.toString().toUpperCase()));
    }

    private CompoundEventResult<ClaimResult> before(CommandSourceStack source, ClaimedChunk chunk, String id) {
        return FTBChunksKubeJSEvents.BEFORE.post(ScriptType.SERVER, new BeforeEventJS(source, chunk)).archCompound();
    }

    private void after(CommandSourceStack source, ClaimedChunk chunk, String id) {
        FTBChunksKubeJSEvents.AFTER.post(ScriptType.SERVER, id, new AfterEventJS(source, chunk));
    }
}