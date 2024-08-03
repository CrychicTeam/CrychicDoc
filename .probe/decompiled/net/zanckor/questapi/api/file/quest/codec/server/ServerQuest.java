package net.zanckor.questapi.api.file.quest.codec.server;

import java.util.List;
import net.zanckor.questapi.api.file.FileAbstract;

public class ServerQuest extends FileAbstract {

    private String id;

    private String title;

    private boolean hasTimeLimit;

    private int timeLimitInSeconds;

    private List<ServerGoal> goals;

    private List<ServerReward> rewards;

    private List<ServerRequirement> requirements;

    private String description;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasTimeLimit() {
        return this.hasTimeLimit;
    }

    public void setHasTimeLimit(boolean hasTimeLimit) {
        this.hasTimeLimit = hasTimeLimit;
    }

    public int getTimeLimitInSeconds() {
        return this.timeLimitInSeconds;
    }

    public void setTimeLimitInSeconds(int timeLimitInSeconds) {
        this.timeLimitInSeconds = timeLimitInSeconds;
    }

    public List<ServerGoal> getGoalList() {
        return this.goals;
    }

    public void setGoalList(List<ServerGoal> goals) {
        this.goals = goals;
    }

    public List<ServerReward> getRewards() {
        return this.rewards;
    }

    public void setRewards(List<ServerReward> ServerRewards) {
        this.rewards = ServerRewards;
    }

    public List<ServerRequirement> getRequirements() {
        return this.requirements;
    }

    public void setRequirements(List<ServerRequirement> ServerRequirements) {
        this.requirements = ServerRequirements;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}