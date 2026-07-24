package GameRent;
import java.time.LocalDate;

public class Transacao {
    private TipoTransacao tipo;
    private double valor;
    private LocalDate data;
    private Locacao locacao;

    public Transacao(Locacao locacao, LocalDate data){
        if(locacao == null){
            throw new IllegalArgumentException("A locação nao pode ser nula");
        }
        this.locacao = locacao;
        this.data = data;

        if (locacao.getStatus().equals(StatusLocacao.DEVOLVIDO)){
            if(locacao.getValorPago() == 0){
                this.valor = 0;
                this.tipo = TipoTransacao.LOCACAO_GRATUITA;
            }else{
                this.valor = locacao.getValorPago();
                this.tipo = TipoTransacao.LOCACAO;
            }
        }
        else if (locacao.getStatus().equals(StatusLocacao.DEVOLVIDO_COM_ATRASO)){
            this.tipo = TipoTransacao.MULTA_ATRASO;
            this.valor = locacao.getValorPago();
        }
        else if(locacao.getStatus().equals(StatusLocacao.DEVOLVIDO_COM_DANOS)){
            this.tipo = TipoTransacao.TAXA_DANO;
            this.valor = locacao.getValorPago();
        }
        else{
            throw new IllegalArgumentException("Status de locacao inválido para transação");
        }
    }

    @Override
    public String toString(){
        return "Transação = {" + "tipo= " + tipo + ",valor= " + valor + ",data= " + data + ",locacao= " + locacao + "}";
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }

    public Locacao getLocacao() {
        return locacao;
    }
}
