package WebsocketSampler;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Random;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.glassfish.tyrus.client.ClientManager;

@ClientEndpoint
public class NewWebSocketSampler extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;

	// ws://dev-messaging-bamboo-loop.cloudapp.net/api/web
	// http://dev-messaging-bamboo-loop.cloudapp.net/api/session
	//	private static final String uri = "ws://10.144.142.35:89/api/web";
	
//PROD US - wss://qa-messaging-us.bamboo-loop.com/api/web
	
	private static final String uri = "ws://qa-messaging-us.bamboo-loop.com/api/web";
	private static  String sessionID = "919805ed-503e-4d2a-a49b-dfdfe93c8483";
	private static final String FilePath="/Users/nenko/Documents/WacomTestingProjects/JmeterWebsocketClient/bin/UPDATE_CANVAS_ADD_STROKE";
	private static  int userID = 50;
	private Random rnd;
	private static long StartTime;
	private static long EndTime;
	private static long ExecutionTime;


	
    private static final Logger log = LoggingManager.getLoggerForClass();
    
    public static String GetNewSessionID()
    {
    
      String url = "http://qa-messaging-us.bamboo-loop.com/api/session";
      String GetSessionID=null;
            try {
                HttpClient client = new HttpClient();
                PostMethod method = new PostMethod(url);
                int statusCode = client.executeMethod(method);
                if (statusCode != -1) {
                GetSessionID = method.getResponseBodyAsString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            GetSessionID=GetSessionID.replace("\"", "").trim();
            return GetSessionID;
    }
    
    

    
  
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		rnd=new Random();
		ClientManager client = ClientManager.createClient();
		    
		SampleResult result = new SampleResult();
		result.sampleStart();
        
        try {
        	client.connectToServer(NewWebSocketSampler.class, URI.create(uri));
     
            result.sampleEnd();
            result.setSuccessful( true );
            result.setResponseMessage( "Successfully performed action" );
            result.setResponseCodeOK();
		} catch (Exception e) {
			e.printStackTrace();
			
            result.sampleEnd();
            result.setSuccessful( false );
            result.setResponseMessage( "Exception: " + e );
            result.setResponseCode("500");
		}
        
		return result;
	}

	private static byte[] getMessage(String sessionID, int userID) throws IOException {
		DecimalFormat df = new DecimalFormat("00000000");

		//Path path = Paths.get("/Users/nenko/Documents/workspace/WebSocketExample/bin/UPDATE_CANVAS_ADD_STROKE");
		Path path=Paths.get(FilePath);
		byte[] data = Files.readAllBytes(path);
		byte[] systemInfo = (sessionID + df.format(userID)).getBytes("UTF-8");
		
		for (int i = 0; i < systemInfo.length; i++) {
			int dataIDX = data.length - systemInfo.length + i;
			data[dataIDX] = systemInfo[i];
		}
		
		return data;
	}
	
	private static int GetRandomUserID()
	{
		Random rnd=new Random();
		return rnd.nextInt(10000)+rnd.nextInt(1000)+rnd.nextInt(100);	
	}
	
	
  	@OnOpen
    public void onOpen(Session session) {
    	byte[] message;
    	//sessionID=GetNewSessionID();
    	//sessionID="2b424f84-6cda-4fe9-a08b-70bb07172ae5";
    	
    	
    //	for (int i=0;i<25;i++)
    	//{
    	userID=GetRandomUserID();
		try {
			message = getMessage(sessionID, userID);
			 StartTime=System.currentTimeMillis();
			session.getBasicRemote().sendBinary(ByteBuffer.wrap(message));
			log.info("SessionID: "+sessionID+", UserID: "+Integer.toString(userID)+", Connected ... " + session.getId()+", SessionResponseTime: "+ExecutionTime);
	    	
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
    	//}

    }

    @OnMessage
    public void onMessage(ByteBuffer msg) {
    	log.info("Received ... " + msg.capacity());
    	EndTime=System.currentTimeMillis();
    	ExecutionTime=EndTime-StartTime;
    	log.info("Execution Time: " + ExecutionTime);
        new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,"Test");
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
    	log.info(String.format("Session %s close because of %s",  closeReason));
    }
}
