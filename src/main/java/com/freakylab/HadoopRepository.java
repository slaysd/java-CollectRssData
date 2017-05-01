package com.freakylab;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapreduce.Job;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by js on 2017. 4. 30..
 */
public class HadoopRepository {
	private static final HadoopRepository instance = new HadoopRepository();
	private static final String WORKING_DIR = "/user/hive/warehouse";
	Configuration conf = null;
	Job job = null;

	private HadoopRepository() {
		conf = new Configuration();
		conf.addResource(new Path("/home/jinh574/hadoop2/etc/hadoop/core-site.xml"));
		conf.addResource(new Path("/home/jinh574/hadoop2/etc/hadoop/hdfs-site.xml"));
		conf.addResource(new Path("/home/jinh574/hadoop2/etc/hadoop/mapred-site.xml"));
	}

	public static HadoopRepository getInstance() {
		return instance;
	}

	public Path[] printDir(String path) {
		FileSystem fs = null;
		Path[] paths = null;
		try {
			fs = FileSystem.get(conf);
			FileStatus[] fileStatuses = fs.listStatus(new Path(path));
			paths = FileUtil.stat2Paths(fileStatuses);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paths;
	}

	public void writeFile(ArrayList<RSS> feeds) {
		Date date = new Date();
		Path newFilePath = new Path(WORKING_DIR + "/" + date.getTime() + ".log");
		FileSystem fs = null;

		StringBuilder sb = new StringBuilder();
		for(RSS feed : feeds) {
			//System.out.println(feed.getPubDate().getTime() + " / " + lastUpdateTime().getTime() + " / " + (feed.getPubDate().getTime() > lastUpdateTime().getTime()));
			if(feed.getPubDate().getTime() > lastUpdateTime().getTime()) {
				sb.append(feed.toString());
			}
		}
		byte[] byt = sb.toString().getBytes();


		try {
			fs = FileSystem.get(conf);
			fs.createNewFile(newFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			FSDataOutputStream fsOutStream = fs.create(newFilePath);
			fsOutStream.write(byt);
			fsOutStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Written data to HDFS file.");
	}

	public Date lastUpdateTime() {
		System.out.println("Reading from HDFS file.");
		Path[] paths = printDir(WORKING_DIR);
		Long timestamp = 0L;
		for(Path path : paths) {
			String s = path.getName();
			String[] splitStr = s.split("\\.");
			if(splitStr.length != 2) {
				continue;
			} else if (!isNum(splitStr[0])) {
				continue;
			} else {
				Long tmp = Long.parseLong(splitStr[0]);
				if (timestamp < tmp) {
					timestamp = tmp;
				}
			}
		}
		System.out.println("Last Update Time : " + new Date(timestamp));
		return new Date(timestamp);
	}

	public boolean isNum(String value) {
		for (int i = 0; i < value.length(); i++) {
			if(!Character.isDigit(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
