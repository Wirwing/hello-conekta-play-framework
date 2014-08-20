# Place all the behaviors and hooks related to the matching controller here.
# All this logic will automatically be available in application.js.
# You can use CoffeeScript in this file: http://coffeescript.org/
$ ->
  checkout.setupForm()

checkout =
  setupForm: ->
    $('#checkout-btn').click ->
      checkout.processCard()
      false
    
  processCard: ->
    $("#checkout-btn").attr("disabled", "disabled")
    $form = $('#checkout-form')
    console.log $form
    Conekta.token.create $form, checkout.conektaSuccessResponseHandler, checkout.conektaErrorResponseHandler

  conektaSuccessResponseHandler: (response) ->
    $("#checkout-btn").removeAttr("disabled")
    $('#buyForm #cardToken').val(response.id)
    $('#buyForm').get(0).submit()

  conektaErrorResponseHandler: (response) ->
    $("#checkout-message").html(response.message)
    $("#checkout-message").show 150
    $("#checkout-btn").removeAttr("disabled")