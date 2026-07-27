package GameRent;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class TelaPorca extends JFrame {

    // Instância da sua classe real de gerenciamento
    private SistemaLoja sistemaLoja = new SistemaLoja();

    private JTextField txtNomeCliente, txtCpfCliente, txtIdadeCliente;
    private JTextField txtNomeJogo, txtValorJogo;
    private JComboBox<String> cbTipoCliente, cbClassificacao, cbPlataforma, cbTipoJogo;
    private JTextArea txtConsoleGiga;

    public TelaPorca() {
        setTitle("!!! SISTEMA GAMERENT v1.0 FINAL OFICIAL DEFINITIVO !!!");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // --- PAINEL CLIENTE ---
        JPanel panelCliente = new JPanel();
        panelCliente.setBackground(Color.YELLOW);
        panelCliente.setBorder(BorderFactory.createTitledBorder("CADASTRO DE CLIENTE"));

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

        // --- PAINEL JOGO ---
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

        // --- BOTÕES DE AÇÃO ---
        JPanel panelAcoes = new JPanel();
        panelAcoes.setBackground(Color.MAGENTA);

        JButton btnAlugar = new JButton(">>> ALUGAR AGORA <<<");
        btnAlugar.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        panelAcoes.add(btnAlugar);

        add(panelAcoes);

        // --- LOG DO SISTEMA ---
        txtConsoleGiga = new JTextArea(12, 60);
        txtConsoleGiga.setBackground(Color.BLACK);
        txtConsoleGiga.setForeground(Color.GREEN);
        txtConsoleGiga.setText("--- LOG DO SISTEMA ---\n");
        add(new JScrollPane(txtConsoleGiga));

        // =========================================================================
        // EVENTOS USANDO OS MÉTODOS CORRETOS DO SISTEMALOJA
        // =========================================================================

        btnCadCliente.addActionListener(e -> {
            try {
                String nome = txtNomeCliente.getText();
                String cpf = txtCpfCliente.getText();
                int idade = Integer.parseInt(txtIdadeCliente.getText());

                Cliente c = cbTipoCliente.getSelectedItem().equals("Premium")
                        ? new ClientePremium(nome, cpf, nome + "@email.com", idade)
                        : new ClienteComum(nome, cpf, nome + "@email.com", idade);

                // Método correto da sua classe SistemaLoja
                sistemaLoja.adicionarCliente(c);

                txtConsoleGiga.append("CLIENTE CADASTRADO: " + nome + "\n");
                JOptionPane.showMessageDialog(null, "CLIENTE SALVO COM SUCESSO!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "ERRO: " + ex.getMessage());
            }
        });

        btnCadJogo.addActionListener(e -> {
            try {
                String nome = txtNomeJogo.getText();
                double valor = Double.parseDouble(txtValorJogo.getText());
                ClassificacaoEtaria classif = ClassificacaoEtaria.valueOf((String) cbClassificacao.getSelectedItem());
                Plataforma plat = Plataforma.valueOf((String) cbPlataforma.getSelectedItem());

                Jogo j = cbTipoJogo.getSelectedItem().equals("Fisico")
                        ? new JogoFisico(nome, plat, "Ação", 5, valor, classif)
                        : new JogoDigital(nome, plat, "Ação", "KEY123", valor, classif, 50);

                // Método correto da sua classe SistemaLoja
                sistemaLoja.adicionarJogo(j);

                txtConsoleGiga.append("JOGO CADASTRADO: " + nome + "\n");
                JOptionPane.showMessageDialog(null, "JOGO FOI PRO BANCO DE DADOS!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "DEU PAU NO JOGO: " + ex);
            }
        });

        btnAlugar.addActionListener(e -> {
            try {
                // Pega o primeiro cliente e jogo cadastrados na tora
                Cliente c = sistemaLoja.getClientesCadastrados().get(0);
                Jogo j = sistemaLoja.getJogosCadastrados().get(0);

                Locacao loc = new Locacao(c, j, 3, LocalDate.now());
                sistemaLoja.registrarLocacao(loc);

                txtConsoleGiga.append("ALUGADO! Cliente: " + c.getNome() + " | Jogo: " + j.getNome() + " | Total: R$" + loc.getValorTotal() + "\n");
                JOptionPane.showMessageDialog(null, "ALUGADO COM SUCESSO!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "NAO DEU PRA ALUGAR! MOTIVO: " + ex.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        TelaPorca t = new TelaPorca();
        t.setVisible(true);
    }
}