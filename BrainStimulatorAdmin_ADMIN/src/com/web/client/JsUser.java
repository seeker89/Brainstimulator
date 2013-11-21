package com.web.client;

import com.google.gwt.core.client.JavaScriptObject;

class JsUser extends JavaScriptObject{

	protected JsUser() {}
	
	public final native String getName()
	/*-{ return this.firstName +" "+ this.lastName; }-*/;
	
	public final native int getId()
	/*-{ return this.id; }-*/;
	
	public final native String getMail()
	/*-{ return this.mail; }-*/;
	
	public final native String asJSON()
	/*-{ return JSON.stringify(this); }-*/;
	
	public static final native JsUser asUser(String jsonStr)
	/*-{ var a = JSON.parse(jsonStr); return a; }-*/;
}