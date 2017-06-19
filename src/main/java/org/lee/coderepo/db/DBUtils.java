package org.lee.coderepo.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2017/6/14.
 */
public class DBUtils {
	
	public static <T> List<T> select(Database db, String sql, String[] paramArray, Class<T> clazz) throws
	                                                                                               ClassNotFoundException,
	                                                                                               SQLException {
		String url = buildUrl(db);
		Class.forName(JdbcDriverMap.get(db.getType()));
		Connection conn = DriverManager.getConnection(url, db.getUsername(), db.getPassword());
		return new QueryRunner().query(conn, sql, paramArray, new BeanListHandler<T>(clazz));
	}
	
	public static int update(Database db, String sql, String[] paramArray) throws ClassNotFoundException, SQLException {
		
		String url = buildUrl(db);
		Class.forName(JdbcDriverMap.get(db.getType()));
		Connection conn = DriverManager.getConnection(url, db.getUsername(), db.getPassword());
		return new QueryRunner().update(conn, sql, paramArray);
	}
	
	private static String buildUrl(Database database) {
		
		StringBuilder urlBuilder = new StringBuilder();
		if (DB_TYPE_MYSQL.equals(database.getType())) {
			urlBuilder.append("jdbc:mysql://" + database.getIp() + ":" + database.getPort() + "/" + database.getSid());
			urlBuilder.append("?useUnicode=true&amp;characterEncoding=UTF-8&amp;allowMultiQueries=true&amp;useSSL=false");
		} else if (DB_TYPE_ORACLE.equals(database.getType())) {
			urlBuilder.append("jdbc:oracle:thin:@" + database.getIp() + ":" + database.getPort() + ":" + database.getSid());
		} else if (DB_TYPE_SQLITE.equals(database.getType())) {
			urlBuilder.append("jdbc:sqlite:" + database.getSid());
		}
		return urlBuilder.toString();
	}
	
	
	public static final String DB_TYPE_MYSQL = "mysql";
	public static final String DB_TYPE_ORACLE = "oracle";
	public static final String DB_TYPE_SQLITE = "sqlite";
	private static final Map<String, String> JdbcDriverMap = new HashMap<String, String>();
	
	static {
		JdbcDriverMap.put(DB_TYPE_MYSQL, "com.mysql.jdbc.Driver");
		JdbcDriverMap.put(DB_TYPE_ORACLE, "oracle.jdbc.driver.OracleDriver");
		JdbcDriverMap.put(DB_TYPE_SQLITE, "org.sqlite.JDBC");
	}
	
	
}
