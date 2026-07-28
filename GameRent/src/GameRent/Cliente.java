package GameRent;

public abstract class Cliente {
	private String nome;
	private String cpf;
	private String email;
	private int idade;
	private Fidelidade fidelidade;
	public Cliente(String nome, String cpf, String email, int idade){
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.idade = idade;

		fidelidade = new Fidelidade(this);
	}

	public Fidelidade getFidelidade(){
		return fidelidade;
	}

	public String getNome() {
		return nome;
	}

	public String getCpf() {
		return cpf;
	}

	public String getEmail() {
		return email;
	}

	public int getIdade() {
		return idade;
	}

	@Override
	public String toString() {
		return "Cliente: {" + "nome= " + nome + ", cpf= " + cpf + ", email= " + email + ", idade= " + idade + ", pontos de fidelidade= " + fidelidade.getPontos() + "}\n";
	}

	public abstract double calcularDesconto(double valor);
}