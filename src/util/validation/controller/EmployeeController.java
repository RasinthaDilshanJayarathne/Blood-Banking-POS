package util.validation.controller;

import db.DbConnection;
import javafx.scene.chart.XYChart;
import model.Donor;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeController {

    public static List<Employee> searchEmployee(String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Employee WHERE name LIKE '%"+value+"%' or city LIKE '%"+value+"%'");
        ResultSet rst = pstm.executeQuery();

        List<Employee> employees=new ArrayList<>();
        while (rst.next()) {
            employees.add(new Employee(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            ));
        }
        return employees;
    }

    //-------------------- get User Ids --------------------------------------
    public List<String> getUserId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().
                getConnection().prepareStatement("SELECT * FROM Users").executeQuery();
        List<String> userId = new ArrayList<>();
        while (rst.next()) {
            userId.add(
                    rst.getString(1)
            );
        }
        return userId;
    }

    public boolean saveEmployee(Employee b) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Employee VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, b.getEmpId());
        stm.setObject(2, b.getUserID());
        stm.setObject(3, b.getEmpName());
        stm.setObject(4, b.getEmpAddress());
        stm.setObject(5, b.getEmpCity());
        stm.setObject(6, b.getEmpType());
        stm.setObject(7, b.getEmpGender());
        stm.setObject(8, b.getEmpPhoneNo());
        return stm.executeUpdate() > 0;
    }

    public boolean updateEmployee(Employee e) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Employee SET userId=?,name=?,address=?,city=?,employeeType=?,gender=?,contact=? WHERE eId=?");
        stm.setObject(1, e.getUserID());
        stm.setObject(2, e.getEmpName());
        stm.setObject(3, e.getEmpAddress());
        stm.setObject(4, e.getEmpCity());
        stm.setObject(5, e.getEmpType());
        stm.setObject(6, e.getEmpGender());
        stm.setObject(7, e.getEmpPhoneNo());
        stm.setObject(8, e.getEmpId());
        return stm.executeUpdate() > 0;
    }

    public boolean deleteEmployee(String id) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Employee WHERE eId='" + id + "'").executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Employee getEmployee(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Employee WHERE eId=?");
        stm.setObject(1, id);

        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            return new Employee(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );

        } else {
            return null;
        }
    }

    public ArrayList<Employee> getAllEmployee() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Employee");
        ResultSet rst = stm.executeQuery();
        ArrayList<Employee> employee = new ArrayList<>();
        while (rst.next()) {
            employee.add(new Employee(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            ));
        }
        return employee;
    }

    //-------------------- get Employee Count --------------------------------------
    public int employeeCount() throws SQLException, ClassNotFoundException {
        int numberRow = 0;
        PreparedStatement statement = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT COUNT(*) FROM employee");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            numberRow = resultSet.getInt("count(*)");
        }
        return numberRow;
    }
}
