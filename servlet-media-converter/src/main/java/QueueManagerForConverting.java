

import java.io.File;
import java.io.IOException;

public class QueueManagerForConverting extends QueueManager {
	
		static boolean convertionIsRunning = false;
	
		public QueueManagerForConverting(String videoRepository){
	    	super.videoRepository = videoRepository;   
	    }
	
	    @Override
	    public void run() {
	        try {
	        	if(!convertionIsRunning)convertionIsRunning=true;
	        	updateQueue();
	        } catch (InterruptedException e) {
	        	e.printStackTrace();
	        	convertionIsRunning=false;
	        }
	    }
	    
		protected void updateQueue() throws InterruptedException {
	        //get video list to convert
	        //iterate on to all videos and convert one after another  
			//go ahead with iteration 
			for(int i = 0; i<videoListForConverting.size(); i++){
				String v = videoListForConverting.get(i);
				File fileToConvert = new File(v);
				//mp4 converting process already done
				//add video to list, if not exists yet
				if(fileToConvert.isFile()){
					//start to convert this video
					try {
						new Converter().convertFileToMp4(fileToConvert); 
					} catch (IOException e) { e.printStackTrace(); }
				}else{//update Queue if file not found
					removeFileFromVideoListForConverting(fileToConvert);
				}
				//wait, if queue is full
				while(videoQueue.size()==MAXQUEUE){
					sleep(5000);
				}
			}   
			//sleeping 
			sleep(15000);
		updateQueue();
	    } 	
		
		public static boolean removeFileFromVideoListForConverting(File fileToConvert){
			for(int a=0;a<videoListForConverting.size();a++){
				if(videoListForConverting.get(a).equals(fileToConvert.toString())){
					videoListForConverting.remove(a);
				}
			}
			return true;
		}
		
}
