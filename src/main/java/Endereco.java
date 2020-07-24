
/**
 * Classe que auxilia na desserialização de um json ao buscar por cep
 */
public class Endereco {
	
	private String cep;
	private String logradouro;
	private String complemento;
	private String bairro;
	private String localidade;
	private String uf;
	private String unidade;
	private String ibge;
	private String gia;

	public Endereco() {	}

	public String getCep() {
		return cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public String getUf() {
		return uf;
	}

	public String getUnidade() {
		return unidade;
	}

	public String getIbge() {
		return ibge;
	}

	public String getGia() {
		return gia;
	}

	@Override
	public String toString() {
		return "Endereco [cep=" + cep + ", logradouro=" + logradouro + ", complemento=" + complemento + ", bairro="
				+ bairro + ", localidade=" + localidade + ", uf=" + uf + ", unidade=" + unidade + ", ibge=" + ibge
				+ ", gia=" + gia + "]";
	}

	public String getEnderecoFormatado() {
		
		StringBuilder enderecoFormatado = new StringBuilder();
		enderecoFormatado.append("Endereço: ").append(this.getLogradouro());
		enderecoFormatado.append("\nBairro: ").append(this.getBairro());
		enderecoFormatado.append("\nCidade: ").append(this.getLocalidade());
		enderecoFormatado.append("\nEstado: ").append(this.getUf());
		
		return enderecoFormatado.toString();
	}
}
