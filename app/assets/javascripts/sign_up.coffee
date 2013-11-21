setUp = ->
	$("#form-signup").submit checkForm

$(window).bind "load", setUp

checkForm = ->
	v1 = $("#form-signup>fieldset>input")[3].value
	v2 = $("#form-signup>fieldset>input")[4].value
	v3 = $("#form-signup>fieldset>input")[5].value
	v4 = $("#form-signup>fieldset>input")[6].value
	v1 == v2 && v3 == v4