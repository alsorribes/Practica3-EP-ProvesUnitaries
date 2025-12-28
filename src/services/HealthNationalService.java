package services;

import data.HealthCardID;
import medicalconsultation.MedicalHistory;
import medicalconsultation.MedicalPrescription;
import exceptions.*;
import java.net.ConnectException;

/**
 * External services for managing and storing ePrescriptions from population and IA support.
 * This interface represents the Health National Service (Sistema Nacional de Salud - SNS).
 *
 * All methods may throw ConnectException if network fails.
 */
public interface HealthNationalService {

    /**
     * Retrieves the medical history of a patient from the National Health Service.
     *
     * @param cip the personal identification code (HealthCardID) of the patient
     * @return the MedicalHistory of the patient
     * @throws ConnectException if the network connection fails
     * @throws HealthCardIDException if the patient ID is not registered in the HNS
     */
    MedicalHistory getMedicalHistory(HealthCardID cip)
            throws ConnectException, HealthCardIDException, IncorrectParametersException;

    /**
     * Retrieves the medical prescription of a patient for a specific illness.
     *
     * @param cip the personal identification code of the patient
     * @param illness the illness name for which the prescription is requested
     * @return the MedicalPrescription for that patient and illness
     * @throws ConnectException if the network connection fails
     * @throws HealthCardIDException if the patient ID is not registered
     * @throws AnyCurrentPrescriptionException if no active prescription exists for that illness
     */
    MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException;

    /**
     * Sends the updated medical history and prescription to the HNS for remote storage.
     * If successful, generates a new treatment code for the prescription.
     *
     * @param cip the patient's health card ID
     * @param hce the updated medical history
     * @param illness the illness associated with the prescription
     * @param mPresc the updated medical prescription
     * @return the MedicalPrescription with the new treatment code assigned by HNS
     * @throws ConnectException if the network connection fails
     * @throws HealthCardIDException if the patient ID is not registered
     * @throws AnyCurrentPrescriptionException if no active prescription exists
     * @throws NotCompletedMedicalPrescriptionException if the prescription is incomplete
     */
    MedicalPrescription sendHistoryAndPrescription(HealthCardID cip, MedicalHistory hce,
                                                   String illness, MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException, NotCompletedMedicalPrescriptionException, IncorrectParametersException;

    /**
     * Internal operation: generates a new treatment code and registers it in the system.
     *
     * @param ePresc the medical prescription to register
     * @return the MedicalPrescription with the generated code
     * @throws ConnectException if the network connection fails
     */
    MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc)
            throws ConnectException, IncorrectParametersException;
}