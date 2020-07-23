/**
 * Classe que auxilia na desserialização de um json ao buscar pela previsão do tempo de determinada cidade
 */

public class Tempo {
  
  private String temp;
  private String temp_max;
  private String temp_min;
  private String feels_like;
  private String humidity;
  private String speed;
  private String name;

  public Tempo(){}

  public String getTemperatura() {
    return temp;
  }

  public String getTempMaxima() {
    return temp_max;
  }

  public String getTempMinima() {
    return temp_min;
  }

  public Object getSensacaoTermica() {
    return feels_like;
  }

  public String getHumidity() {
    return humidity;
  }

  public String getWind(){
    return speed;
  }

  public String getName(){
    return name;
  }

  @Override
  public String toString() {
    return "Previsão do tempo para: " + name + "[ Temperatura = " + temp + " °C , Sensação Térmica = " + feels_like + " °C , Máxima = " + temp_max + " °C , Mínima = " + temp_min + " °C , Umidade do Ar = " + humidity + " % , Velocidade do Vento = " + speed + " km/h ]";
  }
}