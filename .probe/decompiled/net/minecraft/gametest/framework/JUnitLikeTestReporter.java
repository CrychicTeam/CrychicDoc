package net.minecraft.gametest.framework;

import com.google.common.base.Stopwatch;
import java.io.File;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JUnitLikeTestReporter implements TestReporter {

    private final Document document;

    private final Element testSuite;

    private final Stopwatch stopwatch;

    private final File destination;

    public JUnitLikeTestReporter(File file0) throws ParserConfigurationException {
        this.destination = file0;
        this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        this.testSuite = this.document.createElement("testsuite");
        Element $$1 = this.document.createElement("testsuite");
        $$1.appendChild(this.testSuite);
        this.document.appendChild($$1);
        this.testSuite.setAttribute("timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
        this.stopwatch = Stopwatch.createStarted();
    }

    private Element createTestCase(GameTestInfo gameTestInfo0, String string1) {
        Element $$2 = this.document.createElement("testcase");
        $$2.setAttribute("name", string1);
        $$2.setAttribute("classname", gameTestInfo0.getStructureName());
        $$2.setAttribute("time", String.valueOf((double) gameTestInfo0.getRunTime() / 1000.0));
        this.testSuite.appendChild($$2);
        return $$2;
    }

    @Override
    public void onTestFailed(GameTestInfo gameTestInfo0) {
        String $$1 = gameTestInfo0.getTestName();
        String $$2 = gameTestInfo0.getError().getMessage();
        Element $$3;
        if (gameTestInfo0.isRequired()) {
            $$3 = this.document.createElement("failure");
            $$3.setAttribute("message", $$2);
        } else {
            $$3 = this.document.createElement("skipped");
            $$3.setAttribute("message", $$2);
        }
        Element $$5 = this.createTestCase(gameTestInfo0, $$1);
        $$5.appendChild($$3);
    }

    @Override
    public void onTestSuccess(GameTestInfo gameTestInfo0) {
        String $$1 = gameTestInfo0.getTestName();
        this.createTestCase(gameTestInfo0, $$1);
    }

    @Override
    public void finish() {
        this.stopwatch.stop();
        this.testSuite.setAttribute("time", String.valueOf((double) this.stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0));
        try {
            this.save(this.destination);
        } catch (TransformerException var2) {
            throw new Error("Couldn't save test report", var2);
        }
    }

    public void save(File file0) throws TransformerException {
        TransformerFactory $$1 = TransformerFactory.newInstance();
        Transformer $$2 = $$1.newTransformer();
        DOMSource $$3 = new DOMSource(this.document);
        StreamResult $$4 = new StreamResult(file0);
        $$2.transform($$3, $$4);
    }
}