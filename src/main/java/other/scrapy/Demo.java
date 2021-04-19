package other.scrapy;

public class Demo {

  public static final String Url = "https://www.usatoday.com/news/politics/";

  public static void main(String[] args) throws Exception {
    ScrapyHandler handler = new ScrapyHandler(Url);
    handler.getPageSource().formatContent().saveFile();
    String fileName = handler.getFileName();

    PoliticHandler politicHandler =
        new PoliticHandler(fileName, fileName.replace("politics.html", "politics.txt"));
    politicHandler.handle().formatOutput().saveFile();
  }
}

