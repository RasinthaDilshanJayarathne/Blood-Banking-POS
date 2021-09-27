package util.controller;

import db.DbConnection;
import model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginController {
    public boolean saveUserLogin(User login) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getInstance().getConnection().
                prepareStatement("INSERT INTO login VALUES (?,?,?,?)");
        statement.setObject(1, login.getId());
        statement.setObject(2, login.getName());
        statement.setObject(3, login.getPassword());
        statement.setObject(4, login.getType());
        return statement.executeUpdate()>0;
    }
}
