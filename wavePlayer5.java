import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.nio.file.*;
import java.io.*;
import java.io.IOException;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.HashMap;

class wavePlayer5 extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
	static JFrame f; 
	static JButton b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11;
	static JTextArea jt;  // text area for info
    static List wavlist = new List();  // java List widget to display available wav files
    static Clip clip = null;  // needed to make this a class variable to get pause to work
    long   clipTime  = 0;  // same
    static ArrayList<String> playList = new ArrayList<>();        // used by buttons 'PREV' and 'NEXT'
    static int playListIndex = -1;  // records current play index (for use with 'PREV' and 'NEXT')
    static HashMap<String, Integer> statistics = new HashMap<>();  // used by button 'STATS'

	wavePlayer5() {}  	// default constructor

	public static void main(String[] args)
	{
		f = new JFrame("CS3 wave audio player");

		b1 = new JButton("LIST");
		b2 = new JButton("UPLOAD");
		b3 = new JButton("REMOVE");
		b4 = new JButton("HELP");      
		b5 = new JButton("PLAY");
		b6 = new JButton("PAUSE");
        b7 = new JButton("RESUME");
        b8 = new JButton("STOP");
        b9 = new JButton("PREV");
        b10 = new JButton("NEXT");
        b11 = new JButton("STATS");

		wavePlayer5 wp = new wavePlayer5();
		jt = new JTextArea();
		JPanel p = new JPanel();
        p.setLayout(null);
      
		b1.addActionListener(wp);
		b2.addActionListener(wp);
		b3.addActionListener(wp);
		b4.addActionListener(wp);
		b5.addActionListener(wp);
		b6.addActionListener(wp);
		b7.addActionListener(wp);
		b8.addActionListener(wp);
		b9.addActionListener(wp);
		b10.addActionListener(wp);
		b11.addActionListener(wp);
        wavlist.addActionListener(wp);

		// add the components to panel
        p.add(wavlist);
		p.add(b1);
		p.add(b2);
		p.add(b3);
		p.add(b4);
		p.add(b5);
		p.add(b6);
		p.add(b7);
		p.add(b8);
		p.add(b9);
		p.add(b10);
		p.add(b11);
        p.add(jt);

      int wavlistWidth = 300;
      wavlist.setBounds(5, 5, wavlistWidth, 505);
      Insets insets = p.getInsets();
      //System.out.println(b2.getPreferredSize());
      int swidth = 100;
      int sheight = 30;
     //Dimension size = b2.getPreferredSize();  // b2 is the largest button (so using it to size others)
      b1.setBounds(wavlistWidth             + 25 + insets.left, 5 + insets.top, swidth, sheight);
      b2.setBounds(wavlistWidth + swidth    + 25 + insets.left, 5 + insets.top, swidth, sheight);
      b3.setBounds(wavlistWidth + 2*swidth  + 25 + insets.left, 5 + insets.top, swidth, sheight);
      b4.setBounds(wavlistWidth + 3*swidth  + 25 + insets.left, 5 + insets.top, swidth, sheight);
      
      b5.setBounds(wavlistWidth             + 25 + insets.left, 8 + insets.top + sheight, swidth, sheight);
      b6.setBounds(wavlistWidth + swidth    + 25 + insets.left, 8 + insets.top + sheight, swidth, sheight);
      b7.setBounds(wavlistWidth + 2*swidth  + 25 + insets.left, 8 + insets.top + sheight, swidth, sheight);
      b8.setBounds(wavlistWidth + 3*swidth  + 25 + insets.left, 8 + insets.top + sheight, swidth, sheight);

      b9.setBounds(wavlistWidth             + 25 + insets.left, 8 + insets.top + 2*sheight, swidth, sheight);
      b10.setBounds(wavlistWidth + swidth   + 25 + insets.left, 8 + insets.top + 2*sheight, swidth, sheight);
      b11.setBounds(wavlistWidth + 2*swidth + 25 + insets.left, 8 + insets.top + 2*sheight, swidth, sheight);
      
      jt.setBounds(wavlistWidth + 25 + insets.left, 30 + insets.top + 3*sheight, 4*swidth, 570 - 60 - (30 + insets.top + 3*sheight));
      
		f.add(p);
		//f.setSize(750, 410);   // set the size of frame
		f.setSize(750, 570);   // set the size of frame
		f.setVisible(true);
	}

	// if the button is pressed
	public void actionPerformed(ActionEvent e)
	{
		String s = e.getActionCommand();
      
      if (s.equals("LIST")) 
      {
          wavlist.removeAll();
          File folder = new File(System.getProperty("user.dir") + "\\Disney");
          File[] listOfFiles = folder.listFiles();
          for (File file : listOfFiles)
              if (file.isFile()) {
                  wavlist.add(file.getName());
                  playList.add(file.getName());  // create arraylist for PREV and NEXT
              }
		} 
      else if (s.equals("PAUSE")) 
      {
          clipTime = clip.getMicrosecondPosition();
          clip.stop();
      } 
      else if (s.equals("RESUME")) 
      {
          clip.setMicrosecondPosition(clipTime);
          clip.start();
      }
      else if (s.equals("STOP")) 
      {
          clipTime = 0;
          clip.stop();
      }
      else if (s.equals("PLAY")) 
      {
          if (wavlist.getSelectedItem() != null) 
          {
            try 
            {
              playListIndex = playList.indexOf(wavlist.getSelectedItem());  // get index of currently playing song
              if (statistics.get(wavlist.getSelectedItem()) == null)
                  statistics.put(wavlist.getSelectedItem(), 1);
              else
                  statistics.put(wavlist.getSelectedItem(), statistics.get(wavlist.getSelectedItem()) + 1);
            
              File yourFile = new File(System.getProperty("user.dir") + "\\Disney\\" + wavlist.getSelectedItem());
              AudioInputStream stream;
              AudioFormat format;
              DataLine.Info info;
              
              stream = AudioSystem.getAudioInputStream(yourFile);
              format = stream.getFormat();
              info = new DataLine.Info(Clip.class, format);
              clip = (Clip) AudioSystem.getLine(info);
              clip.open(stream);
              clip.start();
            } 
            catch (Exception x) 
            {
              System.err.println("error occurred during play or pause");
            }
          } 
          else 
          {
              jt.setText(null);
              jt.append("Please use 'LIST' button to see the list\nand select one of the items from the list before PLAY\n");
          }
		} 
      else if (s.equals("PREV")) 
      {
          if (playListIndex > 0) 
          {
              try 
              {
                  playListIndex--;
                  clipTime = 0;
                  clip.stop(); // stop if another song is playing
                  if (statistics.get(playList.get(playListIndex)) == null)
                      statistics.put(playList.get(playListIndex), 1);
                  else
                      statistics.put(playList.get(playListIndex), statistics.get(playList.get(playListIndex)) + 1);

                  File yourFile = new File(System.getProperty("user.dir") + "\\Disney\\" + playList.get(playListIndex));
                  AudioInputStream stream;
                  AudioFormat format;
                  DataLine.Info info;
              
                  stream = AudioSystem.getAudioInputStream(yourFile);
                  format = stream.getFormat();
                  info = new DataLine.Info(Clip.class, format);
                  clip = (Clip) AudioSystem.getLine(info);
                  clip.open(stream);
                  clip.start();
              } 
              catch (Exception x) 
              {
                  System.err.println("error occurred during play or pause");
              }
          } 
          else if (playListIndex == 0)
          {
              jt.setText(null);
              jt.append("You are already at the top of the list, please make a different selection\n");
          }
          else if (playListIndex <= -1)
          {
              jt.setText(null);
              jt.append("Please PLAY a song first before using the PREV feature\n");
          }
      }
      else if (s.equals("NEXT")) 
      {
          if (playListIndex > -1 && playListIndex < playList.size()-1) 
          {
              try 
              {
                  playListIndex++;
                  clipTime = 0;
                  clip.stop(); // stop if another song is playing
                  if (statistics.get(playList.get(playListIndex)) == null)
                      statistics.put(playList.get(playListIndex), 1);
                  else
                      statistics.put(playList.get(playListIndex), statistics.get(playList.get(playListIndex)) + 1);
                  
                  File yourFile = new File(System.getProperty("user.dir") + "\\Disney\\" + playList.get(playListIndex));
                  AudioInputStream stream;
                  AudioFormat format;
                  DataLine.Info info;
              
                  stream = AudioSystem.getAudioInputStream(yourFile);
                  format = stream.getFormat();
                  info = new DataLine.Info(Clip.class, format);
                  clip = (Clip) AudioSystem.getLine(info);
                  clip.open(stream);
                  clip.start();
              } 
              catch (Exception x) 
              {
                  System.err.println("error occurred during play or pause");
              }
          } 
          else if (playListIndex <= -1)
          {
              jt.setText(null);
              jt.append("Please PLAY a song first before using the PREV feature\n");
          }
          else if (playListIndex == playList.size()-1)
          {
              jt.setText(null);
              jt.append("You are already at the bottom of the list, please make a different selection\n");
          }
      }
      else if (s.equals("HELP")) 
      {
          jt.setText(null);  // clear out the textArea
          //Font font = new Font("Ink Free", Font.BOLD, 13);
          //jt.setFont(font);
          jt.append("Media player instructions:\n\n" +
                    "LIST:   shows the list of available wav files\n\n" +
                    "UPLOAD:   allows the user to find a wav file on their computer to upload\n\n" +
                    "REMOVE:   allows the user to remove one of the wav files from the list\n\n" +                     
                    "HELP:   prints directions on how to use this media player\n\n" + 
                    "PLAY:   plays the wav file that was previously selected\n\n" +
                    "PAUSE:   pauses the wav file that is currently playing\n\n" +
                    "RESUME:   resumes the wav file that was previously paused\n\n" +
                    "STOP:   stops the wav file that is currently playing\n\n" +
                    "PREV:   play the previous song in the list\n\n" +
                    "NEXT:   play the next song in the list\n\n" +
                    "STATS:  display statistics about the frequency of played songs\n\n"
                    );
		} 
      else if (s.equals("UPLOAD")) 
      {
          jt.setText(null);  // clear out the textArea
          FileDialog fd = new FileDialog(f);
          fd.setMode(FileDialog.LOAD);
          fd.setVisible(true);
          String filePath = fd.getDirectory() + fd.getFile();
          Path sourceFile = Paths.get(filePath);
          
          String wavPath = System.getProperty("user.dir") + "\\Disney";  // get directory path to stored wav files
          Path targetFile = Paths.get(wavPath + "\\" + fd.getFile());
          try 
          {
              Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
              jt.setText("wave file (" + fd.getFile() + ") has been uploaded");
          } 
          catch (IOException ex) 
          {
              System.err.format("I/O Error when copying file");
          }
		} 
      else if (s.equals("REMOVE")) 
      {
          jt.setText(null);  // clear out the textArea
          if (wavlist.getSelectedItem() != null) 
          {
              File f = new File(System.getProperty("user.dir") + "\\Disney\\" + wavlist.getSelectedItem());
              if (f.delete())
                  jt.setText("wave file (" + wavlist.getSelectedItem() + ") has been removed");
              else
                  jt.setText("was unable to remove wave file (" + wavlist.getSelectedItem() + ")");
          } 
          else 
          {
              jt.setText(null);
              jt.append("Please use 'LIST' button to see the list\nand select one of the items from the list before REMOVE\n");
          }
		}
      else if (s.equals("STATS")) 
      {
          jt.setText(null);
          for (String name : statistics.keySet()) {
              String result = String.format("wav file %s was played %s times\n", name, Integer.toString(statistics.get(name)));
              jt.append(result);
          }
      }
	}
}
