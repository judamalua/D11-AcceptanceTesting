
package services;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entailment;

public class TextRazorTest {

	public static void main(final String[] args) throws NetworkException, AnalysisException {

		final TextRazor client = new TextRazor("2a7775eb6f2695154d305aad841da8856f2c633f5a0541d3208dad40");

		client.addExtractor("words");
		client.addExtractor("entities");
		client.addExtractor("relations");
		client.addExtractor("topics");
		client.addExtractor("dependency-trees");
		client.addExtractor("entailments");
		client.setCleanupHTML(true);

		final AnalyzedText response = client.analyzeUrl("https://www.coca-colacompany.com/");
		for (final Entailment entity : response.getResponse().getEntailments())
			System.out.println("Matched Entity: " + entity.getEntailedWords() + " score:" + entity.getScore());

	}
}
