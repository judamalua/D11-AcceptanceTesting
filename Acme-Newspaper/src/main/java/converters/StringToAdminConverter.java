
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.AdminRepository;
import domain.Admin;

@Component
@Transactional
public class StringToAdminConverter implements Converter<String, Admin> {

	@Autowired
	AdminRepository	adminRepository;


	@Override
	public Admin convert(final String text) {
		Admin result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.adminRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
