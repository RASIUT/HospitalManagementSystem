package HospitalManagementSystem;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

public class HospitalManagementSystem {
	private static final String url = "jdbc:mysql://localhost:3306/hospital";
	private static final String username = "root";
	private static final String password = "RMRout@0209";

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Scanner scanner = new Scanner(System.in);

		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Patient patient = new Patient(connection, scanner);
			Doctors doctors = new Doctors(connection);
			while(true) {
				System.out.println("HOSPITAL MANAGEMENT SYSTEM");
				System.out.println("1. Add Patient");
				System.out.println("2. View Patients");
				System.out.println("3. View Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. EXIT");
				System.out.println("ENTER YOUR CHOICE: ");
				int choice = scanner.nextInt();
				switch(choice) {
					case 1: 
						//Add Patient
						patient.addPatient();
						System.out.println();
					case 2: 
						//View Patients
						patient.viewPatient();
						System.out.println();
					case 3:
						//View Doctorws
						doctors.viewDoctors();
						System.out.println();
					case 4:
						//Book apointment
						BookAppointment(patient, doctors, connection, scanner);
						System.out.println();
					case 5:
						return;
					default:
						System.out.println("INVALID CHOICE");
				}
			}

		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void BookAppointment(Patient patient, Doctors doctors, Connection connection, Scanner scanner) {
		System.out.print("ENTER PATIENT ID: ");
		int pid = scanner.nextInt();
		System.out.print("ENTER DOCTOR ID: ");
		int docid = scanner.nextInt();
		System.out.print("ENTER APPOINTMENT DATE (YYYY-MM-DD): ");
		String appointmentDate = scanner.nextLine();

		if(patient.getPatientById(pid) && doctors.getDoctorById(docid)) {
			if(checkDoctorAvailability(connection, docid, appointmentDate)) {
				String appointmentQuery = "INSERT INTO APPOINTMENTS(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
					preparedStatement.setInt(1, pid);
					preparedStatement.setInt(2, docid);
					preparedStatement.setString(3, appointmentDate);
					int affectedRows = preparedStatement.executeUpdate();
					if(affectedRows > 0) {
						System.out.println("Appointment Booked!");
					} else {
						System.out.println("Failed to book appointment!");
					}
				} catch(SQLException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("DOCTOR NOT AVAILABLE ON THIS DATE!");
			}
		} else {
			System.out.println("EITHER PATIENT OR DOCTOR DOESN'T EXIST");
		}
	}
	public static boolean checkDoctorAvailability(Connection connection, int docid, String appointmentDate) {
		String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, docid);
			preparedStatement.setString(2, appointmentDate);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				int count = resultSet.getInt(1);
				if(count == 0) {
					return true;
				} else {
					return false;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
