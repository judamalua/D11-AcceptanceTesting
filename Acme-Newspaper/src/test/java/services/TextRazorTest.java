
package services;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.account.AccountManager;
import com.textrazor.account.model.Account;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entailment;

public class TextRazorTest {

	public static void main(final String[] args) throws NetworkException, AnalysisException {
		final AccountManager manager = new AccountManager("2a7775eb6f2695154d305aad841da8856f2c633f5a0541d3208dad40");

		final Account account = manager.getAccount();

		System.out.println("Your current account plan is " + account.getPlan() + ", which includes " + account.getPlanDailyRequestsIncluded() + " daily requests, " + account.getRequestsUsedToday() + " used today");

		final TextRazor client = new TextRazor("2a7775eb6f2695154d305aad841da8856f2c633f5a0541d3208dad40");

		client.addExtractor("entailments");
		client.addExtractor("entities");
		client.addExtractor("words");
		client.setCleanupHTML(true);

		final AnalyzedText response = client.analyze("LONDON - Barclays misled shareholders and the public RBS about one of the biggest investments in the bank's history, a BBC Panrama investigation has found.");
		for (final Entailment entity : response.getResponse().getEntailments())
			System.out.println("Matched Entity: " + entity.getEntailedWords() + " score:" + entity.getScore());
	}
}
