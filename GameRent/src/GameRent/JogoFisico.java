package GameRent;

public class JogoFisico extends Jogo{
	private int qntdestoque;
	private int avaria;

	public JogoFisico(String nome, String plataforma, String genero, int qntdestoque, int valorDiario, int classificacao, int contador, int avaria){
		super(nome,plataforma,genero,valorDiario,classificacao,contador);

		this.qntdestoque = qntdestoque;
		this.avaria = avaria;
	}
}
