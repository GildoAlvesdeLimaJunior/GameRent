package GameRent;

public class JogoDigital extends Jogo {
	private String chaveAcesso;
	private int tamanhoGB;
	private int limiteAcessosSimultaneos = 5; // Exemplo
	private int acessosAtivos = 0;

	public JogoDigital(String nome, Plataforma plataforma, String genero, String chaveAcesso, int valorDiario, ClassificacaoEtaria classificacao, int tamanhoGB){
		super(nome,plataforma,genero,valorDiario,classificacao);

		this.chaveAcesso = chaveAcesso;
		this.tamanhoGB = tamanhoGB;
	}

	public boolean isDisponivel(){
		if(limiteAcessosSimultaneos > acessosAtivos){
			return true;
		}else{
			return false;
		}
	}

	public boolean reservarAcesso(){
		if(limiteAcessosSimultaneos > acessosAtivos){
			acessosAtivos++;
			return true;
		}else{
			return false;
		}
	}

	public boolean liberarAcesso(){
		if(acessosAtivos > 0){
			acessosAtivos--;
			return true;
		}else{
			return false;
		}
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public int getTamanhoGB() {
		return tamanhoGB;
	}
}
