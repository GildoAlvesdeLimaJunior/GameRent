package GameRent;

public class JogoFisico extends Jogo{
	private int qtdEstoque;
	private int qtdAlugado = 0;

	public JogoFisico(String nome, Plataforma plataforma, String genero, int qtdEstoque, double valorDiario, ClassificacaoEtaria classificacao){
		super(nome,plataforma,genero,valorDiario,classificacao);

		this.qtdEstoque = qtdEstoque;
	}

	@Override
	public boolean isDisponivel() {
		if(qtdEstoque > qtdAlugado){
			return true;
		}else{
			return false;
		}
	}

	public boolean reservarUnidade(){
		if(qtdEstoque > qtdAlugado){
			qtdAlugado++;
			return true;
		}else{
			return false;
		}
	}

	public boolean liberarUnidade(){
		if(qtdAlugado > 0){
			qtdAlugado--;
			return true;
		}else{
			return false;
		}
	}

	public int getQntdEstoque() {
		return qtdEstoque;
	}
}
