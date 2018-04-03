package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ArticleRepository;
import domain.Article;

@Service
@Transactional
public class ArticleService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ArticleRepository	articleRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Article create() {
		Article result;

		result = new Article();

		return result;
	}

	public Collection<Article> findAll() {

		Collection<Article> result;

		Assert.notNull(this.articleRepository);
		result = this.articleRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Article findOne(final int articleId) {

		Article result;

		result = this.articleRepository.findOne(articleId);

		return result;

	}

	public Article save(final Article article) {

		assert article != null;

		Article result;

		result = this.articleRepository.save(article);

		return result;

	}

	public void delete(final Article article) {

		assert article != null;
		assert article.getId() != 0;

		Assert.isTrue(this.articleRepository.exists(article.getId()));

		this.articleRepository.delete(article);

	}
}

