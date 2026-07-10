package GameRent;

public abstract class Jogo {
	private String nome;
	private String genero;
	private int valorDiario;
	private int contador = 0;
	private Plataforma plataforma;
	private ClassificacaoEtaria classificacao;

	public Jogo(String nome, Plataforma plataforma, String genero, int valorDiario, ClassificacaoEtaria classificacao){
		this.nome = nome;
		this.plataforma = plataforma;
		this.genero = genero;
		this.valorDiario = valorDiario;
		this.classificacao = classificacao;
	}

	public abstract boolean isDisponivel();

	public void incrementarContador() {
		this.contador++;
	}

	public String getNome() {
		return nome;
	}

	public Plataforma getPlataforma() {
		return plataforma;
	}

	public String getGenero() {
		return genero;
	}

	public int getValorDiario() {
		return valorDiario;
	}

	public ClassificacaoEtaria getClassificacao() {
		return classificacao;
	}

	public int getContador() {
		return contador;
	}
}
