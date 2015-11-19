package org.stephenfox.dittimetables.utilities;


import java.util.Set;

/**
 * This interface provides the method signature
 * all should use if they wish to search for a course through
 * a set of courses.
 **/
public interface CourseSearchable {
  String[] performStringSearch(Set<String> dataSet, String query, boolean caseSensitive);
}
