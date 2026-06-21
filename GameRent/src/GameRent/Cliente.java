package GameRent;

public abstract class Cliente {
	private String nome;
	private String cpf;
	private String email;
	private int idade;
	
	public Cliente(String nome, String cpf, String email, int idade){
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.idade = idade;
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
		return "Cliente: {" +
				"nome='" + nome + '\'' +
				", cpf='" + cpf + '\'' +
				", email='" + email + '\'' +
				", idade=" + idade +
				'}';
	}
}
