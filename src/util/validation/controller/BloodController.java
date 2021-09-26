package util.validation.controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import model.Blood;
import model.Donor;
import view.tm.BloodTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BloodController {

    public boolean saveBlood(Blood b) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        String query="INSERT INTO Blood VALUES(?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,b.getBlId());
        stm.setObject(2,b.getBlType());
        stm.setObject(3,b.getBlDescription());
        return stm.executeUpdate()>0;
    }

    public boolean updateBlood(Blood b) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Blood SET bloodGroup=?,  description=? WHERE blId=?");
        stm.setObject(1,b.getBlType());
        stm.setObject(2,b.getBlDescription());
        stm.setObject(3,b.getBlId());
        return stm.executeUpdate()>0;
    }

    public boolean deleteBlood(String id) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Blood WHERE blId='"+id+"'").executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    public Blood getBlood(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Blood WHERE blId=?");
        stm.setObject(1, id);

        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            return new Blood(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3)
            );

        } else {
            return null;
        }
    }

    public static List<Blood>searchBlood(String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Blood WHERE blId LIKE '%"+value+"%' or bloodGroup LIKE '%"+value+"%'");
        ResultSet rst = pstm.executeQuery();

        List<Blood> blood=new ArrayList<>();
        while (rst.next()) {
            blood.add(new Blood(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3)
            ));
        }
        return blood;
    }

    public ArrayList<Blood> getAllBlood() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Blood");
        ResultSet rst = stm.executeQuery();
        ArrayList<Blood> blood = new ArrayList<>();
        while (rst.next()) {
            blood.add(new Blood(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3)
            ));
        }
        return blood;
    }
    public int bloodCount() throws SQLException, ClassNotFoundException {
        int numberRow = 0;
        PreparedStatement statement = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT COUNT(*) FROM blood");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            numberRow = resultSet.getInt("count(*)");
        }
        return numberRow;
    }
}
