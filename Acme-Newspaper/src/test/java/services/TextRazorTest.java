
package services;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.account.AccountManager;
import com.textrazor.account.model.Account;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entailment;
import com.textrazor.annotations.Word;

public class TextRazorTest {

	public static void main(final String[] args) throws NetworkException, AnalysisException {
		final AccountManager manager;
		final AnalyzedText response;
		final Account account;
		final TextRazor client;

		manager = new AccountManager("2a7775eb6f2695154d305aad841da8856f2c633f5a0541d3208dad40");

		account = manager.getAccount();

		System.out.println("Your current account plan is " + account.getPlan() + ", which includes " + account.getPlanDailyRequestsIncluded() + " daily requests, " + account.getRequestsUsedToday() + " used today");

		client = new TextRazor("2a7775eb6f2695154d305aad841da8856f2c633f5a0541d3208dad40");

		client.addExtractor("words");
		client.addExtractor("entailments");

		response = client.analyze("I love pop, pop is the best music in the world.");
		for (final Word word : response.getResponse().getWords())
			System.out.println("Matched Word: " + word.getStem());

		for (final Entailment entailment : response.getResponse().getEntailments())
			System.out.println("Matched Entailment: " + entailment.getEntailedWords() + " score:" + entailment.getScore());

	}
}
