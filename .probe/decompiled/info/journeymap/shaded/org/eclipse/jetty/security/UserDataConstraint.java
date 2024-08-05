package info.journeymap.shaded.org.eclipse.jetty.security;

public enum UserDataConstraint {

    None, Integral, Confidential;

    public static UserDataConstraint get(int dataConstraint) {
        if (dataConstraint < -1 || dataConstraint > 2) {
            throw new IllegalArgumentException("Expected -1, 0, 1, or 2, not: " + dataConstraint);
        } else {
            return dataConstraint == -1 ? None : values()[dataConstraint];
        }
    }

    public UserDataConstraint combine(UserDataConstraint other) {
        return this.compareTo(other) < 0 ? this : other;
    }
}