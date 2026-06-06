package GameRent;

public class Cliente {
	private String nome;
	private String cpf;
	private String email;
	private int idade;
	private boolean premium;
	
	public Cliente(String nome, String cpf, String email, int idade, boolean premium){
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.idade = idade;
		this.premium = premium;
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

	public boolean isPremium() {
		return premium;
	}

	@Override
	public String toString() {
		return "Cliente{" +
				"nome='" + nome + '\'' +
				", cpf='" + cpf + '\'' +
				", email='" + email + '\'' +
				", idade=" + idade +
				", premium=" + premium +
				'}';
	}
}
