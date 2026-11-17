## MySQL Database Design

1. Table: patients

  Stores information about registered patients.
  
  id: BIGINT, Primary Key, Auto Increment
  
  first_name: VARCHAR(100), NOT NULL
  
  last_name: VARCHAR(100), NOT NULL
  
  email: VARCHAR(150), UNIQUE, NOT NULL
  
  phone: VARCHAR(15), UNIQUE, NOT NULL
  
  date_of_birth: DATE, NOT NULL
  
  gender: VARCHAR(20)
  
  address: TEXT
  
  created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP
  
  updated_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP


2. Table: doctors

  Stores information about registered doctors.
  
  id: BIGINT, Primary Key, Auto Increment
  
  first_name: VARCHAR(100), NOT NULL
  
  last_name: VARCHAR(100), NOT NULL
  
  email: VARCHAR(150), UNIQUE, NOT NULL
  
  phone: VARCHAR(15), UNIQUE, NOT NULL
  
  specialization: VARCHAR(100), NOT NULL
  
  qualification: VARCHAR(255)
  
  experience_years: INT
  
  consultation_fee: DECIMAL(10,2), NOT NULL
  
  clinic_location_id: BIGINT, Foreign Key → clinic_locations(id)
  
  created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP


3.  Table: admin

  Stores administrator account information.
  
  id: BIGINT, Primary Key, Auto Increment
  
  name: VARCHAR(150), NOT NULL
  
  email: VARCHAR(150), UNIQUE, NOT NULL
  
  password_hash: VARCHAR(255), NOT NULL
  
  role: VARCHAR(50), DEFAULT 'ADMIN'
  
  created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP
  
  Note: Passwords must be stored as secure hashes, never as plain text.


4. Table: clinic_locations

  Stores details about different clinic branches.
  
  id: BIGINT, Primary Key, Auto Increment
  
  clinic_name: VARCHAR(150), NOT NULL
  
  address: TEXT, NOT NULL
  
  city: VARCHAR(100), NOT NULL
  
  state: VARCHAR(100), NOT NULL
  
  postal_code: VARCHAR(10)
  
  phone: VARCHAR(15)
  
  created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP


5. Table: appointments

  Manages appointments between patients and doctors.
  
  id: BIGINT, Primary Key, Auto Increment
  
  patient_id: BIGINT, Foreign Key → patients(id), NOT NULL
  
  doctor_id: BIGINT, Foreign Key → doctors(id), NOT NULL
  
  appointment_time: DATETIME, NOT NULL
  
  duration_minutes: INT, DEFAULT 30
  
  reason: TEXT
  
  status: VARCHAR(20), DEFAULT 'SCHEDULED'
  
  created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP
  
  updated_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  
  Appointment status values:
  
  SCHEDULED
  
  COMPLETED
  
  CANCELLED
  
  NO_SHOW
   

6. Table: payments

   Stores payment information associated with appointments.

   id: BIGINT, Primary Key, Auto Increment
  
   appointment_id: BIGINT, Foreign Key → appointments(id), NOT NULL
  
   amount: DECIMAL(10,2), NOT NULL
  
   payment_method: VARCHAR(30), NOT NULL
  
   payment_status: VARCHAR(20), DEFAULT 'PENDING'
  
   transaction_reference: VARCHAR(150), UNIQUE
  
   payment_date: DATETIME
  
   created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP
  
   Payment status values:
  
   PENDING
  
   COMPLETED
  
   FAILED
  
   REFUNDED




## MongoDB Collection Design

Collection: prescriptions

The prescriptions collection stores prescriptions created by doctors for patients following consultations.

Example document:

{
  "_id": "ObjectId('673abc1234567890abcdef12')",
  "patientId": 101,
  "doctorId": 201,
  "appointmentId": 501,
  "prescriptionDate": "2026-11-17T10:30:00Z",
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Twice a day",
      "duration": "5 days",
      "instructions": "Take after meals"
    },
    {
      "name": "Vitamin C",
      "dosage": "500mg",
      "frequency": "Once a day",
      "duration": "10 days",
      "instructions": "Take after breakfast"
    }
  ],
  "doctorNotes": "Patient has mild fever and requires rest.",
  "followUp": {
    "required": true,
    "date": "2026-11-24"
  },
  "attachments": [
    {
      "fileName": "blood-test-report.pdf",
      "fileType": "application/pdf",
      "storageUrl": "https://storage.example.com/reports/blood-test-report.pdf"
    }
  ],
  "tags": [
    "fever",
    "general-medicine"
  ],
  "metadata": {
    "createdBy": "doctor-201",
    "version": 1,
    "source": "online-consultation"
  },
  "createdAt": "2026-11-17T10:30:00Z",
  "updatedAt": "2026-11-17T10:30:00Z"
}
