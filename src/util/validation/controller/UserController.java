package util.validation.controller;

import db.DbConnection;
import model.Blood;
import model.StoreDetail;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    public static List<User> searchUser(String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Users WHERE userName LIKE '%"+value+"%' or possision LIKE '%"+value+"%'");
        ResultSet rst = pstm.executeQuery();

        List<User> users=new ArrayList<>();
        while (rst.next()) {
            users.add(new User(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            ));
        }
        return users;
    }

    public boolean saveUser(User u) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        String query="INSERT INTO Users VALUES(?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,u.getId());
        stm.setObject(2,u.getName());
        stm.setObject(3,u.getPassword());
        stm.setObject(4,u.getType());
        return stm.executeUpdate()>0;
    }

    public boolean updateUser(User u) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Users SET userName=?, password=?,possision=? WHERE uId=?");
        stm.setObject(1,u.getName());
        stm.setObject(2,u.getPassword());
        stm.setObject(3,u.getType());
        stm.setObject(4,u.getId());
        return stm.executeUpdate()>0;
    }

    public boolean deleteUser(String id) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Users WHERE uId='"+id+"'").executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    public User getUser(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM Users WHERE uId=?");
        stm.setObject(1, id);

        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            return new User(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            );

        } else {
            return null;
        }
    }

    public ArrayList<User> getAllUser() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Users");
        ResultSet rst = stm.executeQuery();
        ArrayList<User> user = new ArrayList<>();
        while (rst.next()) {
            user.add(new User(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            ));
        }
        return user;
    }
}
