package GameRent;
import java.time.LocalDate;
import java.util.*;

public class Caixa {
    private double saldoInicial;
    private double saldoAtual;
    private boolean aberto;
    private List<Transacao> transacoes = new ArrayList<>();
    private LocalDate dataAbertura;
    private LocalDate dataFechamento;

    public Caixa(double saldoInicial, LocalDate dataAbertura){
        this.saldoInicial = saldoInicial;
        this.saldoAtual = saldoInicial;
        this.aberto = true;
        this.dataAbertura = dataAbertura;
    }

    public List<Transacao> fecharCaixa(LocalDate dataFechamento){
        if(this.aberto) {
            this.aberto = false;
            this.dataFechamento = dataFechamento;
            return transacoes;
        }else{
            return null;
        }
    }

    public void registrarTransacao(Transacao transacao){
        if(this.aberto){
            this.saldoAtual += transacao.getValor();
            transacoes.add(transacao);
        }
    }

    public double calcularTroco(double valorPago, double totalDevido){
        return valorPago - totalDevido;
    }

    public double getTotalReceita(){
        return saldoAtual - saldoInicial;
    }

    public double getTotalDespesas(){
        double totalDespesas = 0;
        for(Transacao transacao : transacoes){
            if(transacao.getValor() < 0 ){
                totalDespesas += Math.abs(transacao.getValor());
            }
        }
        return totalDespesas;
    }
    public double getSaldoInicial() {
        return saldoInicial;
    }

    public double getSaldoAtual() {
        return saldoAtual;
    }

    public boolean isAberto() {
        return aberto;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }
}
