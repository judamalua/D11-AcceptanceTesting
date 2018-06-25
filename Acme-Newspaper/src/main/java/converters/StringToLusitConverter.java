
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.LusitRepository;
import domain.Lusit;

@Component
@Transactional
public class StringToLusitConverter implements Converter<String, Lusit> {

	@Autowired
	LusitRepository	lusitRepository;


	@Override
	public Lusit convert(final String text) {
		Lusit result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.lusitRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
