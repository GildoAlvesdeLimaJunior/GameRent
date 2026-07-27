package GameRent;

import java.util.ArrayList;
import java.util.List;

public class SistemaLoja {
    private List<Jogo> jogosCadastrados;
    private List<Cliente> clientesCadastrados;
    private List<Locacao> locacoesRegistradas;

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
    
    public void registrarLocacao(Locacao locacao) {
        this.locacoesRegistradas.add(locacao);
    }

    public List<Jogo> getJogosCadastrados(){
        return this.jogosCadastrados;
    }
    
    public List<Cliente> getClientesCadastrados(){
        return this.clientesCadastrados;
    }

    public List<Locacao> getLocacoesRegistradas() {
        return this.locacoesRegistradas;
    }
}