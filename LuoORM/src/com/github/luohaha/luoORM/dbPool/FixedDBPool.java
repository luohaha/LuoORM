package com.github.luohaha.luoORM.dbPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.github.luohaha.luoORM.tools.ConfReader;

public class FixedDBPool implements DBPool {

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

	public FixedDBPool() throws IOException, ClassNotFoundException {
		this("./conf/db.conf");
	}

	public FixedDBPool(String file) throws IOException, ClassNotFoundException {

		Map<String, String> conf = ConfReader.create(file).getResult();
		user = conf.get("user");
		password = conf.get("password");
		url = conf.get("url");
		driver = conf.get("driver");
		Class.forName(driver);
		initSource();
		scheduledCheck();
	}

	public FixedDBPool(String driver, String url, String user, String password)
			throws IOException, ClassNotFoundException {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		Class.forName(this.driver);
		initSource();
		scheduledCheck();
	}

	@Override
	public synchronized Connection getConnection() throws SQLException {
		while (connections.size() <= 0) {
			// return
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return connections.poll();

	}

	@Override
	public synchronized void closeConnection(Connection con) throws SQLException {
		connections.add(con);
		notify();
	}

	/**
	 * 设置连接池的大小
	 * 
	 * @param size
	 * 线程池的大小
	 * @return
	 * 返回线程池
	 */
	public FixedDBPool setConnectionPoolSize(int size) {
		this.connectionPoolSize = size;
		return this;
	}

	@Override
	public void initSource() {
		for (int i = 0; i < this.connectionPoolSize; i++) {
			try {
				this.connections.add(DriverManager.getConnection(url, user, password));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void closePool() throws SQLException {
		this.service.shutdownNow();
		while (!this.connections.isEmpty()) {
			this.connections.poll().close();
		}
	}
	
	public FixedDBPool setCheckTimeout(int checkTimeout) {
		this.checkTimeout = checkTimeout;
		return this;
	}

	public FixedDBPool setCheckScheduleTime(int checkScheduleTime) {
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
						this.connections.add(DriverManager.getConnection(url, user, password));
					}
				} catch (Exception e) {
					Logger.getLogger("logger").warning(e.toString());
				}
			}
		}, this.checkScheduleTime, this.checkScheduleTime, TimeUnit.SECONDS);
	}
}
