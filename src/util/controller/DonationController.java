package util.controller;

import db.DbConnection;
import javafx.scene.chart.XYChart;
import model.DonateDetail;
import model.StoreDetail;

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

    public static ArrayList<DonateDetail> setUpDailyBarChart() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(" SELECT date,sum(totalQty),sum(QtyOnHand),rId as QtyOnHand from `Donate Detail` group by date,rId order by QtyOnHand desc");
        ResultSet rst = stm.executeQuery();
        ArrayList<DonateDetail> donateDetails = new ArrayList<>();
        while (rst.next()) {
            donateDetails.add(new DonateDetail(rst.getString(1),rst.getInt(2),rst.getInt(3)));
        }
        return donateDetails;
    }


    public static ArrayList<DonateDetail> setUpDailyBarChartMonthly() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("  SELECT * FROM `Donate Detail` WHERE date BETWEEN ? AND ?");
        ResultSet rst = stm.executeQuery();
        ArrayList<DonateDetail> donateDetails = new ArrayList<>();
        while (rst.next()) {
            donateDetails.add(new DonateDetail(rst.getString(1),rst.getInt(2),rst.getInt(3)));
        }
        return donateDetails;
    }

    public XYChart.Series<String, Integer> setUpBarChartFromDatabase() throws SQLException, ClassNotFoundException {
        XYChart.Series<String,Integer> series=new XYChart.Series<>();
        PreparedStatement statement = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT name,totalQty FROM rack");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()){
            series.getData().add(new XYChart.Data<>(resultSet.getString(1),resultSet.getInt(2)));
        }
        return series;
    }

    public boolean checkAvailabilityOrNot(String rackId) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Donate Detail` WHERE rId=?");
        stm.setObject(1,rackId);
        ResultSet rst = stm.executeQuery();

        if (rst.next()){
            return true;
        }
        return false;
    }

    //-------------------- get Donation Count --------------------------------------
    public int donationCount() throws SQLException, ClassNotFoundException {
        int numberRow = 0;
        PreparedStatement statement = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT COUNT(*) FROM `Order Detail`");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            numberRow = resultSet.getInt("count(*)");
        }
        return numberRow;
    }
}
