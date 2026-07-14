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
	private double multaAtraso = 5; 		// multa por atraso (sujeito a alterações posteriores)
	
	public Locacao(Cliente cliente ,Jogo jogo, int prazoDias) {
		this.cliente = cliente;
		this.jogo = jogo;
		this.prazoDias = prazoDias;
		this.cliente.setJogoAlugado(jogo);
		this.cliente.setPrazoDias(prazoDias);
		this.dataInicio = LocalDate.now(); 								// aqui vai cravar o dia que alugou
		this.dataPrevistaParaEntrega = dataInicio.plusDays(prazoDias);	// aqui crava o dia que tem que devolver
		this.status = StatusLocacao.ATIVO;
    }

	// valor base pode ser resgatado tanto para o cliente comum quanto para o cliente premium, o que muda, é para o cliente premium
	public double calcularValorBase(){
		double valorBase = this.jogo.getValorDiario()* this.prazoDias;
		return valorBase;
	}
	// Faz realmente sentindo deixar o calculo de desconto aqui? por que tecnicamente, isso nao pertence de forma exclusiva para o cliente premium?
	public double calcularDesconto(){
		return cliente.calcularDesconto(calcularValorBase());
	}

	public double calcularValorComDesconto(){
		return calcularValorBase() - calcularDesconto();
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
		this.cliente.setJogoAlugado(null);
		this.status = StatusLocacao.DEVOLVIDO;
		return calcularValorFinal();
	}

	public double registrarDevolucao(boolean arranhado){ // sobrecarga de funcoes, se essa for escolhida entao a midia é fisica
		this.danificado = true;
		this.status = StatusLocacao.DEVOLVIDO;
		this.cliente.setJogoAlugado(null);
		return calcularValorFinal();
	}
}