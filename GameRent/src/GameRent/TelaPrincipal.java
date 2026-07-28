import GameRent.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private SistemaLoja loja;
    private Caixa caixa;
    private Relatorio relatorio;

    // Fontes Personalizadas
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONTE_TEXTO = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    // Cores do Tema Customizado
    private static final Color COR_PRIMARIA = new Color(30, 41, 59);
    private static final Color COR_ACENTO = new Color(14, 116, 144);

    // Tabelas da Aplicação
    private DefaultTableModel modeloTabelaJogos;
    private DefaultTableModel modeloTabelaClientes;
    private DefaultTableModel modeloTabelaLocacoes;

    // Componentes de Caixa e Consultas
    private JTextArea txtCaixaRelatorio;
    private JTextArea txtConsultasArea;
    private JLabel lblSaldoCaixa;

    // Componentes Dinâmicos para a Aba de Locações
    private JComboBox<Cliente> cbLocacaoCliente;
    private JComboBox<Jogo> cbLocacaoJogo;
    private JComboBox<Locacao> cbDevolucaoLocacao;

    public TelaPrincipal() {
        // Inicializa o Domínio do Sistema
        loja = new SistemaLoja();
        caixa = new Caixa(100.0, LocalDate.now());
        relatorio = new Relatorio();

        configurarAparencia();

        setTitle("GameRent - Gestão de Locadora de Jogos");
        setSize(1020, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Aba Principal
        JTabbedPane abas = new JTabbedPane();
        abas.setFont(FONTE_BOLD);

        abas.addTab("🎮 Jogos", criarPainelJogos());
        abas.addTab("👥 Clientes", criarPainelClientes());
        abas.addTab("📋 Locações & Devoluções", criarPainelLocacoes());
        abas.addTab("💰 Caixa & Financeiro", criarPainelCaixa());
        abas.addTab("📊 Consultas & Rankings", criarPainelConsultas());

        add(abas);

        // Carga Inicial de Teste e Sincronização
        carregarDadosIniciais();
        atualizarExibicaoCaixa();
    }

    private void configurarAparencia() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

    // ==========================================
    // 1. PAINEL DE JOGOS (Físico e Digital)
    // ==========================================
    private JPanel criarPainelJogos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Título", "Plataforma", "Gênero", "Diária (R$)", "Classificação", "Tipo", "Detalhes / Disponibilidade"};
        modeloTabelaJogos = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabelaJogos);
        tabela.setFont(FONTE_TEXTO);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

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

        JLabel lblQtd = new JLabel("Qtd Estoque:");
        JTextField txtQtd = new JTextField("1", 4);

        JLabel lblChave = new JLabel("Chave Acesso:");
        JTextField txtChave = new JTextField(8);
        lblChave.setVisible(false); txtChave.setVisible(false);

        JLabel lblGb = new JLabel("Tamanho (GB):");
        JTextField txtGb = new JTextField("50", 4);
        lblGb.setVisible(false); txtGb.setVisible(false);

        JLabel lblLimite = new JLabel("Acessos Simultâneos:");
        JTextField txtLimite = new JTextField("5", 4);
        lblLimite.setVisible(false); txtLimite.setVisible(false);

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

        gbc.gridx = 0; gbc.gridy = 0; painelForm.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; painelForm.add(rbFisico, gbc);
        gbc.gridx = 2; painelForm.add(rbDigital, gbc);
        gbc.gridx = 3; painelForm.add(new JLabel("Título:"), gbc);
        gbc.gridx = 4; painelForm.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelForm.add(new JLabel("Plataforma:"), gbc);
        gbc.gridx = 1; painelForm.add(cbPlataforma, gbc);
        gbc.gridx = 2; painelForm.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 3; painelForm.add(txtGenero, gbc);
        gbc.gridx = 4; painelForm.add(new JLabel("Diária (R$):"), gbc);
        gbc.gridx = 5; painelForm.add(txtDiaria, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelForm.add(new JLabel("Classificação:"), gbc);
        gbc.gridx = 1; painelForm.add(cbClassificacao, gbc);
        gbc.gridx = 2; painelForm.add(lblQtd, gbc);
        gbc.gridx = 3; painelForm.add(txtQtd, gbc);
        gbc.gridx = 2; painelForm.add(lblChave, gbc);
        gbc.gridx = 3; painelForm.add(txtChave, gbc);
        gbc.gridx = 4; painelForm.add(lblGb, gbc);
        gbc.gridx = 5; painelForm.add(txtGb, gbc);

        JButton btnCadastrar = new JButton("Cadastrar Jogo");
        btnCadastrar.setBackground(COR_ACENTO);
        btnCadastrar.setFont(FONTE_BOLD);

        btnCadastrar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText().trim();
                Plataforma plat = (Plataforma) cbPlataforma.getSelectedItem();
                String genero = txtGenero.getText().trim();
                double diaria = Double.parseDouble(txtDiaria.getText().replace(",", "."));
                ClassificacaoEtaria etaria = (ClassificacaoEtaria) cbClassificacao.getSelectedItem();

                if (titulo.isEmpty() || genero.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Título e Gênero são obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (diaria <= 0) {
                    JOptionPane.showMessageDialog(this, "A diária deve ser um valor maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Jogo jogo;
                String detalhe;

                if (rbFisico.isSelected()) {
                    int qtd = Integer.parseInt(txtQtd.getText());
                    if (qtd < 0) {
                        JOptionPane.showMessageDialog(this, "Quantidade de estoque não pode ser negativa!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    jogo = new JogoFisico(titulo, plat, genero, qtd, diaria, etaria);
                    detalhe = "Físico (Estoque: " + qtd + ")";
                } else {
                    String chave = txtChave.getText().trim();
                    int gb = Integer.parseInt(txtGb.getText());
                    int limite = Integer.parseInt(txtLimite.getText());
                    if (gb <= 0 || limite <= 0) {
                        JOptionPane.showMessageDialog(this, "Tamanho (GB) e Limite de acessos devem ser positivos!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    jogo = new JogoDigital(titulo, plat, genero, chave, diaria, etaria, gb, limite);
                    detalhe = "Digital (" + gb + "GB | Max " + limite + " acessos)";
                }

                loja.adicionarJogo(jogo);
                cbLocacaoJogo.addItem(jogo);
                modeloTabelaJogos.addRow(new Object[]{jogo.getNome(), jogo.getPlataforma(), jogo.getGenero(), jogo.getValorDiario(), jogo.getClassificacao(), rbFisico.isSelected() ? "Físico" : "Digital", detalhe});

                JOptionPane.showMessageDialog(this, "Jogo '" + titulo + "' cadastrado com sucesso!");
                txtTitulo.setText(""); txtGenero.setText(""); txtDiaria.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Verifique os números preenchidos!", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 6;
        painelForm.add(btnCadastrar, gbc);

        painel.add(painelForm, BorderLayout.SOUTH);
        return painel;
    }

    // ==========================================
    // 2. PAINEL DE CLIENTES
    // ==========================================
    private JPanel criarPainelClientes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Nome", "CPF", "E-mail", "Idade", "Tipo de Cliente", "Pontos Fidelidade"};
        modeloTabelaClientes = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabelaClientes);
        tabela.setFont(FONTE_TEXTO);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
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
                String nome = txtNome.getText().trim();
                String cpf = txtCpf.getText().trim();
                String email = txtEmail.getText().trim();
                int idade = Integer.parseInt(txtIdade.getText());
                String tipo = (String) cbTipo.getSelectedItem();

                if (nome.isEmpty() || cpf.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome e CPF são obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (idade <= 0) {
                    JOptionPane.showMessageDialog(this, "Idade deve ser maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Cliente cliente = "Premium".equals(tipo) 
                    ? new ClientePremium(nome, cpf, email, idade)
                    : new ClienteComum(nome, cpf, email, idade);

                loja.adicionarCliente(cliente);
                cbLocacaoCliente.addItem(cliente);
                modeloTabelaClientes.addRow(new Object[]{cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getIdade(), tipo, cliente.getFidelidade().getPontos()});

                JOptionPane.showMessageDialog(this, "Cliente '" + nome + "' cadastrado com sucesso!");
                txtNome.setText(""); txtCpf.setText(""); txtEmail.setText(""); txtIdade.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Idade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(painelForm, BorderLayout.SOUTH);
        return painel;
    }

    // ==========================================
    // 3. PAINEL DE LOCAÇÕES & DEVOLUÇÕES
    // ==========================================
    private JPanel criarPainelLocacoes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Cliente", "Jogo", "Início", "Prev. Devolução", "Dias", "Valor Base (R$)", "Status"};
        modeloTabelaLocacoes = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabelaLocacoes);
        tabela.setFont(FONTE_TEXTO);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new GridLayout(2, 1, 5, 5));

        // Form 1: Registra Locação
        JPanel formLocacao = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        formLocacao.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COR_ACENTO), "1. Registrar Nova Locação", 0, 0, FONTE_TITULO, COR_PRIMARIA));

        cbLocacaoCliente = new JComboBox<>();
        cbLocacaoJogo = new JComboBox<>();
        JTextField txtDias = new JTextField("3", 3);
        JTextField txtDataInicio = new JTextField(LocalDate.now().toString(), 8);
        JButton btnRegistrarLocacao = new JButton("Criar Locação");
        btnRegistrarLocacao.setFont(FONTE_BOLD);

        formLocacao.add(new JLabel("Cliente:")); formLocacao.add(cbLocacaoCliente);
        formLocacao.add(new JLabel("Jogo:")); formLocacao.add(cbLocacaoJogo);
        formLocacao.add(new JLabel("Dias:")); formLocacao.add(txtDias);
        formLocacao.add(new JLabel("Data Início:")); formLocacao.add(txtDataInicio);
        formLocacao.add(btnRegistrarLocacao);

        btnRegistrarLocacao.addActionListener(e -> {
            try {
                Cliente cliente = (Cliente) cbLocacaoCliente.getSelectedItem();
                Jogo jogo = (Jogo) cbLocacaoJogo.getSelectedItem();
                int dias = Integer.parseInt(txtDias.getText());
                LocalDate inicio = LocalDate.parse(txtDataInicio.getText().trim());

                if (cliente == null || jogo == null) {
                    JOptionPane.showMessageDialog(this, "Selecione cliente e jogo!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Criação da locação no domínio (lança exceções se violar regras de negócio)
                Locacao loc = new Locacao(cliente, jogo, dias, inicio);
                loja.registrarLocacao(loc);
                cbDevolucaoLocacao.addItem(loc);

                modeloTabelaLocacoes.addRow(new Object[]{
                    cliente.getNome(), jogo.getNome(), loc.getDataInicio(),
                    loc.getDataPrevistaDevolucao(), dias, loc.getValorTotal(), loc.getStatus()
                });

                atualizarTabelaClientes();
                JOptionPane.showMessageDialog(this, "Locação registrada com sucesso!");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Regra de Negócio Violada", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data em formato inválido! Use AAAA-MM-DD", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Form 2: Registra Devolução e Gera Transação no Caixa
        JPanel formDevolucao = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        formDevolucao.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COR_ACENTO), "2. Registrar Devolução de Jogo", 0, 0, FONTE_TITULO, COR_PRIMARIA));

        cbDevolucaoLocacao = new JComboBox<>();
        JTextField txtDataDevolucao = new JTextField(LocalDate.now().toString(), 8);
        JCheckBox chkDano = new JCheckBox("Jogo Danificado (+R$ 50)");
        JButton btnRegistrarDevolucao = new JButton("Confirmar Devolução");
        btnRegistrarDevolucao.setFont(FONTE_BOLD);

        formDevolucao.add(new JLabel("Locação Ativa:")); formDevolucao.add(cbDevolucaoLocacao);
        formDevolucao.add(new JLabel("Data Devolução:")); formDevolucao.add(txtDataDevolucao);
        formDevolucao.add(chkDano);
        formDevolucao.add(btnRegistrarDevolucao);

        btnRegistrarDevolucao.addActionListener(e -> {
            try {
                Locacao loc = (Locacao) cbDevolucaoLocacao.getSelectedItem();
                if (loc == null) {
                    JOptionPane.showMessageDialog(this, "Nenhuma locação selecionada!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                LocalDate dataDev = LocalDate.parse(txtDataDevolucao.getText().trim());
                boolean danificado = chkDano.isSelected();

                // Executa devolução no domínio
                loc.registrarDevolucao(dataDev, danificado);

                // Registra transação automática no caixa se aberto
                if (caixa != null && caixa.isAberto()) {
                    Transacao t = new Transacao(loc, dataDev);
                    caixa.registrarTransacao(t);
                }

                atualizarTabelaLocacoes();
                atualizarTabelaClientes();
                atualizarExibicaoCaixa();

                JOptionPane.showMessageDialog(this, "Devolução Concluída!\nStatus: " + loc.getStatus() + "\nValor Pago Final: R$ " + String.format("%.2f", loc.getValorPago()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro na Devolução", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelInferior.add(formLocacao);
        painelInferior.add(formDevolucao);
        painel.add(painelInferior, BorderLayout.SOUTH);

        return painel;
    }

    // ==========================================
    // 4. PAINEL DE CAIXA E FINANCEIRO
    // ==========================================
    private JPanel criarPainelCaixa() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel painelConfig = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelConfig.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COR_ACENTO), "Abertura / Reabertura do Caixa", 0, 0, FONTE_TITULO, COR_PRIMARIA));

        JTextField txtDataCaixa = new JTextField(LocalDate.now().toString(), 8);
        JTextField txtValorInicial = new JTextField("100.00", 6);
        JButton btnAbrirCaixa = new JButton("Abrir Caixa");
        btnAbrirCaixa.setFont(FONTE_BOLD);

        painelConfig.add(new JLabel("Data Abertura:")); painelConfig.add(txtDataCaixa);
        painelConfig.add(new JLabel("Valor Inicial (R$):")); painelConfig.add(txtValorInicial);
        painelConfig.add(btnAbrirCaixa);

        btnAbrirCaixa.addActionListener(e -> {
            try {
                LocalDate data = LocalDate.parse(txtDataCaixa.getText().trim());
                double inicial = Double.parseDouble(txtValorInicial.getText().replace(",", "."));

                if (inicial < 0) {
                    JOptionPane.showMessageDialog(this, "Valor inicial não pode ser negativo!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                caixa = new Caixa(inicial, data);
                atualizarExibicaoCaixa();
                JOptionPane.showMessageDialog(this, "Caixa aberto com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Verifique os dados fornecidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Banner de Saldo
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.add(painelConfig, BorderLayout.NORTH);

        JPanel painelSaldo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelSaldo.setBackground(COR_PRIMARIA);
        lblSaldoCaixa = new JLabel("Saldo Atual em Caixa: R$ 0,00");
        lblSaldoCaixa.setFont(FONTE_TITULO);
        lblSaldoCaixa.setForeground(Color.WHITE);
        painelSaldo.add(lblSaldoCaixa);
        painelTopo.add(painelSaldo, BorderLayout.SOUTH);

        painel.add(painelTopo, BorderLayout.NORTH);

        // Relatório do Fechamento
        txtCaixaRelatorio = new JTextArea();
        txtCaixaRelatorio.setEditable(false);
        txtCaixaRelatorio.setFont(new Font("Consolas", Font.PLAIN, 13));
        painel.add(new JScrollPane(txtCaixaRelatorio), BorderLayout.CENTER);

        JButton btnFecharCaixa = new JButton("Fechar Caixa & Gerar Relatório de Fechamento");
        btnFecharCaixa.setFont(FONTE_BOLD);
        btnFecharCaixa.addActionListener(e -> {
            if (caixa != null && caixa.isAberto()) {
                caixa.fecharCaixa(LocalDate.now());
                atualizarExibicaoCaixa();
                JOptionPane.showMessageDialog(this, "Caixa Fechado!");
            } else {
                JOptionPane.showMessageDialog(this, "O caixa já está fechado!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        painel.add(btnFecharCaixa, BorderLayout.SOUTH);
        return painel;
    }

    // ==========================================
    // 5. PAINEL DE CONSULTAS E RELATÓRIOS
    // ==========================================
    private JPanel criarPainelConsultas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelBotoes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COR_ACENTO), "Consultas Rápidas do Sistema", 0, 0, FONTE_TITULO, COR_PRIMARIA));

        JButton btnTopJogos = new JButton("🏆 Top 3 Jogos Mais Alugados");
        JButton btnFiltroGenero = new JButton("🔍 Filtrar Jogos por Gênero");
        JButton btnLocacoesAtivas = new JButton("📋 Locações Ativas de um Cliente");

        painelBotoes.add(btnTopJogos);
        painelBotoes.add(btnFiltroGenero);
        painelBotoes.add(btnLocacoesAtivas);

        painel.add(painelBotoes, BorderLayout.NORTH);

        txtConsultasArea = new JTextArea();
        txtConsultasArea.setEditable(false);
        txtConsultasArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        painel.add(new JScrollPane(txtConsultasArea), BorderLayout.CENTER);

        // Ações das Consultas
        btnTopJogos.addActionListener(e -> {
            List<Jogo> top = relatorio.topJogosMaisAlugados(loja.getJogosCadastrados(), 3);
            StringBuilder sb = new StringBuilder("=== RANKING: TOP JOGOS MAIS ALUGADOS ===\n\n");
            int rank = 1;
            for (Jogo j : top) {
                sb.append(rank++).append("º Place: ").append(j.getNome())
                  .append(" | Aluguéis: ").append(j.getContador())
                  .append(" | Plataforma: ").append(j.getPlataforma()).append("\n");
            }
            txtConsultasArea.setText(sb.toString());
        });

        btnFiltroGenero.addActionListener(e -> {
            String genero = JOptionPane.showInputDialog(this, "Digite o gênero que deseja buscar (ex: RPG, Aventura):");
            if (genero != null && !genero.trim().isEmpty()) {
                List<Jogo> resultado = relatorio.buscaPorGenero(loja.getJogosCadastrados(), genero.trim());
                StringBuilder sb = new StringBuilder("=== RESULTADO DA BUSCA POR GÊNERO: '").append(genero).append("' ===\n\n");
                if (resultado.isEmpty()) {
                    sb.append("Nenhum jogo encontrado para este gênero.");
                } else {
                    for (Jogo j : resultado) {
                        sb.append("- ").append(j.getNome()).append(" [").append(j.getPlataforma()).append("] - Diária: R$ ").append(j.getValorDiario()).append("\n");
                    }
                }
                txtConsultasArea.setText(sb.toString());
            }
        });

        btnLocacoesAtivas.addActionListener(e -> {
            Cliente cliente = (Cliente) cbLocacaoCliente.getSelectedItem();
            if (cliente != null) {
                List<Locacao> ativas = relatorio.locacoesAtivas(loja.getLocacoesRegistradas(), cliente);
                StringBuilder sb = new StringBuilder("=== LOCAÇÕES ATIVAS DO CLIENTE: ").append(cliente.getNome()).append(" ===\n\n");
                if (ativas.isEmpty()) {
                    sb.append("Nenhuma locação ativa no momento.");
                } else {
                    for (Locacao l : ativas) {
                        sb.append("- Jogo: ").append(l.getJogo().getNome())
                          .append(" | Início: ").append(l.getDataInicio())
                          .append(" | Prev. Devolução: ").append(l.getDataPrevistaDevolucao())
                          .append(" | Status: ").append(l.getStatus()).append("\n");
                    }
                }
                txtConsultasArea.setText(sb.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Cadastre um cliente primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        return painel;
    }

    // ==========================================
    // MÉTODOS DE ATUALIZAÇÃO E AUXILIARES
    // ==========================================
    private void atualizarExibicaoCaixa() {
        if (caixa != null && relatorio != null) {
            txtCaixaRelatorio.setText(relatorio.gerarRelatorioFechamento(caixa));
            lblSaldoCaixa.setText(String.format("Saldo Atual em Caixa: R$ %.2f (%s)", caixa.getSaldoAtual(), caixa.isAberto() ? "Aberto" : "Fechado"));
        }
    }

    private void atualizarTabelaClientes() {
        modeloTabelaClientes.setRowCount(0);
        for (Cliente c : loja.getClientesCadastrados()) {
            modeloTabelaClientes.addRow(new Object[]{
                c.getNome(), c.getCpf(), c.getEmail(), c.getIdade(),
                (c instanceof ClientePremium ? "Premium" : "Comum"),
                c.getFidelidade().getPontos()
            });
        }
    }

    private void atualizarTabelaLocacoes() {
        modeloTabelaLocacoes.setRowCount(0);
        for (Locacao l : loja.getLocacoesRegistradas()) {
            modeloTabelaLocacoes.addRow(new Object[]{
                l.getCliente().getNome(), l.getJogo().getNome(), l.getDataInicio(),
                l.getDataPrevistaDevolucao(), l.getDiasAlugados(), l.getValorTotal(), l.getStatus()
            });
        }
    }

    private void carregarDadosIniciais() {
        // Popula instâncias iniciais exatamente como na demonstração do projeto
        JogoFisico zelda = new JogoFisico("Zelda TOTK", Plataforma.SWITCH_1, "Aventura", 3, 15.0, ClassificacaoEtaria.DOZE);
        JogoDigital cyberpunk = new JogoDigital("Cyberpunk 2077", Plataforma.PC, "RPG", "CDPR-KEY-001", 20.0, ClassificacaoEtaria.DEZOITO, 70, 5);
        loja.adicionarJogo(zelda);
        loja.adicionarJogo(cyberpunk);

        cbLocacaoJogo.addItem(zelda);
        cbLocacaoJogo.addItem(cyberpunk);

        modeloTabelaJogos.addRow(new Object[]{zelda.getNome(), zelda.getPlataforma(), zelda.getGenero(), zelda.getValorDiario(), zelda.getClassificacao(), "Físico", "Físico (Estoque: 3)"});
        modeloTabelaJogos.addRow(new Object[]{cyberpunk.getNome(), cyberpunk.getPlataforma(), cyberpunk.getGenero(), cyberpunk.getValorDiario(), cyberpunk.getClassificacao(), "Digital", "Digital (70GB | Key: CDPR-KEY-001)"});

        ClienteComum joao = new ClienteComum("João", "123", "joao@email.com", 15);
        ClientePremium maria = new ClientePremium("Maria", "456", "maria@email.com", 25);
        loja.adicionarCliente(joao);
        loja.adicionarCliente(maria);

        cbLocacaoCliente.addItem(joao);
        cbLocacaoCliente.addItem(maria);

        atualizarTabelaClientes();

        // Locação de Teste Inicial
        Locacao loc1 = new Locacao(maria, zelda, 5, LocalDate.of(2026, 7, 20));
        loja.registrarLocacao(loc1);
        cbDevolucaoLocacao.addItem(loc1);

        atualizarTabelaLocacoes();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
