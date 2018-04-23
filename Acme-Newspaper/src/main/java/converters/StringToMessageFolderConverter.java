
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.MessageFolderRepository;
import domain.MessageFolder;

@Component
@Transactional
public class StringToMessageFolderConverter implements Converter<String, MessageFolder> {

	@Autowired
	MessageFolderRepository	messageFolderRepository;


	@Override
	public MessageFolder convert(final String text) {
		MessageFolder result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.messageFolderRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
