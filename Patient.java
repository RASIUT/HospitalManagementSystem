package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;

public class Patient {
	private Connection connection;
	private Scanner scanner;
	
	public Patient(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public void addPatient() {
		System.out.print("Enter patient Name: ");
		String name = scanner.nextLine();
		scanner.nextLine();
		System.out.print("Enter patient Age: ");
		int age = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Enter patient Gender: ");
		String gender = scanner.next();

		try {
			String querry = "INSERT INTO PATIENTS(name, age, gender) VALUES(?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(querry);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, age);
			preparedStatement.setString(3, gender);

			int affectedRows = preparedStatement.executeUpdate();
			if(affectedRows > 0) {
				System.out.println("Patient added successfully");
			} else {
				System.out.println("Failed to add patient");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void viewPatient() {
		String query = "SELECT * FROM PATIENTS";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("Patients: ");
			System.out.println("+----+--------------------------------------+------+-------+");
			System.out.println("| ID | Name                                 | Age  | Gender|");
			System.out.println("+----|--------------------------------------|------|-------+");
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				int age = resultSet.getInt("age");
				String gender = resultSet.getString("gender");
				System.out.printf("|%-4s|%-38s|%-6s|%-7s|\n", id, name, age, gender);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean getPatientById(int id) {
		String query = "SELECT * FROM PATIENTS WHERE id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
