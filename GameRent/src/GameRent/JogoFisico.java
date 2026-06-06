package GameRent;

public class JogoFisico extends Jogo{
	private int qntdestoque;
	private int avaria; //a avaria varia de 1 a 5, 1 em perfeito estado e 5 muito danificado, fazendo a multa variar conforme o estado da mídia

	public JogoFisico(String nome, String plataforma, String genero, int qntdestoque, int valorDiario, int classificacao, int contador, int avaria){
		super(nome,plataforma,genero,valorDiario,classificacao,contador);

		this.qntdestoque = qntdestoque;
		this.avaria = avaria;
	}
}
