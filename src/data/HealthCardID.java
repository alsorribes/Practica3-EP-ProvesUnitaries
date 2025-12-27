package data;

import exceptions.IncorrectParametersException;

/**
 * The personal identifying code in the National Health Service.
 */
public final class HealthCardID {
    private final String personalID;

    public HealthCardID(String code) throws IncorrectParametersException {
        // Validate that code is not null and not empty
        if(code == null || code.trim().isEmpty()){
            throw new IncorrectParametersException("HealthCardID cannot be null or empty.");
        }

        // Validate that code contains 16 alphanumeric characters
        if(!isValidFormat(code)){
            throw new IncorrectParametersException("HealthCardID has an invalid format: " + code);
        }

        this.personalID = code;
    }

    private boolean isValidFormat(String code){
        return code.length() == 16 && code.matches("^[a-zA-Z0-9]{16}$");
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