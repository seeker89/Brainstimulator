package com.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class brainstimulatoradmin implements EntryPoint {
 
  public void onModuleLoad() {
	  
	  
    final Label errorLabel = new Label();
    RootPanel.get("errorLabelContainer").add(errorLabel);
    
    
    
    // Create a handler
    class RemoveHandlerVideos implements ClickHandler {
    	
    	public Label errorLabel;
    	public int id;
    	public int row;
    	public FlexTable table;
    	
      /**
       * Fired when the user clicks on the sendButton.
       */
      public void onClick(ClickEvent event) {
    	  RequestBuilder builderPost = new RequestBuilder(RequestBuilder.DELETE, "admin/videos/remove/"+id);
    	  try {
			Request request = builderPost.sendRequest("id="+id, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					errorLabel.setText("Problem deleting video... : " + exception);
				}
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						System.out.println("request successful");
						table.removeRow(row);
					}
				}
			});
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    }
    
    // Create a handler
    class RemoveHandlerUsers implements ClickHandler {
    	
    	public Label errorLabel;
    	public int id;
    	public int row;
    	public FlexTable table;
    	
      /**
       * Fired when the user clicks on the sendButton.
       */
      public void onClick(ClickEvent event) {
    	  RequestBuilder builderPost = new RequestBuilder(RequestBuilder.DELETE, "admin/users/remove/"+id);
    	  try {
			Request request = builderPost.sendRequest("id="+id, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					errorLabel.setText("Problem deleting user... : " + exception);
				}
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						System.out.println("request successful");
						table.removeRow(row);
					}
				}
			});
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    }
    
    
    
    // VIDEOS TABLE
    RequestBuilder builderUsers = new RequestBuilder(RequestBuilder.GET, "/admin/users");
    
    try {
		Request request = builderUsers.sendRequest(null, new RequestCallback() {
			public void onError(Request request, Throwable exception) {
				errorLabel.setText("Pb JSON: " + exception);
			}
			public void onResponseReceived(Request request, Response response) {
				if (200 == response.getStatusCode()) {
					System.out.println(response.getText());
					JsArray p= JsArray.asVideo(response.getText());
					
					System.out.println(p.length() + " videos parsed");
					
					// header
					FlexTable t = new FlexTable();
					t.setStyleName("table");
					t.addStyleName("table_hover");
					t.addStyleName("table_striped");
					t.setText(0, 1, "Id");
					t.setText(0, 2, "Name");
					t.setText(0, 3, "Mail");
					t.setText(0, 4, "Actions");
					
					for(int i=0; i < p.length(); i++){
						JsUser usr = JsUser.asUser(p.nElement(i));

					    t.setText(i+1, 1, Integer.toString(usr.getId()));
					    t.setText(i+1, 2, usr.getName());
					    t.setText(i+1, 3, usr.getMail());
					    
					    // remove button
					    Button removeBtn = new Button("remove");
					    
					    t.setWidget(i+1, 4, removeBtn);
					    
					    RemoveHandlerUsers handler = new RemoveHandlerUsers();
					    handler.errorLabel = errorLabel;
					    handler.id = usr.getId();
					    handler.row = i+1;
					    handler.table = t;
					    removeBtn.addClickHandler(handler);
					}

					RootPanel.get("table_users").add(t);
					
				} else {
					errorLabel.setText("Probleme JSON: status code: " + response.getStatusCode());
				}
			}
		});
	} catch (RequestException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    
    // VIDEOS TABLE
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "/admin/videos");
    
    try {
		Request request = builder.sendRequest(null, new RequestCallback() {
			public void onError(Request request, Throwable exception) {
				errorLabel.setText("Pb JSON: " + exception);
			}
			public void onResponseReceived(Request request, Response response) {
				if (200 == response.getStatusCode()) {
					System.out.println(response.getText());
					JsArray p= JsArray.asVideo(response.getText());
					
					System.out.println(p.length() + " videos parsed");
					
					// header
					FlexTable t = new FlexTable();
					t.setStyleName("table");
					t.addStyleName("table_hover");
					t.addStyleName("table_striped");
					
					t.setText(0, 1, "Title");
					t.setText(0, 2, "Url");
					t.setText(0, 3, "Id");
					t.setText(0, 4, "Views");
					t.setText(0, 5, "Created");
					t.setText(0, 6, "Actions");
					
					for(int i=0; i < p.length(); i++){
						JsVideo video = JsVideo.asVideo(p.nElement(i));
						System.out.println(video.getTitle());

					    t.setText(i+1, 1, video.getTitle());
					    t.setText(i+1, 2, video.getUrl());
					    t.setText(i+1, 3, Integer.toString(video.getId()));
					    t.setText(i+1, 4, Integer.toString(video.getViews()));
					    t.setText(i+1, 5, video.getDate());
					    
					    
					    // remove button
					    Button removeBtn = new Button("remove");
					    
					    t.setWidget(i+1, 6, removeBtn);
					    
					    RemoveHandlerVideos handler = new RemoveHandlerVideos();
					    handler.errorLabel = errorLabel;
					    handler.id = video.getId();
					    handler.row = i+1;
					    handler.table = t;
					    removeBtn.addClickHandler(handler);
					}

					RootPanel.get("table_videos").add(t);
					
				} else {
					errorLabel.setText("Probleme JSON: status code: " + response.getStatusCode());
				}
			}
		});
	} catch (RequestException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}
