import java.util.Scanner;

public class IMC {
    float altura;
    float peso;
    float imc;
    String error;

    public void setAltura(float valor) {
        altura = valor;
    }

    public void setPeso(float valor) {
        peso = valor;
    }

    public void calcIMC() {
        imc = peso / (altura * altura);
    }

    public String resultIMC() {
        String result;

        if (imc < 18.5)
            result = "abaixo do peso";
        else if (imc < 25)
            result = "com o peso ideal";
        else if (imc < 30)
            result = "acima do peso";
        else if (imc < 35)
            result = "com obesidade grau I";
        else if (imc < 40)
            result = "com obesidade grau II";
        else
            result = "com obesidade grau III";

        return result;
    }

    public boolean check(String tipo) {
        if (tipo.equalsIgnoreCase("altura")) {

            if (altura <= 0) {
                error = "Valor de " + tipo + " deve ser positivo";
            } else if(altura >= 2.75) {
                error = "O homem mais alto da história foi Robert Wadlow com 2,74 metros";
            } else {
                return true;
            }

        } else if (tipo.equalsIgnoreCase("peso")) {

            if (peso <= 0) {
                error = "Valor de " + tipo + " deve ser positivo";
            }
            else if(peso > 600) {
                error = "O homem mais pesado da história foi Manuel Uribe com um peso aproximado de 600 kg";
            } else {
                return true;
            }

        }

        return false;
    }

    @Override
    public String toString() {
        return "O seu IMC é de " + imc + "\nVocê está " + this.resultIMC();
    }

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        IMC indice = new IMC();
        boolean check = false;

        System.out.println("Vamos calcular o seu IMC");

        System.out.println("Digite sua altura em metros:");
        while (!check) {
            indice.setAltura(entrada.nextFloat());
            check = indice.check("altura");
            if (!check) {
                System.out.println(indice.error + "\nTente novamente:");
            }
        }

        check = false;
        System.out.println("Digite seu peso:");
        while (!check) {
            indice.setPeso(entrada.nextFloat());
            check = indice.check("peso");
            if (!check) {
                System.out.println(indice.error + "\nTente novamente:");
            }
        }

        entrada.close();

        indice.calcIMC();
        System.out.println(indice);
    }
}