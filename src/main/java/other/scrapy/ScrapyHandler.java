package other.scrapy;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.IOException;

public class ScrapyHandler {

  private static final String chromeDriver = "/Users/cheng/opt/soft/chromedriver";
  private String url;
  private String htmlContent;
  private String fileName;
  private String workPath = "/Users/cheng/Downloads/tmp/test/scrapy/";

  public ScrapyHandler(String url) {
    this.url = url;
    int offset = 0;
    if (url.endsWith("/")) {
      offset = 1;
    }
    this.fileName = workPath + url.split("/")[url.split("/").length - offset] + ".html";
  }

  public ScrapyHandler(String url, String fileName) {
    this.url = url;
    if (fileName.contains("/") || fileName.contains("\\")) {
      this.fileName = fileName;
    } else {
      this.fileName = workPath + fileName;
    }
  }

  public ScrapyHandler getPageSource() throws Exception {
    long time = System.currentTimeMillis();
    // 可省略，若驱动放在其他目录需指定驱动路径
    System.setProperty("webdriver.chrome.driver", chromeDriver);
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments("--headless");
    ChromeDriver driver = new ChromeDriver(chromeOptions);
    driver.get(url);
    // 休眠1s,为了让js执行完
    Thread.sleep(1000l);
    // 网页源码
    htmlContent = driver.getPageSource();
    driver.close();
    long getTime = System.currentTimeMillis();
    System.out.println("获取耗时:" + (getTime - time));

    return this;
  }

  public ScrapyHandler saveFile() {
    long time = System.currentTimeMillis();
    FileWriter fwriter = null;
    try {
      fwriter = new FileWriter(fileName);
      fwriter.write(htmlContent);
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      try {
        fwriter.flush();
        fwriter.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    System.out.println("保存耗时:" + (System.currentTimeMillis() - time) + "\n文件路径为：" + fileName);
    return this;
  }

  public ScrapyHandler formatContent() {
    return this;
  }

  public String getFileName() {
    return fileName;
  }
}

