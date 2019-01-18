package WebsocketSampler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;



public class Test {

	public static void main(String[] args) throws IOException {
		/*
		Path path = Paths.get("/Users/nenko/Documents/workspace/WebSocketExample/bin/UPDATE_CANVAS_ADD_STROKE");
		byte[] data = Files.readAllBytes(path);
		String sessionID = "3360b5cd-19dc-4d2e-9aad-c50edafa9d62";
		String userID = "00000050";
		byte[] systemInfo = (sessionID + userID).getBytes("UTF-8");
		
		for (int i = 0; i < systemInfo.length; i++) {
			int dataIDX = data.length - systemInfo.length + i;
			data[dataIDX] = systemInfo[i];
		}
		
		System.out.println(data.length + " :: " + systemInfo.length);
		*/
		DecimalFormat df = new DecimalFormat("00000000");
		String value = df.format(50);
		System.out.println(value);
	}
	
	
	
	
	
	

}
