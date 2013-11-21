package com.web.client;

import com.google.gwt.core.client.JavaScriptObject;

class JsVideo extends JavaScriptObject{

	protected JsVideo() {}
	
	public final native String getTitle()
	/*-{ return this.title; }-*/;
	
	public final native int getId()
	/*-{ return this.id; }-*/;
	
	public final native String getUrl()
	/*-{ return this.url; }-*/;
	
	public final native int getViews()
	/*-{ return this.views; }-*/;
	
	public final native String getDate()
	/*-{ return Date(this.registered); }-*/;
	
	public final native int length()
	/*-{ return this.length; }-*/;
	
	public final native String asJSON()
	/*-{ return JSON.stringify(this); }-*/;
	
	public static final native JsVideo asVideo(String jsonStr)
	/*-{ var a = JSON.parse(jsonStr); return a; }-*/;
}