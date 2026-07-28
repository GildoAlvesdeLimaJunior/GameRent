import GameRent.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        SistemaLoja loja = new SistemaLoja();

        JogoFisico zelda = new JogoFisico("Zelda TOTK", Plataforma.SWITCH_1, "Aventura", 3, 15.0, ClassificacaoEtaria.DOZE);
        JogoDigital cyberpunk = new JogoDigital("Cyberpunk 2077", Plataforma.PC, "RPG", "CDPR-KEY-001", 20.0, ClassificacaoEtaria.DEZOITO, 70, 6);
        loja.adicionarJogo(zelda);
        loja.adicionarJogo(cyberpunk);

        ClienteComum joao = new ClienteComum("João", "123", "joao@email.com", 15);
        ClientePremium maria = new ClientePremium("Maria", "456", "maria@email.com", 25);
        loja.adicionarCliente(joao);
        loja.adicionarCliente(maria);

        Caixa caixa = new Caixa(100.0, LocalDate.now());

        System.out.println("\n=== TESTE: FILTROS ===");
        Relatorio rel = new Relatorio();
        System.out.println("Busca por gênero 'Aventura': "
                + rel.buscaPorGenero(loja.getJogosCadastrados(), "Aventura").size() + " jogo(s)");

        System.out.println("\n=== TESTE: LOCAÇÃO NORMAL ===");
        Locacao loc1 = new Locacao(maria, zelda, 5, LocalDate.of(2026, 7, 20));
        loja.registrarLocacao(loc1);
        System.out.println("Locação criada: " + loc1.getStatus());

        System.out.println("\n=== TESTE: LOCAÇÕES ATIVAS ===");
        System.out.println("Locações ativas da Maria: "
                + rel.locacoesAtivas(loja.getLocacoesRegistradas(), maria).size());

        System.out.println("\n=== TESTE: DEVOLUÇÃO NO PRAZO ===");
        loc1.registrarDevolucao(LocalDate.of(2026, 7, 25));
        System.out.println("Status: " + loc1.getStatus());
        System.out.println("Valor pago: R$ " + loc1.getValorPago());

        Transacao t1 = new Transacao(loc1, LocalDate.of(2026, 7, 25));
        caixa.registrarTransacao(t1);
        System.out.println("Transação: " + t1.getTipo() + " | R$ " + t1.getValor());

        System.out.println("\n=== TESTE: DEVOLUÇÃO COM ATRASO ===");
        Locacao loc2 = new Locacao(joao, zelda, 3, LocalDate.of(2026, 7, 20));
        loja.registrarLocacao(loc2);
        loc2.registrarDevolucao(LocalDate.of(2026, 7, 28));
        System.out.println("Status: " + loc2.getStatus());
        System.out.println("Multa: R$ " + loc2.calcularMulta(LocalDate.of(2026, 7, 28)));

        Transacao t2 = new Transacao(loc2, LocalDate.of(2026, 7, 28));
        caixa.registrarTransacao(t2);
        System.out.println("Transação: " + t2.getTipo() + " | R$ " + t2.getValor());

        System.out.println("\n=== TESTE: DEVOLUÇÃO COM DANO ===");
        Locacao loc3 = new Locacao(maria, zelda, 2, LocalDate.of(2026, 7, 20));
        loja.registrarLocacao(loc3);
        loc3.registrarDevolucao(LocalDate.of(2026, 7, 22), true);
        System.out.println("Status: " + loc3.getStatus());
        System.out.println("Valor (com taxa de dano): R$ " + loc3.getValorPago());

        Transacao t3 = new Transacao(loc3, LocalDate.of(2026, 7, 22));
        caixa.registrarTransacao(t3);
        System.out.println("Transação: " + t3.getTipo() + " | R$ " + t3.getValor());

        System.out.println("\n=== TESTE: FECHAMENTO ===");
        caixa.fecharCaixa(LocalDate.now());
        System.out.println(rel.gerarRelatorioFechamento(caixa));

        System.out.println("\n=== TESTE: RANKING ===");
        System.out.println("Top 3 jogos mais alugados: "
                + rel.topJogosMaisAlugados(loja.getJogosCadastrados(), 3));
    }
}
