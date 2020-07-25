
/**
 * Classe que auxilia na desserialização de um json ao busar por cep
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
		
		StringBuilder endereco = new StringBuilder();
		endereco.append("Endereço: ").append(this.getLogradouro());
		endereco.append("\nBairro: ").append(this.getBairro());
		endereco.append("\nCidade: ").append(this.getLocalidade());
		endereco.append("\nEstado: ").append(this.getUf());
		
		return endereco.toString();
	}
}
