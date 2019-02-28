package src.assignment.system;

import java.awt.*;
import java.util.*;
import java.lang.*;

public interface InterfaceAudioManager {
	
	public void registerAudio(AudioSource audio);
	
	public void rebuildAudioClips();

	public void addToPlayList(String name);
	
	public void shufflePlayList();
	
	public String getNameFromPlayList(int index);
	public String getNameFromPlayList();
	public String getRandomFromPlayList();
	public void resetPlayList();
	
	public void playFromPlayList(int index);
	
	public void loopFromPlayList(int index);
	
	public void playAudio(String name);

	public void startAudioLoop(String name);
	
	public void stopAudioLoop(String name);
	
	public void stopCurrentAudioLoop();
}