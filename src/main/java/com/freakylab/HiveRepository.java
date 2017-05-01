package com.freakylab;

import java.sql.*;

/**
 * Created by js on 2017. 4. 29..
 */
public class HiveRepository {
	private static final HiveRepository instance = new HiveRepository();
	private static final String driverName = "org.apache.hive.jdbc.HiveDriver";
	private String connectionUri = "jdbc:hive2://localhost:10000/default";
	private String userName = "hive";
	private String password = "hive";
	private Connection conn =  null;
	private Statement stmt = null;

	private HiveRepository() {
		System.out.println("Making Singleton Instance...");
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(connectionUri,userName,password);
			stmt = conn.createStatement();
			stmt.executeUpdate("CREATE EXTERNAL TABLE IF NOT EXISTS feed "
					+ "( pressType String, contents String, pubDate String, linkUrl String)"
					+" COMMENT 'Rss list'"
					+" ROW FORMAT DELIMITED"
					+" FIELDS TERMINATED BY '\t'"
					+" LINES TERMINATED BY '\n'"
					+" STORED AS TEXTFILE");
		} catch (Exception e) {
			System.err.println("테이블 생성 및 확인에 오류가 발생");
			e.printStackTrace();
			System.exit(2);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Make Singleton Instance Done.");
	}

	public static HiveRepository getInstance() {
		return instance;
	}

	public void insertData() {
		try {
			conn = DriverManager.getConnection(connectionUri,userName,password);
			stmt = conn.createStatement();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
