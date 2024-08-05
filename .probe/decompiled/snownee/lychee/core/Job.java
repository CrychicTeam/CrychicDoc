package snownee.lychee.core;

import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.recipe.ILycheeRecipe;

public class Job {

    public PostAction action;

    public int times;

    public Job(PostAction action, int times) {
        this.action = action;
        this.times = times;
    }

    public void apply(ILycheeRecipe<?> recipe, LycheeContext ctx) {
        int t = this.action.checkConditions(recipe, ctx, this.times);
        if (t > 0) {
            this.action.doApply(recipe, ctx, t);
        } else {
            this.action.onFailure(recipe, ctx, this.times);
        }
    }
}