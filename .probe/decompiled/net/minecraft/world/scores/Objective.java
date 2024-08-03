package net.minecraft.world.scores;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class Objective {

    private final Scoreboard scoreboard;

    private final String name;

    private final ObjectiveCriteria criteria;

    private Component displayName;

    private Component formattedDisplayName;

    private ObjectiveCriteria.RenderType renderType;

    public Objective(Scoreboard scoreboard0, String string1, ObjectiveCriteria objectiveCriteria2, Component component3, ObjectiveCriteria.RenderType objectiveCriteriaRenderType4) {
        this.scoreboard = scoreboard0;
        this.name = string1;
        this.criteria = objectiveCriteria2;
        this.displayName = component3;
        this.formattedDisplayName = this.createFormattedDisplayName();
        this.renderType = objectiveCriteriaRenderType4;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public String getName() {
        return this.name;
    }

    public ObjectiveCriteria getCriteria() {
        return this.criteria;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    private Component createFormattedDisplayName() {
        return ComponentUtils.wrapInSquareBrackets(this.displayName.copy().withStyle(p_83319_ -> p_83319_.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(this.name)))));
    }

    public Component getFormattedDisplayName() {
        return this.formattedDisplayName;
    }

    public void setDisplayName(Component component0) {
        this.displayName = component0;
        this.formattedDisplayName = this.createFormattedDisplayName();
        this.scoreboard.onObjectiveChanged(this);
    }

    public ObjectiveCriteria.RenderType getRenderType() {
        return this.renderType;
    }

    public void setRenderType(ObjectiveCriteria.RenderType objectiveCriteriaRenderType0) {
        this.renderType = objectiveCriteriaRenderType0;
        this.scoreboard.onObjectiveChanged(this);
    }
}