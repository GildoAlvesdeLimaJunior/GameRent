package GameRent;

public class ClientePremium extends Cliente{
    private double percentualDesconto = 0.25;

    public ClientePremium(String nome, String cpf, String email, int idade) {
        super(nome, cpf, email, idade);
    }

    public double calcularDesconto(double valor){
        return valor * percentualDesconto;
    }
}
