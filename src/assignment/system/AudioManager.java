package src.assignment.system;

import java.awt.*;
import java.util.*;
import java.lang.*;
import javax.sound.sampled.*;
import src.assignment.state.*;

public class AudioManager implements InterfaceAudioManager {
	
	/***Properties***/
	
	protected ArrayList<String> audioList;
	protected Map<String, AudioSource> audioMap;
	protected ArrayList<String> playList;
	protected ArrayList<String> playedList;
	protected String currentlyPlaying;
	
	protected InterfaceGame game;
	
	/***Constructor***/
	
	public AudioManager(InterfaceGame game) {
		this.game = game;
		audioList = new ArrayList<String>();
		audioMap = new HashMap<String, AudioSource> ();
		playList = new ArrayList<String>();
		playedList = new ArrayList<String>();
		currentlyPlaying = "";
	}
	
	public void registerAudio(AudioSource audio) {
		audioList.add(audio.name());
		audioMap.put(audio.name(), audio);
	}
	
	public void rebuildAudioClips() {
		for (int i = 0; i < audioList.size(); i++) {
			String name = audioList.get(i);
			audioMap.get(name).rebuildClips();
		}
	}
	
	public void addToPlayList(String name) {
		AudioSource audio = audioMap.get(name);
		if (audio == null) {
			throw new NullPointerException(String.format("Audio %s has not been registered.", name));
		} 
		playList.add(name);
	}
	
	public void shufflePlayList() {
		Collections.shuffle(playList);
	}
	
	public String getNameFromPlayList(int index) {
		return playList.get(index);
	}
		
	public String getNameFromPlayList() { return getNameFromPlayList(0); }

	public String getRandomFromPlayList() {
		String justPlayed = currentlyPlaying;
		
		if (playList.size() ==  0) {
			resetPlayList();
		}
		
		String name = playList.get(0);
		while(name == justPlayed) {
			resetPlayList();	
			name = playList.get(0);
		}
		
		playedList.add(name);
		playList.remove(0);
		return name;
	}
	
	public void resetPlayList() {
		for (int i = 0; i < playedList.size(); i++) {
			playList.add(playedList.get(i));
			playedList.remove(i);
		}
		shufflePlayList();
	}
	
	public void playFromPlayList(int index) {
		String name = playList.get(index);
		playAudio(name);
	}
	
	public void loopFromPlayList(int index) {
		String name = playList.get(index);
		startAudioLoop(name);
	}
	
	public void playAudio(String name) {
		AudioSource audio = audioMap.get(name);
		if (audio == null) {
			throw new NullPointerException(String.format("Audio %s has not been registered.", name));
		} 
		currentlyPlaying = audio.name();
		audio.playAudio();
	}
	
	public void startAudioLoop(String name) {
		AudioSource audio = audioMap.get(name);
		if (audio == null) {
			throw new NullPointerException(String.format("Audio %s has not been registered.", name));
		} 
		currentlyPlaying = audio.name();
		game.startAudioLoop(audio);
	}
	
	public void stopAudioLoop(String name) {
		AudioSource audio = audioMap.get(name);
		if (audio == null) {
			throw new NullPointerException(String.format("Audio %s has not been registered.", name));
		} 
		game.stopAudioLoop(audio);
	}
	
	public void stopCurrentAudioLoop() {
		AudioSource audio = audioMap.get(currentlyPlaying);
		if (audio != null) { game.stopAudioLoop(audio); } 
		
	}
}