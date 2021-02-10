package ru.netology.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;

public class SqlHelper {
    private static final String url = "jdbc:postgresql://localhost:5432/app";
    private static final String user = "admin";
    private static final String password = "password";

    public static void clean() throws SQLException {
        val runner = new QueryRunner();
        val delDataPaymentEntity = "DELETE FROM payment_entity";
        val delDataCreditEntity = "DELETE FROM credit_request_entity";
        try(
                val conn = DriverManager.getConnection(url, user, password)
        ){
            runner.update(conn, delDataPaymentEntity);
            runner.update(conn, delDataCreditEntity);
        }
    }

    private static String getStatus(String statement) {
        String result = "";
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(url, user, password)
        ) {
            val count = runner.query(conn, statement, new ScalarHandler<>());
            result = count.toString();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public static String getStatusPay() {
        String statement = "SELECT status FROM payment_entity";
        return getStatus(statement);
    }

    public static String getStatusCredit() {
        String statement = "SELECT status FROM credit_request_entity";
        return getStatus(statement);
    }
}
