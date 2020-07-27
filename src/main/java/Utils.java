import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.XML;

import com.google.gson.GsonBuilder;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;

/**
 * Classe utilitaria que fará algumas buscas por APIs
 */
@SuppressWarnings("ALL")
public class Utils {

	/**
	 * Fornecida a cidade, faz a busca da {@link Previsão do Tempo}
	 * 
	 * @param cidade
	 * @return {@link Previsão do Tempo}
	 * @throws IOException
	 */
	public static Tempo buscaPrevisaoTempoCidade(String cidade) {

		String urlTempo = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=4a601093649a88540fad287e496bfc07&units=metric", cidade);
		
		try {
			
			String jsonString = buscaUrl(urlTempo);

			return new GsonBuilder().create().fromJson(jsonString, Tempo.class);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Dado um determinado cep, faz a busca do {@link Endereco}
	 * 
	 * @param cep
	 * @return {@link Endereco}
	 * @throws IOException
	 */
	public static Endereco buscaEnderecoPorCEP(String cep) {

		String urlString = String.format("http://viacep.com.br/ws/%s/json", cep);

		try {

			String jsonString = buscaUrl(urlString);

			return new GsonBuilder().create().fromJson(jsonString, Endereco.class);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Dado uma determinada url, faz a busca na api e retorna uma string com
	 * valores(json ou xml)
	 * 
	 * @param urlString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static String buscaUrl(String urlString) throws MalformedURLException, IOException {

		URL url = new URL(urlString);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

		StringBuilder string = new StringBuilder();

		in.lines().forEach(l -> string.append(l.trim()));

		in.close();

		return string.toString();
	}

	/**
	 * Dado um determinado ano, faz a buscas dos {@link Feriado}
	 * 
	 * @param ano
	 * @return
	 */
	public static Feriado buscaFeriadosPorAno(String data) {
		
		if(data.length() == 10) 
			data = data.split("/")[2];

		String urlString = String.format("http://services.sapo.pt/Holiday/GetNationalHolidays?year=%s", data);

		try {

			String xmlString = buscaUrl(urlString);

			JSONObject paisesJson = XML.toJSONObject(xmlString);
			JSONObject getNationalHolidaysResponse = (JSONObject) paisesJson.get("GetNationalHolidaysResponse");
			Object getNationalHolidaysResult = getNationalHolidaysResponse.get("GetNationalHolidaysResult");
			
			Feriado feriado = new GsonBuilder().create().fromJson(getNationalHolidaysResult.toString(), Feriado.class);
			
			Map<String, Holiday> feriados = feriado.getHoliday()
	                .stream()
	                .collect(Collectors.toMap(holiday -> holiday.getDateFormatado(), holiday -> holiday, (first, second) -> first, LinkedHashMap::new));
			feriado.setMapaDataFeriado(feriados);

			return feriado;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Dado um determinado cep, faz a validação
	 * 
	 * @param cep
	 * @return
	 */
	public static boolean isCEPValido(String cep) {
		String padrao = "\\d{5}\\d{3}";
		return cep.matches(padrao);
	}

	/**
	 * Dado um determinado cpf, faz a validação dele
	 * 
	 * @param cpf
	 * @return true valido, false invalido
	 */
	public static boolean isCPFValido(String cpf) {

		CPFValidator cpfValidator = new CPFValidator();
		List<ValidationMessage> erros = cpfValidator.invalidMessagesFor(cpf);

		if (erros.size() > 0)
			return false;

		return true;
	}

	public static void main(String[] args) throws IOException {

		Feriado buscaFeriadosPorAno = buscaFeriadosPorAno("2020");
		
		Map<String, Holiday> collect = buscaFeriadosPorAno.getHoliday().stream().collect(Collectors.toMap(h -> h.getDateFormatado(), h -> h));
		
		System.out.println(buscaFeriadosPorAno);
		System.out.println(collect);
	}
}