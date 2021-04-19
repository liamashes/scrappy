package other.scrapy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class PoliticHandler {
  private static final String delimiter = "|||";
  private String fileName;
  private List<PoliticInfo> politicInfoList;
  private StringBuilder outContent = new StringBuilder();
  private String outFileName;

  public PoliticHandler(String fileName, String outFileName) {
    this.fileName = fileName;
    this.outFileName = outFileName;
  }

  public static void handleElement(
      List<PoliticInfo> politicInfos, Element element, Boolean hasCbr) {
    PoliticInfo info = new PoliticInfo();
    List<TextNode> textNode = element.textNodes();
    info.setTitle(textNode.get(0).getWholeText());
    if (hasCbr) {
      info.setAbstracts(element.child(0).parent().attr("data-c-br"));
    }
    info.setTime(element.child(1).attr("data-c-dt"));
    info.setMs(element.child(1).attr("data-c-ms"));
    politicInfos.add(info);
  }

  public PoliticHandler handle() {
    Document htmlFile = null;
    politicInfoList = new ArrayList<>();
    try {
      htmlFile = Jsoup.parse(new File(fileName), "UTF-8");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } // right
    String title = htmlFile.title();
    Elements gntPr = htmlFile.getElementsByClass("gnt_pr");
    for (Element tmpPr : gntPr) {
      handleElement(politicInfoList, tmpPr.child(0), false);
      Element list = tmpPr.child(1);
      for (int i = 0; i < list.childrenSize(); i++) {
        handleElement(politicInfoList, list.child(i), true);
      }
    }

    outContent.append(politicInfoList.toString());
    return this;
  }

  public PoliticHandler formatOutput() {
    outContent = new StringBuilder();
    outContent
        .append("Title")
        .append(delimiter)
        .append("abstract")
        .append(delimiter)
        .append("ms")
        .append(delimiter)
        .append("time")
        .append("\n");

    for (PoliticInfo info : politicInfoList) {
      outContent
          .append(info.getTitle())
          .append(delimiter)
          .append(info.getAbstracts())
          .append(delimiter)
          .append(info.getMs())
          .append(delimiter)
          .append(info.getTime())
          .append("\n");
    }

    return this;
  }

  public PoliticHandler saveFile() {
    long time = System.currentTimeMillis();
    FileWriter fwriter = null;
    try {
      fwriter = new FileWriter(outFileName);
      fwriter.write(outContent.toString());
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
    System.out.println("保存耗时:" + (System.currentTimeMillis() - time) + "\n文件路径为：" + outFileName);
    return this;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  private static class PoliticInfo {
    String title;
    String time;
    String abstracts;
    String ms;
  }
}

