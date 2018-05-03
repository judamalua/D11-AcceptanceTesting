
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

		//		client = new TextRazor("2a7775eb6f2695154d305aad841da8856f2c633f5a0541d3208dad40");
		//
		//		client.addExtractor("words");
		//		client.addExtractor("entailments");
		//		client.setCleanupHTML(true);
		//
		//		entailmentScore = new HashMap<>();
		//		wordScore = new HashMap<>();
		//		entailedWords = new HashSet<>();
		//		maxScore = Double.MIN_VALUE;
		//		allStrings = "";
		//
		//		advertisements = this.advertisementService.findAll();
		//
		//		for (final Advertisement advertisement : advertisements)
		//			try {
		//				response = client.analyzeUrl(advertisement.getAdditionalInfoLink());
		//				entailments = response.getResponse().getEntailments();
		//
		//				if (entailments != null)
		//					for (final Entailment entailment : entailments) {
		//						System.out.println("Matched Entailments: " + entailment.getEntailedWords() + " score:" + entailment.getScore());
		//						for (final String entailedWord : entailment.getEntailedWords())
		//							if (entailedWord != null && entailmentScore.containsKey(entailedWord))
		//								entailmentScore.put(entailedWord, entailmentScore.get(entailedWord) + entailment.getScore());
		//							else if (entailedWord != null)
		//								entailmentScore.put(entailedWord, entailment.getScore());
		//					}
		//				else {
		//					words = response.getResponse().getWords();
		//					for (final Word word : words)
		//						if (word.getLemma() != null && wordScore.containsKey(word.getLemma())) {
		//							System.out.println("Matched word: " + word.getLemma() + " score:" + word.getLemma());
		//							wordScore.put(word.getLemma(), wordScore.get(word.getLemma()) + 1.);
		//						} else if (word.getLemma() != null) {
		//							System.out.println("Matched word: " + word.getLemma() + " score:" + word.getLemma());
		//							wordScore.put(word.getLemma(), 1.);
		//						}
		//				}
		//				allStrings += newspaper.getTitle() + " " + newspaper.getDescription();
		//
		//				for (final Article article : newspaper.getArticles())
		//					allStrings += " " + article.getTitle() + " " + article.getSummary() + " " + article.getBody();
		//
		//				responseNewspaper = client.analyze(allStrings);
		//
		//				if (entailments != null)
		//					for (final Entailment entailment : responseNewspaper.getResponse().getEntailments()) {
		//						System.out.println("Matched Entailments: " + entailment.getEntailedWords() + " score:" + entailment.getScore());
		//						for (final String entailedWord : entailment.getEntailedWords())
		//							if (entailedWord != null && entailmentScore.containsKey(entailedWord) && maxScore < entailmentScore.get(entailedWord)) {
		//								maxScore = entailmentScore.get(entailedWord);
		//								maxAdvertisement = advertisement;
		//							}
		//					}
		//				else
		//					for (final Word word : responseNewspaper.getResponse().getWords()) {
		//						System.out.println("Matched word: " + word.getLemma() + " score:" + word.getLemma());
		//
		//						if (word.getLemma() != null && wordScore.containsKey(word.getLemma()) && maxScore < entailmentScore.get(word.getLemma())) {
		//							maxScore = wordScore.get(word.getLemma());
		//							maxAdvertisement = advertisement;
		//						}
		//					}
		//
		//			} catch (final NetworkException e) {
		//			} catch (final AnalysisException e) {
		//			}
		//		if (maxAdvertisement != null)
		//			newspaper.getAdvertisements().add(maxAdvertisement);

	}
}
