package util.controller;

import db.DbConnection;
import javafx.scene.chart.XYChart;
import model.DonateDetail;
import model.Donor;
import model.Employee;
import model.StoreDetail;
import view.tm.StoreDetailTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DonationController {
    public static List<StoreDetail> searchStore(String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT r.blId,r.bloodGroup, r.rId, r.name, r.totalQty,dn.totalQty FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId WHERE bloodGroup LIKE'%"+value+"%' or name LIKE '%"+value+"%' GROUP BY r.rId");
        ResultSet rst = pstm.executeQuery();

        List<StoreDetail> storeDetails=new ArrayList<>();
        while (rst.next()) {
            storeDetails.add(new StoreDetail(
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

    public XYChart.Series<String, Integer> setUpBarChartFromDatabase() throws SQLException, ClassNotFoundException {
        XYChart.Series<String,Integer> series=new XYChart.Series<>();
        PreparedStatement statement = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT rId,QtyOnHand FROM `Donate detail`");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()){
            series.getData().add(new XYChart.Data<>(resultSet.getString(1),resultSet.getInt(2)));
        }
        return series;
    }

    public String getAvailability(String rackId) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement(" select totalQty from `donate detail` where rId=? ORDER BY totalQty ASC LIMIT 1;");
        stm.setObject(1,rackId);

        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            return rst.getString("totalQty");
        }
        return null;
    }

    public String getAvailability2(String rackId) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement(" select totalQty from Rack where rId=?");
        stm.setObject(1,rackId);

        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            return rst.getString("totalQty");
        }
        return null;
    }

   /* public ArrayList<StoreDetailTM> getAllDonateDetail() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT r.rId,r.blId,dn.QtyOnHand,dn.date,dn.time FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId");
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
    }*/



    //-------------------- get Donation Count --------------------------------------
    public int donationCount() throws SQLException, ClassNotFoundException {
        int numberRow = 0;
        PreparedStatement statement = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT COUNT(*) FROM `Donate Detail`");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            numberRow = resultSet.getInt("count(*)");
        }
        return numberRow;
    }
}
