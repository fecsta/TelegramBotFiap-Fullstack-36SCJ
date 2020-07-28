import java.util.Scanner;


/**
 * Classe que faz o cálculo de temperatura, para analizar o status febril
 */
public class Temperatura {
    float febre;
    String error;

    public void setTemp(float valor) {
        febre = valor;
    }

    public String resultTMP() {
        String result;

        if ( febre < 35.8)
            result = "temperatura Baixa";
        else if (febre < 37)
            result = "temperatura normal";
        else if (febre < 37.5)
            result = "temperatura elevada";
        else if (febre < 38)
            result = "febre ligeira";
        else if (febre < 38.5)
            result = "febre moderada";
        else if (febre < 39.5)
            result = "febre alta";
        else if (febre < 42)
            result = "febre muito alta";
        else
            result = "Procure ** MÉDICO URGENTE **";

        return result;
    }

    public boolean check() {

            if (febre <= 0) {
                error = "Valor da temperatura deve ser positiva";
            } else if(febre <= 35.7) {
                error = "Verifique se o termômetro está marcando corretamente ";
            } else if(febre >= 42.1) {
                error = "O valor maximo do corpo sem efeitos colaterais é 42 Graus, ** PROCURE MÉDICO URGENTE **";
            } else {
                return true;
            }

        return false;
    }

    @Override
    public String toString() {
        return "A sua temperatura é de " + febre + "\nVocê está com " + this.resultTMP();
    }

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        Temperatura temperatura = new Temperatura();
        boolean check = false;

        System.out.println("Digite sua temperatura em Graus: ");
        while (!check) {
            temperatura.setTemp(entrada.nextFloat());
            check = temperatura.check();
            if (!check) {
                System.out.println(temperatura.error + "\nTente novamente:");
            }
        }

        entrada.close();
        System.out.println(temperatura);
    }
}