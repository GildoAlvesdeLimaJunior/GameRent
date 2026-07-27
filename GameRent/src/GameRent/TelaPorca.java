package GameRent;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class TelaPorca extends JFrame {

    // Instancia tudo direto no arquivo, sem arquitetura nenhuma
    private Locadora locadora = new Locadora();

    // Variáveis globais para os campos para poder pegar em qualquer método gambiarrado
    private JTextField txtNomeCliente, txtCpfCliente, txtIdadeCliente;
    private JTextField txtNomeJogo, txtValorJogo;
    private JComboBox<String> cbTipoCliente, cbClassificacao, cbPlataforma, cbTipoJogo;
    private JTextArea txtConsoleGiga;

    public TelaPorca() {
        // Título clássico de iniciante
        setTitle("!!! SISTEMA GAMERENT v1.0 FINAL OFICIAL DEFINITIVO !!!");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout porcalhoso: FlowLayout ou Null (vamos misturar tudo num GridLayout tosco)
        setLayout(new FlowLayout());

        // --- PAINEL 1: CORES FORTES E SEM SENTIDO ---
        JPanel panelCliente = new JPanel();
        panelCliente.setBackground(Color.YELLOW); // Amarelo marca-texto
        panelCliente.setBorder(BorderFactory.createTitledBorder("CADASTRO DE CLIENTE (DIGITE CERTO)"));

        panelCliente.add(new JLabel("Nome:"));
        txtNomeCliente = new JTextField(10);
        panelCliente.add(txtNomeCliente);

        panelCliente.add(new JLabel("CPF:"));
        txtCpfCliente = new JTextField(8);
        panelCliente.add(txtCpfCliente);

        panelCliente.add(new JLabel("Idade:"));
        txtIdadeCliente = new JTextField(3);
        panelCliente.add(txtIdadeCliente);

        panelCliente.add(new JLabel("Tipo:"));
        cbTipoCliente = new JComboBox<>(new String[]{"Comum", "Premium"});
        panelCliente.add(cbTipoCliente);

        JButton btnCadCliente = new JButton("SALVAR CLIENTE!!!");
        btnCadCliente.setBackground(Color.GREEN);
        panelCliente.add(btnCadCliente);

        add(panelCliente);

        // --- PAINEL 2: OUTRA COR ALEATÓRIA ---
        JPanel panelJogo = new JPanel();
        panelJogo.setBackground(Color.CYAN);
        panelJogo.setBorder(BorderFactory.createTitledBorder("JOGOS AQUI"));

        panelJogo.add(new JLabel("Nome Jogo:"));
        txtNomeJogo = new JTextField(8);
        panelJogo.add(txtNomeJogo);

        panelJogo.add(new JLabel("Diaria:"));
        txtValorJogo = new JTextField(4);
        panelJogo.add(txtValorJogo);

        panelJogo.add(new JLabel("Tipo:"));
        cbTipoJogo = new JComboBox<>(new String[]{"Fisico", "Digital"});
        panelJogo.add(cbTipoJogo);

        panelJogo.add(new JLabel("Classificacao:"));
        cbClassificacao = new JComboBox<>(new String[]{"LIVRE", "DEZ", "DOZE", "QUATORZE", "DEZESSEIS", "DEZOITO"});
        panelJogo.add(cbClassificacao);

        panelJogo.add(new JLabel("Plataforma:"));
        cbPlataforma = new JComboBox<>(new String[]{"PS5", "PC", "XBOX_S", "SWITCH_1"});
        panelJogo.add(cbPlataforma);

        JButton btnCadJogo = new JButton("CADASTRAR JOGO");
        btnCadJogo.setBackground(Color.ORANGE);
        panelJogo.add(btnCadJogo);

        add(panelJogo);

        // --- BOTOES SOLTOS DE ALUGUEL ---
        JPanel panelAcoes = new JPanel();
        panelAcoes.setBackground(Color.MAGENTA);

        JButton btnAlugar = new JButton(">>> ALUGAR AGORA <<<");
        btnAlugar.setFont(new Font("Comic Sans MS", Font.BOLD, 16)); // Comic Sans obrigatória
        btnAcoesAdd(panelAcoes, btnAlugar);

        JButton btnDevolver = new JButton("DEVOLVER TUDO");
        panelAcoes.add(btnDevolver);

        JButton btnGeraRelatorio = new JButton("VER FATURAMENTO $$$");
        panelAcoes.add(btnGeraRelatorio);

        add(panelAcoes);

        // --- TEXTAREA GIGANTE PROS 'PRINTS' ---
        txtConsoleGiga = new JTextArea(12, 60);
        txtConsoleGiga.setBackground(Color.BLACK);
        txtConsoleGiga.setForeground(Color.GREEN); // Estilo Hacker do Matrix
        txtConsoleGiga.setText("--- LOG DO SISTEMA (NAO APAGUE) ---\n");
        JScrollPane scroll = new JScrollPane(txtConsoleGiga);
        add(scroll);

        // =========================================================================
        // GAMBIARRAS NOS EVENTOS DOS BOTÕES (TUDO COM CATCH GENÉRICO E POP-UP)
        // =========================================================================

        btnCadCliente.addActionListener(e -> {
            try {
                String nome = txtNomeCliente.getText();
                String cpf = txtCpfCliente.getText();
                int idade = Integer.parseInt(txtIdadeCliente.getText());
                
                Cliente c;
                if (cbTipoCliente.getSelectedItem().equals("Premium")) {
                    c = new ClientePremium(nome, cpf, nome + "@email.com", idade);
                } else {
                    c = new ClienteComum(nome, cpf, nome + "@email.com", idade);
                }
                
                locadora.cadastrarCliente(c);
                txtConsoleGiga.append("CLIENTE CADASTRADO: " + nome + "\n");
                JOptionPane.showMessageDialog(null, "CLIENTE SALVO COM SUCESSO DEUS ABNÇOE");
            } catch (Exception ex) {
                // Catch genérico mostrando o erro bruto na tela
                JOptionPane.showMessageDialog(null, "ERRO!!! DIGITOU ALGO ERRADO: " + ex.getMessage());
            }
        });

        btnCadJogo.addActionListener(e -> {
            try {
                String nome = txtNomeJogo.getText();
                double valor = Double.parseDouble(txtValorJogo.getText());
                ClassificacaoEtaria classif = ClassificacaoEtaria.valueOf((String) cbClassificacao.getSelectedItem());
                Plataforma plat = Plataforma.valueOf((String) cbPlataforma.getSelectedItem());

                Jogo j;
                if (cbTipoJogo.getSelectedItem().equals("Fisico")) {
                    j = new JogoFisico(nome, plat, "Ação", 5, valor, classif);
                } else {
                    j = new JogoDigital(nome, plat, "Ação", "KEY123", valor, classif, 50);
                }

                locadora.cadastrarJogo(j);
                txtConsoleGiga.append("JOGO CADASTRADO: " + nome + "\n");
                JOptionPane.showMessageDialog(null, "JOGO FOI PRO BANCO DE DADOS!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "DEU PAU NO JOGO: " + ex);
            }
        });

        btnAlugar.addActionListener(e -> {
            try {
                // Pega o primeiro cliente e o primeiro jogo que achar no sistema na tora
                Cliente c = locadora.getClientes().get(0);
                Jogo j = locadora.getJogos().get(0);

                Locacao loc = locadora.alugarJogo(c, j, 3, LocalDate.now());
                txtConsoleGiga.append("ALUGADO! Cliente: " + c.getNome() + " | Jogo: " + j.getNome() + " | Total: R$" + loc.getValorTotal() + "\n");
                JOptionPane.showMessageDialog(null, "ALUGADO COM SUCESSO!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "NAO DEU PRA ALUGAR! MOTIVO: " + ex.getMessage());
            }
        });

        btnGeraRelatorio.addActionListener(e -> {
            txtConsoleGiga.append("\n=== RELATORIO DE FATURAMENTO ===\n");
            txtConsoleGiga.append("TOTAL GANHO: R$ " + locadora.faturamentoTotal() + "\n");
            JOptionPane.showMessageDialog(null, "Faturamento atual: R$ " + locadora.faturamentoTotal());
        });
    }

    private void btnAcoesAdd(JPanel p, JButton b) {
        p.add(b);
    }

    // Main rodando direto dentro da própria classe da tela
    public static void main(String[] args) {
        TelaPorca t = new TelaPorca();
        t.setVisible(true);
    }
}