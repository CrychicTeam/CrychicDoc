package mezz.jei.core.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TextHistory {

    private static final int MAX_HISTORY = 100;

    private final List<String> history = new LinkedList();

    public boolean add(String currentText) {
        if (currentText.length() > 0) {
            this.history.remove(currentText);
            this.history.add(currentText);
            if (this.history.size() > 100) {
                this.history.remove(0);
            }
            return true;
        } else {
            return false;
        }
    }

    public Optional<String> get(TextHistory.Direction direction, String currentText) {
        return switch(direction) {
            case NEXT ->
                this.getNext(currentText);
            case PREVIOUS ->
                this.getPrevious(currentText);
        };
    }

    public Optional<String> getPrevious(String currentText) {
        int historyIndex = this.history.indexOf(currentText);
        if (historyIndex < 0) {
            if (this.add(currentText)) {
                historyIndex = this.history.size() - 1;
            } else {
                historyIndex = this.history.size();
            }
        }
        if (historyIndex <= 0) {
            return Optional.empty();
        } else {
            String value = (String) this.history.get(historyIndex - 1);
            return Optional.of(value);
        }
    }

    public Optional<String> getNext(String currentText) {
        int historyIndex = this.history.indexOf(currentText);
        if (historyIndex < 0) {
            return Optional.empty();
        } else {
            String historyString;
            if (historyIndex + 1 < this.history.size()) {
                historyString = (String) this.history.get(historyIndex + 1);
            } else {
                historyString = "";
            }
            return Optional.of(historyString);
        }
    }

    public static enum Direction {

        NEXT, PREVIOUS
    }
}