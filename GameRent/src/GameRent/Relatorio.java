package GameRent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Relatorio {

    public List<Jogo> buscarPorNome(List<Jogo> jogos, String nome) {
        List<Jogo> resultado = new ArrayList<>();
        for (Jogo jogo : jogos) {
            if (jogo.getNome().equalsIgnoreCase(nome)) {
                resultado.add(jogo);
            }
        }
        return resultado;
    }

    public List<Jogo> buscaPorGenero(List<Jogo> jogos, String genero) {
        List<Jogo> resultado = new ArrayList<>();
        for (Jogo jogo : jogos) {
            if (jogo.getGenero().equalsIgnoreCase(genero)) {
                resultado.add(jogo);
            }
        }
        return resultado;
    }

    public List<Jogo> buscaPorPlataforma(List<Jogo> jogos, Plataforma plataforma) {
        List<Jogo> resultado = new ArrayList<>();
        for (Jogo jogo : jogos) {
            if (jogo.getPlataforma() == plataforma) {
                resultado.add(jogo);
            }
        }
        return resultado;
    }

    public List<Jogo> buscaPorClassificacao(List<Jogo> jogos, ClassificacaoEtaria classificacao) {
        List<Jogo> resultado = new ArrayList<>();
        for (Jogo jogo : jogos) {
            if (jogo.getClassificacao() == classificacao) {
                resultado.add(jogo);
            }
        }
        return resultado;
    }

    public List<Locacao> locacoesAtivas(List<Locacao> locacoes, Cliente cliente) {
        List<Locacao> resultado = new ArrayList<>();
        for (Locacao locacao : locacoes) {
            StatusLocacao s = locacao.getStatus();
            if (locacao.getCliente() == cliente && (s == StatusLocacao.ATIVO || s == StatusLocacao.ATRASADO)) {
                resultado.add(locacao);
            }
        }
        return resultado;
    }

    public List<Locacao> historicoLocacoes(List<Locacao> locacoes, Cliente cliente) {
        List<Locacao> resultado = new ArrayList<>();
        for (Locacao locacao : locacoes) {
            if (locacao.getCliente() == cliente) {
                resultado.add(locacao);
            }
        }
        return resultado;
    }

    public List<Jogo> topJogosMaisAlugados(List<Jogo> jogos, int topN) {
        return jogos.stream()
                .sorted(Comparator.comparingInt(Jogo::getContador).reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }

    public String gerarRelatorioFechamento(Caixa caixa) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE FECHAMENTO DE CAIXA ===\n");
        sb.append("Data Abertura: ").append(caixa.getDataAbertura()).append("\n");
        sb.append("Data Fechamento: ").append(caixa.getDataFechamento()).append("\n");
        sb.append("Status do Caixa: ").append(caixa.isAberto() ? "Aberto" : "Fechado").append("\n");
        sb.append(String.format("Saldo Inicial: R$ %.2f\n", caixa.getSaldoInicial()));
        sb.append(String.format("Saldo Final: R$ %.2f\n", caixa.getSaldoAtual()));
        sb.append(String.format("Receita Total: R$ %.2f\n", caixa.getTotalReceita()));
        sb.append("---------------------------------------\n");
        sb.append("TRANSAÇÕES REGISTRADAS:\n");

        if (caixa.getTransacoes().isEmpty()) {
            sb.append("Nenhuma transação registrada.\n");
        } else {
            for (Transacao t : caixa.getTransacoes()) {
                sb.append(String.format("- [%s] Tipo: %s | Valor: R$ %.2f | Data: %s\n",
                        t.getLocacao().getJogo().getNome(),
                        t.getTipo().getDescricao(),
                        t.getValor(),
                        t.getData()));
            }
        }
        sb.append("=======================================");
        return sb.toString();
    }
}