package GameRent;

public abstract class Jogo {
	private String nome;
	private String plataforma;
	private String genero;
	private int valordiario;
	private int classificacao;
	private int contador;

	public Jogo(String nome, String plataforma, String genero, int valordiario, int classificacao, int contador){
		this.nome = nome;
		this.plataforma = plataforma;
		this.genero = genero;
		this.valordiario = valordiario;
		this.classificacao = classificacao;
		this.contador = contador;
	}

	public String getNome() {
		return nome;
	}

	public String getPlataforma() {
		return plataforma;
	}

	public String getGenero() {
		return genero;
	}

	public int getValordiario() {
		return valordiario;
	}

	public int getClassificacao() {
		return classificacao;
	}

	public int getContador() {
		return contador;
	}
}
