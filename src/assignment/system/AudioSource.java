package src.assignment.system;

import java.util.*;
import java.lang.*;
import javax.sound.sampled.*;

public class AudioSource<T> {
	
	/***Properties***/
	
	public AudioFormat mFormat;

	public byte[] mData;

	public int mLength;
		
	protected int numOfClips;
	
	protected String name;
	
	protected ArrayList<Clip> clipList;
	
	private T t;

	/***Constructor***/
	
	public AudioSource(String name, T t) {
		this.t = t;
		this.name = name;
		numOfClips = 0;
		clipList = new ArrayList<Clip>();
	}
	
	/***Getters and Setters***/
	
	public T get() { 
		return t; 
	}
	
	public String name() {
		return name;
	}
	
	public void addClips(int numOfClips) {
		this.numOfClips = numOfClips;
		buildClips(numOfClips);
	}
	
	public void rebuildClips() {
		for (int i = 0; i < clipList.size(); i++) {
			clipList.get(i).close();
			clipList.remove(i);
		}
		buildClips(numOfClips);
	}
	
	public void playAudio() {
		
		for (int i = 0; i < clipList.size(); i++) {
			Clip clip = clipList.get(i);
			
			if (clip.getMicrosecondPosition() == 0) {
				clip.start();
				break;
			} else if (clip.getMicrosecondPosition() == clip.getMicrosecondLength()) {
				clip.setFramePosition(0);
				clip.start();
				break;
			}
			
		}
		
	}
	
	protected void buildClips(int numOfClips) {
		try {
			for (int i = 0; i < numOfClips; i++) {
				Clip clip = AudioSystem.getClip();
				clip.open(mFormat, mData, 0, mLength);
				clipList.add(clip);
			}			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}
	
}