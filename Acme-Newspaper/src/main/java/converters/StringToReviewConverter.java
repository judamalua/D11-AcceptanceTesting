
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ReviewRepository;
import domain.Review;

@Component
@Transactional
public class StringToReviewConverter implements Converter<String, Review> {

	@Autowired
	ReviewRepository	reviewRepository;


	@Override
	public Review convert(final String text) {
		Review result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.reviewRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
