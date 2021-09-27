package util.controller;


import db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderController {
    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT oId FROM orders ORDER BY oId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()) {

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                return "O00-000" + tempId;
            } else if (tempId < 99) {
                return "O00-00" + tempId;
            } else if (tempId < 999) {
                return "O00-0" + tempId;
            } else {
                return "O00-" + tempId;
            }

        } else {
            return "O00-0001";
        }
    }

    public String getAvailability(String name) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement(" SELECT SUM(dn.QtyOnHand) FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId where name=?");
        stm.setObject(1,name);

        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            return rst.getString("SUM(dn.QtyOnHand)");
        }
        return null;
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

    public String updateNewAvalibilityQty(String name, String qty, String type) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement(" SELECT r.rId FROM Rack r WHERE r.name =?");
        stm.setObject(1,name);
        ResultSet rst=stm.executeQuery();

        String rackID=null;
        if (rst.next()){
            System.out.println("1");
            rackID=rst.getString(1);
        }
        stm = con.prepareStatement(" SELECT b.blId FROM Blood b WHERE b.bloodGroup =?");
        stm.setObject(1,type);
        ResultSet rst1=stm.executeQuery();

        String bloodID=null;
        if (rst1.next()){
            System.out.println("2");
            bloodID=rst1.getString(1);
        }

        stm=con.prepareStatement("UPDATE Rack r LEFT JOIN `donate detail` dn ON dn.rId = r.rId SET dn.QtyOnHand =(dn.QtyOnHand-?) WHERE dn.blId =? AND dn.rId =?");
        stm.setObject(1,qty);
        stm.setObject(2,bloodID);
        stm.setObject(3,rackID);

        if (stm.executeUpdate()>0) {
            System.out.println("3");
            stm=con.prepareStatement("SELECT QtyOnHand FROM `donate detail` dn WHERE dn.blId =? AND dn.rId =?");
            stm.setObject(1,bloodID);
            stm.setObject(2,rackID);

            ResultSet rst2=stm.executeQuery();
            if (rst2.next()){
                System.out.println("4");
                return rst2.getString(1);
            }
        }
        return null;
    }

   /* public ArrayList<Order> getAllOrders() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders");
        ResultSet rst = stm.executeQuery();
        ArrayList<Order> orders = new ArrayList<>();
        while (rst.next()) {
            orders.add(new Order(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getInt(6),
                    rst.getString(7),
                    rst.getString(8)
            ));
        }
        return orders;
    }*/
}
