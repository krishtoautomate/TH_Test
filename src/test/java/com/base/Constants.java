package com.base;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

  public static String IP_ADDRESS = "127.0.0.1";

  public static String HOST_OS = System.getProperty("os.name");

  public static String USER_DIR = System.getProperty("user.dir");

  public static String OUTPUT_DIRECTORY = USER_DIR + "/test-output/";

  public static String DATE_NOW = new SimpleDateFormat("MMddyyyy").format(new Date());

  public static String SCREENSHOTS_DIRECTORY = OUTPUT_DIRECTORY + DATE_NOW + "/img/";

  public static String EXTENT_REPORT_CONFIG_XML =
      USER_DIR + "/src/main/resources/extent-config.xml";
  public static String REPORT_DIR = OUTPUT_DIRECTORY + DATE_NOW + "/";
  public static String EXTENT_HTML_REPORT = REPORT_DIR + "AUTOMATION_REPORT" + ".html";
  public static String EMAILABLE_REPORT = REPORT_DIR + "EmailableReport" + ".html";

}
