package org.stephenfox.dittimetables.utilities;


import java.util.ArrayList;
import java.util.Set;

public class Search implements CourseSearchable {

  /**
   * Performs a search on a set of strings.
   *
   * @param dataSet The Set to search.
   * @param query The string to find in the data set.
   * @param caseSensitive Is the search case sensitive.
   * @return All the possible results.
   **/
  public String[] performStringSearch(Set<String> dataSet, String query, boolean caseSensitive) {
    ArrayList<String> resultSet = new ArrayList<>();

    if (!caseSensitive) {
      for (String possibleResult : dataSet) {
        if (possibleResult.toLowerCase().contains(query.toLowerCase())) {
          resultSet.add(possibleResult);
        }
      }
    }
    else {
      for (String possibleResult : dataSet) {
        if (possibleResult.contains(query)) {
          resultSet.add(possibleResult);
        }
      }
    }
    return resultSet.toArray(new String[resultSet.size()]);
  }
}
