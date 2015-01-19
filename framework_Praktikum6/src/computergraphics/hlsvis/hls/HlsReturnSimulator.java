package computergraphics.hlsvis.hls;

import computergraphics.hlsvis.rabbitmq.RabbitMqCommunication;

public class HlsReturnSimulator {

	private RabbitMqCommunication transportEventQueue = new RabbitMqCommunication(
			HlsConstants.SENDUNGSEREIGNIS_QUEUE, "win-devel.informatik.haw-hamburg.de", "CGTeams", "Rwj9joAi");
	
	
	public void sendEvent(TransportEvent transportEvent){
		String jsonMessage = transportEvent.toJson();
//		System.out.println(jsonMessage);
		transportEventQueue.connect();
		transportEventQueue.sendMessage(jsonMessage);
		transportEventQueue.disconnect();
	}
}
