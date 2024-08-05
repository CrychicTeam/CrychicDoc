package info.journeymap.shaded.org.javax.servlet.http;

public interface HttpServletMapping {

    String getMatchValue();

    String getPattern();

    String getServletName();

    MappingMatch getMappingMatch();
}