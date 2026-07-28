package GameRent;

public class JogoDigital extends Jogo {
	private String chaveAcesso;
	private int tamanhoGB;
	private int limiteAcessosSimultaneos; 
	private int acessosAtivos = 0;

	public JogoDigital(String nome, Plataforma plataforma, String genero, String chaveAcesso, double valorDiario, ClassificacaoEtaria classificacao, int tamanhoGB, int limiteAcessosSimultaneos){
		super(nome,plataforma,genero,valorDiario,classificacao);

		this.chaveAcesso = chaveAcesso;
		this.tamanhoGB = tamanhoGB;
		this.limiteAcessosSimultaneos = limiteAcessosSimultaneos; 
	}

	public boolean isDisponivel(){
		if(limiteAcessosSimultaneos > acessosAtivos){
			return true;
		}else{
			return false;
		}
	}

	public boolean reservarUnidade(){
		if(limiteAcessosSimultaneos > acessosAtivos){
			acessosAtivos++;
			return true;
		}else{
			return false;
		}
	}

	public boolean liberarUnidade(){
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
