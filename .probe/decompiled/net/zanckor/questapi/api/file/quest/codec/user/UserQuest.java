package net.zanckor.questapi.api.file.quest.codec.user;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.FileAbstract;
import net.zanckor.questapi.api.file.quest.codec.server.ServerGoal;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.registry.EnumRegistry;

public class UserQuest extends FileAbstract {

    private String id;

    private String title;

    private List<UserGoal> questGoals;

    private boolean timeLimit;

    private int timeLimitInSeconds;

    private boolean completed;

    private String description;

    public static UserQuest createQuest(ServerQuest serverQuest, Path path) {
        UserQuest userQuest = new UserQuest();
        List<UserGoal> questGoalList = new ArrayList();
        String questModid = path.getFileName().toString().split(File.separator + ".")[0];
        userQuest.setId(serverQuest.getId());
        userQuest.setTitle(serverQuest.getTitle());
        userQuest.setTimeLimit(serverQuest.hasTimeLimit());
        userQuest.setTimeLimitInSeconds(serverQuest.getTimeLimitInSeconds());
        userQuest.setCompleted(false);
        userQuest.setDescription(serverQuest.getDescription());
        for (int goalsIndex = 0; goalsIndex < serverQuest.getGoalList().size(); goalsIndex++) {
            UserGoal questGoal = UserGoal.createQuestGoal((ServerGoal) serverQuest.getGoalList().get(goalsIndex), questModid);
            Enum<?> goalEnum = EnumRegistry.getEnum(questGoal.getType(), EnumRegistry.getQuestGoal());
            questGoalList.add(questGoal);
            QuestDialogManager.registerQuestTypeLocation(goalEnum, path);
        }
        userQuest.setQuestGoals(questGoalList);
        QuestDialogManager.registerQuestByID(userQuest.getId(), path);
        return userQuest;
    }

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

    public boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<UserGoal> getQuestGoals() {
        return this.questGoals;
    }

    public void setQuestGoals(List<UserGoal> questGoals) {
        this.questGoals = questGoals;
    }

    public boolean hasTimeLimit() {
        return this.timeLimit;
    }

    public void setTimeLimit(boolean timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getTimeLimitInSeconds() {
        return this.timeLimitInSeconds;
    }

    public void setTimeLimitInSeconds(int timeLimitInSeconds) {
        this.timeLimitInSeconds = timeLimitInSeconds;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}