$(window).bind "load", () ->
  console.log "mettre en place commentaires"
  $("#button-comment").bind "click", addComment

insertComment = (text) ->
	$("#container-comments").append('<p class="well">You just said: ' + text + '</p>')

addComment = (event) ->
  console.log "comments button clicked for the video id="+ event.target.href.split('-')[2]
  event.preventDefault()
  
  $(event.target).addClass("disabled");
  
  console.log "sending a new comment: " + $("#textarea-comment").val()
  
  
  request = $.ajax(
    url: "/addCommentAsync"
    type: "POST"
    data:
      'id': event.target.href.split('-')[2]
      'text': $("#textarea-comment").val()
    success: (data, status) ->
      console.log "success"
      $(event.target).html("Added")
      insertComment($("#textarea-comment").val())
      $("#textarea-comment").val("")
      $(event.target).removeClass("disabled");
    error: (data, status, err) ->
      console.log "error: "+ status + " " + err
      $(event.target).removeClass("disabled");
      $(event.target).html("Could not send! Please verify your network connection.")
  )