package com.android.apps.naverdic.servicecall;

import android.view.View;

public class RequestTaskHandler {
	private RequestTask rt;
	
	
	public RequestTaskHandler(View view) {
		this.rt = new RequestTask(view);
	}
	
	public void getRealTimeSearchOrder(String ID) {
		rt.setID(ID);
		rt.execute("http://openapi.naver.com/search?key=c1b406b32dbbbbeee5f2a36ddc14067f&query=nexearch&target=rank");
	}
	
	public void getNewsSearchResult(String ID) {
		rt.setID(ID);
		rt.execute("http://openapi.naver.com/search?key=c1b406b32dbbbbeee5f2a36ddc14067f&query=%EC%A3%BC%EC%8B%9D&target=news&start=1&display=10");
	}
	
	public void getMovieSearchResult(String ID) {
		rt.setID(ID);
		rt.execute("http://openapi.naver.com/search?key=c1b406b32dbbbbeee5f2a36ddc14067f&query=%EB%B2%A4%ED%97%88&display=10&start=1&target=movie");
	}
	
	

}
