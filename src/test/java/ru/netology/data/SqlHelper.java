package ru.netology.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;

public class SqlHelper {

    public static void set() throws SQLException {
        val runner = new QueryRunner();
        val delDataPaymentEntity = "DELETE FROM payment_entity";
        val delDataCreditEntity = "DELETE FROM credit_request_entity";
        try(
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ){
            runner.update(conn, delDataPaymentEntity);
            runner.update(conn, delDataCreditEntity);
        }
    }

    private static String getStatus(String statement) {
        String result = "";
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            val count = runner.query(conn, statement, new ScalarHandler<>());
            result = count.toString();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public static String getStatusPayApproved() {
        String statement = "SELECT status FROM payment_entity";
        return getStatus(statement);
    }


    public static String getStatusCreditApproved() {
        String statement = "SELECT status FROM credit_request_entity";
        return getStatus(statement);
    }

    public static String getStatusPayDeclined() {
        String statement = "SELECT status FROM payment_entity";
        return getStatus(statement);
    }

    public static String getStatusCreditPayDeclined() {
        String statement = "SELECT status FROM credit_request_entity";
        return getStatus(statement);
    }

}
