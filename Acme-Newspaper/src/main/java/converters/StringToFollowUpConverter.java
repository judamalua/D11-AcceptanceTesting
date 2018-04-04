
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.FollowUpRepository;
import domain.FollowUp;

@Component
@Transactional
public class StringToFollowUpConverter implements Converter<String, FollowUp> {

	@Autowired
	FollowUpRepository	followUpRepository;


	@Override
	public FollowUp convert(final String text) {
		FollowUp result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.followUpRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
