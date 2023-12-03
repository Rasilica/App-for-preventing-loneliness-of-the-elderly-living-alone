import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class ShowAllFrame extends JFrame {
    DefaultTableModel model;

    public ShowAllFrame() {
        setTitle("전체 보기");
        setSize(500, 300);
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("기관명");
        model.addColumn("도로명주소");
        model.addColumn("유형");
        model.addColumn("전화번호");

        loadData("전체"); // 초기에는 전체 데이터를 보여줍니다.

        add(new JScrollPane(table), BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu filterMenu = new JMenu("Filter by 유형");
        String[] types = {"교육", "복지", "의료기관", "일자리", "취미", "전체"}; // 유형 필터링 조건
        for (String type : types) {
            JMenuItem menuItem = new JMenuItem(type);
            menuItem.addActionListener(e -> loadData(type));
            filterMenu.add(menuItem);
        }
        menuBar.add(filterMenu);

        // Export to CSV menu item
        JMenu exportMenu = new JMenu("Export");
        JMenuItem csvMenuItem = new JMenuItem("Export to CSV");
        csvMenuItem.addActionListener(e -> exportToCsv());
        exportMenu.add(csvMenuItem);
        menuBar.add(exportMenu);

        setJMenuBar(menuBar);

        setVisible(true);
    }

    public void loadData(String filter) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DBConnManager.getConnection();
            String sql = "SELECT 기관명, 도로명주소, 유형, 전화번호 FROM 기관";
            if (!filter.equals("전체")) {
                sql += " WHERE 유형 = ?";
            }
            sql += ";";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            if (!filter.equals("전체")) {
                pstmt.setString(1, filter); // 필터링 조건 설정
            }
            ResultSet rs = pstmt.executeQuery();

            model.setRowCount(0); // 기존 데이터 삭제
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void exportToCsv() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("기관정보.csv"));
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    String value = (String) model.getValueAt(i, j);
                    if (value == null) {
                        value = "";
                    }
                    bw.write(value);
                    if (j < model.getColumnCount() - 1) {
                        bw.write(",");
                    }
                }
                bw.newLine();
            }
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
