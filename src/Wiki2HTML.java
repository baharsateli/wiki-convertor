import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;

public class Wiki2HTML {
	
	public static void main(String args[]){
		String wikitext = "Is this '''working?''' Yes it does Jean-Francois!";
		
		MarkupParser markupParser = new MarkupParser();
		markupParser.setMarkupLanguage(new MediaWikiLanguage());
		String htmlContent = markupParser.parseToHtml(wikitext);
		System.out.println(htmlContent);
	}

}
