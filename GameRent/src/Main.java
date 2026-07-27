import GameRent.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        // ===================== 1. CADASTROS =====================

        SistemaLoja loja = new SistemaLoja();

        // Jogos
        JogoFisico zelda = new JogoFisico("Zelda TOTK", Plataforma.SWITCH_1, "Aventura", 3, 15.0, ClassificacaoEtaria.DOZE);
        JogoDigital cyberpunk = new JogoDigital("Cyberpunk 2077", Plataforma.PC, "RPG", "CDPR-KEY-001", 20.0, ClassificacaoEtaria.DEZOITO, 70);
        loja.adicionarJogo(zelda);
        loja.adicionarJogo(cyberpunk);

        // Clientes
        ClienteComum joao = new ClienteComum("João", "123", "joao@email.com", 15);
        ClientePremium maria = new ClientePremium("Maria", "456", "maria@email.com", 25);
        loja.adicionarCliente(joao);
        loja.adicionarCliente(maria);

        // ===================== 2. ABRIR CAIXA =====================

        Caixa caixa = new Caixa(100.0, LocalDate.now());

        // ===================== 3. TESTAR VALIDAÇÃO DE IDADE =====================

        System.out.println("=== TESTE: VALIDAÇÃO DE IDADE ===");
        try {
            new Locacao(joao, cyberpunk, 3, LocalDate.now());
        } catch (IllegalArgumentException e) {
            System.out.println(">> Correto: " + e.getMessage());
        }

        // ===================== 4. FILTROS (Relatorio) =====================

        System.out.println("\n=== TESTE: FILTROS ===");
        Relatorio rel = new Relatorio();
        System.out.println("Busca por gênero 'Aventura': "
                + rel.buscaPorGenero(loja.getJogosCadastrados(), "Aventura").size() + " jogo(s)");

        // ===================== 5. ALUGUEL NORMAL =====================

        System.out.println("\n=== TESTE: LOCAÇÃO NORMAL ===");
        Locacao loc1 = new Locacao(maria, zelda, 5, LocalDate.of(2026, 7, 20));
        loja.registrarLocacao(loc1);
        System.out.println("Locação criada: " + loc1.getStatus());

        // ===================== 6. LOCAÇÕES ATIVAS =====================

        System.out.println("\n=== TESTE: LOCAÇÕES ATIVAS ===");
        System.out.println("Locações ativas da Maria: "
                + rel.locacoesAtivas(loja.getLocacoesRegistradas(), maria).size());

        // ===================== 7. DEVOLUÇÃO NO PRAZO =====================

        System.out.println("\n=== TESTE: DEVOLUÇÃO NO PRAZO ===");
        loc1.registrarDevolucao(LocalDate.of(2026, 7, 25));
        System.out.println("Status: " + loc1.getStatus());
        System.out.println("Valor pago: R$ " + loc1.getValorPago());

        Transacao t1 = new Transacao(loc1, LocalDate.of(2026, 7, 25));
        caixa.registrarTransacao(t1);
        System.out.println("Transação: " + t1.getTipo() + " | R$ " + t1.getValor());

        // ===================== 8. DEVOLUÇÃO COM ATRASO =====================

        System.out.println("\n=== TESTE: DEVOLUÇÃO COM ATRASO ===");
        Locacao loc2 = new Locacao(joao, zelda, 3, LocalDate.of(2026, 7, 20));
        loja.registrarLocacao(loc2);
        loc2.registrarDevolucao(LocalDate.of(2026, 7, 28));
        System.out.println("Status: " + loc2.getStatus());
        System.out.println("Multa: R$ " + loc2.calcularMulta(LocalDate.of(2026, 7, 28)));

        Transacao t2 = new Transacao(loc2, LocalDate.of(2026, 7, 28));
        caixa.registrarTransacao(t2);
        System.out.println("Transação: " + t2.getTipo() + " | R$ " + t2.getValor());

        // ===================== 9. DEVOLUÇÃO COM DANO =====================

        System.out.println("\n=== TESTE: DEVOLUÇÃO COM DANO ===");
        Locacao loc3 = new Locacao(maria, zelda, 2, LocalDate.of(2026, 7, 20));
        loja.registrarLocacao(loc3);
        loc3.registrarDevolucao(LocalDate.of(2026, 7, 22), true);
        System.out.println("Status: " + loc3.getStatus());
        System.out.println("Valor (com taxa de dano): R$ " + loc3.getValorPago());

        Transacao t3 = new Transacao(loc3, LocalDate.of(2026, 7, 22));
        caixa.registrarTransacao(t3);
        System.out.println("Transação: " + t3.getTipo() + " | R$ " + t3.getValor());

        // ===================== 10. INDISPONIBILIDADE =====================

        System.out.println("\n=== TESTE: JOGO INDISPONÍVEL ===");
        // Esgota o estoque do Zelda (estoque = 3, já alugaram 3 unidades)
        try {
            Locacao l1 = new Locacao(maria, zelda, 5, LocalDate.now());
            Locacao l2 = new Locacao(joao,  zelda, 3, LocalDate.now());
            Locacao l3 = new Locacao(maria, zelda, 2, LocalDate.now());
            new Locacao(maria, zelda, 1, LocalDate.of(2026, 7, 20));
        } catch (IllegalStateException e) {
            System.out.println(">> Correto: " + e.getMessage());
        }

        // ===================== 11. TROCO =====================

        System.out.println("\n=== TESTE: TROCO ===");
        double troco = caixa.calcularTroco(100.0, loc3.getValorPago());
        System.out.println("Troco para R$ 100,00 pagos: R$ " + troco);

        // ===================== 12. FECHAR CAIXA E RELATÓRIO =====================

        System.out.println("\n=== TESTE: FECHAMENTO ===");
        caixa.fecharCaixa(LocalDate.now());
        System.out.println(rel.gerarRelatorioFechamento(caixa));

        // ===================== 13. TOP JOGOS =====================

        System.out.println("\n=== TESTE: RANKING ===");
        System.out.println("Top 3 jogos mais alugados: "
                + rel.topJogosMaisAlugados(loja.getJogosCadastrados(), 3));

        // ===================== 14. FIDELIDADE =====================

        System.out.println("\n=== TESTE: FIDELIDADE ===");
        System.out.println("Pontos da Maria: " + maria.getFidelidade().getPontos());
        System.out.println("Pode resgatar locação grátis? "
                + maria.getFidelidade().podeResgatarLocacaoGratis());
    }
}