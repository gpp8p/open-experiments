package org.sakaiproject.kernel.util;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

public class PathUtilsTest {

  @Test
  public void testGetUserPrefix() {
    assertEquals("/61/51/anon/", PathUtils.getUserPrefix(""));
    assertNull(PathUtils.getUserPrefix(null));
    assertEquals("/22/C6/Lorem/", PathUtils.getUserPrefix("Lorem"));
    assertEquals("/DA/3B/ipsum/", PathUtils.getUserPrefix("ipsum"));
    assertEquals("/90/8B/amet_/", PathUtils.getUserPrefix("amet."));
  }

  @Test
  public void testGetMessagePrefix() {
    Pattern prefixFormat = Pattern.compile("^\\d{4}/\\d{1,2}/$");
    assertTrue(prefixFormat.matcher(PathUtils.getMessagePrefix()).matches());
  }

  @Test
  public void testGetParentReference() {
    assertEquals("/Lorem/ipsum/dolor", PathUtils
        .getParentReference("/Lorem/ipsum/dolor/sit"));
    assertEquals("/", PathUtils.getParentReference("/Lorem/"));
    assertEquals("/", PathUtils.getParentReference("/"));
    assertEquals("/", PathUtils.getParentReference(""));
  }

  @Test
  public void testGetPoolPrefix() {
    Pattern prefixFormat = Pattern
        .compile("^\\d{4}/\\d{1,2}//\\p{XDigit}{2}/\\p{XDigit}{2}/\\w+/$");
    String path = PathUtils.getPoolPrefix("Lorem");
    assertTrue(prefixFormat.matcher(path).matches());
    assertTrue(path.endsWith("//22/C6/Lorem/"));
  }

  @Test
  public void testNormalizePath() {
    assertEquals("/Lorem/ipsum/dolor/sit", PathUtils
        .normalizePath("/Lorem//ipsum/dolor///sit"));
    assertEquals("/Lorem/ipsum", PathUtils.normalizePath("//Lorem/ipsum/"));
    assertEquals("/Lorem/ipsum", PathUtils.normalizePath("/Lorem/ipsum//"));
    assertEquals("/", PathUtils.normalizePath("/"));
    assertEquals("/Lorem", PathUtils.normalizePath("Lorem"));
    assertEquals("/", PathUtils.normalizePath(""));
  }
}