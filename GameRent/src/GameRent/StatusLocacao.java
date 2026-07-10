package GameRent;

public enum StatusLocacao {
    ATIVO("Ativo"),
    DEVOLVIDO("Devolvido"),
    ATRASADO("Atrasado");

    private final String descricao;

    StatusLocacao(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
