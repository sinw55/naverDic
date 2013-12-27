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
import com.android.apps.naverdic.xmlclass.Item;
import com.android.apps.naverdic.xmlclass.RNum;
import com.android.apps.naverdic.xmlclass.Result;

public class RequestTask extends AsyncTask<String, Integer, String> {	
	private View view;
	private Result result;
	private RNum rnum;
	private Item item;
	private String text;
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
			result = parserRealTimeSearchOrder(xmlResult);
			((TextView) view.findViewById(R.id.item_detail)).setText(getResultInfo(result));	
		} else if (ID.equals("2")) {
			((TextView) view.findViewById(R.id.item_detail)).setText(xmlResult);
		} else if (ID.equals("3")) {
			((TextView) view.findViewById(R.id.item_detail)).setText(xmlResult);
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
	
	protected String getResultInfo(Result result) {
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
	                    	item = new Item();
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

}
