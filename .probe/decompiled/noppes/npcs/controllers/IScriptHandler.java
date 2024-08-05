package noppes.npcs.controllers;

import java.util.List;
import java.util.Map;
import net.minecraftforge.eventbus.api.Event;
import noppes.npcs.constants.EnumScriptType;

public interface IScriptHandler {

    void runScript(EnumScriptType var1, Event var2);

    boolean isClient();

    boolean getEnabled();

    void setEnabled(boolean var1);

    String getLanguage();

    void setLanguage(String var1);

    List<ScriptContainer> getScripts();

    String noticeString();

    Map<Long, String> getConsoleText();

    void clearConsole();
}