package util.controller;

import db.DbConnection;
import model.Donor;
import model.Hospital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HospitalController {
    public static List<String> getHospitalIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().
                getConnection().prepareStatement("SELECT * FROM Hospital").executeQuery();
        List<String> hospitalId = new ArrayList<>();
        while (rst.next()){
            hospitalId.add(
                    rst.getString(1)
            );
        }
        return hospitalId;
    }

    public static List<Hospital> searchHospital(String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Hospital WHERE name LIKE '%"+value+"%' or city LIKE '%"+value+"%'");
        ResultSet rst = pstm.executeQuery();

        List<Hospital> hospitals=new ArrayList<>();
        while (rst.next()) {
            hospitals.add(new Hospital(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return hospitals;
    }

    public boolean saveHospital(Hospital h) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        String query="INSERT INTO Hospital VALUES(?,?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,h.getHospitalId());
        stm.setObject(2,h.getName());
        stm.setObject(3,h.getEmail());
        stm.setObject(4,h.getAddress());
        stm.setObject(5,h.getCity());
        stm.setObject(6,h.getPhoneNo());
        stm.setObject(7,h.getWebsite());
        return stm.executeUpdate()>0;
    }

    public boolean updateHospital(Hospital h) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Hospital SET name=?,email=?,address=?,city=?,contact=?,webSite=? WHERE hId=?");
        stm.setObject(1,h.getName());
        stm.setObject(2,h.getEmail());
        stm.setObject(3,h.getAddress());
        stm.setObject(4,h.getCity());
        stm.setObject(5,h.getPhoneNo());
        stm.setObject(6,h.getWebsite());
        stm.setObject(7,h.getHospitalId());
        return stm.executeUpdate()>0;
    }

    public boolean deleteHospital(String id) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Hospital WHERE hId='"+id+"'").executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    public Hospital getHospital(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Hospital WHERE hId=?");
        stm.setObject(1, id);

        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            return new Hospital(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );

        } else {
            return null;
        }
    }

    public  ArrayList<Hospital> getAllHospital() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Hospital");
        ResultSet rst = stm.executeQuery();
        ArrayList<Hospital> hospital = new ArrayList<>();
        while (rst.next()) {
            hospital.add(new Hospital(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return hospital;
    }

    //-------------------- get Hospital Count --------------------------------------
    public int hospitalCount() throws SQLException, ClassNotFoundException {
        int numberRow = 0;
        PreparedStatement statement = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT COUNT(*) FROM hospital");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            numberRow = resultSet.getInt("count(*)");
        }
        return numberRow;
    }
}
