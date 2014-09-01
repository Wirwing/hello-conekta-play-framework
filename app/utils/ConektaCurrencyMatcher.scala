package utils

object ConektaCurrencyMatcher {

  //Add two trailing zeros to match conekta amount format
  def convertToConektaAmount(amount: Int): Int = amount * 100

}