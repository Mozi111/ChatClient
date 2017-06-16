import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class PrimerUporabe {

	public static void pretvori() throws IOException {
		// s tem objektom pretvarjamo iz in v JSON
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());

		// en uporabnik v JSON
		Uporabnik asistent = new Uporabnik("aljaz", new Date());
		String jsonAsistent = mapper.writeValueAsString(asistent);
		System.out.println(jsonAsistent);

		// iz JSON v enega uporabnika
		Uporabnik asistent2 = mapper.readValue(jsonAsistent, Uporabnik.class);
		System.out.println(asistent2);

		// seznam uporabnikov v JSON
		List<Uporabnik> asistenti = new ArrayList<Uporabnik>();
		asistenti.add(new Uporabnik("aljaz", new Date()));
		asistenti.add(new Uporabnik("matjaz", new Date()));
		String jsonAsistenti = mapper.writeValueAsString(asistenti);
		System.out.println(jsonAsistenti);

		// To ne dela, ker ne ve, da mora narediti Uporabnike.
		// List<Uporabnik> asistenti3 = mapper.readValue(jsonAsistenti,
		// List.class);
		// System.out.println("asistenti3: " + asistenti3);

		// iz JSON v seznam uporabnikov
		TypeReference<List<Uporabnik>> t = new TypeReference<List<Uporabnik>>() {
		};
		List<Uporabnik> asistenti2 = mapper.readValue(jsonAsistenti, t);
		System.out.println(asistenti2);
	}

}
