package data;

/**
 * TEMPORARY -
 * The electronic prescription code.
 */
public final class ePrescripCode {
    private final String code;

    public ePrescripCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ePrescripCode that = (ePrescripCode) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "ePrescripCode{" + "code='" + code + '\'' + '}';
    }
}