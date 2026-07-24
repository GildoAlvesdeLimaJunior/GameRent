package GameRent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Locacao {

    private Cliente cliente;
    private Jogo jogo;
    private LocalDate dataInicio;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private int diasAlugados;
    private double valorTotal;
    private double valorPago;
    private StatusLocacao status;
    private boolean danificado;
    private static final double TAXA_MULTA_DIARIA = 2.00; 
    private static final double TAXA_DANO = 50.00;         

    public Locacao(Cliente cliente, Jogo jogo, int diasAlugados, LocalDate dataInicio) {
        validarIdade(cliente, jogo);
		if(!jogo.isDisponivel()){
			throw new IllegalStateException("O jogo '"+ jogo.getNome() + "' não esta disponivel para locação");
		}
		this.cliente = cliente;
        this.jogo = jogo;
        this.diasAlugados = diasAlugados;
        this.dataInicio = dataInicio;
        this.dataPrevistaDevolucao = dataInicio.plusDays(diasAlugados);
        this.valorTotal = jogo.getValorDiario() * diasAlugados; 
        this.valorPago = 0.0;
        this.status = StatusLocacao.ATIVO;
        this.danificado = false;
		if(jogo instanceof JogoFisico){
			((JogoFisico) jogo).reservarUnidade();
		}
		else if(jogo instanceof JogoDigital){
			((JogoDigital) jogo).reservarUnidade();
		}
		jogo.incrementarContador();
    }
	public static void validarIdade(Cliente cliente, Jogo jogo){
		if(cliente.getIdade() < jogo.getClassificacao().getIdadeMinima()){
			throw new IllegalArgumentException(
				"Cliente com " + cliente.getIdade() + " anos não possui idade mínima necessária (" 
                + jogo.getClassificacao().getIdadeMinima() + " anos) para o jogo " + jogo.getNome()
			);
		}
	}

    public void registrarDevolucao(LocalDate dataDevolucao) {
        registrarDevolucao(dataDevolucao, false);
    }

    public void registrarDevolucao(LocalDate dataDevolucao, boolean danificado) {
        if(this.status != StatusLocacao.ATIVO){
			return;
		}
		
		this.dataDevolucao = dataDevolucao;
        this.danificado = danificado;

        if (danificado) {
            this.status = StatusLocacao.DEVOLVIDO_COM_DANOS;
        } 
		else if (dataDevolucao.isAfter(dataPrevistaDevolucao)) {
            this.status = StatusLocacao.DEVOLVIDO_COM_ATRASO;
        } 
		else {
            this.status = StatusLocacao.DEVOLVIDO;
        }
		if(!atrasado && !danificado){
			this.cliente.getFidelidade().acumularPonto();
		}
        this.valorPago = calcularValorFinal(dataDevolucao);
    }

    public double calcularMulta(LocalDate dataDevolucao) {
        if (dataDevolucao.isAfter(dataPrevistaDevolucao)) {
            long diasAtraso = ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataDevolucao);
            return diasAtraso * TAXA_MULTA_DIARIA;
        }
        return 0.0;
    }

    public double calcularValorFinal(LocalDate dataDevolucao) {
        double total = this.valorTotal + calcularMulta(dataDevolucao);
        if (this.danificado) {
            total += TAXA_DANO;
        }
        return total;
    }

    public Cliente getCliente() { return cliente; }
    public Jogo getJogo() { return jogo; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public int getDiasAlugados() { return diasAlugados; }
    public double getValorTotal() { return valorTotal; }
    public double getValorPago() { return valorPago; }
    public StatusLocacao getStatus() { return status; }
    public boolean isDanificado() { return danificado; }
}