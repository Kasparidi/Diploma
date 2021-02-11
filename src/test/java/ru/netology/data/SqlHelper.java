package ru.netology.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;

public class SqlHelper {

    private static String getUrl(){
        return System.getProperty("db.url");
    }

    private static String getUser() {
        return System.getProperty("db.user");
    }

    private static String getPassword(){
        return System.getProperty("db.password");
    }

    public static String getStatusPay() throws SQLException {
        String statement = "SELECT status FROM payment_entity";
        return getStatus(statement);
    }

    public static String getStatusCredit() throws SQLException {
        String statement = "SELECT status FROM credit_request_entity";
        return getStatus(statement);
    }

    public static void clean() {
        val runner = new QueryRunner();
        val delDataPaymentEntity = "DELETE FROM payment_entity";
        val delDataCreditEntity = "DELETE FROM credit_request_entity";
        try(
                val conn = DriverManager.getConnection(getUrl(), getUser(), getPassword())
        ){
            runner.update(conn, delDataPaymentEntity);
            runner.update(conn, delDataCreditEntity);
        } catch (SQLException e) {
            e.getErrorCode();
        }
    }

    private static String getStatus(String query) throws SQLException {
        String data = "";
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(
                        getUrl(), getUser(), getPassword()
                );
        ) {
            data = runner.query(conn, query, new ScalarHandler<>());
        }
        return data;
    }
}
