package util.controller;

import db.DbConnection;
import javafx.scene.chart.XYChart;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BloodRackController {

    //-------------------- get Blood Types --------------------------------------
    public static List<String> getBloodType() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().
                getConnection().prepareStatement("SELECT * FROM Blood").executeQuery();
        List<String> bloodGroup = new ArrayList<>();
        while (rst.next()){
            bloodGroup.add(
                    rst.getString(2)
            );
        }
        return bloodGroup;
    }

    //-------------------- get Blood Ids --------------------------------------
   /* public String getRackIds(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT rId,QtyOnHand FROM rack WHERE name=?");
        stm.setObject(1,id);

        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            return rst.getString("rId");
        }
        return null;
    }*/

    public BloodRack getRackIds(String name) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Rack WHERE name=?");
        stm.setObject(1, name);

        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            return new BloodRack(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getInt(6)
            );

        } else {
            return null;
        }
    }

    public static List<String> getBloodRackName() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().
                getConnection().prepareStatement("SELECT * FROM Rack").executeQuery();
        List<String> rackName = new ArrayList<>();
        while (rst.next()){
            rackName.add(
                    rst.getString(3)
            );
        }
        return rackName;
    }

    public boolean saveBloodRack(BloodRack r) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Rack VALUES(?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, r.getId());
        stm.setObject(2, r.getBlId());
        stm.setObject(3, r.getName());
        stm.setObject(4, r.getQty());
        stm.setObject(5, r.getBloodType());
        stm.setObject(6, r.getStoreQty());

        return stm.executeUpdate() > 0;
    }

    public boolean updateRack(BloodRack r) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Rack SET blId=?,name=?,totalQty=?,bloodGroup=?,storeQty=? WHERE rId=?");
        stm.setObject(1, r.getBlId());
        stm.setObject(2, r.getName());
        stm.setObject(3, r.getQty());
        stm.setObject(4, r.getBloodType());
        stm.setObject(5, r.getStoreQty());
        stm.setObject(6, r.getId());

        return stm.executeUpdate()>0;
    }

    public boolean deleteRack(String id) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Rack WHERE rId='"+id+"'").executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    public BloodRack getRack(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Rack WHERE rId=?");
        stm.setObject(1, id);

        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            return new BloodRack(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getInt(6)
            );

        } else {
            return null;
        }
    }

    public ArrayList<BloodRack> getAllRack() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Rack");
        ResultSet rst = stm.executeQuery();
        ArrayList<BloodRack> racks = new ArrayList<>();
        while (rst.next()) {
            racks.add(new BloodRack(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getInt(6)
            ));
        }
        return racks;
    }

    public static List<BloodRack> searchBloodRack(String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Rack WHERE name LIKE '%"+value+"%' or bloodGroup LIKE '%"+value+"%'");
        ResultSet rst = pstm.executeQuery();

        List<BloodRack> racks=new ArrayList<>();
        while (rst.next()) {
            racks.add(new BloodRack(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getInt(6)
            ));
        }
        return racks;
    }


    /*public XYChart.Series<String, Integer> setUpBarChartFromDatabase() throws SQLException, ClassNotFoundException {
        XYChart.Series<String,Integer> series=new XYChart.Series<>();
        PreparedStatement statement = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT name,totalQty FROM rack");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()){
            series.getData().add(new XYChart.Data<>(resultSet.getString(1),resultSet.getInt(2)));
        }
        return series;
    }*/

    public static ArrayList<BloodRack> setUpDailyBarChart() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT rId,name,sum(totalQty),sum(storeQty) as totalQty from Rack group by rId order by totalQty desc");
        ResultSet rst = stm.executeQuery();
        ArrayList<BloodRack> racks = new ArrayList<>();
        while (rst.next()) {
            racks.add(new BloodRack(rst.getString(1),rst.getString(2),rst.getInt(3),rst.getInt(4)));
        }
        return racks;
    }

    /*public static ArrayList<BloodRack> setUpDashBoardBarChart() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT name,sum(totalQty) as totalQty from Rack group by name order by totalQty desc");
        ResultSet rst = stm.executeQuery();
        ArrayList<BloodRack> racks = new ArrayList<>();
        while (rst.next()) {
            racks.add(new BloodRack(rst.getString(1),rst.getInt(2)));
        }
        return racks;
    }*/

    public List<String> loadRackNameByBloodType(String bloodType, List<String> name) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT name FROM Rack WHERE bloodGroup=?");
        pstm.setObject(1,bloodType);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()){
            name.add(rst.getString(1));

        }
        return name;
    }

    public String getCapacity(String rackId) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT totalQty FROM Rack WHERE rId=?");
        pstm.setObject(1,rackId);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public boolean updateRackStoreQty(String rackId, int qty) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = con.prepareStatement("UPDATE Rack SET storeQty=storeQty-? WHERE rId=?");
        preparedStatement.setObject(1,qty);
        preparedStatement.setObject(2,rackId);
        System.out.println("update rack stor qty method");
        return preparedStatement.executeUpdate()>0;
    }

    public String getUpdatedStoreQty(String newValue) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT storeQty FROM Rack WHERE name=?");
        pstm.setObject(1,newValue);
        ResultSet rst = pstm.executeQuery();

         if (rst.next()){
           return String.valueOf(rst.getInt(1));

        }
        return null;
    }
}
