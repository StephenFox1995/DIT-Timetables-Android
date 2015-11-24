package org.stephenfox.dittimetables.utilities;

/**
 * The Nomad methods.
 * Methods which are used across the app but don't really
 * belong anywhere, so they're given refuge here.
 */
public class Utilities {


  /**
   * Replaces a character index of a string.
   *
   * @param s The string.
   * @param c The character to replace at index.
   * @param index the index of the character
   * @return A new string with the replaced index.
   * */
  public static String stringWithReplacedIndex(String s, char c, int index) {
    StringBuilder stringBuilder = new StringBuilder(s);
    stringBuilder.setCharAt(index, c);
    return stringBuilder.toString();
  }



  public static String stringRemoveWhiteSpace(String s) {
    return s.replaceAll("\\s","");
  }

}
