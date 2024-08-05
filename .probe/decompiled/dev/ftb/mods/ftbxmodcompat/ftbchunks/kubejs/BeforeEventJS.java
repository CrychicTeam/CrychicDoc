package dev.ftb.mods.ftbxmodcompat.ftbchunks.kubejs;

import dev.ftb.mods.ftbchunks.api.ClaimResult;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.latvian.mods.kubejs.event.EventExit;
import net.minecraft.commands.CommandSourceStack;

public class BeforeEventJS extends AfterEventJS {

    private ClaimResult result;

    public BeforeEventJS(CommandSourceStack source, ClaimedChunk chunk) {
        super(source, chunk);
        this.result = chunk;
    }

    public ClaimResult getResult() {
        return this.result;
    }

    public void setResult(ClaimResult r) throws EventExit {
        this.result = r;
        this.cancel(this.result);
    }

    public void setCustomResult(String messageKey) throws EventExit {
        this.result = ClaimResult.customProblem(messageKey);
        this.cancel(this.result);
    }
}