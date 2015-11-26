package ViralNewsCollector.viralsources;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.logging.Level;

/**
 * Created by apatterson on 26/11/2015.
 */
public class LadBible extends ViralSource {
    private String rootURL = "http://www.theladbible.com/type/viral";

    private HashSet<String> urlCache = new HashSet<String>(); //hold a list of urls already retrieved.

    @Override
    public void crawl() {
        Document doc;
        try {


            doc = Jsoup.connect(rootURL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A").get();

            Elements articles = doc.select("section.list article");

            System.out.println("Number of articles found:"+articles.size());
            //list of links found
            for(Element article: articles){

                Element titleLink = article.select("a").first();
                System.out.println(titleLink.attr("abs:href"));

                //if the url found is in the cache, do not retrieve the url.
                if(urlCache.contains(titleLink.attr("abs:href"))) {
                    System.out.println("Doucment found in cache, ignoring document");
                    continue;
                }

                storeDocument(Jsoup.connect(titleLink.attr("abs:href")).get());

                //add the URL to the cache so that we don't retrieve it again.
                urlCache.add(titleLink.attr("abs:href"));
            }


        } catch (Exception e){
            logger.log(Level.SEVERE, "Error Retrieving from URL", e);
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        ViralSource vs = new LadBible();
        vs.crawl();

    }
}
