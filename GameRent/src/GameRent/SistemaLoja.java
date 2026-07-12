package GameRent;
import java.util.*;

public class SistemaLoja {
	private List<Jogo> jogosCadastrados;
	private List<Cliente> clientesCadastrados;
	//private List<Locacao> locacoesRegistradas; (preciso que a pessoa 2 termine sua parte)
	//private Caixa caixa; (tem que criar a classe caixa primeiro ne pai)
	public SistemaLoja() {
		this.clientesCadastrados = new ArrayList<>();
		this.jogosCadastrados = new ArrayList<>();
	}
	
	public void adicionarJogo(Jogo jogo) {
		this.jogosCadastrados.add(jogo);
	}
	
	public void adicionarCliente(Cliente cliente) {
		this.clientesCadastrados.add(cliente);
	}
	
	public List<Jogo> getJogosCadastrados(){
		return this.jogosCadastrados;
	}
	
	public List<Cliente> getClientesCadastrados(){
		return this.clientesCadastrados;
	}
	
	public List<Jogo> filtrarPorNome(String nome) {
	    List<Jogo> resultado = new ArrayList<>();
	    for (Jogo jogo : this.jogosCadastrados) {
	        if (jogo.getNome().equalsIgnoreCase(nome)) {
	            resultado.add(jogo);
	        }
	    }
	    return resultado;
	}
	
	public List<Jogo> buscaPorGenero(String genero){
		List<Jogo> resultado = new ArrayList<>();
		for(Jogo jogo : this.jogosCadastrados) {
			if(jogo.getGenero().equalsIgnoreCase(genero)) {
				resultado.add(jogo);
			}
		}
		return resultado;
	}
	
	public List<Jogo> buscaPorPlataforma(Plataforma plataforma) {
		List<Jogo> resultado = new ArrayList<>();
		for(Jogo jogo : this.jogosCadastrados) {
			if(jogo.getPlataforma() == plataforma) {
				resultado.add(jogo);
			}
		}
		return resultado;
	}
}
