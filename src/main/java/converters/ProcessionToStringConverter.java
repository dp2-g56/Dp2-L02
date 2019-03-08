
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Procession;

@Component
@Transactional
public class ProcessionToStringConverter implements Converter<Procession, String> {

	@Override
	public String convert(Procession procession) {
		String result;

		if (procession == null)
			result = null;
		else
			result = String.valueOf(procession.getId());
		return result;
	}

}
