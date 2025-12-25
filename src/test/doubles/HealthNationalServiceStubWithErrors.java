package test.doubles;

import data.HealthCardID;
import exceptions.*;
import medicalconsultation.MedicalHistory;
import medicalconsultation.MedicalPrescription;
import services.HealthNationalService;

import java.net.ConnectException;

/**
 * Test Double (Stub) for HealthNationalService - Error scenarios.
 * Simulates various error conditions for testing exception handling.
 */
public class HealthNationalServiceStubWithErrors implements HealthNationalService {

    // Configuration for which errors to throw
    private boolean throwConnectException;
    private boolean throwHealthCardIDException;
    private boolean throwAnyCurrentPrescriptionException;
    private boolean throwNotCompletedPrescriptionException;

    public HealthNationalServiceStubWithErrors() {
        this.throwConnectException = false;
        this.throwHealthCardIDException = false;
        this.throwAnyCurrentPrescriptionException = false;
        this.throwNotCompletedPrescriptionException = false;
    }

    // Configuration methods
    public void setThrowConnectException(boolean value) {
        this.throwConnectException = value;
    }

    public void setThrowHealthCardIDException(boolean value) {
        this.throwHealthCardIDException = value;
    }

    public void setThrowAnyCurrentPrescriptionException(boolean value) {
        this.throwAnyCurrentPrescriptionException = value;
    }

    public void setThrowNotCompletedPrescriptionException(boolean value) {
        this.throwNotCompletedPrescriptionException = value;
    }

    @Override
    public MedicalHistory getMedicalHistory(HealthCardID cip)
            throws ConnectException, HealthCardIDException {

        if (throwConnectException) {
            throw new ConnectException("Simulated network failure");
        }

        if (throwHealthCardIDException) {
            throw new HealthCardIDException("Patient ID not registered");
        }

        // If no error configured, return a basic history
        return new MedicalHistory(cip, 12345);
    }

    @Override
    public MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException {

        if (throwConnectException) {
            throw new ConnectException("Simulated network failure");
        }

        if (throwHealthCardIDException) {
            throw new HealthCardIDException("Patient ID not registered");
        }

        if (throwAnyCurrentPrescriptionException) {
            throw new AnyCurrentPrescriptionException(
                    "No active prescription for illness: " + illness);
        }

        // If no error configured, return a basic prescription
        return new MedicalPrescription(cip, 12345, illness);
    }

    @Override
    public MedicalPrescription sendHistoryAndPrescription(
            HealthCardID cip, MedicalHistory hce, String illness, MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException, NotCompletedMedicalPrescriptionException {

        if (throwConnectException) {
            throw new ConnectException("Simulated network failure");
        }

        if (throwHealthCardIDException) {
            throw new HealthCardIDException("Patient ID not registered");
        }

        if (throwAnyCurrentPrescriptionException) {
            throw new AnyCurrentPrescriptionException(
                    "No active prescription for this illness");
        }

        if (throwNotCompletedPrescriptionException) {
            throw new NotCompletedMedicalPrescriptionException(
                    "Prescription validation failed");
        }

        // If no error configured, generate code
        return generateTreatmCodeAndRegister(mPresc);
    }

    @Override
    public MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc)
            throws ConnectException {

        if (throwConnectException) {
            throw new ConnectException("Simulated network failure during code generation");
        }

        // If no error, just return the same prescription (simplified)
        return ePresc;
    }
}