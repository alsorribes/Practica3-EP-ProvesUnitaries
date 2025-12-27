package data;

/**
 * The personal identifying code in the National Health Service.
 */
public final class HealthCardID {
    private final String personalID;

    public HealthCardID(String code) {
        this.personalID = code;
    }

    public String getPersonalID() {
        return personalID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthCardID that = (HealthCardID) o;
        return personalID.equals(that.personalID);
    }

    @Override
    public int hashCode() {
        return personalID.hashCode();
    }

    @Override
    public String toString() {
        return "HealthCardID{" + "personalID='" + personalID + '\'' + '}';
    }
}