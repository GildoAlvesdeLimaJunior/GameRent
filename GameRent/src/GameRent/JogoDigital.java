package GameRent;

public class JogoDigital extends Jogo {
	private String chaveAcesso;
	private int tamanhoGB;

	public JogoDigital(String nome, String plataforma, String genero, String chaveAcesso, int valorDiario, int classificacao, int tamanhoGB){
		super(nome,plataforma,genero,valorDiario,classificacao);

		this.chaveAcesso = chaveAcesso;
		this.tamanhoGB = tamanhoGB;
	}
	public String getChaveAcesso() {
	    return chaveAcesso;
	}

	public int getTamanhoGB() {
	    return tamanhoGB;
	}
}
