package snownee.lychee.core;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntIterator;
import java.util.LinkedList;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import snownee.lychee.Lychee;
import snownee.lychee.core.post.Delay;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.CommonProxy;

public class ActionRuntime {

    public boolean doDefault = true;

    public ActionRuntime.State state = ActionRuntime.State.RUNNING;

    public final LinkedList<Job> jobs = Lists.newLinkedList();

    public Delay.LycheeMarker marker;

    void enqueue(Stream<PostAction> actions, int times) {
        this.jobs.addAll(0, actions.map($ -> new Job($, times)).toList());
    }

    public void run(ILycheeRecipe<?> recipe, LycheeContext ctx) {
        ILycheeRecipe.NBTPatchContext patchContext = (ILycheeRecipe.NBTPatchContext) ILycheeRecipe.patchContexts.get(recipe.lychee$getId());
        if (patchContext != null && ctx.json == null) {
            ctx.json = patchContext.template().deepCopy();
            IntIterator e = patchContext.usedIndexes().iterator();
            while (e.hasNext()) {
                Integer index = (Integer) e.next();
                ItemStack item = ctx.getItem(index);
                ctx.json.add(index.toString(), CommonProxy.tagToJson(item.save(new CompoundTag())));
            }
            this.jobs.forEach($ -> $.action.preApply(recipe, ctx, patchContext));
            e = patchContext.usedIndexes().iterator();
            while (e.hasNext()) {
                Integer index = (Integer) e.next();
                try {
                    CompoundTag tag = CommonProxy.jsonToTag(ctx.json.get(Integer.toString(index)));
                    ctx.setItem(index, ItemStack.of(tag));
                } catch (Throwable var7) {
                    Lychee.LOGGER.error("Error parsing json result into item " + ctx.json, var7);
                    ctx.runtime.state = ActionRuntime.State.STOPPED;
                }
            }
        }
        try {
            while (!this.jobs.isEmpty()) {
                ((Job) this.jobs.pop()).apply(recipe, ctx);
                if (ctx.runtime.state != ActionRuntime.State.RUNNING) {
                    break;
                }
            }
            if (ctx.runtime.state == ActionRuntime.State.RUNNING || this.jobs.isEmpty()) {
                ctx.runtime.state = ActionRuntime.State.STOPPED;
            }
        } catch (Throwable var8) {
            Lychee.LOGGER.error("Error running actions", var8);
            ctx.runtime.state = ActionRuntime.State.STOPPED;
        }
    }

    public static enum State {

        RUNNING, PAUSED, STOPPED
    }
}