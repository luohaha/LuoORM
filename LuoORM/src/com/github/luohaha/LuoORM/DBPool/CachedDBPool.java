package com.github.luohaha.LuoORM.DBPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.luohaha.LuoORM.ConfReader;

public class CachedDBPool implements DBPool {

	private String user;
	private String password;
	private String url;
	private String driver;
	
	private ScheduledExecutorService service;
	/*
	 * 连接池
	 */
	private BlockingQueue<Connection> connections = new LinkedBlockingQueue<>();
	private int connectionPoolSize = 10;
	private int checkTimeout = 5; //second
	private int checkScheduleTime = 60 * 5; //second

	public CachedDBPool() throws IOException, ClassNotFoundException {
		this("./conf/db.conf");
	}

	public CachedDBPool(String file) throws IOException, ClassNotFoundException {

		Map<String, String> conf = ConfReader.create(file).getResult();
		user = conf.get("user");
		password = conf.get("password");
		url = conf.get("url");
		driver = conf.get("driver");
		Class.forName(driver);
		scheduledCheck();
	}

	public CachedDBPool(String driver, String url, String user, String password)
			throws IOException, ClassNotFoundException {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		Class.forName(this.driver);
		scheduledCheck();
	}

	@Override
	public synchronized Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		if (connections.size() <= 0) {
			return DriverManager.getConnection(url, user, password);
		} else {
			return connections.poll();
		}
	}

	@Override
	public synchronized void closeConnection(Connection con) throws SQLException {
		// TODO Auto-generated method stub

		if (connections.size() >= connectionPoolSize) {
			con.close();
		} else {
			connections.add(con);
		}
	}

	/**
	 * 设置连接池的大小
	 * 
	 * @param size
	 * 线程池的大小
	 * @return
	 * 返回新建立的线程池
	 */
	public CachedDBPool setConnectionPoolSize(int size) {
		this.connectionPoolSize = size;
		return this;
	}

	@Override
	public void initSource() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closePool() throws SQLException {
		// TODO Auto-generated method stub
		this.service.shutdownNow();
		while (!this.connections.isEmpty()) {
			this.connections.poll().close();
		}
	}
	
	public CachedDBPool setCheckTimeout(int checkTimeout) {
		this.checkTimeout = checkTimeout;
		return this;
	}

	public CachedDBPool setCheckScheduleTime(int checkScheduleTime) {
		this.checkScheduleTime = checkScheduleTime;
		return this;
	}

	private void scheduledCheck() {
		this.service = Executors.newScheduledThreadPool(1);
		this.service.scheduleAtFixedRate(() -> {
			for (Connection each : this.connections) {
				try {
					if (!each.isValid(this.checkTimeout)) {
						//false
						this.connections.remove(each);
						each.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, this.checkScheduleTime, this.checkScheduleTime, TimeUnit.SECONDS);
	}
}
