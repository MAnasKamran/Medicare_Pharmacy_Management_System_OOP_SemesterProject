package com.pharmacy.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Prescription extends Entity {
    private static final long serialVersionUID = 1L;

    private String        prescriptionId;
    private String        patientId;
    private String        doctorName;
    private List<Medicine> medicines;
    private boolean       fulfilled;
    private LocalDate     issueDate;

    public Prescription() {
        super();
        this.medicines = new ArrayList<>();
        this.issueDate = LocalDate.now();
    }

    @Override
    public String getDisplayName() {
        return "Prescription " + (prescriptionId != null ? prescriptionId : getId());
    }

    @Override
    public boolean isValid() {
        return doctorName != null && !doctorName.isBlank();
    }

    public boolean validate() {
        return isValid() && medicines != null && !medicines.isEmpty();
    }

    public void fulfill() { this.fulfilled = true; }

    // getters / setters
    public String         getPrescriptionId()          { return prescriptionId; }
    public void           setPrescriptionId(String v)  { this.prescriptionId = v; }
    public String         getPatientId()               { return patientId; }
    public void           setPatientId(String v)       { this.patientId = v; }
    public String         getDoctorName()              { return doctorName; }
    public void           setDoctorName(String v)      { this.doctorName = v; }
    public List<Medicine> getMedicines()               { return medicines; }
    public void           setMedicines(List<Medicine> v){ this.medicines = v != null ? v : new ArrayList<>(); }
    public boolean        isFulfilled()                { return fulfilled; }
    public void           setFulfilled(boolean v)      { this.fulfilled = v; }
    public LocalDate      getIssueDate()               { return issueDate; }
    public void           setIssueDate(LocalDate v)    { this.issueDate = v; }
}
