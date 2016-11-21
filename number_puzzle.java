
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JLayeredPane;

class number_puzzle extends JFrame implements KeyListener, MouseListener{

	JPanel[] game_panels; 
	JPanel mainPanel,game;
	int score,best_score;
	int panel_nums[];
	JLabel score_label,bestscore_label,gameover;
	JLabel[] game_labels;
	JLayeredPane layer;
	
    //処理時の計算順番（type変数の中身）
	int down[][]={{12,8,4,0},{13,9,5,1},{14,10,6,2},{15,11,7,3}};
	int up[][]={{0,4,8,12},{1,5,9,13},{2,6,10,14},{3,7,11,15}};
	int left[][]={{0,1,2,3},{4,5,6,7},{8,9,10,11},{12,13,14,15}};
	int right[][]={{3,2,1,0},{7,6,5,4},{11,10,9,8},{15,14,13,12}};

	public static void main(String[] args){
		number_puzzle test=new number_puzzle();
	}

	number_puzzle(){
		
		this.setTitle("2048");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,600);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter(){ 
			public void windowClosing(WindowEvent ev){
				System.exit(0);
			}
		});
		addKeyListener(this);

		Container contentPane=this.getContentPane();
		mainPanel=new JPanel();
		mainPanel.setBackground(new Color(255,255,230));
		contentPane.add(mainPanel);

        //上部に位置調整用パネルを構築しその中に2段パネルを構築
		JPanel design=new JPanel();
		JPanel upper_design=new JPanel();
		JPanel lower_design=new JPanel();

		design.setBorder(new EmptyBorder(30,30,10,30));
		design.setBackground(new Color(255,255,230));

    //upper_design panelレイアウト
        //タイトル
		JLabel title=new JLabel("2048");
		title.setForeground(new Color(105,105,105));
		title.setFont(new Font("Arial",Font.BOLD,30));
		title.setPreferredSize(new Dimension(90,45));

        //スコアボード
		JPanel score_board=new JPanel();
		score_board.setBackground(new Color(210,180,140));
		score_board.setPreferredSize(new Dimension(70,45));
		score_board.setLayout(new GridLayout(2,1));
		JLabel score_title=new JLabel("SCORE");
		score_title.setFont(new Font("Arial",Font.BOLD,15));
		score_title.setHorizontalAlignment(JLabel.CENTER);
		score_title.setForeground(new Color(255,239,213));
		score_label=new JLabel(Integer.toString(score));
		score_label.setForeground(Color.WHITE);
		score_label.setFont(new Font("Arial",Font.BOLD,15));
		score_label.setHorizontalAlignment(JLabel.CENTER);
		score_board.add(score_title);
		score_board.add(score_label);

        //ベストスコアボード
		JPanel bestscore_board=new JPanel();
		bestscore_board.setBackground(new Color(210,180,140));
		bestscore_board.setPreferredSize(new Dimension(70,45));
		bestscore_board.setLayout(new GridLayout(2,1));
		JLabel bestscore_title=new JLabel("BEST");
		bestscore_title.setForeground(new Color(255,239,213));
		bestscore_title.setFont(new Font("Arial",Font.BOLD,15));
		bestscore_title.setHorizontalAlignment(JLabel.CENTER);
		bestscore_label=new JLabel(Integer.toString(best_score));
		bestscore_label.setForeground(Color.WHITE);
		bestscore_label.setFont(new Font("Arial",Font.BOLD,15));
		bestscore_label.setHorizontalAlignment(JLabel.CENTER);
		bestscore_board.add(bestscore_title);
		bestscore_board.add(bestscore_label);

        //upper_design　panelに配置
		upper_design.add(title);
		upper_design.add(score_board);
		upper_design.add(bestscore_board);
		upper_design.setBackground(new Color(255,255,230));

		//lower_design panelレイアウト
        //ルール説明
		JLabel rule=new JLabel("<html>足し算して2048を作ろう！</html>");
		rule.setPreferredSize(new Dimension(150,40));
		rule.setFont(new Font("Arial",Font.PLAIN,16));

        //New Gameボタン
		JPanel newgame_button=new JPanel();
		newgame_button.setBackground(new Color(205,133,63));
		newgame_button.setPreferredSize(new Dimension(120,40));
		JLabel newgame_label=new JLabel("New Game");
		newgame_label.setForeground(new Color(255,239,213));
		newgame_label.setFont(new Font("Arial",Font.BOLD,16));
		newgame_button.add(newgame_label,BorderLayout.CENTER);
		newgame_button.addMouseListener(this);

        //lower_design panelに配置
		lower_design.add(rule);
		lower_design.add(newgame_button);
		lower_design.setBackground(new Color(255,255,230));

        //位置調整用のパネルにupper・lower panelを配置
		design.setLayout(new GridLayout(2,1));
		design.add(upper_design);
		design.add(lower_design);

		//ゲーム画面レイアウト
		layer=new JLayeredPane();
		layer.setPreferredSize(new Dimension(270,270));
		game=new JPanel();
		game.setBackground(Color.GRAY);
		game.setPreferredSize(new Dimension(270,270));
		game.setBorder(new LineBorder(new Color(255,255,230),0,false));
		game.setLayout(new GridLayout(4,4));
        //4*4のマス（パネル）を構築
        game_panels=new JPanel[16];
        game_labels=new JLabel[16];
        panel_nums=new int[16];
        for(int n=0; n<16; n++){
            game_panels[n]=new JPanel();
            game_panels[n].setBackground(new Color(169,169,169));
            game_panels[n].setBorder(new LineBorder(new Color(210,180,140),5,false));
            game_labels[n]=new JLabel();
            game_labels[n].setOpaque(true); //透明化
            game_labels[n].setFont(new Font("Arial",Font.BOLD,32));
            game_panels[n].add(game_labels[n],BorderLayout.CENTER);
        }
        //マスを配置
		layer.add(game,2);
		for(int n=0; n<16; n++){
			game.add(game_panels[n]);
			//layer.add(game_panels[n],JLayeredPane.PALETTE_LAYER);
		}

		//gameover時の表示画面の設定
		gameover=new JLabel("GAME OVER");
		gameover.setBackground(new Color(128,128,128,100));
		gameover.setForeground(new Color(245,245,245,100));
		gameover.setFont(new Font("Arial",Font.BOLD,40));
//		gameover.setLocation(0,0);
		gameover.setPreferredSize(new Dimension(270,270));
		mainPanel.add(gameover,BorderLayout.CENTER);
		layer.add(gameover,3);
		
//		gameover.add(gameover_label,BorderLayout.CENTER);
//		layer.add(gameover_label,JLayeredPane.MODAL_LAYER);
		//通常時は不可視に設定する
		//gameover.setVisible(false);
		//gameover_label.setVisible(false);


		mainPanel.add(design,BorderLayout.NORTH);
		mainPanel.add(game,BorderLayout.CENTER);

		NewPanel();

		this.setVisible(true);
	}

    //スコア設定
	void ChangeScore(){
        //元々のスコアに新しく追加された数値を加算
        score=0;
		for(int num: panel_nums){
			score+=num;
		}
		score_label.setText(Integer.toString(score));
        //ベストスコアの更新
		if(score > best_score){
			best_score=score;
			bestscore_label.setText(Integer.toString(best_score));
		}
	}

    //マス目の表示を設定
    void ChangePanel(){
		Color panelColor,numColor;
		int num;
		for(int i=0;i<16;i++){
			num=panel_nums[i];
			switch(num){//マス目のフォント
				case 2:
					panelColor=new Color(226,228,240);
					numColor=Color.BLACK;
					break;
				case 4:
					panelColor=new Color(255,235,205);
					numColor=Color.BLACK;
					break;
				case 8:
					panelColor=new Color(255,140,80);
					numColor=Color.WHITE;
					break;
				case 16:
					panelColor=new Color(255,140,100);
					numColor=Color.WHITE;
					break;
				case 32:
					panelColor=new Color(220,20,60);
					numColor=Color.WHITE;
					break;
				case 64:
					panelColor=new Color(255,0,51);
					numColor=Color.WHITE;
					break;
				case 128:
					panelColor=new Color(255,255,120);
					numColor=Color.WHITE;
					break;
				case 256:
					panelColor=new Color(255,255,100);
					numColor=Color.WHITE;
					break;
				case 512:
					panelColor=new Color(255,255,80);
					numColor=Color.WHITE;
					break;
				case 1024:
					panelColor=new Color(255,255,60);
					numColor=Color.WHITE;
					break;
				case 2048:
					panelColor=new Color(255,255,40);
					numColor=Color.WHITE;
					break;
				case 4096:
					panelColor=new Color(255,255,20);
					numColor=Color.WHITE;
					break;
				case 8192:
					panelColor=new Color(255,255,10);
					numColor=Color.WHITE;
					break;
				case 16384:
					panelColor=new Color(255,255,0);
					numColor=Color.WHITE;
					break;
				default:
					panelColor=new Color(255,255,0);
					numColor=Color.WHITE;
			}
			if(num==0){//マスが空のときの表示
				game_panels[i].setBackground(new Color(169,169,169));
				game_labels[i].setText("");
				game_labels[i].setOpaque(true);
			}else{//マスが埋まっているときの表示
				game_panels[i].setBackground(panelColor);
				game_labels[i].setText(Integer.toString(num));
				game_labels[i].setOpaque(false);
				game_labels[i].setForeground(numColor);
			}
		}
	}

	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}

	public void keyPressed(KeyEvent e){
		Boolean key=true;
		Boolean ismoved=false;
		int type[][]=new int[4][4];
		switch(e.getKeyCode()){//押されたキーの判別
			case KeyEvent.VK_DOWN:
				type=down;
				key=true;
				break;
			case KeyEvent.VK_UP:
				type=up;
				key=true;
				break;
			case KeyEvent.VK_LEFT:
				type=left;
				key=true;
				break;
			case KeyEvent.VK_RIGHT:
				type=right;
				key=true;
				break;
			default:
				key=false;
				break;
		}
		int num=-1;
		if(!key){}else{
			for(int i=0; i<4; i++){//type配列を順番に総当り
				for(int ii=0; ii<4; ii++){
                    if(panel_nums[type[i][ii]]!=0){
                    	if(num==-1){
                    		num=type[i][ii];
                    	}else{
	                        if(panel_nums[num]==panel_nums[type[i][ii]]){//該当マスと記録マスが同じ値の場合
    	                       panel_nums[type[i][ii]]=panel_nums[num]*2;
        	                   panel_nums[num]=0;
            	               num=-1;
            	               ismoved=true;
                	        }else{
                    	       num=type[i][ii];
                        	}
                        }
                    }
                }
                num=-1;
				int ii=0;
				while(ii<4){//パネルを詰める
					if(num==-1){
						if(panel_nums[type[i][ii]]==0){
							num=ii;
						}
					}else{
						if(panel_nums[type[i][ii]]!=0){
							panel_nums[type[i][num]]=panel_nums[type[i][ii]];
							panel_nums[type[i][ii]]=0;
							ii=num;
							num=-1;
							ismoved=true;
						}
					}
					ii++;
				}
				ii=0;
				num=-1;
			}
		}
		if(ismoved){
			NewPanel();
		}else{
			if(Arrays.binarySearch(panel_nums,0) <0){
				//gameOverメッセージ表示（未実装）
/*				gameover=new JPanel();
				gameover_label=new JLabel("GAME OVER");
				gameover.setBackground(new Color(128,128,128,100));
				gameover.setLocation(0,0);
				gameover.setPreferredSize(new Dimension(270,270));
				mainPanel.add(gameover,BorderLayout.CENTER);
				layer.add(gameover,JLayeredPane.MODAL_LAYER);
				gameover_label.setForeground(new Color(245,245,245,100));
				gameover_label.setFont(new Font("Arial",Font.BOLD,40));
				gameover.add(gameover_label,BorderLayout.CENTER);
				layer.add(gameover_label,JLayeredPane.MODAL_LAYER);
				gameover.setVisible(true);
				gameover_label.setVisible(true);*/
			}
		}
	}

	void NewPanel(){
		ArrayList<Integer> zeros=new ArrayList<Integer>();
		for(int i=0; i<16; i++){
			if(panel_nums[i]==0){
				zeros.add(i);
			}
		}

		int new_number;
		if(Math.random()<0.8){
			new_number=2;
		}else{
			new_number=4;
		}

		int new_panel=zeros.get((int)(Math.random()*zeros.size()));
		panel_nums[new_panel]=new_number;
		ChangePanel();
		ChangeScore();
	}

	public void mouseClicked(MouseEvent e){
		Arrays.fill(panel_nums,0);
		NewPanel();
	}
	public void mouseEntered(MouseEvent e){
		 setCursor(Cursor.getPredefinedCursor(
            Cursor.HAND_CURSOR));
	}
	public void mouseExited(MouseEvent e){
		 setCursor(Cursor.getPredefinedCursor(
            Cursor.DEFAULT_CURSOR));
	}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){;}

}



