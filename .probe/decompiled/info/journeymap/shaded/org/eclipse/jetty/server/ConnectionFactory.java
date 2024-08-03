package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.BadMessageException;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpFields;
import info.journeymap.shaded.org.eclipse.jetty.http.MetaData;
import info.journeymap.shaded.org.eclipse.jetty.io.Connection;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import java.util.List;

public interface ConnectionFactory {

    String getProtocol();

    List<String> getProtocols();

    Connection newConnection(Connector var1, EndPoint var2);

    public interface Upgrading extends ConnectionFactory {

        Connection upgradeConnection(Connector var1, EndPoint var2, MetaData.Request var3, HttpFields var4) throws BadMessageException;
    }
}