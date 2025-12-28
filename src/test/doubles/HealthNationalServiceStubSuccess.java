package test.doubles;

import data.HealthCardID;
import data.ePrescripCode;
import exceptions.*;
import medicalconsultation.MedicalHistory;
import medicalconsultation.MedicalPrescription;
import services.HealthNationalService;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test Double (Stub) for HealthNationalService - Success scenario.
 * Simulates successful interactions with the Health National Service.
 * Returns valid data for all operations.
 */
public class HealthNationalServiceStubSuccess implements HealthNationalService {

    // Simulated database of patients
    private Map<String, MedicalHistory> medicalHistories;
    private Map<String, MedicalPrescription> prescriptions;

    public HealthNationalServiceStubSuccess() {
        this.medicalHistories = new HashMap<>();
        this.prescriptions = new HashMap<>();
    }

    @Override
    public MedicalHistory getMedicalHistory(HealthCardID cip)
            throws ConnectException, HealthCardIDException, IncorrectParametersException {

        // Simulate successful retrieval
        String cipCode = cip.getPersonalID();

        // If not in our "database", create a new one
        if (!medicalHistories.containsKey(cipCode)) {
            try {
                MedicalHistory history = new MedicalHistory(cip, 12345);
                medicalHistories.put(cipCode, history);
            } catch (IncorrectParametersException e) {
                // This should never happen in our test stub
                throw new RuntimeException("Test stub configuration error: " + e.getMessage(), e);
            }
        }

        return medicalHistories.get(cipCode);
    }

    @Override
    public MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException {

        // Simulate successful retrieval
        String key = cip.getPersonalID() + "_" + illness;

        // If not in our "database", create a new one
        if (!prescriptions.containsKey(key)) {
            try {
                MedicalPrescription prescription = new MedicalPrescription(cip, 12345, illness);
                prescriptions.put(key, prescription);
            } catch (IncorrectParametersException e) {
                // This should never happen in our test stub
                throw new RuntimeException("Test stub configuration error: " + e.getMessage(), e);
            }
        }

        return prescriptions.get(key);
    }

    @Override
    public MedicalPrescription sendHistoryAndPrescription(
            HealthCardID cip, MedicalHistory hce, String illness, MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException, NotCompletedMedicalPrescriptionException, IncorrectParametersException {

        // Validate prescription is complete (has signature and dates)
        if (mPresc.geteSign() == null) {
            throw new NotCompletedMedicalPrescriptionException(
                    "Prescription missing electronic signature");
        }

        if (mPresc.getPrescDate() == null || mPresc.getEndDate() == null) {
            throw new NotCompletedMedicalPrescriptionException(
                    "Prescription missing dates");
        }

        // Generate treatment code and register
        return generateTreatmCodeAndRegister(mPresc);
    }

    @Override
    public MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc)
            throws ConnectException, IncorrectParametersException {

        // Generate a new treatment code (16 alphanumeric characters as required)
        String newCode = String.format("TREAT%012d", System.currentTimeMillis() % 1000000000000L);
        ePrescripCode prescCode = new ePrescripCode(newCode);

        // Set the code on the prescription
        ePresc.setPrescCode(prescCode);

        // Store in our "database"
        String key = ePresc.getCip().getPersonalID() + "_" + ePresc.getIllness();
        prescriptions.put(key, ePresc);

        // Return the updated prescription
        return ePresc;
    }
}