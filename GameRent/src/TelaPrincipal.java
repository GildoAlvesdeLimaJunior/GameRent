import GameRent.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class TelaPrincipal extends JFrame {

    private SistemaLoja loja;
    private Caixa caixa;
    private Relatorio relatorio;

    // Fontes Personalizadas
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONTE_TEXTO = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    // Cores do Tema Customizado
    private static final Color COR_PRIMARIA = new Color(30, 41, 59);   // Slate Dark
    private static final Color COR_ACENTO = new Color(14, 116, 144);   // Ocean Blue
    private static final Color COR_FUNDO = new Color(248, 250, 252);   // Light Gray

    // Tabelas e Componentes
    private DefaultTableModel modeloTabelaJogos;
    private DefaultTableModel modeloTabelaClientes;
    private DefaultTableModel modeloTabelaLocacoes;
    private JTextArea txtCaixaRelatorio;
    private JLabel lblSaldoCaixa;

    public TelaPrincipal() {
        // Inicializa o Backend
        loja = new SistemaLoja();
        caixa = new Caixa(100.0, LocalDate.now());
        relatorio = new Relatorio();

        // Estilo Global da Interface
        configurarAparencia();

        setTitle("GameRent - Gestão de Locadora");
        setSize(950, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(FONTE_BOLD);

        abas.addTab("🎮 Jogos", criarPainelJogos());
        abas.addTab("👥 Clientes", criarPainelClientes());
        abas.addTab("📋 Locações", criarPainelLocacoes());
        abas.addTab("💰 Caixa & Financeiro", criarPainelCaixa());

        add(abas);

        // Carga Inicial dos Dados
        carregarDadosIniciais();
        atualizarExibicaoCaixa();
    }

    private void configurarAparencia() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

    // ==========================================
    // 1. PAINEL DE JOGOS (Físico + Digital)
    // ==========================================
    private JPanel criarPainelJogos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] colunas = {"Título", "Plataforma", "Gênero", "Diária (R$)", "Classificação", "Tipo", "Detalhes/Estoque"};
        modeloTabelaJogos = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabelaJogos);
        tabela.setFont(FONTE_TEXTO);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Formulário de Cadastro
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COR_ACENTO), "Cadastrar Novo Jogo", 0, 0, FONTE_TITULO, COR_PRIMARIA));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtTitulo = new JTextField(10);
        JComboBox<Plataforma> cbPlataforma = new JComboBox<>(Plataforma.values());
        JTextField txtGenero = new JTextField(8);
        JTextField txtDiaria = new JTextField(5);
        JComboBox<ClassificacaoEtaria> cbClassificacao = new JComboBox<>(ClassificacaoEtaria.values());

        JRadioButton rbFisico = new JRadioButton("Físico", true);
        JRadioButton rbDigital = new JRadioButton("Digital");
        ButtonGroup bgTipo = new ButtonGroup();
        bgTipo.add(rbFisico);
        bgTipo.add(rbDigital);

        // Campos Específicos
        JLabel lblQtd = new JLabel("Qtd Estoque:");
        JTextField txtQtd = new JTextField("1", 5);

        JLabel lblChave = new JLabel("Chave Acesso:");
        JTextField txtChave = new JTextField(8);
        lblChave.setVisible(false);
        txtChave.setVisible(false);

        JLabel lblGb = new JLabel("Tamanho (GB):");
        JTextField txtGb = new JTextField("50", 4);
        lblGb.setVisible(false);
        txtGb.setVisible(false);

        JLabel lblLimite = new JLabel("Acessos Simultan.:");
        JTextField txtLimite = new JTextField("1", 4);
        lblLimite.setVisible(false);
        txtLimite.setVisible(false);

        // Alternar campos dinamicamente entre Físico / Digital
        rbFisico.addActionListener(e -> {
            lblQtd.setVisible(true); txtQtd.setVisible(true);
            lblChave.setVisible(false); txtChave.setVisible(false);
            lblGb.setVisible(false); txtGb.setVisible(false);
            lblLimite.setVisible(false); txtLimite.setVisible(false);
        });

        rbDigital.addActionListener(e -> {
            lblQtd.setVisible(false); txtQtd.setVisible(false);
            lblChave.setVisible(true); txtChave.setVisible(true);
            lblGb.setVisible(true); txtGb.setVisible(true);
            lblLimite.setVisible(true); txtLimite.setVisible(true);
        });

        // Linha 1
        gbc.gridx = 0; gbc.gridy = 0; painelForm.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; painelForm.add(rbFisico, gbc);
        gbc.gridx = 2; painelForm.add(rbDigital, gbc);

        gbc.gridx = 3; painelForm.add(new JLabel("Título:"), gbc);
        gbc.gridx = 4; painelForm.add(txtTitulo, gbc);

        // Linha 2
        gbc.gridx = 0; gbc.gridy = 1; painelForm.add(new JLabel("Plataforma:"), gbc);
        gbc.gridx = 1; painelForm.add(cbPlataforma, gbc);

        gbc.gridx = 2; painelForm.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 3; painelForm.add(txtGenero, gbc);

        gbc.gridx = 4; painelForm.add(new JLabel("Diária R$:"), gbc);
        gbc.gridx = 5; painelForm.add(txtDiaria, gbc);

        // Linha 3
        gbc.gridx = 0; gbc.gridy = 2; painelForm.add(new JLabel("Classificação:"), gbc);
        gbc.gridx = 1; painelForm.add(cbClassificacao, gbc);

        // Dinâmicos Físico
        gbc.gridx = 2; painelForm.add(lblQtd, gbc);
        gbc.gridx = 3; painelForm.add(txtQtd, gbc);

        // Dinâmicos Digital
        gbc.gridx = 2; painelForm.add(lblChave, gbc);
        gbc.gridx = 3; painelForm.add(txtChave, gbc);
        gbc.gridx = 4; painelForm.add(lblGb, gbc);
        gbc.gridx = 5; painelForm.add(txtGb, gbc);

        // Botão Cadastrar
        JButton btnCadastrar = new JButton("Salvar Jogo");
        btnCadastrar.setBackground(COR_ACENTO);
        btnCadastrar.setFont(FONTE_BOLD);

        btnCadastrar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText();
                Plataforma plat = (Plataforma) cbPlataforma.getSelectedItem();
                String genero = txtGenero.getText();
                double diaria = Double.parseDouble(txtDiaria.getText().replace(",", "."));
                ClassificacaoEtaria etaria = (ClassificacaoEtaria) cbClassificacao.getSelectedItem();

                if (rbFisico.isSelected()) {
                    int qtd = Integer.parseInt(txtQtd.getText());
                    JogoFisico jogo = new JogoFisico(titulo, plat, genero, qtd, diaria, etaria);
                    loja.adicionarJogo(jogo);
                    modeloTabelaJogos.addRow(new Object[]{titulo, plat, genero, diaria, etaria, "Físico", "Qtd: " + qtd});
                } else {
                    String chave = txtChave.getText();
                    int gb = Integer.parseInt(txtGb.getText());
                    int limite = Integer.parseInt(txtLimite.getText());
                    JogoDigital jogo = new JogoDigital(titulo, plat, genero, chave, diaria, etaria, gb, limite);
                    loja.adicionarJogo(jogo);
                    modeloTabelaJogos.addRow(new Object[]{titulo, plat, genero, diaria, etaria, "Digital", gb + "GB | Key: " + chave});
                }

                JOptionPane.showMessageDialog(this, "Jogo cadastrado com sucesso!");
                txtTitulo.setText(""); txtGenero.setText(""); txtDiaria.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Preencha os campos corretamente!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 6;
        painelForm.add(btnCadastrar, gbc);

        painel.add(painelForm, BorderLayout.SOUTH);
        return painel;
    }

    // ==========================================
    // 2. PAINEL DE CLIENTES (Novo Formulário)
    // ==========================================
    private JPanel criarPainelClientes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Nome", "CPF", "E-mail", "Idade", "Tipo de Cliente"};
        modeloTabelaClientes = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabelaClientes);
        tabela.setFont(FONTE_TEXTO);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Formulário de Clientes
        JPanel painelForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelForm.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COR_ACENTO), "Cadastrar Novo Cliente", 0, 0, FONTE_TITULO, COR_PRIMARIA));

        JTextField txtNome = new JTextField(10);
        JTextField txtCpf = new JTextField(8);
        JTextField txtEmail = new JTextField(10);
        JTextField txtIdade = new JTextField(3);
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Comum", "Premium"});
        JButton btnCadastrar = new JButton("Cadastrar Cliente");
        btnCadastrar.setFont(FONTE_BOLD);

        painelForm.add(new JLabel("Nome:")); painelForm.add(txtNome);
        painelForm.add(new JLabel("CPF:")); painelForm.add(txtCpf);
        painelForm.add(new JLabel("E-mail:")); painelForm.add(txtEmail);
        painelForm.add(new JLabel("Idade:")); painelForm.add(txtIdade);
        painelForm.add(new JLabel("Tipo:")); painelForm.add(cbTipo);
        painelForm.add(btnCadastrar);

        btnCadastrar.addActionListener(e -> {
            try {
                String nome = txtNome.getText();
                String cpf = txtCpf.getText();
                String email = txtEmail.getText();
                int idade = Integer.parseInt(txtIdade.getText());
                String tipo = (String) cbTipo.getSelectedItem();

                if ("Premium".equals(tipo)) {
                    ClientePremium cliente = new ClientePremium(nome, cpf, email, idade);
                    loja.adicionarCliente(cliente);
                } else {
                    ClienteComum cliente = new ClienteComum(nome, cpf, email, idade);
                    loja.adicionarCliente(cliente);
                }

                modeloTabelaClientes.addRow(new Object[]{nome, cpf, email, idade, tipo});
                JOptionPane.showMessageDialog(this, "Cliente " + nome + " cadastrado!");
                txtNome.setText(""); txtCpf.setText(""); txtEmail.setText(""); txtIdade.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos para o cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(painelForm, BorderLayout.SOUTH);
        return painel;
    }

    // ==========================================
    // 3. PAINEL DE LOCAÇÕES
    // ==========================================
    private JPanel criarPainelLocacoes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Cliente", "Jogo", "Dias", "Data Locação", "Status"};
        modeloTabelaLocacoes = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabelaLocacoes);
        tabela.setFont(FONTE_TEXTO);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        return painel;
    }

    // ==========================================
    // 4. PAINEL DE CAIXA E FINANCEIRO (Revisado)
    // ==========================================
    private JPanel criarPainelCaixa() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header com Saldo
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.setBackground(COR_PRIMARIA);
        lblSaldoCaixa = new JLabel("Saldo Atual em Caixa: R$ 0,00");
        lblSaldoCaixa.setFont(FONTE_TITULO);
        lblSaldoCaixa.setForeground(Color.WHITE);
        painelTopo.add(lblSaldoCaixa);
        painel.add(painelTopo, BorderLayout.NORTH);

        // Relatório
        txtCaixaRelatorio = new JTextArea();
        txtCaixaRelatorio.setEditable(false);
        txtCaixaRelatorio.setFont(new Font("Consolas", Font.PLAIN, 13));
        painel.add(new JScrollPane(txtCaixaRelatorio), BorderLayout.CENTER);

        // Botão de Atualizar / Fechar Caixa
        JButton btnFecharCaixa = new JButton("Fechar Caixa & Gerar Relatório");
        btnFecharCaixa.setFont(FONTE_BOLD);
        btnFecharCaixa.addActionListener(e -> {
            caixa.fecharCaixa(LocalDate.now());
            atualizarExibicaoCaixa();
            JOptionPane.showMessageDialog(this, "Caixa fechado para a data atual!");
        });

        painel.add(btnFecharCaixa, BorderLayout.SOUTH);
        return painel;
    }

    private void atualizarExibicaoCaixa() {
        if (caixa != null && relatorio != null) {
            txtCaixaRelatorio.setText(relatorio.gerarRelatorioFechamento(caixa));
            lblSaldoCaixa.setText(String.format("Saldo Atual em Caixa: R$ %.2f", caixa.getSaldoAtual()));
        }
    }

    // ==========================================
    // CARGA DE DADOS DE TESTE
    // ==========================================
    private void carregarDadosIniciais() {
        JogoFisico zelda = new JogoFisico("Zelda TOTK", Plataforma.SWITCH_1, "Aventura", 3, 15.0, ClassificacaoEtaria.DOZE);
        JogoDigital cyberpunk = new JogoDigital("Cyberpunk 2077", Plataforma.PC, "RPG", "CDPR-KEY-001", 20.0, ClassificacaoEtaria.DEZOITO, 70, 1);
        loja.adicionarJogo(zelda);
        loja.adicionarJogo(cyberpunk);

        modeloTabelaJogos.addRow(new Object[]{zelda.getNome(), zelda.getPlataforma(), zelda.getGenero(), zelda.getValorDiario(), zelda.getClassificacao(), "Físico", "Qtd: 3"});
        modeloTabelaJogos.addRow(new Object[]{cyberpunk.getNome(), cyberpunk.getPlataforma(), cyberpunk.getGenero(), cyberpunk.getValorDiario(), cyberpunk.getClassificacao(), "Digital", "70GB | Key: CDPR-KEY-001"});

        ClienteComum joao = new ClienteComum("João", "123", "joao@email.com", 15);
        ClientePremium maria = new ClientePremium("Maria", "456", "maria@email.com", 25);
        loja.adicionarCliente(joao);
        loja.adicionarCliente(maria);

        modeloTabelaClientes.addRow(new Object[]{joao.getNome(), joao.getCpf(), joao.getEmail(), joao.getIdade(), "Comum"});
        modeloTabelaClientes.addRow(new Object[]{maria.getNome(), maria.getCpf(), maria.getEmail(), maria.getIdade(), "Premium"});

        Locacao loc1 = new Locacao(maria, zelda, 5, LocalDate.of(2026, 7, 20));
        loja.registrarLocacao(loc1);
        modeloTabelaLocacoes.addRow(new Object[]{maria.getNome(), zelda.getNome(), 5, "2026-07-20", loc1.getStatus()});

        loc1.registrarDevolucao(LocalDate.of(2026, 7, 25));
        Transacao t1 = new Transacao(loc1, LocalDate.of(2026, 7, 25));
        caixa.registrarTransacao(t1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}