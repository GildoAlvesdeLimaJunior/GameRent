import GameRent.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class TelaPrincipal extends JFrame {

    private SistemaLoja loja;
    private Caixa caixa;
    private Relatorio relatorio;

    // Tabelas para exibição visual dos dados
    private DefaultTableModel modeloTabelaJogos;
    private DefaultTableModel modeloTabelaClientes;
    private DefaultTableModel modeloTabelaLocacoes;
    private JTextArea txtCaixaRelatorio;

    public TelaPrincipal() {
        // Inicializa o Backend
        loja = new SistemaLoja();
        caixa = new Caixa(100.0, LocalDate.now());
        relatorio = new Relatorio();

        // Configurações da Janela
        setTitle("GameRent - Sistema de Locadora de Jogos");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela

        // Criando Painel com Abas
        JTabbedPane abas = new JTabbedPane();

        abas.addTab("🎮 Jogos", criarPainelJogos());
        abas.addTab("👥 Clientes", criarPainelClientes());
        abas.addTab("📋 Locações", criarPainelLocacoes());
        abas.addTab("💰 Caixa & Relatórios", criarPainelCaixa());

        add(abas);

        // Carrega dados de teste iniciais (os mesmos do seu Main)
        carregarDadosIniciais();
    }

    // 1. Painel de Jogos
    private JPanel criarPainelJogos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        
        String[] colunas = {"Título", "Plataforma", "Gênero", "Diária (R$)", "Tipo"};
        modeloTabelaJogos = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabelaJogos);
        
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Formulário simplificado de cadastro
        JPanel painelForm = new JPanel(new FlowLayout());
        JTextField txtTitulo = new JTextField(12);
        JComboBox<Plataforma> cbPlataforma = new JComboBox<>(Plataforma.values());
        JTextField txtGenero = new JTextField(8);
        JTextField txtDiaria = new JTextField(5);
        JButton btnCadastrar = new JButton("Cadastrar Jogo Físico");

        painelForm.add(new JLabel("Título:"));
        painelForm.add(txtTitulo);
        painelForm.add(new JLabel("Plataforma:"));
        painelForm.add(cbPlataforma);
        painelForm.add(new JLabel("Gênero:"));
        painelForm.add(txtGenero);
        painelForm.add(new JLabel("Diária:"));
        painelForm.add(txtDiaria);
        painelForm.add(btnCadastrar);

        btnCadastrar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText();
                Plataforma plat = (Plataforma) cbPlataforma.getSelectedItem();
                String genero = txtGenero.getText();
                double diaria = Double.parseDouble(txtDiaria.getText());

                JogoFisico jogo = new JogoFisico(titulo, plat, genero, 1, diaria, ClassificacaoEtaria.LIVRE);
                loja.adicionarJogo(jogo);
                
                modeloTabelaJogos.addRow(new Object[]{titulo, plat, genero, diaria, "Físico"});
                
                txtTitulo.setText("");
                txtGenero.setText("");
                txtDiaria.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro nos dados preenchidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(painelForm, BorderLayout.SOUTH);
        return painel;
    }

    // 2. Painel de Clientes
    private JPanel criarPainelClientes() {
        JPanel painel = new JPanel(new BorderLayout());
        String[] colunas = {"Nome", "CPF", "Email", "Idade"};
        modeloTabelaClientes = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabelaClientes);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    // 3. Painel de Locações
    private JPanel criarPainelLocacoes() {
        JPanel painel = new JPanel(new BorderLayout());
        String[] colunas = {"Cliente", "Jogo", "Dias", "Data Locação", "Status"};
        modeloTabelaLocacoes = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabelaLocacoes);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    // 4. Painel de Caixa e Relatórios
    private JPanel criarPainelCaixa() {
        JPanel painel = new JPanel(new BorderLayout());
        txtCaixaRelatorio = new JTextArea();
        txtCaixaRelatorio.setEditable(false);
        txtCaixaRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JButton btnAtualizar = new JButton("Gerar Relatório de Fechamento do Caixa");
        btnAtualizar.addActionListener(e -> {
            txtCaixaRelatorio.setText(relatorio.gerarRelatorioFechamento(caixa));
        });

        painel.add(new JScrollPane(txtCaixaRelatorio), BorderLayout.CENTER);
        painel.add(btnAtualizar, BorderLayout.SOUTH);
        return painel;
    }

    // Carrega os dados de teste que estavam no seu Main.java
    private void carregarDadosIniciais() {
        JogoFisico zelda = new JogoFisico("Zelda TOTK", Plataforma.SWITCH_1, "Aventura", 3, 15.0, ClassificacaoEtaria.DOZE);
        JogoDigital cyberpunk = new JogoDigital("Cyberpunk 2077", Plataforma.PC, "RPG", "CDPR-KEY-001", 20.0, ClassificacaoEtaria.DEZOITO, 70);
        loja.adicionarJogo(zelda);
        loja.adicionarJogo(cyberpunk);

        modeloTabelaJogos.addRow(new Object[]{zelda.getNome(), zelda.getPlataforma(), zelda.getGenero(), zelda.getValorDiario(), "Físico"});
        modeloTabelaJogos.addRow(new Object[]{cyberpunk.getNome(), cyberpunk.getPlataforma(), cyberpunk.getGenero(), cyberpunk.getValorDiario(), "Digital"});

        ClienteComum joao = new ClienteComum("João", "123", "joao@email.com", 15);
        ClientePremium maria = new ClientePremium("Maria", "456", "maria@email.com", 25);
        loja.adicionarCliente(joao);
        loja.adicionarCliente(maria);

        modeloTabelaClientes.addRow(new Object[]{joao.getNome(), joao.getCpf(), joao.getEmail(), joao.getIdade()});
        modeloTabelaClientes.addRow(new Object[]{maria.getNome(), maria.getCpf(), maria.getEmail(), maria.getIdade()});

        Locacao loc1 = new Locacao(maria, zelda, 5, LocalDate.of(2026, 7, 20));
        loja.registrarLocacao(loc1);
        modeloTabelaLocacoes.addRow(new Object[]{maria.getNome(), zelda.getNome(), 5, "2026-07-20", loc1.getStatus()});

        loc1.registrarDevolucao(LocalDate.of(2026, 7, 25));
        Transacao t1 = new Transacao(loc1, LocalDate.of(2026, 7, 25));
        caixa.registrarTransacao(t1);

        txtCaixaRelatorio.setText(relatorio.gerarRelatorioFechamento(caixa));
    }

    public static void main(String[] args) {
        // Executa a interface gráfica na thread apropriada do Swing
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}