package com.freakylab;

import java.util.Date;

/**
 * Created by js on 2017. 4. 30..
 */
public class RSS {
	private String pressType;
	private String contents;
	private Date pubDate;
	private String link;

	public RSS(String pressType, String contents, Date pubDate, String link) {
		this.pressType = pressType;
		this.contents = contents;
		this.pubDate = pubDate;
		this.link = link;
	}

	@Override
	public String toString() {
		return pressType + "\t" + contents + "\t" + pubDate + "\t" + "link" + "\n";
	}

	public String getPressType() {
		return pressType;
	}

	public String getContents() {
		return contents;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public String getLink() {
		return link;
	}
}
