package com.freakylab;

import com.rometools.rome.io.FeedException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args ) {
        CollectRSS rss = new CollectRSS();
        HiveRepository hiveRepository = HiveRepository.getInstance();
        new Thread(() -> {
        	while(true) {
				try {
					rss.getUpdateRss();
					//hiveRepository.insertData();
					Thread.sleep(300000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
        }).start();
    }
}
