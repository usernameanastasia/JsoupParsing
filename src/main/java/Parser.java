import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static void main(String[] args) throws IOException, JSONException {

    try{
        Document doc = Jsoup.connect("https://www.apple.com/").get();
        String name = doc.title();
        System.out.println("Name of page html: " + name);

        List<Headline> headlines = new ArrayList<>();
        List<Subhead> subheads = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        // Print headlines
        Elements h2Elements = doc.getElementsByAttributeValue("class", "headline");
        h2Elements.forEach(h2Element ->{
            String headline = h2Element.text();
            headlines.add(new Headline(headline));
        });
        headlines.forEach(System.out::println);

        // Print subheads
        Elements h3Elements = doc.getElementsByAttributeValue("class", "subhead");
        h3Elements.forEach(h3Element ->{
            String title = h3Element.text();
           subheads.add(new Subhead(title));
        });
        subheads.forEach(System.out::println);

        // Print links
        Elements linkElements = doc.getElementsByAttributeValue("class", "cta-links");

        linkElements.forEach(linkElement ->{
            Elements elements = linkElement.children();

            elements.forEach(myElement ->{
            String url =  myElement.attr("abs:href");
            String text = myElement.text();

            links.add(new Link(url, text));});
        });

       links.forEach(System.out::println);


       // JSON OBJECT
        Apple apple = new Apple(headlines, subheads, links);
        Gson gson = new Gson();
        String json = gson.toJson(apple);


       // Все гиперссылки на странице
        Elements urls = doc.select("a");
        for(Element url : urls){
                System.out.println("\n" + url.attr("abs:href"));
        }


    }
    catch (IOException e){
    e.printStackTrace();
    }

   }


    static class Subhead {
        private String subhead;

        Subhead(String subhead) {
           this.subhead = subhead;
       }

       public String getSubhead() {
           return subhead;
       }

       public void setSubhead(String subhead) {
           this.subhead = subhead;
       }

        @Override
        public String toString() {
            return "Subhead:\t"+subhead;
        }
   }

   static class Link{
        private String url;
        private String text;

       Link(String url, String text) {
           this.url = url;
           this.text = text;
       }

       @Override
       public String toString() {
           return "Url:\t"+url + "\nText:\t" + text ;
       }

       public String getText() {
           return text;
       }

       public void setText(String text) {
           this.text = text;
       }
   }

   static class Headline{
       private String headline;

       Headline(String headline) {
           this.headline = headline;
       }

       public void setHeadline(String headline) {
           this.headline = headline;
       }

       public String getHeadline() {
           return headline;
       }

       @Override
       public String toString() {
           return "Headline:\t" + headline;
       }
   }

   static class Apple {
       List<Headline> headline;
       List<Subhead> subhead;
       List<Link> link;

       Apple(List<Headline> headline, List<Subhead> subhead, List<Link> link) {
           this.headline = headline;
           this.subhead = subhead;
           this.link = link;
       }
   }

}