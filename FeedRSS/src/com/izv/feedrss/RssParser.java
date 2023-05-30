package com.izv.feedrss;

	import java.io.IOException;
	import java.io.InputStream;
	import java.net.MalformedURLException;
	import java.net.URL;
	import java.util.List;
	import javax.xml.parsers.SAXParser;
	import javax.xml.parsers.SAXParserFactory;

public class RssParser {
	
	private URL url;
    
    public RssParser(String url){
    	try{
    		this.url = new URL(url);
    	}catch (MalformedURLException e){
    		throw new RuntimeException(e);
    	}
    }
    
    public List<ItemRss> parse() {
    	SAXParserFactory factory = SAXParserFactory.newInstance();
    	try{
	        SAXParser parser = factory.newSAXParser();
	        RssHandler handler = new RssHandler();
	        parser.parse(this.getInputStream(), handler);
	        return handler.getElemento();
    	}catch (Exception e){
    		throw new RuntimeException(e);
    	}
    }

    private InputStream getInputStream(){
    	try{
    		return url.openConnection().getInputStream();
    	}catch (IOException e){
    		throw new RuntimeException(e);
    	}
    }
}
