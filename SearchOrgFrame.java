import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchOrgFrame extends JFrame {
    private JTextField txtOrgName;
    private JTextField txtAddress, txtType, txtPhoneNumber;
    private JButton btnSearch, btnClose, btnReference;
    private JComboBox<String> connectionOptions1, connectionOptions2, connectionOptions3;
    private JPanel resultPanel, result_buttonPanel; // 검색 결과를 표시할 패널
    static final String FAVORITES_FILE = "favorites.txt"; // 즐겨찾기 텍스트 파일
    private JComboBox<String> insertOptions1, insertOptions2, insertOptions3;


    static String Name;
    static String Type;
    static String Adress;
    static String Number;

    center centerFrame = new center();
    public SearchOrgFrame() {
        setTitle("기관 검색");
        setSize(1000, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 입력 패널 설정
        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel inputLeftPanel = new JPanel();
        inputLeftPanel.add(new JLabel("기관명: "));
        txtOrgName = new JTextField(15);
        inputLeftPanel.add(txtOrgName);
        inputPanel.add(inputLeftPanel, BorderLayout.WEST);
        btnSearch = new JButton("검색");
        inputPanel.add(btnSearch, BorderLayout.SOUTH);
        add(inputPanel, BorderLayout.NORTH);

        // 유형, 분류, 업무(콤보박스) 패널 설정
        JPanel connectionOptionsPanel = new JPanel();
        String[] options1 = {"없음", "교육", "복지", "의료기관", "일자리", "취미"};
        connectionOptions1 = new JComboBox<>(options1);
        connectionOptionsPanel.add(new JLabel("유형: "));
        connectionOptionsPanel.add(connectionOptions1);

        String[] options2 = {"없음","병원", "요양병원", "정신병원", "종합병원", "치과병원", "한방병원"};
        connectionOptions2 = new JComboBox<>(options2);
        connectionOptionsPanel.add(new JLabel("분류: "));
        connectionOptionsPanel.add(connectionOptions2);

        String[] options3 = {"없음", "권역센터", "응급의료지원센터", "지역기관", "지역센터"};
        connectionOptions3 = new JComboBox<>(options3);
        connectionOptionsPanel.add(new JLabel("업무: "));
        connectionOptionsPanel.add(connectionOptions3);

        inputPanel.add(connectionOptionsPanel, BorderLayout.EAST);

        // 결과 패널 설정
        resultPanel = new JPanel(new GridLayout(4, 3));
        resultPanel.setBorder(BorderFactory.createTitledBorder("검색 결과"));


        resultPanel.add(new JLabel("도로명주소: "));
        txtAddress = new JTextField();
        resultPanel.add(txtAddress);

        resultPanel.add(new JLabel("유형: "));
        txtType = new JTextField();
        resultPanel.add(txtType);


        resultPanel.add(new JLabel("전화번호: "));
        txtPhoneNumber = new JTextField();
        resultPanel.add(txtPhoneNumber);

        result_buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton delButton, updateButton, favoritesButton;
        delButton = new JButton("삭제");
        result_buttonPanel.add(delButton);

        updateButton = new JButton("수정");
        result_buttonPanel.add(updateButton);

        favoritesButton = new JButton("즐겨찾기 등록");
        result_buttonPanel.add(favoritesButton);

        resultPanel.add(result_buttonPanel);  // resultPanel에 result_buttonPanel 추가

        add(resultPanel, BorderLayout.CENTER);



        // 기관 추가 패널
        JPanel insertPanel = new JPanel(new GridLayout(1, 8));
        insertPanel.setBorder(BorderFactory.createTitledBorder("기관 추가"));
        JTextField addressText, cityText, orgNameText, phoneText;
        JButton insButton;

        insertOptions1 = new JComboBox<>(options1);
        insertOptions2 = new JComboBox<>(options2);
        insertOptions3 = new JComboBox<>(options3);

        insertPanel.add(new JLabel("도로명주소"));
        addressText = new JTextField(15);
        insertPanel.add(addressText);
        insertPanel.add(new JLabel("시도명"));
        cityText = new JTextField(15);
        insertPanel.add(cityText);
        insertPanel.add(new JLabel("기관명"));
        orgNameText = new JTextField(15);
        insertPanel.add(orgNameText);
        insertPanel.add(new JLabel("전화번호"));
        phoneText = new JTextField(15);
        insertPanel.add(phoneText);
        JComboBox<String> insertOptions1 = new JComboBox<>(options1);
        insertPanel.add(new JLabel("유형: "));
        insertPanel.add(insertOptions1);
        JComboBox<String> insertOptions2 = new JComboBox<>(options2);
        insertPanel.add(new JLabel("분류: "));
        insertPanel.add(insertOptions2);
        JComboBox<String> insertOptions3 = new JComboBox<>(options3);
        insertPanel.add(new JLabel("업무: "));
        insertPanel.add(insertOptions3);

        insButton = new JButton("삽입");
        insertPanel.add(insButton);



        btnReference = new JButton("즐겨찾기 조회");

        btnReference.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                centerFrame();
            }
            private void centerFrame() {
                center frame = new center();
                frame.setPreferredSize(new Dimension(1000, 400));
                frame.setLocation(500, 400);
                frame.pack();
                frame.setVisible(true);
                frame.Favoritestxt();

            }

        });

        // 닫기 버튼 설정
        btnClose = new JButton("닫기");

        //전체 보기
        JButton btnShowAll = new JButton("전체 보기");
        Dimension preferredSize = btnClose.getPreferredSize();
        preferredSize.width += 40;
        btnShowAll.setPreferredSize(preferredSize);
        btnShowAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShowAllFrame();
            }
        });


        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // 닫기 버튼 패널 설정
        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        closePanel.add(btnShowAll);
        closePanel.add(btnReference);
        closePanel.add(btnClose);

        // 기관 추가 패널과 닫기 버튼 패널 묶기
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(insertPanel, BorderLayout.CENTER);
        southPanel.add(closePanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // 액션 리스너 설정
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOrgInfo();
            }
        });

        txtOrgName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOrgInfo();
            }
        });

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        insButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertOrg(addressText, cityText, orgNameText, phoneText,
                        (String) insertOptions1.getSelectedItem(),
                        (String) insertOptions2.getSelectedItem(),
                        (String) insertOptions3.getSelectedItem());
            }
        });

        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteOrg(txtAddress);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOrg(txtAddress, txtPhoneNumber);
            }
        });

        favoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                favoritesOrg();
            }
        });
        setVisible(true);
    }

    private void searchOrgInfo() {
        String inputOrgName = txtOrgName.getText();
        String selectedType = (String) connectionOptions1.getSelectedItem();
        String selectedClassification = (String) connectionOptions2.getSelectedItem();
        String selectedTask = (String) connectionOptions3.getSelectedItem();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DBConnManager.getConnection();

            // 동적으로 SQL 쿼리 생성
            StringBuilder queryBuilder = new StringBuilder("SELECT 도로명주소, 유형, 전화번호 FROM 기관 " +
                    "LEFT JOIN 기능 ON 기관.도로명주소 = 기능.기관_도로명주소 " +
                    "LEFT JOIN 목적 ON 기관.도로명주소 = 목적.기관_도로명주소 " +
                    "LEFT JOIN 구분 ON 기관.도로명주소 = 구분.기관_도로명주소 " +
                    "WHERE 1=1");

            if (!inputOrgName.isEmpty()) {
                queryBuilder.append(" AND 기관.기관명 like ?");
            }
            if (!selectedType.isEmpty()) {
                switch(selectedType) {
                    case "교육":
                    case "복지":
                    case "의료기관":
                    case "일자리":
                    case "취미":
                        queryBuilder.append(" AND 기능.유형_유형명 = ?");
                        break;
                    default:
                        break;
                }
            }

            if (!selectedClassification.isEmpty()) {
                switch(selectedClassification) {
                    case "병원":
                    case "요양병원":
                    case "정신병원":
                    case "종합병원":
                    case "치과병원":
                    case "한방병원":
                        queryBuilder.append(" AND 목적.분류_분류명= ?");
                        break;
                    default:
                        break;
                }

            }
            if (!selectedTask.isEmpty()) {
                switch(selectedTask) {
                    case "권역센터":
                    case "응급의료지원센터":
                    case "지역기관":
                    case "지역센터":
                        queryBuilder.append(" AND 구분.업무_업무명 = ?");
                        break;
                    default:
                        break;
                }
            }


            // 최종 쿼리 생성
            String finalQuery = queryBuilder.toString();
            PreparedStatement pstmt = conn.prepareStatement(finalQuery);

            // 동적으로 값 설정
            int parameterIndex = 1;

            if (!inputOrgName.isEmpty()) {
                pstmt.setString(parameterIndex++, "%" + inputOrgName + "%");
            }

            if (!selectedType.isEmpty()) {
                switch(selectedType) {
                    case "교육":
                    case "복지":
                    case "의료기관":
                    case "일자리":
                    case "취미":
                        pstmt.setString(parameterIndex++, selectedType);
                        break;
                    default:
                        break;
                }
            }

            if (!selectedClassification.isEmpty()) {
                switch(selectedClassification) {
                    case "병원":
                    case "요양병원":
                    case "정신병원":
                    case "종합병원":
                    case "치과병원":
                    case "한방병원":
                        pstmt.setString(parameterIndex++, selectedClassification);
                        break;
                    default:
                        break;
                }

            }
            if (!selectedTask.isEmpty()) {
                switch(selectedTask) {
                    case "권역센터":
                    case "응급의료지원센터":
                    case "지역기관":
                    case "지역센터":
                        pstmt.setString(parameterIndex, selectedTask);
                        break;
                    default:
                        break;
                }
            }

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 결과 패널에 검색 결과 표시
                txtAddress.setText(rs.getString(1));
                txtType.setText(rs.getString(2));
                txtPhoneNumber.setText(rs.getString(3));

                Name = inputOrgName;
                Type = rs.getString(2);
                Adress = rs.getString(1);
                Number = rs.getString(3);
            } else {
                // 검색 결과가 없을 때 처리
                txtAddress.setText("검색 결과가 없습니다. ");
                txtType.setText("");
                txtPhoneNumber.setText("");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }


    Connection conn = null;
    JTable table;
    JLabel status;
    String colNames[] = {"도로명주소", "시도명", "기관명", "전화번호"};
    DefaultTableModel model = new DefaultTableModel(colNames, 0) {
        public boolean isCellEditable(int row, int col) {
            if (col == 0) return false;
            else return true;
        }
    };

    private void insertOrg(JTextField addressText, JTextField cityText, JTextField orgNameText, JTextField phoneText, String typeStr, String categoryStr, String taskStr) {
        String addressStr = addressText.getText().trim();
        String cityStr = cityText.getText().trim();
        String orgNameStr = orgNameText.getText().trim();
        String phoneStr = phoneText.getText().trim();
        String sqlInsert = "insert 기관(도로명주소, 시도명, 기관명, 전화번호, 유형) values(?, ?, ?, ?, ?);";
        String sqlInsertCategory = "insert 목적(기관_도로명주소, 분류_분류명) values(?, ?);";
        String sqlInsertTask = "insert 구분(기관_도로명주소, 업무_업무명) values(?, ?);";

        table = new JTable(model);
        status = new JLabel();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DBConnManager.getConnection();
            conn.setAutoCommit(false);
            if (addressStr.length() != 0) {
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                stmtInsert.setString(1, addressStr);
                stmtInsert.setString(2, cityStr);
                stmtInsert.setString(3, orgNameStr);
                stmtInsert.setString(4, phoneStr);
                stmtInsert.setString(5, typeStr);
                int num = stmtInsert.executeUpdate();

                conn.commit();

                if (categoryStr != "없음") {
                    PreparedStatement stmtInsertCategory = conn.prepareStatement(sqlInsertCategory);
                    stmtInsertCategory.setString(1, addressStr);
                    stmtInsertCategory.setString(2, categoryStr);
                    stmtInsertCategory.executeUpdate();
                }

                if (taskStr != "없음") {
                    PreparedStatement stmtInsertTask = conn.prepareStatement(sqlInsertTask);
                    stmtInsertTask.setString(1, addressStr);
                    stmtInsertTask.setString(2, taskStr);
                    stmtInsertTask.executeUpdate();
                }
                conn.commit();

                if (num > 0) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    String arr[] = new String[4];
                    arr[0] = addressStr;
                    arr[1] = cityStr;
                    arr[2] = orgNameStr;
                    arr[3] = phoneStr;
                    model.addRow(arr);
                    addressText.setText("");
                    cityText.setText("");
                    orgNameText.setText("");
                    phoneText.setText("");
                    JOptionPane.showMessageDialog(null, "기관 정보가 추가되었습니다. ");
                    stmtInsert.close();
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null,"기관 정보를 다시 입력해 주세요.");
            ex.printStackTrace();
        }
    }

    private void deleteOrg(JTextField addressText) {
        table = new JTable(model);
        status = new JLabel();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DBConnManager.getConnection();
            String sqlDelete = "delete from 기관 where 도로명주소 = ?;";
            String addressStr = addressText.getText().trim();
            PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete);
            stmtDelete = conn.prepareStatement(sqlDelete);
            stmtDelete.setString(1, addressStr);
            JOptionPane.showMessageDialog(null, "기관 정보를 삭제했습니다.");
            stmtDelete.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "기관 정보 삭제에 실패했습니다.");
            ex.printStackTrace();
        }
    }

    private void updateOrg(JTextField addressText, JTextField phoneText) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DBConnManager.getConnection();
            String sqlUpdate = "update 기관 set 도로명주소 = ?,전화번호 = ? where 도로명주소 = ?;";
            String addressStr = addressText.getText().trim();
            String phoneStr = phoneText.getText().trim();
            PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
            stmtUpdate.setString(1, addressStr);
            stmtUpdate.setString(2, phoneStr);
            stmtUpdate.setString(3, addressStr);
            JOptionPane.showMessageDialog(null, "기관 정보를 수정했습니다.");
            stmtUpdate.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "기관 정보 수정에 실패했습니다.");
            ex.printStackTrace();
        }
    }

    //즐겨찾기 텍스트 파일 읽어오기
    private List<String> readFavoritesFromFile() throws IOException {
        List<String> favoritesList = new ArrayList<>();
        File file = new File(FAVORITES_FILE);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                favoritesList.add(line);
            }
        }

        return favoritesList;
    }


    private void favoritesOrg() { //즐겨찾기 등록 버튼 이벤트
        if (Name.length() == 0 && Adress.length() == 0) {
            JOptionPane.showMessageDialog(null, "기관 검색 후 즐겨찾기 등록 버튼을 눌러주세요.");
        }
        else {
            if (addToFavorites(Name, Adress, Type,  Number)) {
                JOptionPane.showMessageDialog(null, "즐겨찾기 등록에 성공했습니다. ");
            } else {
                JOptionPane.showMessageDialog(null, "즐겨찾기 등록에 실패했습니다. ");
            }
        }
    }

    //즐겨찾기 등록
    private boolean addToFavorites(String name, String address, String category, String phone) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FAVORITES_FILE, true))) {
            writer.write(name + "\t" + address + "\t" + category + "\t" + phone);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        // Swing UI는 Event Dispatch Thread (EDT)에서 실행되어야 합니다.
        SwingUtilities.invokeLater(() -> {
            new SearchOrgFrame();
        });
    }
}
