package data;

import exceptions.IncorrectParametersException;

/**
 * The electronic prescription code.
 */
public final class ePrescripCode {
    private final String code;

    public ePrescripCode(String code) throws IncorrectParametersException {
        // Validate that code is not null and not empty
        if(code == null || code.trim().isEmpty()){
            throw new IncorrectParametersException("ePrescriptionCode cannot be null or empty.");
        }

        // Validate that code contains 16 alphanumeric characters
        if(!isValidFormat(code)){
            throw new IncorrectParametersException("ePrescriptionCode has an invalid format: " + code);
        }

        this.code = code;
    }

    private boolean isValidFormat(String code){
        return code.length() == 16 && code.matches("^[a-zA-Z0-9]{16}$");
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