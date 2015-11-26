package ViralNewsCollector.viralsources;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Document;
import persistence.CouchDB;
import persistence.DB;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.lang.Thread.sleep;

/**
 * Created by apatterson on 19/11/2015.
 */
public abstract class ViralSource implements Runnable{
    protected static final Logger logger = Logger.getLogger(ViralSource.class.getName());

    abstract public void crawl();

    public ViralSource(){
        FileHandler fh = null;
        try {
            fh = new FileHandler("failure.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }
    public void storeDocument(Document document){
        DB dbInterface = new CouchDB(this.getClass().getSimpleName().toLowerCase(), "localhost", 5984, "http");

        String src = StringEscapeUtils.escapeJson(document.outerHtml());
        String title = StringEscapeUtils.escapeJson(document.title());
        String location = StringEscapeUtils.escapeJson(document.location());
        String json = String.format("{title : \"%s\", url: \"%s\", timestamp: \"%s\", source: \"%s\" }", title, location, new Date(), src);

        //store document in json format: link, timestamp, page source
        if(!dbInterface.save(json))
            System.out.println("error saving document to database");

        System.out.println("Saved document");
    }

    public void run() {
        while(true){
            crawl();
            try {
                sleep(1800000);//evry  30 minutes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
