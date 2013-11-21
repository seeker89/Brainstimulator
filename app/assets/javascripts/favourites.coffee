mettreEnPlace = ->
  console.log "mettre en place"
  $("#btn_favourite").bind "click", addFavourite
  $("#btn_vote").bind "click", vote

$(window).bind "load", mettreEnPlace

addFavourite = (event) ->
  console.log "favourites clicked, id ="+event.target.href.split('/')[4]
  event.preventDefault()
  
  $(event.target).addClass("disabled");
  
  request = $.ajax(
    url: "/addFavouriteAsync"
    type: "POST"
    data:
      'id': event.target.href.split('/')[4]
    success: (data, status) ->
      console.log "success"
      $(event.target).html("Already in Favourites")
      #$(event.target).hide()
    error: (data, status, err) ->
      console.log "error: "+ status + " " + err
      $(event.target).removeClass("disabled");
  )

vote = (event) ->
  console.log "favourites clicked, id ="+event.target.href.split('/')[4]
  event.preventDefault()

  $(event.target).addClass("disabled");

  request = $.ajax(
    url: "/voteAsc"
    type: "POST"
    data:
      'id': event.target.href.split('/')[4]
    success: (data, status) ->
      console.log "success:" + data.number
      $("#btn_vote").html("Voted")
      $("#votes-number").html(data.number)
    error: (data, status, err) ->
      console.log "error: "+ status + " " + err
      $(event.target).removeClass("disabled");
  )
