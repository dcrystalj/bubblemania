package sound;

import java.io.*;
import javax.sound.sampled.*;
   
// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
public class Audioapp {
   public String[] music = new String []{"sounds/balloonpop.wav","sounds/gangnam.wav"};
   // Constructor
   public Audioapp(int i) {
   
      try {
         // Open an audio input stream.
    	 File f = new File(music[i]);
		 AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);// Get a sound clip resource.
         Clip clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
         clip.start();
         if(i==1)
        	 clip.loop(1000);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   }
   
}