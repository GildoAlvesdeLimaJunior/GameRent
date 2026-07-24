package GameRent;
import java.time.LocalDate; // biblioteca de data
import java.time.temporal.ChronoUnit; // calcular a diferença de dias

// Locacao. (Une as entidades Cliente, Jogo e integra regras com Fidelidade)
// em termos mais simples, basicamente atribui um jogo a uma pessoa e joga as regras de negocio

public class Locacao {
	private Cliente cliente;
	private Jogo jogo;
	private LocalDate dataInicio;
	private LocalDate dataPrevistaParaEntrega;
	private int prazoDias;
	private StatusLocacao status;
	private double valorPago;
	private boolean danificado;
	private double multaDanificado = 20; 	// multa por danificação (sujeito a alterações posteriores)
	private double multaAtraso = 5; // multa por atraso (sujeito a alterações posteriores)
	private boolean bonusFidelidade = false; // Se esse campo for true o valor base será sempre 0.0 por causa do bonus de fidelidade do cliente

	public Locacao(Cliente cliente ,Jogo jogo, int prazoDias) {
		this.cliente = cliente;
		this.jogo = jogo;
		this.prazoDias = prazoDias;
		this.dataInicio = LocalDate.now(); 								// aqui vai cravar o dia que alugou
		this.dataPrevistaParaEntrega = dataInicio.plusDays(prazoDias);	// aqui crava o dia que tem que devolver
		this.status = StatusLocacao.ATIVO;
		this.jogo.incrementarContador(); // aqui a gente ta subindo o contador de alugueis do jogo para o ranking
		if (jogo instanceof JogoFisico) {
	        ((JogoFisico) jogo).reservarUnidade();
	    } else if (jogo instanceof JogoDigital) {
	        ((JogoDigital) jogo).reservarUnidade();
	    }
	    this.bonusFidelidade=cliente.getFidelidade().podeResgatarLocacaoGratis();
		if(bonusFidelidade){
			cliente.getFidelidade().resgatarLocacaoGratis();
		}
	}
	public boolean getBonusFidelidade(){
		return this.bonusFidelidade;
	}
	// valor base pode ser resgatado tanto para o cliente comum quanto para o cliente premium, o que muda, é para o cliente premium
	public double calcularValorBase(){
		double valorBase = this.jogo.getValorDiario()* this.prazoDias;
		//if(bonusFidelidade){
		//	return 0.0;
		//}
		return valorBase;
	}
	// Faz realmente sentindo deixar o calculo de desconto aqui? por que tecnicamente, isso nao pertence de forma exclusiva para o cliente premium?
	public double calcularDesconto(){
		double val=  cliente.calcularDesconto(calcularValorBase()) + calcularPontosBonus() ;
		if(val>calcularValorBase()){
			return calcularValorBase();
		}
		return val;
	}
	
	public double calcularPontosBonus(){
		if(bonusFidelidade){
			return calcularValorBase();
		}
		return 0.0;
	}	
	
	public double calcularValorComDesconto(){
		double val =  calcularValorBase() - calcularDesconto();
		if(val>=0.0){
			return val;
		}
		return 0.0;
	}
	
	
	public double calcularMulta(){
		LocalDate dataDevolucao = LocalDate.now(); // Entregado, agora veremos se esta tudo certo ou não.
		if(dataDevolucao.isAfter(dataPrevistaParaEntrega)) { // Testa pra ver se está atrasado
			double diasAtrasados = ChronoUnit.DAYS.between(dataPrevistaParaEntrega, dataDevolucao); // calculo da diferença de dias
			this.status = StatusLocacao.ATRASADO;
			return diasAtrasados * multaAtraso; // calculo final
		}
		return 0; // caso nao esteja atrasado
	}
	
	public double calcularTaxaDano(){
		if(danificado) {
			return multaDanificado;// calcular primeiro a multa por danos
		}
		return 0; // caso nao esteja danificado
	}
	
	public double calcularValorFinal(){
		double valorFinal = calcularValorComDesconto() + calcularMulta() + calcularTaxaDano();
		return  valorFinal;
	}

	public double registrarDevolucao(){
		this.status = StatusLocacao.DEVOLVIDO;

		if (jogo instanceof JogoFisico) {
			((JogoFisico) jogo).liberarUnidade();
		}

		cliente.getFidelidade().acumularPonto();
		return calcularValorFinal();
	}

	public double registrarDevolucao(boolean arranhado){
		if (!(jogo instanceof JogoFisico)) {
			return registrarDevolucao();
		}
		this.danificado = true;
		this.status = StatusLocacao.DEVOLVIDO;
		((JogoFisico) jogo).liberarUnidade();
		return calcularValorFinal();
	}
	
	public Cliente getCliente() {
	    return cliente;
	}

	public Jogo getJogo() {
	    return jogo;
	}

	public StatusLocacao getStatus() {
	    return status;
	}

	public LocalDate getDataInicio() {
	    return dataInicio;
	}

	public LocalDate getDataPrevistaParaEntrega() {
	    return dataPrevistaParaEntrega;
	}

	public double getValorPago(){ return valorPago;}
}
