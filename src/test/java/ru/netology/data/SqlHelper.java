package ru.netology.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;

public class SqlHelper {

    private static String url(){
        return System.getProperty("db.url");
    }

    private static String user() {
        return System.getProperty("db.user");
    }

    private static String password(){
        return System.getProperty("db.password");
    }

    public static void clean() {
        val runner = new QueryRunner();
        val delDataPaymentEntity = "DELETE FROM payment_entity";
        val delDataCreditEntity = "DELETE FROM credit_request_entity";
        try(
                val conn = DriverManager.getConnection(url(), user(), password())
        ){
            runner.update(conn, delDataPaymentEntity);
            runner.update(conn, delDataCreditEntity);
        } catch (SQLException e) {
            e.getErrorCode();
        }
    }

    private static String getStatus(String statement) {
        String result = "";
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(url(), user(), password())
        ) {
            val count = runner.query(conn, statement, new ScalarHandler<>());
            result = count.toString();
        } catch (SQLException throwable) {
            throwable.getErrorCode();
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
