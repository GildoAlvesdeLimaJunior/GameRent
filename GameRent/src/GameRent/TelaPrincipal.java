import GameRent.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class TelaPrincipal extends JFrame {

    private SistemaLoja loja = new SistemaLoja();
    private Caixa caixa = new Caixa(100.0, LocalDate.now());
    private Relatorio relatorio = new Relatorio();

    private DefaultTableModel modJogos, modClientes, modLocacoes;
    private JTextArea txtCaixa = new JTextArea(), txtConsultas = new JTextArea();
    private JLabel lblSaldo = new JLabel();

    private JComboBox<Cliente> cbLocCliente = new JComboBox<>();
    private JComboBox<Jogo> cbLocJogo = new JComboBox<>();
    private JComboBox<Locacao> cbDevLocacao = new JComboBox<>();

    public TelaPrincipal() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        setTitle("GameRent - Gestão de Locadora");
        setSize(950, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("🎮 Jogos", criarPainelJogos());
        abas.addTab("👥 Clientes", criarPainelClientes());
        abas.addTab("📋 Locações & Devoluções", criarPainelLocacoes());
        abas.addTab("💰 Caixa", criarPainelCaixa());
        abas.addTab("📊 Consultas", criarPainelConsultas());
        add(abas);

        carregarDadosIniciais();
        atualizarExibicaoCaixa();
    }

    // 1. PAINEL DE JOGOS
    private JPanel criarPainelJogos() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        modJogos = new DefaultTableModel(new String[]{"Título", "Plataforma", "Gênero", "Diária (R$)", "Classificação", "Tipo", "Detalhes"}, 0);
        p.add(new JScrollPane(new JTable(modJogos)), BorderLayout.CENTER);

        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        f.setBorder(BorderFactory.createTitledBorder("Cadastrar Jogo"));

        JTextField txtTit = new JTextField(8), txtGen = new JTextField(6), txtVal = new JTextField(4), txtEstGb = new JTextField("1", 3);
        JComboBox<Plataforma> cbPlat = new JComboBox<>(Plataforma.values());
        JComboBox<ClassificacaoEtaria> cbClass = new JComboBox<>(ClassificacaoEtaria.values());
        JRadioButton rbFisico = new JRadioButton("Físico", true), rbDigital = new JRadioButton("Digital");
        ButtonGroup bg = new ButtonGroup(); bg.add(rbFisico); bg.add(rbDigital);

        JButton btnSalvar = new JButton("Salvar Jogo");
        btnSalvar.addActionListener(e -> {
            try {
                String tit = txtTit.getText().trim(), gen = txtGen.getText().trim();
                double val = Double.parseDouble(txtVal.getText().replace(",", "."));
                int num = Integer.parseInt(txtEstGb.getText());
                if (val <= 0 || num <= 0 || tit.isEmpty()) throw new IllegalArgumentException("Valores inválidos!");

                Jogo j = rbFisico.isSelected() 
                    ? new JogoFisico(tit, (Plataforma)cbPlat.getSelectedItem(), gen, num, val, (ClassificacaoEtaria)cbClass.getSelectedItem())
                    : new JogoDigital(tit, (Plataforma)cbPlat.getSelectedItem(), gen, "KEY-AUTO", val, (ClassificacaoEtaria)cbClass.getSelectedItem(), num, 5);

                loja.adicionarJogo(j);
                cbLocJogo.addItem(j);
                modJogos.addRow(new Object[]{j.getNome(), j.getPlataforma(), j.getGenero(), j.getValorDiario(), j.getClassificacao(), rbFisico.isSelected()?"Físico":"Digital", num + (rbFisico.isSelected()?" Un.":" GB")});
                txtTit.setText(""); txtGen.setText(""); txtVal.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
        });

        f.add(rbFisico); f.add(rbDigital); f.add(new JLabel("Título:")); f.add(txtTit);
        f.add(new JLabel("Plat:")); f.add(cbPlat); f.add(new JLabel("Gên:")); f.add(txtGen);
        f.add(new JLabel("Diária:")); f.add(txtVal); f.add(new JLabel("Class:")); f.add(cbClass);
        f.add(new JLabel("Estoque/GB:")); f.add(txtEstGb); f.add(btnSalvar);

        p.add(f, BorderLayout.SOUTH);
        return p;
    }

    // 2. PAINEL DE CLIENTES
    private JPanel criarPainelClientes() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        modClientes = new DefaultTableModel(new String[]{"Nome", "CPF", "E-mail", "Idade", "Tipo", "Pontos"}, 0);
        p.add(new JScrollPane(new JTable(modClientes)), BorderLayout.CENTER);

        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        f.setBorder(BorderFactory.createTitledBorder("Cadastrar Cliente"));

        JTextField txtNome = new JTextField(8), txtCpf = new JTextField(6), txtMail = new JTextField(8), txtIdade = new JTextField(3);
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Comum", "Premium"});
        JButton btnAdd = new JButton("Salvar Cliente");

        btnAdd.addActionListener(e -> {
            try {
                String n = txtNome.getText().trim(), c = txtCpf.getText().trim(), m = txtMail.getText().trim();
                int idade = Integer.parseInt(txtIdade.getText());
                if (idade <= 0 || n.isEmpty()) throw new IllegalArgumentException("Dados inválidos!");

                Cliente cli = "Premium".equals(cbTipo.getSelectedItem()) ? new ClientePremium(n, c, m, idade) : new ClienteComum(n, c, m, idade);
                loja.adicionarCliente(cli);
                cbLocCliente.addItem(cli);
                atualizarTabelaClientes();
                txtNome.setText(""); txtCpf.setText(""); txtMail.setText(""); txtIdade.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
        });

        f.add(new JLabel("Nome:")); f.add(txtNome); f.add(new JLabel("CPF:")); f.add(txtCpf);
        f.add(new JLabel("Mail:")); f.add(txtMail); f.add(new JLabel("Idade:")); f.add(txtIdade);
        f.add(new JLabel("Tipo:")); f.add(cbTipo); f.add(btnAdd);

        p.add(f, BorderLayout.SOUTH);
        return p;
    }

    // 3. PAINEL DE LOCAÇÕES & DEVOLUÇÕES
    private JPanel criarPainelLocacoes() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        modLocacoes = new DefaultTableModel(new String[]{"Cliente", "Jogo", "Início", "Dev. Prevista", "Dias", "Total (R$)", "Status"}, 0);
        p.add(new JScrollPane(new JTable(modLocacoes)), BorderLayout.CENTER);

        JPanel pForm = new JPanel(new GridLayout(2, 1, 5, 5));

        // Form Locação
        JPanel fLoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        fLoc.setBorder(BorderFactory.createTitledBorder("1. Nova Locação"));
        JTextField txtDias = new JTextField("3", 3), txtIni = new JTextField(LocalDate.now().toString(), 8);
        JButton btnLoc = new JButton("Criar Locação");

        btnLoc.addActionListener(e -> {
            try {
                Cliente c = (Cliente) cbLocCliente.getSelectedItem();
                Jogo j = (Jogo) cbLocJogo.getSelectedItem();
                Locacao loc = new Locacao(c, j, Integer.parseInt(txtDias.getText()), LocalDate.parse(txtIni.getText().trim()));
                loja.registrarLocacao(loc);
                cbDevLocacao.addItem(loc);
                atualizarTabelaLocacoes();
                atualizarTabelaClientes();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
        });

        fLoc.add(new JLabel("Cliente:")); fLoc.add(cbLocCliente); fLoc.add(new JLabel("Jogo:")); fLoc.add(cbLocJogo);
        fLoc.add(new JLabel("Dias:")); fLoc.add(txtDias); fLoc.add(new JLabel("Início:")); fLoc.add(txtIni); fLoc.add(btnLoc);

        // Form Devolução
        JPanel fDev = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        fDev.setBorder(BorderFactory.createTitledBorder("2. Registrar Devolução"));
        JTextField txtDev = new JTextField(LocalDate.now().toString(), 8);
        JCheckBox chkDano = new JCheckBox("Danificado (+R$50)");
        JButton btnDev = new JButton("Confirmar Devolução");

        btnDev.addActionListener(e -> {
            try {
                Locacao loc = (Locacao) cbDevLocacao.getSelectedItem();
                if (loc == null) return;
                LocalDate d = LocalDate.parse(txtDev.getText().trim());
                loc.registrarDevolucao(d, chkDano.isSelected());
                if (caixa != null && caixa.isAberto()) caixa.registrarTransacao(new Transacao(loc, d));
                
                atualizarTabelaLocacoes();
                atualizarTabelaClientes();
                atualizarExibicaoCaixa();
                JOptionPane.showMessageDialog(this, "Devolvido! Valor Final: R$ " + String.format("%.2f", loc.getValorPago()));
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
        });

        fDev.add(new JLabel("Locação:")); fDev.add(cbDevLocacao); fDev.add(new JLabel("Data Dev:")); fDev.add(txtDev);
        fDev.add(chkDano); fDev.add(btnDev);

        pForm.add(fLoc); pForm.add(fDev);
        p.add(pForm, BorderLayout.SOUTH);
        return p;
    }

    // 4. PAINEL DE CAIXA
    private JPanel criarPainelCaixa() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtData = new JTextField(LocalDate.now().toString(), 8), txtIni = new JTextField("100.00", 5);
        JButton btnAbrir = new JButton("Abrir/Reiniciar Caixa");

        btnAbrir.addActionListener(e -> {
            try {
                caixa = new Caixa(Double.parseDouble(txtIni.getText().replace(",", ".")), LocalDate.parse(txtData.getText().trim()));
                atualizarExibicaoCaixa();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro nos dados do caixa!"); }
        });

        topo.add(new JLabel("Data:")); topo.add(txtData); topo.add(new JLabel("Valor Inicial:")); topo.add(txtIni); topo.add(btnAbrir);
        
        JPanel pCenter = new JPanel(new BorderLayout());
        lblSaldo.setOpaque(true); lblSaldo.setBackground(new Color(30, 41, 59)); lblSaldo.setForeground(Color.WHITE);
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pCenter.add(lblSaldo, BorderLayout.NORTH);
        
        txtCaixa.setFont(new Font("Consolas", Font.PLAIN, 12)); txtCaixa.setEditable(false);
        pCenter.add(new JScrollPane(txtCaixa), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar Caixa & Gerar Relatório");
        btnFechar.addActionListener(e -> {
            if (caixa.isAberto()) { caixa.fecharCaixa(LocalDate.now()); atualizarExibicaoCaixa(); }
        });

        p.add(topo, BorderLayout.NORTH); p.add(pCenter, BorderLayout.CENTER); p.add(btnFechar, BorderLayout.SOUTH);
        return p;
    }

    // 5. PAINEL DE CONSULTAS
    private JPanel criarPainelConsultas() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTop = new JButton("🏆 Top 3 Jogos"), btnGen = new JButton("🔍 Buscar Gênero"), btnAtivas = new JButton("📋 Locações Ativas");

        txtConsultas.setFont(new Font("Consolas", Font.PLAIN, 12)); txtConsultas.setEditable(false);

        btnTop.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("=== TOP JOGOS MAIS ALUGADOS ===\n\n");
            int r = 1;
            for (Jogo j : relatorio.topJogosMaisAlugados(loja.getJogosCadastrados(), 3))
                sb.append(r++).append("º ").append(j.getNome()).append(" | Aluguéis: ").append(j.getContador()).append("\n");
            txtConsultas.setText(sb.toString());
        });

        btnGen.addActionListener(e -> {
            String g = JOptionPane.showInputDialog(this, "Gênero:");
            if (g != null && !g.trim().isEmpty()) {
                StringBuilder sb = new StringBuilder("=== JOGOS DO GÊNERO: ").append(g).append(" ===\n\n");
                for (Jogo j : relatorio.buscaPorGenero(loja.getJogosCadastrados(), g.trim()))
                    sb.append("- ").append(j.getNome()).append(" (R$ ").append(j.getValorDiario()).append(")\n");
                txtConsultas.setText(sb.toString());
            }
        });

        btnAtivas.addActionListener(e -> {
            Cliente c = (Cliente) cbLocCliente.getSelectedItem();
            if (c != null) {
                StringBuilder sb = new StringBuilder("=== LOCAÇÕES ATIVAS: ").append(c.getNome()).append(" ===\n\n");
                for (Locacao l : relatorio.locacoesAtivas(loja.getLocacoesRegistradas(), c))
                    sb.append("- Jogo: ").append(l.getJogo().getNome()).append(" | Status: ").append(l.getStatus()).append("\n");
                txtConsultas.setText(sb.toString());
            }
        });

        topo.add(btnTop); topo.add(btnGen); topo.add(btnAtivas);
        p.add(topo, BorderLayout.NORTH); p.add(new JScrollPane(txtConsultas), BorderLayout.CENTER);
        return p;
    }

    // ATUALIZAÇÕES E DADOS
    private void atualizarExibicaoCaixa() {
        if (caixa != null && relatorio != null) {
            txtCaixa.setText(relatorio.gerarRelatorioFechamento(caixa));
            lblSaldo.setText(String.format("  Saldo Atual: R$ %.2f (%s)", caixa.getSaldoAtual(), caixa.isAberto() ? "Aberto" : "Fechado"));
        }
    }

    private void atualizarTabelaClientes() {
        modClientes.setRowCount(0);
        for (Cliente c : loja.getClientesCadastrados())
            modClientes.addRow(new Object[]{c.getNome(), c.getCpf(), c.getEmail(), c.getIdade(), (c instanceof ClientePremium ? "Premium" : "Comum"), c.getFidelidade().getPontos()});
    }

    private void atualizarTabelaLocacoes() {
        modLocacoes.setRowCount(0);
        for (Locacao l : loja.getLocacoesRegistradas())
            modLocacoes.addRow(new Object[]{l.getCliente().getNome(), l.getJogo().getNome(), l.getDataInicio(), l.getDataPrevistaDevolucao(), l.getDiasAlugados(), l.getValorTotal(), l.getStatus()});
    }

    private void carregarDadosIniciais() {
        JogoFisico zelda = new JogoFisico("Zelda TOTK", Plataforma.SWITCH_1, "Aventura", 3, 15.0, ClassificacaoEtaria.DOZE);
        JogoDigital cyberpunk = new JogoDigital("Cyberpunk 2077", Plataforma.PC, "RPG", "CDPR-KEY-001", 20.0, ClassificacaoEtaria.DEZOITO, 70, 5);
        loja.adicionarJogo(zelda); loja.adicionarJogo(cyberpunk);
        cbLocJogo.addItem(zelda); cbLocJogo.addItem(cyberpunk);
        modJogos.addRow(new Object[]{zelda.getNome(), zelda.getPlataforma(), zelda.getGenero(), zelda.getValorDiario(), zelda.getClassificacao(), "Físico", "3 Un."});
        modJogos.addRow(new Object[]{cyberpunk.getNome(), cyberpunk.getPlataforma(), cyberpunk.getGenero(), cyberpunk.getValorDiario(), cyberpunk.getClassificacao(), "Digital", "70 GB"});

        ClienteComum joao = new ClienteComum("João", "123", "joao@email.com", 15);
        ClientePremium maria = new ClientePremium("Maria", "456", "maria@email.com", 25);
        loja.adicionarCliente(joao); loja.adicionarCliente(maria);
        cbLocCliente.addItem(joao); cbLocCliente.addItem(maria);
        atualizarTabelaClientes();

        Locacao loc1 = new Locacao(maria, zelda, 5, LocalDate.of(2026, 7, 20));
        loja.registrarLocacao(loc1);
        cbDevLocacao.addItem(loc1);
        atualizarTabelaLocacoes();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
