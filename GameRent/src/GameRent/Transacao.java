package GameRent;
import java.time.LocalDate;

public class Transacao {
    private TipoTransacao tipo;
    private double valor;
    private LocalDate data;
    private Locacao locacao;

    public Transacao(Locacao locacao){
        this.locacao = locacao;
        this.data =  LocalDate.now();

        if (locacao.getStatus().equals(StatusLocacao.DEVOLVIDO)){
            if(locacao.getValorPago() == 0){
                this.valor = 0;
                this.tipo = TipoTransacao.LOCACAO_GRATUITA;
            }else{
                this.valor = locacao.getValorPago();
                this.tipo = TipoTransacao.LOCACAO;
            }
        }else if (locacao.getStatus().equals(StatusLocacao.DEVOLVIDO_COM_ATRASO)){
            this.tipo = TipoTransacao.MULTA_ATRASO;
            this.valor = locacao.getValorPago();
        }else if(locacao.getStatus().equals(StatusLocacao.DEVOLVIDO_COM_DANOS)){
            this.tipo = TipoTransacao.TAXA_DANO;
            this.valor = locacao.getValorPago();
        }
    }

    @Override
    public String toString(){
        return "Transaçãoo = {" + "tipo= " + tipo + ",valor= " + valor + ",data= " + data + ",locacao= " + locacao + "}";
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
