package GameRent;

public enum StatusLocacao {
    ATIVO("Ativo"),
    ATRASADO("Atrasado"),
    DEVOLVIDO("Devolvido normalmente"),
    DEVOLVIDO_COM_ATRASO("Devolvido com atraso"),
    DEVOLVIDO_COM_DANOS("Devolvido com danos");

    private final String descricao;

    StatusLocacao(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
