
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Lusit;

@Component
@Transactional
public class LusitToStringConverter implements Converter<Lusit, String> {

	@Override
	public String convert(final Lusit chirp) {
		String result;

		if (chirp == null) {
			result = null;
		} else {
			result = String.valueOf(chirp.getId());
		}

		return result;
	}

}
