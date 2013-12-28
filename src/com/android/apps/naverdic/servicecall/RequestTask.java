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
import android.widget.TextView;

import com.android.apps.naverdic.R;
import com.android.apps.naverdic.xmlclass.movie.Channel;
import com.android.apps.naverdic.xmlclass.movie.MovieItem;
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
		((TextView) view.findViewById(R.id.item_detail)).setText("Progressing...");
    }
	
	@Override
	protected void onPostExecute(String xmlResult) {
		if (ID.equals("1")) {
			((TextView) view.findViewById(R.id.item_detail)).setText(getRealTimeSearchOrderResultInfo(parserRealTimeSearchOrder(xmlResult)));	
		} else if (ID.equals("2")) {
			((TextView) view.findViewById(R.id.item_detail)).setText(xmlResult);
		} else if (ID.equals("3")) {
			((TextView) view.findViewById(R.id.item_detail)).setText(getMovieSearchResultInfo(parserMovieSearchOrder(xmlResult)));
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
	protected String getMovieSearchResultInfo(Channel channel) {
		StringBuilder sb = new StringBuilder();
		List<MovieItem> items = channel.getItems();
		sb.append("title : " + channel.getTitle() + "\n");
		sb.append("link : " + channel.getLink() + "\n");
		sb.append("description" + channel.getDescription() + "\n");
		sb.append("lastBuildDate" + channel.getLastBuildDate() + "\n");
		sb.append("total : " + channel.getTotal() + "\n");
		sb.append("start : " + channel.getStart() + "\n");
		sb.append("display : " + channel.getDisplay() + "\n");
		
		for (MovieItem item : items) {
			sb.append("----------------\n");
			sb.append("title : " + item.getTitle() + "\n");
			sb.append("link : " + item.getLink() + "\n");
			sb.append("image : " + item.getImage() + "\n");
			sb.append("subtitle : " + item.getSubtitle() + "\n");
			sb.append("pubDate : " + item.getPubDate() + "\n");
			sb.append("director : " + item.getDirector() + "\n");
			sb.append("actor : " + item.getActor() + "\n");
			sb.append("userRating : " + item.getUserRating() + "\n");
			
		}
		return sb.toString();
		
	}
	protected String getRealTimeSearchOrderResultInfo(Result result) {
    	StringBuilder sb = new StringBuilder();
    	List<RNum> rns = result.getItem().getRns();
    	
    	for (RNum rn : rns) {
    		sb.append(rn.getK() + " ," + rn.getS() + " ," + rn.getV() + "\n");
    	}
    	
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
	
	protected Channel parserMovieSearchOrder(String xmlResult) {
		Channel channel = null;
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
	                    	channel = new Channel();
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
	                			channel.setTitle(text);
		                    } else if (tagname.equalsIgnoreCase("link")) {
		                    	channel.setLink(text);
		                    } else if (tagname.equalsIgnoreCase("description")) {
		                    	channel.setDescription(text);
		                    } else if (tagname.equalsIgnoreCase("lastBuildDate")) {
		                    	channel.setLastBuildDate(text);
		                    } else if (tagname.equalsIgnoreCase("total")) {
		                    	channel.setTotal(Integer.parseInt(text));
		                    } else if (tagname.equalsIgnoreCase("start")) {
		                    	channel.setStart(Integer.parseInt(text));
		                    } else if (tagname.equalsIgnoreCase("display")) {
		                    	channel.setDisplay(Integer.parseInt(text));
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
		                    	channel.addItem(item);
		                    } 
	                	}
	                    break;
	                default:
	                    break;
				}
                eventType = xpp.next();
			}
		} catch (Exception e) {}
		return channel;
	}

}
