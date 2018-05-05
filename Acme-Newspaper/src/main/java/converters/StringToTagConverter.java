
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.TagRepository;
import domain.Tag;

@Component
@Transactional
public class StringToTagConverter implements Converter<String, Tag> {

	@Autowired
	TagRepository	tagRepository;


	@Override
	public Tag convert(final String text) {
		Tag result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.tagRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
