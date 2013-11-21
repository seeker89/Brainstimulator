doGeolocalisation = ->
  options =
    zoom: 5
    mapTypeId: google.maps.MapTypeId.ROADMAP
  ###
  map = new google.maps.Map(document.getElementById("carte"), options)
  
  navigator.geolocation.getCurrentPosition ((position) ->
	  pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude)
	  infowindow = new google.maps.InfoWindow(
	    map: map
	    position: pos
	    content: "You are here!"
	  )
	  map.setCenter pos    
	 )
  ###
  
  navigator.geolocation.getCurrentPosition ((position) ->
  	
  	coords = new google.maps.LatLng(position.coords.latitude, position.coords.longitude)
  	
  	console.log(JSON.stringify(coords))
  	
  	request = $.ajax(
	    url: "http://maps.googleapis.com/maps/api/geocode/json?latlng="+coords.jb+","+coords.kb+"&sensor=true"
	    type: "GET"
	    datatype: "json"
	    success: (data, status) ->
	      console.log "success: "+ data.results[0].formatted_address
	      alert "You are connected from " + data.results[0].formatted_address + "... Creepy isn't it?"
	    error: (data, status, err) ->
	      console.log "error: "+ status + " " + err
	  	)
  )

$("#geo-me").bind "click", ->
  doGeolocalisation()

#google.maps.event.addDomListener window, "load", doGeolocalisation