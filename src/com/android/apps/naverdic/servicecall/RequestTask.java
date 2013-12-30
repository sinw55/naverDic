package com.android.apps.naverdic.servicecall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.view.View;
import android.webkit.WebView;

import com.android.apps.naverdic.R;
import com.android.apps.naverdic.xmlclass.movie.MovieChannel;
import com.android.apps.naverdic.xmlclass.movie.MovieItem;
import com.android.apps.naverdic.xmlclass.news.NewsChannel;
import com.android.apps.naverdic.xmlclass.news.NewsItem;
import com.android.apps.naverdic.xmlclass.realtime.RNum;
import com.android.apps.naverdic.xmlclass.realtime.RTItem;
import com.android.apps.naverdic.xmlclass.realtime.Result;

public class RequestTask extends AsyncTask<String, Integer, String> {	
	private View view;	
	private String ID;
	public RequestTask(View view) {
		this.view = view;
	}
	
	public void setID(String ID) {
		this.ID = ID;
	}
	
	public String getID() {
		return this.ID;
	}

	@Override
	protected String doInBackground(String... uri) {
		try {
			URL url = new URL(uri[0]);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			return readStream(con.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		//((TextView) view.findViewById(R.id.item_detail)).setText("Progressing...");
    }
	
	@Override
	protected void onPostExecute(String xmlResult) {
		
		
		if (ID.equals("1")) {
			//((TextView) view.findViewById(R.id.item_detail)).setText(getRealTimeSearchOrderResultInfo(parserRealTimeSearchOrder(xmlResult)));
			
			((WebView)view.findViewById(R.id.webView)).loadData(getRealTimeSearchOrderResultInfo(parserRealTimeSearchOrder(xmlResult)), "text/html", null);
		} else if (ID.equals("2")) {
			//((TextView) view.findViewById(R.id.item_detail)).setText(getNewsSearchResultInfo(parserNewsSearchOrder(xmlResult)));
			((WebView)view.findViewById(R.id.webView)).loadData(getNewsSearchResultInfo(parserNewsSearchOrder(xmlResult)), "text/html", "UTF-8");
		} else if (ID.equals("3")) {
			//((TextView) view.findViewById(R.id.item_detail)).setText(getMovieSearchResultInfo(parserMovieSearchOrder(xmlResult)));
			((WebView)view.findViewById(R.id.webView)).loadData(getMovieSearchResultInfo(parserMovieSearchOrder(xmlResult)), "text/html", "UTF-8");
		}
    }

	protected String readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuilder st = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				st.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return st.toString();
	}
	protected String getMovieSearchResultInfo(MovieChannel movieChannel) {
		StringBuilder sb = new StringBuilder();
		List<MovieItem> items = movieChannel.getItems();
		sb.append("<html><body>");
		sb.append("<h3><a href='" + movieChannel.getLink() + "'>" + movieChannel.getTitle() + "</a></h3>");
		sb.append("<p>description" + movieChannel.getDescription() + "</p>");
		sb.append("<p>lastBuildDate" + movieChannel.getLastBuildDate() + "</p>");
		sb.append("<p>total : " + movieChannel.getTotal() + "</p>");
		sb.append("<p>start : " + movieChannel.getStart() + "</p>");
		sb.append("<p>display : " + movieChannel.getDisplay() + "</p>");
		
		sb.append("<div><ul>");
		for (MovieItem item : items) {
			sb.append("<li><a href='" + item.getLink() + "'>");
			sb.append("<img src='" + item.getImage() + "'>");
			sb.append("<span>" + item.getTitle() + "</span>");
			sb.append("<div>" + item.getSubtitle() + "</div>");
			sb.append("<div>" + item.getPubDate() + "</div>");
			sb.append("<div>" + item.getDirector() + "</div>");
			sb.append("<div>" + item.getActor() + "</div>");
			sb.append("<div>" + item.getUserRating() + "</div>");
			sb.append("</a></li>");
		}
		sb.append("</ul></div>");
		sb.append("</body></html>");
		return sb.toString();
		
	}
	
	protected String getNewsSearchResultInfo(NewsChannel newsChannel) {
		StringBuilder sb = new StringBuilder();
		List<NewsItem> items = newsChannel.getItems();
		sb.append("title : " + newsChannel.getTitle() + "\n");
		sb.append("link : " + newsChannel.getLink() + "\n");
		sb.append("description" + newsChannel.getDescription() + "\n");
		sb.append("lastBuildDate" + newsChannel.getLastBuildDate() + "\n");
		sb.append("total : " + newsChannel.getTotal() + "\n");
		sb.append("start : " + newsChannel.getStart() + "\n");
		sb.append("display : " + newsChannel.getDisplay() + "\n");
		
		for (NewsItem item : items) {
			sb.append("----------------\n");
			sb.append("title : " + item.getTitle() + "\n");
			sb.append("originallink : " + item.getOriginallink() + "\n");
			sb.append("link : " + item.getLink() + "\n");
			sb.append("description : " + item.getDescription() + "\n");
			sb.append("pubDate : " + item.getPubDate() + "\n");
		}
		return sb.toString();
		
	}
	
	protected String getRealTimeSearchOrderResultInfo(Result result) {
    	StringBuilder sb = new StringBuilder();
    	List<RNum> rns = result.getItem().getRns();
    	sb.append("<html><body><ol>");
    	for (RNum rn : rns) {
    		sb.append("<li>" + rn.getK() + " ," + rn.getS() + " ," + rn.getV() + "</li>");
    	}
    	sb.append("</ol></body></html>");
    	return sb.toString();
    }
    
    //http://theopentutorials.com/tutorials/android/xml/android-simple-xmlpullparser-tutorial/
	protected Result parserRealTimeSearchOrder(String xmlResult) {
		Result result = null;
		RTItem item = null;
		RNum rnum = null;
		String text = "";
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xmlResult));
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagname = xpp.getName();
                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("result")) {
	                        result = new Result();
	                    } else if (tagname.equalsIgnoreCase("item")) {
	                    	item = new RTItem();
	                    } else if (tagname.matches("R([1-9]|10)")) {
	                    	rnum = new RNum();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = xpp.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                	if (tagname.equalsIgnoreCase("K")) {
	                        rnum.setK(text);
	                    } else if (tagname.equalsIgnoreCase("S")) {
	                    	rnum.setS(text);
	                    } else if (tagname.equalsIgnoreCase("V")) {
	                    	rnum.setV(text);
	                    } else if (tagname.matches("R([1-9]|10)")) {
	                    	item.addRNum(rnum);
	                    } else if (tagname.equalsIgnoreCase("item")) {
	                        result.setItem(item);
	                    } 
	                    break;
	                default:
	                    break;
				}
                eventType = xpp.next();
			}
		} catch (Exception e) {}
		return result;
	}
	
	protected MovieChannel parserMovieSearchOrder(String xmlResult) {
		MovieChannel movieChannel = null;
		MovieItem item = null;
		String text = "";
		boolean isChannelChild = false;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xmlResult));
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagname = xpp.getName();
                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("channel")) {
	                    	movieChannel = new MovieChannel();
	                    	isChannelChild = true;
	                    } else if (tagname.equalsIgnoreCase("item")) {
	                    	item = new MovieItem();
	                    	isChannelChild = false;
	                    } 
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = xpp.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                	if (isChannelChild) {
	                		if (tagname.equalsIgnoreCase("title")) {
	                			movieChannel.setTitle(text);
		                    } else if (tagname.equalsIgnoreCase("link")) {
		                    	movieChannel.setLink(text);
		                    } else if (tagname.equalsIgnoreCase("description")) {
		                    	movieChannel.setDescription(text);
		                    } else if (tagname.equalsIgnoreCase("lastBuildDate")) {
		                    	movieChannel.setLastBuildDate(text);
		                    } else if (tagname.equalsIgnoreCase("total")) {
		                    	movieChannel.setTotal(Integer.parseInt(text));
		                    } else if (tagname.equalsIgnoreCase("start")) {
		                    	movieChannel.setStart(Integer.parseInt(text));
		                    } else if (tagname.equalsIgnoreCase("display")) {
		                    	movieChannel.setDisplay(Integer.parseInt(text));
		                    }
	                	} else {
	                		if (tagname.equalsIgnoreCase("title")) {
	                			item.setTitle(text);
		                    } else if (tagname.equalsIgnoreCase("link")) {
		                    	item.setLink(text);
		                    } else if (tagname.equalsIgnoreCase("image")) {
		                    	item.setImage(text);
		                    } else if (tagname.equalsIgnoreCase("subtitle")) {
		                    	item.setSubtitle(text);
		                    } else if (tagname.equalsIgnoreCase("pubDate")) {
		                        item.setPubDate(text);
		                    } else if (tagname.equalsIgnoreCase("director")) {
		                        item.setDirector(text);
		                    } else if (tagname.equalsIgnoreCase("actor")) {
		                        item.setActor(text);
		                    } else if (tagname.equalsIgnoreCase("userRating")) {
		                        item.setUserRating(text);
		                    } else if (tagname.equalsIgnoreCase("item")) {
		                    	movieChannel.addItem(item);
		                    } 
	                	}
	                    break;
	                default:
	                    break;
				}
                eventType = xpp.next();
			}
		} catch (Exception e) {}
		return movieChannel;
	}
	
	protected NewsChannel parserNewsSearchOrder(String xmlResult) {
		NewsChannel newsChannel = null;
		NewsItem item = null;
		String text = "";
		boolean isChannelChild = false;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xmlResult));
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagname = xpp.getName();
                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("channel")) {
	                    	newsChannel = new NewsChannel();
	                    	isChannelChild = true;
	                    } else if (tagname.equalsIgnoreCase("item")) {
	                    	item = new NewsItem();
	                    	isChannelChild = false;
	                    } 
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = xpp.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                	if (isChannelChild) {
	                		if (tagname.equalsIgnoreCase("title")) {
	                			newsChannel.setTitle(text);
		                    } else if (tagname.equalsIgnoreCase("link")) {
		                    	newsChannel.setLink(text);
		                    } else if (tagname.equalsIgnoreCase("description")) {
		                    	newsChannel.setDescription(text);
		                    } else if (tagname.equalsIgnoreCase("lastBuildDate")) {
		                    	newsChannel.setLastBuildDate(text);
		                    } else if (tagname.equalsIgnoreCase("total")) {
		                    	newsChannel.setTotal(Integer.parseInt(text));
		                    } else if (tagname.equalsIgnoreCase("start")) {
		                    	newsChannel.setStart(Integer.parseInt(text));
		                    } else if (tagname.equalsIgnoreCase("display")) {
		                    	newsChannel.setDisplay(Integer.parseInt(text));
		                    }
	                	} else {
	                		if (tagname.equalsIgnoreCase("title")) {
	                			item.setTitle(text);
		                    } else if (tagname.equalsIgnoreCase("originallink")) {
		                    	item.setOriginallink(text);
		                    } else if (tagname.equalsIgnoreCase("link")) {
		                    	item.setLink(text);
		                    } else if (tagname.equalsIgnoreCase("description")) {
		                    	item.setDescription(text);
		                    } else if (tagname.equalsIgnoreCase("pubDate")) {
		                        item.setPubDate(text);
		                    } else if (tagname.equalsIgnoreCase("item")) {
		                    	newsChannel.addItem(item);
		                    } 
	                	}
	                    break;
	                default:
	                    break;
				}
                eventType = xpp.next();
			}
		} catch (Exception e) {}
		return newsChannel;
	}

}
