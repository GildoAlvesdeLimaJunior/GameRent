package GameRent;

public enum TipoTransacao {
    LOCACAO("Locação"),
    MULTA_ATRASO("Multa por atraso"),
    TAXA_DANO("Taxa de Dano"),
    LOCACAO_GRATUITA("Locação gratuita");

    private final String descricao;

    TipoTransacao(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
