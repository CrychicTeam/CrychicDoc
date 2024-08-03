package dev.ftb.mods.ftbquests.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventActor;
import dev.architectury.event.EventFactory;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.task.Task;

public class ObjectCompletedEvent<T extends QuestObject> extends ObjectProgressEvent<T> {

    public static final Event<EventActor<ObjectCompletedEvent<?>>> GENERIC = EventFactory.createEventActorLoop();

    public static final Event<EventActor<ObjectCompletedEvent.FileEvent>> FILE = EventFactory.createEventActorLoop();

    public static final Event<EventActor<ObjectCompletedEvent.ChapterEvent>> CHAPTER = EventFactory.createEventActorLoop();

    public static final Event<EventActor<ObjectCompletedEvent.QuestEvent>> QUEST = EventFactory.createEventActorLoop();

    public static final Event<EventActor<ObjectCompletedEvent.TaskEvent>> TASK = EventFactory.createEventActorLoop();

    private ObjectCompletedEvent(QuestProgressEventData<T> d) {
        super(d);
    }

    static {
        FILE.register(event -> GENERIC.invoker().act(event));
        CHAPTER.register(event -> GENERIC.invoker().act(event));
        QUEST.register(event -> GENERIC.invoker().act(event));
        TASK.register(event -> GENERIC.invoker().act(event));
    }

    public static class ChapterEvent extends ObjectCompletedEvent<Chapter> {

        public ChapterEvent(QuestProgressEventData<Chapter> d) {
            super(d);
        }

        public Chapter getChapter() {
            return (Chapter) this.getObject();
        }
    }

    public static class FileEvent extends ObjectCompletedEvent<BaseQuestFile> {

        public FileEvent(QuestProgressEventData<BaseQuestFile> d) {
            super(d);
        }

        public BaseQuestFile getFile() {
            return (BaseQuestFile) this.getObject();
        }
    }

    public static class QuestEvent extends ObjectCompletedEvent<Quest> {

        public QuestEvent(QuestProgressEventData<Quest> d) {
            super(d);
        }

        public Quest getQuest() {
            return (Quest) this.getObject();
        }
    }

    public static class TaskEvent extends ObjectCompletedEvent<Task> {

        public TaskEvent(QuestProgressEventData<Task> d) {
            super(d);
        }

        public Task getTask() {
            return (Task) this.getObject();
        }
    }
}