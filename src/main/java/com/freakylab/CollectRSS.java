package com.freakylab;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by js on 2017. 4. 29..
 */
public class CollectRSS {
	private static final String LIST_PATH = "rss_list.txt";
	private ArrayList<String> rssList;
	private ArrayList<RSS> feed;
	private Date lastUpdated;

	public ArrayList<String> getRssList() {
		return rssList;
	}

	public CollectRSS() {
		File file = new File("rss_list.txt");
		FileReader fileReader = null;
		feed = new ArrayList<RSS>();

		if (getRssList() == null) {
			rssList = new ArrayList<String>();
		} else {
			rssList.clear();
		}

		try {
			if (file.exists()) {
				fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String rss = null;
				while ((rss = bufferedReader.readLine()) != null) {
					rssList.add(rss);
				}
				System.out.println("Read Rss List Successful.");
			} else {
				System.out.println("파일이 존재 하지 않습니다. : " + LIST_PATH);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getUpdateRss() throws IOException, FeedException {
		lastUpdated = new Date();
		System.out.println("Updating RSS...");
		feed.clear();
		for (String rssUrl : rssList) {
			String data[] = rssUrl.split(",");
			URL feedUrl = new URL(data[2]);
			if(Integer.parseInt(data[0]) > 2) {
				continue;
			}
			SyndFeedInput input = new SyndFeedInput();
			XmlReader xmlReader;
			try {
				xmlReader = new XmlReader(feedUrl);
			} catch (IOException e) {
				continue;
			}
			SyndFeed syndFeed = input.build(xmlReader);

			System.out.print("Starting " + data[1] + "...");
			List<SyndEntry> entries = syndFeed.getEntries();
			for(SyndEntry entry : entries) {
				Date update = null;
				//System.out.println("'" + entry.getPublishedDate() + "' // '" + entry.getPublishedDate() + "'");
				if(entry.getUpdatedDate() == null) {
					update = entry.getPublishedDate();
				} else if (entry.getPublishedDate() == null){
					update = entry.getUpdatedDate();
				} else {
					System.err.println("날짜 데이터가 없습니다. : " + data[1]);
				}

				//System.out.println(data[1] + " // " + update);
				if (entry.getContents() != null && entry.getContents().toString().contains("SyndContentImpl.value=")) {
					String tmp = entry.getContents().toString().substring(entry.getContents().toString().indexOf("SyndContentImpl.value=") + 22);
					feed.add(new RSS(data[1], "[" + entry.getTitle() + "]"+Jsoup.parse(tmp).text(), update, entry.getLink()));
				} else if (entry.getDescription() != null && entry.getDescription().toString().contains("SyndContentImpl.value=")) {
					String tmp = entry.getDescription().toString().substring(entry.getDescription().toString().indexOf("SyndContentImpl.value=") + 22);
					feed.add(new RSS(data[1], "[" + entry.getTitle() + "]"+Jsoup.parse(tmp).text(), update, entry.getLink()));
				}
				else {
					System.err.println("컨텐츠가 없습니다. : " + data[1]);
					break;
				}
			}
			System.out.println("Done! : " + feed.size());

		}
		HadoopRepository hadoopRepository = HadoopRepository.getInstance();
		hadoopRepository.writeFile(feed);
	}
}
