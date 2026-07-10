package GameRent;

public class JogoFisico extends Jogo{
	private int qntdestoque;
	private int avaria;

	public JogoFisico(String nome, String plataforma, String genero, int qntdestoque, int valorDiario, int classificacao, int avaria){
		super(nome,plataforma,genero,valorDiario,classificacao);

		this.qntdestoque = qntdestoque;
		this.avaria = avaria;
	}
	public int getQntdestoque() {
	    return qntdestoque;
	}

	public int getAvaria() {
	    return avaria;
	}
}
