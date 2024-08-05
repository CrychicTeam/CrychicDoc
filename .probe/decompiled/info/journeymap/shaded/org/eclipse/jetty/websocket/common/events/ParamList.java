package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events;

import java.util.ArrayList;

public class ParamList extends ArrayList<Class<?>[]> {

    public void addParams(Class<?>... paramTypes) {
        this.add(paramTypes);
    }
}