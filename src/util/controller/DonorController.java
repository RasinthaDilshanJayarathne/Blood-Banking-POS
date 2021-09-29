package util.controller;


import db.DbConnection;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import model.DonateDetail;
import model.Donor;
import model.StoreDetail;
import view.tm.StoreDetailTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DonorController {

    //-------------------- get User Ids --------------------------------------
    /*public static List<String> getUserID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().
                getConnection().prepareStatement("SELECT * FROM Users").executeQuery();
        List<String> userId = new ArrayList<>();
        while (rst.next()){
            userId.add(
                    rst.getString(1)
            );
        }
        return userId;
    }*/

    //-------------------- get Blood Ids --------------------------------------
    public String getBloodIds(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT blId FROM blood WHERE bloodGroup=?");
        stm.setObject(1,id);

        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            return rst.getString("blId");
        }
        return null;
    }
    //-------------------- get Blood Types --------------------------------------
   /* public static List<String> getBloodType() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().
                getConnection().prepareStatement("SELECT bloodGroup FROM blood").executeQuery();
        List<String> bloodType = new ArrayList<>();
        while (rst.next()){
            bloodType.add(
                    rst.getString(1)
            );
        }
        return bloodType;
    }*/

    /*public static ObservableList<StoreDetailTM> getAllDetail(ObservableList<StoreDetailTM> objects) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT r.blId, r.bloodGroup, r.rId, r.name, r.totalQty, dn.QtyOnHand FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId");
        ResultSet rst = stm.executeQuery();
        while (rst.next()) {
            objects.add(new StoreDetailTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getInt(6)
            ));
        }
        return objects;
    }*/

    public ArrayList<StoreDetailTM> getAllDetail() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT r.blId, r.bloodGroup, r.rId, r.name, r.totalQty, SUM(dn.QtyOnHand) FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId GROUP BY r.rId");
        ResultSet rst = stm.executeQuery();
        ArrayList<StoreDetailTM> storeDetails = new ArrayList<>();
        while (rst.next()) {
            storeDetails.add(new StoreDetailTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getInt(6)
            ));
        }
        return storeDetails;
    }

    public boolean saveDonor(Donor d, DonateDetail donateDetail){
        Connection con=null;
        try {
            con= DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            String query="INSERT INTO Donor VALUES(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setObject(1,d.getNic());
            stm.setObject(2,d.getUserID());
            stm.setObject(3,d.getName());
            stm.setObject(4,d.getAddress());
            stm.setObject(5,d.getCity());
            stm.setObject(6,d.getType());
            stm.setObject(7,d.getBlID());
            stm.setObject(8,d.getGender());
            stm.setObject(9,d.getPhoneNo());
            stm.setObject(10,d.getEmail());

            if (stm.executeUpdate()>0){
                if (saveDonateDetail(donateDetail)){
                    con.commit();
                    return true;
                }else {
                    con.rollback();
                    return false;
                }
            }else {
                con.rollback();
                return false;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateDonor(Donor d) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Donor SET uId=?,fullName=?,address=?,city=?,bloodGroup=?,blID=?,gender=?,contact=?,email=? WHERE nic=?");
        stm.setObject(1,d.getUserID());
        stm.setObject(2,d.getName());
        stm.setObject(3,d.getAddress());
        stm.setObject(4,d.getCity());
        stm.setObject(5,d.getType());
        stm.setObject(6,d.getBlID());
        stm.setObject(7,d.getGender());
        stm.setObject(8,d.getPhoneNo());
        stm.setObject(9,d.getEmail());
        stm.setObject(10,d.getNic());
        return stm.executeUpdate()>0;
    }

    public boolean deleteDonor(String id) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Donor WHERE nic='"+id+"'").executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    public Donor getDonor(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Donor WHERE nic=?");
        stm.setObject(1, id);

        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            return new Donor(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8),
                    rst.getString(9),
                    rst.getString(10)
            );

        } else {
            return null;
        }
    }

    public ArrayList<Donor> getAllDonor() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Donor");
        ResultSet rst = stm.executeQuery();
        ArrayList<Donor> donor = new ArrayList<>();
        while (rst.next()) {
            donor.add(new Donor(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8),
                    rst.getString(9),
                    rst.getString(10)
            ));
        }
        return donor;
    }

    public boolean saveDonateDetail(DonateDetail d) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO `Donate Detail` VALUES(?,?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, d.getBlID());
        stm.setObject(2, d.getrID());
        stm.setObject(3, d.getNic());
        stm.setObject(4, d.getDate());
        stm.setObject(5, d.getTime());
        stm.setObject(6, d.getQtyOnHand());
        stm.setObject(7, d.getTotalQty());

        if (stm.executeUpdate() > 0) {

            if (updateQty(d.getrID(),d.getQtyOnHand(),d.getNic(),d.getDate())){
            }else{
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean updateQty(String rackId, Integer quantity,String nic,String date) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = con.prepareStatement("UPDATE  `Donate Detail` SET totalQty=totalQty-? WHERE rId=? AND nic=? AND date=?");
        preparedStatement.setObject(1,quantity);
        preparedStatement.setObject(2,rackId);
        preparedStatement.setObject(3,nic);
        preparedStatement.setObject(4,date);
        return preparedStatement.executeUpdate()>0;
    }

    //-------------------- get Donor Count --------------------------------------
    public int donorCount() throws SQLException, ClassNotFoundException {
        int numberRow = 0;
        PreparedStatement statement = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT COUNT(*) FROM donor");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            numberRow = resultSet.getInt("count(*)");
        }
        return numberRow;
    }

    public static List<Donor> searchDonor (String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Donor WHERE fullName LIKE '%"+value+"%' or city LIKE '%"+value+"%'");
        ResultSet rst = pstm.executeQuery();

        List<Donor> donors=new ArrayList<>();
        while (rst.next()) {
            donors.add(new Donor(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8),
                    rst.getString(9),
                    rst.getString(10)
            ));
        }
        return donors;
    }

    public List<String> getDonorNic() throws SQLException, ClassNotFoundException {
        Connection con=DbConnection.getInstance().getConnection();
        PreparedStatement pstm=con.prepareStatement("SELECT nic FROM Donor");
        ResultSet rst=pstm.executeQuery();

        List<String>stringList=new ArrayList<>();
        while (rst.next()){
            stringList.add(rst.getString(1));
        }
        return stringList;
    }
}
