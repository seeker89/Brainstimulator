package com.web.client;

import com.google.gwt.core.client.JavaScriptObject;

class JsArray extends JavaScriptObject{

	protected JsArray() {}

	public final native int length()
	/*-{ return this.length; }-*/;
	
	public final native String asJSON()
	/*-{ return JSON.stringify(this); }-*/;
	
	public final native String nElement(int n)
	/*-{ return JSON.stringify(this[n]); }-*/;
	
	public static final native JsArray asVideo(String jsonStr)
	/*-{ var a = JSON.parse(jsonStr); return a; }-*/;
}