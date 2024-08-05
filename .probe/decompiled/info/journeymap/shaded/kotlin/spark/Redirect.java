package info.journeymap.shaded.kotlin.spark;

public final class Redirect {

    private Routable http;

    static Redirect create(Routable http) {
        return new Redirect(http);
    }

    private Redirect(Routable http) {
        this.http = http;
    }

    public void any(String fromPath, String toPath) {
        this.any(fromPath, toPath, null);
    }

    public void get(String fromPath, String toPath) {
        this.get(fromPath, toPath, null);
    }

    public void post(String fromPath, String toPath) {
        this.post(fromPath, toPath, null);
    }

    public void put(String fromPath, String toPath) {
        this.put(fromPath, toPath, null);
    }

    public void delete(String fromPath, String toPath) {
        this.delete(fromPath, toPath, null);
    }

    public void any(String fromPath, String toPath, Redirect.Status status) {
        this.get(fromPath, toPath, status);
        this.post(fromPath, toPath, status);
        this.put(fromPath, toPath, status);
        this.delete(fromPath, toPath, status);
    }

    public void get(String fromPath, String toPath, Redirect.Status status) {
        this.http.get(fromPath, redirectRoute(toPath, status));
    }

    public void post(String fromPath, String toPath, Redirect.Status status) {
        this.http.post(fromPath, redirectRoute(toPath, status));
    }

    public void put(String fromPath, String toPath, Redirect.Status status) {
        this.http.put(fromPath, redirectRoute(toPath, status));
    }

    public void delete(String fromPath, String toPath, Redirect.Status status) {
        this.http.delete(fromPath, redirectRoute(toPath, status));
    }

    private static Route redirectRoute(String toPath, Redirect.Status status) {
        return (req, res) -> {
            if (status != null) {
                res.redirect(toPath, status.intValue());
            } else {
                res.redirect(toPath);
            }
            return null;
        };
    }

    public static enum Status {

        MULTIPLE_CHOICES(300),
        MOVED_PERMANENTLY(301),
        FOUND(302),
        SEE_OTHER(303),
        NOT_MODIFIED(304),
        USE_PROXY(305),
        SWITCH_PROXY(306),
        TEMPORARY_REDIRECT(307),
        PERMANENT_REDIRECT(308);

        private int intValue;

        private Status(int intValue) {
            this.intValue = intValue;
        }

        public int intValue() {
            return this.intValue;
        }
    }
}