import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class Klient {

	public static List<Sporocilo> prejmi_sporocilo(String link, String username) throws ClientProtocolException, IOException, URISyntaxException {
		String time = Long.toString(new Date().getTime());
		URI uri = new URIBuilder(link).addParameter("username", username).addParameter("stop_cache", time).build();
		String responseBody = Request.Get(uri).execute().returnContent().asString();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());
		TypeReference<List<Sporocilo>> t = new TypeReference<List<Sporocilo>>() {
		};
		List<Sporocilo> sporocila = mapper.readValue(responseBody, t);
		return sporocila;
	}

	public static String poslji_sporocilo(String link, String username, String sporocilo, Boolean javno)
			throws ClientProtocolException, IOException, URISyntaxException {
		String time = Long.toString(new Date().getTime());
		URI uri = new URIBuilder(link).addParameter("username", username).addParameter("stop_cache", time).build();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());
		Sporocilo sporociloJSON = new Sporocilo(javno, sporocilo);
		String SporociloString = mapper.writeValueAsString(sporociloJSON);
		System.out.println(SporociloString);

		String responseBody = Request.Post(uri).bodyString(SporociloString, ContentType.APPLICATION_JSON).execute()
				.returnContent().asString();
		
		return responseBody;
	}

	public static List<String> imena_uporabnikov(List<Uporabnik> seznam) {
		List<String> seznam_imen = new ArrayList<String>();

		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());

		for (Uporabnik uporabnik : seznam) {
			seznam_imen.add(uporabnik.getUsername());
		}

		return seznam_imen;
	}

	public static List<Uporabnik> seznam_uporabnikov(String link)
			throws ClientProtocolException, IOException, URISyntaxException {
		String responseBody = Request.Get(link).execute().returnContent().asString();

		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());

		TypeReference<List<Uporabnik>> t = new TypeReference<List<Uporabnik>>() {
		};
		List<Uporabnik> uporabniki = mapper.readValue(responseBody, t);
		return uporabniki;
	}

	public static String odjavi(String link, String username)
			throws ClientProtocolException, IOException, URISyntaxException {
		String time = Long.toString(new Date().getTime());
		URI uri = new URIBuilder(link).addParameter("username", username).addParameter("stop_cache", time).build();
		String responseBody = Request.Delete(uri).execute().returnContent().asString();
		return responseBody;
	}

	public static String prijavi(String link, String username)
			throws URISyntaxException, ClientProtocolException, IOException {
		String time = Long.toString(new Date().getTime());
		URI uri = new URIBuilder(link).addParameter("username", username).addParameter("stop_cache", time).build();
		String responseBody = Request.Post(uri).execute().returnContent().asString();
		return responseBody;
	}

}
