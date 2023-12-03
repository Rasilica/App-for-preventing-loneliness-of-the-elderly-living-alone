import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
	
public class center extends JFrame implements ActionListener{
 	static final String FAVORITES_FILE = "favorites.txt";
	
	JTable table;
	JButton delButton, favoritesButton, closeButton;
	JPanel panel;
	JLabel status;
	
	center() {
		setTitle("즐겨찾기");
		Container contentPane = getContentPane();
		
		// table
		DefaultTableModel model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int col) {
				if (col == 0) return false;
				else return true;
			}
		};
		table = new JTable(model);
		status = new JLabel();
		
		contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
		
		// panel
		JPanel inputPanel = new JPanel();
		favoritesButton = new JButton("즐겨찾기 조회");  // favoritesButton 초기화 추가
		delButton = new JButton("삭제");  // delButton 초기화 추가
		closeButton = new JButton("닫기");
		inputPanel.add(favoritesButton);
		inputPanel.add(delButton);
		inputPanel.add(closeButton);
		

		panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(inputPanel, BorderLayout.CENTER);
		panel.add(status, FlowLayout.LEFT);
		
		contentPane.add(panel, BorderLayout.SOUTH);
		
		// action listener
	
		favoritesButton.addActionListener(this);
		delButton.addActionListener(this);
		closeButton.addActionListener(this);

	}
	
	//즐겨찾기 텍스트 파일 읽어오기 
	public List<String> readFavoritesFromFile() throws IOException {
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
	
	//즐겨찾기 등록 
	public boolean addToFavorites(String name, String address, String category, String phone) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FAVORITES_FILE, true))) {
	        writer.write(name + "\t" + address + "\t" + category + "\t" + phone);
	        writer.newLine();
	        return true;
	    } catch (IOException e) {
	    	e.printStackTrace();
	        return false;
	    }
	}
	//즐겨찾기 행 삭제 
	public void removeFromFavorites(String name, String address, String category, String phone) {
	    try {
	        List<String> favoritesList = readFavoritesFromFile();
	        favoritesList.remove(name + "\t" + address + "\t" + category + "\t" + phone);
	        writeFavoritesToFile(favoritesList);
	    } catch (IOException e) {
	        setStatus("즐겨찾기 파일을 업데이트하는 데 실패했습니다.");
	        e.printStackTrace();
	    }
	}
	//즐겨찾기 텍스트 파일 갱신 
	public void writeFavoritesToFile(List<String> favoritesList) throws IOException {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FAVORITES_FILE))) {
	        for (String line : favoritesList) {
	            writer.write(line);
	            writer.newLine();
	        }
	    }
	}
	
	
	//즐겨찾기 텍스트 파일 조회 
	public void Favoritestxt() {
	    File favoritesFile = new File(FAVORITES_FILE);
	    int count = 0;
	    if (favoritesFile.exists()) {
	        try {
	            List<String> favoritesList = readFavoritesFromFile();
	            
	            DefaultTableModel model = (DefaultTableModel) table.getModel();
	            model.setColumnIdentifiers(new Object[]{"기관명", "도로명주소", "유형", "전화번호"});
	            model.setRowCount(0); // 기존 데이터 초기화

	            for (String line : favoritesList) {
	                String[] parts = line.split("\t");
	                model.addRow(parts);
	                count++;
	            }
	        } catch (IOException e) {
	            setStatus("즐겨찾기 파일을 읽어오는 데 실패했습니다.");
	            e.printStackTrace();
	        }finally {
	        	setStatus(count + " 즐겨찾에 존재합니다.");
	        }
	    }
	}
	
	
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if(src == favoritesButton) { //즐겨찾기 DB 조회
			Favoritestxt();
		}
		else if (src == delButton) {	// 즐겨찾기 삭제 
			  int row = table.getSelectedRow();
			    if (row == -1) {
			        JOptionPane.showMessageDialog(null, "삭제할 행을 먼저 선택한 후 삭제 버튼을 클릭하세요.");
			    } else {
			        DefaultTableModel model = (DefaultTableModel) table.getModel();
			        String name = Objects.toString(model.getValueAt(row, 0), ""); //값이 있으면 첫 번째 null이면 두번째 값
			        String address = Objects.toString(model.getValueAt(row, 1), "");
			        String category = Objects.toString(model.getValueAt(row, 2), "");
			        String phone = Objects.toString(model.getValueAt(row, 3), "");
			        // 텍스트 파일에서 해당 행을 삭제
			        removeFromFavorites(name, address, category, phone);

			        model.removeRow(row); // 테이블에서 삭제
			        setStatus("즐겨찾기에서 삭제 되었습니다.");
			    }
		}
		else if(src==closeButton) {
			dispose();
		}
	}
	
	
	
	public void setStatus(String s) {
		status.setText(s);
		this.validate();
	}
		public static void main(String[] args) {
	
		}
	
	}
