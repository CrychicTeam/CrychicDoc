package dev.ftb.mods.ftbxmodcompat.ftbquests.kubejs;

import dev.ftb.mods.ftbquests.events.CustomTaskEvent;
import dev.ftb.mods.ftbquests.quest.task.CustomTask;
import dev.latvian.mods.kubejs.event.EventJS;

public class CustomTaskEventJS extends EventJS {

    public final CustomTaskEvent event;

    CustomTaskEventJS(CustomTaskEvent e) {
        this.event = e;
    }

    public CustomTask getTask() {
        return this.event.getTask();
    }

    public void setCheck(CustomTask.Check c) {
        this.getTask().setCheck(c);
    }

    public void setCheckTimer(int t) {
        this.getTask().setCheckTimer(t);
    }

    public void setEnableButton(boolean b) {
        this.getTask().setEnableButton(b);
    }

    public void setMaxProgress(long max) {
        this.getTask().setMaxProgress(max);
    }
}