package util.validation.controller;


import db.DbConnection;
import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
