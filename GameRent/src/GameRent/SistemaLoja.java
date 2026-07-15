package GameRent;
import java.util.*;

public class SistemaLoja {
	private List<Jogo> jogosCadastrados;
	private List<Cliente> clientesCadastrados;
	private List<Locacao> locacoesRegistradas; 
	//private Caixa caixa; (tem que criar a classe caixa primeiro ne pai)
	public SistemaLoja() {
		this.clientesCadastrados = new ArrayList<>();
		this.jogosCadastrados = new ArrayList<>();
		this.locacoesRegistradas = new ArrayList<>();
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
	public void registrarLocacao(Locacao locacao) {
	    this.locacoesRegistradas.add(locacao);
	}

	public List<Locacao> getLocacoesRegistradas() {
	    return this.locacoesRegistradas;
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
	public List<Jogo> buscaPorClassificacao (ClassificacaoEtaria classificacao){
		List<Jogo> resultado = new ArrayList<>();
		for (Jogo jogo : this.jogosCadastrados) {
			if(jogo.getClassificacao() == classificacao ) {
				resultado.add(jogo);
			}
		}
		return resultado;
	}
	public List<Locacao> locacoesAtivas(Cliente cliente) {
	    List<Locacao> resultado = new ArrayList<>();
	    for (Locacao locacao : this.locacoesRegistradas) {
	        if (locacao.getCliente() == cliente && locacao.getStatus() == StatusLocacao.ATIVO) {
	            resultado.add(locacao);
	        }
	    }
	    return resultado;
	}
	public List<Locacao> historicoLocacoes(Cliente cliente) {
	    List<Locacao> resultado = new ArrayList<>();
	    for (Locacao locacao : this.locacoesRegistradas) {
	        if (locacao.getCliente() == cliente) {
	            resultado.add(locacao);
	        }
	    }
	    return resultado;
	}
}
