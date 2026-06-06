package GameRent;

public class JogoDigital extends Jogo {
	private String chaveAcesso;
	private int tamanhoGB;

	public JogoDigital(String nome, String plataforma, String genero, String chaveAcesso, int valorDiario, int classificacao, int contador, int tamanhoGB){
		super(nome,plataforma,genero,valorDiario,classificacao,contador);

		this.chaveAcesso = chaveAcesso;
		this.tamanhoGB = tamanhoGB;
	}
}
