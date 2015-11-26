package ViralNewsCollector.viralsources;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.logging.Level;

/**
 * Created by apatterson on 26/11/2015.
 * Mashable contains a section for posts related to content found on Twitter.
 * This crawler can only crawl static content so the what's hot section is unable to be crawled
 */
public class MashableTwitter extends ViralSource{
    private String rootURL = "http://mashable.com/category/twitter/";

    private HashSet<String> urlCache = new HashSet<String>(); //hold a list of urls already retrieved.

    @Override
    public void crawl() {
        Document doc;
        try {

            doc = Jsoup.connect(rootURL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A").get();

            Elements links = doc.select("h1.article-title a");

            //list of links found
            for(Element link: links){
                System.out.println(link);
                System.out.println(link.attr("abs:href"));

                //in the unlikely case that we can't find a link, dont attempt to use this 'link'
                if(link.attr("abs:href") == null)
                    continue;

                //if the url found is in the cache, do not retrieve the url.
                if(urlCache.contains(link.attr("abs:href"))) {
                    System.out.println("Doucment found in cache, ignoring document");
                    continue;
                }

                storeDocument(Jsoup.connect(link.attr("abs:href")).get());

                //add the URL to the cache so that we don't retrieve it again.
                urlCache.add(link.attr("abs:href"));
            }


        } catch (Exception e){
            logger.log(Level.SEVERE, "Error Retrieving from URL", e);
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        ViralSource vs = new MashableTwitter();
        vs.crawl();

    }
}
